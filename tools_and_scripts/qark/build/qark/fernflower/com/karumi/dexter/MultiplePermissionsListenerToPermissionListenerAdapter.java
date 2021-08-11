package com.karumi.dexter;

import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.List;

final class MultiplePermissionsListenerToPermissionListenerAdapter implements MultiplePermissionsListener {
   private final PermissionListener listener;

   MultiplePermissionsListenerToPermissionListenerAdapter(PermissionListener var1) {
      this.listener = var1;
   }

   public void onPermissionRationaleShouldBeShown(List var1, PermissionToken var2) {
      PermissionRequest var3 = (PermissionRequest)var1.get(0);
      this.listener.onPermissionRationaleShouldBeShown(var3, var2);
   }

   public void onPermissionsChecked(MultiplePermissionsReport var1) {
      List var2 = var1.getDeniedPermissionResponses();
      List var3 = var1.getGrantedPermissionResponses();
      if (!var2.isEmpty()) {
         PermissionDeniedResponse var5 = (PermissionDeniedResponse)var2.get(0);
         this.listener.onPermissionDenied(var5);
      } else {
         PermissionGrantedResponse var4 = (PermissionGrantedResponse)var3.get(0);
         this.listener.onPermissionGranted(var4);
      }
   }
}
