/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  org.threeten.bp.zone.ZoneRulesInitializer
 */
package com.jakewharton.threetenabp;

import android.app.Application;
import android.content.Context;
import com.jakewharton.threetenabp.AssetsZoneRulesInitializer;
import java.util.concurrent.atomic.AtomicBoolean;
import org.threeten.bp.zone.ZoneRulesInitializer;

public final class AndroidThreeTen {
    private static final AtomicBoolean initialized = new AtomicBoolean();

    private AndroidThreeTen() {
        throw new AssertionError();
    }

    public static void init(Application application) {
        AndroidThreeTen.init((Context)application);
    }

    public static void init(Context context) {
        AndroidThreeTen.init(context, "org/threeten/bp/TZDB.dat");
    }

    public static void init(Context context, String string2) {
        if (!initialized.getAndSet(true)) {
            ZoneRulesInitializer.setInitializer((ZoneRulesInitializer)new AssetsZoneRulesInitializer(context, string2));
        }
    }
}

