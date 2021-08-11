package android.support.v4.media.session;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@TargetApi(14)
@RequiresApi(14)
class MediaSessionCompatApi14 {
   private static final long ACTION_FAST_FORWARD = 64L;
   private static final long ACTION_PAUSE = 2L;
   private static final long ACTION_PLAY = 4L;
   private static final long ACTION_PLAY_PAUSE = 512L;
   private static final long ACTION_REWIND = 8L;
   private static final long ACTION_SKIP_TO_NEXT = 32L;
   private static final long ACTION_SKIP_TO_PREVIOUS = 16L;
   private static final long ACTION_STOP = 1L;
   private static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
   private static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
   private static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
   private static final String METADATA_KEY_ART = "android.media.metadata.ART";
   private static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
   private static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
   private static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
   private static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
   private static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
   private static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
   private static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
   private static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
   private static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
   private static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
   private static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
   static final int RCC_PLAYSTATE_NONE = 0;
   static final int STATE_BUFFERING = 6;
   static final int STATE_CONNECTING = 8;
   static final int STATE_ERROR = 7;
   static final int STATE_FAST_FORWARDING = 4;
   static final int STATE_NONE = 0;
   static final int STATE_PAUSED = 2;
   static final int STATE_PLAYING = 3;
   static final int STATE_REWINDING = 5;
   static final int STATE_SKIPPING_TO_NEXT = 10;
   static final int STATE_SKIPPING_TO_PREVIOUS = 9;
   static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
   static final int STATE_STOPPED = 1;

   static void buildOldMetadata(Bundle var0, MetadataEditor var1) {
      if (var0 != null) {
         if (var0.containsKey("android.media.metadata.ART")) {
            var1.putBitmap(100, (Bitmap)var0.getParcelable("android.media.metadata.ART"));
         } else if (var0.containsKey("android.media.metadata.ALBUM_ART")) {
            var1.putBitmap(100, (Bitmap)var0.getParcelable("android.media.metadata.ALBUM_ART"));
         }

         if (var0.containsKey("android.media.metadata.ALBUM")) {
            var1.putString(1, var0.getString("android.media.metadata.ALBUM"));
         }

         if (var0.containsKey("android.media.metadata.ALBUM_ARTIST")) {
            var1.putString(13, var0.getString("android.media.metadata.ALBUM_ARTIST"));
         }

         if (var0.containsKey("android.media.metadata.ARTIST")) {
            var1.putString(2, var0.getString("android.media.metadata.ARTIST"));
         }

         if (var0.containsKey("android.media.metadata.AUTHOR")) {
            var1.putString(3, var0.getString("android.media.metadata.AUTHOR"));
         }

         if (var0.containsKey("android.media.metadata.COMPILATION")) {
            var1.putString(15, var0.getString("android.media.metadata.COMPILATION"));
         }

         if (var0.containsKey("android.media.metadata.COMPOSER")) {
            var1.putString(4, var0.getString("android.media.metadata.COMPOSER"));
         }

         if (var0.containsKey("android.media.metadata.DATE")) {
            var1.putString(5, var0.getString("android.media.metadata.DATE"));
         }

         if (var0.containsKey("android.media.metadata.DISC_NUMBER")) {
            var1.putLong(14, var0.getLong("android.media.metadata.DISC_NUMBER"));
         }

         if (var0.containsKey("android.media.metadata.DURATION")) {
            var1.putLong(9, var0.getLong("android.media.metadata.DURATION"));
         }

         if (var0.containsKey("android.media.metadata.GENRE")) {
            var1.putString(6, var0.getString("android.media.metadata.GENRE"));
         }

         if (var0.containsKey("android.media.metadata.TITLE")) {
            var1.putString(7, var0.getString("android.media.metadata.TITLE"));
         }

         if (var0.containsKey("android.media.metadata.TRACK_NUMBER")) {
            var1.putLong(0, var0.getLong("android.media.metadata.TRACK_NUMBER"));
         }

         if (var0.containsKey("android.media.metadata.WRITER")) {
            var1.putString(11, var0.getString("android.media.metadata.WRITER"));
            return;
         }
      }

   }

   public static Object createRemoteControlClient(PendingIntent var0) {
      return new RemoteControlClient(var0);
   }

   static int getRccStateFromState(int var0) {
      switch(var0) {
      case 0:
         return 0;
      case 1:
         return 1;
      case 2:
         return 2;
      case 3:
         return 3;
      case 4:
         return 4;
      case 5:
         return 5;
      case 6:
      case 8:
         return 8;
      case 7:
         return 9;
      case 9:
         return 7;
      case 10:
      case 11:
         return 6;
      default:
         return -1;
      }
   }

   static int getRccTransportControlFlagsFromActions(long var0) {
      int var3 = 0;
      if ((1L & var0) != 0L) {
         var3 = 0 | 32;
      }

      int var2 = var3;
      if ((2L & var0) != 0L) {
         var2 = var3 | 16;
      }

      var3 = var2;
      if ((4L & var0) != 0L) {
         var3 = var2 | 4;
      }

      var2 = var3;
      if ((8L & var0) != 0L) {
         var2 = var3 | 2;
      }

      var3 = var2;
      if ((16L & var0) != 0L) {
         var3 = var2 | 1;
      }

      var2 = var3;
      if ((32L & var0) != 0L) {
         var2 = var3 | 128;
      }

      var3 = var2;
      if ((64L & var0) != 0L) {
         var3 = var2 | 64;
      }

      var2 = var3;
      if ((512L & var0) != 0L) {
         var2 = var3 | 8;
      }

      return var2;
   }

   public static void registerRemoteControlClient(Context var0, Object var1) {
      ((AudioManager)var0.getSystemService("audio")).registerRemoteControlClient((RemoteControlClient)var1);
   }

   public static void setMetadata(Object var0, Bundle var1) {
      MetadataEditor var2 = ((RemoteControlClient)var0).editMetadata(true);
      buildOldMetadata(var1, var2);
      var2.apply();
   }

   public static void setState(Object var0, int var1) {
      ((RemoteControlClient)var0).setPlaybackState(getRccStateFromState(var1));
   }

   public static void setTransportControlFlags(Object var0, long var1) {
      ((RemoteControlClient)var0).setTransportControlFlags(getRccTransportControlFlagsFromActions(var1));
   }

   public static void unregisterRemoteControlClient(Context var0, Object var1) {
      ((AudioManager)var0.getSystemService("audio")).unregisterRemoteControlClient((RemoteControlClient)var1);
   }
}
