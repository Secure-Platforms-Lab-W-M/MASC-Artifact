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
package android.support.v13.view;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;

public final class DragAndDropPermissionsCompat {
    private static DragAndDropPermissionsCompatImpl IMPL = Build.VERSION.SDK_INT >= 24 ? new Api24DragAndDropPermissionsCompatImpl() : new BaseDragAndDropPermissionsCompatImpl();
    private Object mDragAndDropPermissions;

    private DragAndDropPermissionsCompat(Object object) {
        this.mDragAndDropPermissions = object;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static DragAndDropPermissionsCompat request(Activity object, DragEvent dragEvent) {
        if ((object = IMPL.request((Activity)object, dragEvent)) != null) {
            return new DragAndDropPermissionsCompat(object);
        }
        return null;
    }

    public void release() {
        IMPL.release(this.mDragAndDropPermissions);
    }

    @RequiresApi(value=24)
    static class Api24DragAndDropPermissionsCompatImpl
    extends BaseDragAndDropPermissionsCompatImpl {
        Api24DragAndDropPermissionsCompatImpl() {
        }

        @Override
        public void release(Object object) {
            ((DragAndDropPermissions)object).release();
        }

        @Override
        public Object request(Activity activity, DragEvent dragEvent) {
            return activity.requestDragAndDropPermissions(dragEvent);
        }
    }

    static class BaseDragAndDropPermissionsCompatImpl
    implements DragAndDropPermissionsCompatImpl {
        BaseDragAndDropPermissionsCompatImpl() {
        }

        @Override
        public void release(Object object) {
        }

        @Override
        public Object request(Activity activity, DragEvent dragEvent) {
            return null;
        }
    }

    static interface DragAndDropPermissionsCompatImpl {
        public void release(Object var1);

        public Object request(Activity var1, DragEvent var2);
    }

}

