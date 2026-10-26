const container = document.getElementById("options-container");
const addButton = document.getElementById("add-option");
const optionError = document.getElementById("option-error");

function updateOptionNumbers() {
    const options =container.querySelectorAll(".option-box");

    options.forEach(function (option, index) {
        option.querySelector("h3").textContent ="Option " + (index + 1);
    });
}

function createOptionBox() {
    const option = document.createElement("section");

    option.className = "option-box";

    option.innerHTML = `
        <h3>Option</h3>

        <label>Name:*</label>
        <input name="optionLabel"
                type="text"
                required>

        <label>Description:</label>
        <textarea name="optionDescription"></textarea>

        <button type="button"
                class="remove-option">
            Remove option
        </button>
    `;

    container.appendChild(option);
    updateOptionNumbers();
}

addButton.addEventListener("click", function () {
    createOptionBox();
    optionError.hidden = true;
});

function showOptionError(message) {
    optionError.textContent = message;
    optionError.hidden = false;

    optionError.scrollIntoView({
        behavior: "smooth",
        block: "center"
    });

    optionError.focus();
}

container.addEventListener("click", function (event) {
    if (!event.target.classList.contains("remove-option")) {
        return;
    }

    const options = container.querySelectorAll(".option-box");

    // Keep at least two options
    if (options.length <= 2) {
        showOptionError("A decision must have at least two options.");
        return;
    }

    event.target.closest(".option-box").remove();
    updateOptionNumbers();
});