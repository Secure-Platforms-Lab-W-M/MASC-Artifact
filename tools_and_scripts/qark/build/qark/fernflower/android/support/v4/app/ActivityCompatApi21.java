package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.view.View;
import java.util.List;
import java.util.Map;

@TargetApi(21)
@RequiresApi(21)
class ActivityCompatApi21 {
   private static android.app.SharedElementCallback createCallback(ActivityCompatApi21.SharedElementCallback21 var0) {
      ActivityCompatApi21.SharedElementCallbackImpl var1 = null;
      if (var0 != null) {
         var1 = new ActivityCompatApi21.SharedElementCallbackImpl(var0);
      }

      return var1;
   }

   public static void finishAfterTransition(Activity var0) {
      var0.finishAfterTransition();
   }

   public static void postponeEnterTransition(Activity var0) {
      var0.postponeEnterTransition();
   }

   public static void setEnterSharedElementCallback(Activity var0, ActivityCompatApi21.SharedElementCallback21 var1) {
      var0.setEnterSharedElementCallback(createCallback(var1));
   }

   public static void setExitSharedElementCallback(Activity var0, ActivityCompatApi21.SharedElementCallback21 var1) {
      var0.setExitSharedElementCallback(createCallback(var1));
   }

   public static void startPostponedEnterTransition(Activity var0) {
      var0.startPostponedEnterTransition();
   }

   public abstract static class SharedElementCallback21 {
      public abstract Parcelable onCaptureSharedElementSnapshot(View var1, Matrix var2, RectF var3);

      public abstract View onCreateSnapshotView(Context var1, Parcelable var2);

      public abstract void onMapSharedElements(List var1, Map var2);

      public abstract void onRejectSharedElements(List var1);

      public abstract void onSharedElementEnd(List var1, List var2, List var3);

      public abstract void onSharedElementStart(List var1, List var2, List var3);
   }

   private static class SharedElementCallbackImpl extends android.app.SharedElementCallback {
      private ActivityCompatApi21.SharedElementCallback21 mCallback;

      public SharedElementCallbackImpl(ActivityCompatApi21.SharedElementCallback21 var1) {
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
}
