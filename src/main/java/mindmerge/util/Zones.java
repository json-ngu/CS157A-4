package mindmerge.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Showing stored times in the viewer's own time zone.
 *
 * MySQL saves DATETIME values (NOW(), CURRENT_TIMESTAMP) in its system time zone, which is the
 * same machine and zone as Tomcat here, so ZoneId.systemDefault() says what zone they are in.
 * The browser reports its zone in the "tz" cookie (js/timezone.js); without it, times stay in
 * the server's zone.
 */
public final class Zones {

    public static final String COOKIE = "tz";

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("MMM d, h:mm a", Locale.US);
    private static final DateTimeFormatter DATE_AT_TIME = DateTimeFormatter.ofPattern("MMM d 'at' h:mm a", Locale.US);

    private Zones() {
    }

    /** The viewer's zone id from the "tz" cookie, or the server's zone when missing or invalid. */
    public static String viewerZone(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (COOKIE.equals(c.getName())) {
                    try {
                        return ZoneId.of(URLDecoder.decode(c.getValue(), "UTF-8")).getId();
                    } catch (DateTimeException | IllegalArgumentException | UnsupportedEncodingException e) {
                        break; // a bad or tampered cookie just falls back to the server's zone
                    }
                }
            }
        }
        return ZoneId.systemDefault().getId();
    }

    /** "May 14, 2:30 PM" in the given zone. */
    public static String dateTime(LocalDateTime stored, String zoneId) {
        return format(stored, zoneId, DATE_TIME);
    }

    /** "May 14 at 2:30 PM" in the given zone, for sentences such as "Proposed on ...". */
    public static String dateAtTime(LocalDateTime stored, String zoneId) {
        return format(stored, zoneId, DATE_AT_TIME);
    }

    private static String format(LocalDateTime stored, String zoneId, DateTimeFormatter pattern) {
        if (stored == null) return "";
        ZoneId target;
        try {
            target = zoneId == null ? ZoneId.systemDefault() : ZoneId.of(zoneId);
        } catch (DateTimeException e) {
            target = ZoneId.systemDefault();
        }
        return stored.atZone(ZoneId.systemDefault()).withZoneSameInstant(target).format(pattern);
    }
}
