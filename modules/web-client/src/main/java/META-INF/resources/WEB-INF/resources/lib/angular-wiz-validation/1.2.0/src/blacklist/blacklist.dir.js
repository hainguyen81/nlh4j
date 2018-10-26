angular.module('wiz.validation.blacklist')

	.directive('wizValBlacklist', function () {
		return {
			restrict: 'A',
			require: 'ngModel',
			scope: { blacklist: '=wizValBlacklist' },
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

					if (typeof value === "undefined") {
						value = "";
					}

					if (typeof scope.blacklist !== "undefined") {
						for (var i = scope.blacklist.length - 1; i >= 0; i--) {
							if (value === scope.blacklist[i]) {
								valid = false;
								break;
							}
						}
					}
					ngModel.$setValidity('wizValBlacklist', valid);
					return value;
				}
			}
		};
	});