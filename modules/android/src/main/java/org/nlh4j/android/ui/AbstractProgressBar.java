/*
 * @(#)AbstractProgressBar.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.ui;

import java.io.Serializable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Abstract {@link ProgressBar}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public abstract class AbstractProgressBar extends ProgressBar implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link AbstractProgressBar}
     *
     * @param context application context
     */
    protected AbstractProgressBar(Context context) {
        super(context);
    }
    /**
     * Initialize a new instance of {@link AbstractProgressBar}
     *
     * @param context application context
     * @param attrs {@link AttributeSet}
     */
    protected  AbstractProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * Initialize a new instance of {@link AbstractProgressBar}
     *
     * @param context application context
     * @param attrs {@link AttributeSet}
     * @param defStyle default style
     */
    protected  AbstractProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
