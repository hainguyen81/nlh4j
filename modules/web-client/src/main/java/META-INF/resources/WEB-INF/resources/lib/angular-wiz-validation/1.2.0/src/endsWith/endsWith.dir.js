angular.module('wiz.validation.endsWith')

	.directive('wizValEndsWith', function () {
		return {
			restrict: 'A',
			require: 'ngModel',
			scope: {
				endsWith: '=wizValEndsWith'
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
					var valid = false;
					if (typeof value === "undefined") {
						value = "";
					}

					if (typeof scope.endsWith !== "undefined") {
						valid = value.indexOf(scope.endsWith, value.length - scope.endsWith.length) !== -1;
					}
					ngModel.$setValidity('wizValEndsWith', valid);
					return value;
				}
			}
		};
	});
