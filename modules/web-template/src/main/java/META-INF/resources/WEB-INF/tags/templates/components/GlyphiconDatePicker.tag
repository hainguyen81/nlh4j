<%@ tag pageEncoding="utf-8" description="The &lt;input&gt; HTML element with jQuery DateTimePicker wrapper"%>
<%@ tag import="org.nlh4j.util.DateUtils"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="groupCssClass" required="false"
description="Whole component area CSS class"%>
<%@ attribute name="dtpLabel" required="false"
description="Component label value"%>
<%@ attribute name="dtpLabelCssClass" required="false"
description="Component label CSS class"%>
<%@ attribute name="dtpName" required="false"
description="Component HTML element identity, name"%>
<%@ attribute name="dtpRequired" required="false"
description="Specify component is required (ng-required)"%>
<%@ attribute name="dtpNgIf" required="false"
description="Specify component whether has been rendered (ng-if)"%>
<%@ attribute name="dtpNgShow" required="false"
description="Specify component whether has been shown (ng-show)"%>
<%@ attribute name="dtpCssClass" required="false"
description="Main input text CSS class"%>
<%@ attribute name="dtpPlaceHolder" required="false"
description="Main input text place-holder"%>
<%@ attribute name="dtpAppendToBody" required="false"
description="Specify dropdown calendar whether has been rendered in body HTML tag or current component area (for overlap cases)"%>
<%@ attribute name="dtpModel" required="true"
description="Specify angular model (ng-model)"%>
<%@ attribute name="dtpModelOptions" required="false"
description="Specify angular model options (ng-model-options)"%>
<%@ attribute name="dtpPattern" required="true"
description="Specify date/time pattern of picker"%>
<%@ attribute name="dtpOptions" required="true"
description="Specify more date/time picker options (JSON object) such as min-date, max-date, etc."%>
<%@ attribute name="dtpTodayText" required="false"
description="Specify today button caption"%>
<%@ attribute name="dtpCloseText" required="false"
description="Specify close button caption"%>
<%@ attribute name="dtpClearText" required="false"
description="Specify clear button caption"%>
<%@ attribute name="dtpReadonly" required="false"
description="Specify component is read-only (ng-readonly)"%>
<%@ attribute name="dtpUniqueAction" required="false"
description="The URL to post to check unique value"%>
<%@ attribute name="dtpUniqueConditions" required="false"
description="Angular block script or function that has been used to check conditions that allows posting unique"%>
<%@ attribute name="dtpUniqueActionParameters" required="false"
description="Angular block script or function that has been used to parse more parameters while posting"%>
<%@ attribute name="dtpUniqueWaitConditions" required="false"
description="Angular block script or function specify whenever the waiting unique action should be shown"%>
<%@ attribute name="dtpUniqueWaitCssClass" required="false"
description="The unique waiting area CSS class"%>
<%@ attribute name="dtpTooltip" required="false"
description="Component tooltip (usually using for showing error tip)"%>
<%@ attribute name="dtpTooltipPlacement" required="false"
description="The placement that tooltip should be shown such as top, bottom, etc."%>
<%@ attribute name="dtpTooltipTrigger" required="false"
description="The trigger for showing tooltip such as click, mouseenter, etc."%>

<c:if test="${empty dtpName || fn:length(dtpName) <= 0}">
<%
	long tm = DateUtils.currentTimestamp().getTime();
	dtpName = "dtp-" + String.valueOf(tm);
%>
</c:if>
<%	dtpName = dtpName.replace("\"", "_")
		.replace("'", "_").replace("[", "_").replace("]", "_")
		.replace("\\", "_").replace("\n", "_").replace("<", "_")
		.replace(">", "_").replace("&", "_").replace("#", "_")
		.replace("$", "_"); %>

