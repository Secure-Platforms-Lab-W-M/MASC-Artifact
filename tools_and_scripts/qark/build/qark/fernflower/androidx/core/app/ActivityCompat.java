package androidx.core.app;

import android.app.Activity;
import android.app.SharedElementCallback.OnSharedElementsReadyListener;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.view.DragEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.view.DragAndDropPermissionsCompat;
import java.util.List;
import java.util.Map;

public class ActivityCompat extends ContextCompat {
   private static ActivityCompat.PermissionCompatDelegate sDelegate;

   protected ActivityCompat() {
   }

   public static void finishAffinity(Activity var0) {
      if (VERSION.SDK_INT >= 16) {
         var0.finishAffinity();
      } else {
         var0.finish();
      }
   }

   public static void finishAfterTransition(Activity var0) {
      if (VERSION.SDK_INT >= 21) {
         var0.finishAfterTransition();
      } else {
         var0.finish();
      }
   }

   public static ActivityCompat.PermissionCompatDelegate getPermissionCompatDelegate() {
      return sDelegate;
   }

   public static Uri getReferrer(Activity var0) {
      if (VERSION.SDK_INT >= 22) {
         return var0.getReferrer();
      } else {
         Intent var2 = var0.getIntent();
         Uri var1 = (Uri)var2.getParcelableExtra("android.intent.extra.REFERRER");
         if (var1 != null) {
            return var1;
         } else {
            String var3 = var2.getStringExtra("android.intent.extra.REFERRER_NAME");
            return var3 != null ? Uri.parse(var3) : null;
         }
      }
   }

   @Deprecated
   public static boolean invalidateOptionsMenu(Activity var0) {
      var0.invalidateOptionsMenu();
      return true;
   }

   public static void postponeEnterTransition(Activity var0) {
      if (VERSION.SDK_INT >= 21) {
         var0.postponeEnterTransition();
      }

   }

   public static void recreate(Activity var0) {
      if (VERSION.SDK_INT >= 28) {
         var0.recreate();
      } else {
         if (!ActivityRecreator.recreate(var0)) {
            var0.recreate();
         }

      }
   }

   public static DragAndDropPermissionsCompat requestDragAndDropPermissions(Activity var0, DragEvent var1) {
      return DragAndDropPermissionsCompat.request(var0, var1);
   }

