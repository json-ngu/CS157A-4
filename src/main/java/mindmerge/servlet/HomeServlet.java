package mindmerge.servlet;

import mindmerge.dao.DecisionDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final DecisionDao decisionDao = new DecisionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Logged-out visitors get the landing page, which shows no decisions.
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            try {
                request.setAttribute("decisions", decisionDao.findAll());
            } catch (SQLException e) {
                getServletContext().log("Could not load decisions", e);
                request.setAttribute("error", "Could not load decisions. Check that the database is running.");
            }
        }

        request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
    }
}
