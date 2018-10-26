<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterCommon.inc" %>

<!-- START::view header -->
<div class="panel-heading">
	<c:if test="${fn:length(headerExtActions) > 0}">
		<div class="panel-title pull-left">
			<i class="${moduleClass}"></i>&nbsp;<nlh4j:message code="${pageModule}.title" />
		</div>
		<tiles:insertAttribute name="headerExtActions" ignore="true" />
	</c:if>
	<c:if test="${fn:length(headerExtActions) <= 0}">
		<i class="${moduleClass}"></i>&nbsp;<nlh4j:message code="${pageModule}.title" />
	</c:if>
	<div class="pull-right">
		<c:if test="${fn:length(headerActions) > 0}">
			<tiles:insertAttribute name="headerActions" ignore="true" />
		</c:if>
		<c:if test="${showBaseHeaderActions}">
			<c:if test="${insertable}">
				<c:set var="createUrl" value="${controllerMapping}/create" />
				<button type="button" class="btn btn-xs btn-create"
				ng-disabled="isSearching()"
				ng-click="go('${createUrl}')"
				title="<nlh4j:message code="button.create" />">
					<span class="btn-title"><nlh4j:message code="button.create" /></span>
				</button>
			</c:if>
			<c:if test="${showBaseHeaderBackAction}">
				<c:url var="backUrl" value="/dashboard" />
				<button type="button" class="btn btn-xs btn-back"
				ng-disabled="isSearching()"
				ng-click="go('${backUrl}')"
				title="<nlh4j:message code="button.back" />">
					<span class="btn-title"><nlh4j:message code="button.back" /></span>
				</button>
			</c:if>
		</c:if>
	</div>
</div>
<!-- END::view header -->
