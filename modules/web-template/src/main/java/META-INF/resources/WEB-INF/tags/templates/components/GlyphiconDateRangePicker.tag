<%@ tag pageEncoding="utf-8" description="The &lt;input&gt; HTML element with jQuery DateTimePicker wrapper"%>
<%@ tag import="org.nlh4j.util.DateUtils"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<%@ attribute name="groupCssClass" required="false"
description="Whole component area CSS class"%>
<%@ attribute name="dtpLabelFrom" required="false"
description="Component label (FROM) value"%>
<%@ attribute name="dtpLabelTo" required="false"
description="Component label (TO) value"%>
<%@ attribute name="dtpLabelCssClassFrom" required="false"
description="Component label (FROM) CSS class"%>
<%@ attribute name="dtpLabelCssClassTo" required="false"
description="Component label (TO) CSS class"%>
<%@ attribute name="dtpNameFrom" required="false"
description="Component HTML element (FROM) identity, name"%>
<%@ attribute name="dtpNameTo" required="false"
description="Component HTML element (TO) identity, name"%>
<%@ attribute name="dtpRequiredFrom" required="false"
description="Specify component (FROM) is required (ng-required)"%>
<%@ attribute name="dtpRequiredTo" required="false"
description="Specify component (TO) is required (ng-required)"%>
<%@ attribute name="dtpNgIf" required="false"
description="Specify component whether has been rendered (ng-if)"%>
<%@ attribute name="dtpNgShow" required="false"
description="Specify component whether has been shown (ng-show)"%>
<%@ attribute name="dtpCssClassFrom" required="false"
description="Main input text (FROM) CSS class"%>
<%@ attribute name="dtpCssClassTo" required="false"
description="Main input text (TO) CSS class"%>
<%@ attribute name="dtpPlaceHolderFrom" required="false"
description="Main input text (FROM) place-holder"%>
<%@ attribute name="dtpPlaceHolderTo" required="false"
description="Main input text (TO) place-holder"%>
<%@ attribute name="dtpAppendToBody" required="false"
description="Specify dropdown calendar whether has been rendered in body HTML tag or current component area (for overlap cases)"%>
<%@ attribute name="dtpModelFrom" required="false"
description="Specify angular model (FROM) (ng-model)"%>
<%@ attribute name="dtpInitDateFrom" required="false"
description="The initial date view when no model value is specified"%>
<%@ attribute name="dtpModelOptionsFrom" required="false"
description="Specify angular model options (FROM) (ng-model-options)"%>
<%@ attribute name="dtpModelTo" required="true"
description="Specify angular model (TO) (ng-model)"%>
<%@ attribute name="dtpInitDateTo" required="false"
description="The initial date view when no model value is specified"%>
<%@ attribute name="dtpModelOptionsTo" required="false"
description="Specify angular model options (TO) (ng-model-options)"%>
<%@ attribute name="dtpReadonlyFrom" required="false"
description="Specify component (FROM) is read-only (ng-readony)"%>
<%@ attribute name="dtpReadonlyTo" required="false"
description="Specify component (TO) is read-only (ng-readony)"%>
<%@ attribute name="dtpPattern" required="true"
description="Specify date/time pattern of picker"%>
<%@ attribute name="dtpMode" required="false"
description="Specify the mode of the datepicker (day|month|year). Can be used to initialize datepicker to specific mode. Default is 'day'"%>
<%@ attribute name="dtpShowWeeks" required="false"
description="Whether to display week numbers"%>
<%@ attribute name="dtpStartingDay" required="false"
description="Starting day of the week from 0-6 (0=Sunday, ..., 6=Saturday)"%>
<%@ attribute name="dtpOptions" required="true"
description="Specify more date/time picker options (JSON object) such as min-date, max-date, etc."%>
<%@ attribute name="dtpTodayText" required="false"
description="Specify today button caption"%>
<%@ attribute name="dtpCloseText" required="false"
description="Specify close button caption"%>
<%@ attribute name="dtpClearText" required="false"
description="Specify clear button caption"%>
<%@ attribute name="dtpUniqueActionFrom" required="false"
description="The URL to post to check unique value (FROM)"%>
<%@ attribute name="dtpUniqueConditionsFrom" required="false"
description="Angular block script or function that has been used to check conditions that allows posting unique (FROM)"%>
<%@ attribute name="dtpUniqueActionParametersFrom" required="false"
description="Angular block script or function that has been used to parse more parameters while posting (FROM)"%>
<%@ attribute name="dtpUniqueWaitConditionsFrom" required="false"
description="Angular block script or function specify whenever the waiting unique action should be shown (FROM)"%>
<%@ attribute name="dtpUniqueWaitCssClassFrom" required="false"
description="The unique waiting area CSS class (FROM)"%>
<%@ attribute name="dtpTooltipFrom" required="false"
description="Component (FROM) tooltip (usually using for showing error tip)"%>
<%@ attribute name="dtpTooltipPlacementFrom" required="false"
description="The placement that tooltip (FROM) should be shown such as top, bottom, etc."%>
<%@ attribute name="dtpTooltipTriggerFrom" required="false"
description="The trigger for showing tooltip (FROM) such as click, mouseenter, etc."%>
<%@ attribute name="dtpUniqueActionTo" required="false"
description="The URL to post to check unique value (TO)"%>
<%@ attribute name="dtpUniqueConditionsTo" required="false"
description="Angular block script or function that has been used to check conditions that allows posting unique (TO)"%>
<%@ attribute name="dtpUniqueActionParametersTo" required="false"
description="Angular block script or function that has been used to parse more parameters while posting (TO)"%>
<%@ attribute name="dtpUniqueWaitConditionsTo" required="false"
description="Angular block script or function specify whenever the waiting unique action should be shown (TO)"%>
<%@ attribute name="dtpUniqueWaitCssClassTo" required="false"
description="The unique waiting area CSS class (TO)"%>
<%@ attribute name="dtpTooltipTo" required="false"
description="Component (TO) tooltip (usually using for showing error tip)"%>
<%@ attribute name="dtpTooltipPlacementTo" required="false"
description="The placement that tooltip (TO) should be shown such as top, bottom, etc."%>
<%@ attribute name="dtpTooltipTriggerTo" required="false"
description="The trigger for showing tooltip (TO) such as click, mouseenter, etc."%>

