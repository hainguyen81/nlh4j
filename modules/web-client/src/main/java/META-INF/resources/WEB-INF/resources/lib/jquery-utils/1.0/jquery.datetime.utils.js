/*
 * Date prototype extensions. Doesn't depend on any
 * other code. Doens't overwrite existing methods.
 *
 * Adds dayNames, abbrDayNames, monthNames and abbrMonthNames static properties and isLeapYear,
 * isWeekend, isWeekDay, getDaysInMonth, getDayName, getMonthName, getDayOfYear, getWeekOfYear,
 * setDayOfYear, addYears, addMonths, addDays, addHours, addMinutes, addSeconds methods
 *
 * Copyright (c) 2006 Jörn Zaefferer and Brandon Aaron (brandon.aaron@gmail.com || http://brandonaaron.net)
 *
 * Additional methods and properties added by Kelvin Luck: firstDayOfWeek, dateFormat, zeroTime, asString, fromString -
 * I've added my name to these methods so you know who to blame if they are broken!
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 *
 * ***********************************************************************************************
 * NOTE: Base on [Parse and Format Library]
 * http://www.xaprb.com/blog/2005/12/12/javascript-closures-for-runtime-efficiency/
 * -----------------------------------------------------------------------------------------------
 *
 * Copyright (C) 2004 Baron Schwartz <baron at sequent dot org>
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, version 2.1.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 */

Date.parseFunctions = { count: 0 };
Date.parseRegexes = [];
Date.formatFunctions = { count: 0};

/**
 * An Array of abbreviated day names starting with Sun.<br>
 *
 * @example abbrDayNames[0]
 * @result 'Sun'
 *
 * @name abbrDayNames
 * @type Array
 * @cat Plugins/Methods/Date
 */
Date.abbrDayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
// days in month
Date.daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
// month names in EN-US
Date.monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
/**
 * An Array of abbreviated month names starting with Jan.<br>
 *
 * @example abbrMonthNames[0]
 * @result 'Jan'
 *
 * @name monthNames
 * @type Array
 * @cat Plugins/Methods/Date
 */
Date.abbrMonthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
// day names in EN-US
Date.dayNames = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
Date.y2kYear = 50;
// month numbers
Date.monthNumbers = {
    Jan: 0,
    Feb: 1,
    Mar: 2,
    Apr: 3,
    May: 4,
    Jun: 5,
    Jul: 6,
    Aug: 7,
    Sep: 8,
    Oct: 9,
    Nov: 10,
    Dec: 11
};
// date pattern
Date.patterns = {
    ISO8601LongPattern: "Y-m-d H:i:s",
    ISO8601ShortPattern: "Y-m-d",
    ShortDatePattern: "n/j/Y",
    LongDatePattern: "l, F d, Y",
    FullDateTimePattern: "l, F d, Y g:i:s A",
    MonthDayPattern: "F d",
    ShortTimePattern: "g:i A",
    LongTimePattern: "g:i:s A",
    SortableDateTimePattern: "Y-m-d\\TH:i:s",
    UniversalSortableDateTimePattern: "Y-m-d H:i:sO",
    YearMonthPattern: "F, Y"
};

/**
 * The first day of the week for this locale.<br>
 *
 * @name firstDayOfWeek
 * @type Number
 * @cat Plugins/Methods/Date
 * @author Kelvin Luck
 */
Date.firstDayOfWeek = 1;

/**
 * Debug mode
 */
Date.DEBUG_MODE = false;


/************************************************************************************
 * Initializes some Date prototype
 ************************************************************************************/
