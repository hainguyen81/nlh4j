<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterActionCommon.inc" %>

<%-- START: Content --%>
<div class="panel panel-primary action-content">
	<form id="${form}" name="${form}" method="POST"
	class="form-horizontal" onsubmit="javascript:void(0);"
	ng-submit="onSave($event)"
	action="${action}" modelAttribute="e" novalidate="novalidate"
	reference-form-controller
	prevent-keys="${detailFormPreventKeys}">

		<%-- START: Content Header --%>
		<%@ include file="/WEB-INF/tags/templates/master/MasterActionContentHeader.inc" %>
		<%-- END: Content Header --%>

		<!-- FORM BODY/FIELDS -->
		<div class="panel-body">
			<tiles:insertAttribute name="detailForm" ignore="false" />
		</div>

		<%-- START: Content Footer --%>
		<%@ include file="/WEB-INF/tags/templates/master/MasterActionContentFooter.inc" %>
		<%-- END: Content Footer --%>
	</form>
</div>
<%-- END: Content --%>
