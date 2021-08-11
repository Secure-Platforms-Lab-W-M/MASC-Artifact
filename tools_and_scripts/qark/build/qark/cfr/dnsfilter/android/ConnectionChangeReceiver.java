/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 */
package dnsfilter.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import dnsfilter.android.DNSFilterService;
import util.ExecutionEnvironment;
import util.Logger;

public class ConnectionChangeReceiver
extends BroadcastReceiver
implements Runnable {
    private static ConnectionChangeReceiver instance = new ConnectionChangeReceiver();

    public static ConnectionChangeReceiver getInstance() {
        return instance;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void onReceive(Context object, Intent intent) {
        // MONITORENTER : this
        if (ExecutionEnvironment.getEnvironment().debug()) {
            object = Logger.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Received Network Connection Event: ");
            stringBuilder.append(intent.getAction());
            object.logLine(stringBuilder.toString());
        }
        DNSFilterService.possibleNetworkChange();
        new Thread(this).start();
        return;
        catch (Exception exception) {
            Logger.getLogger().logException(exception);
        }
        // MONITOREXIT : this
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000L);
            DNSFilterService.possibleNetworkChange();
            return;
        }
        catch (InterruptedException interruptedException) {
            Logger.getLogger().logException(interruptedException);
            return;
        }
    }
}

