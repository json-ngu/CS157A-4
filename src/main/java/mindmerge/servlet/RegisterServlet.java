package mindmerge.servlet;

import mindmerge.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final int DUPLICATE_KEY = 1062;

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showForm(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = trim(request.getParameter("username"));
        String displayName = trim(request.getParameter("displayName"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username.isEmpty() || displayName.isEmpty()
                || password == null || password.trim().isEmpty()) {
            showForm(request, response, "Please complete every field.");
            return;
        }
        if (!username.matches("[A-Za-z0-9_]{3,50}")) {
            showForm(request, response, "Username must be 3-50 letters, numbers, or underscores.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showForm(request, response, "Passwords do not match.");
            return;
        }

        try {
            userDao.createUser(username, displayName, password);
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY) {
                showForm(request, response, "That username is already taken.");
            } else {
                getServletContext().log("Registration failed", e);
                showForm(request, response, "The database is unavailable.");
            }
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    private String trim(String input) {
        return input == null ? "" : input.trim();
    }
}