   public static void requestPermissions(final Activity var0, final String[] var1, final int var2) {
      ActivityCompat.PermissionCompatDelegate var3 = sDelegate;
      if (var3 == null || !var3.requestPermissions(var0, var1, var2)) {
         if (VERSION.SDK_INT >= 23) {
            if (var0 instanceof ActivityCompat.RequestPermissionsRequestCodeValidator) {
               ((ActivityCompat.RequestPermissionsRequestCodeValidator)var0).validateRequestPermissionsRequestCode(var2);
            }

            var0.requestPermissions(var1, var2);
         } else {
            if (var0 instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     int[] var3 = new int[var1.length];
                     PackageManager var4 = var0.getPackageManager();
                     String var5 = var0.getPackageName();
                     int var2x = var1.length;

                     for(int var1x = 0; var1x < var2x; ++var1x) {
                        var3[var1x] = var4.checkPermission(var1[var1x], var5);
                     }

                     ((ActivityCompat.OnRequestPermissionsResultCallback)var0).onRequestPermissionsResult(var2, var1, var3);
                  }
               });
            }

         }
      }
   }

   public static View requireViewById(Activity var0, int var1) {
      if (VERSION.SDK_INT >= 28) {
         return var0.requireViewById(var1);
      } else {
         View var2 = var0.findViewById(var1);
         if (var2 != null) {
            return var2;
         } else {
            throw new IllegalArgumentException("ID does not reference a View inside this Activity");
         }
      }
   }

   public static void setEnterSharedElementCallback(Activity var0, SharedElementCallback var1) {
      if (VERSION.SDK_INT >= 21) {
         ActivityCompat.SharedElementCallback21Impl var2;
         if (var1 != null) {
            var2 = new ActivityCompat.SharedElementCallback21Impl(var1);
         } else {
            var2 = null;
         }

         var0.setEnterSharedElementCallback(var2);
      }

   }

   public static void setExitSharedElementCallback(Activity var0, SharedElementCallback var1) {
      if (VERSION.SDK_INT >= 21) {
         ActivityCompat.SharedElementCallback21Impl var2;
         if (var1 != null) {
            var2 = new ActivityCompat.SharedElementCallback21Impl(var1);
         } else {
            var2 = null;
         }

         var0.setExitSharedElementCallback(var2);
      }

   }

   public static void setPermissionCompatDelegate(ActivityCompat.PermissionCompatDelegate var0) {
      sDelegate = var0;
   }

   public static boolean shouldShowRequestPermissionRationale(Activity var0, String var1) {
      return VERSION.SDK_INT >= 23 ? var0.shouldShowRequestPermissionRationale(var1) : false;
   }

   public static void startActivityForResult(Activity var0, Intent var1, int var2, Bundle var3) {
      if (VERSION.SDK_INT >= 16) {
         var0.startActivityForResult(var1, var2, var3);
      } else {
         var0.startActivityForResult(var1, var2);
      }
   }

   public static void startIntentSenderForResult(Activity var0, IntentSender var1, int var2, Intent var3, int var4, int var5, int var6, Bundle var7) throws SendIntentException {
      if (VERSION.SDK_INT >= 16) {
         var0.startIntentSenderForResult(var1, var2, var3, var4, var5, var6, var7);
      } else {
         var0.startIntentSenderForResult(var1, var2, var3, var4, var5, var6);
      }
   }

   public static void startPostponedEnterTransition(Activity var0) {
      if (VERSION.SDK_INT >= 21) {
         var0.startPostponedEnterTransition();
      }

   }

   public interface OnRequestPermissionsResultCallback {
      void onRequestPermissionsResult(int var1, String[] var2, int[] var3);
   }

   public interface PermissionCompatDelegate {
      boolean onActivityResult(Activity var1, int var2, int var3, Intent var4);

      boolean requestPermissions(Activity var1, String[] var2, int var3);
   }

   public interface RequestPermissionsRequestCodeValidator {
      void validateRequestPermissionsRequestCode(int var1);
   }

   private static class SharedElementCallback21Impl extends android.app.SharedElementCallback {
      private final SharedElementCallback mCallback;

      SharedElementCallback21Impl(SharedElementCallback var1) {
         this.mCallback = var1;
      }

      public Parcelable onCaptureSharedElementSnapshot(View var1, Matrix var2, RectF var3) {
         return this.mCallback.onCaptureSharedElementSnapshot(var1, var2, var3);
      }

      public View onCreateSnapshotView(Context var1, Parcelable var2) {
         return this.mCallback.onCreateSnapshotView(var1, var2);
      }

      public void onMapSharedElements(List var1, Map var2) {
         this.mCallback.onMapSharedElements(var1, var2);
      }

      public void onRejectSharedElements(List var1) {
         this.mCallback.onRejectSharedElements(var1);
      }

      public void onSharedElementEnd(List var1, List var2, List var3) {
         this.mCallback.onSharedElementEnd(var1, var2, var3);
      }

      public void onSharedElementStart(List var1, List var2, List var3) {
         this.mCallback.onSharedElementStart(var1, var2, var3);
      }

      public void onSharedElementsArrived(List var1, List var2, final OnSharedElementsReadyListener var3) {
         this.mCallback.onSharedElementsArrived(var1, var2, new SharedElementCallback.OnSharedElementsReadyListener() {
            public void onSharedElementsReady() {
               var3.onSharedElementsReady();
            }
         });
      }
   }
}
