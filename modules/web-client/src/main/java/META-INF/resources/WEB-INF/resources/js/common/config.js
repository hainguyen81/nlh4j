'use strict';

// -------------------------------------------------
// FUNCTIONS HELPER
// -------------------------------------------------
/**
 * Show/Hide the process backdrop
 *
 * @param show true for showing
 */
function turnBackdrop(show) {
	var backdrop = $('.main-backdrop');
	var backdropLoad = $('.main-backdrop-loading');
	var body = document.body, html = document.documentElement;
	// check for duplicate applied
	if (backdrop.length) {
		var showed = (backdrop[0].style.visibility == 'visible');
		if (showed == show) return false;
	};
	var restrictFunc = function(e) {
		e.preventDefault();
		e.returnValue = false;
		e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;
		return false;
	};
	var resizeFunc = function() {
		if (show == true && backdropLoad.length) {
			var height = Math.max(
					body.scrollHeight, body.offsetHeight,
					html.clientHeight, html.scrollHeight,
					html.offsetHeight, $(this).innerHeight());
			var width = Math.max(
					body.scrollWidth, body.offsetWidth,
					html.clientWidth, html.scrollWidth,
					html.offsetWidth, $(this).innerWidth());
			backdrop.css('height', height + 'px');
			backdrop.css('width', width + 'px');
		} else if (backdropLoad.length) {
			backdrop.css('height', '0px');
			backdrop.css('width', '0px');
		}
	};
	var restrictEvents = function() {
		backdropLoad.on('mousedown', restrictFunc);
		backdropLoad.on('mouseup', restrictFunc);
		backdropLoad.on('click', restrictFunc);
		backdrop.on('mousedown', restrictFunc);
		backdrop.on('mouseup', restrictFunc);
		backdrop.on('click', restrictFunc);
		$(body).on('mousedown', restrictFunc);
		$(body).on('mouseup', restrictFunc);
		$(body).on('click', restrictFunc);
		$(body).on('resize', resizeFunc);
		$(body).css('overflow-y', 'hidden');
		$(body).css('pointer-events', 'none');
	};
	var unrestrictEvents = function() {
		backdropLoad.off('mousedown');
		backdropLoad.off('mouseup');
		backdropLoad.off('click');
		backdrop.off('mousedown');
		backdrop.off('mouseup');
		backdrop.off('click');
		backdrop.css('height', '0px');
		backdrop.css('width', '0px');
		$(body).off('mousedown');
		$(body).off('mouseup');
		$(body).off('click');
		$(body).off('resize');
		$(body).css('overflow-y', 'auto');
		$(body).css('pointer-events', 'auto');
	};
	// using spin library
	if (typeof backdropLoad.spin === 'function') {
		backdropLoad.html('');
		backdropLoad.spin({
			lines: 17 // The number of lines to draw
			, length: 45 // The length of each line
			, width: 14 // The line thickness
			, radius: 0 // The radius of the inner circle
			, scale: 1.25 // Scales overall size of the spinner
			, corners: 0.7 // Corner roundness (0..1)
			, color: '#3f82ab' // #rgb or #rrggbb or array of colors
			, opacity: 0.4 // Opacity of the lines
			, rotate: 25 // The rotation offset
			, direction: 1 // 1: clockwise, -1: counterclockwise
			, speed: 1 // Rounds per second
			, trail: 36 // Afterglow percentage
			, fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
			, zIndex: 2e9 // The z-index (defaults to 2000000000)
			, className: 'spinner' // The CSS class to assign to the spinner
			, top: '50%' // Top position relative to parent
			, left: '50%' // Left position relative to parent
			, shadow: true // Whether to render a shadow
			, hwaccel: false // Whether to use hardware acceleration
			, position: 'absolute' // Element positioning
		}, '#3f82ab');
		if (show == true && backdropLoad.length) {
			restrictEvents();
			resizeFunc();
			backdrop[0].style.visibility = 'visible';
			backdropLoad[0].style.visibility = 'visible';
		} else if (backdropLoad.length) {
			unrestrictEvents();
			backdropLoad[0].style.visibility = 'hidden';
			backdrop[0].style.visibility = 'hidden';
		}

		// using spin by image GIF
	} else {
		if (show == true && backdrop.length) {
			restrictEvents();
			resizeFunc();
			backdrop[0].style.visibility = 'visible';
			backdrop[0].innerHTML = backdrop[0].innerHTML;
		}
		if (show == true && backdropLoad.length) {
			backdropLoad[0].style.visibility = 'visible';
			backdropLoad[0].innerHTML = backdropLoad[0].innerHTML;
		}
		if (show !== true && backdropLoad.length) {
			unrestrictEvents();
			backdropLoad[0].style.visibility = 'hidden';
		}
		if (show !== true && backdrop.length) {
			unrestrictEvents();
			backdrop[0].style.visibility = 'hidden';
		}
	}
};
/**
 * Show/Hide the offline backdrop
 *
 * @param show true for showing
 */
