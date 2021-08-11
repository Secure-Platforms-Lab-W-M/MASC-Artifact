package com.karumi.dexter.listener.single;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;

public interface PermissionListener {
   void onPermissionDenied(PermissionDeniedResponse var1);

   void onPermissionGranted(PermissionGrantedResponse var1);

   void onPermissionRationaleShouldBeShown(PermissionRequest var1, PermissionToken var2);
}
