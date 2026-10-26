package mindmerge.dao;

import mindmerge.model.Decision;
import mindmerge.model.Project;
import mindmerge.model.Tag;
import mindmerge.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Queries for the dashboard. Everything is scoped to the teams the viewer currently belongs to. */
public class DashboardDao {

    // Shared by the review list and the feed. The first parameter is always the viewer's id.
    private static final String DECISIONS =
        "SELECT d.decision_id, p.name AS project_name, d.title, d.context, d.rationale, d.status, " +
        "       d.decided_at, d.created_at, u.full_name AS decided_by, o.label AS chosen_option, " +
        "       (SELECT GROUP_CONCAT(t.name ORDER BY t.name SEPARATOR ',') " +
        "          FROM decision_tag dt JOIN tag t ON t.tag_id = dt.tag_id " +
        "         WHERE dt.decision_id = d.decision_id) AS tags, " +
        "       (SELECT r.title FROM decision_link l JOIN decision r ON r.decision_id = l.decision_id " +
        "         WHERE l.related_decision_id = d.decision_id AND l.link_type = 'supersedes' " +
        "         LIMIT 1) AS replaced_by " +
        "FROM decision d " +
        "JOIN project p ON p.project_id = d.project_id " +
        "JOIN team_member tm ON tm.team_id = p.team_id AND tm.user_id = ? AND tm.left_at IS NULL " +
        "JOIN `user` u ON u.user_id = d.created_by " +
        "LEFT JOIN decision_option o ON o.decision_id = d.decision_id AND o.is_chosen = TRUE ";

    private static final String NEEDS_REVIEW =
        DECISIONS +
        "WHERE d.status = 'proposed' AND tm.team_role IN ('lead', 'approver') " +
        "ORDER BY d.created_at DESC";

    private static final String FEED_ORDER = "ORDER BY COALESCE(d.decided_at, d.created_at) DESC ";
    private static final int RECENT_LIMIT = 12;  // the plain Recent decisions list
    private static final int SEARCH_LIMIT = 200; // a search shows every match, up to this safety cap

    private static final String FIND_PROJECTS =
        "SELECT p.project_id, p.name, p.status, t.name AS team_name, " +
        "       COUNT(d.decision_id) AS decision_count, " +
        "       MAX(COALESCE(d.decided_at, d.created_at)) AS last_activity " +
        "FROM project p " +
        "JOIN team t ON t.team_id = p.team_id " +
        "JOIN team_member tm ON tm.team_id = p.team_id AND tm.user_id = ? AND tm.left_at IS NULL " +
        "LEFT JOIN decision d ON d.project_id = p.project_id " +
        "GROUP BY p.project_id, p.name, p.status, t.name " +
        "ORDER BY last_activity IS NULL, last_activity DESC";
    
    /* This function implements the tag for FindFeed*/
    private String normalizeTag(String tagName) {
        if (tagName == null) return null;

        String tag = tagName.trim();

        // The "All tags" option sends an empty string: no filter needed.
        return tag.isEmpty() ? null : tag;
    }

