/*
 * @(#)CommonsMultipartResolver.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.handlers;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import org.nlh4j.util.FileUtils;

/**
 * An extended class of {@link org.springframework.web.multipart.commons.CommonsMultipartResolver}
 * for resolving error:<br>
 * {@link IllegalArgumentException}: Given uploadTempDir could not be created
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class CommonsMultipartResolver
        extends org.springframework.web.multipart.commons.CommonsMultipartResolver
        implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * slf4j
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /* (Non-Javadoc)
     * @see org.springframework.web.multipart.commons.CommonsFileUploadSupport#setUploadTempDir(org.springframework.core.io.Resource)
     */
    @Override
    public void setUploadTempDir(Resource uploadTempDir) throws IOException {
        if (uploadTempDir != null && uploadTempDir.getFile() != null) {
            // ensure upload temporary directory
            if (FileUtils.ensureDirectory(uploadTempDir.getFile().getPath())) {
                // FIXME try catch for SPRING exception while temporary directory has been created
                try { super.setUploadTempDir(uploadTempDir); }
                catch (Exception e) { logger.error("[NOT IMPORTANT] " + e.getMessage()); }

            } else if (logger.isDebugEnabled()) {
                logger.debug("Could not creating the specified uploaded temporary directory!");
            }

        } else if (logger.isDebugEnabled()) {
            logger.debug("Not specify the temporary upload directory!");
        }
    }
}
