/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.DragAndDropPermissions
 *  android.view.DragEvent
 */
package androidx.core.view;

import android.app.Activity;
import android.os.Build;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;

public final class DragAndDropPermissionsCompat {
    private Object mDragAndDropPermissions;

    private DragAndDropPermissionsCompat(Object object) {
        this.mDragAndDropPermissions = object;
    }

    public static DragAndDropPermissionsCompat request(Activity activity, DragEvent dragEvent) {
        if (Build.VERSION.SDK_INT >= 24 && (activity = activity.requestDragAndDropPermissions(dragEvent)) != null) {
            return new DragAndDropPermissionsCompat((Object)activity);
        }
        return null;
    }

    public void release() {
        if (Build.VERSION.SDK_INT >= 24) {
            ((DragAndDropPermissions)this.mDragAndDropPermissions).release();
        }
    }
}

