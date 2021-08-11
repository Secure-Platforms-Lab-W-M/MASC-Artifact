// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.os.PowerManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.content.Intent;
import android.os.PowerManager$WakeLock;
import android.util.SparseArray;
import android.content.BroadcastReceiver;

@Deprecated
public abstract class WakefulBroadcastReceiver extends BroadcastReceiver
{
    private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
    private static int mNextId;
    private static final SparseArray<PowerManager$WakeLock> sActiveWakeLocks;
    
    static {
        sActiveWakeLocks = new SparseArray();
        WakefulBroadcastReceiver.mNextId = 1;
    }
    
    public static boolean completeWakefulIntent(final Intent intent) {
        final int intExtra = intent.getIntExtra("android.support.content.wakelockid", 0);
        if (intExtra == 0) {
            return false;
        }
        synchronized (WakefulBroadcastReceiver.sActiveWakeLocks) {
            final PowerManager$WakeLock powerManager$WakeLock = (PowerManager$WakeLock)WakefulBroadcastReceiver.sActiveWakeLocks.get(intExtra);
            if (powerManager$WakeLock != null) {
                powerManager$WakeLock.release();
                WakefulBroadcastReceiver.sActiveWakeLocks.remove(intExtra);
                return true;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("No active wake lock id #");
            sb.append(intExtra);
            Log.w("WakefulBroadcastReceiv.", sb.toString());
            return true;
        }
    }
    
    public static ComponentName startWakefulService(final Context context, Intent startService) {
        while (true) {
            while (true) {
                Label_0130: {
                    synchronized (WakefulBroadcastReceiver.sActiveWakeLocks) {
                        final int mNextId = WakefulBroadcastReceiver.mNextId;
                        ++WakefulBroadcastReceiver.mNextId;
                        if (WakefulBroadcastReceiver.mNextId > 0) {
                            break Label_0130;
                        }
                        WakefulBroadcastReceiver.mNextId = 1;
                        startService.putExtra("android.support.content.wakelockid", mNextId);
                        startService = (Intent)context.startService(startService);
                        if (startService == null) {
                            return null;
                        }
                        final PowerManager powerManager = (PowerManager)context.getSystemService("power");
                        final StringBuilder sb = new StringBuilder();
                        sb.append("wake:");
                        sb.append(((ComponentName)startService).flattenToShortString());
                        final PowerManager$WakeLock wakeLock = powerManager.newWakeLock(1, sb.toString());
                        wakeLock.setReferenceCounted(false);
                        wakeLock.acquire(60000L);
                        WakefulBroadcastReceiver.sActiveWakeLocks.put(mNextId, (Object)wakeLock);
                        return (ComponentName)startService;
                    }
                }
                continue;
            }
        }
    }
}
