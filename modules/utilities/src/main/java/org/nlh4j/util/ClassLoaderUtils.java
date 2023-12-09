/*
 * @(#)ClassLoaderUtils.java Copyright 2015 by GNU Lesser General
 * Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import com.machinezoo.noexception.Exceptions;

/**
 * {@link ClassLoader} utilities
 */
public final class ClassLoaderUtils implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Load all files/sub-directories {@link URL} from the specified path
	 * 
	 * @param path to load
	 * 
	 * @return found {@link URL}(s) collection
	 */
	public static Collection<URL> loadFiles(String path) {
		Collection<URL> urls = new ArrayList<URL>();
		File f = null;
		try {
			f = new File(path);
			if (f.exists()) urls.add(f.toURI().toURL());
			LogUtils.logDebug(ClassLoaderUtils.class,
					String.format("Found URL from path [%s]: %s", path, f.toURI().getPath()));
		} catch (Exception e) {
			LogUtils.logWarn(ClassLoaderUtils.class,
					String.format("Could not find the URL of [%s]: %s", path, e.getMessage()));
		}

		// continue to load sub files/directories if the present path is directory
		if (f != null && f.exists() && f.isDirectory() && f.canRead()) {
			Stream.of(f.listFiles()).parallel()
			.map(ExceptionUtils.wrap().function(Exceptions.wrap().function(ff -> loadFiles(ff.getPath()))))
			.filter(Optional::isPresent).map(Optional::get)
			.map(Collection.class::cast)
			.flatMap(Collection<URL>::parallelStream)
			.forEach(urls::add);
		}
		return urls;
	}

	/**
	 * Create a new instance {@link ClassLoader} from the specified file/directory path
	 * 
	 * @param path to create
	 * @param parent its parent
	 * 
	 * @return {@link ClassLoader}
	 */
	public static ClassLoader createDirectoryLoader(String path, ClassLoader parent) {
		Collection<URL> urls = loadFiles(path);
		parent = (parent == null ? Thread.currentThread().getContextClassLoader() : parent);
		return URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), parent);
	}
	
	/**
	 * Create a new instance {@link ClassLoader} from the specified file/directory path
	 * 
	 * @param path to create
	 * 
	 * @return {@link ClassLoader}
	 */
	public static ClassLoader createDirectoryLoader(String path) {
		return createDirectoryLoader(path, null);
	}
}
