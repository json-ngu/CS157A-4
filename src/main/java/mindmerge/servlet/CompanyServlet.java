package mindmerge.servlet;

import mindmerge.dao.CompanyDao;
import mindmerge.dao.UserDao;
import mindmerge.model.User;
import mindmerge.util.Auth;
import mindmerge.util.Limits;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** Any signed-in user can create a company and becomes its owner. */
@WebServlet("/companies/new")
public class CompanyServlet extends HttpServlet {

    private final CompanyDao companyDao = new CompanyDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = Auth.requireUser(request, response);
        if (user == null) return;
        showForm(request, response, user, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = Auth.requireUser(request, response);
        if (user == null) return;

        String name = Auth.trim(request.getParameter("name"));
        request.setAttribute("name", name);

        if (name.isEmpty() || name.length() > Limits.COMPANY_NAME) {
            showForm(request, response, user, "Company name must be 1 to " + Limits.COMPANY_NAME + " characters.");
            return;
        }

        try {
            int companyId = companyDao.create(name, user.getUserId());
            Auth.flash(request, name + " has been created. As its owner, you can now invite members and create teams.", false);
            response.sendRedirect(request.getContextPath() + "/people?company=" + companyId);
        } catch (SQLException e) {
            getServletContext().log("Could not create company", e);
            showForm(request, response, user, "The database is unavailable. The company was not created.");
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, User user, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        try {
            request.setAttribute("viewer", userDao.findViewer(user.getUserId()));
        } catch (SQLException e) {
            getServletContext().log("Could not load viewer", e);
        }
        request.getRequestDispatcher("/WEB-INF/company-new.jsp").forward(request, response);
    }
}
