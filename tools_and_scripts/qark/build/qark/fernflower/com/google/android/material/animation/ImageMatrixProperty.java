package com.google.android.material.animation;

import android.graphics.Matrix;
import android.util.Property;
import android.widget.ImageView;

public class ImageMatrixProperty extends Property {
   private final Matrix matrix = new Matrix();

   public ImageMatrixProperty() {
      super(Matrix.class, "imageMatrixProperty");
   }

   public Matrix get(ImageView var1) {
      this.matrix.set(var1.getImageMatrix());
      return this.matrix;
   }

   public void set(ImageView var1, Matrix var2) {
      var1.setImageMatrix(var2);
   }
}
