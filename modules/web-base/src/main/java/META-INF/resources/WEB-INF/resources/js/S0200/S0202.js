importModuleController(
		'S0200',
		'S0202Ctrl',
		function($scope, ajaxService, $filter) {
			/**
			 * Scope data model schema
			 */
			$scope.model = {
					/**
					 * user info
					 */
					user: {}
					/**
					 * roles list
					 */
					, roles: []
					, disabledRole: true
					/**
					 * companies list
					 */
					, companies: []
					/**
					 * employees list
					 */
					, employees: []
			};

			/**
			 * Initialize
			 *
			 * @param groupId the group identity
			 */
			$scope.onInit = function() {
				// initialize data
				// var data = $scope.getEntityPk();
				var data = $scope.findData('entitypk') || {};
				var username = (data && data.username ? data.username || '' : '');
				var company = (data && data.company ? data.company || '' : '');
				$scope.onInitData(company, username);
			};
			// initialize data
			$scope.onInitData = function(company, username) {
				$scope.onData(company, username);
			};

			// -------------------------------------------------
			// UNIQUE CHECK
			// -------------------------------------------------
			$scope.onUniqueParams = function(value) {
				return { pk: { username: value, companyCode: $scope.model.user.companyCode } };
			};

			// -------------------------------------------------
			// SEARCH
			// -------------------------------------------------
			/**
			 * Get user data
			 *
			 * @param company company code
			 * @param username user name
			 */
			$scope.onData = function(company, username) {
				$scope.model.user = {};
				$scope.detailForm && $scope.detailForm.$setPristine();
				$scope.detailForm && $scope.detailForm.$setUntouched();
				// load user information
				if (username && username.length) {
					ajaxService.post({
		        		url: '/system/user/entity',
		        		data: { pk: { username: username, companyCode: company || '' } },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			if (json) $scope.model.user = json;
		        			// require language
		        			if (($scope.model.user.language || '').length
		        					&& $scope.languages && $scope.languages.length) {
	        					$scope.model.user.language = $filter('propsEqualFilterFirst')(
	        							$scope.languages, {
	        								filterKey: 'languages',
	        								expression: { code: $scope.model.user.language }
	        							});
		        			}
		        			else if ($scope.language
		        					&& $scope.language.code
		        					&& $scope.language.code.length) {
		        				$scope.model.user.language = $scope.language;
		        			}
		        			// require password
		        			$scope.model.user.changepwd =
		        				(!$scope.model.user.id || isNaN($scope.model.user.id) || $scope.model.user.id <= 0);
		        			// default enabled
		        			if (!$scope.model.user.id || isNaN($scope.model.user.id) || $scope.model.user.id <= 0) {
		        				$scope.model.user.enabled = true;
		        			}
		        			// load roles
		    				$scope.onRoles();
		    				// load companies
		    				$scope.onCompanies();
		    				// load employees
		    				$scope.onEmployees();
				        }
					});
				}
				else {
        			// require language
					if ($scope.language
        					&& $scope.language.code
        					&& $scope.language.code.length) {
        				$scope.model.user.language = $scope.language;
        			}
        			// require password
        			$scope.model.user.changepwd =
        				(!$scope.model.user.id || isNaN($scope.model.user.id) || $scope.model.user.id <= 0);
        			// default enabled
        			if (!$scope.model.user.id || isNaN($scope.model.user.id) || $scope.model.user.id <= 0) {
        				$scope.model.user.enabled = true;
        			}
					// load roles
					$scope.onRoles();
					// load companies
					$scope.onCompanies();
					// load employees
					$scope.onEmployees();
				}
			};
			$scope.onRoles = function() {
				$scope.model.user.role = {
						id: null
						, code: null
						, name: angular.element('[name=role]').attr('data-placeholder')
				};
				$scope.model.roles = [ $scope.model.user.role ];
				$scope.model.disabledRole = true;
				ajaxService.jpost({
		        		url: '/system/user/roles',
				        statusCode: {
				        	200: function(data) {
			        			var json = angular.fromJson(data || {});
			        			if (angular.isArray(json || []) && (json || []).length) {
			        				$scope.model.roles = $scope.model.roles.concat(json || []);
			        			}
			        			// current role
			        			if (String($scope.model.user.gid || '').length && $scope.model.roles.length) {
			        				var role = $filter('propsEqualFilterFirst')(
			        						$scope.model.roles, {
			        							filterKey: 'roles',
			        							expression: { id: $scope.model.user.gid }
			        						});
			        				if (role && String(role.id || '').length) {
			        					$scope.model.user.role = role;
			        					$scope.model.disabledRole = false;
			        				}
			        				else {
			        					$scope.model.user.role = {
			        							id: $scope.model.user.gid
			        							, code: $scope.model.user.groupCode
			        							, name: $scope.model.user.groupName
			        					};
			        				}
			        			}
					        }
				        }
				});
			};
			$scope.onCompanies = function() {
				$scope.model.user.company = {
						id: null
						, code: null
						, name: angular.element('[name=company]').attr('data-placeholder')
				};
				$scope.model.companies = [ $scope.model.user.company ];
				if (angular.element('[name=company]').length) {
					ajaxService.jpost({
			        		url: '/system/user/companies',
					        statusCode: {
					        	200: function(data) {
				        			var json = angular.fromJson(data || {});
				        			if (angular.isArray(json || []) && (json || []).length) {
				        				$scope.model.companies = $scope.model.companies.concat(json || []);
				        			}
				        			// current company by main layout selection
				        			var pageInfo = $scope.findData('pageInfo') || {};
				        			var company = (($scope.model.user.companyCode || '').length
				        					? $scope.model.user.companyCode
				        							: !!pageInfo && !!pageInfo.company
				        							&& pageInfo.company.length
				        							? pageInfo.company || '' : '');
				        			if (company.length && $scope.model.companies.length) {
			        					var company = $filter('propsEqualFilterFirst')(
			        							$scope.model.companies, {
			        								filterKey: 'companies',
			        								expression: {  code: company }
			        							});
			        					if (company && String(company.id || '').length) {
			        						$scope.model.user.company = company;
			        					}
				        			}
						        }
					        }
					});
				}
			};
			$scope.onEmployees = function() {
				$scope.model.user.employee = {
						id: null
						, code: null
						, name: angular.element('[name=employee]').attr('data-placeholder')
				};
				$scope.model.employees = [ $scope.model.user.employee ];
				if (angular.element('[name=employee]').length) {
					ajaxService.jpost({
			        		url: '/system/user/employees',
					        statusCode: {
					        	200: function(data) {
				        			var json = angular.fromJson(data || {});
				        			if (angular.isArray(json || []) && (json || []).length) {
				        				$scope.model.employees = $scope.model.employees.concat(json || []);
				        			}
				        			// current employee
				        			if (String($scope.model.user.eid || '').length && $scope.model.employees.length) {
				        				var employee = $filter('propsEqualFilterFirst')(
				        						$scope.model.employees, {
			        								filterKey: 'employees',
			        								expression: { id: $scope.model.user.eid }
				        						});
				        				if (employee && String(employee.id || '').length) {
				        					$scope.model.user.employee = employee;
				        				}
				        			}
						        }
					        }
					});
				}
			};

			// -------------------------------------------------
			// Main actions
			// -------------------------------------------------
			/**
			 * Parse user data
			 */
			$scope.parseUser = function() {
				// parse attached information
				var language = ($scope.model.user.language
						&& $scope.model.user.language.code
						&& $scope.model.user.language.code.length
						? $scope.model.user.language : null);
				var company = ($scope.model.user.company
						&& !isNaN($scope.model.user.company.id)
						&& $scope.model.user.company.id > 0
						? $scope.model.user.company : null);
				var group = ($scope.model.user.role
						&& !isNaN($scope.model.user.role.id)
						&& $scope.model.user.role.id > 0
						? $scope.model.user.role : null);
				var employee = ($scope.model.user.employee
						&& !isNaN($scope.model.user.employee.id)
						&& $scope.model.user.employee.id > 0
						? $scope.model.user.employee : null);

				// convert to JSON string to remove all properties that start with $$
				return {
					id: $scope.model.user.id
					, cid: (company ? company.id : null)
					, companyCode: (company ? company.code : null)
					, companyName: (company ? company.name : null)
				    , gid: (group ? group.id : null)
				    , groupCode: (group ? group.code : null)
				    , groupName: (group ? group.name : null)
				    , eid: (employee ? employee.id : null)
				    , employeeCode: (employee ? employee.code : null)
				    , employeeName: (employee ? employee.name : null)
				    , employeeTel: ''
				    , username: $scope.model.user.username
				    , password: ($scope.model.user.changepwd ? $scope.model.user.password : '')
				    , changepwd: $scope.model.user.changepwd
				    , email: $scope.model.user.email
				    , enabled: $scope.model.user.enabled
				    , sysadmin: $scope.model.user.sysadmin
				    , language: (language ? language.code : null)
				    , description: $scope.model.user.description
				    , login_at: $scope.model.user.login_at
				    , expired_at: $scope.model.user.expired_at
				};
			};
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
		        		data: $scope.parseUser(),
		        		success: function(data) {
	        				$scope.success({ body: $('#btnSave').attr('data-success') });
		        			$scope.go('/system/user');
		        		}
				});
				return false;
			};
		}
);
