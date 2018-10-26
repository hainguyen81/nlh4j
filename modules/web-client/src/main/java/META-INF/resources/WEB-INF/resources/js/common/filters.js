'use strict';

//filter for formatting string
importFilter(
		"stringFormat",
		function($log) {
			return function(str) {
				if (!str || arguments.length <=1 ) return str;
				var args = arguments;
				for (var i = 1; i < arguments.length; i++) {
					var reg = new RegExp("\\{" + (i - 1) + "\\}", "gm");
					str = str.replace(reg, arguments[i]);
				}
				return str;
			}
		});

//filter for replacing string
importFilter(
		"replace",
		function($log) {
			return function(str, pattern, replacement, global) {
				global = (typeof global == 'undefined' ? true : global);
				try {
					str = str ? (typeof global == 'string' ? str : str.toString()) : '';
					return str.replace(new RegExp(pattern, global ? "g" : ""), function(match, group) {
						return replacement;
					});
				} catch (e) {
					($log && typeof $log.error === 'function')
					&& $log.error("error in string.replace", e);
					return (str || '');
				}
			}
		});

//filter for padding number with 0
importFilter(
		'lpad',
		function () {
			return function (n, len) {
				var num = parseInt(n, 10);
				len = parseInt(len, 10);
				if (isNaN(num) || isNaN(len)) return n;
				num = '' + num;
				while (num.length < len) num = '0' + num;
				return num;
			};
		});

//filter for filtering object property by equals
importFilter(
		'propsEqualFilterFirst',
		function() {
			// TODO use memoize for avoiding: 10 $digest() iterations reached. Aborting!
			return _.memoize(function(items, props) {
				var found = null;
				var expression = (props && props.expression ? props.expression || {} : {});
				if (angular.isArray(items) && Object.keys(expression).length > 0) {
					items.forEach(function(item) {
						if (found == null) {
							var itemMatches = false;
							var keys = Object.keys(expression);
							for (var i = 0; i < keys.length; i++) {
								var prop = keys[i];
								var text = (typeof expression[prop] === 'string'
									? expression[prop].toLowerCase()
											: expression[prop] === null || expression[prop] == undefined
											? '' : expression[prop].toString());
								var ittext = (typeof item[prop] === 'string'
									? item[prop].toLowerCase() : item[prop] === null || item[prop] == undefined
											? '' : item[prop].toString());
								// FIXME searching base on Latin character
								var latinText = (text.length > 0 && !text.isLatin() ? text.latinize() : text);
								var latinIttext = (ittext.length > 0 && !ittext.isLatin() ? ittext.latinize() : ittext);
								if (text.length <= 0
										|| (ittext.length > 0 && ittext.toLowerCase() === text.toLowerCase())
										|| (ittext.length > 0 && ittext.toLowerCase() === latinText.toLowerCase())
										|| (latinIttext.length > 0 && latinIttext.toLowerCase() === text.toLowerCase())
										|| (latinIttext.length > 0 && latinIttext.toLowerCase() === latinText.toLowerCase())) {
									found = item;
									break;
								}
							}
						}
					});
				}
				return found;
			}, function(items, props) {
				var cnt = (angular.isArray(items) ? items.length : 0);
				var key = (props && props.filterKey ? props.filterKey.toString() : '');
				var expression = (props && props.expression ? props.expression || {} : {});
				if (Object.keys(expression).length > 0) {
					var keys = Object.keys(expression);
					cnt += keys.length;
					keys.forEach(function(prop) {
						var text = (typeof expression[prop] === 'string'
							? expression[prop].toLowerCase()
									: expression[prop] === null || expression[prop] == undefined
									? '' : expression[prop].toString());
						cnt += prop.length + text.length + '-' + text;
						if (items && items.length) {
							items.forEach(function(it) {
								var itText = (typeof it[prop] === 'string'
									? it[prop].toLowerCase()
											: it[prop] === null || it[prop] == undefined
											? '' : it[prop].toString());
								cnt += '-' + itText.length + '-' + itText;
							});
						}
					});
				}
				return (key + '-' + cnt);
			});
		});

