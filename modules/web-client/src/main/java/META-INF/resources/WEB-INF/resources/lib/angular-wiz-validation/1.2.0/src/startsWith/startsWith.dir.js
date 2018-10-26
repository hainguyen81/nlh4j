angular.module('wiz.validation.startsWith')

	.directive('wizValStartsWith', function () {
		return {
			restrict: 'A',
			require: 'ngModel',
			scope: {
				startsWith: '=wizValStartsWith'
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
					if (typeof value === "undefined") {
						value = "";
					}

					var valid = value.lastIndexOf(scope.startsWith, 0) === 0;
					ngModel.$setValidity('wizValStartsWith', valid);
					return value;
				}
			}
		};
	});
