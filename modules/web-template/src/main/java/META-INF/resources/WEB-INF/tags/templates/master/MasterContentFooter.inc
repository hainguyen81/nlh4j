<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterCommon.inc" %>

<!-- START::pagination -->
<div class="panel-sub-footer">
	<c:if test="${showPagination}">
		<pagination boundary-links="true"
					previous-text="&lsaquo;"
					next-text="&rsaquo;"
					first-text="&laquo;"
					last-text="&raquo;"
					max-size = "5"
					ng-if="(getPagination() && !isNaN(getPagination().pageNumber) && getPagination().pageNumber > 0 && !isNaN(getPagination().limit) && getPagination().limit >= 0)"
					total-items="(getPagination() && isNaN(getPagination().totalRows) ? 0 : getPagination().totalRows)"
					ng-model="getPagination().pageNumber"
					items-per-page="(getPagination() && isNaN(getPagination().limit) ? -1 : getPagination().limit)"
					ng-change="onSearch(getPagination() && !isNaN(getPagination().pageNumber) && getPagination().pageNumber > 0 ? getPagination().pageNumber : 0)"/>
	</c:if>
</div>
<!-- END::pagination -->
