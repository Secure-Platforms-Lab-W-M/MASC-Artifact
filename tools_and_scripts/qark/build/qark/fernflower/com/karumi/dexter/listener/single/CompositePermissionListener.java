package com.karumi.dexter.listener.single;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CompositePermissionListener implements PermissionListener {
   private final Collection listeners;

   public CompositePermissionListener(Collection var1) {
      this.listeners = var1;
   }

   public CompositePermissionListener(PermissionListener... var1) {
      this((Collection)Arrays.asList(var1));
   }

   public void onPermissionDenied(PermissionDeniedResponse var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((PermissionListener)var2.next()).onPermissionDenied(var1);
      }

   }

   public void onPermissionGranted(PermissionGrantedResponse var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((PermissionListener)var2.next()).onPermissionGranted(var1);
      }

   }

   public void onPermissionRationaleShouldBeShown(PermissionRequest var1, PermissionToken var2) {
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((PermissionListener)var3.next()).onPermissionRationaleShouldBeShown(var1, var2);
      }

   }
}
