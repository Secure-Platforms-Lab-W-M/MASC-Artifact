package com.yalantis.ucrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.FastBitmapDrawable;
import com.yalantis.ucrop.util.RectUtils;

public class TransformImageView extends ImageView {
   private static final int MATRIX_VALUES_COUNT = 9;
   private static final int RECT_CENTER_POINT_COORDS = 2;
   private static final int RECT_CORNER_POINTS_COORDS = 8;
   private static final String TAG = "TransformImageView";
   protected boolean mBitmapDecoded;
   protected boolean mBitmapLaidOut;
   protected final float[] mCurrentImageCenter;
   protected final float[] mCurrentImageCorners;
   protected Matrix mCurrentImageMatrix;
   private ExifInfo mExifInfo;
   private String mImageInputPath;
   private String mImageOutputPath;
   private float[] mInitialImageCenter;
   private float[] mInitialImageCorners;
   private final float[] mMatrixValues;
   private int mMaxBitmapSize;
   protected int mThisHeight;
   protected int mThisWidth;
   protected TransformImageView.TransformImageListener mTransformImageListener;

   public TransformImageView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TransformImageView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public TransformImageView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mCurrentImageCorners = new float[8];
      this.mCurrentImageCenter = new float[2];
      this.mMatrixValues = new float[9];
      this.mCurrentImageMatrix = new Matrix();
      this.mBitmapDecoded = false;
      this.mBitmapLaidOut = false;
      this.mMaxBitmapSize = 0;
      this.init();
   }

   private void updateCurrentImagePoints() {
      this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCorners, this.mInitialImageCorners);
      this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCenter, this.mInitialImageCenter);
   }

   public float getCurrentAngle() {
      return this.getMatrixAngle(this.mCurrentImageMatrix);
   }

   public float getCurrentScale() {
      return this.getMatrixScale(this.mCurrentImageMatrix);
   }

   public ExifInfo getExifInfo() {
      return this.mExifInfo;
   }

   public String getImageInputPath() {
      return this.mImageInputPath;
   }

   public String getImageOutputPath() {
      return this.mImageOutputPath;
   }

   public float getMatrixAngle(Matrix var1) {
      return (float)(-(Math.atan2((double)this.getMatrixValue(var1, 1), (double)this.getMatrixValue(var1, 0)) * 57.29577951308232D));
   }

   public float getMatrixScale(Matrix var1) {
      return (float)Math.sqrt(Math.pow((double)this.getMatrixValue(var1, 0), 2.0D) + Math.pow((double)this.getMatrixValue(var1, 3), 2.0D));
   }

   protected float getMatrixValue(Matrix var1, int var2) {
      var1.getValues(this.mMatrixValues);
      return this.mMatrixValues[var2];
   }

   public int getMaxBitmapSize() {
      if (this.mMaxBitmapSize <= 0) {
         this.mMaxBitmapSize = BitmapLoadUtils.calculateMaxBitmapSize(this.getContext());
      }

      return this.mMaxBitmapSize;
   }

   public Bitmap getViewBitmap() {
      return this.getDrawable() != null && this.getDrawable() instanceof FastBitmapDrawable ? ((FastBitmapDrawable)this.getDrawable()).getBitmap() : null;
   }

   protected void init() {
      this.setScaleType(ScaleType.MATRIX);
   }

   protected void onImageLaidOut() {
      Drawable var3 = this.getDrawable();
      if (var3 != null) {
         float var1 = (float)var3.getIntrinsicWidth();
         float var2 = (float)var3.getIntrinsicHeight();
         Log.d("TransformImageView", String.format("Image size: [%d:%d]", (int)var1, (int)var2));
         RectF var4 = new RectF(0.0F, 0.0F, var1, var2);
         this.mInitialImageCorners = RectUtils.getCornersFromRect(var4);
         this.mInitialImageCenter = RectUtils.getCenterFromRect(var4);
         this.mBitmapLaidOut = true;
         TransformImageView.TransformImageListener var5 = this.mTransformImageListener;
         if (var5 != null) {
            var5.onLoadComplete();
         }

      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (var1 || this.mBitmapDecoded && !this.mBitmapLaidOut) {
         var2 = this.getPaddingLeft();
         var3 = this.getPaddingTop();
         var4 = this.getWidth();
         var5 = this.getPaddingRight();
         int var6 = this.getHeight();
         int var7 = this.getPaddingBottom();
         this.mThisWidth = var4 - var5 - var2;
         this.mThisHeight = var6 - var7 - var3;
         this.onImageLaidOut();
      }

   }

   public void postRotate(float var1, float var2, float var3) {
      if (var1 != 0.0F) {
         this.mCurrentImageMatrix.postRotate(var1, var2, var3);
         this.setImageMatrix(this.mCurrentImageMatrix);
         TransformImageView.TransformImageListener var4 = this.mTransformImageListener;
         if (var4 != null) {
            var4.onRotate(this.getMatrixAngle(this.mCurrentImageMatrix));
         }
      }

   }

   public void postScale(float var1, float var2, float var3) {
      if (var1 != 0.0F) {
         this.mCurrentImageMatrix.postScale(var1, var1, var2, var3);
         this.setImageMatrix(this.mCurrentImageMatrix);
         TransformImageView.TransformImageListener var4 = this.mTransformImageListener;
         if (var4 != null) {
            var4.onScale(this.getMatrixScale(this.mCurrentImageMatrix));
         }
      }

   }

   public void postTranslate(float var1, float var2) {
      if (var1 != 0.0F || var2 != 0.0F) {
         this.mCurrentImageMatrix.postTranslate(var1, var2);
         this.setImageMatrix(this.mCurrentImageMatrix);
      }

   }

   protected void printMatrix(String var1, Matrix var2) {
      float var3 = this.getMatrixValue(var2, 2);
      float var4 = this.getMatrixValue(var2, 5);
      float var5 = this.getMatrixScale(var2);
      float var6 = this.getMatrixAngle(var2);
      StringBuilder var7 = new StringBuilder();
      var7.append(var1);
      var7.append(": matrix: { x: ");
      var7.append(var3);
      var7.append(", y: ");
      var7.append(var4);
      var7.append(", scale: ");
      var7.append(var5);
      var7.append(", angle: ");
      var7.append(var6);
      var7.append(" }");
      Log.d("TransformImageView", var7.toString());
   }

   public void setImageBitmap(Bitmap var1) {
      this.setImageDrawable(new FastBitmapDrawable(var1));
   }

   public void setImageMatrix(Matrix var1) {
      super.setImageMatrix(var1);
      this.mCurrentImageMatrix.set(var1);
      this.updateCurrentImagePoints();
   }

   public void setImageUri(Uri var1, Uri var2) throws Exception {
      int var3 = this.getMaxBitmapSize();
      BitmapLoadUtils.decodeBitmapInBackground(this.getContext(), var1, var2, var3, var3, new BitmapLoadCallback() {
         public void onBitmapLoaded(Bitmap var1, ExifInfo var2, String var3, String var4) {
            TransformImageView.this.mImageInputPath = var3;
            TransformImageView.this.mImageOutputPath = var4;
            TransformImageView.this.mExifInfo = var2;
            TransformImageView.this.mBitmapDecoded = true;
            TransformImageView.this.setImageBitmap(var1);
         }

         public void onFailure(Exception var1) {
            Log.e("TransformImageView", "onFailure: setImageUri", var1);
            if (TransformImageView.this.mTransformImageListener != null) {
               TransformImageView.this.mTransformImageListener.onLoadFailure(var1);
            }

         }
      });
   }

   public void setMaxBitmapSize(int var1) {
      this.mMaxBitmapSize = var1;
   }

   public void setScaleType(ScaleType var1) {
      if (var1 == ScaleType.MATRIX) {
         super.setScaleType(var1);
      } else {
         Log.w("TransformImageView", "Invalid ScaleType. Only ScaleType.MATRIX can be used");
      }
   }

   public void setTransformImageListener(TransformImageView.TransformImageListener var1) {
      this.mTransformImageListener = var1;
   }

   public interface TransformImageListener {
      void onLoadComplete();

      void onLoadFailure(Exception var1);

      void onRotate(float var1);

      void onScale(float var1);
   }
}
