<%@ tag pageEncoding="utf-8" description="The &lt;div&gt; HTML element with Angular ListView directive wrapper"%>
<%@ tag import="org.nlh4j.util.DateUtils"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="groupCssClass" required="false"
description="Whole component area CSS class"%>
<%@ attribute name="listLabel" required="false"
description="Component label value"%>
<%@ attribute name="listLabelCssClass" required="false"
description="Component label CSS class"%>
<%@ attribute name="listName" required="false"
description="Component HTML element identity, name"%>
<%@ attribute name="listDescription" required="false"
description="Component list description has been shown as table tooltip"%>
<%@ attribute name="listModelsList" required="true"
description="Specify angular repeat data list (array of JSON object) to shown data list (ng-repeat)"%>
<%@ attribute name="listModel" required="false"
description="Specify angular model to save the selected item on list (ng-model - usually not using)"%>
<%@ attribute name="listModelOptions" required="false"
description="Specify angular model options to save the selected item on list (ng-model-options - usually not using)"%>
<%@ attribute name="listHideHeader" required="false"
description="Specify list view whether should hide column headers (data-hide-header)"%>
<%@ attribute name="listColumns" required="true"
description="Specify list columns by format [JSON item property 1 name=column 1 title]|[JSON item property 2 name=column 2 title]|etc."%>
<%@ attribute name="listTemplate" required="true"
description="Specify list view template to render (default template by framework, so no need using it)"%>
<%@ attribute name="listReadonly" required="false"
description="Specify list view is read-only. If read-only, events has not been fired."%>
<%@ attribute name="listSortable" required="false"
description="Specify list view whether could be sorted columns (data-sortable)"%>
<%@ attribute name="listRemoveIfMovedOut" required="false"
description="Specify list view whether should remove item if dragging out (data-allow-remove-if-moved-out) - required listSortable is true"%>
<%@ attribute name="listDisabled" required="false"
description="Specify list view is disabled (ng-disabled)"%>
<%@ attribute name="listRequired" required="false"
description="Specify lis view whether required selected value (ng-required - usually not using by not using ng-model)"%>
<%@ attribute name="listNgIf" required="false"
description="Specify list view whether has been rendered (ng-if)"%>
<%@ attribute name="listNgShow" required="false"
description="Specify list view whether has been shown (ng-show)"%>
<%@ attribute name="onGetValue" required="false"
description="Angular block script or function that has been used to parse cell value if using custom column"%>
<%@ attribute name="onSelect" required="false"
description="Angular block script or function has been called when an item has been selected"%>
<%@ attribute name="onDbClick" required="false"
description="Angular block script or function has been called when double clicking on an item"%>
<%@ attribute name="onChange" required="false"
description="Angular block script or function has been called when a checkbox item has been changed state"%>
<%@ attribute name="onListeners" required="false"
description="Angular block script or function has been called when an item has been dragged/dropped"%>
<%@ attribute name="listUniqueAction" required="false"
description="The URL to post to check unique value"%>
<%@ attribute name="listUniqueConditions" required="false"
description="Angular block script or function that has been used to check conditions that allows posting unique"%>
<%@ attribute name="listUniqueActionParameters" required="false"
description="Angular block script or function that has been used to parse more parameters while posting"%>
<%@ attribute name="listUniqueWaitConditions" required="false"
description="Angular block script or function specify whenever the waiting unique action should be shown"%>
<%@ attribute name="listUniqueWaitCssClass" required="false"
description="The unique waiting area CSS class"%>
<%@ attribute name="listTooltip" required="false"
description="Component tooltip (usually using for showing error tip)"%>
<%@ attribute name="listTooltipPlacement" required="false"
description="The placement that tooltip should be shown such as top, bottom, etc."%>
<%@ attribute name="listTooltipTrigger" required="false"
description="The trigger for showing tooltip such as click, mouseenter, etc."%>

<c:if test="${empty listName || fn:length(listName) <= 0}">
<%
	long tm = DateUtils.currentTimestamp().getTime();
	listName = "listview-" + String.valueOf(tm);
%>
</c:if>
<%	listName = listName.replace("\"", "_")
		.replace("'", "_").replace("[", "_").replace("]", "_")
		.replace("\\", "_").replace("\n", "_").replace("<", "_")
		.replace(">", "_").replace("&", "_").replace("#", "_")
		.replace("$", "_"); %>

