<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterCommon.inc" %>

<!-- START::page body -->
<c:if test="${showBody}">
	<div class="panel-body">
		<%-- START: Content Body Search Form --%>
		<%@ include file="/WEB-INF/tags/templates/master/MasterContentBodySearch.inc" %>
		<%-- END: Content Body Search Form --%>

		<%-- START: Content Body Search Result --%>
		<%@ include file="/WEB-INF/tags/templates/master/MasterContentBodySearchResult.inc" %>
		<%-- END: Content Body Search Result --%>

		<%-- START: Content Body Extra --%>
		<c:if test="${fn:length(bodyExtra) > 0}">
			<tiles:insertAttribute name="bodyExtra" />
		</c:if>
		<%-- END: Content Body Extra --%>
	</div>
</c:if>
<!-- END::page body -->
