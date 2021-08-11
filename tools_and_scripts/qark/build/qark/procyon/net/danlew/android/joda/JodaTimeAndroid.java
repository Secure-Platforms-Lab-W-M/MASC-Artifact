// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package net.danlew.android.joda;

import java.io.IOException;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import org.joda.time.tz.Provider;
import org.joda.time.DateTimeZone;
import android.content.Context;

public final class JodaTimeAndroid
{
    private static boolean sInitCalled;
    
    static {
        JodaTimeAndroid.sInitCalled = false;
    }
    
    private JodaTimeAndroid() {
        throw new AssertionError();
    }
    
    public static void init(final Context context) {
        if (JodaTimeAndroid.sInitCalled) {
            return;
        }
        JodaTimeAndroid.sInitCalled = true;
        try {
            DateTimeZone.setProvider((Provider)new ResourceZoneInfoProvider(context));
            context.getApplicationContext().registerReceiver((BroadcastReceiver)new TimeZoneChangedReceiver(), new IntentFilter("android.intent.action.TIMEZONE_CHANGED"));
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not read ZoneInfoMap. You are probably using Proguard wrong.", ex);
        }
    }
}
