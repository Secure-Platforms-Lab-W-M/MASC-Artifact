package androidx.media.session;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver.PendingResult;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Build.VERSION;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;
import java.util.List;

public class MediaButtonReceiver extends BroadcastReceiver {
   private static final String TAG = "MediaButtonReceiver";

   public static PendingIntent buildMediaButtonPendingIntent(Context var0, long var1) {
      ComponentName var3 = getMediaButtonReceiverComponent(var0);
      if (var3 == null) {
         Log.w("MediaButtonReceiver", "A unique media button receiver could not be found in the given context, so couldn't build a pending intent.");
         return null;
      } else {
         return buildMediaButtonPendingIntent(var0, var3, var1);
      }
   }

   public static PendingIntent buildMediaButtonPendingIntent(Context var0, ComponentName var1, long var2) {
      if (var1 == null) {
         Log.w("MediaButtonReceiver", "The component name of media button receiver should be provided.");
         return null;
      } else {
         int var4 = PlaybackStateCompat.toKeyCode(var2);
         if (var4 == 0) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Cannot build a media button pending intent with the given action: ");
            var6.append(var2);
            Log.w("MediaButtonReceiver", var6.toString());
            return null;
         } else {
            Intent var5 = new Intent("android.intent.action.MEDIA_BUTTON");
            var5.setComponent(var1);
            var5.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, var4));
            return PendingIntent.getBroadcast(var0, var4, var5, 0);
         }
      }
   }

   public static ComponentName getMediaButtonReceiverComponent(Context var0) {
      Intent var1 = new Intent("android.intent.action.MEDIA_BUTTON");
      var1.setPackage(var0.getPackageName());
      List var2 = var0.getPackageManager().queryBroadcastReceivers(var1, 0);
      if (var2.size() == 1) {
         ResolveInfo var3 = (ResolveInfo)var2.get(0);
         return new ComponentName(var3.activityInfo.packageName, var3.activityInfo.name);
      } else {
         if (var2.size() > 1) {
            Log.w("MediaButtonReceiver", "More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, returning null.");
         }

         return null;
      }
   }

   private static ComponentName getServiceComponentByAction(Context var0, String var1) {
      PackageManager var2 = var0.getPackageManager();
      Intent var3 = new Intent(var1);
      var3.setPackage(var0.getPackageName());
      List var4 = var2.queryIntentServices(var3, 0);
      if (var4.size() == 1) {
         ResolveInfo var5 = (ResolveInfo)var4.get(0);
         return new ComponentName(var5.serviceInfo.packageName, var5.serviceInfo.name);
      } else if (var4.isEmpty()) {
         return null;
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("Expected 1 service that handles ");
         var6.append(var1);
         var6.append(", found ");
         var6.append(var4.size());
         throw new IllegalStateException(var6.toString());
      }
   }

   public static KeyEvent handleIntent(MediaSessionCompat var0, Intent var1) {
      if (var0 != null && var1 != null && "android.intent.action.MEDIA_BUTTON".equals(var1.getAction()) && var1.hasExtra("android.intent.extra.KEY_EVENT")) {
         KeyEvent var2 = (KeyEvent)var1.getParcelableExtra("android.intent.extra.KEY_EVENT");
         var0.getController().dispatchMediaButtonEvent(var2);
         return var2;
      } else {
         return null;
      }
   }

   private static void startForegroundService(Context var0, Intent var1) {
      if (VERSION.SDK_INT >= 26) {
         var0.startForegroundService(var1);
      } else {
         var0.startService(var1);
      }
   }

   public void onReceive(Context var1, Intent var2) {
      if (var2 != null && "android.intent.action.MEDIA_BUTTON".equals(var2.getAction()) && var2.hasExtra("android.intent.extra.KEY_EVENT")) {
         ComponentName var3 = getServiceComponentByAction(var1, "android.intent.action.MEDIA_BUTTON");
         if (var3 != null) {
            var2.setComponent(var3);
            startForegroundService(var1, var2);
         } else {
            var3 = getServiceComponentByAction(var1, "android.media.browse.MediaBrowserService");
            if (var3 != null) {
               PendingResult var4 = this.goAsync();
               var1 = var1.getApplicationContext();
               MediaButtonReceiver.MediaButtonConnectionCallback var7 = new MediaButtonReceiver.MediaButtonConnectionCallback(var1, var2, var4);
               MediaBrowserCompat var6 = new MediaBrowserCompat(var1, var3, var7, (Bundle)null);
               var7.setMediaBrowser(var6);
               var6.connect();
            } else {
               throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or implements a media browser service.");
            }
         }
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Ignore unsupported intent: ");
         var5.append(var2);
         Log.d("MediaButtonReceiver", var5.toString());
      }
   }

   private static class MediaButtonConnectionCallback extends MediaBrowserCompat.ConnectionCallback {
      private final Context mContext;
      private final Intent mIntent;
      private MediaBrowserCompat mMediaBrowser;
      private final PendingResult mPendingResult;

      MediaButtonConnectionCallback(Context var1, Intent var2, PendingResult var3) {
         this.mContext = var1;
         this.mIntent = var2;
         this.mPendingResult = var3;
      }

      private void finish() {
         this.mMediaBrowser.disconnect();
         this.mPendingResult.finish();
      }

      public void onConnected() {
         try {
            (new MediaControllerCompat(this.mContext, this.mMediaBrowser.getSessionToken())).dispatchMediaButtonEvent((KeyEvent)this.mIntent.getParcelableExtra("android.intent.extra.KEY_EVENT"));
         } catch (RemoteException var2) {
            Log.e("MediaButtonReceiver", "Failed to create a media controller", var2);
         }

         this.finish();
      }

      public void onConnectionFailed() {
         this.finish();
      }

      public void onConnectionSuspended() {
         this.finish();
      }

      void setMediaBrowser(MediaBrowserCompat var1) {
         this.mMediaBrowser = var1;
      }
   }
}
