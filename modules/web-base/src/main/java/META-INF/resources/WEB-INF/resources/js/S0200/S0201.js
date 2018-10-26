importModuleController(
		'S0200',
		'S0201Ctrl',
		function($scope, ajaxService, $rootScope) {
			/**
			 * Declare model
			 */
			$scope.model = {
					users: {
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
						expired: null
					},
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
				return $scope.model.users.data;
			};
			$scope.getPagination = function() {
				return $scope.model.users.pagination;
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
		        		url: '/system/user/search',
		        		data: { c: $scope.model.search, p: page },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			if (json && json.data && json.data.length > 0) {
		        				$scope.model.users = json;
		        			}
		        			else {
		        				$scope.model.users = {
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
						'/system/user/view',
						//{ username: data.username, companyCode: data.companyCode });
						{ username: data.username });
			};

			// -------------------------------------------------
			// UPDATE
			// -------------------------------------------------
			$scope.$on('$clickUpdateMenu', function(e, args) { $scope.onUpdate(args.data); });
			$scope.onUpdate = function(data) {
				if (!data || isNaN(data.id) || data.id <= 0) return false;
				$scope.postHiddenForm(
						'/system/user/edit',
						//{ username: data.username, companyCode: data.companyCode });
						{ username: data.username });
			};

			// -------------------------------------------------
			// DELETE
			// -------------------------------------------------
			$scope.$on('$clickDeleteMenu', function(e, args) { $scope.onDelete(args.data); });
			$scope.onDelete = function(data) {
				if (!data || isNaN(data.id) || data.id <= 0) return false;
				ajaxService.post({
					url: '/system/user/delete',
					//data: { pk: { username: data.username, companyCode: data.companyCode } },
					data: { pk: { username: data.username } },
					success: function(data) {
						$scope.onSearch(1);
					}
		        });
		  		return false;
		    };
		}
);
