"'use strict';\n"

// directive for socket notification element
importDirective(
		'ngNotification',
		function($window, $document, $timeout, socketService, toaster, $log) {
			return {
				restrict : 'CA',
				link : function(scope, element, attrs, ngModelCtrl) {
					$(function() {
						var timeout = null;
						var title = attrs.ngTitle || '';
						var url = attrs.ngUrl || '/notify';
						var topic = attrs.ngTopic || '/topic/notify';

						// element settings
						element.css('height', element.innerHeight() + 'px');
						element.css('width', element.innerWidth() + 'px');
						var top = angular.element($window).scrollTop()
								+ angular.element($window).height();
						if (angular.element('footer').length > 0) {
							var position = angular.element('footer').offset();
							if (position && top <= position.top
									&& position.top <= top + element.outerHeight()) {
								top = position.top;
							}
						}
						element.css('top', top + 'px');
						element.css('display', 'none');
						element.css('visibility', 'hidden');

						// bind window resize
						scope.$on('$resize', function() {
							if (element.css('display') != 'none') {
								var top = (angular.element(this).scrollTop()
										+ angular.element(this).height()
										- element.outerHeight() - 25);
								if (angular.element('footer').length > 0) {
									var position = angular.element('footer').offset();
									if (position.top <= angular.element(this).scrollTop()
											+ angular.element(this).height()) {
										top = (position.top - element.outerHeight() - 5);
									}
								}
								element.css('top', top + 'px');
							}
						});
						// bind window scroll
						scope.$on("$scroll", function(e) {
							if (element.css('display') != 'none') {
								var top = (angular.element(this).scrollTop()
										+ angular.element(this).height()
										- element.outerHeight() - 25);
								if (angular.element('footer').length > 0) {
									var position = angular.element('footer').offset();
									if (position.top <= angular.element(this).scrollTop()
											+ angular.element(this).height()) {
										top = (position.top - element.outerHeight() - 5);
									}
								}
								element.css('top', top + 'px');
							}
						});

						//opensocket
						socketService.onSocket({
							id: 'ngNotification',
							debug: true,
							connectInfo:{
								url: url,
								headers: {},
								options: { transports:["xhr-streaming"], },
								error: function(reason) {
									// FIXME Because socket could not keep every refreshing page;
									// so no need show error in this case
									// toaster.show({ type: 4, body: reason, });
								},
								success: function(data) {},
							},
							topicInfo: {
								topic: topic,
								subscription: function(payload, headers, res){
									var message = payload.data;
									($log && typeof $log.info === 'function')
									&& $log.info('>>>>>>> Payload:');
									($log && typeof $log.info === 'function')
									&& $log.info(payload)
									if (message && message.length > 0) {
										message = ((title && title.length)
												? ('<b>' + title + '</b><br>' + message) : message);
										var type = payload.type;
										if (isNaN(type)) type=0;
										element.removeClass('success').removeClass('info')
											.removeClass('warning').removeClass('error')
											.removeClass('wait');
										switch(type) {
											case 1: {
												element.addClass('success');
												break;
											}
											case 2: {
												element.addClass('info');
												break;
											}
											case 3: {
												element.addClass('warning');
												break;
											}
											case 4: {
												element.addClass('error');
												break;
											}
											case5: {
												element.addClass('wait');
												break;
											}
										}
										element.html(message);
										element.css('width', 'auto');
										element.css('height', 'auto');
										element.css('display', 'block');
										element.css('visibility', 'visible');

										var top = (angular.element($window).scrollTop()
												+ angular.element($window).height()
												- element.outerHeight());
										// padding bottom with the window bottom
										top -= 25;
										if (angular.element('footer').length > 0) {
											var position = angular.element('footer').offset();
											if (position && top <= position.top
													&& position.top <= top + element.outerHeight()) {
												top = (position.top - element.outerHeight());
												// padding bottom with footer
												top -= 10;
											}
										}

										// show animate
										element.animate({ top: top, }, 'slow', function() {
											timeout = $timeout(function() {
												element.css('height', element.innerHeight() + 'px');
												element.css('width', element.innerWidth() + 'px');
												var top = angular.element($window).scrollTop()
														+ angular.element($window).height();
												if (angular.element('footer').length > 0) {
													var position = angular.element('footer').offset();
													if (position && top <= position.top
															&& position.top <= top + element.outerHeight()) {
														top = position.top;
													}
												}
												element.css('top', top + 'px');
												element.css('display', 'none');
												element.css('visibility', 'hidden');
												element.html('');
												$timeout.cancel(timeout);
											}, 10000);
										});

										// raise notification
					        			scope.$broadcast('$notification',
					        					{ payload: payload, headers: headers, response: res });
									}
								},
							},
						});
					});
				}
			};
		});

