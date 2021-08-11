package android.support.v4.app;

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
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.ContextCompat;
import android.view.View;
import java.util.List;
import java.util.Map;

public class ActivityCompat extends ContextCompat {
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

   @Nullable
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

   public static boolean invalidateOptionsMenu(Activity var0) {
      var0.invalidateOptionsMenu();
      return true;
   }

   public static void postponeEnterTransition(Activity var0) {
      if (VERSION.SDK_INT >= 21) {
         var0.postponeEnterTransition();
      }

   }

   public static void requestPermissions(@NonNull final Activity var0, @NonNull final String[] var1, @IntRange(from = 0L) final int var2) {
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

   public static void setEnterSharedElementCallback(Activity var0, SharedElementCallback var1) {
      int var2 = VERSION.SDK_INT;
      Object var4 = null;
      ActivityCompat.SharedElementCallback23Impl var3 = null;
      if (var2 >= 23) {
         if (var1 != null) {
            var3 = new ActivityCompat.SharedElementCallback23Impl(var1);
         }

         var0.setEnterSharedElementCallback(var3);
      } else if (VERSION.SDK_INT >= 21) {
         ActivityCompat.SharedElementCallback21Impl var5 = (ActivityCompat.SharedElementCallback21Impl)var4;
         if (var1 != null) {
            var5 = new ActivityCompat.SharedElementCallback21Impl(var1);
         }

         var0.setEnterSharedElementCallback(var5);
         return;
      }

   }

   public static void setExitSharedElementCallback(Activity var0, SharedElementCallback var1) {
      int var2 = VERSION.SDK_INT;
      Object var4 = null;
      ActivityCompat.SharedElementCallback23Impl var3 = null;
      if (var2 >= 23) {
         if (var1 != null) {
            var3 = new ActivityCompat.SharedElementCallback23Impl(var1);
         }

         var0.setExitSharedElementCallback(var3);
      } else if (VERSION.SDK_INT >= 21) {
         ActivityCompat.SharedElementCallback21Impl var5 = (ActivityCompat.SharedElementCallback21Impl)var4;
         if (var1 != null) {
            var5 = new ActivityCompat.SharedElementCallback21Impl(var1);
         }

         var0.setExitSharedElementCallback(var5);
         return;
      }

   }

   public static boolean shouldShowRequestPermissionRationale(@NonNull Activity var0, @NonNull String var1) {
      return VERSION.SDK_INT >= 23 ? var0.shouldShowRequestPermissionRationale(var1) : false;
   }

   public static void startActivityForResult(Activity var0, Intent var1, int var2, @Nullable Bundle var3) {
      if (VERSION.SDK_INT >= 16) {
         var0.startActivityForResult(var1, var2, var3);
      } else {
         var0.startActivityForResult(var1, var2);
      }
   }

   public static void startIntentSenderForResult(Activity var0, IntentSender var1, int var2, Intent var3, int var4, int var5, int var6, @Nullable Bundle var7) throws SendIntentException {
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
      void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public interface RequestPermissionsRequestCodeValidator {
      void validateRequestPermissionsRequestCode(int var1);
   }

   @RequiresApi(21)
   private static class SharedElementCallback21Impl extends android.app.SharedElementCallback {
      protected SharedElementCallback mCallback;

      public SharedElementCallback21Impl(SharedElementCallback var1) {
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
   }

   @RequiresApi(23)
   private static class SharedElementCallback23Impl extends ActivityCompat.SharedElementCallback21Impl {
      public SharedElementCallback23Impl(SharedElementCallback var1) {
         super(var1);
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
