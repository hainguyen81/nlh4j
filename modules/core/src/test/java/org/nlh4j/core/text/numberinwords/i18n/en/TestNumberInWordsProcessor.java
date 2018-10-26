/*
 * @(#)TestNumberInWordsProcessor.java 1.0 Oct 5, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.en;

import java.io.Serializable;

import org.junit.Test;

/**
 * Test class of {@link NumberInWordsProcessor}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TestNumberInWordsProcessor implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Test {@link NumberInWordsProcessor}
     */
    @Test
    public static void numberInEnglishWords() {
        NumberInWordsProcessor processor = new NumberInWordsProcessor();
        long[] values = new long[] {
                0,
                4,
                10,
                12,
                100,
                108,
                299,
                1000,
                1003,
                2040,
                45213,
                100000,
                100005,
                100010,
                202020,
                202022,
                999999,
                1000000,
                1000001,
                10000000,
                10000007,
                99999999,
                Long.MAX_VALUE,
                Long.MIN_VALUE
        };
        String[] strValues = new String[] {
                "0001.2",
                "3.141592",
                "11.00"
        };

        for (long val : values) {
            System.out.println(val + " = " + processor.getName(val));
        }

        for (String strVal : strValues) {
            System.out.println(strVal + " = " + processor.getName(strVal));
        }

        // generate a very big number...
        StringBuilder bigNumber = new StringBuilder();
        for (int d = 0; d < 66; d++) {
            bigNumber.append((char) ((Math.random() * 10) + '0'));
        }
        bigNumber.append(".");
        for (int d = 0; d < 26; d++) {
            bigNumber.append((char) ((Math.random() * 10) + '0'));
        }

        System.out.println(bigNumber.toString() + " = " + processor.getName(bigNumber.toString()));
    }
}
