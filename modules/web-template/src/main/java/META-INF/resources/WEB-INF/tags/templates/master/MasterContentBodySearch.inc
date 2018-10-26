<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterCommon.inc" %>

<!-- START::search form -->
<c:if test="${searchable}">
	<div class="row">
		<form class="form-inline" id="searchForm" name="searchForm"
		ng-submit="onSearch(1)" style="margin-bottom: 10px;"
		modelAttribute="c" novalidate="novalidate">
			<tiles:insertAttribute name="searchForm" />
			<div style="text-align: center;" class="search-actions">
				<button type="submit" class="btn btn-search"
				ng-disabled="isSearching()"
				title="<nlh4j:message code="button.search" />">
					<span class="btn-title"><nlh4j:message code="button.search" /></span>
				</button>
				<button type="reset" class="btn btn-reset"
				ng-disabled="isSearching()"
				ng-click="searchForm && searchForm.$setPristine(); searchForm && searchForm.$setUntouched(); onInit();"
				title="<nlh4j:message code="button.reset" />">
					<span class="btn-title"><nlh4j:message code="button.reset" /></span>
				</button>
				<c:if test="${showSearchActions}">
					<tiles:insertAttribute name="searchFormActions" />
				</c:if>
			</div>
		</form>
	</div>
</c:if>
<!-- END::search form -->
