/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.Looper
 *  android.os.Message
 *  android.os.Parcelable
 *  android.os.RemoteException
 *  android.os.ResultReceiver
 *  android.text.TextUtils
 *  android.util.Log
 *  android.view.KeyEvent
 */
package android.support.v4.media.session;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.media.session.MediaControllerCompatApi21;
import android.support.v4.media.session.MediaControllerCompatApi23;
import android.support.v4.media.session.MediaControllerCompatApi24;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.ParcelableVolumeInfo;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import androidx.core.app.BundleCompat;
import androidx.core.app.ComponentActivity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class MediaControllerCompat {
    public static final String COMMAND_ADD_QUEUE_ITEM = "android.support.v4.media.session.command.ADD_QUEUE_ITEM";
    public static final String COMMAND_ADD_QUEUE_ITEM_AT = "android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT";
    public static final String COMMAND_ARGUMENT_INDEX = "android.support.v4.media.session.command.ARGUMENT_INDEX";
    public static final String COMMAND_ARGUMENT_MEDIA_DESCRIPTION = "android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION";
    public static final String COMMAND_GET_EXTRA_BINDER = "android.support.v4.media.session.command.GET_EXTRA_BINDER";
    public static final String COMMAND_REMOVE_QUEUE_ITEM = "android.support.v4.media.session.command.REMOVE_QUEUE_ITEM";
    public static final String COMMAND_REMOVE_QUEUE_ITEM_AT = "android.support.v4.media.session.command.REMOVE_QUEUE_ITEM_AT";
    static final String TAG = "MediaControllerCompat";
    private final MediaControllerImpl mImpl;
    private final HashSet<Callback> mRegisteredCallbacks = new HashSet();
    private final MediaSessionCompat.Token mToken;

    public MediaControllerCompat(Context context, MediaSessionCompat.Token token) throws RemoteException {
        if (token != null) {
            this.mToken = token;
            if (Build.VERSION.SDK_INT >= 24) {
                this.mImpl = new MediaControllerImplApi24(context, token);
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                this.mImpl = new MediaControllerImplApi23(context, token);
                return;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                this.mImpl = new MediaControllerImplApi21(context, token);
                return;
            }
            this.mImpl = new MediaControllerImplBase(token);
            return;
        }
        throw new IllegalArgumentException("sessionToken must not be null");
    }

    public MediaControllerCompat(Context object, MediaSessionCompat mediaSessionCompat) {
        if (mediaSessionCompat != null) {
            this.mToken = mediaSessionCompat.getSessionToken();
            mediaSessionCompat = null;
            try {
                object = Build.VERSION.SDK_INT >= 24 ? new MediaControllerImplApi24((Context)object, this.mToken) : (Build.VERSION.SDK_INT >= 23 ? new MediaControllerImplApi23((Context)object, this.mToken) : (Build.VERSION.SDK_INT >= 21 ? new MediaControllerImplApi21((Context)object, this.mToken) : new MediaControllerImplBase(this.mToken)));
            }
            catch (RemoteException remoteException) {
                Log.w((String)"MediaControllerCompat", (String)"Failed to create MediaControllerImpl.", (Throwable)remoteException);
                object = mediaSessionCompat;
            }
            this.mImpl = object;
            return;
        }
        throw new IllegalArgumentException("session must not be null");
    }

    public static MediaControllerCompat getMediaController(Activity object) {
        boolean bl = object instanceof ComponentActivity;
        Object object2 = null;
        if (bl) {
            MediaControllerExtraData mediaControllerExtraData = ((ComponentActivity)object).getExtraData(MediaControllerExtraData.class);
            object = object2;
            if (mediaControllerExtraData != null) {
                object = mediaControllerExtraData.getMediaController();
            }
            return object;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            object2 = MediaControllerCompatApi21.getMediaController((Activity)object);
            if (object2 == null) {
                return null;
            }
            object2 = MediaControllerCompatApi21.getSessionToken(object2);
            try {
                object = new MediaControllerCompat((Context)object, MediaSessionCompat.Token.fromToken(object2));
                return object;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getMediaController.", (Throwable)remoteException);
            }
        }
        return null;
    }

    public static void setMediaController(Activity activity, MediaControllerCompat mediaControllerCompat) {
        if (activity instanceof ComponentActivity) {
            ((ComponentActivity)activity).putExtraData(new MediaControllerExtraData(mediaControllerCompat));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Object object = null;
            if (mediaControllerCompat != null) {
                object = MediaControllerCompatApi21.fromToken((Context)activity, mediaControllerCompat.getSessionToken().getToken());
            }
            MediaControllerCompatApi21.setMediaController(activity, object);
        }
    }

    static void validateCustomAction(String string, Bundle object) {
        if (string == null) {
            return;
        }
        int n = -1;
        int n2 = string.hashCode();
        if (n2 != -1348483723) {
            if (n2 == 503011406 && string.equals("android.support.v4.media.session.action.UNFOLLOW")) {
                n = 1;
            }
        } else if (string.equals("android.support.v4.media.session.action.FOLLOW")) {
            n = 0;
        }
        if (n != 0 && n != 1) {
            return;
        }
        if (object != null && object.containsKey("android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE")) {
            return;
        }
        object = new StringBuilder();
        object.append("An extra field android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE is required for this action ");
        object.append(string);
        object.append(".");
        throw new IllegalArgumentException(object.toString());
    }

    public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
        this.mImpl.addQueueItem(mediaDescriptionCompat);
    }

    public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat, int n) {
        this.mImpl.addQueueItem(mediaDescriptionCompat, n);
    }

    public void adjustVolume(int n, int n2) {
        this.mImpl.adjustVolume(n, n2);
    }

    public boolean dispatchMediaButtonEvent(KeyEvent keyEvent) {
        if (keyEvent != null) {
            return this.mImpl.dispatchMediaButtonEvent(keyEvent);
        }
        throw new IllegalArgumentException("KeyEvent may not be null");
    }

    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }

    public long getFlags() {
        return this.mImpl.getFlags();
    }

    public Object getMediaController() {
        return this.mImpl.getMediaController();
    }

    public MediaMetadataCompat getMetadata() {
        return this.mImpl.getMetadata();
    }

    public String getPackageName() {
        return this.mImpl.getPackageName();
    }

    public PlaybackInfo getPlaybackInfo() {
        return this.mImpl.getPlaybackInfo();
    }

    public PlaybackStateCompat getPlaybackState() {
        return this.mImpl.getPlaybackState();
    }

    public List<MediaSessionCompat.QueueItem> getQueue() {
        return this.mImpl.getQueue();
    }

    public CharSequence getQueueTitle() {
        return this.mImpl.getQueueTitle();
    }

    public int getRatingType() {
        return this.mImpl.getRatingType();
    }

    public int getRepeatMode() {
        return this.mImpl.getRepeatMode();
    }

    public PendingIntent getSessionActivity() {
        return this.mImpl.getSessionActivity();
    }

    public MediaSessionCompat.Token getSessionToken() {
        return this.mToken;
    }

    public Bundle getSessionToken2Bundle() {
        return this.mToken.getSessionToken2Bundle();
    }

    public int getShuffleMode() {
        return this.mImpl.getShuffleMode();
    }

    public TransportControls getTransportControls() {
        return this.mImpl.getTransportControls();
    }

    public boolean isCaptioningEnabled() {
        return this.mImpl.isCaptioningEnabled();
    }

    public boolean isSessionReady() {
        return this.mImpl.isSessionReady();
    }

    public void registerCallback(Callback callback) {
        this.registerCallback(callback, null);
    }

    public void registerCallback(Callback callback, Handler handler) {
        if (callback != null) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler();
            }
            callback.setHandler(handler2);
            this.mImpl.registerCallback(callback, handler2);
            this.mRegisteredCallbacks.add(callback);
            return;
        }
        throw new IllegalArgumentException("callback must not be null");
    }

    public void removeQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
        this.mImpl.removeQueueItem(mediaDescriptionCompat);
    }

    @Deprecated
    public void removeQueueItemAt(int n) {
        List<MediaSessionCompat.QueueItem> list = this.getQueue();
        if (list != null && n >= 0 && n < list.size() && (list = list.get(n)) != null) {
            this.removeQueueItem(list.getDescription());
        }
    }

    public void sendCommand(String string, Bundle bundle, ResultReceiver resultReceiver) {
        if (!TextUtils.isEmpty((CharSequence)string)) {
            this.mImpl.sendCommand(string, bundle, resultReceiver);
            return;
        }
        throw new IllegalArgumentException("command must neither be null nor empty");
    }

    public void setVolumeTo(int n, int n2) {
        this.mImpl.setVolumeTo(n, n2);
    }

    public void unregisterCallback(Callback callback) {
        if (callback != null) {
            try {
                this.mRegisteredCallbacks.remove(callback);
                this.mImpl.unregisterCallback(callback);
                return;
            }
            finally {
                callback.setHandler(null);
            }
        }
        throw new IllegalArgumentException("callback must not be null");
    }

    public static abstract class Callback
    implements IBinder.DeathRecipient {
        final Object mCallbackObj;
        MessageHandler mHandler;
        IMediaControllerCallback mIControllerCallback;

        public Callback() {
            if (Build.VERSION.SDK_INT >= 21) {
                this.mCallbackObj = MediaControllerCompatApi21.createCallback(new StubApi21(this));
                return;
            }
            StubCompat stubCompat = new StubCompat(this);
            this.mIControllerCallback = stubCompat;
            this.mCallbackObj = stubCompat;
        }

        public void binderDied() {
            this.postToHandler(8, null, null);
        }

        public IMediaControllerCallback getIControllerCallback() {
            return this.mIControllerCallback;
        }

        public void onAudioInfoChanged(PlaybackInfo playbackInfo) {
        }

        public void onCaptioningEnabledChanged(boolean bl) {
        }

        public void onExtrasChanged(Bundle bundle) {
        }

        public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) {
        }

        public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) {
        }

        public void onQueueChanged(List<MediaSessionCompat.QueueItem> list) {
        }

        public void onQueueTitleChanged(CharSequence charSequence) {
        }

        public void onRepeatModeChanged(int n) {
        }

        public void onSessionDestroyed() {
        }

        public void onSessionEvent(String string, Bundle bundle) {
        }

        public void onSessionReady() {
        }

        public void onShuffleModeChanged(int n) {
        }

        void postToHandler(int n, Object object, Bundle bundle) {
            MessageHandler messageHandler = this.mHandler;
            if (messageHandler != null) {
                object = messageHandler.obtainMessage(n, object);
                object.setData(bundle);
                object.sendToTarget();
            }
        }

        void setHandler(Handler handler) {
            if (handler == null) {
                handler = this.mHandler;
                if (handler != null) {
                    handler.mRegistered = false;
                    this.mHandler.removeCallbacksAndMessages((Object)null);
                    this.mHandler = null;
                    return;
                }
            } else {
                handler = new MessageHandler(handler.getLooper());
                this.mHandler = handler;
                handler.mRegistered = true;
            }
        }

        private class MessageHandler
        extends Handler {
            private static final int MSG_DESTROYED = 8;
            private static final int MSG_EVENT = 1;
            private static final int MSG_SESSION_READY = 13;
            private static final int MSG_UPDATE_CAPTIONING_ENABLED = 11;
            private static final int MSG_UPDATE_EXTRAS = 7;
            private static final int MSG_UPDATE_METADATA = 3;
            private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
            private static final int MSG_UPDATE_QUEUE = 5;
            private static final int MSG_UPDATE_QUEUE_TITLE = 6;
            private static final int MSG_UPDATE_REPEAT_MODE = 9;
            private static final int MSG_UPDATE_SHUFFLE_MODE = 12;
            private static final int MSG_UPDATE_VOLUME = 4;
            boolean mRegistered;

            MessageHandler(Looper looper) {
                super(looper);
                this.mRegistered = false;
            }

            public void handleMessage(Message message) {
                if (!this.mRegistered) {
                    return;
                }
                switch (message.what) {
                    default: {
                        return;
                    }
                    case 13: {
                        Callback.this.onSessionReady();
                        return;
                    }
                    case 12: {
                        Callback.this.onShuffleModeChanged((Integer)message.obj);
                        return;
                    }
                    case 11: {
                        Callback.this.onCaptioningEnabledChanged((Boolean)message.obj);
                        return;
                    }
                    case 9: {
                        Callback.this.onRepeatModeChanged((Integer)message.obj);
                        return;
                    }
                    case 8: {
                        Callback.this.onSessionDestroyed();
                        return;
                    }
                    case 7: {
                        message = (Bundle)message.obj;
                        MediaSessionCompat.ensureClassLoader((Bundle)message);
                        Callback.this.onExtrasChanged((Bundle)message);
                        return;
                    }
                    case 6: {
                        Callback.this.onQueueTitleChanged((CharSequence)message.obj);
                        return;
                    }
                    case 5: {
                        Callback.this.onQueueChanged((List)message.obj);
                        return;
                    }
                    case 4: {
                        Callback.this.onAudioInfoChanged((PlaybackInfo)message.obj);
                        return;
                    }
                    case 3: {
                        Callback.this.onMetadataChanged((MediaMetadataCompat)message.obj);
                        return;
                    }
                    case 2: {
                        Callback.this.onPlaybackStateChanged((PlaybackStateCompat)message.obj);
                        return;
                    }
                    case 1: 
                }
                Bundle bundle = message.getData();
                MediaSessionCompat.ensureClassLoader(bundle);
                Callback.this.onSessionEvent((String)message.obj, bundle);
            }
        }

        private static class StubApi21
        implements MediaControllerCompatApi21.Callback {
            private final WeakReference<Callback> mCallback;

            StubApi21(Callback callback) {
                this.mCallback = new WeakReference<Callback>(callback);
            }

            @Override
            public void onAudioInfoChanged(int n, int n2, int n3, int n4, int n5) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onAudioInfoChanged(new PlaybackInfo(n, n2, n3, n4, n5));
                }
            }

            @Override
            public void onExtrasChanged(Bundle bundle) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onExtrasChanged(bundle);
                }
            }

            @Override
            public void onMetadataChanged(Object object) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(object));
                }
            }

            @Override
            public void onPlaybackStateChanged(Object object) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    if (callback.mIControllerCallback != null) {
                        return;
                    }
                    callback.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(object));
                }
            }

            @Override
            public void onQueueChanged(List<?> list) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(list));
                }
            }

            @Override
            public void onQueueTitleChanged(CharSequence charSequence) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onQueueTitleChanged(charSequence);
                }
            }

            @Override
            public void onSessionDestroyed() {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.onSessionDestroyed();
                }
            }

            @Override
            public void onSessionEvent(String string, Bundle bundle) {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    if (callback.mIControllerCallback != null && Build.VERSION.SDK_INT < 23) {
                        return;
                    }
                    callback.onSessionEvent(string, bundle);
                }
            }
        }

        private static class StubCompat
        extends IMediaControllerCallback.Stub {
            private final WeakReference<Callback> mCallback;

            StubCompat(Callback callback) {
                this.mCallback = new WeakReference<Callback>(callback);
            }

            @Override
            public void onCaptioningEnabledChanged(boolean bl) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(11, bl, null);
                }
            }

            @Override
            public void onEvent(String string, Bundle bundle) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(1, string, bundle);
                }
            }

            @Override
            public void onExtrasChanged(Bundle bundle) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(7, (Object)bundle, null);
                }
            }

            @Override
            public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(3, mediaMetadataCompat, null);
                }
            }

            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(2, playbackStateCompat, null);
                }
            }

            @Override
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> list) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(5, list, null);
                }
            }

            @Override
            public void onQueueTitleChanged(CharSequence charSequence) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(6, charSequence, null);
                }
            }

            @Override
            public void onRepeatModeChanged(int n) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(9, n, null);
                }
            }

            @Override
            public void onSessionDestroyed() throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(8, null, null);
                }
            }

            @Override
            public void onSessionReady() throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(13, null, null);
                }
            }

            @Override
            public void onShuffleModeChanged(int n) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    callback.postToHandler(12, n, null);
                }
            }

            @Override
            public void onShuffleModeChangedRemoved(boolean bl) throws RemoteException {
            }

            @Override
            public void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                Callback callback = this.mCallback.get();
                if (callback != null) {
                    PlaybackInfo playbackInfo = null;
                    if (parcelableVolumeInfo != null) {
                        playbackInfo = new PlaybackInfo(parcelableVolumeInfo.volumeType, parcelableVolumeInfo.audioStream, parcelableVolumeInfo.controlType, parcelableVolumeInfo.maxVolume, parcelableVolumeInfo.currentVolume);
                    }
                    callback.postToHandler(4, playbackInfo, null);
                }
            }
        }

    }

    private static class MediaControllerExtraData
    extends ComponentActivity.ExtraData {
        private final MediaControllerCompat mMediaController;

        MediaControllerExtraData(MediaControllerCompat mediaControllerCompat) {
            this.mMediaController = mediaControllerCompat;
        }

        MediaControllerCompat getMediaController() {
            return this.mMediaController;
        }
    }

    static interface MediaControllerImpl {
        public void addQueueItem(MediaDescriptionCompat var1);

        public void addQueueItem(MediaDescriptionCompat var1, int var2);

        public void adjustVolume(int var1, int var2);

        public boolean dispatchMediaButtonEvent(KeyEvent var1);

        public Bundle getExtras();

        public long getFlags();

        public Object getMediaController();

        public MediaMetadataCompat getMetadata();

        public String getPackageName();

        public PlaybackInfo getPlaybackInfo();

        public PlaybackStateCompat getPlaybackState();

        public List<MediaSessionCompat.QueueItem> getQueue();

        public CharSequence getQueueTitle();

        public int getRatingType();

        public int getRepeatMode();

        public PendingIntent getSessionActivity();

        public int getShuffleMode();

        public TransportControls getTransportControls();

        public boolean isCaptioningEnabled();

        public boolean isSessionReady();

        public void registerCallback(Callback var1, Handler var2);

        public void removeQueueItem(MediaDescriptionCompat var1);

        public void sendCommand(String var1, Bundle var2, ResultReceiver var3);

        public void setVolumeTo(int var1, int var2);

        public void unregisterCallback(Callback var1);
    }

    static class MediaControllerImplApi21
    implements MediaControllerImpl {
        private HashMap<Callback, ExtraCallback> mCallbackMap = new HashMap();
        protected final Object mControllerObj;
        final Object mLock = new Object();
        private final List<Callback> mPendingCallbacks = new ArrayList<Callback>();
        final MediaSessionCompat.Token mSessionToken;

        public MediaControllerImplApi21(Context object, MediaSessionCompat.Token token) throws RemoteException {
            this.mSessionToken = token;
            this.mControllerObj = object = MediaControllerCompatApi21.fromToken((Context)object, token.getToken());
            if (object != null) {
                if (this.mSessionToken.getExtraBinder() == null) {
                    this.requestExtraBinder();
                }
                return;
            }
            throw new RemoteException();
        }

        private void requestExtraBinder() {
            this.sendCommand("android.support.v4.media.session.command.GET_EXTRA_BINDER", null, new ExtraBinderRequestResultReceiver(this));
        }

        @Override
        public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
            if ((4L & this.getFlags()) != 0L) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", (Parcelable)mediaDescriptionCompat);
                this.sendCommand("android.support.v4.media.session.command.ADD_QUEUE_ITEM", bundle, null);
                return;
            }
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
        }

        @Override
        public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat, int n) {
            if ((4L & this.getFlags()) != 0L) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", (Parcelable)mediaDescriptionCompat);
                bundle.putInt("android.support.v4.media.session.command.ARGUMENT_INDEX", n);
                this.sendCommand("android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT", bundle, null);
                return;
            }
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
        }

        @Override
        public void adjustVolume(int n, int n2) {
            MediaControllerCompatApi21.adjustVolume(this.mControllerObj, n, n2);
        }

        @Override
        public boolean dispatchMediaButtonEvent(KeyEvent keyEvent) {
            return MediaControllerCompatApi21.dispatchMediaButtonEvent(this.mControllerObj, keyEvent);
        }

        @Override
        public Bundle getExtras() {
            return MediaControllerCompatApi21.getExtras(this.mControllerObj);
        }

        @Override
        public long getFlags() {
            return MediaControllerCompatApi21.getFlags(this.mControllerObj);
        }

        @Override
        public Object getMediaController() {
            return this.mControllerObj;
        }

        @Override
        public MediaMetadataCompat getMetadata() {
            Object object = MediaControllerCompatApi21.getMetadata(this.mControllerObj);
            if (object != null) {
                return MediaMetadataCompat.fromMediaMetadata(object);
            }
            return null;
        }

        @Override
        public String getPackageName() {
            return MediaControllerCompatApi21.getPackageName(this.mControllerObj);
        }

        @Override
        public PlaybackInfo getPlaybackInfo() {
            Object object = MediaControllerCompatApi21.getPlaybackInfo(this.mControllerObj);
            if (object != null) {
                return new PlaybackInfo(MediaControllerCompatApi21.PlaybackInfo.getPlaybackType(object), MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(object), MediaControllerCompatApi21.PlaybackInfo.getVolumeControl(object), MediaControllerCompatApi21.PlaybackInfo.getMaxVolume(object), MediaControllerCompatApi21.PlaybackInfo.getCurrentVolume(object));
            }
            return null;
        }

        @Override
        public PlaybackStateCompat getPlaybackState() {
            Object object;
            if (this.mSessionToken.getExtraBinder() != null) {
                try {
                    PlaybackStateCompat playbackStateCompat = this.mSessionToken.getExtraBinder().getPlaybackState();
                    return playbackStateCompat;
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in getPlaybackState.", (Throwable)remoteException);
                }
            }
            if ((object = MediaControllerCompatApi21.getPlaybackState(this.mControllerObj)) != null) {
                return PlaybackStateCompat.fromPlaybackState(object);
            }
            return null;
        }

        @Override
        public List<MediaSessionCompat.QueueItem> getQueue() {
            List<Object> list = MediaControllerCompatApi21.getQueue(this.mControllerObj);
            if (list != null) {
                return MediaSessionCompat.QueueItem.fromQueueItemList(list);
            }
            return null;
        }

        @Override
        public CharSequence getQueueTitle() {
            return MediaControllerCompatApi21.getQueueTitle(this.mControllerObj);
        }

        @Override
        public int getRatingType() {
            if (Build.VERSION.SDK_INT < 22 && this.mSessionToken.getExtraBinder() != null) {
                try {
                    int n = this.mSessionToken.getExtraBinder().getRatingType();
                    return n;
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in getRatingType.", (Throwable)remoteException);
                }
            }
            return MediaControllerCompatApi21.getRatingType(this.mControllerObj);
        }

        @Override
        public int getRepeatMode() {
            if (this.mSessionToken.getExtraBinder() != null) {
                try {
                    int n = this.mSessionToken.getExtraBinder().getRepeatMode();
                    return n;
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in getRepeatMode.", (Throwable)remoteException);
                }
            }
            return -1;
        }

        @Override
        public PendingIntent getSessionActivity() {
            return MediaControllerCompatApi21.getSessionActivity(this.mControllerObj);
        }

        @Override
        public int getShuffleMode() {
            if (this.mSessionToken.getExtraBinder() != null) {
                try {
                    int n = this.mSessionToken.getExtraBinder().getShuffleMode();
                    return n;
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in getShuffleMode.", (Throwable)remoteException);
                }
            }
            return -1;
        }

        @Override
        public TransportControls getTransportControls() {
            Object object = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
            if (object != null) {
                return new TransportControlsApi21(object);
            }
            return null;
        }

        @Override
        public boolean isCaptioningEnabled() {
            if (this.mSessionToken.getExtraBinder() != null) {
                try {
                    boolean bl = this.mSessionToken.getExtraBinder().isCaptioningEnabled();
                    return bl;
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in isCaptioningEnabled.", (Throwable)remoteException);
                }
            }
            return false;
        }

        @Override
        public boolean isSessionReady() {
            if (this.mSessionToken.getExtraBinder() != null) {
                return true;
            }
            return false;
        }

        void processPendingCallbacksLocked() {
            if (this.mSessionToken.getExtraBinder() == null) {
                return;
            }
            for (Callback callback : this.mPendingCallbacks) {
                ExtraCallback extraCallback = new ExtraCallback(callback);
                this.mCallbackMap.put(callback, extraCallback);
                callback.mIControllerCallback = extraCallback;
                try {
                    this.mSessionToken.getExtraBinder().registerCallbackListener(extraCallback);
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in registerCallback.", (Throwable)remoteException);
                    break;
                }
                callback.postToHandler(13, null, null);
            }
            this.mPendingCallbacks.clear();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public final void registerCallback(Callback callback, Handler object) {
            MediaControllerCompatApi21.registerCallback(this.mControllerObj, callback.mCallbackObj, (Handler)object);
            object = this.mLock;
            synchronized (object) {
                if (this.mSessionToken.getExtraBinder() != null) {
                    ExtraCallback extraCallback = new ExtraCallback(callback);
                    this.mCallbackMap.put(callback, extraCallback);
                    callback.mIControllerCallback = extraCallback;
                    try {
                        this.mSessionToken.getExtraBinder().registerCallbackListener(extraCallback);
                        callback.postToHandler(13, null, null);
                    }
                    catch (RemoteException remoteException) {
                        Log.e((String)"MediaControllerCompat", (String)"Dead object in registerCallback.", (Throwable)remoteException);
                    }
                } else {
                    callback.mIControllerCallback = null;
                    this.mPendingCallbacks.add(callback);
                }
                return;
            }
        }

        @Override
        public void removeQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
            if ((4L & this.getFlags()) != 0L) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", (Parcelable)mediaDescriptionCompat);
                this.sendCommand("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM", bundle, null);
                return;
            }
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
        }

        @Override
        public void sendCommand(String string, Bundle bundle, ResultReceiver resultReceiver) {
            MediaControllerCompatApi21.sendCommand(this.mControllerObj, string, bundle, resultReceiver);
        }

        @Override
        public void setVolumeTo(int n, int n2) {
            MediaControllerCompatApi21.setVolumeTo(this.mControllerObj, n, n2);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public final void unregisterCallback(Callback callback) {
            MediaControllerCompatApi21.unregisterCallback(this.mControllerObj, callback.mCallbackObj);
            Object object = this.mLock;
            synchronized (object) {
                Object object2 = this.mSessionToken.getExtraBinder();
                if (object2 != null) {
                    try {
                        object2 = this.mCallbackMap.remove(callback);
                        if (object2 != null) {
                            callback.mIControllerCallback = null;
                            this.mSessionToken.getExtraBinder().unregisterCallbackListener((IMediaControllerCallback)object2);
                        }
                    }
                    catch (RemoteException remoteException) {
                        Log.e((String)"MediaControllerCompat", (String)"Dead object in unregisterCallback.", (Throwable)remoteException);
                    }
                } else {
                    this.mPendingCallbacks.remove(callback);
                }
                return;
            }
        }

        private static class ExtraBinderRequestResultReceiver
        extends ResultReceiver {
            private WeakReference<MediaControllerImplApi21> mMediaControllerImpl;

            ExtraBinderRequestResultReceiver(MediaControllerImplApi21 mediaControllerImplApi21) {
                super(null);
                this.mMediaControllerImpl = new WeakReference<MediaControllerImplApi21>(mediaControllerImplApi21);
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            protected void onReceiveResult(int n, Bundle bundle) {
                MediaControllerImplApi21 mediaControllerImplApi21 = this.mMediaControllerImpl.get();
                if (mediaControllerImplApi21 == null) {
                    return;
                }
                if (bundle == null) {
                    return;
                }
                Object object = mediaControllerImplApi21.mLock;
                synchronized (object) {
                    mediaControllerImplApi21.mSessionToken.setExtraBinder(IMediaSession.Stub.asInterface(BundleCompat.getBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER")));
                    mediaControllerImplApi21.mSessionToken.setSessionToken2Bundle(bundle.getBundle("android.support.v4.media.session.SESSION_TOKEN2_BUNDLE"));
                    mediaControllerImplApi21.processPendingCallbacksLocked();
                    return;
                }
            }
        }

        private static class ExtraCallback
        extends Callback.StubCompat {
            ExtraCallback(Callback callback) {
                super(callback);
            }

            @Override
            public void onExtrasChanged(Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void onMetadataChanged(MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void onQueueChanged(List<MediaSessionCompat.QueueItem> list) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void onQueueTitleChanged(CharSequence charSequence) throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void onSessionDestroyed() throws RemoteException {
                throw new AssertionError();
            }

            @Override
            public void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                throw new AssertionError();
            }
        }

    }

    static class MediaControllerImplApi23
    extends MediaControllerImplApi21 {
        public MediaControllerImplApi23(Context context, MediaSessionCompat.Token token) throws RemoteException {
            super(context, token);
        }

        @Override
        public TransportControls getTransportControls() {
            Object object = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
            if (object != null) {
                return new TransportControlsApi23(object);
            }
            return null;
        }
    }

    static class MediaControllerImplApi24
    extends MediaControllerImplApi23 {
        public MediaControllerImplApi24(Context context, MediaSessionCompat.Token token) throws RemoteException {
            super(context, token);
        }

        @Override
        public TransportControls getTransportControls() {
            Object object = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
            if (object != null) {
                return new TransportControlsApi24(object);
            }
            return null;
        }
    }

    static class MediaControllerImplBase
    implements MediaControllerImpl {
        private IMediaSession mBinder;
        private TransportControls mTransportControls;

        public MediaControllerImplBase(MediaSessionCompat.Token token) {
            this.mBinder = IMediaSession.Stub.asInterface((IBinder)token.getToken());
        }

        @Override
        public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
            try {
                if ((4L & this.mBinder.getFlags()) != 0L) {
                    this.mBinder.addQueueItem(mediaDescriptionCompat);
                    return;
                }
                throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in addQueueItem.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void addQueueItem(MediaDescriptionCompat mediaDescriptionCompat, int n) {
            try {
                if ((4L & this.mBinder.getFlags()) != 0L) {
                    this.mBinder.addQueueItemAt(mediaDescriptionCompat, n);
                    return;
                }
                throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in addQueueItemAt.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void adjustVolume(int n, int n2) {
            try {
                this.mBinder.adjustVolume(n, n2, null);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in adjustVolume.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public boolean dispatchMediaButtonEvent(KeyEvent keyEvent) {
            if (keyEvent != null) {
                try {
                    this.mBinder.sendMediaButton(keyEvent);
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in dispatchMediaButtonEvent.", (Throwable)remoteException);
                }
                return false;
            }
            throw new IllegalArgumentException("event may not be null.");
        }

        @Override
        public Bundle getExtras() {
            try {
                Bundle bundle = this.mBinder.getExtras();
                return bundle;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getExtras.", (Throwable)remoteException);
                return null;
            }
        }

        @Override
        public long getFlags() {
            try {
                long l = this.mBinder.getFlags();
                return l;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getFlags.", (Throwable)remoteException);
                return 0L;
            }
        }

        @Override
        public Object getMediaController() {
            return null;
        }

        @Override
        public MediaMetadataCompat getMetadata() {
            try {
                MediaMetadataCompat mediaMetadataCompat = this.mBinder.getMetadata();
                return mediaMetadataCompat;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getMetadata.", (Throwable)remoteException);
                return null;
            }
        }

        @Override
        public String getPackageName() {
            try {
                String string = this.mBinder.getPackageName();
                return string;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getPackageName.", (Throwable)remoteException);
                return null;
            }
        }

        @Override
        public PlaybackInfo getPlaybackInfo() {
            try {
                Object object = this.mBinder.getVolumeAttributes();
                object = new PlaybackInfo(object.volumeType, object.audioStream, object.controlType, object.maxVolume, object.currentVolume);
                return object;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getPlaybackInfo.", (Throwable)remoteException);
                return null;
            }
        }

        @Override
        public PlaybackStateCompat getPlaybackState() {
            try {
                PlaybackStateCompat playbackStateCompat = this.mBinder.getPlaybackState();
                return playbackStateCompat;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getPlaybackState.", (Throwable)remoteException);
                return null;
            }
        }

        @Override
        public List<MediaSessionCompat.QueueItem> getQueue() {
            try {
                List<MediaSessionCompat.QueueItem> list = this.mBinder.getQueue();
                return list;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getQueue.", (Throwable)remoteException);
                return null;
            }
        }

        @Override
        public CharSequence getQueueTitle() {
            try {
                CharSequence charSequence = this.mBinder.getQueueTitle();
                return charSequence;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getQueueTitle.", (Throwable)remoteException);
                return null;
            }
        }

        @Override
        public int getRatingType() {
            try {
                int n = this.mBinder.getRatingType();
                return n;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getRatingType.", (Throwable)remoteException);
                return 0;
            }
        }

        @Override
        public int getRepeatMode() {
            try {
                int n = this.mBinder.getRepeatMode();
                return n;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getRepeatMode.", (Throwable)remoteException);
                return -1;
            }
        }

        @Override
        public PendingIntent getSessionActivity() {
            try {
                PendingIntent pendingIntent = this.mBinder.getLaunchPendingIntent();
                return pendingIntent;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getSessionActivity.", (Throwable)remoteException);
                return null;
            }
        }

        @Override
        public int getShuffleMode() {
            try {
                int n = this.mBinder.getShuffleMode();
                return n;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in getShuffleMode.", (Throwable)remoteException);
                return -1;
            }
        }

        @Override
        public TransportControls getTransportControls() {
            if (this.mTransportControls == null) {
                this.mTransportControls = new TransportControlsBase(this.mBinder);
            }
            return this.mTransportControls;
        }

        @Override
        public boolean isCaptioningEnabled() {
            try {
                boolean bl = this.mBinder.isCaptioningEnabled();
                return bl;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in isCaptioningEnabled.", (Throwable)remoteException);
                return false;
            }
        }

        @Override
        public boolean isSessionReady() {
            return true;
        }

        @Override
        public void registerCallback(Callback callback, Handler handler) {
            if (callback != null) {
                try {
                    this.mBinder.asBinder().linkToDeath((IBinder.DeathRecipient)callback, 0);
                    this.mBinder.registerCallbackListener((IMediaControllerCallback)callback.mCallbackObj);
                    callback.postToHandler(13, null, null);
                    return;
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in registerCallback.", (Throwable)remoteException);
                    callback.postToHandler(8, null, null);
                    return;
                }
            }
            throw new IllegalArgumentException("callback may not be null.");
        }

        @Override
        public void removeQueueItem(MediaDescriptionCompat mediaDescriptionCompat) {
            try {
                if ((4L & this.mBinder.getFlags()) != 0L) {
                    this.mBinder.removeQueueItem(mediaDescriptionCompat);
                    return;
                }
                throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in removeQueueItem.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void sendCommand(String string, Bundle bundle, ResultReceiver resultReceiver) {
            try {
                this.mBinder.sendCommand(string, bundle, new MediaSessionCompat.ResultReceiverWrapper(resultReceiver));
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in sendCommand.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void setVolumeTo(int n, int n2) {
            try {
                this.mBinder.setVolumeTo(n, n2, null);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in setVolumeTo.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void unregisterCallback(Callback callback) {
            if (callback != null) {
                try {
                    this.mBinder.unregisterCallbackListener((IMediaControllerCallback)callback.mCallbackObj);
                    this.mBinder.asBinder().unlinkToDeath((IBinder.DeathRecipient)callback, 0);
                    return;
                }
                catch (RemoteException remoteException) {
                    Log.e((String)"MediaControllerCompat", (String)"Dead object in unregisterCallback.", (Throwable)remoteException);
                    return;
                }
            }
            throw new IllegalArgumentException("callback may not be null.");
        }
    }

    public static final class PlaybackInfo {
        public static final int PLAYBACK_TYPE_LOCAL = 1;
        public static final int PLAYBACK_TYPE_REMOTE = 2;
        private final int mAudioStream;
        private final int mCurrentVolume;
        private final int mMaxVolume;
        private final int mPlaybackType;
        private final int mVolumeControl;

        PlaybackInfo(int n, int n2, int n3, int n4, int n5) {
            this.mPlaybackType = n;
            this.mAudioStream = n2;
            this.mVolumeControl = n3;
            this.mMaxVolume = n4;
            this.mCurrentVolume = n5;
        }

        public int getAudioStream() {
            return this.mAudioStream;
        }

        public int getCurrentVolume() {
            return this.mCurrentVolume;
        }

        public int getMaxVolume() {
            return this.mMaxVolume;
        }

        public int getPlaybackType() {
            return this.mPlaybackType;
        }

        public int getVolumeControl() {
            return this.mVolumeControl;
        }
    }

    public static abstract class TransportControls {
        public static final String EXTRA_LEGACY_STREAM_TYPE = "android.media.session.extra.LEGACY_STREAM_TYPE";

        TransportControls() {
        }

        public abstract void fastForward();

        public abstract void pause();

        public abstract void play();

        public abstract void playFromMediaId(String var1, Bundle var2);

        public abstract void playFromSearch(String var1, Bundle var2);

        public abstract void playFromUri(Uri var1, Bundle var2);

        public abstract void prepare();

        public abstract void prepareFromMediaId(String var1, Bundle var2);

        public abstract void prepareFromSearch(String var1, Bundle var2);

        public abstract void prepareFromUri(Uri var1, Bundle var2);

        public abstract void rewind();

        public abstract void seekTo(long var1);

        public abstract void sendCustomAction(PlaybackStateCompat.CustomAction var1, Bundle var2);

        public abstract void sendCustomAction(String var1, Bundle var2);

        public abstract void setCaptioningEnabled(boolean var1);

        public abstract void setRating(RatingCompat var1);

        public abstract void setRating(RatingCompat var1, Bundle var2);

        public abstract void setRepeatMode(int var1);

        public abstract void setShuffleMode(int var1);

        public abstract void skipToNext();

        public abstract void skipToPrevious();

        public abstract void skipToQueueItem(long var1);

        public abstract void stop();
    }

    static class TransportControlsApi21
    extends TransportControls {
        protected final Object mControlsObj;

        public TransportControlsApi21(Object object) {
            this.mControlsObj = object;
        }

        @Override
        public void fastForward() {
            MediaControllerCompatApi21.TransportControls.fastForward(this.mControlsObj);
        }

        @Override
        public void pause() {
            MediaControllerCompatApi21.TransportControls.pause(this.mControlsObj);
        }

        @Override
        public void play() {
            MediaControllerCompatApi21.TransportControls.play(this.mControlsObj);
        }

        @Override
        public void playFromMediaId(String string, Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.playFromMediaId(this.mControlsObj, string, bundle);
        }

        @Override
        public void playFromSearch(String string, Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.playFromSearch(this.mControlsObj, string, bundle);
        }

        @Override
        public void playFromUri(Uri uri, Bundle bundle) {
            if (uri != null && !Uri.EMPTY.equals((Object)uri)) {
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", (Parcelable)uri);
                bundle2.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", bundle);
                this.sendCustomAction("android.support.v4.media.session.action.PLAY_FROM_URI", bundle2);
                return;
            }
            throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
        }

        @Override
        public void prepare() {
            this.sendCustomAction("android.support.v4.media.session.action.PREPARE", null);
        }

        @Override
        public void prepareFromMediaId(String string, Bundle bundle) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID", string);
            bundle2.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", bundle);
            this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID", bundle2);
        }

        @Override
        public void prepareFromSearch(String string, Bundle bundle) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("android.support.v4.media.session.action.ARGUMENT_QUERY", string);
            bundle2.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", bundle);
            this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_SEARCH", bundle2);
        }

        @Override
        public void prepareFromUri(Uri uri, Bundle bundle) {
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", (Parcelable)uri);
            bundle2.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", bundle);
            this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_URI", bundle2);
        }

        @Override
        public void rewind() {
            MediaControllerCompatApi21.TransportControls.rewind(this.mControlsObj);
        }

        @Override
        public void seekTo(long l) {
            MediaControllerCompatApi21.TransportControls.seekTo(this.mControlsObj, l);
        }

        @Override
        public void sendCustomAction(PlaybackStateCompat.CustomAction customAction, Bundle bundle) {
            MediaControllerCompat.validateCustomAction(customAction.getAction(), bundle);
            MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, customAction.getAction(), bundle);
        }

        @Override
        public void sendCustomAction(String string, Bundle bundle) {
            MediaControllerCompat.validateCustomAction(string, bundle);
            MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, string, bundle);
        }

        @Override
        public void setCaptioningEnabled(boolean bl) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED", bl);
            this.sendCustomAction("android.support.v4.media.session.action.SET_CAPTIONING_ENABLED", bundle);
        }

        @Override
        public void setRating(RatingCompat object) {
            Object object2 = this.mControlsObj;
            object = object != null ? object.getRating() : null;
            MediaControllerCompatApi21.TransportControls.setRating(object2, object);
        }

        @Override
        public void setRating(RatingCompat ratingCompat, Bundle bundle) {
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable("android.support.v4.media.session.action.ARGUMENT_RATING", (Parcelable)ratingCompat);
            bundle2.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", bundle);
            this.sendCustomAction("android.support.v4.media.session.action.SET_RATING", bundle2);
        }

        @Override
        public void setRepeatMode(int n) {
            Bundle bundle = new Bundle();
            bundle.putInt("android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE", n);
            this.sendCustomAction("android.support.v4.media.session.action.SET_REPEAT_MODE", bundle);
        }

        @Override
        public void setShuffleMode(int n) {
            Bundle bundle = new Bundle();
            bundle.putInt("android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE", n);
            this.sendCustomAction("android.support.v4.media.session.action.SET_SHUFFLE_MODE", bundle);
        }

        @Override
        public void skipToNext() {
            MediaControllerCompatApi21.TransportControls.skipToNext(this.mControlsObj);
        }

        @Override
        public void skipToPrevious() {
            MediaControllerCompatApi21.TransportControls.skipToPrevious(this.mControlsObj);
        }

        @Override
        public void skipToQueueItem(long l) {
            MediaControllerCompatApi21.TransportControls.skipToQueueItem(this.mControlsObj, l);
        }

        @Override
        public void stop() {
            MediaControllerCompatApi21.TransportControls.stop(this.mControlsObj);
        }
    }

    static class TransportControlsApi23
    extends TransportControlsApi21 {
        public TransportControlsApi23(Object object) {
            super(object);
        }

        @Override
        public void playFromUri(Uri uri, Bundle bundle) {
            MediaControllerCompatApi23.TransportControls.playFromUri(this.mControlsObj, uri, bundle);
        }
    }

    static class TransportControlsApi24
    extends TransportControlsApi23 {
        public TransportControlsApi24(Object object) {
            super(object);
        }

        @Override
        public void prepare() {
            MediaControllerCompatApi24.TransportControls.prepare(this.mControlsObj);
        }

        @Override
        public void prepareFromMediaId(String string, Bundle bundle) {
            MediaControllerCompatApi24.TransportControls.prepareFromMediaId(this.mControlsObj, string, bundle);
        }

        @Override
        public void prepareFromSearch(String string, Bundle bundle) {
            MediaControllerCompatApi24.TransportControls.prepareFromSearch(this.mControlsObj, string, bundle);
        }

        @Override
        public void prepareFromUri(Uri uri, Bundle bundle) {
            MediaControllerCompatApi24.TransportControls.prepareFromUri(this.mControlsObj, uri, bundle);
        }
    }

    static class TransportControlsBase
    extends TransportControls {
        private IMediaSession mBinder;

        public TransportControlsBase(IMediaSession iMediaSession) {
            this.mBinder = iMediaSession;
        }

        @Override
        public void fastForward() {
            try {
                this.mBinder.fastForward();
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in fastForward.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void pause() {
            try {
                this.mBinder.pause();
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in pause.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void play() {
            try {
                this.mBinder.play();
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in play.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void playFromMediaId(String string, Bundle bundle) {
            try {
                this.mBinder.playFromMediaId(string, bundle);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in playFromMediaId.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void playFromSearch(String string, Bundle bundle) {
            try {
                this.mBinder.playFromSearch(string, bundle);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in playFromSearch.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void playFromUri(Uri uri, Bundle bundle) {
            try {
                this.mBinder.playFromUri(uri, bundle);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in playFromUri.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void prepare() {
            try {
                this.mBinder.prepare();
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in prepare.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void prepareFromMediaId(String string, Bundle bundle) {
            try {
                this.mBinder.prepareFromMediaId(string, bundle);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in prepareFromMediaId.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void prepareFromSearch(String string, Bundle bundle) {
            try {
                this.mBinder.prepareFromSearch(string, bundle);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in prepareFromSearch.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void prepareFromUri(Uri uri, Bundle bundle) {
            try {
                this.mBinder.prepareFromUri(uri, bundle);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in prepareFromUri.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void rewind() {
            try {
                this.mBinder.rewind();
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in rewind.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void seekTo(long l) {
            try {
                this.mBinder.seekTo(l);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in seekTo.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void sendCustomAction(PlaybackStateCompat.CustomAction customAction, Bundle bundle) {
            this.sendCustomAction(customAction.getAction(), bundle);
        }

        @Override
        public void sendCustomAction(String string, Bundle bundle) {
            MediaControllerCompat.validateCustomAction(string, bundle);
            try {
                this.mBinder.sendCustomAction(string, bundle);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in sendCustomAction.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void setCaptioningEnabled(boolean bl) {
            try {
                this.mBinder.setCaptioningEnabled(bl);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in setCaptioningEnabled.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void setRating(RatingCompat ratingCompat) {
            try {
                this.mBinder.rate(ratingCompat);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in setRating.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void setRating(RatingCompat ratingCompat, Bundle bundle) {
            try {
                this.mBinder.rateWithExtras(ratingCompat, bundle);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in setRating.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void setRepeatMode(int n) {
            try {
                this.mBinder.setRepeatMode(n);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in setRepeatMode.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void setShuffleMode(int n) {
            try {
                this.mBinder.setShuffleMode(n);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in setShuffleMode.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void skipToNext() {
            try {
                this.mBinder.next();
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in skipToNext.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void skipToPrevious() {
            try {
                this.mBinder.previous();
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in skipToPrevious.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void skipToQueueItem(long l) {
            try {
                this.mBinder.skipToQueueItem(l);
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in skipToQueueItem.", (Throwable)remoteException);
                return;
            }
        }

        @Override
        public void stop() {
            try {
                this.mBinder.stop();
                return;
            }
            catch (RemoteException remoteException) {
                Log.e((String)"MediaControllerCompat", (String)"Dead object in stop.", (Throwable)remoteException);
                return;
            }
        }
    }

}

