<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="elementId" required="false"%>
<%@ attribute name="appModule" required="false"%>
<%@ attribute name="appController" required="false"%>
<%@ attribute name="fnPreInit" required="false"%>
<%@ attribute name="fnInit" required="false"%>
<%@ attribute name="cssClass" required="false"%>
<%@ attribute name="hideChat" required="false" %>
<%@ attribute name="hideNotification" required="false"%>
<%@ attribute name="hideScrollTop" required="false"%>
<%@ attribute name="useBarcode" required="false"%>
<%@ attribute name="barcodeLength" required="false"%>
<%@ attribute name="scanDuration" required="false"%>

<body ${nlh4j:angularBodyAttr(elementId, appModule, appController, fnPreInit, fnInit, cssClass)}
>
	<jsp:doBody />

	<%-- START::action spinner --%>
  	<div id="processBackdrop" class="main-backdrop" style="visibility: hidden"></div>
  	<div id="processBackdrop" class="main-backdrop-loading" style="visibility: hidden">
  		<nlh4j:message code="COM_MSG_ID_INF_000"/></div>
  	<div id="offlineBackdrop" class="main-backdrop-offline" style="visibility: hidden">
 		<img alt="${errorInternet}" src="${resourcePath}/image/offline.png" /></div>
  	<%-- END::action spinner --%>

	<%-- START::BARCODE --%>
	<c:if test="${useBarcode eq 'true' or useBarcode eq true}">
	<nlh4jTagsComp:GlyphiconBarcodeListener
	onScan="onBarcodeScan" barcodeLength="${barcodeLength}" scanDuration="${scanDuration}" />
	</c:if>
	<%-- END::BARCODE --%>

	<%-- START::SOCKET CHAT/NOTIFICATION --%>
	<c:if test="${!hideChat && nlh4j:isSocketChatProfile()}">
		<div id="chatIcon" class="chat-window" ng-chat>
			<i class="fa fa-weixin" style="font-size: 4em; display: inline-block; position: relative;" ng-random-text-color></i>
		</div>
	</c:if>
	<c:if test="${!hideNotification && nlh4j:isSocketNotificationProfile()}">
		<nlh4j:message code="application.notification" var="notificationTitle" />
		<div id="notificationViewer" class="notification-socket"
		ng-title="${notificationTitle}"
		ng-notification>&nbsp;</div>
	</c:if>
	<c:if test="${empty hideScrollTop || !hideScrollTop || (hideScrollTop ne 'true' && hideScrollTop ne '1')}">
		<div class="scroll-top"
		ng-window-scroll-top
		show-on-over=".content-wrapper .content">
			<i class="fa fa-chevron-up"></i>
		</div>
	</c:if>
	<%-- END::SOCKET CHAT/NOTIFICATION --%>
</body>