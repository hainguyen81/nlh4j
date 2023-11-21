<%@ tag pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="title" required="false" %>
<%@ attribute name="titleArguments" required="false" %>
<%@ attribute name="titleArgumentSeparator" required="false" %>

<head>
<%@ include file="/WEB-INF/tags/Meta.inc" %>
<link rel="shortcut icon" href="${faviconUrl}" type="image/x-icon" />
<title>
	<nlh4j:message code="${title}"
	arguments="${titleArguments}"
	argumentSeparator="${titleArgumentSeparator}" />
</title>

<!-- ------------------------------------------------- -->
<!-- STYLESHEET -->
<!-- ------------------------------------------------- -->
<%@ include file="/WEB-INF/tags/app/AppCssLibs.inc" %>

<%-- NLH4J - Styles --%>
<%@ include file="/WEB-INF/tags/app/AppCss.inc" %>

<!-- ------------------------------------------------- -->
<!-- JAVASCRIPT -->
<!-- ------------------------------------------------- -->
<%@ include file="/WEB-INF/tags/app/AppJsLibs.inc" %>

<%-- NLH4J - Scripts --%>
<%@ include file="/WEB-INF/tags/app/AppJs.inc" %>

<jsp:doBody />

<!-- autocomplete: off, autocorrect: off, autocapitalize: off, spellcheck: false --> 
<tag autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false"/>
</head>
