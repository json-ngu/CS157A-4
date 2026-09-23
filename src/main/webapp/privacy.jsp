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
  <title>Privacy Policy | MindMerge</title>
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
      <a href="${pageContext.request.contextPath}/terms.jsp">Terms of Service</a>
      <button class="theme-toggle" type="button" id="themeToggle" aria-pressed="true" hidden>
        <span class="theme-swatch" aria-hidden="true"></span><span class="theme-label">Dark</span>
      </button>
    </nav>
  </div>
</header>

<main class="legal">

  <h1>Privacy Policy</h1>
  <p class="updated">Last updated September 23, 2026</p>

  <p>
    This policy explains what information MindMerge collects, why it is collected, and how
    it is handled. MindMerge is a student project built for CS157A at San José State
    University and is not a commercial service.
  </p>

  <h2>1. Information we collect</h2>
  <p>MindMerge stores only what is needed for the application to work:</p>
  <ul>
    <li><strong>Account information.</strong> Your name, email address, and a hashed version of your password. Plain text passwords are never stored.</li>
    <li><strong>Content you enter.</strong> Decisions and their titles, context, options considered, rationale, tags, and dates, along with the team or project they belong to.</li>
    <li><strong>Activity records.</strong> An audit log entry each time a decision is created, approved, replaced, or reassigned, recording which account performed the action and when.</li>
    <li><strong>Session data.</strong> A temporary session identifier that keeps you signed in while you use the application.</li>
  </ul>
  <p>
    MindMerge does not use analytics services, advertising networks, or third party
    tracking.
  </p>

  <h2>2. How the information is used</h2>
  <p>Collected information is used to:</p>
  <ul>
    <li>Authenticate you and keep you signed in.</li>
    <li>Display decisions to the people who have access to them.</li>
    <li>Attribute a decision to the person who made it, which is central to the purpose of the application.</li>
    <li>Maintain the audit log, so changes to a decision can be traced.</li>
    <li>Develop, test, and grade the project as coursework.</li>
  </ul>
  <p>Your information is not sold, rented, or shared for marketing.</p>

  <h2>3. Where the information is stored</h2>
  <p>
    Data is stored in a MySQL database that runs on a team member's local development
    machine. It is not hosted on a public server and is not accessible over the internet.
    Each team member runs a separate copy of the database with its own data.
  </p>

  <h2>4. Retention and deletion</h2>
  <p>
    Decisions are designed to persist. A decision that no longer applies is marked as
    replaced rather than deleted, because the history is the point of the application.
  </p>
  <p>
    Separately, the entire development database is rebuilt from a schema file during normal
    development work, which erases everything in it. Treat any data entered during the
    course of this project as temporary.
  </p>
  <p>
    When the course ends, the project team will delete the development databases holding
    project data.
  </p>

  <h2>5. Access to your information</h2>
  <p>
    Within the application, access follows the role you are assigned. Team members see
    decisions for the projects they belong to. Administrators can view accounts, team
    membership, and audit logs. Outside the application, the project team can read the
    database directly as part of developing and grading the project, and the course
    instructor may review the project and its contents.
  </p>

  <h2>6. Security</h2>
  <p>The application uses the following measures:</p>
  <ul>
    <li>Passwords are stored as hashes rather than plain text.</li>
    <li>Database queries use prepared statements, which guard against SQL injection.</li>
    <li>Permission checks run on the server, not only in the browser.</li>
    <li>Sessions expire after a period of inactivity.</li>
    <li>Database credentials are kept out of the source code repository.</li>
  </ul>
  <p>
    No system is completely secure. Because this is a student project running in a
    development environment, it should not be used to store sensitive information.
  </p>

  <h2>7. Your choices</h2>
  <p>
    You can request that your account be deactivated or that content you entered be
    corrected or removed by contacting the project team. Deactivating an account does not
    remove the attribution on decisions that account already made, since the record of who
    decided something is part of the decision itself.
  </p>

  <h2>8. Children's privacy</h2>
  <p>
    MindMerge is intended for use by university students working on this course project. It
    is not directed to children under 13, and information is not knowingly collected from
    them.
  </p>

  <h2>9. Changes to this policy</h2>
  <p>
    This policy may be updated as the project develops. The date at the top of this page
    reflects the most recent change.
  </p>

  <h2>10. Contact</h2>
  <p>
    Questions about this policy can be directed to the project team: Jason Nguyen, Andrew
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