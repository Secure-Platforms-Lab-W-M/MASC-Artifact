package android.support.v4.media.session;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;

@TargetApi(18)
@RequiresApi(18)
class MediaSessionCompatApi18 {
   private static final long ACTION_SEEK_TO = 256L;
   private static final String TAG = "MediaSessionCompatApi18";
   private static boolean sIsMbrPendingIntentSupported = true;

   public static Object createPlaybackPositionUpdateListener(MediaSessionCompatApi18.Callback var0) {
      return new MediaSessionCompatApi18.OnPlaybackPositionUpdateListener(var0);
   }

   static int getRccTransportControlFlagsFromActions(long var0) {
      int var3 = MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(var0);
      int var2 = var3;
      if ((256L & var0) != 0L) {
         var2 = var3 | 256;
      }

      return var2;
   }

   public static void registerMediaButtonEventReceiver(Context var0, PendingIntent var1, ComponentName var2) {
      AudioManager var4 = (AudioManager)var0.getSystemService("audio");
      if (sIsMbrPendingIntentSupported) {
         try {
            var4.registerMediaButtonEventReceiver(var1);
         } catch (NullPointerException var3) {
            Log.w("MediaSessionCompatApi18", "Unable to register media button event receiver with PendingIntent, falling back to ComponentName.");
            sIsMbrPendingIntentSupported = false;
         }
      }

      if (!sIsMbrPendingIntentSupported) {
         var4.registerMediaButtonEventReceiver(var2);
      }

   }

   public static void setOnPlaybackPositionUpdateListener(Object var0, Object var1) {
      ((RemoteControlClient)var0).setPlaybackPositionUpdateListener((android.media.RemoteControlClient.OnPlaybackPositionUpdateListener)var1);
   }

   public static void setState(Object var0, int var1, long var2, float var4, long var5) {
      long var9 = SystemClock.elapsedRealtime();
      long var7 = var2;
      if (var1 == 3) {
         var7 = var2;
         if (var2 > 0L) {
            var7 = 0L;
            if (var5 > 0L) {
               var5 = var9 - var5;
               var7 = var5;
               if (var4 > 0.0F) {
                  var7 = var5;
                  if (var4 != 1.0F) {
                     var7 = (long)((float)var5 * var4);
                  }
               }
            }

            var7 += var2;
         }
      }

      var1 = MediaSessionCompatApi14.getRccStateFromState(var1);
      ((RemoteControlClient)var0).setPlaybackState(var1, var7, var4);
   }

   public static void setTransportControlFlags(Object var0, long var1) {
      ((RemoteControlClient)var0).setTransportControlFlags(getRccTransportControlFlagsFromActions(var1));
   }

   public static void unregisterMediaButtonEventReceiver(Context var0, PendingIntent var1, ComponentName var2) {
      AudioManager var3 = (AudioManager)var0.getSystemService("audio");
      if (sIsMbrPendingIntentSupported) {
         var3.unregisterMediaButtonEventReceiver(var1);
      } else {
         var3.unregisterMediaButtonEventReceiver(var2);
      }
   }

   interface Callback {
      void onSeekTo(long var1);
   }

   static class OnPlaybackPositionUpdateListener implements android.media.RemoteControlClient.OnPlaybackPositionUpdateListener {
      protected final MediaSessionCompatApi18.Callback mCallback;

      public OnPlaybackPositionUpdateListener(MediaSessionCompatApi18.Callback var1) {
         this.mCallback = var1;
      }

      public void onPlaybackPositionUpdate(long var1) {
         this.mCallback.onSeekTo(var1);
      }
   }
}
