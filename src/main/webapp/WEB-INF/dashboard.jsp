<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Dashboard | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="app">
  <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

  <main class="app-main dash">
    <header class="dash-head">
      <div>
        <h1><c:out value="${greeting}"/><c:if test="${not empty viewer}">, <c:out value="${viewer.firstName}"/></c:if></h1>
        <c:if test="${not empty viewer}">
          <c:if test="${viewer.inCompany}">
            <p class="dash-sub"><c:out value="${viewer.companies}"/><c:if test="${not empty viewer.teams}">: <c:out value="${viewer.teams}"/></c:if></p>
          </c:if>
        </c:if>
      </div>
      <c:if test="${viewer.inCompany}">
      <form class="dash-search" action="${pageContext.request.contextPath}/dashboard" method="get" role="search">
        <label class="visually-hidden" for="q">Search decisions</label>
        <input id="q" name="q" type="search" value="<c:out value='${q}'/>" placeholder="Search decisions…" autocomplete="off">
        <c:if test="${view eq 'mine'}"><input type="hidden" name="view" value="mine"></c:if>
      </form>
      </c:if>
      <p class="visually-hidden" id="searchStatus" role="status" aria-live="polite"></p>
    </header>

    <%@ include file="/WEB-INF/jspf/notice.jspf" %>
    <c:if test="${not empty error}">
      <p class="alert" role="alert"><c:out value="${error}"/></p>
    </c:if>

    <c:if test="${empty error}">
      <%-- Pending invites: the invited person joins or declines here. --%>
      <c:forEach var="inv" items="${invites}">
        <section class="invite-banner" aria-label="Invitation from ${inv.companyName}">
          <p>
            <strong><c:out value="${inv.invitedByName}"/></strong> invited you to join
            <strong><c:out value="${inv.companyName}"/></strong> as
            <c:out value="${inv.role eq 'admin' ? 'an admin' : 'a member'}"/>.
          </p>
          <form class="invite-actions" method="post" action="${pageContext.request.contextPath}/invites">
            <input type="hidden" name="invite" value="${inv.inviteId}">
            <input type="hidden" name="companyName" value="<c:out value='${inv.companyName}'/>">
            <button class="pill pill-glow pill-sm" type="submit" name="action" value="accept">Join</button>
            <button class="pill pill-ghost pill-sm" type="submit" name="action" value="decline">Decline</button>
          </form>
        </section>
      </c:forEach>
    </c:if>

    <c:if test="${empty error and not viewer.inCompany}">
      <section class="no-company">
        <h2>You're not in a company yet</h2>
        <p>
          Create one to start recording decisions with your team, or ask a company admin to invite you.
          <c:choose>
            <c:when test="${viewer.allowInvites}">Invites are on, so they'll appear here.</c:when>
            <c:otherwise>Invites are off; you can turn them on in <a href="${pageContext.request.contextPath}/settings">Settings</a>.</c:otherwise>
          </c:choose>
        </p>
        <a class="pill pill-glow" href="${pageContext.request.contextPath}/companies/new">Create a company</a>
      </section>
    </c:if>

    <c:if test="${empty error and viewer.inCompany}">
      <%-- Proposed decisions on teams the viewer leads or approves for. --%>
      <section class="dash-review" aria-labelledby="reviewHeading">
        <h2 id="reviewHeading">Needs your review</h2>
        <c:choose>
          <c:when test="${empty review}">
            <p class="dash-empty">Nothing is waiting on you. Proposed decisions on teams you lead or approve will show up here.</p>
          </c:when>
          <c:otherwise>
            <ul class="review-list">
              <c:forEach var="d" items="${review}">
                <li class="review-card">
                  <p class="feed-project"><c:out value="${d.projectName}"/></p>
                  <p class="review-title"><c:out value="${d.title}"/></p>
                  <p class="feed-meta"><span><c:out value="${d.decidedBy}"/></span><span>Proposed <c:out value="${d.shortDate}"/></span></p>
                </li>
              </c:forEach>
            </ul>
          </c:otherwise>
        </c:choose>
      </section>

      <div class="dash-grid">
        <section class="dash-feed" id="feed" aria-labelledby="feedHeading">
          <div class="section-bar">
            <h2 id="feedHeading">
              <c:choose>
                <c:when test="${not empty q}">Results for “<c:out value="${q}"/>” <span class="muted"><c:out value="${fn:length(feed)}"/> <c:out value="${fn:length(feed) == 1 ? 'decision' : 'decisions'}"/></span></c:when>
                <c:otherwise>Recent decisions</c:otherwise>
              </c:choose>
            </h2>
            <nav class="segmented" aria-label="Whose decisions">
              <c:url var="teamUrl" value="/dashboard"><c:if test="${not empty q}"><c:param name="q" value="${q}"/></c:if></c:url>
              <c:url var="mineUrl" value="/dashboard"><c:param name="view" value="mine"/><c:if test="${not empty q}"><c:param name="q" value="${q}"/></c:if></c:url>
              <a href="${teamUrl}" class="${view eq 'team' ? 'is-active' : ''}" ${view eq 'team' ? 'aria-current="page"' : ''}>Team</a>
              <a href="${mineUrl}" class="${view eq 'mine' ? 'is-active' : ''}" ${view eq 'mine' ? 'aria-current="page"' : ''}>Mine</a>
            </nav>
          </div>

          <c:choose>
            <c:when test="${empty feed and not empty q}">
              <p class="dash-empty">No decisions match “<c:out value="${q}"/>”. Try a shorter or different word.</p>
            </c:when>
            <c:when test="${empty feed and view eq 'mine'}">
              <p class="dash-empty">You haven't recorded any decisions yet.</p>
            </c:when>
            <c:when test="${empty feed}">
              <p class="dash-empty">No decisions on your teams yet.</p>
            </c:when>
            <c:otherwise>
              <ol class="feed">
                <c:forEach var="d" items="${feed}">
                  <li class="feed-item feed-${d.status}">
                    <p class="feed-project"><c:out value="${d.projectName}"/></p>
                    <p class="feed-title"><c:out value="${d.title}"/></p>
                    <c:if test="${not empty d.chosenOption}">
                      <p class="feed-chose">Chose <span><c:out value="${d.chosenOption}"/></span></p>
                    </c:if>
                    <c:if test="${not empty d.replacedBy}">
                      <p class="feed-replaced">Replaced by <span><c:out value="${d.replacedBy}"/></span></p>
                    </c:if>
                    <p class="feed-meta">
                      <span class="status status-${d.status}"><c:out value="${d.status}"/></span>
                      <span><c:out value="${d.decidedBy}"/></span>
                      <span><c:out value="${d.shortDate}"/></span>
                    </p>
                    <c:if test="${not empty d.tags}">
                      <ul class="tags">
                        <c:forEach var="t" items="${d.tags}">
                          <li class="tag tag-${t.color}"><c:out value="${t.name}"/></li>
                        </c:forEach>
                      </ul>
                    </c:if>
                  </li>
                </c:forEach>
              </ol>
            </c:otherwise>
          </c:choose>
        </section>

        <aside class="dash-projects" aria-labelledby="projectsHeading">
          <h2 id="projectsHeading">Your projects</h2>
          <c:choose>
            <c:when test="${empty projects}">
              <p class="dash-empty">You're not on any project teams yet.</p>
            </c:when>
            <c:otherwise>
              <ul class="project-list">
                <c:forEach var="p" items="${projects}">
                  <li class="project-row">
                    <div>
                      <p class="project-name"><c:out value="${p.name}"/></p>
                      <p class="project-meta">
                        <span class="project-status project-${p.status}"><c:out value="${p.status eq 'on_hold' ? 'on hold' : p.status}"/></span>
                        <span><c:out value="${p.teamName}"/></span>
                      </p>
                    </div>
                    <p class="project-count" title="Decisions recorded">
                      <c:out value="${p.decisionCount}"/>
                      <span><c:out value="${p.decisionCount == 1 ? 'decision' : 'decisions'}"/></span>
                    </p>
                  </li>
                </c:forEach>
              </ul>
            </c:otherwise>
          </c:choose>
        </aside>
      </div>
    </c:if>
  </main>
</div>

<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>
