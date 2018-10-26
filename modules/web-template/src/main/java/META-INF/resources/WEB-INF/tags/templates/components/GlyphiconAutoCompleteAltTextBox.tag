<%@ tag pageEncoding="utf-8" description="The &lt;input&gt; HTML element wrapper for autocomplete inputted value (base on angucomplete-alt library)"%>
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
<%@ attribute name="textMatchCssClass" required="false"
description="If it is assigned, matching part of title is highlighted with given class style."%>
<%@ attribute name="textMaxLength" required="false"
description="Specify the maximum characters number that could be inputted. (maxlength)"%>
<%@ attribute name="textPause" required="false"
description="The time to wait (in milliseconds) before searching when the user enters new characters. (pause)"%>
<%@ attribute name="textMinLength" required="false"
description="Specify the minimum required characters number that must be inputted before searching. (minlength)"%>
<%@ attribute name="onSelected" required="false"
description="Either an object in your scope or callback function. If you set an object, it will be passed to the directive with '=' sign but it is actually one-way-bound data. So, setting it from your scope has no effect on input string. If you set a callback, it gets called when selection is made. To get attributes of the input from which the assignment was made, use this.$parent.$index within your function. (selected-object)"%>
<%@ attribute name="textSelectedObjectData" required="false"
description="A second parameter which will be passed to onSelected (selected-object). Only works when using onSelected (selected-object)."%>
<%@ attribute name="textRemoteURL" required="false"
description="The remote URL to hit to query for results in JSON. angucomplete will automatically append the search string on the end of this, so it must be a GET request. (remote-url)"%>
<%@ attribute name="textRemoteReceiver" required="false"
description="The name of the field in the JSON object returned back that holds the Array of objects to be used for the autocomplete list. (remote-url-data-field)"%>
<%@ attribute name="textRemoteReqFormatter" required="false"
description="A function that takes a query string and returns parameter(s) for GET. It should take the query string as argument and returns a key-value object. (remote-url-request-formatter)"%>
<%@ attribute name="textRemoteReqWithCredenticals" required="false"
description="A boolean that accepts parameters with credentials. (remote-url-request-with-credentials)"%>
<%@ attribute name="textRemoteRespFormatter" required="false"
description="A function on the scope that will modify raw response from remote API before it is rendered in the drop-down. Useful for adding data that may not be available from the API. The specified function must return the object in the format that angucomplete understands. (remote-url-response-formatter)"%>
<%@ attribute name="textRemoteErrCallback" required="false"
description="A callback funciton to handle error response from $http. (remote-url-error-callback)"%>
<%@ attribute name="textRemoteApiHandler" required="false"
description="This gives a way to fully delegate handling of remote search API. This function takes user input string and timeout promise, and it needs to return a promise. For example, if your search API is based on POST, you can use this function to create your own http handler. (remote-api-handler)"%>
<%@ attribute name="textAcFieldsList" required="false"
description="The name of the field in the JSON objects returned back that should be used for displaying the title in the autocomplete list. Note, if you want to combine fields together, you can comma separate them here (e.g. for a first and last name combined). If you want to access nested field, use dot to connect attributes (e.g. name.first). (title-field)"%>
<%@ attribute name="textAcDescField" required="false"
description="The name of the field in the JSON objects returned back that should be used for displaying the description in the autocomplete list. (description-field)"%>
<%@ attribute name="textAcImageField" required="false"
description="The name of the field in the JSON objects returned back that should be used for displaying an image in the autocomplete list. (image-field)"%>
<%@ attribute name="textAcModelsList" required="false"
description="The local data variable to use from your controller. Should be an array of objects. (local-data)"%>
<%@ attribute name="textSearchPerform" required="false"
description="A function that search local data. It should take a input string and an array of items as arguments and returns an array of matched items. (local-search)"%>
<%@ attribute name="textSearchFieldsList" required="false"
description="The fields from your local data to search on (comma separate them). Each field can contain dots for accessing nested attribute. (search-fields)"%>
<%@ attribute name="textClearAfterSelect" required="false"
description="To clear out input field upon selecting an item, set this attribute to true. (clear-selected)"%>
<%@ attribute name="textOverrideSuggestions" required="false"
description="To override suggestions and set the value in input field to selectedObject if onSelected is object (not function). (override-suggestions)"%>
<%@ attribute name="textRequired" required="false"
description="Set field to be required. Requirement for this to work is that this directive needs to be in a form and you need to provide input-name. Default class name is 'autocomplete-required'. (field-required)"%>
<%@ attribute name="textRequiredClass" required="false"
description="Set custom class name for required. (field-required-class)"%>
<%@ attribute name="textSearchingPrompt" required="false"
description="Custom string to show when search is in progress. Set this to 'false' prevents text to show up. (text-searching)"%>
<%@ attribute name="textNoResultsPrompt" required="false"
description="Custom string to show when there is no match. Set this to 'false' prevents text to show up. (text-no-results)"%>
<%@ attribute name="textInitialValue" required="false"
description="Initial value for component. If string, the internal model is set to the string value, if an object, the title-field attribute is used to parse the correct title for the view, and the internal model is set to the object. (initial-value)"%>
<%@ attribute name="onChange" required="false"
description="A callback function that is called when input field is changed. To get attributes of the input from which the assignment was made, use this.$parent.$index within your function. (input-changed)"%>
<%@ attribute name="textAutoMatch" required="false"
description="Allows for auto selecting an item if the search text matches a search results attributes exactly. (auto-match)"%>
<%@ attribute name="onFocus" required="false"
description="A function or expression to be called when input field gets focused. (focus-in)"%>
<%@ attribute name="onBlur" required="false"
description="A function or expression to be called when input field lose focus. (focus-out)"%>
<%@ attribute name="textDisabled" required="false"
description="A model to control disable/enable of input field. (disable-input)"%>
<%@ attribute name="textTemplateUrl" required="false"
description="Customize the markup of the autocomplete template. (template-url)"%>
<%@ attribute name="textFocusFirst" required="false"
description="Automatically select the first match from the result list. (focus-first)"%>
<%@ attribute name="textParseKeyword" required="false"
description="A function or expression to parse input string before comparing into search process. (parse-input)"%>
<%@ attribute name="textTabIndex" required="false"
description="Setting the tabindex attribute on the input field. (field-tabindex)"%>
<%@ attribute name="textNgIf" required="false"
description="Specify component whether has been rendered (ng-if)"%>
<%@ attribute name="textNgShow" required="false"
description="Specify component whether has been shown (ng-show)"%>
<%@ attribute name="textTooltip" required="false"
description="Component tooltip (usually using for showing error tip)"%>
<%@ attribute name="textTooltipPlacement" required="false"
description="The placement that tooltip should be shown such as top, bottom, etc."%>
<%@ attribute name="textTooltipTrigger" required="false"
description="The trigger for showing tooltip such as click, mouseenter, etc."%>
<%@ attribute name="textModel" required="false"
description="Specify angular model (ng-model)"%>
<%@ attribute name="textModelOptions" required="false"
description="Specify angular model options (ng-model-options)"%>
<%@ attribute name="textCheckSpecialChars" required="false"
description="Specify component whether check the inputted special characters. If the special characters has been inputted, validated error has been occurred"%>
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

