(function($) {
	var _debuger = false;
	function _debug(message) {
		if (_debuger && window && window.console) {
			try {
				window.console.log(message);
			}
			catch(e) {}
		}
	}
	function _debugStart(groupName) {
		if (_debuger && window && window.console) {
			try {
				window.console.group(groupName);
			}
			catch(e) {}
		}
	}
	function _debugEnd(groupName) {
		if (_debuger && window && window.console) {
			try {
				window.console.groupEnd(groupName);
			}
			catch(e) {}
		}
	}

	$.pxToCm = function(px) {
		if (isNaN(px)) return -1;

		var d = $("<div/>").css({
			position: 'absolute',
			top: '-1000cm',
			left: '-1000cm',
			height: '1000cm',
			width: '1000cm',
			display: 'none',
			visibility: 'hidden',
		}).appendTo('body');
		var px_per_cm = d.height() / 1000;
		d.remove();
		return px / px_per_cm;
	};

	$.cmToPx = function(cm) {
		if (isNaN(cm)) return -1;

		var d = $("<div/>").css({
			position: 'absolute',
			top: '-1000cm',
			left: '-1000cm',
			height: '1000cm',
			width: '1000cm',
			display: 'none',
			visibility: 'hidden',
		}).appendTo('body');
		var px_per_cm = d.height() / 1000;
		d.remove();
		return (cm * px_per_cm);
	};

	$.clearTimeouts = function(timeoutids) {
		if (!window.setTimeout || !window.clearTimeout) return;
		var GROUP_DEBUG = "UTILS - CLEAR TIMEOUT";
		_debugStart(GROUP_DEBUG);
		if (timeoutids != null && timeoutids != undefined
				&& timeoutids.constructor === Array && timeoutids.length > 0) {
			for(var id in timeoutids) {
				if (id == null || id == undefined) continue;
				_debug('Clear timeout [' + id + ']');
				window.clearTimeout(id);
			}
		}
		else {
			var id = window.setTimeout(null,0);
			while(id--) {
				_debug('Clear timeout [' + id + ']');
				window.clearTimeout(id);
			}
		}
		_debugEnd(GROUP_DEBUG);
	};

	$.clearAllTimeout = function() {
		$.clearTimeouts([]);
	};

	$.nvlElements = function(arrElems) {
		if (arrElems != null && arrElems != undefined
				&& arrElems.constructor === Array) {
			for(var i = 0; i < arrElems.length; i++) {
				var elem = arrElems[i];
				if (elem != null && elem != undefined && elem.length > 0) {
					return elem;
				}
			}
		}
		return [];
	};

	/**
	 * Detect the specified event data and return event moud position such as:
	 * { x: event.pageX (clientX), y: event.pageY (clientY) }
	 *
	 * @param event to parse (x, y < 0 if invalid)
	 *
	 * @reurn { x: valueX, y: valueY }
	 */
	$.eventPosition = function(event) {
		var body = document.body;
		var docEl = document.documentElement;
		var x = -1, y = -1;
		if (!event) event = window.event;
		// detect X
		if (event.pageX) {
			x = event.pageX;
		} else if (event.clientX) {
			x = event.clientX + body.scrollLeft + docEl.scrollLeft;
		} else if (event.originalEvent
				&& event.originalEvent.touches
				&& event.originalEvent.touches.length) {
			var touch = event.originalEvent.touches[0];
			if (touch.pageX) {
				x = touch.pageX;
			} else if (touch.clientX) {
				x = touch.clientX + body.scrollLeft + docEl.scrollLeft;
			}
		}
		// detect Y
		if (event.pageY) {
			y = event.pageY;
		} else if (event.clientY) {
			y = event.clientY + body.scrollTop + docEl.scrollTop;
		} else if (event.originalEvent
				&& event.originalEvent.touches
				&& event.originalEvent.touches.length) {
			var touch = event.originalEvent.touches[0];
			if (touch.pageY) {
				y = touch.pageY;
			} else if (touch.clientY) {
				y = touch.clientY + body.scrollTop + docEl.scrollTop;
			}
		}
		return { x: x, y: y };
	};

	/**
	 * Parse the parameter names of the present object if it's a function
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @return parameter names array or null/empty if it's not a function or it's has no any parameter
	 */
 	Object.defineProperty(
    		Object.prototype, 'parameterNames',
    		function() {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				if (typeof $this !== 'function') return [];
				var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
				var ARGUMENT_NAMES = /([^\s,]+)/g;
				var fnStr = $this.toString().replace(STRIP_COMMENTS, '');
				var result = fnStr.slice(fnStr.indexOf('(')+1, fnStr.indexOf(')')).match(ARGUMENT_NAMES);
				if (result === null) result = [];
				return result;
			});
	/**
	 * Get the parameter names of the specified function
	 *
	 * @param func to parse
	 *
	 * @return parameter names array or empty if failed
	 */
	$.getFunctionParamNames = function(func) {
		if (!func || typeof func !== 'function') return [];
		if (typeof func.parameterNames !== 'function') {
			var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
			var ARGUMENT_NAMES = /([^\s,]+)/g;
			var fnStr = func.toString().replace(STRIP_COMMENTS, '');
			var result = fnStr.slice(fnStr.indexOf('(')+1, fnStr.indexOf(')')).match(ARGUMENT_NAMES);
			if (result === null) result = [];
		} else {
			result = func.parameterNames();
		}
		return result;
	};

	/**
	 * Parse the function name of the present object if it's a function
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @return function name or null/empty if it's not a function
	 */
 	Object.defineProperty(
    		Object.prototype, 'functionName',
    		function() {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				if (typeof $this !== 'function') return null;
				var FUNC_NAME = /function (.{1,})\(/;
				var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
				var fnStr = $this.toString().replace(STRIP_COMMENTS, '');
				var results = (FUNC_NAME).exec(fnStr);
				return ((results && results.length > 1) ? results[1] : "");
			});
	/**
	 * Get the function name of the specified function
	 *
	 * @param func to parse
	 *
	 * @return function name or empty if failed or function has no name
	 */
	$.getFunctionName = function(func) {
		if (!func || typeof func !== 'function') return [];
		if (typeof func.functionName !== 'function') {
			var FUNC_NAME = /function (.{1,})\(/;
			var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
			var fnStr = func.toString().replace(STRIP_COMMENTS, '');
			var results = (FUNC_NAME).exec(fnStr);
			return ((results && results.length > 1) ? results[1] : "");
		} else {
			return func.functionName();
		}
	};

    /**
     * Check the prevent object (DIV) whether has horizontal scroll-bar (overflow)
     *
     * @return true for having scroll-bar; else false
     */
	$.fn.hasHorizontalScrollBar = function() {
        return this.get(0) ? this.get(0).scrollWidth > this.outerWidth() : false;
    };

    /**
     * Check the prevent object (DIV) whether has vertical scroll-bar (overflow)
     *
     * @return true for having scroll-bar; else false
     */
	$.fn.hasVerticalScrollBar = function() {
        return this.get(0) ? this.get(0).scrollHeight > this.outerHeight() : false;
    };

    /**
     * Check the prevent object (DIV) whether has horizontal/vertical scroll-bar (overflow)
     *
     * @return true for having scroll-bar; else false
     */
	$.fn.hasScrollBar = function() {
        return (this.hasHorizontalScrollBar() || this.hasVerticalScrollBar());
    };

    /**
	 * Check the specified object whether is NULL or UNDEFINED
	 *
	 * @param obj to check
	 *
	 * @return true for null/undefined; else false
	 */
	$.isUndefined = function(obj) {
		return (obj === null || obj === undefined || typeof obj === 'undefined');
	};

	/**
	 * Detect the present object instance whether is Date
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @return true for Date; else false
	 */
 	Object.defineProperty(
    		Object.prototype, 'isDateTime',
    		function() {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				return (Object.prototype.toString.call($this) === "[object Date]"
					&& typeof $this.getTime === 'function' && !isNaN($this.getTime()));
			});
    /**
     * Check the specified object whether is Date
     *
     * @param obj to check
     *
     * @return true for Date; else false
     */
	$.isDate = function(obj) {
		if (!$.isUndefined(obj) && typeof obj.isDateTime === 'function' && obj.isDateTime()) return true;
		return (Object.prototype.toString.call(obj) === "[object Date]"
			&& typeof obj.getTime === 'function' && !isNaN(obj.getTime()));
	};

	/**
	 * Detect the present object instance whether is Array and not empty
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @return true for not empty array; else false for not a array or empty
	 */
 	Object.defineProperty(
    		Object.prototype, 'isArrayNotEmpty',
    		function() {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				return (($this instanceof Array || Object.prototype.toString.call($this) === "[object Array]") && $this.length > 0);
			});
	/**
     * Check the specified object whether is Array and not empty
     *
     * @param obj to check
     *
     * @return true for not empty Array; else false
     */
	$.isNotEmptyArray = function(obj) {
		if (!$.isUndefined(obj) && typeof obj.isArrayNotEmpty === 'function' && obj.isArrayNotEmpty()) return true;
		return ((obj instanceof Array || Object.prototype.toString.call(obj) === "[object Array]") && obj.length > 0);
	};

	/**
	 * Detect the present object instance whether is Array and empty
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @return true for empty array; else false for not a array or not empty
	 */
 	Object.defineProperty(
    		Object.prototype, 'isArrayEmpty',
    		function() {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				return (($this instanceof Array || Object.prototype.toString.call($this) === "[object Array]") && $this.length <= 0);
			});
	/**
     * Check the specified object whether is Array and empty
     *
     * @param obj to check
     *
     * @return true for empty Array; else false
     */
	$.isEmptyArray = function(obj) {
		if (!$.isUndefined(obj) && typeof obj.isArrayEmpty === 'function' && obj.isArrayEmpty()) return true;
		return ((obj instanceof Array || Object.prototype.toString.call(obj) === "[object Array]") && obj.length <= 0);
	};

	/**
	 * Iterate object properties into stack
	 *
	 * @param obj to build
	 * @param stack for recursively
	 * @param properties returned property name array
	 */
	function _iteratePropertyNames(obj, stack, properties) {
		properties = (properties || []);
		stack = new String(stack || '');
		if (typeof obj == "object" && obj != null) {
	        for (var property in obj) {
	            if (obj.hasOwnProperty(property) && obj[property] != null) {
	            	var key = (stack.length ? (stack + '.' + property) : property);
	                if (typeof obj[property] == "object" && !$.isArray(obj[property])
	                		&& !$.isDate(obj[property])) {
	                	_iteratePropertyNames(obj[property], key, properties);
	                } else if ($.isArray(obj[property]) && obj[property].length > 0) {
                		var i = 0;
                		obj[property].forEach(function(o) {
                			_iterateSimpleObject(o, (key + '[' + i + ']'), newObj);
                			i++;
                		});
	                } else {
	                	properties.push(key);
	                }
	            }
	        }
		} else if (obj != null && stack.length > 0) {
			properties.push(stack);
		}
        return properties;
    };
    /**
	 * Parse property names (deep) of the present object
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @return property names array or empty array, such as:<br>
	 * [aProperty.aSetting1<br>
     * , aProperty.aSetting2<br>
     * , bProperty.bSetting1.bPropertySubSetting]
	 */
 	Object.defineProperty(
    		Object.prototype, 'simpleProperties',
    		function() {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				return _iteratePropertyNames($this, '', []);
			});
    /**
     * Build the property name arrays of the specified object such as:<br>
     * [aProperty.aSetting1<br>
     * , aProperty.aSetting2<br>
     * , bProperty.bSetting1.bPropertySubSetting]
     *
     * @param obj to build
     *
     * @return property names array or empty array
     */
    $.buildPropertyNames = function(obj) {
    	if (!$.isUndefined(obj) && typeof obj.simpleProperties === 'function') {
    		return obj.simpleProperties();
    	}
    	return _iteratePropertyNames(obj, '', []);
    };

	/**
     * Check the specified object whether has own property
     *
     * @param obj to check
     * @param propKey to check
     *
     * @return true for object not null/undefined and has property; else false
     */
	$.hasProperty = function(obj, propKey) {
		return (!$.isUndefined(obj) && !$.isUndefined(propKey) && obj.hasOwnProperty(propKey));
	};

    /**
	 * Iterate object properties into stack
	 *
	 * @param obj to build
	 * @param stack for recursively
	 * @param newObj returned new object with formatted:<br>
	 * {
	 * 	aProperty.aSetting1: value1<br>
     * , aProperty.aSetting2: value2<br>
     * , bProperty.bSetting1.bPropertySubSetting: value3
	 * }
	 */
	function _iterateSimpleObject(obj, stack, newObj) {
		newObj = (newObj || {});
		stack = new String(stack || '');
		if (typeof obj == "object" && obj != null) {
	        for (var property in obj) {
	            if (obj.hasOwnProperty(property) && obj[property] != null) {
	            	var key = (stack.length ? (stack + '.' + property) : property);
	                if (typeof obj[property] == "object" && !$.isArray(obj[property])
	                		&& !$.isDate(obj[property])) {
	                	_iterateSimpleObject(obj[property], key, newObj);
	                } else if ($.isArray(obj[property]) && obj[property].length > 0) {
                		var i = 0;
                		obj[property].forEach(function(o) {
                			_iterateSimpleObject(o, (key + '[' + i + ']'), newObj);
                			i++;
                		});
	                } else if ($.isDate(obj[property])) {
	                	if (typeof obj[property].toISOString === 'function') {
	                		newObj[key] = obj[property].toISOString();
						} else {
							newObj[key] = obj[property].getTime();
						}
	                } else {
	                	newObj[key] = obj[property];
	                }
	            }
	        }
		} else if (!$.isUndefined(obj) && stack.length > 0) {
			newObj[stack] = obj;
		}
        return newObj;
    };
    /**
	 * Convert the present object to simple object, such as:<br>
     * {
	 * 	aProperty.aSetting1: value1<br>
     * , aProperty.aSetting2: value2<br>
     * , bProperty.bSetting1.bPropertySubSetting: value3<br>
	 * }<br>
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @return simple object or empty
	 */
 	Object.defineProperty(
    		Object.prototype, 'simple',
    		function() {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				return _iterateSimpleObject($this, '', {});
			});
    /**
     * Build the simple object of the specified object such as:<br>
     * {
	 * 	aProperty.aSetting1: value1<br>
     * , aProperty.aSetting2: value2<br>
     * , bProperty.bSetting1.bPropertySubSetting: value3<br>
	 * }
     *
     * @param obj to build
     *
     * @return simple object or empty
     */
    $.buildSimpleObject = function(obj) {
    	if (!$.isUndefined(obj) && typeof obj.simple === 'function') {
    		return obj.simple();
    	}
    	return _iterateSimpleObject(obj, '', {});
    };

	/**
	 * Remove properties from object.<br>
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @param props property names to remove
	 *
	 * @return current object after removing
	 */
    Object.defineProperty(
    		Object.prototype, 'remove',
    		function(props) {
    			// FIXME Because jQuery conflict with native prototype
    		    // so using Object.defineProperty(Object.prototype, ...);
    		    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				if (Object.keys($this).length > 0
						&& $.isNotEmptyArray(props)) {
					props.forEach(function(k) {
						delete $this[k];
					});
				}
				return $this;
			});
 	/**
	 * Remove properties from object
	 *
	 * @param obj to remove
	 * @param props property names to remove
	 *
	 * @return current object after removing
	 */
	$.removeProperties = function(obj, props) {
 		if (!$.isUndefined(obj) && !$.isUndefined(props)
 				&& Object.prototype.toString.call(obj) === "[object]"
 					&& typeof obj.remove === 'function') {
 			return obj.remove(props);
 		}
 		if (Object.keys(obj).length > 0 && $.isNotEmptyArray(props)) {
			props.forEach(function(k) {
				delete obj[k];
			});
		}
		return obj;
 	};

	/**
	 * Pick properties from present object to new instance
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @param props property names to pick
	 *
	 * @return new object
	 */
 	Object.defineProperty(
    		Object.prototype, 'pick',
    		function(props) {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				var newObj = {};
				if (Object.keys($this).length > 0
						&& $.isNotEmptyArray(props)) {
					props.forEach(function(k) {
						newObj[k] = $this[k];
					});
				}
				return newObj;
			});
  	/**
	 * Pick properties from present object to new instance
	 *
	 * @param obj to pick
	 * @param props property names to pick
	 *
	 * @return new object
	 */
  	$.pickProperties = function(obj, props) {
  		if (!$.isUndefined(obj) && !$.isUndefined(props)
 				&& Object.prototype.toString.call(obj) === "[object]"
 					&& typeof obj.pick === 'function') {
  			return obj.pick(props);
  		}
  		var newObj = {};
		if (Object.keys(obj).length > 0
				&& $.isNotEmptyArray(props)) {
			props.forEach(function(k) {
				newObj[k] = obj[k];
			});
		}
		return newObj;
  	};

  	/**
	 * Check the existed properties from present object instance
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @param props property names to check
	 *
	 * @return true for existed
	 */
 	Object.defineProperty(
    		Object.prototype, 'exists',
    		function(props) {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				var existed = false;
				if (Object.keys($this).length > 0
						&& $.isNotEmptyArray(props)) {
					existed = true;
					for(var i = 0; i < props.length; i++) {
						existed = $this.hasOwnProperty(props[i]);
						if (!existed) break;
					}
				}
				return existed;
			});
 	/**
	 * Check the existed properties, at least one, from present object instance
	 * FIXME Because jQuery conflict with native prototype;<br>
	 * so using Object.defineProperty(Object.prototype, ...);<br>
	 * see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
	 *
	 * @param props property names to check
	 *
	 * @return true for existed
	 */
 	Object.defineProperty(
    		Object.prototype, 'existsOne',
    		function(props) {
				// FIXME Because jQuery conflict with native prototype
			    // so using Object.defineProperty(Object.prototype, ...);
			    // see http://stackoverflow.com/questions/32169408/userscript-issue-with-object-prototype
				var $this = this;
				var existed = false;
				if (Object.keys($this).length > 0
						&& $.isNotEmptyArray(props)) {
					for(var i = 0; i < props.length; i++) {
						existed = $this.hasOwnProperty(props[i]);
						if (existed) break;
					}
				}
				return existed;
			});
  	/**
	 * Check the existed properties from present object instance
	 *
	 * @param obj to pick
	 * @param props property names to check
	 *
	 * @return true for existed
	 */
  	$.existsProperties = function(obj, props) {
  		if (!$.isUndefined(obj) && !$.isUndefined(props)
 				&& Object.prototype.toString.call(obj) === "[object]"
 					&& typeof obj.exists === 'function') {
  			return obj.exists(props);
  		}
  		var existed = false;
		if (Object.keys(obj).length > 0
				&& $.isNotEmptyArray(props)) {
			existed = true;
			for(var i = 0; i < props.length; i++) {
				existed = obj.hasOwnProperty(props[i]);
				if (!existed) break;
			}
		}
		return existed;
  	};
  	/**
	 * Check the existed properties, at least one, from present object instance
	 *
	 * @param obj to pick
	 * @param props property names to check
	 *
	 * @return true for existed
	 */
  	$.existsOneProperties = function(obj, props) {
  		if (!$.isUndefined(obj) && !$.isUndefined(props)
 				&& Object.prototype.toString.call(obj) === "[object]"
 					&& typeof obj.existsOne === 'function') {
  			return obj.existsOne(props);
  		}
  		var existed = false;
		if (Object.keys(obj).length > 0
				&& $.isNotEmptyArray(props)) {
			for(var i = 0; i < props.length; i++) {
				existed = obj.hasOwnProperty(props[i]);
				if (existed) break;
			}
		}
		return existed;
  	};
})(jQuery);
