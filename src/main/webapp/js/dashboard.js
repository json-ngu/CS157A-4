// Dashboard: search as you type. The server still renders the results; this fetches
// /dashboard?q=... in the background and swaps in just the #feed section.
// Without JS the search form submits normally on Enter.
(function () {
  "use strict";

  var form = document.querySelector(".dash-search");
  var input = document.getElementById("q");
  var feed = document.getElementById("feed");
  var status = document.getElementById("searchStatus");
  if (!form || !input || !feed || !window.fetch || !window.DOMParser || !window.URLSearchParams) return;

  var DELAY = 250;       // ms of quiet typing before searching
  var timer = null;
  var controller = null; // aborts the previous request when a newer one starts
  var last = input.value.trim();

  // Same parameters the form would send (q, plus view=mine when that tab is on).
  function urlFor(query) {
    var params = new URLSearchParams(new FormData(form));
    if (query) params.set("q", query); else params.delete("q");
    var qs = params.toString();
    return form.getAttribute("action") + (qs ? "?" + qs : "");
  }

  function announce(query) {
    if (!status) return;
    if (!query) {
      status.textContent = "";
      return;
    }
    var count = feed.querySelectorAll(".feed-item").length;
    status.textContent = count === 0 ? "No results" : count + (count === 1 ? " result" : " results");
  }

  function search(query) {
    if (controller) controller.abort();
    controller = window.AbortController ? new AbortController() : null;

    var url = urlFor(query);
    feed.setAttribute("aria-busy", "true");

    fetch(url, {
      credentials: "same-origin",
      headers: { "Accept": "text/html" },
      signal: controller ? controller.signal : undefined
    })
      .then(function (res) {
        // The session expired: the server sent us to the login page.
        if (res.redirected && /\/login(\?|$)/.test(res.url)) {
          window.location.href = res.url;
          throw new Error("signed out");
        }
        if (!res.ok) throw new Error("HTTP " + res.status);
        return res.text();
      })
      .then(function (html) {
        // Move the parsed nodes across (no HTML strings; every value was escaped by the JSP).
        var fresh = new DOMParser().parseFromString(html, "text/html").getElementById("feed");
        if (!fresh) return;
        while (feed.firstChild) feed.removeChild(feed.firstChild);
        while (fresh.firstChild) feed.appendChild(document.adoptNode(fresh.firstChild));
        history.replaceState(null, "", url);
        announce(query);
      })
      .catch(function () {
        // Aborted by a newer search, or the request failed: keep the current results.
      })
      .then(function () {
        feed.removeAttribute("aria-busy");
      });
  }

  input.addEventListener("input", function () {
    clearTimeout(timer);
    timer = setTimeout(function () {
      var query = input.value.trim();
      if (query === last) return;
      last = query;
      search(query);
    }, DELAY);
  });

  // Enter searches right away instead of reloading the page.
  form.addEventListener("submit", function (event) {
    event.preventDefault();
    clearTimeout(timer);
    last = input.value.trim();
    search(last);
  });
})();
