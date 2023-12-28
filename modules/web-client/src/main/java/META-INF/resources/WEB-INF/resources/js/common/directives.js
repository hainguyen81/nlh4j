"'use strict';\n"

/**
 * Fix for IE select menus getting stuck when their underlying list changes.
 * Original code: http://kkurni.blogspot.com.au/2013/10/angularjs-ng-option-with-ie8.html
 *
 * Set the `ie-select-fix` attribute to the model expression that should trigger the list to re-render.
 *
 * @example <select ng-model="modelValue" ie-select-fix="itemList" ng-options="item.label for item in itemList">
 */
importDirective(
		'ieSelectFix',
        function($document) {
            return {
                restrict: 'A',
                require: 'ngModel',
                link: function(scope, el, attrs, ngModelCtrl) {
                    var isIE = $document[0] && $document[0].attachEvent;
                    if (!isIE) {
                    	/**
                    	 * detect IE
                    	 * returns version of IE or false, if browser is not Internet Explorer
                    	 */
                    	isIE = function detectIE() {
                    		var ua = window.navigator.userAgent;
                    		// Test values; Uncomment to check result …
                    		// IE 10
                    		// ua = 'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)';
                    		// IE 11
                    		// ua = 'Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko';
                    		// Edge 12 (Spartan)
                    		// ua = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0';
                    		// Edge 13
                    		// ua = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586';
                    		var msie = ua.indexOf('MSIE ');
                    		if (msie > 0) {
                    			// IE 10 or older => return version number
                    			return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
                    		}
                    		var trident = ua.indexOf('Trident/');
                    		if (trident > 0) {
                    			// IE 11 => return version number
                    			var rv = ua.indexOf('rv:');
                    			return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
                    		}
                    		var edge = ua.indexOf('Edge/');
                    		if (edge > 0) {
                    			// Edge (IE 12+) => return version number
                    			return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
                    		}
                    		// other browser
                    		return false;
                    	}
                    	if (!isIE()) return;
                    }

                    var control = el[0];
                    //to fix IE8 issue with parent and detail controller, we need to depend on the parent controller
                    scope.$watch(attrs.ieSelectFix, function() {
                        // setTimeout is needed starting from angular 1.3+
                        setTimeout(function() {
                            //this will add and remove the options to trigger the rendering in IE8
                            var option = document.createElement("option");
                            control.add(option, null);
                            control.remove(control.options.length - 1);
                        }, 0);
                    });
                }
            }
		});

/**
 * Directive for read-only checkbox, radio
 */
importDirective(
		'ngReadonly',
		function($window, $log, $timeout, $document) {
			function handleReadonly(el) {
				var $this = angular.element(el);
				var groups = $this.data('groups') || [];
				if ($this.is(':checked')) {
					groups.push({ id: $this.attr('id'), name: $this.attr('name') });
				}
				$this.data('groups', groups);
				$this.data('checked', $this.prop('checked'));
				$this.change(function(e) {
					$this.prop('checked', $this.data('checked'));
					var checkedgrps = $this.data('groups');
					if ((checkedgrps || []).length) {
						for(var i = 0; i < checkedgrps.length; i++) {
							var grp = checkedgrps[i];
							if ((grp.id || '').length) {
								angular.element('input:radio[id=' + grp.id + '][readonly],input:checkbox[id=' + grp.id + '][readonly]').prop('checked', true);
							}
							if ((grp.name || '').length) {
								angular.element('input:radio[name=' + grp.name + '][readonly],input:checkbox[name=' + grp.name + '][readonly]').prop('checked', true);
							}
						}
					}
					e.preventDefault();
					e.returnValue = false;
					return false;
				});
			}

			return {
				restrict: "A",
				link: function(scope, element, attrs, ctrls) {
					var name = attrs.name || '';
					var id = attrs.id || '';
					var type = attrs.type || '';
					var tagName = ((element && element.length && element[0].tagName)
							? element[0].tagName || '' : '');
					if (((id || '').length || (name || '').length)
							&& (tagName.toLowerCase() == 'input')
							&& (type == 'radio' || type == 'checkbox')) {
						var $thisEl = angular.element(element);
						var groups = [];
						// by name
						var groupNameEls = angular.element('input:radio[name=' + name + '][readonly],input:checkbox[name=' + name + '][readonly]');
						if (groupNameEls.length) {
							angular.forEach(groupNameEls, function(el) { handleReadonly(el); });
						}
						// by id
						var groupIdEls = angular.element('input:radio[id=' + id + '][readonly],input:checkbox[id=' + id + '][readonly]');
						if (groupIdEls) {
							angular.forEach(groupIdEls, function(el) { handleReadonly(el); });
						}
					}
				}
			};
		});

/**
 * Directive for preventing keys
 */
importDirective(
		'ngPreventKeys',
		function($window, $log, $timeout, $document) {
			return {
				restrict: "A",
				link: function(scope, element, attrs, ctrls) {
					var restrictKeys = scope.$eval(attrs.keys);
					element.on('keydown', function(e) {
						angular.forEach(restrictKeys, function(key) {
							if (key == e.which) {
								// Doesn't work at all
								e.preventDefault();
								e.returnValue = false;
								e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;
								// Works in all browsers but IE
								try { $window.stop(); }
								catch (e) {};
								// Works in IE
								try { $document.execCommand("Stop"); }
								catch (e) {};
								// Don't event know why it's here. Does nothing.
								return false;
							}
						});
					});
				}
			};
		});

/**
 * Directive for scaling image aspect ratio
 */
