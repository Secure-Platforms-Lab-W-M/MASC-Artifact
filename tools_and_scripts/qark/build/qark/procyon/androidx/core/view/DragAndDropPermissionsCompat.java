// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.view.DragAndDropPermissions;
import android.os.Build$VERSION;
import android.view.DragEvent;
import android.app.Activity;

public final class DragAndDropPermissionsCompat
{
    private Object mDragAndDropPermissions;
    
    private DragAndDropPermissionsCompat(final Object mDragAndDropPermissions) {
        this.mDragAndDropPermissions = mDragAndDropPermissions;
    }
    
    public static DragAndDropPermissionsCompat request(final Activity activity, final DragEvent dragEvent) {
        if (Build$VERSION.SDK_INT >= 24) {
            final DragAndDropPermissions requestDragAndDropPermissions = activity.requestDragAndDropPermissions(dragEvent);
            if (requestDragAndDropPermissions != null) {
                return new DragAndDropPermissionsCompat(requestDragAndDropPermissions);
            }
        }
        return null;
    }
    
    public void release() {
        if (Build$VERSION.SDK_INT >= 24) {
            ((DragAndDropPermissions)this.mDragAndDropPermissions).release();
        }
    }
}
