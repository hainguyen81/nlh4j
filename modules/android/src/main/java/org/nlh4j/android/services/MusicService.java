/*
 * @(#)MusicService.java 1.0 Nov 15, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.services;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import org.nlh4j.android.entities.AudioEntity;
import org.nlh4j.android.receivers.AbstractBroadcastReceiver;
import org.nlh4j.android.util.AudioUtils;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.util.CollectionUtils;

/**
 * Music player service
 *
 * @author Hai Nguyen
 *
 */
public class MusicService extends AbstractService {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Seek/Backward/Forward Play receiver
     */
    protected class MusicSeekReceiver extends AbstractBroadcastReceiver {

        /**
         * default serial version id
         */
        private static final long serialVersionUID = 1L;

        /* (Non-Javadoc)
         * @see org.nlh4j.android.receivers.AbstractBroadcastReceiver#receive(android.content.Context, android.content.Intent)
         */
        @Override
        protected void receive(Context context, Intent intent) {
            boolean backward = INTENT_PLAY_BACKWARD_KEY.equalsIgnoreCase(intent.getAction());
            int seek = intent.getIntExtra(backward ? INTENT_PLAY_BACKWARD_KEY : INTENT_PLAY_FORWARD_KEY, -1);
            // auto backward/forward
            if (seek < 0) {
                if (backward) {
                    MusicService.this.backward();
                } else {
                    MusicService.this.forward();
                }

                // just seek to bookmark
            } else {
                MusicService.this.seek(seek);
            }
        }

        /* (Non-Javadoc)
         * @see org.nlh4j.android.receivers.AbstractBroadcastReceiver#validIntent(android.content.Intent)
         */
        @Override
        protected boolean validIntent(Intent intent) {
            return (intent != null
                    && (INTENT_PLAY_BACKWARD_KEY.equalsIgnoreCase(intent.getAction())
                            || INTENT_PLAY_FORWARD_KEY.equalsIgnoreCase(intent.getAction())));
        }
    }

    /**
     * Next/Previous/Specified Music Play receiver
     */
    protected class MusicPlayReceiver extends AbstractBroadcastReceiver {

        /**
         * default serial version id
         */
        private static final long serialVersionUID = 1L;

        /* (Non-Javadoc)
         * @see org.nlh4j.android.receivers.AbstractBroadcastReceiver#receive(android.content.Context, android.content.Intent)
         */
        @Override
        protected void receive(Context context, Intent intent) {
            boolean next = INTENT_PLAY_NEXT_KEY.equalsIgnoreCase(intent.getAction());
            int song = intent.getIntExtra(next ? INTENT_PLAY_NEXT_KEY : INTENT_PLAY_PREVIOUS_KEY, -1);
            // auto backward/forward
            if (song >= 0) {
                MusicService.this.play(Boolean.FALSE, song);
            }
        }

        /* (Non-Javadoc)
         * @see org.nlh4j.android.receivers.AbstractBroadcastReceiver#validIntent(android.content.Intent)
         */
        @Override
        protected boolean validIntent(Intent intent) {
            return (intent != null
                    && (INTENT_PLAY_NEXT_KEY.equalsIgnoreCase(intent.getAction())
                            || INTENT_PLAY_PREVIOUS_KEY.equalsIgnoreCase(intent.getAction())));
        }
    }

    /** Action intent key to receive audio position in audio list */
    public static final String INTENT_PLAY_POSITION_KEY = MusicService.class.getName() + ".action.PLAY_POSITION";
    /** Action intent key to send message about playing audio time in milliseconds from {@link Handler} */
    public static final String INTENT_PLAY_TIME_KEY = MusicService.class.getName() + ".action.PLAY_TIME";
    /** Action intent key to receive message about playing audio time backward (in milliseconds) from {@link MusicSeekReceiver} */
    public static final String INTENT_PLAY_BACKWARD_KEY = MusicService.class.getName() + ".action.PLAY_BACKWARD";
    /** Action intent key to receive message about playing audio time forward (in milliseconds) from {@link MusicSeekReceiver} */
    public static final String INTENT_PLAY_FORWARD_KEY = MusicService.class.getName() + ".action.PLAY_FORWARD";
    /** Action intent key to receive message about playing next audio time from {@link MusicPlayReceiver} */
    public static final String INTENT_PLAY_NEXT_KEY = MusicService.class.getName() + ".action.PLAY_NEXT";
    /** Action intent key to receive message about playing previous audio time from {@link MusicPlayReceiver} */
    public static final String INTENT_PLAY_PREVIOUS_KEY = MusicService.class.getName() + ".action.PLAY_PREVIOUS";
    /** Delay time in milliseconds to send audio duration */
    private static final long HANDLER_DELAY_TIME = 200;
    /** Backward/Forward time in milliseconds to seek audio */
    private static final int BACKWARD_FORWARD_TIME = 5000;
    /** {@link MediaPlayer} */
    private MediaPlayer mediaPlayer;
    /** {@link AudioEntity} list */
    private List<AudioEntity> audioLst;
    /** play audio at position */
    private int songPosition = 0;
    /** {@link Handler} to send message */
    private Handler handler;
    /** {@link Thread} to send message */
    private Runnable runnable;

