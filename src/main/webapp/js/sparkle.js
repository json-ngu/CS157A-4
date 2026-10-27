// Signed-in pages: tiny specks of light drifting up in the top right of the window, gathered
// around the purple beam, a quiet echo of the landing hero's dust. The canvas is fixed to the
// window behind all content (so it keeps going as the page scrolls) and ignores the pointer.
// It pauses when the tab is hidden and draws one still frame under prefers-reduced-motion.
(function () {
  "use strict";

  var main = document.querySelector(".app-main");
  if (!main) return;

  var canvas = document.createElement("canvas");
  if (!canvas.getContext) return;
  canvas.className = "app-sparkle";
  canvas.setAttribute("aria-hidden", "true");
  main.appendChild(canvas);

  var ctx = canvas.getContext("2d");
  var reduce = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  var GLINT_RATE = 1 / 8; // share of specks drawn as brighter glints with a soft halo
  var FADE_IN = 1.5;      // seconds a newly born speck takes to reach full brightness
  var w = 0, h = 0, beamX = 0.78;
  var specks = [];
  var time = 0, last = 0;
  var running = false;

  // Kept to the top right: about two-thirds gather around the beam, the rest stay in the right half.
  function speckX() {
    if (Math.random() < 0.65) {
      var spread = (Math.random() + Math.random() + Math.random() - 1.5) * 0.16; // roughly bell-shaped
      return (beamX + spread) * w;
    }
    return (0.5 + Math.random() * 0.5) * w;
  }

  // Specks are born in the upper part of the window (35-65% down), fade in, and drift up and out
  // the top, so the lower part stays clear. At start-up they are already spread over the top 65%.
  function spawn(s, anywhere) {
    s.x = speckX();
    s.y = (anywhere ? Math.random() * 0.65 : 0.35 + Math.random() * 0.3) * h;
    s.age = anywhere ? FADE_IN : 0;
    s.size = 0.6 + Math.random() * 1.0;
    s.speed = 6 + Math.random() * 8;          // px per second, upward
    s.sway = 2 + Math.random() * 5;           // px of sideways drift
    s.swayRate = 0.2 + Math.random() * 0.4;   // radians per second
    s.twinkle = 0.6 + Math.random() * 1.2;    // radians per second
    s.phase = Math.random() * Math.PI * 2;
    s.max = 0.25 + Math.random() * 0.45;
    s.glint = Math.random() < GLINT_RATE;
    return s;
  }

  function resize() {
    var dpr = Math.min(window.devicePixelRatio || 1, 2);
    var oldW = w, oldH = h;
    w = canvas.clientWidth;
    h = canvas.clientHeight;
    canvas.width = Math.round(w * dpr);
    canvas.height = Math.round(h * dpr);
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);

    var narrow = w < 700;
    beamX = narrow ? 0.93 : 0.78; // matches the beam's position in style.css
    var count = narrow ? 25 : 50;

    // A resize (including a phone's address bar showing or hiding) keeps the specks where they
    // are, scaled to the new size, so nothing jumps; only a change in count starts them afresh.
    if (specks.length === count && oldW && oldH) {
      for (var j = 0; j < specks.length; j++) {
        specks[j].x *= w / oldW;
        specks[j].y *= h / oldH;
      }
      return;
    }
    specks = [];
    for (var i = 0; i < count; i++) specks.push(spawn({}, true));
  }

  function draw() {
    ctx.clearRect(0, 0, w, h);
    for (var i = 0; i < specks.length; i++) {
      var s = specks[i];
      var x = s.x + Math.sin(time * s.swayRate + s.phase) * s.sway;
      // Fade in after birth, and out near the top edge, so specks never pop in or out of view.
      var edge = Math.min(1, s.y / 60) * Math.min(1, s.age / FADE_IN);
      var alpha = s.max * (0.45 + 0.55 * Math.sin(time * s.twinkle + s.phase)) * edge;
      if (alpha < 0.02) continue;

      if (s.glint) {
        ctx.fillStyle = "rgba(225, 212, 252," + (alpha * 0.18).toFixed(3) + ")";
        ctx.beginPath();
        ctx.arc(x, s.y, s.size * 4, 0, Math.PI * 2);
        ctx.fill();
        alpha = Math.min(1, alpha * 1.4);
      }
      ctx.fillStyle = "rgba(225, 212, 252," + alpha.toFixed(3) + ")";
      ctx.beginPath();
      ctx.arc(x, s.y, s.size, 0, Math.PI * 2);
      ctx.fill();
    }
  }

  function step(dt) {
    for (var i = 0; i < specks.length; i++) {
      var s = specks[i];
      s.y -= s.speed * dt;
      s.age += dt;
      if (s.y < -6) spawn(s, false);
    }
  }

  function frame(now) {
    if (!running) return;
    var dt = last ? Math.min((now - last) / 1000, 0.05) : 0;
    last = now;
    time += dt;
    step(dt);
    draw();
    window.requestAnimationFrame(frame);
  }

  function start() {
    if (running || reduce || document.hidden) return;
    running = true;
    last = 0;
    window.requestAnimationFrame(frame);
  }

  function stop() {
    running = false;
  }

  resize();
  if (reduce) {
    time = Math.random() * 20; // one still frame
    draw();
    return;
  }

  window.addEventListener("resize", function () {
    resize();
    if (!running) draw();
  });

  document.addEventListener("visibilitychange", function () {
    if (document.hidden) stop(); else start();
  });

  start();
})();
