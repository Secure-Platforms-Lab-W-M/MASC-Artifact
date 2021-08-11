package android.support.transition;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Matrix.ScaleToFit;

class MatrixUtils {
   static final Matrix IDENTITY_MATRIX = new Matrix() {
      void oops() {
         throw new IllegalStateException("Matrix can not be modified");
      }

      public boolean postConcat(Matrix var1) {
         this.oops();
         return false;
      }

      public boolean postRotate(float var1) {
         this.oops();
         return false;
      }

      public boolean postRotate(float var1, float var2, float var3) {
         this.oops();
         return false;
      }

      public boolean postScale(float var1, float var2) {
         this.oops();
         return false;
      }

      public boolean postScale(float var1, float var2, float var3, float var4) {
         this.oops();
         return false;
      }

      public boolean postSkew(float var1, float var2) {
         this.oops();
         return false;
      }

      public boolean postSkew(float var1, float var2, float var3, float var4) {
         this.oops();
         return false;
      }

      public boolean postTranslate(float var1, float var2) {
         this.oops();
         return false;
      }

      public boolean preConcat(Matrix var1) {
         this.oops();
         return false;
      }

      public boolean preRotate(float var1) {
         this.oops();
         return false;
      }

      public boolean preRotate(float var1, float var2, float var3) {
         this.oops();
         return false;
      }

      public boolean preScale(float var1, float var2) {
         this.oops();
         return false;
      }

      public boolean preScale(float var1, float var2, float var3, float var4) {
         this.oops();
         return false;
      }

      public boolean preSkew(float var1, float var2) {
         this.oops();
         return false;
      }

      public boolean preSkew(float var1, float var2, float var3, float var4) {
         this.oops();
         return false;
      }

      public boolean preTranslate(float var1, float var2) {
         this.oops();
         return false;
      }

      public void reset() {
         this.oops();
      }

      public void set(Matrix var1) {
         this.oops();
      }

      public boolean setConcat(Matrix var1, Matrix var2) {
         this.oops();
         return false;
      }

      public boolean setPolyToPoly(float[] var1, int var2, float[] var3, int var4, int var5) {
         this.oops();
         return false;
      }

      public boolean setRectToRect(RectF var1, RectF var2, ScaleToFit var3) {
         this.oops();
         return false;
      }

      public void setRotate(float var1) {
         this.oops();
      }

      public void setRotate(float var1, float var2, float var3) {
         this.oops();
      }

      public void setScale(float var1, float var2) {
         this.oops();
      }

      public void setScale(float var1, float var2, float var3, float var4) {
         this.oops();
      }

      public void setSinCos(float var1, float var2) {
         this.oops();
      }

      public void setSinCos(float var1, float var2, float var3, float var4) {
         this.oops();
      }

      public void setSkew(float var1, float var2) {
         this.oops();
      }

      public void setSkew(float var1, float var2, float var3, float var4) {
         this.oops();
      }

      public void setTranslate(float var1, float var2) {
         this.oops();
      }

      public void setValues(float[] var1) {
         this.oops();
      }
   };
}
