'use strict';

/**
 * Internal function to handling AJAX request successfully
 *
 * @param callback success callback function handler
 * @param statusCodeCallback status callback object such as jQuery AJAX style
 * @param errorCallback error callback function handler
 * @param data response data
 * @param status response status
 * @param headers request headers
 * @param config request configuration
 * @param $log angular log delegator
 * @param toaster angular toaster
 * @param idleService IDLE service
 */
function __successHandler(
		callback, statusCodeCallback, errorCallback,
		data, status, headers, config,
		$log, toaster, idleService) {
	try {
		// log end request
		config.actionEndDate = new Date();
		($log && typeof $log.info === 'function')
		&& $log.info('[ACTION OUTPUT]:', data);
		($log && typeof $log.info === 'function')
		&& $log.info('[ACTION END   ]:', config.url, '[', (config.actionEndDate - config.actionStartDate), 'ms]');

		// check response data
		if (angular.isDefined(data) && data.returnCd == "NG"
			&& toaster && typeof toaster.pop === 'function') {
			if ((data.returnMsg || '').length > 0) {
				toaster.pop('error', $document[0].title, data.returnMsg);
			} else {
				toaster.pop('error', $document[0].title, 'システムエラー');
			}
		}

		// callback if necessary
		if (callback && typeof callback === 'function') {
			// log starting callback
			config.callbackStartDate = new Date();
			($log && typeof $log.info === 'function')
			&& $log.info('[CALLBACK START]:', config.url);

			// callback
			callback(data, status, headers);

			// log ending callback
			config.callbackEndDate = new Date();
			($log && typeof $log.info === 'function')
			&& $log.info('[CALLBACK END  ]:', config.url, '[', (config.callbackEndDate - config.callbackStartDate), 'ms]');
		}
		// check for success statuses
		else if (100 <= status && status <= 299 && statusCodeCallback
			&& typeof statusCodeCallback === 'object' && statusCodeCallback.hasOwnProperty(status)) {
			statusCodeCallback[status](data, headers);
		}
		// check for error code
		else if (300 <= status && errorCallback && typeof errorCallback === 'function') {
			errorCallback(data, status, headers, config);
		}

		// reset IDLE status
		idleService.reset();
	} catch (e) {
		($log && typeof $log.error === 'function')
		&& $log.error('[ACTION SUCCESS - ERROR]:', e);
	}
};

/**
 * Internal function to handling AJAX request error
 *
 * @param $rootScope angular custom root scope
 * @param errcallback error callback function handler
 * @param data error response data
 * @param status error response status
 * @param headers request headers
 * @param config request configuration
 * @param statusText error response status text
 * @param $log angular log delegator
 * @param toaster angular toaster
 * @param idleService IDLE service
 * @param $document angular document object
 */
