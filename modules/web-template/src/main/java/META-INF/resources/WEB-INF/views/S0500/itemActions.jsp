<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<c:if test="${(insertable || updatable)}">
	<button class="btn btn-xs btn-assign-company"
	type="button" ng-click="onAssignCompany(data)"
	ng-show="data.enabled && !data.common && data.visibled && !data.service"
	title="<spring:message code="button.assign.company" />"></button>
</c:if>

