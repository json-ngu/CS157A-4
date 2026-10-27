<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Privacy Policy | MindMerge"/>
<c:set var="legalPage" value="privacy"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="legal">
<%@ include file="/WEB-INF/jspf/legal-bar.jspf" %>

<main class="legal-page">
  <h1>Privacy Policy</h1>
  <p class="legal-date">Effective October 27, 2026</p>

  <p class="legal-notice">
    MindMerge is a course project for CS157A (Introduction to Database Management Systems) at
    San José State University, built by Team 4. It is provided as-is for demonstration and is not
    a commercial service. Please do not store confidential, sensitive or personal information in it.
  </p>

  <p>This policy explains what information MindMerge collects, how it is used and who can see it.</p>

  <h2>1. Information we collect</h2>
  <ul>
    <li><strong>Account information.</strong> Your full name and email address. Your password is never stored; only a salted PBKDF2-HMAC-SHA256 hash of it is kept.</li>
    <li><strong>Workspace content.</strong> The companies, teams and projects you create or join, your roles in them, invitations you send or receive, and the decisions you record, including their options, rationale, tags and comments.</li>
    <li><strong>Activity records.</strong> An audit log of actions such as creating projects and approving decisions, with who performed them and when.</li>
    <li><strong>Technical information.</strong> The cookies described below, and standard web server logs, which may include your IP address, the time of a request and the page requested.</li>
  </ul>

  <h2>2. How we use it</h2>
  <p>
    Information is used only to operate MindMerge: to sign you in, to show decisions to the members
    of your teams, to deliver invitations and approvals, to keep an audit history and to protect the
    service. We do not sell information, show advertising or use analytics or tracking tools.
  </p>

  <h2>3. Who can see your information</h2>
  <ul>
    <li>Members of your teams can see the decisions recorded in their projects, including your name as the author.</li>
    <li>Owners and administrators of a company can see its members' names, email addresses and roles.</li>
    <li>When you send an invitation, the recipient sees your name and the company's name.</li>
    <li>Team 4, as the operators of the project, can access the database for development and grading.</li>
  </ul>

  <h2>4. Cookies</h2>
  <ul>
    <li><strong>Session cookie</strong> (<code>JSESSIONID</code>). Keeps you signed in. It ends when you log out or after 30 minutes of inactivity.</li>
    <li><strong>Time zone cookie</strong> (<code>tz</code>). Stores your browser's time zone, such as America/Los_Angeles, so that times are shown in your local time. It lasts one year.</li>
  </ul>
  <p>MindMerge uses no advertising or tracking cookies.</p>

  <h2>5. Third-party services</h2>
  <p>
    Every page loads its typeface from Google Fonts, and the landing page loads a scrolling library
    from jsDelivr. These services receive your IP address and browser information when the files are
    requested, under their own privacy policies. No account information is shared with them.
  </p>

  <h2>6. Retention</h2>
  <p>
    MindMerge keeps a permanent record by design. Decisions, options and comments are never deleted.
    When someone leaves or is removed from a company, their membership is marked as ended rather
    than erased, and their past contributions remain. Deactivated accounts cannot sign in, but their
    records remain. Because this is a course project, the database may be reset at any time, which
    permanently removes all data.
  </p>

  <h2>7. Your choices</h2>
  <ul>
    <li>You can turn off company invitations in Settings, which also cancels any pending invitations.</li>
    <li>You can leave a company at any time from the People page.</li>
    <li>To request a correction or the deactivation of your account, contact us as described below.</li>
  </ul>

  <h2>8. Security</h2>
  <p>
    Passwords are stored only as salted hashes, session cookies cannot be read by scripts and are not
    sent with form submissions from other sites, and sessions expire after 30 minutes of inactivity. No system
    is completely secure, and a demonstration deployment may run without HTTPS. Do not reuse a
    password from another service.
  </p>

  <h2>9. Children</h2>
  <p>MindMerge is not intended for children under 13, and we do not knowingly collect their information.</p>

  <h2>10. Changes to this policy</h2>
  <p>If this policy changes, the effective date above will be updated. Continued use after a change means you accept the updated policy.</p>

  <h2>11. Contact</h2>
  <p>
    Questions and requests can be submitted as an issue on the project's
    <a href="https://github.com/json-ngu/CS157A-4/issues" target="_blank" rel="noopener noreferrer">GitHub repository</a>.
    Issues are public, so do not include passwords or other sensitive details.
  </p>
</main>

<footer class="legal-footer">
  <p>MindMerge. A CS157A project at San José State University.</p>
  <a href="${pageContext.request.contextPath}/home">Back to home</a>
</footer>
</body>
</html>