function turnOffline(show) {
	var backdrop = $('.main-backdrop');
	var backdropLoad = $('.main-backdrop-offline');
	var body = document.body, html = document.documentElement;
	// check for duplicate applied
	if (backdrop.length) {
		var showed = (backdrop[0].style.visibility == 'visible');
		if (showed == show) return false;
	}
	var restrictFunc = function(e) {
		e.preventDefault();
		e.returnValue = false;
		e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;
		e.stopImmediatePropagation && e.stopImmediatePropagation();
		return false;
	};
	var resizeFunc = function() {
		if (show == true && backdropLoad.length) {
			var height = Math.max(
					body.scrollHeight, body.offsetHeight,
					html.clientHeight, html.scrollHeight,
					html.offsetHeight, $(this).innerHeight());
			var width = Math.max(
					body.scrollWidth, body.offsetWidth,
					html.clientWidth, html.scrollWidth,
					html.offsetWidth, $(this).innerWidth());
			backdrop.css('height', height + 'px');
			backdrop.css('width', width + 'px');
		} else if (backdropLoad.length) {
			backdrop.css('height', '0px');
			backdrop.css('width', '0px');
		}
	};
	var restrictEvents = function() {
		backdropLoad.on('mousedown', restrictFunc);
		backdropLoad.on('mouseup', restrictFunc);
		backdropLoad.on('click', restrictFunc);
		backdrop.on('mousedown', restrictFunc);
		backdrop.on('mouseup', restrictFunc);
		backdrop.on('click', restrictFunc);
		$(body).on('mousedown', restrictFunc);
		$(body).on('mouseup', restrictFunc);
		$(body).on('click', restrictFunc);
		$(body).on('resize', resizeFunc);
		$(body).css('overflow-y', 'hidden');
		$(body).css('pointer-events', 'none');
	};
	var unrestrictEvents = function() {
		backdropLoad.off('mousedown');
		backdropLoad.off('mouseup');
		backdropLoad.off('click');
		backdrop.off('mousedown');
		backdrop.off('mouseup');
		backdrop.off('click');
		backdrop.css('height', '0px');
		backdrop.css('width', '0px');
		$(body).off('mousedown');
		$(body).off('mouseup');
		$(body).off('click');
		$(body).off('resize');
		$(body).css('overflow-y', 'auto');
		$(body).css('pointer-events', 'auto');
	};
	if (show == true && backdrop.length) {
		backdrop[0].style.visibility = 'visible';
		backdrop[0].innerHTML = backdrop[0].innerHTML;
		restrictEvents();
		resizeFunc();
	}
	if (show == true && backdropLoad.length) {
		backdropLoad[0].style.visibility = 'visible';
		backdropLoad[0].innerHTML = backdropLoad[0].innerHTML;
	}
	if (show !== true && backdropLoad.length) {
		unrestrictEvents();
		backdropLoad[0].style.visibility = 'hidden';
	}
	if (show !== true && backdrop.length) {
		unrestrictEvents();
		backdrop[0].style.visibility = 'hidden';
	}
}

/**
 * Get the base context path
 *
 * @returns the base context path
 */
