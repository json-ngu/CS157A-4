<%@ page contentType="text/html; charset=UTF-8" %>
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
  <title>Terms of Service | MindMerge</title>
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
      <a href="${pageContext.request.contextPath}/home">Home</a>
      <a href="${pageContext.request.contextPath}/privacy.jsp">Privacy Policy</a>
      <button class="theme-toggle" type="button" id="themeToggle" aria-pressed="true" hidden>
        <span class="theme-swatch" aria-hidden="true"></span><span class="theme-label">Dark</span>
      </button>
    </nav>
  </div>
</header>

<main class="legal">

  <h1>Terms of Service</h1>
  <p class="updated">Last updated September 23, 2026</p>

  <p>
    MindMerge is a student project built for CS157A at San José State University. It is
    coursework, not a commercial product. These terms describe what you can expect from
    the application and what we expect from anyone who uses it.
  </p>

  <h2>1. Acceptance</h2>
  <p>
    By accessing or using MindMerge, you agree to these terms. If you do not agree with
    them, do not use the application.
  </p>

  <h2>2. What MindMerge does</h2>
  <p>
    MindMerge allows a team to record decisions along with their context, the options that
    were considered, the reasoning behind the final choice, and the person who made it.
    Recorded decisions can be searched, linked to related decisions, and replaced by newer
    decisions.
  </p>

  <h2>3. Academic project status</h2>
  <p>
    This application is developed and operated by students as part of a university course.
    It runs in a local development environment, is not monitored, and may be taken offline,
    reset, or substantially changed at any time without notice. Data stored in it may be
    deleted as part of normal development work, including when the database schema is
    rebuilt.
  </p>
  <p>
    Do not use MindMerge to store confidential, regulated, or otherwise sensitive
    information.
  </p>

  <h2>4. Accounts</h2>
  <p>
    Some features require an account. You are responsible for the accuracy of the
    information you provide and for keeping your password confidential. You are responsible
    for activity that occurs under your account. Tell the project team if you believe your
    account has been accessed by someone else.
  </p>

  <h2>5. Your content</h2>
  <p>
    You keep ownership of the decisions and text you enter. By entering content, you grant
    the project team permission to store and display it within the application so that it
    works as intended, and to view it while developing, testing, and grading the project.
  </p>
  <p>
    Decisions in MindMerge are not permanently deleted through normal use. A decision that
    no longer applies is marked as replaced and remains visible as part of the project
    history. Consider that before entering anything you would want removed later.
  </p>

  <h2>6. Acceptable use</h2>
  <p>You agree not to:</p>
  <ul>
    <li>Enter unlawful, harassing, or deliberately false content.</li>
    <li>Enter content that infringes someone else's rights.</li>
    <li>Attempt to access accounts, data, or systems you have not been given access to.</li>
    <li>Interfere with the operation of the application, including by overloading it or probing it for vulnerabilities without permission.</li>
    <li>Use automated tools to extract data at scale.</li>
  </ul>

  <h2>7. Availability</h2>
  <p>
    MindMerge is provided on an as available basis. There is no uptime commitment, no
    backup guarantee, and no support obligation. Access may be suspended or ended at any
    time.
  </p>

  <h2>8. No warranty</h2>
  <p>
    The application is provided as is, without warranties of any kind, whether express or
    implied, including any implied warranty of merchantability, fitness for a particular
    purpose, or non-infringement. The project team does not warrant that the application
    will be uninterrupted, secure, or free of errors, or that stored data will be preserved.
  </p>

  <h2>9. Limitation of liability</h2>
  <p>
    To the fullest extent permitted by law, the project team and its members are not liable
    for any indirect, incidental, special, or consequential damages, or for any loss of
    data, arising from your use of the application.
  </p>

  <h2>10. Changes to these terms</h2>
  <p>
    These terms may be updated as the project develops. The date at the top of this page
    reflects the most recent change. Continuing to use the application after a change means
    you accept the updated terms.
  </p>

  <h2>11. Contact</h2>
  <p>
    Questions about these terms can be directed to the project team: Jason Nguyen, Andrew
    Khoi Luu, and Hailey Liu, CS157A Team 4, San José State University.
  </p>

  <a class="back" href="${pageContext.request.contextPath}/home">Back to home</a>

</main>

<footer class="site-footer">
  <div class="foot-grid">
    <div>
      <p class="foot-mark">MindMerge</p>
      <p class="foot-line">A student project for CS157A at San José State University.</p>
    </div>
    <div>
      <p class="foot-head">Legal</p>
      <p class="foot-line"><a href="${pageContext.request.contextPath}/terms.jsp">Terms of Service</a></p>
      <p class="foot-line"><a href="${pageContext.request.contextPath}/privacy.jsp">Privacy Policy</a></p>
    </div>
    <div></div>
  </div>
</footer>

<script src="${pageContext.request.contextPath}/js/theme.js"></script>
<script src="https://cdn.jsdelivr.net/npm/lenis@1.3.26/dist/lenis.min.js"></script>
<script src="${pageContext.request.contextPath}/js/motion.js"></script>
</body>
</html>