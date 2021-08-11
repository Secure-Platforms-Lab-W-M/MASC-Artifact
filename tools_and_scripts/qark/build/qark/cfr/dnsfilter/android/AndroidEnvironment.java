/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.wifi.WifiManager
 *  android.net.wifi.WifiManager$WifiLock
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 */
package dnsfilter.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import dnsfilter.android.DNSFilterService;
import dnsfilter.android.DNSProxyActivity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import util.ExecutionEnvironment;
import util.ExecutionEnvironmentInterface;
import util.Logger;

public class AndroidEnvironment
implements ExecutionEnvironmentInterface {
    private static AndroidEnvironment INSTANCE;
    private static Context ctx;
    private static Stack wakeLooks;

    static {
        ctx = null;
        INSTANCE = new AndroidEnvironment();
        wakeLooks = new Stack();
        ExecutionEnvironment.setEnvironment(INSTANCE);
    }

    public static void initEnvironment(Context context) {
        ctx = context;
    }

    @Override
    public boolean debug() {
        return DNSProxyActivity.debug;
    }

    @Override
    public InputStream getAsset(String string2) throws IOException {
        return ctx.getAssets().open(string2);
    }

    @Override
    public String getWorkDir() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DNSProxyActivity.WORKPATH);
        stringBuilder.append("/");
        return stringBuilder.toString();
    }

    @Override
    public boolean hasNetwork() {
        NetworkInfo networkInfo = ((ConnectivityManager)ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onReload() throws IOException {
        DNSFilterService.onReload();
    }

    @Override
    public void releaseAllWakeLocks() {
        while (!wakeLooks.isEmpty()) {
            PowerManager.WakeLock wakeLock;
            try {
                wakeLock = (PowerManager.WakeLock)wakeLooks.pop();
            }
            catch (Exception exception) {
                Logger.getLogger().logException(exception);
                return;
            }
            WifiManager.WifiLock wifiLock = (WifiManager.WifiLock)wakeLock[0];
            wakeLock = (PowerManager.WakeLock)wakeLock[1];
            wifiLock.release();
            wakeLock.release();
            Logger.getLogger().logLine("Released WIFI lock and partial wake lock!");
        }
    }

    @Override
    public void releaseWakeLock() {
        PowerManager.WakeLock wakeLock;
        try {
            wakeLock = (PowerManager.WakeLock)wakeLooks.pop();
        }
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
            return;
        }
        WifiManager.WifiLock wifiLock = (WifiManager.WifiLock)wakeLock[0];
        wakeLock = (PowerManager.WakeLock)wakeLock[1];
        wifiLock.release();
        wakeLock.release();
        Logger.getLogger().logLine("Released WIFI lock and partial wake lock!");
    }

    @Override
    public void wakeLock() {
        WifiManager.WifiLock wifiLock = ((WifiManager)ctx.getApplicationContext().getSystemService("wifi")).createWifiLock(1, "personalHttpProxy");
        wifiLock.acquire();
        PowerManager.WakeLock wakeLock = ((PowerManager)ctx.getSystemService("power")).newWakeLock(1, "personalHttpProxy");
        wakeLock.acquire();
        wakeLooks.push(new Object[]{wifiLock, wakeLock});
        Logger.getLogger().logLine("Aquired WIFI lock and partial wake lock!");
    }
}

