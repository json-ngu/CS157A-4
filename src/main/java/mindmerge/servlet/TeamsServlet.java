package mindmerge.servlet;

import mindmerge.dao.CompanyDao;
import mindmerge.dao.TeamDao;
import mindmerge.dao.UserDao;
import mindmerge.model.Company;
import mindmerge.model.User;
import mindmerge.util.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/** A company's teams and who is on them. Only that company's owners and admins can use it. */
@WebServlet("/teams")
public class TeamsServlet extends HttpServlet {

    private static final int MAX_TEAM_NAME = 150; // team.name

    private final CompanyDao companyDao = new CompanyDao();
    private final TeamDao teamDao = new TeamDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = Auth.requireUser(request, response);
        if (user == null) return;

        try {
            List<Company> companies = companyDao.findManageable(user.getUserId());
            Company company = PeopleServlet.pick(companies, Auth.intParam(request, "company"),
                    request.getParameter("company") != null);
            if (company == null) {
                Auth.flash(request, "You can only manage teams in companies where you're an owner or admin.", true);
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            Auth.consumeFlash(request);
            request.setAttribute("viewer", userDao.findViewer(user.getUserId()));
            request.setAttribute("activeNav", "teams");
            request.setAttribute("companies", companies);
            request.setAttribute("company", company);
            request.setAttribute("teams", teamDao.findTeams(company.getCompanyId()));
            request.setAttribute("members", companyDao.findMembers(company.getCompanyId()));
        } catch (SQLException e) {
            getServletContext().log("Could not load teams", e);
            request.setAttribute("error", "Could not load this page. Check that the database is running.");
        }
        request.getRequestDispatcher("/WEB-INF/teams.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = Auth.requireUser(request, response);
        if (user == null) return;

        int companyId = Auth.intParam(request, "company");
        String back = request.getContextPath() + "/teams?company=" + companyId;

        try {
            String myRole = companyDao.findRole(companyId, user.getUserId());
            if (!"owner".equals(myRole) && !"admin".equals(myRole)) {
                Auth.flash(request, "You can only manage teams in companies where you're an owner or admin.", true);
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            String action = String.valueOf(request.getParameter("action"));
            if ("create".equals(action)) {
                String name = request.getParameter("name") == null ? "" : request.getParameter("name").trim();
                if (name.isEmpty() || name.length() > MAX_TEAM_NAME) {
                    Auth.flash(request, "Team name must be 1 to " + MAX_TEAM_NAME + " characters.", true);
                } else {
                    teamDao.create(companyId, name);
                    Auth.flash(request, "Team “" + name + "” created.", false);
                }
                response.sendRedirect(back);
                return;
            }

            // Every other action is on one team, which must belong to this company.
            int teamId = Auth.intParam(request, "team");
            int target = Auth.intParam(request, "user");
            String role = request.getParameter("role");
            if (teamDao.findCompanyId(teamId) != companyId) {
                Auth.flash(request, "That team isn't in this company.", true);
            } else if ("add".equals(action)) {
                if (companyDao.findRole(companyId, target) == null) {
                    Auth.flash(request, "Only members of this company can join its teams.", true);
                } else if (!isTeamRole(role)) {
                    Auth.flash(request, "Pick a team role.", true);
                } else {
                    teamDao.addMember(teamId, target, role);
                    Auth.flash(request, "Added to the team.", false);
                }
            } else if ("role".equals(action)) {
                if (!isTeamRole(role)) {
                    Auth.flash(request, "Pick a team role.", true);
                } else {
                    teamDao.changeRole(teamId, target, role);
                    Auth.flash(request, "Team role updated.", false);
                }
            } else if ("remove".equals(action)) {
                teamDao.removeMember(teamId, target);
                Auth.flash(request, "Removed from the team. Their decisions and comments are kept.", false);
            } else {
                Auth.flash(request, "Unknown action.", true);
            }
        } catch (SQLException e) {
            getServletContext().log("Teams action failed", e);
            Auth.flash(request, "The database is unavailable. Nothing was changed.", true);
        }
        response.sendRedirect(back);
    }

    private boolean isTeamRole(String role) {
        return "lead".equals(role) || "approver".equals(role) || "member".equals(role);
    }
}