importDirective(
		'ngScaleAspectRatio',
		function($window, $log, $timeout) {
			/**
			 * Scale image/background image
			 */
			function scale(element, attrs, background, width, height) {
				var circle = attrs.ngCircle || '';
        		circle = (circle === 'true' || circle === '1' || circle === true);
        		// max width (-1 for original width)
        		var maxWidth = ((!isNaN(attrs.ngMaxWidth) && parseInt(attrs.ngMaxWidth, 10) > 0)
        				? parseInt(attrs.ngMaxWidth, 10) : -1);
        		maxWidth = (maxWidth < 0 ? -1 : maxWidth);
        		// max height (-1 for original height)
                var maxHeight = ((!isNaN(attrs.ngMaxHeight) && parseInt(attrs.ngMaxHeight, 10) > 0)
        				? parseInt(attrs.ngMaxHeight, 10) : -1);
                maxHeight = (maxHeight < 0 ? -1 : maxHeight);
                if ((width > 0 || height > 0) && (maxWidth >= 0 || maxHeight >= 0)) {
                	maxWidth = (maxWidth <= 0 ? maxHeight : maxWidth);
                	maxHeight = (maxHeight <= 0 ? maxWidth : maxHeight);
                    var ratioW = 1;  // Used for aspect ratio
                    var ratioH = 1;  // Used for aspect ratio
                    width = (width <= 0 ? maxWidth : width);
                    height = (height <= 0 ? maxHeight : height);
                    var minWH = (width > height ? height : width);
                    ratioW = (width > maxWidth ? maxWidth / width : ratioW);
                    ratioH = (height > maxHeight ? maxHeight / height : ratioH);
                    var newW = (width * ratioH);
                    var newH = (height * ratioW);
                    newW = (newW > maxWidth ? (width * ratioW) : newW);
                    newH = (newH > maxHeight ? (height * ratioH) : newH);
                    if (background) {
	                    element.css('background-size', newW + 'px ' + newH + 'px');
                    } else {
                    	element.css('height', newH + 'px');
	                    element.css('width', newW + 'px');
                    }
                    if (circle) element.css('border-radius', '50%');
                }
			};

			/**
			 * Parse element image source
			 */
			function requireImageSrc(element, attrs) {
				var tagName = ((element && element.length && element[0].tagName)
            			? element[0].tagName || '' : '');
				// detect image element
            	if (tagName.toLowerCase() == 'img') {
            		return element.attr('src');

            		// detect element's background image
            	} else {
            	    return element.css('background-image').replace(/url\(|\)$|"/ig, '');
            	}
			};
			/**
			 * Perform scaling element image or background image
			 */
			function scaleElement(element, attrs) {
				var oldSrc = requireImageSrc(element, attrs);
				if (!oldSrc || oldSrc.length <= 0) return;

				// clone image
				var tagName = ((element && element.length && element[0].tagName)
            			? element[0].tagName || '' : '');
				var img = new Image();
				img.src = oldSrc;
        		img.onerror = function() {
        			($log && typeof $log.warn === 'function')
            		&& $log.warn('Only apply ngScaleAspectRatio directive for image element or element has background-image!');
        		};

        		// reset current image source
        		(tagName.toLowerCase() == 'img')
        		&& element.attr('src', '');
        		(tagName.toLowerCase() != 'img')
        		&& element.css('background-image', '');

        		// scale image
            	if (img.width > 0 || img.height > 0) {
            		var timeout = $timeout(function() {
            			scale(element, attrs, (tagName.toLowerCase() !== 'img'), img.width, img.height);
	            		if (tagName.toLowerCase() !== 'img') {
	            			element.css('background-image', oldSrc);
	            		} else {
	            			element.attr('src', oldSrc);
	            		}
	            		if (timeout) {
        					$timeout.cancel(timeout);
        					timeout = null;
        				}
            		}, 100);
        	    } else {
        	    	(tagName.toLowerCase() == 'img')
            		&& element.attr('src', oldSrc);
            		(tagName.toLowerCase() != 'img')
            		&& element.css('background-image', oldSrc);
        	    }
			};

	        return {
	            restrict: "A",
	            link: function(scope, element, attrs, ctrls) {
	            	// max width (-1 for original width)
	        		var maxWidth = ((!isNaN(attrs.ngMaxWidth) && parseInt(attrs.ngMaxWidth, 10) > 0)
	        				? parseInt(attrs.ngMaxWidth, 10) : -1);
	        		maxWidth = (maxWidth < 0 ? -1 : maxWidth);
	        		// max height (-1 for original height)
	                var maxHeight = ((!isNaN(attrs.ngMaxHeight) && parseInt(attrs.ngMaxHeight, 10) > 0)
	        				? parseInt(attrs.ngMaxHeight, 10) : -1);
	                maxHeight = (maxHeight < 0 ? -1 : maxHeight);
	            	(maxWidth > 0) && element.css('width', maxWidth);
	            	(maxHeight > 0) && element.css('height', maxHeight);
	            	// scale
	            	scope.$on('$load', function(e) {
						//	//	var visibility = element.css('visibility') || 'visible';
						//	//	element.css('visibility', 'hidden');
						//	var timeout = $timeout(function() {
	            			scaleElement(element, attrs);
						//		//	element.css('visibility', visibility);
						//		if (timeout) {
						//			$timeout.cancel(timeout);
						//			timeout = null;
						//		}
						//	}, 100);
	            	});
	            }
	        }
		});

/**
 * Directive for showing scroll top element while clicking on it
 */
importDirective(
		'ngWindowScrollTop',
		function($window, $position, $timeout) {
			/**
			 * Show/hide scroll element
			 */
			function showScroll(element, attrs) {
				var winTop = $window.pageYOffset;
            	var limitTop = 10;
            	var limitElem = angular.element(attrs.showOnOver);
            	var distance = (isNaN(attrs.overDistance) || parseInt(attrs.overDistance, 10) <= 0
            			? 0 : parseInt(attrs.overDistance, 10));
            	if (limitElem && limitElem.length) {
            		var pos = $position.position(limitElem);
            		limitTop = pos.top;
            	}
            	if (winTop > limitTop + distance) {
            		element.css('display', 'block');
            	} else {
            		element.css('display', 'none');
            	}
			};

	        return {
	            restrict: "A",
	            link: function(scope, element, attrs, ctrls) {
	            	// click element to scroll top
	            	element.on('click', function() {
	            		// show animate
	            		angular.element('body').animate({ scrollTop: 0, }, 'slow', function() {
							timeout = $timeout(function() {
								// show/hide element
								showScroll(element, attrs);
								$timeout.cancel(timeout);
							}, 1000);
						});
	            	});
	            	// listen window scroll event
	            	scope.$on('$scroll', function(e) {
	            		// show/hide element
						showScroll(element, attrs);
	            	});
	            }
	        }
		});

/*
 * Directive for checking required value of angular ui-select
 */
importDirective(
		'uiSelectRequired',
		function($q, $window, $log) {
			/**
			 * Check the specified value whether is valid
			 *
			 * @param multiple specify checking multiple selection
			 * @param value selection value
			 * @param requiredOnAttribute specify attribute name to check if necessary by key value
			 *
			 * @return true for valid; else false
			 */
			function checkValid(multiple, value, requiredOnAttribute) {
				var arrValues = (value == null || value == undefined
						? [] : !angular.isArray(value) ? [value] : value);
				if (multiple && arrValues.length <= 0) {
					return false;
				} else if (multiple && arrValues.length > 0) {
					var checkAttr = requiredOnAttribute || '';
					var required = true;
					$.map(arrValues, function(v) {
						var checkVal = (
								checkAttr.length > 0
								&& typeof v === 'object'
								&& v.hasOwnProperty(checkAttr)
								? v[checkAttr] : v);
						if (checkAttr != null && checkAttr != undefined
								&& String(checkAttr).length > 0) {
							required = false;
						}
					});
					if (required) return false;
				} else if (!multiple) {
					var checkAttr = requiredOnAttribute || '';
					var checkVal = (
							checkAttr.length > 0
							&& typeof value === 'object'
							&& value.hasOwnProperty(checkAttr)
							? value[checkAttr] : value);
					if (checkAttr == null || checkAttr == undefined
							|| String(checkAttr).length <= 0) {
						return false;
					}
				}
				return true;
			};

		    return {
		        restrict: 'A',
		        require: 'ngModel',
		        link: function (scope, element, attrs, ngModel) {
		        	// validate by sync validators
		        	if (ngModel && ngModel.$asyncValidators) {
		        		ngModel.$validators.uiSelectRequired = function(modelValue, viewValue) {
		        			var deferred = $q.defer();
		        			var multiple = attrs.multiple || false;
		        			var requiredOnAttribute = attrs.requiredOnAttribute || '';
							var value = modelValue || viewValue;
							if (checkValid(multiple, value, requiredOnAttribute)) {
								deferred.resolve();
							} else {
								deferred.reject();
							}
							return deferred.promise;
		        		};

		        		// check by ng-required (belong to ng-required)
			        	ngModel.$isEmpty = function(modelValue) {
			            	var multiple = attrs.multiple || false;
			            	var requiredOnAttribute = attrs.requiredOnAttribute || '';
			                return !checkValid(multiple, modelValue, requiredOnAttribute);
			            };
		        	} else
		        		$log && $log.warn && $log.warn('Only applying when having ngModel atrribute!!!');
		        }
		    };
		});

/*
 * Directive for checking value and the first value
 * whether are match such as password and confirm password
 */
importDirective(
		'ngMatch',
		function ($parse) {
			var directive = {
					link: link,
					restrict: 'A',
					require: '?ngModel'
			};
			return directive;

			function link(scope, elem, attrs, ctrl) {
				// if ngModel is not defined, we don't need to do anything
				if (!ctrl) return;
				if (!attrs[directiveId]) return;

				var firstValue = $parse(attrs[directiveId]);
				var validator = function (value) {
					var temp = firstValue(scope),
					v = value === temp;
					ctrl.$setValidity('match', v);
					return value;
				}

				ctrl.$parsers.unshift(validator);
				ctrl.$formatters.push(validator);
				attrs.$observe(directiveId, function () {
					validator(ctrl.$viewValue);
				});
			}
		});

/*
 * Directive for broadcast listener to rootScope/scope
 * about a new NgModelController has been initialized.
 * The broadcast listener will be contain reference to that NgModelController
 */
importDirective(
		'ngModel',
		function($rootScope) {
	        return {
	            restrict: "A",
	            require : 'ngModel', // require NgModelController
	            link: function(scope, element, attrs, ctrls) {
	                // broadcast existence of new NgModelController
	                $rootScope.$broadcast('referenceNgModelController', ctrls);
	            }
	        }
		});
importDirective(
		'referenceNgModelController',
		function($rootScope) {
	        return {
	            restrict: "A",
	            require : 'ngModel', // require NgModelController
	            link: function(scope, element, attrs, ctrls) {
	                // broadcast existence of new NgModelController
	                $rootScope.$broadcast('referenceNgModelController', ctrls);
	            }
	        }
		});

/*
 * Directive for broadcast listener to rootScope/scope
 * about a new FormController has been initialized.
 * The broadcast listener will be contain reference to that FormController
 */
importDirective(
		'form',
		function($rootScope, $window, $document) {
	        return {
	            restrict: "E",
	            require: '^form', // require FormController
	            link: function(scope, element, attrs, ctrls) {
	            	var preventKeys = scope.$eval(attrs.preventKeys);
	            	if (angular.isArray(preventKeys) && preventKeys.length > 0) {
	            		element.on('keydown', function(e) {
	            			angular.forEach(preventKeys, function(key) {
								if (key == e.which) {
									// Doesn't work at all
									e.preventDefault();
									e.returnValue = false;
									e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;
									// Works in all browsers but IE
									try { $window.stop(); }
									catch (e) {};
									// Works in IE
									try { $document.execCommand("Stop"); }
									catch (e) {};
									// Don't event know why it's here. Does nothing.
									return false;
								}
	            			});
						});
	            	}
	                // broadcast existence of new FormController
	                $rootScope.$broadcast('referenceFormController', ctrls);
	            }
	        }
		});
importDirective(
		'referenceFormController',
		function($rootScope) {
	        return {
	            restrict: "A",
	            require: '^form', // require FormController
	            link: function(scope, element, attrs, ctrls) {
	                // broadcast existence of new FormController
	                $rootScope.$broadcast('referenceFormController', ctrls);
	            }
	        }
		});

// directive for validating ui-select
importDirective('validateOnProp', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			var propName = scope.$eval(attrs.validateOnProp);
			if (!propName || propName.length <= 0)
				return;
			ctrl.$validators[propName] = function(modelValue, viewValue) {
				if (!modelValue || ctrl.$isEmpty(modelValue)) {
					// consider empty models to be in-valid
					return false;
				}
				if (!viewValue || typeof viewValue !== 'object' || !viewValue.hasOwnProperty(propName) || !viewValue[propName] || ctrl.$isEmpty(viewValue[propName])) {
					return false;
				}
				// it is valid
				return true;
			};
		}
	};
});

