'use strict';

/**
 * Factory for handling some events of window
 */
importFactory(
		'$winEvents',
		function ($rootScope, $window, $log, $interval) {
			// Events are broadcast outside the Scope Lifecycle
			var beforeUnload = function (e) {
				($log && typeof $log.info === 'function')
				&& $log.info('>>> window will be unloaded...');
				e = (($window || []).length ? $window[0].event : e);
				var confirmation = {};
				var event = $rootScope.$broadcast('$beforeUnload', confirmation);
				if (event.defaultPrevented && ((confirmation || {}).message || '').length) {
					var userAgent = ($window.navigator || {}).userAgent || '';
					var firefox = ((/firefox/i).test(userAgent) || ($.browser && $.browser.firefox));
					if (firefox) {
						// Add custom dialog
						// Firefox does not accept window.showModalDialog(), window.alert(), window.confirm(), and window.prompt() furthermore
						var dialog = document.createElement("div");
						document.body.appendChild(dialog);
						dialog.id = "dialog";
						dialog.style.visibility = "hidden";
						dialog.innerHTML = confirmation.message;
						var left = (document.body.clientWidth / 2 - dialog.clientWidth / 2);
						dialog.style.left = left + "px";
						dialog.style.visibility = "visible";
						var shadow = document.createElement("div");
						document.body.appendChild(shadow);
						shadow.id = "shadow";
						//tip with setTimeout
						setTimeout(function () {
							document.body.removeChild(document.getElementById("dialog"));
							document.body.removeChild(document.getElementById("shadow"));
						}, 0);
					}
					if (e) e.returnValue = confirmation.message;
					return confirmation.message;
				}
			};
			$window.onbeforeunload = beforeUnload;
			//angular.element($window).on('beforeunload', beforeUnload);

			// bind window unload
			var unloadFn = function (e) {
				($log && typeof $log.info === 'function')
				&& $log.info('>>> window has been unloaded...');
				e = (($window || []).length ? $window[0].event : e);
				$rootScope.$broadcast('$unload', e);
			};
			$window.onunload = unloadFn;
			//angular.element($window).on('unload', unloadFn);

			// bind window load
			var loadFn = function(e) {
				// debug
				($log && typeof $log.info === 'function')
				&& $log.info('>>> window will be loaded...');

				// turn on backdrop
				$rootScope.__backdropOnLoad = true;
				(typeof turnBackdrop == 'function')
				&& turnBackdrop(true);

				// fire event
				$rootScope.$broadcast('$load', e);
			};
			$window.onload = loadFn;
			//angular.element($window).on('load', loadFn);

			// bind window resize
			var resizeFn = function(e) {
				($log && typeof $log.info === 'function')
				&& $log.info('>>> window will be resized...');
				$rootScope.$broadcast('$resize', e);
			};
			$window.onresize = resizeFn;
			//angular.element($window).on('resize', resizeFn);

			// bind window resize
			var resizeBodyFn = function(e) {
				($log && typeof $log.info === 'function')
				&& $log.info('>>> body will be resized...');
				$rootScope.$broadcast('$resize', e);
			};
			var resizeBodyWatch = $rootScope.$watch(
					function () {
						return ( angular.element('body').prop('offsetWidth')
								+ ',' + angular.element('body').prop('offsetHeight') );
					},
					function (newValue, oldValue) {
						// fire event
						if (newValue !== oldValue) resizeBodyFn(null);
					}
			);

			// bind window scroll
			var scrollFn = function(e) {
				($log && typeof $log.info === 'function')
				&& $log.info('>>> window will be scrolled...');
				$rootScope.$broadcast('$scroll', e);
			};
			$window.onscroll = scrollFn;
			//angular.element($window).on("scroll", scrollFn);

			// FIXME Bad performance
			//	// bind body DOM changed
			//	var changedDOM = function() {
			//		($log && typeof $log.info === 'function')
			//		&& $log.info('>>> body content has been changed...');
			//		$rootScope.$broadcast('$changedDOM');
			//	};
			//	// watch for changed DOM
			//	var unwatch = $rootScope.$watch(
			//			function () { return angular.element('body').html(); },
			//			function (newValue, oldValue) {
			//				// fire event
			//				if (newValue !== oldValue) changedDOM();
			//			}
			//	);

			// bind scope digest
			$rootScope.$$internalDisgest = $rootScope.$digest;
			$rootScope.__backdropOnDigest = false;
			$rootScope.$digest = function() {
				// debug
				//	($log && typeof $log.info === 'function')
				//	&& $log.info('>>> Scope has been digested...');
				if ($rootScope.__offline !== true) {
					// show sprinner for digest
					if ($rootScope.__backdropOnDigest !== true) {
						$rootScope.__backdropOnDigest = true;
						(typeof turnBackdrop == 'function')
						&& turnBackdrop(true);
					}

					// digest
					try {
						$rootScope.$$internalDisgest();
						$rootScope.$broadcast('$$digest');
					} catch (e) {
						($log && typeof $log.error === 'function')
						&& $log.error('>>> Error:', e);
					}

					// turn off backdrop
					if (($rootScope.__backdropOnLoad == true || $rootScope.__backdropOnDigest == true)
							&& $rootScope.__checkServer !== true) {
						(typeof turnBackdrop == 'function')
						&& turnBackdrop(false);
						$rootScope.__backdropOnLoad = false;
						$rootScope.__backdropOnDigest = false;
					}

					// continue for checking server
				} else {
					$rootScope.$$internalDisgest();
				}
			};

			// Custom $off function to un-register the listener.
	        $rootScope.$off = function(name, listener) {
	            var listeners = this.$$listeners[name];
	            if (angular.isArray(listeners) && listeners.length > 0) {
	                // Loop through the array of named listeners and remove them from the array.
	                for (var i = 0; i < listeners.length; i++) {
	                    if (listeners[i] === listener) {
	                        return listeners.splice(i, 1);
	                    }
	                }
	            }
	            return [];
	        };

	        // check server has been down
	        $rootScope.__offline = false;
	        if (SERVER_CHECKING || false) {
		        var offline = angular.element($window)[0].Offline;
		        var __offline_handler = function() {
		        	($log && typeof $log.info === 'function')
		        	&& $log.info('Server has been down!!!');
		        	$rootScope.__offline = true;
		        	(typeof turnOffline === 'function')
		        	&& turnOffline(true);
		        	$rootScope.$broadcast('$offline');
		        };
		        var __online_handler = function() {
		        	($log && typeof $log.info === 'function')
		        	&& $log.info('Server has been up!!!');
		        	$rootScope.__offline = false;
		        	(typeof turnOffline === 'function')
		        	&& turnOffline(false);
		        	$rootScope.$broadcast('$online');
		        };
		        var __interval = null;
		        var offlineUrl = (typeof parseMetaData === 'function'
	        		? parseMetaData('_offline_checker')
	        				: angular.element('meta[name="_offline_checker"]').attr('content'));
	        	if (!(offlineUrl || '').length) {
	        		offlineUrl = angular.element('link[rel="shortcut icon"]').attr('href');
	        	}
	        	if (offline && (offlineUrl || '').length) {
		        	offline.options = {
		        			checks: {
		        				xhr: {
		        					url: function() {
		        						return offlineUrl + '?_=' + ((new Date()).getTime());
		        					},
		        					timeout: 5000,
		        					type: 'HEAD'
		        				},
		        				image: {
		        					url: function() {
		        						return offlineUrl + '?_=' + ((new Date()).getTime());
		        					}
		        				},
		        				active: 'xhr'
		        			},
		        			checkOnLoad: false,
		        			interceptRequests: true,
		        			reconnect: false,
		        			deDupBody: false
		        	};
		        	offline.on('up', __online_handler);
		        	offline.on('down', __offline_handler);
		        	__interval = $interval(function() {
		        		($log && typeof $log.info === 'function')
			        	&& $log.info('Check server connection!!!');
		        		$rootScope.__checkServer = true;
		        		offline.check();
		        		$rootScope.__checkServer = false;
		        	}, (SERVER_CHECKING_INTERVAL || 5000), 0);
		        };
	        }

			// destroy
			$rootScope.$on("$destroy", function() {
				// turn off interval/offline checking
				SERVER_CHECKING && __interval && $interval.cancel(__interval);
				SERVER_CHECKING && offline && offline.off('up', __online_handler);
				SERVER_CHECKING && offline && offline.off('down', __offline_handler);
				//	// unwatch
				//	unwatch();
				resizeBodyWatch();
				// off events
				$window.onresize = null;
				angular.element($window).off('resize', resizeFn);
				$window.onscroll = null;
				angular.element($window).off("scroll", scrollFn);
				$window.onunload = unloadFn;
				angular.element($window).off('unload', unloadFn);
				$window.onload = null;
				angular.element($window).off('load', loadFn);
				$window.onbeforeunload = null;
				angular.element($window).off('beforeunload', beforeUnload);
			});
			return {};
		});
