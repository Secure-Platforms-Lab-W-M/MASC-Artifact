// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v13.app;

import android.support.v13.view.DragAndDropPermissionsCompat;
import android.view.DragEvent;
import android.app.Activity;

public class ActivityCompat extends android.support.v4.app.ActivityCompat
{
    protected ActivityCompat() {
    }
    
    public static DragAndDropPermissionsCompat requestDragAndDropPermissions(final Activity activity, final DragEvent dragEvent) {
        return DragAndDropPermissionsCompat.request(activity, dragEvent);
    }
}
