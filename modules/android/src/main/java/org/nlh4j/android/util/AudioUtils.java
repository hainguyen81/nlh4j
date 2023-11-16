/*
 * @(#)AudioUtils.java 1.0 Nov 12, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import org.nlh4j.android.entities.AudioEntity;
import org.nlh4j.android.enums.AudioEncoderType;
import org.nlh4j.android.enums.AudioModeType;
import org.nlh4j.android.enums.AudioOutputFormat;
import org.nlh4j.android.enums.AudioSourceType;
import org.nlh4j.android.enums.RingerModeType;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.NumberUtils;
import org.nlh4j.util.StringUtils;

/**
 * {@link Audio} utilities
 *
 * @author Hai Nguyen
 *
 */
public final class AudioUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /** {@link MediaRecorder} */
    private static MediaRecorder mRecorder;
    private static String recordedFile;
    /**
     * Get the media recorder
     *
     * @return the media recorder
     */
    private static MediaRecorder getRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        synchronized (mRecorder) {
            return mRecorder;
        }
    }

    /**
     * Start recording audio
     *
     * @param context {@link Context}
     * @param fileName output file name
     * @param type audio source type
     * @param format audio format type
     * @param encoder audio encoder type
     *
     * @return true for successful; else false
     */
    public static boolean record(Context context, String fileName, AudioSourceType type, AudioOutputFormat format, AudioEncoderType encoder) {
        boolean ok = false;
        if (context != null && StringUtils.hasText(fileName)) {
            type = (type == null || AudioSourceType.UNKNOWN.equals(type) ? AudioSourceType.MIC : type);
            format = (format == null || AudioOutputFormat.UNKNOWN.equals(format) ? AudioOutputFormat.MPEG_4 : format);
            encoder = (encoder == null || AudioEncoderType.UNKNOWN.equals(encoder) ? AudioEncoderType.AAC : encoder);
            try {
                // prepare storage path
                File storage = FileUtils.getStorageDirectory(context, "application-utils/audio");
                String storagePath = storage.getAbsolutePath();
                if (StringUtils.hasText(storagePath) && !storagePath.endsWith("/")) storagePath += "/";
                storagePath += fileName;

                // start recording
                MediaRecorder recorder = getRecorder();
                recorder.setAudioSource(type.getValue());
                recorder.setOutputFormat(format.getValue());
                recorder.setOutputFile(storagePath);
                recorder.setAudioEncoder(encoder.getValue());
                recorder.prepare();
                recorder.start();

                // cache storage path to save
                recordedFile = storagePath;
                ok = true;
            } catch (Exception e) {
                LogUtils.e(e.getMessage(), e);
                throw new ApplicationRuntimeException(e);
            }
        }
        return ok;
    }
    /**
     * Start recording audio from {@link AudioSourceType#MIC}, {@link AudioOutputFormat#MPEG_4} and {@link AudioEncoderType#AAC}
     *
     * @param context {@link Context}
     * @param fileName output file name
     *
     * @return true for successful; else false
     */
    public static boolean record(Context context, String fileName) {
        return record(context, fileName, AudioSourceType.MIC, AudioOutputFormat.MPEG_4, AudioEncoderType.AAC);
    }

    /**
     * Stop recording and save autio file
     *
     * @param context {@link Context}
     *
     * @return the saved audio {@link Uri} or NULL if failed
     */
    public static Uri stopRecording(Context context) {
        try {
            MediaRecorder recorder = getRecorder();
            recorder.stop();
            recorder.reset();    // set state to idle
            recorder.release();
            recorder = null;
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
        return saveAudio(context, recordedFile);
    }

    /**
     * Save the specified recorded file
     *
     * @param context {@link Context}
     * @param recordedFile the recorded file path
     *
     * @return the media content {@link Uri} to the newly created audio, or null if failed for any reason.
     */
    private static Uri saveAudio(Context context, String recordedFile) {
        File audioFile = (StringUtils.hasText(recordedFile) ? new File(recordedFile) : null);
        Uri uri = null;
        if (context != null && audioFile != null && audioFile.exists()) {
            try {
                // prepare content
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, audioFile.getAbsolutePath());
                values.put(MediaStore.MediaColumns.TITLE, "");
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
                values.put(MediaStore.MediaColumns.SIZE, audioFile.length());
                values.put(MediaStore.Audio.Media.ARTIST, "");
                values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                // Now set some extra features it depend on you
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                values.put(MediaStore.Audio.Media.IS_ALARM, false);
                values.put(MediaStore.Audio.Media.IS_MUSIC, false);

                // save meta-data
                Uri ctxUri = MediaStore.Audio.Media.getContentUriForPath(audioFile.getAbsolutePath());
                uri = context.getContentResolver().insert(ctxUri, values);
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                uri = null;
            }
        }
        return uri;
    }

    /**
     * Remove audio file that is existing on the device.
     *
     * @param context {@link Context}
     * @param uri {@link Uri} of the audio file
     *
     * @return true if successful; else false
     */
    public static boolean removeAudio(Context context, Uri uri) {
        boolean ok = false;
        if (context != null && uri != null) {
            try {
                ok = (context.getContentResolver().delete(uri, null, null) != 0);
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                ok = false;
            }
        }
        return ok;
    }

    /**
     * Get {@link Audio} list
     *
     * @param context {@link Context}
     * @param alarm required alarm {@link Audio}
     * @param music required music {@link Audio}
     * @param notification required notification {@link Audio}
     * @param podcast required podcast {@link Audio}
     * @param ringtone required ringtone {@link Audio}
     *
     * @return {@link Audio} list or empty if failed
     */
    public static List<AudioEntity> getAudioList(
            Context context, boolean alarm, boolean music,
            boolean notification, boolean podcast, boolean ringtone) {
        List<AudioEntity> audioLst = new LinkedList<AudioEntity>();
        Uri uri = null;
        if (context != null) {
            try {
                // require content resolver
                ContentResolver cr = context.getContentResolver();
                if (cr != null) {
                    // prepare
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    String[] columns = {
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.DISPLAY_NAME,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.ALBUM_ID,
                            MediaStore.Audio.Media.ALBUM,
                            MediaStore.Audio.Media.ARTIST_ID,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.DURATION,
                            MediaStore.Audio.Media.BOOKMARK,
                            MediaStore.Audio.Media.COMPOSER,
                            MediaStore.Audio.Media.TRACK,
                            MediaStore.Audio.Media.YEAR,
                            MediaStore.Audio.Media.DATA
                    };
                    StringBuffer conds = new StringBuffer();
                    conds.append(MessageFormat.format(
                            "{0} = {1,number}", MediaStore.Audio.Media.IS_ALARM, (alarm ? 1 : 0)));
                    conds.append(" AND ");
                    conds.append(MessageFormat.format(
                            "{0} = {1,number}", MediaStore.Audio.Media.IS_MUSIC, (music ? 1 : 0)));
                    conds.append(" AND ");
                    conds.append(MessageFormat.format(
                            "{0} = {1,number}", MediaStore.Audio.Media.IS_NOTIFICATION, (notification ? 1 : 0)));
                    conds.append(" AND ");
                    conds.append(MessageFormat.format(
                            "{0} = {1,number}", MediaStore.Audio.Media.IS_PODCAST, (podcast ? 1 : 0)));
                    conds.append(" AND ");
                    conds.append(MessageFormat.format(
                            "{0} = {1,number}", MediaStore.Audio.Media.IS_RINGTONE, (ringtone ? 1 : 0)));
                    // query
                    Cursor c = cr.query(
                            uri, columns, conds.toString(), null,
                            MediaStore.Audio.Media.TITLE + " ASC");
                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        AudioEntity audio = new AudioEntity();
                        audio.setId(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                        audio.setDisplayName(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                        audio.setTitle(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                        audio.setAlbumId(NumberUtils.toLong(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));
                        audio.setAlbum(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                        audio.setArtistId(NumberUtils.toLong(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))));
                        audio.setArtist(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                        audio.setDuration(NumberUtils.toLong(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))));
                        audio.setBookmark(NumberUtils.toLong(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.BOOKMARK))));
                        audio.setComposer(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER)));
                        audio.setTrack(NumberUtils.toLong(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK))));
                        audio.setYear(NumberUtils.toInt(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR))));
                        audio.setPath(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                        audioLst.add(audio);
                    }
                }
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                audioLst.clear();
            }
        }
        return audioLst;
    }

    /**
     * Get a boolean value indicating that the speaker is on/off
     *
     * @param context {@link Context}
     *
     * @return true for on; else false
     */
    public static boolean isSpeakerOn(Context context) {
        AudioManager am = ServiceUtils.getAudioManager(context);
        if (am != null) {
            try {
                return am.isSpeakerphoneOn();
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return false;
    }
    /**
     * Turn on/of speaker. Need {@link permission#MODIFY_AUDIO_SETTINGS}
     *
     * @param context {@link Context}
     * @param onOff true for on; else false
     *
     * @return true for successful; else false
     */
    public static boolean turnSpeaker(Context context, boolean onOff) {
        AudioManager am = ServiceUtils.getAudioManager(context);
        if (am != null) {
            try {
                am.setSpeakerphoneOn(onOff);
                return true;
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return false;
    }

    /**
     * Get a boolean value indicating that the music is active
     *
     * @param context {@link Context}
     *
     * @return true for active; else false
     */
    public static boolean isMusicActive(Context context) {
        AudioManager am = ServiceUtils.getAudioManager(context);
        if (am != null) {
            try {
                return am.isMusicActive();
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return false;
    }

    /**
     * Get a boolean value indicating that the microphone is mute
     *
     * @param context {@link Context}
     *
     * @return true for mute; else false
     */
    public static boolean isMicrophoneMute(Context context) {
        AudioManager am = ServiceUtils.getAudioManager(context);
        if (am != null) {
            try {
                return am.isMicrophoneMute();
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return false;
    }
    /**
     * Turn on/of microphone
     *
     * @param context {@link Context}
     * @param onOff true for mute; else false
     *
     * @return true for successful; else false
     */
    public static boolean turnMicrophone(Context context, boolean onOff) {
        AudioManager am = ServiceUtils.getAudioManager(context);
        if (am != null) {
            try {
                am.setMicrophoneMute(onOff);
                return true;
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return false;
    }

    /**
     * Get the ringer mode
     *
     * @param context {@link Context}
     *
     * @return the ringer mode
     */
    public static RingerModeType getRingerMode(Context context) {
        AudioManager am = ServiceUtils.getAudioManager(context);
        if (am != null) {
            try {
                return RingerModeType.valueOf(am.getRingerMode());
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return RingerModeType.UNKNOWN;
    }
    /**
     * Set the ringer mode
     *
     * @param context {@link Context}
     * @param mode ringer mode
     *
     * @return true for successful; else false
     */
    public static boolean setRingerMode(Context context, RingerModeType mode) {
        if (!RingerModeType.UNKNOWN.equals(mode)) {
            AudioManager am = ServiceUtils.getAudioManager(context);
            if (am != null) {
                try {
                    am.setRingerMode(mode.getValue());
                    return true;
                } catch (Exception e) {
                    LogUtils.w(e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * Get the audio mode
     *
     * @param context {@link Context}
     *
     * @return the audio mode
     */
    public static AudioModeType getAudioMode(Context context) {
        AudioManager am = ServiceUtils.getAudioManager(context);
        if (am != null) {
            try {
                return AudioModeType.valueOf(am.getMode());
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return AudioModeType.UNKNOWN;
    }
    /**
     * Set the audio mode
     *
     * @param context {@link Context}
     * @param mode audio mode
     *
     * @return true for successful; else false
     */
    public static boolean setAudioMode(Context context, AudioModeType mode) {
        if (!AudioModeType.UNKNOWN.equals(mode)) {
            AudioManager am = ServiceUtils.getAudioManager(context);
            if (am != null) {
                try {
                    am.setMode(mode.getValue());
                    return true;
                } catch (Exception e) {
                    LogUtils.w(e.getMessage());
                }
            }
        }
        return false;
    }
}