//filter for filtering object property by equals
importFilter(
		'propsEqualFilter',
		function() {
			// TODO use memoize for avoiding: 10 $digest() iterations reached. Aborting!
			return _.memoize(function(items, props) {
				var out = [];
				var expression = (props && props.expression ? props.expression || {} : {});
				if (angular.isArray(items) && Object.keys(expression).length > 0) {
					items.forEach(function(item) {
						var itemMatches = false;
						var keys = Object.keys(expression);
						for (var i = 0; i < keys.length; i++) {
							var prop = keys[i];
							var text = (typeof expression[prop] === 'string'
								? expression[prop].toLowerCase()
										: expression[prop] === null || expression[prop] == undefined
										? '' : expression[prop].toString());
							var ittext = (typeof item[prop] === 'string'
								? item[prop].toLowerCase() : item[prop] === null || item[prop] == undefined
										? '' : item[prop].toString());
							// FIXME searching base on Latin character
							var latinText = (text.length > 0 && !text.isLatin() ? text.latinize() : text);
							var latinIttext = (ittext.length > 0 && !ittext.isLatin() ? ittext.latinize() : ittext);
							if (text.length <= 0
									|| (ittext.length > 0 && ittext.toLowerCase() === text.toLowerCase())
									|| (ittext.length > 0 && ittext.toLowerCase() === latinText.toLowerCase())
									|| (latinIttext.length > 0 && latinIttext.toLowerCase() === text.toLowerCase())
									|| (latinIttext.length > 0 && latinIttext.toLowerCase() === latinText.toLowerCase())) {
								out.push(item);
								break;
							}
						}
					});
				} else out = items;
				return out;
			}, function(items, props) {
				var cnt = (angular.isArray(items) ? items.length : 0);
				var key = (props && props.filterKey ? props.filterKey.toString() : '');
				var expression = (props && props.expression ? props.expression || {} : {});
				if (Object.keys(expression).length > 0) {
					var keys = Object.keys(expression);
					cnt += keys.length;
					keys.forEach(function(prop) {
						var text = (typeof expression[prop] === 'string'
							? expression[prop].toLowerCase()
									: expression[prop] === null || expression[prop] == undefined
									? '' : expression[prop].toString());
						cnt += prop.length + text.length + '-' + text;
						if (items && items.length) {
							items.forEach(function(it) {
								var itText = (typeof it[prop] === 'string'
									? it[prop].toLowerCase()
											: it[prop] === null || it[prop] == undefined
											? '' : it[prop].toString());
								cnt += '-' + itText.length + '-' + itText;
							});
						}
					});
				}
				return (key + '-' + cnt);
			});
		});

//filter for filtering object property
importFilter(
		'propsFilter',
		function() {
			// TODO use memoize for avoiding: 10 $digest() iterations reached. Aborting!
			return _.memoize(function(items, props) {
				var out = [];
				var expression = (props && props.expression ? props.expression || {} : {});
				if (angular.isArray(items) && Object.keys(expression).length > 0) {
					items.forEach(function(item) {
						var itemMatches = false;
						var keys = Object.keys(expression);
						for (var i = 0; i < keys.length; i++) {
							var prop = keys[i];
							var text = (typeof expression[prop] === 'string'
								? expression[prop].toLowerCase()
										: expression[prop] === null || expression[prop] == undefined
										? '' : expression[prop].toString());
							var ittext = (typeof item[prop] === 'string'
								? item[prop].toLowerCase() : item[prop] === null || item[prop] == undefined
										? '' : item[prop].toString());
							// FIXME searching base on Latin character
							var latinText = (text.length > 0 && !text.isLatin() ? text.latinize() : text);
							var latinIttext = (ittext.length > 0 && !ittext.isLatin() ? ittext.latinize() : ittext);
							if (text.length <= 0
									|| (ittext.length > 0 && ittext.toLowerCase().indexOf(text) !== -1)
									|| (ittext.length > 0 && ittext.toLowerCase().indexOf(latinText) !== -1)
									|| (latinIttext.length > 0 && latinIttext.toLowerCase().indexOf(text) !== -1)
									|| (latinIttext.length > 0 && latinIttext.toLowerCase().indexOf(latinText) !== -1)) {
								out.push(item);
								break;
							}
						}
					});
				} else out = items;
				return out;
			}, function(items, props) {
				var cnt = (angular.isArray(items) ? items.length : 0);
				var key = (props && props.filterKey ? props.filterKey.toString() : '');
				var expression = (props && props.expression ? props.expression || {} : {});
				if (props && Object.keys(expression).length > 0) {
					var keys = Object.keys(expression);
					cnt += keys.length;
					keys.forEach(function(prop) {
						var text = (typeof expression[prop] === 'string'
							? expression[prop].toLowerCase()
									: expression[prop] === null || expression[prop] == undefined
									? '' : expression[prop].toString());
						cnt += prop.length + text.length + '-' + text;
						if (items && items.length) {
							items.forEach(function(it) {
								var itText = (typeof it[prop] === 'string'
									? it[prop].toLowerCase()
											: it[prop] === null || it[prop] == undefined
											? '' : it[prop].toString());
								cnt += '-' + itText.length + '-' + itText;
							});
						}
					});
				}
				return (key + '-' + cnt);
			});
		});

