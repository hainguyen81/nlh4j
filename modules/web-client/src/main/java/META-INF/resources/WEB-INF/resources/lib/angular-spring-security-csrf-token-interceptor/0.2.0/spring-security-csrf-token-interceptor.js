/*
 * Copyright 2014 Allan Ditzel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * spring-security-csrf-token-interceptor
 *
 * Sets up an interceptor for all HTTP requests that adds the CSRF Token Header that Spring Security requires.
 */
(function() {
    'use strict';
    angular.module('spring-security-csrf-token-interceptor', [])
        .factory('csrfInterceptor', ['$injector', '$q', '$window',
            function($injector, $window) {
                var $q = $injector.get('$q'),
                    csrf = $injector.get('csrf'),
                    // initialize the csrf provider service which in-turn invokes the injected csrfService
                    // to fire the synchronous XHR call to get the CSRF token
                    csrfService = csrf.init();

                return {
                    request: function(config) {
                    	// intercept HTTP request only for the configured HTTP types
                        if (csrfService && typeof csrfService.settings === 'object'
                        	&& csrfService.token && csrfService.token.length > 0
                        	&& !csrfService.isExcluded()
                        	&& csrfService.settings.httpTypes.indexOf(config.method.toUpperCase()) > -1) {
                        	// apply request cookie
                        	var ckhname = null;
                        	if (typeof csrfService.settings.csrfCookie === 'function') {
                        		ckhname = csrfService.settings.csrfCookie.call();
                        	}
                        	else if (typeof csrfService.settings.csrfCookie === 'string') {
                        		ckhname = csrfService.settings.csrfCookie;
                        	}

                        	// if cookie and current scrf token is difference
                        	// then maybe timeout or server has been restart;
                        	// so retry koten one more time
                        	var cookie = csrfService.getCookie(ckhname);
                        	if (cookie && cookie.length > 0 && cookie !== csrfService.token
                        			&& csrfService.settings.maxRetries <= 0) {
                        		csrfService.getTokenData();
                        	}
                            if (ckhname && ckhname.length > 0) {
                            	config.headers('Cookie', ((ckhname + '=') + csrfService.token));
                            }

                        	// apply request headers
                        	var hname = null;
                        	if (typeof csrfService.settings.csrfTokenHeader === 'function') {
                        		hname = csrfService.settings.csrfTokenHeader.call();
                        	}
                        	else if (typeof csrfService.settings.csrfTokenHeader === 'string') {
                        		hname = csrfService.settings.csrfTokenHeader;
                        	}
                        	if (hname && hname.length > 0) {
                        		config.headers[hname] = csrfService.token;
                        	}

                        	// apply page header
                        	applyTokenHeader && applyTokenHeader(csrfService.token);
                        	(ckhname && ckhname.length > 0) && csrfService.deleteCookie(ckhname, csrfService.settings.url);
                        	(ckhname && ckhname.length > 0) && csrfService.setCookie(ckhname, csrfService.token, 7, csrfService.settings.url);
                        }
                        return config || $q.when(config);
                    },
                    responseError: function(response) {
                        var $http,
                            newToken = (response.headers ? response.headers(csrfService.settings.csrfTokenHeader) : null);

                        if (response.status === 403 && csrfService.numRetries < csrfService.settings.maxRetries) {
                            csrfService.getTokenData();
                            $http = $injector.get('$http');
                            csrfService.numRetries = csrfService.numRetries + 1;
                            return $http(response.config);

                            // unauthorized
                        } else if (response.status === 401
                        		&& csrfService.settings.loginUrl
                        		&& csrfService.settings.loginUrl.length > 0) {
                        	$window.location.href = csrfService.settings.loginUrl;
                        	return;

                        } else if (newToken) {
                            // update the csrf token in-case of response errors other than 403
                            csrfService.token = newToken;
                        }
                        // Fix for interceptor causing failing requests
                        return $q.reject(response);
                    },
                    response: function(response) {
                        // reset number of retries on a successful response
                        csrfService.numRetries = 0;
                        return response;
                    }
                };
            }
        ]).factory('csrfService', [ '$log', '$window',

            function($log, $window) {
                var defaults = {
                    url: '/', // the URL to which the CSRF call has to be made to get the token
                    csrfHttpType: 'head', // the HTTP method type which is used for making the CSRF token call
                    maxRetries: 5, // number of retires allowed for forbidden requests
                    csrfTokenHeader: 'X-CSRF-TOKEN',
                    httpTypes: ['GET', 'HEAD', 'PUT', 'POST', 'DELETE'], // default allowed HTTP types
                	excludes: [],
                	headers: [],
                	csrfCookie: null,
                	loginUrl: null,	// login path in 401 status
                	error: null // function(xhr, status, statusText, response)
                };
                return {
                    inited: false,
                    settings: null,
                    numRetries: 0,
                    token: '',
                    init: function(options) {
                        this.settings = angular.extend({}, defaults, options);
                        this.getTokenData();
                    },
                    isExcluded: function() {
                    	var found = false;
                    	if (this.settings.excludes && this.settings.excludes.length > 0) {
                    		for(var exurl in this.settings.excludes) {
                    			found = (exurl == this.settings.url
                    					|| (exurl && exurl.length > 0 && this.settings.url.match(exurl)));
                    			if (found) break;
                    		}
                    	}
                    	return found;
                    },
                    getCookie: function(name) {
                    	var value = "; " + document.cookie;
                    	var parts = value.split("; " + name + "=");
                    	if (parts.length == 2) return parts.pop().split(";").shift();
                    	return null;
              		},
                    setCookie: function(name, value, exdays, path) {
                    	var d = new Date();
                        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
                        var expires = "expires=" + d.toUTCString();
                       	document.cookie = name + "=" + value + "; " + expires + '; path=' + path;
                       	($log && typeof $log.debug === 'function')
                       	&& $log.debug(document.cookie);
              		},
                    deleteCookie: function(name, path) {
                    	setCookie(name, '', -1, path);
              		},
                    getTokenData: function() {
                    	var found = false;
                    	if (this.settings.excludes && this.settings.excludes.length > 0) {
                    		for(var exurl in this.settings.excludes) {
                    			found = (exurl == this.settings.url
                    					|| (exurl && exurl.length > 0 && this.settings.url.match(exurl)));
                    			if (found) break;
                    		}
                    	}
                    	if (!found) {
                    		// creates XHR request
	                    	var xhr = new XMLHttpRequest();
	                    	xhr.open(this.settings.csrfHttpType, this.settings.url, false);

	                    	// parse headers
	                    	var headers = {};
	                    	if (typeof this.settings.headers === 'object'
	                    		&& Object.keys(this.settings.headers).length > 0) {
	                    		headers = this.settings.headers;
	                    	}
	                    	else if (typeof this.settings.headers === 'function') {
	                    		headers = this.settings.headers.call();
	                    	}
	                    	if (typeof headers === 'object'
	                    		&& Object.keys(headers).length > 0) {
	                    		var keys = Object.keys(headers);
	                    		for(var i = 0; i < keys.length; i++) {
	                    			var key = keys[i];
	                    			var value = headers[key];
	                    			if (key && key.length > 0 && value && value.length > 0) {
                                		switch(key) {
                                			case 'withCredentials':
                                				try { xhr.withCredentials = value; } catch(e) {}
                                				break;
                                			case 'useXDomain':
                                				try { xhr.useXDomain = value; } catch(e) {}
                                				break;
                            				default:
                            					xhr.setRequestHeader(key, value);
                                		}
	                    			}
	                    		}
	                    	}
	                    	try { xhr.send(); }
	                    	catch (e) {}

	                    	// check for unauthorized
	                    	if (xhr && xhr.status == 401
	                    			&& this.settings.loginUrl
	                    			&& this.settings.loginUrl.length > 0) {
	                    		$window.location.href = this.settings.loginUrl;
	                    		return;
	                    	} else if (!xhr || xhr.status < 200 || xhr.status >= 300
	                    			&& typeof this.settings.error === 'function') {
	                    		this.settings.error(
	                    				xhr, // XHR
	                    				(xhr == null ? 0 : xhr.status),	// status
	                    				(xhr == null ? null : xhr.statusText), // status text
	                    				(xhr == null ? null : xhr.response) // response
                				);
	                    		return;
	                    	}

	                    	// parse header
	                        var respHeader = null;
	                    	if (typeof this.settings.csrfTokenHeader === 'function') {
	                    		respHeader = this.settings.csrfTokenHeader.call();
	                    	}
	                    	else {
	                    		respHeader = this.settings.csrfTokenHeader;
	                    	}
	                        this.token = xhr.getResponseHeader(respHeader);
	                        this.inited = true;
                    	}
                    }
                };

            }
        ]).provider('csrf', [

            function() {
                var CsrfModel = function CsrfModel(options) {
                    return {
                        options: options,
                        csrfService: null
                    };
                };

                return {
                    $get: ['csrfService',
                        function(csrfService) {
                            var self = this;
                            return {
                                init: function() {
                                    self.model = new CsrfModel(self.options);
                                    self.model.csrfService = csrfService;
                                    self.model.csrfService.init(self.model.options);
                                    return self.model.csrfService;
                                }
                            };
                        }
                    ],

                    model: null,

                    options: {},

                    config: function(options) {
                        this.options = options;
                    }
                };
            }
        ]).config(['$httpProvider',
            function($httpProvider) {
                $httpProvider.interceptors.push('csrfInterceptor');
            }
        ]);
}());