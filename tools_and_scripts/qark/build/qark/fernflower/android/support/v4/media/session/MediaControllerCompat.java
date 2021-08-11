package android.support.v4.media.session;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.Build.VERSION;
import android.os.IBinder.DeathRecipient;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import androidx.core.app.ComponentActivity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
   private final MediaControllerCompat.MediaControllerImpl mImpl;
   private final HashSet mRegisteredCallbacks = new HashSet();
   private final MediaSessionCompat.Token mToken;

   public MediaControllerCompat(Context var1, MediaSessionCompat.Token var2) throws RemoteException {
      if (var2 != null) {
         this.mToken = var2;
         if (VERSION.SDK_INT >= 24) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi24(var1, var2);
         } else if (VERSION.SDK_INT >= 23) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi23(var1, var2);
         } else if (VERSION.SDK_INT >= 21) {
            this.mImpl = new MediaControllerCompat.MediaControllerImplApi21(var1, var2);
         } else {
            this.mImpl = new MediaControllerCompat.MediaControllerImplBase(var2);
         }
      } else {
         throw new IllegalArgumentException("sessionToken must not be null");
      }
   }

   public MediaControllerCompat(Context var1, MediaSessionCompat var2) {
      if (var2 != null) {
         this.mToken = var2.getSessionToken();
         var2 = null;

         Object var4;
         try {
            if (VERSION.SDK_INT >= 24) {
               var4 = new MediaControllerCompat.MediaControllerImplApi24(var1, this.mToken);
            } else if (VERSION.SDK_INT >= 23) {
               var4 = new MediaControllerCompat.MediaControllerImplApi23(var1, this.mToken);
            } else if (VERSION.SDK_INT >= 21) {
               var4 = new MediaControllerCompat.MediaControllerImplApi21(var1, this.mToken);
            } else {
               var4 = new MediaControllerCompat.MediaControllerImplBase(this.mToken);
            }
         } catch (RemoteException var3) {
            Log.w("MediaControllerCompat", "Failed to create MediaControllerImpl.", var3);
            var4 = var2;
         }

         this.mImpl = (MediaControllerCompat.MediaControllerImpl)var4;
      } else {
         throw new IllegalArgumentException("session must not be null");
      }
   }

   public static MediaControllerCompat getMediaController(Activity var0) {
      boolean var1 = var0 instanceof ComponentActivity;
      Object var2 = null;
      MediaControllerCompat var5;
      if (var1) {
         MediaControllerCompat.MediaControllerExtraData var3 = (MediaControllerCompat.MediaControllerExtraData)((ComponentActivity)var0).getExtraData(MediaControllerCompat.MediaControllerExtraData.class);
         var5 = (MediaControllerCompat)var2;
         if (var3 != null) {
            var5 = var3.getMediaController();
         }

         return var5;
      } else {
         if (VERSION.SDK_INT >= 21) {
            var2 = MediaControllerCompatApi21.getMediaController(var0);
            if (var2 == null) {
               return null;
            }

            var2 = MediaControllerCompatApi21.getSessionToken(var2);

            try {
               var5 = new MediaControllerCompat(var0, MediaSessionCompat.Token.fromToken(var2));
               return var5;
            } catch (RemoteException var4) {
               Log.e("MediaControllerCompat", "Dead object in getMediaController.", var4);
            }
         }

         return null;
      }
   }

   public static void setMediaController(Activity var0, MediaControllerCompat var1) {
      if (var0 instanceof ComponentActivity) {
         ((ComponentActivity)var0).putExtraData(new MediaControllerCompat.MediaControllerExtraData(var1));
      }

      if (VERSION.SDK_INT >= 21) {
         Object var2 = null;
         if (var1 != null) {
            var2 = MediaControllerCompatApi21.fromToken(var0, var1.getSessionToken().getToken());
         }

         MediaControllerCompatApi21.setMediaController(var0, var2);
      }

   }

   static void validateCustomAction(String var0, Bundle var1) {
      if (var0 != null) {
         byte var2 = -1;
         int var3 = var0.hashCode();
         if (var3 != -1348483723) {
            if (var3 == 503011406 && var0.equals("android.support.v4.media.session.action.UNFOLLOW")) {
               var2 = 1;
            }
         } else if (var0.equals("android.support.v4.media.session.action.FOLLOW")) {
            var2 = 0;
         }

         if (var2 == 0 || var2 == 1) {
            if (var1 == null || !var1.containsKey("android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE")) {
               StringBuilder var4 = new StringBuilder();
               var4.append("An extra field android.support.v4.media.session.ARGUMENT_MEDIA_ATTRIBUTE is required for this action ");
               var4.append(var0);
               var4.append(".");
               throw new IllegalArgumentException(var4.toString());
            }
         }
      }
   }

   public void addQueueItem(MediaDescriptionCompat var1) {
      this.mImpl.addQueueItem(var1);
   }

   public void addQueueItem(MediaDescriptionCompat var1, int var2) {
      this.mImpl.addQueueItem(var1, var2);
   }

   public void adjustVolume(int var1, int var2) {
      this.mImpl.adjustVolume(var1, var2);
   }

   public boolean dispatchMediaButtonEvent(KeyEvent var1) {
      if (var1 != null) {
         return this.mImpl.dispatchMediaButtonEvent(var1);
      } else {
         throw new IllegalArgumentException("KeyEvent may not be null");
      }
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

   public MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
      return this.mImpl.getPlaybackInfo();
   }

   public PlaybackStateCompat getPlaybackState() {
      return this.mImpl.getPlaybackState();
   }

   public List getQueue() {
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

   public MediaControllerCompat.TransportControls getTransportControls() {
      return this.mImpl.getTransportControls();
   }

   public boolean isCaptioningEnabled() {
      return this.mImpl.isCaptioningEnabled();
   }

   public boolean isSessionReady() {
      return this.mImpl.isSessionReady();
   }

   public void registerCallback(MediaControllerCompat.Callback var1) {
      this.registerCallback(var1, (Handler)null);
   }

   public void registerCallback(MediaControllerCompat.Callback var1, Handler var2) {
      if (var1 != null) {
         Handler var3 = var2;
         if (var2 == null) {
            var3 = new Handler();
         }

         var1.setHandler(var3);
         this.mImpl.registerCallback(var1, var3);
         this.mRegisteredCallbacks.add(var1);
      } else {
         throw new IllegalArgumentException("callback must not be null");
      }
   }

   public void removeQueueItem(MediaDescriptionCompat var1) {
      this.mImpl.removeQueueItem(var1);
   }

   @Deprecated
   public void removeQueueItemAt(int var1) {
      List var2 = this.getQueue();
      if (var2 != null && var1 >= 0 && var1 < var2.size()) {
         MediaSessionCompat.QueueItem var3 = (MediaSessionCompat.QueueItem)var2.get(var1);
         if (var3 != null) {
            this.removeQueueItem(var3.getDescription());
         }
      }

   }

   public void sendCommand(String var1, Bundle var2, ResultReceiver var3) {
      if (!TextUtils.isEmpty(var1)) {
         this.mImpl.sendCommand(var1, var2, var3);
      } else {
         throw new IllegalArgumentException("command must neither be null nor empty");
      }
   }

   public void setVolumeTo(int var1, int var2) {
      this.mImpl.setVolumeTo(var1, var2);
   }

   public void unregisterCallback(MediaControllerCompat.Callback var1) {
      if (var1 != null) {
         try {
            this.mRegisteredCallbacks.remove(var1);
            this.mImpl.unregisterCallback(var1);
         } finally {
            var1.setHandler((Handler)null);
         }

      } else {
         throw new IllegalArgumentException("callback must not be null");
      }
   }

   public abstract static class Callback implements DeathRecipient {
      final Object mCallbackObj;
      MediaControllerCompat.Callback.MessageHandler mHandler;
      IMediaControllerCallback mIControllerCallback;

      public Callback() {
         if (VERSION.SDK_INT >= 21) {
            this.mCallbackObj = MediaControllerCompatApi21.createCallback(new MediaControllerCompat.Callback.StubApi21(this));
         } else {
            MediaControllerCompat.Callback.StubCompat var1 = new MediaControllerCompat.Callback.StubCompat(this);
            this.mIControllerCallback = var1;
            this.mCallbackObj = var1;
         }
      }

      public void binderDied() {
         this.postToHandler(8, (Object)null, (Bundle)null);
      }

      public IMediaControllerCallback getIControllerCallback() {
         return this.mIControllerCallback;
      }

      public void onAudioInfoChanged(MediaControllerCompat.PlaybackInfo var1) {
      }

      public void onCaptioningEnabledChanged(boolean var1) {
      }

      public void onExtrasChanged(Bundle var1) {
      }

      public void onMetadataChanged(MediaMetadataCompat var1) {
      }

      public void onPlaybackStateChanged(PlaybackStateCompat var1) {
      }

      public void onQueueChanged(List var1) {
      }

      public void onQueueTitleChanged(CharSequence var1) {
      }

      public void onRepeatModeChanged(int var1) {
      }

      public void onSessionDestroyed() {
      }

      public void onSessionEvent(String var1, Bundle var2) {
      }

      public void onSessionReady() {
      }

      public void onShuffleModeChanged(int var1) {
      }

      void postToHandler(int var1, Object var2, Bundle var3) {
         MediaControllerCompat.Callback.MessageHandler var4 = this.mHandler;
         if (var4 != null) {
            Message var5 = var4.obtainMessage(var1, var2);
            var5.setData(var3);
            var5.sendToTarget();
         }

      }

      void setHandler(Handler var1) {
         MediaControllerCompat.Callback.MessageHandler var2;
         if (var1 == null) {
            var2 = this.mHandler;
            if (var2 != null) {
               var2.mRegistered = false;
               this.mHandler.removeCallbacksAndMessages((Object)null);
               this.mHandler = null;
               return;
            }
         } else {
            var2 = new MediaControllerCompat.Callback.MessageHandler(var1.getLooper());
            this.mHandler = var2;
            var2.mRegistered = true;
         }

      }

      private class MessageHandler extends Handler {
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
         boolean mRegistered = false;

         MessageHandler(Looper var2) {
            super(var2);
         }

         public void handleMessage(Message var1) {
            if (this.mRegistered) {
               switch(var1.what) {
               case 1:
                  Bundle var2 = var1.getData();
                  MediaSessionCompat.ensureClassLoader(var2);
                  Callback.this.onSessionEvent((String)var1.obj, var2);
                  return;
               case 2:
                  Callback.this.onPlaybackStateChanged((PlaybackStateCompat)var1.obj);
                  return;
               case 3:
                  Callback.this.onMetadataChanged((MediaMetadataCompat)var1.obj);
                  return;
               case 4:
                  Callback.this.onAudioInfoChanged((MediaControllerCompat.PlaybackInfo)var1.obj);
                  return;
               case 5:
                  Callback.this.onQueueChanged((List)var1.obj);
                  return;
               case 6:
                  Callback.this.onQueueTitleChanged((CharSequence)var1.obj);
                  return;
               case 7:
                  Bundle var3 = (Bundle)var1.obj;
                  MediaSessionCompat.ensureClassLoader(var3);
                  Callback.this.onExtrasChanged(var3);
                  return;
               case 8:
                  Callback.this.onSessionDestroyed();
                  return;
               case 9:
                  Callback.this.onRepeatModeChanged((Integer)var1.obj);
                  return;
               case 10:
               default:
                  return;
               case 11:
                  Callback.this.onCaptioningEnabledChanged((Boolean)var1.obj);
                  return;
               case 12:
                  Callback.this.onShuffleModeChanged((Integer)var1.obj);
                  return;
               case 13:
                  Callback.this.onSessionReady();
               }
            }
         }
      }

      private static class StubApi21 implements MediaControllerCompatApi21.Callback {
         private final WeakReference mCallback;

         StubApi21(MediaControllerCompat.Callback var1) {
            this.mCallback = new WeakReference(var1);
         }

         public void onAudioInfoChanged(int var1, int var2, int var3, int var4, int var5) {
            MediaControllerCompat.Callback var6 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var6 != null) {
               var6.onAudioInfoChanged(new MediaControllerCompat.PlaybackInfo(var1, var2, var3, var4, var5));
            }

         }

         public void onExtrasChanged(Bundle var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.onExtrasChanged(var1);
            }

         }

         public void onMetadataChanged(Object var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(var1));
            }

         }

         public void onPlaybackStateChanged(Object var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               if (var2.mIControllerCallback != null) {
                  return;
               }

               var2.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(var1));
            }

         }

         public void onQueueChanged(List var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(var1));
            }

         }

         public void onQueueTitleChanged(CharSequence var1) {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.onQueueTitleChanged(var1);
            }

         }

         public void onSessionDestroyed() {
            MediaControllerCompat.Callback var1 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var1 != null) {
               var1.onSessionDestroyed();
            }

         }

         public void onSessionEvent(String var1, Bundle var2) {
            MediaControllerCompat.Callback var3 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var3 != null) {
               if (var3.mIControllerCallback != null && VERSION.SDK_INT < 23) {
                  return;
               }

               var3.onSessionEvent(var1, var2);
            }

         }
      }

      private static class StubCompat extends IMediaControllerCallback.Stub {
         private final WeakReference mCallback;

         StubCompat(MediaControllerCompat.Callback var1) {
            this.mCallback = new WeakReference(var1);
         }

         public void onCaptioningEnabledChanged(boolean var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(11, var1, (Bundle)null);
            }

         }

         public void onEvent(String var1, Bundle var2) throws RemoteException {
            MediaControllerCompat.Callback var3 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var3 != null) {
               var3.postToHandler(1, var1, var2);
            }

         }

         public void onExtrasChanged(Bundle var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(7, var1, (Bundle)null);
            }

         }

         public void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(3, var1, (Bundle)null);
            }

         }

         public void onPlaybackStateChanged(PlaybackStateCompat var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(2, var1, (Bundle)null);
            }

         }

         public void onQueueChanged(List var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(5, var1, (Bundle)null);
            }

         }

         public void onQueueTitleChanged(CharSequence var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(6, var1, (Bundle)null);
            }

         }

         public void onRepeatModeChanged(int var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(9, var1, (Bundle)null);
            }

         }

         public void onSessionDestroyed() throws RemoteException {
            MediaControllerCompat.Callback var1 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var1 != null) {
               var1.postToHandler(8, (Object)null, (Bundle)null);
            }

         }

         public void onSessionReady() throws RemoteException {
            MediaControllerCompat.Callback var1 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var1 != null) {
               var1.postToHandler(13, (Object)null, (Bundle)null);
            }

         }

         public void onShuffleModeChanged(int var1) throws RemoteException {
            MediaControllerCompat.Callback var2 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var2 != null) {
               var2.postToHandler(12, var1, (Bundle)null);
            }

         }

         public void onShuffleModeChangedRemoved(boolean var1) throws RemoteException {
         }

         public void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException {
            MediaControllerCompat.Callback var3 = (MediaControllerCompat.Callback)this.mCallback.get();
            if (var3 != null) {
               MediaControllerCompat.PlaybackInfo var2 = null;
               if (var1 != null) {
                  var2 = new MediaControllerCompat.PlaybackInfo(var1.volumeType, var1.audioStream, var1.controlType, var1.maxVolume, var1.currentVolume);
               }

               var3.postToHandler(4, var2, (Bundle)null);
            }

         }
      }
   }

   private static class MediaControllerExtraData extends ComponentActivity.ExtraData {
      private final MediaControllerCompat mMediaController;

      MediaControllerExtraData(MediaControllerCompat var1) {
         this.mMediaController = var1;
      }

      MediaControllerCompat getMediaController() {
         return this.mMediaController;
      }
   }

   interface MediaControllerImpl {
      void addQueueItem(MediaDescriptionCompat var1);

      void addQueueItem(MediaDescriptionCompat var1, int var2);

      void adjustVolume(int var1, int var2);

      boolean dispatchMediaButtonEvent(KeyEvent var1);

      Bundle getExtras();

      long getFlags();

      Object getMediaController();

      MediaMetadataCompat getMetadata();

      String getPackageName();

      MediaControllerCompat.PlaybackInfo getPlaybackInfo();

      PlaybackStateCompat getPlaybackState();

      List getQueue();

      CharSequence getQueueTitle();

      int getRatingType();

      int getRepeatMode();

      PendingIntent getSessionActivity();

      int getShuffleMode();

      MediaControllerCompat.TransportControls getTransportControls();

      boolean isCaptioningEnabled();

      boolean isSessionReady();

      void registerCallback(MediaControllerCompat.Callback var1, Handler var2);

      void removeQueueItem(MediaDescriptionCompat var1);

      void sendCommand(String var1, Bundle var2, ResultReceiver var3);

      void setVolumeTo(int var1, int var2);

      void unregisterCallback(MediaControllerCompat.Callback var1);
   }

   static class MediaControllerImplApi21 implements MediaControllerCompat.MediaControllerImpl {
      private HashMap mCallbackMap = new HashMap();
      protected final Object mControllerObj;
      final Object mLock = new Object();
      private final List mPendingCallbacks = new ArrayList();
      final MediaSessionCompat.Token mSessionToken;

      public MediaControllerImplApi21(Context var1, MediaSessionCompat.Token var2) throws RemoteException {
         this.mSessionToken = var2;
         Object var3 = MediaControllerCompatApi21.fromToken(var1, var2.getToken());
         this.mControllerObj = var3;
         if (var3 != null) {
            if (this.mSessionToken.getExtraBinder() == null) {
               this.requestExtraBinder();
            }

         } else {
            throw new RemoteException();
         }
      }

      private void requestExtraBinder() {
         this.sendCommand("android.support.v4.media.session.command.GET_EXTRA_BINDER", (Bundle)null, new MediaControllerCompat.MediaControllerImplApi21.ExtraBinderRequestResultReceiver(this));
      }

      public void addQueueItem(MediaDescriptionCompat var1) {
         if ((4L & this.getFlags()) != 0L) {
            Bundle var2 = new Bundle();
            var2.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", var1);
            this.sendCommand("android.support.v4.media.session.command.ADD_QUEUE_ITEM", var2, (ResultReceiver)null);
         } else {
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
         }
      }

      public void addQueueItem(MediaDescriptionCompat var1, int var2) {
         if ((4L & this.getFlags()) != 0L) {
            Bundle var3 = new Bundle();
            var3.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", var1);
            var3.putInt("android.support.v4.media.session.command.ARGUMENT_INDEX", var2);
            this.sendCommand("android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT", var3, (ResultReceiver)null);
         } else {
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
         }
      }

      public void adjustVolume(int var1, int var2) {
         MediaControllerCompatApi21.adjustVolume(this.mControllerObj, var1, var2);
      }

      public boolean dispatchMediaButtonEvent(KeyEvent var1) {
         return MediaControllerCompatApi21.dispatchMediaButtonEvent(this.mControllerObj, var1);
      }

      public Bundle getExtras() {
         return MediaControllerCompatApi21.getExtras(this.mControllerObj);
      }

      public long getFlags() {
         return MediaControllerCompatApi21.getFlags(this.mControllerObj);
      }

      public Object getMediaController() {
         return this.mControllerObj;
      }

      public MediaMetadataCompat getMetadata() {
         Object var1 = MediaControllerCompatApi21.getMetadata(this.mControllerObj);
         return var1 != null ? MediaMetadataCompat.fromMediaMetadata(var1) : null;
      }

      public String getPackageName() {
         return MediaControllerCompatApi21.getPackageName(this.mControllerObj);
      }

      public MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
         Object var1 = MediaControllerCompatApi21.getPlaybackInfo(this.mControllerObj);
         return var1 != null ? new MediaControllerCompat.PlaybackInfo(MediaControllerCompatApi21.PlaybackInfo.getPlaybackType(var1), MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(var1), MediaControllerCompatApi21.PlaybackInfo.getVolumeControl(var1), MediaControllerCompatApi21.PlaybackInfo.getMaxVolume(var1), MediaControllerCompatApi21.PlaybackInfo.getCurrentVolume(var1)) : null;
      }

      public PlaybackStateCompat getPlaybackState() {
         if (this.mSessionToken.getExtraBinder() != null) {
            try {
               PlaybackStateCompat var3 = this.mSessionToken.getExtraBinder().getPlaybackState();
               return var3;
            } catch (RemoteException var2) {
               Log.e("MediaControllerCompat", "Dead object in getPlaybackState.", var2);
            }
         }

         Object var1 = MediaControllerCompatApi21.getPlaybackState(this.mControllerObj);
         return var1 != null ? PlaybackStateCompat.fromPlaybackState(var1) : null;
      }

      public List getQueue() {
         List var1 = MediaControllerCompatApi21.getQueue(this.mControllerObj);
         return var1 != null ? MediaSessionCompat.QueueItem.fromQueueItemList(var1) : null;
      }

      public CharSequence getQueueTitle() {
         return MediaControllerCompatApi21.getQueueTitle(this.mControllerObj);
      }

      public int getRatingType() {
         if (VERSION.SDK_INT < 22 && this.mSessionToken.getExtraBinder() != null) {
            try {
               int var1 = this.mSessionToken.getExtraBinder().getRatingType();
               return var1;
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in getRatingType.", var3);
            }
         }

         return MediaControllerCompatApi21.getRatingType(this.mControllerObj);
      }

      public int getRepeatMode() {
         if (this.mSessionToken.getExtraBinder() != null) {
            try {
               int var1 = this.mSessionToken.getExtraBinder().getRepeatMode();
               return var1;
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in getRepeatMode.", var3);
            }
         }

         return -1;
      }

      public PendingIntent getSessionActivity() {
         return MediaControllerCompatApi21.getSessionActivity(this.mControllerObj);
      }

      public int getShuffleMode() {
         if (this.mSessionToken.getExtraBinder() != null) {
            try {
               int var1 = this.mSessionToken.getExtraBinder().getShuffleMode();
               return var1;
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in getShuffleMode.", var3);
            }
         }

         return -1;
      }

      public MediaControllerCompat.TransportControls getTransportControls() {
         Object var1 = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
         return var1 != null ? new MediaControllerCompat.TransportControlsApi21(var1) : null;
      }

      public boolean isCaptioningEnabled() {
         if (this.mSessionToken.getExtraBinder() != null) {
            try {
               boolean var1 = this.mSessionToken.getExtraBinder().isCaptioningEnabled();
               return var1;
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in isCaptioningEnabled.", var3);
            }
         }

         return false;
      }

      public boolean isSessionReady() {
         return this.mSessionToken.getExtraBinder() != null;
      }

      void processPendingCallbacksLocked() {
         if (this.mSessionToken.getExtraBinder() != null) {
            MediaControllerCompat.Callback var2;
            for(Iterator var1 = this.mPendingCallbacks.iterator(); var1.hasNext(); var2.postToHandler(13, (Object)null, (Bundle)null)) {
               var2 = (MediaControllerCompat.Callback)var1.next();
               MediaControllerCompat.MediaControllerImplApi21.ExtraCallback var3 = new MediaControllerCompat.MediaControllerImplApi21.ExtraCallback(var2);
               this.mCallbackMap.put(var2, var3);
               var2.mIControllerCallback = var3;

               try {
                  this.mSessionToken.getExtraBinder().registerCallbackListener(var3);
               } catch (RemoteException var4) {
                  Log.e("MediaControllerCompat", "Dead object in registerCallback.", var4);
                  break;
               }
            }

            this.mPendingCallbacks.clear();
         }
      }

      public final void registerCallback(MediaControllerCompat.Callback param1, Handler param2) {
         // $FF: Couldn't be decompiled
      }

      public void removeQueueItem(MediaDescriptionCompat var1) {
         if ((4L & this.getFlags()) != 0L) {
            Bundle var2 = new Bundle();
            var2.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", var1);
            this.sendCommand("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM", var2, (ResultReceiver)null);
         } else {
            throw new UnsupportedOperationException("This session doesn't support queue management operations");
         }
      }

      public void sendCommand(String var1, Bundle var2, ResultReceiver var3) {
         MediaControllerCompatApi21.sendCommand(this.mControllerObj, var1, var2, var3);
      }

      public void setVolumeTo(int var1, int var2) {
         MediaControllerCompatApi21.setVolumeTo(this.mControllerObj, var1, var2);
      }

      public final void unregisterCallback(MediaControllerCompat.Callback param1) {
         // $FF: Couldn't be decompiled
      }

      private static class ExtraBinderRequestResultReceiver extends ResultReceiver {
         private WeakReference mMediaControllerImpl;

         ExtraBinderRequestResultReceiver(MediaControllerCompat.MediaControllerImplApi21 var1) {
            super((Handler)null);
            this.mMediaControllerImpl = new WeakReference(var1);
         }

         protected void onReceiveResult(int param1, Bundle param2) {
            // $FF: Couldn't be decompiled
         }
      }

      private static class ExtraCallback extends MediaControllerCompat.Callback.StubCompat {
         ExtraCallback(MediaControllerCompat.Callback var1) {
            super(var1);
         }

         public void onExtrasChanged(Bundle var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onMetadataChanged(MediaMetadataCompat var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onQueueChanged(List var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onQueueTitleChanged(CharSequence var1) throws RemoteException {
            throw new AssertionError();
         }

         public void onSessionDestroyed() throws RemoteException {
            throw new AssertionError();
         }

         public void onVolumeInfoChanged(ParcelableVolumeInfo var1) throws RemoteException {
            throw new AssertionError();
         }
      }
   }

   static class MediaControllerImplApi23 extends MediaControllerCompat.MediaControllerImplApi21 {
      public MediaControllerImplApi23(Context var1, MediaSessionCompat.Token var2) throws RemoteException {
         super(var1, var2);
      }

      public MediaControllerCompat.TransportControls getTransportControls() {
         Object var1 = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
         return var1 != null ? new MediaControllerCompat.TransportControlsApi23(var1) : null;
      }
   }

   static class MediaControllerImplApi24 extends MediaControllerCompat.MediaControllerImplApi23 {
      public MediaControllerImplApi24(Context var1, MediaSessionCompat.Token var2) throws RemoteException {
         super(var1, var2);
      }

      public MediaControllerCompat.TransportControls getTransportControls() {
         Object var1 = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
         return var1 != null ? new MediaControllerCompat.TransportControlsApi24(var1) : null;
      }
   }

   static class MediaControllerImplBase implements MediaControllerCompat.MediaControllerImpl {
      private IMediaSession mBinder;
      private MediaControllerCompat.TransportControls mTransportControls;

      public MediaControllerImplBase(MediaSessionCompat.Token var1) {
         this.mBinder = IMediaSession.Stub.asInterface((IBinder)var1.getToken());
      }

      public void addQueueItem(MediaDescriptionCompat var1) {
         try {
            if ((4L & this.mBinder.getFlags()) != 0L) {
               this.mBinder.addQueueItem(var1);
            } else {
               throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in addQueueItem.", var2);
         }
      }

      public void addQueueItem(MediaDescriptionCompat var1, int var2) {
         try {
            if ((4L & this.mBinder.getFlags()) != 0L) {
               this.mBinder.addQueueItemAt(var1, var2);
            } else {
               throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in addQueueItemAt.", var3);
         }
      }

      public void adjustVolume(int var1, int var2) {
         try {
            this.mBinder.adjustVolume(var1, var2, (String)null);
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in adjustVolume.", var4);
         }
      }

      public boolean dispatchMediaButtonEvent(KeyEvent var1) {
         if (var1 != null) {
            try {
               this.mBinder.sendMediaButton(var1);
            } catch (RemoteException var2) {
               Log.e("MediaControllerCompat", "Dead object in dispatchMediaButtonEvent.", var2);
            }

            return false;
         } else {
            throw new IllegalArgumentException("event may not be null.");
         }
      }

      public Bundle getExtras() {
         try {
            Bundle var1 = this.mBinder.getExtras();
            return var1;
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getExtras.", var2);
            return null;
         }
      }

      public long getFlags() {
         try {
            long var1 = this.mBinder.getFlags();
            return var1;
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in getFlags.", var4);
            return 0L;
         }
      }

      public Object getMediaController() {
         return null;
      }

      public MediaMetadataCompat getMetadata() {
         try {
            MediaMetadataCompat var1 = this.mBinder.getMetadata();
            return var1;
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getMetadata.", var2);
            return null;
         }
      }

      public String getPackageName() {
         try {
            String var1 = this.mBinder.getPackageName();
            return var1;
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getPackageName.", var2);
            return null;
         }
      }

      public MediaControllerCompat.PlaybackInfo getPlaybackInfo() {
         try {
            ParcelableVolumeInfo var1 = this.mBinder.getVolumeAttributes();
            MediaControllerCompat.PlaybackInfo var3 = new MediaControllerCompat.PlaybackInfo(var1.volumeType, var1.audioStream, var1.controlType, var1.maxVolume, var1.currentVolume);
            return var3;
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getPlaybackInfo.", var2);
            return null;
         }
      }

      public PlaybackStateCompat getPlaybackState() {
         try {
            PlaybackStateCompat var1 = this.mBinder.getPlaybackState();
            return var1;
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getPlaybackState.", var2);
            return null;
         }
      }

      public List getQueue() {
         try {
            List var1 = this.mBinder.getQueue();
            return var1;
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getQueue.", var2);
            return null;
         }
      }

      public CharSequence getQueueTitle() {
         try {
            CharSequence var1 = this.mBinder.getQueueTitle();
            return var1;
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getQueueTitle.", var2);
            return null;
         }
      }

      public int getRatingType() {
         try {
            int var1 = this.mBinder.getRatingType();
            return var1;
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in getRatingType.", var3);
            return 0;
         }
      }

      public int getRepeatMode() {
         try {
            int var1 = this.mBinder.getRepeatMode();
            return var1;
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in getRepeatMode.", var3);
            return -1;
         }
      }

      public PendingIntent getSessionActivity() {
         try {
            PendingIntent var1 = this.mBinder.getLaunchPendingIntent();
            return var1;
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in getSessionActivity.", var2);
            return null;
         }
      }

      public int getShuffleMode() {
         try {
            int var1 = this.mBinder.getShuffleMode();
            return var1;
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in getShuffleMode.", var3);
            return -1;
         }
      }

      public MediaControllerCompat.TransportControls getTransportControls() {
         if (this.mTransportControls == null) {
            this.mTransportControls = new MediaControllerCompat.TransportControlsBase(this.mBinder);
         }

         return this.mTransportControls;
      }

      public boolean isCaptioningEnabled() {
         try {
            boolean var1 = this.mBinder.isCaptioningEnabled();
            return var1;
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in isCaptioningEnabled.", var3);
            return false;
         }
      }

      public boolean isSessionReady() {
         return true;
      }

      public void registerCallback(MediaControllerCompat.Callback var1, Handler var2) {
         if (var1 != null) {
            try {
               this.mBinder.asBinder().linkToDeath(var1, 0);
               this.mBinder.registerCallbackListener((IMediaControllerCallback)var1.mCallbackObj);
               var1.postToHandler(13, (Object)null, (Bundle)null);
            } catch (RemoteException var3) {
               Log.e("MediaControllerCompat", "Dead object in registerCallback.", var3);
               var1.postToHandler(8, (Object)null, (Bundle)null);
            }
         } else {
            throw new IllegalArgumentException("callback may not be null.");
         }
      }

      public void removeQueueItem(MediaDescriptionCompat var1) {
         try {
            if ((4L & this.mBinder.getFlags()) != 0L) {
               this.mBinder.removeQueueItem(var1);
            } else {
               throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in removeQueueItem.", var2);
         }
      }

      public void sendCommand(String var1, Bundle var2, ResultReceiver var3) {
         try {
            this.mBinder.sendCommand(var1, var2, new MediaSessionCompat.ResultReceiverWrapper(var3));
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in sendCommand.", var4);
         }
      }

      public void setVolumeTo(int var1, int var2) {
         try {
            this.mBinder.setVolumeTo(var1, var2, (String)null);
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in setVolumeTo.", var4);
         }
      }

      public void unregisterCallback(MediaControllerCompat.Callback var1) {
         if (var1 != null) {
            try {
               this.mBinder.unregisterCallbackListener((IMediaControllerCallback)var1.mCallbackObj);
               this.mBinder.asBinder().unlinkToDeath(var1, 0);
            } catch (RemoteException var2) {
               Log.e("MediaControllerCompat", "Dead object in unregisterCallback.", var2);
            }
         } else {
            throw new IllegalArgumentException("callback may not be null.");
         }
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

      PlaybackInfo(int var1, int var2, int var3, int var4, int var5) {
         this.mPlaybackType = var1;
         this.mAudioStream = var2;
         this.mVolumeControl = var3;
         this.mMaxVolume = var4;
         this.mCurrentVolume = var5;
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

   public abstract static class TransportControls {
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

   static class TransportControlsApi21 extends MediaControllerCompat.TransportControls {
      protected final Object mControlsObj;

      public TransportControlsApi21(Object var1) {
         this.mControlsObj = var1;
      }

      public void fastForward() {
         MediaControllerCompatApi21.TransportControls.fastForward(this.mControlsObj);
      }

      public void pause() {
         MediaControllerCompatApi21.TransportControls.pause(this.mControlsObj);
      }

      public void play() {
         MediaControllerCompatApi21.TransportControls.play(this.mControlsObj);
      }

      public void playFromMediaId(String var1, Bundle var2) {
         MediaControllerCompatApi21.TransportControls.playFromMediaId(this.mControlsObj, var1, var2);
      }

      public void playFromSearch(String var1, Bundle var2) {
         MediaControllerCompatApi21.TransportControls.playFromSearch(this.mControlsObj, var1, var2);
      }

      public void playFromUri(Uri var1, Bundle var2) {
         if (var1 != null && !Uri.EMPTY.equals(var1)) {
            Bundle var3 = new Bundle();
            var3.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", var1);
            var3.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
            this.sendCustomAction("android.support.v4.media.session.action.PLAY_FROM_URI", var3);
         } else {
            throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
         }
      }

      public void prepare() {
         this.sendCustomAction((String)"android.support.v4.media.session.action.PREPARE", (Bundle)null);
      }

      public void prepareFromMediaId(String var1, Bundle var2) {
         Bundle var3 = new Bundle();
         var3.putString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID", var1);
         var3.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
         this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID", var3);
      }

      public void prepareFromSearch(String var1, Bundle var2) {
         Bundle var3 = new Bundle();
         var3.putString("android.support.v4.media.session.action.ARGUMENT_QUERY", var1);
         var3.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
         this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_SEARCH", var3);
      }

      public void prepareFromUri(Uri var1, Bundle var2) {
         Bundle var3 = new Bundle();
         var3.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", var1);
         var3.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
         this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_URI", var3);
      }

      public void rewind() {
         MediaControllerCompatApi21.TransportControls.rewind(this.mControlsObj);
      }

      public void seekTo(long var1) {
         MediaControllerCompatApi21.TransportControls.seekTo(this.mControlsObj, var1);
      }

      public void sendCustomAction(PlaybackStateCompat.CustomAction var1, Bundle var2) {
         MediaControllerCompat.validateCustomAction(var1.getAction(), var2);
         MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, var1.getAction(), var2);
      }

      public void sendCustomAction(String var1, Bundle var2) {
         MediaControllerCompat.validateCustomAction(var1, var2);
         MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, var1, var2);
      }

      public void setCaptioningEnabled(boolean var1) {
         Bundle var2 = new Bundle();
         var2.putBoolean("android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED", var1);
         this.sendCustomAction("android.support.v4.media.session.action.SET_CAPTIONING_ENABLED", var2);
      }

      public void setRating(RatingCompat var1) {
         Object var2 = this.mControlsObj;
         Object var3;
         if (var1 != null) {
            var3 = var1.getRating();
         } else {
            var3 = null;
         }

         MediaControllerCompatApi21.TransportControls.setRating(var2, var3);
      }

      public void setRating(RatingCompat var1, Bundle var2) {
         Bundle var3 = new Bundle();
         var3.putParcelable("android.support.v4.media.session.action.ARGUMENT_RATING", var1);
         var3.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", var2);
         this.sendCustomAction("android.support.v4.media.session.action.SET_RATING", var3);
      }

      public void setRepeatMode(int var1) {
         Bundle var2 = new Bundle();
         var2.putInt("android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE", var1);
         this.sendCustomAction("android.support.v4.media.session.action.SET_REPEAT_MODE", var2);
      }

      public void setShuffleMode(int var1) {
         Bundle var2 = new Bundle();
         var2.putInt("android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE", var1);
         this.sendCustomAction("android.support.v4.media.session.action.SET_SHUFFLE_MODE", var2);
      }

      public void skipToNext() {
         MediaControllerCompatApi21.TransportControls.skipToNext(this.mControlsObj);
      }

      public void skipToPrevious() {
         MediaControllerCompatApi21.TransportControls.skipToPrevious(this.mControlsObj);
      }

      public void skipToQueueItem(long var1) {
         MediaControllerCompatApi21.TransportControls.skipToQueueItem(this.mControlsObj, var1);
      }

      public void stop() {
         MediaControllerCompatApi21.TransportControls.stop(this.mControlsObj);
      }
   }

   static class TransportControlsApi23 extends MediaControllerCompat.TransportControlsApi21 {
      public TransportControlsApi23(Object var1) {
         super(var1);
      }

      public void playFromUri(Uri var1, Bundle var2) {
         MediaControllerCompatApi23.TransportControls.playFromUri(this.mControlsObj, var1, var2);
      }
   }

   static class TransportControlsApi24 extends MediaControllerCompat.TransportControlsApi23 {
      public TransportControlsApi24(Object var1) {
         super(var1);
      }

      public void prepare() {
         MediaControllerCompatApi24.TransportControls.prepare(this.mControlsObj);
      }

      public void prepareFromMediaId(String var1, Bundle var2) {
         MediaControllerCompatApi24.TransportControls.prepareFromMediaId(this.mControlsObj, var1, var2);
      }

      public void prepareFromSearch(String var1, Bundle var2) {
         MediaControllerCompatApi24.TransportControls.prepareFromSearch(this.mControlsObj, var1, var2);
      }

      public void prepareFromUri(Uri var1, Bundle var2) {
         MediaControllerCompatApi24.TransportControls.prepareFromUri(this.mControlsObj, var1, var2);
      }
   }

   static class TransportControlsBase extends MediaControllerCompat.TransportControls {
      private IMediaSession mBinder;

      public TransportControlsBase(IMediaSession var1) {
         this.mBinder = var1;
      }

      public void fastForward() {
         try {
            this.mBinder.fastForward();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in fastForward.", var2);
         }
      }

      public void pause() {
         try {
            this.mBinder.pause();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in pause.", var2);
         }
      }

      public void play() {
         try {
            this.mBinder.play();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in play.", var2);
         }
      }

      public void playFromMediaId(String var1, Bundle var2) {
         try {
            this.mBinder.playFromMediaId(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in playFromMediaId.", var3);
         }
      }

      public void playFromSearch(String var1, Bundle var2) {
         try {
            this.mBinder.playFromSearch(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in playFromSearch.", var3);
         }
      }

      public void playFromUri(Uri var1, Bundle var2) {
         try {
            this.mBinder.playFromUri(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in playFromUri.", var3);
         }
      }

      public void prepare() {
         try {
            this.mBinder.prepare();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in prepare.", var2);
         }
      }

      public void prepareFromMediaId(String var1, Bundle var2) {
         try {
            this.mBinder.prepareFromMediaId(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in prepareFromMediaId.", var3);
         }
      }

      public void prepareFromSearch(String var1, Bundle var2) {
         try {
            this.mBinder.prepareFromSearch(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in prepareFromSearch.", var3);
         }
      }

      public void prepareFromUri(Uri var1, Bundle var2) {
         try {
            this.mBinder.prepareFromUri(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in prepareFromUri.", var3);
         }
      }

      public void rewind() {
         try {
            this.mBinder.rewind();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in rewind.", var2);
         }
      }

      public void seekTo(long var1) {
         try {
            this.mBinder.seekTo(var1);
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in seekTo.", var4);
         }
      }

      public void sendCustomAction(PlaybackStateCompat.CustomAction var1, Bundle var2) {
         this.sendCustomAction(var1.getAction(), var2);
      }

      public void sendCustomAction(String var1, Bundle var2) {
         MediaControllerCompat.validateCustomAction(var1, var2);

         try {
            this.mBinder.sendCustomAction(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in sendCustomAction.", var3);
         }
      }

      public void setCaptioningEnabled(boolean var1) {
         try {
            this.mBinder.setCaptioningEnabled(var1);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in setCaptioningEnabled.", var3);
         }
      }

      public void setRating(RatingCompat var1) {
         try {
            this.mBinder.rate(var1);
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in setRating.", var2);
         }
      }

      public void setRating(RatingCompat var1, Bundle var2) {
         try {
            this.mBinder.rateWithExtras(var1, var2);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in setRating.", var3);
         }
      }

      public void setRepeatMode(int var1) {
         try {
            this.mBinder.setRepeatMode(var1);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in setRepeatMode.", var3);
         }
      }

      public void setShuffleMode(int var1) {
         try {
            this.mBinder.setShuffleMode(var1);
         } catch (RemoteException var3) {
            Log.e("MediaControllerCompat", "Dead object in setShuffleMode.", var3);
         }
      }

      public void skipToNext() {
         try {
            this.mBinder.next();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in skipToNext.", var2);
         }
      }

      public void skipToPrevious() {
         try {
            this.mBinder.previous();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in skipToPrevious.", var2);
         }
      }

      public void skipToQueueItem(long var1) {
         try {
            this.mBinder.skipToQueueItem(var1);
         } catch (RemoteException var4) {
            Log.e("MediaControllerCompat", "Dead object in skipToQueueItem.", var4);
         }
      }

      public void stop() {
         try {
            this.mBinder.stop();
         } catch (RemoteException var2) {
            Log.e("MediaControllerCompat", "Dead object in stop.", var2);
         }
      }
   }
}
