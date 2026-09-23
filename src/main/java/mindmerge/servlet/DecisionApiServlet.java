package mindmerge.servlet;

import mindmerge.dao.DecisionDao;
import mindmerge.model.Decision;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/decisions")
public class DecisionApiServlet extends HttpServlet {

    private final DecisionDao decisionDao = new DecisionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            try {
                List<Decision> decisions = decisionDao.findAll();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < decisions.size(); i++) {
                    Decision d = decisions.get(i);
                    if (i > 0) json.append(',');
                    json.append('{')
                        .append("\"title\":\"").append(esc(d.getTitle())).append("\",")
                        .append("\"chosenOption\":\"").append(esc(d.getChosenOption())).append("\",")
                        .append("\"rationale\":\"").append(esc(d.getRationale())).append("\",")
                        .append("\"decidedBy\":\"").append(esc(d.getDecidedBy())).append("\",")
                        .append("\"decidedAt\":\"").append(d.getDecidedAt()).append('"')
                        .append('}');
                }
                out.print(json.append(']'));
            } catch (SQLException e) {
                getServletContext().log("Could not load decisions", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\":\"database unavailable\"}");
            }
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        StringBuilder b = new StringBuilder(s.length() + 16);
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  b.append("\\\""); break;
                case '\\': b.append("\\\\"); break;
                case '\n': b.append("\\n");  break;
                case '\r': b.append("\\r");  break;
                case '\t': b.append("\\t");  break;
                default:
                    if (c < 0x20) b.append(String.format("\\u%04x", (int) c));
                    else b.append(c);
            }
        }
        return b.toString();
    }
}