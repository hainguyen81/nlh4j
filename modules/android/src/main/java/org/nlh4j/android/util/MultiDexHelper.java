/*
 * @(#)MultiDexHelper.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StreamUtils;
import org.springframework.util.Assert;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Multiple DEX utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class MultiDexHelper implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    private static final String OPTIMIZED_FOLDER_NAME = "optimized";
    private static final String WORKING_FOLDER_NAME = "working";
    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final String EXTRACTED_SUFFIX = ".zip";
    //    private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";
    private static final String KEY_DEX_NUMBER = "dex.number";
    private static final String PREFS_FILE = "multidex.version";

    /**
     * Get the DEX source file paths list
     *
     * @param context to parse
     *
     * @return the DEX source file paths list
     * @throws PackageManager.NameNotFoundException thrown if invalid package
     * @throws IllegalArgumentException thrown if invalid package or invalid {@link SharedPreferences}
     * @throws IOException thrown if missing source file path
     */
    public static List<String> getSourcePaths(Context context)
            throws PackageManager.NameNotFoundException, IllegalArgumentException, IOException {
        List<String> sourcePaths = new ArrayList<String>();

        // parse source/data directory
        ApplicationInfo ai = null;
        PackageManager pm = context.getPackageManager();
        ai = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        String srcDir = ai.sourceDir;
        String dataDir = ai.dataDir;
        Assert.hasText(srcDir, "sourceDir");
        Assert.hasText(dataDir, "dataDir");
        LogUtils.d("Application source directory {0}", srcDir);
        LogUtils.d("Application data directory {0}", dataDir);

        // parse DEX preference
        SharedPreferences sp = context.getSharedPreferences(PREFS_FILE,
                !CompatibilityUtils.isHoneycomb() ? Context.MODE_PRIVATE
                        : Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        Assert.notNull(sp, "SharedPreferences");

        // initialize source file paths list
        File sourceApk = new File(srcDir);
        File dexDir = new File(dataDir, "code_cache" + File.separator + "secondary-dexes");
        sourcePaths.add(srcDir); //add the default apk path

        //the prefix of extracted file, ie: test.classes
        String extractedFilePrefix = sourceApk.getName() + EXTRACTED_NAME_EXT;
        //the total dex numbers
        int totalDexNumber = sp.getInt(KEY_DEX_NUMBER, 1);
        LogUtils.d("Found {0} DEX file parts", totalDexNumber);
        for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
            // for each dex file, ie: test.classes2.zip, test.classes3.zip...
            String fileName = extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX;
            File extractedFile = new File(dexDir, fileName);
            LogUtils.d("Adding {0} DEX file", extractedFile.getAbsolutePath());
            if (extractedFile.isFile()) {
                sourcePaths.add(extractedFile.getAbsolutePath());

            } else {
                sourcePaths.clear();
                throw new IOException("Missing extracted secondary dex file '" +
                        extractedFile.getPath() + "'");
            }
        }

        // DEX source file paths
        return sourcePaths;
    }

    /**
     * Get the application class names list DEX parts in "classes.dex", "classes2.dex", ....
     *
     * @param context to parse
     *
     * @return the application class names list DEX parts in "classes.dex", "classes2.dex", ....
     * @throws PackageManager.NameNotFoundException thrown if invalid package
     * @throws IllegalArgumentException thrown if invalid package or invalid {@link SharedPreferences}
     * @throws IOException thrown if missing source file path
     */
    public static List<String> getAllClasses(Context context)
            throws PackageManager.NameNotFoundException, IllegalArgumentException, IOException {
        List<String> classNames = new ArrayList<String>();
        List<String> srcPaths = getSourcePaths(context);
        for (String path : srcPaths) {
            try {
                DexFile dexfile = null;
                LogUtils.d("Loading {0} DEX file ....", new Object[] { path });
                if (path.toLowerCase().endsWith(EXTRACTED_SUFFIX)) {
                    //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                    dexfile = DexFile.loadDex(path, path + ".tmp", 0);

                } else {
                    dexfile = new DexFile(path);
                }
                Enumeration<String> dexEntries = dexfile.entries();
                while (dexEntries.hasMoreElements()) {
                    String className = dexEntries.nextElement();
                    LogUtils.d("Adding {0} class ....", new Object[] { className });
                    classNames.add(className);
                }
            } catch (IOException e) {
                throw new IOException("Error at loading dex file '" + path + "'");
            }
        }
        return classNames;
    }

    /**
     * Load all DEX parts
     *
     * @param context to search DEX parts
     */
    public static void loadAllDexes(Context context) {
        Assert.notNull(context, "context");
        File optimizedDir = context.getDir(OPTIMIZED_FOLDER_NAME, Context.MODE_PRIVATE);
        File workDir = context.getDir(WORKING_FOLDER_NAME, Context.MODE_PRIVATE);
        ClassLoader localClassLoader = context.getClassLoader();
        if (localClassLoader != null
                && (BaseDexClassLoader.class.equals(localClassLoader.getClass())
                        || localClassLoader.getClass().isAssignableFrom(BaseDexClassLoader.class)
                        || localClassLoader instanceof BaseDexClassLoader)) {
            try {
                int i=2;
                BaseDexClassLoader classLoader = (BaseDexClassLoader) localClassLoader;
                Object existing = getDexClassLoaderElements(classLoader);
                DexClassLoader dexClassLoader = loadFromAssets(context, optimizedDir, workDir, i, localClassLoader);
                while (dexClassLoader != null) {
                    Object incoming = getDexClassLoaderElements(dexClassLoader);
                    existing = CollectionUtils.joinArrays(incoming, existing);
                    i++;
                    dexClassLoader = loadFromAssets(context, optimizedDir, workDir, i, localClassLoader);
                }
                setDexClassLoaderElements(classLoader, existing);

            } catch (Exception e) {
                e.printStackTrace();
                //                LogUtils.e(e.getMessage(), e);
            }
        } else {
            throw new UnsupportedOperationException("Class loader not supported");
        }
    }

    /**
     * Load DEX parts from assets
     *
     * @param context to search DEX parts
     * @param optimizedDir optimized directory
     * @param workDir working directory
     * @param dexIndex DEX part number
     * @param localClassLoader DEX class loader
     * @return
     */
    private static DexClassLoader loadFromAssets(
            Context context, File optimizedDir, File workDir,
            int dexIndex, ClassLoader localClassLoader) {
        String fileName = "classes" + dexIndex + ".dex";
        File optimized = new File(optimizedDir, fileName);
        optimized.mkdir();
        File work = new File(workDir, fileName);
        InputStream inputDex = null;
        FileOutputStream outputDex = null;
        try {
            inputDex = context.getAssets().open(fileName);
            outputDex = new FileOutputStream(work);
            byte[] buf = new byte[0x1000];
            while (true) {
                int r = inputDex.read(buf);
                if (r == -1)
                {
                    break;
                }
                outputDex.write(buf, 0, r);
            }

            return new DexClassLoader(
                    work.getAbsolutePath(), optimized.getAbsolutePath(),
                    null, localClassLoader);
        } catch (IOException e) {
            e.printStackTrace();
            //            LogUtils.e(e.getMessage(), e);
            return null;
        } finally {
            StreamUtils.closeQuitely(outputDex);
            StreamUtils.closeQuitely(inputDex);
        }
    }

    /**
     * Set DEX class loader elements
     *
     * @param classLoader DEX class loader
     * @param elements DEX elements
     * @throws Exception thrown if failed
     */
    private static void setDexClassLoaderElements(
            BaseDexClassLoader classLoader, Object elements) throws Exception {
        Class<BaseDexClassLoader> dexClassLoaderClass = BaseDexClassLoader.class;
        Field pathListField = dexClassLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, elements);
    }

    /**
     * Get DEX class loader elements
     *
     * @param classLoader DEX class loader
     *
     * @return DEX class loader elements
     * @throws Exception thrown if failed
     */
    private static Object getDexClassLoaderElements(BaseDexClassLoader classLoader) throws Exception {
        Class<BaseDexClassLoader> dexClassLoaderClass = BaseDexClassLoader.class;
        Field pathListField = dexClassLoaderClass.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }
}
