<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<div class="input-group col-md-3">
	<span class="input-group-addon" id="basic-addon1">
		<i class="fa fa-lock fa-2"></i></span>
	<input type="text" class="form-control"
	placeholder="<spring:message code="${pageModule}.search.prompt" />"
	aria-describedby="basic-addon1"
	ng-model="model.search">
</div>