//Must invoke the service at least once
run(function ($winEvents) {});

/**
 * A set of utility methods that can be use to retrieve position of DOM elements.
 * It is meant to be used where we need to absolute-position DOM elements in
 * relation to other, existing elements (this is the case for tooltips, popovers,
 * typeahead suggestions etc.).
 */
importFactory(
		'$position',
		function ($document, $window) {
			/**
			 * Get element style
			 *
			 * @param el to parse
			 * @param cssprop style property
			 *
			 * @return element style
			 */
			function getStyle(el, cssprop) {
				if (el && el.currentStyle) { //IE
					return el.currentStyle[cssprop];
				} else if (el && $window.getComputedStyle) {
					return $window.getComputedStyle(el)[cssprop];
				}
				// finally try and get inline style
				return (el ? el.style[cssprop] : '');
			};

			/**
			 * Checks if a given element is statically positioned
			 *
			 * @param element - raw DOM element
			 *
			 * @return true for static position; else false
			 */
			function isStaticPositioned(element) {
				return ((getStyle(element, 'position') || 'static' ) === 'static');
			};

			/**
			 * returns the closest, non-statically positioned parentOffset of a given element
			 *
			 * @param element to parse parent
			 */
			var parentOffsetEl = function (element) {
				var docDomEl = $document[0];
				var offsetParent = (element ? element.offsetParent : null) || docDomEl;
				while (offsetParent && offsetParent !== docDomEl && isStaticPositioned(offsetParent) ) {
					offsetParent = offsetParent.offsetParent;
				}
				return (offsetParent || docDomEl);
			};

			return {
				/**
				 * Provides read-only equivalent of jQuery's position function:
				 * http://api.jquery.com/position/
				 *
				 * @param element to get position
				 *
				 * @return element's position (width, height, top, left)
				 */
				position: function (element) {
					var valid = (element && element.length);
					var elBCR = (valid ? this.offset(element) : null);
					var offsetParentBCR = { top: 0, left: 0 };
					var offsetParentEl = (valid ? parentOffsetEl(element[0]) : null);
					if (offsetParentEl && offsetParentEl != $document[0]) {
						offsetParentBCR = this.offset(angular.element(offsetParentEl));
						offsetParentBCR.top += offsetParentEl.clientTop;
						offsetParentBCR.left += offsetParentEl.clientLeft;
					}

					return {
						width: (valid ? element.prop('offsetWidth') : 0),
						height: (valid ? element.prop('offsetHeight') : 0),
						top: (valid ? (elBCR.top - offsetParentBCR.top) : 0),
						left: (valid ? (elBCR.left - offsetParentBCR.left) : 0)
					};
				},

				/**
				 * Provides read-only equivalent of jQuery's offset function:
				 * http://api.jquery.com/offset/
				 *
				 * @param element to get offset
				 *
				 * @return element's offset (width, height, top, left)
				 */
				offset: function (element) {
					var valid = (element && element.length);
					var boundingClientRect = (valid ? element[0].getBoundingClientRect() : null);
					return {
						width: (valid ? element.prop('offsetWidth') : 0),
						height: (valid ? element.prop('offsetHeight') : 0),
						top: (valid ? boundingClientRect.top + $window.pageYOffset : 0),
						left: (valid ? boundingClientRect.left + $window.pageXOffset : 0)
					};
				}
			};
		});

