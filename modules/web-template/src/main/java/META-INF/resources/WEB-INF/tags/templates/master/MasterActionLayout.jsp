<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterActionCommon.inc" %>

<%-- <%@ attribute name="module" required="true" type="String" %>
<%@ attribute name="page" required="true" type="String" %>
<%@ attribute name="controllerMapping" required="true" type="String" %>
<%@ attribute name="detailForm" required="false" type="String" %> --%>

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
			<%@ include file="/WEB-INF/tags/templates/master/MasterActionContent.inc" %>
			<%-- END: Master Script --%>
		</nlh4jTags:Layout>
	</nlh4jTags:Body>
</nlh4jTags:Html>