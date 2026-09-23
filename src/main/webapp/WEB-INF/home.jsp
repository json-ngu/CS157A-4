<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" data-theme="dark">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script>
    (function () {
      var theme = "dark";
      try { if (localStorage.getItem("mm-theme") === "light") theme = "light"; } catch (e) {}
      document.documentElement.setAttribute("data-theme", theme);
    })();
  </script>
  <title>MindMerge</title>
  <meta name="description" content="MindMerge records the reasoning behind a team's decisions so it can be found again later.">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=IBM+Plex+Mono:wght@400;500&family=IBM+Plex+Sans:wght@400;500;600&display=swap">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<header class="site-header">
  <div class="bar">
    <a class="wordmark" href="${pageContext.request.contextPath}/home">MindMerge</a>
    <nav class="nav">
      <a href="#problem">Why</a>
      <a href="#how">How it works</a>
      <a href="#demo">Demo</a>
      <a href="#decisions">Decisions</a>
      <button class="theme-toggle" type="button" id="themeToggle" aria-pressed="true" hidden>
        <span class="theme-swatch" aria-hidden="true"></span><span class="theme-label">Dark</span>
      </button>
    </nav>
  </div>
</header>

<main>

  <section class="hero">
    <div class="hero-inner">
      <div class="hero-copy">
        <p class="kicker"><span class="kicker-dot"></span>Decision memory for teams</p>
        <h1>Six months from now, someone will ask <em>why.</em></h1>
        <p class="lede">
          Organizations make consequential decisions every week: which database to adopt,
          which vendor to engage, which feature to cut. The outcome is recorded. The
          reasoning behind it rarely is. MindMerge captures the context, the alternatives
          considered, and the justification for the final choice, so that reasoning remains
          available long after the people involved have moved on.
        </p>
        <div class="actions">
          <a class="btn" href="#decisions">See recent decisions (Testing)</a>
          <a class="btn btn-quiet" href="#how">How it works</a>
        </div>
      </div>

      <div class="hero-visual" aria-hidden="true">
        <div class="record record-old">
          <div class="record-old-row">
            <p class="record-id">Decision 12</p>
            <p class="record-title">Keep sessions in server memory</p>
            <span class="stamp">Superseded</span>
          </div>
        </div>

        <div class="record record-new">
          <div class="record-top">
            <p class="record-id">Decision 31</p>
            <span class="pill pill-sage">Active</span>
          </div>
          <p class="record-title">Move session storage to Postgres</p>

          <p class="field-label">Context</p>
          <p class="field-text">Sessions are lost on every deploy and the second server cannot see them.</p>

          <p class="field-label">Options considered</p>
          <ul class="options">
            <li class="opt">Redis cache</li>
            <li class="opt is-chosen">Postgres table</li>
            <li class="opt">Sticky sessions</li>
          </ul>

          <p class="field-label">Rationale</p>
          <p class="field-text">We already run Postgres. A second datastore adds an on call burden we cannot staff yet.</p>

          <div class="record-foot">
            <span class="avatars"><span class="av av-a">JN</span><span class="av av-b">AL</span><span class="av av-c">HL</span></span>
            <span class="supersedes">Replaces decision 12</span>
          </div>
        </div>
      </div>
    </div>
  </section>

  <section class="section" id="problem">
    <div class="section-head">
      <p class="eyebrow">Why</p>
      <h2 class="section-title">Where the reasoning goes</h2>
    </div>

    <div class="cols">
      <div>
        <h3>The record is incomplete</h3>
        <p>
          A decision typically leaves behind a single line of notes, a merged pull request,
          or a message in a thread that is difficult to locate later. The outcome is
          documented. The tradeoffs, the rejected alternatives, and the constraint that
          drove the choice are not.
        </p>
      </div>
      <div>
        <h3>The context leaves with the decision maker</h3>
        <p>
          When the person responsible for a decision changes teams or leaves the
          organization, the reasoning behind it goes unrecorded. What remains is a system
          no one can fully account for, and a settled debate the team is obliged to repeat
          before anyone is willing to change it.
        </p>
      </div>
    </div>

    <div class="compare">
      <div class="compare-panel compare-before">
        <p class="compare-label">What usually survives</p>
        <div class="scrap">
          <p class="scrap-line">Switched sessions to postgres</p>
          <p class="scrap-meta">merged 3 weeks ago</p>
        </div>
        <p class="compare-note">One line. No alternatives, no constraints, no names.</p>
      </div>
      <div class="compare-panel compare-after">
        <p class="compare-label">What MindMerge keeps</p>
        <dl class="kept">
          <div><dt>Context</dt><dd>Sessions lost on deploy</dd></div>
          <div><dt>Options</dt><dd>Redis, Postgres, sticky sessions</dd></div>
          <div><dt>Chosen</dt><dd>Postgres table</dd></div>
          <div><dt>Rationale</dt><dd>No staff for a second datastore</dd></div>
          <div><dt>Decided by</dt><dd>Three people, on a known date</dd></div>
          <div><dt>History</dt><dd>Replaces decision 12</dd></div>
        </dl>
      </div>
    </div>
  </section>

  <section class="section section-tint" id="how">
    <div class="section-head">
      <p class="eyebrow">How it works</p>
      <h2 class="section-title">Four habits, one searchable record</h2>
    </div>
    <ol class="steps">
      <li>
        <span class="step-num">1</span>
        <h3>Record the decision while it is fresh</h3>
        <p>Title, context, the options on the table, the one you chose, and why. Each goes in its own structured field.</p>
      </li>
      <li>
        <span class="step-num">2</span>
        <h3>Keep the alternatives</h3>
        <p>Rejected options are stored alongside the chosen one, so a later reader can see what was already considered and ruled out.</p>
      </li>
      <li>
        <span class="step-num">3</span>
        <h3>Supersede instead of deleting</h3>
        <p>When a decision changes, the new one links back to the old. The history stays intact and reads as a timeline.</p>
      </li>
      <li>
        <span class="step-num">4</span>
        <h3>Search when the question comes up</h3>
        <p>Filter by project, tag, or person, and search the reasoning itself. "Why are we doing it this way" becomes a quick query.</p>
      </li>
    </ol>
  </section>

  <section class="section" id="demo">
    <div class="section-head">
      <p class="eyebrow">Demo</p>
      <h2 class="section-title">See it in use</h2>
    </div>
    <figure class="demo">
      <%-- Replace the contents of .demo-screen with a video embed (iframe or <video>) when the walkthrough is recorded. --%>
      <div class="demo-frame" id="demoFrame">
        <div class="demo-chrome" aria-hidden="true">
          <span class="chrome-dot"></span><span class="chrome-dot"></span><span class="chrome-dot"></span>
          <span class="chrome-url">mindmerge / decisions</span>
        </div>
        <div class="demo-screen" aria-label="Product demo placeholder">
          <div class="demo-skel" aria-hidden="true">
            <div class="demo-side">
              <div class="skeleton skeleton-line w-80"></div>
              <div class="skeleton skeleton-line w-60"></div>
              <div class="skeleton skeleton-line w-70"></div>
              <div class="skeleton skeleton-line w-45"></div>
            </div>
            <div class="demo-main">
              <div class="skeleton skeleton-block"></div>
              <div class="skeleton skeleton-line w-90"></div>
              <div class="skeleton skeleton-line w-75"></div>
              <div class="skeleton skeleton-block short"></div>
              <div class="skeleton skeleton-line w-85"></div>
            </div>
          </div>
          <div class="demo-overlay">
            <span class="play" aria-hidden="true"></span>
            <p class="demo-note">Walkthrough video coming soon</p>
          </div>
        </div>
      </div>
      <figcaption>Logging a decision, searching past decisions, and replacing one that changed.</figcaption>
    </figure>
  </section>

  <section class="section section-tint" id="decisions">
    <div class="section-head">
      <p class="eyebrow">Live data (This section will be removed for production, only kept for testing purposes)</p>
      <h2 class="section-title">Recent decisions</h2>
      <p class="section-note">Loaded from the database when this page opens.</p>
    </div>

    <ul class="decision-list" id="decisionList" aria-busy="true" aria-live="polite">
      <li class="decision is-skeleton" aria-hidden="true">
        <div class="skeleton skeleton-line w-70 tall"></div>
        <div class="skeleton skeleton-chip"></div>
        <div class="skeleton skeleton-line w-90"></div>
        <div class="skeleton skeleton-line w-60"></div>
        <div class="skeleton skeleton-line w-30"></div>
      </li>
      <li class="decision is-skeleton" aria-hidden="true">
        <div class="skeleton skeleton-line w-60 tall"></div>
        <div class="skeleton skeleton-chip"></div>
        <div class="skeleton skeleton-line w-85"></div>
        <div class="skeleton skeleton-line w-70"></div>
        <div class="skeleton skeleton-line w-25"></div>
      </li>
      <li class="decision is-skeleton" aria-hidden="true">
        <div class="skeleton skeleton-line w-75 tall"></div>
        <div class="skeleton skeleton-chip"></div>
        <div class="skeleton skeleton-line w-80"></div>
        <div class="skeleton skeleton-line w-45"></div>
        <div class="skeleton skeleton-line w-30"></div>
      </li>
      <li class="decision is-skeleton" aria-hidden="true">
        <div class="skeleton skeleton-line w-60 tall"></div>
        <div class="skeleton skeleton-chip"></div>
        <div class="skeleton skeleton-line w-90"></div>
        <div class="skeleton skeleton-line w-60"></div>
        <div class="skeleton skeleton-line w-35"></div>
      </li>
    </ul>

    <noscript>
      <p class="section-note">JavaScript is off, so the list below is rendered by the server instead.</p>
      <ul class="decision-list">
        <c:forEach var="d" items="${decisions}">
          <li class="decision">
            <h3><c:out value="${d.title}"/></h3>
            <p class="choice">Chose <strong><c:out value="${d.chosenOption}"/></strong></p>
            <p class="why"><c:out value="${d.rationale}"/></p>
            <p class="meta"><c:out value="${d.decidedBy}"/>, <c:out value="${d.decidedAt}"/></p>
          </li>
        </c:forEach>
      </ul>
    </noscript>
  </section>

  <section class="closing">
    <div class="closing-inner">
      <h2>Write down the why while you still remember it.</h2>
      <a class="btn" href="">Start Now</a>
    </div>
  </section>

