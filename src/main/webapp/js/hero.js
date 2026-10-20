// Landing hero: dotted option streams converge on one point, and only the chosen
// stream rises from it as the beam. A layer of drifting dust fills the rest of the hero.
// The beam glow itself is CSS (.hero-beam).
(function () {
  "use strict";

  var canvas = document.querySelector(".hero-canvas");
  if (!canvas || !canvas.getContext) return;

  var ctx = canvas.getContext("2d");
  var reduce = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  var WINNER = 2;          // index of the stream that becomes the beam
  var SPARK_RATE = 1 / 12; // share of stream dots drawn as brighter sparks

  var w = 0, h = 0, merge = null, narrow = false;
  var streams = [];
  var particles = [];
  var dust = [];
  var scatter = [];
  var time = 0;
  var running = false, visible = true, last = 0;

  // Start and control points as fractions of the hero size; every stream ends at the merge point.
  var SHAPES = [
    [[-0.05, 0.22], [0.30, 0.18], [0.52, 0.30]],
    [[-0.05, 0.62], [0.28, 0.70], [0.50, 0.42]],
    [[0.10, 1.05], [0.42, 0.95], [0.60, 0.70]],
    [[0.45, 1.08], [0.62, 0.90], [0.66, 0.72]],
    [[1.05, 0.95], [0.90, 0.80], [0.76, 0.62]],
    [[-0.05, 0.42], [0.25, 0.48], [0.45, 0.55]],
    [[0.25, 1.08], [0.40, 0.80], [0.58, 0.64]],
    [[1.05, 0.30], [0.92, 0.38], [0.80, 0.50]],
    [[0.85, 1.08], [0.80, 0.85], [0.72, 0.68]]
  ];

  function bezier(s, t) {
    var u = 1 - t, a = u * u * u, b = 3 * u * u * t, c = 3 * u * t * t, d = t * t * t;
    return {
      x: a * s[0].x + b * s[1].x + c * s[2].x + d * s[3].x,
      y: a * s[0].y + b * s[1].y + c * s[2].y + d * s[3].y
    };
  }

  function tangent(s, t) {
    var u = 1 - t;
    var x = 3 * u * u * (s[1].x - s[0].x) + 6 * u * t * (s[2].x - s[1].x) + 3 * t * t * (s[3].x - s[2].x);
    var y = 3 * u * u * (s[1].y - s[0].y) + 6 * u * t * (s[2].y - s[1].y) + 3 * t * t * (s[3].y - s[2].y);
    var len = Math.sqrt(x * x + y * y) || 1;
    return { x: x / len, y: y / len };
  }

  function smooth(a, b, x) {
    var t = Math.min(1, Math.max(0, (x - a) / (b - a)));
    return t * t * (3 - 2 * t);
  }

  // How visible a dot may be at x: quiet behind the headline, full strength near the beam.
  function reach(x) {
    return smooth(0.12 * w, 0.25 * w, x) * (0.25 + 0.75 * smooth(0.45 * w, 0.62 * w, x));
  }

  function resize() {
    var rect = canvas.getBoundingClientRect();
    var dpr = Math.min(window.devicePixelRatio || 1, 2);
    w = rect.width;
    h = rect.height;
    canvas.width = Math.round(w * dpr);
    canvas.height = Math.round(h * dpr);
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);

    // Keep the merge point centered on the CSS beam (left + width / 2).
    narrow = w < 820;
    merge = { x: w * (narrow ? 0.79 : 0.685), y: h * 0.56 };
    streams = SHAPES.map(function (shape) {
      return [
        { x: shape[0][0] * w, y: shape[0][1] * h },
        { x: shape[1][0] * w, y: shape[1][1] * h },
        { x: shape[2][0] * w, y: shape[2][1] * h },
        merge
      ];
    });
  }

  // Dust gathers around the beam: most of it near the beam, the rest anywhere.
  function dustX() {
    if (Math.random() < 0.4) return Math.random() * w;
    var g = (Math.random() + Math.random() + Math.random() - 1.5) / 1.5;
    return merge.x + g * 0.35 * w;
  }

  // Scatter: faint specks that start beside the beam and drift outward, like light scattering off it.
  function spawnScatter(sp, anywhere) {
    sp.x0 = merge.x + (Math.random() - 0.5) * 0.04 * w;
    if (Math.random() < 0.3) {
      sp.y = merge.y + (Math.random() + Math.random() - 1) * 0.15 * h;
    } else {
      sp.y = Math.random() * h;
    }
    sp.dir = Math.random() < 0.5 ? -1 : 1;
    sp.speed = 2 + Math.random() * 4;            // px per second, outward
    sp.lift = 0.5 + Math.random() * 1.5;         // px per second, upward
    sp.life = (0.08 + Math.random() * 0.12) * w; // how far it travels before fading out
    sp.dist = anywhere ? Math.random() * sp.life : 0;
    sp.max = 0.1 + Math.random() * 0.25;
    sp.size = 1 + Math.random() * 0.6;
    return sp;
  }

  function seed() {
    var scale = narrow ? 0.6 : 1;
    var perStream = Math.round(150 * scale);
    var risers = Math.round(90 * scale);
    var dustCount = Math.round(260 * scale);
    var scatterCount = Math.round(180 * scale);

    particles = [];
    for (var s = 0; s < streams.length; s++) {
      for (var i = 0; i < perStream; i++) {
        particles.push({
          s: s,
          t: Math.random(),
          speed: 0.02 + Math.random() * 0.025,
          spread: (Math.random() - 0.5) * 2,
          size: Math.random() < 0.2 ? 2.4 : 1.6,
          spark: Math.random() < SPARK_RATE
        });
      }
    }
    for (var r = 0; r < risers; r++) {
      particles.push({ s: -1, t: Math.random(), speed: 0.04 + Math.random() * 0.04, spread: (Math.random() - 0.5) * 2, size: 1.6, spark: false });
    }

    dust = [];
    for (var d = 0; d < dustCount; d++) {
      dust.push({
        x: dustX(),
        y: Math.random() * h,
        rise: 1.5 + Math.random() * 3,     // px per second
        size: 0.8 + Math.random() * 0.6,
        phase: Math.random() * Math.PI * 2,
        twinkle: 0.25 + Math.random() * 0.55 // radians per second
      });
    }

    scatter = [];
    for (var k = 0; k < scatterCount; k++) {
      scatter.push(spawnScatter({}, true));
    }
  }

  function drawScatter() {
    for (var i = 0; i < scatter.length; i++) {
      var sp = scatter[i];
      var x = sp.x0 + sp.dir * sp.dist;
      var progress = sp.dist / sp.life;
      // Fade in just off the beam, fade out as it drifts away, and never reach the headline.
      var alpha = sp.max * Math.min(1, progress / 0.1) * (1 - progress) * smooth(0.4 * w, 0.55 * w, x);
      if (alpha < 0.01) continue;
      ctx.fillStyle = "rgba(225, 212, 252," + alpha.toFixed(3) + ")";
      ctx.fillRect(x, sp.y, sp.size, sp.size);
    }
  }

  function drawDust() {
    for (var i = 0; i < dust.length; i++) {
      var d = dust[i];
      var x = d.x + Math.sin(time * 0.3 + d.phase) * 6;
      var near = Math.max(0, 1 - Math.abs(x - merge.x) / (0.45 * w));
      var max = 0.15 + 0.55 * Math.pow(near, 1.5);
      if (x < 0.5 * w) max = Math.min(max, 0.15);
      var alpha = max * (0.5 + 0.5 * Math.sin(time * d.twinkle + d.phase));
      if (alpha < 0.01) continue;
      ctx.fillStyle = "rgba(214, 196, 250," + alpha.toFixed(3) + ")";
      ctx.fillRect(x, d.y, d.size, d.size);
    }
  }

  function draw() {
    ctx.clearRect(0, 0, w, h);
    drawDust();
    drawScatter();

    for (var i = 0; i < particles.length; i++) {
      var p = particles[i], x, y, alpha, color;

      if (p.s === -1) {
        // Rising beam: tight column above the merge point, brightest near it.
        x = merge.x + p.spread * 3;
        y = merge.y * (1 - p.t);
        alpha = 0.9 * (1 - p.t * 0.7);
        color = "239, 230, 255";
      } else {
        var stream = streams[p.s];
        var pos = bezier(stream, p.t);
        var tan = tangent(stream, p.t);
        // Fibers start wide and pull together as they approach the merge point.
        var offset = p.spread * 20 * (1 - p.t);
        x = pos.x - tan.y * offset;
        y = pos.y + tan.x * offset;

        alpha = 0.95 * reach(x);
        if (p.s !== WINNER) alpha *= 1 - smooth(0.82, 1, p.t);
        if (p.s === WINNER) {
          color = "214, 196, 250";
        } else {
          color = "180, 144, 242";
          alpha *= 0.85;
        }
      }

      if (alpha <= 0.01) continue;

      // Sparks get a soft square halo instead of canvas shadowBlur, which is far cheaper.
      if (p.spark) {
        ctx.fillStyle = "rgba(" + color + "," + (alpha * 0.22).toFixed(3) + ")";
        ctx.fillRect(x - 3, y - 3, p.size + 6, p.size + 6);
        color = "239, 230, 255";
      }
      ctx.fillStyle = "rgba(" + color + "," + alpha.toFixed(3) + ")";
      ctx.fillRect(x, y, p.size, p.size);
    }
  }

  function step(dt) {
    time += dt;
    for (var i = 0; i < particles.length; i++) {
      var p = particles[i];
      p.t += p.speed * dt;
      if (p.t >= 1) {
        p.t -= 1;
        p.spread = (Math.random() - 0.5) * 2;
      }
    }
    for (var j = 0; j < dust.length; j++) {
      var d = dust[j];
      d.y -= d.rise * dt;
      if (d.y < -2) {
        d.y = h + 2;
        d.x = dustX();
      }
    }
    for (var k = 0; k < scatter.length; k++) {
      var sp = scatter[k];
      sp.dist += sp.speed * dt;
      sp.y -= sp.lift * dt;
      if (sp.dist >= sp.life || sp.y < -2) spawnScatter(sp, false);
    }
  }

  function frame(now) {
    if (!running) return;
    var dt = last ? Math.min((now - last) / 1000, 0.05) : 0;
    last = now;
    step(dt);
    draw();
    window.requestAnimationFrame(frame);
  }

  function start() {
    if (running || reduce || !visible || document.hidden) return;
    running = true;
    last = 0;
    window.requestAnimationFrame(frame);
  }

  function stop() {
    running = false;
  }

  resize();
  seed();
  draw();

  if (reduce) return;

  window.addEventListener("resize", function () {
    var wasNarrow = narrow;
    resize();
    if (narrow !== wasNarrow) seed();
    draw();
  });

  document.addEventListener("visibilitychange", function () {
    if (document.hidden) stop(); else start();
  });

  if ("IntersectionObserver" in window) {
    new IntersectionObserver(function (entries) {
      visible = entries[0].isIntersecting;
      if (visible) start(); else stop();
    }).observe(canvas);
  }

  start();
})();
