<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/templates/master/MasterActionCommon.inc" %>

<!-- FORM HEADER -->
<div class="panel-heading">
	<c:if test="${fn:length(detailExtActions) > 0}">
		<div class="panel-title pull-left">
			<i class="${moduleClass}"></i>&nbsp;${formTitle}
		</div>
		<tiles:insertAttribute name="detailExtActions" ignore="true" />
	</c:if>
	<c:if test="${fn:length(detailExtActions) <= 0}">
		<i class="${moduleClass}"></i>&nbsp;${formTitle}
	</c:if>
	<div class="pull-right">
		<c:if test="${fn:length(detailActions) > 0}">
			<tiles:insertAttribute name="detailActions" ignore="true" />
		</c:if>
		<c:if test="${showBaseHeaderActions}">
			<c:if test="${editable}">
				<button class="btn btn-xs btn-reset round" type="reset"
				data-ng-disabled="performAction() == true"
				data-ng-click="detailForm && detailForm.$setPristine(); detailForm && detailForm.$setUntouched(); onInit();"
				title="<nlh4j:message code="button.reset" />">
					<span class="btn-title"><nlh4j:message code="button.reset" /></span>
				</button>
				<button class="btn btn-xs btn-save round"
				id="btnSave" type="submit"
				data-ng-disabled="performAction() == true"
				data-success="${successMsg}"
				<c:if test="${!isNew}">
					data-confirmation="<nlh4j:message code="COM_MSG_ID_INF_009" />"
					data-confirm-title="${formTitle}"
					data-confirm-ok="<nlh4j:message code="button.yes" />"
					data-confirm-cancel="<nlh4j:message code="button.no" />"
				</c:if>
				title="<nlh4j:message code="button.save" />"
				><span class="btn-title"><nlh4j:message code="button.save" /></span></button>
			</c:if>
			<c:if test="${showBaseHeaderBackAction}">
				<c:url var="backUrl" value="${controllerMapping}" />
				<a href="${backUrl}" class="btn btn-xs btn-back"
				title="<nlh4j:message code="button.back" />">
					<span class="btn-title"><nlh4j:message code="button.back" /></span>
				</a>
			</c:if>
		</c:if>
	</div>
</div>
