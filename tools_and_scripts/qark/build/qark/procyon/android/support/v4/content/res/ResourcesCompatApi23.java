// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources$NotFoundException;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class ResourcesCompatApi23
{
    public static int getColor(final Resources resources, final int n, final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        return resources.getColor(n, resources$Theme);
    }
    
    public static ColorStateList getColorStateList(final Resources resources, final int n, final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        return resources.getColorStateList(n, resources$Theme);
    }
}