function getBasePath() {
	// if user defined context path
	// if (pageInfo && pageInfo.context && pageInfo.context.length > 0) return pageInfo.context;
	if (!$.isUndefined(pageInfo) && !$.isUndefined(pageInfo.context)) {
		return pageInfo.context;
	}

	// FIXME Important note: Does only work for the "root" context path. Does not work with "subfolders",
	// or if context path has a slash ("/") in it.
	// parse by default client page
	var base = $('base');
	base = (base && base.length > 0 ? base[0] : null);
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    }
    else {
        base = (document && document.URL ? document.URL : null);
    }
    return (base && base.length > 0
    		? base.substr(0, base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1)) : null);
}
/**
 * Parse meta data of head tag
 *
 * @param key meta name key
 */
function parseMetaData(key) {
	var meta = $("meta[name='" + (key || '') + "']");
	return (meta.length ? meta.attr("content") : pageInfo && pageInfo.hasOwnProperty(key) ? pageInfo[key] : '');
}
/**
 * Parse error message from meta of head tag
 */
function parseErrorInternetMessage() {
	var headerkey = (typeof ERROR_INTERNET_META_HEADER_KEY === 'string'
		? ERROR_INTERNET_META_HEADER_KEY : 'errorInternet');
	return parseMetaData(headerkey);
}
/**
 * Parse error message from meta of head tag
 */
function parseErrorServerMessage() {
	var headerkey = (typeof ERROR_SERVER_META_HEADER_KEY === 'string'
		? ERROR_SERVER_META_HEADER_KEY : 'errorServer');
	return parseMetaData(headerkey);
}
/**
 * Parse token header name from meta of head tag
 */
function parseTokenHeaderName() {
	var tokennameheaderkey = (typeof CSRF_NAME_META_HEADER_KEY === 'string'
		? CSRF_NAME_META_HEADER_KEY : '_csrf_header');
	return parseMetaData(tokennameheaderkey);
}
/**
 * Parse token header name from meta of head tag
 */
function parseTokenParameterName() {
	var tokennameheaderkey = (typeof CSRF_PARAM_NAME_META_HEADER_KEY === 'string'
		? CSRF_PARAM_NAME_META_HEADER_KEY : '_csrf_param');
	return parseMetaData(tokennameheaderkey);
}
/**
 * Parse token header value from meta of head tag
 */
function parseTokenHeader(csrfProvider, csrfService) {
	csrfService = (!csrfService
			? (csrfProvider && csrfProvider.model && csrfProvider.model.csrfService
					? csrfProvider.model.csrfService : null) : csrfService);
	var csrf = (csrfService && csrfService.token ? csrfService.token : null);
	var tokenheaderkey = (typeof CSRF_TOKEN_META_HEADER_KEY === 'string'
		? CSRF_TOKEN_META_HEADER_KEY : '_csrf');
	var pagetoken = parseMetaData(tokenheaderkey);
	if (!csrf || typeof csrf === 'string' || csrf !== pagetoken) {
		csrfService && csrfService.getTokenData && csrfService.getTokenData();
		if (csrfService && csrfService.token && csrfService.token.length > 0) {
			pagetoken = csrfService.token;
			$("meta[name='" + tokenheaderkey + "']").attr("content", pagetoken);
		}
	}
	return pagetoken;
}
/**
 * Parse token header value from meta of head tag
 */
function applyTokenHeader(token) {
	var tokenheaderkey = (typeof CSRF_TOKEN_META_HEADER_KEY === 'string'
		? CSRF_TOKEN_META_HEADER_KEY : '_csrf');
	$("meta[name='" + tokenheaderkey + "']").attr("content", token);
}
/**
 * Parse token header value from meta of head tag
 */
function parseBasePathKey() {
	return (typeof BASE_CONTEXT_PATH_KEY === 'string' ? BASE_CONTEXT_PATH_KEY : 'Base-Context-Path');
}
/**
 * Parse token header value from meta of head tag
 */
function parseBasePath($http, $log) {
	var basepath = parseBasePathKey();
	var headers = (typeof $http !== 'undefined' && typeof $http.defaults === 'object'
		&& typeof $http.defaults.headers === 'object' && typeof $http.defaults.headers.common === 'object'
			? $http.defaults.headers.common : {});
	 if (headers && typeof headers[basepath] !== 'undefined' && headers[basepath].length > 0) {
		 basepath = headers[basepath];
	 }
	 else {
		 basepath = '';
	 }
	 ($log && typeof $log.debug === 'function')
	 && $log.debug('>>> Base context path:', basepath);
	 return basepath;
}
/**
 * Parse and prepare XHR request headers
 *
 * @returns the headers configuration JSON object
 */
