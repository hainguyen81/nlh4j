<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<spring:message var="enabled_valid" code="${pageModule}.search.status.enabled.valid" />
<spring:message var="enabled_invalid" code="${pageModule}.search.status.enabled.invalid" />

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
<td>{{data.description}}</td>

