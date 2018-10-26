<%@ tag pageEncoding="utf-8" description="The &lt;input&gt; HTML element wrapper for inputting number only"%>
<%@ tag import="org.nlh4j.util.DateUtils"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="groupCssClass" required="false"
description="Whole component area CSS class"%>
<%@ attribute name="textLabel" required="false"
description="Component label value"%>
<%@ attribute name="textLabelCssClass" required="false"
description="Component label CSS class"%>
<%@ attribute name="textName" required="false"
description="Component HTML element identity, name"%>
<%@ attribute name="textPlaceHolder" required="false"
description="Main input text place-holder"%>
<%@ attribute name="textCssClass" required="false"
description="Main input text CSS class"%>
<%@ attribute name="textInitValue" required="false"
description="Main input text initial value"%>
<%@ attribute name="textModel" required="true"
description="Specify angular model (ng-model)"%>
<%@ attribute name="textModelOptions" required="false"
description="Specify angular model options (ng-model-options)"%>
<%@ attribute name="textReadonly" required="false"
description="Specify component is read-only (ng-readony)"%>
<%@ attribute name="textDisabled" required="false"
description="Specify component is disabled (ng-disabled)"%>
<%@ attribute name="textMaxLength" required="false"
description="Specify the maximum characters number that could be inputted"%>
<%@ attribute name="textMinLength" required="false"
description="Specify the minimum required characters number that must be inputted"%>
<%@ attribute name="textRequired" required="false"
description="Specify component is required (ng-required)"%>
<%@ attribute name="textNgIf" required="false"
description="Specify component whether has been rendered (ng-if)"%>
<%@ attribute name="textNgShow" required="false"
description="Specify component whether has been shown (ng-show)"%>
<%@ attribute name="textAllowEmpty" required="false"
description="Specify component allows inputting empty value (data-allow-empty)"%>
<%@ attribute name="textHideGrouping" required="false"
description="Specify component show value under number format pattern that not using group separator such as 10,000 --> 10000 (data-hide-grouping)"%>
<%@ attribute name="textNegative" required="false"
description="Specify component allows inputting negative number (data-allow-negative)"%>
<%@ attribute name="textDecimal" required="false"
description="Specify component allows inputting decimal number (data-allow-decimal)"%>
<%@ attribute name="textDecimalNumbers" required="false"
description="Specify the number of decimal parts such as 1.02 (decimal numbers is 2) (data-decimal-numbers)"%>
<%@ attribute name="textPositiveNumbers" required="false"
description="Specify the number of positive parts such as 1.02 (positive numbers is 1) (data-positive-numbers)"%>
<%@ attribute name="textGreaterThan" required="false"
description="Specify validating the number value must equals or greater than another value (data-ng-greater-than)"%>
<%@ attribute name="textGreaterEquals" required="false"
description="Should validate the number value equals with another value ? (data-ng-greater-equals)"%>
<%@ attribute name="textGreaterValue" required="false"
description="The value to compare (data-ng-greater-compare)"%>
<%@ attribute name="textLessThan" required="false"
description="Specify validating the number value must equals or less than another value (data-ng-less-than)"%>
<%@ attribute name="textLessEquals" required="false"
description="Should validate the number value equals with another value ? (data-ng-less-equals)"%>
<%@ attribute name="textLessValue" required="false"
description="The value to compare (data-ng-less-compare)"%>
<%@ attribute name="textUniqueAction" required="false"
description="The URL to post to check unique value"%>
<%@ attribute name="textUniqueConditions" required="false"
description="Angular block script or function that has been used to check conditions that allows posting unique"%>
<%@ attribute name="textUniqueActionParameters" required="false"
description="Angular block script or function that has been used to parse more parameters while posting"%>
<%@ attribute name="textUniqueWaitConditions" required="false"
description="Angular block script or function specify whenever the waiting unique action should be shown"%>
<%@ attribute name="textUniqueWaitCssClass" required="false"
description="The unique waiting area CSS class"%>
<%@ attribute name="textTooltip" required="false"
description="Component tooltip (usually using for showing error tip)"%>
<%@ attribute name="textTooltipPlacement" required="false"
description="The placement that tooltip should be shown such as top, bottom, etc."%>
<%@ attribute name="textTooltipTrigger" required="false"
description="The trigger for showing tooltip such as click, mouseenter, etc."%>
<%@ attribute name="onChange" required="false"
description="Angular block script or function will be called when text has been changed (ng-change)"%>
<%@ attribute name="onBlur" required="false"
description="Angular block script or function will be called when text has been blured (ng-blur)"%>

<c:if test="${empty textName || fn:length(textName) <= 0}">
<%
	long tm = DateUtils.currentTimestamp().getTime();
	textName = "number-textbox-" + String.valueOf(tm);
