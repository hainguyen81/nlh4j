<%@ tag pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="hideHeader" required="false"%>
<%@ attribute name="hideSidebar" required="false"%>
<%@ attribute name="hideFooter" required="false"%>

<div class="wrapper">
	<!-- Body header -->
	<c:if test="${!hideHeader}">
	<%@ include file="/WEB-INF/tags/BodyHeader.tag" %>
	</c:if>
	
	<!-- Body sidebar -->
	<c:if test="${!hideSidebar}">
	<%@ include file="/WEB-INF/tags/BodySidebar.tag" %>
	</c:if>
	
	<!-- Body sidebar -->
	<%@ include file="/WEB-INF/tags/BodyContent.tag" %>
	
	<!-- Body footer -->
	<c:if test="${!hideFooter}">
	<%@ include file="/WEB-INF/tags/BodyFooter.tag" %>
	</c:if>
</div>

