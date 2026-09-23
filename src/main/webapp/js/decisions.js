(function () {
  "use strict";

  var list = document.getElementById("decisionList");
  if (!list) return;

  var base = window.location.pathname.replace(/\/home\/?$/, "");

  function esc(value) {
    return String(value == null ? "" : value)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;");
  }

  var tones = ["av-a", "av-b", "av-c"];

  function initials(name) {
    var parts = String(name || "").trim().split(/\s+/).filter(Boolean);
    if (!parts.length) return "?";
    var first = parts[0].charAt(0);
    var last = parts.length > 1 ? parts[parts.length - 1].charAt(0) : "";
    return (first + last).toUpperCase();
  }

  function render(decisions) {
    if (!decisions.length) {
      list.innerHTML = '<li class="decision empty">No decisions recorded yet.</li>';
      return;
    }
    list.innerHTML = decisions.map(function (d, i) {
      return '<li class="decision">' +
        '<h3>' + esc(d.title) + '</h3>' +
        '<p class="choice">Chose <strong>' + esc(d.chosenOption) + '</strong></p>' +
        '<p class="why">' + esc(d.rationale) + '</p>' +
        '<p class="meta">' +
          '<span class="av ' + tones[i % tones.length] + '" aria-hidden="true">' + esc(initials(d.decidedBy)) + '</span>' +
          '<span>' + esc(d.decidedBy) + ', ' + esc(d.decidedAt) + '</span>' +
        '</p>' +
        '</li>';
    }).join("");
  }

  function fail(message) {
    list.innerHTML = '<li class="decision empty">' + esc(message) + '</li>';
  }

  fetch(base + "/api/decisions", { headers: { "Accept": "application/json" } })
    .then(function (res) {
      if (!res.ok) throw new Error("request failed");
      return res.json();
    })
    .then(function (data) {
      if (data && data.error) throw new Error(data.error);
      render(data);
    })
    .catch(function () {
      fail("The decision list could not be loaded. Check that the database is running.");
    })
    .then(function () {
      list.setAttribute("aria-busy", "false");
    });
})();