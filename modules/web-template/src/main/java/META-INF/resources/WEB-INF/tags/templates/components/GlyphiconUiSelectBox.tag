<%@ tag pageEncoding="utf-8" description="The angular &lt;ui-select&gt; component wrapper"%>
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
<%@ attribute name="selectModel" required="true"
description="Specify angular model to save the selected item on list (ng-model)"%>
<%@ attribute name="selectModelOptions" required="false"
description="Specify angular model options to save the selected item on list (ng-model-options)"%>
<%@ attribute name="selectModelKey" required="true"
description="Specify model (JSON object) property name to apply as select option value"%>
<%@ attribute name="selectModelValue" required="true"
description="Specify model (JSON object) property name to apply as select option text"%>
<%@ attribute name="selectHideModelKey" required="false"
description="Specify showing model (JSON object) property key value on item UI"%>
<%@ attribute name="selectModelGlyphiconCssClass" required="false"
description="Specify CSS class to apply for item icon"%>
<%@ attribute name="selectDisabled" required="false"
description="Specify component is disabled (ng-disabled)"%>
<%@ attribute name="selectRequired" required="false"
description="Specify component is required (ng-required)"%>
<%@ attribute name="selectValidateOnProp" required="false"
description="Specify component is required on property (validate-on-prop)"%>
<%@ attribute name="selectNgIf" required="false"
description="Specify component whether has been rendered (ng-if)"%>
<%@ attribute name="selectNgShow" required="false"
description="Specify component whether has been shown (ng-show)"%>
<%@ attribute name="selectAppendToBody" required="false"
description="Specify component drop-down whether has been generated in body ontainer or present element"%>
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
<%@ attribute name="onSelect" required="false"
description="Angular script block or function has been called while an item has been selected"%>

<c:if test="${empty selectName || fn:length(selectName) <= 0}">
<%
	long tm = DateUtils.currentTimestamp().getTime();
	selectName = "ui-select-" + String.valueOf(tm);
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
	<ui-select
	class="${selectCssClass}"
	<c:if test="${not empty selectName}">id="${selectName}" name="${selectName}"</c:if>
	<c:if test="${not empty selectModel}">ng-model="${selectModel}" reference-ng-model-controller</c:if>
	<c:if test="${not empty selectModel && not empty selectModelOptions}">ng-model-options="${selectModelOptions}"</c:if>
	allow-clear="true"
	<c:if test="${not empty selectPlaceHolder}">placeholder="${selectPlaceHolder}"</c:if>
	theme="select2"
	<c:if test="${selectAppendToBody eq true}">data-append-to-body="true"</c:if>
	<c:if test="${not empty selectAppendToBody && selectAppendToBody['class'].simpleName ne 'Boolean'}">data-append-to-body="${selectAppendToBody}"</c:if>
	<c:if test="${selectDisabled eq true}">ng-disabled="true"</c:if>
	<c:if test="${not empty selectDisabled && selectDisabled['class'].simpleName ne 'Boolean'}">ng-disabled="${selectDisabled}"</c:if>
	<c:if test="${selectRequired eq true}">ng-required="true" ui-select-required required-on-attribute="${selectModelKey}"</c:if>
	<c:if test="${not empty selectRequired && selectRequired['class'].simpleName ne 'Boolean'}">ng-required="${selectRequired}" ui-select-required required-on-attribute="${selectModelKey}"</c:if>
	<c:if test="${not empty selectValidateOnProp}">validate-on-prop="${selectValidateOnProp}"</c:if>
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
	<c:if test="${not empty onSelect}">on-select="${onSelect}"</c:if>
	>
		<ui-select-match data-placeholder="${selectPlaceHolder}">
			<div class="input-group item">
				<c:if test="${not empty selectModelGlyphiconCssClass}">
				<div class="icon" title="{{$select.selected.${selectModelValue}}}"
				ng-if="$select.selected && $select.selected.${selectModelKey} && $select.selected.${selectModelKey}.length">
					<i class="glyphicon ${selectModelGlyphiconCssClass}"></i>
				</div>
				</c:if>
				<c:if test="${not empty selectHideModelKey && selectHideModelKey ne true}">
				<div data-ng-bind-html="$select.selected.${selectModelKey} | highlight: $select.search"
				class="code" title="{{$select.selected.${selectModelKey}}}"
				ng-if="$select.selected && $select.selected.${selectModelKey} && $select.selected.${selectModelKey}.length"></div>
				<div data-ng-bind-html="$select.selected.${selectModelValue} | highlight: $select.search"
				class="name" title="{{$select.selected.${selectModelValue}}}"
				ng-if="$select.selected && $select.selected.${selectModelValue} && $select.selected.${selectModelValue}.length"></div>
				</c:if>
				<c:if test="${empty selectHideModelKey || selectHideModelKey eq true}">
				<div data-ng-bind-html="$select.selected.${selectModelValue} | highlight: $select.search"
				class="name no-code" title="{{$select.selected.${selectModelValue}}}"
				ng-if="$select.selected && $select.selected.${selectModelValue} && $select.selected.${selectModelValue}.length"></div>
				</c:if>
			</div>
		</ui-select-match>
		<c:if test="${not empty selectHideModelKey && selectHideModelKey ne true}">
		<ui-select-choices repeat="operator in ${selectModelsList} | propsFilter: { filterKey: '${selectName}', expression: { ${selectModelKey}: $select.search, ${selectModelValue}: $select.search } }" refresh-deplay="0">
		</c:if>
		<c:if test="${empty selectHideModelKey || selectHideModelKey eq true}">
		<ui-select-choices repeat="operator in ${selectModelsList} | propsFilter: { filterKey: '${selectName}', expression: { ${selectModelValue}: $select.search } }" refresh-deplay="0">
		</c:if>
			<div class="input-group item">
				<c:if test="${not empty selectModelGlyphiconCssClass}">
				<div class="icon" title="{{operator.${selectModelValue}}}"
				ng-if="operator.${selectModelKey} && operator.${selectModelKey}.length">
					<i class="glyphicon ${selectModelGlyphiconCssClass}"></i>
				</div>
				</c:if>
				<c:if test="${not empty selectHideModelKey && selectHideModelKey ne true}">
				<div data-ng-bind-html="operator.${selectModelKey} | highlight: $select.search"
				class="code" title="{{operator.${selectModelKey}}}"
				ng-if="operator.${selectModelKey} && operator.${selectModelKey}.length"></div>
				<div data-ng-bind-html="operator.${selectModelValue} | highlight: $select.search"
				class="name" title="{{operator.${selectModelValue}}}"
				ng-if="operator.${selectModelValue} && operator.${selectModelValue}.length"></div>
				</c:if>
				<c:if test="${empty selectHideModelKey || selectHideModelKey eq true}">
				<div data-ng-bind-html="operator.${selectModelValue} | highlight: $select.search"
				class="name no-code" title="{{operator.${selectModelValue}}}"
				ng-if="operator.${selectModelValue} && operator.${selectModelValue}.length"></div>
				</c:if>
			</div>
		</ui-select-choices>
	</ui-select>

	<%-- Unique loading --%>
	<c:if test="${not empty selectUniqueAction && not empty selectUniqueWaitConditions}">
	<div class="validate-waiting"
	ng-show="${selectUniqueWaitConditions}">
		<i class="${not empty selectUniqueWaitCssClass ? selectUniqueWaitCssClass : 'fa-li fa fa-spinner fa-spin'}"></i>
	</div>
	</c:if>

	<%-- Add more content into text group if necessary --%>
	<jsp:doBody />
</div>
