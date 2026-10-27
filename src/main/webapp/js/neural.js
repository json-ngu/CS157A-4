// Login and register: "neural light flow". A WebGL shader paints soft, smoky purple and blue light
// that slowly flows and folds behind the card, like activity in a brain scan, with occasional
// brighter flares along its ridges. It is strongest on the beam side and calmer behind the card.
// It renders at half resolution (the light is soft, so this costs nothing visible), pauses when
// the tab is hidden, draws one still frame under prefers-reduced-motion, and does nothing at all
// where WebGL is unavailable (the CSS beam behind it still shows).
(function () {
  "use strict";

  var canvas = document.querySelector(".auth-canvas");
  if (!canvas) return;
  var gl = canvas.getContext("webgl", { alpha: true, premultipliedAlpha: true, antialias: false });
  if (!gl) return;

  var reduce = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
  var SCALE = 0.5; // render at half the CSS size

  var VERTEX = [
    "attribute vec2 aPos;",
    "void main() { gl_Position = vec4(aPos, 0.0, 1.0); }"
  ].join("\n");

  // Domain-warped fractal noise: two warping passes give the slow, folding smoke; the ridges
  // (where the noise crosses its midpoint) read as glowing filaments; a travelling sine wave
  // lights up parts of those ridges now and then as flares.
  var FRAGMENT = [
    "precision mediump float;",
    "uniform vec2 uRes;",
    "uniform float uTime;",
    "float hash(vec2 p) { return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453); }",
    "float noise(vec2 p) {",
    "  vec2 i = floor(p), f = fract(p);",
    "  vec2 u = f * f * (3.0 - 2.0 * f);",
    "  return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), u.x),",
    "             mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), u.x), u.y);",
    "}",
    "float fbm(vec2 p) {",
    "  float v = 0.0, a = 0.5;",
    "  mat2 m = mat2(1.6, 1.2, -1.2, 1.6);",
    "  for (int i = 0; i < 5; i++) { v += a * noise(p); p = m * p; a *= 0.5; }",
    "  return v;",
    "}",
    "void main() {",
    "  vec2 uv = gl_FragCoord.xy / uRes;",
    "  vec2 p = (gl_FragCoord.xy - 0.5 * uRes) / uRes.y;",
    "  float t = uTime * 0.02;",
    "  vec2 q = vec2(fbm(p * 1.5 + vec2(0.0, t)), fbm(p * 1.5 + vec2(5.2, 1.3 - t)));",
    "  vec2 r = vec2(fbm(p * 1.5 + 3.0 * q + vec2(1.7, 9.2) + 1.5 * t),",
    "                fbm(p * 1.5 + 3.0 * q + vec2(8.3, 2.8) - t));",
    "  float f = fbm(p * 1.5 + 3.0 * r);",
    "  float ridge = pow(1.0 - abs(2.0 * f - 1.0), 4.0);",
    "  vec3 blue = vec3(0.20, 0.36, 0.84);",
    "  vec3 purple = vec3(0.44, 0.22, 0.72);",
    "  vec3 lavender = vec3(0.86, 0.78, 1.0);",
    "  vec3 col = mix(blue, purple, clamp(length(q) * 0.9, 0.0, 1.0));",
    "  col = mix(col, lavender, ridge * 0.75);",
    "  float flare = pow(0.5 + 0.5 * sin(uTime * 0.25 + f * 11.0 - p.y * 3.0), 14.0) * ridge;",
    "  col += lavender * flare * 0.5;",
    "  float density = smoothstep(0.4, 0.95, f) * 0.5 + ridge * 0.35 + flare * 0.4;",
    "  float side = 0.3 + 0.7 * smoothstep(0.2, 0.9, uv.x);",            // stronger on the beam side
    "  float fade = smoothstep(1.15, 0.25, length((uv - vec2(0.62, 0.5)) * vec2(1.0, 1.15)));",
    "  float a = clamp(density * side * fade, 0.0, 0.5);",
    "  gl_FragColor = vec4(col * a, a);",
    "}"
  ].join("\n");

  function shader(type, source) {
    var s = gl.createShader(type);
    gl.shaderSource(s, source);
    gl.compileShader(s);
    return gl.getShaderParameter(s, gl.COMPILE_STATUS) ? s : null;
  }

  var vs = shader(gl.VERTEX_SHADER, VERTEX);
  var fs = shader(gl.FRAGMENT_SHADER, FRAGMENT);
  if (!vs || !fs) return;
  var program = gl.createProgram();
  gl.attachShader(program, vs);
  gl.attachShader(program, fs);
  gl.linkProgram(program);
  if (!gl.getProgramParameter(program, gl.LINK_STATUS)) return;
  gl.useProgram(program);

  // One triangle that covers the whole canvas.
  var buffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array([-1, -1, 3, -1, -1, 3]), gl.STATIC_DRAW);
  var aPos = gl.getAttribLocation(program, "aPos");
  gl.enableVertexAttribArray(aPos);
  gl.vertexAttribPointer(aPos, 2, gl.FLOAT, false, 0, 0);

  var uRes = gl.getUniformLocation(program, "uRes");
  var uTime = gl.getUniformLocation(program, "uTime");

  var time = Math.random() * 100; // start somewhere different each visit
  var last = 0, running = false;

  function resize() {
    canvas.width = Math.max(1, Math.round(canvas.clientWidth * SCALE));
    canvas.height = Math.max(1, Math.round(canvas.clientHeight * SCALE));
    gl.viewport(0, 0, canvas.width, canvas.height);
    gl.uniform2f(uRes, canvas.width, canvas.height);
  }

  function draw() {
    gl.uniform1f(uTime, time);
    gl.drawArrays(gl.TRIANGLES, 0, 3);
  }

  function frame(now) {
    if (!running) return;
    var dt = last ? Math.min((now - last) / 1000, 0.05) : 0;
    last = now;
    time += dt;
    draw();
    window.requestAnimationFrame(frame);
  }

  function start() {
    if (running || reduce || document.hidden) return;
    running = true;
    last = 0;
    window.requestAnimationFrame(frame);
  }

  resize();
  if (reduce) {
    draw(); // one still frame
    return;
  }

  window.addEventListener("resize", function () {
    resize();
    if (!running) draw();
  });
  document.addEventListener("visibilitychange", function () {
    if (document.hidden) running = false; else start();
  });
  start();
})();
