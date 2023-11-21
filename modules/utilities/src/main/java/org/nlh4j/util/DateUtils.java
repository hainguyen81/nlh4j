/*
 * @(#)DateUtils.java Copyright 2015 by GNU Lesser General
 * Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.joda.time.LocalDateTime;
import org.springframework.util.Assert;

/**
 * Date/time utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@SuppressWarnings("unchecked")
public final class DateUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    public static final String CONST_TIME_STAMP_PATTERN = "yyyy/MM/dd HH:mm";
    public static final String CONST_TIME_FORMAT = "yyyy/MM/dd";

    /**
     * The {@link Calendar} instance for calculating date time
     */
    private static transient Calendar _calendar = null;

    /**
     * Get the {@link Calendar} instance
     *
     * @return the {@link Calendar} instance
     */
    private static final Calendar getCalendar() {
        if (_calendar == null) {
            _calendar = Calendar.getInstance();
        }
        synchronized (_calendar) {
            return _calendar;
        }
    }

    /**
     * Convert string to {@link Timestamp} with format
     *
     * @param strDate
     *            date string
     * @param format
     *            format pattern
     * @return {@link Timestamp} or NULL
     */
    public static Timestamp toTimestamp(String strDate, String format) {
        if (!StringUtils.hasText(strDate)) return null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date parsedDate = dateFormat.parse(strDate);
            return new java.sql.Timestamp(parsedDate.getTime());
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Convert {@link Date} to {@link Timestamp}
     *
     * @param dt
     *            to convert
     * @return {@link Timestamp} or exception if the specified date is NULL
     */
    public static Timestamp toTimestamp(Date dt) {
        Assert.notNull(dt, "The dates parameter must be not null");
        return new Timestamp(dt.getTime());
    }

    /**
     * Merge {@link Date} and {@link Time} to {@link Timestamp}
     *
     * @param dt
     *            to merge
     * @param time
     *            to merge
     * @return {@link Timestamp} or exception if the specified date/time is NULL
     */
    public static java.sql.Timestamp toTimestamp(Date dt, Time time) {
        Assert.notNull(dt, "The dates parameter must be not null");
        Assert.notNull(time, "The times parameter must be not null");
        // remove time from date
        dt = truncate(dt, new int[] { Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND });
        // create new timestamp
        Date dtTmp = new Timestamp(time.getTime());
        dtTmp = set(dtTmp, Calendar.YEAR, get(dt, Calendar.YEAR));
        dtTmp = set(dtTmp, Calendar.MONTH, get(dt, Calendar.MONTH));
        dtTmp = set(dtTmp, Calendar.DAY_OF_MONTH, get(dt, Calendar.DAY_OF_MONTH));
        return new Timestamp(dtTmp.getTime());
    }

    /**
     * Convert {@link Date} to {@link java.sql.Date}
     *
     * @param dt
     *            to convert
     * @return {@link java.sql.Date} or exception if the specified date is NULL
     */
    public static java.sql.Date toSqlDate(Date dt) {
        Assert.notNull(dt, "The dates parameter must be not null");
        return new java.sql.Date(dt.getTime());
    }

    /**
     * Convert {@link Date} to {@link Time}
     *
     * @param dt
     *            to convert
     * @return {@link Time} or exception if the specified date is NULL
     */
    public static Time toSqlTime(Date dt) {
        Assert.notNull(dt, "The dates parameter must be not null");
        return new java.sql.Time(dt.getTime());
    }

    /**
     * Get {@link Time} from hours, minutes, seconds and milli-seconds
     *
     * @param hours
     *            the hours number (0-23)
     * @param minutes
     *            the minutes number (0-59)
     * @param seconds
     *            the seconds number (0-59)
     * @param milliseconds
     *            the milliseconds number (0-999)
     * @return {@link Time} or exception if the parameters are invalid
     */
    public static java.sql.Time toSqlTime(int hours, int minutes, int seconds, int milliseconds) {
        Assert.isTrue(0 <= hours && hours <= 23, "The hours parameter is invalid (0-23)!");
        Assert.isTrue(0 <= minutes && minutes <= 59, "The minutes parameter is invalid (0-59)!");
        Assert.isTrue(0 <= seconds && seconds <= 59, "The seconds parameter is invalid (0-59)!");
        Assert.isTrue(0 <= milliseconds && milliseconds <= 999, "The milliseconds parameter is invalid (0-999)!");
        Calendar calendar = getCalendar();
        synchronized (calendar) {
            calendar.set(Calendar.YEAR, 1970);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR, hours);
            calendar.set(Calendar.MINUTE, minutes);
            calendar.set(Calendar.SECOND, seconds);
            calendar.set(Calendar.MILLISECOND, milliseconds);
            return new java.sql.Time(calendar.getTime().getTime());
        }
    }

    /**
     * Get the year of current date.
     *
     * @return the year of current date.
     */
    public static int currentYear() {
        return get(new Date(), Calendar.YEAR);
    }

    /**
     * Get current date under {@link Timestamp} type
     *
     * @return current date under {@link Timestamp} type
     */
    public static Timestamp currentTimestamp() {
        return toTimestamp(new Date());
    }

    /**
     * Get the {@link Timestamp} by the specified year/month/day
     *
     * @param year
     *            year
     * @param month
     *            month
     * @param day
     *            day
     * @return the {@link Timestamp} by the specified year/month/day
     */
    public static Timestamp toTimestamp(int year, int month, int day) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return toTimestamp(calendar.getTime());
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Get {@link Timestamp}
     *
     * @param year
     *            year
     * @param month
     *            month
     * @param day
     *            day
     * @param hourOfDay
     *            hour
     * @param minute
     *            minute
     * @param second
     *            second
     * @param milli
     *            millisecond
     * @return {@link Timestamp}
     */
    public static Timestamp toTimestamp(int year, int month, int day, int hourOfDay, int minute, int second,
            int milli) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                calendar.set(Calendar.MILLISECOND, milli);
                return toTimestamp(calendar.getTime());
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Get current {@link java.sql.Date}
     *
     * @return current {@link java.sql.Date}
     */
    public static java.sql.Date currentDate() {
        return toSqlDate(new Date());
    }

    /**
     * Get current {@link Time}
     *
     * @return current {@link Time}
     */
    public static Time currentTime() {
        return toSqlTime(new Date());
    }

    /**
     * Get the midnight {@link Time}
     *
     * @return midnight {@link Time}
     */
    public static Time midNightTime() {
        return toSqlTime(0, 0, 0, 0);
    }

    /**
     * Checks the specified date <code>d</code> whether is equals/greater than
     * the first date <code>d1</code> and equals/less than the second date
     * <code>d2</code> (d1 &lt;= d &lt;= d2) base on compare kind.
     * {@link NullPointerException} if one of two dates is null;
     * {@link IllegalArgumentException} if the specified comparision kind
     * <code>what</code> is not supported
     *
     * @param d
     *            the {@link Date} to check
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @param what
     *            specifies compare by what kind: - {@link Calendar#YEAR}:
     *            compare base on year - {@link Calendar#MONTH}: compare base on
     *            year and month - {@link Calendar#DATE}: compare base on
     *            year/month and date - {@link Calendar#HOUR} or
     *            {@link Calendar#HOUR_OF_DAY}: compare base on year/month/date
     *            and hour - {@link Calendar#MINUTE}: compare base on
     *            year/month/date/hour and minute - {@link Calendar#SECOND}:
     *            compare base on year/month/date/hour/minute and second -
     *            {@link Calendar#MILLISECOND}: compare base on whole date (to
     *            miliseconds)
     * @return 0 if two date are equals by the comparision kind 1 if the second
     *         date <code>d2</code> are greater than the first date
     *         <code>d1</code> (<code>d2</code> &gt; <code>d1</code>) by the
     *         comparision kind -1 if the second date <code>d2</code> are less
     *         than the first date <code>d1</code> (<code>d2</code> &lt;
     *         <code>d1</code>) by the comparision kind
     */
    public static boolean between(Date d, Date d1, Date d2, int what) {
        return (0 <= compareTo(d1, d, what) && 0 <= compareTo(d, d2, what));
    }

    /**
     * Compares two specified date (compare to every millisecond).
     * {@link NullPointerException} if one of two dates is null;
     * {@link IllegalArgumentException} if the specified comparision kind
     * <code>what</code> is not supported
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @param what
     *            specifies compare by what kind: - {@link Calendar#YEAR}:
     *            compare base on year - {@link Calendar#MONTH}: compare base on
     *            year and month - {@link Calendar#DATE}: compare base on
     *            year/month and date - {@link Calendar#HOUR} or
     *            {@link Calendar#HOUR_OF_DAY}: compare base on year/month/date
     *            and hour - {@link Calendar#MINUTE}: compare base on
     *            year/month/date/hour and minute - {@link Calendar#SECOND}:
     *            compare base on year/month/date/hour/minute and second -
     *            {@link Calendar#MILLISECOND}: compare base on whole date (to
     *            miliseconds)
     * @return 0 if two date are equals by the comparision kind 1 if the second
     *         date <code>d2</code> are greater than the first date
     *         <code>d1</code> (<code>d2</code> &gt; <code>d1</code>) by the
     *         comparision kind -1 if the second date <code>d2</code> are less
     *         than the first date <code>d1</code> (<code>d2</code> &lt;
     *         <code>d1</code>) by the comparision kind
     */
    public static boolean equals(Date d1, Date d2, int what) {
        if ((d1 == null && d2 != null) || (d1 != null && d2 == null))
            return false;
        return (compareTo(d1, d2, what) == 0);
    }

    /**
     * Compares two specified date (compare to every millisecond).
     * {@link NullPointerException} if one of two dates is null;
     * {@link IllegalArgumentException} if the specified comparision kind
     * <code>what</code> is not supported
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @param what
     *            specifies compare by what kind: - {@link Calendar#YEAR}:
     *            compare base on year - {@link Calendar#MONTH}: compare base on
     *            year and month - {@link Calendar#DATE}: compare base on
     *            year/month and date - {@link Calendar#HOUR} or
     *            {@link Calendar#HOUR_OF_DAY}: compare base on year/month/date
     *            and hour - {@link Calendar#MINUTE}: compare base on
     *            year/month/date/hour and minute - {@link Calendar#SECOND}:
     *            compare base on year/month/date/hour/minute and second -
     *            {@link Calendar#MILLISECOND}: compare base on whole date (to
     *            miliseconds)
     * @return 0 if two date are equals by the comparision kind 1 if the second
     *         date <code>d2</code> are greater than the first date
     *         <code>d1</code> (<code>d2</code> &gt; <code>d1</code>) by the
     *         comparision kind -1 if the second date <code>d2</code> are less
     *         than the first date <code>d1</code> (<code>d2</code> &lt;
     *         <code>d1</code>) by the comparision kind
     */
    public static int compareTo(Date d1, Date d2, int what) {
        Assert.notNull(d1, "The dates parameter must be not null");
        Assert.notNull(d2, "The dates parameter must be not null");
        Calendar calendar = getCalendar();
        synchronized (calendar) {
            switch (what) {
                // compares year
                case Calendar.YEAR: {
                    calendar.setTime(d1);
                    int y1 = calendar.get(what);
                    calendar.setTime(d2);
                    int y2 = calendar.get(what);
                    if (y1 == y2)
                        return 0;
                    else if (y1 < y2)
                        return 1;
                    else
                        return -1;
                }
                // compares year and month
                case Calendar.MONTH: {
                    int compareYear = compareTo(d1, d2, Calendar.YEAR);
                    if (compareYear != 0)
                        return compareYear;
                    calendar.setTime(d1);
                    int m1 = calendar.get(what);
                    calendar.setTime(d2);
                    int m2 = calendar.get(what);
                    if (m1 == m2)
                        return 0;
                    else if (m1 < m2)
                        return 1;
                    else
                        return -1;
                }
                // compares year, month and date
                case Calendar.DATE: {
                    int compareMonth = compareTo(d1, d2, Calendar.MONTH);
                    if (compareMonth != 0)
                        return compareMonth;
                    calendar.setTime(d1);
                    int date1 = calendar.get(what);
                    calendar.setTime(d2);
                    int date2 = calendar.get(what);
                    if (date1 == date2)
                        return 0;
                    else if (date1 < date2)
                        return 1;
                    else
                        return -1;
                }
                // compares year, month, date and hour
                case Calendar.HOUR:
                case Calendar.HOUR_OF_DAY: {
                    int compareDate = compareTo(d1, d2, Calendar.DATE);
                    if (compareDate != 0)
                        return compareDate;
                    calendar.setTime(d1);
                    int h1 = calendar.get(Calendar.HOUR_OF_DAY);
                    calendar.setTime(d2);
                    int h2 = calendar.get(Calendar.HOUR_OF_DAY);
                    if (h1 == h2)
                        return 0;
                    else if (h1 < h2)
                        return 1;
                    else
                        return -1;
                }
                // compares year, month, date, hour and minute
                case Calendar.MINUTE: {
                    int compareHour = compareTo(d1, d2, Calendar.HOUR);
                    if (compareHour != 0)
                        return compareHour;
                    calendar.setTime(d1);
                    int mi1 = calendar.get(what);
                    calendar.setTime(d2);
                    int mi2 = calendar.get(what);
                    if (mi1 == mi2)
                        return 0;
                    else if (mi1 < mi2)
                        return 1;
                    else
                        return -1;
                }
                // compares year, month, date, hour, minute and second
                case Calendar.SECOND: {
                    int compareMinute = compareTo(d1, d2, Calendar.MINUTE);
                    if (compareMinute != 0)
                        return compareMinute;
                    calendar.setTime(d1);
                    int sec1 = calendar.get(what);
                    calendar.setTime(d2);
                    int sec2 = calendar.get(what);
                    if (sec1 == sec2)
                        return 0;
                    else if (sec1 < sec2)
                        return 1;
                    else
                        return -1;
                }
                // compares year, month, date, hour, minute, second and
                // millisecond
                case Calendar.MILLISECOND: {
                    int compareSecond = compareTo(d1, d2, Calendar.SECOND);
                    if (compareSecond != 0)
                        return compareSecond;
                    calendar.setTime(d1);
                    int mili1 = calendar.get(what);
                    calendar.setTime(d2);
                    int mili2 = calendar.get(what);
                    if (mili1 == mili2)
                        return 0;
                    else if (mili1 < mili2)
                        return 1;
                    else
                        return -1;
                }
                // invalid parameter
                default: {
                    throw new IllegalArgumentException("what");
                }
            }
        }
    }

    /**
     * Compares two specified date (compare to every millisecond).
     * {@link NullPointerException} if one of two dates is null;
     * {@link IllegalArgumentException} if the specified comparision kind
     * <code>what</code> is not supported
     *
     * @param d1
     *            the {@link Date} 1
     * @param fmt1
     *            date 1 format pattern
     * @param d2
     *            the {@link Date} 2
     * @param fmt2
     *            date 2 format pattern
     * @param what
     *            specifies compare by what kind: - {@link Calendar#YEAR}:
     *            compare base on year - {@link Calendar#MONTH}: compare base on
     *            year and month - {@link Calendar#DATE}: compare base on
     *            year/month and date - {@link Calendar#HOUR} or
     *            {@link Calendar#HOUR_OF_DAY}: compare base on year/month/date
     *            and hour - {@link Calendar#MINUTE}: compare base on
     *            year/month/date/hour and minute - {@link Calendar#SECOND}:
     *            compare base on year/month/date/hour/minute and second -
     *            {@link Calendar#MILLISECOND}: compare base on whole date (to
     *            miliseconds)
     * @return 0 if two date are equals by the comparision kind 1 if the second
     *         date <code>d2</code> are greater than the first date
     *         <code>d1</code> (<code>d2</code> &gt; <code>d1</code>) by the
     *         comparision kind -1 if the second date <code>d2</code> are less
     *         than the first date <code>d1</code> (<code>d2</code> &lt;
     *         <code>d1</code>) by the comparision kind
     */
    public static int compareTo(String d1, String fmt1, String d2, String fmt2, int what) {
        return compareTo(toTimestamp(d1, fmt1), toTimestamp(d2, fmt2), what);
    }

    /**
     * Compares two specified date (compare to every millisecond).
     * {@link NullPointerException} if one of two dates is null;
     * {@link IllegalArgumentException} if the specified comparision kind
     * <code>what</code> is not supported
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @param what
     *            specifies compare by what kind: - {@link Calendar#YEAR}:
     *            compare base on year - {@link Calendar#MONTH}: compare base on
     *            year and month - {@link Calendar#DATE}: compare base on
     *            year/month and date - {@link Calendar#HOUR} or
     *            {@link Calendar#HOUR_OF_DAY}: compare base on year/month/date
     *            and hour - {@link Calendar#MINUTE}: compare base on
     *            year/month/date/hour and minute - {@link Calendar#SECOND}:
     *            compare base on year/month/date/hour/minute and second -
     *            {@link Calendar#MILLISECOND}: compare base on whole date (to
     *            miliseconds)
     * @return 0 if two date are equals by the comparision kind 1 if the second
     *         date <code>d2</code> are greater than the first date
     *         <code>d1</code> (<code>d2</code> &gt; <code>d1</code>) by the
     *         comparision kind -1 if the second date <code>d2</code> are less
     *         than the first date <code>d1</code> (<code>d2</code> &lt;
     *         <code>d1</code>) by the comparision kind
     */
    public static int compareTo(String d1, String d2, int what) {
        return compareTo(d1, CONST_TIME_STAMP_PATTERN, d2, CONST_TIME_STAMP_PATTERN, what);
    }

    /**
     * Returns the last millisecond of the specified date.
     *
     * @param date
     *            Date to calculate end of day from
     * @return Last millisecond of <code>date</code>
     */
    public static Date endOfDay(Date date) {
        Assert.notNull(date, "The date parameter must be not null");
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MILLISECOND, 999);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MINUTE, 59);
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Returns a new Date with the hours, milliseconds, seconds and minutes set
     * to 0.
     *
     * @param date
     *            Date used in calculating start of day
     * @return Start of <code>date</code>
     */
    public static Date startOfDay(Date date) {
        Assert.notNull(date, "The date parameter must be not null");
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Returns day in millis with the hours, milliseconds, seconds and minutes
     * set to 0.
     *
     * @param date
     *            long used in calculating start of day
     * @return Start of <code>date</code>
     */
    public static long startOfDayInMillis(long date) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(date);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                return calendar.getTimeInMillis();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }

    /**
     * Returns the last millisecond of the specified date.
     *
     * @param date
     *            long to calculate end of day from
     * @return Last millisecond of <code>date</code>
     */
    public static long endOfDayInMillis(long date) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(date);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MILLISECOND, 999);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MINUTE, 59);
                return calendar.getTimeInMillis();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }

    /**
     * Returns the day after <code>date</code>.
     *
     * @param date
     *            Date used in calculating next day
     * @return Day after <code>date</code>.
     */
    public static Date nextDay(Date date) {
        Assert.notNull(date, "The date parameter must be not null");
        long time = addDaysToMillis(date.getTime(), 1);
        return (time > 0 ? new Date(time) : null);
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param time
     *            Base time
     * @param amount
     *            Amount of increment.
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static long addDaysToMillis(long time, int amount) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(time);
                calendar.add(Calendar.DAY_OF_MONTH, amount);
                return calendar.getTimeInMillis();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param time
     *            Base time
     * @param amount
     *            Amount of increment.
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Date addDays(long time, int amount) {
        return addDate(time, Calendar.DAY_OF_MONTH, amount);
    }

    /**
     * Adds <code>amount</code> days to <code>date</code> and returns the
     * resulting time.
     *
     * @param date
     *            Base date
     * @param amount
     *            Amount of increment.
     * @return the <var>date</var> + <var>amount</var> days
     */
    public static Date addDays(Date date, int amount) {
        Assert.notNull(date, "The date parameter must be not null");
        return addDays(date.getTime(), amount);
    }

    /**
     * Adds <code>amount</code> months to <code>time</code> and returns the
     * resulting time.
     *
     * @param time
     *            Base time
     * @param amount
     *            Amount of increment.
     * @return the <var>time</var> + <var>amount</var> months
     */
    public static Date addMonths(long time, int amount) {
        return addDate(time, Calendar.MONTH, amount);
    }

    /**
     * Adds <code>amount</code> months to <code>date</code> and returns the
     * resulting time.
     *
     * @param date
     *            Base date
     * @param amount
     *            Amount of increment.
     * @return the <var>date</var> + <var>amount</var> months
     */
    public static Date addMonths(Date date, int amount) {
        Assert.notNull(date, "The date parameter must be not null");
        return addMonths(date.getTime(), amount);
    }

    /**
     * Adds <code>amount</code> years to <code>time</code> and returns the
     * resulting time.
     *
     * @param time
     *            Base time
     * @param amount
     *            Amount of increment.
     * @return the <var>time</var> + <var>amount</var> years
     */
    public static Date addYears(long time, int amount) {
        return addDate(time, Calendar.YEAR, amount);
    }

    /**
     * Adds <code>amount</code> years to <code>date</code> and returns the
     * resulting time.
     *
     * @param date
     *            Base date
     * @param amount
     *            Amount of increment.
     * @return the <var>date</var> + <var>amount</var> years
     */
    public static Date addYears(Date date, int amount) {
        Assert.notNull(date, "The date parameter must be not null");
        return addYears(date.getTime(), amount);
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param date
     *            to add more
     * @param amount
     *            Amount of increment.
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Date addBusinessDays(Date date, int amount) {
        Assert.notNull(date, "The date parameter must be not null");
        return addBusinessDays(date.getTime(), amount);
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param time
     *            Base time
     * @param amount
     *            Amount of increment.
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Date addBusinessDays(long time, int amount) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(time);
                while (amount > 0) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    // if the date after adding is not weekends;
                    // then decreasing the mount days
                    if ((calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                            && (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
                        amount--;
                    }
                }
                return new Date(calendar.getTimeInMillis());
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param time
     *            Base time
     * @param field
     *            see {@link Calendar#DATE}, {@link Calendar#DAY_OF_MONTH},
     *            etc...
     * @param amount
     *            Amount of increment.
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Date addDate(long time, int field, int amount) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(time);
                calendar.add(field, amount);
                return new Date(calendar.getTimeInMillis());
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Adds <code>amount</code> days to <code>time</code> and returns the
     * resulting time.
     *
     * @param date
     *            to add more
     * @param field
     *            see {@link Calendar#DATE}, {@link Calendar#DAY_OF_MONTH},
     *            etc...
     * @param amount
     *            Amount of increment.
     * @return the <var>time</var> + <var>amount</var> days
     */
    public static Date addDate(Date date, int field, int amount) {
        Assert.notNull(date, "The date parameter must be not null");
        return addDate(date.getTime(), field, amount);
    }

    /**
     * Returns the day after <code>date</code>.
     *
     * @param date
     *            Date used in calculating next day
     * @return Day after <code>date</code>.
     */
    public static long nextDay(long date) {
        return addDaysToMillis(date, 1);
    }

    /**
     * Returns the week after <code>date</code>.
     *
     * @param date
     *            Date used in calculating next week
     * @return week after <code>date</code>.
     */
    public static long nextWeek(long date) {
        return addDaysToMillis(date, 7);
    }

    /**
     * Returns the number of days difference between <code>t1</code> and
     * <code>t2</code>.
     *
     * @param t1
     *            Time 1
     * @param t2
     *            Time 2
     * @param checkOverflow
     *            indicates whether to check for overflow
     * @return Number of days between <code>start</code> and <code>end</code>.
     *         -1 for exception
     */
    public static int daysDiff(long t1, long t2, boolean checkOverflow) {
        if (t1 > t2) {
            long tmp = t1;
            t1 = t2;
            t2 = tmp;
        }
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(t1);
                int delta = 0;
                while (calendar.getTimeInMillis() < t2) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    delta++;
                }
                if (checkOverflow && (calendar.getTimeInMillis() > t2)) {
                    delta--;
                }
                return delta;
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return -1;
        }
    }

    /**
     * Returns the number of days difference between <code>t1</code> and
     * <code>t2</code>.
     *
     * @param t1
     *            Time 1
     * @param t2
     *            Time 2
     * @return Number of days between <code>start</code> and <code>end</code>
     */
    public static int daysDiff(long t1, long t2) {
        return daysDiff(t1, t2, true);
    }

    /**
     * Check, whether the date passed in is the first day of the year.
     *
     * @param date
     *            date to check in millis
     * @return <code>true</code> if <var>date</var> corresponds to the first day
     *         of a year
     * @see Date#getTime()
     */
    public static boolean isFirstOfYear(long date) {
        boolean ret = false;
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(date);
                int currentYear = calendar.get(Calendar.YEAR);
                // Check yesterday
                calendar.add(Calendar.DATE, -1);
                int yesterdayYear = calendar.get(Calendar.YEAR);
                ret = (currentYear != yesterdayYear);
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            ret = false;
        }
        return ret;
    }

    /**
     * Check, whether the date passed in is the first day of the month.
     *
     * @param date
     *            date to check in millis
     * @return <code>true</code> if <var>date</var> corresponds to the first day
     *         of a month
     * @see Date#getTime()
     */
    public static boolean isFirstOfMonth(long date) {
        boolean ret = false;
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(date);
                int currentMonth = calendar.get(Calendar.MONTH);
                // Check yesterday
                calendar.add(Calendar.DATE, -1);
                int yesterdayMonth = calendar.get(Calendar.MONTH);
                ret = (currentMonth != yesterdayMonth);
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            ret = false;
        }
        return ret;
    }

    /**
     * Returns the day before <code>date</code>.
     *
     * @param date
     *            Date used in calculating previous day
     * @return Day before <code>date</code>.
     */
    public static long previousDay(long date) {
        return addDaysToMillis(date, -1);
    }

    /**
     * Returns the week before <code>date</code>.
     *
     * @param date
     *            Date used in calculating previous week
     * @return week before <code>date</code>.
     */
    public static long previousWeek(long date) {
        return addDaysToMillis(date, -7);
    }

    /**
     * Returns the first day before <code>date</code> that has the day of week
     * matching <code>startOfWeek</code>. For example, if you want to find the
     * previous monday relative to <code>date</code> you would call
     * <code>getPreviousDay(date, Calendar.MONDAY)</code>.
     *
     * @param date
     *            Base date
     * @param startOfWeek
     *            Calendar constant correspoding to start of week.
     * @return start of week, return value will have 0 hours, 0 minutes, 0
     *         seconds and 0 ms.
     */
    public static long previousDay(long date, int startOfWeek) {
        return day(date, startOfWeek, -1);
    }

    /**
     * Returns the first day after <code>date</code> that has the day of week
     * matching <code>startOfWeek</code>. For example, if you want to find the
     * next monday relative to <code>date</code> you would call
     * <code>getPreviousDay(date, Calendar.MONDAY)</code>.
     *
     * @param date
     *            Base date
     * @param startOfWeek
     *            Calendar constant correspoding to start of week.
     * @return start of week, return value will have 0 hours, 0 minutes, 0
     *         seconds and 0 ms.
     */
    public static long nextDay(long date, int startOfWeek) {
        return day(date, startOfWeek, 1);
    }

    /**
     * Gets the day by increment and start of week
     *
     * @param date
     *            the date to increase
     * @param startOfWeek
     *            the start of week to get
     * @param increment
     *            the increase unit
     * @return the date in milliseconds by increment and start of week
     */
    private static long day(long date, int startOfWeek, int increment) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(date);
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                // Normalize the view starting date to a week starting day
                while (day != startOfWeek) {
                    calendar.add(Calendar.DATE, increment);
                    day = calendar.get(Calendar.DAY_OF_WEEK);
                }
                return startOfDayInMillis(calendar.getTimeInMillis());
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }

    /**
     * Returns the previous month.
     *
     * @param date
     *            Base date
     * @return previous month
     */
    public static long previousMonth(long date) {
        return incrementMonth(date, -1);
    }

    /**
     * Returns the next month.
     *
     * @param date
     *            Base date
     * @return next month
     */
    public static long nextMonth(long date) {
        return incrementMonth(date, 1);
    }

    /**
     * Increases the specified date amount of months
     *
     * @param date
     *            the date to increase
     * @param increment
     *            amount of months
     * @return the date (in milliseconds) after increasing amount of months
     */
    private static long incrementMonth(long date, int increment) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(date);
                calendar.add(Calendar.MONTH, increment);
                return calendar.getTimeInMillis();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }

    /**
     * Returns the date corresponding to the start of the month.
     *
     * @param date
     *            Base date
     * @return Start of month.
     */
    public static long startOfMonth(long date) {
        return month(date, -1);
    }

    /**
     * Returns the date corresponding to the end of the month.
     *
     * @param date
     *            Base date
     * @return End of month.
     */
    public static long endOfMonth(long date) {
        return month(date, 1);
    }

    /**
     * Increases the specified date amount of months
     *
     * @param date
     *            the date to increase
     * @param increment
     *            amount of months
     * @return the date (in milliseconds) after increasing amount of months
     */
    private static long month(long date, int increment) {
        long result = 0;
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(date);
                if (increment == -1) {
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    result = startOfDayInMillis(calendar.getTimeInMillis());
                }
                else {
                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.add(Calendar.MILLISECOND, -1);
                    result = calendar.getTimeInMillis();
                }
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            result = 0;
        }
        return result;
    }

    /**
     * Returns the day of the week.
     *
     * @param date
     *            date
     * @return day of week.
     */
    public static int dayOfWeek(long date) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTimeInMillis(date);
                return (calendar.get(Calendar.DAY_OF_WEEK));
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return -1;
        }
    }

    /**
     * Returns the ActualMaximum day of the month.
     *
     * @param date
     *            timestamp
     * @return day of month.
     */
    public static int actualMaximum(Date date) {
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(date);
                return (calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }

    /**
     * goals convert from java.util.Date date to java.sql.Date date
     *
     * @param date
     *            to convert
     * @return - a sql date or null if input parameter is null
     */
    public static java.sql.Date toSQLDate(Date date) {
        if (date == null) {
            return null;
        }
        else {
            return new java.sql.Date(date.getTime());
        }
    }

    /**
     * goals convert from java.sql.Date date to java.util.Date date
     *
     * @param date
     *            to convert
     * @return - a sql date or null if input parameter is null
     */
    public static java.util.Date toUtilDate(java.sql.Date date) {
        if (date == null) {
            return null;
        }
        else {
            return new java.util.Date(date.getTime());
        }
    }

    /**
     * Calculates the number of days between two specified {@link Date}
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @return the number of days between two specified {@link Date}
     */
    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * Calculates the number of days between two specified {@link Date}, with
     * the start of date <i>d1</i> to the end of date <i>d2</i>
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @return the number of days between two specified {@link Date}, with the
     *         start of date <i>d1</i> to the end of date <i>d2</i>
     */
    public static int fullDaysBetween(Date d1, Date d2) {
        d1 = startOfDay(d1);
        d2 = endOfDay(d2);
        return daysBetween(d1, d2);
    }

    /**
     * Formats date time
     *
     * @param pattern
     *            the format pattern
     * @param date
     *            (java.sql.date) the date to format
     * @return the formatted date time
     */
    public static String formatDateTime(String pattern, java.sql.Date date) {
        Assert.notNull(date, "The date parameter must be not null");
        return formatDateTime(pattern, toUtilDate(date));
    }

    /**
     * Formats date time
     *
     * @param pattern
     *            the format pattern
     * @param date
     *            (java.util.date) the date to format
     * @return the formatted date time
     */
    public static String formatDateTime(String pattern, java.util.Date date) {
        return formatDateTime(pattern, date, null);
    }

    /**
     * Formats date time
     *
     * @param pattern
     *            the format pattern
     * @param date
     *            (java.util.date) the date to format
     * @param locale
     *            locale
     * @return the formatted date time
     */
    public static String formatDateTime(String pattern, java.util.Date date, Locale locale) {
        String retDate = "";
        if (date != null) {
            locale = (locale == null ? Locale.getDefault() : locale);
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
                retDate = simpleDateFormat.format(date);
            }
            catch (Exception e) {
                LogUtils.logError(DateUtils.class, e.getMessage());
                retDate = "";
            }
        }
        return retDate;
    }

    /**
     * Convert number of days to minutes
     *
     * @param days
     *            to convert
     * @return minutes number
     */
    public static int daysToMinutes(int days) {
        return days * 24 * 60;
    }

    /**
     * Gets the milliseconds between 2 specified {@link Date} (
     * <code>d2 - d1</code>)
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @return the milliseconds between 2 specified {@link Date}
     */
    public static long milliSecondsDiff(Date d1, Date d2) {
        Assert.notNull(d1, "date 1");
        Assert.notNull(d2, "date 2");
        return (d2.getTime() - d1.getTime());
    }

    /**
     * Gets the seconds between 2 specified {@link Date}
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @return the seconds between 2 specified {@link Date}
     */
    public static long secondsDiff(Date d1, Date d2) {
        return (milliSecondsDiff(d1, d2) / 1000);
    }

    /**
     * Gets the minutes between 2 specified {@link Date}
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @return the minutes between 2 specified {@link Date}
     */
    public static long minutesDiff(Date d1, Date d2) {
        return (secondsDiff(d1, d2) / 60);
    }

    /**
     * Gets the hours between 2 specified {@link Date}
     *
     * @param d1
     *            the {@link Date} 1
     * @param d2
     *            the {@link Date} 2
     * @return the hours between 2 specified {@link Date}
     */
    public static long hoursDiff(Date d1, Date d2) {
        return (minutesDiff(d1, d2) / 60);
    }

    /**
     * Gets the date after truncating the specified kinds
     *
     * @param dt
     *            the {@link Date} to parse
     * @param kinds
     *            the kinds to truncate such as {@link Calendar#MILLISECOND},
     *            {@link Calendar#HOUR_OF_DAY}, etc...
     * @return the date in milliseconds with nanoseconds is truncated
     */
    public static Date truncate(Date dt, int... kinds) {
        Assert.notNull(dt, "The date parameter must be not null");
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(dt);
                if (!CollectionUtils.isEmpty(kinds)) {
                    for (int kind : kinds) {
                        calendar.set(kind, 0);
                    }
                }
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Gets the date after truncating the specified kinds
     *
     * @param dt
     *            the {@link Date} to parse
     * @param kinds
     *            the kinds to truncate such as {@link Calendar#MILLISECOND},
     *            {@link Calendar#HOUR_OF_DAY}, etc...
     * @return the date in milliseconds with nanoseconds is truncated
     */
    public static long truncateInMillis(Date dt, int... kinds) {
        Assert.notNull(dt, "The date parameter must be not null");
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(dt);
                if (!CollectionUtils.isEmpty(kinds)) {
                    for (int kind : kinds) {
                        calendar.set(kind, 0);
                    }
                }
                return calendar.getTimeInMillis();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }

    /**
     * Gets a part of the specified {@link Date}
     *
     * @param dt
     *            the {@link Date} to get
     * @param what
     *            the information to get
     * @return a part of the specified {@link Date}
     */
    public static int get(Date dt, int what) {
        Assert.notNull(dt, "The date parameter must be not null");
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(dt);
                return calendar.get(what);
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return -1;
        }
    }

    /**
     * Sets a part of the specified {@link Date}
     *
     * @param dt
     *            the {@link Date} to set
     * @param what
     *            the information to set
     * @param value
     *            the information value
     * @return new applied {@link Date}
     */
    public static Date set(Date dt, int what, int value) {
        Assert.notNull(dt, "The date parameter must be not null");
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(dt);
                calendar.set(what, value);
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Gets the number days in the year of the specified {@link Date}
     *
     * @param dt
     *            the {@link Date} to get
     * @return the number days in the year of the specified {@link Date}
     */
    public static int daysInYear(Date dt) {
        Assert.notNull(dt, "The date parameter must be not null");
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(dt);
                return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }
    /**
     * Gets the number days in the specified year
     *
     * @param year
     *            the year to get
     * @return the number days in the specified year
     */
    public static int daysInYear(int year) {
        return daysInYear(toTimestamp(year, 1, 1));
    }

    /**
     * Gets the number days in the month of the specified {@link Date}
     *
     * @param dt
     *            the {@link Date} to get
     * @return the number days in the month of the specified {@link Date}
     */
    public static int daysInMonth(Date dt) {
        Assert.notNull(dt, "The date parameter must be not null");
        try {
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.setTime(dt);
                return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return 0;
        }
    }
    /**
     * Gets the number days in the specified year and month
     *
     * @param year
     *            the year to get
     * @param month
     *            the month to get (1,...,12). if month is out of range, exception will be thrown
     * @return the number days in the specified year and month
     */
    public static int daysInMonth(int year, int month) {
        Assert.isTrue(1 <= month && month <= 12, "month");
        return daysInMonth(toTimestamp(year, month, 1));
    }
    /**
     * Gets the number days in the current month
     *
     * @return the number days in the current month
     */
    public static int daysInCurrentMonth() {
        return daysInMonth(currentDate());
    }

    /**
     * Gets the first date of year that is specified by date
     *
     * @param dt
     *            the date to parse year
     * @return the first date of year that is specified by date
     */
    public static Date startDateOfYear(Date dt) {
        Assert.notNull(dt, "The date parameter must be not null");
        // parses year
        try {
            int year = get(dt, Calendar.YEAR);
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Gets the first date of month that is specified by date
     *
     * @param dt
     *            the date to parse month
     * @return the first date of month that is specified by date
     */
    public static Date startDateOfMonth(Date dt) {
        Assert.notNull(dt, "The date parameter must be not null");
        // parses year
        try {
            int year = get(dt, Calendar.YEAR);
            int month = get(dt, Calendar.MONTH);
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Gets the end date of year that is specified by date
     *
     * @param dt
     *            the date to parse year
     * @return the first date of year that is specified by date
     */
    public static Date endDateOfYear(Date dt) {
        Assert.notNull(dt, "The date parameter must be not null");
        // parses year
        try {
            int year = get(dt, Calendar.YEAR);
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.set(Calendar.YEAR, year + 1);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Gets the end date of month that is specified by date
     *
     * @param dt
     *            the date to parse month
     * @return the first date of month that is specified by date
     */
    public static Date endDateOfMonth(Date dt) {
        Assert.notNull(dt, "The date parameter must be not null");
        // parses year
        try {
            int year = get(dt, Calendar.YEAR);
            int month = get(dt, Calendar.MONTH);
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month + 1);
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Gets the first date of next year that is specified by date
     *
     * @param dt
     *            the date to parse year
     * @return the first date of next year that is specified by date
     */
    public static Date getStartDateOfNextYear(Date dt) {
        Assert.notNull(dt, "The date parameter must be not null");
        // parses year
        try {
            int year = get(dt, Calendar.YEAR);
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.set(Calendar.YEAR, (year + 1));
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTime();
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Truncate the specified date
     *
     * @param dt
     *            to truncate
     * @return truncated time
     */
    public static Time truncatDate(Date dt) {
        Assert.notNull(dt, "The date parameter must be not null");
        // parses time
        try {
            int hour = get(dt, Calendar.HOUR_OF_DAY);
            int minute = get(dt, Calendar.MINUTE);
            int second = get(dt, Calendar.SECOND);
            int miliSecond = get(dt, Calendar.MILLISECOND);
            Calendar calendar = getCalendar();
            synchronized (calendar) {
                calendar.set(Calendar.YEAR, 1970);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);
                calendar.set(Calendar.MILLISECOND, miliSecond);
                return new java.sql.Time(calendar.getTime().getTime());
            }
        }
        catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Get the milliseconds of day by the specified {@link Time}
     *
     * @param time
     *            the time to parse
     * @return the milliseconds of day
     */
    public static long localTimeMilli(Time time) {
        Assert.notNull(time, "The parameter must be not null");
        return (new LocalDateTime(time)).getMillisOfDay();
    }

    /**
     * Compare and find the first occurred maximum value.<br>
     * NULL for ignoring comparison
     *
     * @param <T>
     *            {@link Date} type
     * @param values
     *            to compare. NULL or empty for returning NULL
     * @param what
     *            specifies compare by what kind: - {@link Calendar#YEAR}:
     *            compare base on year - {@link Calendar#MONTH}: compare base on
     *            year and month - {@link Calendar#DATE}: compare base on
     *            year/month and date - {@link Calendar#HOUR} or
     *            {@link Calendar#HOUR_OF_DAY}: compare base on year/month/date
     *            and hour - {@link Calendar#MINUTE}: compare base on
     *            year/month/date/hour and minute - {@link Calendar#SECOND}:
     *            compare base on year/month/date/hour/minute and second -
     *            {@link Calendar#MILLISECOND}: compare base on whole date (to
     *            miliseconds)
     * @return the maximum value
     */
    public static <T extends Date> T max(int what, T... values) {
        T maxVal = null;
        if (!CollectionUtils.isEmpty(values)) {
            for (T value : values) {
                if ((value != null && maxVal == null)
                        || (maxVal != null && value != null
                                && compareTo(maxVal, value, what) >= 0)) {
                    maxVal = value;
                }
            }
        }
        return maxVal;
    }

    /**
     * Compare and find the first occurred minimum value.<br>
     * NULL for ignoring comparison
     *
     * @param <T>
     *            {@link Date} type
     * @param values
     *            to compare. NULL or empty for returning NULL
     * @param what
     *            specifies compare by what kind: - {@link Calendar#YEAR}:
     *            compare base on year - {@link Calendar#MONTH}: compare base on
     *            year and month - {@link Calendar#DATE}: compare base on
     *            year/month and date - {@link Calendar#HOUR} or
     *            {@link Calendar#HOUR_OF_DAY}: compare base on year/month/date
     *            and hour - {@link Calendar#MINUTE}: compare base on
     *            year/month/date/hour and minute - {@link Calendar#SECOND}:
     *            compare base on year/month/date/hour/minute and second -
     *            {@link Calendar#MILLISECOND}: compare base on whole date (to
     *            miliseconds)
     * @return the minimum value
     */
    public static <T extends Date> T min(int what, T... values) {
        T minVal = null;
        if (!CollectionUtils.isEmpty(values)) {
            for (T value : values) {
                if ((value != null && minVal == null)
                        || (minVal != null && value != null
                                && compareTo(minVal, value, what) <= 0)) {
                    minVal = value;
                }
            }
        }
        return minVal;
    }

    /**
     * Compare and find the first occurred maximum value.
     *
     * @param <T>
     *            {@link Date} type
     * @param values
     *            to compare. NULL or empty for returning {@link Byte#MAX_VALUE}
     * @return the maximum value or {@link Byte#MAX_VALUE}
     */
    public static <T extends Date> T max(T... values) {
        T maxVal = null;
        if (!CollectionUtils.isEmpty(values)) {
            for (T value : values) {
                if ((value != null && maxVal == null)
                        || (maxVal != null && value != null
                                && compareTo(maxVal, value, Calendar.MILLISECOND) >= 0)) {
                    maxVal = value;
                }
            }
        }
        return maxVal;
    }

    /**
     * Compare and find the first occurred minimum value.
     *
     * @param <T>
     *            {@link Date} type
     * @param values
     *            to compare. NULL or empty for returning {@link Byte#MIN_VALUE}
     * @return the minimum value or {@link Byte#MIN_VALUE}
     */
    public static <T extends Date> T min(T... values) {
        T minVal = null;
        if (!CollectionUtils.isEmpty(values)) {
            for (T value : values) {
                if ((value != null && minVal == null)
                        || (minVal != null && value != null
                                && compareTo(minVal, value, Calendar.MILLISECOND) <= 0)) {
                    minVal = value;
                }
            }
        }
        return minVal;
    }

    /**
     * Get the {@link Date} list between the specified two {@link Date}
     * (included both the start/end dates)
     *
     * @param start
     *            the start {@link Date}
     * @param end
     *            the end {@link Date}
     * @param swap
     *            specify swap the start/end {@link Date} if the start
     *            {@link Date} is greater than the end {@link Date}
     * @return the {@link Date} list between the specified two {@link Date}
     *         (included both the start/end dates)
     */
    public static Iterable<Date> datesBetween(final Date start, final Date end, boolean swap) {
        Assert.notNull(start, "start");
        Assert.notNull(end, "end");
        Assert.isTrue(start.before(end) || swap, "Invalid Range");
        final Date stDt = (start.after(end) && swap ? end : start);
        final Date ndDt = (start.after(end) && swap ? start : end);
        return new Iterable<Date>() {

            /*
             *
             */
            @Override
            public Iterator<Date> iterator() {
                return new Iterator<Date>() {
                    private Date next = stDt;

                    /*
                     *
                     */
                    @Override
                    public boolean hasNext() {
                        return !this.next.after(ndDt);
                    }

                    /*
                     *
                     */
                    @Override
                    public Date next() {
                        if (this.next.after(ndDt)) { throw new NoSuchElementException(); }
                        Date ret = Date.from(this.next.toInstant());
                        this.next = Date.from(this.next.toInstant().plus(1, ChronoUnit.DAYS));
                        return ret;
                    }

                    /*
                     *
                     */
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
    /**
     * Get the {@link Date} list between the specified two {@link Date}
     * (included both the start/end dates)
     *
     * @param start
     *            the start {@link Date}
     * @param end
     *            the end {@link Date}
     * @return the {@link Date} list between the specified two {@link Date}
     *         (included both the start/end dates)
     */
    public static Iterable<Date> datesBetween(final Date start, final Date end) {
        return datesBetween(start, end, Boolean.TRUE);
    }
    /**
     * Get the {@link Date} list between the specified two {@link Date}
     * (included both the start/end dates)
     * @param ySt the year of start date
     * @param mSt the month of start date. 0 for the end month of start year
     * @param dSt the date of start date. 0 for the end date of month
     * @param yEd the year of end date
     * @param mEd the month of end date. 0 for the end month of end year
     * @param dEd the date of end date. 0 for the end date of month
     * @return the {@link Date} list of days and months array from the specified start year to end year
     */
    public static Iterable<Date> datesBetween(int ySt, int mSt, int dSt, int yEd, int mEd, int dEd) {
        try {
            mSt = Math.max(mSt, 0);
            boolean emOfSt = (mSt == 0);
            dSt = Math.max(dSt, 0);
            boolean edOfSt = (dSt == 0);
            mEd = Math.max(mEd, 0);
            boolean emOfEd = (mEd == 0);
            dEd = Math.max(dEd, 0);
            boolean edOfEd = (dEd == 0);
            Date dtSt = toTimestamp(ySt, 1, 1);
            dtSt = (emOfSt ? endDateOfYear(dtSt) : set(dtSt, Calendar.MONTH, mSt - 1));
            dtSt = (edOfSt ? endDateOfMonth(dtSt) : set(dtSt, Calendar.DATE, dSt));
            Date dtEd = toTimestamp(yEd, mEd, 1);
            dtEd = (emOfEd ? endDateOfYear(dtEd) : set(dtEd, Calendar.MONTH, mEd - 1));
            dtEd = (edOfEd ? endDateOfMonth(dtEd) : set(dtEd, Calendar.DATE, dEd));
            return datesBetween(dtSt, dtEd);
        } catch (Exception e) {
            LogUtils.logError(DateUtils.class, e.getMessage());
            return null;
        }
    }

    /**
     * Get the {@link Date} list of days and months array from the specified start year to end year
     * @param ySt the start year. 0 for current year
     * @param yEd the end year. 0 for current year
     * @param days the days array. NULL/empty for current date
     * @param months the months array. NULL/empty for current date
     * @param swap specify swaping the start/end year
     * @return the {@link Date} list of days and months array from the specified start year to end year
     */
    public static Iterable<Date> datesBetween(final int ySt, final int yEd, final int[] days, final int[] months, boolean swap) {
        Assert.isTrue(ySt <= yEd || swap, "Invalid Range");
        Date curDate = currentDate();
        int curYear = get(curDate, Calendar.YEAR);
        int curMonth = get(curDate, Calendar.MONTH) + 1;
        int curDay = get(curDate, Calendar.DATE);
        final int[] mm = (CollectionUtils.isEmpty(months) ? new int[] { curMonth } : months);
        final int[] dd = (CollectionUtils.isEmpty(days) ? new int[] { curDay } : days);
        int minM = Math.max(NumberUtils.min(mm), 1);
        int minD = Math.max(NumberUtils.min(dd), 1);
        int maxM = Math.min(NumberUtils.max(mm), 12);
        int maxD = Math.min(NumberUtils.max(dd), 31);
        int yStart = (ySt > yEd && swap ? yEd : ySt);
        int yEnd = (ySt > yEd && swap ? ySt : yEd);
        yStart = Math.max(yStart, 0);
        yStart = (yStart <= 0 ? curYear : yStart);
        yEnd = Math.max(yEnd, 0);
        yEnd = (yEnd <= 0 ? curYear : yEnd);
        final Date stDt = toTimestamp(yStart, minM, minD);
        int dayEd = get(endDateOfMonth(toTimestamp(yEd, maxM, 1)), Calendar.DATE);
        maxD = Math.min(maxD, dayEd);
        final Date edDt = toTimestamp(yEnd, maxM, maxD);
        Arrays.sort(dd);
        Arrays.sort(mm);
        return new Iterable<Date>() {

            /*
             *
             */
            @Override
            public Iterator<Date> iterator() {
                return new Iterator<Date>() {
                    private Date next = stDt;
                    private int dIdx = 0;
                    private int mIdx = 0;

                    /*
                     *
                     */
                    @Override
                    public boolean hasNext() {
                        return (this.next != null && !this.next.after(edDt));
                    }

                    /*
                     *
                     */
                    @Override
                    public Date next() {
                        if (this.next == null || this.next.after(edDt)) { throw new NoSuchElementException(); }
                        Date ret = Date.from(this.next.toInstant());
                        // apply next date
                        boolean incYear = false;
                        int daysInMonth = 0;
                        do {
                            dIdx++;
                            if (dIdx >= dd.length) {
                                mIdx++;
                                dIdx = 0;
                            }
                            if (mIdx >= mm.length) {
                                mIdx = 0;
                                dIdx = 0;
                                incYear = true;
                            }
                            this.next = set(set(Date.from(this.next.toInstant()), Calendar.DATE, 1), Calendar.MONTH, mm[mIdx] - 1);
                            if (incYear) this.next = addYears(this.next, 1);
                            daysInMonth = daysInMonth(this.next);
                        } while(dd[dIdx] > daysInMonth && this.next.before(edDt));
                        if (this.next.before(edDt)) this.next = set(this.next, Calendar.DATE, dd[dIdx]);
                        return ret;
                    }

                    /*
                     *
                     */
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * Get the {@link Date} list of days and months array from the specified start year to end year
     * @param ySt the start year
     * @param yEd the end year
     * @param days the days array. NULL/empty for current date
     * @param months the months array. NULL/empty for current date
     * @return the {@link Date} list of days and months array from the specified start year to end year
     */
    public static Iterable<Date> datesBetween(final int ySt, final int yEd, final int[] days, final int[] months) {
        return datesBetween(ySt, yEd, days, months, Boolean.TRUE);
    }

    /**
     * Get the {@link Date} list of months array from the specified start year to end year (full days in a month)
     * @param ySt the start year. 0 for current year
     * @param yEd the end year. 0 for current year
     * @param months the months array. NULL/empty for current date
     * @param swap specify swaping the start/end year
     * @return the {@link Date} list of months array from the specified start year to end year
     */
    public static Iterable<Date> datesBetween(final int ySt, final int yEd, final int[] months, boolean swap) {
        int[] days = new int[31];
        for(int i = 1; i <= 31; i++) days[i - 1] = i;
        return datesBetween(ySt, yEd, days, months, swap);
    }

    /**
     * Get the {@link Date} list of months array from the specified start year to end year (full days in a month)
     * @param ySt the start year
     * @param yEd the end year
     * @param months the months array. NULL/empty for current date
     * @return the {@link Date} list of months array from the specified start year to end year
     */
    public static Iterable<Date> datesBetween(final int ySt, final int yEd, final int[] months) {
        return datesBetween(ySt, yEd, months, Boolean.TRUE);
    }

    /**
     * Get the {@link Date} list of days and months array from the specified start year to end year
     * @param ySt the start year. 0 for current year
     * @param yEd the end year. 0 for current year
     * @param weekdays the weekdays array. NULL/empty for current date
     * @param months the months array. NULL/empty for current date
     * @param swap specify swaping the start/end year
     * @return the {@link Date} list of days and months array from the specified start year to end year
     */
    public static Iterable<Date> datesBetweenByWeekdays(final int ySt, final int yEd, final int[] weekdays, final int[] months, boolean swap) {
        Assert.isTrue(ySt <= yEd || swap, "Invalid Range");
        Date curDate = currentDate();
        int curYear = get(curDate, Calendar.YEAR);
        int curMonth = get(curDate, Calendar.MONTH) + 1;
        int curWeek = get(curDate, Calendar.DAY_OF_WEEK);
        // get the months/weekdays array
        final int[] mm = (CollectionUtils.isEmpty(months) ? new int[] { curMonth } : months);
        final int[] ww = (CollectionUtils.isEmpty(weekdays) ? new int[] { curWeek } : weekdays);
        // sort months/weekdays array
        Arrays.sort(ww);
        Arrays.sort(mm);
        // find the start date
        int minM = Math.max(NumberUtils.min(mm), 1);
        int maxM = Math.min(NumberUtils.max(mm), 12);
        int yStart = (ySt > yEd && swap ? yEd : ySt);
        int yEnd = (ySt > yEd && swap ? ySt : yEd);
        yStart = Math.max(yStart, 0);
        yStart = (yStart <= 0 ? curYear : yStart);
        yEnd = Math.max(yEnd, 0);
        yEnd = (yEnd <= 0 ? curYear : yEnd);
        int daysInMonth = daysInMonth(yEnd, maxM);
        Date dt = toTimestamp(yStart, minM, 1);
        // find the week day of the start date
        int wkd = get(dt, Calendar.DAY_OF_WEEK);
        // find the first weekday that later the weekday of the start date
        int wkIdx = 0;
        for(int i = 0; i < ww.length; i++) {
            if (ww[i] >= wkd) {
                wkIdx = i;
                break;
            }
        }
        final int wkStIdx = wkIdx;
        final Date stDt = (ww[wkStIdx] == wkd ? dt : set(dt, Calendar.DAY_OF_WEEK, ww[wkStIdx]));
        final Date edDt = endDateOfMonth(toTimestamp(yEnd, maxM, daysInMonth));
        final int weekNumber = get(stDt, Calendar.WEEK_OF_YEAR);
        return new Iterable<Date>() {

            /*
             *
             */
            @Override
            public Iterator<Date> iterator() {
                return new Iterator<Date>() {
                    private Date next = stDt;
                    private int wkNum = weekNumber;
                    private int wIdx = wkStIdx;
                    private int mIdx = 0;

                    /*
                     *
                     */
                    @Override
                    public boolean hasNext() {
                        return (mIdx < mm.length && !this.next.after(edDt));
                    }

                    /*
                     *
                     */
                    @Override
                    public Date next() {
                        if (mIdx >= mm.length || this.next.after(edDt)) { throw new NoSuchElementException(); }
                        Date ret = Date.from(this.next.toInstant());
                        // apply next date
                        boolean incYear = false;
                        this.wIdx++;
                        if (this.wIdx >= ww.length) {
                            this.wkNum++;
                            this.wIdx = 0;
                        }
                        // apply week number of year and weekday
                        this.next = set(this.next, Calendar.WEEK_OF_YEAR, this.wkNum);
                        this.next = set(this.next, Calendar.DAY_OF_WEEK, ww[this.wIdx]);
                        // check current month
                        int m = get(this.next, Calendar.MONTH) + 1;
                        if (mm[this.mIdx] != m) {
                            this.mIdx++;
                            this.wIdx = 0;
                        }
                        // check need to change year
                        if (this.mIdx >= mm.length) {
                            this.mIdx = 0;
                            this.wIdx = 0;
                            this.wkNum = 0;
                            incYear = true;
                        }
                        if (incYear) {
                            // the first date of next year
                            this.next = getStartDateOfNextYear(this.next);
                            if (this.next.before(edDt)) {
                                // apply week number and weekday
                                this.next = set(this.next, Calendar.MONTH, mm[mIdx] - 1);
                                this.wkNum = get(this.next, Calendar.WEEK_OF_YEAR);
                                // find the nearest weekday
                                int wkd = get(this.next, Calendar.DAY_OF_WEEK);
                                for(int i = 0; i < ww.length; i++) {
                                    if (ww[i] >= wkd) {
                                        this.wIdx = i;
                                        break;
                                    }
                                }
                            }
                        }
                        // if need to apply next date
                        if (this.next.before(edDt)) {
                            this.next = set(this.next, Calendar.WEEK_OF_YEAR, this.wkNum);
                            this.next = set(this.next, Calendar.DAY_OF_WEEK, ww[this.wIdx]);
                        }
                        return ret;
                    }

                    /*
                     *
                     */
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
    /**
     * Get the {@link Date} list of days and months array from the specified start year to end year
     * @param ySt the start year. 0 for current year
     * @param yEd the end year. 0 for current year
     * @param weekdays the weekdays array. NULL/empty for current date
     * @param months the months array. NULL/empty for current date
     * @return the {@link Date} list of days and months array from the specified start year to end year
     */
    public static Iterable<Date> datesBetweenByWeekdays(final int ySt, final int yEd, final int[] weekdays, final int[] months) {
        return datesBetweenByWeekdays(ySt, yEd, weekdays, months, Boolean.TRUE);
    }
}
