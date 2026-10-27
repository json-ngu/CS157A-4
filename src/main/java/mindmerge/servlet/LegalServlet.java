package mindmerge.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// The public Privacy Policy (/privacy) and Terms of Service (/terms). No sign-in needed.
@WebServlet({"/privacy", "/terms"})
public class LegalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page = "/privacy".equals(request.getServletPath()) ? "privacy" : "terms";
        request.getRequestDispatcher("/WEB-INF/" + page + ".jsp").forward(request, response);
    }
}
