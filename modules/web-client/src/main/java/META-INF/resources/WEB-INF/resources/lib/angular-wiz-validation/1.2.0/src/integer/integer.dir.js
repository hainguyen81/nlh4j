angular.module('wiz.validation.integer')

	.directive('wizValInteger', function () {
		return {
			restrict: 'A',
			require: 'ngModel',
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
						valid = /^-?[0-9]+$/.test(value);
					}

					ngModel.$setValidity('wizValInteger', valid);
					return value;
				}
			}
		};
	});
