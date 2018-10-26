<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="src" required="true"%>
<%@ attribute name="theme" required="false"%>
<%@ attribute name="media" required="false"%>

<%-- Cache Resources --%>
<c:if test="${resourceCache eq true}">
	<c:if test="${fn:length(theme) <= 0}">
		<link href="${resourcePath}/lib/${src}"
		rel="stylesheet" media="${(empty media) ? 'screen' : media}"></link>
	</c:if>
	<c:if test="${fn:length(theme) > 0}">
		<link href="${resourcePath}/lib/${theme}/${src}"
		rel="stylesheet" media="${(empty media) ? 'screen' : media}"></link>
	</c:if>
</c:if>

<%-- Not Cache Resources --%>
<c:if test="${resourceCache ne true}">
	<c:if test="${fn:length(theme) <= 0}">
		<link href="${resourcePath}/lib/${src}?<%= System.currentTimeMillis() %>"
		rel="stylesheet" media="${(empty media) ? 'screen' : media}"></link>
	</c:if>
	<c:if test="${fn:length(theme) > 0}">
		<link href="${resourcePath}/lib/${theme}/${src}?<%= System.currentTimeMillis() %>"
		rel="stylesheet" media="${(empty media) ? 'screen' : media}"></link>
	</c:if>
</c:if>