    /**
     * Initialize a new instance of {@link MusicService}
     *
     * @param context {@link Context}
     */
    public MusicService(Context context) {
        super(context);
    }

    /**
     * Get {@link MediaPlayer}
     *
     * @return {@link MediaPlayer}
     */
    protected final MediaPlayer getPlayer() {
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
        }
        synchronized (this.mediaPlayer) {
            return this.mediaPlayer;
        }
    }

    /**
     * Get {@link Handler} to send message
     *
     * @return {@link Handler}
     */
    private Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        synchronized (this.handler) {
            return this.handler;
        }
    }
    /**
     * Get the delayed time while sending message.<br>
     * Default is <b>{@link MusicService#HANDLER_DELAY_TIME}</b>
     * TODO Children class maybe override this method for customizing delayed time
     *
     * @return the delayed time while sending message
     */
    protected long getDelayedTime() {
        return HANDLER_DELAY_TIME;
    }
    /**
     * Send message using {@link Runnable} with delayed time
     */
    protected final void sendPlayTime() {
        if (this.runnable == null) {
            this.runnable = new Runnable() {

                /*
                 * (Non-Javadoc)
                 * @see java.lang.Runnable#run()
                 */
                @Override
                public void run() {
                    long delayed = Math.max(MusicService.this.getDelayedTime(), 0);
                    Handler handler = MusicService.this.getHandler();
                    MediaPlayer player = MusicService.this.getPlayer();
                    if (player != null && handler != null && player.isPlaying()) {
                        try {
                            // create intent
                            Intent intent = new Intent(MusicService.INTENT_PLAY_TIME_KEY);
                            intent.putExtra(MusicService.INTENT_PLAY_TIME_KEY, player.getCurrentPosition());
                            // send broadcast
                            MusicService.this.sendBroadcast(intent);
                            // post message
                            handler.postDelayed(this, delayed);
                        } catch (Exception e) {
                            LogUtils.w(e.getMessage());
                        }

                        // invalid handler
                    } else if (handler == null) {
                        LogUtils.w("Could not create handler to send message!");

                        // invalid player
                    } else if (player == null) {
                        LogUtils.w("Could not create player!");

                        // player has been stopped
                    } else if (player != null && !player.isPlaying()) {
                        LogUtils.w("Player has been stopped!");
                    }
                }
            };
        }
        // post message
        Handler handler = this.getHandler();
        if (handler != null) {
            long delayed = Math.max(MusicService.this.getDelayedTime(), 0);
            handler.postDelayed(this.runnable, delayed);

            // invalid handler
        } else if (handler == null) {
            LogUtils.w("Could not create handler to send message!");
        }
    }

    /**
     * Get a boolean value indicating that should loading alarm audio.<br>
     * Default is <b>false</b><br>
     * TODO Children class maybe override this method for customizing audio list
     *
     * @return true for loading; else false
     */
    protected boolean requireAlarm() {
        return Boolean.FALSE;
    }
    /**
     * Get a boolean value indicating that should loading music audio.<br>
     * Default is <b>true</b><br>
     * TODO Children class maybe override this method for customizing audio list
     *
     * @return true for loading; else false
     */
    protected boolean requireMusic() {
        return Boolean.TRUE;
    }
    /**
     * Get a boolean value indicating that should loading notification audio.<br>
     * Default is <b>false</b><br>
     * TODO Children class maybe override this method for customizing audio list
     *
     * @return true for loading; else false
     */
    protected boolean requireNotification() {
        return Boolean.FALSE;
    }
    /**
     * Get a boolean value indicating that should loading podcast audio.<br>
     * Default is <b>false</b><br>
     * TODO Children class maybe override this method for customizing audio list
     *
     * @return true for loading; else false
     */
    protected boolean requirePodcast() {
        return Boolean.FALSE;
    }
    /**
     * Get a boolean value indicating that should loading ringtone audio.<br>
     * Default is <b>false</b><br>
     * TODO Children class maybe override this method for customizing audio list
     *
     * @return true for loading; else false
     */
    protected boolean requireRingtone() {
        return Boolean.FALSE;
    }
    /**
     * Get the backward/forward time while seeking audio in milliseconds.<br>
     * Default is <b>{@link MusicService#BACKWARD_FORWARD_TIME}</b>
     * TODO Children class maybe override this method for customizing time
     *
     * @return the backward/forward time while seeking audio in milliseconds
     */
    protected int getBackForwardTime() {
        return BACKWARD_FORWARD_TIME;
    }
    /**
     * Get {@link AudioEntity} list
     *
     * @return {@link AudioEntity} list
     */
    protected final List<AudioEntity> getAudioList() {
        if (this.audioLst == null) {
            this.audioLst = new LinkedList<AudioEntity>();
        }
        return Collections.unmodifiableList(this.audioLst);
    }
    /**
     * Reload audio list
     */
    protected final void reloadAudio() {
        // clear old list
        if (this.audioLst != null) this.audioLst.clear();
        // reload audio list
        this.audioLst = AudioUtils.getAudioList(
                super.getContext(),
                this.requireAlarm(),
                this.requireMusic(),
                this.requireNotification(),
                this.requirePodcast(),
                this.requireRingtone());
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.android.services.AbstractService#doCreate()
     */
    @Override
    protected void doCreate() {
        // register receivers
        super.registerReceiver(new MusicSeekReceiver(), new IntentFilter(INTENT_PLAY_BACKWARD_KEY));
        super.registerReceiver(new MusicSeekReceiver(), new IntentFilter(INTENT_PLAY_FORWARD_KEY));
        super.registerReceiver(new MusicPlayReceiver(), new IntentFilter(INTENT_PLAY_NEXT_KEY));
        super.registerReceiver(new MusicPlayReceiver(), new IntentFilter(INTENT_PLAY_PREVIOUS_KEY));
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.android.services.AbstractService#doStartCommand(android.content.Intent, int, int)
     */
    @Override
    protected int doStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            // require play position
            int position = intent.getIntExtra(INTENT_PLAY_POSITION_KEY, 0);
            // play audio
            this.play(Boolean.TRUE, position);
        }
        return START_NOT_STICKY;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.android.services.AbstractService#doDestroy()
     */
    @Override
    protected void doDestroy() {
        this.stop();
    }

    /**
     * Stop playing music
     */
    protected final void stop() {
        // try to close player
        MediaPlayer player;
        try {
            player = this.getPlayer();
            if (player.isPlaying()) player.stop();
            player.release();
            this.mediaPlayer = null;
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
    }

    /**
     * Play audio
     *
     * @param reloadList specify reloading audio list
     * @param audioIdx need to play audio index from list
     */
    private void play(boolean reloadList, int audioIdx) {
        // reload list if necessary
        if (reloadList) this.reloadAudio();
        this.songPosition = audioIdx;
        if (!CollectionUtils.isEmpty(this.audioLst)
                && 0 <= this.songPosition && this.songPosition < this.audioLst.size()) {
            // need to stop
            this.stop();

            // start new audio
            MediaPlayer player;
            try {
                player = this.getPlayer();
                player.setDataSource(this.audioLst.get(this.songPosition).getPath());
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.prepare();
                player.start();
                // send play time
                this.sendPlayTime();
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }

            // empty list
        } else if (CollectionUtils.isEmpty(this.audioLst)) {
            LogUtils.w("Not found any audio file!");

            // index out of range
        } else {
            LogUtils.w("Invalid selected song! It maybe deleted from device!");
        }
    }
    /**
     * Play the specified audio index
     *
     * @param song the audio index
     */
    protected final void play(int song) {
        this.play(Boolean.FALSE, song);
    }

    /**
     * Seek audio to the specified bookmark in milliseconds
     *
     * @param bookmark in milliseconds to seek
     *
     * @return true for successful; else false
     */
    protected final boolean seek(int bookmark) {
        MediaPlayer player = this.getPlayer();
        boolean ok = false;
        if (player != null) {
            try {
                bookmark = Math.min(Math.max(bookmark, 0), player.getCurrentPosition());
                if (bookmark != player.getCurrentPosition()) {
                    player.seekTo(bookmark);
                    ok = true;
                }
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return ok;
    }

    /**
     * Backward audio
     *
     * @return true for successful; else false
     */
    protected final boolean backward() {
        MediaPlayer player = this.getPlayer();
        boolean ok = false;
        if (player != null) {
            int move = this.getBackForwardTime();
            move = Math.max(move, 0);
            ok = this.seek(player.getCurrentPosition() - move);
        }
        return ok;
    }
    /**
     * Forward audio
     *
     * @return true for successful; else false
     */
    protected final boolean forward() {
        MediaPlayer player = this.getPlayer();
        boolean ok = false;
        if (player != null) {
            int move = this.getBackForwardTime();
            move = Math.max(move, 0);
            ok = this.seek(player.getCurrentPosition() + move);
        }
        return ok;
    }
}
