// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content;

import java.io.File;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(24)
@RequiresApi(24)
class ContextCompatApi24
{
    public static Context createDeviceProtectedStorageContext(final Context context) {
        return context.createDeviceProtectedStorageContext();
    }
    
    public static File getDataDir(final Context context) {
        return context.getDataDir();
    }
    
    public static boolean isDeviceProtectedStorage(final Context context) {
        return context.isDeviceProtectedStorage();
    }
}
