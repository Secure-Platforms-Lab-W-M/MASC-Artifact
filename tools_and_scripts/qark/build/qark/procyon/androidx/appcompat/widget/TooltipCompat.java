// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.os.Build$VERSION;
import android.view.View;

public class TooltipCompat
{
    private TooltipCompat() {
    }
    
    public static void setTooltipText(final View view, final CharSequence tooltipText) {
        if (Build$VERSION.SDK_INT >= 26) {
            view.setTooltipText(tooltipText);
            return;
        }
        TooltipCompatHandler.setTooltipText(view, tooltipText);
    }
}
