<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<spring:message var="u_name" code="${pageModule}.detail.username" />
<spring:message var="u_password" code="${pageModule}.detail.password" />
<spring:message var="u_email" code="${pageModule}.detail.email" />
<spring:message var="u_enabled" code="${pageModule}.detail.enabled" />
<spring:message var="u_sysadmin" code="${pageModule}.detail.sysadmin" />
<spring:message var="u_description" code="${pageModule}.detail.description" />
<spring:message var="u_expired" code="${pageModule}.detail.expired" />
<spring:message var="u_company" code="${pageModule}.detail.company" />
<spring:message var="u_company_prompt" code="${pageModule}.detail.company.prompt" />
<spring:message var="u_role" code="${pageModule}.detail.role" />
<spring:message var="u_role_prompt" code="${pageModule}.detail.role.prompt" />
<spring:message var="u_employee" code="${pageModule}.detail.employee" />
<spring:message var="u_employee_prompt" code="${pageModule}.detail.employee.prompt" />

<!-- START::user identity -->
<input type="hidden" id="id" name="id" ng-model="model.user.id" />
<!-- END::user identity -->

<!-- START::User login name -->
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label required col-sm-3">${u_name}</label>
		<input type="text"
		class="form-control"
		style="width: 50%"
		id="username" name="username"
		placeholder="${u_name}"
		ng-model="model.user.username"
		<c:if test="${!editable || !isNew}">
		ng-readonly="true"
		</c:if>
		maxlength="50"
		ng-required="true"
		ng-trim="true"
		ng-special-chars
		<c:if test="${editable && isNew}">
		ng-unique
		ng-unique-action="/system/user/checkUnique"
		ng-unique-param="onUniqueParams"
		</c:if>
		data-tooltip="{{(detailForm.username.$error.required && (detailForm.username.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${u_name}" />' : (detailForm.username.$error.pattern && (detailForm.username.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_015" />' : (detailForm.username.$error.unique && (detailForm.username.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_007" arguments="${u_name}" />' : ''}}"
		data-tooltip-placement="top"
		data-tooltip-trigger="mouseenter,hover,click,focus"
		>
		<c:if test="${editable && isNew}">
			<div
				class="validate-waiting"
				data-ng-show="detailForm.username.$pending.unique && (detailForm.username.$touched || detailForm.username.$submitted)">
				<i class="fa-li fa fa-spinner fa-spin"></i>
			</div>
		</c:if>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::User login name -->
<!-- START::User password -->
<c:if test="${systemAdmin}">
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label required col-sm-3"
		ng-if="model.user.changepwd">${u_password}</label>
		<label class="control-label col-sm-3"
		ng-if="!model.user.changepwd">${u_password}</label>
		<input
			type="password"
			class="form-control"
			style="width: 65%"
			name="password"
			data-ng-model="model.user.password"
			ng-required="model.user.changepwd"
			ng-readonly="!model.user.changepwd"
			ng-minlength="${minPassLength}"
			ng-special-chars
			data-tooltip-placement="top"
			data-tooltip-trigger="mouseenter,hover,click,focus"
			data-tooltip="{{(detailForm.password.$error.required && (detailForm.password.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${u_password}" />' : (detailForm.password.$error.pattern && (detailForm.password.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_015" />' : (detailForm.password.$error.minlength && (detailForm.password.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_014" />' : ''}}"
			<c:if test="${!editable}">
			ng-readonly="true"
			</c:if>
			>
		<c:if test="${editable && !isNew}">
			<input class="form-control"
			style="margin: 9px 0 0 5px"
			type="checkbox" name="checkbox"
			ng-model="model.user.changepwd">
		</c:if>
	</div>
</div>
<div class="vspace-10"></div>
</c:if>
<!-- END::User password -->
<!-- START::User email -->
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label required col-sm-3">${u_email}</label>
		<input type="text"
		class="form-control"
		id="email" name="email"
		placeholder="${u_email}"
		ng-model="model.user.email"
		maxlength="255"
		ng-trim="true"
		ng-required="true"
		ng-valid-email
		data-tooltip="{{(detailForm.email.$error.required && (detailForm.email.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${u_email}" />' : (detailForm.email.$error.email && (detailForm.email.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_005" arguments="${u_email}" />' : ''}}"
		data-tooltip-placement="top"
		data-tooltip-trigger="mouseenter,hover,click,focus"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::User email -->
<!-- START::User enabled status -->
<div class="row"
ng-if="model.user && !model.user.currentUser">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label col-sm-3">${u_enabled}</label>
		<input type="checkbox"
		class="form-control"
		style="margin: 9px 0 0 0"
		id="enabled" name="enabled"
		ng-model="model.user.enabled"
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
	</div>
</div>
<div class="vspace-10"
ng-if="model.user && !model.user.currentUser"></div>
<!-- END::User enabled status -->
<c:if test="${systemAdmin}">
<!-- START::User system administrator status -->
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label col-sm-3">${u_sysadmin}</label>
		<input type="checkbox"
		class="form-control"
		style="margin: 9px 0 0 0"
		id="sysadmin" name="sysadmin"
		ng-model="model.user.sysadmin"
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::User system administrator status -->
<!-- START::User expiration date -->
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label col-sm-3">${u_expired}</label>
		<div class="input-group col-sm-3">
			<nlh4jTagsComp:GlyphiconDatePicker
			dtpModel="model.user.expiredAt"
			dtpOptions="calendar.dateOptions"
			dtpPattern="${cmnDateTimePattern}"
			dtpName="expiredAt"
			dtpCssClass="text-center"
			dtpShowWeeks="false"
			dtpStartingDay="0"
			dtpInitDate="calendar.init" 
			dtpAppendToBody="false"
			/>
		</div>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::User expiration date -->
</c:if>
<!-- START::User language -->
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label col-sm-3">${languageCaption}</label>
		<ui-select
		name="language"
		data-ng-model="model.user.language"
		data-allow-clear="true"
		data-placeholder="${languagePrompt}"
		theme="select2"
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
			<ui-select-match data-placeholder="${languagePrompt}">
				<div class="input-group item">
					<div class="icon {{$select.selected.icon}}"
					title="{{$select.selected.name}}"
					style="background-position: 0"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length">&nbsp;</div>
					<div data-ng-bind-html="$select.selected.name | highlight: $select.search"
					class="name" title="{{$select.selected.name}}"></div>
				</div>
			</ui-select-match>
			<ui-select-choices repeat="operator in data.pageInfo.languages | propsFilter: { filterKey: 'S0200-languages', expression: { code: $select.search, name: $select.search } }">
				<div class="input-group item">
					<div class="icon {{operator.icon}}"
					title="{{operator.name}}"
					style="background-position: 0"
					ng-if="operator.code && operator.code.length">&nbsp;</div>
					<div data-ng-bind-html="operator.name | highlight: $select.search"
					class="name" title="{{operator.name}}"></div>
				</div>
			</ui-select-choices>
		</ui-select>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::User language -->
<!-- START::User roles -->
<div class="row"
ng-if="!model.disabledRole">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label col-sm-3">${u_role}</label>
		<ui-select
		style="width: 70%"
		name="role"
		data-ng-model="model.user.role"
		data-allow-clear="true"
		data-placeholder="${u_role_prompt}"
		theme="select2"
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
			<ui-select-match data-placeholder="${u_role_prompt}">
				<div class="input-group item">
					<div class="icon" title="{{$select.selected.name}}"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length">
						<i class="fa fa-universal-access"></i>
					</div>
					<div data-ng-bind-html="$select.selected.code | highlight: $select.search"
					class="code" title="{{$select.selected.code}}"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length"></div>
					<div data-ng-bind-html="$select.selected.name | highlight: $select.search"
					class="name" title="{{$select.selected.name}}"></div>
				</div>
			</ui-select-match>
			<ui-select-choices repeat="operator in model.roles | propsFilter: { filterKey: 'S0200-roles', expression: { code: $select.search, name: $select.search } }">
				<div class="input-group item">
					<div class="icon" title="{{operator.name}}"
					ng-if="operator.code && operator.code.length">
						<i class="fa fa-universal-access"></i>
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
<div class="vspace-10"
ng-if="!model.disabledRole"></div>
<!-- END::User roles -->
<!-- START::User company -->
<c:if test="${systemAdmin}">
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label col-sm-3">${u_company}</label>
		<ui-select
		style="width: 70%"
		name="company"
		data-ng-model="model.user.company"
		data-allow-clear="true"
		data-placeholder="${u_company_prompt}"
		theme="select2"
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
			<ui-select-match data-placeholder="${u_company_prompt}">
				<div class="input-group item">
					<div class="icon" title="{{$select.selected.name}}"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length">
						<i class="fa fa-building"></i>
					</div>
					<div data-ng-bind-html="$select.selected.code | highlight: $select.search"
					class="code" title="{{$select.selected.code}}"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length"></div>
					<div data-ng-bind-html="$select.selected.name | highlight: $select.search"
					class="name" title="{{$select.selected.name}}"></div>
				</div>
			</ui-select-match>
			<ui-select-choices repeat="operator in model.companies | propsFilter: { filterKey: 'S0200-companies', expression: { code: $select.search, name: $select.search } }">
				<div class="input-group item">
					<div class="icon" title="{{operator.name}}"
					ng-if="operator.code && operator.code.length">
						<i class="fa fa-building"></i>
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
</c:if>
<!-- END::User company -->
<!-- START::User employee -->
<c:if test="${systemAdmin}">
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label col-sm-3">${u_employee}</label>
		<ui-select
		style="width: 70%"
		name="employee"
		data-ng-model="model.user.employee"
		data-allow-clear="true"
		data-placeholder="${u_employee_prompt}"
		theme="select2"
		<c:if test="${!editable}">
		ng-disabled="true"
		</c:if>
		>
			<ui-select-match data-placeholder="${u_employee_prompt}">
				<div class="input-group item">
					<div class="icon" title="{{$select.selected.name}}"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length > 0">
						<i class="fa fa-user"></i>
					</div>
					<div data-ng-bind-html="$select.selected.code | highlight: $select.search"
					class="code" title="{{$select.selected.code}}"
					ng-if="$select.selected && $select.selected.code && $select.selected.code.length > 0"></div>
					<div data-ng-bind-html="$select.selected.name | highlight: $select.search"
					class="name" title="{{$select.selected.name}}"></div>
				</div>
			</ui-select-match>
			<ui-select-choices repeat="operator in model.employees | propsFilter: { filterKey: 'S0200-employees', expression: { code: $select.search, name: $select.search } }">
				<div class="input-group item">
					<div class="icon" title="{{operator.name}}"
					ng-if="operator.code && operator.code.length">
						<i class="fa fa-user"></i>
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
</c:if>
<!-- END::User employee -->
<!-- START::User description -->
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<label class="control-label col-sm-3">${u_description}</label>
		<textarea
		class="form-control"
		id="description" name="description"
		placeholder="${u_description}"
		ng-model="model.user.description"
		ng-trim="true"
		rows="7"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		></textarea>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::User description -->