function prepareXhrHeaders(csrfProvider, csrfService) {
	var headers = {};
	headers[parseBasePathKey()] = getBasePath();
	// angular http request headers configuration
	var tokenname = parseTokenHeaderName();
	var tokenparamname = parseTokenParameterName();
	var token = parseTokenHeader(csrfProvider, csrfService);
	if (tokenname && tokenname.length > 0 && token && token.length > 0) {
		headers[tokenname] = token;
		headers[tokenparamname] = token;
//		// TODO Refused to set unsafe header "Cookie"
//		// need apply xhr setDisableHeaderCheck to ignore this error
//		headers['Cookie'] = (tokenname + '=' + token);
	}
	if (typeof DEFAULT_HEADERS === 'object') {
		var defheaders = DEFAULT_HEADERS;
		var hkeys = Object.keys(defheaders);
		if (hkeys && hkeys.length > 0) {
			for(var i = 0; i < hkeys.length; i++) {
				var key = hkeys[i];
				var value = defheaders[key];
				if (value && value.length > 0 && key && key.length > 0) {
					headers[key] = value;
				}
			}
		}
	}
	return headers;
}

// -------------------------------------------------
// HTTP AJAX CONFIGURATION (MUST BE FIRST FOR BASE CONTEXT PATH)
// -------------------------------------------------
//@TODO Must applying before configuring $httpProvider to intercept $http
//configuration CSRF
importConfig(
		function(csrfProvider) {
			// optional configurations
			var basepath = getBasePath();
			csrfProvider.config({
				basepath: basepath,
				url: basepath + '/csrftoken',
				loginUrl: basepath + '/login',
				maxRetries: 1,
				csrfHttpType: 'head',
				csrfTokenHeader: function() {
					return parseTokenHeaderName();
				},
				// CSRF token will be added only to these method types
				httpTypes: ['GET', 'POST', 'HEAD'],
				excludes: [ basepath, basepath + '/resources/demo/*' ],
				headers: function() {
					return prepareXhrHeaders(null, null);
				},
				csrfCookie: null //CSRF_COOKIE_TOKEN_NAME
			});
		}
);
run(function() {});
//configuration for $log, must be first all configuration
importConfig(
		function($provide) {
			//Just a dummy decorator
			$provide.decorator('$log', function($delegate) {
				var originals = {};
				var levels = ['OFF', 'DEBUG', 'INFO', 'WARN', 'ERROR'];
		        var methods = ['info' , 'debug' , 'warn' , 'error'];
		        angular.forEach(methods, function(m) {
		        	originals[m] = $delegate[m];
		        	$delegate[m] = function() {
		        		// detect log level
		        		var level = (LOG_LEVEL || 'OFF');
						level = (level || '').toUpperCase();
						if (!levels.contain(level)) level = 'OFF';
						var levelIdx = levels.indexOf(level);
		        		// calculate log arguments
		        		var args = [].slice.call(arguments);
                        var timestamp = new Date();
                        var y = timestamp.getFullYear();
                        var mon = (timestamp.getMonth() + 1);
                        mon = (mon.toString().length < 2 ? '0' + mon : mon.toString());
                        var d = timestamp.getDate();
                        d = (d.toString().length < 2 ? '0' + d : d.toString());
                        var h = timestamp.getHours();
                        h = (h.toString().length < 2 ? '0' + h : h.toString());
                        var mi = timestamp.getMinutes();
                        mi = (mi.toString().length < 2 ? '0' + mi : mi.toString());
                        var s = timestamp.getSeconds();
                        s = (s.toString().length < 2 ? '0' + s : s.toString());
                        var ms = timestamp.getMilliseconds();
                        ms = (ms.toString().length <= 1 ? '00' + ms : ms.toString().length <= 2 ? '0' + ms : ms.toString());
                        var dateStr = y + '/' + mon + '/' + d + ' ' + h + ':' + mi + ':' + s + '.' + ms;
		        		// debug
                        var mode = m.toUpperCase();
                        var idx = levels.indexOf(mode);
                        if (idx > levelIdx) {
                        	// if not OFF
                        	if (levelIdx > 0) {
	                        	var msg = 'Log level [' + level + '] could not apply for logger [' + mode + ']! '
	                					+ 'Please apply [LOG_LEVEL] as ["DEBUG" < "INFO" < "WARN" < "ERROR"]!!!';
	                        	mode = '[WARN ]';
	                        	originals['warn'].apply(null, [[mode, dateStr, msg].join(' ')]);
                        	}
                        	return;
                        }

                        // apply logger
                        mode = (mode.length < 5 ? '[' + mode + ' ]' : '[' + mode + ']');
                        args[0] = [mode, dateStr, args[0]].join(' ');
                        originals[m].apply(null , args);
                    };
               });
               return $delegate;
			});
		});

