/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.Bundle
 *  android.os.IBinder
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

@TargetApi(value=18)
@RequiresApi(value=18)
class BundleCompatJellybeanMR2 {
    BundleCompatJellybeanMR2() {
    }

    public static IBinder getBinder(Bundle bundle, String string2) {
        return bundle.getBinder(string2);
    }

    public static void putBinder(Bundle bundle, String string2, IBinder iBinder) {
        bundle.putBinder(string2, iBinder);
    }
}

