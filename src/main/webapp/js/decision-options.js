// New decision page: add and remove option cards. A decision keeps at least two options,
// so Remove only shows when there are more. Without JS the form posts the options it shows.
(function () {
  "use strict";

  var MIN_OPTIONS = 2;
  var list = document.getElementById("optionList");
  var addButton = document.getElementById("addOption");
  if (!list || !addButton) return;

  function cards() {
    return list.querySelectorAll(".option-box");
  }

  // Number the cards 1..n and keep each label pointing at its own field.
  function renumber() {
    var all = cards();
    Array.prototype.forEach.call(all, function (card, index) {
      var n = index + 1;
      card.querySelector("h3").textContent = "Option " + n;
      var labels = card.querySelectorAll("label");
      var name = card.querySelector("input");
      var description = card.querySelector("textarea");
      name.id = "optionLabel" + n;
      description.id = "optionDescription" + n;
      labels[0].htmlFor = name.id;
      labels[1].htmlFor = description.id;
      card.querySelector(".remove-option").hidden = all.length <= MIN_OPTIONS;
    });
  }

  // A new card is a copy of the first one with its fields cleared.
  addButton.addEventListener("click", function () {
    var card = cards()[0].cloneNode(true);
    card.querySelector("input").value = "";
    card.querySelector("textarea").value = "";
    list.appendChild(card);
    renumber();
    card.querySelector("input").focus();
  });

  list.addEventListener("click", function (event) {
    var button = event.target.closest(".remove-option");
    if (!button || cards().length <= MIN_OPTIONS) return;
    button.closest(".option-box").remove();
    renumber();
    addButton.focus();
  });

  addButton.hidden = false;
  renumber();
})();
