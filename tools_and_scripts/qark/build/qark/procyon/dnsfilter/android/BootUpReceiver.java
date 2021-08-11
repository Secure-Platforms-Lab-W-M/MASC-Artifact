// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.android;

import android.net.VpnService;
import android.os.Build$VERSION;
import android.content.Intent;
import android.content.Context;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import android.os.Environment;
import java.util.Properties;
import android.content.BroadcastReceiver;

public class BootUpReceiver extends BroadcastReceiver
{
    public Properties getConfig() {
        final StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append("/PersonalDNSFilter/dnsfilter.conf");
        final File file = new File(sb.toString());
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final Properties properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
            return properties;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public void onReceive(final Context context, Intent intent) {
        final Properties config = this.getConfig();
        if (config != null && Boolean.parseBoolean(config.getProperty("AUTOSTART", "false"))) {
            if (Build$VERSION.SDK_INT >= 28) {
                intent = new Intent(context, (Class)DNSFilterService.class);
                VpnService.prepare(context);
                context.startForegroundService(intent);
                return;
            }
            DNSProxyActivity.BOOT_START = true;
            intent = new Intent(context, (Class)DNSProxyActivity.class);
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }
}
