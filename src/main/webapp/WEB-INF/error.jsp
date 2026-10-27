<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Shown for 404, 405 and 500 (see web.xml). Never prints exception details. --%>
<c:set var="status" value="${requestScope['javax.servlet.error.status_code']}"/>
<c:choose>
  <c:when test="${status == 404}">
    <c:set var="errorTitle" value="Page not found"/>
    <c:set var="errorText" value="The page you requested does not exist or has been moved."/>
  </c:when>
  <c:when test="${status == 405}">
    <c:set var="errorTitle" value="Action not available"/>
    <c:set var="errorText" value="This address cannot be opened directly. Use the buttons within MindMerge instead."/>
  </c:when>
  <c:otherwise>
    <c:set var="errorTitle" value="Something went wrong"/>
    <c:set var="errorText" value="The request could not be completed. Please try again later."/>
  </c:otherwise>
</c:choose>
<c:set var="pageTitle" value="${errorTitle} | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body class="auth">
<div class="auth-light" aria-hidden="true"><div class="hero-beam"></div></div>
<div class="grain" aria-hidden="true"></div>

<main class="auth-card">
  <a class="brand brand-lg" href="${pageContext.request.contextPath}/home"><%@ include file="/WEB-INF/jspf/logo.jspf" %><span>MindMerge</span></a>
  <h1><c:out value="${errorTitle}"/></h1>
  <p class="error-text"><c:out value="${errorText}"/></p>
  <c:choose>
    <c:when test="${not empty sessionScope.user}">
      <a class="pill pill-glow pill-block" href="${pageContext.request.contextPath}/dashboard">Go to dashboard</a>
    </c:when>
    <c:otherwise>
      <a class="pill pill-glow pill-block" href="${pageContext.request.contextPath}/home">Go to home page</a>
    </c:otherwise>
  </c:choose>
</main>
</body>
</html>
