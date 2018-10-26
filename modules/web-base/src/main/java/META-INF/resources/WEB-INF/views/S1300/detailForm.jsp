<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<spring:message var="f_code" code="${pageModule}.detail.code" />
<spring:message var="f_name" code="${pageModule}.detail.name" />
<spring:message var="f_name_key" code="${pageModule}.detail.name.key" />
<spring:message var="f_name_key_title" code="${pageModule}.detail.name.key.title" />
<spring:message var="f_order" code="${pageModule}.detail.order" />
<spring:message var="f_enabled" code="${pageModule}.detail.enabled" />
<spring:message var="f_description" code="${pageModule}.detail.description" />

<nlh4jTags:Stylesheet>
.input-label-control-group .form-control {
	width: 60%;
}
</nlh4jTags:Stylesheet>

<!-- START::user identity -->
<input type="hidden" id="id" name="id" ng-model="model.func.id" />
<!-- END::user identity -->

<!-- START::Function code -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${f_code}" />
		<label class="control-label required" style="width: 120px;">${arguments}</label>
		<input type="text" class="form-control"
		id="code" name="code"
		placeholder="${arguments}"
		ng-model="model.func.code"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		maxlength="50"
		ng-required="true"
		ng-trim="true"
		ng-special-chars
		<c:if test="${editable && isNew}">
		ng-unique
		ng-unique-action="/system/function/checkUnique"
		ng-unique-param="onUniqueParams"
		</c:if>
		data-tooltip="{{(detailForm.code.$error.required && (detailForm.code.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${arguments}" />' : (detailForm.code.$error.pattern && (detailForm.code.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_015" />' : (detailForm.code.$error.unique && (detailForm.code.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_007" arguments="${arguments}" />' : ''}}"
		data-tooltip-placement="top"
		data-tooltip-trigger="mouseenter,hover,click,focus"
		>
		<c:if test="${editable && isNew}">
			<div
				class="validate-waiting"
				style="right: 30px"
				data-ng-show="detailForm.code.$pending.unique && (detailForm.code.$touched || detailForm.$submitted)">
				<i class="fa-li fa fa-spinner fa-spin"></i>
			</div>
		</c:if>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Function code -->

<!-- START::Function name -->
<div class="row">
	<div class="input-label-control-group col-sm-4">
		<c:set var="arguments" value="${f_name}" />
		<label class="control-label required" style="width: 120px;">${arguments}</label>
		<input type="text"
		class="form-control"
		id="name" name="name"
		placeholder="${arguments}"
		ng-model="model.func.name"
		<c:if test="${!editable || !isNew}">
		ng-readonly="true"
		</c:if>
		maxlength="255"
		ng-required="true"
		ng-trim="true"
		data-tooltip="{{(detailForm.name.$error.required && (detailForm.name.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${arguments}" />' : ''}}"
		data-tooltip-placement="top"
		data-tooltip-trigger="mouseenter,hover,click,focus"
		>
	</div>
	<div class="input-label-control-group col-sm-4">
		<c:set var="arguments" value="${f_name_key}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="text"
		class="form-control"
		id="lang_key" name="lang_key"
		placeholder="${arguments}"
		ng-model="model.func.langKey"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		maxlength="255"
		ng-trim="true"
		title="${f_name_key_title}"
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Function name -->

<!-- START::Function order -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${f_order}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="text" class="form-control"
		id="funcOrder" name="funcOrder"
		placeholder="${arguments}"
		ng-model="model.func.funcOrder"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		maxlength="2"
		ng-trim="true"
		data-only-number
		data-allow-empty="true"
		data-hide-grouping="false"
		data-allow-negative="false"
		data-allow-decimal="false"
		data-positive-numbers="2"
		data-ng-greater-than
		data-ng-greater-equals="false"
		data-ng-greater-compare="0"
		data-tooltip="{{(detailForm.vat.$error.greaterThan && (detailForm.vat.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_017" arguments="0" />' : ''}}"
		data-tooltip-placement="top"
		data-tooltip-trigger="mouseenter,hover,click,focus"
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Function order -->

<!-- START::Function enabled -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${f_enabled}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="checkbox"
		class="form-control"
		style="margin: 9px 0 0 0"
		id="enabled" name="enabled"
		ng-model="model.func.enabled"
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Function enabled -->

<!-- START::Function description -->
<div class="row">
	<div class="input-label-control-group col-sm-7">
		<c:set var="arguments" value="${f_description}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<textarea
		class="form-control"
		style="width: 80%"
		id="description" name="description"
		placeholder="${arguments}"
		ng-model="model.func.description"
		ng-trim="true"
		rows="7"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		></textarea>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Function description -->


