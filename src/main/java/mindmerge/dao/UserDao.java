package mindmerge.dao;

import mindmerge.model.User;
import mindmerge.util.DbUtil;
import mindmerge.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private static final String INSERT =
        "INSERT INTO users (username, display_name, password_hash, role) " +
        "VALUES (?, ?, ?, 'TEAM_MEMBER')";

    private static final String FIND_BY_USERNAME =
        "SELECT user_id, username, display_name, password_hash, role " +
        "FROM users WHERE username = ?";

    public void createUser(String username, String displayName, String password) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {

            ps.setString(1, username);
            ps.setString(2, displayName);
            ps.setString(3, PasswordUtil.hash(password));
            ps.executeUpdate();
        }
    }

    /** Returns the matching user, or null if the username is unknown or the password is wrong. */
    public User authenticate(String username, String password) throws SQLException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USERNAME)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtil.matches(password, rs.getString("password_hash"))) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setDisplayName(rs.getString("display_name"));
        u.setRole(rs.getString("role"));
        return u;
    }
}
