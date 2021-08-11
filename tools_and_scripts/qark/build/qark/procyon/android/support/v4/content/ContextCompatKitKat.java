// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import java.io.File;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(19)
@RequiresApi(19)
class ContextCompatKitKat
{
    public static File[] getExternalCacheDirs(final Context context) {
        return context.getExternalCacheDirs();
    }
    
    public static File[] getExternalFilesDirs(final Context context, final String s) {
        return context.getExternalFilesDirs(s);
    }
    
    public static File[] getObbDirs(final Context context) {
        return context.getObbDirs();
    }
}