function __errorHandler(
		$rootScope, errcallback,
		data, status, headers, config, statusText,
		$log, toaster, idleService, $document) {
	try {
		// if status 200, this is OK
		if (status == 200) {
			__successHandler(
					null, null, errcallback,
					data, status, headers, config,
					$log, toaster, idleService);
			return;
		}

		// detect response error
		var meta = (typeof parseMetaData === 'function' ? parseMetaData('e' + status) : '');
		var cfg = (config && config.hasOwnProperty('e' + status) ? config['e' + status] : '');
		data = data || cfg || meta;
		statusText = statusText || cfg || meta;

		// log end request
		config.actionEndDate = new Date();
		($log && typeof $log.info === 'function')
		&& $log.info(headers);
		($log && typeof $log.info === 'function')
		&& $log.info(status);

		// log request error data
		data
		&& ($log && typeof $log.error === 'function')
		&& $log.error(data);
		!data && statusText && statusText.length
		&& ($log && typeof $log.error === 'function')
		&& $log.error(statusText);

		// log end request
		($log && typeof $log.info === 'function')
		&& $log.info('[ACTION ERROR]:', config.url, '[', (config.actionEndDate - config.actionStartDate), 'ms]');

		// callback if necessary
		var rejectErrShow = false;
		if (typeof errcallback === 'function') {
			// log starting callback
			config.callbackStartDate = new Date();
			($log && typeof $log.info === 'function')
			&& $log.info('[CALLBACK ERROR START]:', config.url);

			// callback
			rejectErrShow = errcallback(
					(Object.keys(data).length ? data : cfg.length ? cfg : meta),
					status, headers, statusText);

			// log ending callback
			config.callbackEndDate = new Date();
			($log && typeof $log.info === 'function')
			&& $log.info('[CALLBACK ERROR END]:', config.url, '[', (config.callbackEndDate - config.callbackStartDate), 'ms]');
		}

		// TODO: show error
		if (!rejectErrShow) {
			// show error if response error is not object
			if ((status >= 300 || parseFloat(String(status), 10) >= 300)
				&& typeof data !== 'object' && !angular.isArray(data)) {
				toaster.pop('error', $document[0].title, data,
					statusText && statusText.length ? statusText : null, 'trustedHtml');

			// show error if response error is object
			} else if ((status >= 300 || parseFloat(String(status), 10) >= 300)
				&& (typeof data === 'object' || angular.isArray(data))
				&& angular.isDefined($rootScope)) {
				// check data for mapping
				var respData = [];
				// show if status is un-processable entity
				var showError = (status != 422);
				if (!showError) {
					showError = true;
					// check responsed errors
					if (angular.isDefined(data)
							&& !angular.isArray(data) && Object.keys(data).length) {
						respData = [ data ];
					} else {
						respData = data;
					}

					// find forms
					var forms = [];
					if (typeof $rootScope.findFormsArray === 'function') {
						forms = $rootScope.findFormsArray();
					}
					// if found forms; apply validation errors if necessary
					if (angular.isDefined(forms) && forms.length) {
						$.map(forms, function(f) {
							f['$serverErrors'] = [].concat(respData);
							showError = false;
						});
					}

					// find fields
					var fields = [];
					if (typeof $rootScope.findFieldsArray === 'function') {
						fields = $rootScope.findFieldsArray();
					}
					// if found fields; apply validation errors if necessary
					if (angular.isDefined(fields) && fields.length) {
						$.map(fields, function(f) {
							f['$serverErrors'] = [];
							f['$serverErrorMessages'] = {};
							$.map(respData, function(d) {
								if (d.field && d.field.length
									&& d.code && d.code.length
									&& d.message && d.message.length) {
									f['$serverErrors'].push(d);
									if (!f['$serverErrorMessages'].hasOwnProperty(d.field)) {
										f['$serverErrorMessages'][d.field] = {};
									}
									if (!f['$serverErrorMessages'][d.field].hasOwnProperty('$messages')) {
										f['$serverErrorMessages'][d.field]['$messages'] = [];
									}
									f['$serverErrorMessages'][d.field]['$messages'].push(d.message);
									f['$serverErrorMessages'][d.field]['$message'] = f['$serverErrorMessages'][d.field]['$messages'].join('<br>');
									if (f.$name && f.$name === d.field) {
										f.$setValidity && f.$setValidity('$serverError', false);
									}
									showError = false;
								}
							});
						});
					}
				}

				// if not found forms/fields; just showing as JSON error
				if (showError && data != null && Object.keys(data).length) {
					toaster.pop('error', $document[0].title, angular.fromJson(data),
						statusText && statusText.length ? statusText : null);
				} else if (showError) {
					toaster.pop('error', $document[0].title, cfg.length ? cfg : meta);
				}

			// network connection error
			} else if (status > 0) {
				toaster.pop('error', $document[0].title,
					statusText && statusText.length ? statusText : Messages.getMessage("F0000001"));

			// network connection error
			} else if (status <= 0) {
				toaster.pop('error', $document[0].title, statusText);
			}
		}

		// reset IDLE status
		idleService.reset();
	} catch (e) {
		($log && typeof $log.error === 'function')
		&& $log.error('[ACTION ERROR - ERROR]:', e);
	}
};

/**
 * Internal function to transform AJAX request response
 *
 * @param data response data
 * @param headers request headers
 *
 * @returns transformed response data
 */
function __responseTransform(data, headers) {
	try {
		var JSON_START = /^\[|^\{(?!\{)/;
		var JSON_ENDS = {
			'[' : /]$/,
			'{' : /}$/
		};
		var JSON_PROTECTION_PREFIX = /^\)\]\}',?\n/;
		var APPLICATION_JSON = 'application/json';
		if (angular.isString(data)) {
			// Strip json vulnerability protection prefix and trim whitespace
			var tempData = data.replace(JSON_PROTECTION_PREFIX, '').trim();
			if (tempData) {
				var contentType = headers('Content-Type');
				if (contentType && contentType.indexOf(APPLICATION_JSON) === 0) {
					data = angular.fromJson(tempData);
				} else {
					var jsonStart = tempData.match(JSON_START);
					if (jsonStart && jsonStart.length && JSON_ENDS[jsonStart[0]].test(tempData)) {
						data = angular.fromJson(tempData);
					}
				}
			}
		}
	} catch (e) {}
	return data;
};

//service for demo JSON data
importService(
	'demoService',
	function($http, $q, $log) {
		this.loadDemo = function(jsonfile) {
			var dfd = $q.defer();

			// parse base context path
			var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
			($log && typeof $log.info === 'function')
			&& $log.info('[BASE CONTEXT]:', basepath);

			if (!angular.isArray(jsonfile)) {
				$http.get(basepath + '/resources/demo/' + jsonfile)
					.then(function(res) {
						dfd.resolve(res && res.data ? res.data : {});
					},
						function(err) {
							dfd.reject(err);
						});
			} else {
				for (var json in jsonfile) {
					$http.get(basepath + '/resources/demo/' + json)
						.then(function(res) {
							dfd.resolve(json, res && res.data ? res.data : {});
						},
						function(err) {
							dfd.reject(json, err);
						});
				}
			}
			return dfd.promise;
		};
	}
);

