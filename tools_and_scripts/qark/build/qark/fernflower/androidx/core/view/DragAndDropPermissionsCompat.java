package androidx.core.view;

import android.app.Activity;
import android.os.Build.VERSION;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;

public final class DragAndDropPermissionsCompat {
   private Object mDragAndDropPermissions;

   private DragAndDropPermissionsCompat(Object var1) {
      this.mDragAndDropPermissions = var1;
   }

   public static DragAndDropPermissionsCompat request(Activity var0, DragEvent var1) {
      if (VERSION.SDK_INT >= 24) {
         DragAndDropPermissions var2 = var0.requestDragAndDropPermissions(var1);
         if (var2 != null) {
            return new DragAndDropPermissionsCompat(var2);
         }
      }

      return null;
   }

   public void release() {
      if (VERSION.SDK_INT >= 24) {
         ((DragAndDropPermissions)this.mDragAndDropPermissions).release();
      }

   }
}
