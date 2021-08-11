// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import android.graphics.drawable.Drawable;
import java.io.File;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(21)
@RequiresApi(21)
class ContextCompatApi21
{
    public static File getCodeCacheDir(final Context context) {
        return context.getCodeCacheDir();
    }
    
    public static Drawable getDrawable(final Context context, final int n) {
        return context.getDrawable(n);
    }
    
    public static File getNoBackupFilesDir(final Context context) {
        return context.getNoBackupFilesDir();
    }
}
