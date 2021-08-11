/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.accounts.AccountManager
 *  android.app.ActivityManager
 *  android.app.AlarmManager
 *  android.app.AppOpsManager
 *  android.app.DownloadManager
 *  android.app.KeyguardManager
 *  android.app.NotificationManager
 *  android.app.SearchManager
 *  android.app.UiModeManager
 *  android.app.WallpaperManager
 *  android.app.admin.DevicePolicyManager
 *  android.app.job.JobScheduler
 *  android.app.usage.UsageStatsManager
 *  android.appwidget.AppWidgetManager
 *  android.bluetooth.BluetoothManager
 *  android.content.ClipboardManager
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.RestrictionsManager
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.LauncherApps
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.hardware.ConsumerIrManager
 *  android.hardware.SensorManager
 *  android.hardware.camera2.CameraManager
 *  android.hardware.display.DisplayManager
 *  android.hardware.input.InputManager
 *  android.hardware.usb.UsbManager
 *  android.location.LocationManager
 *  android.media.AudioManager
 *  android.media.MediaRouter
 *  android.media.projection.MediaProjectionManager
 *  android.media.session.MediaSessionManager
 *  android.media.tv.TvInputManager
 *  android.net.ConnectivityManager
 *  android.net.nsd.NsdManager
 *  android.net.wifi.WifiManager
 *  android.net.wifi.p2p.WifiP2pManager
 *  android.nfc.NfcManager
 *  android.os.BatteryManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.DropBoxManager
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.PowerManager
 *  android.os.Process
 *  android.os.UserManager
 *  android.os.Vibrator
 *  android.os.storage.StorageManager
 *  android.print.PrintManager
 *  android.telecom.TelecomManager
 *  android.telephony.SubscriptionManager
 *  android.telephony.TelephonyManager
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.LayoutInflater
 *  android.view.WindowManager
 *  android.view.accessibility.AccessibilityManager
 *  android.view.accessibility.CaptioningManager
 *  android.view.inputmethod.InputMethodManager
 *  android.view.textservice.TextServicesManager
 */
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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.RestrictionsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.res.ColorStateList;
import android.content.res.Resources;
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
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Process;
import android.os.UserManager;
import android.os.Vibrator;
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

    public static int checkSelfPermission(Context context, String string2) {
        if (string2 != null) {
            return context.checkPermission(string2, Process.myPid(), Process.myUid());
        }
        throw new IllegalArgumentException("permission is null");
    }

    public static Context createDeviceProtectedStorageContext(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return context.createDeviceProtectedStorageContext();
        }
        return null;
    }

    private static File createFilesDir(File file) {
        synchronized (ContextCompat.class) {
            block5 : {
                block6 : {
                    if (file.exists() || file.mkdirs()) break block5;
                    boolean bl = file.exists();
                    if (!bl) break block6;
                    return file;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to create files subdir ");
                stringBuilder.append(file.getPath());
                Log.w((String)"ContextCompat", (String)stringBuilder.toString());
                return null;
            }
            return file;
            finally {
            }
        }
    }

    public static File getCodeCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            return context.getCodeCacheDir();
        }
        return ContextCompat.createFilesDir(new File(context.getApplicationInfo().dataDir, "code_cache"));
    }

    public static int getColor(Context context, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColor(n);
        }
        return context.getResources().getColor(n);
    }

    public static ColorStateList getColorStateList(Context context, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        return context.getResources().getColorStateList(n);
    }

    public static File getDataDir(Context object) {
        if (Build.VERSION.SDK_INT >= 24) {
            return object.getDataDir();
        }
        object = object.getApplicationInfo().dataDir;
        if (object != null) {
            return new File((String)object);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Drawable getDrawable(Context context, int n) {
        if (Build.VERSION.SDK_INT >= 21) {
            return context.getDrawable(n);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return context.getResources().getDrawable(n);
        }
        Object object = sLock;
        synchronized (object) {
            if (sTempValue == null) {
                sTempValue = new TypedValue();
            }
            context.getResources().getValue(n, sTempValue, true);
            n = ContextCompat.sTempValue.resourceId;
            return context.getResources().getDrawable(n);
        }
    }

    public static File[] getExternalCacheDirs(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return context.getExternalCacheDirs();
        }
        return new File[]{context.getExternalCacheDir()};
    }

    public static File[] getExternalFilesDirs(Context context, String string2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return context.getExternalFilesDirs(string2);
        }
        return new File[]{context.getExternalFilesDir(string2)};
    }

    public static Executor getMainExecutor(Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            return context.getMainExecutor();
        }
        return new MainHandlerExecutor(new Handler(context.getMainLooper()));
    }

    public static File getNoBackupFilesDir(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            return context.getNoBackupFilesDir();
        }
        return ContextCompat.createFilesDir(new File(context.getApplicationInfo().dataDir, "no_backup"));
    }

    public static File[] getObbDirs(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return context.getObbDirs();
        }
        return new File[]{context.getObbDir()};
    }

    public static <T> T getSystemService(Context context, Class<T> object) {
        if (Build.VERSION.SDK_INT >= 23) {
            return (T)context.getSystemService(object);
        }
        if ((object = ContextCompat.getSystemServiceName(context, object)) != null) {
            return (T)context.getSystemService((String)object);
        }
        return null;
    }

    public static String getSystemServiceName(Context context, Class<?> class_) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getSystemServiceName(class_);
        }
        return LegacyServiceMapHolder.SERVICES.get(class_);
    }

    public static boolean isDeviceProtectedStorage(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return context.isDeviceProtectedStorage();
        }
        return false;
    }

    public static boolean startActivities(Context context, Intent[] arrintent) {
        return ContextCompat.startActivities(context, arrintent, null);
    }

    public static boolean startActivities(Context context, Intent[] arrintent, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            context.startActivities(arrintent, bundle);
        } else {
            context.startActivities(arrintent);
        }
        return true;
    }

    public static void startActivity(Context context, Intent intent, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            context.startActivity(intent, bundle);
            return;
        }
        context.startActivity(intent);
    }

    public static void startForegroundService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
            return;
        }
        context.startService(intent);
    }

    private static final class LegacyServiceMapHolder {
        static final HashMap<Class<?>, String> SERVICES = new HashMap();

        static {
            if (Build.VERSION.SDK_INT >= 22) {
                SERVICES.put(SubscriptionManager.class, "telephony_subscription_service");
                SERVICES.put(UsageStatsManager.class, "usagestats");
            }
            if (Build.VERSION.SDK_INT >= 21) {
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
            if (Build.VERSION.SDK_INT >= 19) {
                SERVICES.put(AppOpsManager.class, "appops");
                SERVICES.put(CaptioningManager.class, "captioning");
                SERVICES.put(ConsumerIrManager.class, "consumer_ir");
                SERVICES.put(PrintManager.class, "print");
            }
            if (Build.VERSION.SDK_INT >= 18) {
                SERVICES.put(BluetoothManager.class, "bluetooth");
            }
            if (Build.VERSION.SDK_INT >= 17) {
                SERVICES.put(DisplayManager.class, "display");
                SERVICES.put(UserManager.class, "user");
            }
            if (Build.VERSION.SDK_INT >= 16) {
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

        private LegacyServiceMapHolder() {
        }
    }

    private static class MainHandlerExecutor
    implements Executor {
        private final Handler mHandler;

        MainHandlerExecutor(Handler handler) {
            this.mHandler = handler;
        }

        @Override
        public void execute(Runnable object) {
            if (this.mHandler.post((Runnable)object)) {
                return;
            }
            object = new StringBuilder();
            object.append((Object)this.mHandler);
            object.append(" is shutting down");
            throw new RejectedExecutionException(object.toString());
        }
    }

}

