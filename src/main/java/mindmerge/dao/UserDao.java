package mindmerge.dao;

import mindmerge.model.User;
import mindmerge.util.DbUtil;
import mindmerge.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public void createUser(
            String username,
            String displayName,
            String password) throws SQLException {

        String sql =
                "INSERT INTO users "
                + "(username, display_name, password_hash, role) "
                + "VALUES (?, ?, ?, 'TEAM_MEMBER')";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, displayName);
            ps.setString(3, PasswordUtil.hash(password));

            ps.executeUpdate();
        }
    }

    public User authenticate(
            String username,
            String password) throws SQLException {

        String sql =
                "SELECT user_id, username, display_name, "
                + "password_hash, role "
                + "FROM users WHERE username = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()
                        && PasswordUtil.matches(
                                password,
                                rs.getString("password_hash"))) {

                    User user = new User();

                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setDisplayName(
                            rs.getString("display_name"));
                    user.setRole(rs.getString("role"));

                    return user;
                }
            }
        }

        return null;
    }
}