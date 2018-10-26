importModuleController(
		'S0900',
		'S0903Ctrl',
		function($scope, ajaxService, $rootScope) {
			/**
			 * Scope data model schema
			 */
			$scope.model = {
					/**
					 * function info
					 */
					func: {},
					/**
					 * List view data source
					 */
					source: [],
					destination: [],
					compDest: {
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
									&& e.source.sortableScope.element.attr('data-listview-id') === 'compDest') {
								apply = true;
							}
							else if (e.dest && e.dest.sortableScope && e.dest.sortableScope.element
									&& e.dest.sortableScope.element.attr('data-listview-id') === 'compDest') {
								apply = true;
							}
							if (apply) {
								// apply touched property
								$scope.setCompDestTouched(true);
								// apply required property
								$scope.setCompDestRequired(
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
			$scope.onInitData = function(code) {
				$scope.onFunction(code);
			};

			// -------------------------------------------------
			// SEARCH
			// -------------------------------------------------
			/**
			 * Get function info
			 *
			 * @param code function code
			 */
			$scope.onFunction = function(code) {
				$scope.model.func = {};
				$scope.model.source = [];
				$scope.model.destination = [];
				$scope.model.compDest = {
					$touched: false,
					$error: {}
				};
				$scope.detailForm && $scope.detailForm.$setPristine();
				$scope.detailForm && $scope.detailForm.$setUntouched();
				if (code && code.length > 0) {
					ajaxService.post({
		        		url: '/system/function/entity',
		        		data: { pk: { code: code || '' } },
				        success: function(data) {
		        			var json = angular.fromJson(data || {});
		        			var needDest = false;
		        			if (json) {
		        				$scope.model.func = json;
		        				needDest = true;
		        			}
		    				// search all roles that has been assigned yet!!!
		    				$scope.onCompanies(code, true);
		    				// search all roles that had been assigned into the specified group!!!
		    				needDest && $scope.onCompanies(code, false);
				        }
					});
				}
			};
			/**
			 * Get companies list
			 *
			 * @param code function code
			 * @param source true - un-assigned companies; false - assigned companies
			 */
			$scope.onCompanies = function(code, source) {
				ajaxService.jpost({
		        		url: '/system/function/companies',
		        		data: { code: code || '', assigned: !source },
				        statusCode: {
				        	200: function(data) {
			        			var json = angular.fromJson(data || {});
			        			if (json) {
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
				$scope.setCompDestTouched(
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
					console && console.warn && console.warn('Not found source selection to move!!!');
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
					console && console.warn && console.warn('Not found destination selection to move!!!');
				}
			};
			/**
			 * Assign/Un-Assign company between companies list
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
					if (a.code < b.code)
						return -1;
					if (a.code > b.code)
						return 1;
					return 0;
				});
				dest.sort(function(a, b) {
					if (a.code < b.code)
						return -1;
					if (a.code > b.code)
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
				$scope.setCompDestTouched(true);
				// apply required property
				$scope.setCompDestRequired(
						!angular.isArray($scope.model.destination)
						|| !$scope.model.destination.length);
			};
			/**
			 * Apply roles list required property
			 */
			$scope.setCompDestRequired = function(required) {
				$scope.model.compDest.$error.required = required;
				// invalid
				$scope.model.compDest.$error.required
				&& $('#compDest').hasClass('ng-valid')
				&& $('#compDest').removeClass('ng-valid');
				$scope.model.compDest.$error.required
				&& $('#compDest').hasClass('ng-valid-required')
				&& $('#compDest').removeClass('ng-valid-required');
				$scope.model.compDest.$error.required
				&& !$('#compDest').hasClass('ng-invalid')
				&& $('#compDest').addClass('ng-invalid');
				$scope.model.compDest.$error.required
				&& !$('#compDest').hasClass('ng-invalid-required')
				&& $('#compDest').addClass('ng-invalid-required');
				// valid
				!$scope.model.compDest.$error.required
				&& $('#compDest').hasClass('ng-invalid')
				&& $('#compDest').removeClass('ng-invalid');
				!$scope.model.compDest.$error.required
				&& $('#compDest').hasClass('ng-invalid-required')
				&& $('#compDest').removeClass('ng-invalid-required');
				!$scope.model.compDest.$error.required
				&& !$('#compDest').hasClass('ng-valid')
				&& $('#compDest').addClass('ng-valid');
				!$scope.model.compDest.$error.required
				&& !$('#compDest').hasClass('ng-valid-required')
				&& $('#compDest').addClass('ng-valid-required');
			}
			/**
			 * Apply roles list touched property
			 */
			$scope.setCompDestTouched = function(touched) {
				$scope.model.compDest.$touched = touched;
				$scope.model.compDest.$touched
				&& $('#compDest').hasClass('ng-untouched')
				&& $('#compDest').removeClass('ng-untouched');
				$scope.model.compDest.$touched
				&& !$('#compDest').hasClass('ng-touched')
				&& $('#compDest').addClass('ng-touched');
				!$scope.model.compDest.$touched
				&& $('#compDest').hasClass('ng-touched')
				&& $('#compDest').removeClass('ng-touched');
				!$scope.model.compDest.$touched
				&& !$('#compDest').hasClass('ng-untouched')
				&& $('#compDest').addClass('ng-untouched');
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
				$scope.setCompDestRequired(error);

				// check form
				$scope.detailForm && $scope.detailForm.$setSubmitted();
				if ($scope.detailForm && !$scope.detailForm.$invalid && error) $scope.detailForm.$invalid = error;
				if (!$scope.detailForm || $scope.detailForm.$invalid || error) return false;

				// convert to JSON string to remove all properties that start with $$
				var func = angular.fromJson(angular.toJson($.extend($scope.model.func, { companies: data })));
				ajaxService.post({
		        		url: '/system/function/assign/save',
		        		data: func,
		        		success: function(data) {
	        				$scope.success({ body: $('#btnSave').attr('data-success') });
		        			$scope.go('/system/function');
		        		}
				});
				return false;
			};
		}
);
