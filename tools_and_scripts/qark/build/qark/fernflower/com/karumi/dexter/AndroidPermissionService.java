package com.karumi.dexter;

import android.app.Activity;
import android.content.Context;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

class AndroidPermissionService {
   int checkSelfPermission(Context var1, String var2) {
      return PermissionChecker.checkSelfPermission(var1, var2);
   }

   void requestPermissions(Activity var1, String[] var2, int var3) {
      if (var1 != null) {
         ActivityCompat.requestPermissions(var1, var2, var3);
      }
   }

   boolean shouldShowRequestPermissionRationale(Activity var1, String var2) {
      return var1 == null ? false : ActivityCompat.shouldShowRequestPermissionRationale(var1, var2);
   }
}
