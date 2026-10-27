package mindmerge.servlet;

import mindmerge.dao.UserDao;
import mindmerge.model.User;
import mindmerge.util.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** The signed-in user's own settings: for now, whether companies may invite them. */
@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = Auth.requireUser(request, response);
        if (user == null) return;

        Auth.consumeFlash(request);
        request.setAttribute("activeNav", "settings");
        try {
            request.setAttribute("viewer", userDao.findViewer(user.getUserId()));
        } catch (SQLException e) {
            getServletContext().log("Could not load settings", e);
            request.setAttribute("error", "Your settings could not be loaded. Verify that the database is running.");
        }
        request.getRequestDispatcher("/WEB-INF/settings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = Auth.requireUser(request, response);
        if (user == null) return;

        // An unchecked checkbox isn't sent at all, so "missing" means off.
        boolean allow = request.getParameter("allowInvites") != null;
        try {
            userDao.setAllowInvites(user.getUserId(), allow);
            Auth.flash(request, allow ? "Invitations are enabled. Companies can now invite you."
                                      : "Invitations are disabled. Any pending invitations have been cancelled.", false);
        } catch (SQLException e) {
            getServletContext().log("Could not save settings", e);
            Auth.flash(request, "The database is unavailable. Your setting was not saved.", true);
        }
        response.sendRedirect(request.getContextPath() + "/settings");
    }
}