<c:if test="${empty textName || fn:length(textName) <= 0}">
<%
	long tm = DateUtils.currentTimestamp().getTime();
	textName = "textbox-" + String.valueOf(tm);
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
	class="control-label<c:if test="${textRequired eq true}"> required</c:if><c:if test="${not empty textRequired && textRequired['class'].simpleName ne 'Boolean'}">{{(${textRequired} ? ' required' : '')}}</c:if> ${(not empty textLabelCssClass ? textLabelCssClass : textName)}"
	<c:if test="${textNgIf eq true}">ng-if="true"</c:if>
	<c:if test="${not empty textNgIf && textNgIf['class'].simpleName ne 'Boolean'}">ng-if="${textNgIf}"</c:if>
	<c:if test="${textNgShow eq true}">ng-show="true"</c:if>
	<c:if test="${not empty textNgShow && textNgShow['class'].simpleName ne 'Boolean'}">ng-show="${textNgShow}"</c:if>
	>${textLabel}</label>
	</c:if>
	<div angucomplete-alt
	<c:if test="${not empty textName}">id="${textName}Cont" name="${textName}Cont"</c:if>
	<c:if test="${not empty textName}">input-name="${textName}"</c:if>
	class="form-control ${textCssClass}"
	<c:if test="${not empty textCssClass}">input-class="${textCssClass}"</c:if>
	<c:if test="${not empty textMatchCssClass}">match-class="${textMatchCssClass}"</c:if>
	<c:if test="${not empty textPlaceHolder}">placeholder="${textPlaceHolder}"</c:if>
	<c:if test="${not empty textMaxLength}">maxlength="${textMaxLength}"</c:if>
	<c:if test="${not empty textMinLength}">minlength="${textMinLength}"</c:if>
	<c:if test="${not empty textPause}">pause="${textPause}"</c:if>
	<c:if test="${not empty onSelected}">selected-object="${onSelected}"</c:if>
	<c:if test="${not empty onSelected && not empty textSelectedObjectData}">selected-object-data="${textSelectedObjectData}"</c:if>
	<c:if test="${not empty textRemoteURL}">remote-url="${textRemoteURL}"</c:if>
	<c:if test="${not empty textRemoteURL && not empty textRemoteReceiver}">remote-url-data-field="${textRemoteReceiver}"</c:if>
	<c:if test="${not empty textRemoteURL && not empty textRemoteReqFormatter}">remote-url-request-formatter="${textRemoteReqFormatter}"</c:if>
	<c:if test="${not empty textRemoteURL && textRemoteReqWithCredenticals eq true}">remote-url-request-with-credentials="true"</c:if>
	<c:if test="${not empty textRemoteURL && not empty textRemoteReqWithCredenticals && textRemoteReqWithCredenticals['class'].simpleName ne 'Boolean'}">remote-url-request-with-credentials="${textRemoteReqWithCredenticals}"</c:if>
	<c:if test="${not empty textRemoteURL && not empty textRemoteRespFormatter}">remote-url-response-formatter="${textRemoteRespFormatter}"</c:if>
	<c:if test="${not empty textRemoteURL && not empty textRemoteErrCallback}">remote-url-error-callback="${textRemoteErrCallback}"</c:if>
	<c:if test="${not empty textRemoteURL && not empty textRemoteApiHandler}">remote-api-handler="${textRemoteApiHandler}"</c:if>
	<c:if test="${not empty textAcFieldsList}">title-field="${textAcFieldsList}"</c:if>
	<c:if test="${not empty textAcDescField}">description-field="${textAcDescField}"</c:if>
	<c:if test="${not empty textAcImageField}">image-field="${textAcImageField}"</c:if>
	<c:if test="${not empty textAcModelsList}">local-data="${textAcModelsList}"</c:if>
	<c:if test="${not empty textAcModelsList && not empty textSearchPerform}">local-search="${textSearchPerform}"</c:if>
	<c:if test="${not empty textAcModelsList && not empty textSearchFieldsList}">search-fields="${textSearchFieldsList}"</c:if>
	<c:if test="${textClearAfterSelect eq true}">clear-selected="true"</c:if>
	<c:if test="${not empty textClearAfterSelect && textClearAfterSelect['class'].simpleName ne 'Boolean'}">clear-selected="${textClearAfterSelect}"</c:if>
	<c:if test="${textOverrideSuggestions eq true}">override-suggestions="true"</c:if>
	<c:if test="${not empty textOverrideSuggestions && textOverrideSuggestions['class'].simpleName ne 'Boolean'}">override-suggestions="${textOverrideSuggestions}"</c:if>
	<c:if test="${textRequired eq true}">field-required="true"</c:if>
	<c:if test="${not empty textRequired && textRequired['class'].simpleName ne 'Boolean'}">field-required="${textRequired}"</c:if>
	<c:if test="${textRequired eq true && not empty textRequiredClass}">field-required-class="${textRequiredClass}"</c:if>
	<c:if test="${not empty textRequired && textRequired['class'].simpleName ne 'Boolean' && not empty textRequiredClass}">field-required-class="${textRequiredClass}"</c:if>
	<c:if test="${not empty textSearchingPrompt}">text-searching="${textSearchingPrompt}"</c:if>
	<c:if test="${not empty textNoResultsPrompt}">text-no-results="${textNoResultsPrompt}"</c:if>
	<c:if test="${not empty textInitialValue}">initial-value="${textInitialValue}"</c:if>
	<c:if test="${not empty onChange}">input-changed="${onChange}"</c:if>
	<c:if test="${not empty onFocus}">focus-in="${onFocus}"</c:if>
	<c:if test="${not empty onBlur}">focus-out="${onBlur}"</c:if>
	<c:if test="${textAutoMatch eq true}">auto-match="true"</c:if>
	<c:if test="${not empty textAutoMatch && textAutoMatch['class'].simpleName ne 'Boolean'}">auto-match="${textAutoMatch}"</c:if>
	<c:if test="${textDisabled eq true}">disable-input="true"</c:if>
	<c:if test="${not empty textDisabled && textDisabled['class'].simpleName ne 'Boolean'}">disable-input="${textDisabled}"</c:if>
	<c:if test="${not empty textTemplateUrl}">template-url="${textTemplateUrl}"</c:if>
	<c:if test="${textFocusFirst eq true}">focus-first="true"</c:if>
	<c:if test="${not empty textFocusFirst && textFocusFirst['class'].simpleName ne 'Boolean'}">focus-first="${textFocusFirst}"</c:if>
	<c:if test="${not empty textParseKeyword}">parse-input="${textParseKeyword}"</c:if>
	<c:if test="${not empty textTabIndex}">field-tabindex="${textTabIndex}"</c:if>
	<c:if test="${not empty textTooltip}">data-tooltip="${textTooltip}"</c:if>
	<c:if test="${not empty textTooltipPlacement}">data-tooltip-placement="${textTooltipPlacement}"</c:if>
	<c:if test="${not empty textTooltipTrigger}">data-tooltip-trigger="${textTooltipTrigger}"</c:if>
	<c:if test="${not empty textModel}">ng-model="${textModel}" reference-ng-model-controller</c:if>
	<c:if test="${not empty textModel && not empty textModelOptions}">ng-model-options="${textModelOptions}"</c:if>
	<c:if test="${textCheckSpecialChars eq true}">ng-special-chars</c:if>
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
	></div>

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
