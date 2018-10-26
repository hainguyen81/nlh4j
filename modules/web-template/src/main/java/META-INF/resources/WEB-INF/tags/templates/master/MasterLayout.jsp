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

<nlh4jTags:Html module="${pageModule}">
	<nlh4jTags:Head title="${pageModule}.title">
		<%-- START: Master Script --%>
		<%@ include file="/WEB-INF/tags/templates/master/MasterScriptCommon.inc" %>
		<%-- END: Master Script --%>

		<%-- START: Module Script --%>
		<nlh4jTags:Css src="${pageModule}/${pageName}.css" />
		<nlh4jTags:Js src="${pageModule}/${pageName}.js" />
		<%-- END: Module Script --%>
	</nlh4jTags:Head>
	<nlh4jTags:Body appController="${pageController}"
	fnInit="onInit()" cssClass="${pageName}"
	hideChat="${!nlh4j:isSocketChatProfile()}"
	hideNotification="${!nlh4j:isSocketNotificationProfile()}"
	useBarcode="${useBarcode}">
		<nlh4jTags:Layout>
			<%-- START: Content --%>
			<%@ include file="/WEB-INF/tags/templates/master/MasterContent.inc" %>
			<%-- END: Content --%>
		</nlh4jTags:Layout>
	</nlh4jTags:Body>
</nlh4jTags:Html>