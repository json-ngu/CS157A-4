// Login and register: show/hide password buttons, and the username rule hint.
(function () {
  "use strict";

  // Eye buttons stay hidden without JS, so the plain password field still works.
  Array.prototype.forEach.call(document.querySelectorAll(".reveal"), function (button) {
    var input = document.getElementById(button.getAttribute("aria-controls"));
    if (!input) return;

    button.hidden = false;
    button.addEventListener("click", function () {
      var show = input.type === "password";
      input.type = show ? "text" : "password";
      button.setAttribute("data-shown", show ? "true" : "false");
      button.setAttribute("aria-label", show ? "Hide password" : "Show password");
    });
  });

  // Same rule as RegisterServlet. The hint appears only when the username breaks it.
  var RULE = /^[A-Za-z0-9_]{3,50}$/;
  var hint = document.getElementById("usernameHint");
  var username = document.getElementById("username");
  if (!hint || !username) return;

  var touched = false;

  function check() {
    var value = username.value.trim();
    var invalid = value !== "" && !RULE.test(value);
    hint.hidden = !invalid;
    if (invalid) {
      username.setAttribute("aria-invalid", "true");
      username.setAttribute("aria-describedby", hint.id);
    } else {
      username.removeAttribute("aria-invalid");
      username.removeAttribute("aria-describedby");
    }
  }

  // Wait for the first blur so a single typed letter isn't flagged, then recheck while typing.
  username.addEventListener("blur", function () {
    touched = true;
    check();
  });
  username.addEventListener("input", function () {
    if (touched) check();
  });
})();
