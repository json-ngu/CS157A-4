package mindmerge.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mindmerge.dao.UserDao;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username = value(request.getParameter("username"));
        String displayName =
                value(request.getParameter("displayName"));

        String password = request.getParameter("password");
        String confirmPassword =
                request.getParameter("confirmPassword");

        if (username.trim().isEmpty()
                || displayName.trim().isEmpty()
                || password == null
                || password.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please complete every field.");

            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);

            return;
        }

        if (!username.matches("[A-Za-z0-9_]{3,50}")) {

            request.setAttribute(
                    "error",
                    "Username must be 3-50 letters, numbers, "
                    + "or underscores.");

            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);

            return;
        }

        if (!password.equals(confirmPassword)) {

            request.setAttribute(
                    "error",
                    "Passwords do not match.");

            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);

            return;
        }

        try {
            userDao.createUser(
                    username,
                    displayName,
                    password);

            response.sendRedirect(
                    request.getContextPath()
                    + "/login?registered=true");

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                request.setAttribute(
                        "error",
                        "That username is already taken.");
            } else {
                request.setAttribute(
                        "error",
                        "The database is unavailable.");
            }

            request.getRequestDispatcher("/register.jsp")
                    .forward(request, response);
        }
    }

    private String value(String input) {
        return input == null ? "" : input.trim();
    }
}
