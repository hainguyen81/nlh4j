/*
 * Copyright (C) 2011 readyState Software Ltd, 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nlh4j.android.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nlh4j.android.util.LogUtils;
import org.nlh4j.android.util.SystemUtils;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.CompressionUtils;
import org.nlh4j.util.FileUtils;
import org.nlh4j.util.StreamUtils;
import org.nlh4j.util.StringUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class to manage database creation and version management using
 * an application's raw asset files.
 *
 * This class provides developers with a simple way to ship their Android app
 * with an existing SQLite database (which may be pre-populated with data) and
 * to manage its initial creation and any upgrades required with subsequent
 * version releases.
 *
 * <p>This class makes it easy for {@link android.content.ContentProvider}
 * implementations to defer opening and upgrading the database until first use,
 * to avoid blocking application startup with long-running database upgrades.
 *
 * <p>For examples see <a href="https://github.com/jgilfelt/android-sqlite-asset-helper">
 * https://github.com/jgilfelt/android-sqlite-asset-helper</a>
 *
 * <p class="note"><strong>Note:</strong> this class assumes
 * monotonically increasing version numbers for upgrades.  Also, there
 * is no concept of a database downgrade; installing a new version of
 * your app which uses a lower version number than a
 * previously-installed version will result in undefined behavior.</p>
 */
