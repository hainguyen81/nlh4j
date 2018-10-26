'use strict';

// service for connecting socket
importService(
		'socketService',
		function($window, $rootScope, $filter, $stomp, $http, csrfService, $timeout, $log) {
			$rootScope.socket = {
					debug: false,
					destroy: function() {
	            		// debug
	            		if ($rootScope.socket.debug && $log) {
	            			$log.info('>>> Attemp cleaning up.......');
	            		}
	            		$.each($rootScope.socket.options, function(k, v) {
	            			v.info.cleanup();
	            		});
	            		$rootScope.socket = {
	        					debug: false,
	        					destroy: null,
	        					/** connection key cache */
	        					connected: {},
	        					/** socket options */
	        					options: {}
	        			};
	            	},
					/** connection key cache */
					connected: {},
					/** socket options */
					options: {}
			};

			/**
			 * Listen destroy for disconnect
			 */
			$rootScope.$on('$beforeUnload', function() {
			    $.each($rootScope.socket.options, function(k, v) {
            		if (v.info && typeof v.info.cleanup === 'function') {
            			$rootScope.socket.debug && $log
            			&& $log.info('>>> Attemp dis-connecting socket for unloading.......');
            			v.info.cleanup();
            		}
			    });
			});

			/**
			 * Send message to server over socket
			 *
			 * @param options: {
			 * 		url: {string},			// destination url
			 * 		data: {json},			// the message content (object or json)
			 * 		headers: {json},		// the request headers (object or json)
			 * 		success: function() {}	// callback function if sending successfully
			 * 		error: function() {}	// callback function if sending fail
			 * }
			 */
			this.onSend = function(options) {
    			// prepare options
				options = options || {};
				var url = options.url;
				var data = options.data || null;
				var headers = $.extend(options.headers || {}, {
					priority: 9,
					custom: 42,
				}, prepareXhrHeaders(null, csrfService));
				var sendSuccess = options.success || function() {
					// debug
            		if ($rootScope.socket.debug && $log) {
            			$log.info('[SOCKET ACTION SUCCESS]: Sent message at [' + (new Date()) + ']');
            		}
				};
				var sendError = options.error || function(e) {
					// debug
            		if ($rootScope.socket.debug && $log) {
            			$log.error('[SOCKET ACTION ERROR]: ' + e);
            		}
				};
				if (url && url.length > 0 && data) {
					$stomp.send(
							// destination
							url,
							// body
							data,
							// headers
							headers
					).then(sendSuccess, sendError);
				}
			};

        	this.onSocket = function(options) {
        		// angularjs http headers configuration
        		var headers = prepareXhrHeaders(null, csrfService);
        		// connection info
        		var id = options.id || '';
            	var connectInfo = options.connectInfo || {};
            	var debug = options.debug || false;
            	var socketUrl = null;
            	var connectHeaders = null;
            	var connectOptions = null;
            	var connectError = null;
            	var connectSuccess = null;
            	if (connectInfo) {
            		socketUrl = connectInfo.url;
            		connectHeaders = connectInfo.headers || { id: id };
                	connectOptions = connectInfo.options || {};
                	connectError = connectInfo.error;
                	connectSuccess = connectInfo.success;

                	// Cross-Site header
    				$.each(headers, function(hname, idx) {
    					if (hname && hname.length > 0
    							&& headers[hname] && headers[hname].length > 0) {
    						connectHeaders[hname] = headers[hname];
    					}
    				});
            	} else {
            		throw new TypeError("Connection information must be defined!");
            	}
            	connectSuccess = (typeof connectSuccess === 'function' ? connectSuccess : function(data) {});
            	connectError = (typeof connectError === 'function' ? connectError : function(reason) {});

            	// subscription info
            	var topicInfo = options.topicInfo || {};
            	var topicDest = null;
            	var topicSubs = null;
            	var topicHeaders = null;
            	if (topicInfo) {
            		topicDest = topicInfo.topic;
            		topicHeaders = topicInfo.headers || {};
            		topicSubs = topicInfo.subscription;
            		if (typeof topicSubs !== 'function') {
            			throw new TypeError("Subscription must be a function!");
            		}

            		// Cross-Site header
            		topicHeaders[id] = id;
    				$.each(headers, function(hname, idx) {
    					if (hname && hname.length > 0
    							&& headers[hname] && headers[hname].length > 0) {
    						topicHeaders[hname] = headers[hname];
    					}
    				});
            	}

            	// create functions
            	var connectionKey = socketUrl;
            	var topicKey = topicDest;
            	var needConnection = false;
            	$rootScope.socket.debug = debug;
            	// check for creating connection handler if necessary
            	if (!$rootScope.socket.options.hasOwnProperty(connectionKey)) {
	            	$rootScope.socket.options[connectionKey] = {
	            			info: {
	            				url: socketUrl
	            				, headers: connectHeaders
	            				, options: connectOptions
	            				, error: connectError
	            				, success: connectSuccess
	            				, connection: null
	            				, connectedAt: null
	            				, connect: null
	            				, disconnect: null
	            			}
	            			, subscriptions: {}
	            	};
	            	needConnection = true;
            	}
            	// check for creating subscription if necessary
            	if (!$rootScope.socket.options[connectionKey].subscriptions.hasOwnProperty(topicDest)) {
	            	$rootScope.socket.options[connectionKey].subscriptions[topicKey] = {
	            			topic: topicDest
	            			, headers: topicHeaders
	            			, callbacks: []
	            			, subscription: null
	            			, internalCallback: null
	            	};
	            	$rootScope.socket.options[connectionKey].subscriptions[topicKey].callbacks.push(topicSubs);
	            	$rootScope.socket.options[connectionKey].subscriptions[topicKey].internalCallback = function(payload, headers, res) {
	            		if (angular.isArray($rootScope.socket.options[connectionKey].subscriptions[topicKey].callbacks)
	            				&& $rootScope.socket.options[connectionKey].subscriptions[topicKey].callbacks.length) {
	            			$.map($rootScope.socket.options[connectionKey].subscriptions[topicKey].callbacks,
	            					function(cb) { cb(payload, headers, res); });
	            		}
	            	};
	            	$rootScope.socket.options[connectionKey].subscriptions[topicKey].subscription = function() {
	            		$stomp.on(
								// detination
		            			$rootScope.socket.options[connectionKey].subscriptions[topicKey].topic,
								// callback
		            			$rootScope.socket.options[connectionKey].subscriptions[topicKey].internalCallback,
								// headers
		            			$rootScope.socket.options[connectionKey].subscriptions[topicKey].headers
						);
	            	};
            	} else {
            		$rootScope.socket.options[connectionKey].subscriptions[topicKey].callbacks.push(topicSubs);
            	}
            	// create connection success handler if necessary
            	if (needConnection) {
	            	$rootScope.socket.options[connectionKey].info.connection = function(data) {
	            		// debug
	            		if ($rootScope.socket.debug && $log) {
	            			$log.info('>>> Attemp socket connection successfully!');
	            		}
	            		// connected time
	            		$rootScope.socket.options[connectionKey].info.connectedAt = new Date();
	            		// callback
	            		$rootScope.socket.options[connectionKey].info.success(data);
	            		// subscription
	            		$rootScope.socket.options[connectionKey].subscriptions[topicDest].subscription();
					};
            	}

            	// connect and waiting for subscripting
            	if (debug && $log && $log.info) $log.info($rootScope.socket);
            	if (needConnection) {
	            	// connection handler
	            	$rootScope.socket.options[connectionKey].info.connect = function() {
	            		if (!$rootScope.socket.connected[connectionKey]) {
		            		// debug
		            		if ($rootScope.socket.debug && $log) {
		            			$log.info('>>> Attemp connecting socket after 5 seconds.......');
		            		}
		            		$rootScope.socket.connected[connectionKey] = true;
		            		var timeout = $timeout(function() {
		            			// debug
			            		if ($rootScope.socket.debug && $log) {
			            			$log.info('>>> Attemp connecting socket.......');
			            		}
			            		var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
			            		$stomp.connect(
										// url
										basepath + socketUrl,
										// headers
										$rootScope.socket.options[connectionKey].info.headers,
										// options
										$rootScope.socket.options[connectionKey].info.options
								).then(
										function(data) {
											$rootScope.socket.options[connectionKey].info.connection(data);
											$timeout.cancel(timeout);
										}
										, function(reason) {
											// debug
						            		if ($rootScope.socket.debug && $log) {
						            			$log.error('>>> Attemp connecting socket error');
						            			$log.error(reason);
						            		}
						            		$rootScope.socket.connected[connectionKey] = false;
											$rootScope.socket.options[connectionKey].info.error(reason);
											$timeout.cancel(timeout);
										});
		            		}, 5000);
	            		}
	            	};
	            	// disconnection handler
	            	$rootScope.socket.options[connectionKey].info.disconnect = function() {
            			// debug
	            		if ($rootScope.socket.debug && $log) {
	            			$log.info('>>> Attemp dis-connecting socket.......');
	            		}
	            		$stomp.disconnect().then(function() {
	            			$rootScope.socket.connected[connectionKey] = false;
		            		$rootScope.socket.options[connectionKey].info.connectedAt = null;
	            			$rootScope.socket.options[connectionKey].info.connect();
	            		});
	            	};
	            	// just disconnect and not re-connect
	            	$rootScope.socket.options[connectionKey].info.cleanup = function() {
	            		// debug
	            		if ($rootScope.socket.debug && $log) {
	            			$log.info('>>> Attemp dis-connecting socket.......');
	            		}
	            		$stomp.disconnect();
	            	};
            	}

            	// perform connection
            	if (needConnection
            			&& (!$rootScope.socket.connected.hasOwnProperty(connectionKey)
            					|| !$rootScope.socket.connected[connectionKey])) {
            		// just call dis-connect for auto connecting socket in 5(s)
	            	$rootScope.socket.options[connectionKey].info.disconnect();
            	}
        	};
        }
);
