/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.media.AudioManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.KeyEvent
 *  android.view.KeyEvent$Callback
 *  android.view.View
 *  android.view.Window
 */
package android.support.v4.media;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.media.TransportController;
import android.support.v4.media.TransportMediatorCallback;
import android.support.v4.media.TransportMediatorJellybeanMR2;
import android.support.v4.media.TransportPerformer;
import android.support.v4.media.TransportStateListener;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import java.util.ArrayList;

public class TransportMediator
extends TransportController {
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
    public static final int FLAG_KEY_MEDIA_NEXT = 128;
    public static final int FLAG_KEY_MEDIA_PAUSE = 16;
    public static final int FLAG_KEY_MEDIA_PLAY = 4;
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
    public static final int FLAG_KEY_MEDIA_REWIND = 2;
    public static final int FLAG_KEY_MEDIA_STOP = 32;
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MEDIA_RECORD = 130;
    final AudioManager mAudioManager;
    final TransportPerformer mCallbacks;
    final Context mContext;
    final TransportMediatorJellybeanMR2 mController;
    final Object mDispatcherState;
    final KeyEvent.Callback mKeyEventCallback;
    final ArrayList<TransportStateListener> mListeners = new ArrayList();
    final TransportMediatorCallback mTransportKeyCallback;
    final View mView;

    public TransportMediator(Activity activity, TransportPerformer transportPerformer) {
        this(activity, null, transportPerformer);
    }

    private TransportMediator(Activity activity, View view, TransportPerformer transportPerformer) {
        this.mTransportKeyCallback = new TransportMediatorCallback(){

            @Override
            public long getPlaybackPosition() {
                return TransportMediator.this.mCallbacks.onGetCurrentPosition();
            }

            @Override
            public void handleAudioFocusChange(int n) {
                TransportMediator.this.mCallbacks.onAudioFocusChange(n);
            }

            @Override
            public void handleKey(KeyEvent keyEvent) {
                keyEvent.dispatch(TransportMediator.this.mKeyEventCallback);
            }

            @Override
            public void playbackPositionUpdate(long l) {
                TransportMediator.this.mCallbacks.onSeekTo(l);
            }
        };
        this.mKeyEventCallback = new KeyEvent.Callback(){

            public boolean onKeyDown(int n, KeyEvent keyEvent) {
                if (TransportMediator.isMediaKey(n)) {
                    return TransportMediator.this.mCallbacks.onMediaButtonDown(n, keyEvent);
                }
                return false;
            }

            public boolean onKeyLongPress(int n, KeyEvent keyEvent) {
                return false;
            }

            public boolean onKeyMultiple(int n, int n2, KeyEvent keyEvent) {
                return false;
            }

            public boolean onKeyUp(int n, KeyEvent keyEvent) {
                if (TransportMediator.isMediaKey(n)) {
                    return TransportMediator.this.mCallbacks.onMediaButtonUp(n, keyEvent);
                }
                return false;
            }
        };
        Activity activity2 = activity != null ? activity : view.getContext();
        this.mContext = activity2;
        this.mCallbacks = transportPerformer;
        this.mAudioManager = (AudioManager)this.mContext.getSystemService("audio");
        if (activity != null) {
            view = activity.getWindow().getDecorView();
        }
        this.mView = view;
        this.mDispatcherState = KeyEventCompat.getKeyDispatcherState(this.mView);
        if (Build.VERSION.SDK_INT >= 18) {
            this.mController = new TransportMediatorJellybeanMR2(this.mContext, this.mAudioManager, this.mView, this.mTransportKeyCallback);
            return;
        }
        this.mController = null;
    }

    public TransportMediator(View view, TransportPerformer transportPerformer) {
        this(null, view, transportPerformer);
    }

    private TransportStateListener[] getListeners() {
        if (this.mListeners.size() <= 0) {
            return null;
        }
        TransportStateListener[] arrtransportStateListener = new TransportStateListener[this.mListeners.size()];
        this.mListeners.toArray(arrtransportStateListener);
        return arrtransportStateListener;
    }

    static boolean isMediaKey(int n) {
        if (n != 79 && n != 130) {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            return false;
                        }
                        case 126: 
                        case 127: 
                    }
                }
                case 85: 
                case 86: 
                case 87: 
                case 88: 
                case 89: 
                case 90: 
                case 91: 
            }
        }
        return true;
    }

    private void pushControllerState() {
        if (this.mController != null) {
            this.mController.refreshState(this.mCallbacks.onIsPlaying(), this.mCallbacks.onGetCurrentPosition(), this.mCallbacks.onGetTransportControlFlags());
        }
    }

    private void reportPlayingChanged() {
        TransportStateListener[] arrtransportStateListener = this.getListeners();
        if (arrtransportStateListener != null) {
            int n = arrtransportStateListener.length;
            for (int i = 0; i < n; ++i) {
                arrtransportStateListener[i].onPlayingChanged(this);
            }
        }
    }

    private void reportTransportControlsChanged() {
        TransportStateListener[] arrtransportStateListener = this.getListeners();
        if (arrtransportStateListener != null) {
            int n = arrtransportStateListener.length;
            for (int i = 0; i < n; ++i) {
                arrtransportStateListener[i].onTransportControlsChanged(this);
            }
        }
    }

    public void destroy() {
        this.mController.destroy();
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return KeyEventCompat.dispatch(keyEvent, this.mKeyEventCallback, this.mDispatcherState, this);
    }

    @Override
    public int getBufferPercentage() {
        return this.mCallbacks.onGetBufferPercentage();
    }

    @Override
    public long getCurrentPosition() {
        return this.mCallbacks.onGetCurrentPosition();
    }

    @Override
    public long getDuration() {
        return this.mCallbacks.onGetDuration();
    }

    public Object getRemoteControlClient() {
        if (this.mController != null) {
            return this.mController.getRemoteControlClient();
        }
        return null;
    }

    @Override
    public int getTransportControlFlags() {
        return this.mCallbacks.onGetTransportControlFlags();
    }

    @Override
    public boolean isPlaying() {
        return this.mCallbacks.onIsPlaying();
    }

    @Override
    public void pausePlaying() {
        if (this.mController != null) {
            this.mController.pausePlaying();
        }
        this.mCallbacks.onPause();
        this.pushControllerState();
        this.reportPlayingChanged();
    }

    public void refreshState() {
        this.pushControllerState();
        this.reportPlayingChanged();
        this.reportTransportControlsChanged();
    }

    @Override
    public void registerStateListener(TransportStateListener transportStateListener) {
        this.mListeners.add(transportStateListener);
    }

    @Override
    public void seekTo(long l) {
        this.mCallbacks.onSeekTo(l);
    }

    @Override
    public void startPlaying() {
        if (this.mController != null) {
            this.mController.startPlaying();
        }
        this.mCallbacks.onStart();
        this.pushControllerState();
        this.reportPlayingChanged();
    }

    @Override
    public void stopPlaying() {
        if (this.mController != null) {
            this.mController.stopPlaying();
        }
        this.mCallbacks.onStop();
        this.pushControllerState();
        this.reportPlayingChanged();
    }

    @Override
    public void unregisterStateListener(TransportStateListener transportStateListener) {
        this.mListeners.remove(transportStateListener);
    }

}

