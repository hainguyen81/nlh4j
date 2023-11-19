/*
 * @(#)TestConvertProps.java 1.0 Dec 27, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

/**
 * Read property files from folder and write it with key as value
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TestConvertProps {

    /**
     * Read properties
     *
     * @return properties
     */
    private Properties readProps(String file) {
        Properties props = null;
        try {
            props = new Properties();
            props.load(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            props = null;
        }
        return props;
    }

    /**
     * Write key as value
     *
     * @param props properties
     *
     * @return new properties with key as value
     */
    private Properties keyAsValue(Properties props) {
        Properties newProps = null;
        try {
            newProps = new Properties();
            for(final Iterator<Object> it = props.keySet().iterator(); it.hasNext();) {
                Object key = it.next();
                if (StringUtils.hasText(props.get(key))) {
                    newProps.put(key, key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            newProps = null;
        }
        return newProps;
    }

    /**
     * Write properties to file
     *
     * @param props properties
     * @param file file
     * @param comments comments
     *
     * @return true for successful; else failed
     */
    private boolean writeProps(Properties props, String file, String comments) {
        boolean ok = false;
        try {
            props.store(new FileOutputStream(file), comments);
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }

    /**
     * Get the file paths list from folder
     *
     * @param folder folder
     * @param filter filter
     *
     * @return file paths list
     */
    private List<String> listFiles(String folder, final String filter) {
        List<String> files = new LinkedList<String>();
        try {
            File dir = new File(folder);
            File[] lstFiles = dir.listFiles(new FilenameFilter() {

                /*
                 * (Non-Javadoc)
                 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
                 */
                @Override
                public boolean accept(File dir, String name) {
                    return (!StringUtils.hasText(filter) || name.matches(filter));
                }
            });
            if (!CollectionUtils.isEmpty(lstFiles)) {
                for(File file : lstFiles) {
                    files.add(file.getPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            files.clear();
        }
        return files;
    }

    /**
     * Read property file from folder and write it again with key as value into same folder
     *
     * @param folder folder
     */
    private void doIt(String folder) {
        try {
            List<String> files = listFiles(folder, "jp.*.properties");
            for(String file : files) {
                String fileName = FileUtils.getBaseFileName(file);
                String newFileName = fileName.replace("jp.", "");
                String newFile = file.replace(fileName, newFileName);
                Properties props = readProps(file);
                props = keyAsValue(props);
                writeProps(props, newFile, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read Pleiades Eclipse property file from folder and write it again with key as value into same folder
     */
    @Test
    public void convertPleiades() {
        doIt("F:\\Java.Working\\1.tools\\eclipse\\pleiades-eclipse-neon-4.6.2-x64\\eclipse\\dropins\\MergeDoc\\eclipse\\plugins\\jp.sourceforge.mergedoc.pleiades\\conf");
    }

    /**
     * Main
     * @param args arguments
     */
    public static void main(String[] args) {
        TestConvertProps testTool = new TestConvertProps();
        testTool.convertPleiades();
    }
}
