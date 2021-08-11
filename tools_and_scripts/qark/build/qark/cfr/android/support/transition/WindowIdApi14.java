/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 */
package android.support.transition;

import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.transition.WindowIdImpl;

@RequiresApi(value=14)
class WindowIdApi14
implements WindowIdImpl {
    private final IBinder mToken;

    WindowIdApi14(IBinder iBinder) {
        this.mToken = iBinder;
    }

    public boolean equals(Object object) {
        if (object instanceof WindowIdApi14 && ((WindowIdApi14)object).mToken.equals((Object)this.mToken)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.mToken.hashCode();
    }
}

