importModuleController(
		'S0900',
		'S0901Ctrl',
		function($scope, ajaxService, $rootScope, $controller) {
			/**
			 * Strict for overriding {@link moreMenuOptions} function
			 */
			$scope.strictScope($scope);

			/**
			 * Declare model
			 */
			$scope.model = {
					functions: {
						data: [],
						pagination: {
							pageNumber: 1,
							totalRows: 0,
							limit: 15
						}
					},
					search: {
						keyword: '',
						enabled: null,
					},
					searching: false
			};

			/**
			 * Initialization
			 */
			$scope.onInit = function() {
				// search default page number 1
				$scope.onSearch(1);
			};

			// -------------------------------------------------
			// BASE
			// -------------------------------------------------
			$scope.getData = function() {
				return $scope.model.functions.data;
			};
			$scope.getPagination = function() {
				return $scope.model.functions.pagination;
			};

			// -------------------------------------------------
			// SEARCH
			// -------------------------------------------------
			$scope.isSearching = function() {
				return $scope.model.searching;
			};
			$scope.onSearch = function(page) {
				$scope.model.searching = true;
				ajaxService.post({
		        		url: '/system/function/search',
		        		data: { c: $scope.model.search, p: page },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			if (json && json.data && json.data.length > 0) {
		        				$scope.model.functions = json;
		        			}
		        			else {
		        				$scope.model.functions = {
		        						data: [],
		        						pagination: {
		        							pageNumber: 1,
		        							totalRows: 0,
		        							limit: 10
		        						}
		        				};
		        			}
		        			$scope.model.searching = false;
				        },
				        error: function(data) {
				        	$scope.model.searching = false;
				        }
				});
			};

			// -------------------------------------------------
			// VIEW
			// -------------------------------------------------
			$scope.$on('$clickViewMenu', function(e, args) { $scope.onView(args.data); });
			$scope.onView = function(data) {
				if (!data || isNaN(data.id) || data.id <= 0) return false;
				$scope.postHiddenForm(
						'/system/function/view',
						{ code: data.code });
			};

			// -------------------------------------------------
			// UPDATE
			// -------------------------------------------------
			$scope.$on('$clickUpdateMenu', function(e, args) { $scope.onUpdate(args.data); });
			$scope.onUpdate = function(data) {
				if (!data || isNaN(data.id) || data.id <= 0) return false;
				$scope.postHiddenForm(
						'/system/function/edit',
						{ code: data.code });
			};

			// -------------------------------------------------
			// DELETE
			// -------------------------------------------------
			$scope.$on('$clickDeleteMenu', function(e, args) { $scope.onDelete(args.data); });
			$scope.onDelete = function(data) {
				if (!data || isNaN(data.id) || data.id <= 0) return false;
				ajaxService.post({
					url: '/system/function/delete',
					data: { pk: { code: data.code } },
					success: function(data) {
						$scope.onSearch(1);
					}
		        });
		  		return false;
		    };

			// -------------------------------------------------
			// ASIGN COMPANY
			// -------------------------------------------------
		    $scope.$on('$clickAssignCompanyMenu', function(e, args) { $scope.onAssignCompany(args.data); });
			$scope.onAssignCompany = function(data) {
				if (!data || isNaN(data.id) || data.id <= 0) return false;
				$scope.postHiddenForm(
						'/system/function/assign',
						{ code: data.code });
		  		return false;
		    };
		    $scope.moreMenuOptions = function(data, viewable, insertable, updatable, deletable) {
		    	// if function is not enabled; then no need to assign it for company
		    	if (!data.enabled) return [];
				return [
				        [function ($itemScope) {
				        	return angular.element('.btn-assign-company').attr('title');
				        },
				        function ($itemScope) {
			        		if (insertable || updatable) {
			        			// fire event from context-menu to child scope
			        			$scope.$broadcast('$clickAssignCompanyMenu', { data: data });
			        		}
				        	return false;
				        },
				        function ($itemScope) {
				        	return (insertable || updatable);	// enable menu
				        },
				        function ($itemScope) {
				        	return 'assign-company';	// menu class
				        },
				        function ($itemScope) {
				        	return (insertable || updatable);	// visible menu
				        }]
		        ];
			};
		}
);
