(function($) {
	// clear array
	if (!Array.prototype.clear) {
		/**
		 * Remove all items
		 */
		Array.prototype.clear = function() {
		    this.splice(0, this.length);
		};
	}

	// inject item/array at the specified index
	if (!Array.prototype.inject) {
		/**
		 * Inject item/array at the specified index of array
		 *
		 * @param idx to inject
		 * @param items item/array to inject
		 */
		Array.prototype.inject = function(idx, items) {
			var subItems = [];
			if (items instanceof Array || Object.prototype.toString.call(items) === "[object Array]") {
				subItems = [].concat(items);
			} else if (items) {
				subItems.push(items);
			}
		    if (subItems.length && !isNaN(idx) && 0 <= idx && idx < this.length) {
		    	this.splice.apply(this, [idx + 1, 0].concat(subItems));
		    }
		};
	}

	// remove item at the specified index
	if (!Array.prototype.removeAt) {
		/**
		 * Remove item at the specified index out of array
		 *
		 * @param idx to remove
		 * @param cnt number items to remove
		 *
		 * @return item has been removed or null if invalid index
		 */
		Array.prototype.removeAt = function(idx, cnt) {
			var item = null;
			cnt = (isNaN(cnt) || cnt <= 0 ? 1 : cnt);
		    if (!isNaN(idx) && 0 <= idx && idx < this.length) {
		    	item = this[idx];
		    	cnt = Math.min(cnt, this.length - idx);
		    	this.splice(idx, cnt);
		    }
		    return item;
		};
	}

	// remove items array out of array
	if (!Array.prototype.removeArray) {
		/**
		 * Remove items out of array
		 *
		 * @param items to remove
		 *
		 * @return the number items has been removed
		 */
		Array.prototype.removeArray = function(items) {
			var cnt = 0;
			var subItems = [];
			if (items instanceof Array || Object.prototype.toString.call(items) === "[object Array]") {
				subItems = [].concat(items);
			} else if (items) {
				subItems.push(items);
			}
		    if (subItems.length) {
		    	for(var i = 0; i < subItems.length; i++) {
		    		cnt += (this.remove(subItems[i]) >= 0 ? 1 : 0);
		    	}
		    }
		    return cnt;
		};
	}

	// remove item out of array
	if (!Array.prototype.remove) {
		/**
		 * Remove item out of array
		 *
		 * @param item to remove
		 *
		 * @return index of removed item or -1 if not found
		 */
		Array.prototype.remove = function(item) {
			var idx = -1;
		    if (this.indexOf(item) >= 0) {
		    	idx = this.indexOf(item);
		    	if (0 <= idx && idx < this.length) this.splice(idx, 1);
		    	else idx = -1;
		    }
		    return idx;
		};
	}

	// require first item
	if (!Array.prototype.first) {
		/**
		 * Get the first item from array
		 *
		 * @return the first item or null if array is empty
		 */
		Array.prototype.first = function() {
			return (this.length <= 0 ? null : this[0]);
		};
	}

	// require last item
	if (!Array.prototype.last) {
		/**
		 * Get the last item from array
		 *
		 * @return the last item or null if array is empty
		 */
		Array.prototype.last = function() {
			return (this.length <= 0 ? null : this[this.length - 1]);
		};
	}

	// add item into array at index
	if (!Array.prototype.add) {
		/**
		 * Add the specified item into array at index
		 *
		 * @param item to add
		 * @param idx index for new item. <= 0 for first index. >= array length for last index
		 */
		Array.prototype.add = function(item, idx) {
			idx = (isNaN(idx) || idx <= 0 ? 0 : idx);
			idx = (idx >= this.length ? this.length : idx);
			this.splice(idx, 0, item);
		};
	}

	// remove duplicated items out of array (unique items)
	if (!Array.prototype.unique) {
		/**
		 * Clone an unique array (not duplicated items)
		 *
		 * @return uniqued array
		 */
		Array.prototype.unique = function() {
		    var a = this.concat();
		    if (this.length) {
			    for(var i = 0; i < a.length; i++) {
			        for(var j = i + 1; j < a.length; j++) {
			            if (a[i] === a[j]) a.splice(j--, 1);
			        }
			    }
		    }
		    return a;
		};
	}

	// sort array with a item or item key
	if (!Array.prototype.sortBy) {
		/**
		 * Sort array by item key
		 *
		 * @param desc descending
		 * @param key item key. NULL/empty for sorting by item
		 */
		Array.prototype.sortBy = function(desc, key) {
			if (this.length) {
				this.sort(function(it1, it2) {
					return desc ? ~~(key && key.length ? it1[key] < it2[key] : it1 < it2)
							: ~~(key ? it1[key] > it2[key] : it1 > it2);
				});
			}
		};
	}

	// check array whether contains item
	if (!Array.prototype.contain) {
		/**
		 * Check present array whether contains the specified item
		 *
		 * @param item to check
		 *
		 * @reurn true for containing; else false
		 */
		Array.prototype.contain = function(item) {
			return (this.length && this.indexOf(item) >= 0);
		};
	}

	// check the present array difference with the specified array
	if (!Array.prototype.diff) {
		Array.prototype.diff = function(a) {
			var isDiff = true;
			if (a instanceof Array || Object.prototype.toString.call(a) === "[object Array]") {
				var b = this;
				isDiff = (b.length != a.length);
				if (!isDiff) {
					for(var i = 0; i < a.length; i++) {
						if (!b.contain(a[i])) {
							isDiff = true;
							break;
						}
					}
				}
			}
			return isDiff;
		};
	}
})(jQuery);
