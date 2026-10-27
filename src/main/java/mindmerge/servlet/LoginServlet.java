package mindmerge.servlet;

import mindmerge.dao.UserDao;
import mindmerge.model.User;
import mindmerge.util.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        Auth.consumeFlash(request);
        showForm(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = Auth.trim(request.getParameter("email")).toLowerCase(Locale.ROOT);
        String password = request.getParameter("password");
        request.setAttribute("email", email);

        if (email.isEmpty() || password == null || password.isEmpty()) {
            showForm(request, response, "Enter your email address and password.");
            return;
        }

        try {
            User user = userDao.authenticate(email, password);
            if (user == null) {
                showForm(request, response, "Invalid email or password.");
                return;
            }
            if (!user.isActive()) {
                showForm(request, response, "This account is inactive. Contact your company administrator for access.");
                return;
            }

            // Start a fresh session on login so an earlier session id can't be reused.
            HttpSession old = request.getSession(false);
            if (old != null) {
                old.invalidate();
            }
            request.getSession(true).setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (SQLException e) {
            getServletContext().log("Login failed", e);
            showForm(request, response, "The database is unavailable. Please try again later.");
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
