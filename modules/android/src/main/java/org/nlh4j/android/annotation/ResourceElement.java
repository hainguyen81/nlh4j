/*
 * @(#)ResourceElement.java 1.0 Oct 27, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import android.view.View;
import android.widget.EditText;

/**
 * Annotation to specified resource identity when generate layout entity elements
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface ResourceElement {
    /**
     * Get field label component resource identity value. Default is 0
     *
     * @return field label component resource identity value
     */
    int value() default 0;

    /**
     * Get a boolean value indicating field caption will be created as default automatically
     *
     * @return true for auto; else false
     */
    boolean caption() default true;

    /**
     * Get field component class.<br>
     * @return field component class. NULL for default {@link EditText} component
     */
    Class<? extends View> component() default EditText.class;

    /**
     * Get the display order of field component. Default is 0.
     * This is also order of group in root layout if this field belongs to any group.
     * @return the display order of field component
     */
    int order() default 0;

    /**
     * Get the group name to group fields that has same group name. Default empty or NULL or non-group
     * @return the group name to group fields that has same group name
     */
    String group() default "";
}
