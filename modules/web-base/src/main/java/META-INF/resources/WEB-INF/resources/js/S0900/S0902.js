importModuleController(
		'S0900',
		'S0902Ctrl',
		function($scope, ajaxService, $filter) {
			/**
			 * Scope data model schema
			 */
			$scope.model = {
					/**
					 * function info
					 */
					func: {}
			};

			/**
			 * Initialize
			 *
			 * @param groupId the group identity
			 */
			$scope.onInit = function() {
				// initialize data
				var data = $scope.findData('entitypk') || {};
				var code = (data && data.code ? data.code || '' : '');
				$scope.onInitData(code);
			};
			// initialize data
			$scope.onInitData = function(code) {
				$scope.onData(code);
			};

			// -------------------------------------------------
			// UNIQUE CHECK
			// -------------------------------------------------
			$scope.onUniqueParams = function(value) {
				return { pk: { code: value } };
			};

			// -------------------------------------------------
			// SEARCH
			// -------------------------------------------------
			/**
			 * Get function data
			 *
			 * @param code the function code
			 */
			$scope.onData = function(code) {
				$scope.model.func = {};
				$scope.detailForm && $scope.detailForm.$setPristine();
				$scope.detailForm && $scope.detailForm.$setUntouched();
				// load user information
				if (code && code.length) {
					ajaxService.post({
		        		url: '/system/function/entity',
		        		data: { pk: { code: code } },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			if (json) $scope.model.func = json;
		        			// default enabled
		        			if (!$scope.model.func.id || isNaN($scope.model.func.id) || $scope.model.func.id <= 0) {
		        				$scope.model.func.enabled = true;
		        			}
				        }
					});
				}
				else {
					// default enabled
        			if (!$scope.model.func.id || isNaN($scope.model.func.id) || $scope.model.func.id <= 0) {
        				$scope.model.func.enabled = true;
        			}
				}
			};

			// -------------------------------------------------
			// Main actions
			// -------------------------------------------------
			$scope.onSave = function(e) {
				// disable submit event
				e.preventDefault();
				e.returnValue = false;
				e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

				// check form
				$scope.detailForm && $scope.detailForm.$setSubmitted();
				if (!$scope.detailForm || $scope.detailForm.$invalid) return false;

				// perform to save
				ajaxService.post({
		        		url: angular.element(e.target).attr('action'),
		        		data: $scope.model.func,
		        		success: function(data) {
	        				$scope.success({ body: $('#btnSave').attr('data-success') });
		        			$scope.go('/system/function');
		        		}
				});
				return false;
			};
		}
);
