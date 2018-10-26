/*
 * @(#)PropertyUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Property utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class PropertyUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private final boolean ENCRYPTED_FILE = Boolean.FALSE;

    /** Constant for the escaping character.*/
    private static final String ESCAPE = "\\";

    /** Constant for the escaped escaping character.*/
    private static final String DOUBLE_ESC = ESCAPE + ESCAPE;

    /** Constant for the initial size when creating a string buffer. */
    private static final int BUF_SIZE = 8;

    /** The list of possible key/value separators */
    private static final String SEPARATORS = "=:";

    /** The white space characters used as key/value separators. */
    private static final String WHITE_SPACE = " \t\f";

    /** The default properties file extension */
    private static final String DEFAULT_PROPERTIES_EXTENSION = ".properties";

	/**
	 * The system configuration instance
	 */
	private transient Configuration configuration = null;
	private String propertyPassword;
	private String propertyFile;

    /**
     * Initializes a new instance of the {@link PropertyUtils} class.
     *
     * @param fileName property file
     */
    public PropertyUtils(final String fileName) {
        this(null, fileName, null);
    }
    /**
     * Initializes a new instance of the {@link PropertyUtils} class.
     *
     * @param fileName property file
     * @param propPassword password that has been used to encrypted properties file
     */
    public PropertyUtils(final String fileName, final String propPassword) {
        this(null, fileName, propPassword);
    }
	/**
	 * Initializes a new instance of the {@link PropertyUtils} class.
	 *
	 * @param classLoader class loader
     * @param fileName property file
     * @param propPassword password that has been used to encrypted properties file. NULL/empty for default
	 */
	public PropertyUtils(ClassLoader classLoader, final String fileName, final String propPassword) {
		// checks parameter
		Assert.hasText(fileName, "fileName");
		this.propertyPassword = propPassword;
		try {
		    classLoader = (classLoader == null ? this.getClass().getClassLoader() : classLoader);
			String confPath = getConfigurePath(classLoader, fileName);
			if (StringUtils.hasText(confPath)) {
				this.propertyFile = confPath;
    			// if only encrypt value (not encrypted file)
    			if (!Boolean.TRUE.equals(ENCRYPTED_FILE)) {
    				this.configuration = new PropertiesConfiguration(confPath) {

    					/**
    					 * (non-Javadoc)
    					 * @see org.apache.commons.configuration.PropertiesConfiguration#getIOFactory()
    					 */
    					@Override
    					public IOFactory getIOFactory() {
    						return new IOFactory() {

    							/**
    							 * (non-Javadoc)
    							 * @see org.apache.commons.configuration.PropertiesConfiguration.IOFactory#createPropertiesReader(java.io.Reader, char)
    							 */
    							@Override
    							public PropertiesReader createPropertiesReader(Reader in, char delimiter) {
    								return new PropertiesReader(in, delimiter) {

    									/**
    									 * (non-Javadoc)
    									 * @see org.apache.commons.configuration.PropertiesConfiguration.PropertiesReader#getPropertyValue()
    									 */
    									@Override
    									public String getPropertyValue() {
    										String keyaspasswd = super.getPropertyName();
    										String value = super.getPropertyValue();
    										value = (value == null ? "" : value.trim());
    										// if encrypting properties
    										if (StringUtils.hasText(PropertyUtils.this.getPassword())) {
    											return EncryptUtils.decrypt(keyaspasswd, value);
    										}
    										return value;
    									}
    								};
    							}

    							/**
    							 * (non-Javadoc)
    							 * @see org.apache.commons.configuration.PropertiesConfiguration.IOFactory#createPropertiesWriter(java.io.Writer, char)
    							 */
    							@Override
    							public PropertiesWriter createPropertiesWriter(Writer out, final char delimiter) {
    								return new PropertiesWriter(out, delimiter) {

    									/**
    									 * Performs the escaping of backslashes in the specified properties
    									 * value. Because a double backslash is used to escape the escape
    									 * character of a list delimiter, double backslashes also have to be
    									 * escaped if the property is part of a (single line) list. Then, in all
    									 * cases each backslash has to be doubled in order to produce a valid
    									 * properties file.
    									 *
    									 * @param value the value to be escaped
    									 * @param inList a flag whether the value is part of a list
    									 * @return the value with escaped backslashes as string
    									 */
    									private String handleBackslashs(Object value, boolean inList) {
    										String strValue = String.valueOf(value);
    										if (inList && strValue.indexOf(DOUBLE_ESC) >= 0) {
    											char esc = ESCAPE.charAt(0);
    											StringBuffer buf = new StringBuffer(strValue.length() + BUF_SIZE);
    											for (int i = 0; i < strValue.length(); i++) {
    												if (strValue.charAt(i) == esc && i < strValue.length() - 1
    														&& strValue.charAt(i + 1) == esc) {
    													buf.append(DOUBLE_ESC).append(DOUBLE_ESC);
    													i++;
    												}
    												else {
    													buf.append(strValue.charAt(i));
    												}
    											}
    											strValue = buf.toString();
    										}
    										return StringEscapeUtils.escapeJava(strValue);
    									}
    									/**
    									 * Escapes the given property value. Delimiter characters in the value
    									 * will be escaped.
    									 *
    									 * @param value the property value
    									 * @param inList a flag whether the value is part of a list
    									 * @return the escaped property value
    									 * @since 1.3
    									 */
    									private String escapeValue(Object value, boolean inList) {
    										String escapedValue = handleBackslashs(value, inList);
    										if (delimiter != 0) {
    											escapedValue = org.springframework.util.StringUtils.replace(
    											        escapedValue, String.valueOf(delimiter), ESCAPE + delimiter);
    										}
    										return escapedValue;
    									}
    									/**
    									 * Returns the number of trailing backslashes. This is sometimes needed for
    									 * the correct handling of escape characters.
    									 *
    									 * @param line the string to investigate
    									 * @return the number of trailing backslashes
    									 */
    									private int countTrailingBS(String line) {
    										int bsCount = 0;
    										for (int idx = line.length() - 1; idx >= 0 && line.charAt(idx) == '\\'; idx--) {
    											bsCount++;
    										}
    										return bsCount;
    									}
    									/**
    									 * Transforms a list of values into a single line value.
    									 *
    									 * @param values the list with the values
    									 * @return a string with the single line value (can be <b>null</b>)
    									 * @since 1.3
    									 */
    									private String makeSingleLineValue(List<?> values) {
    										if (!values.isEmpty()) {
    											Iterator<?> it = values.iterator();
    											String lastValue = escapeValue(it.next(), true);
    											StringBuffer buf = new StringBuffer(lastValue);
    											while (it.hasNext()) {
    												// if the last value ended with an escape character, it has
    												// to be escaped itself; otherwise the list delimiter will
    												// be escaped
    												if (lastValue.endsWith(ESCAPE) && (countTrailingBS(lastValue) / 2) % 2 != 0) {
    													buf.append(ESCAPE).append(ESCAPE);
    												}
    												buf.append(delimiter);
    												lastValue = escapeValue(it.next(), true);
    												buf.append(lastValue);
    											}
    											return buf.toString();
    										}
    										else {
    											return null;
    										}
    									}
    									/**
    									 * Escape the separators in the key.
    									 *
    									 * @param key the key
    									 * @return the escaped key
    									 * @since 1.2
    									 */
    									private String escapeKey(String key) {
    										StringBuffer newkey = new StringBuffer();
    										char[] separators = SEPARATORS.toCharArray();
    										char[] whiteSpaces = WHITE_SPACE.toCharArray();
    										for (int i = 0; i < key.length(); i++) {
    											char c = key.charAt(i);
    											if (ArrayUtils.contains(separators, c) || ArrayUtils.contains(whiteSpaces, c)) {
    												// escape the separator
    												newkey.append('\\');
    												newkey.append(c);
    											}
    											else {
    												newkey.append(c);
    											}
    										}
    										return newkey.toString();
    									}
    									/**
    									 * (non-Javadoc)
    									 * @see org.apache.commons.configuration.PropertiesConfiguration.PropertiesWriter#writeProperty(java.lang.String, java.lang.Object, boolean)
    									 */
    									@Override
    									public void writeProperty(String key, Object value, boolean forceSingleLine) throws IOException {
    										String v;
    										String k = escapeKey(key);
    										if (BeanUtils.isInstanceOf(value, List.class)) {
    											List<?> values = (List<?>) value;
    											if (forceSingleLine) {
    												v = makeSingleLineValue(values);
    											}
    											else {
    												super.writeProperty(key, values);
    												return;
    											}
    										}
    										else {
    											v = escapeValue(value, false);
    										}

    										write(k);
    										write(fetchSeparator(key, value));
    										v = (v == null ? "" : v.trim());
    										// if encrypting properties
    										if (StringUtils.hasText(PropertyUtils.this.getPassword())) {
    											v = EncryptUtils.encrypt(k, v);
    										}
    										write(v);

    										writeln(null);
    									}
    								};
    							}
    						};
    					}
    				};
    			}
    			else {
    				this.configuration = new PropertiesConfiguration(confPath) {

    					/**
    					 * (non-Javadoc)
    					 * @see org.apache.commons.configuration.AbstractFileConfiguration#load(java.io.InputStream, java.lang.String)
    					 */
    					@Override
    					public void load(InputStream in, String encoding) throws ConfigurationException {
    						if (StringUtils.hasText(propPassword)) {
    							boolean error = true;
    							ByteArrayOutputStream baos = null;
    							ByteArrayInputStream bais = null;
    							try {
    								baos = new ByteArrayOutputStream();
    								EncryptUtils.decryptPBE(propPassword, in, baos);
    								bais = new ByteArrayInputStream(baos.toByteArray());
    								error = false;
    							}
    							catch (Exception e) {
    								e.printStackTrace();
    							}
    							finally {
    							    StreamUtils.closeQuitely(baos);
    							    if (error) StreamUtils.closeQuitely(bais);
    							}
    							// loads decrypted stream
    							if (!error) {
    								super.load(bais, encoding);
    								StreamUtils.closeQuitely(bais);
    								return;
    							}
    						}
    						// loads ad default
    						super.load(in, encoding);
    					}
    				};
    			}
			}
		}
		catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get password that has been used to encrypt property values
	 *
	 * @return
	 */
	private String getPassword() {
		return this.propertyPassword;
	}

	/**
	 * Gets the configuration value
	 *
	 * @return the configuration
	 */
	protected Configuration getConfiguration() {
		return this.configuration;
	}

	/**
	 * Save to file
	 *
	 * @param fileOut file to write
	 *
	 * @return true for successful; else false
	 */
	public boolean store(String fileOut) {
		Configuration config = this.getConfiguration();
		if (config == null) return false;
		try {
			if (StringUtils.hasText(fileOut)) {
				((PropertiesConfiguration) config).save(fileOut);
			} else {
				((PropertiesConfiguration) config).save();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Save to current file
	 *
	 * @return true for successful; else false
	 */
	public boolean store() {
		return this.store(this.propertyFile);
	}

	/**
     * Get a boolean value indicating that the specified property key exists in property file
     * @param key property key
     * @return true for existed; else false
     */
    public boolean hasProperty(String key) {
        Configuration config = this.getConfiguration();
        if (config == null || config.isEmpty()) return false;
        return config.containsKey(key);
    }

	/**
	 * Get property value under string type
	 * @param key property key
	 * @return property value under string type
	 */
	public String getPropertyString(String key) {
		return this.getPropertyString(key, "");
	}
	/**
     * Get property value under string type
     * @param key property key
     * @param defaultvalue default value in fail
     * @return property value under string type
     */
    public String getPropertyString(String key, String defaultvalue) {
		Configuration config = this.getConfiguration();
		if (config == null) return defaultvalue;
		try {
			return config.getString(key, defaultvalue);
		}
		catch (Exception e) {
			return defaultvalue;
		}
	}

    /**
     * Get property value under integer type
     * @param key property key
     * @return property value under integer type
     */
    public int getPropertyInt(String key) {
		return this.getPropertyInt(key, 0);
	}
    /**
     * Get property value under integer type
     * @param key property key
     * @param defaultvalue default value in fail
     * @return property value under integer type
     */
    public int getPropertyInt(String key, int defaultvalue) {
		Configuration config = this.getConfiguration();
		if (config == null) return defaultvalue;
		try {
			return config.getInt(key, defaultvalue);
		}
		catch (Exception e) {
			return defaultvalue;
		}
	}

    /**
     * Get property value under boolean type
     * @param key property key
     * @return property value under boolean type
     */
    public boolean getPropertyBool(String key) {
		return this.getPropertyBool(key, false);
	}
    /**
     * Get property value under boolean type
     * @param key property key
     * @param defaultvalue default value in fail
     * @return property value under boolean type
     */
    public boolean getPropertyBool(String key, boolean defaultvalue) {
		Configuration config = this.getConfiguration();
		if (config == null) return defaultvalue;
		try {
			return config.getBoolean(key, defaultvalue);
		}
		catch (Exception e) {
			return defaultvalue;
		}
	}

    /**
     * Get property values array
     * @param key property key
     * @return property values array or empty
     */
    public String[] getPropertyStringArray(String key) {
		Configuration config = this.getConfiguration();
		if (config == null) return new String[] {};
		try {
			return config.getStringArray(key);
		}
		catch (Exception e) {
			return new String[] {};
		}
	}

	/**
	 * Sets property value by key
	 *
	 * @param key the property key
	 * @param value the property value
	 */
	public void setProperty(String key, Object value) {
		Configuration config = this.getConfiguration();
		if (config == null) return;
		config.setProperty(key, value);
	}

	/**
	 * Get {@link Properties} by the specified key
	 *
	 * @param key the property key. NULL for all
	 *
	 * @return {@link Properties} by the specified key
	 */
	public Properties getProperties(String key) {
	    Properties props = new Properties();
	    Configuration config = this.getConfiguration();
        if (config == null) return props;
	    if (StringUtils.hasText(key)) {
	        try {
	            props = config.getProperties(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	            if (props != null) props.clear();
	        }
	    } else {
	        for(final Iterator<String> it = config.getKeys(); it.hasNext();) {
	            key = it.next();
	            props.put(key, config.getProperty(key));
	        }
	    }
	    return props;
	}
	/**
     * Get {@link Properties} by the specified key
     *
     * @param key the property key. NULL for all
     *
     * @return {@link Properties} by the specified key
     */
    public Map<String, Object> getPropertiesMap(String key) {
        Map<String, Object> props = new LinkedHashMap<String, Object>();
        Configuration config = this.getConfiguration();
        if (config == null) return props;
        if (StringUtils.hasText(key)) {
            try {
                props.put(key, config.getProperties(key));
            } catch (Exception e) {
                e.printStackTrace();
                if (props != null) props.clear();
            }
        } else {
            for(final Iterator<String> it = config.getKeys(); it.hasNext();) {
                key = it.next();
                props.put(key, config.getProperty(key));
            }
        }
        return props;
    }

	/**
	 * Gets the configuration property file
	 *
	 * @param classLoader the class loader
	 * @param name the file name
	 *
	 * @return the configuration property file
	 */
	protected String getConfigurePath(ClassLoader classLoader, String name) {
		classLoader = (classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader);
		URL url = classLoader.getResource(name);
		if (url == null && StringUtils.hasText(name) && !name.toLowerCase().endsWith(DEFAULT_PROPERTIES_EXTENSION)) {
		    url = classLoader.getResource(name + DEFAULT_PROPERTIES_EXTENSION);
		}
		if (url == null && !classLoader.equals(ClassLoader.getSystemClassLoader())) {
	        classLoader = classLoader.getParent();
	        return getConfigurePath(classLoader, name);
		}
		else if (url == null) {
		    // try with logical file
		    if (StringUtils.hasText(name)) {
		        // check file exist
		        File f = new File(name);
		        if (f.exists() && !f.isDirectory()) return f.getPath();

		        // check with default properties extension
		        if (!name.toLowerCase().endsWith(DEFAULT_PROPERTIES_EXTENSION)) {
		            f = new File(name + DEFAULT_PROPERTIES_EXTENSION);
		            if (f.exists() && !f.isDirectory()) return f.getPath();
		        }
		    }
		    return null;
		}

		// If the resource is located inside of a JAR, then EasyConf needs the
		// "jar:file:" prefix appended to the path. Use URL.toExternalForm() to
		// achieve that. When running under JBoss, the protocol returned is
		// "vfszip". When running under OC4J, the protocol returned is
		// "code-source". When running under WebLogic, the protocol returned is
		// "zip". When running under WebSphere, the protocol returned is
		// "wsjar".

		String protocol = url.getProtocol();

		if (protocol.equalsIgnoreCase("code-source")
				|| protocol.equalsIgnoreCase("jar")
				|| protocol.equalsIgnoreCase("vfszip")
				|| protocol.equalsIgnoreCase("wsjar")
				|| protocol.equalsIgnoreCase("zip")) {
			name = url.toExternalForm();
		}
		else {
			try {
				name = new URI(url.getPath()).getPath();
			}
			catch (URISyntaxException urise) {
				name = url.getFile();
			}
		}

		return name;
	}
}
