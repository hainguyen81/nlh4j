importModuleController(
		'C1200',
		'C1202Ctrl',
		function($scope, ajaxService, $rootScope) {
			/**
			 * Declaration
			 */
			$scope.model = {};

			/**
			 * Initialization
			 */
			$scope.onInit = function() {
				// initialize data
				// var data = $scope.getEntityPk();
				var data = $scope.findData('entitypk') || {};
				var username = (data && data.username ? data.username || '' : '');
				$scope.searchUser(username);
			};

			/**
			 * Search model
			 */
			$scope.searchUser = function(username) {
				if (username && username.length > 0) {
					ajaxService.post({
						url: '/chgpwd/entity',
						data: { pk: { username: username } },
						success: function(data) {
							var data = angular.fromJson(data || {});
							if (data) $scope.model = data;
						}
					});
				}
				else {
					$scope.model = {};
				}
			};

			/**
			 * Validation
			 */
			$scope.onCheckMatch = function(type) {
				var newPwd = $scope.detailForm.newPassword.$viewValue || '';
				var retypePwd = $scope.detailForm.retypePassword.$viewValue || '';
				var matched = (newPwd.length > 0 && retypePwd.length > 0 && newPwd == retypePwd);
				($log && typeof $log.info === 'function')
				&& $log.info(matched);
				switch(type) {
					case 0: {
						$scope.detailForm.retypePassword.$setValidity("dontMatch", true);
						$scope.detailForm.newPassword.$setValidity("dontMatch", matched);
						break;
					}
					case 1: {
						$scope.detailForm.newPassword.$setValidity("dontMatch", true);
						$scope.detailForm.retypePassword.$setValidity("dontMatch", matched);
						break;
					}
				}
			};

			// -------------------------------------------------
			// Main actions
			// -------------------------------------------------
			/**
			 * Refresh role settings to original
			 */
			$scope.onSave = function(e) {
				// disable submit event
				e.preventDefault();
				e.returnValue = false;
				e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

				// validate form
				$scope.detailForm && $scope.detailForm.$setSubmitted();
				if (!$scope.detailForm || $scope.detailForm.$invalid) return false;

				// change password
				ajaxService.post({
					url: angular.element(e.target).attr('action'),
					data: $scope.serializeObject(angular.element(e.target)),
				    success: function(data) {
			    		$scope.success({body: angular.element(e.target).attr('data-success') });
			    		$scope.go('/dashboard');
			    	}
				});
				return false;
			};
		});

