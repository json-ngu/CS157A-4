<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<%-- The public landing page. --%>
<main>
<div class="hero">
  <div class="hero-light" aria-hidden="true"><div class="hero-beam"></div></div>
  <canvas class="hero-canvas" aria-hidden="true"></canvas>
  <div class="grain" aria-hidden="true"></div>

  <%@ include file="/WEB-INF/jspf/topbar.jspf" %>

  <div class="hero-body">
    <h1>Remember why you decided.</h1>
    <c:choose>
      <c:when test="${not empty sessionScope.user}"><a class="pill pill-glow pill-lg" href="${pageContext.request.contextPath}/dashboard">Go to dashboard</a></c:when>
      <c:otherwise><a class="pill pill-glow pill-lg" href="${pageContext.request.contextPath}/register">Log your first decision</a></c:otherwise>
    </c:choose>
  </div>

  <div class="hero-foot">
    <p class="hero-lede">
      MindMerge keeps track of what your team considered, what you picked and why,
      so nobody has to dig through old chats to find out.
    </p>
    <p class="hero-note">
      <svg class="mark" viewBox="0 0 10 10" aria-hidden="true"><path d="M1 1 9 9M9 1 1 9"/></svg>
      Old decisions are never deleted. You can always see what changed and why.
    </p>
  </div>
</div>

<section class="section intro" id="how">
  <div class="intro-copy fade" data-reveal>
    <h2 class="display">Decisions get made. The reasons get lost.</h2>
    <p>
      Most choices happen in a quick meeting or a chat thread. A few weeks later,
      nobody remembers why.
    </p>
    <p>
      MindMerge gives each decision a simple page: what was going on, the options you
      looked at, what you picked and why.
    </p>
  </div>

  <%-- An example record, drawn in the product's own style. Illustration only. --%>
  <div class="record" aria-hidden="true" data-reveal>
    <p class="record-project">Decision Log Service</p>
    <p class="record-title">Store decisions in MySQL rather than a document store</p>
    <p class="record-meta"><span class="chip">Accepted</span> Decided by Hailey Liu on Jan 19, 2026</p>

    <div class="record-section">
      <p class="record-label">Options considered</p>
      <ul class="record-options">
        <li class="is-chosen">
          <span class="option-name">Relational schema in MySQL <span class="option-flag">Picked</span></span>
          <span class="option-note">Joins across projects, options and tags are handled by the database.</span>
        </li>
        <li>
          <span class="option-name">MongoDB document store</span>
          <span class="option-note">Flexible shape, but every join moves into application code.</span>
        </li>
        <li>
          <span class="option-name">JSON files in the repository</span>
          <span class="option-note">Nothing to run, and nothing to query.</span>
        </li>
      </ul>
    </div>

    <div class="record-section">
      <p class="record-label">Why</p>
      <p class="record-why">
        Decision records have a fixed shape and are read through their links to projects,
        options and tags. A relational schema keeps those links in the database.
      </p>
    </div>
  </div>
</section>

<section class="section" id="principles">
  <h2 class="display fade" data-reveal>Built for small teams.</h2>
  <div class="panels" data-reveal>
    <article class="panel panel-violet">
      <svg class="panel-art" viewBox="0 0 320 150" aria-hidden="true">
        <path class="dots dim" d="M20 40 C 120 40, 170 75, 300 75"/>
        <path class="dots dim" d="M20 110 C 120 110, 170 75, 300 75"/>
        <path class="dots lit" d="M20 75 H 300"/>
        <circle class="pulse pulse-sm" cx="300" cy="75" r="5"/>
        <circle class="node" cx="300" cy="75" r="5"/>
      </svg>
      <div class="panel-body">
        <h3>Keep every option</h3>
        <p>See what you picked and what you passed on, with the pros and cons of each.</p>
      </div>
    </article>
    <article class="panel panel-navy">
      <svg class="panel-art" viewBox="0 0 320 150" aria-hidden="true">
        <circle class="ring dim old" cx="80" cy="75" r="26"/>
        <path class="dots lit" d="M112 75 C 150 35, 185 35, 218 62"/>
        <path class="arrow" pathLength="1" d="M207 60 219 63 214 52"/>
        <circle class="pulse pulse-lg" cx="250" cy="75" r="26"/>
        <circle class="node node-lg" cx="250" cy="75" r="26"/>
      </svg>
      <div class="panel-body">
        <h3>Change your mind safely</h3>
        <p>Made a new call? Link it to the old one, and the history stays intact.</p>
      </div>
    </article>
    <article class="panel panel-umber">
      <svg class="panel-art" viewBox="0 0 320 150" aria-hidden="true">
        <circle class="ring spin" cx="160" cy="75" r="44"/>
        <path class="check" pathLength="1" d="M140 76 154 90 182 60"/>
      </svg>
      <div class="panel-body">
        <h3>Get a quick sign-off</h3>
        <p>Your team lead approves a decision, so everyone knows it's final.</p>
      </div>
    </article>
  </div>
</section>

<section class="statement" data-reveal>
  <p>Someone will ask, "Why did we do it this way?"</p>
  <p>Now you'll have the answer.</p>
</section>

<section class="closing">
  <h2 class="display">Start keeping track of your team's decisions.</h2>
  <div class="closing-actions">
    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <a class="pill pill-glow pill-lg" href="${pageContext.request.contextPath}/dashboard">Go to dashboard</a>
      </c:when>
      <c:otherwise>
        <a class="pill pill-glow pill-lg" href="${pageContext.request.contextPath}/register">Get started</a>
        <a class="pill pill-ghost pill-lg" href="${pageContext.request.contextPath}/login">Log in</a>
      </c:otherwise>
    </c:choose>
  </div>
</section>
</main>

<footer class="site-footer">
  <div class="footer-brand">
    <a class="brand" href="${pageContext.request.contextPath}/home"><%@ include file="/WEB-INF/jspf/logo.jspf" %><span>MindMerge</span></a>
    <p>Decision tracking for small teams. A CS157A project at San José State University.</p>
  </div>
  <div>
    <h2>Product</h2>
    <c:choose>
      <c:when test="${not empty sessionScope.user}"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></c:when>
      <c:otherwise>
        <a href="${pageContext.request.contextPath}/login">Log in</a>
        <a href="${pageContext.request.contextPath}/register">Get started</a>
      </c:otherwise>
    </c:choose>
    <a class="footer-github" href="https://github.com/json-ngu/CS157A-4" target="_blank" rel="noopener noreferrer">
      <svg viewBox="0 0 16 16" aria-hidden="true" focusable="false"><path fill="currentColor" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"/></svg>
      GitHub
    </a>
  </div>
  <div>
    <h2>Legal</h2>
    <a href="${pageContext.request.contextPath}/privacy">Privacy Policy</a>
    <a href="${pageContext.request.contextPath}/terms">Terms of Service</a>
  </div>
  <div>
    <h2>Team 4</h2>
    <p>Jason Nguyen</p>
    <p>Andrew Khoi Luu</p>
    <p>Hailey Liu</p>
  </div>
</footer>

<script src="${pageContext.request.contextPath}/js/hero.js"></script>
<script src="https://cdn.jsdelivr.net/npm/lenis@1.3.26/dist/lenis.min.js"></script>
<script src="${pageContext.request.contextPath}/js/smooth.js"></script>
<script src="${pageContext.request.contextPath}/js/scroll.js"></script>
</body>
</html>
