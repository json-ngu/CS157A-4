<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Approvals | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="app">
  <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

  <main class="app-main manage">
    <header class="manage-head">
      <div>
        <h1>Approvals</h1>
        <p class="dash-sub">Proposed decisions on teams where you are a lead or approver. Review each proposal and approve it to make it final.</p>
      </div>
    </header>

    <%@ include file="/WEB-INF/jspf/notice.jspf" %>
    <c:if test="${not empty error}">
      <p class="alert" role="alert"><c:out value="${error}"/></p>
    </c:if>

    <%-- One card in both states, so the empty page matches the full one. --%>
    <c:if test="${empty error}">
      <section class="box" aria-labelledby="queueHeading">
        <h2 id="queueHeading">Awaiting your approval <span class="count"><c:out value="${fn:length(review)}"/></span></h2>
        <c:choose>
          <c:when test="${empty review}">
            <p class="panel-note">No decisions are awaiting your approval. Decisions you propose are reviewed by another lead or approver.</p>
          </c:when>
          <c:otherwise>
            <ul class="row-list">
              <c:forEach var="d" items="${review}">
                <li class="row">
                  <div>
                    <p class="row-meta"><c:out value="${d.projectName}"/></p>
                    <p class="row-title"><c:out value="${d.title}"/></p>
                    <p class="row-meta">Proposed by <c:out value="${d.decidedBy}"/> on <c:out value="${d.dateAtTime(userZone)}"/></p>
                  </div>
                  <a class="pill pill-ghost pill-sm" href="${pageContext.request.contextPath}/approvals?decision=${d.decisionId}">Review</a>
                </li>
              </c:forEach>
            </ul>
          </c:otherwise>
        </c:choose>
      </section>
    </c:if>
  </main>
</div>
</body>
</html>