// factory for importing data from outside into angular scope
importFactory(
		'importData',
		function($window) {
			var impData = {};
		    var scopes = [];

		    $window.import = function(key, data) {
		    	impData[key] = data;
		        angular.forEach(scopes, function(scope) {
		            scope.$digest();
		        });
		    };

		    return {
		        data: impData,
		        register: function(scope) { scopes.push(scope); }
		    };
		});

// factory for recursively
importFactory(
		'recursionHelper',
		function($compile) {
			return {
				/**
				 * Manually compiles the element, fixing the recursion loop.
				 * @param element
				 * @param [link] A post-link function, or an object with function(s) registered via pre and post properties.
				 * @returns An object containing the linking functions.
				 */
				compile: function(element, link) {
					// Normalize the link parameter
					if (angular.isFunction(link)){
						link = { post: link };
					}

				    // Break the recursion loop by removing the contents
				    var contents = element.contents().remove();
				    var compiledContents = false;
				    return {
				        pre: (link && link.pre) ? link.pre : null,
				        /**
				         * Compiles and re-adds the contents
				         */
				        post: function(scope, element){
				            // Compile the contents
				            if (!compiledContents) {
				                compiledContents = $compile(contents);
				            }
				            // Re-add the compiled contents to the element
				            compiledContents(scope, function(clone) {
				                element.append(clone);
				            });

				            // Call the post-linking function, if any
				            if(link && link.post){
				                link.post.apply(null, arguments);
				            }
				        }
				    };
				}
			};
		});

// factory for reading file
importFactory(
		'fileReader',
		function($q, $log) {
			 var onLoad = function(reader, deferred, scope) {
				 return function() {
					 scope.$apply(
							 function() {
								 deferred.resolve(reader.result);
							 }
					 );
				 };
			 };
			 var onError = function(reader, deferred, scope) {
				 return function() {
					 scope.$apply(
							 function() {
								 deferred.reject(reader.result);
							 }
					 );
				 };
			 };
			 var onProgress = function(reader, scope) {
				 return function(event) {
					 scope.$broadcast(
							 'fileProgress',
							 {
								 total: event.total,
								 loaded: event.loaded,
							 }
					 );
				 };
			 };
			 var getReader = function(deferred, scope) {
				 var reader = new FileReader();
				 reader.onload = onLoad(reader, deferred, scope);
				 reader.onerror = onError(reader, deferred, scope);
				 reader.onprogress = onProgress(reader, scope);
				 return reader;
			 };
			 var readAsDataURL = function(file, scope) {
				 var deferred = $q.defer();
				 var reader = getReader(deferred, scope);
				 reader.readAsDataURL(file);
				 return deferred.promise;
			 };
			 return {
				 readAsDataUrl: readAsDataURL
			 };
		 });
