/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.WindowId
 */
package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.WindowIdImpl;
import android.view.View;
import android.view.WindowId;

@RequiresApi(value=18)
class WindowIdApi18
implements WindowIdImpl {
    private final WindowId mWindowId;

    WindowIdApi18(@NonNull View view) {
        this.mWindowId = view.getWindowId();
    }

    public boolean equals(Object object) {
        if (object instanceof WindowIdApi18 && ((WindowIdApi18)object).mWindowId.equals((Object)this.mWindowId)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.mWindowId.hashCode();
    }
}

