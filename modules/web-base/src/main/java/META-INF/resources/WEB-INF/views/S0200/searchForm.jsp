<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<!-- START: user name -->
<div class="input-group col-md-3" style="float: left;">
	<span class="input-group-addon" id="basic-addon1">
		<i class="fa fa-user fa-2"></i></span>
	<input type="text" class="form-control"
	placeholder="<spring:message code="${pageModule}.search.username.prompt" />"
	aria-describedby="basic-addon1"
	ng-model="model.search.keyword">
</div>
<!-- END: user name -->
<!-- START: status -->
<div class="input-label-control-group">
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
<!-- END: status -->
<!-- START: expired -->
<div class="input-label-control-group">
	<label class="control-label">
		<spring:message code="${pageModule}.search.status.expired" />
	</label>
	<select class="form-control"
	data-ng-model="model.search.expired">
		<option value=""><spring:message code="${pageModule}.search.status.expired.prompt" /></option>
		<option value="true"><spring:message code="${pageModule}.search.status.expired.valid" /></option>
		<option value="false"><spring:message code="${pageModule}.search.status.expired.invalid" /></option>
	</select>
</div>
<!-- END: expired -->
