/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.os.IBinder
 *  android.view.View
 */
package android.support.transition;

import android.annotation.TargetApi;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(value=14)
@RequiresApi(value=14)
class WindowIdPort {
    private final IBinder mToken;

    private WindowIdPort(IBinder iBinder) {
        this.mToken = iBinder;
    }

    static WindowIdPort getWindowId(@NonNull View view) {
        return new WindowIdPort(view.getWindowToken());
    }

    public boolean equals(Object object) {
        if (object instanceof WindowIdPort && ((WindowIdPort)object).mToken.equals((Object)this.mToken)) {
            return true;
        }
        return false;
    }
}

