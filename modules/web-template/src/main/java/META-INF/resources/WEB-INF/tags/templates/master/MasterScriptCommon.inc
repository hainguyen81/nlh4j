<%@ include file="/WEB-INF/tags/Common.inc" %>

<%-- START: Apply JSP entity JSON into scope if necessary --%>
<c:if test="${fn:length(pkJson) > 0}">
	<nlh4jTags:Script>
		// cache entity primary/unique key information into angular scope
		var entitypk = {};
		try { entitypk = angular.fromJson(${pkJson}); }
		catch(e) { entitypk = {}; }
	</nlh4jTags:Script>
</c:if>
<nlh4jTags:Script>
	// permission
	document.title = '<nlh4j:message code="${pageModule}.title" />';
	var permissions = {
		viewable: (eval('${viewable}') == true) ? true : false
		, insertable: (eval('${insertable}') == true) ? true : false
		, updatable: (eval('${updatable}') == true) ? true : false
		, deletable: (eval('${deletable}') == true) ? true : false
		, writable: (eval('${writable}') == true) ? true : false
		, hasPermission: function(func) {
			var functionsLst = eval('${functionsString}');
			functionsLst = (functionsLst == null || functionsLst == undefined || functionsLst.length <= 0 ? '' : functionsLst);
			if (functionsLst.length > 0) {
				if (!functionsLst.startsWith('|')) functionsLst = '|' + functionsLst;
				if (!functionsLst.endsWith('|')) functionsLst = functionsLst + '|';
			}
			return (functionsLst.length > 0 ? functionsLst.indexOf('|' + func + '|') >= 0 : false);
		}
	};
</nlh4jTags:Script>
<%-- END: Apply JSP entity JSON into scope if necessary --%>