//service for AJAX $http
importService(
	'ajaxService',
	function($http, toaster, idleService, $document, csrfService, $rootScope, $log) {
		// angular ajax
		this.post = function(options) {
			// turn on backdrop
			(typeof turnBackdrop == 'function')
			&& turnBackdrop(true);

			// register for turning off backdrop
			$rootScope._offBackdrop = false;
			var $$digestFn = function() {
				if ($rootScope._offBackdrop == true) {
					$rootScope._offBackdrop = false;
					$rootScope.$off('$$digest', $$digestFn);
					(typeof turnBackdrop == 'function')
					&& turnBackdrop(false);
				}
			};
			$rootScope.$off('$$digest', $$digestFn);
			$rootScope.$on('$$digest', $$digestFn);

			// parse base context path
			var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
			($log && typeof $log.info === 'function')
			&& $log.info('[BASE CONTEXT]:', basepath);

			// parse options
			var callback = (options.success || null);
			var errcallback = (options.error || null);
			var successCallback = (options.statusCode || null);
			options = $.extend(options, {
				method : 'post',
				actionStartDate : new Date(),
				actionEndDate : null,
				callbackStartDate : null,
				callbackEndDate : null,
				// request success handler
				// function(data, status, header, config) {}
				success: function(data, status, headers, config) {
					// handle success response
					__successHandler(
							callback, successCallback, options.error,
							data, status, headers, config,
							$log, toaster, idleService);

					// turn off backdrop
					$rootScope._offBackdrop = true;
				},
				// request failure handler
				// function(data, status, header, config) {}
				error: function(data, status, headers, config, statusText) {
					// turn off backdrop
					$rootScope._offBackdrop = true;

					// handle error response
					__errorHandler(
							$rootScope, errcallback,
							data, status, headers, config, statusText,
							$log, toaster, idleService, $document);
				},
				/**
				 * Clone from angular
				 */
				transformResponse: __responseTransform
			});
			if (options.url)
				options.url = basepath + options.url;

			// debug
			($log && typeof $log.info === 'function')
			&& $log.info('[ACTION START]:', options.url);
			($log && typeof $log.info === 'function')
			&& $log.info('[ACTION INPUT]:', options.data);

			// post
			try {
				// post data
				if (typeof $http.success === 'function') {
					// FIXME from angular ver.1.5.8 and early
					$http(options)
						.success(options.success)
						.error(options.error);
				} else {
					// FIXME from angular ver.1.6.3 and later
					// The response object has these properties:
					// + data – {string|Object} – The response body transformed with the transform functions.
					// + status – {number} – HTTP status code of the response.
					// + headers – {function([headerName])} – Header getter function.
					// + config – {Object} – The configuration object that was used to generate the request.
					// + statusText – {string} – HTTP status text of the response.
					// + xhrStatus – {string} – Status of the XMLHttpRequest (complete, error, timeout or abort).
					$http(options).then(
							function success(response) {
								options.success(
										(response !== null && response !== undefined ? response.data || {} : {}),
										(response !== null && response !== undefined ? response.status || 200 : 200),
										(response !== null && response !== undefined ? response.headers || {} : {}),
										(response !== null && response !== undefined ? response.config || {} : {}),
										(response !== null && response !== undefined ? response.statusText || 'OK' : 'OK'),
										(response !== null && response !== undefined ? response.xhrStatus || 'complete' : 'complete')
								);
							}
							, function error(response) {
								options.error(
										(response !== null && response !== undefined ? response.data || {} : {}),
										(response !== null && response !== undefined ? response.status || 200 : 200),
										(response !== null && response !== undefined ? response.headers || {} : {}),
										(response !== null && response !== undefined ? response.config || {} : {}),
										(response !== null && response !== undefined ? response.statusText || 'OK' : 'OK')
								);
							});
				}
			} catch (e) {
				($log && typeof $log.error === 'function')
				&& $log.error('[ERROR]:', options);
			}
		};

		// jquery ajax
		this.jpost = function(options) {
			// parse base context path
			var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
			($log && typeof $log.info === 'function')
			&& $log.info('[BASE CONTEXT]:', basepath);

			// check options
			options = options || {};
			var errcallback = (options.error || null);
			options = $.extend(options, {
				type : 'post',
				actionStartDate : new Date(),
				actionEndDate : null,
				// エラー
				error : function(xhr, status, errorThrown) {
					// detect response error
					if ((!!errorThrown || errorThrown.length <= 0)
						&& xhr && xhr.responseText && xhr.responseText.length > 0) {
						errorThrown = xhr.responseText;
					}
					if (isNaN(status) && xhr && !isNaN(xhr.status))
						status = xhr.status;
					if ((!!errorThrown || errorThrown.length <= 0) && typeof parseMetaData === 'function') {
						errorThrown = parseMetaData('e' + status);
					}

					// log end request
					($log && typeof $log.info === 'function')
					&& $log.info(status, '[', (xhr.responseText && xhr.responseText.length > 0 ? xhr.responseText : 'Unknown'), ']');

					// log request error data
					($log && typeof $log.error === 'function')
					&& $log.error(options.data);

					// log end request
					($log && typeof $log.info === 'function')
					&& $log.info('[ACTION ERROR]:', options.url);

					// callback if necessary
					if (errcallback) {
						// log starting callback
						var callbackstart = new Date();
						($log && typeof $log.info === 'function')
						&& $log.info('[CALLBACK ERROR START]:', options.url);

						// callback
						errcallback(xhr, status, errorThrown);

						// log ending callback
						($log && typeof $log.info === 'function')
						&& $log.info('[CALLBACK ERROR END]:', options.url, '[', (new Date() - callbackstart), 'ms]');
					}
					// default processing
					else {
						switch (status) {
						// サーバーに接続できませんでした。
						case 0: {
							errorThrown = (errorThrown && errorThrown.length
								? errorThrown : 'サーバーに接続できませんでした。ネットワークをチェックしてください。');
							toaster.show({
								type : 4,
								body : errorThrown
							});
							break;
						}
						// サーバー
						case 500: {
							errorThrown = (errorThrown && errorThrown.length
								? errorThrown : 'サーバーエラーが発生しました。');
							toaster.show({
								type : 4,
								body : errorThrown
							});
							break;
						}
						// OK
						case 200:
							return;
						default: {
							toaster.show({
								type : 4,
								body : errorThrown
							});
							break;
						}
						}
					}

					// reset IDLE status
					idleService.reset();
				}
			});
			if (options.url)
				options.url = basepath + options.url;

			// jquery ajax request
			$.ajax(options);
		};
	}
);

