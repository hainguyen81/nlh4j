importModuleController(
		'C2000',
		'C2000Ctrl',
		//function($window, $scope, ajaxService, $rootScope, socketService, $window) {
		function($window, $scope, ajaxService, $rootScope, $window) {
			$scope.model = { modules: [] };
			$scope.onInit = function(pid) {
				$scope.onViewSettings();
				$scope.onModule(isNaN(pid) ? 0 : pid);
			};

			/*************************************************
			 * LOAD MODULES
			 *************************************************/
			$scope.onModule = function(pid) {
				ajaxService.post({
		        		url: '/dashboard/search',
		        		data: { c: { mid: pid }, p: 1 },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			if (json && $.isArray(json.data)) {
								// clears screen
								var gridsterItems = $('.gridster-item');
								if (gridsterItems.length > 0) {
									gridsterItems.each(function() {
										$(this).remove();
									});
								}

		        				// shows new items
		        				$scope.model.modules = json.data;
		        			}
				        }
		        });
			};

			/*************************************************
			 * VIEW SETTINGS
			 *************************************************/
			$scope.onViewSettings = function() {
				$scope.gridsterOptions = {
						margins: [0, 0],
						columns: 36,
						minSizeX: 1,
						draggable: {
							handle: '.panel-heading',
						},
						resizable: {
							enabled: true,
							handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
							// optional callback fired when item is resized,
							resize: $scope.gridsterResize,
					    },
				};
			};
			/**
			 * Handle widget resize
			 */
			$scope.gridsterResize = function(event, $element, widget) {
				// common resize
				var width = $element.innerWidth();
				var height = $element.innerHeight();
				var maincontainer = $element.find('.panel.panel-primary');
				if (maincontainer) {
					// resize main container
					maincontainer.css('width', width + 'px');
					maincontainer.css('height', height + 'px');
				}
			};
		}
);