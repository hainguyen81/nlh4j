importModuleController(
		'S0400',
		'S0402Ctrl',
		function($scope, ajaxService, $filter) {
			/**
			 * Scope data model schema
			 */
			$scope.model = {
					/**
					 * module info
					 */
					module: {}
					/**
					 * parent modules array
					 */
					, parent: {}
					, parents: []
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
			$scope.onUniqueUrlParams = function(value) {
				var code = ($scope.model.module && $scope.model.module.code
						? $scope.model.module.code || '' : '');
				return { code: code, url: value };
			};

			// -------------------------------------------------
			// SEARCH
			// -------------------------------------------------
			/**
			 * Get parent modules list
			 */
			$scope.onParentModules = function(code) {
				$scope.model.parent = {
						id: null
						, code: null
						, name: angular.element('[name=parentMod]').attr('data-placeholder')
				};
				$scope.model.parents = [ $scope.model.parent ];
				ajaxService.jpost({
		        		url: '/system/module/list',
		        		data: { code: code || '' },
				        statusCode: {
				        	200: function(data) {
			        			var json = angular.fromJson(data || {});
			        			$scope.model.parents = $scope.model.parents.concat(json || []);
			        			// current role
			        			if (String($scope.model.module.pid || '').length && $scope.model.parents.length) {
			        				$scope.model.parent = $filter('propsEqualFilterFirst')(
			        						$scope.model.parents, {
		        								filterKey: 'modules',
		        								expression: { id: $scope.model.module.pid }
			        						});
			        			}
					        }
				        }
				});
			};
			/**
			 * Get module data
			 *
			 * @param code the module code
			 */
			$scope.onData = function(code) {
				$scope.model.module = {};
				$scope.detailForm && $scope.detailForm.$setPristine();
				$scope.detailForm && $scope.detailForm.$setUntouched();
				// load user information
				if (code && code.length) {
					ajaxService.post({
		        		url: '/system/module/entity',
		        		data: { pk: { code: code } },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			if (json) $scope.model.module = json;
		        			// default enabled
		        			if (!$scope.model.module.id || isNaN($scope.model.module.id) || $scope.model.module.id <= 0) {
		        				$scope.model.module.enabled = true;
		        			}
		        			// load parent modules
		        			$scope.onParentModules(code);
				        }
					});
				}
				else {
					// default enabled
        			if (!$scope.model.module.id || isNaN($scope.model.module.id) || $scope.model.module.id <= 0) {
        				$scope.model.module.enabled = true;
        			}
        			// load parent modules
        			$scope.onParentModules(code);
				}
			};
			/**
			 * Enabled check-box changed listener
			 */
			$scope.onEnableChanged = function() {
				if ($scope.model.module && !$scope.model.module.enabled) {
					$scope.model.module.common = false;
					$scope.model.module.service = false;
					$scope.model.module.visibled = false;
				}
			};
			/**
			 * URL text-box changed listener
			 */
			$scope.onUrlChanged = function() {
				if ($scope.model.module && $scope.model.module.mainUrl
						&& $scope.model.module.mainUrl.length) {
					$scope.model.module.urlRegex = "^(" + $scope.model.module.mainUrl.trim() + ")";
				}
				else {
					$scope.model.module.urlRegex = null;
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

				// parent parent module
				var parent = ($scope.model.parent
						&& !isNaN($scope.model.parent.id)
						&& $scope.model.parent.id > 0
						? $scope.model.parent : null);
				$scope.model.module.pid = (parent ? parent.id : null);

				// perform to save
				ajaxService.post({
		        		url: angular.element(e.target).attr('action'),
		        		data: $scope.model.module,
		        		success: function(data) {
	        				$scope.success({ body: $('#btnSave').attr('data-success') });
		        			$scope.go('/system/module');
		        		}
				});
				return false;
			};
		}
);
