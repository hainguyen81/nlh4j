<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<spring:message var="m_code" code="${pageModule}.detail.code" />
<spring:message var="m_name" code="${pageModule}.detail.name" />
<spring:message var="m_name_key" code="${pageModule}.detail.name.key" />
<spring:message var="m_name_key_title" code="${pageModule}.detail.name.key.title" />
<spring:message var="m_parent" code="${pageModule}.detail.parent" />
<spring:message var="m_parent_prompt" code="${pageModule}.detail.parent.prompt" />
<spring:message var="m_order" code="${pageModule}.detail.order" />
<spring:message var="m_enabled" code="${pageModule}.detail.enabled" />
<spring:message var="m_visibled" code="${pageModule}.detail.visibled" />
<spring:message var="m_service" code="${pageModule}.detail.service" />
<spring:message var="m_common" code="${pageModule}.detail.common" />
<spring:message var="m_url" code="${pageModule}.detail.url" />
<spring:message var="m_url_regex" code="${pageModule}.detail.url_regex" />
<spring:message var="m_css" code="${pageModule}.detail.css" />
<spring:message var="m_library" code="${pageModule}.detail.library" />
<spring:message var="m_description" code="${pageModule}.detail.description" />

<nlh4jTags:Stylesheet>
.input-label-control-group .form-control {
	width: 60%;
}
</nlh4jTags:Stylesheet>

<!-- START::user identity -->
<input type="hidden" id="id" name="id" ng-model="model.module.id" />
<!-- END::user identity -->

<!-- START::Module code -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_code}" />
		<label class="control-label required" style="width: 120px;">${arguments}</label>
		<input type="text" class="form-control"
		id="code" name="code"
		placeholder="${arguments}"
		ng-model="model.module.code"
		<c:if test="${!editable || !isNew}">
		ng-readonly="true"
		</c:if>
		maxlength="50"
		ng-required="true"
		ng-trim="true"
		ng-special-chars
		<c:if test="${editable && isNew}">
		ng-unique
		ng-unique-action="/system/module/checkUnique"
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
<!-- END::Module code -->

<!-- START::Module name -->
<div class="row">
	<div class="input-label-control-group col-sm-4">
		<c:set var="arguments" value="${m_name}" />
		<label class="control-label required" style="width: 120px;">${arguments}</label>
		<input type="text"
		class="form-control"
		id="name" name="name"
		placeholder="${arguments}"
		ng-model="model.module.name"
		<c:if test="${!editable}">
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
		<c:set var="arguments" value="${m_name_key}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="text"
		class="form-control"
		id="lang_key" name="lang_key"
		placeholder="${arguments}"
		ng-model="model.module.langKey"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		maxlength="255"
		ng-trim="true"
		title="${m_name_key_title}"
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Module name -->

<!-- START::Parent modules -->
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<c:set var="arguments" value="${m_parent}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<ui-select
		style="width: 70%"
		name="parentMod"
		data-ng-model="model.parent"
		data-allow-clear="true"
		data-placeholder="${m_parent_prompt}"
		theme="select2"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		>
			<ui-select-match data-placeholder="${m_parent_prompt}">
				<div class="input-group item">
					<div class="icon" title="{{$select.selected.name}}"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length">
						<i class="fa fa-braille"></i>
					</div>
					<div data-ng-bind-html="$select.selected.code | highlight: $select.search"
					class="code" title="{{$select.selected.code}}"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length"></div>
					<div data-ng-bind-html="$select.selected.name | highlight: $select.search"
					class="name" title="{{$select.selected.name}}"></div>
				</div>
			</ui-select-match>
			<ui-select-choices repeat="operator in model.parents | propsFilter: { filterKey: 'S0400-modules', expression: { code: $select.search, name: $select.search } }">
				<div class="input-group item"
				style="width: 100%; display: inline-block; margin-top: 5px; margin-left: {{(operator.depth - 1) * 15}}px">
					<div class="icon" title="{{operator.name}}"
					ng-if="operator.code && operator.code.length">
						<i class="fa fa-braille"></i>
					</div>
					<div data-ng-bind-html="operator.code | highlight: $select.search"
					class="code" title="{{operator.code}}"
					ng-if="operator.code && operator.code.length"></div>
					<div data-ng-bind-html="operator.name | highlight: $select.search"
					class="name" title="{{operator.name}}"></div>
				</div>
			</ui-select-choices>
		</ui-select>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Parent modules -->

