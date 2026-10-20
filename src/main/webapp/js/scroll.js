// Landing scroll story. Adds .motion to <html> so the hidden "before" states in the CSS
// only apply when this script runs; without it (or with reduced motion) everything shows as is.
(function () {
  "use strict";

  var reduce = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
  if (reduce || !("IntersectionObserver" in window)) return;

  document.documentElement.classList.add("motion");

  // Each [data-reveal] block plays its entrance once, as soon as it starts coming on screen.
  var observer = new IntersectionObserver(function (entries) {
    entries.forEach(function (entry) {
      if (!entry.isIntersecting) return;
      entry.target.classList.add("is-in");
      observer.unobserve(entry.target);
    });
  }, { threshold: 0.1 });

  Array.prototype.forEach.call(document.querySelectorAll("[data-reveal]"), function (el) {
    observer.observe(el);
  });

  // As the hero scrolls away, --leave goes 0 to 1; the CSS drifts the headline and beam apart.
  var hero = document.querySelector(".hero");
  if (!hero) return;

  var pending = false;

  function update() {
    pending = false;
    var progress = Math.min(1, Math.max(0, window.pageYOffset / hero.offsetHeight));
    hero.style.setProperty("--leave", progress.toFixed(3));
  }

  window.addEventListener("scroll", function () {
    if (pending) return;
    pending = true;
    window.requestAnimationFrame(update);
  }, { passive: true });

  update();
})();
