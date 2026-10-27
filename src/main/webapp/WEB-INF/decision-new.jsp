<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="New decision | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="app">
  <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

  <main class="app-main manage">
    <header class="manage-head">
      <div>
        <h1>New decision</h1>
        <p class="dash-sub">Record the question and the options on the table. Leads and approvers review it next.</p>
      </div>
    </header>

    <c:choose>
      <c:when test="${empty projects and empty error}">
        <p class="dash-empty">You're not on a team with a project yet, so there's nowhere to record a decision.</p>
      </c:when>
      <c:otherwise>
        <section class="box">
          <c:if test="${not empty error}">
            <p class="alert" role="alert"><c:out value="${error}"/></p>
          </c:if>

          <form class="form decision-form" method="post" action="${pageContext.request.contextPath}/decisions/new">
            <label for="projectId">Project</label>
            <select id="projectId" class="select" name="projectId" required>
              <option value="">Choose a project</option>
              <c:forEach var="p" items="${projects}">
                <option value="${p.projectId}" ${p.projectId == projectId ? 'selected' : ''}><c:out value="${p.name}"/> (<c:out value="${p.teamName}"/>)</option>
              </c:forEach>
            </select>

            <label for="title">Title</label>
            <input id="title" name="title" type="text" required maxlength="220"
                   placeholder="e.g. Which database should store decisions?" value="<c:out value='${title}'/>">

            <label for="context">Context</label>
            <textarea id="context" name="context" required
                      placeholder="What prompted this decision, and what constraints matter?"><c:out value="${context}"/></textarea>

            <fieldset class="options">
              <legend class="options-heading">Options</legend>
              <p class="panel-note">List at least two. You'll pick one when the decision is made.</p>

              <div id="optionList">
                <c:forEach var="o" items="${options}" varStatus="s">
                  <section class="option-box">
                    <h3>Option <c:out value="${s.count}"/></h3>
                    <label for="optionLabel${s.count}">Name</label>
                    <input id="optionLabel${s.count}" name="optionLabel" type="text" required maxlength="220" value="<c:out value='${o.label}'/>">
                    <label for="optionDescription${s.count}">Description <span class="optional">(optional)</span></label>
                    <textarea id="optionDescription${s.count}" name="optionDescription"><c:out value="${o.description}"/></textarea>
                    <button class="remove-option" type="button" hidden>Remove option</button>
                  </section>
                </c:forEach>
              </div>

              <button id="addOption" class="pill pill-ghost pill-sm add-option" type="button" hidden>Add option</button>
            </fieldset>

            <button class="pill pill-glow pill-block" type="submit">Record decision</button>
          </form>
        </section>
      </c:otherwise>
    </c:choose>
  </main>
</div>
<script src="${pageContext.request.contextPath}/js/decision-options.js"></script>
</body>
</html>
