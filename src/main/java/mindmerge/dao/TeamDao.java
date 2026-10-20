package mindmerge.dao;

import mindmerge.model.Team;
import mindmerge.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/** Teams in a company. Removing someone sets team_member.left_at; rows are never deleted. */
public class TeamDao {

    private static final String FIND_TEAMS =
        "SELECT team_id, name FROM team WHERE company_id = ? ORDER BY name";

    private static final String FIND_TEAM_MEMBERS =
        "SELECT tm.team_id, u.user_id, u.full_name, u.email, tm.team_role AS role, tm.joined_at " +
        "FROM team_member tm " +
        "JOIN team t ON t.team_id = tm.team_id " +
        "JOIN `user` u ON u.user_id = tm.user_id " +
        "WHERE t.company_id = ? AND tm.left_at IS NULL " +
        "ORDER BY FIELD(tm.team_role, 'lead', 'approver', 'member'), u.full_name";

    private static final String INSERT_TEAM = "INSERT INTO team (company_id, name) VALUES (?, ?)";

    private static final String FIND_COMPANY = "SELECT company_id FROM team WHERE team_id = ?";

    // Adds someone, or reopens their earlier membership on this team.
    private static final String OPEN_MEMBERSHIP =
        "INSERT INTO team_member (user_id, team_id, team_role, joined_at) VALUES (?, ?, ?, NOW()) " +
        "ON DUPLICATE KEY UPDATE team_role = VALUES(team_role), joined_at = NOW(), left_at = NULL";

    private static final String CHANGE_ROLE =
        "UPDATE team_member SET team_role = ? WHERE team_id = ? AND user_id = ? AND left_at IS NULL";

    private static final String CLOSE_MEMBERSHIP =
        "UPDATE team_member SET left_at = NOW() WHERE team_id = ? AND user_id = ? AND left_at IS NULL";

    /** The company's teams, each with its current members. */
    public List<Team> findTeams(int companyId) throws SQLException {
        Map<Integer, Team> teams = new LinkedHashMap<>();

        try (Connection conn = DbUtil.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(FIND_TEAMS)) {
                ps.setInt(1, companyId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Team t = new Team();
                        t.setTeamId(rs.getInt("team_id"));
                        t.setName(rs.getString("name"));
                        teams.put(t.getTeamId(), t);
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(FIND_TEAM_MEMBERS)) {
                ps.setInt(1, companyId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Team t = teams.get(rs.getInt("team_id"));
                        if (t != null) t.getMembers().add(CompanyDao.mapMember(rs));
                    }
                }
            }
        }
        return new ArrayList<>(teams.values());
    }

    public void create(int companyId, String name) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_TEAM)) {

            ps.setInt(1, companyId);
            ps.setString(2, name);
            ps.executeUpdate();
        }
    }

    /** The company a team belongs to, or -1 when the team doesn't exist. */
    public int findCompanyId(int teamId) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_COMPANY)) {

            ps.setInt(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public void addMember(int teamId, int userId, String role) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(OPEN_MEMBERSHIP)) {

            ps.setInt(1, userId);
            ps.setInt(2, teamId);
            ps.setString(3, role);
            ps.executeUpdate();
        }
    }

    public void changeRole(int teamId, int userId, String role) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHANGE_ROLE)) {

            ps.setString(1, role);
            ps.setInt(2, teamId);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }

    public void removeMember(int teamId, int userId) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(CLOSE_MEMBERSHIP)) {

            ps.setInt(1, teamId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}