<div class="form-group has-feedback ${groupCssClass}"
<c:if test="${dtpNgIf eq true}">ng-if="true"</c:if>
<c:if test="${not empty dtpNgIf && dtpNgIf['class'].simpleName ne 'Boolean'}">ng-if="${dtpNgIf}"</c:if>
<c:if test="${dtpNgShow eq true}">ng-show="true"</c:if>
<c:if test="${not empty dtpNgShow && dtpNgShow['class'].simpleName ne 'Boolean'}">ng-show="${dtpNgShow}"</c:if>
>
	<c:if test="${not empty dtpLabel}">
		<label
		class="control-label<c:if test="${dtpRequired eq true}"> required</c:if><c:if test="${not empty dtpRequired && dtpRequired['class'].simpleName ne 'Boolean'}">{{${dtpRequired} ? ' required' : ''}}</c:if> ${(not empty dtpLabelCssClass ? dtpLabelCssClass : dtpName)}"
		<c:if test="${dtpNgIf eq true}">ng-if="true"</c:if>
		<c:if test="${not empty dtpNgIf && dtpNgIf['class'].simpleName ne 'Boolean'}">ng-if="${dtpNgIf}"</c:if>
		<c:if test="${dtpNgShow eq true}">ng-show="true"</c:if>
		<c:if test="${not empty dtpNgShow && dtpNgShow['class'].simpleName ne 'Boolean'}">ng-show="${dtpNgShow}"</c:if>
		>${dtpLabel}</label>
	</c:if>
	<div class="input-group dtpicker">
		<input type="text"
		<c:if test="${not empty dtpName}">id="${dtpName}" name="${dtpName}"</c:if>
		class="form-control ${dtpCssClass}"
		<c:if test="${not empty dtpPlaceHolder}">placeholder="${dtpPlaceHolder}"</c:if>
		<c:if test="${not empty dtpModel}">ng-model="${dtpModel}" reference-ng-model-controller</c:if>
		<c:if test="${not empty dtpModel && not empty dtpModelOptions}">ng-model-options="${dtpModelOptions}"</c:if>
        data-datepicker-popup="${dtpPattern}"
        data-is-open="findData('dtPicker').opened['${dtpName}']"
        data-datepicker-options="${dtpOptions}"
        data-current-text="${dtpTodayText}"
        data-close-text="${dtpCloseText}"
        data-clear-text="${dtpClearText}"
        data-ng-disabled="true"
        <c:if test="${dtpAppendToBody eq true}">datepicker-append-to-body="true"</c:if>
        <c:if test="${not empty dtpAppendToBody && dtpAppendToBody['class'].simpleName ne 'Boolean'}">datepicker-append-to-body="${dtpAppendToBody}"</c:if>
        <c:if test="${dtpRequired eq true}">ng-required="true"</c:if>
		<c:if test="${not empty dtpRequired && dtpRequired['class'].simpleName ne 'Boolean'}">ng-required="${dtpRequired}"</c:if>
		<c:if test="${dtpNgIf eq true}">ng-if="true"</c:if>
		<c:if test="${not empty dtpNgIf && dtpNgIf['class'].simpleName ne 'Boolean'}">ng-if="${dtpNgIf}"</c:if>
		<c:if test="${dtpNgShow eq true}">ng-show="true"</c:if>
		<c:if test="${not empty dtpNgShow && dtpNgShow['class'].simpleName ne 'Boolean'}">ng-show="${dtpNgShow}"</c:if>
		<c:if test="${not empty dtpUniqueAction && not empty dtpUniqueActionParameters}">
		ng-unique
		ng-unique-action="${dtpUniqueAction}"
		ng-unique-param="${dtpUniqueActionParameters}"
		</c:if>
		<c:if test="${not empty dtpUniqueAction && empty dtpUniqueActionParameters}">
		ng-unique
		ng-unique-action="${dtpUniqueAction}"
		</c:if>
		<c:if test="${not empty dtpUniqueAction && not empty dtpUniqueConditions}">
		ng-unique-conditions="${dtpUniqueConditions}"
		</c:if>
		<c:if test="${not empty dtpTooltip}">data-tooltip="${dtpTooltip}"</c:if>
		<c:if test="${not empty dtpTooltipPlacement}">data-tooltip-placement="${dtpTooltipPlacement}"</c:if>
		<c:if test="${not empty dtpTooltipTrigger}">data-tooltip-trigger="${dtpTooltipTrigger}"</c:if>
		/>
		<!-- Calendar button -->
		<span class="input-group-btn">
        	<button type="button" class="btn btn-calendar"
        	<c:if test="${dtpReadonly eq true}">ng-disabled="true"</c:if>
			<c:if test="${not empty dtpReadonly && dtpReadonly['class'].simpleName ne 'Boolean'}">ng-disabled="${dtpReadonly}"</c:if>
			<c:if test="${not empty dtpName}">data-ng-click="findData('dtPicker').open($event, '${dtpName}')"</c:if>
         	<c:if test="${empty dtpName}">data-ng-click="findData('dtPicker').open($event, 'dtpicker')"</c:if>
         	></button>
       	</span>
	</div>

	<%-- Unique loading --%>
	<c:if test="${not empty dtpUniqueAction && not empty dtpUniqueWaitConditions}">
	<div class="validate-waiting"
	data-ng-show="${dtpUniqueWaitConditions}">
		<i class="${not empty dtpUniqueWaitCssClass ? dtpUniqueWaitCssClass : 'fa-li fa fa-spinner fa-spin'}"></i>
	</div>
	</c:if>

	<%-- Add more content into text group if necessary --%>
	<jsp:doBody />
</div>