// configuration request headers
importConfig(
		function($httpProvider, csrfProvider, $logProvider) {
			var $log = ($logProvider ? $logProvider.$get() : null);
			// default xDomain and cridentitcals
			$httpProvider.defaults.useXDomain = (typeof X_REQ_XDOMAIN === 'boolean' ? X_REQ_XDOMAIN : true);
			$httpProvider.defaults.withCredentials = (typeof X_REQ_CRIDENTIALS === 'boolean' ? X_REQ_CRIDENTIALS : true);
			$httpProvider.defaults.errorInternet = (parseErrorInternetMessage
					? parseErrorInternetMessage() : DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['errorInternet']);
			$httpProvider.defaults.errorServer = (parseErrorServerMessage
					? parseErrorServerMessage() : DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['errorServer']);
			$httpProvider.defaults['e0'] = (parseMetaData ? parseMetaData('0')
					: DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['e0']);
			$httpProvider.defaults['e401'] = (parseMetaData ? parseMetaData('401')
					: DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['e401']);
			$httpProvider.defaults['e403'] = (parseMetaData ? parseMetaData('403')
					: DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['e403']);
			$httpProvider.defaults['e404'] = (parseMetaData ? parseMetaData('404')
					: DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['e404']);
			$httpProvider.defaults['e405'] = (parseMetaData ? parseMetaData('405')
					: DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['e405']);
			$httpProvider.defaults['e409'] = (parseMetaData ? parseMetaData('409')
					: DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['e409']);
			$httpProvider.defaults['e417'] = (parseMetaData ? parseMetaData('417')
					: DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['e417']);
			$httpProvider.defaults['e500'] = (parseMetaData ? parseMetaData('500')
					: DEFAULT_ERROR_MESSAGES[DEFAULT_LOCALE]['e500']);
			// apply request headers
			var headers = prepareXhrHeaders(csrfProvider, null);
        	if (typeof headers === 'object' && Object.keys(headers).length > 0) {
        		var keys = Object.keys(headers);
        		for(var i = 0; i < keys.length; i++) {
        			var key = keys[i];
        			var value = headers[key];
        			if (key && key.length > 0 && value && value.length > 0) {
        				$httpProvider.defaults.headers.common[key] = value;
        			}
        		}
        	}

			//jquery ajax request headers options
			var options = {
					cache: false,
					// 通信を開始します
					beforeSend: function(xhr, settings) {
						// apply default xDomain and cridenticals
						xhr.useXDomain = $httpProvider.defaults.useXDomain;
						xhr.withCredentials = $httpProvider.defaults.withCredentials;

						// Cross-Site header
						var headers = prepareXhrHeaders(csrfProvider, null);
						$.each(headers, function(hname, idx) {
							 if (hname && hname.length > 0
									 && headers[hname] && headers[hname].length > 0
									 && !settings.hasOwnProperty(hname)) {
	                    		switch(hname) {
	                    			case 'withCredentials':
	                    				xhr.withCredentials = headers[hname];
	                    				break;
	                    			case 'useXDomain':
	                    				xhr.useXDomain = headers[hname];
	                    				break;
	                				default:
	                					xhr.setRequestHeader(hname, headers[hname]);
	                    		}
							 }
						});

						// debug
						 this.url = settings.url;
						 this.actionStartDate = new Date();
						 ($log && typeof $log.info === 'function')
						 && $log.info('[ACTION START]: ' + settings.url);
						 ($log && typeof $log.info === 'function')
						 && $log.info('[ACTION INPUT] : ' + angular.toJson(settings.data));
					},
					// 通信を終了します
					complete: function(xhr, status) {
						// log end request
						this.actionEndDate = new Date();
						($log && typeof $log.info === 'function')
						&& $log.info('[ACTION STATUS]: ' + status);
						if (this.actionStartDate) {
							($log && typeof $log.info === 'function')
							&& $log.info('[ACTION END] : ' + this.url + ' [' + (this.actionEndDate - this.actionStartDate) + 'ms]');
						}
						else {
							($log && typeof $log.info === 'function')
							&& $log.info('[ACTION END] : ' + this.url + ' [' + this.actionEndDate + ']');
						}
					},
					// エラー
					error: function(xhr, status, errorThrown) {
						var varst = (xhr && xhr.status != null && xhr.status != undefined ? xhr.status : errorThrown);
	     				if (xhr && xhr.status != null && xhr.status != undefined) {
	     					var errMsg = xhr.responseText;
	     					if ((!errMsg || errMsg.length <= 0)
	     							&& $httpProvider && $httpProvider.defaults
	     							&& $httpProvider.defaults.hasOwnProperty('e' + xhr.status)) {
	     						errMsg = $httpProvider.defaults['e' + xhr.status];
	     					}
	     					switch(xhr.status) {
	     						// OK
	     						case 200: {
	     							($log && typeof $log.info === 'function')
	     							&& $log.info('ステータス200、エラーなし');
	     							return;
	     						}
	     						default: {
	     							($log && typeof $log.error === 'function')
	     							&& $log.error(errMsg || 'Unknown response error!');
	     							break;
	     						}
	     					}
	     				}
	     				($log && typeof $log.info === 'function')
						&& $log.error('エラー「' + varst + '」が発生しました。');
	     			}
			};
			$.ajaxSetup(options);
        }
);

