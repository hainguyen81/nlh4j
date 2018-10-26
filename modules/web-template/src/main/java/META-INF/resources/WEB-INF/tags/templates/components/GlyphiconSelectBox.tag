<%@ tag pageEncoding="utf-8" description="The &lt;select&gt; HTML element wrapper"%>
<%@ tag import="org.nlh4j.util.DateUtils"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="groupCssClass" required="false"
description="Whole component area CSS class"%>
<%@ attribute name="selectLabel" required="false"
description="Component label value"%>
<%@ attribute name="selectLabelCssClass" required="false"
description="Component label CSS class"%>
<%@ attribute name="selectName" required="false"
description="Component HTML element identity, name"%>
<%@ attribute name="selectPlaceHolder" required="false"
description="Main select box place-holder"%>
<%@ attribute name="selectCssClass" required="false"
description="Main select box CSS class"%>
<%@ attribute name="selectModelsList" required="true"
description="Specify angular repeat data list (array of JSON object) to shown data list (ng-repeat)"%>
<%@ attribute name="useNgOptions" required="true"
description="Specify whether using ng-options"%>
<%@ attribute name="selectModel" required="true"
description="Specify angular model to save the selected item on list (ng-model)"%>
<%@ attribute name="selectModelOptions" required="false"
description="Specify angular model options to save the selected item on list (ng-model-options)"%>
<%@ attribute name="selectModelKey" required="true"
description="Specify model (JSON object) property name to apply as select option value"%>
<%@ attribute name="selectModelValue" required="true"
description="Specify model (JSON object) property name to apply as select option text"%>
<%@ attribute name="selectDisabled" required="false"
description="Specify component is disabled (ng-disabled)"%>
<%@ attribute name="selectRequired" required="false"
description="Specify component is required (ng-required)"%>
<%@ attribute name="selectNgIf" required="false"
description="Specify component whether has been rendered (ng-if)"%>
<%@ attribute name="selectNgShow" required="false"
description="Specify component whether has been shown (ng-show)"%>
<%@ attribute name="selectUniqueAction" required="false"
description="The URL to post to check unique value"%>
<%@ attribute name="selectUniqueConditions" required="false"
description="Angular block script or function that has been used to check conditions that allows posting unique"%>
<%@ attribute name="selectUniqueActionParameters" required="false"
description="Angular block script or function that has been used to parse more parameters while posting"%>
<%@ attribute name="selectUniqueWaitConditions" required="false"
description="Angular block script or function specify whenever the waiting unique action should be shown"%>
<%@ attribute name="selectUniqueWaitCssClass" required="false"
description="The unique waiting area CSS class"%>
<%@ attribute name="selectTooltip" required="false"
description="Component tooltip (usually using for showing error tip)"%>
<%@ attribute name="selectTooltipPlacement" required="false"
description="The placement that tooltip should be shown such as top, bottom, etc."%>
<%@ attribute name="selectTooltipTrigger" required="false"
description="The trigger for showing tooltip such as click, mouseenter, etc."%>
<%@ attribute name="onChange" required="false"
description="Angular script block or function has been called while an selected item has been changed (ng-change)"%>

<c:if test="${empty selectName || fn:length(selectName) <= 0}">
<%
	long tm = DateUtils.currentTimestamp().getTime();
	selectName = "selectbox-" + String.valueOf(tm);
%>
</c:if>
<%	selectName = selectName.replace("\"", "_")
		.replace("'", "_").replace("[", "_").replace("]", "_")
		.replace("\\", "_").replace("\n", "_").replace("<", "_")
		.replace(">", "_").replace("&", "_").replace("#", "_")
		.replace("$", "_"); %>

