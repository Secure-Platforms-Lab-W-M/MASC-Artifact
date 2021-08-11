/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.PendingIntent
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Resources
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.media.AudioManager
 *  android.media.MediaMetadataEditor
 *  android.media.Rating
 *  android.media.RemoteControlClient
 *  android.media.RemoteControlClient$MetadataEditor
 *  android.media.RemoteControlClient$OnMetadataUpdateListener
 *  android.media.RemoteControlClient$OnPlaybackPositionUpdateListener
 *  android.media.session.MediaSession
 *  android.media.session.MediaSessionManager
 *  android.media.session.MediaSessionManager$RemoteUserInfo
 *  android.net.Uri
 *  android.os.BadParcelableException
 *  android.os.Binder
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.RemoteCallbackList
 *  android.os.RemoteException
 *  android.os.ResultReceiver
 *  android.os.SystemClock
 *  android.text.TextUtils
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.KeyEvent
 *  android.view.ViewConfiguration
 */
package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataEditor;
import android.media.Rating;
import android.media.RemoteControlClient;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompatApi21;
import android.support.v4.media.session.MediaSessionCompatApi22;
import android.support.v4.media.session.MediaSessionCompatApi23;
import android.support.v4.media.session.MediaSessionCompatApi24;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import androidx.core.app.BundleCompat;
import androidx.media.MediaSessionManager;
import androidx.media.VolumeProviderCompat;
import androidx.media.session.MediaButtonReceiver;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaSessionCompat {
    public static final String ACTION_ARGUMENT_CAPTIONING_ENABLED = "android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED";
    public static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
    public static final String ACTION_ARGUMENT_MEDIA_ID = "android.support.v4.media.session.action.ARGUMENT_MEDIA_ID";
    public static final String ACTION_ARGUMENT_QUERY = "android.support.v4.media.session.action.ARGUMENT_QUERY";
    public static final String ACTION_ARGUMENT_RATING = "android.support.v4.media.session.action.ARGUMENT_RATING";
    public static final String ACTION_ARGUMENT_REPEAT_MODE = "android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE";
    public static final String ACTION_ARGUMENT_SHUFFLE_MODE = "android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE";
    public static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
    public static final String ACTION_FLAG_AS_INAPPROPRIATE = "android.support.v4.media.session.action.FLAG_AS_INAPPROPRIATE";
    public static final String ACTION_FOLLOW = "android.support.v4.media.session.action.FOLLOW";
    public static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
    public static final String ACTION_PREPARE = "android.support.v4.media.session.action.PREPARE";
    public static final String ACTION_PREPARE_FROM_MEDIA_ID = "android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID";
    public static final String ACTION_PREPARE_FROM_SEARCH = "android.support.v4.media.session.action.PREPARE_FROM_SEARCH";
    public static final String ACTION_PREPARE_FROM_URI = "android.support.v4.media.session.action.PREPARE_FROM_URI";
    public static final String ACTION_SET_CAPTIONING_ENABLED = "android.support.v4.media.session.action.SET_CAPTIONING_ENABLED";
    public static final String ACTION_SET_RATING = "android.support.v4.media.session.action.SET_RATING";
    public static final String ACTION_SET_REPEAT_MODE = "android.support.v4.media.session.action.SET_REPEAT_MODE";
    public static final String ACTION_SET_SHUFFLE_MODE = "android.support.v4.media.session.action.SET_SHUFFLE_MODE";
    public static final String ACTION_SKIP_AD = "android.support.v4.media.session.action.SKIP_AD";
    public static final String ACTION_UNFOLLOW = "android.support.v4.media.session.action.UNFOLLOW";
    public static final String ARGUMENT_MEDIA_ATTRIBUTE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE";
    public static final String ARGUMENT_MEDIA_ATTRIBUTE_VALUE = "android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE_VALUE";
    private static final String DATA_CALLING_PACKAGE = "data_calling_pkg";
    private static final String DATA_CALLING_PID = "data_calling_pid";
    private static final String DATA_CALLING_UID = "data_calling_uid";
    private static final String DATA_EXTRAS = "data_extras";
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_QUEUE_COMMANDS = 4;
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    public static final String KEY_EXTRA_BINDER = "android.support.v4.media.session.EXTRA_BINDER";
    public static final String KEY_SESSION_TOKEN2_BUNDLE = "android.support.v4.media.session.SESSION_TOKEN2_BUNDLE";
    public static final String KEY_TOKEN = "android.support.v4.media.session.TOKEN";
    private static final int MAX_BITMAP_SIZE_IN_DP = 320;
    public static final int MEDIA_ATTRIBUTE_ALBUM = 1;
    public static final int MEDIA_ATTRIBUTE_ARTIST = 0;
    public static final int MEDIA_ATTRIBUTE_PLAYLIST = 2;
    static final String TAG = "MediaSessionCompat";
    static int sMaxBitmapSize;
    private final ArrayList<OnActiveChangeListener> mActiveListeners = new ArrayList();
    private final MediaControllerCompat mController;
    private final MediaSessionImpl mImpl;

    private MediaSessionCompat(Context context, MediaSessionImpl mediaSessionImpl) {
        this.mImpl = mediaSessionImpl;
        if (Build.VERSION.SDK_INT >= 21 && !MediaSessionCompatApi21.hasCallback(mediaSessionImpl.getMediaSession())) {
            this.setCallback(new Callback(){});
        }
        this.mController = new MediaControllerCompat(context, this);
    }

    public MediaSessionCompat(Context context, String string) {
        this(context, string, null, null);
    }

    public MediaSessionCompat(Context context, String string, ComponentName componentName, PendingIntent pendingIntent) {
        this(context, string, componentName, pendingIntent, null);
    }

    private MediaSessionCompat(Context context, String string, ComponentName componentName, PendingIntent pendingIntent, Bundle bundle) {
        if (context != null) {
            if (!TextUtils.isEmpty((CharSequence)string)) {
                ComponentName componentName2 = componentName;
                if (componentName == null) {
                    componentName2 = componentName = MediaButtonReceiver.getMediaButtonReceiverComponent(context);
                    if (componentName == null) {
                        Log.w((String)"MediaSessionCompat", (String)"Couldn't find a unique registered media button receiver in the given context.");
                        componentName2 = componentName;
                    }
                }
                componentName = pendingIntent;
                if (componentName2 != null) {
                    componentName = pendingIntent;
                    if (pendingIntent == null) {
                        componentName = new Intent("android.intent.action.MEDIA_BUTTON");
                        componentName.setComponent(componentName2);
                        componentName = PendingIntent.getBroadcast((Context)context, (int)0, (Intent)componentName, (int)0);
                    }
                }
                if (Build.VERSION.SDK_INT >= 28) {
                    this.mImpl = new MediaSessionImplApi28(context, string, bundle);
                    this.setCallback(new Callback(){});
                    this.mImpl.setMediaButtonReceiver((PendingIntent)componentName);
                } else if (Build.VERSION.SDK_INT >= 21) {
                    this.mImpl = new MediaSessionImplApi21(context, string, bundle);
                    this.setCallback(new Callback(){});
                    this.mImpl.setMediaButtonReceiver((PendingIntent)componentName);
                } else {
                    this.mImpl = Build.VERSION.SDK_INT >= 19 ? new MediaSessionImplApi19(context, string, componentName2, (PendingIntent)componentName) : (Build.VERSION.SDK_INT >= 18 ? new MediaSessionImplApi18(context, string, componentName2, (PendingIntent)componentName) : new MediaSessionImplBase(context, string, componentName2, (PendingIntent)componentName));
                }
                this.mController = new MediaControllerCompat(context, this);
                if (sMaxBitmapSize == 0) {
                    sMaxBitmapSize = (int)(TypedValue.applyDimension((int)1, (float)320.0f, (DisplayMetrics)context.getResources().getDisplayMetrics()) + 0.5f);
                }
                return;
            }
            throw new IllegalArgumentException("tag must not be null or empty");
        }
        throw new IllegalArgumentException("context must not be null");
    }

    public MediaSessionCompat(Context context, String string, Bundle bundle) {
        this(context, string, null, null, bundle);
    }

    public static void ensureClassLoader(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
        }
    }

    public static MediaSessionCompat fromMediaSession(Context context, Object object) {
        if (context != null && object != null && Build.VERSION.SDK_INT >= 21) {
            return new MediaSessionCompat(context, new MediaSessionImplApi21(object));
        }
        return null;
    }

    static PlaybackStateCompat getStateWithUpdatedPosition(PlaybackStateCompat playbackStateCompat, MediaMetadataCompat mediaMetadataCompat) {
        if (playbackStateCompat != null) {
            long l;
            if (playbackStateCompat.getPosition() == -1L) {
                return playbackStateCompat;
            }
            if ((playbackStateCompat.getState() == 3 || playbackStateCompat.getState() == 4 || playbackStateCompat.getState() == 5) && (l = playbackStateCompat.getLastPositionUpdateTime()) > 0L) {
                long l2 = SystemClock.elapsedRealtime();
                long l3 = (long)(playbackStateCompat.getPlaybackSpeed() * (float)(l2 - l)) + playbackStateCompat.getPosition();
                l = mediaMetadataCompat != null && mediaMetadataCompat.containsKey("android.media.metadata.DURATION") ? mediaMetadataCompat.getLong("android.media.metadata.DURATION") : -1L;
                if (l < 0L || l3 <= l) {
                    l = l3 < 0L ? 0L : l3;
                }
                return new PlaybackStateCompat.Builder(playbackStateCompat).setState(playbackStateCompat.getState(), l, playbackStateCompat.getPlaybackSpeed(), l2).build();
            }
            return playbackStateCompat;
        }
        return playbackStateCompat;
    }

    public void addOnActiveChangeListener(OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener != null) {
            this.mActiveListeners.add(onActiveChangeListener);
            return;
        }
        throw new IllegalArgumentException("Listener may not be null");
    }

    public String getCallingPackage() {
        return this.mImpl.getCallingPackage();
    }

    public MediaControllerCompat getController() {
        return this.mController;
    }

    public final MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
        return this.mImpl.getCurrentControllerInfo();
    }

    public Object getMediaSession() {
        return this.mImpl.getMediaSession();
    }

    public Object getRemoteControlClient() {
        return this.mImpl.getRemoteControlClient();
    }

    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }

    public boolean isActive() {
        return this.mImpl.isActive();
    }

    public void release() {
        this.mImpl.release();
    }

    public void removeOnActiveChangeListener(OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener != null) {
            this.mActiveListeners.remove(onActiveChangeListener);
            return;
        }
        throw new IllegalArgumentException("Listener may not be null");
    }

    public void sendSessionEvent(String string, Bundle bundle) {
        if (!TextUtils.isEmpty((CharSequence)string)) {
            this.mImpl.sendSessionEvent(string, bundle);
            return;
        }
        throw new IllegalArgumentException("event cannot be null or empty");
    }

    public void setActive(boolean bl) {
        this.mImpl.setActive(bl);
        Iterator<OnActiveChangeListener> iterator = this.mActiveListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onActiveChanged();
        }
    }

    public void setCallback(Callback callback) {
        this.setCallback(callback, null);
    }

    public void setCallback(Callback callback, Handler handler) {
        if (callback == null) {
            this.mImpl.setCallback(null, null);
            return;
        }
        MediaSessionImpl mediaSessionImpl = this.mImpl;
        if (handler == null) {
            handler = new Handler();
        }
        mediaSessionImpl.setCallback(callback, handler);
    }

    public void setCaptioningEnabled(boolean bl) {
        this.mImpl.setCaptioningEnabled(bl);
    }

    public void setExtras(Bundle bundle) {
        this.mImpl.setExtras(bundle);
    }

    public void setFlags(int n) {
        this.mImpl.setFlags(n);
    }

    public void setMediaButtonReceiver(PendingIntent pendingIntent) {
        this.mImpl.setMediaButtonReceiver(pendingIntent);
    }

    public void setMetadata(MediaMetadataCompat mediaMetadataCompat) {
        this.mImpl.setMetadata(mediaMetadataCompat);
    }

    public void setPlaybackState(PlaybackStateCompat playbackStateCompat) {
        this.mImpl.setPlaybackState(playbackStateCompat);
    }

    public void setPlaybackToLocal(int n) {
        this.mImpl.setPlaybackToLocal(n);
    }

    public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
        if (volumeProviderCompat != null) {
            this.mImpl.setPlaybackToRemote(volumeProviderCompat);
            return;
        }
        throw new IllegalArgumentException("volumeProvider may not be null!");
    }

    public void setQueue(List<QueueItem> list) {
        this.mImpl.setQueue(list);
    }

    public void setQueueTitle(CharSequence charSequence) {
        this.mImpl.setQueueTitle(charSequence);
    }

    public void setRatingType(int n) {
        this.mImpl.setRatingType(n);
    }

    public void setRepeatMode(int n) {
        this.mImpl.setRepeatMode(n);
    }

    public void setSessionActivity(PendingIntent pendingIntent) {
        this.mImpl.setSessionActivity(pendingIntent);
    }

    public void setShuffleMode(int n) {
        this.mImpl.setShuffleMode(n);
    }

    public static abstract class Callback {
        private CallbackHandler mCallbackHandler = null;
        final Object mCallbackObj;
        private boolean mMediaPlayPauseKeyPending;
        WeakReference<MediaSessionImpl> mSessionImpl;

        public Callback() {
            if (Build.VERSION.SDK_INT >= 24) {
                this.mCallbackObj = MediaSessionCompatApi24.createCallback(new StubApi24());
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                this.mCallbackObj = MediaSessionCompatApi23.createCallback(new StubApi23());
                return;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                this.mCallbackObj = MediaSessionCompatApi21.createCallback(new StubApi21());
                return;
            }
            this.mCallbackObj = null;
        }

        void handleMediaPlayPauseKeySingleTapIfPending(MediaSessionManager.RemoteUserInfo remoteUserInfo) {
            if (!this.mMediaPlayPauseKeyPending) {
                return;
            }
            boolean bl = false;
            this.mMediaPlayPauseKeyPending = false;
            this.mCallbackHandler.removeMessages(1);
            MediaSessionImpl mediaSessionImpl = this.mSessionImpl.get();
            if (mediaSessionImpl == null) {
                return;
            }
            PlaybackStateCompat playbackStateCompat = mediaSessionImpl.getPlaybackState();
            long l = playbackStateCompat == null ? 0L : playbackStateCompat.getActions();
            boolean bl2 = playbackStateCompat != null && playbackStateCompat.getState() == 3;
            boolean bl3 = (516L & l) != 0L;
            if ((514L & l) != 0L) {
                bl = true;
            }
            mediaSessionImpl.setCurrentControllerInfo(remoteUserInfo);
            if (bl2 && bl) {
                this.onPause();
            } else if (!bl2 && bl3) {
                this.onPlay();
            }
            mediaSessionImpl.setCurrentControllerInfo(null);
        }

        public void onAddQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
        }

        public void onAddQueueItem(MediaDescriptionCompat mediaDescriptionCompat, int n) {
        }

        public void onCommand(String string, Bundle bundle, ResultReceiver resultReceiver) {
        }

        public void onCustomAction(String string, Bundle bundle) {
        }

        public void onFastForward() {
        }

        public boolean onMediaButtonEvent(Intent object) {
            if (Build.VERSION.SDK_INT >= 27) {
                return false;
            }
            Object object2 = this.mSessionImpl.get();
            if (object2 != null) {
                if (this.mCallbackHandler == null) {
                    return false;
                }
                KeyEvent keyEvent = (KeyEvent)object.getParcelableExtra("android.intent.extra.KEY_EVENT");
                if (keyEvent != null) {
                    if (keyEvent.getAction() != 0) {
                        return false;
                    }
                    object = object2.getCurrentControllerInfo();
                    int n = keyEvent.getKeyCode();
                    if (n != 79 && n != 85) {
                        this.handleMediaPlayPauseKeySingleTapIfPending((MediaSessionManager.RemoteUserInfo)object);
                        return false;
                    }
                    if (keyEvent.getRepeatCount() > 0) {
                        this.handleMediaPlayPauseKeySingleTapIfPending((MediaSessionManager.RemoteUserInfo)object);
                        return true;
                    }
                    if (this.mMediaPlayPauseKeyPending) {
                        this.mCallbackHandler.removeMessages(1);
                        this.mMediaPlayPauseKeyPending = false;
                        object = object2.getPlaybackState();
                        long l = object == null ? 0L : object.getActions();
                        if ((32L & l) != 0L) {
                            this.onSkipToNext();
                        }
                        return true;
                    }
                    this.mMediaPlayPauseKeyPending = true;
                    object2 = this.mCallbackHandler;
                    object2.sendMessageDelayed(object2.obtainMessage(1, object), (long)ViewConfiguration.getDoubleTapTimeout());
                    return true;
                }
                return false;
            }
            return false;
        }

        public void onPause() {
        }

        public void onPlay() {
        }

        public void onPlayFromMediaId(String string, Bundle bundle) {
        }

        public void onPlayFromSearch(String string, Bundle bundle) {
        }

        public void onPlayFromUri(Uri uri, Bundle bundle) {
        }

        public void onPrepare() {
        }

        public void onPrepareFromMediaId(String string, Bundle bundle) {
        }

        public void onPrepareFromSearch(String string, Bundle bundle) {
        }

        public void onPrepareFromUri(Uri uri, Bundle bundle) {
        }

        public void onRemoveQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
        }

        @Deprecated
        public void onRemoveQueueItemAt(int n) {
        }

        public void onRewind() {
        }

        public void onSeekTo(long l) {
        }

        public void onSetCaptioningEnabled(boolean bl) {
        }

        public void onSetRating(RatingCompat ratingCompat) {
        }

        public void onSetRating(RatingCompat ratingCompat, Bundle bundle) {
        }

        public void onSetRepeatMode(int n) {
        }

        public void onSetShuffleMode(int n) {
        }

        public void onSkipToNext() {
        }

        public void onSkipToPrevious() {
        }

        public void onSkipToQueueItem(long l) {
        }

        public void onStop() {
        }

        void setSessionImpl(MediaSessionImpl object, Handler handler) {
            this.mSessionImpl = new WeakReference<MediaSessionImpl>((MediaSessionImpl)object);
            object = this.mCallbackHandler;
            if (object != null) {
                object.removeCallbacksAndMessages((Object)null);
            }
            this.mCallbackHandler = new CallbackHandler(handler.getLooper());
        }

        private class CallbackHandler
        extends Handler {
            private static final int MSG_MEDIA_PLAY_PAUSE_KEY_DOUBLE_TAP_TIMEOUT = 1;

            CallbackHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message message) {
                if (message.what == 1) {
                    Callback.this.handleMediaPlayPauseKeySingleTapIfPending((MediaSessionManager.RemoteUserInfo)message.obj);
                }
            }
        }

        private class StubApi21
        implements MediaSessionCompatApi21.Callback {
            StubApi21() {
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void onCommand(String object, Bundle bundle, ResultReceiver object2) {
                try {
                    boolean bl = object.equals("android.support.v4.media.session.command.GET_EXTRA_BINDER");
                    Token token = null;
                    Object var6_7 = null;
                    if (bl) {
                        object = (MediaSessionImplApi21)Callback.this.mSessionImpl.get();
                        if (object == null) return;
                        bundle = new Bundle();
                        token = object.getSessionToken();
                        object = token.getExtraBinder();
                        object = object == null ? var6_7 : object.asBinder();
                        BundleCompat.putBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER", (IBinder)object);
                        bundle.putBundle("android.support.v4.media.session.SESSION_TOKEN2_BUNDLE", token.getSessionToken2Bundle());
                        object2.send(0, bundle);
                        return;
                    }
                    bl = object.equals("android.support.v4.media.session.command.ADD_QUEUE_ITEM");
                    if (bl) {
                        Callback.this.onAddQueueItem((MediaDescriptionCompat)bundle.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"));
                        return;
                    }
                    bl = object.equals("android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT");
                    if (bl) {
                        Callback.this.onAddQueueItem((MediaDescriptionCompat)bundle.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"), bundle.getInt("android.support.v4.media.session.command.ARGUMENT_INDEX"));
                        return;
                    }
                    if (object.equals("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM")) {
                        Callback.this.onRemoveQueueItem((MediaDescriptionCompat)bundle.getParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION"));
                        return;
                    }
                    if (!object.equals("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM_AT")) {
                        Callback.this.onCommand((String)object, bundle, (ResultReceiver)object2);
                        return;
                    }
                    object2 = (MediaSessionImplApi21)Callback.this.mSessionImpl.get();
                    if (object2 == null) return;
                    if (object2.mQueue == null) return;
                    int n = bundle.getInt("android.support.v4.media.session.command.ARGUMENT_INDEX", -1);
                    object = token;
                    if (n >= 0) {
                        object = token;
                        if (n < object2.mQueue.size()) {
                            object = object2.mQueue.get(n);
                        }
                    }
                    if (object == null) return;
                    Callback.this.onRemoveQueueItem(object.getDescription());
                    return;
                }
                catch (BadParcelableException badParcelableException) {
                    Log.e((String)"MediaSessionCompat", (String)"Could not unparcel the extra data.");
                    return;
                }
            }

            @Override
            public void onCustomAction(String object, Bundle bundle) {
                Bundle bundle2 = bundle.getBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
                MediaSessionCompat.ensureClassLoader(bundle2);
                if (object.equals("android.support.v4.media.session.action.PLAY_FROM_URI")) {
                    object = (Uri)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
                    Callback.this.onPlayFromUri((Uri)object, bundle2);
                    return;
                }
                if (object.equals("android.support.v4.media.session.action.PREPARE")) {
                    Callback.this.onPrepare();
                    return;
                }
                if (object.equals("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID")) {
                    object = bundle.getString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID");
                    Callback.this.onPrepareFromMediaId((String)object, bundle2);
                    return;
                }
                if (object.equals("android.support.v4.media.session.action.PREPARE_FROM_SEARCH")) {
                    object = bundle.getString("android.support.v4.media.session.action.ARGUMENT_QUERY");
                    Callback.this.onPrepareFromSearch((String)object, bundle2);
                    return;
                }
                if (object.equals("android.support.v4.media.session.action.PREPARE_FROM_URI")) {
                    object = (Uri)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
                    Callback.this.onPrepareFromUri((Uri)object, bundle2);
                    return;
                }
                if (object.equals("android.support.v4.media.session.action.SET_CAPTIONING_ENABLED")) {
                    boolean bl = bundle.getBoolean("android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED");
                    Callback.this.onSetCaptioningEnabled(bl);
                    return;
                }
                if (object.equals("android.support.v4.media.session.action.SET_REPEAT_MODE")) {
                    int n = bundle.getInt("android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE");
                    Callback.this.onSetRepeatMode(n);
                    return;
                }
                if (object.equals("android.support.v4.media.session.action.SET_SHUFFLE_MODE")) {
                    int n = bundle.getInt("android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE");
                    Callback.this.onSetShuffleMode(n);
                    return;
                }
                if (object.equals("android.support.v4.media.session.action.SET_RATING")) {
                    object = (RatingCompat)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_RATING");
                    Callback.this.onSetRating((RatingCompat)object, bundle2);
                    return;
                }
                Callback.this.onCustomAction((String)object, bundle);
            }

            @Override
            public void onFastForward() {
                Callback.this.onFastForward();
            }

            @Override
            public boolean onMediaButtonEvent(Intent intent) {
                return Callback.this.onMediaButtonEvent(intent);
            }

            @Override
            public void onPause() {
                Callback.this.onPause();
            }

            @Override
            public void onPlay() {
                Callback.this.onPlay();
            }

            @Override
            public void onPlayFromMediaId(String string, Bundle bundle) {
                Callback.this.onPlayFromMediaId(string, bundle);
            }

            @Override
            public void onPlayFromSearch(String string, Bundle bundle) {
                Callback.this.onPlayFromSearch(string, bundle);
            }

            @Override
            public void onRewind() {
                Callback.this.onRewind();
            }

            @Override
            public void onSeekTo(long l) {
                Callback.this.onSeekTo(l);
            }

            @Override
            public void onSetRating(Object object) {
                Callback.this.onSetRating(RatingCompat.fromRating(object));
            }

            @Override
            public void onSetRating(Object object, Bundle bundle) {
            }

            @Override
            public void onSkipToNext() {
                Callback.this.onSkipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                Callback.this.onSkipToPrevious();
            }

            @Override
            public void onSkipToQueueItem(long l) {
                Callback.this.onSkipToQueueItem(l);
            }

            @Override
            public void onStop() {
                Callback.this.onStop();
            }
        }

        private class StubApi23
        extends StubApi21
        implements MediaSessionCompatApi23.Callback {
            StubApi23() {
            }

            @Override
            public void onPlayFromUri(Uri uri, Bundle bundle) {
                Callback.this.onPlayFromUri(uri, bundle);
            }
        }

        private class StubApi24
        extends StubApi23
        implements MediaSessionCompatApi24.Callback {
            StubApi24() {
            }

            @Override
            public void onPrepare() {
                Callback.this.onPrepare();
            }

            @Override
            public void onPrepareFromMediaId(String string, Bundle bundle) {
                Callback.this.onPrepareFromMediaId(string, bundle);
            }

            @Override
            public void onPrepareFromSearch(String string, Bundle bundle) {
                Callback.this.onPrepareFromSearch(string, bundle);
            }

            @Override
            public void onPrepareFromUri(Uri uri, Bundle bundle) {
                Callback.this.onPrepareFromUri(uri, bundle);
            }
        }

    }

    static interface MediaSessionImpl {
        public String getCallingPackage();

        public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo();

        public Object getMediaSession();

        public PlaybackStateCompat getPlaybackState();

        public Object getRemoteControlClient();

        public Token getSessionToken();

        public boolean isActive();

        public void release();

        public void sendSessionEvent(String var1, Bundle var2);

        public void setActive(boolean var1);

        public void setCallback(Callback var1, Handler var2);

        public void setCaptioningEnabled(boolean var1);

        public void setCurrentControllerInfo(MediaSessionManager.RemoteUserInfo var1);

        public void setExtras(Bundle var1);

        public void setFlags(int var1);

        public void setMediaButtonReceiver(PendingIntent var1);

        public void setMetadata(MediaMetadataCompat var1);

        public void setPlaybackState(PlaybackStateCompat var1);

        public void setPlaybackToLocal(int var1);

        public void setPlaybackToRemote(VolumeProviderCompat var1);

        public void setQueue(List<QueueItem> var1);

        public void setQueueTitle(CharSequence var1);

        public void setRatingType(int var1);

        public void setRepeatMode(int var1);

        public void setSessionActivity(PendingIntent var1);

        public void setShuffleMode(int var1);
    }

    static class MediaSessionImplApi18
    extends MediaSessionImplBase {
        private static boolean sIsMbrPendingIntentSupported = true;

        MediaSessionImplApi18(Context context, String string, ComponentName componentName, PendingIntent pendingIntent) {
            super(context, string, componentName, pendingIntent);
        }

        @Override
        int getRccTransportControlFlagsFromActions(long l) {
            int n;
            int n2 = n = super.getRccTransportControlFlagsFromActions(l);
            if ((256L & l) != 0L) {
                n2 = n | 256;
            }
            return n2;
        }

        @Override
        void registerMediaButtonEventReceiver(PendingIntent pendingIntent, ComponentName componentName) {
            if (sIsMbrPendingIntentSupported) {
                try {
                    this.mAudioManager.registerMediaButtonEventReceiver(pendingIntent);
                }
                catch (NullPointerException nullPointerException) {
                    Log.w((String)"MediaSessionCompat", (String)"Unable to register media button event receiver with PendingIntent, falling back to ComponentName.");
                    sIsMbrPendingIntentSupported = false;
                }
            }
            if (!sIsMbrPendingIntentSupported) {
                super.registerMediaButtonEventReceiver(pendingIntent, componentName);
            }
        }

        @Override
        public void setCallback(Callback object, Handler handler) {
            super.setCallback((Callback)object, handler);
            if (object == null) {
                this.mRcc.setPlaybackPositionUpdateListener(null);
                return;
            }
            object = new RemoteControlClient.OnPlaybackPositionUpdateListener(){

                public void onPlaybackPositionUpdate(long l) {
                    MediaSessionImplApi18.this.postToHandler(18, -1, -1, l, null);
                }
            };
            this.mRcc.setPlaybackPositionUpdateListener((RemoteControlClient.OnPlaybackPositionUpdateListener)object);
        }

        @Override
        void setRccState(PlaybackStateCompat playbackStateCompat) {
            long l = playbackStateCompat.getPosition();
            float f = playbackStateCompat.getPlaybackSpeed();
            long l2 = playbackStateCompat.getLastPositionUpdateTime();
            long l3 = SystemClock.elapsedRealtime();
            long l4 = l;
            if (playbackStateCompat.getState() == 3) {
                l4 = l;
                if (l > 0L) {
                    l4 = 0L;
                    if (l2 > 0L) {
                        l4 = l2 = l3 - l2;
                        if (f > 0.0f) {
                            l4 = l2;
                            if (f != 1.0f) {
                                l4 = (long)((float)l2 * f);
                            }
                        }
                    }
                    l4 = l + l4;
                }
            }
            this.mRcc.setPlaybackState(this.getRccStateFromState(playbackStateCompat.getState()), l4, f);
        }

        @Override
        void unregisterMediaButtonEventReceiver(PendingIntent pendingIntent, ComponentName componentName) {
            if (sIsMbrPendingIntentSupported) {
                this.mAudioManager.unregisterMediaButtonEventReceiver(pendingIntent);
                return;
            }
            super.unregisterMediaButtonEventReceiver(pendingIntent, componentName);
        }

    }

    static class MediaSessionImplApi19
    extends MediaSessionImplApi18 {
        MediaSessionImplApi19(Context context, String string, ComponentName componentName, PendingIntent pendingIntent) {
            super(context, string, componentName, pendingIntent);
        }

        @Override
        RemoteControlClient.MetadataEditor buildRccMetadata(Bundle bundle) {
            RemoteControlClient.MetadataEditor metadataEditor = super.buildRccMetadata(bundle);
            long l = this.mState == null ? 0L : this.mState.getActions();
            if ((128L & l) != 0L) {
                metadataEditor.addEditableKey(268435457);
            }
            if (bundle == null) {
                return metadataEditor;
            }
            if (bundle.containsKey("android.media.metadata.YEAR")) {
                metadataEditor.putLong(8, bundle.getLong("android.media.metadata.YEAR"));
            }
            if (bundle.containsKey("android.media.metadata.RATING")) {
                metadataEditor.putObject(101, (Object)bundle.getParcelable("android.media.metadata.RATING"));
            }
            if (bundle.containsKey("android.media.metadata.USER_RATING")) {
                metadataEditor.putObject(268435457, (Object)bundle.getParcelable("android.media.metadata.USER_RATING"));
            }
            return metadataEditor;
        }

        @Override
        int getRccTransportControlFlagsFromActions(long l) {
            int n;
            int n2 = n = super.getRccTransportControlFlagsFromActions(l);
            if ((128L & l) != 0L) {
                n2 = n | 512;
            }
            return n2;
        }

        @Override
        public void setCallback(Callback object, Handler handler) {
            super.setCallback((Callback)object, handler);
            if (object == null) {
                this.mRcc.setMetadataUpdateListener(null);
                return;
            }
            object = new RemoteControlClient.OnMetadataUpdateListener(){

                public void onMetadataUpdate(int n, Object object) {
                    if (n == 268435457 && object instanceof Rating) {
                        MediaSessionImplApi19.this.postToHandler(19, -1, -1, RatingCompat.fromRating(object), null);
                    }
                }
            };
            this.mRcc.setMetadataUpdateListener((RemoteControlClient.OnMetadataUpdateListener)object);
        }

    }

    static class MediaSessionImplApi21
    implements MediaSessionImpl {
        boolean mCaptioningEnabled;
        boolean mDestroyed = false;
        final RemoteCallbackList<IMediaControllerCallback> mExtraControllerCallbacks = new RemoteCallbackList();
        MediaMetadataCompat mMetadata;
        PlaybackStateCompat mPlaybackState;
        List<QueueItem> mQueue;
        int mRatingType;
        int mRepeatMode;
        final Object mSessionObj;
        int mShuffleMode;
        final Token mToken;

        MediaSessionImplApi21(Context object, String string, Bundle bundle) {
            this.mSessionObj = object = MediaSessionCompatApi21.createSession((Context)object, string);
            this.mToken = new Token((Object)MediaSessionCompatApi21.getSessionToken(object), new ExtraSession(), bundle);
        }

        MediaSessionImplApi21(Object object) {
            this.mSessionObj = object = MediaSessionCompatApi21.verifySession(object);
            this.mToken = new Token((Object)MediaSessionCompatApi21.getSessionToken(object), new ExtraSession());
        }

        @Override
        public String getCallingPackage() {
            if (Build.VERSION.SDK_INT < 24) {
                return null;
            }
            return MediaSessionCompatApi24.getCallingPackage(this.mSessionObj);
        }

        @Override
        public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            return null;
        }

        @Override
        public Object getMediaSession() {
            return this.mSessionObj;
        }

        @Override
        public PlaybackStateCompat getPlaybackState() {
            return this.mPlaybackState;
        }

        @Override
        public Object getRemoteControlClient() {
            return null;
        }

        @Override
        public Token getSessionToken() {
            return this.mToken;
        }

        @Override
        public boolean isActive() {
            return MediaSessionCompatApi21.isActive(this.mSessionObj);
        }

        @Override
        public void release() {
            this.mDestroyed = true;
            MediaSessionCompatApi21.release(this.mSessionObj);
        }

        @Override
        public void sendSessionEvent(String string, Bundle bundle) {
            if (Build.VERSION.SDK_INT < 23) {
                for (int i = this.mExtraControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                    IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(i);
                    try {
                        iMediaControllerCallback.onEvent(string, bundle);
                        continue;
                    }
                    catch (RemoteException remoteException) {
                        // empty catch block
                    }
                }
                this.mExtraControllerCallbacks.finishBroadcast();
            }
            MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, string, bundle);
        }

        @Override
        public void setActive(boolean bl) {
            MediaSessionCompatApi21.setActive(this.mSessionObj, bl);
        }

        @Override
        public void setCallback(Callback callback, Handler handler) {
            Object object = this.mSessionObj;
            Object object2 = callback == null ? null : callback.mCallbackObj;
            MediaSessionCompatApi21.setCallback(object, object2, handler);
            if (callback != null) {
                callback.setSessionImpl(this, handler);
            }
        }

        @Override
        public void setCaptioningEnabled(boolean bl) {
            if (this.mCaptioningEnabled != bl) {
                this.mCaptioningEnabled = bl;
                for (int i = this.mExtraControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                    IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(i);
                    try {
                        iMediaControllerCallback.onCaptioningEnabledChanged(bl);
                        continue;
                    }
                    catch (RemoteException remoteException) {
                        // empty catch block
                    }
                }
                this.mExtraControllerCallbacks.finishBroadcast();
            }
        }

        @Override
        public void setCurrentControllerInfo(MediaSessionManager.RemoteUserInfo remoteUserInfo) {
        }

        @Override
        public void setExtras(Bundle bundle) {
            MediaSessionCompatApi21.setExtras(this.mSessionObj, bundle);
        }

        @Override
        public void setFlags(int n) {
            MediaSessionCompatApi21.setFlags(this.mSessionObj, n);
        }

        @Override
        public void setMediaButtonReceiver(PendingIntent pendingIntent) {
            MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, pendingIntent);
        }

        @Override
        public void setMetadata(MediaMetadataCompat object) {
            this.mMetadata = object;
            Object object2 = this.mSessionObj;
            object = object == null ? null : object.getMediaMetadata();
            MediaSessionCompatApi21.setMetadata(object2, object);
        }

        @Override
        public void setPlaybackState(PlaybackStateCompat object) {
            Object object2;
            this.mPlaybackState = object;
            for (int i = this.mExtraControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                object2 = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(i);
                try {
                    object2.onPlaybackStateChanged((PlaybackStateCompat)object);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mExtraControllerCallbacks.finishBroadcast();
            object2 = this.mSessionObj;
            object = object == null ? null : object.getPlaybackState();
            MediaSessionCompatApi21.setPlaybackState(object2, object);
        }

        @Override
        public void setPlaybackToLocal(int n) {
            MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, n);
        }

        @Override
        public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
            MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, volumeProviderCompat.getVolumeProvider());
        }

        @Override
        public void setQueue(List<QueueItem> object) {
            this.mQueue = object;
            ArrayList<Object> arrayList = null;
            if (object != null) {
                ArrayList<Object> arrayList2 = new ArrayList<Object>();
                object = object.iterator();
                do {
                    arrayList = arrayList2;
                    if (!object.hasNext()) break;
                    arrayList2.add(((QueueItem)object.next()).getQueueItem());
                } while (true);
            }
            MediaSessionCompatApi21.setQueue(this.mSessionObj, arrayList);
        }

        @Override
        public void setQueueTitle(CharSequence charSequence) {
            MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, charSequence);
        }

        @Override
        public void setRatingType(int n) {
            if (Build.VERSION.SDK_INT < 22) {
                this.mRatingType = n;
                return;
            }
            MediaSessionCompatApi22.setRatingType(this.mSessionObj, n);
        }

        @Override
        public void setRepeatMode(int n) {
            if (this.mRepeatMode != n) {
                this.mRepeatMode = n;
                for (int i = this.mExtraControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                    IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(i);
                    try {
                        iMediaControllerCallback.onRepeatModeChanged(n);
                        continue;
                    }
                    catch (RemoteException remoteException) {
                        // empty catch block
                    }
                }
                this.mExtraControllerCallbacks.finishBroadcast();
            }
        }

        @Override
        public void setSessionActivity(PendingIntent pendingIntent) {
            MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, pendingIntent);
        }

        @Override
        public void setShuffleMode(int n) {
            if (this.mShuffleMode != n) {
                this.mShuffleMode = n;
                for (int i = this.mExtraControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                    IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mExtraControllerCallbacks.getBroadcastItem(i);
                    try {
                        iMediaControllerCallback.onShuffleModeChanged(n);
                        continue;
                    }
                    catch (RemoteException remoteException) {
                        // empty catch block
                    }
                }
                this.mExtraControllerCallbacks.finishBroadcast();
            }
        }

        class ExtraSession
        extends IMediaSession.Stub {
            ExtraSession() {
            }

            @Override
            public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
                throw new AssertionError();
            }

            @Override
            public void addQueueItemAt(MediaDescriptionCompat mediaDescriptionCompat, int n) {
                throw new AssertionError();
            }

            @Override
            public void adjustVolume(int n, int n2, String string) {
                throw new AssertionError();
            }

            @Override
            public void fastForward() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public Bundle getExtras() {
                throw new AssertionError();
            }

            @Override
            public long getFlags() {
                throw new AssertionError();
            }

            @Override
            public PendingIntent getLaunchPendingIntent() {
                throw new AssertionError();
            }

            @Override
            public MediaMetadataCompat getMetadata() {
                throw new AssertionError();
            }

            @Override
            public String getPackageName() {
                throw new AssertionError();
            }

            @Override
            public PlaybackStateCompat getPlaybackState() {
                return MediaSessionCompat.getStateWithUpdatedPosition(MediaSessionImplApi21.this.mPlaybackState, MediaSessionImplApi21.this.mMetadata);
            }

            @Override
            public List<QueueItem> getQueue() {
                return null;
            }

            @Override
            public CharSequence getQueueTitle() {
                throw new AssertionError();
            }

            @Override
            public int getRatingType() {
                return MediaSessionImplApi21.this.mRatingType;
            }

            @Override
            public int getRepeatMode() {
                return MediaSessionImplApi21.this.mRepeatMode;
            }

            @Override
            public int getShuffleMode() {
                return MediaSessionImplApi21.this.mShuffleMode;
            }

            @Override
            public String getTag() {
                throw new AssertionError();
            }

            @Override
            public ParcelableVolumeInfo getVolumeAttributes() {
                throw new AssertionError();
            }

            @Override
            public boolean isCaptioningEnabled() {
                return MediaSessionImplApi21.this.mCaptioningEnabled;
            }

            @Override
            public boolean isShuffleModeEnabledRemoved() {
                return false;
            }

            @Override
            public boolean isTransportControlEnabled() {
                throw new AssertionError();
            }

            @Override
            public void next() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void pause() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void play() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void playFromMediaId(String string, Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void playFromSearch(String string, Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void playFromUri(Uri uri, Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void prepare() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void prepareFromMediaId(String string, Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void prepareFromSearch(String string, Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void prepareFromUri(Uri uri, Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void previous() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void rate(RatingCompat ratingCompat) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void rateWithExtras(RatingCompat ratingCompat, Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void registerCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                if (!MediaSessionImplApi21.this.mDestroyed) {
                    String string = MediaSessionImplApi21.this.getCallingPackage();
                    Object object = string;
                    if (string == null) {
                        object = "android.media.session.MediaController";
                    }
                    object = new MediaSessionManager.RemoteUserInfo((String)object, ExtraSession.getCallingPid(), ExtraSession.getCallingUid());
                    MediaSessionImplApi21.this.mExtraControllerCallbacks.register((IInterface)iMediaControllerCallback, object);
                }
            }

            @Override
            public void removeQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
                throw new AssertionError();
            }

            @Override
            public void removeQueueItemAt(int n) {
                throw new AssertionError();
            }

            @Override
            public void rewind() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void seekTo(long l) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void sendCommand(String string, Bundle bundle, ResultReceiverWrapper resultReceiverWrapper) {
                throw new AssertionError();
            }

            @Override
            public void sendCustomAction(String string, Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public boolean sendMediaButton(KeyEvent keyEvent) {
                throw new AssertionError();
            }

            @Override
            public void setCaptioningEnabled(boolean bl) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void setRepeatMode(int n) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void setShuffleMode(int n) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void setShuffleModeEnabledRemoved(boolean bl) throws RemoteException {
            }

            @Override
            public void setVolumeTo(int n, int n2, String string) {
                throw new AssertionError();
            }

            @Override
            public void skipToQueueItem(long l) {
                throw new AssertionError();
            }

            @Override
            public void stop() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void unregisterCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                MediaSessionImplApi21.this.mExtraControllerCallbacks.unregister((IInterface)iMediaControllerCallback);
            }
        }

    }

    static class MediaSessionImplApi28
    extends MediaSessionImplApi21 {
        MediaSessionImplApi28(Context context, String string, Bundle bundle) {
            super(context, string, bundle);
        }

        MediaSessionImplApi28(Object object) {
            super(object);
        }

        @Override
        public final MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            return new MediaSessionManager.RemoteUserInfo(((MediaSession)this.mSessionObj).getCurrentControllerInfo());
        }

        @Override
        public void setCurrentControllerInfo(MediaSessionManager.RemoteUserInfo remoteUserInfo) {
        }
    }

    static class MediaSessionImplBase
    implements MediaSessionImpl {
        static final int RCC_PLAYSTATE_NONE = 0;
        final AudioManager mAudioManager;
        volatile Callback mCallback;
        boolean mCaptioningEnabled;
        private final Context mContext;
        final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks = new RemoteCallbackList();
        boolean mDestroyed = false;
        Bundle mExtras;
        int mFlags;
        private MessageHandler mHandler;
        boolean mIsActive = false;
        private boolean mIsMbrRegistered = false;
        private boolean mIsRccRegistered = false;
        int mLocalStream;
        final Object mLock = new Object();
        private final ComponentName mMediaButtonReceiverComponentName;
        private final PendingIntent mMediaButtonReceiverIntent;
        MediaMetadataCompat mMetadata;
        final String mPackageName;
        List<QueueItem> mQueue;
        CharSequence mQueueTitle;
        int mRatingType;
        final RemoteControlClient mRcc;
        private MediaSessionManager.RemoteUserInfo mRemoteUserInfo;
        int mRepeatMode;
        PendingIntent mSessionActivity;
        int mShuffleMode;
        PlaybackStateCompat mState;
        private final MediaSessionStub mStub;
        final String mTag;
        private final Token mToken;
        private VolumeProviderCompat.Callback mVolumeCallback;
        VolumeProviderCompat mVolumeProvider;
        int mVolumeType;

        public MediaSessionImplBase(Context context, String string, ComponentName componentName, PendingIntent pendingIntent) {
            this.mVolumeCallback = new VolumeProviderCompat.Callback(){

                @Override
                public void onVolumeChanged(VolumeProviderCompat object) {
                    if (MediaSessionImplBase.this.mVolumeProvider != object) {
                        return;
                    }
                    object = new ParcelableVolumeInfo(MediaSessionImplBase.this.mVolumeType, MediaSessionImplBase.this.mLocalStream, object.getVolumeControl(), object.getMaxVolume(), object.getCurrentVolume());
                    MediaSessionImplBase.this.sendVolumeInfoChanged((ParcelableVolumeInfo)object);
                }
            };
            if (componentName != null) {
                this.mContext = context;
                this.mPackageName = context.getPackageName();
                this.mAudioManager = (AudioManager)context.getSystemService("audio");
                this.mTag = string;
                this.mMediaButtonReceiverComponentName = componentName;
                this.mMediaButtonReceiverIntent = pendingIntent;
                this.mStub = new MediaSessionStub();
                this.mToken = new Token(this.mStub);
                this.mRatingType = 0;
                this.mVolumeType = 1;
                this.mLocalStream = 3;
                this.mRcc = new RemoteControlClient(pendingIntent);
                return;
            }
            throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
        }

        private void sendCaptioningEnabled(boolean bl) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onCaptioningEnabledChanged(bl);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendEvent(String string, Bundle bundle) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onEvent(string, bundle);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendExtras(Bundle bundle) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onExtrasChanged(bundle);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendMetadata(MediaMetadataCompat mediaMetadataCompat) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onMetadataChanged(mediaMetadataCompat);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendQueue(List<QueueItem> list) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onQueueChanged(list);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendQueueTitle(CharSequence charSequence) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onQueueTitleChanged(charSequence);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendRepeatMode(int n) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onRepeatModeChanged(n);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendSessionDestroyed() {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onSessionDestroyed();
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
            this.mControllerCallbacks.kill();
        }

        private void sendShuffleMode(int n) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onShuffleModeChanged(n);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        private void sendState(PlaybackStateCompat playbackStateCompat) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onPlaybackStateChanged(playbackStateCompat);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        void adjustVolume(int n, int n2) {
            if (this.mVolumeType == 2) {
                VolumeProviderCompat volumeProviderCompat = this.mVolumeProvider;
                if (volumeProviderCompat != null) {
                    volumeProviderCompat.onAdjustVolume(n);
                    return;
                }
            } else {
                this.mAudioManager.adjustStreamVolume(this.mLocalStream, n, n2);
            }
        }

        RemoteControlClient.MetadataEditor buildRccMetadata(Bundle bundle) {
            RemoteControlClient.MetadataEditor metadataEditor = this.mRcc.editMetadata(true);
            if (bundle == null) {
                return metadataEditor;
            }
            if (bundle.containsKey("android.media.metadata.ART")) {
                Bitmap bitmap;
                Bitmap bitmap2 = bitmap = (Bitmap)bundle.getParcelable("android.media.metadata.ART");
                if (bitmap != null) {
                    bitmap2 = bitmap.copy(bitmap.getConfig(), false);
                }
                metadataEditor.putBitmap(100, bitmap2);
            } else if (bundle.containsKey("android.media.metadata.ALBUM_ART")) {
                Bitmap bitmap;
                Bitmap bitmap3 = bitmap = (Bitmap)bundle.getParcelable("android.media.metadata.ALBUM_ART");
                if (bitmap != null) {
                    bitmap3 = bitmap.copy(bitmap.getConfig(), false);
                }
                metadataEditor.putBitmap(100, bitmap3);
            }
            if (bundle.containsKey("android.media.metadata.ALBUM")) {
                metadataEditor.putString(1, bundle.getString("android.media.metadata.ALBUM"));
            }
            if (bundle.containsKey("android.media.metadata.ALBUM_ARTIST")) {
                metadataEditor.putString(13, bundle.getString("android.media.metadata.ALBUM_ARTIST"));
            }
            if (bundle.containsKey("android.media.metadata.ARTIST")) {
                metadataEditor.putString(2, bundle.getString("android.media.metadata.ARTIST"));
            }
            if (bundle.containsKey("android.media.metadata.AUTHOR")) {
                metadataEditor.putString(3, bundle.getString("android.media.metadata.AUTHOR"));
            }
            if (bundle.containsKey("android.media.metadata.COMPILATION")) {
                metadataEditor.putString(15, bundle.getString("android.media.metadata.COMPILATION"));
            }
            if (bundle.containsKey("android.media.metadata.COMPOSER")) {
                metadataEditor.putString(4, bundle.getString("android.media.metadata.COMPOSER"));
            }
            if (bundle.containsKey("android.media.metadata.DATE")) {
                metadataEditor.putString(5, bundle.getString("android.media.metadata.DATE"));
            }
            if (bundle.containsKey("android.media.metadata.DISC_NUMBER")) {
                metadataEditor.putLong(14, bundle.getLong("android.media.metadata.DISC_NUMBER"));
            }
            if (bundle.containsKey("android.media.metadata.DURATION")) {
                metadataEditor.putLong(9, bundle.getLong("android.media.metadata.DURATION"));
            }
            if (bundle.containsKey("android.media.metadata.GENRE")) {
                metadataEditor.putString(6, bundle.getString("android.media.metadata.GENRE"));
            }
            if (bundle.containsKey("android.media.metadata.TITLE")) {
                metadataEditor.putString(7, bundle.getString("android.media.metadata.TITLE"));
            }
            if (bundle.containsKey("android.media.metadata.TRACK_NUMBER")) {
                metadataEditor.putLong(0, bundle.getLong("android.media.metadata.TRACK_NUMBER"));
            }
            if (bundle.containsKey("android.media.metadata.WRITER")) {
                metadataEditor.putString(11, bundle.getString("android.media.metadata.WRITER"));
            }
            return metadataEditor;
        }

        @Override
        public String getCallingPackage() {
            return null;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
            Object object = this.mLock;
            synchronized (object) {
                return this.mRemoteUserInfo;
            }
        }

        @Override
        public Object getMediaSession() {
            return null;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public PlaybackStateCompat getPlaybackState() {
            Object object = this.mLock;
            synchronized (object) {
                return this.mState;
            }
        }

        int getRccStateFromState(int n) {
            switch (n) {
                default: {
                    return -1;
                }
                case 10: 
                case 11: {
                    return 6;
                }
                case 9: {
                    return 7;
                }
                case 7: {
                    return 9;
                }
                case 6: 
                case 8: {
                    return 8;
                }
                case 5: {
                    return 5;
                }
                case 4: {
                    return 4;
                }
                case 3: {
                    return 3;
                }
                case 2: {
                    return 2;
                }
                case 1: {
                    return 1;
                }
                case 0: 
            }
            return 0;
        }

        int getRccTransportControlFlagsFromActions(long l) {
            int n = 0;
            if ((1L & l) != 0L) {
                n = 0 | 32;
            }
            int n2 = n;
            if ((2L & l) != 0L) {
                n2 = n | 16;
            }
            n = n2;
            if ((4L & l) != 0L) {
                n = n2 | 4;
            }
            n2 = n;
            if ((8L & l) != 0L) {
                n2 = n | 2;
            }
            n = n2;
            if ((16L & l) != 0L) {
                n = n2 | 1;
            }
            n2 = n;
            if ((32L & l) != 0L) {
                n2 = n | 128;
            }
            n = n2;
            if ((64L & l) != 0L) {
                n = n2 | 64;
            }
            n2 = n;
            if ((512L & l) != 0L) {
                n2 = n | 8;
            }
            return n2;
        }

        @Override
        public Object getRemoteControlClient() {
            return null;
        }

        @Override
        public Token getSessionToken() {
            return this.mToken;
        }

        @Override
        public boolean isActive() {
            return this.mIsActive;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        void postToHandler(int n, int n2, int n3, Object object, Bundle bundle) {
            Object object2 = this.mLock;
            synchronized (object2) {
                if (this.mHandler != null) {
                    object = this.mHandler.obtainMessage(n, n2, n3, object);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("data_calling_pkg", "android.media.session.MediaController");
                    bundle2.putInt("data_calling_pid", Binder.getCallingPid());
                    bundle2.putInt("data_calling_uid", Binder.getCallingUid());
                    if (bundle != null) {
                        bundle2.putBundle("data_extras", bundle);
                    }
                    object.setData(bundle2);
                    object.sendToTarget();
                }
                return;
            }
        }

        void registerMediaButtonEventReceiver(PendingIntent pendingIntent, ComponentName componentName) {
            this.mAudioManager.registerMediaButtonEventReceiver(componentName);
        }

        @Override
        public void release() {
            this.mIsActive = false;
            this.mDestroyed = true;
            this.update();
            this.sendSessionDestroyed();
        }

        @Override
        public void sendSessionEvent(String string, Bundle bundle) {
            this.sendEvent(string, bundle);
        }

        void sendVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) {
            for (int i = this.mControllerCallbacks.beginBroadcast() - 1; i >= 0; --i) {
                IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
                try {
                    iMediaControllerCallback.onVolumeInfoChanged(parcelableVolumeInfo);
                    continue;
                }
                catch (RemoteException remoteException) {
                    // empty catch block
                }
            }
            this.mControllerCallbacks.finishBroadcast();
        }

        @Override
        public void setActive(boolean bl) {
            if (bl == this.mIsActive) {
                return;
            }
            this.mIsActive = bl;
            if (this.update()) {
                this.setMetadata(this.mMetadata);
                this.setPlaybackState(this.mState);
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setCallback(Callback callback, Handler object) {
            this.mCallback = callback;
            if (callback == null) {
                return;
            }
            callback = object == null ? new Handler() : object;
            object = this.mLock;
            synchronized (object) {
                if (this.mHandler != null) {
                    this.mHandler.removeCallbacksAndMessages((Object)null);
                }
                this.mHandler = new MessageHandler(callback.getLooper());
                this.mCallback.setSessionImpl(this, (Handler)callback);
                return;
            }
        }

        @Override
        public void setCaptioningEnabled(boolean bl) {
            if (this.mCaptioningEnabled != bl) {
                this.mCaptioningEnabled = bl;
                this.sendCaptioningEnabled(bl);
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setCurrentControllerInfo(MediaSessionManager.RemoteUserInfo remoteUserInfo) {
            Object object = this.mLock;
            synchronized (object) {
                this.mRemoteUserInfo = remoteUserInfo;
                return;
            }
        }

        @Override
        public void setExtras(Bundle bundle) {
            this.mExtras = bundle;
            this.sendExtras(bundle);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setFlags(int n) {
            Object object = this.mLock;
            synchronized (object) {
                this.mFlags = n;
            }
            this.update();
        }

        @Override
        public void setMediaButtonReceiver(PendingIntent pendingIntent) {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setMetadata(MediaMetadataCompat object) {
            MediaMetadataCompat mediaMetadataCompat = object;
            if (object != null) {
                mediaMetadataCompat = new MediaMetadataCompat.Builder((MediaMetadataCompat)object, MediaSessionCompat.sMaxBitmapSize).build();
            }
            object = this.mLock;
            synchronized (object) {
                this.mMetadata = mediaMetadataCompat;
            }
            this.sendMetadata(mediaMetadataCompat);
            if (!this.mIsActive) {
                return;
            }
            object = mediaMetadataCompat == null ? null : mediaMetadataCompat.getBundle();
            this.buildRccMetadata((Bundle)object).apply();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setPlaybackState(PlaybackStateCompat playbackStateCompat) {
            Object object = this.mLock;
            synchronized (object) {
                this.mState = playbackStateCompat;
            }
            this.sendState(playbackStateCompat);
            if (!this.mIsActive) {
                return;
            }
            if (playbackStateCompat == null) {
                this.mRcc.setPlaybackState(0);
                this.mRcc.setTransportControlFlags(0);
                return;
            }
            this.setRccState(playbackStateCompat);
            this.mRcc.setTransportControlFlags(this.getRccTransportControlFlagsFromActions(playbackStateCompat.getActions()));
        }

        @Override
        public void setPlaybackToLocal(int n) {
            VolumeProviderCompat volumeProviderCompat = this.mVolumeProvider;
            if (volumeProviderCompat != null) {
                volumeProviderCompat.setCallback(null);
            }
            this.mLocalStream = n;
            this.mVolumeType = 1;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(1, n, 2, this.mAudioManager.getStreamMaxVolume(n), this.mAudioManager.getStreamVolume(this.mLocalStream)));
        }

        @Override
        public void setPlaybackToRemote(VolumeProviderCompat volumeProviderCompat) {
            if (volumeProviderCompat != null) {
                VolumeProviderCompat volumeProviderCompat2 = this.mVolumeProvider;
                if (volumeProviderCompat2 != null) {
                    volumeProviderCompat2.setCallback(null);
                }
                this.mVolumeType = 2;
                this.mVolumeProvider = volumeProviderCompat;
                this.sendVolumeInfoChanged(new ParcelableVolumeInfo(2, this.mLocalStream, volumeProviderCompat.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
                volumeProviderCompat.setCallback(this.mVolumeCallback);
                return;
            }
            throw new IllegalArgumentException("volumeProvider may not be null");
        }

        @Override
        public void setQueue(List<QueueItem> list) {
            this.mQueue = list;
            this.sendQueue(list);
        }

        @Override
        public void setQueueTitle(CharSequence charSequence) {
            this.mQueueTitle = charSequence;
            this.sendQueueTitle(charSequence);
        }

        @Override
        public void setRatingType(int n) {
            this.mRatingType = n;
        }

        void setRccState(PlaybackStateCompat playbackStateCompat) {
            this.mRcc.setPlaybackState(this.getRccStateFromState(playbackStateCompat.getState()));
        }

        @Override
        public void setRepeatMode(int n) {
            if (this.mRepeatMode != n) {
                this.mRepeatMode = n;
                this.sendRepeatMode(n);
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void setSessionActivity(PendingIntent pendingIntent) {
            Object object = this.mLock;
            synchronized (object) {
                this.mSessionActivity = pendingIntent;
                return;
            }
        }

        @Override
        public void setShuffleMode(int n) {
            if (this.mShuffleMode != n) {
                this.mShuffleMode = n;
                this.sendShuffleMode(n);
            }
        }

        void setVolumeTo(int n, int n2) {
            if (this.mVolumeType == 2) {
                VolumeProviderCompat volumeProviderCompat = this.mVolumeProvider;
                if (volumeProviderCompat != null) {
                    volumeProviderCompat.onSetVolumeTo(n);
                    return;
                }
            } else {
                this.mAudioManager.setStreamVolume(this.mLocalStream, n, n2);
            }
        }

        void unregisterMediaButtonEventReceiver(PendingIntent pendingIntent, ComponentName componentName) {
            this.mAudioManager.unregisterMediaButtonEventReceiver(componentName);
        }

        boolean update() {
            if (this.mIsActive) {
                if (!this.mIsMbrRegistered && (this.mFlags & 1) != 0) {
                    this.registerMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
                    this.mIsMbrRegistered = true;
                } else if (this.mIsMbrRegistered && (this.mFlags & 1) == 0) {
                    this.unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
                    this.mIsMbrRegistered = false;
                }
                if (!this.mIsRccRegistered && (this.mFlags & 2) != 0) {
                    this.mAudioManager.registerRemoteControlClient(this.mRcc);
                    this.mIsRccRegistered = true;
                    return true;
                }
                if (this.mIsRccRegistered && (this.mFlags & 2) == 0) {
                    this.mRcc.setPlaybackState(0);
                    this.mAudioManager.unregisterRemoteControlClient(this.mRcc);
                    this.mIsRccRegistered = false;
                    return false;
                }
            } else {
                if (this.mIsMbrRegistered) {
                    this.unregisterMediaButtonEventReceiver(this.mMediaButtonReceiverIntent, this.mMediaButtonReceiverComponentName);
                    this.mIsMbrRegistered = false;
                }
                if (this.mIsRccRegistered) {
                    this.mRcc.setPlaybackState(0);
                    this.mAudioManager.unregisterRemoteControlClient(this.mRcc);
                    this.mIsRccRegistered = false;
                }
            }
            return false;
        }

        private static final class Command {
            public final String command;
            public final Bundle extras;
            public final ResultReceiver stub;

            public Command(String string, Bundle bundle, ResultReceiver resultReceiver) {
                this.command = string;
                this.extras = bundle;
                this.stub = resultReceiver;
            }
        }

        class MediaSessionStub
        extends IMediaSession.Stub {
            MediaSessionStub() {
            }

            @Override
            public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
                this.postToHandler(25, mediaDescriptionCompat);
            }

            @Override
            public void addQueueItemAt(MediaDescriptionCompat mediaDescriptionCompat, int n) {
                this.postToHandler(26, (Object)mediaDescriptionCompat, n);
            }

            @Override
            public void adjustVolume(int n, int n2, String string) {
                MediaSessionImplBase.this.adjustVolume(n, n2);
            }

            @Override
            public void fastForward() throws RemoteException {
                this.postToHandler(16);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public Bundle getExtras() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    return MediaSessionImplBase.this.mExtras;
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public long getFlags() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    return MediaSessionImplBase.this.mFlags;
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public PendingIntent getLaunchPendingIntent() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    return MediaSessionImplBase.this.mSessionActivity;
                }
            }

            @Override
            public MediaMetadataCompat getMetadata() {
                return MediaSessionImplBase.this.mMetadata;
            }

            @Override
            public String getPackageName() {
                return MediaSessionImplBase.this.mPackageName;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public PlaybackStateCompat getPlaybackState() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    MediaMetadataCompat mediaMetadataCompat;
                    PlaybackStateCompat playbackStateCompat = MediaSessionImplBase.this.mState;
                    try {
                        mediaMetadataCompat = MediaSessionImplBase.this.mMetadata;
                    }
                    catch (Throwable throwable) {
                        do {
                            void throwable2;
                            try {}
                            catch (Throwable throwable3) {
                                continue;
                            }
                            throw throwable2;
                            break;
                        } while (true);
                    }
                    return MediaSessionCompat.getStateWithUpdatedPosition(playbackStateCompat, mediaMetadataCompat);
                }
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public List<QueueItem> getQueue() {
                Object object = MediaSessionImplBase.this.mLock;
                synchronized (object) {
                    return MediaSessionImplBase.this.mQueue;
                }
            }

            @Override
            public CharSequence getQueueTitle() {
                return MediaSessionImplBase.this.mQueueTitle;
            }

            @Override
            public int getRatingType() {
                return MediaSessionImplBase.this.mRatingType;
            }

            @Override
            public int getRepeatMode() {
                return MediaSessionImplBase.this.mRepeatMode;
            }

            @Override
            public int getShuffleMode() {
                return MediaSessionImplBase.this.mShuffleMode;
            }

            @Override
            public String getTag() {
                return MediaSessionImplBase.this.mTag;
            }

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            @Override
            public ParcelableVolumeInfo getVolumeAttributes() {
                void var6_10;
                int n4;
                int n3;
                int n2;
                int n5;
                int n;
                block16 : {
                    Object object = MediaSessionImplBase.this.mLock;
                    // MONITORENTER : object
                    n2 = MediaSessionImplBase.this.mVolumeType;
                    n = MediaSessionImplBase.this.mLocalStream;
                    VolumeProviderCompat volumeProviderCompat = MediaSessionImplBase.this.mVolumeProvider;
                    if (n2 != 2) break block16;
                    n5 = volumeProviderCompat.getVolumeControl();
                    n4 = volumeProviderCompat.getMaxVolume();
                    try {
                        n3 = volumeProviderCompat.getCurrentVolume();
                        return new ParcelableVolumeInfo(n2, n, n5, n4, n3);
                    }
                    catch (Throwable throwable) {
                        throw var6_10;
                    }
                }
                try {
                    n4 = MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(n);
                }
                catch (Throwable throwable) {
                    throw var6_10;
                }
                n3 = MediaSessionImplBase.this.mAudioManager.getStreamVolume(n);
                n5 = 2;
                return new ParcelableVolumeInfo(n2, n, n5, n4, n3);
                catch (Throwable throwable) {
                    throw var6_10;
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
                throw var6_10;
            }

            @Override
            public boolean isCaptioningEnabled() {
                return MediaSessionImplBase.this.mCaptioningEnabled;
            }

            @Override
            public boolean isShuffleModeEnabledRemoved() {
                return false;
            }

            @Override
            public boolean isTransportControlEnabled() {
                if ((MediaSessionImplBase.this.mFlags & 2) != 0) {
                    return true;
                }
                return false;
            }

            @Override
            public void next() throws RemoteException {
                this.postToHandler(14);
            }

            @Override
            public void pause() throws RemoteException {
                this.postToHandler(12);
            }

            @Override
            public void play() throws RemoteException {
                this.postToHandler(7);
            }

            @Override
            public void playFromMediaId(String string, Bundle bundle) throws RemoteException {
                this.postToHandler(8, (Object)string, bundle);
            }

            @Override
            public void playFromSearch(String string, Bundle bundle) throws RemoteException {
                this.postToHandler(9, (Object)string, bundle);
            }

            @Override
            public void playFromUri(Uri uri, Bundle bundle) throws RemoteException {
                this.postToHandler(10, (Object)uri, bundle);
            }

            void postToHandler(int n) {
                MediaSessionImplBase.this.postToHandler(n, 0, 0, null, null);
            }

            void postToHandler(int n, int n2) {
                MediaSessionImplBase.this.postToHandler(n, n2, 0, null, null);
            }

            void postToHandler(int n, Object object) {
                MediaSessionImplBase.this.postToHandler(n, 0, 0, object, null);
            }

            void postToHandler(int n, Object object, int n2) {
                MediaSessionImplBase.this.postToHandler(n, n2, 0, object, null);
            }

            void postToHandler(int n, Object object, Bundle bundle) {
                MediaSessionImplBase.this.postToHandler(n, 0, 0, object, bundle);
            }

            @Override
            public void prepare() throws RemoteException {
                this.postToHandler(3);
            }

            @Override
            public void prepareFromMediaId(String string, Bundle bundle) throws RemoteException {
                this.postToHandler(4, (Object)string, bundle);
            }

            @Override
            public void prepareFromSearch(String string, Bundle bundle) throws RemoteException {
                this.postToHandler(5, (Object)string, bundle);
            }

            @Override
            public void prepareFromUri(Uri uri, Bundle bundle) throws RemoteException {
                this.postToHandler(6, (Object)uri, bundle);
            }

            @Override
            public void previous() throws RemoteException {
                this.postToHandler(15);
            }

            @Override
            public void rate(RatingCompat ratingCompat) throws RemoteException {
                this.postToHandler(19, ratingCompat);
            }

            @Override
            public void rateWithExtras(RatingCompat ratingCompat, Bundle bundle) throws RemoteException {
                this.postToHandler(31, (Object)ratingCompat, bundle);
            }

            @Override
            public void registerCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                if (MediaSessionImplBase.this.mDestroyed) {
                    try {
                        iMediaControllerCallback.onSessionDestroyed();
                        return;
                    }
                    catch (Exception exception) {
                        return;
                    }
                }
                MediaSessionManager.RemoteUserInfo remoteUserInfo = new MediaSessionManager.RemoteUserInfo("android.media.session.MediaController", MediaSessionStub.getCallingPid(), MediaSessionStub.getCallingUid());
                MediaSessionImplBase.this.mControllerCallbacks.register((IInterface)iMediaControllerCallback, (Object)remoteUserInfo);
            }

            @Override
            public void removeQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
                this.postToHandler(27, mediaDescriptionCompat);
            }

            @Override
            public void removeQueueItemAt(int n) {
                this.postToHandler(28, n);
            }

            @Override
            public void rewind() throws RemoteException {
                this.postToHandler(17);
            }

            @Override
            public void seekTo(long l) throws RemoteException {
                this.postToHandler(18, l);
            }

            @Override
            public void sendCommand(String string, Bundle bundle, ResultReceiverWrapper resultReceiverWrapper) {
                this.postToHandler(1, new Command(string, bundle, resultReceiverWrapper.mResultReceiver));
            }

            @Override
            public void sendCustomAction(String string, Bundle bundle) throws RemoteException {
                this.postToHandler(20, (Object)string, bundle);
            }

            @Override
            public boolean sendMediaButton(KeyEvent keyEvent) {
                int n = MediaSessionImplBase.this.mFlags;
                boolean bl = true;
                if ((n & 1) == 0) {
                    bl = false;
                }
                if (bl) {
                    this.postToHandler(21, (Object)keyEvent);
                }
                return bl;
            }

            @Override
            public void setCaptioningEnabled(boolean bl) throws RemoteException {
                this.postToHandler(29, bl);
            }

            @Override
            public void setRepeatMode(int n) throws RemoteException {
                this.postToHandler(23, n);
            }

            @Override
            public void setShuffleMode(int n) throws RemoteException {
                this.postToHandler(30, n);
            }

            @Override
            public void setShuffleModeEnabledRemoved(boolean bl) throws RemoteException {
            }

            @Override
            public void setVolumeTo(int n, int n2, String string) {
                MediaSessionImplBase.this.setVolumeTo(n, n2);
            }

            @Override
            public void skipToQueueItem(long l) {
                this.postToHandler(11, l);
            }

            @Override
            public void stop() throws RemoteException {
                this.postToHandler(13);
            }

            @Override
            public void unregisterCallbackListener(IMediaControllerCallback iMediaControllerCallback) {
                MediaSessionImplBase.this.mControllerCallbacks.unregister((IInterface)iMediaControllerCallback);
            }
        }

        class MessageHandler
        extends Handler {
            private static final int KEYCODE_MEDIA_PAUSE = 127;
            private static final int KEYCODE_MEDIA_PLAY = 126;
            private static final int MSG_ADD_QUEUE_ITEM = 25;
            private static final int MSG_ADD_QUEUE_ITEM_AT = 26;
            private static final int MSG_ADJUST_VOLUME = 2;
            private static final int MSG_COMMAND = 1;
            private static final int MSG_CUSTOM_ACTION = 20;
            private static final int MSG_FAST_FORWARD = 16;
            private static final int MSG_MEDIA_BUTTON = 21;
            private static final int MSG_NEXT = 14;
            private static final int MSG_PAUSE = 12;
            private static final int MSG_PLAY = 7;
            private static final int MSG_PLAY_MEDIA_ID = 8;
            private static final int MSG_PLAY_SEARCH = 9;
            private static final int MSG_PLAY_URI = 10;
            private static final int MSG_PREPARE = 3;
            private static final int MSG_PREPARE_MEDIA_ID = 4;
            private static final int MSG_PREPARE_SEARCH = 5;
            private static final int MSG_PREPARE_URI = 6;
            private static final int MSG_PREVIOUS = 15;
            private static final int MSG_RATE = 19;
            private static final int MSG_RATE_EXTRA = 31;
            private static final int MSG_REMOVE_QUEUE_ITEM = 27;
            private static final int MSG_REMOVE_QUEUE_ITEM_AT = 28;
            private static final int MSG_REWIND = 17;
            private static final int MSG_SEEK_TO = 18;
            private static final int MSG_SET_CAPTIONING_ENABLED = 29;
            private static final int MSG_SET_REPEAT_MODE = 23;
            private static final int MSG_SET_SHUFFLE_MODE = 30;
            private static final int MSG_SET_VOLUME = 22;
            private static final int MSG_SKIP_TO_ITEM = 11;
            private static final int MSG_STOP = 13;

            public MessageHandler(Looper looper) {
                super(looper);
            }

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             */
            private void onMediaButtonEvent(KeyEvent var1_1, Callback var2_2) {
                if (var1_1 == null) {
                    return;
                }
                if (var1_1.getAction() != 0) {
                    return;
                }
                var4_3 = MediaSessionImplBase.this.mState == null ? 0L : MediaSessionImplBase.this.mState.getActions();
                var3_4 = var1_1.getKeyCode();
                if (var3_4 == 79) ** GOTO lbl-1000
                if (var3_4 == 126) ** GOTO lbl36
                if (var3_4 == 127) ** GOTO lbl33
                switch (var3_4) {
                    default: {
                        return;
                    }
                    case 90: {
                        if ((64L & var4_3) == 0L) break;
                        var2_2.onFastForward();
                        return;
                    }
                    case 89: {
                        if ((8L & var4_3) == 0L) break;
                        var2_2.onRewind();
                        return;
                    }
                    case 88: {
                        if ((16L & var4_3) == 0L) break;
                        var2_2.onSkipToPrevious();
                        return;
                    }
                    case 87: {
                        if ((32L & var4_3) == 0L) break;
                        var2_2.onSkipToNext();
                        return;
                    }
                    case 86: {
                        if ((1L & var4_3) == 0L) break;
                        var2_2.onStop();
                        return;
                    }
lbl33: // 1 sources:
                    if ((2L & var4_3) == 0L) break;
                    var2_2.onPause();
                    return;
lbl36: // 1 sources:
                    if ((4L & var4_3) == 0L) break;
                    var2_2.onPlay();
                    return;
                    case 85: lbl-1000: // 2 sources:
                    {
                        Log.w((String)"MediaSessionCompat", (String)"KEYCODE_MEDIA_PLAY_PAUSE and KEYCODE_HEADSETHOOK are handled already");
                    }
                }
            }

            /*
             * Exception decompiling
             */
            public void handleMessage(Message var1_1) {
                // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
                // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 1[SWITCH]
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:420)
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:472)
                // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2880)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:838)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
                // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
                // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
                // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
                // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
                // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:682)
                // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:765)
                // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:682)
                // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:765)
                // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
                // org.benf.cfr.reader.Main.doJar(Main.java:134)
                // org.benf.cfr.reader.Main.main(Main.java:189)
                throw new IllegalStateException("Decompilation failed");
            }
        }

    }

    public static interface OnActiveChangeListener {
        public void onActiveChanged();
    }

    public static final class QueueItem
    implements Parcelable {
        public static final Parcelable.Creator<QueueItem> CREATOR = new Parcelable.Creator<QueueItem>(){

            public QueueItem createFromParcel(Parcel parcel) {
                return new QueueItem(parcel);
            }

            public QueueItem[] newArray(int n) {
                return new QueueItem[n];
            }
        };
        public static final int UNKNOWN_ID = -1;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private Object mItem;

        QueueItem(Parcel parcel) {
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.mId = parcel.readLong();
        }

        public QueueItem(MediaDescriptionCompat mediaDescriptionCompat, long l) {
            this(null, mediaDescriptionCompat, l);
        }

        private QueueItem(Object object, MediaDescriptionCompat mediaDescriptionCompat, long l) {
            if (mediaDescriptionCompat != null) {
                if (l != -1L) {
                    this.mDescription = mediaDescriptionCompat;
                    this.mId = l;
                    this.mItem = object;
                    return;
                }
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            throw new IllegalArgumentException("Description cannot be null.");
        }

        public static QueueItem fromQueueItem(Object object) {
            if (object != null && Build.VERSION.SDK_INT >= 21) {
                return new QueueItem(object, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(object)), MediaSessionCompatApi21.QueueItem.getQueueId(object));
            }
            return null;
        }

        public static List<QueueItem> fromQueueItemList(List<?> object) {
            if (object != null && Build.VERSION.SDK_INT >= 21) {
                ArrayList<QueueItem> arrayList = new ArrayList<QueueItem>();
                object = object.iterator();
                while (object.hasNext()) {
                    arrayList.add(QueueItem.fromQueueItem(object.next()));
                }
                return arrayList;
            }
            return null;
        }

        public int describeContents() {
            return 0;
        }

        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }

        public long getQueueId() {
            return this.mId;
        }

        public Object getQueueItem() {
            if (this.mItem == null && Build.VERSION.SDK_INT >= 21) {
                Object object;
                this.mItem = object = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
                return object;
            }
            return this.mItem;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MediaSession.QueueItem {Description=");
            stringBuilder.append(this.mDescription);
            stringBuilder.append(", Id=");
            stringBuilder.append(this.mId);
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel parcel, int n) {
            this.mDescription.writeToParcel(parcel, n);
            parcel.writeLong(this.mId);
        }

    }

    public static final class ResultReceiverWrapper
    implements Parcelable {
        public static final Parcelable.Creator<ResultReceiverWrapper> CREATOR = new Parcelable.Creator<ResultReceiverWrapper>(){

            public ResultReceiverWrapper createFromParcel(Parcel parcel) {
                return new ResultReceiverWrapper(parcel);
            }

            public ResultReceiverWrapper[] newArray(int n) {
                return new ResultReceiverWrapper[n];
            }
        };
        ResultReceiver mResultReceiver;

        ResultReceiverWrapper(Parcel parcel) {
            this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
        }

        public ResultReceiverWrapper(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int n) {
            this.mResultReceiver.writeToParcel(parcel, n);
        }

    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface SessionFlags {
    }

    public static final class Token
    implements Parcelable {
        public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>(){

            public Token createFromParcel(Parcel parcel) {
                parcel = Build.VERSION.SDK_INT >= 21 ? parcel.readParcelable(null) : parcel.readStrongBinder();
                return new Token((Object)parcel);
            }

            public Token[] newArray(int n) {
                return new Token[n];
            }
        };
        private IMediaSession mExtraBinder;
        private final Object mInner;
        private Bundle mSessionToken2Bundle;

        Token(Object object) {
            this(object, null, null);
        }

        Token(Object object, IMediaSession iMediaSession) {
            this(object, iMediaSession, null);
        }

        Token(Object object, IMediaSession iMediaSession, Bundle bundle) {
            this.mInner = object;
            this.mExtraBinder = iMediaSession;
            this.mSessionToken2Bundle = bundle;
        }

        public static Token fromBundle(Bundle object) {
            if (object == null) {
                return null;
            }
            IMediaSession iMediaSession = IMediaSession.Stub.asInterface(BundleCompat.getBinder((Bundle)object, "android.support.v4.media.session.EXTRA_BINDER"));
            Bundle bundle = object.getBundle("android.support.v4.media.session.SESSION_TOKEN2_BUNDLE");
            if ((object = (Token)object.getParcelable("android.support.v4.media.session.TOKEN")) == null) {
                return null;
            }
            return new Token(object.mInner, iMediaSession, bundle);
        }

        public static Token fromToken(Object object) {
            return Token.fromToken(object, null);
        }

        public static Token fromToken(Object object, IMediaSession iMediaSession) {
            if (object != null && Build.VERSION.SDK_INT >= 21) {
                return new Token(MediaSessionCompatApi21.verifyToken(object), iMediaSession);
            }
            return null;
        }

        public int describeContents() {
            return 0;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Token)) {
                return false;
            }
            Object object2 = (Token)object;
            object = this.mInner;
            if (object == null) {
                if (object2.mInner == null) {
                    return true;
                }
                return false;
            }
            object2 = object2.mInner;
            if (object2 == null) {
                return false;
            }
            return object.equals(object2);
        }

        public IMediaSession getExtraBinder() {
            return this.mExtraBinder;
        }

        public Bundle getSessionToken2Bundle() {
            return this.mSessionToken2Bundle;
        }

        public Object getToken() {
            return this.mInner;
        }

        public int hashCode() {
            Object object = this.mInner;
            if (object == null) {
                return 0;
            }
            return object.hashCode();
        }

        public void setExtraBinder(IMediaSession iMediaSession) {
            this.mExtraBinder = iMediaSession;
        }

        public void setSessionToken2Bundle(Bundle bundle) {
            this.mSessionToken2Bundle = bundle;
        }

        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("android.support.v4.media.session.TOKEN", (Parcelable)this);
            IMediaSession iMediaSession = this.mExtraBinder;
            if (iMediaSession != null) {
                BundleCompat.putBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER", iMediaSession.asBinder());
            }
            if ((iMediaSession = this.mSessionToken2Bundle) != null) {
                bundle.putBundle("android.support.v4.media.session.SESSION_TOKEN2_BUNDLE", (Bundle)iMediaSession);
            }
            return bundle;
        }

        public void writeToParcel(Parcel parcel, int n) {
            if (Build.VERSION.SDK_INT >= 21) {
                parcel.writeParcelable((Parcelable)this.mInner, n);
                return;
            }
            parcel.writeStrongBinder((IBinder)this.mInner);
        }

    }

}

