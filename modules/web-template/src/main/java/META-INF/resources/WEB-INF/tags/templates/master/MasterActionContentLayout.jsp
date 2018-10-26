<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterActionCommon.inc" %>

<%-- <%@ attribute name="module" required="true" type="String" %>
<%@ attribute name="page" required="true" type="String" %>
<%@ attribute name="controllerMapping" required="true" type="String" %>
<%@ attribute name="detailForm" required="false" type="String" %> --%>

<%-- START: Master Script --%>
<%@ include file="/WEB-INF/tags/templates/master/MasterScriptCommon.inc" %>
<%@ include file="/WEB-INF/tags/templates/master/MasterAjaxScriptCommon.inc" %>
<%-- END: Master Script --%>

<%-- START: Module Script --%>
<nlh4jTags:Css src="${pagePath}.css" />
<nlh4jTags:Js src="${pagePath}.js" />
<%-- END: Module Script --%>

<div id="${pageName}" class="${cssClass}">
	<%-- START: Content --%>
	<%@ include file="/WEB-INF/tags/templates/master/MasterActionContent.inc" %>
	<%-- END: Master Script --%>
</div>
