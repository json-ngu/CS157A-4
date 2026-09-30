package mindmerge.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mindmerge.dao.UserDao;
import mindmerge.model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username = value(request.getParameter("username"));
        String password = request.getParameter("password");

        if (password == null || password.isBlank()) {

            request.setAttribute(
                    "error",
                    "Please enter your password.");

            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);

            return;
        }

        try {
            User user = userDao.authenticate(username, password);

            if (user == null) {

                request.setAttribute(
                        "error",
                        "Invalid username or password.");

                request.getRequestDispatcher("/login.jsp")
                        .forward(request, response);

                return;
            }

            request.getSession(true)
                    .setAttribute("user", user);

            response.sendRedirect(
                    request.getContextPath() + "/home");

        } catch (SQLException e) {

            request.setAttribute(
                    "error",
                    "The database is unavailable.");

            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
        }
    }

    private String value(String input) {
        return input == null ? "" : input.trim();
    }
}