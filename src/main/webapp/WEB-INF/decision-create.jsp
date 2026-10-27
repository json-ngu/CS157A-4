<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create decision | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="app">
  <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

  <main class="app-main manage">
    <header class="manage-head">
      <div>
        <h1>Create decision</h1>
        <p class="dash-sub">Describe the decision, the options considered and the option you recommend. A team lead or approver will review and approve it.</p>
      </div>
    </header>

    <c:choose>
      <c:when test="${empty projects and empty error}">
        <section class="box" aria-labelledby="noProjectsHeading">
          <h2 id="noProjectsHeading">No projects available</h2>
          <p class="panel-note">You are not a member of any team with a project. Decisions are recorded within a project.</p>
        </section>
      </c:when>
      <c:otherwise>
        <section class="box">
          <c:if test="${not empty error}">
            <p class="alert" role="alert"><c:out value="${error}"/></p>
          </c:if>

          <form class="form decision-form" method="post" action="${pageContext.request.contextPath}/decisions/create">
            <label for="projectId">Project</label>
            <select id="projectId" class="select" name="projectId" required>
              <option value="">Select a project</option>
              <c:forEach var="p" items="${projects}">
                <option value="${p.projectId}" ${p.projectId == projectId ? 'selected' : ''}><c:out value="${p.name}"/> (<c:out value="${p.teamName}"/>)</option>
              </c:forEach>
            </select>

            <label for="title">Title</label>
            <input id="title" name="title" type="text" required maxlength="220"
                   placeholder="For example: Which database should store decision records?" value="<c:out value='${title}'/>">

            <label for="context">Context</label>
            <textarea id="context" name="context" required maxlength="10000"
                      placeholder="Describe the circumstances that require a decision and the relevant constraints."><c:out value="${context}"/></textarea>

            <fieldset class="options">
              <legend class="options-heading">Options</legend>
              <p class="panel-note">List between two and ten options, then select the one you recommend.</p>

              <div id="optionList">
                <c:forEach var="o" items="${options}" varStatus="s">
                  <section class="option-box">
                    <h3>Option <c:out value="${s.count}"/></h3>
                    <label for="optionLabel${s.count}">Name</label>
                    <input id="optionLabel${s.count}" name="optionLabel" type="text" required maxlength="220" value="<c:out value='${o.label}'/>">
                    <label for="optionDescription${s.count}">Description <span class="optional">(optional)</span></label>
                    <textarea id="optionDescription${s.count}" name="optionDescription" maxlength="10000"><c:out value="${o.description}"/></textarea>
                    <label class="select-option">
                      <input type="radio" name="selected" value="${s.count}" required ${s.count == selected ? 'checked' : ''}>
                      <span>Select this option</span>
                    </label>
                    <button class="remove-option" type="button" hidden>Remove option</button>
                  </section>
                </c:forEach>
              </div>

              <button id="addOption" class="pill pill-ghost pill-sm add-option" type="button" hidden>Add option</button>
            </fieldset>

            <label for="rationale">Rationale</label>
            <textarea id="rationale" name="rationale" required maxlength="10000"
                      placeholder="Explain why the selected option is preferred and which trade-offs are accepted."><c:out value="${rationale}"/></textarea>

            <button class="pill pill-glow pill-block" type="submit">Submit for approval</button>
          </form>
        </section>
      </c:otherwise>
    </c:choose>
  </main>
</div>
<script src="${pageContext.request.contextPath}/js/decision-options.js"></script>
</body>
</html>
