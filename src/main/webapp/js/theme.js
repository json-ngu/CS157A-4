(function () {
  "use strict";

  var root = document.documentElement;
  var button = document.getElementById("themeToggle");
  if (!button) return;

  var label = button.querySelector(".theme-label");

  function sync() {
    var dark = root.getAttribute("data-theme") !== "light";
    label.textContent = dark ? "Dark" : "Light";
    button.setAttribute("aria-pressed", dark ? "true" : "false");
    button.setAttribute("aria-label", dark ? "Dark mode on. Switch to light mode" : "Light mode on. Switch to dark mode");
  }

  button.addEventListener("click", function () {
    var next = root.getAttribute("data-theme") === "light" ? "dark" : "light";
    root.setAttribute("data-theme", next);
    try { localStorage.setItem("mm-theme", next); } catch (e) {}
    sync();
  });

  sync();
  button.hidden = false;
})();
