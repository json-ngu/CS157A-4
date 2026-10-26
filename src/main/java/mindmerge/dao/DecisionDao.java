package mindmerge.dao;

import mindmerge.model.Decision;
import mindmerge.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class DecisionDao {

    // The chosen option comes from decision_option; proposed decisions have none, so LEFT JOIN.
    // Newest decisions first; proposed ones (no decided_at) sort by when they were recorded.
    private static final String FIND_ALL =
        "SELECT d.decision_id, p.name AS project_name, d.title, d.context, d.rationale, " +
        "       d.status, d.decided_at, u.full_name AS decided_by, o.label AS chosen_option " +
        "FROM decision d " +
        "JOIN project p ON p.project_id = d.project_id " +
        "JOIN `user` u ON u.user_id = d.created_by " +
        "LEFT JOIN decision_option o ON o.decision_id = d.decision_id AND o.is_chosen = TRUE " +
        "ORDER BY COALESCE(d.decided_at, d.created_at) DESC";

    private static final String CREATE_DECISION = 
        "INSERT INTO decision " + 
        "(project_id, created_by, title, context) " + 
        "VALUES (?, ?, ?, ?)";

    private static final String CREATE_DECISION_OPTION = 
        "INSERT INTO decision_option " +
        "(decision_id, option_num, label, description) " + 
        "VALUES (?, ?, ?, ?)";
    
    public List<Decision> findAll() throws SQLException {
        List<Decision> results = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }
        }
        return results;
    }

    public int create(Connection connection, int projectId, int userId, String title, String context) throws SQLException {
        try (
            PreparedStatement statement =
                connection.prepareStatement(CREATE_DECISION, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, projectId);
            statement.setInt(2, userId);
            statement.setString(3, title);
            statement.setString(4, context);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException("Could not get the new decision ID.");
    }

    public void createOption(Connection connection, int decisionId, int optionNum, String label, String description) throws SQLException {
        try (
            PreparedStatement statement =
                connection.prepareStatement(CREATE_DECISION_OPTION)
        ) {
            statement.setInt(1, decisionId);
            statement.setInt(2, optionNum);
            statement.setString(3, label);

            if (description == null || description.trim().isEmpty()) {
                statement.setNull(4, java.sql.Types.LONGVARCHAR);
            } 
            else {
                statement.setString(4, description);
            }

            statement.executeUpdate();
        }
    }

    private Decision mapRow(ResultSet rs) throws SQLException {
        Decision d = new Decision();
        d.setDecisionId(rs.getInt("decision_id"));
        d.setProjectName(rs.getString("project_name"));
        d.setTitle(rs.getString("title"));
        d.setContext(rs.getString("context"));
        d.setRationale(rs.getString("rationale"));
        d.setStatus(rs.getString("status"));
        d.setDecidedBy(rs.getString("decided_by"));
        d.setChosenOption(rs.getString("chosen_option"));
        Timestamp decidedAt = rs.getTimestamp("decided_at");
        d.setDecidedAt(decidedAt == null ? null : decidedAt.toLocalDateTime());
        return d;
    }
}
