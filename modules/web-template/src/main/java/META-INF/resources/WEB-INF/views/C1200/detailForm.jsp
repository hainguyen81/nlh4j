<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<input type="hidden" name="id" ng-value="model.id" />
<input type="hidden" name="username" ng-value="model.username" />
<div class="form-group col-sm-10">
	<spring:message var="arguments" code="${page}.password" />
	<div class="form-group">
		<label class="control-label required-field col-sm-2">${arguments}</label>
		<div class="col-sm-6">
			<input
				type="password"
				class="form-control"
				name="currentPassword"
				data-ng-model="model.currentPassword"
				required="required"
				ng-minlength="${minPassLength}"
				ng-special-chars
				data-tooltip-placement="top"
				data-tooltip-trigger="mouseenter,hover,click,focus"
				data-tooltip="{{(detailForm.currentPassword.$error.required && (detailForm.currentPassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${arguments}" />' : (detailForm.currentPassword.$error.pattern && (detailForm.currentPassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_015" />' : (detailForm.currentPassword.$error.minlength && (detailForm.currentPassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_014" />' : ''}}"
				>
		</div>
	</div>
	<spring:message var="arguments" code="${page}.password.new" />
	<div class="form-group" style="margin-top: 3px;">
		<label class="control-label required-field col-sm-2">${arguments}</label>
		<div class="col-sm-6">
			<input
				type="password"
				class="form-control"
				name="newPassword"
				data-ng-model="model.newPassword"
				required="required"
				ng-minlength="${minPassLength}"
				ng-special-chars
				ng-keyup="onCheckMatch(0)"
				ng-blur="onCheckMatch(0)"
				data-tooltip-placement="top"
				data-tooltip-trigger="mouseenter,hover,click,focus"
				data-tooltip="{{(detailForm.newPassword.$error.required && (detailForm.newPassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${arguments}" />' : (detailForm.newPassword.$error.pattern && (detailForm.newPassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_015" />' : (detailForm.newPassword.$error.minlength && (detailForm.newPassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_014" />' : (detailForm.newPassword.$error.dontMatch && (detailForm.newPassword.$touched || detailForm.$submitted)) ? '<spring:message code="C1202_MSG_ID_ERR_001" />' : ''}}"
				>
		</div>
	</div>
	<spring:message var="arguments" code="${page}.password.retype" />
	<div class="form-group" style="margin-top: 4px;">
		<label class="control-label required-field col-sm-2">${arguments}</label>
		<div class="col-sm-6">
			<input
				type="password"
				class="form-control"
				name="retypePassword"
				required="required"
				data-ng-model="model.retypePassword"
				ng-minlength="${minPassLength}"
				ng-special-chars
				ng-keyup="onCheckMatch(1)"
				ng-blur="onCheckMatch(1)"
				data-tooltip-placement="top"
				data-tooltip-trigger="mouseenter,hover,click,focus"
				data-tooltip="{{(detailForm.retypePassword.$error.required && (detailForm.retypePassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${arguments}" />' : (detailForm.retypePassword.$error.pattern && (detailForm.retypePassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_015" />' : (detailForm.retypePassword.$error.minlength && (detailForm.retypePassword.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_014" />' : (detailForm.retypePassword.$error.dontMatch && (detailForm.retypePassword.$touched || detailForm.$submitted)) ? '<spring:message code="C1202_MSG_ID_ERR_001" />' : ''}}"
				>
		</div>
	</div>
</div>
