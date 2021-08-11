// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content.res;

import android.support.annotation.NonNull;
import android.content.res.Resources;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(17)
@RequiresApi(17)
class ConfigurationHelperJellybeanMr1
{
    static int getDensityDpi(@NonNull final Resources resources) {
        return resources.getConfiguration().densityDpi;
    }
}