//filter for filtering object property
importFilter(
		'ignoreCasePropsFilter',
		function($log) {
			// TODO use memoize for avoiding: 10 $digest() iterations reached. Aborting!
			return _.memoize(function(items, props) {
				var out = [];
				if (angular.isArray(items) && items.length > 0
						&& Object.keys(props).length > 0) {
					var ignoreCase = (props.hasOwnProperty('ignoreCase') ? props['ignoreCase'] : true);
					ignoreCase = (typeof ignoreCase === 'boolean' ? ignoreCase : true);
					var equals = (props.hasOwnProperty('equals') ? props['equals'] : false);
					equals = (typeof equals === 'boolean' ? equals : false);
					var ascii = (props.hasOwnProperty('ascii') ? props['ascii'] : true);
					ascii = (typeof ascii === 'boolean' ? ascii : true);
					var exclude = (props.hasOwnProperty('exclude') ? props['exclude'] : '');
					exclude = (typeof exclude === 'string'
						? (ignoreCase ? (exclude || '').toLowerCase() : (exclude || ''))
								: (ignoreCase ? (exclude || '').toString().toLowerCase() : (exclude || '').toString()));
					var keyword = (props.hasOwnProperty('keyword') ? props['keyword'] : '');
					keyword = (typeof keyword === 'string'
						? (ignoreCase ? (keyword || '').toLowerCase() : (keyword || ''))
								: (ignoreCase ? (keyword || '').toString().toLowerCase() : (keyword || '').toString()));
					// FIXME searching base on Latin character
					var latinKeyword = (keyword.length > 0 && !keyword.isLatin() ? keyword.latinize() : keyword);
					var latinExKeyword = (exclude.length > 0 && !exclude.isLatin() ? exclude.latinize() : exclude);
					($log && typeof $log.debug === 'function')
					&& $log.debug('>>> Filter keyword: [', keyword, ']-[', latinKeyword, ']'
							, ' - exclude: [', exclude, ']-[', latinExKeyword, ']'
							, ' - case insensitive: ', ignoreCase
							, ' - ascii: ', ascii, ' - equals: ', equals);
					var keys = (props.hasOwnProperty('fields') ? props['fields'] : []);
					keys = (angular.isArray(keys) ? keys : []);
					if (keyword.length > 0 && keys.length > 0) {
						angular.forEach(items, function(item, key) {
							angular.forEach(item, function(v, k) {
								if (keys.contain(k)) {
									var ittext = (typeof v === 'string'
										? (ignoreCase ? v.toLowerCase() : v)
												: v === null || v == undefined
												? '' : (ignoreCase ? v.toString().toLowerCase() : v.toString()));
									// FIXME searching base on Latin character
									var latinIttext = (ittext.length > 0 && !ittext.isLatin() ? ittext.latinize() : ittext);
									($log && typeof $log.debug === 'function')
									&& $log.debug('>>> Item[' + k + ']: [' + ittext + ']-[' + latinIttext + ']');
									if (!out.contain(item)
											&& (((!equals && ittext.indexOf(keyword) >= 0) || ittext == keyword)
													|| (ascii && ((!equals && ittext.indexOf(latinKeyword) >= 0) || ittext == latinKeyword))
													|| (ascii && ((!equals && latinIttext.indexOf(keyword) >= 0) || latinIttext == keyword))
													|| (ascii && ((!equals && latinIttext.indexOf(latinKeyword) >= 0) || latinIttext == latinKeyword)))
											&& (!latinExKeyword.length || latinIttext.indexOf(latinExKeyword) < 0)) {
										($log && typeof $log.debug === 'function')
										&& $log.debug('--> Found [' + k + ']: [' + ittext + ']-[' + latinIttext + ']');
										out.push(item);
									}
								}
							});
						});
					} else out = items;
				} else out = items;
				return out;
			}, function(items, props) {
				var ignoreCase = (props && props.hasOwnProperty('ignoreCase') ? props['ignoreCase'] : true);
				ignoreCase = (typeof ignoreCase === 'boolean' ? ignoreCase : true);
				var equals = (props && props.hasOwnProperty('equals') ? props['equals'] : false);
				equals = (typeof equals === 'boolean' ? equals : false);
				var ascii = (props.hasOwnProperty('ascii') ? props['ascii'] : true);
				ascii = (typeof ascii === 'boolean' ? ascii : true);
				var key = (props && props.filterKey ? props.filterKey.toString() : '');
				var keyword = (props && props.keyword ? props.keyword.toString() : '');
				var exclude = (props.hasOwnProperty('exclude') ? props['exclude'] : '');
				var cnt = ((angular.isArray(items) ? items.length : 0)
						+ (ignoreCase ? 1 : 0) + (equals ? 1 : 0) + (ascii ? 1 : 0)
						+ (props && props.keyword ? props.keyword.toString().length : 0)
						+ (props && props.exclude ? props.exclude.toString().length : 0)
						+ (props && angular.isArray(props.fields) ? props.fields.length : 0));
				if (props && angular.isArray(props.fields)) {
					props.fields.forEach(function(f) {
						cnt += (f && f.length > 0 ? f.toString().length : 0);
						if (items && items.length) {
							items.forEach(function(it) {
								var itText = (typeof it[f] === 'string'
									? it[f].toLowerCase()
											: it[f] === null || it[f] == undefined
											? '' : it[f].toString());
								cnt += '-' + f + '-' + itText.length + '-' + itText;
							});
						}
					});
				}
				return (key + '-' + keyword + '-' + cnt);
			});
		});

