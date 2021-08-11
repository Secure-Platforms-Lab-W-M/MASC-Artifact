// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import android.os.Build$VERSION;
import android.view.View;
import android.app.Dialog;

public class DialogCompat
{
    private DialogCompat() {
    }
    
    public static View requireViewById(final Dialog dialog, final int n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return dialog.requireViewById(n);
        }
        final View viewById = dialog.findViewById(n);
        if (viewById != null) {
            return viewById;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Dialog");
    }
}