</main>

<footer class="site-footer">
  <div class="foot-grid">
    <div>
      <p class="foot-mark wordmark">MindMerge</p>
      <p class="foot-line">A student project for CS157A at San José State University.</p>
      <p class="foot-line">Team 4: Jason Nguyen, Andrew Khoi Luu, Hailey Liu</p>
    </div>
    <div>
      <p class="foot-head">Legal</p>
      <p class="foot-line"><a href="${pageContext.request.contextPath}/terms.jsp">Terms of Service</a></p>
      <p class="foot-line"><a href="${pageContext.request.contextPath}/privacy.jsp">Privacy Policy</a></p>
    </div>
    <div>
      <p class="foot-head">Status</p>
      <p class="foot-line status">
        Database:
        <c:choose>
          <c:when test="${dbStatus eq 'connected'}"><span class="ok">Connected</span></c:when>
          <c:otherwise><span class="bad"><c:out value="${dbStatus}"/></span></c:otherwise>
        </c:choose>
      </p>
      <p class="foot-line status">Server time: <%= new java.util.Date() %></p>
    </div>
  </div>
</footer>

<script src="${pageContext.request.contextPath}/js/theme.js"></script>
<script src="https://cdn.jsdelivr.net/npm/lenis@1.3.26/dist/lenis.min.js"></script>
<script src="${pageContext.request.contextPath}/js/motion.js"></script>
<script src="${pageContext.request.contextPath}/js/decisions.js"></script>
</body>
</html>
