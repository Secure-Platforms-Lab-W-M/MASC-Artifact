/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  net.danlew.android.joda.ResourceZoneInfoProvider
 *  net.danlew.android.joda.TimeZoneChangedReceiver
 *  org.joda.time.tz.Provider
 */
package net.danlew.android.joda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.IOException;
import net.danlew.android.joda.ResourceZoneInfoProvider;
import net.danlew.android.joda.TimeZoneChangedReceiver;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.Provider;

public final class JodaTimeAndroid {
    private static boolean sInitCalled = false;

    private JodaTimeAndroid() {
        throw new AssertionError();
    }

    public static void init(Context context) {
        if (sInitCalled) {
            return;
        }
        sInitCalled = true;
        try {
            DateTimeZone.setProvider((Provider)new ResourceZoneInfoProvider(context));
        }
        catch (IOException iOException) {
            throw new RuntimeException("Could not read ZoneInfoMap. You are probably using Proguard wrong.", iOException);
        }
        context.getApplicationContext().registerReceiver((BroadcastReceiver)new TimeZoneChangedReceiver(), new IntentFilter("android.intent.action.TIMEZONE_CHANGED"));
    }
}

