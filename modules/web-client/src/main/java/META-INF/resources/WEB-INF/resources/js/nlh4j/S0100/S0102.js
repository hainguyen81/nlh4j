importModuleController(
		'S0100',
		'S0102Ctrl',
		function($scope, ajaxService, $rootScope, $log) {
			/**
			 * Scope data model schema
			 */
			$scope.model = {
					/**
					 * Role info
					 */
					group: {},
					/**
					 * master functions info
					 */
					masterFunctions: [],
					/**
					 * List view data source
					 */
					source: [],
					destination: [],
					rolesDest: {
						$touched: false,
						$error: {}
					},
					/**
					 * Structure:
					 * row: the HTML data row <br/>
					 * data: the attached data <br/>
					 * - pattern: [ { row: row1, data: data1 }, etc... ]
					 */
					// source selection
					srcSelection: [],
					// destination selection
					destSelection: [],
					/**
					 * Listview sortable listeners
					 */
					sortable: {
						itemMoved: function(e) {
							var apply = false;
							if (e.source && e.source.sortableScope && e.source.sortableScope.element
									&& e.source.sortableScope.element.attr('data-listview-id') === 'rolesDest') {
								apply = true;
							}
							else if (e.dest && e.dest.sortableScope && e.dest.sortableScope.element
									&& e.dest.sortableScope.element.attr('data-listview-id') === 'rolesDest') {
								apply = true;
							}
							if (apply) {
								// apply touched property
								$scope.setRolesTouched(true);
								// apply required property
								$scope.setRolesRequired(
										!angular.isArray($scope.model.destination)
										|| !$scope.model.destination.length);
							}
						}
					}
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
				var code = (data && data.code ? data.code || '' : '');
				$scope.onInitData(code);
			};
			// initialize data
			$scope.onInitData = function(gcd) {
				// require group information
				$scope.onGroup(gcd);
				if(gcd && gcd.length>0)
					$scope.initValueRole();
			};
			/**
			 * init role by P01
			 */
			$scope.initValueRole=function(){
				var initSel='P01';
				angular.forEach($scope.model.source, function(value, key){
					if(value.data.moduleCd==initSel){
						$scope.model.srcSelection=value;
						$scope.onAssignByAction();
						return;
					}
				});
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
			 * Get roles list
			 *
			 * @param gcd the group code
			 */
			$scope.onGroup = function(gcd) {
				$scope.model.group = {};
				$scope.model.rolesDest = {
					$touched: false,
					$error: {}
				};
				$scope.detailForm && $scope.detailForm.$setPristine();
				$scope.detailForm && $scope.detailForm.$setUntouched();
				if (gcd && gcd.length > 0) {
					ajaxService.post({
		        		url: '/system/role/entity',
		        		data: { pk: { code: gcd || '' } },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			var needDest = false;
		        			if (json) {
		        				$scope.model.group = json;
		        				needDest = true;
		        			}
		    				// search all roles that has been assigned yet!!!
		    				$scope.onRoles(gcd, true);
		    				// search all roles that had been assigned into the specified group!!!
		    				needDest && $scope.onRoles(gcd, false);
				        }
					});
				}
				else {
					// search all roles that has been assigned yet!!!
    				$scope.onRoles(gcd, true);
				}
			};
			$scope.onRoles = function(gcd, source) {
				ajaxService.jpost({
		        		url: '/system/role/childs',
		        		data: { gcd: gcd || '', assigned: !source },
				        statusCode: {
				        	200: function(data) {
			        			var json = angular.fromJson(data || {});
			        			if (json) {
			        				// detect for function columns
		        					$.map(json, function(r) {
		        						var permissions = (r.functions || '').split('|');
		        						r['permissions'] = permissions || [];
		        						$.map(permissions, function(fn) {
		        							if (fn.length) r[fn] = ($.inArray(permissions, fn) >= 0);
		        						});
			        				});
			        				// cache roles
			        				if (!source) $scope.model.destination = json;
			        				else $scope.model.source = json;
			        				$scope.$apply();
			        			}
			        			else {
			        				if (!source) $scope.model.destination = [];
			        				else $scope.model.source = [];
			        			}
			        			// reset cache selection
			        			$scope._reset(-1, source);
					        }
				        }
				});
			};

			// -------------------------------------------------
			// Selection helpers
			// -------------------------------------------------
			/**
			 * Reset all selected items
			 */
			$scope._reset = function(idx, source) {
				// reset data
				var selection = (source ? $scope.model.srcSelection : $scope.model.destSelection);
				if (selection && selection.length > 0) {
					if (idx >= 0 && idx < selection.length) {
						$scope._select(selection[i], false, source);
					}
					else {
						for (var i = 0; i < selection.length; i++) {
							$scope._select(selection[i], false, source);
						}
						if (source) {
							$scope.model.srcSelection = [];
						}
						else {
							$scope.model.destSelection = [];
						}
					}
				}
			};
			/**
			 * Select/Un-select one item
			 */
			$scope._select = function(item, selected, source) {
				// check parameters
				if (!item || !item.row || !item.data) return;
				var selection = (source ? $scope.model.srcSelection : $scope.model.destSelection);
				var targetidx = (item.row.index ? item.row.index() : -1);
				if (targetidx < 0 || (!selected && (!selection || selection.length <= 0))) return;

				// if selected
				if (selected && !item.row.hasClass('selected')) {
					// select target
					item.row.addClass('selected');
					// cache target
					source && $scope.model.srcSelection.push(item);
					!source && $scope.model.destSelection.push(item);
				}
				// else if un-selected
				else if (!selected && item.row.hasClass('selected')) {
					// un-select target
					item.row.removeClass('selected');
					// remove cache
					var i = -1;
					for (i = 0; i < selection.length; i++) {
						if (selection[i].row.index() == targetidx) {
							break;
						}
					}
					if (0 <= i && i < selection.length) {
						selection.removeAt(i);
						if (source) {
							$scope.model.srcSelection = selection;
						}
						else {
							$scope.model.destSelection = selection;
						}
					}
				}
				// apply touched property
				$scope.setRolesTouched(
						!source && angular.isArray($scope.model.destSelection)
						&& $scope.model.destSelection.length);
			};
			/**
			 * Specify target whether is in selected block
			 */
			$scope._isInSelectedBlock = function(target, source) {
				// check parameters
				if (!target || target.length <= 0) return false;
				var selection = (source ? $scope.model.srcSelection : $scope.model.destSelection);
				var targetidx = (target.index ? target.index() : -1);
				if (targetidx < 0 || !selection || selection.length <= 1) return false;

				for (var i = 0; i < selection.length; i++) {
					if (selection[i].row.index() == targetidx) return true;
				}
				return false;
			};

			// -------------------------------------------------
			// Selection events
			// -------------------------------------------------
			$scope.onGetValue = function(prop, item) {
				if (prop.length && !item.hasOwnProperty(prop)) {
					item[prop] = ($.inArray(item['permissions'], prop) >= 0);
				}
				return item[prop];
			};
			$scope.onChangePermission = function(prop, item) {
				// if checked
				if (item[prop] == true
						&& $.inArray(item['permissions'], prop) < 0) {
					item['permissions'].push(prop);

				} else if (item[prop] == false
						&& $.inArray(item['permissions'], prop) >= 0) {
					item['permissions'].remove(prop);
				}
			};
			/**
			 * Select item event handler on source list
			 */
			$scope.onSrcSelect = function(e, item) {
				$scope.onSelect(e, item, true);
			};
			/**
			 * Select item event handler on destination list
			 */
			$scope.onDestSelect = function(e, item) {
				$scope.onSelect(e, item, false);
			};
			/**
			 * Select item event handler helper
			 */
			$scope.onSelect = function(e, item, source) {
				// parse event target
				var target = $(e.currentTarget);
				var selection = (source ? $scope.model.srcSelection : $scope.model.destSelection);
				var targetidx = (target.length > 0 ? target.index() : -1);
				if (targetidx < 0 || !selection) return;

				// check status
				var selected = target.hasClass('selected');
				var inBlock = $scope._isInSelectedBlock(target, source);
				selected = (selected && inBlock ? false : selected);

				// ctrl + click
				if (e.ctrlKey) {
					$scope._select({ row: target, data: item}, !selected, source);
				}

				// shift + click
				else if (e.shiftKey) {
					// find minimum selected item index
					var minidx = -1;
					for (var i = 0; i < selection.length; i++) {
						var selitem = selection[i];
						if (!selitem || !selitem.row) continue;
						if (minidx < 0 || minidx <= selitem.row.index()) {
							minidx = selitem.row.index();
						}
					}
					// if found
					var tmp1 = Math.min(minidx, targetidx);
					var tmp2 = Math.max(minidx, targetidx);
					minidx = tmp1;
					targetidx = tmp2;
					if (minidx >= 0) {
						// select from minimum to current index
						var table = target.closest('tbody');
						if (table && table.length > 0) {
							for (var i = minidx; i <= targetidx; i++) {
								var row = $(table[0].rows[i]);
								$scope._select({ row: row, data: row.data('data') }, true, source);
							}
						}
					}
				}

				// else just click
				else {
					// reset cache
					$scope._reset(-1, source);
					// select/un-select current item
					$scope._select({ row: target, data: item }, !selected, source);
				}
			};

			// -------------------------------------------------
			// Assign roles from source to destination (grant role)
			// -------------------------------------------------
			/**
			 * Assign role from source to destination by double-click
			 */
			$scope.onAssignByDbClick = function(e, item) {
				$scope.onAssign([item], true);
			};
			/**
			 * Un-Assign role from destination to source by double-click
			 */
			$scope.onUnAssignByDbClick = function(e, item) {
				$scope.onAssign([item], false);
			};
			/**
			 * Assign role from source to destination by action button
			 */
			$scope.onAssignByAction = function() {
				var items = [];
				var selection = $scope.model.srcSelection;
				if (angular.isArray(selection)) {
					for(var i = 0; i < selection.length; i++) {
						if (!selection[i].data) continue;
						items.push(selection[i].data);
					}
					$scope.debug(items);
					$scope.onAssign(items, true);
				}
				else {
					($log && typeof $log.warn === 'function')
					&& $log.warn('Not found source selection to move!!!');
				}
			};
			/**
			 * Un-Assign role from destination to source by action button
			 */
			$scope.onUnAssignByAction = function(e, item) {
				var items = [];
				var selection = $scope.model.destSelection;
				if (angular.isArray(selection)) {
					for(var i = 0; i < selection.length; i++) {
						if (!selection[i].data) continue;
						items.push(selection[i].data);
					}
					$scope.onAssign(items, false);
				}
				else {
					($log && typeof $log.warn === 'function')
					&& $log.warn('Not found destination selection to move!!!');
				}
			};
			/**
			 * Assign/Un-Assign role between roles list
			 */
			$scope.onAssign = function(items, source) {
				// check parameters
				if (!items || items.length < 0) return;

				// parse datasource
				var src = (source ? $scope.model.source : $scope.model.destination);
				src = (!src ? [] : src);
				var dest = (source ? $scope.model.destination : $scope.model.source);
				dest = (!dest ? [] : dest);

				// remove item from source
				if (angular.isArray(src)) {
					for(var i = 0; i < items.length; i++) {
						src.remove(items[i]);
					}
				}

				// add item into destination
				for(var i = 0; i < items.length; i++) {
					dest.push(items[i]);
				}

				// resort data
				src.sort(function(a, b) {
					if (a.moduleCd < b.moduleCd)
						return -1;
					if (a.moduleCd > b.moduleCd)
						return 1;
					return 0;
				});
				dest.sort(function(a, b) {
					if (a.moduleCd < b.moduleCd)
						return -1;
					if (a.moduleCd > b.moduleCd)
						return 1;
					return 0;
				});

				// re-assign data source
				if (source) {
					$scope.model.source = src;
					$scope.model.destination = dest;
				}
				else {
					$scope.model.source = dest;
					$scope.model.destination = src;
				}

				// reset selection cache
				$scope._reset(-1, true);
				$scope._reset(-1, false);

				// apply touched property
				$scope.setRolesTouched(true);
				// apply required property
				$scope.setRolesRequired(
						!angular.isArray($scope.model.destination)
						|| !$scope.model.destination.length);
			};
			/**
			 * Apply roles list required property
			 */
			$scope.setRolesRequired = function(required) {
				$scope.model.rolesDest.$error.required = required;
				// invalid
				$scope.model.rolesDest.$error.required
				&& $('#rolesDest').hasClass('ng-valid')
				&& $('#rolesDest').removeClass('ng-valid');
				$scope.model.rolesDest.$error.required
				&& $('#rolesDest').hasClass('ng-valid-required')
				&& $('#rolesDest').removeClass('ng-valid-required');
				$scope.model.rolesDest.$error.required
				&& !$('#rolesDest').hasClass('ng-invalid')
				&& $('#rolesDest').addClass('ng-invalid');
				$scope.model.rolesDest.$error.required
				&& !$('#rolesDest').hasClass('ng-invalid-required')
				&& $('#rolesDest').addClass('ng-invalid-required');
				// valid
				!$scope.model.rolesDest.$error.required
				&& $('#rolesDest').hasClass('ng-invalid')
				&& $('#rolesDest').removeClass('ng-invalid');
				!$scope.model.rolesDest.$error.required
				&& $('#rolesDest').hasClass('ng-invalid-required')
				&& $('#rolesDest').removeClass('ng-invalid-required');
				!$scope.model.rolesDest.$error.required
				&& !$('#rolesDest').hasClass('ng-valid')
				&& $('#rolesDest').addClass('ng-valid');
				!$scope.model.rolesDest.$error.required
				&& !$('#rolesDest').hasClass('ng-valid-required')
				&& $('#rolesDest').addClass('ng-valid-required');
			}
			/**
			 * Apply roles list touched property
			 */
			$scope.setRolesTouched = function(touched) {
				$scope.model.rolesDest.$touched = touched;
				$scope.model.rolesDest.$touched
				&& $('#rolesDest').hasClass('ng-untouched')
				&& $('#rolesDest').removeClass('ng-untouched');
				$scope.model.rolesDest.$touched
				&& !$('#rolesDest').hasClass('ng-touched')
				&& $('#rolesDest').addClass('ng-touched');
				!$scope.model.rolesDest.$touched
				&& $('#rolesDest').hasClass('ng-touched')
				&& $('#rolesDest').removeClass('ng-touched');
				!$scope.model.rolesDest.$touched
				&& !$('#rolesDest').hasClass('ng-untouched')
				&& $('#rolesDest').addClass('ng-untouched');
			}

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

				// check data
				var data = $scope.model.destination;
				var error = (!angular.isArray(data) || !data.length);
				// apply required property
				$scope.setRolesRequired(error);

				// check form
				$scope.detailForm && $scope.detailForm.$setSubmitted();
				if ($scope.detailForm && !$scope.detailForm.$invalid && error) $scope.detailForm.$invalid = error;
				if (!$scope.detailForm || $scope.detailForm.$invalid || error) return false;

				// detect for master functions
				$.map(data, function(r) {
					r.functions = '';
					$.map(r['permissions'], function(fn) {
						if (fn.length) {
							if (r.functions.length) r.functions += '|';
							r.functions += fn;
						}
						if (r.hasOwnProperty(fn)) delete r[fn];
						$.map($scope.masterFunctions || '', function(mfn) {
							if (r.hasOwnProperty(mfn)) delete r[mfn];
						});
					});
					if (r.hasOwnProperty('permissions')) delete r['permissions'];
				});

				// convert to JSON string to remove all properties that start with $$
				var group = angular.fromJson(angular.toJson($.extend($scope.model.group, { roles: data })));
				ajaxService.post({
		        		url: angular.element(e.target).attr('action') || angular.element('#detailForm').attr('action'),
		        		data: group,
		        		success: function(data) {
	        				$scope.success({ body: $('#btnSave').attr('data-success') });
		        			$scope.go('/system/role');
		        		}
				});
				return false;
			};
		}
);
