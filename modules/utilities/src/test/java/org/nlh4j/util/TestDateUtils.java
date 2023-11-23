/*
 * @(#)TestDateUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;

/**
 * @author Hai Nguyen
 *
 */
public final class TestDateUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Test:<br>
     * 1. {@link DateUtils#datesBetween(java.util.Date, java.util.Date)} / {@link DateUtils#datesBetween(java.util.Date, java.util.Date, boolean)}<br>
     * 2. {@link DateUtils#datesBetween(int, int, int, int, int, int)}<br>
     * 3. {@link DateUtils#datesBetween(int, int, int[], int[])}<br>
     * 4. {@link DateUtils#datesBetween(int, int, int[])}<br>
     * 5. {@link DateUtils#datesBetweenByWeekdays(int, int, int[], int[])}
     */
    @Test
    public void testDatesBetween() {
        try {
            Date d1 = DateUtils.toTimestamp(2018, 1, 1);
            Date d2 = DateUtils.toTimestamp(2018, 3, 31);
            System.out.println("1. {@link DateUtils#datesBetween(2018/01/01, 2018/03/31)}");
            final Iterable<Date> iterable1 = DateUtils.datesBetween(d1, d2);
            for(final Iterator<Date> it1 = iterable1.iterator(); it1.hasNext();) {
                Date dt = it1.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
            System.out.println("2. {@link DateUtils#datesBetween(2018, 1, 1, 2018, 3, 15)}");
            final Iterable<Date> iterable2 = DateUtils.datesBetween(2018, 1, 1, 2018, 3, 15);
            for(final Iterator<Date> it2 = iterable2.iterator(); it2.hasNext();) {
                Date dt = it2.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
            System.out.println("3.1 {@link DateUtils#datesBetween(2018, 2019, { 1,15,30 }, { 2, 3 })}");
            final Iterable<Date> iterable31 = DateUtils.datesBetween(2018, 2019, new int[] { 1,15,30 }, new int[] { 2,3 });
            for(final Iterator<Date> it31 = iterable31.iterator(); it31.hasNext();) {
                Date dt = it31.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
            System.out.println("3.2 {@link DateUtils#datesBetween(2018, 2019, {}, { 2, 3 })}");
            final Iterable<Date> iterable32 = DateUtils.datesBetween(2018, 2019, null, new int[] { 2,3 });
            for(final Iterator<Date> it32 = iterable32.iterator(); it32.hasNext();) {
                Date dt = it32.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
            System.out.println("3.3 {@link DateUtils#datesBetween(2018, 2019, {}, {})}");
            final Iterable<Date> iterable33 = DateUtils.datesBetween(2018, 2019, null, null);
            for(final Iterator<Date> it33 = iterable33.iterator(); it33.hasNext();) {
                Date dt = it33.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
            System.out.println("4.1 {@link DateUtils#datesBetween(2018, 2019, {})}");
            final Iterable<Date> iterable41 = DateUtils.datesBetween(2018, 2019, null);
            for(final Iterator<Date> it41 = iterable41.iterator(); it41.hasNext();) {
                Date dt = it41.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
            System.out.println("4.2 {@link DateUtils#datesBetween(2018, 2019, { 2, 3 })}");
            final Iterable<Date> iterable42 = DateUtils.datesBetween(2018, 2019, new int[] { 2,3 });
            for(final Iterator<Date> it42 = iterable42.iterator(); it42.hasNext();) {
                Date dt = it42.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
            System.out.println("5.1 {@link DateUtils#datesBetweenByWeekdays(2018, 2018, { 1,3,5,7 }, { 4 })}");
            final Iterable<Date> iterable51 = DateUtils.datesBetweenByWeekdays(2018, 2019, new int[] { 1,3,5,7 }, new int[] { 4 });
            for(final Iterator<Date> it51 = iterable51.iterator(); it51.hasNext();) {
                Date dt = it51.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
            System.out.println("5.2 {@link DateUtils#datesBetweenByWeekdays(2018, 2018, {}, {})}");
            final Iterable<Date> iterable5 = DateUtils.datesBetweenByWeekdays(2018, 2019, null, null);
            for(final Iterator<Date> it5 = iterable5.iterator(); it5.hasNext();) {
                Date dt = it5.next();
                System.out.println("   - " + DateUtils.formatDateTime("yyyy/MM/dd", dt));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
