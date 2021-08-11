// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.os.IBinder;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(18)
@RequiresApi(18)
class BundleCompatJellybeanMR2
{
    public static IBinder getBinder(final Bundle bundle, final String s) {
        return bundle.getBinder(s);
    }
    
    public static void putBinder(final Bundle bundle, final String s, final IBinder binder) {
        bundle.putBinder(s, binder);
    }
}
