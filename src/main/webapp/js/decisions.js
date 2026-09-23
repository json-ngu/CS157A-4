(function () {
  "use strict";

  var list = document.getElementById("decisionList");
  if (!list) return;

  var base = window.location.pathname.replace(/\/home\/?$/, "");
  var POLL_MS = 5000;
  var lastPayload = null;
  var timer = null;

  function esc(value) {
    return String(value == null ? "" : value)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;");
  }

  function render(decisions) {
    if (!decisions.length) {
      list.innerHTML = '<li class="decision empty">No decisions recorded yet.</li>';
      return;
    }
    list.innerHTML = decisions.map(function (d) {
      return '<li class="decision">' +
        '<h3>' + esc(d.title) + '</h3>' +
        '<p class="choice">Chose <strong>' + esc(d.chosenOption) + '</strong></p>' +
        '<p class="why">' + esc(d.rationale) + '</p>' +
        '<p class="meta">' + esc(d.decidedBy) + ', ' + esc(d.decidedAt) + '</p>' +
        '</li>';
    }).join("");
  }

  function load() {
    return fetch(base + "/api/decisions", { headers: { "Accept": "application/json" } })
      .then(function (res) {
        if (!res.ok) throw new Error("request failed");
        return res.text();
      })
      .then(function (text) {
        if (text === lastPayload) return;
        lastPayload = text;

        var data = JSON.parse(text);
        if (data && data.error) throw new Error(data.error);
        render(data);
      })
      .catch(function () {
        if (lastPayload === null) {
          list.innerHTML = '<li class="decision empty">' +
            'The decision list could not be loaded. Check that the database is running.' +
            '</li>';
        }
        
      })
      .then(function () {
        list.setAttribute("aria-busy", "false");
      });
  }

  function start() {
    if (timer) return;
    timer = setInterval(load, POLL_MS);
  }

  function stop() {
    clearInterval(timer);
    timer = null;
  }

  document.addEventListener("visibilitychange", function () {
    if (document.hidden) {
      stop();
    } else {
      load();
      start();
    }
  });

  load();
  start();
})();