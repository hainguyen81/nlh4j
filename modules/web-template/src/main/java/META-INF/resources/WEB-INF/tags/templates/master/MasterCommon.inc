<%@ include file="/WEB-INF/tags/Common.inc" %>

<%-- Parse template attributes --%>
<c:set var="attrModule" scope="application"><tiles:getAsString name="module" ignore="false" /></c:set>
<c:set var="attrPage" scope="application"><tiles:getAsString name="page" ignore="false" /></c:set>
<c:set var="controllerMapping" scope="application"><tiles:getAsString name="controllerMapping" ignore="false" /></c:set>
<c:set var="headerExtActions"><tiles:getAsString name="headerActions" ignore="true" /></c:set>
<c:set var="headerActions"><tiles:getAsString name="headerActions" ignore="true" /></c:set>
<c:set var="searchForm"><tiles:getAsString name="searchForm" ignore="true" /></c:set>
<c:set var="searchFormActions"><tiles:getAsString name="searchFormActions" ignore="true" /></c:set>
<c:set var="listColumns"><tiles:getAsString name="listColumns" ignore="true" /></c:set>
<c:set var="listItems"><tiles:getAsString name="listItems" ignore="true" /></c:set>
<c:set var="customListItems"><tiles:getAsString name="customListItems" ignore="true" /></c:set>
<c:set var="bodyExtra"><tiles:getAsString name="bodyExtra" ignore="true" /></c:set>
<c:set var="listFooter"><tiles:getAsString name="listFooter" ignore="true" /></c:set>
<c:set var="showBaseHeaderActions"><tiles:getAsString name="showBaseHeaderActions" ignore="true" /></c:set>
<c:set var="showBaseHeaderBackAction"><tiles:getAsString name="showBaseHeaderBackAction" ignore="true" /></c:set>
<c:set var="showBaseActions"><tiles:getAsString name="showBaseActions" ignore="true" /></c:set>
<c:set var="itemActions"><tiles:getAsString name="itemActions" ignore="true" /></c:set>
<c:set var="showPagination"><tiles:getAsString name="showPagination" ignore="true" /></c:set>
<c:set var="useBarcode"><tiles:getAsString name="useBarcode" ignore="true" /></c:set>

<%-- Detect showing flags --%>
<c:set var="searchable" value="${fn:length(searchForm) > 0}"/>
<c:set var="showResult" value="${fn:length(listColumns) > 0 && (fn:length(listItems) > 0 || fn:length(customListItems) > 0)}"/>
<c:set var="showSearchActions" value="${fn:length(searchFormActions) > 0}"/>
<c:set var="showBody" value="${searchable || showResult}"/>
<c:set var="showBaseHeaderActions" value="${showBaseHeaderActions == 'true'}"/>
<c:set var="showBaseHeaderBackAction" value="${showBaseHeaderBackAction == 'true'}"/>
<c:set var="showBaseActions" value="${showBaseActions == 'true'}"/>
<c:set var="showItemActions" value="${fn:length(itemActions) > 0}"/>
<c:set var="showPagination" value="${showPagination == 'true'}"/>
<c:set var="useBarcode" value="${useBarcode == 'true'}"/>

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
