/*
 * @(#)MimeTypeDetector.java 1.0 May 29, 2015
 * Copyright (C) 2011 Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Come_IN-Team <come_in-team@listserv.uni-siegen.de>
 */
package org.nlh4j.thumbnailer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nlh4j.thumbnailer.mime.DocFileIdentifier;
import org.nlh4j.thumbnailer.mime.MimeTypeIdentifier;
import org.nlh4j.thumbnailer.mime.Office2K7FileIdentifier;
import org.nlh4j.thumbnailer.mime.PptFileIdentifier;
import org.nlh4j.thumbnailer.mime.ScratchFileIdentifier;
import org.nlh4j.thumbnailer.mime.XlsFileIdentifier;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.LogUtils;
import org.nlh4j.util.StreamUtils;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifierFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Wrapper class for MIME Identification of Files.
 *
 * Depends:
 * <li>Aperture (for MIME-Detection)
 */
public class MimeTypeDetector implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** {@link MagicMimeTypeIdentifier} */
	private transient MagicMimeTypeIdentifier mimeTypeIdentifier;
	protected final MagicMimeTypeIdentifier getMimeTypeIdentifier() {
	    if (this.mimeTypeIdentifier == null) {
	        MagicMimeTypeIdentifierFactory mimeTypeFactory = new MagicMimeTypeIdentifierFactory();
	        this.mimeTypeIdentifier = BeanUtils.safeType(mimeTypeFactory.get(), MagicMimeTypeIdentifier.class);
	    }
	    return this.mimeTypeIdentifier;
	}

	/** extended identifier(s) */
	private List<MimeTypeIdentifier> extraIdentifiers;
	protected final List<MimeTypeIdentifier> getExtraIdentifiers() {
	    if (this.extraIdentifiers == null) {
	        this.extraIdentifiers = new LinkedList<MimeTypeIdentifier>();
	    }
	    return this.extraIdentifiers;
	}

	/** cached extension(s) by MIME type */
	private transient Map<String, List<String>> extensionsCache;
	protected final Map<String, List<String>> getCachedExtensions() {
	    if (this.extensionsCache == null) {
	        this.extensionsCache = new LinkedHashMap<String, List<String>>();
	    }
	    return this.extensionsCache;
	}
    protected List<String> getCachedExtensions(String mimeType) {
        if (!StringUtils.hasText(mimeType)) return Collections.emptyList();
        // check cache
        List<String> extensions = this.getCachedExtensions().get(mimeType);
        if (!CollectionUtils.isEmpty(extensions)) {
            return extensions;
        }
        // parse extension(s) by default
        extensions = CollectionUtils.toList(
                this.getMimeTypeIdentifier().getExtensionsFor(mimeType),
                String.class);
        if (!CollectionUtils.isEmpty(extensions)) {
            this.getCachedExtensions().put(mimeType, extensions);
            return extensions;
        }
        // parse extension(s) by extra identifer(s)
        for (MimeTypeIdentifier identifier : this.getExtraIdentifiers()) {
            extensions = identifier.getExtensionsFor(mimeType);
            if (!CollectionUtils.isEmpty(extensions)) break;
        }
        if (!CollectionUtils.isEmpty(extensions)) {
            this.getCachedExtensions().put(mimeType, extensions);
            return extensions;
        }
        return Collections.emptyList();
    }

	/**
	 * Initialize a new instance of class {@link MimeTypeDetector}
	 */
	public MimeTypeDetector() {
		this.addMimeTypeIdentifier(new ScratchFileIdentifier());
		this.addMimeTypeIdentifier(new Office2K7FileIdentifier());
		this.addMimeTypeIdentifier(new PptFileIdentifier());
		this.addMimeTypeIdentifier(new XlsFileIdentifier());
		this.addMimeTypeIdentifier(new DocFileIdentifier());
	}

	/**
	 * Add a new MimeTypeIdentifier to this Detector.
	 * MimeTypeIdentifier may override the decision of the detector.
	 * The order the identifiers are added will also be the order they will be executed
	 * (i.e., the last identifiers may override all others.)
	 *
	 * @param identifier	a new MimeTypeIdentifier
	 */
	public void addMimeTypeIdentifier(MimeTypeIdentifier identifier){
	    Assert.notNull(identifier, "identifier");
		this.getExtraIdentifiers().add(identifier);
	}

	/**
	 * Detect MIME-Type for this file.
	 *
	 * @param file	File to analyse
	 * @return	String of MIME-Type, or null if no detection was possible (or unknown MIME Type)
	 */
	public String getMimeType(File file) {
	    int bytesLength = this.getMimeTypeIdentifier().getMinArrayLength();
		byte[] bytes = new byte[bytesLength];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			fis.read(bytes);
		} catch (IOException e) {
			LogUtils.logError(this.getClass(), e.getMessage(), e);
			return null;
		} finally {
			StreamUtils.closeQuitely(fis);
		}

		// detect MIME type
		try {
    		String fileUrl = file.toURI().toASCIIString();
    		String mimeType = this.getMimeTypeIdentifier().identify(
    		        bytes, file.getPath(), new URIImpl(fileUrl));
    		mimeType = (!StringUtils.hasText(mimeType) ? null : mimeType);
            // Identifiers may re-write MIME.
            for (MimeTypeIdentifier identifier : this.getExtraIdentifiers()) {
                mimeType = identifier.identify(mimeType, bytes, file);
            }
            LogUtils.logDebug("Detected MIME-Type of " + file.getName() + " is " + mimeType);
            return mimeType;
		} catch (Exception e) {
		    LogUtils.logError(this.getClass(), e.getMessage(), e);
            return null;
		}
	}

	/**
	 * Return the standard extension of a specific MIME-Type.
	 * What are these files "normally" called?
	 *
	 * @param mimeType	MIME-Type, e.g. "text/plain"
	 * @return	Extension, e.g. "txt"
	 */
	public String getStandardExtensionForMimeType(String mimeType) {
		List<String> extensions = getExtensionsCached(mimeType);
		if (extensions == null)
			return null;

		try {
			return extensions.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}





	/**
	 * Test if an given extension can contain a File of MIME-Type
	 * @param extension	Filename extension (e.g. "txt")
	 * @param mimeType	MIME-Type		   (e.g. "text/plain")
	 * @return	True if compatible.
	 */
	public boolean doesExtensionMatchMimeType(String extension, String mimeType)
	{
		List<String> extensions = getExtensionsCached(mimeType);

		if (extensions == null)
			return false;

		return extensions.contains(extension);
	}
}
