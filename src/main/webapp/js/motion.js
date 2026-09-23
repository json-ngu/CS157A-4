(function () {
  "use strict";

  var reduce = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
  if (reduce) return;

  // Smooth wheel and trackpad scrolling. Anchor links scroll smoothly and clear the sticky header.
  if (window.Lenis) {
    new window.Lenis({
      autoRaf: true,
      lerp: 0.1,
      anchors: { offset: -76 }
    });
  }

  // Fade sections up as they come into view.
  if (!("IntersectionObserver" in window)) return;

  var targets = document.querySelectorAll(
    ".section-head, .cols, .compare, .steps li, .demo, .decision-list, .closing-inner, .legal > *"
  );

  var observer = new IntersectionObserver(function (entries) {
    entries.forEach(function (entry) {
      if (!entry.isIntersecting) return;
      entry.target.classList.add("is-visible");
      observer.unobserve(entry.target);
    });
  }, { rootMargin: "0px 0px -40px 0px", threshold: 0.08 });

  Array.prototype.forEach.call(targets, function (el) {
    var siblings = el.parentElement ? el.parentElement.children : [];
    var index = Array.prototype.indexOf.call(siblings, el);
    if (el.matches(".steps li")) el.style.transitionDelay = (index * 90) + "ms";
    el.classList.add("reveal");
    observer.observe(el);
  });
})();