<%
	long tm = DateUtils.currentTimestamp().getTime();
%>
<c:if test="${empty dtpNameFrom || fn:length(dtpNameFrom) <= 0}">
<%
	dtpNameFrom = "dtpf-" + String.valueOf(tm);
%>
</c:if>
<c:if test="${empty dtpNameTo || fn:length(dtpNameTo) <= 0}">
<%
	dtpNameTo = "dtpt-" + String.valueOf(tm);
%>
</c:if>
<%	dtpNameFrom = dtpNameFrom.replace("\"", "_")
		.replace("'", "_").replace("[", "_").replace("]", "_")
		.replace("\\", "_").replace("\n", "_").replace("<", "_")
		.replace(">", "_").replace("&", "_").replace("#", "_")
		.replace("$", "_");
	dtpNameTo = dtpNameTo.replace("\"", "_")
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
	<%-- FROM --%>
	<div class="input-group dtpicker-from">
		<c:if test="${not empty dtpLabelFrom}">
			<label
			class="control-label<c:if test="${dtpRequiredFrom eq true}"> required</c:if><c:if test="${not empty dtpRequiredFrom && dtpRequiredFrom['class'].simpleName ne 'Boolean'}">{{${dtpRequiredFrom} ? ' required' : ''}}</c:if> ${(not empty dtpLabelCssClassFrom ? dtpLabelCssClassFrom : dtpNameFrom)}"
			<c:if test="${dtpNgIf eq true}">ng-if="true"</c:if>
			<c:if test="${not empty dtpNgIf && dtpNgIf['class'].simpleName ne 'Boolean'}">ng-if="${dtpNgIf}"</c:if>
			<c:if test="${dtpNgShow eq true}">ng-show="true"</c:if>
			<c:if test="${not empty dtpNgShow && dtpNgShow['class'].simpleName ne 'Boolean'}">ng-show="${dtpNgShow}"</c:if>
			>${dtpLabelFrom}</label>
		</c:if>
		<div class="input-group dtpicker">
			<input type="text"
			<c:if test="${not empty dtpNameFrom}">id="${dtpNameFrom}" name="${dtpNameFrom}"</c:if>
			class="form-control ${dtpCssClassFrom}"
			<c:if test="${not empty dtpPlaceHolderFrom}">placeholder="${dtpPlaceHolderFrom}"</c:if>
			<c:if test="${not empty dtpModelFrom}">ng-model="${dtpModelFrom}" reference-ng-model-controller</c:if>
			<c:if test="${not empty dtpModelFrom && not empty dtpModelOptionsFrom}">ng-model-options="${dtpModelOptionsFrom}"</c:if>
	        data-datepicker-popup="${dtpPattern}"
	        <c:if test="${not empty dtpNameFrom}">data-is-open="findData('dtPicker').opened['${dtpNameFrom}']"</c:if>
	        <c:if test="${empty dtpNameFrom}">data-is-open="findData('dtPicker').opened['dtpicker-from']"</c:if>
			<c:if test="${not empty dtpMode}">data-datepicker-mode="${dtpMode}"</c:if>
			<c:if test="${empty dtpMode}">data-datepicker-mode="'day'"</c:if>
        	<c:if test="${not empty dtpShowWeeks && dtpShowWeeks['class'].simpleName ne 'Boolean'}">data-datepicker-mode="${dtpShowWeeks}"</c:if>
			<c:if test="${not empty dtpStartingDay}">data-starting-day="${dtpStartingDay}"</c:if>
			<c:if test="${not empty dtpInitDateFrom}">data-init-date="${dtpInitDateFrom}"</c:if>
	        data-datepicker-options="${dtpOptions}"
	        data-current-text="${dtpTodayText}"
	        data-close-text="${dtpCloseText}"
	        data-clear-text="${dtpClearText}"
	        data-ng-disabled="true"
	        <c:if test="${not empty dtpModelTo}">data-max-date="${dtpModelTo}"</c:if>
	        <c:if test="${not empty dtpModelTo && not empty dtpModelOptionsTo}">ng-model-options="${dtpModelOptionsTo}"</c:if>
	        <c:if test="${dtpAppendToBody eq true}">datepicker-append-to-body="true"</c:if>
	        <c:if test="${not empty dtpAppendToBody && dtpAppendToBody['class'].simpleName ne 'Boolean'}">datepicker-append-to-body="${dtpAppendToBody}"</c:if>
	        <c:if test="${dtpRequiredFrom eq true}">ng-required="true"</c:if>
			<c:if test="${not empty dtpRequiredFrom && dtpRequiredFrom['class'].simpleName ne 'Boolean'}">ng-required="${dtpRequiredFrom}"</c:if>
			<c:if test="${dtpNgIf eq true}">ng-if="true"</c:if>
			<c:if test="${not empty dtpNgIf && dtpNgIf['class'].simpleName ne 'Boolean'}">ng-if="${dtpNgIf}"</c:if>
			<c:if test="${dtpNgShow eq true}">ng-show="true"</c:if>
			<c:if test="${not empty dtpNgShow && dtpNgShow['class'].simpleName ne 'Boolean'}">ng-show="${dtpNgShow}"</c:if>
			<c:if test="${not empty dtpUniqueActionFrom && not empty dtpUniqueActionParametersFrom}">
			ng-unique
			ng-unique-action="${dtpUniqueActionFrom}"
			ng-unique-param="${dtpUniqueActionParametersFrom}"
			</c:if>
			<c:if test="${not empty dtpUniqueActionFrom && empty dtpUniqueActionParametersFrom}">
			ng-unique
			ng-unique-action="${dtpUniqueActionFrom}"
			</c:if>
			<c:if test="${not empty dtpUniqueActionFrom && not empty dtpUniqueConditionsFrom}">
			ng-unique-conditions="${dtpUniqueConditionsFrom}"
			</c:if>
			<c:if test="${not empty dtpTooltipFrom}">data-tooltip="${dtpTooltipFrom}"</c:if>
			<c:if test="${not empty dtpTooltipPlacementFrom}">data-tooltip-placement="${dtpTooltipPlacementFrom}"</c:if>
			<c:if test="${not empty dtpTooltipTriggerFrom}">data-tooltip-trigger="${dtpTooltipTriggerFrom}"</c:if>
			/>
			<!-- Calendar button -->
			<span class="input-group-btn">
	        	<button type="button" class="btn btn-calendar"
	         	<c:if test="${dtpReadonlyFrom eq true}">ng-disabled="true"</c:if>
				<c:if test="${not empty dtpReadonlyFrom && dtpReadonlyFrom['class'].simpleName ne 'Boolean'}">ng-disabled="${dtpReadonlyFrom}"</c:if>
				<c:if test="${not empty dtpNameFrom}">data-ng-click="findData('dtPicker').open($event, '${dtpNameFrom}')"</c:if>
	         	<c:if test="${empty dtpNameFrom}">data-ng-click="findData('dtPicker').open($event, 'dtpicker-from')"</c:if>
	         	></button>
	       	</span>
		</div>

		<%-- Unique loading --%>
		<c:if test="${not empty dtpUniqueActionFrom && not empty dtpUniqueWaitConditionsFrom}">
		<div class="validate-waiting"
		data-ng-show="${dtpUniqueWaitConditionsFrom}">
			<i class="${not empty dtpUniqueWaitCssClassFrom ? dtpUniqueWaitCssClassFrom : 'fa-li fa fa-spinner fa-spin'}"></i>
		</div>
		</c:if>
	</div>

	<%-- TO --%>
	<div class="input-group dtpicker-to">
		<c:if test="${not empty dtpLabelTo}">
			<label
			class="control-label<c:if test="${dtpRequiredTo eq true}"> required</c:if><c:if test="${not empty dtpRequiredTo && dtpRequiredTo['class'].simpleName ne 'Boolean'}">{{${dtpRequiredTo} ? ' required' : ''}}</c:if> ${(not empty dtpLabelCssClassTo ? dtpLabelCssClassTo : dtpNameTo)}"
			<c:if test="${dtpNgIf eq true}">ng-if="true"</c:if>
			<c:if test="${not empty dtpNgIf && dtpNgIf['class'].simpleName ne 'Boolean'}">ng-if="${dtpNgIf}"</c:if>
			<c:if test="${dtpNgShow eq true}">ng-show="true"</c:if>
			<c:if test="${not empty dtpNgShow && dtpNgShow['class'].simpleName ne 'Boolean'}">ng-show="${dtpNgShow}"</c:if>
			>${dtpLabelTo}</label>
		</c:if>
		<div class="input-group dtpicker">
			<input type="text"
			<c:if test="${not empty dtpNameTo}">id="${dtpNameTo}" name="${dtpNameTo}"</c:if>
			class="form-control ${dtpCssClassTo}"
			<c:if test="${not empty dtpPlaceHolderTo}">placeholder="${dtpPlaceHolderTo}"</c:if>
			<c:if test="${not empty dtpModelTo}">ng-model="${dtpModelTo}" reference-ng-model-controller</c:if>
	        data-datepicker-popup="${dtpPattern}"
	        <c:if test="${not empty dtpNameTo}">data-is-open="findData('dtPicker').opened['${dtpNameTo}']"</c:if>
	        <c:if test="${empty dtpNameTo}">data-is-open="findData('dtPicker').opened['dtpicker-to']"</c:if>
			<c:if test="${not empty dtpMode}">data-datepicker-mode="${dtpMode}"</c:if>
			<c:if test="${empty dtpMode}">data-datepicker-mode="'day'"</c:if>
        	<c:if test="${not empty dtpShowWeeks && dtpShowWeeks['class'].simpleName ne 'Boolean'}">data-datepicker-mode="${dtpShowWeeks}"</c:if>
			<c:if test="${not empty dtpStartingDay}">data-starting-day="${dtpStartingDay}"</c:if>
			<c:if test="${not empty dtpInitDateTo}">data-init-date="${dtpInitDateTo}"</c:if>
	        data-datepicker-options="${dtpOptions}"
	        data-current-text="${dtpTodayText}"
	        data-close-text="${dtpCloseText}"
	        data-clear-text="${dtpClearText}"
	        data-ng-disabled="true"
	        <c:if test="${not empty dtpModelFrom}">data-min-date="${dtpModelFrom}"</c:if>
	        <c:if test="${dtpAppendToBody eq true}">datepicker-append-to-body="true"</c:if>
	        <c:if test="${not empty dtpAppendToBody && dtpAppendToBody['class'].simpleName ne 'Boolean'}">datepicker-append-to-body="${dtpAppendToBody}"</c:if>
	        <c:if test="${dtpRequiredTo eq true}">ng-required="true"</c:if>
			<c:if test="${not empty dtpRequiredTo && dtpRequiredTo['class'].simpleName ne 'Boolean'}">ng-required="${dtpRequiredTo}"</c:if>
			<c:if test="${dtpNgIf eq true}">ng-if="true"</c:if>
			<c:if test="${not empty dtpNgIf && dtpNgIf['class'].simpleName ne 'Boolean'}">ng-if="${dtpNgIf}"</c:if>
			<c:if test="${dtpNgShow eq true}">ng-show="true"</c:if>
			<c:if test="${not empty dtpNgShow && dtpNgShow['class'].simpleName ne 'Boolean'}">ng-show="${dtpNgShow}"</c:if>
			<c:if test="${not empty dtpUniqueActionTo && not empty dtpUniqueActionParametersTo}">
			ng-unique
			ng-unique-action="${dtpUniqueActionTo}"
			ng-unique-param="${dtpUniqueActionParametersTo}"
			</c:if>
			<c:if test="${not empty dtpUniqueActionTo && empty dtpUniqueActionParametersTo}">
			ng-unique
			ng-unique-action="${dtpUniqueActionTo}"
			</c:if>
			<c:if test="${not empty dtpUniqueActionTo && not empty dtpUniqueConditionsTo}">
			ng-unique-conditions="${dtpUniqueConditionsTo}"
			</c:if>
			<c:if test="${not empty dtpTooltipTo}">data-tooltip="${dtpTooltipTo}"</c:if>
			<c:if test="${not empty dtpTooltipPlacementTo}">data-tooltip-placement="${dtpTooltipPlacementTo}"</c:if>
			<c:if test="${not empty dtpTooltipTriggerTo}">data-tooltip-trigger="${dtpTooltipTriggerTo}"</c:if>
			/>
			<!-- Calendar button -->
			<span class="input-group-btn">
	        	<button type="button" class="btn btn-calendar"
	         	<c:if test="${dtpReadonlyTo eq true}">ng-disabled="true"</c:if>
				<c:if test="${not empty dtpReadonlyTo && dtpReadonlyTo['class'].simpleName ne 'Boolean'}">ng-disabled="${dtpReadonlyTo}"</c:if>
				<c:if test="${not empty dtpNameTo}">data-ng-click="findData('dtPicker').open($event, '${dtpNameTo}')"</c:if>
	         	<c:if test="${empty dtpNameTo}">data-ng-click="findData('dtPicker').open($event, 'dtpicker-to')"</c:if>
	         	></button>
	       	</span>
		</div>

		<%-- Unique loading --%>
		<c:if test="${not empty dtpUniqueActionTo && not empty dtpUniqueWaitConditionsTo}">
		<div class="validate-waiting"
		data-ng-show="${dtpUniqueWaitConditionsTo}">
			<i class="${not empty dtpUniqueWaitCssClassTo ? dtpUniqueWaitCssClassTo : 'fa-li fa fa-spinner fa-spin'}"></i>
		</div>
		</c:if>
	</div>

	<%-- Add more content into text group if necessary --%>
	<jsp:doBody />
</div>
