<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<spring:message var="f_codename" code="${pageModule}.detail.function" />
<spring:message var="f_codename_prompt" code="${pageModule}.detail.function.prompt" />
<spring:message var="c_company" code="${pageModule}.detail.company.assigned" />
<spring:message var="c_code" code="${pageModule}.detail.company.code" />
<spring:message var="c_name" code="${pageModule}.detail.company.name" />

<!-- START::Function identity -->
<input type="hidden" id="id" name="id" ng-model="model.func.id" />
<!-- END::Module identity -->

<!-- START::Module code/name -->
<div class="row">
	<div class="input-label-control-group col-sm-5">
		<c:set var="arguments" value="${f_codename}" />
		<label class="control-label" style="width: 120px;">${arguments}</label>
		<input type="text"
		class="form-control"
		id="codename" name="codename"
		placeholder="${u_name}"
		ng-model="model.func.codeNameDisplay"
		ng-readonly="true"
		ng-trim="true"
		>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END::Module code/name -->

<!-- START::Company assignment -->
<div class="row">
	<!-- START::Source listview -->
	<div class="col-md-5">
		<div id="compSrc"
		class="form-control"
		style="min-height: 300px; height: auto;"
		data-listview=""
		data-items="model.source"
		data-ng-model="model.source"
		data-columns="code=${c_code}|name=${c_name}"
		template-base="${resourcePath}/lib/angular-listview/"
		<c:if test="${editable}">
			data-click="onSrcSelect"
			data-dbclick="onAssignByDbClick"
			data-sortable="true"
			data-listeners="model.sortable"
		</c:if>
		<c:if test="${!editable}">
		data-readonly="true"
		</c:if>
		>
			<!-- Source content -->
		</div>
	</div>
	<!-- END::Source listview -->

	<!-- START::Actions -->
      	<div class="col-md-1 move-actions">
      		<div class="row">
      			<a href="javascript:void(0)"
      			<c:if test="${editable}">
      			ng-click="onAssignByAction()"
      			</c:if>
      			>
      				<i class="fa fa-arrow-right"></i>
      			</a>
      		</div>
      		<div class="row">
      			<a href="javascript:void(0)"
      			<c:if test="${editable}">
      			ng-click="onUnAssignByAction()"
      			</c:if>
      			>
      				<i class="fa fa-arrow-left"></i>
      			</a>
      		</div>
      	</div>
      	<!-- END::Actions -->

	<!-- START::Destination listview -->
	<div class="col-md-6">
		<div id="compDest"
		class="form-control"
		style="min-height: 300px; height: auto;"
		data-listview=""
		data-items="model.destination"
		data-ng-model="model.destination"
		data-columns="code=${c_code}|name=${c_name}"
		template-base="${resourcePath}/lib/angular-listview/"
		<c:if test="${editable}">
			data-click="onDestSelect"
			data-dbclick="onUnAssignByDbClick"
			data-sortable="${editable}"
			data-listeners="model.sortable"
		</c:if>
		<c:if test="${!editable}">
		data-readonly="true"
		</c:if>
		tooltip="{{(model.compDest.$error.required && (model.compDest.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${c_company}" />' : ''}}"
		tooltip-placement="top" tooltip-trigger="mouseenter"
		>
			<!-- Destination content -->
		</div>
	</div>
	<!-- END::Destination listview -->
</div>
<!-- END::Role assignment -->
