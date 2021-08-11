package com.karumi.dexter.listener.multi;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import java.util.List;

public interface MultiplePermissionsListener {
   void onPermissionRationaleShouldBeShown(List var1, PermissionToken var2);

   void onPermissionsChecked(MultiplePermissionsReport var1);
}
