<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Projects | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="app">
  <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

  <main class="app-main manage">
    <header class="manage-head">
      <div>
        <h1>Projects</h1>
        <p class="dash-sub">Projects on your teams and in the companies you administer.</p>
      </div>
    </header>

    <%@ include file="/WEB-INF/jspf/notice.jspf" %>
    <c:if test="${not empty loadError or not empty error}">
      <p class="alert page-notice" role="alert"><c:out value="${empty loadError ? error : loadError}"/></p>
    </c:if>

    <c:if test="${empty loadError}">
      <%-- Only team leads and company owners/admins have teams here; ProjectServlet re-checks on submit. --%>
      <c:if test="${not empty teams}">
        <section class="box" aria-labelledby="newProjectHeading">
          <h2 id="newProjectHeading">Create a project</h2>
          <form class="form project-form" method="post" action="${pageContext.request.contextPath}/projects">
            <input type="hidden" name="action" value="create">

            <label for="team">Team</label>
            <select id="team" class="select" name="team" required>
              <option value="">Select a team</option>
              <c:forEach var="t" items="${teams}">
                <option value="${t.teamId}" ${t.teamId == teamId ? 'selected' : ''}><c:out value="${t.name}"/> (<c:out value="${t.companyName}"/>)</option>
              </c:forEach>
            </select>

            <label for="name">Name</label>
            <input id="name" name="name" type="text" required maxlength="180" value="<c:out value='${name}'/>">

            <label for="description">Description <span class="optional">(optional)</span></label>
            <textarea id="description" name="description" maxlength="10000"
                      placeholder="Describe the scope and goals of the project."><c:out value="${description}"/></textarea>

            <button class="pill pill-glow" type="submit">Create project</button>
          </form>
        </section>
      </c:if>

      <c:choose>
        <c:when test="${empty groups}">
          <section class="box" aria-labelledby="noProjectsHeading">
            <h2 id="noProjectsHeading">Projects <span class="count">0</span></h2>
            <p class="panel-note">You do not have access to any projects. Projects appear here once you join a team that has one.</p>
          </section>
        </c:when>
        <c:otherwise>
          <c:forEach var="g" items="${groups}">
            <section class="box" aria-label="${g.key} projects">
              <h2><c:out value="${g.key}"/> <span class="count"><c:out value="${fn:length(g.value)}"/></span></h2>
              <ul class="row-list">
                <c:forEach var="p" items="${g.value}">
                  <li class="row">
                    <div>
                      <p class="row-title"><a href="${pageContext.request.contextPath}/projects?project=${p.projectId}"><c:out value="${p.name}"/></a></p>
                      <p class="row-meta"><c:out value="${p.teamName}"/>, <c:out value="${p.companyName}"/></p>
                    </div>
                    <p class="project-count">
                      <c:out value="${p.decisionCount}"/>
                      <span><c:out value="${p.decisionCount == 1 ? 'decision' : 'decisions'}"/></span>
                    </p>
                  </li>
                </c:forEach>
              </ul>
            </section>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </c:if>
  </main>
</div>
</body>
</html>
