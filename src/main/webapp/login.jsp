<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Log in | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="auth">
<div class="auth-light" aria-hidden="true"><div class="hero-beam"></div></div>
<div class="auth-orb" aria-hidden="true"></div>
<div class="grain" aria-hidden="true"></div>

<main class="auth-card">
  <a class="brand brand-lg" href="${pageContext.request.contextPath}/home"><%@ include file="/WEB-INF/jspf/logo.jspf" %><span>MindMerge</span></a>
  <h1>Log in</h1>

  <c:if test="${not empty error}">
    <p class="alert" role="alert"><c:out value="${error}"/></p>
  </c:if>
  <%@ include file="/WEB-INF/jspf/notice.jspf" %>
  <c:if test="${param.registered eq 'true'}">
    <p class="notice">Account created. You can log in now.</p>
  </c:if>

  <form class="form" method="post" action="${pageContext.request.contextPath}/login">
    <label for="email">Email</label>
    <input id="email" name="email" type="email" required autocomplete="username" maxlength="180" value="<c:out value='${email}'/>">

    <label for="password">Password</label>
    <div class="password-field">
      <input id="password" name="password" type="password" required autocomplete="current-password">
      <button class="reveal" type="button" aria-controls="password" aria-label="Show password" hidden>
        <svg class="icon-eye" viewBox="0 0 24 24" aria-hidden="true"><path d="M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7S2 12 2 12Z"/><circle cx="12" cy="12" r="3"/></svg>
        <svg class="icon-eye-off" viewBox="0 0 24 24" aria-hidden="true"><path d="M3 3l18 18"/><path d="M10.6 5.1A10.4 10.4 0 0 1 12 5c6.5 0 10 7 10 7a17.6 17.6 0 0 1-3.2 4.2M6.6 6.6C3.9 8.4 2 12 2 12s3.5 7 10 7c1.9 0 3.5-.5 4.9-1.3"/><path d="M9.9 9.9a3 3 0 0 0 4.2 4.2"/></svg>
      </button>
    </div>

    <button class="pill pill-glow pill-block" type="submit">Log in</button>
  </form>

  <p class="auth-switch">Need an account? <a href="${pageContext.request.contextPath}/register">Create one</a></p>
</main>

<script src="${pageContext.request.contextPath}/js/auth.js"></script>
</body>
</html>
