angular.module('wiz.validation.unique')

	.service('wizUniqueSvc', ['$filter', function ($filter) {
		this.values = [];

		this.cleanup = function () {
			this.values = [];
		};

		this.addValue = function (value) {
			if (typeof value.value === "undefined") {
				value.value = "";
			}

			var existingValue = false;
			for (var i = 0; i < this.values.length; i++) {
				if (this.values[i].name === value.name) {
					this.values[i] = value;
					existingValue = true;
					break;
				}
			}

			if (!existingValue) {
				this.values.push(value);
			}
		};

		this.isUnique = function (group) {
			var isUnique = true;
			var groupValues = $filter('filter')(this.values, { group: group }, true);
			for (var i = 0; i < groupValues.length; i++) {
				if (!isUnique) {
					break;
				}

				for (var j = 0; j < groupValues.length; j++) {
					if (i === j) {
						continue;
					}

					if (groupValues[i].value === groupValues[j].value) {
						isUnique = false;
						break;
					}
				}
			}
			return isUnique;
		};
	}]);