<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<!-- START: module code/name -->
<div class="row">
	<div class="input-group col-md-3" style="float: left; padding-right: 15px;">
		<span class="input-group-addon" id="basic-addon-building">
			<i class="fa fa-braille"></i></span>
		<input type="text" class="form-control"
		placeholder="<spring:message code="${pageModule}.search.codename.prompt" />"
		aria-describedby="basic-addon-building"
		ng-model="model.search.keyword">
	</div>
</div>
<div class="vspace-10"></div>
<!-- END: module code/name -->

<!-- START: module enabled/common -->
<div class="row">
	<div class="input-label-control-group col-md-3" style="padding-left: 0">
		<label class="control-label">
			<spring:message code="${pageModule}.search.status.enabled" />
		</label>
		<select class="form-control"
		data-ng-model="model.search.enabled">
			<option value=""><spring:message code="${pageModule}.search.status.enabled.prompt" /></option>
			<option value="true"><spring:message code="${pageModule}.search.status.enabled.valid" /></option>
			<option value="false"><spring:message code="${pageModule}.search.status.enabled.invalid" /></option>
		</select>
	</div>
	<div class="input-label-control-group col-md-3" style="padding-left: 0">
		<label class="control-label">
			<spring:message code="${pageModule}.search.status.common" />
		</label>
		<select class="form-control"
		data-ng-model="model.search.common">
			<option value=""><spring:message code="${pageModule}.search.status.common.prompt" /></option>
			<option value="true"><spring:message code="${pageModule}.search.status.common.valid" /></option>
			<option value="false"><spring:message code="${pageModule}.search.status.common.invalid" /></option>
		</select>
	</div>
</div>
<div class="vspace-10"></div>
<!-- END: module enabled/common -->

<!-- START: module service/visibled -->
<div class="input-label-control-group col-md-3" style="padding-left: 0">
	<label class="control-label">
		<spring:message code="${pageModule}.search.status.service" />
	</label>
	<select class="form-control"
	data-ng-model="model.search.service">
		<option value=""><spring:message code="${pageModule}.search.status.service.prompt" /></option>
		<option value="true"><spring:message code="${pageModule}.search.status.service.valid" /></option>
		<option value="false"><spring:message code="${pageModule}.search.status.service.invalid" /></option>
	</select>
</div>
<div class="input-label-control-group col-md-3" style="padding-left: 0">
	<label class="control-label">
		<spring:message code="${pageModule}.search.status.visibled" />
	</label>
	<select class="form-control"
	data-ng-model="model.search.visibled">
		<option value=""><spring:message code="${pageModule}.search.status.visibled.prompt" /></option>
		<option value="true"><spring:message code="${pageModule}.search.status.visibled.valid" /></option>
		<option value="false"><spring:message code="${pageModule}.search.status.visibled.invalid" /></option>
	</select>
</div>
<!-- END: module service/visibled -->
