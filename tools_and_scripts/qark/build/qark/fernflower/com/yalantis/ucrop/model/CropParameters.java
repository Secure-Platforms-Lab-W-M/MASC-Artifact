package com.yalantis.ucrop.model;

import android.graphics.Bitmap.CompressFormat;

public class CropParameters {
   private CompressFormat mCompressFormat;
   private int mCompressQuality;
   private ExifInfo mExifInfo;
   private String mImageInputPath;
   private String mImageOutputPath;
   private int mMaxResultImageSizeX;
   private int mMaxResultImageSizeY;

   public CropParameters(int var1, int var2, CompressFormat var3, int var4, String var5, String var6, ExifInfo var7) {
      this.mMaxResultImageSizeX = var1;
      this.mMaxResultImageSizeY = var2;
      this.mCompressFormat = var3;
      this.mCompressQuality = var4;
      this.mImageInputPath = var5;
      this.mImageOutputPath = var6;
      this.mExifInfo = var7;
   }

   public CompressFormat getCompressFormat() {
      return this.mCompressFormat;
   }

   public int getCompressQuality() {
      return this.mCompressQuality;
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

   public int getMaxResultImageSizeX() {
      return this.mMaxResultImageSizeX;
   }

   public int getMaxResultImageSizeY() {
      return this.mMaxResultImageSizeY;
   }
}
