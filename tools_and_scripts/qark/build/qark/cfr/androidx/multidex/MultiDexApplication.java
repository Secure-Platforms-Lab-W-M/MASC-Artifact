/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 */
package androidx.multidex;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class MultiDexApplication
extends Application {
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install((Context)this);
    }
}

