// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.multidex;

import android.content.Context;
import android.app.Application;

public class MultiDexApplication extends Application
{
    protected void attachBaseContext(final Context context) {
        super.attachBaseContext(context);
        MultiDex.install((Context)this);
    }
}
