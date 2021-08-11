/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.net.VpnService
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Environment
 */
package dnsfilter.android;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.Environment;
import dnsfilter.android.DNSFilterService;
import dnsfilter.android.DNSProxyActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class BootUpReceiver
extends BroadcastReceiver {
    public Properties getConfig() {
        Object object = new StringBuilder();
        object.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        object.append("/PersonalDNSFilter/dnsfilter.conf");
        object = new File(object.toString());
        try {
            object = new FileInputStream((File)object);
            Properties properties = new Properties();
            properties.load((InputStream)object);
            object.close();
            return properties;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public void onReceive(Context context, Intent object) {
        object = this.getConfig();
        if (object != null && Boolean.parseBoolean(object.getProperty("AUTOSTART", "false"))) {
            if (Build.VERSION.SDK_INT >= 28) {
                object = new Intent(context, DNSFilterService.class);
                VpnService.prepare((Context)context);
                context.startForegroundService((Intent)object);
                return;
            }
            DNSProxyActivity.BOOT_START = true;
            object = new Intent(context, DNSProxyActivity.class);
            object.addFlags(268435456);
            context.startActivity((Intent)object);
        }
    }
}

