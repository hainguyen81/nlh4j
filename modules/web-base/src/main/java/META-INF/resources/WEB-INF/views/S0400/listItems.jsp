<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<spring:message var="enabled_valid" code="${pageModule}.search.status.enabled.valid" />
<spring:message var="enabled_invalid" code="${pageModule}.search.status.enabled.invalid" />
<spring:message var="common_valid" code="${pageModule}.search.status.common.valid" />
<spring:message var="common_invalid" code="${pageModule}.search.status.common.invalid" />
<spring:message var="service_valid" code="${pageModule}.search.status.service.valid" />
<spring:message var="service_invalid" code="${pageModule}.search.status.service.invalid" />
<spring:message var="visibled_valid" code="${pageModule}.search.status.visibled.valid" />
<spring:message var="visibled_invalid" code="${pageModule}.search.status.visibled.invalid" />

<td type="no">{{$index + 1}}</td>
<td>
	<div class="animate-switch" style="margin-left:{{(data.depth - 1) * 20}}px;">
		{{data.code}}
	</div>
</td>
<td>{{data.display}}</td>
<td type="enum">
	<input type="checkbox" name="checkbox"
	ng-model="data.enabled" disabled="disabled"
	ng-if="data.enabled"
	title="${enabled_valid}">
	<input type="checkbox" name="checkbox"
	ng-model="data.enabled" disabled="disabled"
	ng-if="!data.enabled"
	title="${enabled_invalid}">
</td>
<td type="enum">
	<input type="checkbox" name="checkbox"
	ng-model="data.common" disabled="disabled"
	ng-if="data.common"
	title="${common_valid}">
	<input type="checkbox" name="checkbox"
	ng-model="data.common" disabled="disabled"
	ng-if="!data.common"
	title="${common_invalid}">
</td>
<td type="enum">
	<input type="checkbox" name="checkbox"
	ng-model="data.service" disabled="disabled"
	ng-if="data.service"
	title="${service_valid}">
	<input type="checkbox" name="checkbox"
	ng-model="data.service" disabled="disabled"
	ng-if="!data.service"
	title="${service_invalid}">
</td>
<td type="enum">
	<input type="checkbox" name="checkbox"
	ng-model="data.visibled" disabled="disabled"
	ng-if="data.visibled"
	title="${visibled_valid}">
	<input type="checkbox" name="checkbox"
	ng-model="data.visibled" disabled="disabled"
	ng-if="!data.visibled"
	title="${visibled_invalid}">
</td>
<td>{{data.urlRegex}}</td>
<td>{{data.link}}</td>
<td>{{data.description}}</td>

