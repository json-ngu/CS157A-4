// Tells the server the viewer's time zone (e.g. "America/Los_Angeles") in the "tz" cookie,
// so decision times are shown in local time (see mindmerge.util.Zones). Loaded on every page,
// including login, so the zone is known before the first signed-in page.
(function () {
  "use strict";

  var zone;
  try {
    zone = Intl.DateTimeFormat().resolvedOptions().timeZone;
  } catch (e) {
    return;
  }
  if (!zone) return;

  var current = document.cookie.match(/(?:^|; )tz=([^;]*)/);
  if (current && decodeURIComponent(current[1]) === zone) return;

  document.cookie = "tz=" + encodeURIComponent(zone) + "; path=/; max-age=31536000; SameSite=Lax";
})();
