<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="src" required="true"%>
<%@ attribute name="theme" required="false"%>

<%-- Cache Resources --%>
<c:if test="${resourceCache eq true}">
	<c:if test="${fn:length(theme) <= 0}">
		<script src="${resourcePath}/lib/${src}"></script>
	</c:if>
	<c:if test="${fn:length(theme) > 0}">
		<script src="${resourcePath}/lib/${theme}/${src}"></script>
	</c:if>
</c:if>

<%-- Not Cache Resources --%>
<c:if test="${resourceCache ne true}">
	<c:if test="${fn:length(theme) <= 0}">
		<script src="${resourcePath}/lib/${src}?<%= System.currentTimeMillis() %>"></script>
	</c:if>
	<c:if test="${fn:length(theme) > 0}">
		<script src="${resourcePath}/lib/${theme}/${src}?<%= System.currentTimeMillis() %>"></script>
	</c:if>
</c:if>
