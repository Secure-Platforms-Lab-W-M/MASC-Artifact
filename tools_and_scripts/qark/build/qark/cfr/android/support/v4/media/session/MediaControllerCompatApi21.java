/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.media.AudioAttributes
 *  android.media.MediaMetadata
 *  android.media.Rating
 *  android.media.session.MediaController
 *  android.media.session.MediaController$Callback
 *  android.media.session.MediaController$PlaybackInfo
 *  android.media.session.MediaController$TransportControls
 *  android.media.session.MediaSession
 *  android.media.session.MediaSession$QueueItem
 *  android.media.session.MediaSession$Token
 *  android.media.session.PlaybackState
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.ResultReceiver
 *  android.view.KeyEvent
 */
package android.support.v4.media.session;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class MediaControllerCompatApi21 {
    private MediaControllerCompatApi21() {
    }

    public static void adjustVolume(Object object, int n, int n2) {
        ((MediaController)object).adjustVolume(n, n2);
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy<Callback>(callback);
    }

    public static boolean dispatchMediaButtonEvent(Object object, KeyEvent keyEvent) {
        return ((MediaController)object).dispatchMediaButtonEvent(keyEvent);
    }

    public static Object fromToken(Context context, Object object) {
        return new MediaController(context, (MediaSession.Token)object);
    }

    public static Bundle getExtras(Object object) {
        return ((MediaController)object).getExtras();
    }

    public static long getFlags(Object object) {
        return ((MediaController)object).getFlags();
    }

    public static Object getMediaController(Activity activity) {
        return activity.getMediaController();
    }

    public static Object getMetadata(Object object) {
        return ((MediaController)object).getMetadata();
    }

    public static String getPackageName(Object object) {
        return ((MediaController)object).getPackageName();
    }

    public static Object getPlaybackInfo(Object object) {
        return ((MediaController)object).getPlaybackInfo();
    }

    public static Object getPlaybackState(Object object) {
        return ((MediaController)object).getPlaybackState();
    }

    public static List<Object> getQueue(Object object) {
        if ((object = ((MediaController)object).getQueue()) == null) {
            return null;
        }
        return new ArrayList<Object>((Collection<Object>)object);
    }

    public static CharSequence getQueueTitle(Object object) {
        return ((MediaController)object).getQueueTitle();
    }

    public static int getRatingType(Object object) {
        return ((MediaController)object).getRatingType();
    }

    public static PendingIntent getSessionActivity(Object object) {
        return ((MediaController)object).getSessionActivity();
    }

    public static Object getSessionToken(Object object) {
        return ((MediaController)object).getSessionToken();
    }

    public static Object getTransportControls(Object object) {
        return ((MediaController)object).getTransportControls();
    }

    public static void registerCallback(Object object, Object object2, Handler handler) {
        ((MediaController)object).registerCallback((MediaController.Callback)object2, handler);
    }

    public static void sendCommand(Object object, String string, Bundle bundle, ResultReceiver resultReceiver) {
        ((MediaController)object).sendCommand(string, bundle, resultReceiver);
    }

    public static void setMediaController(Activity activity, Object object) {
        activity.setMediaController((MediaController)object);
    }

    public static void setVolumeTo(Object object, int n, int n2) {
        ((MediaController)object).setVolumeTo(n, n2);
    }

    public static void unregisterCallback(Object object, Object object2) {
        ((MediaController)object).unregisterCallback((MediaController.Callback)object2);
    }

    public static interface Callback {
        public void onAudioInfoChanged(int var1, int var2, int var3, int var4, int var5);

        public void onExtrasChanged(Bundle var1);

        public void onMetadataChanged(Object var1);

        public void onPlaybackStateChanged(Object var1);

        public void onQueueChanged(List<?> var1);

        public void onQueueTitleChanged(CharSequence var1);

        public void onSessionDestroyed();

        public void onSessionEvent(String var1, Bundle var2);
    }

    static class CallbackProxy<T extends Callback>
    extends MediaController.Callback {
        protected final T mCallback;

        public CallbackProxy(T t) {
            this.mCallback = t;
        }

        public void onAudioInfoChanged(MediaController.PlaybackInfo playbackInfo) {
            this.mCallback.onAudioInfoChanged(playbackInfo.getPlaybackType(), PlaybackInfo.getLegacyAudioStream((Object)playbackInfo), playbackInfo.getVolumeControl(), playbackInfo.getMaxVolume(), playbackInfo.getCurrentVolume());
        }

        public void onExtrasChanged(Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            this.mCallback.onExtrasChanged(bundle);
        }

        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            this.mCallback.onMetadataChanged((Object)mediaMetadata);
        }

        public void onPlaybackStateChanged(PlaybackState playbackState) {
            this.mCallback.onPlaybackStateChanged((Object)playbackState);
        }

        public void onQueueChanged(List<MediaSession.QueueItem> list) {
            this.mCallback.onQueueChanged(list);
        }

        public void onQueueTitleChanged(CharSequence charSequence) {
            this.mCallback.onQueueTitleChanged(charSequence);
        }

        public void onSessionDestroyed() {
            this.mCallback.onSessionDestroyed();
        }

        public void onSessionEvent(String string, Bundle bundle) {
            MediaSessionCompat.ensureClassLoader(bundle);
            this.mCallback.onSessionEvent(string, bundle);
        }
    }

    public static class PlaybackInfo {
        private static final int FLAG_SCO = 4;
        private static final int STREAM_BLUETOOTH_SCO = 6;
        private static final int STREAM_SYSTEM_ENFORCED = 7;

        private PlaybackInfo() {
        }

        public static AudioAttributes getAudioAttributes(Object object) {
            return ((MediaController.PlaybackInfo)object).getAudioAttributes();
        }

        public static int getCurrentVolume(Object object) {
            return ((MediaController.PlaybackInfo)object).getCurrentVolume();
        }

        public static int getLegacyAudioStream(Object object) {
            return PlaybackInfo.toLegacyStreamType(PlaybackInfo.getAudioAttributes(object));
        }

        public static int getMaxVolume(Object object) {
            return ((MediaController.PlaybackInfo)object).getMaxVolume();
        }

        public static int getPlaybackType(Object object) {
            return ((MediaController.PlaybackInfo)object).getPlaybackType();
        }

        public static int getVolumeControl(Object object) {
            return ((MediaController.PlaybackInfo)object).getVolumeControl();
        }

        private static int toLegacyStreamType(AudioAttributes audioAttributes) {
            if ((audioAttributes.getFlags() & 1) == 1) {
                return 7;
            }
            if ((audioAttributes.getFlags() & 4) == 4) {
                return 6;
            }
            int n = audioAttributes.getUsage();
            if (n != 13) {
                switch (n) {
                    default: {
                        return 3;
                    }
                    case 6: {
                        return 2;
                    }
                    case 5: 
                    case 7: 
                    case 8: 
                    case 9: 
                    case 10: {
                        return 5;
                    }
                    case 4: {
                        return 4;
                    }
                    case 3: {
                        return 8;
                    }
                    case 2: 
                }
                return 0;
            }
            return 1;
        }
    }

    public static class TransportControls {
        private TransportControls() {
        }

        public static void fastForward(Object object) {
            ((MediaController.TransportControls)object).fastForward();
        }

        public static void pause(Object object) {
            ((MediaController.TransportControls)object).pause();
        }

        public static void play(Object object) {
            ((MediaController.TransportControls)object).play();
        }

        public static void playFromMediaId(Object object, String string, Bundle bundle) {
            ((MediaController.TransportControls)object).playFromMediaId(string, bundle);
        }

        public static void playFromSearch(Object object, String string, Bundle bundle) {
            ((MediaController.TransportControls)object).playFromSearch(string, bundle);
        }

        public static void rewind(Object object) {
            ((MediaController.TransportControls)object).rewind();
        }

        public static void seekTo(Object object, long l) {
            ((MediaController.TransportControls)object).seekTo(l);
        }

        public static void sendCustomAction(Object object, String string, Bundle bundle) {
            ((MediaController.TransportControls)object).sendCustomAction(string, bundle);
        }

        public static void setRating(Object object, Object object2) {
            ((MediaController.TransportControls)object).setRating((Rating)object2);
        }

        public static void skipToNext(Object object) {
            ((MediaController.TransportControls)object).skipToNext();
        }

        public static void skipToPrevious(Object object) {
            ((MediaController.TransportControls)object).skipToPrevious();
        }

        public static void skipToQueueItem(Object object, long l) {
            ((MediaController.TransportControls)object).skipToQueueItem(l);
        }

        public static void stop(Object object) {
            ((MediaController.TransportControls)object).stop();
        }
    }

}

