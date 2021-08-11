package com.karumi.dexter.listener.multi;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import java.util.List;

public class BaseMultiplePermissionsListener implements MultiplePermissionsListener {
   public void onPermissionRationaleShouldBeShown(List var1, PermissionToken var2) {
      var2.continuePermissionRequest();
   }

   public void onPermissionsChecked(MultiplePermissionsReport var1) {
   }
}