// service for IDLE state
importService(
	'idleService',
	function($timeout, $window, $log) {
		this.timers = [];

		this.start = function(action, timeout) {
			if (timeout > 0 && angular.isFunction(action)) {
				this.timers[this.timers.length] = $timeout(action, timeout);
			}
			return (this.timers.length - 1);
		};

		this.stop = function(idx) {
			if (0 < idx && idx < this.timers.length) {
				$timeout.cancel(this.timers[idx]);
			}
		};

		this._clear = function() {
			if (this.timers.length > 0) {
				for (var i = 0; i < this.timers.length; i++) {
					$timeout.cancel(this.timers[i]);
				}
				this.timers = [];
			}
		};

		this.reset = function() {
			this._clear();
			this.start();
		};
	}
);

// service for downloading/uploading file
importService(
	'fileService',
	function($http, $window, $document, toaster, idleService, csrfService, $log, $rootScope) {
		// download file
		this.downloadFile = function(action, params) {
			// parse base context path
			var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');

			// prepare download form
			var path = basepath + action;
			var htmlform = "<form method='post' action='" + path + "' name='form'>";
			var token = parseTokenHeader(null, csrfService);
			var tokenparamname = parseTokenParameterName();
			var data = (params && angular.isObject(params.data)
				? angular.extend({}, params.data)
				: params && angular.isObject(params)
					? angular.extend({}, params) : {});
			htmlform += "<input type='hidden' name='" + tokenparamname + "' value='" + token + "'>";
			data = $.buildSimpleObject(data);
			$.each(data, function(key, value) {
				if (key !== '$$hashKey') {
					if (!angular.isArray(value) && value != null && value != undefined) {
						// just object, not date
						if (angular.isObject(value) && !$.isDate(value)) {
							$.each(value, function(k, v) {
								if (k !== '$$hashKey' && v != null && v != undefined) {
									if ($.isDate(v)) {
										if (typeof v.toISOString === 'function') {
											htmlform += '<input type="hidden" name="' + key + '.' + k + '" value="' + v.toISOString() + '" />';
										} else {
											htmlform += '<input type="hidden" name="' + key + '.' + k + '" value="' + v.getTime() + '" />';
										}
									} else {
										htmlform += '<input type="hidden" name="' + key + '.' + k + '" value="' + v + '" />';
									}
								}
							});

							// date object
						} else if ($.isDate(value)) {
							if (typeof value.toISOString === 'function') {
								htmlform += '<input type="hidden" name="' + key + '" value="' + value.toISOString() + '" />';
							} else {
								htmlform += '<input type="hidden" name="' + key + '" value="' + value.getTime() + '" />';
							}

							// simple value
						} else {
							htmlform += '<input type="hidden" name="' + key + '" value="' + value + '" />';
						}

					} else if (angular.isArray(value) && value.length > 0) {
						$.each(value, function(i) {
							var val = this;
							// just object, not date
							if (angular.isObject(val) && !$.isDate(val)) {
								$.each(val, function(k, v) {
									if (k !== '$$hashKey' && v != null && v != undefined) {
										if ($.isDate(v)) {
											if (typeof v.toISOString === 'function') {
												htmlform += '<input type="hidden" name="' + key + '[' + i + '].' + k + '" value="' + v.toISOString() + '" />';
											} else {
												htmlform += '<input type="hidden" name="' + key + '[' + i + '].' + k + '" value="' + v.getTime() + '" />';
											}
										} else {
											htmlform += '<input type="hidden" name="' + key + '[' + i + '].' + k + '" value="' + v + '" />';
										}
									}
								});

								// date object
							} else if ($.isDate(val)) {
								if (typeof val.toISOString === 'function') {
									htmlform += '<input type="hidden" name="' + key + '[' + i + ']" value="' + val.toISOString() + '" />';
								} else {
									htmlform += '<input type="hidden" name="' + key + '[' + i + ']" value="' + val.getTime() + '" />';
								}

								// simple value
							} else {
								htmlform += '<input type="hidden" name="' + key + '[' + i + ']" value="' + val + '" />';
							}
						});
					}
				}
			});
			htmlform += "</form>";

			// log form submit
			($log && typeof $log.info === 'function')
			&& $log.info('[SUBMIT   START]:', action, params);

			// first check for removing
			$('#downloadIframe').remove();

			// submit form
			var iframe = document.createElement("IFRAME");
			iframe.setAttribute("id", "downloadIframe");
			iframe.setAttribute("scrolling", "no");
			iframe.setAttribute("frameborder", "no");
			iframe.setAttribute("style", "height: 0px; width: 0px; position: absolute; left: -1000px; top: -1000px;");
			document.body.appendChild(iframe);
			// Gets content window from i-frame
			var win = (iframe.contentWindow || iframe.contentDocument);
			if (win == null || win == undefined) {
				document.body.removeChild(iframe);
				return;
			}
			var ifrm_doc = win;
			if (win.document) {
				ifrm_doc = win.document;
			}
			if (ifrm_doc == null || ifrm_doc == undefined) {
				document.body.removeChild(iframe);
				return;
			}
			// -------------------------------------------------
			// Outs the preview part to i-frame
			// -------------------------------------------------
			ifrm_doc.open();
			var tokenname = parseTokenHeaderName();
			ifrm_doc.open();
			ifrm_doc.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">'
				+ '<head></head><body></body></html>');
			var ifrm_head = ifrm_doc.getElementsByTagName("head")[0];
			ifrm_head.innerHTML = "<title>Download Iframe</title>"
				+ "<meta name='_csrf' content='" + token + "'/>"
				+ "<meta name='_csrf_header' content='" + tokenname + "'/>";
			var ifrm_body = ifrm_doc.getElementsByTagName("body")[0];
			ifrm_body.innerHTML = htmlform;
			ifrm_doc.close();
			var form = $(ifrm_doc).find('form');
			form.submit();
		};

		// download file
		this.jdownloadFile = function(action, params) {
			// parse base context path
			var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
			var data = (params && angular.isObject(params.data)
				? angular.extend({}, params.data)
				: params && angular.isObject(params)
					? angular.extend({}, params) : {});
			var token = parseTokenHeader(null, csrfService);
			var tokenparamname = parseTokenParameterName();
			data[tokenparamname] = token;

			// prepare download parameters
			var path = basepath + action;
			params = $.extend({
				httpMethod : 'POST'
			}, params);
			params.data = $.extend({}, data);

			// prepare download request headers
			var tokenname = parseTokenHeaderName();
			params.headers = [ "<meta name='_csrf' content='" + token + "'/>",
				"<meta name='_csrf_header' content='" + tokenname + "'/>" ];

			// download file
			$.fileDownload(path, params);
		};

		/**
		 * Upload file
		 */
		this.uploadFile = function(action, files, params, callbackFn, callbackFnErr) {
			// turn on backdrop
			(typeof turnBackdrop == 'function')
			&& turnBackdrop(true);

			// register for turning off backdrop
			$rootScope._offBackdrop = false;
			var $$digestFn = function() {
				if ($rootScope._offBackdrop == true) {
					$rootScope._offBackdrop = false;
					$rootScope.$off('$$digest', $$digestFn);
					(typeof turnBackdrop == 'function')
					&& turnBackdrop(false);
				}
			};
			$rootScope.$off('$$digest', $$digestFn);
			$rootScope.$on('$$digest', $$digestFn);

			// parse base context path
			var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');

			// prepare upload form
			var url = basepath + action;
			var formData = new FormData();
			var data = (params && angular.isObject(params.data)
				? angular.extend({}, params.data)
				: params && angular.isObject(params)
					? angular.extend({}, params) : {});
			data = $.buildSimpleObject(data);

			// attach files
			if (!angular.isArray(files) && files != null) {
				formData.append("files[0]", files);
			} else if (angular.isArray(files) && files.length) {
				var i = 0;
				$.map(files, function(f) {
					if (f != null) {
						formData.append("files[" + i + "]", f);
						i++;
					}
				});
			}

			// attached JSON data
			$.each(data, function(key, value) {
				if (key !== '$$hashKey') {
					if (!angular.isArray(value) && value != null && value != undefined) {
						// just object, not date
						if (angular.isObject(value) && !$.isDate(value)) {
							$.each(value, function(k, v) {
								if (k !== '$$hashKey' && v != null && v != undefined) {
									if ($.isDate(v)) {
										if (typeof v.toISOString === 'function') {
											formData.append(key + '.' + k, v.toISOString());
										} else {
											formData.append(key + '.' + k, v.getTime());
										}
									} else {
										formData.append(key + '.' + k, v);
									}
								}
							});

							// date object
						} else if ($.isDate(value)) {
							if (typeof value.toISOString === 'function') {
								formData.append(key, value.toISOString());
							} else {
								formData.append(key, value.getTime());
							}

							// simple value
						} else {
							formData.append(key, value);
						}

					} else if (angular.isArray(value) && value.length > 0) {
						$.each(value, function(i) {
							var val = this;
							// just object, not date
							if (angular.isObject(val) && !$.isDate(val)) {
								$.each(val, function(k, v) {
									if (k !== '$$hashKey' && v != null && v != undefined) {
										if ($.isDate(v)) {
											if (typeof v.toISOString === 'function') {
												formData.append(key + '[' + i + '].' + k, v.toISOString());
											} else {
												formData.append(key + '[' + i + '].' + k, v.getTime());
											}
										} else {
											formData.append(key + '[' + i + '].' + k, v);
										}
									}
								});

								// date object
							} else if ($.isDate(val)) {
								if (typeof val.toISOString === 'function') {
									formData.append(key + '[' + i + ']', val.toISOString());
								} else {
									formData.append(key + '[' + i + ']', val.getTime());
								}

								// simple value
							} else {
								formData.append(key + '[' + i + ']', val);
							}
						});
					}
				}
			});

			// log starting upload
			var actionStartDate = new Date();
			($log && typeof $log.info === 'function')
			&& $log.info('[ACTION START]:', url);
			($log && typeof $log.info === 'function')
			&& $log.info('[ACTION FILE ]:', files);
			($log && typeof $log.info === 'function')
			&& $log.info('[ACTION INPUT]:', params);

			// apply request headers
			var headers = prepareXhrHeaders(null, csrfService);
			headers['Content-Type'] = undefined;
			headers['Accept'] = 'application/json; charset=utf-8';

			// upload file
			var callback = (callbackFn || null);
			var errcallback = (callbackFnErr || null);
			try {
				// functions callback
				var onError = function(data, status, headers, config, statusText) {
					// turn off backdrop
					$rootScope._offBackdrop = true;

					// handle error response
					__errorHandler(
							$rootScope, errcallback,
							data, status, headers, config, statusText,
							$log, toaster, idleService, $document);
				};
				var onSuccess = function(data, status, headers, config) {
					// handle success response
					__successHandler(
							callback, null, onError,
							data, status, headers, config,
							$log, toaster, idleService);

					// turn off backdrop
					$rootScope._offBackdrop = true;
				};
				// post data
				if (typeof $http.success === 'function') {
					// FIXME from angular ver.1.5.8 and early
					$http.post(
						url,
						formData,
						{
							transformRequest : angular.identity,
							transformResponse: __responseTransform,
							headers : headers
						}
					)
					// request success handler
					.success(onSuccess)
					// request error handler
					.error(onError);
				} else {
					// FIXME from angular ver.1.6.3 and later
					// The response object has these properties:
					// + data – {string|Object} – The response body transformed with the transform functions.
					// + status – {number} – HTTP status code of the response.
					// + headers – {function([headerName])} – Header getter function.
					// + config – {Object} – The configuration object that was used to generate the request.
					// + statusText – {string} – HTTP status text of the response.
					// + xhrStatus – {string} – Status of the XMLHttpRequest (complete, error, timeout or abort).
					$http.post(
						url,
						formData,
						{
							transformRequest : angular.identity,
							transformResponse: __responseTransform,
							headers : headers
						}
					).then(
							function success(response) {
								onSuccess(
										(response !== null && response !== undefined ? response.data || {} : {}),
										(response !== null && response !== undefined ? response.status || 200 : 200),
										(response !== null && response !== undefined ? response.headers || {} : {}),
										(response !== null && response !== undefined ? response.config || {} : {}),
										(response !== null && response !== undefined ? response.statusText || 'OK' : 'OK'),
										(response !== null && response !== undefined ? response.xhrStatus || 'complete' : 'complete')
								);
							}
							, function error(response) {
								onError(
										(response !== null && response !== undefined ? response.data || {} : {}),
										(response !== null && response !== undefined ? response.status || 200 : 200),
										(response !== null && response !== undefined ? response.headers || {} : {}),
										(response !== null && response !== undefined ? response.config || {} : {}),
										(response !== null && response !== undefined ? response.statusText || 'OK' : 'OK')
								);
							}
					);
				}
			} catch (e) {
				($log && typeof $log.error === 'function')
				&& $log.error('[ERROR]:', e, ' - PARAMETERS: ', params);
			}
		};
	}
);

