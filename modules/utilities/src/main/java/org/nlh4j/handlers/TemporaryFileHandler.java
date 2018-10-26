/*
 * @(#)TemporaryFileHandler.java 1.0 Mar 2, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.nlh4j.util.FileUtils;
import org.nlh4j.util.StreamUtils;
import org.springframework.beans.factory.DisposableBean;

import lombok.Getter;

/**
 * Temporary file/directory handler
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class TemporaryFileHandler implements AutoCloseable, DisposableBean, Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Temporary {@link File}
     */
    @Getter
    private File file;
    /**
     * Temporary {@link FileInputStream}
     */
    private transient FileInputStream fis;

    /**
     * Initialize a new instance of {@link TemporaryFileHandler}
     *
     * @param is the source {@link InputStream} to write into file
     * @param filePath the file path to write, and it will be deleted automatically
     *
     * @throws Exception thrown if failed
     */
    public TemporaryFileHandler(final InputStream is, final String filePath) throws Exception {
        FileUtils.toFile(is, filePath, Boolean.TRUE, Boolean.FALSE);
        this.file = new File(filePath);
    }
    /**
     * Initialize a new instance of {@link TemporaryFileHandler}
     *
     * @param is the source {@link InputStream} to write into file
     *
     * @throws Exception thrown if failed
     */
    public TemporaryFileHandler(final InputStream is) throws Exception {
        String tempFile = FileUtils.createTempFile();
        FileUtils.toFile(is, tempFile, Boolean.TRUE, Boolean.FALSE);
        this.file = new File(tempFile);
    }

    /**
     * Get the input stream of temporary file
     *
     * @return the input stream of temporary file
     * @throws IOException thrown if failed
     */
    public final InputStream getAsInputStream() throws IOException {
        if (this.getFile() == null) return null;
     // create new file if not exist
        if (!this.getFile().exists() && !this.getFile().createNewFile()) return null;
        if (this.fis == null) this.fis = new FileInputStream(this.getFile());
        return this.fis;
    }

    /* (Non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        // close stream if necessary
        if (this.fis != null) StreamUtils.closeQuitely(this.fis);
        // delete file or deleting on exit
        FileUtils.safeDelete(this.getFile());
    }

	/* (Non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		this.close();
	}
}
