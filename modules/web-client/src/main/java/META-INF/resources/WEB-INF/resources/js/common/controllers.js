'use strict';

// Main Controller
importController(
		APP_CTRL,
		function($rootScope, $scope, $window, $log, $http,
				$confirm, $locale, $filter, $document,
				toaster, idleService, ajaxService, csrfService,
				$controller, $compile, $templateCache, ModalService,
				localStorageService, $position, $timeout) {

			// -------------------------------------------------
		    // MAPPING SOME VARIABLES
		    // -------------------------------------------------
			$rootScope.Math = $window.Math;
			$scope.Math = $rootScope.Math;

			// -------------------------------------------------
		    // STRICT SCOPE FOR OVERRIDING FUNCTIONS
		    // -------------------------------------------------
			$scope.strictScope = function(scope) {
				$scope.strictControllerScope(APP_CTRL, scope);
			};
			$scope.strictControllerScope = function(controller, scope) {
				if (!controller || !controller.length || !scope) return;
				'use strict';
				$controller(controller, { $scope: scope });
			};

			// -------------------------------------------------
		    // BARCODE SCANNER
		    // -------------------------------------------------
			$rootScope.onBarcodeScan = function(barcode) {
				((BARCODE_SUPPORT || false) && (barcode || '').length > 0)
				&& ($log && typeof $log.info === 'function')
				&& $log.info('>>> SCANNED: ', { barcode: barcode });
				((BARCODE_SUPPORT || false) && (barcode || '').length > 0)
				&& $rootScope.$broadcast('$barcode', { barcode: barcode });
			};
			$scope.onBarcodeScan = $rootScope.onBarcodeScan;

			// -------------------------------------------------
		    // LOCAL STORAGE
		    // -------------------------------------------------
			/**
			 * Save the specified data to local storage with key
			 *
			 * @param key data key
			 * @param data to save
			 */
			$rootScope.toStorage = function(key, data) {
				if ((key || '').length > 0 && data) {
					// date
					if (Object.prototype.toString.call(data) === "[object Date]"
						&& typeof data.getTime === 'function' && !isNaN(data.getTime())) {
						data = data.getTime();

						// string
					} else if (typeof data === 'string' || data instanceof String) {
						;

						// object
					}  else if (typeof data === 'object') {
						try { data = angular.toJson(data); }
						catch (e) { data = null; }
					}
					if (data != null && data != undefined) {
						localStorageService.set(key, data);
					}
				}
			};
			$scope.toStorage = $rootScope.toStorage;
			/**
			 * Get data from local storage by key
			 *
			 * @param key data key
			 *
			 * @return data from storage
			 */
			$rootScope.fromStorage = function(key) {
				var data = null;
				if ((key || '').length > 0) {
					data = localStorageService.get(key);
				}
				return data;
			};
			$scope.fromStorage = $rootScope.fromStorage;
			/**
			 * Remove data from local storage by arguments
			 */
			$rootScope.clearStorage = function() {
				if (arguments && arguments.length > 0) {
					for(var i = 0; i < arguments.length; i++) {
						var key = arguments[i];
						localStorageService.remove(key);
					}
				}
			};
			$scope.clearStorage = $rootScope.clearStorage;

		    // -------------------------------------------------
		    // DATE-TIMEPICKER SETTINGS
		    // -------------------------------------------------
			$rootScope.calendar = {
					opened : {}
					, dateOptions: {}
					, open : function($event, which) {
						$event.preventDefault();
						$event.stopPropagation();
						if (Object.keys($scope.calendar.opened).length) {
							$scope.calendar.opened = {};
						}
						$scope.calendar.opened[which] = true;
					}
			};
			$scope.calendar = $.extend({}, $rootScope.calendar);
			/**
			 * Sleep in milliseconds
			 * @param millis to sleep
			 */
			$rootScope.sleep = function(millis) {
				var ms = (isNaN(millis) || millis <= 0 ? 0 : millis);
				if (ms <= 0) return;
				var date = new Date();
				var curDate = null;
				do { curDate = new Date(); } while ((curDate - date) < ms);
			};
			$scope.sleep = $rootScope.sleep;
			/**
			 * Support for re-rendering date picker control views
			 */
			$rootScope.refreshDatePickerView = function() {
				var ngModelControllers = $rootScope['$$NgModelControllers'] || $scope['$$NgModelControllers'] || [];
				if (arguments && arguments.length > 0 && ngModelControllers.length) {
					for(var i = 0; i < arguments.length; i++) {
						var datePickerCtrlName = arguments[i];
						if ((datePickerCtrlName || '').length > 0) {
							var datePickerCtrl = ngModelControllers.find(ctrl => (ctrl['$name'] || '') === datePickerCtrlName);
							datePickerCtrl
							&& typeof datePickerCtrl.$render === 'function'
							&& datePickerCtrl.$render();
						}
					}
				}
			};
			$scope.refreshDatePickerView = $rootScope.refreshDatePickerView;

		    // -------------------------------------------------
		    // ANALYSING OUTBOUND DATA
		    // -------------------------------------------------
			$rootScope.data = {};
			$scope.data = {};
			/** Analysing window variable and import into angular scope */
			$rootScope.analyseData = function() {
				var keys = Object.keys($window);
				if (keys && keys.length > 0) {
					$.each($window, function(key, value) {
						if (value && typeof value !== 'function') {
							$rootScope.data[key] = value;
							$scope.data[key] = value;
							$rootScope[key] = value;
							$scope[key] = value;
						}
					});
				}
				// process language
				if ($scope.pageInfo && $scope.pageInfo.languages && $scope.pageInfo.languages.length
						&& $scope.pageInfo.locale && $scope.pageInfo.locale.length) {
					var languages = $scope.pageInfo.languages;
					var language = $scope.pageInfo.locale;
					language = $filter('propsEqualFilterFirst')(
							languages, {
								filterKey: 'languages',
								expression: { code: language }
							});
					$rootScope.data['languages'] = languages;
					$scope.data['languages'] = languages;
					$scope['languages'] = languages;
					$rootScope.data['language'] = language;
					$scope.data['language'] = language;
					$scope['language'] = language;
				}
				// debug
				($log && typeof $log.debug === 'function')
				&& $log.debug('Scanned window variables:', $scope.data);
			};
			$scope.portData = function() { $rootScope.analyseData(); };
			$rootScope.analyseData();
			/** Analyse window variable and import into scope */
			$rootScope.$on('$load', $rootScope.analyseData());
			/** Analysing window variable and import into angular scope */
			$rootScope.findData = function(key) {
				// find from child scope
				if (!!$scope.data && $scope.data.hasOwnProperty(key)) {
					return $scope.data[key];
				} else if ($scope.hasOwnProperty(key)) {
					return $scope[key];
				}
				// find from root scope
				if (!!$rootScope.data && $rootScope.data.hasOwnProperty(key)) {
					return $rootScope.data[key];
				} else if ($rootScope.hasOwnProperty(key)) {
					return $rootScope[key];
				}
				// find from window
				if ($window.hasOwnProperty(key) && !!$window[key] && typeof $window[key] !== 'function') {
					$rootScope.data[key] = value;
					$scope.data[key] = value;
					$rootScope[key] = value;
					$scope[key] = value;
					return $window[key];
				}
				// if not found
				return null;
			};
			/**
			 * Compile template by identity and scope data
			 *
			 * @param name template identity
			 * @param scope scope data
			 *
			 * @return compiled template
			 */
			$rootScope.compileTemplate = function(name, scope) {
				scope = (scope == null ? $scope : scope);
				var template = $templateCache.get(name);
				return (template == null || template == undefined ? null : $compile(template)(scope));
			};
			$scope.compileTemplate = $rootScope.compileTemplate;
			/**
			 * Get the configuration page information
			 * @return the configuration page information
			 */
			$rootScope.findPageInfo = function() {
				return ($rootScope.findData('pageInfo') || {});
			};
			$scope.findPageInfo = $rootScope.findPageInfo;

			// -------------------------------------------------
		    // SUPPORT EVENTS
		    // -------------------------------------------------
			/**
			 * Detect the specified event data and return event moud position such as:
			 * { x: event.pageX (clientX), y: event.pageY (clientY) }
			 *
			 * @param event to parse (x, y < 0 if invalid)
			 *
			 * @reurn { x: valueX, y: valueY }
			 */
			$scope.detectEventPosition = function(event) {
				// detect valid event
				if (event && event.length) event = event[0];
				if (!event) event = window.event;
				try {
					// initialize variables
					var body = document.body;
					var docEl = document.documentElement;
					var el = angular.element(event.currentTarget);
					var elPos = (el && el.length ? $position.position(el)
							: { left: -1, top: -1, width: 0, height: 0 });
					// default position is event target position
					var x = elPos.left, y = (elPos.top + elPos.height);
					// detect X
					if (event.pageX) {
						x = event.pageX;
					} else if (event.clientX) {
						x = event.clientX + body.scrollLeft + docEl.scrollLeft;
					} else if (event.originalEvent
							&& event.originalEvent.touches
							&& event.originalEvent.touches.length) {
						var touch = event.originalEvent.touches[0];
						if (touch.pageX) {
							x = touch.pageX;
						} else if (touch.clientX) {
							x = touch.clientX + body.scrollLeft + docEl.scrollLeft;
						}
					}
					// detect Y
					if (event.pageY) {
						y = event.pageY;
					} else if (event.clientY) {
						y = event.clientY + body.scrollTop + docEl.scrollTop;
					} else if (event.originalEvent
							&& event.originalEvent.touches
							&& event.originalEvent.touches.length) {
						var touch = event.originalEvent.touches[0];
						if (touch.pageY) {
							y = touch.pageY;
						} else if (touch.clientY) {
							y = touch.clientY + body.scrollTop + docEl.scrollTop;
						}
					}
				} catch (e) {
					($log && typeof $log.error === 'function')
					&& $log.error(e);
					x = -1; y = -1;
				}
				return { x: x, y: y };
			};
			$rootScope.detectEventPosition = $scope.detectEventPosition;

		    // -------------------------------------------------
		    // SUPPORT FORM CONTROLLER
		    // -------------------------------------------------
			/**
			 * Listener for the referenceNgModelController event
			 * from the [referenceNgModelController] directive,
			 * to cache the reference NgModelControler reference every it has been created
			 */
            $rootScope.$on('referenceNgModelController', function(e, ctrl) {
                // check if it is not in array already
            	if (!$rootScope.hasOwnProperty('$$NgModelControllers')) {
            		$rootScope['$$NgModelControllers'] = [];
            	}
                // add it to array
            	var found = false;
            	if (ctrl.$name && ctrl.$name.length) {
	            	$.map($rootScope['$$NgModelControllers'], function(c) {
	            		if (c.$name && ctrl.$name === c.$name) {
	            			found = true;
	            		}
	            	});
            	}
            	if (!found) $rootScope['$$NgModelControllers'].push(ctrl);
                // for scope
                if (!$scope.hasOwnProperty('$$NgModelControllers')) {
                	$scope['$$NgModelControllers'] = [];
            	}
                // add it to array
                if (!found) $scope['$$NgModelControllers'].push(ctrl);
            	// check for FormController
            	if (ctrl.$$parentForm) {
            		$rootScope.$broadcast('referenceFormController', ctrl.$$parentForm);
            	}
            });
			/**
			 * Listener for the referenceFormController event
			 * from the [referenceFormController] directive,
			 * to cache the reference FormController reference every it has been created
			 */
            $rootScope.$on('referenceFormController', function(e, ctrl) {
            	// create reset prototype
            	if (ctrl) {
            		var resetMed = function() {
            			(typeof ctrl.$setPristine === 'function')
            			&& ctrl.$setPristine();
            			(typeof ctrl.$setUntouched === 'function')
            			&& ctrl.$setUntouched();
            		};
            		if (ctrl.prototype && !ctrl.prototype.reset) {
            			ctrl.prototype.reset = resetMed;

            		} else if (!ctrl.reset) {
            			ctrl.reset = resetMed;
            		}
            	}
                // check if it is not in array already
            	if (!$rootScope.hasOwnProperty('$$FormControllers')) {
            		$rootScope['$$FormControllers'] = [];
            	}
                if ($rootScope['$$FormControllers'].indexOf(ctrl) < 0) {
                    // add it to array
                	$rootScope['$$FormControllers'].push(ctrl);
                }
                // for scope
                if (!$scope.hasOwnProperty('$$FormControllers')) {
                	$scope['$$FormControllers'] = [];
            	}
                if ($scope['$$FormControllers'].indexOf(ctrl) < 0) {
                    // add it to array
                	$scope['$$FormControllers'].push(ctrl);
                	if (!$.isUndefined(ctrl.$name) && ctrl.$name.length > 0
                			&& !$scope.hasOwnProperty(ctrl.$name)) {
                		$scope[ctrl.$name] = ctrl;
                	}
                }
            });
			/**
			 * Search all form controller in scope<br>
			 *
			 * @reurn form controllers array
			 */
			$scope.findFormsArray = function() {
				var FUNC_NAME = /function (.{1,})\(/;
				var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
				var forms = [];
				// detect by referenceFormController directive
				if (this.hasOwnProperty('$$FormControllers')
						&& angular.isArray(this['$$FormControllers'])
						&& this['$$FormControllers'].length) {
					forms = forms.concat(this['$$FormControllers']);
				}
				// detect by scope property
				$.each(this, function(k, v) {
					var checked = false;
					if (angular.isFunction(v)) {
						var fnStr = v.toString().replace(STRIP_COMMENTS, '');
						var results = (FUNC_NAME).exec(fnStr);
						if (angular.isArray(results) && results.length > 1
								&& results[1] === 'FormController') {
							forms.push(v);
							checked = true;
						}
					}
					if (!checked && v && v.hasOwnProperty('$setSubmitted')
							&& angular.isFunction(v['$setSubmitted'])) {
						forms.push(v);
					}
				});
				return forms;
			};
			$rootScope.findFormsArray = $scope.findFormsArray;
			/**
			 * Search all form controller in scope<br>
			 * TODO This method required jquery.utils.js with Object prototype [getName]
			 * to get object constructor function name
			 *
			 * @reurn form controllers object with key: form  (in scope), value: form controller
			 */
			$scope.findForms = function() {
				var FUNC_NAME = /function (.{1,})\(/;
				var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
				var forms = {};
				// detect by referenceFormController directive
				if (this.hasOwnProperty('$$FormControllers')
						&& angular.isArray(this['$$FormControllers'])
						&& this['$$FormControllers'].length) {
					$.map(this['$$FormControllers'], function(f) {
						forms[f.$name] = f;
					});
				}
				// detect by scope property
				$.each(this, function(k, v) {
					var checked = false;
					if (angular.isFunction(v)) {
						var fnStr = v.toString().replace(STRIP_COMMENTS, '');
						var results = (FUNC_NAME).exec(fnStr);
						if (angular.isArray(results) && results.length > 1
								&& results[1] === 'FormController') {
							forms[k] = v;
							checked = true;
						}
					}
					if (!checked && v && v.hasOwnProperty('$setSubmitted')
							&& angular.isFunction(v['$setSubmitted'])) {
						forms[k] = v;
					}
				});
				return forms;
			};
			$rootScope.findForms = $scope.findForms;
			/**
			 * Search all field controllers of all forms in scope<br>
			 * TODO This method required jquery.utils.js with Object prototype [getName]
			 * to get object constructor function name
			 *
			 * @reurn field controllers array
			 */
			$scope.findFieldsArray = function() {
				var FUNC_NAME = /function (.{1,})\(/;
				var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
				var fields = [];
				// detect by referenceNgModelController directive
				if (this.hasOwnProperty('$NgModelControlers')
						&& angular.isArray(this['$NgModelControlers'])
						&& this['$NgModelControlers'].length) {
					$.map(this['$NgModelControlers'], function(f) {
						fields.push(f);
					});
				}
				// detect by scope property
				var forms = this.findFormsArray();
				if (forms != null && forms.length) {
					$.map(forms, function(f) {
						$.each(f, function(k, v) {
							var checked = false;
							if (angular.isFunction(v)) {
								var fnStr = v.toString().replace(STRIP_COMMENTS, '');
								var results = (FUNC_NAME).exec(fnStr);
								if (angular.isArray(results) && results.length > 1
										&& results[1] === 'NgModelController') {
									fields.push(v);
									checked = true;
								}
							}
							if (!checked && v && v.hasOwnProperty('$modelView')) {
								fields.push(v);
							}
						});
					});
				}
				return fields;
			};
			$rootScope.findFieldsArray = $scope.findFieldsArray;
			/**
			 * Search all field controllers of all forms in scope<br>
			 * TODO This method required jquery.utils.js with Object prototype [getName]
			 * to get object constructor function name
			 *
			 * @reurn Object with:<br>
			 * {
			 * 		&lt;form_name_1&gt;: {
			 * 			&lt;field_name_1&gt;: field controller 1
			 * 			, &lt;field_name_2&gt;: field controller 2
			 * 		}
			 * 		, &lt;form_name_2&gt;: {
			 * 			&lt;field_name_1&gt;: field controller 1
			 * 			, &lt;field_name_2&gt;: field controller 2
			 * 		}
			 * }
			 */
			$scope.findFields = function() {
				var FUNC_NAME = /function (.{1,})\(/;
				var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
				var fields = {};
				// detect by referenceNgModelController directive
				if (this.hasOwnProperty('$NgModelControlers')
						&& angular.isArray(this['$NgModelControlers'])
						&& this['$NgModelControlers'].length) {
					$.map(this['$NgModelControlers'], function(f) {
						var formName = '-';
						if (f.$$parentForm && f.$$parentForm.$name) {
							formName = f.$$parentForm.$name;
						}
						if (!fields.hasOwnProperty(formName)) {
							fields[formName] = {};
						}
						fields[formName][f.$name] = f;
					});
				}
				// detect by scope property
				var forms = this.findForms();
				if (forms != null && Object.keys(forms).length) {
					$.each(forms, function(fk, fv) {
						fields[fk] = {};
						$.each(fv, function(k, v) {
							var checked = false;
							if (angular.isFunction(v)) {
								var fnStr = v.toString().replace(STRIP_COMMENTS, '');
								var results = (FUNC_NAME).exec(fnStr);
								if (angular.isArray(results) && results.length > 1
										&& results[1] === 'NgModelController') {
									fields[fk][k] = v;
									checked = true;
								}
							}
							if (!checked && v && v.hasOwnProperty('$modelView')) {
								fields[fk][k] = v;
							}
						});
					});
				}
				return fields;
			};
			$rootScope.findFields = $scope.findFields;
			/**
			 * Search field controller by form/field names in scope<br>
			 * TODO This method required jquery.utils.js with Object prototype [getName]
			 * to get object constructor function name
			 *
			 * @reurn field controller or null if failed
			 */
			$scope.findField = function(formName, fieldName) {
				var fields = this.findFields();
				var field = null;
				if (fields != null && fields.hasOwnProperty(formName)
						&& fields[formName] && fields[formName].hasOwnProperty(fieldName)) {
					formName = (formName && formName.length ? formName : '-');
					field = fields[formName][fieldName];
				}
				return field;
			};
			$rootScope.findField = $scope.findField;

		    // -------------------------------------------------
		    // SUPPORT FUNCTIONS
		    // -------------------------------------------------
			$scope.parseNumber = function(value) {
				if (value == 0) return value;
				var grpsep = '\\' + $locale.NUMBER_FORMATS.GROUP_SEP;
				var grpregex = new RegExp(grpsep, 'g');
				value = ((!value || isNaN(String(value).replace( grpregex , ""))) ? null : String(value).replace( grpregex , ""));
				return parseFloat(value, 10);
			};
			$scope.formatDate = function(value, fmt) {
				return (value && value instanceof Date ? $filter('date')(value, fmt) : null);
			};
			$scope.debug = function() {
				($log && typeof $log.debug === 'function')
				&& $log.debug(arguments);
			};
			$scope.serializeObject = function(serializeObj) {
			    var obj = {};
			    var fields = $(serializeObj).serializeArray();
			    $.each(fields, function() {
					if (this.name && this.name.length > 0) {
						var objVal = null;
						if (obj.hasOwnProperty(this.name)) {
							objVal = obj[this.name];
							if (!$.isArray(objVal)) objVal = [objVal];
							if ($.isArray(this.value)) {
								$.each(this.value, function() {
									objVal.push(this);
								});
							}
							else objVal.push(this.value);
						}
						else objVal = this.value;
						obj[this.name] = objVal;
					}
					else if (Object.keys(this).length > 0) {
						$.each(this, function(key, value) {
							var objVal = null;
							if (obj.hasOwnProperty(key)) {
								objVal = obj[key];
								if (!$.isArray(objVal)) objVal = [objVal];
								if ($.isArray(value)) {
									$.each(value, function() {
										objVal.push(this);
									});
								}
								else objVal.push(value);
							}
							else objVal = value;
							obj[key] = objVal;
						});
					}
				});
			    return obj;
			};
			$rootScope.serializeObject = $scope.serializeObject;

		    // -------------------------------------------------
		    // Popovers, tooltips
		    // -------------------------------------------------
			/**
			 * Close all popovers
			 *
			 * @param e event
			 */
			$rootScope.closeAllPopovers = function(e) {
				var popovers = angular.element('[data-toggle="popover"]');
				if ((popovers || []).length) {
					popovers.each(function () {
						if (e) {
							if (!angular.element(this).is(e.target)
									&& angular.element(this).has(e.target).length === 0) {
								angular.element(this).popover('hide');
							}
						} else {
							// No event passed - closing all popovers programmatically
							angular.element(this).popover('hide');
						}
				    });
				}
				//	popovers = angular.element('.popover');
				//	if ((popovers || []).length) popovers.remove();
			};
			/**
			 * Close all tooltips
			 *
			 * @param e event
			 */
			$rootScope.closeAllTooltips = function(e) {
				var tooltips = angular.element('[data-toggle="tooltip"]');
				if ((tooltips || []).length) {
					tooltips.each(function () {
						if (e) {
							if (!angular.element(this).is(e.target)
									&& angular.element(this).has(e.target).length === 0) {
								angular.element(this).tooltip('hide');
							}
						} else {
							// No event passed - closing all tooltips programmatically
							angular.element(this).tooltip('hide');
						}
				    });
				}
				//	tooltips = angular.element('.tooltip');
				//	if ((tooltips || []).length) tooltips.remove();
			};
			/**
			 * Close all popovers and tooltips
			 *
			 * @param e event
			 */
			$rootScope.closeAllTooltipPopovers = function(e) {
				// popovers
				$rootScope.closeAllPopovers(e)
				// tooltips
				$rootScope.closeAllTooltips(e);
			};
			$scope.closeAllPopovers = $rootScope.closeAllPopovers;
			$scope.closeAllTooltips = $rootScope.closeAllTooltips;
			$scope.closeAllTooltipPopovers = $rootScope.closeAllTooltipPopovers;
			/**
			 * Calculate tooltip placement
			 *
			 * @param $document DOM document element
			 * @param $el element to show tooltip
			 * @param $tip tooltip component element
			 */
			$rootScope._internalTooltipPlacement = function($document, $el, $tip) {
				var docEl = angular.element($document),
		        ttEl = angular.element($el),
		        tipEl = angular.element($tip),
		        offset = ttEl.offset(),
		        ttElWidth = ttEl.outerWidth(),
		        ttElHeight = ttEl.outerHeight(),
		        tipElWidth = tipEl.outerWidth(),
		        tipElHeight = tipEl.outerHeight(),
		        height = docEl.outerHeight() - tipElHeight,
		        width = docEl.outerWidth() - tipElWidth,
		        vert = height - offset.top - ttElHeight,
		        vertPlacement = vert > 0 ? 'bottom' : 'top',
		        horiz = width - offset.left - ttElWidth,
		        horizPlacement = horiz > 0 ? 'right' : 'left',
		        placement = (Math.abs(horiz) > Math.abs(vert)
		        		?  horizPlacement : vertPlacement);
		        ($log && typeof $log.debug === 'function')
		        && $log.debug('Element: [', offset.top, offset.left, ttElWidth, ttElHeight, ']',
		        		' - Tip: [', tipElWidth, tipElHeight, ']',
		        		' - Document: [', width, height, ']',
		        		' - Placement: ', placement);
		        return placement;
			};
			/**
			 * Apply tooltip for all elements automatically
			 *
			 * @param el elements array to apply tooltip. NULL/empty for all elements
			 */
			$rootScope.autoTooltip = function(el) {
				try {
					var elements = (el && el.length ? el
							: (el ? angular.element(el) : angular.element('*')));
					$.each(elements, function() {
						var $this = angular.element(this);
						var uiSelect = $this.closest('.ui-select-dropdown,.ui-select-container');
						var tagName = ($this.prop('tagName') || '').toLowerCase();
						var elType = ($this.attr('type') || '').toLowerCase();
						// exclude ui-select/href/button
						if ((!uiSelect || uiSelect.length <= 0)
								&& !$this.hasClass('btn')
								&& (tagName !== 'select')
								&& (tagName !== 'a')
								&& (tagName !== 'button')) {
							var orgTtl = $this.attr('data-original-title') || '';
							var title = ($this.attr('title')
									|| $this.attr('data-title')
									|| $this.attr('data-original-title')
									|| $this.attr('alt') // image
									|| '');
							var dataTt = ($this.attr('tooltip')
									|| $this.attr('data-tooltip')
									|| '');
							var dataTgl = ($this.attr('data-toggle') || '');
							var tooltipped = (dataTgl && dataTgl === 'tooltip');
							if (!tooltipped && title.length && !dataTt && dataTgl !== 'tooltip') {
								($log && typeof $log.debug === 'function')
								&& $log.debug('apply tooltip:', $this);
								// hide first
								$this.tooltip('hide');
								if (!dataTgl) $this.attr('data-toggle', 'tooltip');
								(!orgTtl || orgTtl.length <= 0)
								&& $this.attr('data-original-title', title);
								$this.tooltip({
									container: 'body',
									html: true,
									placement: function(tip, el) {
								        return $rootScope._internalTooltipPlacement(
								        		$document, el, tip);
									},
									trigger: 'mouseenter,hover,click,focus'
								});
								tooltipped = true;

								// if this element had been tooltip
							} else if ($this.tooltip) {
								// hide first
								try { $this.tooltip('hide'); }
								catch (e) {
									($log && typeof $log.warn === 'function')
									&& $log.warn(e);
								}
							}
							// turns off another tooltips before showing
							if (tooltipped) {
								$this.on('show.bs.tooltip',
										function () {
											$('[data-toggle="tooltip"]').each(
													function () {
														if (!$(this).is($this)
																&& $(this).has($this).length === 0) {
															$(this).tooltip('hide');
														}
												    });
											// re-calculate tooltip placement on showing
											var $tooltip = $(this).data('bs.tooltip');
											var placement = $rootScope._internalTooltipPlacement(
									        		$document, $this, $tooltip.$tip);
									        $tooltip.options.placement = placement;
										});
							}
						} else {
							$this.attr('title', '');
							$this.attr('data-title', '');
							$this.attr('data-original-title', '');
							$this.attr('alt', '');
						}
					});
				} catch(e) {
					($log && typeof $log.warn === 'function')
					&& $log.warn(e);
				}
			};
			$scope.autoTooltip = $rootScope.autoTooltip;
			// load apply tooltip
			$rootScope._internalApplyTooltips = function() {
				// apply tooltip for current elements
				var timeout = $timeout(function() {
					$rootScope.autoTooltip(null);
					if (timeout) {
						$timeout.cancel(timeout);
						timeout = null;
					}
				}, 200);
			};
			$rootScope.$on('$load', $rootScope._internalApplyTooltips);
			// FIXME Bad performance in watching body DOM (HTML) changed
			//	// listen changed DOM event
			//	$rootScope.$on('$changedDOM', $rootScope._internalAPplyTooltips);
			// click to close all tooltips and popovers
			$document.on('click', $scope.closeAllTooltipPopovers);
			// scroll to close all tooltips and popovers
			$scope.$on("$scroll", function(e) {
				$scope.closeAllTooltipPopovers;
			});
			$scope.$on("$destroy", function() {
				$document.off("click", $scope.closeAllTooltipPopovers);
			});

		    // -------------------------------------------------
		    // AJAX Routing
		    // -------------------------------------------------
			// ajax
			$scope.post = function(options) {
				ajaxService.post(options);
			};
			$scope.jpost = function(options) {
				ajaxService.jpost(options);
			};

			// -------------------------------------------------
		    // NOTIFICATION Routing
		    // -------------------------------------------------
			// notification
			$scope.notice = function(options) {
				toaster.show(options);
			};
			$scope.success = function(options) {
				options = $.extend(options, { type: 1 });
				$scope.notice(options);
			};
			$scope.info = function(options) {
				options = $.extend(options, { type: 2 });
				$scope.notice(options);
			};
			$scope.warning = function(options) {
				options = $.extend(options, { type: 3 });
				$scope.notice(options);
			};
			$scope.warn = function(options) {
				$scope.warning(options);
			};
			$scope.error = function(options) {
				options = $.extend(options, { type: 4 });
				$scope.notice(options);
			};
			$scope.errorInternet = function() {
				if (parseErrorInternetMessage) {
					var msg = parseErrorInternetMessage();
					if (msg && msg.length > 0) {
						$scope.error({ body: msg });
					}
				}
			};
			$scope.wait = function(options) {
				options = $.extend(options, { type: 5 });
				$scope.notice(options);
			};
			/**
			 * Confirmation dialog
			 *
			 * opts: {
			 * 		title: &lt;title&gt;
			 * 		ok: &lt;ok button caption&gt;
			 * 		cancel: &lt;cancel button caption&gt;
			 * 		text: &lt;confirmation message&gt;
			 * }
			 * callback: callback function while yes
			 */
			$scope.confirm = function(opts, callback) {
				opts = opts || {};
				opts.title = $window.document.title;
				$confirm(opts).then(callback);
			};

		    // -------------------------------------------------
		    // MODAL DIALOGUGE
		    // -------------------------------------------------
			/**
			 * Destroy modal dialogure
			 *
			 * @param m modal instance (to access element via m.element) (it's a bootstrap element)
			 */
			$scope.destroyModal = function(m) {
				// close and destroy modal dialogure
				m && m.element && m.element.modal && m.element.modal('hide');
				// check if modal-open existed in body element
				var body = angular.element('body');
				if (body && body.length && body.hasClass('modal-open')) {
					body.removeClass('modal-open');
				}
				// backdrop
				angular.element('.modal-backdrop')
				&& angular.element('.modal-backdrop').remove();
				m && m.element && m.element.remove();
				// release all
				m && m.scope && m.scope.close && m.scope.close(null, 200);
			};
			/**
			 * Show modal dialogue
			 *
			 * @param template modal template
			 * @param scope scope data as parameter
			 * @param controller controller name as parameter
			 * @param inputs A set of values to pass as inputs to the controller.<br>
			 * Each value provided is injected into the controller constructor.
			 * @param appendElement The custom angular element to append the modal to instead of default body element.
			 * @param callback function to callback while buttons or href has been clicked on modal<br>
			 * function with parameters (function(b,e,m) {}): b,e,m:<br>
			 * 		+ b button element or link (as jQuery element)<br>
			 * 		+ e click event<br>
			 * 		+ m modal instance (to access element via m.element) (it's a bootstrap element)
			 * @param showCallback function to callback while showing modal<br>
			 * function with parameters (function(m) {}): m:<br>
			 * 		+ m modal instance (to access element via m.element) (it's a bootstrap element)
			 */
			$scope.showModal = function(template, scope, controller, inputs, appendElement, callback, showCallback) {
				ModalService.showModal({
					templateUrl: template,
		            controller: controller,
		            scope: scope || $scope || $rootScope,
		            inputs: inputs || {},
		            appendElement: appendElement || false
				}).then(function(modal) {
					// detect modal buttons
					var buttons = modal.element.find('button')
					if (buttons.length && typeof callback === 'function') {
						$.map(buttons, function(b) {
							var btn = angular.element(b);
							btn.on('click', function(e) {
								callback(angular.element(this), e, modal);
							});
						});
					}
					// detect modal buttons
					var links = modal.element.find('a');
					if (links.length && typeof callback === 'function') {
						$.map(links, function(link) {
							var lnk = angular.element(link);
							if (!lnk.attr('href') || lnk.attr('href').length <= 0
									|| lnk.attr('href') == '#'
										|| lnk.attr('href') == 'javascript:void(0)'
											|| lnk.attr('href') == 'void(0)') {
								lnk.on('click', function(e) {
									callback(angular.element(this), e, modal);
								});
							}
						});
					}
					// handle showing event
					if (typeof showCallback === 'function') {
						modal.element.on('shown.bs.modal', function() {
							showCallback(modal);
						});
					}
		            // it's a bootstrap element, use 'modal' to show it
		            modal.element.modal();
				});
			};

			// -------------------------------------------------
		    // FORWARD Routing
		    // -------------------------------------------------
			// forward
			$scope.go = function(url) {
				// parse base context path
				var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
				($log && typeof $log.info === 'function')
				&& $log.info('[BASE CONTEXT]: ' + basepath);
				$window.location.href = basepath + url;
			};
			/**
			 * Call URL using AJAX, and show result page into the specified element identity
			 *
			 * @param url URL
			 * @param data POST data
			 * @param elem DOM element identity to assign result (using innerHTML)
			 */
			$scope.goAjax = function(url, data, elem) {
				if ($(elem).length) {
					var token = parseTokenHeader(null, csrfService);
	            	var tokenparamname = parseTokenParameterName();
	            	data = angular.fromJson(data || {});
	            	data = data || {};
	            	data[tokenparamname] = token;
					ajaxService.post({
		        		url: url,
		        		data: data,
				        success: function(data) {
				        	$(elem).html($compile(data)($scope));
				        }
					});
				}
			}

			//			// -------------------------------------------------
			//		    // BIND SCROLL FOR PAGE FOOTER
			//		    // -------------------------------------------------
			//			// bind window scroll
			//			$(".pageTop").hide();
			//			angular.element('.pageTop').bind(
			//					"click",
			//					function(e) {
			//						$('.page-content')[0].scrollTop = 0;
			//						return false;
			//					});
			//			angular.element('.page-content').bind(
			//					"scroll",
			//					function(e) {
			//						// re-position footer
			//						var footer = $('footer');
			//						if (footer.length > 0) {
			//							var top = ($('body').scrollTop() + $('body').height() - footer.outerHeight());
			//							footer.css('top', top + 'px');
			//						}
			//
			//						// parse content position
			//						if ($(this).scrollTop() > 5) {
			//							$(".pageTop", footer).fadeIn();
			//						} else {
			//							$(".pageTop", footer).fadeOut();
			//						}
			//					});
			//			// bind window resize
			//			angular.element('.page-content').bind(
			//					"resize",
			//					function(e) {
			//						var footer = $('footer');
			//						if (footer.length > 0) {
			//							var top = ($('body').scrollTop() + $('body').height() - footer.outerHeight());
			//							footer.css('top', top + 'px');
			//						}
			//					});

			// -------------------------------------------------
			// SIDEBAR
			// -------------------------------------------------
			$scope.sidebar = [];
			$scope.getSidebar = function() {
				// require sidebar menu
				ajaxService.jpost({
		        		url: '/dashboard/sidebar',
		        		statusCode: {
				        	200: function(data) {
			        			var json = angular.fromJson(data || {});
			        			if (json) {
			        				$scope.sidebar = json;
			        			}
				        	}
				        }
		        });
			};

			// -------------------------------------------------
		    // CALCULATE PAGE CONTENT
		    // -------------------------------------------------
			//			// bind window resize
			//			angular.element($window).bind(
			//					"resize",
			//					function(e) {
			//						var pageHdr = $('.page-header');
			//						if (!pageHdr.length) pageHdr = $('header');
			//						var pageHdrHeight = pageHdr.outerHeight();
			//						var pageFtr = $('.page-footer');
			//						if (!pageFtr.length) pageFtr = $('footer');
			//						var pageFtrHeight = pageFtr.outerHeight();
			//						var pageCnt = $('.page-content');
			//						if (!pageCnt.length) pageFtr = $('content');
			//						var winHeight = $(this).height();
			//						var pageCntHeight = (winHeight - pageHdrHeight - pageFtrHeight);
			//						pageCnt.css('height', pageCntHeight);
			//						pageCnt.css('max-height', pageCntHeight);
			//						pageCnt.css('min-height', pageCntHeight);
			//						pageCnt.css('overflow', 'auto');
			//					});
			//			angular.element($window).resize();

			// -------------------------------------------------
			// FORM Routing
			// -------------------------------------------------
			// 非表示のフォームを作成します
			$scope.attachForm = function(url, method, enctype, formSelector, fields, clone) {
				var form = $(formSelector);
				clone = eval(clone || false) || false;
				if (clone && form && form.length > 0) {
					var cloneForm = form.clone();
					cloneForm.css('visibility', 'hidden');
					cloneForm.attr('id', 'CLN-' + (new Date()).getTime());
					cloneForm.attr('name', 'CLN-' + (new Date()).getTime());
					$.each(form.serializeArray(), function() {
						var clnFields = $('[name=' + this.name + '],[id=' + this.name + ']', cloneForm);
						if (clnFields.length > 0) {
							if ($.isArray(this.value)) {
								$.each(this.value, function(i) {
									$(clnFields.get(i)).val(this);
								});
							}
							else {
								clnFields.val(this.value);
							}
						}
					});
				}
				url = pageInfo && typeof pageInfo.parseUrl === 'function'
					&& url && url.length > 0 ? pageInfo.parseUrl(url || '') : url || '';
				method = method || 'POST';
				enctype = enctype || 'application/x-www-form-urlencoded';
				if (form && form.length > 0) {
					form.attr('method', method);
					// restrict changing form action
					if (url && url.length > 0
							&& form.attr('action').toLowerCase() != url.toLowerCase()
							&& form.attr('action').toLowerCase() != url.toLowerCase() + '/') {
						form.attr('action', url);
					}
					form.attr('enctype', enctype);

					// parse form fields
					var arrFields = {};
					if (!$.isArray(fields) && Object.keys(fields).length > 0) {
						arrFields = $.extend({}, fields);
					}
					else if ($.isArray(fields)) {
						$.each(fields, function() {
							if (this.name && this.name.length > 0) {
								var values = (arrFields.hasOwnProperty(this.name) ? arrFields[this.name] || [] : []);
								if ($.isArray(this.value)) {
									$.each(this.value, function() {
										values.push(this);
									});
								}
								else values.push(this.value);
								arrFields[this.name] = values;
							}
							else if (Object.keys(this).length > 0) {
								$.each(this, function(key, value) {
									var values = (arrFields.hasOwnProperty(key) ? arrFields[key] || [] : []);
									if ($.isArray(value)) {
										$.each(value, function() {
											values.push(this);
										});
									}
									else values.push(value);
									arrFields[key] = values;
								});
							}
						});
					}

					// attach form fields
					$.each(arrFields, function(key, value) {
						if (key !== '$$hashKey') {
							if (!angular.isArray(value) && value != null && value != undefined) {
								// just object, not date
								if (angular.isObject(value) && !$.isDate(value)) {
									$.each(value, function(k, v) {
										if (k !== '$$hashKey' && v != null && v != undefined) {
											var hdField = $('<input type=\'hidden\' name=\'' + key + '.' + k + '\'>');
											hdField.appendTo(form);
											if ($.isDate(v)) {
												if (typeof value.toISOString === 'function') {
													hdField.val(v.toISOString());

												} else {
													hdField.val(v.getTime());
												}

											} else {
												hdField.val(v);
											}
										}
									});

									// date object
								} else if ($.isDate(value)) {
									var hdField = $('<input type=\'hidden\' name=\'' + key + '\'>');
									hdField.appendTo(form);
									if (typeof value.toISOString === 'function') {
										hdField.val(value.toISOString());
									} else {
										hdField.val(value.getTime());
									}

									// simple value
								} else {
									var hdField = $('<input type=\'hidden\' name=\'' + key + '\'>');
									hdField.appendTo(form);
									hdField.val(value);
								}

							} else if (angular.isArray(value) && value.length > 0) {
								$.each(value, function(i) {
									var val = this;
									// just object, not date
									if (angular.isObject(val) && !$.isDate(val)) {
										$.each(val, function(k, v) {
											if (k !== '$$hashKey' && v != null && v != undefined) {
												var hdField = $('<input type=\'hidden\' name=\'' + key + '[' + i + '].' + k + '\'>');
												hdField.appendTo(form);
												if ($.isDate(v)) {
													if (typeof val.toISOString === 'function') {
														hdField.val(v.toISOString());

													} else {
														hdField.val(v.getTime());
													}

												} else {
													hdField.val(v);
												}
											}
										});

										// date object
									} else if ($.isDate(val)) {
										var hdField = $('<input type=\'hidden\' name=\'' + key + '[' + i + ']\'>');
										hdField.appendTo(form);
										if (typeof val.toISOString === 'function') {
											hdField.val(val.toISOString());
										} else {
											hdField.val(val.getTime());
										}

										// simple value
									} else {
										var hdField = $('<input type=\'hidden\' name=\'' + key + '[' + i + ']\'>');
										hdField.appendTo(form);
										hdField.val(val);
									}
								});
							}
						}
					});

					// apply hidden token field
					var token = parseTokenHeader(null, csrfService);
	            	var tokenparamname = parseTokenParameterName();
					if (form.find('[name="' + tokenparamname + '"]').length <= 0) {
						var hdField = $('<input type="hidden" name="' + tokenparamname + '">');
						hdField.appendTo(form);
						hdField.val(token);
					}
					else {
						var tokenEl = form.find('[name="' + tokenparamname + '"]');
						var tokenElVal = (tokenEl && tokenEl.length > 0 ? tokenEl.val() : '');
						if (!tokenElVal.length || tokenElVal != token) tokenElVal = token;
						tokenEl.val(tokenElVal);
					}
				}
				return form;
			};
			$scope.createHiddenForm = function(url, method, fields) {
				var form = $('<form action="' + url
						+ '" method="' + method
						+ '" enctype="application/x-www-form-urlencoded"></form>');
				form.appendTo('body');
				form = $scope.attachForm(url, method, false, form, fields, false);
				return form;
			};
			$scope.submitHiddenForm = function(url, method, fields) {
				var form = $scope.createHiddenForm(url, method, fields);
				if (form && form.length > 0) form.submit();
				return false;
			};
			$scope.postHiddenForm = function(url, fields) {
				return $scope.submitHiddenForm(url, 'POST', fields);
			};
			$scope.submitForm = function(url, method, formSelector, fields) {
				var form = $scope.attachForm(url, method, false, formSelector, fields, false);
				if (form && form.length > 0) form.submit();
				return false;
			};
			$scope.postForm = function(url, formSelector, fields) {
				return $scope.submitForm(url, 'POST', formSelector, fields);
			};

			// -------------------------------------------------
		    // SUPPORT APPLYING VALIDATION STYLE FOR ELEMENTS
		    // -------------------------------------------------
			/**
			 * Apply elements validation style
			 *
			 * @param el element (by angular.element)
			 * @param elCtrl element field controller
			 * @param valid true for valid
			 * @param error error code (such as: required, etc.)
			 */
			$rootScope.setElValidality = function(el, elCtrl, valid, error) {
				if (!valid && el.length) {
					el.hasClass('ng-valid') && el.removeClass('ng-valid');
					el.hasClass('ng-valid-' + error) && el.removeClass('ng-valid-' + error);
					!el.hasClass('ng-invalid') && el.addClass('ng-invalid');
					!el.hasClass('ng-invalid-' + error) && el.addClass('ng-invalid-' + error);
					if (!$.isUndefined(elCtrl)) {
						if ($.isUndefined(elCtrl.$error)) {
							elCtrl.$error = {};
						}
						elCtrl.$error[error] = true;
					}
				} else {
					!el.hasClass('ng-valid') && el.addClass('ng-valid');
					!el.hasClass('ng-valid-' + error) && el.addClass('ng-valid-' + error);
					el.hasClass('ng-invalid') && el.removeClass('ng-invalid');
					el.hasClass('ng-invalid-' + error) && el.removeClass('ng-invalid-' + error);
					if (!$.isUndefined(elCtrl)
							&& !$.isUndefined(elCtrl.$error)
							&& elCtrl.$error.hasOwnProperty(error)) {
						delete elCtrl.$error[error];
					}
				}
			};
			$scope.setElValidality = $rootScope.setElValidality;

			// -------------------------------------------------
			// ADJUST TABLE COLUMN ACTION WIDTH
			// -------------------------------------------------
			/**
			 * Adjust table action column while table body showing content
			 * Apply for ngToggleShow directive
			 */
			$scope.adjustActionColumn = function(show) {
				if (show == true) {
					// resize actions column
    				var actHdrCol = angular.element('table.table thead tr th[type="action"]');
    				var actBdyCol = angular.element('table.table tbody tr td[type="action"]');
    				var btns = angular.element('table.table tbody tr:first td[type="action"] .btn');
    				if (actHdrCol && actHdrCol.length
    						&& actBdyCol && actBdyCol.length
    						&& btns && btns.length) {
    					var btnsWidth = 0;
    					angular.forEach(btns, function(btn, idx) {
    						btnsWidth += (angular.element(btn).outerWidth(true) + 5);
    					});
    					actHdrCol.css('min-width', btnsWidth + 'px');
    					actHdrCol.css('max-width', btnsWidth + 'px');
    					actHdrCol.css('width', btnsWidth + 'px');
    					actBdyCol.css('min-width', btnsWidth + 'px');
    					actBdyCol.css('max-width', btnsWidth + 'px');
    					actBdyCol.css('width', btnsWidth + 'px');
    				}
				}
			};

			// -------------------------------------------------
			// CONTEXT-MENU
			// -------------------------------------------------
			/**
			 * TODO Children scopes maybe override this method for adding more options into main menu
			 *
			 * @param data data item of menu context
			 * @param viewable page viewable permission
			 * @param insertable page viewable permission
			 * @param updatable page viewable permission
			 * @param deletable page viewable permission
			 */
			$scope.moreMenuOptions = function(data, viewable, insertable, updatable, deletable) { return null; };
			$scope.menuOptions = function(data, viewable, insertable, updatable, deletable) {
				if (!data || (!viewable && !insertable && !updatable && !deletable)) return [];
				var moreOpts = (typeof $scope.moreMenuOptions === 'function'
					? $scope.moreMenuOptions(data, viewable, insertable, updatable, deletable) : null);
				moreOpts = moreOpts || [];
				if (moreOpts && !angular.isArray(moreOpts)) moreOpts = [ moreOpts ];
				var rowCtxMnu = angular.element('tr[data-context-menu]');
				var viewCtxMnu = (rowCtxMnu.attr('data-action-view') || '');
				var updateCtxMnu = (rowCtxMnu.attr('data-action-update') || '');
				var deleteCtxMnu = (rowCtxMnu.attr('data-action-delete') || '');
				var deleteConfirmCtxMnu = (rowCtxMnu.attr('data-delete-confirm') || '');
				var deleteConfirmTitleCtxMnu = (rowCtxMnu.attr('data-delete-confirm-title') || '');
				var deleteConfirmOkCtxMnu = (rowCtxMnu.attr('data-delete-confirm-ok') || '');
				var deleteConfirmCancelCtxMnu = (rowCtxMnu.attr('data-delete-confirm-cancel') || '');
				var mainOpts = [
				        [function ($itemScope) {
				        	return viewCtxMnu;
				        },
				        function ($itemScope) {
			        		if (viewable) {
			        			// fire event from context-menu to child scope
			        			$scope.$broadcast('$clickViewMenu', { data: data });
			        		}
				        	return false;
				        },
				        function ($itemScope) {
				        	return (viewCtxMnu.length && viewable);	// enable menu
				        },
				        function ($itemScope) {
				        	return 'view';	// menu class
				        },
				        function ($itemScope) {
				        	return (viewCtxMnu.length && viewable);	// visible menu
				        }],
				        [function ($itemScope) {
				        	return updateCtxMnu;
				        },
				        function ($itemScope) {
			        		if (insertable || updatable) {
			        			// fire event from context-menu to child scope
			        			$scope.$broadcast('$clickUpdateMenu', { data: data });
			        		}
				        	return false;
				        },
				        function ($itemScope) {
				        	return (updateCtxMnu.length && (insertable || updatable));	// enable menu
				        },
				        function ($itemScope) {
				        	return 'edit';	// menu class
				        },
				        function ($itemScope) {
				        	return (updateCtxMnu.length && (insertable || updatable));	// visible menu
				        }],
				        [function ($itemScope) {
				        	return deleteCtxMnu;
				        },
				        function ($itemScope) {
				        	if (insertable || deletable) {
				        		if (deleteConfirmCtxMnu.length > 0) {
					    			$scope.confirm(
					    					{
					    						title: deleteConfirmTitleCtxMnu
					    						, ok: deleteConfirmOkCtxMnu
					    						, cancel: deleteConfirmCancelCtxMnu
					    						, text: deleteConfirmCtxMnu
					    					}
					    					, function() {
					    						// fire event from context-menu to child scope
							        			$scope.$broadcast('$clickDeleteMenu', { data: data });
							        		});
				        		} else {
				        			($log && typeof $log.warn === 'function')
				        			&& $log.warn('Not found confirmation message to show deletion confirmation dialogure!!!');
				        			// fire event from context-menu to child scope
				        			$scope.$broadcast('$clickDeleteMenu', { data: data });
				        		}
				        	}
				        	return false;
				        },
				        function ($itemScope) {
				        	return (deleteCtxMnu.length && (insertable || deletable));	// enable menu
				        },
				        function ($itemScope) {
				        	return 'delete';	// menu class
				        },
				        function ($itemScope) {
				        	return (deleteCtxMnu.length && (insertable || deletable));	// visible menu
				        }]
				];
				// append more options at first and separator if necessary
				if (moreOpts.length) {
					moreOpts.push(null);
					mainOpts = moreOpts.concat(mainOpts);
				}
				return mainOpts;
			};

			// -------------------------------------------------
			// AUTOCOMPLETE
			// -------------------------------------------------
			$rootScope.$on('$load', function(e) {
				var timeout = $timeout(function() {
					// form
					var components = angular.element('form, input, textarea');
					components.attr('autocomplete', 'off');
					components.attr('autocorrect', 'off');
					components.attr('autocapitalize', 'off');
					components.attr('spellcheck', 'false');
					if (timeout) {
						$timeout.cancel(timeout);
						timeout = null;
					}
				}, 200);
			});

			// -------------------------------------------------
			// NOTIFICATION Routing
			// -------------------------------------------------
			idleService.start();

			// -------------------------------------------------
			// Body loading effects
			// -------------------------------------------------
			//			$(".animsition").animsition({
			//				inClass: 'flip-in-x',
			//				outClass: 'flip-out-x',
			//				inDuration: 1500,
			//				outDuration: 800,
			//				linkElement: '.animsition-link',
			//				// e.g. linkElement: 'a:not([target="_blank"]):not([href^=#])'
			//				loading: true,
			//				loadingParentElement: 'body', //animsition wrapper element
			//				loadingClass: 'animsition-loading',
			//				loadingInner: '', // e.g '<img src="loading.svg" />'
			//				timeout: false,
			//				timeoutCountdown: 5000,
			//				onLoadEvent: true,
			//				browser: [ 'animation-duration', '-webkit-animation-duration'],
			//				// "browser" option allows you to disable the "animsition" in case the css property in the array is not supported by your browser.
			//				// The default setting is to disable the "animsition" in a browser that does not support "animation-duration".
			//				overlay : false,
			//				overlayClass : 'animsition-overlay-slide',
			//				overlayParentElement : 'body',
			//				transition: function(url){ window.location.href = url; }
			//			});

			// -------------------------------------------------
		    // Body randomize classes
		    // -------------------------------------------------
			//			//Add random background class to selected element
			//        	var classes = [];
			//    		var maxSkins = (typeof MAX_DASHBOARD_SKINS_NUMBER === 'number'
			//    			&& MAX_DASHBOARD_SKINS_NUMBER > 0 ? MAX_DASHBOARD_SKINS_NUMBER : 10);
			//    		for(var i = 1; i <= maxSkins; i++) {
			//    			classes.push('skin-' + i);
			//    		}
			//        	$('body').addClass(classes[Math.floor(Math.random() * (classes.length - 1)) + 1]);
		}
);

// controller for date/time picker directive
importController(
		'nlh4jDatepickerController',
		function($scope, $element, $attrs, $filter) {
			 var format = $attrs.format || 'dd-MM-yyyy';
			 $scope.id = $scope.dateId;
			 $scope.name = $attrs.name;
			 $scope.today = $scope.formatDate(new Date(), format);
			 // datepicker 初期設定
			 $scope.initDatePicker = function() {
				 $('#' + $scope.id).datepicker({
					 dateFormat: format,
					 numberOfMonths: 1,
					 showCurrentAtPos: 0,
					 onSelect: function(select, target) {
						 // 日付が選択されたときの処理(buttonタイプの場合)
						 if ($scope.dateType === "button") {
							 $scope.$apply(
									 function() {
										 $scope.dateValue = select;
									 }
							 );
							 angular.element(this).closest('datepicker').hide();
							 angular.element(this).closest('datepicker').prev().show();
						 }
						 $scope.$apply(
								 function() {
									 $scope.updateDatepicker();
								 }
						 );
					 }
				 });
			 };

			 /* ++++++++++++++++++++++++++++++++++++++++
			  * フォーマットをチェック処理
			  * ++++++++++++++++++++++++++++++++++++++++
			  */
			 $scope.formatCheck = function() {
				 var check1 = $( '#' + $scope.dateId );
				 var check2 = $scope.dateValue;
				 var check1Filter = $scope.formatDate(new Date(check1.val()), format);
				 var check2Filter = $scope.formatDate(new Date(check2), format);
				 if (check1.val() !== check2 && check1Filter === check2Filter) {
					 $scope.dateValue = check1.val();
				 }
			 };

			 /* ++++++++++++++++++++++++++++++++++++++++
			  * Datepicker 更新
			  * ++++++++++++++++++++++++++++++++++++++++
			  */
			 $scope.updateDatepicker = function() {
				 // 最大値または最小値が空またはnullまたはundefinedであれば、defaultDateオプションをnullに設定する
				 if ($scope.dateMinValue === "" || $scope.dateMinValue === null || $scope.dateMinValue === undefined
						 || $scope.dateMaxValue === "" || $scope.dateMaxValue === null || $scope.dateMaxVaule === undefined) {
					 $('#' + $scope.id).datepicker('option', 'defaultDate', null);
				 }
				 // カレンダーの表示月変更処理(最大値)
				 $scope.changeMonthDisplay($scope.dateMaxValue, 'maxDate', 2);
				 // カレンダーの表示月変更処理(最小値)
				 $scope.changeMonthDisplay($scope.dateMinValue, 'minDate', 0);
				 // dateValueがnullでないかつundefinedでないかつ空でなければ、現在表示している月の表示順序を変更する
				 if ($scope.dateValue !== null && $scope.dateValue !== undefined && $('#' + $scope.id).val() !== "") {
					 $('#' + $scope.id).datepicker('option', 'showCurrentAtPos', 0);
				 }
				 $scope.dateValue = $('#' + $scope.dateId).val();
			 };

			 /* ++++++++++++++++++++++++++++++++++++++++
			  * カレンダーの表示月変更処理
			  * ++++++++++++++++++++++++++++++++++++++++
			  */
			 $scope.changeMonthDisplay = function(date, optionDate, showCurrentAtPos) {
				 var defaultDate = undefined;
				 var tmp = date;
				 // dateがnullでないかつundefinedでないならば、処理を行う
				 if (date !== null && date !== undefined) {
					 // 桁数チェック
					 if (date.length === 10) {
						 if (date == null || angular.isUndefined(date)) {
							 date = $scope.today;
						 }
						 defaultDate = $scope.formatDate(date, format);
						 var option = {
								 'showCurrentAtPos': showCurrentAtPos,
								 'defaultDate': defaultDate,
						 };
						 option[optionDate] = tmp;
						 $('#' + $scope.id).datepicker('option', option);
					 }
					 else {
						 $('#' + $scope.id).datepicker('option', optionDate, null);
						 $('#' + $scope.id).datepicker('option', 'showCurrentAtPos', 0);
					 }
				 }
			 };

			 /* ++++++++++++++++++++
			  * 日付送り処理
			  * ++++++++++++++++++++
			  */
			 $scope.moveDay = function(move) {
				 var date = '';
				 // 日付が選択されていない場合、操作当日をdateに設定する
				 if ($scope.dateValue == null || $scope.dateValue === '') {
					 $scope.dateValue = $scope.today;
				 }
				 // 日付の取得
				 // date = new Date($scope.dateValue);
				 date =  new Date( $scope.dateValue.replace( /(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3") );
				 formatCheck = $scope.formatDate(date, format);
				 if (angular.isDate(date) && date.toString() !== "Invalid Date" && angular.isNumber(move)) {
					 // 移動した日付を設定
					 $scope.dateValue = $scope.formatDate(date.setDate(date.getDate() + move), format);
					 // 日付範囲指定の変更
					 $scope.changeDayRange($scope.dateValue);
				 }
			 };
			 $scope.getToday = function() {
				 // 操作当日を設定
				 $scope.dateValue = $scope.today;
				 // 日付範囲指定の変更
				 $scope.changeDayRange($scope.dateValue);
			 };
			 /* ++++++++++++++++++++++++++++++++++++++++
			  * 操作当日の設定処理
			  * ++++++++++++++++++++++++++++++++++++++++
			  */
			 $scope.changeDayRange = function(newDate) {
				 if ($scope.dateMinValue != null && $scope.dateMinValue !== undefined && $scope.dateMinValue > newDate) {
					 $scope.dateMinValue = newDate;
				 }
				 if ($scope.dateMaxValue != null && $scope.dateMaxValue !== undefined && $scope.dateMaxValue < newDate) {
					 $scope.dateMaxValue = newDate;
				 }
			 };

			 /* ++++++++++++++++++++++++++++++++++++++++
			  * validation チェック
			  * ++++++++++++++++++++++++++++++++++++++++
			  */
			 $scope.dateValidationFunc = function() {
				 if ($scope.dateValue === null || $scope.dateValue === undefined || $scope.dateValue === "") {
					 $scope.invalid = true;
				 }
				 else {
					 $scope.invalid = false;
				 }
			 };
			 // datepicikerの個数分、配列に挿入する
			 if (angular.isArray($scope.dateValidation)) {
				 $scope.dateValidation.push($scope.dateValidationFunc);
			 }
		}
);