%>
</c:if>
<%	textName = textName.replace("\"", "_")
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
	<c:if test="${not empty textLabel}">
	<label
	class="control-label<c:if test="${textRequired eq true}"> required</c:if><c:if test="${not empty textRequired && textRequired['class'].simpleName ne 'Boolean'}">{{${textRequired} ? ' required' : ''}}</c:if> ${(not empty textLabelCssClass ? textLabelCssClass : textName)}"
	<c:if test="${textNgIf eq true}">ng-if="true"</c:if>
	<c:if test="${not empty textNgIf && textNgIf['class'].simpleName ne 'Boolean'}">ng-if="${textNgIf}"</c:if>
	<c:if test="${textNgShow eq true}">ng-show="true"</c:if>
	<c:if test="${not empty textNgShow && textNgShow['class'].simpleName ne 'Boolean'}">ng-show="${textNgShow}"</c:if>
	>${textLabel}</label>
	</c:if>
	<input type="text"
	<c:if test="${not empty textName}">id="${textName}" name="${textName}"</c:if>
	class="form-control ${textCssClass}"
	<c:if test="${not empty textInitValue}">value="${textInitValue}"</c:if>
	<c:if test="${not empty textModel}">ng-model="${textModel}" reference-ng-model-controller</c:if>
	<c:if test="${not empty textModel && not empty textModelOptions}">ng-model-options="${textModelOptions}"</c:if>
	<c:if test="${not empty textPlaceHolder}">placeholder="${textPlaceHolder}"</c:if>
	ng-trim="true"
	data-only-number
	<c:if test="${textReadonly eq true}">ng-readonly="true"</c:if>
	<c:if test="${not empty textReadonly && textReadonly['class'].simpleName ne 'Boolean'}">ng-readonly="${textReadonly}"</c:if>
	<c:if test="${textDisabled eq true}">ng-disabled="true"</c:if>
	<c:if test="${not empty textDisabled && textDisabled['class'].simpleName ne 'Boolean'}">ng-disabled="${textDisabled}"</c:if>
	<c:if test="${textRequired eq true}">ng-required="true"</c:if>
	<c:if test="${not empty textRequired && textRequired['class'].simpleName ne 'Boolean'}">ng-required="${textRequired}"</c:if>
	<c:if test="${not empty textMinLength}">ng-minlength="${textMinLength}"</c:if>
	<c:if test="${not empty textMaxLength}">maxlength="${textMaxLength}"</c:if>
	<c:if test="${not empty textAllowEmpty}">data-allow-empty="${textAllowEmpty}"</c:if>
	<c:if test="${not empty textHideGrouping}">data-hide-grouping="${textHideGrouping}"</c:if>
	<c:if test="${not empty textNegative}">data-allow-negative="${textNegative}"</c:if>
	<c:if test="${not empty textDecimal}">data-allow-decimal="${textDecimal}"</c:if>
	<c:if test="${not empty textDecimal && not empty textDecimalNumbers}">data-decimal-numbers="${textDecimalNumbers}"</c:if>
	<c:if test="${not empty textPositiveNumbers}">data-positive-numbers="${textPositiveNumbers}"</c:if>
	<c:if test="${textGreaterThan eq true}">data-ng-greater-than</c:if>
	<c:if test="${textGreaterThan eq true && not empty textGreaterEquals}">data-ng-greater-equals="${textGreaterEquals}"</c:if>
	<c:if test="${textGreaterThan eq true && not empty textGreaterValue}">data-ng-greater-compare="${textGreaterValue}"</c:if>
	<c:if test="${textLessThan eq true}">data-ng-less-than</c:if>
	<c:if test="${textLessThan eq true && not empty textLessEquals}">data-ng-less-equals="${textLessEquals}"</c:if>
	<c:if test="${textLessThan eq true && not empty textLessValue}">data-ng-less-compare="${textLessValue}"</c:if>
	<c:if test="${not empty textUniqueAction && not empty textUniqueActionParameters}">
	ng-unique
	ng-unique-action="${textUniqueAction}"
	ng-unique-param="${textUniqueActionParameters}"
	</c:if>
	<c:if test="${not empty textUniqueAction && empty textUniqueActionParameters}">
	ng-unique
	ng-unique-action="${textUniqueAction}"
	</c:if>
	<c:if test="${not empty textUniqueAction && not empty textUniqueConditions}">
	ng-unique-conditions="${textUniqueConditions}"
	</c:if>
	<c:if test="${not empty textTooltip}">data-tooltip="${textTooltip}"</c:if>
	<c:if test="${not empty textTooltipPlacement}">data-tooltip-placement="${textTooltipPlacement}"</c:if>
	<c:if test="${not empty textTooltipTrigger}">data-tooltip-trigger="${textTooltipTrigger}"</c:if>
	<c:if test="${not empty onChange}">ng-change="${onChange}"</c:if>
	<c:if test="${not empty onBlur}">ng-blur="${onBlur}"</c:if>
	>

	<%-- Unique loading --%>
	<c:if test="${not empty textUniqueAction && not empty textUniqueWaitConditions}">
	<div class="validate-waiting"
	data-ng-show="${textUniqueWaitConditions}">
		<i class="${not empty textUniqueWaitCssClass ? textUniqueWaitCssClass : 'fa-li fa fa-spinner fa-spin'}"></i>
	</div>
	</c:if>

	<%-- Add more content into text group if necessary --%>
	<jsp:doBody />
</div>
