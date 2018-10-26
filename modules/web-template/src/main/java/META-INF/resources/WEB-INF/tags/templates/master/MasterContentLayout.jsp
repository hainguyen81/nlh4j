<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterCommon.inc" %>

<%-- <%@ attribute name="module" required="true" type="String" %>
<%@ attribute name="page" required="true" type="String" %>
<%@ attribute name="controllerMapping" required="true" type="String" %>
<%@ attribute name="searchForm" required="false" type="String" %>
<%@ attribute name="searchFormActions" required="false" type="String" %>
<%@ attribute name="listColumns" required="false" type="String" %>
<%@ attribute name="listItems" required="false" type="String" %>
<%@ attribute name="showBaseActions" required="false" type="Boolean" %>
<%@ attribute name="showPagination" required="false" type="Boolean" %> --%>

<%-- START: Master Script --%>
<%@ include file="/WEB-INF/tags/templates/master/MasterScriptCommon.inc" %>
<%@ include file="/WEB-INF/tags/templates/master/MasterAjaxScriptCommon.inc" %>
<%-- END: Master Script --%>

<%-- START: Module Script --%>
<nlh4jTags:Css src="${pagePath}.css" />
<nlh4jTags:Js src="${pagePath}.js" />
<%-- END: Module Script --%>

<div id="${pageName}">
	<%-- START: Content --%>
	<%@ include file="/WEB-INF/tags/templates/master/MasterContent.inc" %>
	<%-- END: Content --%>
</div>
