/*
 * @(#)TestBeanUtils.java 1.0 Oct 29, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;

import org.junit.Test;

import lombok.Getter;
import lombok.Setter;

/**
 * Test class {@link BeanUtils}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TestBeanUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public static class newInstanceClassTest implements Serializable {

        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 1L;
        @Getter
        @Setter
        private String field1;
        @Getter
        @Setter
        private String field2;
    }

    /**
     * Test {@link BeanUtils#newInstance(Class)} / {@link BeanUtils#newInstance(Class, Object...)}
     */
    @Test
    public void testNewInstance() {
        try {
            BeanUtils.newInstance(newInstanceClassTest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
