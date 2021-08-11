package androidx.core.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback {
   private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
   private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
   private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
   private static final int MAX_IMAGE_SIZE = 1048576;
   private Matrix mTempMatrix;

   private static Bitmap createDrawableBitmap(Drawable var0) {
      int var2 = var0.getIntrinsicWidth();
      int var3 = var0.getIntrinsicHeight();
      if (var2 > 0 && var3 > 0) {
         float var1 = Math.min(1.0F, 1048576.0F / (float)(var2 * var3));
         if (var0 instanceof BitmapDrawable && var1 == 1.0F) {
            return ((BitmapDrawable)var0).getBitmap();
         } else {
            var2 = (int)((float)var2 * var1);
            var3 = (int)((float)var3 * var1);
            Bitmap var8 = Bitmap.createBitmap(var2, var3, Config.ARGB_8888);
            Canvas var9 = new Canvas(var8);
            Rect var10 = var0.getBounds();
            int var4 = var10.left;
            int var5 = var10.top;
            int var6 = var10.right;
            int var7 = var10.bottom;
            var0.setBounds(0, 0, var2, var3);
            var0.draw(var9);
            var0.setBounds(var4, var5, var6, var7);
            return var8;
         }
      } else {
         return null;
      }
   }

   public Parcelable onCaptureSharedElementSnapshot(View var1, Matrix var2, RectF var3) {
      Drawable var8;
      if (var1 instanceof ImageView) {
         ImageView var7 = (ImageView)var1;
         var8 = var7.getDrawable();
         Drawable var9 = var7.getBackground();
         if (var8 != null && var9 == null) {
            Bitmap var14 = createDrawableBitmap(var8);
            if (var14 != null) {
               Bundle var10 = new Bundle();
               var10.putParcelable("sharedElement:snapshot:bitmap", var14);
               var10.putString("sharedElement:snapshot:imageScaleType", var7.getScaleType().toString());
               if (var7.getScaleType() == ScaleType.MATRIX) {
                  var2 = var7.getImageMatrix();
                  float[] var12 = new float[9];
                  var2.getValues(var12);
                  var10.putFloatArray("sharedElement:snapshot:imageMatrix", var12);
               }

               return var10;
            }
         }
      }

      int var6 = Math.round(var3.width());
      int var5 = Math.round(var3.height());
      var8 = null;
      Bitmap var13 = var8;
      if (var6 > 0) {
         var13 = var8;
         if (var5 > 0) {
            float var4 = Math.min(1.0F, 1048576.0F / (float)(var6 * var5));
            var6 = (int)((float)var6 * var4);
            var5 = (int)((float)var5 * var4);
            if (this.mTempMatrix == null) {
               this.mTempMatrix = new Matrix();
            }

            this.mTempMatrix.set(var2);
            this.mTempMatrix.postTranslate(-var3.left, -var3.top);
            this.mTempMatrix.postScale(var4, var4);
            var13 = Bitmap.createBitmap(var6, var5, Config.ARGB_8888);
            Canvas var11 = new Canvas(var13);
            var11.concat(this.mTempMatrix);
            var1.draw(var11);
         }
      }

      return var13;
   }

   public View onCreateSnapshotView(Context var1, Parcelable var2) {
      ImageView var3 = null;
      if (var2 instanceof Bundle) {
         Bundle var4 = (Bundle)var2;
         Bitmap var8 = (Bitmap)var4.getParcelable("sharedElement:snapshot:bitmap");
         if (var8 == null) {
            return null;
         }

         ImageView var6 = new ImageView(var1);
         var6.setImageBitmap(var8);
         var6.setScaleType(ScaleType.valueOf(var4.getString("sharedElement:snapshot:imageScaleType")));
         var3 = var6;
         if (var6.getScaleType() == ScaleType.MATRIX) {
            float[] var9 = var4.getFloatArray("sharedElement:snapshot:imageMatrix");
            Matrix var10 = new Matrix();
            var10.setValues(var9);
            var6.setImageMatrix(var10);
            var3 = var6;
         }
      } else if (var2 instanceof Bitmap) {
         Bitmap var7 = (Bitmap)var2;
         ImageView var5 = new ImageView(var1);
         var5.setImageBitmap(var7);
         return var5;
      }

      return var3;
   }

   public void onMapSharedElements(List var1, Map var2) {
   }

   public void onRejectSharedElements(List var1) {
   }

   public void onSharedElementEnd(List var1, List var2, List var3) {
   }

   public void onSharedElementStart(List var1, List var2, List var3) {
   }

   public void onSharedElementsArrived(List var1, List var2, SharedElementCallback.OnSharedElementsReadyListener var3) {
      var3.onSharedElementsReady();
   }

   public interface OnSharedElementsReadyListener {
      void onSharedElementsReady();
   }
}
