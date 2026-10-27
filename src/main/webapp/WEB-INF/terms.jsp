<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Terms of Service | MindMerge"/>
<c:set var="legalPage" value="terms"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="legal">
<%@ include file="/WEB-INF/jspf/legal-bar.jspf" %>

<main class="legal-page">
  <h1>Terms of Service</h1>
  <p class="legal-date">Effective October 27, 2026</p>

  <p class="legal-notice">
    MindMerge is a course project for CS157A (Introduction to Database Management Systems) at
    San José State University, built by Team 4. It is provided as-is for demonstration and is not
    a commercial service. Please do not store confidential, sensitive or personal information in it.
  </p>

  <h2>1. Acceptance</h2>
  <p>
    By creating an account or using MindMerge, you agree to these Terms of Service and acknowledge the
    <a href="${pageContext.request.contextPath}/privacy">Privacy Policy</a>. If you do not agree, do not use MindMerge.
  </p>

  <h2>2. The service</h2>
  <p>
    MindMerge lets teams record decisions, the options considered and the reasoning behind them. Because
    it is a course project, features may change, the service may be unavailable, and the database may be
    reset at any time without notice, which permanently removes all data.
  </p>

  <h2>3. Accounts</h2>
  <ul>
    <li>Provide accurate information and keep your password confidential.</li>
    <li>Each account is for one person. You are responsible for activity under your account.</li>
    <li>Report any unauthorized use of your account as described under Contact.</li>
  </ul>

  <h2>4. Your content</h2>
  <p>
    You keep ownership of the content you add, such as decisions, options, rationale and comments. You
    grant Team 4 permission to store it and display it to the people who have access to the relevant
    company, team or project, as needed to operate MindMerge. You are responsible for your content and
    must have the right to share it.
  </p>

  <h2>5. Acceptable use</h2>
  <p>You agree not to:</p>
  <ul>
    <li>use MindMerge for anything unlawful, harmful or deceptive;</li>
    <li>access, or attempt to access, data or accounts you are not authorized to use, or bypass access controls;</li>
    <li>disrupt or overload the service, or scrape it with automated tools;</li>
    <li>impersonate another person or misrepresent your affiliation;</li>
    <li>upload malicious code, or confidential or personal information about others without their consent.</li>
  </ul>

  <h2>6. Companies, teams and records</h2>
  <p>
    Company owners and administrators manage membership and roles. MindMerge keeps a permanent record:
    decisions are never deleted, and when a member leaves or is removed, their membership is marked as
    ended while their past contributions remain visible to the people with access to them.
  </p>

  <h2>7. Suspension and termination</h2>
  <p>
    We may deactivate accounts that violate these terms or that put the service or other users at risk.
    You may stop using MindMerge at any time.
  </p>

  <h2>8. Disclaimer</h2>
  <p>
    MindMerge is provided "as is" and "as available", without warranties of any kind, express or implied,
    including warranties of merchantability, fitness for a particular purpose and non-infringement. We do
    not guarantee that it will be accurate, secure, uninterrupted or free of errors.
  </p>

  <h2>9. Limitation of liability</h2>
  <p>
    To the fullest extent permitted by law, Team 4 and San José State University are not liable for any
    indirect, incidental or consequential damages, or for any loss of data, arising from your use of MindMerge.
  </p>

  <h2>10. Changes to these terms</h2>
  <p>If these terms change, the effective date above will be updated. Continued use after a change means you accept the updated terms.</p>

  <h2>11. Governing law</h2>
  <p>These terms are governed by the laws of the State of California.</p>

  <h2>12. Contact</h2>
  <p>
    Questions can be submitted as an issue on the project's
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
