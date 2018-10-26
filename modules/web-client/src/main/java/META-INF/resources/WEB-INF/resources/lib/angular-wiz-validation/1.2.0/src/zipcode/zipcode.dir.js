angular.module('wiz.validation.zipcode')

	.directive('wizValZipcode', function () {
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
						valid = /(^\d{5}-?\d{4}$)|(^\d{5}$)/.test(value);
					}

					ngModel.$setValidity('wizValZipcode', valid);
					return value;
				}
			}
		};
	});
