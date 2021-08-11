package androidx.core.content;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobScheduler;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.RestrictionsManager;
import android.content.pm.LauncherApps;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.hardware.ConsumerIrManager;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class ContextCompat {
   private static final String TAG = "ContextCompat";
   private static final Object sLock = new Object();
   private static TypedValue sTempValue;

   protected ContextCompat() {
   }

   public static int checkSelfPermission(Context var0, String var1) {
      if (var1 != null) {
         return var0.checkPermission(var1, Process.myPid(), Process.myUid());
      } else {
         throw new IllegalArgumentException("permission is null");
      }
   }

   public static Context createDeviceProtectedStorageContext(Context var0) {
      return VERSION.SDK_INT >= 24 ? var0.createDeviceProtectedStorageContext() : null;
   }

   private static File createFilesDir(File var0) {
      synchronized(ContextCompat.class){}

      label103: {
         Throwable var10000;
         label107: {
            boolean var1;
            boolean var10001;
            try {
               if (var0.exists() || var0.mkdirs()) {
                  break label103;
               }

               var1 = var0.exists();
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break label107;
            }

            if (var1) {
               return var0;
            }

            try {
               StringBuilder var2 = new StringBuilder();
               var2.append("Unable to create files subdir ");
               var2.append(var0.getPath());
               Log.w("ContextCompat", var2.toString());
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label107;
            }

            return null;
         }

         Throwable var9 = var10000;
         throw var9;
      }

      return var0;
   }

   public static File getCodeCacheDir(Context var0) {
      return VERSION.SDK_INT >= 21 ? var0.getCodeCacheDir() : createFilesDir(new File(var0.getApplicationInfo().dataDir, "code_cache"));
   }

   public static int getColor(Context var0, int var1) {
      return VERSION.SDK_INT >= 23 ? var0.getColor(var1) : var0.getResources().getColor(var1);
   }

   public static ColorStateList getColorStateList(Context var0, int var1) {
      return VERSION.SDK_INT >= 23 ? var0.getColorStateList(var1) : var0.getResources().getColorStateList(var1);
   }

   public static File getDataDir(Context var0) {
      if (VERSION.SDK_INT >= 24) {
         return var0.getDataDir();
      } else {
         String var1 = var0.getApplicationInfo().dataDir;
         return var1 != null ? new File(var1) : null;
      }
   }

   public static Drawable getDrawable(Context var0, int var1) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getDrawable(var1);
      } else if (VERSION.SDK_INT >= 16) {
         return var0.getResources().getDrawable(var1);
      } else {
         Object var2 = sLock;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label160: {
            try {
               if (sTempValue == null) {
                  sTempValue = new TypedValue();
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label160;
            }

            label157:
            try {
               var0.getResources().getValue(var1, sTempValue, true);
               var1 = sTempValue.resourceId;
               return var0.getResources().getDrawable(var1);
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label157;
            }
         }

         while(true) {
            Throwable var15 = var10000;

            try {
               throw var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static File[] getExternalCacheDirs(Context var0) {
      return VERSION.SDK_INT >= 19 ? var0.getExternalCacheDirs() : new File[]{var0.getExternalCacheDir()};
   }

   public static File[] getExternalFilesDirs(Context var0, String var1) {
      return VERSION.SDK_INT >= 19 ? var0.getExternalFilesDirs(var1) : new File[]{var0.getExternalFilesDir(var1)};
   }

   public static Executor getMainExecutor(Context var0) {
      return (Executor)(VERSION.SDK_INT >= 28 ? var0.getMainExecutor() : new ContextCompat.MainHandlerExecutor(new Handler(var0.getMainLooper())));
   }

   public static File getNoBackupFilesDir(Context var0) {
      return VERSION.SDK_INT >= 21 ? var0.getNoBackupFilesDir() : createFilesDir(new File(var0.getApplicationInfo().dataDir, "no_backup"));
   }

   public static File[] getObbDirs(Context var0) {
      return VERSION.SDK_INT >= 19 ? var0.getObbDirs() : new File[]{var0.getObbDir()};
   }

   public static Object getSystemService(Context var0, Class var1) {
      if (VERSION.SDK_INT >= 23) {
         return var0.getSystemService(var1);
      } else {
         String var2 = getSystemServiceName(var0, var1);
         return var2 != null ? var0.getSystemService(var2) : null;
      }
   }

   public static String getSystemServiceName(Context var0, Class var1) {
      return VERSION.SDK_INT >= 23 ? var0.getSystemServiceName(var1) : (String)ContextCompat.LegacyServiceMapHolder.SERVICES.get(var1);
   }

   public static boolean isDeviceProtectedStorage(Context var0) {
      return VERSION.SDK_INT >= 24 ? var0.isDeviceProtectedStorage() : false;
   }

   public static boolean startActivities(Context var0, Intent[] var1) {
      return startActivities(var0, var1, (Bundle)null);
   }

   public static boolean startActivities(Context var0, Intent[] var1, Bundle var2) {
      if (VERSION.SDK_INT >= 16) {
         var0.startActivities(var1, var2);
      } else {
         var0.startActivities(var1);
      }

      return true;
   }

   public static void startActivity(Context var0, Intent var1, Bundle var2) {
      if (VERSION.SDK_INT >= 16) {
         var0.startActivity(var1, var2);
      } else {
         var0.startActivity(var1);
      }
   }

   public static void startForegroundService(Context var0, Intent var1) {
      if (VERSION.SDK_INT >= 26) {
         var0.startForegroundService(var1);
      } else {
         var0.startService(var1);
      }
   }

   private static final class LegacyServiceMapHolder {
      static final HashMap SERVICES = new HashMap();

      static {
         if (VERSION.SDK_INT >= 22) {
            SERVICES.put(SubscriptionManager.class, "telephony_subscription_service");
            SERVICES.put(UsageStatsManager.class, "usagestats");
         }

         if (VERSION.SDK_INT >= 21) {
            SERVICES.put(AppWidgetManager.class, "appwidget");
            SERVICES.put(BatteryManager.class, "batterymanager");
            SERVICES.put(CameraManager.class, "camera");
            SERVICES.put(JobScheduler.class, "jobscheduler");
            SERVICES.put(LauncherApps.class, "launcherapps");
            SERVICES.put(MediaProjectionManager.class, "media_projection");
            SERVICES.put(MediaSessionManager.class, "media_session");
            SERVICES.put(RestrictionsManager.class, "restrictions");
            SERVICES.put(TelecomManager.class, "telecom");
            SERVICES.put(TvInputManager.class, "tv_input");
         }

         if (VERSION.SDK_INT >= 19) {
            SERVICES.put(AppOpsManager.class, "appops");
            SERVICES.put(CaptioningManager.class, "captioning");
            SERVICES.put(ConsumerIrManager.class, "consumer_ir");
            SERVICES.put(PrintManager.class, "print");
         }

         if (VERSION.SDK_INT >= 18) {
            SERVICES.put(BluetoothManager.class, "bluetooth");
         }

         if (VERSION.SDK_INT >= 17) {
            SERVICES.put(DisplayManager.class, "display");
            SERVICES.put(UserManager.class, "user");
         }

         if (VERSION.SDK_INT >= 16) {
            SERVICES.put(InputManager.class, "input");
            SERVICES.put(MediaRouter.class, "media_router");
            SERVICES.put(NsdManager.class, "servicediscovery");
         }

         SERVICES.put(AccessibilityManager.class, "accessibility");
         SERVICES.put(AccountManager.class, "account");
         SERVICES.put(ActivityManager.class, "activity");
         SERVICES.put(AlarmManager.class, "alarm");
         SERVICES.put(AudioManager.class, "audio");
         SERVICES.put(ClipboardManager.class, "clipboard");
         SERVICES.put(ConnectivityManager.class, "connectivity");
         SERVICES.put(DevicePolicyManager.class, "device_policy");
         SERVICES.put(DownloadManager.class, "download");
         SERVICES.put(DropBoxManager.class, "dropbox");
         SERVICES.put(InputMethodManager.class, "input_method");
         SERVICES.put(KeyguardManager.class, "keyguard");
         SERVICES.put(LayoutInflater.class, "layout_inflater");
         SERVICES.put(LocationManager.class, "location");
         SERVICES.put(NfcManager.class, "nfc");
         SERVICES.put(NotificationManager.class, "notification");
         SERVICES.put(PowerManager.class, "power");
         SERVICES.put(SearchManager.class, "search");
         SERVICES.put(SensorManager.class, "sensor");
         SERVICES.put(StorageManager.class, "storage");
         SERVICES.put(TelephonyManager.class, "phone");
         SERVICES.put(TextServicesManager.class, "textservices");
         SERVICES.put(UiModeManager.class, "uimode");
         SERVICES.put(UsbManager.class, "usb");
         SERVICES.put(Vibrator.class, "vibrator");
         SERVICES.put(WallpaperManager.class, "wallpaper");
         SERVICES.put(WifiP2pManager.class, "wifip2p");
         SERVICES.put(WifiManager.class, "wifi");
         SERVICES.put(WindowManager.class, "window");
      }
   }

   private static class MainHandlerExecutor implements Executor {
      private final Handler mHandler;

      MainHandlerExecutor(Handler var1) {
         this.mHandler = var1;
      }

      public void execute(Runnable var1) {
         if (!this.mHandler.post(var1)) {
            StringBuilder var2 = new StringBuilder();
            var2.append(this.mHandler);
            var2.append(" is shutting down");
            throw new RejectedExecutionException(var2.toString());
         }
      }
   }
}
