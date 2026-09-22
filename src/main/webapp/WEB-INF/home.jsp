<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>MindMerge</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <header class="site-header">
    <h1>MindMerge</h1>
    <p class="tagline">Remember why, not just what.</p>
  </header>

  <main>
    <section class="card">
      <h2>Decision memory for teams</h2>
      <p>
        MindMerge records the reasoning behind your team's decisions:
        the context, the options considered, and why the final choice was made.
      </p>
    </section>

    <section class="card">
      <h2>Recent decisions</h2>

      <c:choose>
        <c:when test="${empty decisions}">
          <p class="pending">No decisions found.</p>
        </c:when>
        <c:otherwise>
          <ul class="decision-list">
            <c:forEach var="d" items="${decisions}">
              <li class="decision">
                <h3><c:out value="${d.title}"/></h3>
                <p class="choice">Chose: <strong><c:out value="${d.chosenOption}"/></strong></p>
                <p class="why"><c:out value="${d.rationale}"/></p>
                <p class="meta"><c:out value="${d.decidedBy}"/> - ${d.decidedAt}</p>
              </li>
            </c:forEach>
          </ul>
        </c:otherwise>
      </c:choose>
    </section>

    <section class="card status">
      <h2>System status</h2>
      <p>Server time: <%= new java.util.Date() %></p>
      <p>Database:
        <c:choose>
          <c:when test="${dbStatus eq 'connected'}">
            <span class="ok">connected</span> - ${fn:length(decisions)} rows
          </c:when>
          <c:otherwise>
            <span class="pending"><c:out value="${dbStatus}"/></span>
          </c:otherwise>
        </c:choose>
      </p>
    </section>
  </main>

  <footer>
    <p>CS157A - Team 4 - Jason Nguyen, Andrew Khoi Luu, Hailey Liu</p>
  </footer>
</body>
</html>