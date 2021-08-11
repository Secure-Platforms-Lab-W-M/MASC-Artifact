// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v13.view;

import android.view.DragAndDropPermissions;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.view.DragEvent;
import android.app.Activity;
import android.os.Build$VERSION;

public final class DragAndDropPermissionsCompat
{
    private static DragAndDropPermissionsCompatImpl IMPL;
    private Object mDragAndDropPermissions;
    
    static {
        if (Build$VERSION.SDK_INT >= 24) {
            DragAndDropPermissionsCompat.IMPL = (DragAndDropPermissionsCompatImpl)new Api24DragAndDropPermissionsCompatImpl();
            return;
        }
        DragAndDropPermissionsCompat.IMPL = (DragAndDropPermissionsCompatImpl)new BaseDragAndDropPermissionsCompatImpl();
    }
    
    private DragAndDropPermissionsCompat(final Object mDragAndDropPermissions) {
        this.mDragAndDropPermissions = mDragAndDropPermissions;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static DragAndDropPermissionsCompat request(final Activity activity, final DragEvent dragEvent) {
        final Object request = DragAndDropPermissionsCompat.IMPL.request(activity, dragEvent);
        if (request != null) {
            return new DragAndDropPermissionsCompat(request);
        }
        return null;
    }
    
    public void release() {
        DragAndDropPermissionsCompat.IMPL.release(this.mDragAndDropPermissions);
    }
    
    @RequiresApi(24)
    static class Api24DragAndDropPermissionsCompatImpl extends BaseDragAndDropPermissionsCompatImpl
    {
        @Override
        public void release(final Object o) {
            ((DragAndDropPermissions)o).release();
        }
        
        @Override
        public Object request(final Activity activity, final DragEvent dragEvent) {
            return activity.requestDragAndDropPermissions(dragEvent);
        }
    }
    
    static class BaseDragAndDropPermissionsCompatImpl implements DragAndDropPermissionsCompatImpl
    {
        @Override
        public void release(final Object o) {
        }
        
        @Override
        public Object request(final Activity activity, final DragEvent dragEvent) {
            return null;
        }
    }
    
    interface DragAndDropPermissionsCompatImpl
    {
        void release(final Object p0);
        
        Object request(final Activity p0, final DragEvent p1);
    }
}
