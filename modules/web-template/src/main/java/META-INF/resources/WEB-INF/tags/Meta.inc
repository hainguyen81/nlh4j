<%@ include file="/WEB-INF/tags/Common.inc" %>

<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<%-- Cache Resources: 604800 seconds [7 day(s)] --%>
<%-- <c:if test="${resourceCache eq true}">
	<%
	response.setHeader("Cache-Control", "max-age=604800,public");
	response.setHeader("Pragma", "public");
	response.setDateHeader("Expires", (System.currentTimeMillis() + 604800000L));
	%>
	<meta http-equiv="Cache-Control" content="max-age=604800,public" />
	<meta http-equiv="Expires" content="<%= (System.currentTimeMillis() + 604800000L) %>" />
	<meta http-equiv="Pragma" content="public" />
</c:if> --%>
<%-- Not Cache Resources --%>
<%-- <c:if test="${resourceCache ne true}"> --%>
	<%
	response.setHeader("Cache-Control", "max-age=0,no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
	%>
	<meta http-equiv="Cache-Control" content="max-age=0,no-cache" />
	<meta http-equiv="Expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
	<meta http-equiv="Pragma" content="no-cache" />
<%-- </c:if> --%>
<meta http-equiv="Content-Language" content="${currentLocale}">
<meta http-equiv="Content-Type" content="text/html;charset=Utf-8" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta name="viewport" content="width=device-width, initial-scale=1, height=device-height" />
<meta name="_csrf" content="${_csrf.token}"/>
<!-- offline URL checker -->
<meta name="_offline_checker" content="${faviconUrl}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="_csrf_param" content="${_csrf.parameterName}"/>
<%-- Error Token --%>
<meta name="errorInternet" content="${errorInternet}"/>
<meta name="errorServer" content="${errorServer}"/>
<meta name="e0" content="${errorInternet}"/>
<meta name="e401" content="${error401}"/>
<meta name="e403" content="${error403}"/>
<meta name="e404" content="${error404}"/>
<meta name="e405" content="${error405}"/>
<meta name="e409" content="${error409}"/>
<meta name="e417" content="${error417}"/>
<meta name="e500" content="${errorServer}"/>
