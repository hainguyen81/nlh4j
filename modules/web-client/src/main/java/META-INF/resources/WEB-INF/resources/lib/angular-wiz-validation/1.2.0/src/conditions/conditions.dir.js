angular.module('wiz.validation.conditions')

	.directive('wizValConditions', function () {
		return {
			restrict: 'A',
			require: 'ngModel',
			scope: {
				conditions: '=wizValConditions'
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

				if (typeof scope.conditions !== "undefined") {
					scope.$watch('conditions',
						function () {
							validate(ngModel.$viewValue);
						}, true);
				}

				function validate(value) {
					var valid = true;
					if (typeof scope.conditions !== "undefined") {
						for (var i = 0; i < scope.conditions.length; i++) {
							if (scope.conditions[i] === false) {
								valid = false;
								break;
							}
						}
					}

					ngModel.$setValidity('wizValConditions', valid);
					return value;
				}
			}
		};
	});