// directive for generating sidebar
importDirective(
		'sidebar',
		function(recursionHelper) {
			return {
				restrict : 'E',
				scope : {
					module : '='
				},
				template : '<li id="mnu_{{module.id}}" class="dropdown {{module.css}}{{module.childs <= 0 ? \' leaf\' : \'\'}}">'
						+ '<a href="#" class="dropdown-toggle" data-toggle="dropdown" ng-if="module.childs > 0" title="{{module.display}}">'
						+ '<span class="title">{{module.display}}</span>'
						+ '<span class="caret" ng-if="module.depth <= 1"></span>'
						+ '</a>'
						+ '<a href="javascript:void(0);" such-href="{{module.mainUrl}}" class="dropdown-toggle" data-toggle="dropdown" ng-if="module.childs <= 0" title="{{module.display}}">'
						+ '<span class="title">{{module.display}}</span>' + '</a>' + '<ul class="dropdown-menu forAnimate" role="menu" ng-if="module.childs > 0">'
						+ '<li ng-repeat="child in module.children">' + '<sidebar module="child"></sidebar>' + '</li>' + '</ul>' + '</li>',
				compile : function(element) {
					// Use the compile function from the RecursionHelper,
					// And return the linking function(s) which it returns
					return recursionHelper.compile(element);
				}
			};
		});

// directive for checking model value whether greater (equals) than value
importDirective("ngGreaterThan", function($q, $locale, $window) {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, elm, attrs, ngModel) {
			if (ngModel && ngModel.$asyncValidators) {
				var grpsep = '\\' + $locale.NUMBER_FORMATS.GROUP_SEP;
				var grpregex = new RegExp(grpsep, 'g');
				ngModel.$asyncValidators.greaterThan = function(modelValue, viewValue) {
					var deferred = $q.defer();
					var equals = attrs.ngGreaterEquals;
					equals = (equals != "false");
					var compare = attrs.ngGreaterCompare;
					compare = (compare ? String(compare).replace(grpregex, "") : compare);
					var value = modelValue || viewValue;
					value = (value ? String(value).replace(grpregex, "") : value);
					if (!value && !compare) {
						deferred.resolve();
					} else {
						if (equals && parseFloat(value, 10) < parseFloat(compare, 10))
							deferred.reject();
						else if (equals && parseFloat(value, 10) >= parseFloat(compare, 10))
							deferred.resolve();
						else if (!equals && parseFloat(value, 10) <= parseFloat(compare, 10))
							deferred.reject();
						else if (!equals && parseFloat(value, 10) > parseFloat(compare, 10))
							deferred.resolve();
						else
							deferred.resolve();
					}
					return deferred.promise;
				};
			} else
				$log && $log.warn && $log.warn('Only applying when having ngModel atrribute!!!');
		}
	};
});

// directive for checking model value whether less (equals) than value
importDirective("ngLessThan", function($q, $locale, $window) {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, elm, attrs, ngModel) {
			if (ngModel && ngModel.$asyncValidators) {
				var grpsep = '\\' + $locale.NUMBER_FORMATS.GROUP_SEP;
				var grpregex = new RegExp(grpsep, 'g');
				ngModel.$asyncValidators.lessThan = function(modelValue, viewValue) {
					var deferred = $q.defer();
					var equals = attrs.ngLessEquals;
					equals = (equals != "false");
					var compare = attrs.ngLessCompare;
					compare = (compare ? String(compare).replace(grpregex, "") : compare);
					var value = modelValue || viewValue;
					value = (value ? String(value).replace(grpregex, "") : value);
					if (!value && !compare) {
						deferred.resolve();
					} else {
						if (equals && parseFloat(value, 10) > parseFloat(compare, 10))
							deferred.reject();
						else if (equals && parseFloat(value, 10) <= parseFloat(compare, 10))
							deferred.resolve();
						else if (!equals && parseFloat(value, 10) >= parseFloat(compare, 10))
							deferred.reject();
						else if (!equals && parseFloat(value, 10) < parseFloat(compare, 10))
							deferred.resolve();
						else
							deferred.resolve();
					}
					return deferred.promise;
				};
			} else
				$log && $log.warn && $log.warn('Only applying when having ngModel atrribute!!!');
		}
	};
});

