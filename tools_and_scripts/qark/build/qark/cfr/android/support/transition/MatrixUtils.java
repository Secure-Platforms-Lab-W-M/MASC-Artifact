/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Matrix$ScaleToFit
 *  android.graphics.RectF
 */
package android.support.transition;

import android.graphics.Matrix;
import android.graphics.RectF;

class MatrixUtils {
    static final Matrix IDENTITY_MATRIX = new Matrix(){

        void oops() {
            throw new IllegalStateException("Matrix can not be modified");
        }

        public boolean postConcat(Matrix matrix) {
            this.oops();
            return false;
        }

        public boolean postRotate(float f) {
            this.oops();
            return false;
        }

        public boolean postRotate(float f, float f2, float f3) {
            this.oops();
            return false;
        }

        public boolean postScale(float f, float f2) {
            this.oops();
            return false;
        }

        public boolean postScale(float f, float f2, float f3, float f4) {
            this.oops();
            return false;
        }

        public boolean postSkew(float f, float f2) {
            this.oops();
            return false;
        }

        public boolean postSkew(float f, float f2, float f3, float f4) {
            this.oops();
            return false;
        }

        public boolean postTranslate(float f, float f2) {
            this.oops();
            return false;
        }

        public boolean preConcat(Matrix matrix) {
            this.oops();
            return false;
        }

        public boolean preRotate(float f) {
            this.oops();
            return false;
        }

        public boolean preRotate(float f, float f2, float f3) {
            this.oops();
            return false;
        }

        public boolean preScale(float f, float f2) {
            this.oops();
            return false;
        }

        public boolean preScale(float f, float f2, float f3, float f4) {
            this.oops();
            return false;
        }

        public boolean preSkew(float f, float f2) {
            this.oops();
            return false;
        }

        public boolean preSkew(float f, float f2, float f3, float f4) {
            this.oops();
            return false;
        }

        public boolean preTranslate(float f, float f2) {
            this.oops();
            return false;
        }

        public void reset() {
            this.oops();
        }

        public void set(Matrix matrix) {
            this.oops();
        }

        public boolean setConcat(Matrix matrix, Matrix matrix2) {
            this.oops();
            return false;
        }

        public boolean setPolyToPoly(float[] arrf, int n, float[] arrf2, int n2, int n3) {
            this.oops();
            return false;
        }

        public boolean setRectToRect(RectF rectF, RectF rectF2, Matrix.ScaleToFit scaleToFit) {
            this.oops();
            return false;
        }

        public void setRotate(float f) {
            this.oops();
        }

        public void setRotate(float f, float f2, float f3) {
            this.oops();
        }

        public void setScale(float f, float f2) {
            this.oops();
        }

        public void setScale(float f, float f2, float f3, float f4) {
            this.oops();
        }

        public void setSinCos(float f, float f2) {
            this.oops();
        }

        public void setSinCos(float f, float f2, float f3, float f4) {
            this.oops();
        }

        public void setSkew(float f, float f2) {
            this.oops();
        }

        public void setSkew(float f, float f2, float f3, float f4) {
            this.oops();
        }

        public void setTranslate(float f, float f2) {
            this.oops();
        }

        public void setValues(float[] arrf) {
            this.oops();
        }
    };

    MatrixUtils() {
    }

}

