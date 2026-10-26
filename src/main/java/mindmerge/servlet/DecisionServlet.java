package mindmerge.servlet;

import mindmerge.dao.DashboardDao;
import mindmerge.dao.DecisionDao;
import mindmerge.dao.UserDao;
import mindmerge.model.Project;
import mindmerge.model.User;
import mindmerge.util.Auth;
import mindmerge.util.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



@WebServlet("/decisions/new")
public class DecisionServlet extends HttpServlet {

    private final DecisionDao decisionDao = new DecisionDao();
    private final UserDao userDao = new UserDao();
    private final DashboardDao dashboardDao = new DashboardDao();

    // displays or loads a page
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        User user = Auth.requireUser(request, response);
        if (user == null) return;


        request.setAttribute("activeNav", "new-decision");

        try {
            request.setAttribute("viewer", userDao.findViewer(user.getUserId()));
            List<Project> projects = dashboardDao.findProjects(user.getUserId());
            request.setAttribute("projects", projects);

        } catch (SQLException e) {
            getServletContext().log("Could not load decision page", e);
            request.setAttribute("error","The page could not be loaded.");
        }
        request.getRequestDispatcher("/WEB-INF/decision-new.jsp").forward(request, response);
    }

    // processes data submitted by user
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

        User user = Auth.requireUser(request, response);
        if (user == null) return;
        
        request.setAttribute("activeNav", "new-decision");

        String title = request.getParameter("title");
        String context = request.getParameter("context");
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        String[] optionLabels = request.getParameterValues("optionLabel");
        String[] optionDescriptions = request.getParameterValues("optionDescription");
        
        Connection connection = null;

        try {
            connection = DbUtil.getConnection();
            connection.setAutoCommit(false);

            int decisionId = decisionDao.create(connection, projectId, user.getUserId(), title, context);
            if (optionLabels == null || optionLabels.length < 2) {
                request.setAttribute("error","Please provide at least two options.");
                request.getRequestDispatcher("/WEB-INF/decision-new.jsp").forward(request, response);
                return;
            }

            for (int i = 0; i < optionLabels.length; i++) {
                String label = optionLabels[i].trim();
                String description = "";

                if (optionDescriptions != null && i < optionDescriptions.length && optionDescriptions[i] != null) {
                    description = optionDescriptions[i].trim();
                }

                if (label.isEmpty()) {
                    throw new SQLException("Option name cannot be empty.");
                }
                decisionDao.createOption(connection, decisionId, i + 1, label, description);
            }
            connection.commit();
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (SQLException e) {
            getServletContext().log("Could not create decision.", e);
            request.setAttribute("error", "The decision could not be created.");
            
            try {
                request.setAttribute("viewer", userDao.findViewer(user.getUserId()));
            } catch (SQLException viewerError) {
                getServletContext().log("Could not load reload viewer", e);
            }

             try {
                connection.rollback();
            } catch (SQLException rollbackError) {
                getServletContext().log("Rollback failed", rollbackError);
            }

            request.getRequestDispatcher("/WEB-INF/decision-new.jsp").forward(request, response);
        }
    }
}