public class SqlLiteAssetHelper extends SQLiteOpenHelper implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    private static final String ASSET_DB_PATH = "databases";

    private final Context mContext;
    private final String mName;
    private final CursorFactory mFactory;
    private final int mNewVersion;

    private SQLiteDatabase mDatabase = null;
    private boolean mIsInitializing = false;

    private String mDatabasePath;
    private String mAssetPath;
    private String mUpgradePathFormat;

    private int mForcedUpgradeVersion = 0;

    /**
     * Create a helper object to create, open, and/or manage a database in
     * a specified location.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name of the database file
     * @param storageDirectory to store the database file upon creation; caller must
     *     ensure that the specified absolute path is available and can be written to
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *     SQL file(s) contained within the application assets folder will be used to
     *     upgrade the database
     * @exception IllegalArgumentException thrown if version &lt; 1, or the database file name is null/empty
     */
    public SqlLiteAssetHelper(Context context, String name, String storageDirectory, CursorFactory factory, int version) {
        super(context, name, factory, version);

        if (version < 1) {
            throw new ApplicationRuntimeException(
                    new IllegalArgumentException("Version must be >= 1, was " + version));
        }
        if (!StringUtils.hasText(name)) {
            throw new ApplicationRuntimeException(
                    new IllegalArgumentException("Database name cannot be null"));
        }

        mContext = context;
        mName = name;
        mFactory = factory;
        mNewVersion = version;

        mAssetPath = ASSET_DB_PATH + "/" + name;
        mDatabasePath = (StringUtils.hasText(storageDirectory)
                ? storageDirectory : SystemUtils.getAppDataDir(context) + "/databases");
        mUpgradePathFormat = ASSET_DB_PATH + "/" + name + "_upgrade_%s-%s.sql";
    }

    /**
     * Create a helper object to create, open, and/or manage a database in
     * the application's default private data directory.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name of the database file
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *     SQL file(s) contained within the application assets folder will be used to
     *     upgrade the database
     * @exception IllegalArgumentException thrown if version &lt; 1, or the database file name is null/empty
     */
    public SqlLiteAssetHelper(Context context, String name, CursorFactory factory, int version) {
        this(context, name, null, factory, version);
    }

    /**
     * Create and/or open a database that will be used for reading and writing.
     * The first time this is called, the database will be extracted and copied
     * from the application's assets folder.
     *
     * <p>Once opened successfully, the database is cached, so you can
     * call this method every time you need to write to the database.
     * (Make sure to call {@link #close} when you no longer need the database.)
     * Errors such as bad permissions or a full disk may cause this method
     * to fail, but future attempts may succeed if the problem is fixed.</p>
     *
     * <p class="caution">Database upgrade may take a long time, you
     * should not call this method from the application main thread, including
     * from {@link android.content.ContentProvider#onCreate ContentProvider.onCreate()}.
     *
     * @throws SQLiteException if the database cannot be opened for writing
     * @return a read/write database object valid until {@link #close} is called
     */
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
            return mDatabase;  // The database is already open for business
        }

        if (mIsInitializing) {
            throw new ApplicationRuntimeException(
                    new IllegalStateException("getWritableDatabase called recursively"));
        }

        // If we have a read-only database open, someone could be using it
        // (though they shouldn't), which would cause a lock to be held on
        // the file, and our attempts to open the database read-write would
        // fail waiting for the file lock.  To prevent that, we acquire the
        // lock on the read-only database, which shuts out other users.

        boolean success = false;
        SQLiteDatabase db = null;
        //if (mDatabase != null) mDatabase.lock();
        try {
            mIsInitializing = true;
            //if (mName == null) {
            //    db = SQLiteDatabase.create(null);
            //} else {
            //    db = mContext.openOrCreateDatabase(mName, 0, mFactory);
            //}
            db = createOrOpenDatabase(false);

            int version = db.getVersion();

            // do force upgrade
            if (version != 0 && version < mForcedUpgradeVersion) {
                db = createOrOpenDatabase(true);
                db.setVersion(mNewVersion);
                version = db.getVersion();
            }

            if (version != mNewVersion) {
                db.beginTransaction();
                try {
                    if (version == 0) {
                        onCreate(db);
                    } else {
                        if (version > mNewVersion) {
                            LogUtils.w(
                                    "Can't downgrade read-only database from version " +
                                    version + " to " + mNewVersion + ": " + db.getPath());
                        }
                        onUpgrade(db, version, mNewVersion);
                    }
                    db.setVersion(mNewVersion);
                    db.setTransactionSuccessful();
                } catch(Exception e) {
                    LogUtils.e(e.getMessage(), e);
                } finally {
                    db.endTransaction();
                }
            }

            onOpen(db);
            success = true;
        } catch(Exception e) {
            LogUtils.e(e.getMessage(), e);
        } finally {
            mIsInitializing = false;
            if (success) {
                if (mDatabase != null) {
                    StreamUtils.closeQuitely(mDatabase);
                    //mDatabase.unlock();
                }
                mDatabase = db;
            } else {
                StreamUtils.closeQuitely(db);
                //if (mDatabase != null) mDatabase.unlock();
            }
        }
        return db;
    }

    /**
     * Create and/or open a database.  This will be the same object returned by
     * {@link #getWritableDatabase} unless some problem, such as a full disk,
     * requires the database to be opened read-only.  In that case, a read-only
     * database object will be returned.  If the problem is fixed, a future call
     * to {@link #getWritableDatabase} may succeed, in which case the read-only
     * database object will be closed and the read/write object will be returned
     * in the future.
     *
     * <p class="caution">Like {@link #getWritableDatabase}, this method may
     * take a long time to return, so you should not call it from the
     * application main thread, including from
     * {@link android.content.ContentProvider#onCreate ContentProvider.onCreate()}.
     *
     * @throws SQLiteException if the database cannot be opened
     * @return a database object valid until {@link #getWritableDatabase}
     *     or {@link #close} is called.
     */
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (mDatabase != null && mDatabase.isOpen()) {
            return mDatabase;  // The database is already open for business
        }

        if (mIsInitializing) {
            throw new ApplicationRuntimeException(
                    new IllegalStateException("getReadableDatabase called recursively"));
        }

        try {
            return getWritableDatabase();
        } catch (SQLiteException e) {
            if (mName == null) throw e;  // Can't open a temp database read-only!
            LogUtils.e("Couldn't open " + mName + " for writing (will try read-only):", e);
        }

        SQLiteDatabase db = null;
        try {
            mIsInitializing = true;
            String path = mContext.getDatabasePath(mName).getPath();
            db = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READONLY);
            if (db.getVersion() != mNewVersion) {
                throw new SQLiteException("Can't upgrade read-only database from version " +
                        db.getVersion() + " to " + mNewVersion + ": " + path);
            }

            onOpen(db);
            LogUtils.d("Opened " + mName + " in read-only mode");
            mDatabase = db;
        } catch(Exception e) {
            LogUtils.e(e.getMessage(), e);
        } finally {
            mIsInitializing = false;
            if (db != null && db != mDatabase) {
                StreamUtils.closeQuitely(db);
            }
        }
        return mDatabase;
    }

    /**
     * Close any open database object.
     */
    @Override
    public synchronized void close() {
        if (mIsInitializing) {
            throw new ApplicationRuntimeException(
                    new IllegalStateException("Closed during initialization"));
        }

        if (mDatabase != null && mDatabase.isOpen()) {
            StreamUtils.closeQuitely(mDatabase);
            mDatabase = null;
        }
    }

    /*
     * (Non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onConfigure(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    @Deprecated
    public final void onConfigure(SQLiteDatabase db) {
        // not supported!
    }

    /*
     * (Non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    @Deprecated
    public final void onCreate(SQLiteDatabase db) {
        // do nothing - createOrOpenDatabase() is called in
        // getWritableDatabase() to handle database creation.
    }

    /*
     * (Non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.d("Upgrading database " + mName + " from version " + oldVersion + " to " + newVersion + "...");

        ArrayList<String> paths = new ArrayList<String>();
        getUpgradeFilePaths(oldVersion, newVersion-1, newVersion, paths);

        if (paths.isEmpty()) {
            LogUtils.e("no upgrade script path from " + oldVersion + " to " + newVersion);
            throw new ApplicationRuntimeException(
                    new SQLiteException("no upgrade script path from " + oldVersion + " to " + newVersion));
        }

        Collections.sort(
                paths,
                new Comparator<String>() {

                    /** version pattern comparator */
                    private Pattern pattern = Pattern.compile(".*_upgrade_([0-9]+)-([0-9]+).*");

                    /*
                     * (Non-Javadoc)
                     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
                     */
                    @Override
                    public int compare(String file0, String file1) {
                        Matcher m0 = pattern.matcher(file0);
                        Matcher m1 = pattern.matcher(file1);

                        if (!m0.matches()) {
                            LogUtils.w("could not parse upgrade script file: " + file0);
                            throw new ApplicationRuntimeException(
                                    new SQLiteException("Invalid upgrade script file"));
                        }

                        if (!m1.matches()) {
                            LogUtils.w("could not parse upgrade script file: " + file1);
                            throw new ApplicationRuntimeException(
                                    new SQLiteException("Invalid upgrade script file"));
                        }

                        int v0_from = Integer.valueOf(m0.group(1));
                        int v1_from = Integer.valueOf(m1.group(1));
                        int v0_to = Integer.valueOf(m0.group(2));
                        int v1_to = Integer.valueOf(m1.group(2));

                        if (v0_from == v1_from) {
                            // 'from' versions match for both; check 'to' version next
                            if (v0_to == v1_to) {
                                return 0;
                            }
                            return v0_to < v1_to ? -1 : 1;
                        }
                        return v0_from < v1_from ? -1 : 1;
                    }
                });

        for (String path : paths) {
            try {
                LogUtils.d("processing upgrade: " + path);
                InputStream is = mContext.getAssets().open(path);
                String sql = StringUtils.toString(is);
                if (sql != null) {
                    List<String> cmds = splitSqlScript(sql, ';');
                    for (String cmd : cmds) {
                        //Log.d(TAG, "cmd=" + cmd);
                        if (cmd.trim().length() > 0) {
                            db.execSQL(cmd);
                        }
                    }
                }
            } catch (IOException e) {
                LogUtils.e(e.getMessage(), e);
            }
        }

        LogUtils.d("Successfully upgraded database " + mName + " from version " + oldVersion + " to " + newVersion);

    }
    /**
     * Split the specified string by the specified delimit character
     *
     * @param script the string to split
     * @param delim the delimit character
     *
     * @return the strings list
     */
    private static List<String> splitSqlScript(String script, char delim) {
        List<String> statements = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean inLiteral = false;
        char[] content = script.toCharArray();
        for (int i = 0; i < script.length(); i++) {
            if (content[i] == '"') {
                inLiteral = !inLiteral;
            }
            if (content[i] == delim && !inLiteral) {
                if (sb.length() > 0) {
                    statements.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(content[i]);
            }
        }
        if (sb.length() > 0) {
            statements.add(sb.toString().trim());
        }
        return statements;
    }

    /*
     * (Non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onDowngrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    @Deprecated
    public final void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // not supported!
    }

    /**
     * Bypass the upgrade process (for each increment up to a given version) and simply
     * overwrite the existing database with the supplied asset file.
     *
     * @param version bypass upgrade up to this version number - should never be greater than the
     *                latest database version.
     *
     * @deprecated use {@link #setForcedUpgrade} instead.
     */
    @Deprecated
    public void setForcedUpgradeVersion(int version) {
        setForcedUpgrade(version);
    }

    /**
     * Bypass the upgrade process (for each increment up to a given version) and simply
     * overwrite the existing database with the supplied asset file.
     *
     * @param version bypass upgrade up to this version number - should never be greater than the
     *                latest database version.
     */
    public void setForcedUpgrade(int version) {
        mForcedUpgradeVersion = version;
    }

    /**
     * Bypass the upgrade process for every version increment and simply overwrite the existing
     * database with the supplied asset file.
     */
    public void setForcedUpgrade() {
        setForcedUpgrade(mNewVersion);
    }

    /**
     * Create/Open database
     *
     * @param force specify forcing the creating/opening database
     *
     * @return database that has been created/opened
     * @throws ApplicationRuntimeException thrown if could not create/open database with inner exception {@link SQLiteException}
     */
    private SQLiteDatabase createOrOpenDatabase(boolean force) throws ApplicationRuntimeException {

        // test for the existence of the db file first and don't attempt open
        // to prevent the error trace in log on API 14+
        SQLiteDatabase db = null;
        File file = new File (mDatabasePath + "/" + mName);
        if (file.exists()) {
            db = returnDatabase();
        }

        if (db != null) {
            // database already exists
            if (force) {
                LogUtils.d("forcing database upgrade!");
                copyDatabaseFromAssets();
                db = returnDatabase();
            }
            return db;
        } else {
            // database does not exist, copy it from assets and return it
            copyDatabaseFromAssets();
            db = returnDatabase();
            return db;
        }
    }

    /**
     * Open database
     * @return database that has been opened, or null
     */
    private SQLiteDatabase returnDatabase(){
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(
                    mDatabasePath + "/" + mName, mFactory,
                    SQLiteDatabase.OPEN_READWRITE);
            LogUtils.d("successfully opened database " + mName);
            return db;
        } catch (SQLiteException e) {
            LogUtils.w("could not open database " + mName + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Clone database from assets
     * @throws ApplicationRuntimeException thrown if could not clone database with inner {@link SQLiteException}
     */
    private void copyDatabaseFromAssets() throws ApplicationRuntimeException {
        String path = mAssetPath;
        String dest = mDatabasePath + "/" + mName;
        boolean isZip = false;

        // open database from assets
        InputStream is = null;
        try {
            is = this.mContext.getAssets().open(path);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(
                    new SQLiteException(MessageFormat.format(
                            "Could not open or found {0} database from assets", path)));
        }

        // try uncompressed
        if (FileUtils.ensureDirectory(mDatabasePath)) {
            try {
                isZip = CompressionUtils.decompress(is, mDatabasePath);
            }
            catch (Exception e) {
                LogUtils.w(e.getMessage());
                isZip = false;
            }
            if (!isZip) {
                File fDest = new File(dest);
                FileOutputStream fos = null;

                // debug
                try {
                    // source stream
                    is = this.mContext.getAssets().open(path);
                    // destination stream
                    fos = new FileOutputStream(fDest);
                    // copy stream data
                    StreamUtils.copyStream(is, fos);
                }
                catch (IOException e) {
                    LogUtils.e(e.getMessage(), e);
                    throw new ApplicationRuntimeException(
                            new SQLiteException(MessageFormat.format(
                                    "Could not copy {0} database asset file to {1}", path, dest)));
                } finally {
                    StreamUtils.closeQuitely(is);
                    StreamUtils.closeQuitely(fos);
                }
            } else {
                // close assets stream
                StreamUtils.closeQuitely(is);

                // check destination database file by de-compressing
                File fDest = new File(dest);
                if (!fDest.exists()) {
                    LogUtils.e("--- Missing {0} file (or .zip, .gz archive) in assets, or target folder not writable", new Object[] { mAssetPath });
                    throw new ApplicationRuntimeException(
                            new SQLiteException(MessageFormat.format(
                                    "Missing {0} file (or .zip, .gz archive) in assets, or target folder not writable",
                                    mAssetPath)));
                }
            }
        } else {
            // close assets stream
            StreamUtils.closeQuitely(is);

            // thrown exception
            LogUtils.e("--- Could not create {0} directory to copy from assets", new Object[] { mDatabasePath });
            throw new ApplicationRuntimeException(
                    new SQLiteException(MessageFormat.format(
                            "Could not create {0} directory to copy from assets",
                            mDatabasePath)));
        }
    }

    /**
     * Open the upgrade SQL file
     *
     * @param oldVersion old version
     * @param newVersion new version
     *
     * @return input-stream to the upgraded SQL file or null
     */
    private InputStream getUpgradeSQLStream(int oldVersion, int newVersion) {
        String path = String.format(mUpgradePathFormat, oldVersion, newVersion);
        try {
            return mContext.getAssets().open(path);
        } catch (IOException e) {
            LogUtils.w("missing database upgrade script: " + path);
            return null;
        }
    }

    /**
     * Get the file paths list of all upgraded SQL files
     *
     * @param baseVersion base version
     * @param start start version
     * @param end end version
     * @param paths the returned SQL file paths list
     */
    private void getUpgradeFilePaths(int baseVersion, int start, int end, ArrayList<String> paths) {
        int a;
        int b;
        InputStream is = getUpgradeSQLStream(start, end);
        if (is != null) {
            String path = String.format(mUpgradePathFormat, start, end);
            paths.add(path);
            //Log.d(TAG, "found script: " + path);
            a = start - 1;
            b = start;
            is = null;
        } else {
            a = start - 1;
            b = end;
        }

        if (a >= baseVersion) {
            getUpgradeFilePaths(baseVersion, a, b, paths); // recursive call
        }
    }
}