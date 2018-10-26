angular.module('wiz.validation.equalTo')

	.directive('wizValEqualTo', ['wizEqualToSvc', function (wizEqualToSvc) {
		return {
			restrict: 'A',
			require: 'ngModel',
			link: function (scope, elem, attrs, ngModel) {

				//For DOM -> model validation
				ngModel.$parsers.unshift(function (value) {
					addValue(value);
					return value;
				});

				//For model -> DOM validation
				ngModel.$formatters.unshift(function (value) {
					addValue(value);
					return value;
				});

				function addValue(value) {
					wizEqualToSvc.addValue({
						name: attrs.ngModel,
						group: attrs.wizValEqualTo,
						value: value
					});
				}

				function validate() {
					var valid = wizEqualToSvc.isEqual(attrs.wizValEqualTo);
					ngModel.$setValidity('wizValEqualTo', valid);
				}

				scope.$watch(function () {
					return wizEqualToSvc.values;
				}, function () {
					validate();
				}, true);

				scope.$on('$destroy', function () {
					wizEqualToSvc.cleanup();
				});
			}
		};
	}]);
