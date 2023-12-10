<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%-- START: System Administrator --%>
<c:if test="${nlh4jBase:isSystemAdmin()}">
<c:if test="${company == null}">
<td type="no">{{$index + 1}}</td>
<td>{{data.username}}</td>
<td>{{data.companyCode}}</td>
<td>{{data.companyName}}</td>
<td>{{data.groupCode}}</td>
<td>{{data.groupName}}</td>
<td>{{data.email}}</td>
<td type="enum">{{data.displayLanguage}}</td>
<td type="enum">
	<input type="checkbox" name="checkbox" ng-model="data.enabled" disabled="disabled">
</td>
<td type="enum">
	<input type="checkbox" name="checkbox" ng-model="data.sysadmin" disabled="disabled">
</td>
<td width="10%" type="date">{{data.expiredAt | date:'${cmnDateTimePattern}'}}</td>
</c:if>
<c:if test="${company != null}">
<td type="no">{{$index + 1}}</td>
<td>{{data.username}}</td>
<td>{{data.groupCode}}</td>
<td>{{data.groupName}}</td>
<td>{{data.email}}</td>
<td type="enum">{{data.displayLanguage}}</td>
<td type="enum">
	<input type="checkbox" name="checkbox" ng-model="data.enabled" disabled="disabled">
</td>
<td type="enum">
	<input type="checkbox" name="checkbox" ng-model="data.sysadmin" disabled="disabled">
</td>
<td width="10%" type="date">{{data.expiredAt | date:'${cmnDateTimePattern}'}}</td>
</c:if>
</c:if>
<%-- END: System Administrator --%>
<%-- START: Normal User --%>
<c:if test="${!nlh4jBase:isSystemAdmin()}">
<td type="no">{{$index + 1}}</td>
<td>{{data.username}}</td>
<td>{{data.groupCode}}</td>
<td>{{data.groupName}}</td>
<td>{{data.email}}</td>
<td type="enum">{{data.displayLanguage}}</td>
<td type="enum">
	<input type="checkbox" name="checkbox" ng-model="data.enabled" disabled="disabled">
</td>
<td width="10%" type="date">{{data.expiredAt | date:'${cmnDateTimePattern}'}}</td>
</c:if>
<%-- END: Normal User --%>