//directive for checking whitespace and special characters
importDirective("ngSpecialChars", function($q) {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, elm, attrs, ngModel) {
			if (ngModel && ngModel.$asyncValidators) {
				ngModel.$asyncValidators.pattern = function(modelValue, viewValue) {
					var deferred = $q.defer();
					var value = modelValue || viewValue;
					if (value && (value.match(/^\s*$/) || !value.match(/^[a-zA-Z0-9_-]*$/))) {
						deferred.reject();
					} else {
						deferred.resolve();
					}
					return deferred.promise;
				};
			} else
				$log && $log.warn && $log.warn('Only applying when having ngModel atrribute!!!');
		}
	};
});


// directive for checking valid email
importDirective("ngValidEmail", function($q) {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, elm, attrs, ngModel) {
			if (ngModel && ngModel.$asyncValidators) {
				ngModel.$asyncValidators.email = function(modelValue, viewValue) {
					var deferred = $q.defer();
					var value = modelValue || viewValue;
					// var regex = /^[_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.(([0-9]{1,3})|([a-zA-Z]{2,3})|(aero|coop|info|museum|name))$/;
					var regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
					if (value && value.length && (!value.indexOf('@') < 0 || !value.match(regex))) {
						deferred.reject();
					} else {
						deferred.resolve();
					}
					return deferred.promise;
				};
			} else
				$log && $log.warn && $log.warn('Only applying when having ngModel atrribute!!!');
		}
	};
});

// directive for checking unique key
importDirective("ngUnique", function($q, ajaxService, $window) {
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, elm, attrs, ngModel) {
			if (ngModel && ngModel.$asyncValidators) {
				ngModel.$asyncValidators.unique = function(modelValue, viewValue) {
					var deferred = $q.defer();
					var value = modelValue || viewValue;
					var shouldCheck = ((typeof value === 'string' && value.length)
							|| (typeof value !== 'string' && value != null && value != undefined));
					if (shouldCheck) {
						var action = attrs.ngUniqueAction;
						var shouldInvoke = scope[attrs.ngUniqueConditions];
						var invoker = scope[attrs.ngUniqueParam];
						if (action && action.length > 0 && typeof invoker === 'function') {
							// check before posting
							var should = true;
							if (typeof shouldInvoke === 'function') {
								should = shouldInvoke(value, elm) || false;
							}
							if (should == true) {
								var params = invoker(value, elm) || {};
								ajaxService.post({
									url: action,
									data: params,
									success: function(result) {
										deferred.resolve();
									},
									error: function(data) {
										deferred.reject();
									}
								});
							} else {
								deferred.resolve();
							}
						}
					} else {
						deferred.resolve(); // Ensure promise is resolved if we hit this
					}
					return deferred.promise;
				};
			} else
				$log && $log.warn && $log.warn('Only applying when having ngModel atrribute!!!');
		}
	};
});

//directive for checking unique key
importDirective("ngToggleShow", function() {
	return {
		restrict : 'A',
		priority: 1,
		link: function(scope, elm, attrs) {
			// The calling context is passing in an expression that will determine
            // when the container changes its visibility. When it changes, we want
            // to inspect the dimensions of the element.
			var invoker = scope.$eval(attrs.ngPerformShow);
			if (typeof invoker === 'function') {
				if (attrs.ngShow && attrs.ngShow.length > 0) {
					scope.$watch(attrs.ngShow, invoker);
				}
				else if (attrs.ngIf && attrs.ngIf.length > 0) {
					scope.$watch(attrs.ngIf, invoker);
				}
			}
		}
	};
});

// directive for randomizing background-color value
importDirective("ngRandomBackColor", function() {
	return {
		restrict : 'EA',
		replace : false,
		link : function(scope, elem, attr) {
			elem.css('background-color', ('#' + Math.floor(Math.random() * 16777215).toString(16)));
		}
	};
});

// directive for randomizing text-color value
importDirective("ngRandomTextColor", function() {
	return {
		restrict : 'EA',
		replace : false,
		link : function(scope, elem, attr) {
			elem.css('color', ('#' + Math.floor(Math.random() * 16777215).toString(16)));
		}
	};
});

// directive for randomizing dashboard color classes
importDirective("ngRandomClass", function() {
	return {
		restrict : 'EA',
		replace : false,
		scope : {
			ngClasses : "="
		},
		link : function(scope, elem, attr) {
			// Add random background class to selected element
			var classes = scope.ngClasses;
			if (!angular.isArray(classes)) {
				classes = [];
				var maxSkins = (typeof MAX_DASHBOARD_SKINS_NUMBER === 'number' && MAX_DASHBOARD_SKINS_NUMBER > 0 ? MAX_DASHBOARD_SKINS_NUMBER : 10);
				for (var i = 1; i <= maxSkins; i++) {
					classes.push('skin-' + i);
				}
			}
			elem.addClass(classes[Math.floor(Math.random() * (classes.length - 1)) + 1]);
		}
	};
});

// directive for element with href such as a link
importDirective('suchHref', function($http, $window) {
	return {
		restrict : 'A',
		link : function(scope, element, attr) {
			element.attr('style', 'cursor:pointer');
			element.on('click', function(e) {
				if (attr.suchAjax == undefined || attr.suchAjax == null || attr.suchAjax.length <= 0) {
					var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
					$window.location.href = basepath + attr.suchHref;
				} else {
					e.stopPropagation();
					scope.goAjax(attr.suchHref, {}, attr.suchAjax);
				}
				scope.$apply();
			});
			element.on('mouseover', function() {
				if (!element.hasClass('suchHref')) {
					element.addClass('suchHref');
				}
			});
			element.on('mouseout', function() {
				if (element.hasClass('suchHref')) {
					element.removeClass('suchHref');
				}
			});
		}
	};
});

// // directive for formatting [timestamp] element
// importDirective('timeStampFormat', function(dateFilter) {
// return {
// require : 'ngModel',
// link : function(scope, element, attrs, ngModel) {
// var format = attrs.timeStampFormat || 'yyyy-MM-dd HH:mm:ss.ss';
// ngModel.$parsers.push(function(viewValue) {
// return dateFilter(viewValue, format);
// });
// }
// };
// });

// directive for [file] model
importDirective('fileModel', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;
			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
});