    /** Proposed decisions on teams the viewer leads or approves for. */
    public List<Decision> findNeedsReview(int userId) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(NEEDS_REVIEW)) {

            ps.setInt(1, userId);
            return readDecisions(ps);
        }
    }
    /** Tags will be directly added from the database and not hardcoded*/
    public List<Tag> findAllTags() throws SQLException {
        List<Tag> results = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM tag ORDER BY name")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(new Tag(rs.getString("name")));
                }
            }
        }
        return results;
    }


    /**
     * Recent decisions across the viewer's teams, newest first: the latest 12, or every match when searching.
     * mineOnly limits it to decisions the viewer recorded; query is a full-text search (may be empty).
     */
    /**
     * Builds the dashboard feed. A blank tagName means "All tags."
     */
    public List<Decision> findFeed(int userId, boolean mineOnly, String query, String tagName)
            throws SQLException {
        String search = toBooleanSearch(query);
        String tag = normalizeTag(tagName);

        // DECISIONS already restricts results to teams the user belongs to.
        StringBuilder sql = new StringBuilder(DECISIONS).append("WHERE 1 = 1 ");

        if (mineOnly) {
            sql.append("AND d.created_by = ? ");
        }

        if (search != null) {
            sql.append("AND MATCH (d.title, d.context, d.rationale) " +
                       "AGAINST (? IN BOOLEAN MODE) ");
        }

        if (tag != null) {
            // decision_tag connects decisions to tags.
            // EXISTS checks for a match without adding duplicate decision rows.
            sql.append("AND EXISTS (" +
                       "SELECT 1 FROM decision_tag filter_dt " +
                       "JOIN tag filter_t ON filter_t.tag_id = filter_dt.tag_id " +
                       "WHERE filter_dt.decision_id = d.decision_id " +
                       "AND filter_t.name = ?) ");
        }

        // ORDER BY and LIMIT must come after all WHERE conditions.
        // Show up to 200 results when a search or tag filter is active.
        sql.append(FEED_ORDER)
           .append("LIMIT ")
           .append(search == null && tag == null ? RECENT_LIMIT : SEARCH_LIMIT);

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Bind values in the same order as the ? placeholders in the SQL.
            int i = 1;
            ps.setInt(i++, userId);
            if (mineOnly) ps.setInt(i++, userId);
            if (search != null) ps.setString(i++, search);
            if (tag != null) ps.setString(i++, tag);

            return readDecisions(ps);
        }
    }

    public List<Project> findProjects(int userId) throws SQLException {
        List<Project> results = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_PROJECTS)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Project p = new Project();
                    p.setProjectId(rs.getInt("project_id"));
                    p.setName(rs.getString("name"));
                    p.setStatus(rs.getString("status"));
                    p.setTeamName(rs.getString("team_name"));
                    p.setDecisionCount(rs.getInt("decision_count"));
                    p.setLastActivity(toLocal(rs.getTimestamp("last_activity")));
                    results.add(p);
                }
            }
        }
        return results;
    }
    
   


    /**
     * Turns "mysql full text" into "+mysql* +full* +text*": every word must match, as a prefix.
     * Characters that mean something in boolean mode are dropped. Returns null when nothing is left.
     */
    private String toBooleanSearch(String query) {
        if (query == null) return null;
        StringBuilder out = new StringBuilder();
        for (String word : query.trim().split("\\s+")) {
            String clean = word.replaceAll("[^\\p{L}\\p{N}_]", "");
            if (!clean.isEmpty()) out.append('+').append(clean).append("* ");
        }
        return out.length() == 0 ? null : out.toString().trim();
    }

    private List<Decision> readDecisions(PreparedStatement ps) throws SQLException {
        List<Decision> results = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(mapDecision(rs));
            }
        }
        return results;
    }

    private Decision mapDecision(ResultSet rs) throws SQLException {
        Decision d = new Decision();
        d.setDecisionId(rs.getInt("decision_id"));
        d.setProjectName(rs.getString("project_name"));
        d.setTitle(rs.getString("title"));
        d.setContext(rs.getString("context"));
        d.setRationale(rs.getString("rationale"));
        d.setStatus(rs.getString("status"));
        d.setDecidedBy(rs.getString("decided_by"));
        d.setChosenOption(rs.getString("chosen_option"));
        d.setDecidedAt(toLocal(rs.getTimestamp("decided_at")));
        d.setCreatedAt(toLocal(rs.getTimestamp("created_at")));
        d.setReplacedBy(rs.getString("replaced_by"));

        String tags = rs.getString("tags");
        if (tags != null) {
            for (String name : tags.split(",")) {
                d.getTags().add(new Tag(name));
            }
        }
        return d;
    }

    private LocalDateTime toLocal(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