(function($) {
	Date.prototype.dateFormat = function(b) {
		if (b == "unixtime") {
			return parseInt(this.getTime() / 1000);
		}
	    if (Date.formatFunctions[b] == null) {
	        Date.createNewFormat(b);
	    }
	    var a = Date.formatFunctions[b];
	    return this[a]();
	};

	Date.prototype.getTimezone = function() {
	    return this.toString().replace(/^.*? ([A-Z]{3}) [0-9]{4}.*$/, "$1").replace(/^.*?\(([A-Z])[a-z]+ ([A-Z])[a-z]+ ([A-Z])[a-z]+\)$/, "$1$2$3");
	};
	Date.prototype.getGMTOffset = function() {
	    return (this.getTimezoneOffset() > 0 ? "-" : "+") + String.leftPad(Math.floor(Math.abs(this.getTimezoneOffset()) / 60), 2, "0") + String.leftPad(Math.abs(this.getTimezoneOffset()) % 60, 2, "0");
	};
	Date.prototype.getDayOfYear = function() {
	    var a = 0;
	    Date.daysInMonth[1] = this.isLeapYear() ? 29 : 28;
	    for (var b = 0; b < this.getMonth(); ++b) {
	        a += Date.daysInMonth[b];
	    }
	    return a + this.getDate();
	};
	Date.prototype.getWeekOfYear = function() {
	    var b = this.getDayOfYear() + (4 - this.getDay());
	    var a = new Date(this.getFullYear(), 0, 1);
	    var c = (7 - a.getDay() + 4);
	    return String.leftPad(Math.ceil((b - c) / 7) + 1, 2, "0");
	};
	Date.prototype.isLeapYear = function() {
	    var a = this.getFullYear();
	    return ((a & 3) == 0 && (a % 100 || (a % 400 == 0 && a)));
	};
	Date.prototype.getFirstDayOfMonth = function() {
	    var a = (this.getDay() - (this.getDate() - 1)) % 7;
	    return (a < 0) ? (a + 7) : a;
	};
	Date.prototype.getLastDayOfMonth = function() {
	    var a = (this.getDay() + (Date.daysInMonth[this.getMonth()] - this.getDate())) % 7;
	    return (a < 0) ? (a + 7) : a;
	};
	Date.prototype.getDaysInMonth = function() {
	    Date.daysInMonth[1] = this.isLeapYear() ? 29 : 28;
	    return Date.daysInMonth[this.getMonth()];
	};
	Date.prototype.getSuffix = function() {
	    switch (this.getDate()) {
	        case 1:
	        case 21:
	        case 31:
	            return "st";
	        case 2:
	        case 22:
	            return "nd";
	        case 3:
	        case 23:
	            return "rd";
	        default:
	            return "th";
	    }
	};

	String.escape = function(a) {
	    return a.replace(/('|\\)/g, "\\$1");
	};
	String.leftPad = function(d, b, c) {
	    var a = new String(d);
	    if (c == null) {
	        c = " ";
	    }
	    while (a.length < b) {
	        a = c + a;
	    }
	    return a;
	};
})();


/************************************************************************************
 * Additional methods
 ************************************************************************************/
(function() {

        /**
         * Adds a given method under the given name
         * to the Date prototype if it doesn't
         * currently exist.<br>
         *
         * @private
         */
        function add(name, method) {
                if( !Date.prototype[name] ) {
                        Date.prototype[name] = method;
                }
        };

        /**
         * Clone this date to new instance.<br>
         *
         * @return new Date instance
         */
        add("clone", function() {
                return new Date(this.getTime());
        });

        /**
         * Checks if the year is a leap year.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.isLeapYear();
         * @result true
         *
         * @name isLeapYear
         * @type Boolean
         * @cat Plugins/Methods/Date
         */
        add("isLeapYear", function() {
                var y = this.getFullYear();
                return (y%4==0 && y%100!=0) || y%400==0;
        });

        /**
         * Checks if the day is a weekend day (Sat or Sun).<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.isWeekend();
         * @result false
         *
         * @name isWeekend
         * @type Boolean
         * @cat Plugins/Methods/Date
         */
        add("isWeekend", function() {
                return this.getDay()==0 || this.getDay()==6;
        });

        /**
         * Get the first date of current month<br>
         *
         * @example var dtm = new Date("21/12/2008");
         * dtm.firstDate();
         * @result new Date("01/12/2008");
         *
         * @name firstDate
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("firstDate", function() {
                return new Date(this.getFullYear(), this.getMonth(), 1);
        });

        /**
         * Get the last date of current month<br>
         *
         * @example var dtm = new Date("21/12/2008");
         * dtm.lastDate();
         * @result new Date("31/12/2008");
         *
         * @name lastDate
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("lastDate", function() {
                return new Date(this.getFullYear(), this.getMonth() + 1, 0);
        });

        /**
         * Check if the day is a day of the week (Mon-Fri)<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.isWeekDay();
         * @result false
         *
         * @name isWeekDay
         * @type Boolean
         * @cat Plugins/Methods/Date
         */
        add("isWeekDay", function() {
                return !this.isWeekend();
        });

        /**
         * Gets the number of days in the month.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.getDaysInMonth();
         * @result 31
         *
         * @name getDaysInMonth
         * @type Number
         * @cat Plugins/Methods/Date
         */
        add("getDaysInMonth", function() {
                return [31,(this.isLeapYear() ? 29:28),31,30,31,30,31,31,30,31,30,31][this.getMonth()];
        });

        /**
         * Gets the name of the day.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.getDayName();
         * @result 'Saturday'
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.getDayName(true);
         * @result 'Sat'
         *
         * @param abbreviated Boolean When set to true the name will be abbreviated.
         * @name getDayName
         * @type String
         * @cat Plugins/Methods/Date
         */
        add("getDayName", function(abbreviated) {
                return abbreviated && Date.abbrDayNames && Date.abbrDayNames.length >= this.getDay()
                	? Date.abbrDayNames[this.getDay()]
                	: Date.dayNames ? Date.dayNames[this.getDay()] : null;
        });

        /**
         * Gets the name of the month.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.getMonthName();
         * @result 'Janurary'
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.getMonthName(true);
         * @result 'Jan'
         *
         * @param abbreviated Boolean When set to true the name will be abbreviated.
         * @name getDayName
         * @type String
         * @cat Plugins/Methods/Date
         */
        add("getMonthName", function(abbreviated) {
                return abbreviated && Date.abbrMonthNames && Date.abbrMonthNames.length >= this.getMonth()
                	? Date.abbrMonthNames[this.getMonth()]
                	: Date.monthNames ? Date.monthNames[this.getMonth()] : null;
        });

        /**
         * Get the number of the day of the year.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.getDayOfYear();
         * @result 11
         *
         * @name getDayOfYear
         * @type Number
         * @cat Plugins/Methods/Date
         */
        add("getDayOfYear", function() {
                var tmpdtm = new Date("1/1/" + this.getFullYear());
                return Math.floor((this.getTime() - tmpdtm.getTime()) / 86400000);
        });

        /**
         * Get the number of the week of the year.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.getWeekOfYear();
         * @result 2
         *
         * @name getWeekOfYear
         * @type Number
         * @cat Plugins/Methods/Date
         */
        add("getWeekOfYear", function() {
                return Math.ceil(this.getDayOfYear() / 7);
        });

        /**
         * Set the day of the year.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.setDayOfYear(1);
         * dtm.toString();
         * @result 'Tue Jan 01 2008 00:00:00'
         *
         * @name setDayOfYear
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("setDayOfYear", function(day) {
                this.setMonth(0);
                this.setDate(day);
                return this;
        });

        /**
         * Add a number of years to the date object.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.addYears(1);
         * dtm.toString();
         * @result 'Mon Jan 12 2009 00:00:00'
         *
         * @name addYears
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("addYears", function(num) {
                this.setFullYear(this.getFullYear() + num);
                return this;
        });

        /**
         * Add a number of months to the date object.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.addMonths(1);
         * dtm.toString();
         * @result 'Tue Feb 12 2008 00:00:00'
         *
         * @name addMonths
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("addMonths", function(num) {
                var tmpdtm = this.getDate();

                this.setMonth(this.getMonth() + num);

                if (tmpdtm > this.getDate())
                        this.addDays(-this.getDate());

                return this;
        });

        /**
         * Add a number of days to the date object.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.addDays(1);
         * dtm.toString();
         * @result 'Sun Jan 13 2008 00:00:00'
         *
         * @name addDays
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("addDays", function(num) {
                //this.setDate(this.getDate() + num);
                this.setTime(this.getTime() + (num*86400000) );
                return this;
        });

        /**
         * Add a number of hours to the date object.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.addHours(24);
         * dtm.toString();
         * @result 'Sun Jan 13 2008 00:00:00'
         *
         * @name addHours
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("addHours", function(num) {
                this.setHours(this.getHours() + num);
                return this;
        });

        /**
         * Add a number of minutes to the date object.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.addMinutes(60);
         * dtm.toString();
         * @result 'Sat Jan 12 2008 01:00:00'
         *
         * @name addMinutes
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("addMinutes", function(num) {
                this.setMinutes(this.getMinutes() + num);
                return this;
        });

        /**
         * Add a number of seconds to the date object.<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.addSeconds(60);
         * dtm.toString();
         * @result 'Sat Jan 12 2008 00:01:00'
         *
         * @name addSeconds
         * @type Date
         * @cat Plugins/Methods/Date
         */
        add("addSeconds", function(num) {
                this.setSeconds(this.getSeconds() + num);
                return this;
        });

        /**
         * Sets the time component of this Date to zero for cleaner, easier comparison of dates where time is not relevant.<br>
         *
         * @example var dtm = new Date();
         * dtm.zeroTime();
         * dtm.toString();
         * @result 'Sat Jan 12 2008 00:01:00'
         *
         * @name zeroTime
         * @type Date
         * @cat Plugins/Methods/Date
         * @author Kelvin Luck
         */
        add("zeroTime", function() {
                this.setMilliseconds(0);
                this.setSeconds(0);
                this.setMinutes(0);
                this.setHours(0);
                return this;
        });

        /**
         * Returns a string representation of the date object according to Date.format.<br>
         * (Date.toString may be used in other places so I purposefully didn't overwrite it)<br>
         *
         * @example var dtm = new Date("01/12/2008");
         * dtm.asString();
         * @result '12/01/2008' // (where Date.format == 'dd/mm/yyyy'
         *
         * @name asString
         * @type Date
         * @cat Plugins/Methods/Date
         * @author Kelvin Luck
         */
        add("asString", function(format) {
                var r = format || 'mm/dd/yyyy';
                return r
                        .split('yyyy').join(this.getFullYear())
                        .split('YYYY').join(this.getFullYear())
                        .split('yy').join((this.getFullYear() + '').substring(2))
                        .split('mmmm').join(this.getMonthName(false))
                        .split('mmm').join(this.getMonthName(true))
                        .split('mm').join(_zeroPad(this.getMonth()+1))
                        .split('MM').join(_zeroPad(this.getMonth()+1))
                        .split('dd').join(_zeroPad(this.getDate()))
                        .split('DD').join(_zeroPad(this.getDate()));
        });

        /**
         * Calculate date difference between present date instance and specified parameter.
         *
         * @param d to calculate
         * @param what what difference. default is 0 (milliseconds)<br>
         * 			0 - milliseconds
         * 			1 - seconds
         * 			2 - minutes
         * 			3 - hours
         * 			4 - days
         * 			5 - weeks
         * 			6 - months
         * 			7 - years
         */
        add("diff", function(d, what) {
                var time1 = this.getTime();
                var time2 = d.getTIme();
                what = (isNaN(what) ? 0 : what < 0 || what > 6 ? 0 : what);
                switch(what) {
                	case 0:
                		return (time2 - time1);
                	case 1:
                		return ((time2 - time1) / 1000);
                	case 2:
                		return ((time2 - time1) / (60 * 1000));
                	case 3:
                		return ((time2 - time1) / (60 * 60 * 1000));
                	case 4:
                		return ((time2 - time1) / (24 * 60 * 60 * 1000));
                	case 5:
                		return ((time2 - time1) / (7 * 24 * 60 * 60 * 1000));
                	case 6: {
                		var d1Y = this.getFullYear();
                        var d2Y = d.getFullYear();
                        var d1M = this.getMonth();
                        var d2M = d.getMonth();

                        return ((d2M + (12 * d2Y)) - (d1M + (12 * d1Y)));
                	}
                	case 7:
                		return d.getFullYear() - this.getFullYear();
                }
                return -1;
        });

        /**
         * Creates new datetime format function as prototype of Date<br>
         *
         * @param format the datetime format pattern
         */
        Date.createNewFormat = function(format) {
            var funcName = "format" + Date.formatFunctions.count++;
            Date.formatFunctions[format] = funcName;
            var code = "Date.prototype." + funcName + " = function() {return ";
            var special = false;
            var ch = "";
            for (var i = 0; i < format.length; ++i) {
                ch = format.charAt(i);
                if (!special && ch == "\\") {
                    special = true;
                } else {
                    if (special) {
                        special = false;
                        code += "'" + String.escape(ch) + "' + ";
                    } else {
                        code += Date.getFormatCode(ch);
                    }
                }
            }
            eval(code.substring(0, code.length - 3) + ";}");
        };

        /**
         * Get the format javascript code to build prototype<br>
         * @see {@link Date.createNewFormat}
         *
         * @param a the format pattern
         *
         * @returns {String} the javascript code
         */
        Date.getFormatCode = function(a) {
            switch (a) {
                case "d":
                    return "String.leftPad(this.getDate(), 2, '0') + ";
                case "D":
                    return "Date.dayNames[this.getDay()].substring(0, 3) + ";
                case "j":
                    return "this.getDate() + ";
                case "l":
                    return "Date.dayNames[this.getDay()] + ";
                case "S":
                    return "this.getSuffix() + ";
                case "w":
                    return "this.getDay() + ";
                case "z":
                    return "this.getDayOfYear() + ";
                case "W":
                    return "this.getWeekOfYear() + ";
                case "F":
                    return "Date.monthNames[this.getMonth()] + ";
                case "m":
                    return "String.leftPad(this.getMonth() + 1, 2, '0') + ";
                case "M":
                    return "Date.monthNames[this.getMonth()].substring(0, 3) + ";
                case "n":
                    return "(this.getMonth() + 1) + ";
                case "t":
                    return "this.getDaysInMonth() + ";
                case "L":
                    return "(this.isLeapYear() ? 1 : 0) + ";
                case "Y":
                    return "this.getFullYear() + ";
                case "y":
                    return "('' + this.getFullYear()).substring(2, 4) + ";
                case "a":
                    return "(this.getHours() < 12 ? 'am' : 'pm') + ";
                case "A":
                    return "(this.getHours() < 12 ? 'AM' : 'PM') + ";
                case "g":
                    return "((this.getHours() %12) ? this.getHours() % 12 : 12) + ";
                case "G":
                    return "this.getHours() + ";
                case "h":
                    return "String.leftPad((this.getHours() %12) ? this.getHours() % 12 : 12, 2, '0') + ";
                case "H":
                    return "String.leftPad(this.getHours(), 2, '0') + ";
                case "i":
                    return "String.leftPad(this.getMinutes(), 2, '0') + ";
                case "s":
                    return "String.leftPad(this.getSeconds(), 2, '0') + ";
                case "O":
                    return "this.getGMTOffset() + ";
                case "T":
                    return "this.getTimezone() + ";
                case "Z":
                    return "(this.getTimezoneOffset() * -60) + ";
                default:
                    return "'" + String.escape(a) + "' + ";
            }
        };

        /**
         * Parses the specified date string by the specified format pattern<br>
         *
         * @param a	the datetime string value
         * @param c	the datetime format pattern
         *
         * @returns the date object
         */
        Date.parseDate = function(a, c) {
            if (c == "unixtime") {
                return new Date(!isNaN(parseInt(a)) ? parseInt(a) * 1000 : 0);
            }
            if (Date.parseFunctions[c] == null) {
                Date.createParser(c);
            }
            var b = Date.parseFunctions[c];
            return Date[b](a);
        };

        /**
         * Creates the datetime parser prototype to parse date object<br>
         * @see {@link Date.createNewFormat}
         *
         * @param format the datetime format pattern
         */
        Date.createParser = function(format) {
            var funcName = "parse" + Date.parseFunctions.count++;
            var regexNum = Date.parseRegexes.length;
            var currentGroup = 1;
            Date.parseFunctions[format] = funcName;
            var code = "Date." + funcName + " = function(input) {\nvar y = -1, m = -1, d = -1, h = -1, i = -1, s = -1, z = -1;\nvar d = new Date();\ny = d.getFullYear();\nm = d.getMonth();\nd = d.getDate();\nvar results = input.match(Date.parseRegexes[" + regexNum + "]);\nif (results && results.length > 0) {";
            var regex = "";
            var special = false;
            var ch = "";
            for (var i = 0; i < format.length; ++i) {
                ch = format.charAt(i);
                if (!special && ch == "\\") {
                    special = true;
                } else {
                    if (special) {
                        special = false;
                        regex += String.escape(ch);
                    } else {
                        obj = Date.formatCodeToRegex(ch, currentGroup);
                        currentGroup += obj.g;
                        regex += obj.s;
                        if (obj.g && obj.c) {
                            code += obj.c;
                        }
                    }
                }
            }
            code += "if (y > 0 && z > 0){\nvar doyDate = new Date(y,0);\ndoyDate.setDate(z);\nm = doyDate.getMonth();\nd = doyDate.getDate();\n}";
            code += "if (y > 0 && m >= 0 && d > 0 && h >= 0 && i >= 0 && s >= 0)\n{return new Date(y, m, d, h, i, s);}\nelse if (y > 0 && m >= 0 && d > 0 && h >= 0 && i >= 0)\n{return new Date(y, m, d, h, i);}\nelse if (y > 0 && m >= 0 && d > 0 && h >= 0)\n{return new Date(y, m, d, h);}\nelse if (y > 0 && m >= 0 && d > 0)\n{return new Date(y, m, d);}\nelse if (y > 0 && m >= 0)\n{return new Date(y, m);}\nelse if (y > 0)\n{return new Date(y);}\n}return null;}";
            Date.parseRegexes[regexNum] = new RegExp("^" + regex + "$");
            eval(code);
        };

        /**
         * Formats the specified pattern to regular expression<br>
         *
         * @param b the datetime format pattern
         * @param a
         *
         * @returns
         */
        Date.formatCodeToRegex = function(b, a) {
            switch (b) {
                case "D":
                    return {
                        g: 0,
                        c: null,
                        s: "(?:Sun|Mon|Tue|Wed|Thu|Fri|Sat)"
                    };
                case "j":
                case "d":
                    return {
                        g: 1,
                        c: "d = parseInt(results[" + a + "], 10);\n",
                        s: "(\\d{1,2})"
                    };
                case "l":
                    return {
                        g: 0,
                        c: null,
                        s: "(?:" + Date.dayNames.join("|") + ")"
                    };
                case "S":
                    return {
                        g: 0,
                        c: null,
                        s: "(?:st|nd|rd|th)"
                    };
                case "w":
                    return {
                        g: 0,
                        c: null,
                        s: "\\d"
                    };
                case "z":
                    return {
                        g: 1,
                        c: "z = parseInt(results[" + a + "], 10);\n",
                        s: "(\\d{1,3})"
                    };
                case "W":
                    return {
                        g: 0,
                        c: null,
                        s: "(?:\\d{2})"
                    };
                case "F":
                    return {
                        g: 1,
                        c: "m = parseInt(Date.monthNumbers[results[" + a + "].substring(0, 3)], 10);\n",
                        s: "(" + Date.monthNames.join("|") + ")"
                    };
                case "M":
                    return {
                        g: 1,
                        c: "m = parseInt(Date.monthNumbers[results[" + a + "]], 10);\n",
                        s: "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)"
                    };
                case "n":
                case "m":
                    return {
                        g: 1,
                        c: "m = parseInt(results[" + a + "], 10) - 1;\n",
                        s: "(\\d{1,2})"
                    };
                case "t":
                    return {
                        g: 0,
                        c: null,
                        s: "\\d{1,2}"
                    };
                case "L":
                    return {
                        g: 0,
                        c: null,
                        s: "(?:1|0)"
                    };
                case "Y":
                    return {
                        g: 1,
                        c: "y = parseInt(results[" + a + "], 10);\n",
                        s: "(\\d{4})"
                    };
                case "y":
                    return {
                        g: 1,
                        c: "var ty = parseInt(results[" + a + "], 10);\ny = ty > Date.y2kYear ? 1900 + ty : 2000 + ty;\n",
                        s: "(\\d{1,2})"
                    };
                case "a":
                    return {
                        g: 1,
                        c: "if (results[" + a + "] == 'am') {\nif (h == 12) { h = 0; }\n} else { if (h < 12) { h += 12; }}",
                        s: "(am|pm)"
                    };
                case "A":
                    return {
                        g: 1,
                        c: "if (results[" + a + "] == 'AM') {\nif (h == 12) { h = 0; }\n} else { if (h < 12) { h += 12; }}",
                        s: "(AM|PM)"
                    };
                case "g":
                case "G":
                case "h":
                case "H":
                    return {
                        g: 1,
                        c: "h = parseInt(results[" + a + "], 10);\n",
                        s: "(\\d{1,2})"
                    };
                case "i":
                    return {
                        g: 1,
                        c: "i = parseInt(results[" + a + "], 10);\n",
                        s: "(\\d{2})"
                    };
                case "s":
                    return {
                        g: 1,
                        c: "s = parseInt(results[" + a + "], 10);\n",
                        s: "(\\d{2})"
                    };
                case "O":
                    return {
                        g: 0,
                        c: null,
                        s: "[+-]\\d{4}"
                    };
                case "T":
                    return {
                        g: 0,
                        c: null,
                        s: "[A-Z]{3}"
                    };
                case "Z":
                    return {
                        g: 0,
                        c: null,
                        s: "[+-]\\d{1,5}"
                    };
                default:
                    return {
                        g: 0,
                        c: null,
                        s: String.escape(b)
                    };
            }
        };

        /**
         * Converts the date in d to a date-object. The input can be:<br>
         * 	a date object:	returned without modification<br>
         * 	an array     :	Interpreted as [year,month,day]. NOTE: month is 0-11.<br>
         * 	a number     :	Interpreted as number of milliseconds
         * 					since 1 Jan 1970 (a timestamp)<br>
         * 	a string     :	Any format supported by the javascript engine, like
         * 					"YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.<br>
         * 	an object    :	Interpreted as an object with year, month and date
         * 					attributes.  **NOTE** month is 0-11.<br>
         */
        Date.toDate = function(obj,fmt) {
        	if (!obj) return null;
        	if (obj.constructor === Date) _debug('value is Date!');
            if (obj.constructor === Array) _debug('value is Array!');
            if (obj.constructor === Number) _debug('value is Number!');
            if (obj.constructor === String) _debug('value is String!');
            if (typeof obj === "object") _debug('value is Object!');
            return (
                obj.constructor === Date ? obj :
                obj.constructor === Array ? new Date(obj[0],obj[1],obj[2]) :
                obj.constructor === Number ? new Date(obj) :
                obj.constructor === String ? Date.parseDate(obj,fmt) :
                typeof obj === "object" ? new Date(obj.year,obj.month,obj.date) :
                null
            );
        };

        /**
         * Converts the date in d to a date-object. The input can be:<br>
         * 	a date object:	returned without modification<br>
         * 	an array     :	Interpreted as [year,month,day]. NOTE: month is 0-11.<br>
         * 	a number     :	Interpreted as number of milliseconds
         * 					since 1 Jan 1970 (a timestamp)<br>
         * 	a string     :	Any format supported by the javascript engine, like
         * 					"YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.<br>
         * 	an object    :	Interpreted as an object with year, month and date
         * 					attributes.  **NOTE** month is 0-11.<br>
         */
        Date.isValidDate = function(obj,fmt) {
        	return (Date.toDate(obj, fmt) != null);
        };

        /**
         * Compare two dates (could be of any type supported by the convert
         * function above) and returns:<br>
         *  -1			: if d1 < d2<br>
         *   0			: if d1 = d2<br>
         *   1			: if d1 > d2<br>
         * exception	: if d1 or d2 is an illegal date<br>
         */
        Date.compare = function(d1,d2,fmt) {
        	var DEBUG_GROUP = "Date Utils - Compare dates";
        	_debugStart(DEBUG_GROUP);
        	var dd1 = Date.toDate(d1,fmt);
        	var dd2 = Date.toDate(d2,fmt);
        	if (dd1 == null) {
        		throw "Date 1 [" + d1 + "] is not a valid date";
        	}
        	else if (dd2 == null) {
        		throw "Date 2 [" + d2 + "] is not a valid date";
        	}
            var res = ((dd1 > dd2) - (dd1 < dd2));
            _debugEnd(DEBUG_GROUP);
            return res;
        };

        /**
         * Checks if date in d is between dates in start and end.<br>
         * Returns a boolean:<br>
         * 		true		: if d is between start and end (inclusive)<br>
         * 		false		: if d is before start or after end<br>
         * 		exception	: if one or more of the dates is illegal.<br>
         */
       Date.inRange = function(d,start,end,fmt) {
    	   var dd = Date.toDate(d,fmt);
    	   var dstart = Date.toDate(start,fmt);
    	   var dend = Date.toDate(end,fmt);
    	   if (dd == null) {
    		   throw "Date 1 [" + d + "] is not a valid date";
    	   }
    	   else if (dstart == null) {
    		   throw "Date start of range [" + start + "] is not a valid date";
    	   }
    	   else if (dend == null) {
    		   throw "Date end of range [" + end + "] is not a valid date";
    	   }
    	   return (dstart <= dd && dd <= dend);
        };

        // utility method
        var _zeroPad = function(num) {
                var s = '0'+num;
                return s.substring(s.length-2);
                //return ('0'+num).substring(-2); // doesn't work on IE :(
        };

        // logging
        function _debug(message) {
        	if (Date.DEBUG_MODE && Date.DEBUG_MODE === true && window.console) {
        		try {
        			console.log(message);
        		}
        		catch(e) {}
        	}
        }
        function _debugStart(groupName) {
        	if (Date.DEBUG_MODE && Date.DEBUG_MODE === true && window.console) {
        		try {
        			console.group(groupName);
        		}
        		catch(e) {}
        	}
        }
        function _debugEnd(groupName) {
        	if (Date.DEBUG_MODE && Date.DEBUG_MODE === true && window.console) {
        		try {
        			console.groupEnd(groupName);
        		}
        		catch(e) {}
        	}
        }
})();