// directive for jquery date/time picker
importDirective('jqdatepicker', function() {
	var putObject = function(path, object, value) {
		var modelPath = path.split(".");
		function fill(object, elements, depth, value) {
			var hasNext = ((depth + 1) < elements.length);
			if (depth < elements.length && hasNext) {
				if (!object.hasOwnProperty(modelPath[depth])) {
					object[modelPath[depth]] = {};
				}
				fill(object[modelPath[depth]], elements, ++depth, value);
			} else {
				object[modelPath[depth]] = value;
			}
		}
		fill(object, modelPath, 0, value);
	};

	var applyValue = function(scope, attrs, ct) {
		var modelPath = $(this).attr('ng-model');
		if (modelPath && modelPath.length > 0) {
			putObject(modelPath, scope, ct);
			scope.$apply();
		} else if (attrs.onSelectDate) {
			attrs.onSelectDate(ct);
		}
	};

	return {
		restrict : 'A',
		link : function(scope, element, attrs, ngModelCtrl) {
			$(function() {
				element.datetimepicker({
					lang : attrs.lang || 'ja',
					timepicker : attrs.time || false,
					datepicker : attrs.date || true,
					format : attrs.format || 'Y/m/d',
					disableOnClass : attrs.disabledClass || '',
					minDate : attrs.minDate || false,
					maxDate : attrs.maxDate || false,
					minTime : attrs.minTime || false,
					maxTime : attrs.maxTime || false,
					inline : attrs.inline || false,
					monthChangeSpinner : attrs.monthChangeSpinner || true,
					closeOnDateSelect : attrs.closeOnDateSelect || false,
					closeOnWithoutClick : attrs.closeOnWithoutClick || true,
					closeOnInputClick : attrs.closeOnInputClick || true,

					onSelectDate : function(ct, $i) {
						applyValue(scope, attrs, ct);
					},
					onSelectTime : function(ct, $i) {
						applyValue(scope, attrs, ct);
					},
					onChangeMonth : function(ct, $i) {
						applyValue(scope, attrs, ct);
					},
					onChangeDateTime : function(ct, $i) {
						applyValue(scope, attrs, ct);
					},
					onShow : function(ct, $i) {
						applyValue(scope, attrs, ct);
					},
					onClose : function(ct, $i) {
						applyValue(scope, attrs, ct);
					},
					onGenerate : function(ct, $i) {
						applyValue(scope, attrs, ct);
					},
				});
			});
		}
	};
});

// directive for date/time picker
importDirective('datepicker', function($document, $http) {
	var linker = function(scope, element) {
		scope.datepickerTrigger = ((angular.element(element).prev('.datepickerTrigger').length !== 0) ? angular.element(element).prev('.datepickerTrigger') : undefined);
		// dateTypeがbuttonであれば、イベントを設定する
		if (scope.dateType === "button") {
			// 初期化
			angular.element(element).hide();
			// buttonが押下されたら、datepickerを表示する。
			angular.element(scope.datepickerTrigger).on('click', function() {
				angular.element(this).hide();
				angular.element(element).show().find('.nlh4jDatepicker > input').focus();
			});
			// 背景がクリックされたら閉じる
			$document.on('click', function(event) {
				// トリガー要素判定フラグ
				var triggerLength = $(event.target).closest(scope.datepickerTrigger).length;
				// 子要素判定フラグ
				var length1 = $(event.target).closest(element).length;
				// 子要素判定フラグ
				var length2 = $(event.target).closest('.ui-datepicker-group').length;
				// 親への伝播を行わない
				if (event.isPropagationStopped()) {
					return false;
				} else {
					event.stopPropagation();
				}
				if (length1 === null || length1 === undefined)
					return false;
				// トリガーや子要素でない背景が押下されたら、datepickerを閉じ、ボタンを表示する
				if (!triggerLength && !length1 && !length2) {
					angular.element(element).hide();
					angular.element(scope.datepickerTrigger).show();
				}
			});
		}
	};
	var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
	return {
		restrict : 'E',
		templateUrl : basepath + '/common/datepicker',
		scope : {
			dateId : '@',
			dateType : '=',
			dateValue : '=',
			dateMinValue : '=',
			dateMaxValue : '=',
			dateValidation : '=',
		},
		link : linker,
		controller : 'nlh4jDatepickerController'
	};
});

// directive for [input] currency
importDirective('currencyInput', function($filter, $browser) {
	return {
		require : 'ngModel',
		link : function($scope, $element, $attrs, ngModelCtrl) {
			var listener = function() {
				var value = $element.val().replace(/,/g, '');
				$element.val($filter('number')(value, false));
			};
			// This runs when we update the text field
			ngModelCtrl.$parsers.push(function(viewValue) {
				return viewValue.replace(/,/g, '');
			});
			// This runs when the model gets updated on the scope directly and
			// keeps our view in sync
			ngModelCtrl.$render = function() {
				$element.val($filter('number')(ngModelCtrl.$viewValue, false));
			};
			$element.bind('change', listener);
			$element.bind('keydown', function(event) {
				var key = (event.which || event.keyCode);
				// If the keys include the CTRL, SHIFT, ALT, or META keys,
				// or the arrow keys, do nothing.
				// This lets us support copy and paste too
				if (key == 91 || (15 < key && key < 19) || (37 <= key && key <= 40))
					return;
				// Have to do this or changes don't get picked up properly
				$browser.defer(listener);
			});
			$element.bind('paste cut', function() {
				$browser.defer(listener);
			});
		}
	};
});

// directive for [file] element
importDirective('ngFileSelect', function() {
	return {
		link : function($scope, el) {
			el.bind('change', function(e) {
				$scope.file = (e.srcElement || e.target).files[0];
				$scope.getFile($scope.file);
			});
		}
	};
});

// directive for [select] element
importDirective('chosenSelect', function() {
	var linker = function(scope, element, attrs) {
		var width = attrs.chosenWidth;
		var list = attrs['chosenSelect'];
		var isDisabled = attrs['ngDisabled'];
		var deselect = attrs.chosenDeselect;

		scope.$on('chosen#updateList', function(event, data) {
			scope.$watch(attrs.list, function() {
				$(element).trigger('liszt:updated');
				$(element).trigger('chosen:updated');
			});
		});
		if (angular.isDefined(attrs.list) && attrs.list !== null) {
			scope.$watch(attrs.list, function() {
				$(element).trigger('liszt:updated');
				$(element).trigger('chosen:updated');
			});
		}
		if (angular.isDefined(list) && list !== null) {
			scope.$watch(list, function() {
				$(element).trigger('liszt:updated');
				$(element).trigger('chosen:updated');
			});
		}
		scope.$watch(attrs['ngModel'], function() {
			$(element).trigger('chosen:updated');
		});
		if (angular.isDefined(isDisabled) && isDisabled !== null) {
			scope.$watch(isDisabled, function(newValue) {
				$(element).prop('disabled', newValue);
				$(element).trigger('liszt:updated');
				$(element).trigger('chosen:updated');
			});
		}
		scope.$watch(attrs['ngModel'], function() {
			$(element).trigger('chosen:updated');
		});
		width = (width == null || width == '' || width === undefined) ? 'auto' : width;
		$(element).chosen({
			'width' : width,
			allow_single_deselect : true,
			disable_search_threshold : 5
		});
		if (deselect == null || deselect === '' || deselect === 'true' || deselect == true) {
			deselect = true;
		} else if (deselect === 'false' || deselect == false) {
			deselect = false;
		} else {
			deselect = true;
		}
		$(element).data('chosen').allow_single_deselect = deselect;
	};
	return {
		restrict : 'A',
		link : linker,
	};
});

// directive for page view title
importDirective('viewTitle', function($rootScope, $timeout) {
	return {
		restrict : 'EA',
		link : function(scope, iElement, iAttrs, controller, transcludeFn) {
			// If we've been inserted as an element then we detach from the DOM
			// because the caller
			// doesn't want us to have any visual impact in the document.
			// Otherwise, we're piggy-backing on an existing element so we'll
			// just leave it alone.
			var tagName = iElement[0].tagName.toLowerCase();
			if (tagName === 'view-title' || tagName === 'viewtitle') {
				iElement.remove();
			}

			scope.$watch(function() {
				return iElement.text();
			}, function(newTitle) {
				$rootScope.viewTitle = title = newTitle;
			});
			scope.$on('$destroy', function() {
				title = undefined;
				// Wait until next digest cycle do delete viewTitle
				$timeout(function() {
					if (!title) {
						// No other view-title has reassigned title.
						delete $rootScope.viewTitle;
					}
				});
			});
		}
	};
});