<!-- START::Module order -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_order}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="text" class="form-control"
		id="modOrder" name="modOrder"
		placeholder="${arguments}"
		ng-model="model.module.modOrder"
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
<!-- END::Module order -->

<!-- START::Module enabled/common -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_enabled}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="checkbox"
		class="form-control"
		style="margin: 9px 0 0 0"
		id="enabled" name="enabled"
		ng-model="model.module.enabled"
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		ng-change="onEnableChanged()"
		>
	</div>
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_common}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="checkbox"
		class="form-control"
		style="margin: 9px 0 0 0"
		id="common" name="common"
		ng-model="model.module.common"
		<c:if test="${editable}">
		ng-disabled="!model.module.enabled"
		</c:if>
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Module enabled/common -->

<!-- START::Module service/visible -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_service}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="checkbox"
		class="form-control"
		style="margin: 9px 0 0 0"
		id="service" name="service"
		ng-model="model.module.service"
		<c:if test="${editable}">
		ng-disabled="!model.module.enabled"
		</c:if>
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
	</div>
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_visibled}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="checkbox"
		class="form-control"
		style="margin: 9px 0 0 0"
		id="visibled" name="visibled"
		ng-model="model.module.visibled"
		<c:if test="${editable}">
		ng-disabled="!model.module.enabled"
		</c:if>
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Module service/visible -->

<!-- START::Module url/regex -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_url}" />
		<label class="control-label" style="width: 120px;">${m_url}</label>
		<input type="text"
		class="form-control"
		id="mainUrl" name="mainUrl"
		placeholder="${arguments}"
		ng-model="model.module.mainUrl"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		maxlength="255"
		ng-trim="true"
		ng-unique
		ng-unique-action="/system/module/checkUniqueUrl"
		ng-unique-param="onUniqueUrlParams"
		data-tooltip="{{(detailForm.mainUrl.$error.unique && (detailForm.mainUrl.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_007" arguments="${arguments}" />' : ''}}"
		data-tooltip-placement="top"
		data-tooltip-trigger="mouseenter,hover,click,focus"
		ng-blur="onUrlChanged()"
		>
		<div
			class="validate-waiting"
			style="right: 30px"
			data-ng-show="detailForm.mainUrl.$pending.unique && (detailForm.mainUrl.$touched || detailForm.$submitted)">
			<i class="fa-li fa fa-spinner fa-spin"></i>
		</div>
	</div>
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_url_regex}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="text"
		class="form-control"
		id="urlRegex" name="urlRegex"
		placeholder="${arguments}"
		ng-model="model.module.urlRegex"
		ng-readonly="true"
		maxlength="255"
		ng-trim="true"
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Module url/regex -->

<!-- START::Module css/library -->
<div class="row">
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_css}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="text"
		class="form-control"
		id="css" name="css"
		placeholder="${arguments}"
		ng-model="model.module.css"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		maxlength="50"
		ng-trim="true"
		>
	</div>
	<div class="input-label-control-group col-sm-3">
		<c:set var="arguments" value="${m_library}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="text"
		class="form-control"
		id="library" name="library"
		placeholder="${arguments}"
		ng-model="model.module.library"
		ng-readonly="true"
		maxlength="50"
		ng-trim="true"
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Module css/library -->

<!-- START::Module description -->
<div class="row">
	<div class="input-label-control-group col-sm-7">
		<c:set var="arguments" value="${m_description}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<textarea
		class="form-control"
		style="width: 80%"
		id="description" name="description"
		placeholder="${arguments}"
		ng-model="model.module.description"
		ng-trim="true"
		rows="7"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		></textarea>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Module description -->