//jQuery configuration
jQuery.support.cors = (typeof X_REQ_CORS === 'boolean' ? X_REQ_CORS : true);
//jQuery.mobile.allowCrossDomainPages = (typeof X_REQ_MOBILE_CORS === 'boolean' ? X_REQ_MOBILE_CORS : true);

// -------------------------------------------------
// OTHER CONFIGURATION
// -------------------------------------------------
// configuration chart
importConfig(
		function(ChartJsProvider) {
			// Configure all charts
			ChartJsProvider.setOptions({
				colours: ['#FF5252', '#FF8A80'],
				responsive: false
			});
			// Configure all line charts
			ChartJsProvider.setOptions('Line', { datasetFill: false });
		}
);

// configuration storage
importConfig(
		function(localStorageServiceProvider) {
			localStorageServiceProvider
				.setPrefix(APP_NAME)
				.setStorageType(APP_SESSION_STORAGE)
				.setNotify(true, true);
		}
);

//configuration select
importConfig(
		function(uiSelectConfig) {
			uiSelectConfig.theme = 'bootstrap';
			uiSelectConfig.resetSearchInput = true;
			uiSelectConfig.appendToBody = false;
		}
);

//configuration ui-bootstrap-datepicker
importConfig(
		function(uibDatepickerPopupConfig) {
			uibDatepickerPopupConfig.appendToBody = true;
		}
);

//configuration ui-bootstrap-tooltip
importConfig(
		function($tooltipProvider) {
			$tooltipProvider.options({ appendToBody: true });
			$tooltipProvider.setTriggers({
				'show': 'hide',
				'click': 'click'
			});
		}
);

//// configuration exception handler
//importConfig(
//		function($provide) {
//			$provide.decorator(
//					'$exceptionHandler',
//					function($log, $delegate, $injector) {
//						return function(exception, cause) {
//							var $rootScope = $injector.get('$rootScope');
//							if (!!$rootScope && typeof $rootScope.error == 'function' && DEBUG) {
//								$rootScope.error({ body: cause });
//							}
//							$log.warn(cause);
//							if (!!$delegate && typeof $delegate == 'function') {
//								$delegate(exception, cause);
//							}
//						};
//					});
//		}
//);