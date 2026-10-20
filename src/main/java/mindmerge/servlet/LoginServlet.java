package mindmerge.servlet;

import mindmerge.dao.UserDao;
import mindmerge.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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
        String password = request.getParameter("password");

        if (password == null || password.trim().isEmpty()) {
            showForm(request, response, "Please enter your password.");
            return;
        }

        try {
            User user = userDao.authenticate(username, password);
            if (user == null) {
                showForm(request, response, "Invalid username or password.");
                return;
            }
            request.getSession(true).setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (SQLException e) {
            getServletContext().log("Login failed", e);
            showForm(request, response, "The database is unavailable.");
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private String trim(String input) {
        return input == null ? "" : input.trim();
    }
}
