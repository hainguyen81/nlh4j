angular.module('wiz.validation.dateOfBirth')

	.directive('wizValDateOfBirth', function () {
		return {
			restrict: 'A',
			require: 'ngModel',
			scope: {
				wizValDateOfBirth: '=wizValDateOfBirth'
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
						if (value && /^\d+$/.test(scope.wizValDateOfBirth)) {
							// If positive integer used for age then use to check input value
							var today = new Date();
							var birthDate = new Date(value);
							var age = today.getFullYear() - birthDate.getFullYear();
							var m = today.getMonth() - birthDate.getMonth();
							if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
								age--;
							}
							if (age < scope.wizValDateOfBirth) {
								valid = false;
							}
						}
					}

					ngModel.$setValidity('wizValDateOfBirth', valid);
					return value;
				}
			}
		};
	});
