angular.module('wiz.validation.decimal')

	.directive('wizValDecimal', function () {
		return {
			restrict: 'A',
			require: 'ngModel',
			scope: {
				decimalPlaces: '=wizValDecimal'
			},
			link: function (scope, elem, attrs, ngModel) {

				//For DOM -> model validation
				ngModel.$parsers.unshift(function (value) {
					return validate(value);
				});

				//For model -> DOM validation
				ngModel.$formatters.unshift(function (value) {
					return validate(value);
				});

				function validate(value) {
					var valid = true;
					if (angular.isDefined(value) && value.length > 0) {
						var pattern = "^-?([0-9]+)\\.([0-9]+)$";
						if (/^-?[0-9]+$/.test(scope.decimalPlaces)) {
							pattern = "^-?([0-9]+)\\.([0-9]{1," + scope.decimalPlaces + "})$";
						}
						var regEx = new RegExp(pattern);
						valid = regEx.test(value);
					}
					ngModel.$setValidity('wizValDecimal', valid);
					return value;
				}
			}
		};
	});
