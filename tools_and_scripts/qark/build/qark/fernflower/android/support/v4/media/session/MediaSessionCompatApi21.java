package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.VolumeProvider;
import android.media.AudioAttributes.Builder;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.media.session.MediaSession.Token;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class MediaSessionCompatApi21 {
   static final String TAG = "MediaSessionCompatApi21";

   private MediaSessionCompatApi21() {
   }

   public static Object createCallback(MediaSessionCompatApi21.Callback var0) {
      return new MediaSessionCompatApi21.CallbackProxy(var0);
   }

   public static Object createSession(Context var0, String var1) {
      return new MediaSession(var0, var1);
   }

   public static Parcelable getSessionToken(Object var0) {
      return ((MediaSession)var0).getSessionToken();
   }

   public static boolean hasCallback(Object var0) {
      boolean var1 = false;

      label47: {
         label46: {
            boolean var10001;
            Field var2;
            try {
               var2 = var0.getClass().getDeclaredField("mCallback");
            } catch (NoSuchFieldException var5) {
               var10001 = false;
               break label46;
            } catch (IllegalAccessException var6) {
               var10001 = false;
               break label46;
            }

            if (var2 == null) {
               return false;
            }

            try {
               var2.setAccessible(true);
               var0 = var2.get(var0);
               break label47;
            } catch (NoSuchFieldException var3) {
               var10001 = false;
            } catch (IllegalAccessException var4) {
               var10001 = false;
            }
         }

         Log.w("MediaSessionCompatApi21", "Failed to get mCallback object.");
         return false;
      }

      if (var0 != null) {
         var1 = true;
      }

      return var1;
   }

   public static boolean isActive(Object var0) {
      return ((MediaSession)var0).isActive();
   }

   public static void release(Object var0) {
      ((MediaSession)var0).release();
   }

   public static void sendSessionEvent(Object var0, String var1, Bundle var2) {
      ((MediaSession)var0).sendSessionEvent(var1, var2);
   }

   public static void setActive(Object var0, boolean var1) {
      ((MediaSession)var0).setActive(var1);
   }

   public static void setCallback(Object var0, Object var1, Handler var2) {
      ((MediaSession)var0).setCallback((android.media.session.MediaSession.Callback)var1, var2);
   }

   public static void setExtras(Object var0, Bundle var1) {
      ((MediaSession)var0).setExtras(var1);
   }

   public static void setFlags(Object var0, int var1) {
      ((MediaSession)var0).setFlags(var1);
   }

   public static void setMediaButtonReceiver(Object var0, PendingIntent var1) {
      ((MediaSession)var0).setMediaButtonReceiver(var1);
   }

   public static void setMetadata(Object var0, Object var1) {
      ((MediaSession)var0).setMetadata((MediaMetadata)var1);
   }

   public static void setPlaybackState(Object var0, Object var1) {
      ((MediaSession)var0).setPlaybackState((PlaybackState)var1);
   }

   public static void setPlaybackToLocal(Object var0, int var1) {
      Builder var2 = new Builder();
      var2.setLegacyStreamType(var1);
      ((MediaSession)var0).setPlaybackToLocal(var2.build());
   }

   public static void setPlaybackToRemote(Object var0, Object var1) {
      ((MediaSession)var0).setPlaybackToRemote((VolumeProvider)var1);
   }

   public static void setQueue(Object var0, List var1) {
      if (var1 == null) {
         ((MediaSession)var0).setQueue((List)null);
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            var2.add((android.media.session.MediaSession.QueueItem)var3.next());
         }

         ((MediaSession)var0).setQueue(var2);
      }
   }

   public static void setQueueTitle(Object var0, CharSequence var1) {
      ((MediaSession)var0).setQueueTitle(var1);
   }

   public static void setSessionActivity(Object var0, PendingIntent var1) {
      ((MediaSession)var0).setSessionActivity(var1);
   }

   public static Object verifySession(Object var0) {
      if (var0 instanceof MediaSession) {
         return var0;
      } else {
         throw new IllegalArgumentException("mediaSession is not a valid MediaSession object");
      }
   }

   public static Object verifyToken(Object var0) {
      if (var0 instanceof Token) {
         return var0;
      } else {
         throw new IllegalArgumentException("token is not a valid MediaSession.Token object");
      }
   }

   interface Callback {
      void onCommand(String var1, Bundle var2, ResultReceiver var3);

      void onCustomAction(String var1, Bundle var2);

      void onFastForward();

      boolean onMediaButtonEvent(Intent var1);

      void onPause();

      void onPlay();

      void onPlayFromMediaId(String var1, Bundle var2);

      void onPlayFromSearch(String var1, Bundle var2);

      void onRewind();

      void onSeekTo(long var1);

      void onSetRating(Object var1);

      void onSetRating(Object var1, Bundle var2);

      void onSkipToNext();

      void onSkipToPrevious();

      void onSkipToQueueItem(long var1);

      void onStop();
   }

   static class CallbackProxy extends android.media.session.MediaSession.Callback {
      protected final MediaSessionCompatApi21.Callback mCallback;

      public CallbackProxy(MediaSessionCompatApi21.Callback var1) {
         this.mCallback = var1;
      }

      public void onCommand(String var1, Bundle var2, ResultReceiver var3) {
         MediaSessionCompat.ensureClassLoader(var2);
         this.mCallback.onCommand(var1, var2, var3);
      }

      public void onCustomAction(String var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         this.mCallback.onCustomAction(var1, var2);
      }

      public void onFastForward() {
         this.mCallback.onFastForward();
      }

      public boolean onMediaButtonEvent(Intent var1) {
         return this.mCallback.onMediaButtonEvent(var1) || super.onMediaButtonEvent(var1);
      }

      public void onPause() {
         this.mCallback.onPause();
      }

      public void onPlay() {
         this.mCallback.onPlay();
      }

      public void onPlayFromMediaId(String var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         this.mCallback.onPlayFromMediaId(var1, var2);
      }

      public void onPlayFromSearch(String var1, Bundle var2) {
         MediaSessionCompat.ensureClassLoader(var2);
         this.mCallback.onPlayFromSearch(var1, var2);
      }

      public void onRewind() {
         this.mCallback.onRewind();
      }

      public void onSeekTo(long var1) {
         this.mCallback.onSeekTo(var1);
      }

      public void onSetRating(Rating var1) {
         this.mCallback.onSetRating(var1);
      }

      public void onSkipToNext() {
         this.mCallback.onSkipToNext();
      }

      public void onSkipToPrevious() {
         this.mCallback.onSkipToPrevious();
      }

      public void onSkipToQueueItem(long var1) {
         this.mCallback.onSkipToQueueItem(var1);
      }

      public void onStop() {
         this.mCallback.onStop();
      }
   }

   static class QueueItem {
      private QueueItem() {
      }

      public static Object createItem(Object var0, long var1) {
         return new android.media.session.MediaSession.QueueItem((MediaDescription)var0, var1);
      }

      public static Object getDescription(Object var0) {
         return ((android.media.session.MediaSession.QueueItem)var0).getDescription();
      }

      public static long getQueueId(Object var0) {
         return ((android.media.session.MediaSession.QueueItem)var0).getQueueId();
      }
   }
}