// filter for removing currency fraction
importFilter(
		'noFractionCurrency',
		function(filter, locale) {
			var currencyFilter = filter('currency');
			var formats = locale.NUMBER_FORMATS;
			return function(amount, currencySymbol) {
				var value = currencyFilter(amount, currencySymbol);
				var sep = value.indexOf(formats.DECIMAL_SEP);
				if (amount >= 0) {
					return value.substring(0, sep);
				}
				return value.substring(0, sep) + ')';
			};
		});

// filter for formatting the gender human
importFilter(
		'genderFormat',
		function genderFormat() {
			return function(gender) {
				if (gender == 'M' || gender == 0 || gender == '0') {
					return 'Male';
				} else if (gender == 'F' || gender == 1 || gender == '1') {
					return 'Female';
				}
				return 'Unkown';
			};
		});

//Number Filter
importFilter(
		'numberFilter',
		function($filter) {
			return function(num, fractionSize) {
				fractionSize = fractionSize || 2;
				return num || num == 0 ? $filter('number')(num, fractionSize) : num;
			};
		});

//roundFilter
importFilter('roundFilter', function ($filter) {
    return function (input, fractionSize, roundMode) {

    	if (isNaN(input)) return input;

    	if(angular.equals(input, 0) || input == 0 || input == '0') return input;

    	// In case ROUND_UP
    	if(angular.equals(roundMode, 'ROUND_UP')) {

    		var factor = "1" + Array(+(parseInt(fractionSize) > 0 && parseInt(fractionSize) + 1)).join("0");
    		return $filter('number')(Math.round(input * factor) / factor, parseInt(fractionSize));

    	} else if(angular.equals(roundMode, 'ROUND_DOWN')) {
    		//var number = String(input).split(".");
    		if(input != null) {
    			return input.toFixed(fractionSize || 0);
    		}
    		return input;
    	} else if(angular.equals(roundMode, 'ROUND_OFF')) {
    		// In case ROUND_OFF
    		if(input != null) {
    			return $filter('number')(input.toFixed(fractionSize || 0));
    		}
    		return input;
    	}

    	return $filter('number')(input, parseInt(fractionSize));
    };
});


//Time Filter
importFilter(
		'time',
		function($scope, $filter) {
			return function(value, format) {
				if (value == null || value.split(":").length !== 3) {
					return '';
				}

				var parts = value.split(":");
				angular.isNumber(parts[0]);
				if (isNaN(parseInt(parts[0])) || isNaN(parseInt(parts[1]))
						|| parseInt(parts[0]) < 0 || parseInt(parts[0]) > 23
						|| parseInt(parts[1]) < 0 || parseInt(parts[1]) > 59) {
					return '';
				}
				format = format || 'HH:mm:ss.SSS';
				return value ? $scope.formatDate(new Date(1970, 0, 1, parseInt(parts[0]), parseInt(parts[1]), 0), format) : value;
			};
		});

/**
 * Description:
 *     removes white space from text. useful for html values that cannot have spaces
 * Usage:
 *   {{some_text | nospace}}
 */
importFilter('nospace', function () {
    return function (value) {
        return (!value) ? '' : value.replace(/ /g, '');
    };
});

importFilter('truncate', function() {
	return function(text, length, end) {
		if (text !== undefined) {
			if (isNaN(length)) {
				length = 10;
			}

			end = end || "...";

			if (text.length <= length || text.length - end.length <= length) {
				return text;
			} else {
				return String(text).substring(0, length - end.length) + end;
			}
		}
	};
});


// Example Filter
importFilter('ExampleFilter', function() {
	return function(input) {
		return input ? '\u2713' : '\u2718';
	};
});
