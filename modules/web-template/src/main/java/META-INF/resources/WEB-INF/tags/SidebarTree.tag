<%@ tag pageEncoding="UTF-8" description="Display Whole Modules on Tree for Side-bar" %>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="node" type="org.nlh4j.web.base.dashboard.dto.SidebarDto" required="true" %>
<%@ attribute name="rowNumber" required="false" type="java.lang.String" %>
<%@ attribute name="activeUrl" required="false" type="java.lang.String" %>
<%@ attribute name="ajaxElem" required="false" type="java.lang.String" %>
<%@ attribute name="markChildNo" required="false" type="Boolean" %>

<c:set var="cssClass" value="" />
<c:if test="${node.childs <= 0}">
	<c:set var="cssClass" value=" leaf" />
</c:if>
<c:set var="activeClass" value="" />
<c:set var="parentActive" value="false" />
<c:if test="${node.childs <= 0 && not empty activeUrl && fn:startsWith(activeUrl, node.mainUrl)}">
	<c:set var="activeClass" value=" active" />
</c:if>
<c:if test="${node.childs > 0 && not empty activeUrl}">
	<c:forEach var="child" items="${node.children}" varStatus="loop">
		<c:if test="${fn:startsWith(activeUrl, child.mainUrl)}">
			<c:set var="activeClass" value=" open" />
			<c:set var="parentActive" value="true" />
		</c:if>
	</c:forEach>
</c:if>

<c:set var="cssClassTree" value="" />
<c:if test="${node.childs <= 0}">
	<c:set var="cssClassTree" value="" />
</c:if>
<c:if test="${node.childs > 0}">
	<c:set var="cssClassTree" value="treeview " />
</c:if>

<li id="mnu_${node.id}" class="${cssClassTree} ${cssClass} ${activeClass}"
style="padding-left: ${node.depth <= 1 ? 0 : (node.depth * (markChildNo eq true ? 5 : 10))}px">
	<c:if test="${node.childs > 0}">
		<a href="#" title="${node.display}">
			<i class="${node.css}"></i>
			<span>
				<c:if test="${not empty rowNumber}">${rowNumber}.&nbsp;</c:if>
				${node.display}
			</span>
			<span class="pull-right-container">
	              <i class="fa fa-angle-left pull-right"></i>
	         </span>
		</a>
		<ul class="treeview-menu">
			<c:forEach var="child" items="${node.children}" varStatus="loop">
				<c:if test="${markChildNo eq true}">
					<c:if test="${not empty rowNumber}">
						<c:set var="childIndex">${rowNumber}.${loop.index + 1}</c:set>
						<nlh4jTags:SidebarTree node="${child}"
						rowNumber="${childIndex}"
						activeUrl="${activeUrl}"
						ajaxElem="${ajaxElem}"
						markChildNo="${markChildNo}" />
					</c:if>
			        <c:if test="${empty rowNumber}">
			        	<nlh4jTags:SidebarTree node="${child}"
						rowNumber="${childIndex}"
						activeUrl="${activeUrl}"
						ajaxElem="${ajaxElem}"
						markChildNo="${markChildNo}" />
			        </c:if>
				</c:if>
				<c:if test="${markChildNo ne true}">
					<nlh4jTags:SidebarTree node="${child}"
					rowNumber=""
					activeUrl="${activeUrl}"
					ajaxElem="${ajaxElem}"
					markChildNo="${markChildNo}" />
				</c:if>
		    </c:forEach>
		</ul>
	</c:if>
	<c:if test="${node.childs <= 0}">
		<a href="javascript:void(0);"
		such-href="${node.mainUrl}"
		<c:if test="${not empty ajaxElem}">
		such-ajax="${ajaxElem}"
		</c:if>
		title="${node.display}">
			<i class="${node.css}"></i>
			<span>
				<c:if test="${not empty rowNumber}">${rowNumber}.&nbsp;</c:if>
				${node.display}
			</span>
		</a>
	</c:if>
</li>
