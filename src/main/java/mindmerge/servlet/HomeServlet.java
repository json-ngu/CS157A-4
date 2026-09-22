package mindmerge.servlet;

import mindmerge.dao.DecisionDao;
import mindmerge.model.Decision;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private final DecisionDao decisionDao = new DecisionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Decision> decisions = decisionDao.findAll();
            request.setAttribute("decisions", decisions);
            request.setAttribute("dbStatus", "connected");
        } catch (SQLException e) {
            getServletContext().log("Could not load decisions", e);
            request.setAttribute("dbStatus", "error: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
    }
}