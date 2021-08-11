package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SharedElementCallback.OnSharedElementsReadyListener;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.view.View;
import java.util.List;
import java.util.Map;

@TargetApi(23)
@RequiresApi(23)
class ActivityCompatApi23 {
   private static android.app.SharedElementCallback createCallback(ActivityCompatApi23.SharedElementCallback23 var0) {
      ActivityCompatApi23.SharedElementCallbackImpl var1 = null;
      if (var0 != null) {
         var1 = new ActivityCompatApi23.SharedElementCallbackImpl(var0);
      }

      return var1;
   }

   public static void requestPermissions(Activity var0, String[] var1, int var2) {
      if (var0 instanceof ActivityCompatApi23.RequestPermissionsRequestCodeValidator) {
         ((ActivityCompatApi23.RequestPermissionsRequestCodeValidator)var0).validateRequestPermissionsRequestCode(var2);
      }

      var0.requestPermissions(var1, var2);
   }

   public static void setEnterSharedElementCallback(Activity var0, ActivityCompatApi23.SharedElementCallback23 var1) {
      var0.setEnterSharedElementCallback(createCallback(var1));
   }

   public static void setExitSharedElementCallback(Activity var0, ActivityCompatApi23.SharedElementCallback23 var1) {
      var0.setExitSharedElementCallback(createCallback(var1));
   }

   public static boolean shouldShowRequestPermissionRationale(Activity var0, String var1) {
      return var0.shouldShowRequestPermissionRationale(var1);
   }

   public interface OnSharedElementsReadyListenerBridge {
      void onSharedElementsReady();
   }

   public interface RequestPermissionsRequestCodeValidator {
      void validateRequestPermissionsRequestCode(int var1);
   }

   public abstract static class SharedElementCallback23 extends ActivityCompatApi21.SharedElementCallback21 {
      public abstract void onSharedElementsArrived(List var1, List var2, ActivityCompatApi23.OnSharedElementsReadyListenerBridge var3);
   }

   private static class SharedElementCallbackImpl extends android.app.SharedElementCallback {
      private ActivityCompatApi23.SharedElementCallback23 mCallback;

      public SharedElementCallbackImpl(ActivityCompatApi23.SharedElementCallback23 var1) {
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
         this.mCallback.onSharedElementsArrived(var1, var2, new ActivityCompatApi23.OnSharedElementsReadyListenerBridge() {
            public void onSharedElementsReady() {
               var3.onSharedElementsReady();
            }
         });
      }
   }
}
