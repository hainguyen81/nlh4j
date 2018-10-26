<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="module" required="false" %>
<%@ attribute name="controller" required="false" %>

<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<c:choose>
	<c:when test="${not empty module && not empty controller}">
		<html ng-app="${module}" ng-controller="${controller}" ng-cloak class="no-js"><!--<![endif]-->
	</c:when>
	<c:when test="${not empty module && empty controller}">
		<html ng-app="${module}" ng-controller="commonCtrl" ng-cloak class="no-js"><!--<![endif]-->
	</c:when>
	<c:otherwise>
		<html class="no-js"><!--<![endif]-->
	</c:otherwise>
</c:choose>

<jsp:doBody />

</html>