<div class="form-group has-feedback ${groupCssClass}"
<c:if test="${listNgIf eq true}">ng-if="true"</c:if>
<c:if test="${not empty listNgIf && listNgIf['class'].simpleName ne 'Boolean'}">ng-if="${listNgIf}"</c:if>
<c:if test="${listNgShow eq true}">ng-show="true"</c:if>
<c:if test="${not empty listNgShow && listNgShow['class'].simpleName ne 'Boolean'}">ng-show="${listNgShow}"</c:if>
>
	<c:if test="${not empty listLabel}">
		<label
		class="control-label<c:if test="${listRequired eq true}"> required</c:if><c:if test="${not empty listRequired && listRequired['class'].simpleName ne 'Boolean'}">{{${listRequired} ? ' required' : ''}}</c:if> ${(not empty listLabelCssClass ? listLabelCssClass : listName)}"
		<c:if test="${listNgIf eq true}">ng-if="true"</c:if>
		<c:if test="${not empty listNgIf && listNgIf['class'].simpleName ne 'Boolean'}">ng-if="${listNgIf}"</c:if>
		<c:if test="${listNgShow eq true}">ng-show="true"</c:if>
		<c:if test="${not empty listNgShow && listNgShow['class'].simpleName ne 'Boolean'}">ng-show="${listNgShow}"</c:if>
		>${listLabel}</label>
	</c:if>
	<div
	<c:if test="${not empty listName}">id="${listName}" listviewId="${listName}"</c:if>
	class="${listCssClass}"
	data-listview
	data-items="${listModelsList}"
	<c:if test="${not empty listModel}">data-ng-model="${listModel}" reference-ng-model-controller</c:if>
	<c:if test="${not empty listModel && not empty listModelOptions}">ng-model-options="${listModelOptions}"</c:if>
	data-item-description="${listDescription}"
	data-columns="${listColumns}"
	data-get-value="${onGetValue}"
	data-template-base="${listTemplate}"
	<c:if test="${not empty listHideHeader}">data-hide-header="${listHideHeader}"</c:if>
	<c:if test="${listSortable eq true}">data-sortable="true"</c:if>
	<c:if test="${not empty listSortable && listSortable['class'].simpleName ne 'Boolean'}">data-sortable="${listSortable}"</c:if>
	<c:if test="${listSortable eq true && not empty listRemoveIfMovedOut}">allow-remove-if-moved-out="${listRemoveIfMovedOut}"</c:if>
	<c:if test="${listDisabled eq true}">ng-disabled="true"</c:if>
	<c:if test="${not empty listDisabled && listDisabled['class'].simpleName ne 'Boolean'}">ng-disabled="${listDisabled}"</c:if>
	<c:if test="${listRequired eq true}">ng-required="true"</c:if>
	<c:if test="${not empty listRequired && listRequired['class'].simpleName ne 'Boolean'}">ng-required="${listRequired}"</c:if>
	<c:if test="${listNgIf eq true}">ng-if="true"</c:if>
	<c:if test="${not empty listNgIf && listNgIf['class'].simpleName ne 'Boolean'}">ng-if="${listNgIf}"</c:if>
	<c:if test="${listNgShow eq true}">ng-show="true"</c:if>
	<c:if test="${not empty listNgShow && listNgShow['class'].simpleName ne 'Boolean'}">ng-show="${listNgShow}"</c:if>
	<c:if test="${empty listReadonly || listReadonly ne true || listReadonly['class'].simpleName ne 'Boolean'}">
	data-click="${onSelect}"
	data-dbclick="${onDbClick}"
	data-change="${onChange}"
	data-listeners="${onListeners}"
	</c:if>
	<c:if test="${listReadonly eq true}">data-readonly="true"</c:if>
	<c:if test="${not empty listReadonly && listReadonly['class'].simpleName ne 'Boolean'}">data-readonly="${listReadonly}"</c:if>
	<c:if test="${not empty listUniqueAction && not empty listUniqueActionParameters}">
	ng-unique
	ng-unique-action="${listUniqueAction}"
	ng-unique-param="${listUniqueActionParameters}"
	</c:if>
	<c:if test="${not empty listUniqueAction && empty listUniqueActionParameters}">
	ng-unique
	ng-unique-action="${listUniqueAction}"
	</c:if>
	<c:if test="${not empty listUniqueAction && not empty listUniqueConditions}">
	ng-unique-conditions="${listUniqueConditions}"
	</c:if>
	<c:if test="${not empty listTooltip}">data-tooltip="${listTooltip}"</c:if>
	<c:if test="${not empty listTooltipPlacement}">data-tooltip-placement="${listTooltipPlacement}"</c:if>
	<c:if test="${not empty listTooltipTrigger}">data-tooltip-trigger="${listTooltipTrigger}"</c:if>
	><!-- Destination content --></div>

	<%-- Unique loading --%>
	<c:if test="${not empty listUniqueAction && not empty listUniqueWaitConditions}">
	<div class="validate-waiting"
	data-ng-show="${listUniqueWaitConditions}">
		<i class="${not empty listUniqueWaitCssClass ? listUniqueWaitCssClass : 'fa-li fa fa-spinner fa-spin'}"></i>
	</div>
	</c:if>

	<%-- Add more content into text group if necessary --%>
	<jsp:doBody />
</div>
