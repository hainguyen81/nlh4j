<%@ include file="/WEB-INF/tags/Common.inc" %>

<!-- ------------------------------------------------- -->
<!-- BASESCRIPT (DECLARE BEFORE APPLICATION SCRIPTS TO AUTO IMPORT VARIABLES TO ANGULAR) -->
<!-- ------------------------------------------------- -->
<nlh4jTags:Script>
	<!-- Form DateTimePicker Open/Close. For component GlyphiconDatePicker -->
	var dtPicker = {
		opened: {},
		open: function($event, which) {
			$event.preventDefault();
			$event.stopPropagation();
			if (!!this.opened || !Object.keys(this.opened).length) {
				this.opened = {};
			}
			this.opened[which] = true;
		}
	};
	<!-- Page information -->
	var pageInfo = {
			host: '${host}' || ''
			, context: '${contextPath}' || ''
			, baseUrl: '${basePath}' || ''
			, locale: '${currentLocale}' || 'en'
			, resource: '${resourcePath}' || '/resources'
			, '_offline_checker': '${faviconUrl}' || '/resources/favicon.ico'
			, '_csrf': '${_csrf.token}' || ''
			, '_csrf_header': '${_csrf.headerName}' || ''
			, '_csrf_param': '${_csrf.parameterName}' || ''
			, maxUploadSize: '${maxUploadSize}'
			, fullDateTimePattern: '${cmnFullDateTimePattern}' || ''
			, dateTimePattern: '${cmnDateTimePattern}' || ''
			, timePattern: '${cmnTimePattern}' || ''
			, monthPattern: '${cmnMonthPattern}' || ''
			, yearPattern: '${cmnYearPattern}' || ''
			, dayMonthPattern: '${cmnDayMonthPattern}' || ''
			, errorInternet: '${fn:replace(errorInternet, "\'", "\\'")}' || ''
			, errorServer: '${fn:replace(errorServer, "\'", "\\'")}' || ''
			, 'e0': '${fn:replace(errorInternet, "\'", "\\'")}' || ''
			, 'e401': '${fn:replace(error401, "\'", "\\'")}' || ''
			, 'e403': '${fn:replace(error403, "\'", "\\'")}' || ''
			, 'e404': '${fn:replace(error404, "\'", "\\'")}' || ''
			, 'e405': '${fn:replace(error405, "\'", "\\'")}' || ''
			, 'e409': '${fn:replace(error409, "\'", "\\'")}' || ''
			, 'e417': '${fn:replace(error417, "\'", "\\'")}' || ''
			, 'e500': '${fn:replace(errorServer, "\'", "\\'")}' || ''
			, languages: [
				{ icon: null, code: null, name: '${languagePrompt}' }
				, { icon: 'fa-language flags en_US_2', code: 'en', name: '${nlh4j:displayLanguage("en")}' }
				, { icon: 'fa-language flags ja_JP_2', code: 'ja', name: '${nlh4j:displayLanguage("ja")}' }
				, { icon: 'fa-language flags vi_VN', code: 'vi', name: '${nlh4j:displayLanguage("vi")}' }
			]
			//, company: '${company == null ? '' : company.code}'
			, parseUrl: function(url) {
				var host = this.host;
				var ctxPath = this.context;
				var basePath = host + ctxPath;
				url = url || '';
				if (url.indexOf(basePath) >= 0) url = url.substring(url.indexOf(basePath) + basePath.length);
				var arrUrls = url.split('/') || [];
				var urlParts = ctxPath.split('/') || [];
				arrUrls = urlParts.concat(arrUrls);
				if (arrUrls && arrUrls.length > 0) {
					urlParts = [];
					urlParts.push(host);
					for(var i = 0; i < arrUrls.length; i++) {
						var part = arrUrls[i];
						if (!part || part.length <= 0 || urlParts.indexOf(part) >= 0) continue;
						urlParts.push(part);
					}
					url = urlParts.join('/');
					if (url.indexOf('?') < 0) url += '/';
				}
				else url = host + ctxPath + '/';
				return url;
			}
	};
</nlh4jTags:Script>

<!-- ------------------------------------------------- -->
<!-- APPLICATION -->
<!-- ------------------------------------------------- -->
<%-- exex - Scripts --%>
<nlh4jTags:Js src="common/const.js" />
<%-- exex - Include socket script by profile --%>
<c:if test="${nlh4j:isSocketProfile()}">
<nlh4jTags:Script>APP_INCLUDES.push('ngStomp')</nlh4jTags:Script>
</c:if>
<nlh4jTags:Js src="common/app.js" />
<nlh4jTags:Js src="common/config.js" />
<nlh4jTags:Js src="common/factories.js" />
<nlh4jTags:Js src="common/services.js" />
<c:if test="${nlh4j:isSocketProfile()}">
<nlh4jTags:Script>
// include socket provider
if (!!APP_INCLUDES && angular.isArray(APP_INCLUDES)) { APP_INCLUDES.push('ngStomp'); }
</nlh4jTags:Script>
<nlh4jTags:Js src="common/socket-service.js" />
</c:if>
<nlh4jTags:Js src="common/controllers.js" />
<nlh4jTags:Js src="common/animations.js" />
<nlh4jTags:Js src="common/filters.js" />
<nlh4jTags:Js src="common/directives.js" />
<c:if test="${nlh4j:isSocketProfile()}">
<nlh4jTags:Js src="common/socket-directive.js" />
</c:if>
