(function(angular) {

	'use strict';

	// support data model and drag-drop
	// require jquery sortable
	angular.module('ui.listview', [])
	    .directive("listview",
	    		['$compile', '$interpolate', '$templateCache', '$parse',
	    		 function($compile, $interpolate, $templateCache, $parse) {
	        return {
	            restrict: "EA",
	            transclude: false,
	            scope: {
	                listviewId: "@listview",
	                allowRemoveIfMovedOut: "@allowRemoveIfMovedOut",
	                items: "=",
	                methods: "=",
	                click: "=",
	                dbclick: "=",
	                listeners: "=",
	                readonly: "=",
	                change: "=",
	                getValue: "="
	            },
	            templateUrl: function(element, attrs) {
	                if (!attrs.template && !attrs.templateBase) return 'angular-listview.html';
	                attrs.template = attrs.template || 'angular-listview.html';
	                if (!attrs.templateBase) return attrs.template;
	                var path = attrs.templateBase;
	                if (path.substr(path.length - 1, 1) != "/") path += "/";
	                path += attrs.template + '?' + (new Date()).getTime();
	                return path;
	            },
	            link: function(scope, element, attrs) {
	                attrs.$observe('columns', function(val) {
	                	var arrcolumns = new Array();
	                    //var columns = val.replace(/ /g, '').split('|');
	                	var columns = val.split('|');
	                    for (var i = 0; i < columns.length; i++) {
	                    	var column = columns[i];
	                    	var colinfo = column.split('=');
	                    	var key = (colinfo && colinfo.length > 0 ? colinfo[0] : '');
	                    	var title = (colinfo && colinfo.length > 1 ? colinfo[1] : key);
	                    	key = key.replace(/ /g, '');
	                    	if (key.length >= 0) {
	                    		arrcolumns.push({ key: key, title: title });
	                    	}
	                    }
	                    scope.columns = arrcolumns;
	                });
	                attrs.$observe('sortable', function(val) {
	                	scope.sortable = (typeof val === 'boolean'
	                		? val : (val == '1' || val == 'true' ? true : false));
	                });
	                attrs.$observe('readonly', function(val) {
	                	scope.readonly = (typeof val === 'boolean'
	                		? val : (val == '1' || val == 'true' ? true : false));
	                });
	                attrs.$observe('itemDescription', function(val) {
	                	scope.itemDescription = $parse(val)(scope);
	                });
	                attrs.$observe('id', function(val) {
	                	scope.lvId = (val ? String(val) : String((new Date()).getTime()));
	                });
	                attrs.$observe('hideHeader', function(val) {
	                	scope.hideHeader = (typeof val === 'boolean'
	                		? val : (val == '1' || val == 'true' ? true : false));
	                });
	                attrs.$observe('allowRemoveIfMovedOut', function(val) {
	                	scope.allowRemoveIfMovedOut = (typeof val === 'boolean'
	                		? val : (val == '1' || val == 'true' ? true : false));
	                });
	            },
	            controller: function($scope, $interpolate, $compile, $templateCache) {
	                $scope.predicate = 'title';
	                $scope.getTemplate = function(column, item) {
	                    var prefix = $scope.listviewId ? $scope.listviewId + '-' : '';
	                    var template = prefix + 'column-' + column.key + '.html';
	                    var value = item[column.key];
	                    if (typeof value === 'boolean') template = 'column-chkbox.html';
	                    var html = $templateCache.get(template);
	                    return html ? template : 'column-' + 'default.html';
	                };
	                $scope._getValue = function(column, item) {
	                	return (this.getValue && typeof this.getValue === 'function' ? this.getValue(column.key, item) : item[column.key]);
	                };
	                $scope._format = function(column, item) {
	                	var val = $scope._getValue(column, item);
	                    return this.methods && this.methods[column.key] ? this.methods[column.key](val, column.key, item) : val;
	                };
	                $scope._click = function($event) {
	                	var target = $($event.currentTarget);
	                    return (this.click && typeof this.click === 'function' ? this.click($event, target.data('data'), $scope.listviewId) : false);
	                };
	                $scope._dbclick = function($event) {
	                	var target = $($event.currentTarget);
	                    return (this.dbclick && typeof this.dbclick === 'function' ? this.dbclick($event, target.data('data'), $scope.listviewId) : false);
	                };
	                $scope._change = function(column, item) {
	                    return (this.change && typeof this.change === 'function' ? this.change(column.key, item) : false);
	                };
	            }
	        };
	    }])
	    .directive('modelInData', function($parse) {
	    	return {
	    		restrict: 'EA',
	    		link: function($scope, $element, $attrs) {
	    			var model = $parse($attrs.modelInData)($scope);
	    			$attrs.$set('data', model);
	    			$element.data('data', model);
	    		}
	    	};
	    })
	    .filter('capitalize', function() {
	        "use strict";
	        return function(input) {
	            return input.charAt(0).toUpperCase() + input.slice(1);
	        };
	    })

	    ;

})(angular);