// directive for page view head
importDirective('viewHead', function() {
	var head = angular.element(document.head);
	return {
		restrict : 'A',
		link : function(scope, iElement, iAttrs, controller, transcludeFn) {
			// Move the element into the head of the document.
			// Although the physical location of the document changes, the
			// element remains
			// bound to the scope in which it was declared, so it can refer to
			// variables from
			// the view scope if necessary.
			head.append(iElement);

			// When the scope is destroyed, remove the element.
			// This is on the assumption that we're being used in some sort of
			// view scope.
			// It doesn't make sense to use this directive outside of the view,
			// and nor does it
			// make sense to use it inside other scope-creating directives like
			// ng-repeat.
			scope.$on('$destroy', function() {
				iElement.remove();
			});
		}
	};
});

// directive for numeric field
importDirective('onlyNumber', function($filter, $locale) {
	return {
		restrict : 'EA',
		require : 'ngModel',
		link : function(scope, element, attrs, ngModel, $window) {
			var decsep = $locale.NUMBER_FORMATS.DECIMAL_SEP;
			var grpsep = '\\' + $locale.NUMBER_FORMATS.GROUP_SEP;
			var grpregex = new RegExp(grpsep, 'g');
			$(element).css("text-align", "right");
			$(element).attr('focused', '0');
			if (ngModel) {
				// format number on initializing
				var viewValue = ngModel.$viewValue || ngModel.$modelValue || $(element).val();
				var allowDecimal = (attrs.allowDecimal != "false");
				var digits = attrs.decimalNumbers;
				digits = (isNaN(digits) || parseInt(digits, 10) <= 0 || !allowDecimal ? 0 : parseInt(digits, 10));
				var defValue = (isNaN(attrs.defaultValue) ? (attrs.allowEmpty ? '' : 0) : attrs.defaultValue);
				var hideGrp = (attrs.hideGrouping == "true");
				viewValue = ((!viewValue || isNaN(viewValue.replace(grpregex, ""))) ? defValue : String(viewValue).replace(grpregex, ""));
				viewValue = isNaN(viewValue) || viewValue.length <= 0 ? viewValue : hideGrp ? parseFloat(viewValue, 10) : $filter('number')(viewValue, digits);

				// @TODO fix infdig loop (see $digest loop)
				if (!ngModel.$modelValue)
					ngModel.$modelValue = viewValue;
				ngModel.$setViewValue(viewValue);
				ngModel.$render();

				// max-length
				var posdigits = attrs.positiveNumbers;
				posdigits = (isNaN(posdigits) || parseInt(posdigits, 10) <= 0 ? 0 : parseInt(posdigits, 10));
				if (digits == 0 && posdigits > 0 && hideGrp) {
					$(element).attr('maxlength', posdigits);
				} else if (digits == 0) {
					$(element).removeAttr('maxlength');
				}

				// listen focus event for removing grouping
				$(element).bind('focus', function() {
					$(element).attr('focused', '1');
					var viewValue = ngModel.$viewValue || ngModel.$modelValue || $(element).val();
					viewValue = ((!viewValue || isNaN(String(viewValue).replace(grpregex, ""))) ? defValue : String(viewValue).replace(grpregex, ""));
					ngModel.$setViewValue(viewValue);
					ngModel.$render();
				});
				// listen blur event for formatting grouping if necessary
				$(element).bind('blur', function() {
					$(element).attr('focused', '0');
					var viewValue = ngModel.$viewValue || ngModel.$modelValue || $(element).val();
					var digits = attrs.decimalNumbers;
					digits = (isNaN(digits) || parseInt(digits, 10) <= 0 || !allowDecimal ? 0 : parseInt(digits, 10));
					var defValue = (isNaN(attrs.defaultValue) ? (attrs.allowEmpty ? '' : 0) : attrs.defaultValue);
					var hideGrp = (attrs.hideGrouping == "true");
					viewValue = ((!viewValue || isNaN(String(viewValue).replace(grpregex, ""))) ? defValue : String(viewValue).replace(grpregex, ""));
					viewValue = isNaN(viewValue) || viewValue.length <= 0 ? viewValue : hideGrp ? parseFloat(viewValue, 10) : $filter('number')(viewValue, digits);
					ngModel.$setViewValue(viewValue);
					ngModel.$render();
				});

				// watch changing value
				scope.$watch(attrs.ngModel, function(newValue, oldValue) {
					// @TODO fix infdig loop (see $digest loop)
					if (!ngModel.$modelValue) {
						ngModel.$modelValue = newValue;
						return;
					}

					var start = (element.length > 0 && element[0].selectionStart ? element[0].selectionStart : -1);
					var end = (element.length > 0 && element[0].selectionEnd ? element[0].selectionEnd : -1);
					var spiltArray = String(newValue).split("");
					var allowDecimal = (attrs.allowDecimal != "false");
					// var hideGrp = (attrs.hideGrouping == "true");
					var posdigits = attrs.positiveNumbers;
					posdigits = (isNaN(posdigits) || parseInt(posdigits, 10) <= 0 ? 0 : parseInt(posdigits, 10));
					var digits = attrs.decimalNumbers;
					digits = (isNaN(digits) || parseInt(digits, 10) <= 0 || !allowDecimal ? 0 : parseInt(digits, 10));
					var defValue = (isNaN(attrs.defaultValue) ? (attrs.allowEmpty ? '' : 0) : attrs.defaultValue);
					if (defValue)
						defValue = String(defValue).replace(grpregex, "");
					if (oldValue)
						oldValue = String(oldValue).replace(grpregex, "");
					if (newValue)
						newValue = String(newValue).replace(grpregex, "");
					if ((!oldValue && !newValue) || (oldValue === newValue) || (!isNaN(oldValue) && !isNaN(newValue) && parseFloat(oldValue, 10) === parseFloat(newValue, 10))) {
						if ($(element).attr('focused') != '1') {
							var viewValue = newValue;
							var digits = attrs.decimalNumbers;
							digits = (isNaN(digits) || parseInt(digits, 10) <= 0 || !allowDecimal ? 0 : parseInt(digits, 10));
							var defValue = (isNaN(attrs.defaultValue) ? (attrs.allowEmpty ? '' : 0) : attrs.defaultValue);
							var hideGrp = (attrs.hideGrouping == "true");
							viewValue = ((!viewValue || isNaN(String(viewValue).replace(grpregex, ""))) ? defValue : String(viewValue).replace(grpregex, ""));
							viewValue = isNaN(viewValue) || viewValue.length <= 0 ? viewValue : hideGrp ? parseFloat(viewValue, 10) : $filter('number')(viewValue, digits);
							ngModel.$setViewValue(viewValue);
							ngModel.$render();
						}
						return;
					}

					// if valid new value
					if (!isNaN(newValue)) {
						// not allow negative
						if (attrs.allowNegative == "false") {
							if (spiltArray[0] == '-') {
								newValue = newValue.replace("-", "");
								newValue = ((!newValue || isNaN(newValue)) ? defValue : newValue);
								newValue = (posdigits > 0 ? String(newValue).substring(0, posdigits) : newValue);
								if (!isNaN(newValue)) {
									// newValue = hideGrp ? newValue :
									// $filter('number')(newValue, digits);
									ngModel.$setViewValue(newValue);
									ngModel.$render();
								}
							}
						}

						// not allow decimal
						if (!allowDecimal) {
							newValue = parseInt(newValue);
							newValue = ((!newValue || isNaN(newValue)) ? defValue : newValue);
							newValue = (posdigits > 0 ? String(newValue).substring(0, posdigits) : newValue);
							if (!isNaN(newValue)) {
								// newValue = hideGrp ? newValue :
								// $filter('number')(newValue, digits);
								ngModel.$setViewValue(newValue);
								ngModel.$render();
							}
						}

						// allow decimal
						if (allowDecimal) {
							var bdot = (String(newValue).indexOf(decsep) >= 0);
							var n = String(newValue).split(decsep);
							if (n && n.length > 0) {
								n[0] = (posdigits > 0 ? n[0].substring(0, posdigits) : n[0]);
								if (n.length > 1)
									n[1] = (digits > 0 ? n[1].substring(0, digits) : n[1]);
								if ((n.length > 1 && n[1].length > 0) || n[0].length > 0) {
									// newValue = [ hideGrp ? n[0] :
									// $filter('number')(n[0], 0), n[1]
									// ].join(bdot ? "." : "");
									newValue = [ n[0], n[1] ].join(bdot ? decsep : "");
								} else {
									newValue = n[0];
								}
								if (newValue && !isNaN(newValue.replace(grpregex, ""))) {
									ngModel.$setViewValue(newValue);
									ngModel.$render();
								}
							}
						}
					}

					if (spiltArray.length === 0) {
						if (start >= 0 && end >= 0) {
							// element[0].setSelectionRange(start, end);
						}
						return;
					}
					if (spiltArray.length === 1 && ((attrs.allowNegative != "false" && spiltArray[0] == '-') || spiltArray[0] === '.')) {
						if (start >= 0 && end >= 0) {
							// element[0].setSelectionRange(start, end);
						}
						return;
					}
					if (spiltArray.length === 2 && newValue === '-.') {
						if (start >= 0 && end >= 0) {
							// element[0].setSelectionRange(start, end);
						}
						return;
					}

					/* Check it is number or not. */
					if (newValue == undefined || newValue == null || isNaN(String(newValue).replace(grpregex, ""))) {
						oldValue = ((!oldValue || isNaN(oldValue)) ? defValue : oldValue);
						// oldValue = hideGrp || isNaN(oldValue) ? oldValue :
						// $filter('number')(oldValue, digits);
						ngModel.$setViewValue(oldValue);
						ngModel.$render();
					}
					if (start >= 0 && end >= 0) {
						// element[0].setSelectionRange(start, end);
					}
				});
			} else
				$log && $log.warn && $log.warn('Only applying when having ngModel atrribute!!!');
		}
	};
});