//directive for [chat] element
importDirective(
		'ngChat',
		function($window, $http) {
			return {
				restrict : 'CA',
				link : function(scope, element, attrs, ngModelCtrl) {
					$(function() {
						// bind window resize
						scope.$on('$resize', function() {
							if (element.css('display') != 'none') {
								var top = (angular.element(this).scrollTop()
										+ angular.element(this).height()
										- element.outerHeight() - 25);
								if (angular.element('footer').length > 0) {
									var position = angular.element('footer').offset();
									if (position.top <= angular.element(this).scrollTop()
											+ angular.element(this).height()) {
										top = (position.top - element.outerHeight() - 5);
									}
								}
								element.css('top', top + 'px');
								element.css('left', (angular.element(this).scrollLeft()
										+ angular.element(this).width()
										- element.outerWidth() - 25) + 'px');
							}
						});
						// bind window scroll
						scope.$on("$scroll", function(e) {
							if (element.css('display') != 'none') {
								var top = (angular.element(this).scrollTop()
										+ angular.element(this).height()
										- element.outerHeight() - 25);
								if (angular.element('footer').length > 0) {
									var position = angular.element('footer').offset();
									if (position.top <= angular.element(this).scrollTop()
											+ angular.element(this).height()) {
										top = (position.top - element.outerHeight() - 5);
									}
								}
								element.css('top', top + 'px');
								element.css('left', (angular.element(this).scrollLeft()
										+ angular.element(this).width()
										- element.outerWidth() - 25) + 'px');
							}
						});

						// default element position
						element.css('height', element.innerHeight() + 'px');
						element.css('width', element.innerWidth() + 'px');
						var top = (angular.element($window).scrollTop()
								+ angular.element($window).height()
								- element.outerHeight() - 25);
						if (angular.element('footer').length > 0) {
							var position = angular.element('footer').offset();
							if (position && top <= position.top
									&& position.top <= top + element.outerHeight()) {
								top = (position.top - element.outerHeight() - 5);
							}
						}
						element.css('top', top + 'px');
						element.css('left', (angular.element($window).scrollLeft()
								+ angular.element($window).width()
								- element.outerWidth() - 25) + 'px');
						element.css('display', 'block');
						element.css('visibility', 'visible');

						// bind element click
						var basepath = (typeof parseBasePath === 'function' ? parseBasePath($http) : '');
						element.click(function() {
							jDialoguePost((basepath + '/socket/chat'), {
								dataType : 'html',
								cache : false,
							}, {
								id : 'chatViewer',
								// handle for dragging
								handle : '.ui-draggable-handle,.ui-draggable',
								width : '14.53cm',
								modal : false,
								position : {
									my : 'right bottom',
									at : 'right bottom',
									of : window
								},
								resizable : false,
								startup : 0,
								open : function(event, ui) {
									element.css('display', 'none');
									element.css('visibility', 'hidden');
								},
								afterClose : function(result) {
									element.css('display', 'block');
									element.css('visibility', 'visible');
								},
								hide : function(result) {
									element.css('display', 'block');
									element.css('visibility', 'visible');
								},
								show : function(result) {
									element.css('display', 'none');
									element.css('visibility', 'hidden');
									element.removeClass('blinking');
								},
								error : function(xhr, status, errorThrown) {
									// TODO error
									;
								},
							});
						});
						element.click();
					});
				}
			};
		});