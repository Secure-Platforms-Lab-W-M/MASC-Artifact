// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.android;

import android.os.PowerManager;
import android.net.wifi.WifiManager;
import util.Logger;
import android.os.PowerManager$WakeLock;
import android.net.wifi.WifiManager$WifiLock;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import java.io.IOException;
import java.io.InputStream;
import util.ExecutionEnvironment;
import java.util.Stack;
import android.content.Context;
import util.ExecutionEnvironmentInterface;

public class AndroidEnvironment implements ExecutionEnvironmentInterface
{
    private static AndroidEnvironment INSTANCE;
    private static Context ctx;
    private static Stack wakeLooks;
    
    static {
        AndroidEnvironment.ctx = null;
        AndroidEnvironment.INSTANCE = new AndroidEnvironment();
        AndroidEnvironment.wakeLooks = new Stack();
        ExecutionEnvironment.setEnvironment(AndroidEnvironment.INSTANCE);
    }
    
    public static void initEnvironment(final Context ctx) {
        AndroidEnvironment.ctx = ctx;
    }
    
    @Override
    public boolean debug() {
        return DNSProxyActivity.debug;
    }
    
    @Override
    public InputStream getAsset(final String s) throws IOException {
        return AndroidEnvironment.ctx.getAssets().open(s);
    }
    
    @Override
    public String getWorkDir() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DNSProxyActivity.WORKPATH);
        sb.append("/");
        return sb.toString();
    }
    
    @Override
    public boolean hasNetwork() {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)AndroidEnvironment.ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    @Override
    public void onReload() throws IOException {
        DNSFilterService.onReload();
    }
    
    @Override
    public void releaseAllWakeLocks() {
        while (!AndroidEnvironment.wakeLooks.isEmpty()) {
            try {
                final Object[] array = AndroidEnvironment.wakeLooks.pop();
                final WifiManager$WifiLock wifiManager$WifiLock = (WifiManager$WifiLock)array[0];
                final PowerManager$WakeLock powerManager$WakeLock = (PowerManager$WakeLock)array[1];
                wifiManager$WifiLock.release();
                powerManager$WakeLock.release();
                Logger.getLogger().logLine("Released WIFI lock and partial wake lock!");
                continue;
            }
            catch (Exception ex) {
                Logger.getLogger().logException(ex);
                return;
            }
            break;
        }
    }
    
    @Override
    public void releaseWakeLock() {
        try {
            final Object[] array = AndroidEnvironment.wakeLooks.pop();
            final WifiManager$WifiLock wifiManager$WifiLock = (WifiManager$WifiLock)array[0];
            final PowerManager$WakeLock powerManager$WakeLock = (PowerManager$WakeLock)array[1];
            wifiManager$WifiLock.release();
            powerManager$WakeLock.release();
            Logger.getLogger().logLine("Released WIFI lock and partial wake lock!");
        }
        catch (Exception ex) {
            Logger.getLogger().logException(ex);
        }
    }
    
    @Override
    public void wakeLock() {
        final WifiManager$WifiLock wifiLock = ((WifiManager)AndroidEnvironment.ctx.getApplicationContext().getSystemService("wifi")).createWifiLock(1, "personalHttpProxy");
        wifiLock.acquire();
        final PowerManager$WakeLock wakeLock = ((PowerManager)AndroidEnvironment.ctx.getSystemService("power")).newWakeLock(1, "personalHttpProxy");
        wakeLock.acquire();
        AndroidEnvironment.wakeLooks.push(new Object[] { wifiLock, wakeLock });
        Logger.getLogger().logLine("Aquired WIFI lock and partial wake lock!");
    }
}
