<%@ include file="/WEB-INF/tags/Common.inc" %>

<%-- START: Replace angular module and controller for new Ajax content --%>
<nlh4jTags:Script>
	document.documentElement.setAttribute('ng-app', '${pageModule}');
	document.body.setAttribute('ng-controller', '${controller}');
	document.body.setAttribute('ng-init', 'onInit()');
	document.body.setAttribute('ng-preinit', '');
</nlh4jTags:Script>
<%-- END: Replace angular module and controller for new Ajax content --%>