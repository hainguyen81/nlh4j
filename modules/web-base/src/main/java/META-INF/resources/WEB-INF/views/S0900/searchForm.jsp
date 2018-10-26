<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<!-- START: function code/name/enabled -->
<div class="row">
	<div class="input-group col-md-3" style="float: left; padding-right: 15px;">
		<span class="input-group-addon" id="basic-addon-building">
			<i class="fa fa-braille"></i></span>
		<input type="text" class="form-control"
		placeholder="<spring:message code="${pageModule}.search.codename.prompt" />"
		aria-describedby="basic-addon-building"
		ng-model="model.search.keyword">
	</div>
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
</div>
<!-- END: function code/name/enabled -->