importDirective('numberInterger', function() {
	return {
		require : 'ngModel',
		link : function(scope, element, attrs, modelCtrl) {
			modelCtrl.$parsers.push(function(inputValue) {
				// this next if is necessary for when using ng-required on your
				// input.
				// In such cases, when a letter is typed first, this parser will
				// be called
				// again, and the 2nd time, the value will be undefined
				if (inputValue == undefined)
					return ''
				var transformedInput = inputValue.replace(/[^0-9]/g, '');
				if (transformedInput != inputValue) {
					modelCtrl.$setViewValue(transformedInput);
					modelCtrl.$render();
				}

				return transformedInput;
			});
		}
	};
});

// directive format number
importDirective('format', ['$filter', function ($filter) {
    return {
        require: '?ngModel',
        link: function (scope, elem, attrs, ctrl) {
            if (!ctrl) return;

            $(elem).css("text-align", "right");

            ctrl.$formatters.unshift(function (a) {
            	if (isNaN(ctrl.$modelValue)) return ctrl.$modelValue;
                return $filter(attrs.format)(ctrl.$modelValue)
            });


            ctrl.$parsers.unshift(function (viewValue) {
                var plainNumber = viewValue.replace(/[^\d|\-+|\.+]/g, '');
                elem.val($filter(attrs.format)(plainNumber));
                return plainNumber;
            });
        }
    };
}]);

// directive for maxlength field
importDirective('myMaxlength', function() {
	return {
		restrict : 'EA',
		require : 'ngModel',
		link : function(scope, element, attrs, ngModelCtrl) {
			if (!isNaN(attrs.myMaxlength)) {
				var maxlength = Number(attrs.myMaxlength);
				function fromUser(text) {
					if (text.length > maxlength) {
						var transformedInput = text.substring(0, maxlength);
						ngModelCtrl.$setViewValue(transformedInput);
						ngModelCtrl.$render();
						return transformedInput;
					}
					return text;
				}
				ngModelCtrl.$parsers.push(fromUser);
			}
		}
	};
});

// directive disable contents
importDirective('disableContents', function() {
	return {
		compile : function(tElem, tAttrs) {

			var inputs = tElem.find('input');
			inputs.attr('ng-disabled', tAttrs['disableContents']);

			var selects = tElem.find('select');
			selects.attr('ng-disabled', tAttrs['disableContents']);

			var textarea = tElem.find('textarea');
			textarea.attr('ng-disabled', tAttrs['disableContents']);

			var buttons = tElem.find('button');
			buttons.attr('ng-disabled', tAttrs['disableContents']);
		}
	};
});

/**
 * Description: removes white space from text. useful for html values that cannot have spaces
 */
importDirective('removeSpace', function() {
	return {
		restrict : 'EA',
		require : 'ngModel',
		link : function(scope, element, attrs, ngModel) {
			if (ngModel) {
				// check for initializing view
				var viewValue = ngModel.$viewValue;
				if ((!viewValue || isNaN(viewValue))) {
					ngModel.$setViewValue(isNaN(attrs.defaultValue) ? '' : attrs.defaultValue);
				}
				// watch changing value
				scope.$watch(attrs.ngModel, function(newValue, oldValue) {
					// @TODO fix infdig loop (see $digest loop)
					if (!ngModel.$modelValue) {
						ngModel.$modelValue = newValue;
						return;
					}
					ngModel.$setViewValue((!newValue) ? '' : newValue.replace(/ /g, ''));
					ngModel.$render();
				});
			}
		}
	};
});

