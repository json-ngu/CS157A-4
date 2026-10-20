// Login and register: show/hide password buttons, and the password length hint.
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

  // Same minimum as RegisterServlet. The hint appears only when the password is too short.
  var MIN_LENGTH = 8;
  var hint = document.getElementById("passwordHint");
  var password = document.getElementById("password");
  if (!hint || !password) return;

  var touched = false;

  function check() {
    var value = password.value;
    var invalid = value !== "" && value.length < MIN_LENGTH;
    hint.hidden = !invalid;
    if (invalid) {
      password.setAttribute("aria-invalid", "true");
      password.setAttribute("aria-describedby", hint.id);
    } else {
      password.removeAttribute("aria-invalid");
      password.removeAttribute("aria-describedby");
    }
  }

  // Wait for the first blur so a half-typed password isn't flagged, then recheck while typing.
  password.addEventListener("blur", function () {
    touched = true;
    check();
  });
  password.addEventListener("input", function () {
    if (touched) check();
  });
})();
