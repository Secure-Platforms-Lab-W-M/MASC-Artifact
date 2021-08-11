/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 */
package androidx.appcompat.widget;

import android.os.Build;
import android.view.View;
import androidx.appcompat.widget.TooltipCompatHandler;

public class TooltipCompat {
    private TooltipCompat() {
    }

    public static void setTooltipText(View view, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setTooltipText(charSequence);
            return;
        }
        TooltipCompatHandler.setTooltipText(view, charSequence);
    }
}

