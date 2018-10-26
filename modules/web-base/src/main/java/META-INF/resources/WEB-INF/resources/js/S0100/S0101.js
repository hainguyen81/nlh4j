importModuleController(
		'S0100',
		'S0101Ctrl',
		function($scope, ajaxService, $rootScope) {
			/**
			 * Declare model
			 */
			$scope.model = {
					roles: {
						data: [],
						pagination: {
							pageNumber: 1,
							totalRows: 0,
							limit: 15
						}
					},
					search: null,
					searching: false
			};

			/**
			 * Initialization
			 */
			$scope.onInit = function() {
				$scope.onSearch(1);
			};

			// -------------------------------------------------
			// BASE
			// -------------------------------------------------
			$scope.getData = function() {
				return $scope.model.roles.data;
			};
			$scope.getPagination = function() {
				return $scope.model.roles.pagination;
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
		        		url: '/system/role/search',
		        		data: { c: { keyword: $scope.model.search }, p: page },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			if (json && json.data && json.data.length > 0) {
		        				$scope.model.roles = json;
		        			}
		        			else {
		        				$scope.model.roles = {
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
				$scope.postHiddenForm('/system/role/view', { code: data.code });
			};

			// -------------------------------------------------
			// UPDATE
			// -------------------------------------------------
			$scope.$on('$clickUpdateMenu', function(e, args) { $scope.onUpdate(args.data); });
			$scope.onUpdate = function(data) {
				if (!data || isNaN(data.id) || data.id <= 0) return false;
				$scope.postHiddenForm('/system/role/edit', { code: data.code });
			};

			// -------------------------------------------------
			// DELETE
			// -------------------------------------------------
			$scope.$on('$clickDeleteMenu', function(e, args) { $scope.onDelete(args.data); });
			$scope.onDelete = function(data) {
				if (!data || isNaN(data.id) || data.id <= 0) return false;
				ajaxService.post({
					url: '/system/role/delete',
					data: { pk: { code: data.code } },
					success: function(data) {
						$scope.onSearch(1);
					}
		        });
		  		return false;
		    };

			//			// -------------------------------------------------
			//			// ASIGN COMPANY
			//			// -------------------------------------------------
			//		    $scope.$on('$clickAssignCompanyMenu', function(e, args) { $scope.onAssignCompany(args.data); });
			//			$scope.onAssignCompany = function(data) {
			//				if (!data || isNaN(data.id) || data.id <= 0) return false;
			//				$scope.postHiddenForm(
			//						'/system/role/assign',
			//						{ code: data.code });
			//		  		return false;
			//		    };
			//		    $scope.moreMenuOptions = function(data, viewable, insertable, updatable, deletable) {
			//				return [
			//				        [function ($itemScope) {
			//				        	return angular.element('.btn-assign-company').attr('title');
			//				        },
			//				        function ($itemScope) {
			//			        		if (insertable || updatable) {
			//			        			// fire event from context-menu to child scope
			//			        			$scope.$broadcast('$clickAssignCompanyMenu', { data: data });
			//			        		}
			//				        	return false;
			//				        },
			//				        function ($itemScope) {
			//				        	return (angular.element('.btn-assign-company').length && (insertable || updatable));	// enable menu
			//				        },
			//				        function ($itemScope) {
			//				        	return 'assign-company';	// menu class
			//				        },
			//				        function ($itemScope) {
			//				        	return (angular.element('.btn-assign-company').length && (insertable || updatable));	// visible menu
			//				        }]
			//		        ];
			//			};
		}
);
