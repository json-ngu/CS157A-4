package mindmerge.util;

import mindmerge.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/** Session helpers shared by the signed-in pages. */
public final class Auth {

    private static final String FLASH = "flash";
    private static final String FLASH_ERROR = "flashError";

    private Auth() {
    }

    public static User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute("user");
    }

    /** Returns the signed-in user, or redirects to /login and returns null. */
    public static User requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        }
        return user;
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

    /** Parses a positive id from a request parameter; -1 when missing or invalid. */
    public static int intParam(HttpServletRequest request, String name) {
        try {
            return Integer.parseInt(request.getParameter(name));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
