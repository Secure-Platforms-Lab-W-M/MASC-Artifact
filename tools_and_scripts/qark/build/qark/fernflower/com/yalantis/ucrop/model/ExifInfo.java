package com.yalantis.ucrop.model;

public class ExifInfo {
   private int mExifDegrees;
   private int mExifOrientation;
   private int mExifTranslation;

   public ExifInfo(int var1, int var2, int var3) {
      this.mExifOrientation = var1;
      this.mExifDegrees = var2;
      this.mExifTranslation = var3;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null) {
         if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            ExifInfo var2 = (ExifInfo)var1;
            if (this.mExifOrientation != var2.mExifOrientation) {
               return false;
            } else if (this.mExifDegrees != var2.mExifDegrees) {
               return false;
            } else {
               return this.mExifTranslation == var2.mExifTranslation;
            }
         }
      } else {
         return false;
      }
   }

   public int getExifDegrees() {
      return this.mExifDegrees;
   }

   public int getExifOrientation() {
      return this.mExifOrientation;
   }

   public int getExifTranslation() {
      return this.mExifTranslation;
   }

   public int hashCode() {
      return (this.mExifOrientation * 31 + this.mExifDegrees) * 31 + this.mExifTranslation;
   }

   public void setExifDegrees(int var1) {
      this.mExifDegrees = var1;
   }

   public void setExifOrientation(int var1) {
      this.mExifOrientation = var1;
   }

   public void setExifTranslation(int var1) {
      this.mExifTranslation = var1;
   }
}
