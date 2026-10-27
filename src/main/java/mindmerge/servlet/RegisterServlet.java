package mindmerge.servlet;

import mindmerge.dao.UserDao;
import mindmerge.util.Auth;
import mindmerge.util.Limits;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

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

        String fullName = Auth.trim(request.getParameter("fullName"));
        String email = Auth.trim(request.getParameter("email")).toLowerCase(Locale.ROOT);
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Refill the form after an error (never the passwords).
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);

        if (fullName.isEmpty() || email.isEmpty() || password == null || password.isEmpty()) {
            showForm(request, response, "Complete all required fields.");
            return;
        }
        if (fullName.length() > Limits.FULL_NAME) {
            showForm(request, response, "Full name must be " + Limits.FULL_NAME + " characters or fewer.");
            return;
        }
        if (email.length() > Limits.EMAIL || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            showForm(request, response, "Enter a valid email address.");
            return;
        }
        if (password.length() < Limits.MIN_PASSWORD || password.length() > Limits.MAX_PASSWORD) {
            showForm(request, response, "Password must be " + Limits.MIN_PASSWORD + " to " + Limits.MAX_PASSWORD + " characters.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showForm(request, response, "Passwords do not match.");
            return;
        }

        try {
            userDao.createUser(fullName, email, password);
            // Sign out anyone already signed in here, so the new account can log in.
            HttpSession old = request.getSession(false);
            if (old != null) {
                old.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY) {
                showForm(request, response, "An account with that email already exists.");
            } else {
                getServletContext().log("Registration failed", e);
                showForm(request, response, "The database is unavailable. Please try again later.");
            }
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
