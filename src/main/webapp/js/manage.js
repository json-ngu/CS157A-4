// Management pages (People, Teams, Settings): small conveniences on top of plain forms.
// Without JS every form still works with its own Save/Show button.
(function () {
  "use strict";

  // Selects and checkboxes marked data-autosubmit save as soon as they change,
  // so their fallback submit button can be hidden.
  Array.prototype.forEach.call(document.querySelectorAll("[data-autosubmit]"), function (field) {
    var form = field.form;
    if (!form) return;

    var button = form.querySelector("button[type=submit]");
    if (button) button.hidden = true;

    field.addEventListener("change", function () {
      form.submit();
    });
  });

  // Forms with data-confirm ask first (used for removing people).
  Array.prototype.forEach.call(document.querySelectorAll("form[data-confirm]"), function (form) {
    form.addEventListener("submit", function (event) {
      if (!window.confirm(form.getAttribute("data-confirm"))) {
        event.preventDefault();
      }
    });
  });
})();
