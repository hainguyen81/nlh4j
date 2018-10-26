<%@ tag pageEncoding="utf-8" description="The angular &lt;ui-calendar&gt; component wrapper (base on fullcalendar)"%>
<%@ tag import="org.nlh4j.util.DateUtils"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="groupCssClass" required="false"
description="Whole component area CSS class"%>
<%@ attribute name="calLabel" required="false"
description="Component label value"%>
<%@ attribute name="calLabelCssClass" required="false"
description="Component label CSS class"%>
<%@ attribute name="calName" required="false"
description="Component HTML element identity, name. This will be attached to the uiCalendarConfig constant object, that can be accessed via DI"%>
<%@ attribute name="calCssClass" required="false"
description="Main calendar CSS class"%>
<%@ attribute name="calModel" required="true"
description="Specify angular model (ng-model)"%>
<%@ attribute name="calModelOptions" required="false"
description="Specify angular model options (ng-model-options)"%>
<%@ attribute name="textReadonly" required="false"
description="Specify component is read-only (ng-readony)"%>
<%@ attribute name="textDisabled" required="false"
description="Specify component is disabled (ng-disabled)"%>
<%@ attribute name="calNgIf" required="false"
description="Specify component whether has been rendered (ng-if)"%>
<%@ attribute name="calNgShow" required="false"
description="Specify component whether has been shown (ng-show)"%>
<%@ attribute name="calConfig" required="true"
description="Calendar configuration (JSON object)"%>
<%@ attribute name="calWatchEvent" required="false"
description="Angular block script or function has been called to watch event"%>

<c:if test="${empty calName || fn:length(calName) <= 0}">
<%
	long tm = DateUtils.currentTimestamp().getTime();
	calName = "calendar-" + String.valueOf(tm);
%>
</c:if>
<%	calName = calName.replace("\"", "_")
		.replace("'", "_").replace("[", "_").replace("]", "_")
		.replace("\\", "_").replace("\n", "_").replace("<", "_")
		.replace(">", "_").replace("&", "_").replace("#", "_")
		.replace("$", "_"); %>

<div class="form-group has-feedback ${groupCssClass}"
<c:if test="${textNgIf eq true}">ng-if="true"</c:if>
<c:if test="${not empty textNgIf && textNgIf['class'].simpleName ne 'Boolean'}">ng-if="${textNgIf}"</c:if>
<c:if test="${textNgShow eq true}">ng-show="true"</c:if>
<c:if test="${not empty textNgShow && textNgShow['class'].simpleName ne 'Boolean'}">ng-show="${textNgShow}"</c:if>
>
	<c:if test="${not empty calLabel}">
	<label
	class="control-label ${(not empty calLabelCssClass ? calLabelCssClass : calName)}"
	<c:if test="${calNgIf eq true}">ng-if="true"</c:if>
	<c:if test="${not empty calNgIf && calNgIf['class'].simpleName ne 'Boolean'}">ng-if="${calNgIf}"</c:if>
	<c:if test="${calNgShow eq true}">ng-show="true"</c:if>
	<c:if test="${not empty calNgShow && calNgShow['class'].simpleName ne 'Boolean'}">ng-show="${calNgShow}"</c:if>
	>${calLabel}</label>
	</c:if>
	<div
	ui-calendar="${calConfig}"
	<c:if test="${not empty calName}">id="${calName}"</c:if>
	class="form-control ${calCssClass}"
	<c:if test="${not empty calModel}">ng-model="${calModel}" reference-ng-model-controller</c:if>
	<c:if test="${not empty calModel && not empty calModelOptions}">ng-model-options="${calModelOptions}"</c:if>
	<c:if test="${calReadonly eq true}">ng-readonly="true"</c:if>
	<c:if test="${not empty calReadonly && calReadonly['class'].simpleName ne 'Boolean'}">ng-readonly="${calReadonly}"</c:if>
	<c:if test="${calDisabled eq true}">ng-disabled="true"</c:if>
	<c:if test="${not empty calDisabled && calDisabled['class'].simpleName ne 'Boolean'}">ng-disabled="${calDisabled}"</c:if>
	<c:if test="${calNgIf eq true}">ng-if="true"</c:if>
	<c:if test="${not empty calNgIf && calNgIf['class'].simpleName ne 'Boolean'}">ng-if="${calNgIf}"</c:if>
	<c:if test="${calNgShow eq true}">ng-show="true"</c:if>
	<c:if test="${not empty calNgShow && calNgShow['class'].simpleName ne 'Boolean'}">ng-show="${calNgShow}"</c:if>
	<c:if test="${not empty calWatchEvent}">calendar-watch-event="${calWatchEvent}"</c:if>
	>&nbsp;</div>

	<%-- Add more content into text group if necessary --%>
	<jsp:doBody />
</div>