//service for detecting browser
importService(
	'browserService',
	function($window) {
		this.detect = function() {
			var userAgent = $window.navigator.userAgent;
			var browsers = {
				chrome: /chrome/i,
				safari: /safari/i,
				firefox: /firefox/i,
				mozilla: /mozilla/i,
				msie: /internet explorer/i,
				opera: /opr/i,
				appleWebKit: /applewebkit/i
			};
			var browser = 'unknown';
			angular.forEach(browsers, function(v, k) {
				this[k] = v.test(userAgent);
				if (this[k] == true) browser = k;
			});
			return browser;
		};
	});

//service for printting
importService(
	'printService',
	function(browserService, $compile, $document, $rootScope, $log) {
		/**
		 * Print element from selector with the specified title
		 *
		 * @param elSelector selector to detect elements with angular.element(#selector)
		 * @param title print preview title
		 */
		this.print = function(elSelector, title) {
			var element = ((elSelector || '').length
					? angular.element(elSelector) : $document);
			if (!(element || []).length) {
				($log && typeof $log.warn === 'function')
				&& $log.warn('Could not found any element to print by selector [', elSelector, ']!!!')
				return;
			} else if (!($document || []).length || !$document[0].body) {
				($log && typeof $log.warn === 'function')
				&& $log.warn('Could not inject $document to detect elements!!!')
				return;
			}

			// -------------------------------------------------
			// element not appear in print
			// -------------------------------------------------
			var bkstyles = [];
			var blockPrints = [].concat(element.find(".print-block") || [])
							.concat(element.find("[print-block]") || [])
							.concat(element.find("[data-print-block]") || [])
							.concat(element.find("#printBlock") || []);
			if ((blockPrints || []).length) {
				angular.forEach(blockPrints, function(el) {
					var $this = angular.element(el);
					var bkstyle = $this.css('display');
					bkstyles.push((bkstyle || '').length ? bkstyle : 'block');
					$this.css('display', 'none');
				});
			}

			// -------------------------------------------------
			// Creates hidden i-frame
			// -------------------------------------------------
			var document = $document[0];
			var body = document.body;
			var eleParent = angular.element(element[0].parentNode);
			var ifrm = ((eleParent || []).length ? eleParent.find('iframe') : []);
			if ((ifrm || []).length) ifrm.remove();
			ifrm = document.createElement("IFRAME");
			var ifrmId = ('IFR-' + (new Date()).getTime());
			ifrm.setAttribute("id", ifrmId);
			ifrm.setAttribute("name", ifrmId);
			ifrm.setAttribute("scrolling", "no");
			ifrm.setAttribute("frameborder", "no");
			ifrm.setAttribute("style", "height: 0px; width: 0px; position: absolute; left: -1000px; top: -1000px;");
			eleParent[0].appendChild(ifrm);
			// Gets content window from i-frame
			var win = (ifrm.contentWindow || ifrm.contentDocument);
			if (win == null || win == undefined) {
				eleParent[0].removeChild(ifrm);
				return;
			}
			var ifrm_doc = win;
			if (win.document) ifrm_doc = win.document;
			if (ifrm_doc == null || ifrm_doc == undefined) {
				eleParent[0].removeChild(ifrm);
				return;
			}

			// -------------------------------------------------
			// Outs the preview part to i-frame
			// -------------------------------------------------
			ifrm_doc.open();
			// add document type for html page, ie will be verified by document type
			ifrm_doc.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">');

			// -------------------------------------------------
			// Applies document title
			// -------------------------------------------------
			var ifrm_head = ifrm_doc.getElementsByTagName("head")[0];
			title = (!(title || '').length ? ((APP_CUSTOMER || '') + " Printer") : title);
			if (ifrm_head != null && ifrm_head != undefined) {
				var ifrm_title = ifrm_head.getElementsByTagName("title")[0];
				if (ifrm_title != null && ifrm_title != undefined) {
					ifrm_title.innerHTML = title;
				}
				else {
					ifrm_head.write("<title>" + title + "</title>");
				}
			}
			else {
				ifrm_doc.write("<title>" + title + "</title>");
			}

			// -------------------------------------------------
			// Applies all style-sheet from main document to i-frame
			// -------------------------------------------------
			// gets all style-sheet from main document
			var stylesheets = document.getElementsByTagName('link');
			// if valid main document style-sheets and valid head element of i-frame
			if ((stylesheets || []).length) {
				// parses every main document style-sheet
				angular.forEach(stylesheets, function(s) {
					// if valid main document style-sheet
					if (((s.rel || '').toLowerCase() == 'stylesheet') && (s.href || '').length) {
						if ((s.media || '').length) {
							ifrm_doc.write("<link type='text/css' rel='stylesheet' href='" + s.href + "' media='" + s.media + "' />");
						}
						else {
							ifrm_doc.write("<link type='text/css' rel='stylesheet' href='" + s.href + "' media='all' />");
						}
					}
				});
			}
			// compile HTML
			ifrm_doc.write(element[0].innerHTML);
			ifrm_doc.close();

			// -------------------------------------------------
			// Updates for printing background color
			// -------------------------------------------------
			// $("#" +
			// ifrm.getAttribute('id')).contents().find('.freemarker_table_header').each(function(element){
			// // get the size, position and background color
			// // of the element
			// var size = element.getSize();
			// var coord = element.getCoordinates();
			// var elemBgColor = element.getStyle('background-color');
			//
			// // set the original background to nothing
			// element.setStyles({'background': 'none'});
			//
			// // create a <div> that will have top and bottom
			// // borders half the height of the parent element.
			// // also position the element exacly as the parent
			// // and place it behind
			// var borderDiv = $('div', {
			// 'styles': {
			// 'position': 'absolute',
			// 'top': coord.top,
			// 'left': coord.left,
			// 'border-top': size.y/2 + 'px solid ' + elemBgColor,
			// 'border-bottom': size.y/2 + 'px solid ' + elemBgColor,
			// 'width': size.x,
			// 'height': '0',
			// 'z-index': '-1'
			// }
			// });
			//
			// // add the new <div> as a child under the parent element
			// borderDiv.inject(element);
			// });

			// -------------------------------------------------
			// Applies frame body style
			// -------------------------------------------------
			var mainBodyStyle = body.getAttribute("class");
			var ifrm_body = ifrm_doc.getElementsByTagName("body")[0];
			if ((mainBodyStyle || '').length && ifrm_body != null && ifrm_body != undefined) {
				ifrm_body.setAttribute("class", mainBodyStyle);
			}
			// waiting for element digest
			ifrm_body.onload = function() {
				// -------------------------------------------------
				// Calculates iframe viewport size for printing on Opera
				// -------------------------------------------------
				var viewportwidth;
				var viewportheight;
				// the more standards compliant browsers (mozilla/netscape/opera/IE7) use window.innerWidth and window.innerHeight
				if (typeof window.innerWidth != 'undefined') {
					viewportwidth = window.innerWidth;
					viewportheight = window.innerHeight;
				}
				// IE6 in standards compliant mode (i.e. with a valid doctype as the first line in the document)
				else if (typeof document.documentElement != 'undefined'
					&& typeof document.documentElement.clientWidth != 'undefined'
						&& document.documentElement.clientWidth != 0) {
					viewportwidth = document.documentElement.clientWidth;
					viewportheight = document.documentElement.clientHeight;
				}
				// older versions of IE
				else {
					viewportwidth = angular.element(body).clientWidth;
					viewportheight = angular.element(body).clientHeight;
				}
				viewportheight -= 40;
				viewportwidth -= 25;

				// -------------------------------------------------
				// Prints iframe
				// -------------------------------------------------
				var msie = ((browserService && browserService.msie) || ($.browser && $.browser.msie));
				var opera = ((browserService && browserService.opera) || ($.browser && $.browser.opera));
				if (msie) {	/* IE */
					win.focus();
					ifrm_doc.execCommand('print', false, null);
				} else if (opera) { /* Opera */
					var sheet = $(
							[
							 '<style>',
							 '	body * {',
							 '		display: none;',
							 '	}',
							 '<\/style>'
							].join('')
					);
					angular.element('head').append(sheet);
				    // shows iframe full-screen on all elements
					var ifrmstyle = ifrm.getAttribute("style");
					ifrm.setAttribute(
							"style",
							"height: " + viewportheight + "px; width: " + viewportwidth + "px; "
							+ "position: absolute; left: 0px; top: 0px; "
							+ "z-index: 99999; "
							+ "background-color: #FFFFFF; border: none; "
							+ "display: block; margin: 0px; padding: 0px; "
					);
					// waits for printing
					$rootScope.sleep(500);
					// prints iframe
					win.focus();
					win.print();
				    // hides iframe
					sheet.remove();
				    ifrm.setAttribute("style", ifrmstyle);
				} else {
					win.focus();
					win.print();
				}

				// -------------------------------------------------
				// Removes iframe
				// -------------------------------------------------
				self.focus();
				// Re-shows the excluded print part
				if ((blockPrints || []).length) {
					var i = -1;
					angular.forEach(blockPrints, function(el) {
						i++;
						var $this = angular.element(el);
						(bkstyles[i] || '').length
						&& $this.css('display', bkstyles[i]);
					});
				}
			};
		};
	}
);