/**
 * ngStomp
 *
 * @version 0.0.1-alpha.1
 * @author Maik Hummel <yo@beevelop.com>
 * @license WTFPL
 */
(function(angular) {
	'use strict';

	angular
	    .module('ngStomp', [])
	    .service('$stomp', [
	        '$rootScope', '$q', '$window',
	        function ($rootScope, $q, $window) {

	            this.sock = null;
	            this.stomp = null;
	            this.debug = null;

	            this.checkHeaders = function(headers) {
	            	var optHeaders = $.extend(headers || {}, {
	                	'Access-Control-Allow-Origin': '*',
	                	'Access-Control-Allow-Headers': 'Content-Type',
	                	'Access-Control-Allow-Credentials': 'true',
	                	'X-Requested-With': 'XMLHttpRequest',
	                });
	                return optHeaders;
	            };

	            this.setDebug = function(callback) {
	                this.debug = callback;
	            };

	            this.connect = function (endpoint, headers, options) {
	            	headers = this.checkHeaders(headers);
	                options = $.extend(options || {}, {
	                	debug: true,
	                	headers: headers,
	                });
	                if (!options.transports || options.transports.length <= 0) {
	                	options = $.extend(options, {
	                		transports: ["websocket", "xhr-streaming", "xdr-streaming", "xhr-polling", "xdr-polling", "iframe-htmlfile", "iframe-eventsource", "iframe-xhr-polling"],
	                	});
	                }

	                var dfd = $q.defer();

	                this.sock = new SockJS(endpoint, {}, options);
	                this.stomp = Stomp.over(this.sock);
	                this.stomp.debug = this.debug;
	                this.stomp.connect(options.headers, function (frame) {
	                    dfd.resolve(frame);
	                }, function (err) {
	                    dfd.reject(err);
	                });

	                return dfd.promise;
	            };

	            this.isOpened = function() {
	            	return (this.sock != null && this.sock.isOpened());
	            };

	            this.disconnect = function () {
	                var dfd = $q.defer();
	                if (this.stomp != null) {
	                	this.stomp.disconnect(dfd.resolve);
	                } else if (dfd.resolve) {
	                	dfd.resolve();
	                }
	                return dfd.promise;
	            };

	            this.subscribe = this.on = function (destination, callback, headers) {
	            	headers = this.checkHeaders(headers);
	                return this.stomp.subscribe(destination, function (res) {
	                    var payload = null;
	                    try {
	                        payload = JSON.parse(res.body);
	                    } finally {
	                        if (callback) {
	                            callback(payload, res.headers, res);
	                        }
	                    }
	                }, headers);
	            };

	            this.unsubscribe = this.off = function (subscription) {
	                subscription.unsubscribe();
	            };

	            this.send = function (destination, body, headers) {
	                var dfd = $q.defer();
	                try {
	                    var payloadJson = JSON.stringify(body);
	                    headers = this.checkHeaders(headers);
	                    this.stomp.send(destination, headers, payloadJson);
	                    dfd.resolve();
	                } catch (e) {
	                    dfd.reject(e);
	                }
	                return dfd.promise;
	            };
	        }]
	);
})(angular);