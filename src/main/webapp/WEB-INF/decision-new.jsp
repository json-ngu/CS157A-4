<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Create Decision | MindMerge"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>
    <div class="app">

        <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

        <main class="app-main manage">

            <header class="manage-head">
                <h1>Create Decision</h1>
            </header>

            <section class="box">

                <c:if test="${not empty error}">
                    <p class="alert" role="alert">
                        <c:out value="${error}"/>
                    </p>
                </c:if>

                <form class="form"
                      method="post"
                      action="${pageContext.request.contextPath}/decisions/new">

                    <label for="projectId">Project:*</label>
                    <select id="projectId"
                            name="projectId"
                            class="select"
                            required>

                        <option value="">Choose a project</option>

                        <c:forEach var="project" items="${projects}">
                            <option value="${project.projectId}">
                                <c:out value="${project.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                    
                    <label for="title">Title:*</label>
                    <input id="title"
                           name="title"
                           type="text"
                           required
                           maxlength="220">

                    <label for="context">Context:*</label>
                    <textarea id="context"
                              name="context"
                              required></textarea>
                    
                    <h2 class="options-heading">Options</h2>

                    <div id="options-container">
                        <p id="option-error"
                            class="alert"
                            role="alert"
                            aria-live="assertive"
                            tabindex="-1"
                            hidden>
                        </p>

                        <section class="option-box">
                            <h3>Option 1</h3>

                            <label>Name:*</label>
                            <input name="optionLabel"
                                type="text"
                                required>

                            <label>Description:</label>
                            <textarea name="optionDescription"></textarea>

                            <button type="button"
                                    class="remove-option">
                                Remove option
                            </button>
                        </section>

                        <section class="option-box">
                            <h3>Option 2</h3>

                            <label>Name:*</label>
                            <input name="optionLabel"
                                type="text"
                                required>

                            <label>Description:</label>
                            <textarea name="optionDescription"></textarea>

                            <button type="button"
                                    class="remove-option">
                                Remove option
                            </button>
                        </section>

                    </div>

                    <button type="button"
                            id="add-option"
                            class="pill pill-ghost">
                        Add option
                    </button>
                    <button class="pill pill-glow pill-block"
                            type="submit">
                        Create Decision
                    </button>

                </form>

            </section>

        </main>

    </div>
</body>
<script src="${pageContext.request.contextPath}/js/decision-options.js"></script>
</html>