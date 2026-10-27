<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create a company | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="app">
  <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

  <main class="app-main manage">
    <header class="manage-head">
      <div>
        <h1>Create a company</h1>
        <p class="dash-sub">You will be the owner of the new company. You can then invite members and create teams.</p>
      </div>
    </header>

    <section class="box narrow">
      <c:if test="${not empty error}">
        <p class="alert" role="alert"><c:out value="${error}"/></p>
      </c:if>
      <form class="form" method="post" action="${pageContext.request.contextPath}/companies/new">
        <label for="companyName">Company name</label>
        <input id="companyName" name="name" type="text" required maxlength="120" value="<c:out value='${name}'/>">
        <button class="pill pill-glow pill-block" type="submit">Create company</button>
      </form>
    </section>
  </main>
</div>
</body>
</html>