importDirective(
		'editInPlace',
		function() {
			return {
				restrict : 'E',
				scope : {
					ngModel : '=',
					type : '@',
					editable : '=',
					pattern : "@",
					max : '@',
					options : '=',
					optionLabel : '@',
					optionValue : '@',
					defaultLabel : '@',
					cellValueChanged : "&"
				},
				template : function(element, attrs) {

					var tmp;
					switch (attrs.type) {
					case 'time':
						// time
						tmp = '<div ng-click="editable && beginEdit()" ng-bind="ngModel" ng-class="{ editable : editable }"></div>'
								+ '<input type="text" ng-keypress="$event.which === 13 && endEdit()" ng-blur="endEdit()" ng-blur="endEdit()" ng-model="ngModel" data-placement="top" data-align="top" data-autoclose="true" class="text-center" ng-readonly="true"></input>';
						break;
					case 'number':
						// number
						tmp = '<div ng-click="editable && beginEdit()" ng-bind="ngModel" ng-class="{ editable : editable }"></div>'
								+ '<input type="text" ng-keypress="$event.which === 13 && endEdit()" ng-blur="endEdit()" ng-blur="endEdit()" ng-model="ngModel" ng-trim="false" class="text-right"></input>';
						break;
					case 'select':
						// select
						tmp = '<div ng-click="editable && beginEdit()" ng-bind="ngModel[optionLabel]" ng-class="{ editable : editable }"></div>'
								+ '<select ng-keypress="$event.which === 13 && endEdit()" ng-blur="endEdit()" ng-blur="endEdit()" ng-model="ngModel" ng-options="option[optionLabel] for option in options track by option[optionValue]">'
								+ '<option value="" selected="selected">{{::defaultLabel}}</option>' + '</select>';
						break;
					default:
						// text
						tmp = '<div ng-click="editable && beginEdit()" ng-bind="ngModel" ng-class="{ editable : editable }"></div>'
								+ '<input ng-keypress="($event.which === 13) ? endEdit() : false" ng-blur="endEdit()" ng-model="ngModel"></input>';
						break;
					}

					return tmp;
				},
				controller : function($scope) {
				},
				link : function($scope, element, attrs) {
					// Let's get a reference to the input element, as we'll want to
					// reference it.
					var displayElement = angular.element(element.children()[0]);
					var inputElement = angular.element(element.children()[1]);

					// This directive should have a set class so we can style it.
					element.addClass('edit-in-place');

					// Initially, we're not editing.
					$scope.editing = false;

					// ng-click handler to activate edit-in-place
					$scope.beginEdit = function() {

						$scope.oldValue = angular.copy($scope.ngModel);

						// get display element width before hide it
						var displayElementWidth = displayElement[0].clientWidth;
						// Fixed width
						inputElement.css('width', displayElementWidth.toString() + 'px');

						// hide display element
						$scope.editing = true;

						// We control display through a class on the directive
						// itself.
						// See the CSS.
						element.addClass('active');

						// And we must focus the element.
						// `angular.element()` provides a chainable array, like
						// jQuery
						// so to access a native DOM function,
						// we have to reference the first element in the array.
						setTimeout(function() {
							inputElement[0].focus();
						}, 100);

						// initialize for input control
						switch (attrs.type) {
						case 'time':
							inputElement.clockpicker();
							break;
						default:
							break;
						}
					};

					// When we leave the input, we're done editing.
					$scope.endEdit = function() {

						$scope.editing = false;
						element.removeClass('active');
						inputElement.removeAttr('disabled');

						switch (attrs.type) {
						case 'number':
							// not-allow invalid number
							// limit maximun value
							var regex = new RegExp(attrs.pattern);
							if (regex.test($scope.ngModel)) {
								if (Number($scope.ngModel) > $scope.max) {

									$scope.ngModel = $scope.oldValue;
									element.addClass('need-attention');
									setTimeout(function() {
										element.removeClass('need-attention');
									}, 2000);
								}
							} else {

								$scope.ngModel = $scope.oldValue;
								element.addClass('need-attention');
								setTimeout(function() {
									element.removeClass('need-attention');
								}, 2000);
							}
							break;
						default:
							break;
						}

						// fire cell value change event if having a change
						if (typeof $scope.oldValue == 'object' && typeof $scope.ngModel == 'object') {
							if (!angular.equals($scope.oldValue, $scope.ngModel)) {
								$scope.cellValueChanged();
							}
						} else {
							if ($scope.oldValue !== $scope.ngModel) {
								$scope.cellValueChanged();
							}
						}
					};
				}
			};
		});

/**
 * Directive for showing barcode SVG
 */
importDirective(
		'jsbarcode',
		function($rootScope, $compile, $log) {
			var renderBarcode = function(attrs, value) {
				attrs = (attrs || {});
				if ((attrs.__barcodeId || '').length
						&& (angular.element('#' + attrs.__barcodeId) || []).length
						&& (value || '').length) {
					// apply barcode
                	JsBarcode('#' + attrs.__barcodeId, value, {
                		format: (attrs.jsbarcodeFormat || 'code128')
	                	, width: (isNaN(attrs.jsbarcodeWidth)
	                			|| parseInt(attrs.jsbarcodeWidth, 10) <= 0
	                			? 2 : parseInt(attrs.jsbarcodeWidth, 10))
	                	, height: (isNaN(attrs.jsbarcodeHeight)
	                			|| parseInt(attrs.jsbarcodeHeight, 10) <= 0
	                			? 100 : parseInt(attrs.jsbarcodeHeight, 10))
	                	, displayValue: (attrs.jsbarcodeDisplayValue == true)
	                	, fontOptions: (attrs.jsbarcodeFontOptions || '')
	                	, font: (attrs.jsbarcodeFont || 'monospace')
	                	, textAlign: (attrs.jsbarcodeTextAlign || 'center')
	                	, textPosition: (attrs.jsbarcodeTextPosition || 'bottom')
	                	, textMargin: (isNaN(attrs.jsbarcodeTextMargin)
	                			|| parseInt(attrs.jsbarcodeTextMargin, 10) <= 0
	                			? 2 : parseInt(attrs.jsbarcodeTextMargin, 10))
	                	, fontSize: (isNaN(attrs.jsbarcodeFontSize)
	                			|| parseInt(attrs.jsbarcodeFontSize, 10) <= 0
	                			? 20 : parseInt(attrs.jsbarcodeFontSize, 10))
	                	, background: (attrs.jsbarcodeBackground || '#ffffff')
	                	, lineColor: (attrs.jsbarcodeLineColor || '#000000')
	                	, margin: (isNaN(attrs.jsbarcodeMargin)
	                			|| parseInt(attrs.jsbarcodeMargin, 10) <= 0
	                			? 10 : parseInt(attrs.jsbarcodeMargin, 10))
	                	, marginTop: (isNaN(attrs.jsbarcodeMarginTop)
	                			|| parseInt(attrs.jsbarcodeMarginTop, 10) <= 0
	                			? undefined : parseInt(attrs.jsbarcodeMarginTop, 10))
	                	, marginBottom: (isNaN(attrs.jsbarcodeMarginBottom)
	                			|| parseInt(attrs.jsbarcodeMarginBottom, 10) <= 0
	                			? undefined : parseInt(attrs.jsbarcodeMarginBottom, 10))
	                	, marginLeft: (isNaN(attrs.jsbarcodeMarginLeft)
	                			|| parseInt(attrs.jsbarcodeMarginLeft, 10) <= 0
	                			? undefined : parseInt(attrs.jsbarcodeMarginLeft, 10))
	                	, marginRight: (isNaN(attrs.jsbarcodeMarginRight)
	                			|| parseInt(attrs.jsbarcodeMarginRight, 10) <= 0
	                			? undefined : parseInt(attrs.jsbarcodeMarginRight, 10))
                	});
				}
			};

	        return {
	            restrict: "A",
	            require: 'ngModel',
	            link: function(scope, element, attrs, ctrls, ngModel) {
	                if (JsBarcode) {
	                	var tagName = ((element && element.length && element[0].tagName)
	                			? element[0].tagName || '' : '');
	                	var id = attrs.id || '';
	                	id = (!id.length ? ('JsBarcode-' + (new Date()).getTime()) : id);
	                	attrs.__barcodeId = id;
	                	if (tagName.toLowerCase() !== 'svg') {
	                		var svgEl = angular.element('<svg id="' + id + '"></svg>');
	                		element.append(svgEl);
	                		$compile(svgEl)(scope);
	                	} else {
	                		element.attr('id', id);
		                	element.attr('name', id);
	                	}
	                	scope.$watch(
	                            function() {
	                            	var val = null;
	                            	try { val = scope.$eval(attrs.ngModel || ''); }
	                            	catch (e) { val = null; }
	                            	return val;
	                            }, function(newValue, oldValue){
	                            	renderBarcode(attrs, newValue);
	                            }, true);
	                } else {
	                	($log && typeof $log.warn === 'function')
	                	&& $log.warn('Could not found JsBarcode library! Please check or download it from http://lindell.me/JsBarcode/#download !!!');
	                }
	            }
	        }
		});