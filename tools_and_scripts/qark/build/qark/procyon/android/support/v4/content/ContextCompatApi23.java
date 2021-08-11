// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.content.res.ColorStateList;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class ContextCompatApi23
{
    public static int getColor(final Context context, final int n) {
        return context.getColor(n);
    }
    
    public static ColorStateList getColorStateList(final Context context, final int n) {
        return context.getColorStateList(n);
    }
}
