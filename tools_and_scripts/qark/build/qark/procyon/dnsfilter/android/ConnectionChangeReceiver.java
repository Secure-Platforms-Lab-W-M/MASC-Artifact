// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package dnsfilter.android;

import util.LoggerInterface;
import util.Logger;
import util.ExecutionEnvironment;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class ConnectionChangeReceiver extends BroadcastReceiver implements Runnable
{
    private static ConnectionChangeReceiver instance;
    
    static {
        ConnectionChangeReceiver.instance = new ConnectionChangeReceiver();
    }
    
    public static ConnectionChangeReceiver getInstance() {
        return ConnectionChangeReceiver.instance;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        // monitorenter(this)
        try {
            try {
                if (ExecutionEnvironment.getEnvironment().debug()) {
                    final LoggerInterface logger = Logger.getLogger();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Received Network Connection Event: ");
                    sb.append(intent.getAction());
                    logger.logLine(sb.toString());
                }
                DNSFilterService.possibleNetworkChange();
                new Thread(this).start();
            }
            finally {}
        }
        catch (Exception ex) {
            Logger.getLogger().logException(ex);
        }
        // monitorexit(this)
        return;
    }
    // monitorexit(this)
    
    public void run() {
        try {
            Thread.sleep(10000L);
            DNSFilterService.possibleNetworkChange();
        }
        catch (InterruptedException ex) {
            Logger.getLogger().logException(ex);
        }
    }
}
