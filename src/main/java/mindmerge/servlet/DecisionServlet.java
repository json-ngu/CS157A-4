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
import java.util.ArrayList;
import java.util.List;

/** Records a new decision, with its options, in one of the viewer's projects. */
@WebServlet("/decisions/new")
public class DecisionServlet extends HttpServlet {

    private static final int MAX_TITLE = 220;  // decision.title
    private static final int MAX_LABEL = 220;  // decision_option.label
    private static final int MIN_OPTIONS = 2;

    private final DecisionDao decisionDao = new DecisionDao();
    private final UserDao userDao = new UserDao();
    private final DashboardDao dashboardDao = new DashboardDao();

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

        int projectId = Auth.intParam(request, "projectId");
        String title = trim(request.getParameter("title"));
        String context = trim(request.getParameter("context"));
        List<DraftOption> options = readOptions(request);

        // Refill the form after an error.
        request.setAttribute("projectId", projectId);
        request.setAttribute("title", title);
        request.setAttribute("context", context);
        request.setAttribute("options", options);

        String error = validate(title, context, options);
        if (error != null) {
            showForm(request, response, user, error);
            return;
        }

        try {
            // Only projects on the viewer's own teams; the dropdown hiding the rest isn't enough.
            if (!isOwnProject(dashboardDao.findProjects(user.getUserId()), projectId)) {
                showForm(request, response, user, "Choose one of your projects.");
                return;
            }

            try (Connection conn = DbUtil.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    int decisionId = decisionDao.create(conn, projectId, user.getUserId(), title, context);
                    for (int i = 0; i < options.size(); i++) {
                        DraftOption o = options.get(i);
                        decisionDao.createOption(conn, decisionId, i + 1, o.getLabel(), o.getDescription());
                    }
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            }
        } catch (SQLException e) {
            getServletContext().log("Could not create decision", e);
            showForm(request, response, user, "The database is unavailable. Your decision wasn't saved.");
            return;
        }

        Auth.flash(request, "Decision recorded. It's waiting for review.", false);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    /** Returns the first problem with the submitted form, or null when it's valid. */
    private String validate(String title, String context, List<DraftOption> options) {
        if (title.isEmpty() || context.isEmpty()) {
            return "Give the decision a title and some context.";
        }
        if (title.length() > MAX_TITLE) {
            return "Title must be " + MAX_TITLE + " characters or fewer.";
        }
        if (options.size() < MIN_OPTIONS) {
            return "Add at least " + MIN_OPTIONS + " options.";
        }
        for (DraftOption o : options) {
            if (o.getLabel().isEmpty()) {
                return "Every option needs a name.";
            }
            if (o.getLabel().length() > MAX_LABEL) {
                return "Option names must be " + MAX_LABEL + " characters or fewer.";
            }
        }
        return null;
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, User user, String error)
            throws ServletException, IOException {

        request.setAttribute("activeNav", "new-decision");
        request.setAttribute("error", error);
        if (request.getAttribute("options") == null) {
            List<DraftOption> blank = new ArrayList<>();
            for (int i = 0; i < MIN_OPTIONS; i++) blank.add(new DraftOption("", ""));
            request.setAttribute("options", blank);
        }
        try {
            request.setAttribute("viewer", userDao.findViewer(user.getUserId()));
            request.setAttribute("projects", dashboardDao.findProjects(user.getUserId()));
        } catch (SQLException e) {
            getServletContext().log("Could not load the new decision page", e);
            request.setAttribute("error", "Could not load this page. Check that the database is running.");
        }
        request.getRequestDispatcher("/WEB-INF/decision-new.jsp").forward(request, response);
    }

    /** Pairs each option name with its description, in the order they were sent. */
    private List<DraftOption> readOptions(HttpServletRequest request) {
        String[] labels = request.getParameterValues("optionLabel");
        String[] descriptions = request.getParameterValues("optionDescription");
        List<DraftOption> options = new ArrayList<>();
        if (labels == null) return options;

        for (int i = 0; i < labels.length; i++) {
            String description = descriptions != null && i < descriptions.length ? descriptions[i] : null;
            options.add(new DraftOption(trim(labels[i]), trim(description)));
        }
        return options;
    }

    private boolean isOwnProject(List<Project> projects, int projectId) {
        for (Project p : projects) {
            if (p.getProjectId() == projectId) return true;
        }
        return false;
    }

    private String trim(String input) {
        return input == null ? "" : input.trim();
    }

    /** An option as typed into the form; used to refill it after an error. */
    public static class DraftOption {
        private final String label;
        private final String description;

        DraftOption(String label, String description) {
            this.label = label;
            this.description = description;
        }

        public String getLabel()       { return label; }
        public String getDescription() { return description; }
    }
}
