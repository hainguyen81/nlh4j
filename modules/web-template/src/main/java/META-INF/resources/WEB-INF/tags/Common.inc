<%@ include file="/WEB-INF/tags/TagLibs.inc" %>

<%-- Context variables --%>
<%-- <% pageContext.setAttribute("newLineChar", "\n"); %> --%>

<%-- Page information --%>
<c:set var="pagePath" value="${pageModule}/${pageName}" scope="application" />
<c:set var="pageController" value="${pageName}Ctrl" scope="application" />

<%-- Security Variables --%>
<c:set var="authenticated" value="false" scope="application" />
<sec:authorize access="isAuthenticated()">
<c:set var="authenticated" value="true" scope="application" />
</sec:authorize>

<%-- Parse session value --%>
<c:set var="systemAdmin" scope="application" value="false" />
<c:set var="userName" scope="application" value="Anonymous" />
<c:if test="${authenticated eq true}">
	<c:set var="sidebar" scope="application" value="${nlh4jBase:sidebarModules()}" />
	<%-- <c:set var="companies" scope="application" value="${nlh4jBase:companies()}" />
	<c:set var="company" scope="application" value="${nlh4jBase:currentCompany()}" /> --%>
	<c:set var="systemAdmin" scope="application" value="${nlh4jBase:isSystemAdmin()}" />
	<sec:authentication property="principal.username" var="userName" scope="application" />
</c:if>

<%-- Permissions --%>
<c:set var="FUNC_INSERT" value="INSERTABLE" scope="application" />
<c:set var="FUNC_UPDATE" value="UPDATABLE" scope="application" />
<c:set var="FUNC_DELETE" value="DELETABLE" scope="application" />
<c:set var="FUNC_WRITE" value="WRITABLE" scope="application" />
<c:set var="viewable" value="${nlh4j:contains(modules, module)}" scope="application" />
<c:set var="insertable" value="${systemAdmin || nlh4j:contains(functions, FUNC_INSERT) || nlh4j:contains(functions, FUNC_WRITE)}" scope="application" />
<c:set var="updatable" value="${insertable || nlh4j:contains(functions, FUNC_UPDATE)}" scope="application" />
<c:set var="deletable" value="${insertable || nlh4j:contains(functions, FUNC_DELETE)}" scope="application" />
<c:set var="writable" value="${insertable || updatable || deletable}" scope="application" />

<%-- c:if test="${company == null || company.id == null || company.id <= 0}">
	<nlh4j:message code="application.title" var="appTitle" htmlEscape="false" scope="application" />
</c:if>
<c:if test="${company != null && company.id != null && company.id > 0}">
	<c:set var="appTitle" value="${company.getName()}" scope="application" />
</c:if --%>
<nlh4j:message var="cmnFullDateTimePattern" code="date.time.pattern9" scope="application" />
<nlh4j:message var="cmnDateTimePattern" code="date.time.pattern2" scope="application" />
<nlh4j:message var="cmnTimePattern" code="date.time.pattern4" scope="application" />
<nlh4j:message var="cmnMonthPattern" code="date.time.pattern6" scope="application" />
<nlh4j:message var="cmnYearPattern" code="date.time.pattern7" scope="application" />
<nlh4j:message var="cmnDayMonthPattern" code="date.time.pattern10" scope="application" />
<nlh4j:message var="calBtnToday" code="button.calendar.today" scope="application" />
<nlh4j:message var="calBtnClear" code="button.calendar.clear" scope="application" />
<nlh4j:message var="calBtnClose" code="button.calendar.close" scope="application" />
<nlh4j:message var="languageCaption" code="application.language" scope="application" />
<nlh4j:message var="languagePrompt" code="application.language.prompt" scope="application" />
<nlh4j:message var="deleteSuccess" categoryNumber="1" number="1" numLen="3" scope="application" />
<nlh4j:message var="insertSuccess" categoryNumber="1" number="2" numLen="3" scope="application" />
<nlh4j:message var="updateSuccess" categoryNumber="1" number="3" numLen="3" scope="application" />
<nlh4j:message var="deleteConfirmation" categoryNumber="1" number="4" numLen="3" scope="application" />
<nlh4j:message var="saveSuccess" categoryNumber="1" number="5" numLen="3" scope="application" />

<%-- START: common error message --%>
<nlh4j:message var="errorInternet" code="0" scope="application" />
<nlh4j:message var="errorServer" code="500" scope="application" />
<nlh4j:message var="error401" code="401" scope="application" />
<nlh4j:message var="error403" code="403" scope="application" />
<nlh4j:message var="error404" code="404" scope="application" />
<nlh4j:message var="error405" code="405" scope="application" />
<nlh4j:message var="error409" code="409" scope="application" />
<nlh4j:message var="error417" code="417" scope="application" />
<%-- END: common error message --%>

<%-- START: common variables --%>
<c:set var="req" value="${pageContext.request}" />
<c:set var="url" value="${(req eq null ? '' : req.requestURL)}" />
<c:set var="uri" value="${(req eq null ? '' : req.requestURI)}" />
<c:set var="host" value="${fn:substring(url, 0, url.length() - uri.length())}" scope="application" />
<c:set var="contextPath" value="${(req eq null ? '' : req.contextPath)}" scope="application" />
<c:set var="basePath" value="${host}${contextPath}" scope="application" />
<c:set var="curUrl" value="${(requestScope eq null || requestScope['javax.servlet.forward.request_uri'] eq null ? '' : requestScope['javax.servlet.forward.request_uri'].replace(contextPath, ''))}" scope="application" />
<c:set var="resourcePath" value="${host}${contextPath}/resources" scope="application" />
<c:set var="resourceThemePath" value="${resourcePath}/${appTheme}" scope="application" />
<c:url var="faviconUrl" value="/favicon" scope="application" />
<c:set var="listViewTemplate" value="${resourcePath}/lib/angular-listview/${verAngularListview}/" scope="application" />
<c:set var="currentLocale" value="${(pageContext eq null || pageContext.response == null || pageContext.response.locale == null ? '' : pageContext.response.locale.language)}" scope="application" />
<c:if test="${currentLocale == null || fn:length(currentLocale) <= 0}">
	<c:set var="currentLocale" value="en" scope="application" />
</c:if>
<%-- END: common variables --%>

<%-- START: upload variables --%>
<spring:eval var="maxUploadSize" expression="@properties.getProperty('multipart.maximum.size')" scope="application" />
<%-- END: upload variables --%>

<%-- Client Scripts/Stylesheet Configuration --%>
<%@ include file="/WEB-INF/tags/ClientVersion.inc" %>
