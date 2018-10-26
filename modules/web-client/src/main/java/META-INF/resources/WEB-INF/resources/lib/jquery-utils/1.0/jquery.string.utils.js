(function($) {
	// get the number bytes of present string
	if (!String.prototype.getBytes) {
		/**
		 * Get the number bytes of present string
		 * @return the number bytes of present string
		 */
		String.prototype.getBytes = function() {
			var b = [], i, unicode;
		    for(i = 0; i < this.length; i++) {
		        unicode = this.charCodeAt(i);
		        // 0x00000000 - 0x0000007f -> 0xxxxxxx
		        if (unicode <= 0x7f) {
		            b.push(String.fromCharCode(unicode));
		        }
		        // 0x00000080 - 0x000007ff -> 110xxxxx 10xxxxxx
		        else if (unicode <= 0x7ff) {
		            b.push(String.fromCharCode((unicode >> 6) | 0xc0));
		            b.push(String.fromCharCode((unicode & 0x3F) | 0x80));
		        }
		        // 0x00000800 - 0x0000ffff -> 1110xxxx 10xxxxxx 10xxxxxx
		        else if (unicode <= 0xffff) {
		            b.push(String.fromCharCode((unicode >> 12) | 0xe0));
		            b.push(String.fromCharCode(((unicode >> 6) & 0x3f) | 0x80));
		            b.push(String.fromCharCode((unicode & 0x3f) | 0x80));
		        }
		        // 0x00010000 - 0x001fffff -> 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
		        else {
		            b.push(String.fromCharCode((unicode >> 18) | 0xf0));
		            b.push(String.fromCharCode(((unicode >> 12) & 0x3f) | 0x80));
		            b.push(String.fromCharCode(((unicode >> 6) & 0x3f) | 0x80));
		            b.push(String.fromCharCode((unicode & 0x3f) | 0x80));
		        }
		    }
		    return b;
		};
	}

	// detect present string whether is alphabet and numeric
	if (!String.prototype.isAlphaNumeric) {
		/**
		 * Detect present string whether is alphabet and numeric
		 * @return true for alphabet numeric
		 */
		String.prototype.isAlphaNumeric = function() {
			var filter = /[^0-9a-z\xDF-\xFF]/i;
			return !filter.test(this);
		};
	}

	// detect present string whether is multi-bytes (unicode) string
	if (!String.prototype.isMultiBytes) {
		/**
		 * Detect present string whether is multi-bytes (unicode) string
		 * @return true for unicode
		 */
		String.prototype.isMultiBytes = function() {
			var b = [], i, unicode;
			for(i = 0; i < this.length; i++) {
				unicode = new String(this.charAt(i));
				b = unicode.getBytes();
				if (b && b.length >= 2) {
					return true;
				}
			}
			return false;
		};
	}

	// get the bytes length of the present string
	if (!String.prototype.bytesLength) {
		/**
		 * Get the bytes length of the present string
		 * @return the bytes length of the present string
		 */
		String.prototype.bytesLength = function() {
			var b = this.getBytes();
			return (b && b.length || 0);
		};
	}

	// detect present string whether is formatted email string
	if (!String.prototype.isEmail) {
		/**
		 * Detect present string whether is formatted email string
		 * @return true for email
		 */
		String.prototype.isEmail = function() {
			var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
			return filter.test(this);
		};
	}

	// detect present string whether is CSV file path/name
	if (!String.prototype.isCsvFile) {
		/**
		 * Detect present string whether is CSV file path/name
		 * @return true for CSV file
		 */
		String.prototype.isCsvFile = function() {
			var filter = /(\.|\/)(csv)$/i;
			return (!$.isBlank(this) && filter.test(this));
		};
	}

	// Convert the present string to integer value
	if (!String.prototype.toInt) {
		/**
		 * Convert the present string to integer value
		 * @return integer value or null
		 */
		String.prototype.toInt = function() {
			try { return (isNaN(this) ? null : parseInt(this)); }
			catch(e) { return null; }
		};
	}

	// Convert the present string to integer value
	if (!String.prototype.toFloat) {
		/**
		 * Convert the present string to integer value
		 * @return integer value or null
		 */
		String.prototype.toFloat = function() {
			try { return (isNaN(this) ? null : parseFloat(this)); }
			catch(e) { return null; }
		};
	}

	// Convert the present string to Date object (require datetime.utils)
	if (!String.prototype.toDateTime) {
		/**
		 * Convert the present string to Date object (require datetime.utils)
		 *
		 * @param pattern date pattern
		 *
		 * @return Date object or null
		 */
		String.prototype.toDateTime = function(pattern) {
			try { return (Date.toDate ? Date.toDate(this, pattern) : null); }
			catch (e) { return null; }
		};
	}

	// Measure the width in pixels of the present string under CSS font
	if (!String.prototype.mesurePxWidth) {
		/**
		 * Measure the width in pixels of the present string under CSS font
		 *
		 * @param cssFonts CSS fonts configuration to measure
		 *
		 * @return the width in pixels
		 */
		String.prototype.mesurePxWidth = function(cssFonts) {
			cssFonts = $.extend({}, cssFonts);
			$body = $('body');
			var calc = '<div style="clear:both;display:block;visibility:hidden;">'
				+ '<span style="width: auto;inherit;margin:0;">' + this + '</span></div>';
			$body.append(calc);
			var width = $('body').find('span:last').css(cssFonts).width();
			$body.find('span:last').parent().remove();
			return width;
		};
	}

	// Measure the height in pixels of the present string under CSS font
	if (!String.prototype.mesurePxHeight) {
		/**
		 * Measure the height in pixels of the present string under CSS font
		 *
		 * @param cssFonts CSS fonts configuration to measure
		 *
		 * @return the height in pixels
		 */
		String.prototype.mesurePxHeight = function(cssFonts) {
			cssFonts = $.extend({}, cssFonts);
			$body = $('body');
			var calc = '<div style="clear:both;display:block;visibility:hidden;">'
				+ '<span style="height: auto;inherit;margin:0;">' + this + '</span></div>';
			$body.append(calc);
			var height = $('body').find('span:last').css(cssFonts).height();
			$body.find('span:last').parent().remove();
			return height;
		};
	}

	// Fill the present string to the length with the specified character
	if (!String.prototype.leftPad) {
		/**
		 * Fill the present string to the length with the specified character
		 *
		 * @param c to fill. NULL/empty for space
		 * @param length fit length
		 *
		 * @return fitted string
		 */
		String.prototype.leftPad = function(c, length) {
			var s = this;
			if (s.length <= length) return s;
			c = (!!c && c.length > 0 ? c : ' ');
			while(s.length < length) {
				s = c + s;
			}
			return s;
		};
	}

	// Parse plain text from HTML
	if (!String.prototype.toPlainText) {
		/**
		 * Parse plain text from HTML
		 *
		 * @param s parse plain text
		 *
		 * @return plain text
		 */
		String.prototype.toPlainText = function() {
			return this.replace(/<[^>]+>/gm, '');
		};
	}

	// detect present string whether is latin, and convert it to latin formart
	if (!String.prototype.latinise || !String.prototype.latinize || !String.prototype.isLatin) {
		/**
		 * Detect present string whether is multi-bytes (unicode) string
		 * @return true for unicode
		 */
		var Latinise = {};
		Latinise.latin_map = {"Á":"A","Ă":"A","Ắ":"A","Ặ":"A","Ằ":"A","Ẳ":"A","Ẵ":"A","Ǎ":"A","Â":"A","Ấ":"A","Ậ":"A","Ầ":"A","Ẩ":"A","Ẫ":"A","Ä":"A","Ǟ":"A","Ȧ":"A","Ǡ":"A","Ạ":"A","Ȁ":"A","À":"A","Ả":"A","Ȃ":"A","Ā":"A","Ą":"A","Å":"A","Ǻ":"A","Ḁ":"A","Ⱥ":"A","Ã":"A","Ꜳ":"AA","Æ":"AE","Ǽ":"AE","Ǣ":"AE","Ꜵ":"AO","Ꜷ":"AU","Ꜹ":"AV","Ꜻ":"AV","Ꜽ":"AY","Ḃ":"B","Ḅ":"B","Ɓ":"B","Ḇ":"B","Ƀ":"B","Ƃ":"B","Ć":"C","Č":"C","Ç":"C","Ḉ":"C","Ĉ":"C","Ċ":"C","Ƈ":"C","Ȼ":"C","Ď":"D","Ḑ":"D","Ḓ":"D","Ḋ":"D","Ḍ":"D","Ɗ":"D","Ḏ":"D","ǲ":"D","ǅ":"D","Đ":"D","Ƌ":"D","Ǳ":"DZ","Ǆ":"DZ","É":"E","Ĕ":"E","Ě":"E","Ȩ":"E","Ḝ":"E","Ê":"E","Ế":"E","Ệ":"E","Ề":"E","Ể":"E","Ễ":"E","Ḙ":"E","Ë":"E","Ė":"E","Ẹ":"E","Ȅ":"E","È":"E","Ẻ":"E","Ȇ":"E","Ē":"E","Ḗ":"E","Ḕ":"E","Ę":"E","Ɇ":"E","Ẽ":"E","Ḛ":"E","Ꝫ":"ET","Ḟ":"F","Ƒ":"F","Ǵ":"G","Ğ":"G","Ǧ":"G","Ģ":"G","Ĝ":"G","Ġ":"G","Ɠ":"G","Ḡ":"G","Ǥ":"G","Ḫ":"H","Ȟ":"H","Ḩ":"H","Ĥ":"H","Ⱨ":"H","Ḧ":"H","Ḣ":"H","Ḥ":"H","Ħ":"H","Í":"I","Ĭ":"I","Ǐ":"I","Î":"I","Ï":"I","Ḯ":"I","İ":"I","Ị":"I","Ȉ":"I","Ì":"I","Ỉ":"I","Ȋ":"I","Ī":"I","Į":"I","Ɨ":"I","Ĩ":"I","Ḭ":"I","Ꝺ":"D","Ꝼ":"F","Ᵹ":"G","Ꞃ":"R","Ꞅ":"S","Ꞇ":"T","Ꝭ":"IS","Ĵ":"J","Ɉ":"J","Ḱ":"K","Ǩ":"K","Ķ":"K","Ⱪ":"K","Ꝃ":"K","Ḳ":"K","Ƙ":"K","Ḵ":"K","Ꝁ":"K","Ꝅ":"K","Ĺ":"L","Ƚ":"L","Ľ":"L","Ļ":"L","Ḽ":"L","Ḷ":"L","Ḹ":"L","Ⱡ":"L","Ꝉ":"L","Ḻ":"L","Ŀ":"L","Ɫ":"L","ǈ":"L","Ł":"L","Ǉ":"LJ","Ḿ":"M","Ṁ":"M","Ṃ":"M","Ɱ":"M","Ń":"N","Ň":"N","Ņ":"N","Ṋ":"N","Ṅ":"N","Ṇ":"N","Ǹ":"N","Ɲ":"N","Ṉ":"N","Ƞ":"N","ǋ":"N","Ñ":"N","Ǌ":"NJ","Ó":"O","Ŏ":"O","Ǒ":"O","Ô":"O","Ố":"O","Ộ":"O","Ồ":"O","Ổ":"O","Ỗ":"O","Ö":"O","Ȫ":"O","Ȯ":"O","Ȱ":"O","Ọ":"O","Ő":"O","Ȍ":"O","Ò":"O","Ỏ":"O","Ơ":"O","Ớ":"O","Ợ":"O","Ờ":"O","Ở":"O","Ỡ":"O","Ȏ":"O","Ꝋ":"O","Ꝍ":"O","Ō":"O","Ṓ":"O","Ṑ":"O","Ɵ":"O","Ǫ":"O","Ǭ":"O","Ø":"O","Ǿ":"O","Õ":"O","Ṍ":"O","Ṏ":"O","Ȭ":"O","Ƣ":"OI","Ꝏ":"OO","Ɛ":"E","Ɔ":"O","Ȣ":"OU","Ṕ":"P","Ṗ":"P","Ꝓ":"P","Ƥ":"P","Ꝕ":"P","Ᵽ":"P","Ꝑ":"P","Ꝙ":"Q","Ꝗ":"Q","Ŕ":"R","Ř":"R","Ŗ":"R","Ṙ":"R","Ṛ":"R","Ṝ":"R","Ȑ":"R","Ȓ":"R","Ṟ":"R","Ɍ":"R","Ɽ":"R","Ꜿ":"C","Ǝ":"E","Ś":"S","Ṥ":"S","Š":"S","Ṧ":"S","Ş":"S","Ŝ":"S","Ș":"S","Ṡ":"S","Ṣ":"S","Ṩ":"S","Ť":"T","Ţ":"T","Ṱ":"T","Ț":"T","Ⱦ":"T","Ṫ":"T","Ṭ":"T","Ƭ":"T","Ṯ":"T","Ʈ":"T","Ŧ":"T","Ɐ":"A","Ꞁ":"L","Ɯ":"M","Ʌ":"V","Ꜩ":"TZ","Ú":"U","Ŭ":"U","Ǔ":"U","Û":"U","Ṷ":"U","Ü":"U","Ǘ":"U","Ǚ":"U","Ǜ":"U","Ǖ":"U","Ṳ":"U","Ụ":"U","Ű":"U","Ȕ":"U","Ù":"U","Ủ":"U","Ư":"U","Ứ":"U","Ự":"U","Ừ":"U","Ử":"U","Ữ":"U","Ȗ":"U","Ū":"U","Ṻ":"U","Ų":"U","Ů":"U","Ũ":"U","Ṹ":"U","Ṵ":"U","Ꝟ":"V","Ṿ":"V","Ʋ":"V","Ṽ":"V","Ꝡ":"VY","Ẃ":"W","Ŵ":"W","Ẅ":"W","Ẇ":"W","Ẉ":"W","Ẁ":"W","Ⱳ":"W","Ẍ":"X","Ẋ":"X","Ý":"Y","Ŷ":"Y","Ÿ":"Y","Ẏ":"Y","Ỵ":"Y","Ỳ":"Y","Ƴ":"Y","Ỷ":"Y","Ỿ":"Y","Ȳ":"Y","Ɏ":"Y","Ỹ":"Y","Ź":"Z","Ž":"Z","Ẑ":"Z","Ⱬ":"Z","Ż":"Z","Ẓ":"Z","Ȥ":"Z","Ẕ":"Z","Ƶ":"Z","Ĳ":"IJ","Œ":"OE","ᴀ":"A","ᴁ":"AE","ʙ":"B","ᴃ":"B","ᴄ":"C","ᴅ":"D","ᴇ":"E","ꜰ":"F","ɢ":"G","ʛ":"G","ʜ":"H","ɪ":"I","ʁ":"R","ᴊ":"J","ᴋ":"K","ʟ":"L","ᴌ":"L","ᴍ":"M","ɴ":"N","ᴏ":"O","ɶ":"OE","ᴐ":"O","ᴕ":"OU","ᴘ":"P","ʀ":"R","ᴎ":"N","ᴙ":"R","ꜱ":"S","ᴛ":"T","ⱻ":"E","ᴚ":"R","ᴜ":"U","ᴠ":"V","ᴡ":"W","ʏ":"Y","ᴢ":"Z","á":"a","ă":"a","ắ":"a","ặ":"a","ằ":"a","ẳ":"a","ẵ":"a","ǎ":"a","â":"a","ấ":"a","ậ":"a","ầ":"a","ẩ":"a","ẫ":"a","ä":"a","ǟ":"a","ȧ":"a","ǡ":"a","ạ":"a","ȁ":"a","à":"a","ả":"a","ȃ":"a","ā":"a","ą":"a","ᶏ":"a","ẚ":"a","å":"a","ǻ":"a","ḁ":"a","ⱥ":"a","ã":"a","ꜳ":"aa","æ":"ae","ǽ":"ae","ǣ":"ae","ꜵ":"ao","ꜷ":"au","ꜹ":"av","ꜻ":"av","ꜽ":"ay","ḃ":"b","ḅ":"b","ɓ":"b","ḇ":"b","ᵬ":"b","ᶀ":"b","ƀ":"b","ƃ":"b","ɵ":"o","ć":"c","č":"c","ç":"c","ḉ":"c","ĉ":"c","ɕ":"c","ċ":"c","ƈ":"c","ȼ":"c","ď":"d","ḑ":"d","ḓ":"d","ȡ":"d","ḋ":"d","ḍ":"d","ɗ":"d","ᶑ":"d","ḏ":"d","ᵭ":"d","ᶁ":"d","đ":"d","ɖ":"d","ƌ":"d","ı":"i","ȷ":"j","ɟ":"j","ʄ":"j","ǳ":"dz","ǆ":"dz","é":"e","ĕ":"e","ě":"e","ȩ":"e","ḝ":"e","ê":"e","ế":"e","ệ":"e","ề":"e","ể":"e","ễ":"e","ḙ":"e","ë":"e","ė":"e","ẹ":"e","ȅ":"e","è":"e","ẻ":"e","ȇ":"e","ē":"e","ḗ":"e","ḕ":"e","ⱸ":"e","ę":"e","ᶒ":"e","ɇ":"e","ẽ":"e","ḛ":"e","ꝫ":"et","ḟ":"f","ƒ":"f","ᵮ":"f","ᶂ":"f","ǵ":"g","ğ":"g","ǧ":"g","ģ":"g","ĝ":"g","ġ":"g","ɠ":"g","ḡ":"g","ᶃ":"g","ǥ":"g","ḫ":"h","ȟ":"h","ḩ":"h","ĥ":"h","ⱨ":"h","ḧ":"h","ḣ":"h","ḥ":"h","ɦ":"h","ẖ":"h","ħ":"h","ƕ":"hv","í":"i","ĭ":"i","ǐ":"i","î":"i","ï":"i","ḯ":"i","ị":"i","ȉ":"i","ì":"i","ỉ":"i","ȋ":"i","ī":"i","į":"i","ᶖ":"i","ɨ":"i","ĩ":"i","ḭ":"i","ꝺ":"d","ꝼ":"f","ᵹ":"g","ꞃ":"r","ꞅ":"s","ꞇ":"t","ꝭ":"is","ǰ":"j","ĵ":"j","ʝ":"j","ɉ":"j","ḱ":"k","ǩ":"k","ķ":"k","ⱪ":"k","ꝃ":"k","ḳ":"k","ƙ":"k","ḵ":"k","ᶄ":"k","ꝁ":"k","ꝅ":"k","ĺ":"l","ƚ":"l","ɬ":"l","ľ":"l","ļ":"l","ḽ":"l","ȴ":"l","ḷ":"l","ḹ":"l","ⱡ":"l","ꝉ":"l","ḻ":"l","ŀ":"l","ɫ":"l","ᶅ":"l","ɭ":"l","ł":"l","ǉ":"lj","ſ":"s","ẜ":"s","ẛ":"s","ẝ":"s","ḿ":"m","ṁ":"m","ṃ":"m","ɱ":"m","ᵯ":"m","ᶆ":"m","ń":"n","ň":"n","ņ":"n","ṋ":"n","ȵ":"n","ṅ":"n","ṇ":"n","ǹ":"n","ɲ":"n","ṉ":"n","ƞ":"n","ᵰ":"n","ᶇ":"n","ɳ":"n","ñ":"n","ǌ":"nj","ó":"o","ŏ":"o","ǒ":"o","ô":"o","ố":"o","ộ":"o","ồ":"o","ổ":"o","ỗ":"o","ö":"o","ȫ":"o","ȯ":"o","ȱ":"o","ọ":"o","ő":"o","ȍ":"o","ò":"o","ỏ":"o","ơ":"o","ớ":"o","ợ":"o","ờ":"o","ở":"o","ỡ":"o","ȏ":"o","ꝋ":"o","ꝍ":"o","ⱺ":"o","ō":"o","ṓ":"o","ṑ":"o","ǫ":"o","ǭ":"o","ø":"o","ǿ":"o","õ":"o","ṍ":"o","ṏ":"o","ȭ":"o","ƣ":"oi","ꝏ":"oo","ɛ":"e","ᶓ":"e","ɔ":"o","ᶗ":"o","ȣ":"ou","ṕ":"p","ṗ":"p","ꝓ":"p","ƥ":"p","ᵱ":"p","ᶈ":"p","ꝕ":"p","ᵽ":"p","ꝑ":"p","ꝙ":"q","ʠ":"q","ɋ":"q","ꝗ":"q","ŕ":"r","ř":"r","ŗ":"r","ṙ":"r","ṛ":"r","ṝ":"r","ȑ":"r","ɾ":"r","ᵳ":"r","ȓ":"r","ṟ":"r","ɼ":"r","ᵲ":"r","ᶉ":"r","ɍ":"r","ɽ":"r","ↄ":"c","ꜿ":"c","ɘ":"e","ɿ":"r","ś":"s","ṥ":"s","š":"s","ṧ":"s","ş":"s","ŝ":"s","ș":"s","ṡ":"s","ṣ":"s","ṩ":"s","ʂ":"s","ᵴ":"s","ᶊ":"s","ȿ":"s","ɡ":"g","ᴑ":"o","ᴓ":"o","ᴝ":"u","ť":"t","ţ":"t","ṱ":"t","ț":"t","ȶ":"t","ẗ":"t","ⱦ":"t","ṫ":"t","ṭ":"t","ƭ":"t","ṯ":"t","ᵵ":"t","ƫ":"t","ʈ":"t","ŧ":"t","ᵺ":"th","ɐ":"a","ᴂ":"ae","ǝ":"e","ᵷ":"g","ɥ":"h","ʮ":"h","ʯ":"h","ᴉ":"i","ʞ":"k","ꞁ":"l","ɯ":"m","ɰ":"m","ᴔ":"oe","ɹ":"r","ɻ":"r","ɺ":"r","ⱹ":"r","ʇ":"t","ʌ":"v","ʍ":"w","ʎ":"y","ꜩ":"tz","ú":"u","ŭ":"u","ǔ":"u","û":"u","ṷ":"u","ü":"u","ǘ":"u","ǚ":"u","ǜ":"u","ǖ":"u","ṳ":"u","ụ":"u","ű":"u","ȕ":"u","ù":"u","ủ":"u","ư":"u","ứ":"u","ự":"u","ừ":"u","ử":"u","ữ":"u","ȗ":"u","ū":"u","ṻ":"u","ų":"u","ᶙ":"u","ů":"u","ũ":"u","ṹ":"u","ṵ":"u","ᵫ":"ue","ꝸ":"um","ⱴ":"v","ꝟ":"v","ṿ":"v","ʋ":"v","ᶌ":"v","ⱱ":"v","ṽ":"v","ꝡ":"vy","ẃ":"w","ŵ":"w","ẅ":"w","ẇ":"w","ẉ":"w","ẁ":"w","ⱳ":"w","ẘ":"w","ẍ":"x","ẋ":"x","ᶍ":"x","ý":"y","ŷ":"y","ÿ":"y","ẏ":"y","ỵ":"y","ỳ":"y","ƴ":"y","ỷ":"y","ỿ":"y","ȳ":"y","ẙ":"y","ɏ":"y","ỹ":"y","ź":"z","ž":"z","ẑ":"z","ʑ":"z","ⱬ":"z","ż":"z","ẓ":"z","ȥ":"z","ẕ":"z","ᵶ":"z","ᶎ":"z","ʐ":"z","ƶ":"z","ɀ":"z","ﬀ":"ff","ﬃ":"ffi","ﬄ":"ffl","ﬁ":"fi","ﬂ":"fl","ĳ":"ij","œ":"oe","ﬆ":"st","ₐ":"a","ₑ":"e","ᵢ":"i","ⱼ":"j","ₒ":"o","ᵣ":"r","ᵤ":"u","ᵥ":"v","ₓ":"x","ý":"y","ỳ":"y","ỷ":"y","ỹ":"y","ỵ":"y","ẻ":"e","ẽ":"e","ý":"y","ỳ":"y","ỷ":"y","ỹ":"y","ỵ":"y","á":"a","à":"a","ả":"a","ã":"a","ạ":"a","ă":"a","ắ":"a","ằ":"a","ẵ":"a","ẳ":"a","ấ":"a","ầ":"a","ẩ":"a","ẩ":"a","ậ":"a","é":"e","è":"e","ẻ":"e","ẽ":"e","ẽ":"e","ẹ":"e","ú":"u","ù":"u","ủ":"u","ũ":"u","ụ":"u","ư":"u","ứ":"u","ừ":"u","ử":"u","ữ":"u","ự":"u","ô":"o","ố":"o","ồ":"o","ổ":"o","ỗ":"o","ộ":"o","ó":"o","ò":"o","ỏ":"o","õ":"o","ọ":"o","í":"i","ì":"i","ỉ":"i","ĩ":"i","ị":"i","Á":"A","À":"A","Ả":"A","Ã":"A","Ạ":"A","Ă":"A","Ắ":"A","Ằ":"A","Ẵ":"A","Ẳ":"A","Ấ":"A","Ầ":"A","Ẩ":"A","Ẩ":"A","Ậ":"A","É":"E","È":"E","Ẻ":"E","Ẽ":"E","Ẽ":"E","Ẹ":"E","Ú":"U","Ù":"U","Ủ":"U","Ũ":"U","Ụ":"U","Ư":"U","Ứ":"U","Ừ":"U","Ử":"U","Ữ":"U","Ự":"U","Ô":"O","Ố":"O","Ồ":"O","Ổ":"O","Ỗ":"O","Ộ":"O","Ó":"O","Ò":"O","Ỏ":"O","Õ":"O","Ọ":"O","Í":"I","Ì":"I","Ỉ":"I","Ĩ":"I","Ị":"I","̣́̀̉̃":""};
		Latinise.diacritics_map = [
		    {'base':'A', 'letters':/[\u0041\u24B6\uFF21\u00C0\u00C1\u00C2\u1EA6\u1EA4\u1EAA\u1EA8\u00C3\u0100\u0102\u1EB0\u1EAE\u1EB4\u1EB2\u0226\u01E0\u00C4\u01DE\u1EA2\u00C5\u01FA\u01CD\u0200\u0202\u1EA0\u1EAC\u1EB6\u1E00\u0104\u023A\u2C6F]/g},
		    {'base':'AA','letters':/[\uA732]/g},
		    {'base':'AE','letters':/[\u00C6\u01FC\u01E2]/g},
		    {'base':'AO','letters':/[\uA734]/g},
		    {'base':'AU','letters':/[\uA736]/g},
		    {'base':'AV','letters':/[\uA738\uA73A]/g},
		    {'base':'AY','letters':/[\uA73C]/g},
		    {'base':'B', 'letters':/[\u0042\u24B7\uFF22\u1E02\u1E04\u1E06\u0243\u0182\u0181]/g},
		    {'base':'C', 'letters':/[\u0043\u24B8\uFF23\u0106\u0108\u010A\u010C\u00C7\u1E08\u0187\u023B\uA73E]/g},
		    {'base':'D', 'letters':/[\u0044\u24B9\uFF24\u1E0A\u010E\u1E0C\u1E10\u1E12\u1E0E\u0110\u018B\u018A\u0189\uA779]/g},
		    {'base':'DZ','letters':/[\u01F1\u01C4]/g},
		    {'base':'Dz','letters':/[\u01F2\u01C5]/g},
		    {'base':'E', 'letters':/[\u0045\u24BA\uFF25\u00C8\u00C9\u00CA\u1EC0\u1EBE\u1EC4\u1EC2\u1EBC\u0112\u1E14\u1E16\u0114\u0116\u00CB\u1EBA\u011A\u0204\u0206\u1EB8\u1EC6\u0228\u1E1C\u0118\u1E18\u1E1A\u0190\u018E]/g},
		    {'base':'F', 'letters':/[\u0046\u24BB\uFF26\u1E1E\u0191\uA77B]/g},
		    {'base':'G', 'letters':/[\u0047\u24BC\uFF27\u01F4\u011C\u1E20\u011E\u0120\u01E6\u0122\u01E4\u0193\uA7A0\uA77D\uA77E]/g},
		    {'base':'H', 'letters':/[\u0048\u24BD\uFF28\u0124\u1E22\u1E26\u021E\u1E24\u1E28\u1E2A\u0126\u2C67\u2C75\uA78D]/g},
		    {'base':'I', 'letters':/[\u0049\u24BE\uFF29\u00CC\u00CD\u00CE\u0128\u012A\u012C\u0130\u00CF\u1E2E\u1EC8\u01CF\u0208\u020A\u1ECA\u012E\u1E2C\u0197]/g},
		    {'base':'J', 'letters':/[\u004A\u24BF\uFF2A\u0134\u0248]/g},
		    {'base':'K', 'letters':/[\u004B\u24C0\uFF2B\u1E30\u01E8\u1E32\u0136\u1E34\u0198\u2C69\uA740\uA742\uA744\uA7A2]/g},
		    {'base':'L', 'letters':/[\u004C\u24C1\uFF2C\u013F\u0139\u013D\u1E36\u1E38\u013B\u1E3C\u1E3A\u0141\u023D\u2C62\u2C60\uA748\uA746\uA780]/g},
		    {'base':'LJ','letters':/[\u01C7]/g},
		    {'base':'Lj','letters':/[\u01C8]/g},
		    {'base':'M', 'letters':/[\u004D\u24C2\uFF2D\u1E3E\u1E40\u1E42\u2C6E\u019C]/g},
		    {'base':'N', 'letters':/[\u004E\u24C3\uFF2E\u01F8\u0143\u00D1\u1E44\u0147\u1E46\u0145\u1E4A\u1E48\u0220\u019D\uA790\uA7A4]/g},
		    {'base':'NJ','letters':/[\u01CA]/g},
		    {'base':'Nj','letters':/[\u01CB]/g},
		    {'base':'O', 'letters':/[\u004F\u24C4\uFF2F\u00D2\u00D3\u00D4\u1ED2\u1ED0\u1ED6\u1ED4\u00D5\u1E4C\u022C\u1E4E\u014C\u1E50\u1E52\u014E\u022E\u0230\u00D6\u022A\u1ECE\u0150\u01D1\u020C\u020E\u01A0\u1EDC\u1EDA\u1EE0\u1EDE\u1EE2\u1ECC\u1ED8\u01EA\u01EC\u00D8\u01FE\u0186\u019F\uA74A\uA74C]/g},
		    {'base':'OI','letters':/[\u01A2]/g},
		    {'base':'OO','letters':/[\uA74E]/g},
		    {'base':'OU','letters':/[\u0222]/g},
		    {'base':'P', 'letters':/[\u0050\u24C5\uFF30\u1E54\u1E56\u01A4\u2C63\uA750\uA752\uA754]/g},
		    {'base':'Q', 'letters':/[\u0051\u24C6\uFF31\uA756\uA758\u024A]/g},
		    {'base':'R', 'letters':/[\u0052\u24C7\uFF32\u0154\u1E58\u0158\u0210\u0212\u1E5A\u1E5C\u0156\u1E5E\u024C\u2C64\uA75A\uA7A6\uA782]/g},
		    {'base':'S', 'letters':/[\u0053\u24C8\uFF33\u1E9E\u015A\u1E64\u015C\u1E60\u0160\u1E66\u1E62\u1E68\u0218\u015E\u2C7E\uA7A8\uA784]/g},
		    {'base':'T', 'letters':/[\u0054\u24C9\uFF34\u1E6A\u0164\u1E6C\u021A\u0162\u1E70\u1E6E\u0166\u01AC\u01AE\u023E\uA786]/g},
		    {'base':'TZ','letters':/[\uA728]/g},
		    {'base':'U', 'letters':/[\u0055\u24CA\uFF35\u00D9\u00DA\u00DB\u0168\u1E78\u016A\u1E7A\u016C\u00DC\u01DB\u01D7\u01D5\u01D9\u1EE6\u016E\u0170\u01D3\u0214\u0216\u01AF\u1EEA\u1EE8\u1EEE\u1EEC\u1EF0\u1EE4\u1E72\u0172\u1E76\u1E74\u0244]/g},
		    {'base':'V', 'letters':/[\u0056\u24CB\uFF36\u1E7C\u1E7E\u01B2\uA75E\u0245]/g},
		    {'base':'VY','letters':/[\uA760]/g},
		    {'base':'W', 'letters':/[\u0057\u24CC\uFF37\u1E80\u1E82\u0174\u1E86\u1E84\u1E88\u2C72]/g},
		    {'base':'X', 'letters':/[\u0058\u24CD\uFF38\u1E8A\u1E8C]/g},
		    {'base':'Y', 'letters':/[\u0059\u24CE\uFF39\u1EF2\u00DD\u0176\u1EF8\u0232\u1E8E\u0178\u1EF6\u1EF4\u01B3\u024E\u1EFE]/g},
		    {'base':'Z', 'letters':/[\u005A\u24CF\uFF3A\u0179\u1E90\u017B\u017D\u1E92\u1E94\u01B5\u0224\u2C7F\u2C6B\uA762]/g},
		    {'base':'a', 'letters':/[\u0061\u24D0\uFF41\u1E9A\u00E0\u00E1\u00E2\u1EA7\u1EA5\u1EAB\u1EA9\u00E3\u0101\u0103\u1EB1\u1EAF\u1EB5\u1EB3\u0227\u01E1\u00E4\u01DF\u1EA3\u00E5\u01FB\u01CE\u0201\u0203\u1EA1\u1EAD\u1EB7\u1E01\u0105\u2C65\u0250]/g},
		    {'base':'aa','letters':/[\uA733]/g},
		    {'base':'ae','letters':/[\u00E6\u01FD\u01E3]/g},
		    {'base':'ao','letters':/[\uA735]/g},
		    {'base':'au','letters':/[\uA737]/g},
		    {'base':'av','letters':/[\uA739\uA73B]/g},
		    {'base':'ay','letters':/[\uA73D]/g},
		    {'base':'b', 'letters':/[\u0062\u24D1\uFF42\u1E03\u1E05\u1E07\u0180\u0183\u0253]/g},
		    {'base':'c', 'letters':/[\u0063\u24D2\uFF43\u0107\u0109\u010B\u010D\u00E7\u1E09\u0188\u023C\uA73F\u2184]/g},
		    {'base':'d', 'letters':/[\u0064\u24D3\uFF44\u1E0B\u010F\u1E0D\u1E11\u1E13\u1E0F\u0111\u018C\u0256\u0257\uA77A]/g},
		    {'base':'dz','letters':/[\u01F3\u01C6]/g},
		    {'base':'e', 'letters':/[\u0065\u24D4\uFF45\u00E8\u00E9\u00EA\u1EC1\u1EBF\u1EC5\u1EC3\u1EBD\u0113\u1E15\u1E17\u0115\u0117\u00EB\u1EBB\u011B\u0205\u0207\u1EB9\u1EC7\u0229\u1E1D\u0119\u1E19\u1E1B\u0247\u025B\u01DD]/g},
		    {'base':'f', 'letters':/[\u0066\u24D5\uFF46\u1E1F\u0192\uA77C]/g},
		    {'base':'g', 'letters':/[\u0067\u24D6\uFF47\u01F5\u011D\u1E21\u011F\u0121\u01E7\u0123\u01E5\u0260\uA7A1\u1D79\uA77F]/g},
		    {'base':'h', 'letters':/[\u0068\u24D7\uFF48\u0125\u1E23\u1E27\u021F\u1E25\u1E29\u1E2B\u1E96\u0127\u2C68\u2C76\u0265]/g},
		    {'base':'hv','letters':/[\u0195]/g},
		    {'base':'i', 'letters':/[\u0069\u24D8\uFF49\u00EC\u00ED\u00EE\u0129\u012B\u012D\u00EF\u1E2F\u1EC9\u01D0\u0209\u020B\u1ECB\u012F\u1E2D\u0268\u0131]/g},
		    {'base':'j', 'letters':/[\u006A\u24D9\uFF4A\u0135\u01F0\u0249]/g},
		    {'base':'k', 'letters':/[\u006B\u24DA\uFF4B\u1E31\u01E9\u1E33\u0137\u1E35\u0199\u2C6A\uA741\uA743\uA745\uA7A3]/g},
		    {'base':'l', 'letters':/[\u006C\u24DB\uFF4C\u0140\u013A\u013E\u1E37\u1E39\u013C\u1E3D\u1E3B\u017F\u0142\u019A\u026B\u2C61\uA749\uA781\uA747]/g},
		    {'base':'lj','letters':/[\u01C9]/g},
		    {'base':'m', 'letters':/[\u006D\u24DC\uFF4D\u1E3F\u1E41\u1E43\u0271\u026F]/g},
		    {'base':'n', 'letters':/[\u006E\u24DD\uFF4E\u01F9\u0144\u00F1\u1E45\u0148\u1E47\u0146\u1E4B\u1E49\u019E\u0272\u0149\uA791\uA7A5]/g},
		    {'base':'nj','letters':/[\u01CC]/g},
		    {'base':'o', 'letters':/[\u006F\u24DE\uFF4F\u00F2\u00F3\u00F4\u1ED3\u1ED1\u1ED7\u1ED5\u00F5\u1E4D\u022D\u1E4F\u014D\u1E51\u1E53\u014F\u022F\u0231\u00F6\u022B\u1ECF\u0151\u01D2\u020D\u020F\u01A1\u1EDD\u1EDB\u1EE1\u1EDF\u1EE3\u1ECD\u1ED9\u01EB\u01ED\u00F8\u01FF\u0254\uA74B\uA74D\u0275]/g},
		    {'base':'oi','letters':/[\u01A3]/g},
		    {'base':'ou','letters':/[\u0223]/g},
		    {'base':'oo','letters':/[\uA74F]/g},
		    {'base':'p','letters':/[\u0070\u24DF\uFF50\u1E55\u1E57\u01A5\u1D7D\uA751\uA753\uA755]/g},
		    {'base':'q','letters':/[\u0071\u24E0\uFF51\u024B\uA757\uA759]/g},
		    {'base':'r','letters':/[\u0072\u24E1\uFF52\u0155\u1E59\u0159\u0211\u0213\u1E5B\u1E5D\u0157\u1E5F\u024D\u027D\uA75B\uA7A7\uA783]/g},
		    {'base':'s','letters':/[\u0073\u24E2\uFF53\u00DF\u015B\u1E65\u015D\u1E61\u0161\u1E67\u1E63\u1E69\u0219\u015F\u023F\uA7A9\uA785\u1E9B]/g},
		    {'base':'t','letters':/[\u0074\u24E3\uFF54\u1E6B\u1E97\u0165\u1E6D\u021B\u0163\u1E71\u1E6F\u0167\u01AD\u0288\u2C66\uA787]/g},
		    {'base':'tz','letters':/[\uA729]/g},
		    {'base':'u','letters':/[\u0075\u24E4\uFF55\u00F9\u00FA\u00FB\u0169\u1E79\u016B\u1E7B\u016D\u00FC\u01DC\u01D8\u01D6\u01DA\u1EE7\u016F\u0171\u01D4\u0215\u0217\u01B0\u1EEB\u1EE9\u1EEF\u1EED\u1EF1\u1EE5\u1E73\u0173\u1E77\u1E75\u0289]/g},
		    {'base':'v','letters':/[\u0076\u24E5\uFF56\u1E7D\u1E7F\u028B\uA75F\u028C]/g},
		    {'base':'vy','letters':/[\uA761]/g},
		    {'base':'w','letters':/[\u0077\u24E6\uFF57\u1E81\u1E83\u0175\u1E87\u1E85\u1E98\u1E89\u2C73]/g},
		    {'base':'x','letters':/[\u0078\u24E7\uFF58\u1E8B\u1E8D]/g},
		    {'base':'y','letters':/[\u0079\u24E8\uFF59\u1EF3\u00FD\u0177\u1EF9\u0233\u1E8F\u00FF\u1EF7\u1E99\u1EF5\u01B4\u024F\u1EFF]/g},
		    {'base':'z','letters':/[\u007A\u24E9\uFF5A\u017A\u1E91\u017C\u017E\u1E93\u1E95\u01B6\u0225\u0240\u2C6C\uA763]/g},
		    // Lastest Item to remove all VN symbols: (`), ('), (?), (~), (.) to empty
		    {'base':'','letters':/[\u0300\u0301\u0303\u0309\u0323]/g}
		];
		String.prototype.latinise = function() {
			var s = this;
			for(var i = 0; i < Latinise.diacritics_map.length; i++) {
		        s = s.replace(Latinise.diacritics_map[i].letters, Latinise.diacritics_map[i].base);
		    }
			return s.replace(/[^A-Za-z0-9\[\] ]/g, function(a) { return Latinise.latin_map[a]||a; });
		};
		String.prototype.latinize = String.prototype.latinise;
		String.prototype.isLatin = function() {
			return this == this.latinise();
		}
	}

	$.isTrimBlank = function(s, trim) {
		s = trim ? $.trim(s) : s;
		return (!s || 0 === s.length);
	};

	$.isBlank = function(s) {
		return $.isTrimBlank(s, true);
	};

	$.isEmail = function(s) {
		return (!$.isBlank(s) && s.isEmail());
	};

	$.isCsvFile = function(s) {
		return (!$.isBlank(s) && s.isCsvFile());
	};

	$.isMultiBytes = function(s) {
		return (!$.isTrimBlank(s, false) && s.isMultiBytes());
	};

	$.isAlphaNumeric = function(s) {
		return (!$.isTrimBlank(s, false) && s.isAlphaNumeric());
	};

	$.toDateTime = function(s, pattern) {
		return (!$.isBlank(s) ? s.toDateTime(pattern) : null);
	};

	$.bytesLength = function(s) {
		return ($.isTrimBlank(s, false) ? 0 : s.bytesLength());
	};

	$.mesurePxWidth = function(s) {
		return ($.isTrimBlank(s, false) ? 0 : s.mesurePxWidth());
	};

	$.leftPad = function(s, c, length) {
		s = (!!s ? new String(s) : '');
		return s.leftPad(c, length);
	};

	$.toPlainText = function(s) {
		s = (!!s ? new String(s) : '');
		return s.toPlainText();
	};
})(jQuery);