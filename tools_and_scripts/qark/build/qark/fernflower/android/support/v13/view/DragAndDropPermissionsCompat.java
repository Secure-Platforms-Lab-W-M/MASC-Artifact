package android.support.v13.view;

import android.app.Activity;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;

public final class DragAndDropPermissionsCompat {
   private static DragAndDropPermissionsCompat.DragAndDropPermissionsCompatImpl IMPL;
   private Object mDragAndDropPermissions;

   static {
      if (VERSION.SDK_INT >= 24) {
         IMPL = new DragAndDropPermissionsCompat.Api24DragAndDropPermissionsCompatImpl();
      } else {
         IMPL = new DragAndDropPermissionsCompat.BaseDragAndDropPermissionsCompatImpl();
      }
   }

   private DragAndDropPermissionsCompat(Object var1) {
      this.mDragAndDropPermissions = var1;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static DragAndDropPermissionsCompat request(Activity var0, DragEvent var1) {
      Object var2 = IMPL.request(var0, var1);
      return var2 != null ? new DragAndDropPermissionsCompat(var2) : null;
   }

   public void release() {
      IMPL.release(this.mDragAndDropPermissions);
   }

   @RequiresApi(24)
   static class Api24DragAndDropPermissionsCompatImpl extends DragAndDropPermissionsCompat.BaseDragAndDropPermissionsCompatImpl {
      public void release(Object var1) {
         ((DragAndDropPermissions)var1).release();
      }

      public Object request(Activity var1, DragEvent var2) {
         return var1.requestDragAndDropPermissions(var2);
      }
   }

   static class BaseDragAndDropPermissionsCompatImpl implements DragAndDropPermissionsCompat.DragAndDropPermissionsCompatImpl {
      public void release(Object var1) {
      }

      public Object request(Activity var1, DragEvent var2) {
         return null;
      }
   }

   interface DragAndDropPermissionsCompatImpl {
      void release(Object var1);

      Object request(Activity var1, DragEvent var2);
   }
}
