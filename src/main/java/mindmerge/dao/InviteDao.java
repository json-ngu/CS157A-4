package mindmerge.dao;

import mindmerge.model.Invite;
import mindmerge.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Company invites. Rows are never deleted: responding or cancelling only changes the status. */
public class InviteDao {

    // An invitable person: an active account with invites switched on.
    private static final String FIND_INVITABLE =
        "SELECT user_id FROM `user` WHERE email = ? AND status = 'active' AND allow_invites = TRUE";

    private static final String IS_MEMBER =
        "SELECT 1 FROM employee WHERE company_id = ? AND user_id = ? AND left_at IS NULL";

    private static final String HAS_PENDING =
        "SELECT 1 FROM company_invite WHERE company_id = ? AND user_id = ? AND status = 'pending'";

    private static final String INSERT =
        "INSERT INTO company_invite (company_id, user_id, company_role, invited_by) VALUES (?, ?, ?, ?)";

    private static final String SELECT_INVITES =
        "SELECT i.invite_id, i.company_id, c.name AS company_name, u.full_name AS user_name, " +
        "       u.email AS user_email, i.company_role, b.full_name AS invited_by_name, i.created_at " +
        "FROM company_invite i " +
        "JOIN company c ON c.company_id = i.company_id " +
        "JOIN `user` u ON u.user_id = i.user_id " +
        "JOIN `user` b ON b.user_id = i.invited_by ";

    private static final String PENDING_FOR_USER =
        SELECT_INVITES + "WHERE i.user_id = ? AND i.status = 'pending' ORDER BY i.created_at";

    private static final String PENDING_FOR_COMPANY =
        SELECT_INVITES + "WHERE i.company_id = ? AND i.status = 'pending' ORDER BY i.created_at DESC";

    private static final String LOCK_PENDING =
        "SELECT company_id, company_role FROM company_invite " +
        "WHERE invite_id = ? AND user_id = ? AND status = 'pending' FOR UPDATE";

    private static final String RESPOND =
        "UPDATE company_invite SET status = ?, responded_at = NOW() WHERE invite_id = ?";

    private static final String CANCEL =
        "UPDATE company_invite SET status = 'cancelled', responded_at = NOW() " +
        "WHERE invite_id = ? AND company_id = ? AND status = 'pending'";

    /**
     * Invites the account with this email. Returns false, without saying why, when there is no
     * invitable account, the person is already a member, or an invite is already pending.
     */
    public boolean invite(int companyId, String email, String role, int invitedBy) throws SQLException {
        try (Connection conn = DbUtil.getConnection()) {
            int userId;
            try (PreparedStatement ps = conn.prepareStatement(FIND_INVITABLE)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return false;
                    userId = rs.getInt(1);
                }
            }
            if (exists(conn, IS_MEMBER, companyId, userId) || exists(conn, HAS_PENDING, companyId, userId)) {
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
                ps.setInt(1, companyId);
                ps.setInt(2, userId);
                ps.setString(3, role);
                ps.setInt(4, invitedBy);
                ps.executeUpdate();
            }
            return true;
        }
    }

    public List<Invite> findPendingForUser(int userId) throws SQLException {
        return query(PENDING_FOR_USER, userId);
    }

    public List<Invite> findPendingForCompany(int companyId) throws SQLException {
        return query(PENDING_FOR_COMPANY, companyId);
    }

    /**
     * Accepts or declines the user's own pending invite. Accepting also opens (or reopens) their
     * membership with the invited role. Returns false if there is no such pending invite.
     */
    public boolean respond(int inviteId, int userId, boolean accept) throws SQLException {
        try (Connection conn = DbUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int companyId;
                String role;
                try (PreparedStatement ps = conn.prepareStatement(LOCK_PENDING)) {
                    ps.setInt(1, inviteId);
                    ps.setInt(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }
                        companyId = rs.getInt("company_id");
                        role = rs.getString("company_role");
                    }
                }
                if (accept) {
                    try (PreparedStatement ps = conn.prepareStatement(CompanyDao.OPEN_MEMBERSHIP)) {
                        ps.setInt(1, userId);
                        ps.setInt(2, companyId);
                        ps.setString(3, role);
                        ps.executeUpdate();
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(RESPOND)) {
                    ps.setString(1, accept ? "accepted" : "declined");
                    ps.setInt(2, inviteId);
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /** Cancels a pending invite that belongs to this company. */
    public void cancel(int inviteId, int companyId) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(CANCEL)) {

            ps.setInt(1, inviteId);
            ps.setInt(2, companyId);
            ps.executeUpdate();
        }
    }

    private boolean exists(Connection conn, String sql, int companyId, int userId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private List<Invite> query(String sql, int id) throws SQLException {
        List<Invite> results = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invite i = new Invite();
                    i.setInviteId(rs.getInt("invite_id"));
                    i.setCompanyId(rs.getInt("company_id"));
                    i.setCompanyName(rs.getString("company_name"));
                    i.setUserName(rs.getString("user_name"));
                    i.setUserEmail(rs.getString("user_email"));
                    i.setRole(rs.getString("company_role"));
                    i.setInvitedByName(rs.getString("invited_by_name"));
                    Timestamp created = rs.getTimestamp("created_at");
                    i.setCreatedAt(created == null ? null : created.toLocalDateTime());
                    results.add(i);
                }
            }
        }
        return results;
    }
}
