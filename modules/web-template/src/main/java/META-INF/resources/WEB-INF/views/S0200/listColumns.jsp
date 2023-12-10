<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%-- START: System Administrator --%>
<c:if test="${nlh4jBase:isSystemAdmin()}">
<c:if test="${company == null}">
<th type="no" width="5%">#</th>
<th width="10%"><spring:message code="${pageModule}.columns.user.name" /></th>
<th width="10%"><spring:message code="${pageModule}.columns.company.code" /></th>
<th width="15%"><spring:message code="${pageModule}.columns.company.name" /></th>
<th width="10%"><spring:message code="${pageModule}.columns.group.code" /></th>
<th width="15%"><spring:message code="${pageModule}.columns.group.name" /></th>
<th width="10%"><spring:message code="${pageModule}.columns.user.email" /></th>
<th width="5%" type="enum"><spring:message code="${pageModule}.columns.user.language" /></th>
<th width="5%" type="enum"><spring:message code="${pageModule}.columns.user.enabled" /></th>
<th width="5%" type="enum"><spring:message code="${pageModule}.columns.user.sysadmin" /></th>
<th width="10%" type="date"><spring:message code="${pageModule}.columns.user.expired.at" /></th>
</c:if>
<c:if test="${company != null}">
<th type="no" width="5%">#</th>
<th width="10%"><spring:message code="${pageModule}.columns.user.name" /></th>
<th width="10%"><spring:message code="${pageModule}.columns.group.code" /></th>
<th width="20%"><spring:message code="${pageModule}.columns.group.name" /></th>
<th width="25%"><spring:message code="${pageModule}.columns.user.email" /></th>
<th width="10%" type="enum"><spring:message code="${pageModule}.columns.user.language" /></th>
<th width="5%" type="enum"><spring:message code="${pageModule}.columns.user.enabled" /></th>
<th width="5%" type="enum"><spring:message code="${pageModule}.columns.user.sysadmin" /></th>
<th width="10%" type="date"><spring:message code="${pageModule}.columns.user.expired.at" /></th>
</c:if>
</c:if>
<%-- END: System Administrator --%>
<%-- START: Normal User --%>
<c:if test="${!nlh4jBase:isSystemAdmin()}">
<th type="no" width="5%">#</th>
<th width="15%"><spring:message code="${pageModule}.columns.user.name" /></th>
<th width="10%"><spring:message code="${pageModule}.columns.group.code" /></th>
<th width="20%"><spring:message code="${pageModule}.columns.group.name" /></th>
<th width="25%"><spring:message code="${pageModule}.columns.user.email" /></th>
<th width="10%" type="enum"><spring:message code="${pageModule}.columns.user.language" /></th>
<th width="5%" type="enum"><spring:message code="${pageModule}.columns.user.enabled" /></th>
<th width="10%" type="date"><spring:message code="${pageModule}.columns.user.expired.at" /></th>
</c:if>
<%-- END: Normal User --%>
