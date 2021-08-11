package com.karumi.dexter;

import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.util.List;

final class MultiplePermissionListenerThreadDecorator implements MultiplePermissionsListener {
   private final MultiplePermissionsListener listener;
   private final Thread thread;

   MultiplePermissionListenerThreadDecorator(MultiplePermissionsListener var1, Thread var2) {
      this.thread = var2;
      this.listener = var1;
   }

   public void onPermissionRationaleShouldBeShown(final List var1, final PermissionToken var2) {
      this.thread.execute(new Runnable() {
         public void run() {
            MultiplePermissionListenerThreadDecorator.this.listener.onPermissionRationaleShouldBeShown(var1, var2);
         }
      });
   }

   public void onPermissionsChecked(final MultiplePermissionsReport var1) {
      this.thread.execute(new Runnable() {
         public void run() {
            MultiplePermissionListenerThreadDecorator.this.listener.onPermissionsChecked(var1);
         }
      });
   }
}
