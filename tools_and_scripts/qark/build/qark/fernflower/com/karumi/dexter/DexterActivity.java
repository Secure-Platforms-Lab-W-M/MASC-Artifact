package com.karumi.dexter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import java.util.Arrays;
import java.util.LinkedList;

public final class DexterActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
   private boolean isTargetSdkUnderAndroidM() {
      boolean var2 = false;

      int var1;
      try {
         var1 = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).applicationInfo.targetSdkVersion;
      } catch (NameNotFoundException var4) {
         return false;
      }

      if (var1 < 23) {
         var2 = true;
      }

      return var2;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      Dexter.onActivityReady(this);
      this.getWindow().addFlags(16);
   }

   protected void onDestroy() {
      super.onDestroy();
      Dexter.onActivityDestroyed();
   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      Dexter.onActivityReady(this);
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      LinkedList var5 = new LinkedList();
      LinkedList var6 = new LinkedList();
      if (this.isTargetSdkUnderAndroidM()) {
         var6.addAll(Arrays.asList(var2));
      } else {
         for(var1 = 0; var1 < var2.length; ++var1) {
            String var7 = var2[var1];
            int var4 = var3[var1];
            if (var4 != -2 && var4 != -1) {
               if (var4 == 0) {
                  var5.add(var7);
               }
            } else {
               var6.add(var7);
            }
         }
      }

      Dexter.onPermissionsRequested(var5, var6);
   }
}