<div class="form-group has-feedback ${groupCssClass}"
<c:if test="${selectNgIf eq true}">ng-if="true"</c:if>
<c:if test="${not empty selectNgIf && selectNgIf['class'].simpleName ne 'Boolean'}">ng-if="${selectNgIf}"</c:if>
<c:if test="${selectNgShow eq true}">ng-show="true"</c:if>
<c:if test="${not empty selectNgShow && selectNgShow['class'].simpleName ne 'Boolean'}">ng-show="${selectNgShow}"</c:if>
>
	<c:if test="${not empty selectLabel}">
		<label
		class="control-label<c:if test="${selectRequired eq true}"> required</c:if><c:if test="${not empty selectRequired && selectRequired['class'].simpleName ne 'Boolean'}">{{${selectRequired} ? ' required' : ''}}</c:if> ${(not empty selectLabelCssClass ? selectLabelCssClass : selectName)}"
		<c:if test="${selectNgIf eq true}">ng-if="true"</c:if>
		<c:if test="${not empty selectNgIf && selectNgIf['class'].simpleName ne 'Boolean'}">ng-if="${selectNgIf}"</c:if>
		<c:if test="${selectNgShow eq true}">ng-show="true"</c:if>
		<c:if test="${not empty selectNgShow && selectNgShow['class'].simpleName ne 'Boolean'}">ng-show="${selectNgShow}"</c:if>
		>${selectLabel}</label>
	</c:if>
	<select
	class="${selectCssClass}"
	<c:if test="${not empty selectName}">id="${selectName}" name="${selectName}"</c:if>
	<c:if test="${not empty selectModel}">ng-model="${selectModel}" reference-ng-model-controller</c:if>
	<c:if test="${not empty selectModel && not empty selectModelOptions}">ng-model-options="${selectModelOptions}"</c:if>
	<c:if test="${not empty selectPlaceHolder}">placeholder="${selectPlaceHolder}"</c:if>
	<c:if test="${selectDisabled eq true}">ng-disabled="true"</c:if>
	<c:if test="${not empty selectDisabled && selectDisabled['class'].simpleName ne 'Boolean'}">ng-disabled="${selectDisabled}"</c:if>
	<c:if test="${selectRequired eq true}">ng-required="true" ui-select-required required-on-attribute="${selectModelKey}"</c:if>
	<c:if test="${not empty selectRequired && selectRequired['class'].simpleName ne 'Boolean'}">ng-required="${selectRequired}"</c:if>
	<c:if test="${selectNgIf eq true}">ng-if="true"</c:if>
	<c:if test="${not empty selectNgIf && selectNgIf['class'].simpleName ne 'Boolean'}">ng-if="${selectNgIf}"</c:if>
	<c:if test="${selectNgShow eq true}">ng-show="true"</c:if>
	<c:if test="${not empty selectNgShow && selectNgShow['class'].simpleName ne 'Boolean'}">ng-show="${selectNgShow}"</c:if>
	<c:if test="${not empty selectUniqueAction && not empty selectUniqueActionParameters}">
	ng-unique
	ng-unique-action="${selectUniqueAction}"
	ng-unique-param="${selectUniqueActionParameters}"
	</c:if>
	<c:if test="${not empty selectUniqueAction && empty selectUniqueActionParameters}">
	ng-unique
	ng-unique-action="${selectUniqueAction}"
	</c:if>
	<c:if test="${not empty selectUniqueAction && not empty selectUniqueConditions}">
	ng-unique-conditions="${selectUniqueConditions}"
	</c:if>
	<c:if test="${not empty selectTooltip}">data-tooltip="${selectTooltip}"</c:if>
	<c:if test="${not empty selectTooltipPlacement}">data-tooltip-placement="${selectTooltipPlacement}"</c:if>
	<c:if test="${not empty selectTooltipTrigger}">data-tooltip-trigger="${selectTooltipTrigger}"</c:if>
	<c:if test="${not empty onChange}">ng-change="${onChange}"</c:if>
	<c:if test="${useNgOptions eq true}">ng-options="item.${selectModelValue} for item in ${selectModelsList} track by item.${selectModelKey}"</c:if>
	>
		<c:if test="${useNgOptions ne true}">
			<c:if test="${selectRequired}"><option>${selectPlaceHolder}</option></c:if>
			<option value="data.${selectModelKey}" ng-repeat="data in ${selectModelsList}" ng-cloak>{{data.${selectModelValue}}</option>
		</c:if>
	</select>

	<%-- Unique loading --%>
	<c:if test="${not empty selectUniqueAction && not empty selectUniqueWaitConditions}">
	<div class="validate-waiting"
	data-ng-show="${selectUniqueWaitConditions}">
		<i class="${not empty selectUniqueWaitCssClass ? selectUniqueWaitCssClass : 'fa-li fa fa-spinner fa-spin'}"></i>
	</div>
	</c:if>

	<%-- Add more content into text group if necessary --%>
	<jsp:doBody />
</div>
