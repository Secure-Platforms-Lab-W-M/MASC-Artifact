// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content.res;

import android.support.annotation.NonNull;
import android.content.res.Resources;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(13)
@RequiresApi(13)
class ConfigurationHelperHoneycombMr2
{
    static int getScreenHeightDp(@NonNull final Resources resources) {
        return resources.getConfiguration().screenHeightDp;
    }
    
    static int getScreenWidthDp(@NonNull final Resources resources) {
        return resources.getConfiguration().screenWidthDp;
    }
    
    static int getSmallestScreenWidthDp(@NonNull final Resources resources) {
        return resources.getConfiguration().smallestScreenWidthDp;
    }
}
