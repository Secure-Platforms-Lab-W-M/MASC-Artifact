package com.yalantis.ucrop.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.model.CropParameters;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.model.ImageState;
import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.FileUtils;
import com.yalantis.ucrop.util.ImageHeaderParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class BitmapCropTask extends AsyncTask {
   private static final String TAG = "BitmapCropTask";
   private int cropOffsetX;
   private int cropOffsetY;
   private final CompressFormat mCompressFormat;
   private final int mCompressQuality;
   private final WeakReference mContext;
   private final BitmapCropCallback mCropCallback;
   private final RectF mCropRect;
   private int mCroppedImageHeight;
   private int mCroppedImageWidth;
   private float mCurrentAngle;
   private final RectF mCurrentImageRect;
   private float mCurrentScale;
   private final ExifInfo mExifInfo;
   private final String mImageInputPath;
   private final String mImageOutputPath;
   private final int mMaxResultImageSizeX;
   private final int mMaxResultImageSizeY;
   private Bitmap mViewBitmap;

   public BitmapCropTask(Context var1, Bitmap var2, ImageState var3, CropParameters var4, BitmapCropCallback var5) {
      this.mContext = new WeakReference(var1);
      this.mViewBitmap = var2;
      this.mCropRect = var3.getCropRect();
      this.mCurrentImageRect = var3.getCurrentImageRect();
      this.mCurrentScale = var3.getCurrentScale();
      this.mCurrentAngle = var3.getCurrentAngle();
      this.mMaxResultImageSizeX = var4.getMaxResultImageSizeX();
      this.mMaxResultImageSizeY = var4.getMaxResultImageSizeY();
      this.mCompressFormat = var4.getCompressFormat();
      this.mCompressQuality = var4.getCompressQuality();
      this.mImageInputPath = var4.getImageInputPath();
      this.mImageOutputPath = var4.getImageOutputPath();
      this.mExifInfo = var4.getExifInfo();
      this.mCropCallback = var5;
   }

   private boolean crop() throws IOException {
      Bitmap var5;
      Bitmap var6;
      if (this.mMaxResultImageSizeX > 0 && this.mMaxResultImageSizeY > 0) {
         float var1 = this.mCropRect.width() / this.mCurrentScale;
         float var2 = this.mCropRect.height() / this.mCurrentScale;
         if (var1 > (float)this.mMaxResultImageSizeX || var2 > (float)this.mMaxResultImageSizeY) {
            var1 = Math.min((float)this.mMaxResultImageSizeX / var1, (float)this.mMaxResultImageSizeY / var2);
            var5 = this.mViewBitmap;
            var5 = Bitmap.createScaledBitmap(var5, Math.round((float)var5.getWidth() * var1), Math.round((float)this.mViewBitmap.getHeight() * var1), false);
            var6 = this.mViewBitmap;
            if (var6 != var5) {
               var6.recycle();
            }

            this.mViewBitmap = var5;
            this.mCurrentScale /= var1;
         }
      }

      if (this.mCurrentAngle != 0.0F) {
         Matrix var7 = new Matrix();
         var7.setRotate(this.mCurrentAngle, (float)(this.mViewBitmap.getWidth() / 2), (float)(this.mViewBitmap.getHeight() / 2));
         var6 = this.mViewBitmap;
         var5 = Bitmap.createBitmap(var6, 0, 0, var6.getWidth(), this.mViewBitmap.getHeight(), var7, true);
         var6 = this.mViewBitmap;
         if (var6 != var5) {
            var6.recycle();
         }

         this.mViewBitmap = var5;
      }

      this.cropOffsetX = Math.round((this.mCropRect.left - this.mCurrentImageRect.left) / this.mCurrentScale);
      this.cropOffsetY = Math.round((this.mCropRect.top - this.mCurrentImageRect.top) / this.mCurrentScale);
      this.mCroppedImageWidth = Math.round(this.mCropRect.width() / this.mCurrentScale);
      int var3 = Math.round(this.mCropRect.height() / this.mCurrentScale);
      this.mCroppedImageHeight = var3;
      boolean var4 = this.shouldCrop(this.mCroppedImageWidth, var3);
      StringBuilder var8 = new StringBuilder();
      var8.append("Should crop: ");
      var8.append(var4);
      Log.i("BitmapCropTask", var8.toString());
      if (var4) {
         ExifInterface var9 = new ExifInterface(this.mImageInputPath);
         this.saveImage(Bitmap.createBitmap(this.mViewBitmap, this.cropOffsetX, this.cropOffsetY, this.mCroppedImageWidth, this.mCroppedImageHeight));
         if (this.mCompressFormat.equals(CompressFormat.JPEG)) {
            ImageHeaderParser.copyExif(var9, this.mCroppedImageWidth, this.mCroppedImageHeight, this.mImageOutputPath);
         }

         return true;
      } else {
         FileUtils.copyFile(this.mImageInputPath, this.mImageOutputPath);
         return false;
      }
   }

   private void saveImage(Bitmap var1) throws FileNotFoundException {
      Context var3 = (Context)this.mContext.get();
      if (var3 != null) {
         OutputStream var2 = null;

         OutputStream var17;
         label134: {
            Throwable var10000;
            label140: {
               boolean var10001;
               try {
                  var17 = var3.getContentResolver().openOutputStream(Uri.fromFile(new File(this.mImageOutputPath)));
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label140;
               }

               var2 = var17;

               try {
                  var1.compress(this.mCompressFormat, this.mCompressQuality, var17);
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label140;
               }

               var2 = var17;

               label125:
               try {
                  var1.recycle();
                  break label134;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label125;
               }
            }

            Throwable var16 = var10000;
            BitmapLoadUtils.close(var2);
            throw var16;
         }

         BitmapLoadUtils.close(var17);
      }
   }

   private boolean shouldCrop(int var1, int var2) {
      var1 = 1 + Math.round((float)Math.max(var1, var2) / 1000.0F);
      return this.mMaxResultImageSizeX > 0 && this.mMaxResultImageSizeY > 0 || Math.abs(this.mCropRect.left - this.mCurrentImageRect.left) > (float)var1 || Math.abs(this.mCropRect.top - this.mCurrentImageRect.top) > (float)var1 || Math.abs(this.mCropRect.bottom - this.mCurrentImageRect.bottom) > (float)var1 || Math.abs(this.mCropRect.right - this.mCurrentImageRect.right) > (float)var1;
   }

   protected Throwable doInBackground(Void... var1) {
      Bitmap var4 = this.mViewBitmap;
      if (var4 == null) {
         return new NullPointerException("ViewBitmap is null");
      } else if (var4.isRecycled()) {
         return new NullPointerException("ViewBitmap is recycled");
      } else if (this.mCurrentImageRect.isEmpty()) {
         return new NullPointerException("CurrentImageRect is empty");
      } else {
         try {
            this.crop();
            this.mViewBitmap = null;
            return null;
         } catch (Throwable var3) {
            return var3;
         }
      }
   }

   protected void onPostExecute(Throwable var1) {
      BitmapCropCallback var2 = this.mCropCallback;
      if (var2 != null) {
         if (var1 == null) {
            Uri var3 = Uri.fromFile(new File(this.mImageOutputPath));
            this.mCropCallback.onBitmapCropped(var3, this.cropOffsetX, this.cropOffsetY, this.mCroppedImageWidth, this.mCroppedImageHeight);
            return;
         }

         var2.onCropFailure(var1);
      }

   }
}
