// Landing page: eased mouse-wheel scrolling through Lenis. Touch and keyboard stay native.
// Without Lenis (blocked CDN) or with reduced motion, the page simply scrolls natively.
(function () {
  "use strict";

  var reduce = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
  if (reduce || !window.Lenis) return;

  new window.Lenis({
    autoRaf: true,
    lerp: 0.1,
    anchors: true
  });
})();
