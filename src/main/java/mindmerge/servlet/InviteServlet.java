package mindmerge.servlet;

import mindmerge.dao.InviteDao;
import mindmerge.model.User;
import mindmerge.util.Auth;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** Accepting or declining your own invite from the dashboard banner. */
@WebServlet("/invites")
public class InviteServlet extends HttpServlet {

    private final InviteDao inviteDao = new InviteDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = Auth.requireUser(request, response);
        if (user == null) return;

        boolean accept = "accept".equals(request.getParameter("action"));
        String company = request.getParameter("companyName");
        try {
            if (inviteDao.respond(Auth.intParam(request, "invite"), user.getUserId(), accept)) {
                Auth.flash(request, accept ? "You have joined " + company + "." : "Invitation declined.", false);
            } else {
                Auth.flash(request, "This invitation is no longer available.", true);
            }
        } catch (SQLException e) {
            getServletContext().log("Invite response failed", e);
            Auth.flash(request, "The database is unavailable. No changes were made.", true);
        }
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
