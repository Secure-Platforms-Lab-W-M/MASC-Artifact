package android.support.v13.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import java.util.Arrays;

public class FragmentCompat {
   static final FragmentCompat.FragmentCompatImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 24) {
         IMPL = new FragmentCompat.FragmentCompatApi24Impl();
      } else if (VERSION.SDK_INT >= 23) {
         IMPL = new FragmentCompat.FragmentCompatApi23Impl();
      } else if (VERSION.SDK_INT >= 15) {
         IMPL = new FragmentCompat.FragmentCompatApi15Impl();
      } else {
         IMPL = new FragmentCompat.FragmentCompatBaseImpl();
      }
   }

   public static void requestPermissions(@NonNull Fragment var0, @NonNull String[] var1, int var2) {
      IMPL.requestPermissions(var0, var1, var2);
   }

   @Deprecated
   public static void setMenuVisibility(Fragment var0, boolean var1) {
      var0.setMenuVisibility(var1);
   }

   public static void setUserVisibleHint(Fragment var0, boolean var1) {
      IMPL.setUserVisibleHint(var0, var1);
   }

   public static boolean shouldShowRequestPermissionRationale(@NonNull Fragment var0, @NonNull String var1) {
      return IMPL.shouldShowRequestPermissionRationale(var0, var1);
   }

   @RequiresApi(15)
   static class FragmentCompatApi15Impl extends FragmentCompat.FragmentCompatBaseImpl {
      public void setUserVisibleHint(Fragment var1, boolean var2) {
         var1.setUserVisibleHint(var2);
      }
   }

   @RequiresApi(23)
   static class FragmentCompatApi23Impl extends FragmentCompat.FragmentCompatApi15Impl {
      public void requestPermissions(Fragment var1, String[] var2, int var3) {
         var1.requestPermissions(var2, var3);
      }

      public boolean shouldShowRequestPermissionRationale(Fragment var1, String var2) {
         return var1.shouldShowRequestPermissionRationale(var2);
      }
   }

   @RequiresApi(24)
   static class FragmentCompatApi24Impl extends FragmentCompat.FragmentCompatApi23Impl {
      public void setUserVisibleHint(Fragment var1, boolean var2) {
         var1.setUserVisibleHint(var2);
      }
   }

   static class FragmentCompatBaseImpl implements FragmentCompat.FragmentCompatImpl {
      public void requestPermissions(final Fragment var1, final String[] var2, final int var3) {
         (new Handler(Looper.getMainLooper())).post(new Runnable() {
            public void run() {
               int[] var3x = new int[var2.length];
               Activity var5 = var1.getActivity();
               if (var5 != null) {
                  PackageManager var4 = var5.getPackageManager();
                  String var6 = var5.getPackageName();
                  int var2x = var2.length;

                  for(int var1x = 0; var1x < var2x; ++var1x) {
                     var3x[var1x] = var4.checkPermission(var2[var1x], var6);
                  }
               } else {
                  Arrays.fill(var3x, -1);
               }

               ((FragmentCompat.OnRequestPermissionsResultCallback)var1).onRequestPermissionsResult(var3, var2, var3x);
            }
         });
      }

      public void setUserVisibleHint(Fragment var1, boolean var2) {
      }

      public boolean shouldShowRequestPermissionRationale(Fragment var1, String var2) {
         return false;
      }
   }

   interface FragmentCompatImpl {
      void requestPermissions(Fragment var1, String[] var2, int var3);

      void setUserVisibleHint(Fragment var1, boolean var2);

      boolean shouldShowRequestPermissionRationale(Fragment var1, String var2);
   }

   public interface OnRequestPermissionsResultCallback {
      void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3);
   }
}
