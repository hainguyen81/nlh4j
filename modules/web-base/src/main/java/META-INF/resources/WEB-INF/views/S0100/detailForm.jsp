<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<spring:message var="mcode" code="${pageModule}.detail.code" />
<spring:message var="mname" code="${pageModule}.detail.name" />
<spring:message var="rolesDest" code="${pageModule}.detail.destination" />
<spring:message var="viewable" code="${pageModule}.detail.viewable" />
<spring:message var="insertable" code="${pageModule}.detail.insertable" />
<spring:message var="updatable" code="${pageModule}.detail.updatable" />
<spring:message var="deletable" code="${pageModule}.detail.deletable" />

<!-- START::Group identity -->
<input type="hidden" id="id" name="id" ng-model="model.group.id" />
<input type="hidden" id="cid" name="cid" ng-model="model.group.cid" />
<!-- END::Group identity -->

<!-- START::Group name -->
<div class="row">
	<div class="col-md-2">
		<spring:message var="arguments" code="${pageModule}.detail.groupcode" />
		<div class="input-group" style="width: 100%;">
			<span class="input-group-addon" id="basic-addon1">
				<i class="fa fa-lock fa-2"></i>
			</span>
			<input type="text" class="form-control"
			id="groupCd" name="groupCd"
			placeholder="${arguments}"
			aria-describedby="basic-addon1"
			ng-model="model.group.code"
			<c:if test="${!editable || !isNew}">
			ng-readonly="true"
			</c:if>
			maxlength="50"
			ng-required="true"
			ng-trim="false"
			ng-special-chars
			<c:if test="${editable && isNew}">
			ng-unique
			ng-unique-action="/system/role/checkUnique"
			ng-unique-param="onUniqueParams"
			</c:if>
			data-tooltip="{{(detailForm.groupCd.$error.required && (detailForm.groupCd.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${arguments}" />' : (detailForm.groupCd.$error.pattern && (detailForm.groupCd.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_015" />' : (detailForm.groupCd.$error.unique && (detailForm.groupCd.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_007" arguments="${arguments}" />' : ''}}"
			data-tooltip-placement="top"
			data-tooltip-trigger="mouseenter,hover,click,focus"
			>
		</div>
		<c:if test="${editable && isNew}">
			<div
				class="validate-waiting"
				data-ng-show="detailForm.groupCd.$pending.unique && (detailForm.groupCd.$touched || detailForm.groupCd.$submitted)">
				<i class="fa-li fa fa-spinner fa-spin"></i>
			</div>
		</c:if>
	</div>
	<div class="col-md-4" style="margin-left: 15px;">
		<spring:message var="arguments" code="${pageModule}.detail.groupname" />
		<div class="input-group" style="width: 100%;">
			<span class="input-group-addon" id="basic-addon1">
				<i class="fa fa-pencil-square-o fa-2"></i>
			</span>
			<input type="text" class="form-control"
			id="groupName" name="groupName"
			placeholder="${arguments}"
			aria-describedby="basic-addon1"
			ng-model="model.group.name"
			ng-trim="false"
			<c:if test="${!editable}">
			ng-readonly="true"
			</c:if>
			maxlength="255"
			ng-required="true"
			data-tooltip="{{(detailForm.groupName.$error.required && (detailForm.groupName.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${arguments}" />' : ''}}"
			data-tooltip-placement="top" data-tooltip-trigger="mouseenter,hover,click,focus"
			>
		</div>
	</div>
</div>
<div class="vspace-10 clear"></div>
<div class="row">
	<div class="col-md-6">
		<spring:message var="arguments" code="${pageModule}.detail.groupdesc" />
		<input type="text" class="form-control"
		id="groupDescription" name="description"
		placeholder="${arguments}"
		aria-describedby="basic-addon1"
		ng-model="model.group.description"
		ng-trim="false"
		<c:if test="${!editable}">
		ng-readonly="true"
		</c:if>
		>
	</div>
</div>
<div class="vspace-15 clear"></div>
<!-- END::Group name -->

<!-- START::Role assignment -->
<div class="row">
	<!-- START::Source listview -->
	<div class="col-md-5">
		<div id="rolesSrc"
		class="form-control"
		style="min-height: 300px; height: auto;"
		data-listview=""
		data-items="model.source"
		data-ng-model="model.source"
		data-item-description="moduleDescription"
		data-columns="moduleCd=${mcode}|moduleName=${mname}"
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
      	<div class="col-md-1 role actions">
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
		<div id="rolesDest"
		class="form-control"
		style="min-height: 300px; height: auto;"
		data-listview=""
		data-items="model.destination"
		data-ng-model="model.destination"
		data-item-description="moduleDescription"
		data-columns="moduleCd=${mcode}|moduleName=${mname}|viewable=${viewable}|insertable=${insertable}|updatable=${updatable}|deletable=${deletable}"
		template-base="${resourcePath}/lib/angular-listview/"
		<c:if test="${editable}">
			data-click="onDestSelect"
			data-dbclick="onUnAssignByDbClick"
			data-change="onDestCheckChange"
			data-sortable="${editable}"
			data-listeners="model.sortable"
		</c:if>
		<c:if test="${!editable}">
		data-readonly="true"
		</c:if>
		tooltip="{{(model.rolesDest.$error.required && (model.rolesDest.$touched || detailForm.$submitted)) ? '<spring:message code="COM_MSG_ID_ERR_002" arguments="${rolesDest}" />' : ''}}"
		tooltip-placement="top" tooltip-trigger="mouseenter"
		>
			<!-- Destination content -->
		</div>
	</div>
	<!-- END::Destination listview -->
</div>
<!-- END::Role assignment -->
