<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="People | MindMerge"/>
<c:set var="pickerAction" value="/people"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>
<div class="app">
  <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

  <main class="app-main manage">
    <header class="manage-head">
      <div>
        <h1>People</h1>
        <p class="dash-sub"><c:out value="${company.name}"/></p>
      </div>
      <%@ include file="/WEB-INF/jspf/company-picker.jspf" %>
    </header>

    <%@ include file="/WEB-INF/jspf/notice.jspf" %>
    <c:if test="${not empty error}">
      <p class="alert" role="alert"><c:out value="${error}"/></p>
    </c:if>

    <c:if test="${empty error}">
      <c:set var="iAmOwner" value="${company.owner}"/>

      <section class="box" aria-labelledby="inviteHeading">
        <h2 id="inviteHeading">Invite someone</h2>
        <p class="panel-note">They need a MindMerge account. The invite appears on their dashboard to join or decline.</p>
        <form class="inline-form" method="post" action="${pageContext.request.contextPath}/people">
          <input type="hidden" name="company" value="${company.companyId}">
          <input type="hidden" name="action" value="invite">
          <label class="visually-hidden" for="inviteEmail">Email</label>
          <input id="inviteEmail" class="input" name="email" type="email" required maxlength="180" placeholder="name@example.com">
          <%-- Only owners can invite admins; admins invite members. --%>
          <c:choose>
            <c:when test="${iAmOwner}">
              <label class="visually-hidden" for="inviteRole">Role</label>
              <select id="inviteRole" class="select" name="role">
                <option value="member">Member</option>
                <option value="admin">Admin</option>
              </select>
            </c:when>
            <c:otherwise>
              <input type="hidden" name="role" value="member">
              <span class="muted">as a member</span>
            </c:otherwise>
          </c:choose>
          <button class="pill pill-glow" type="submit">Send invite</button>
        </form>

        <c:if test="${not empty invites}">
          <h3 class="subhead">Pending invites</h3>
          <ul class="row-list">
            <c:forEach var="inv" items="${invites}">
              <li class="row">
                <div>
                  <p class="row-title"><c:out value="${inv.userName}"/> <span class="muted"><c:out value="${inv.userEmail}"/></span></p>
                  <p class="row-meta">Invited as <c:out value="${inv.role}"/> by <c:out value="${inv.invitedByName}"/> on <c:out value="${inv.createdDate}"/></p>
                </div>
                <%-- Admin invites come from owners, so only owners can cancel them. --%>
                <c:if test="${iAmOwner or inv.role ne 'admin'}">
                  <form method="post" action="${pageContext.request.contextPath}/people">
                    <input type="hidden" name="company" value="${company.companyId}">
                    <input type="hidden" name="action" value="cancel">
                    <input type="hidden" name="invite" value="${inv.inviteId}">
                    <button class="pill pill-ghost pill-sm" type="submit">Cancel invite</button>
                  </form>
                </c:if>
              </li>
            </c:forEach>
          </ul>
        </c:if>
      </section>

      <section class="box" aria-labelledby="membersHeading">
        <h2 id="membersHeading">Members <span class="muted"><c:out value="${fn:length(members)}"/></span></h2>
        <c:if test="${not iAmOwner}">
          <p class="panel-note">Only owners can change company roles. You can set team roles on the Teams page.</p>
        </c:if>
        <table class="table">
          <thead>
            <tr>
              <th scope="col">Name</th>
              <th scope="col">Role</th>
              <th scope="col">Joined</th>
              <th scope="col"><span class="visually-hidden">Actions</span></th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="m" items="${members}">
              <%-- Company roles are owner-only. Admins may remove members, and anyone may leave. --%>
              <c:set var="isMe" value="${m.userId == sessionScope.user.userId}"/>
              <c:set var="canRemove" value="${iAmOwner or m.role eq 'member' or isMe}"/>
              <tr>
                <td data-label="Name">
                  <p class="row-title"><c:out value="${m.fullName}"/><c:if test="${m.userId == sessionScope.user.userId}"> <span class="muted">(you)</span></c:if><c:if test="${m.status eq 'invited'}"> <span class="status-badge">Invited</span></c:if><c:if test="${m.status eq 'deactivated'}"> <span class="status-badge is-off">Deactivated</span></c:if></p>
                  <p class="row-meta"><c:out value="${m.email}"/></p>
                </td>
                <td data-label="Role">
                  <c:choose>
                    <c:when test="${iAmOwner}">
                      <form class="role-form" method="post" action="${pageContext.request.contextPath}/people">
                        <input type="hidden" name="company" value="${company.companyId}">
                        <input type="hidden" name="action" value="role">
                        <input type="hidden" name="user" value="${m.userId}">
                        <label class="visually-hidden" for="role${m.userId}">Role for <c:out value="${m.fullName}"/></label>
                        <select id="role${m.userId}" class="select select-sm" name="role" data-autosubmit>
                          <option value="owner" ${m.role eq 'owner' ? 'selected' : ''}>Owner</option>
                          <option value="admin" ${m.role eq 'admin' ? 'selected' : ''}>Admin</option>
                          <option value="member" ${m.role eq 'member' ? 'selected' : ''}>Member</option>
                        </select>
                        <button class="pill pill-ghost pill-sm" type="submit">Save</button>
                      </form>
                    </c:when>
                    <c:otherwise><span class="role-chip"><c:out value="${m.role}"/></span></c:otherwise>
                  </c:choose>
                </td>
                <td data-label="Joined" class="muted"><c:out value="${m.joinedDate}"/></td>
                <td class="actions-cell">
                  <c:if test="${canRemove}">
                    <form method="post" action="${pageContext.request.contextPath}/people"
                          data-confirm="Remove ${fn:escapeXml(m.fullName)} from ${fn:escapeXml(company.name)}? Their decisions and comments are kept.">
                      <input type="hidden" name="company" value="${company.companyId}">
                      <input type="hidden" name="action" value="remove">
                      <input type="hidden" name="user" value="${m.userId}">
                      <button class="link-danger" type="submit"><c:out value="${isMe ? 'Leave' : 'Remove'}"/></button>
                    </form>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </section>
    </c:if>
  </main>
</div>
<script src="${pageContext.request.contextPath}/js/manage.js"></script>
</body>
</html>
