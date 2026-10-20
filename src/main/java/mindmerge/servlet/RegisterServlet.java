package mindmerge.servlet;

import mindmerge.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final int DUPLICATE_KEY = 1062;
    private static final int MAX_NAME = 120;     // user.full_name
    private static final int MAX_EMAIL = 180;    // user.email
    private static final int MIN_PASSWORD = 8;
    private static final int MAX_PASSWORD = 128; // bounds the hashing work per request

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showForm(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = trim(request.getParameter("fullName"));
        String email = trim(request.getParameter("email")).toLowerCase(Locale.ROOT);
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Refill the form after an error (never the passwords).
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);

        if (fullName.isEmpty() || email.isEmpty() || password == null || password.isEmpty()) {
            showForm(request, response, "Please complete every field.");
            return;
        }
        if (fullName.length() > MAX_NAME) {
            showForm(request, response, "Full name must be " + MAX_NAME + " characters or fewer.");
            return;
        }
        if (email.length() > MAX_EMAIL || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            showForm(request, response, "Enter a valid email address.");
            return;
        }
        if (password.length() < MIN_PASSWORD || password.length() > MAX_PASSWORD) {
            showForm(request, response, "Password must be " + MIN_PASSWORD + " to " + MAX_PASSWORD + " characters.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showForm(request, response, "Passwords do not match.");
            return;
        }

        try {
            userDao.createUser(fullName, email, password);
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY) {
                showForm(request, response, "An account with that email already exists.");
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
