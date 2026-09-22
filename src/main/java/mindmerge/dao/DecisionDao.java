package mindmerge.dao;

import mindmerge.model.Decision;
import mindmerge.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DecisionDao {

    private static final String FIND_ALL =
        "SELECT decision_id, title, chosen_option, rationale, decided_by, decided_at " +
        "FROM decision ORDER BY decided_at DESC";

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

    private Decision mapRow(ResultSet rs) throws SQLException {
        Decision d = new Decision();
        d.setDecisionId(rs.getInt("decision_id"));
        d.setTitle(rs.getString("title"));
        d.setChosenOption(rs.getString("chosen_option"));
        d.setRationale(rs.getString("rationale"));
        d.setDecidedBy(rs.getString("decided_by"));
        d.setDecidedAt(rs.getObject("decided_at", LocalDate.class));
        return d;
    }
}