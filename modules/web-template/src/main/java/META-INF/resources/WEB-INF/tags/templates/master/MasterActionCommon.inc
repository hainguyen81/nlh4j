<%@ include file="/WEB-INF/tags/Common.inc" %>

<%-- Parse template attributes --%>
<c:set var="attrFormTitleKey" scope="application"><tiles:getAsString name="formTitleKey" ignore="false" /></c:set>
<c:set var="attrModule" scope="application"><tiles:getAsString name="module" ignore="false" /></c:set>
<c:set var="attrPage" scope="application"><tiles:getAsString name="page" ignore="false" /></c:set>
<c:set var="controllerMapping" scope="application"><tiles:getAsString name="controllerMapping" ignore="false" /></c:set>
<c:set var="showBaseHeaderActions" scope="application"><tiles:getAsString name="showBaseHeaderActions" ignore="false" /></c:set>
<c:set var="showBaseHeaderBackAction" scope="application"><tiles:getAsString name="showBaseHeaderBackAction" ignore="false" /></c:set>
<c:set var="detailForm" scope="application"><tiles:getAsString name="detailForm" ignore="false" /></c:set>
<c:set var="detailFormPreventKeys" scope="application"><tiles:getAsString name="detailFormPreventKeys" ignore="true" /></c:set>
<c:set var="detailExtActions" scope="application"><tiles:getAsString name="detailExtActions" ignore="true" /></c:set>
<c:set var="detailActions" scope="application"><tiles:getAsString name="detailActions" ignore="true" /></c:set>
<c:set var="useBarcode"><tiles:getAsString name="useBarcode" ignore="true" /></c:set>

<%-- Check for valid page module, name information if controller not passing, so getting it from tile attributes --%>
<c:if test="${fn:length(pageModule) <= 0}">
<c:set var="pageModule" value="${attrModule}00" scope="application" />
<%-- Re-update viewable permission --%>
<c:set var="viewable" value="${nlh4j:contains(modules, attrModule)}" scope="application"/>
</c:if>
<c:if test="${fn:length(pageName) <= 0}">
<c:set var="pageName" value="${attrPage}" scope="application" />
</c:if>
<c:if test="${fn:length(pagePath) <= 0 && fn:length(pageModule) > 0 && fn:length(pageName) > 0}">
<c:set var="pagePath" value="${pageModule}/${pageName}" scope="application" />
</c:if>
<c:if test="${fn:length(pageController) <= 0 && fn:length(pageModule) > 0 && fn:length(pageName) > 0}">
<c:set var="pageController" value="${pageName}Ctrl" scope="application" />
</c:if>

<%-- Detect showing flags --%>
<c:set var="showBaseHeaderActions" value="${showBaseHeaderActions == 'true'}"/>
<c:set var="showBaseHeaderBackAction" value="${showBaseHeaderActions == 'true'}"/>
<c:set var="useBarcode" value="${useBarcode == 'true'}"/>
<c:set var="form" value="detailForm" scope="application" />
<c:set var="action" value="" scope="application" />
<c:set var="successMsg" value="" scope="application" />
<c:set var="formTitle" value="" scope="application" />
<c:if test="${fn:length(attrFormTitleKey) > 0}">
<nlh4j:message var="formTitle" code="${attrFormTitleKey}" scope="application" />
</c:if>
<c:if test="${fn:length(formTitle) <= 0}">
<nlh4j:message var="formTitle" code="${pageModule}.title.view" scope="application" />
<c:if test="${editable && isNew}">
	<c:set var="action" value="${controllerMapping}/create/save" scope="application" />
	<nlh4j:message var="successMsg" code="COM_MSG_ID_INF_002" scope="application" />
	<nlh4j:message var="formTitle" code="${pageModule}.title.new" scope="application" />
</c:if>
<c:if test="${editable && !isNew}">
	<c:set var="action" value="${controllerMapping}/update/save" scope="application" />
	<nlh4j:message var="successMsg" code="COM_MSG_ID_INF_003" scope="application" />
	<nlh4j:message var="formTitle" code="${pageModule}.title.update" scope="application" />
</c:if>
</c:if>
