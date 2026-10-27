package mindmerge.util;

import mindmerge.dao.UserDao;
import mindmerge.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/** Session helpers shared by the signed-in pages. */
public final class Auth {

    private static final String FLASH = "flash";
    private static final String FLASH_ERROR = "flashError";
    private static final UserDao USERS = new UserDao();

    private Auth() {
    }

    public static User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute("user");
    }

    /**
     * Returns the signed-in user, or redirects to /login and returns null. The account is
     * re-checked on every request, so a user deactivated mid-session is signed out right away.
     */
    public static User requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser(request);
        if (user != null && !stillActive(request, user)) {
            request.getSession().invalidate();
            flash(request, "Your account is inactive. Contact your company administrator for access.", true);
            user = null;
        }
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            // Signed-in pages hold private data: never cache them, so Back after logout shows nothing.
            response.setHeader("Cache-Control", "no-store");
            // Pages show times in the viewer's own time zone, e.g. ${d.dateTime(userZone)}.
            request.setAttribute("userZone", Zones.viewerZone(request));
        }
        return user;
    }

    // If the database is down, keep the user signed in; the page itself reports the outage.
    private static boolean stillActive(HttpServletRequest request, User user) {
        try {
            return USERS.isActive(user.getUserId());
        } catch (SQLException e) {
            request.getServletContext().log("Could not re-check account status", e);
            return true;
        }
    }

    /** A one-time notice shown on the next page after a redirect (post/redirect/get). */
    public static void flash(HttpServletRequest request, String message, boolean error) {
        HttpSession session = request.getSession();
        session.setAttribute(FLASH, message);
        session.setAttribute(FLASH_ERROR, error);
    }

    /** Moves a pending notice into request attributes "notice" and "noticeError" for the page to show. */
    public static void consumeFlash(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(FLASH) == null) return;
        request.setAttribute("notice", session.getAttribute(FLASH));
        request.setAttribute("noticeError", session.getAttribute(FLASH_ERROR));
        session.removeAttribute(FLASH);
        session.removeAttribute(FLASH_ERROR);
    }

    /** A form value with surrounding spaces removed; "" when the field was not sent. */
    public static String trim(String input) {
        return input == null ? "" : input.trim();
    }

    /** Parses a positive id from a request parameter; -1 when missing or invalid. */
    public static int intParam(HttpServletRequest request, String name) {
        try {
            return Integer.parseInt(request.getParameter(name));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
