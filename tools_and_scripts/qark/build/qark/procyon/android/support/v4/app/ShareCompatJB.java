// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.text.Html;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(16)
@RequiresApi(16)
class ShareCompatJB
{
    public static String escapeHtml(final CharSequence charSequence) {
        return Html.escapeHtml(charSequence);
    }
}
