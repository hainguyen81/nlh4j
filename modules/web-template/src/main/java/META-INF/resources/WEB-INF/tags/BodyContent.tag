<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="elementId" required="false"%>
<%@ attribute name="appModule" required="false"%>
<%@ attribute name="appController" required="false"%>
<%@ attribute name="fnPreInit" required="false"%>
<%@ attribute name="fnInit" required="false"%>
<%@ attribute name="cssClass" required="false"%>

<c:set var="style" value="" />
<c:if test="${authenticated ne true}">
<c:set var="style" value="style=\"left: 0; width: 100%\"" />
</c:if>

<!-- START::content::(@TODO must place content after side immediately for CSS selector) -->
<!-- <div class="animsition"
data-animsition-in-class="flip-in-x"
data-animsition-in-duration="1000"
data-animsition-out-class="flip-out-x"
data-animsition-out-duration="800"> -->
<!-- 	<div class="container page-content unselectable"> -->
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper unselectable">
	<%-- Continue another content --%>
	<section class="content">
		<c:choose>
			<c:when test="${not empty appModule && not empty appController}">
				<c:if test="${not empty cssClass}">
					<%-- <div class="container page-content unselectable ${cssClass}"
					ng-slimscroll
					bar-z-index="300"
					color="rgba(229,245,145,1)"
					${nlh4j:angularBodyAttr(elementId, appModule, appController, fnPreInit, fnInit, '')}
					> --%>
					<div class="${cssClass}"
					${style}
					${nlh4j:angularBodyAttr(elementId, appModule, appController, fnPreInit, fnInit, '')}>
				</c:if>
				<c:if test="${not empty cssClass}">
					<%-- <div ng-slimscroll
					bar-z-index="300"
					color="rgba(229,245,145,1)"
					${nlh4j:angularBodyAttr(elementId, appModule, appController, fnPreInit, fnInit, 'container page-content unselectable')}
					> --%>
					<div ${nlh4j:angularBodyAttr(elementId, appModule, appController, fnPreInit, fnInit, 'container page-content unselectable')}
					${style}>
				</c:if>
			</c:when>
			<c:when test="${not empty appModule && empty appController}">
				<c:if test="${not empty cssClass}">
					<%-- <div class="container page-content unselectable ${cssClass}"
					ng-slimscroll
					bar-z-index="300"
					color="rgba(229,245,145,1)"
					${nlh4j:angularBodyAttr(elementId, appModule, 'commonCtrl', fnPreInit, fnInit, '')}
					> --%>
					<div class="${cssClass}"
					${style}
					${nlh4j:angularBodyAttr(elementId, appModule, 'commonCtrl', fnPreInit, fnInit, '')}>
				</c:if>
				<c:if test="${not empty cssClass}">
					<%-- <div ng-slimscroll
					bar-z-index="300"
					color="rgba(229,245,145,1)"
					${nlh4j:angularBodyAttr(elementId, appModule, 'commonCtrl', fnPreInit, fnInit, 'container page-content unselectable')}
					> --%>
					<div ${nlh4j:angularBodyAttr(elementId, appModule, 'commonCtrl', fnPreInit, fnInit, 'container page-content unselectable')}
					${style}>
				</c:if>
			</c:when>
			<c:otherwise>
				<%-- <div class="container page-content unselectable ${cssClass}"
				ng-slimscroll
				bar-z-index="300"
				color="rgba(229,245,145,1)"
				> --%>
				<div class="${cssClass}"
				${style}>
			</c:otherwise>
		</c:choose>
	
			<jsp:doBody />
		</div>
	</section>
</div>
<!-- </div> -->
<!-- END::content::(@TODO must place content after side immediately for CSS selector) -->
