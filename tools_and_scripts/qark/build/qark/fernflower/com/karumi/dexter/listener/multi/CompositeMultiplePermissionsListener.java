package com.karumi.dexter.listener.multi;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CompositeMultiplePermissionsListener implements MultiplePermissionsListener {
   private final Collection listeners;

   public CompositeMultiplePermissionsListener(Collection var1) {
      this.listeners = var1;
   }

   public CompositeMultiplePermissionsListener(MultiplePermissionsListener... var1) {
      this((Collection)Arrays.asList(var1));
   }

   public void onPermissionRationaleShouldBeShown(List var1, PermissionToken var2) {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((MultiplePermissionsListener)var3.next()).onPermissionRationaleShouldBeShown(var1, var2);
      }

   }

   public void onPermissionsChecked(MultiplePermissionsReport var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((MultiplePermissionsListener)var2.next()).onPermissionsChecked(var1);
      }

   }
}
