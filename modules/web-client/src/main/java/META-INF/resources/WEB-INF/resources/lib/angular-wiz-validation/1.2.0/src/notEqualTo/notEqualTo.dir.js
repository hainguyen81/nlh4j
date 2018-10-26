angular.module('wiz.validation.notEqualTo')

	.directive('wizValNotEqualTo', ['wizNotEqualToSvc', function (wizNotEqualToSvc) {
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
					wizNotEqualToSvc.addValue({
						name: attrs.ngModel,
						group: attrs.wizValNotEqualTo,
						value: value
					});
				}

				function validate() {
					var valid = !wizNotEqualToSvc.isEqual(attrs.wizValNotEqualTo);
					ngModel.$setValidity('wizValNotEqualTo', valid);
				}

				scope.$watch(function () {
					return wizNotEqualToSvc.values;
				}, function () {
					validate();
				}, true);

				scope.$on('$destroy', function () {
					wizNotEqualToSvc.cleanup();
				});
			}
		};
	}]);
