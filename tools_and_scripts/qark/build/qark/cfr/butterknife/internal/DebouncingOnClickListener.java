/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.View$OnClickListener
 */
package butterknife.internal;

import android.view.View;
import butterknife.internal.-$$Lambda$DebouncingOnClickListener$EDavjG1Da3G8JTdFPVGk_7OErB8;

public abstract class DebouncingOnClickListener
implements View.OnClickListener {
    private static final Runnable ENABLE_AGAIN;
    static boolean enabled;

    static {
        enabled = true;
        ENABLE_AGAIN = -$$Lambda$DebouncingOnClickListener$EDavjG1Da3G8JTdFPVGk_7OErB8.INSTANCE;
    }

    static /* synthetic */ void lambda$static$0() {
        enabled = true;
    }

    public abstract void doClick(View var1);

    public final void onClick(View view) {
        if (enabled) {
            enabled = false;
            view.post(ENABLE_AGAIN);
            this.doClick(view);
        }
    }
}

