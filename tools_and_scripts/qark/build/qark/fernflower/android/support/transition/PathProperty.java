package android.support.transition;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.Property;

class PathProperty extends Property {
   private float mCurrentFraction;
   private final float mPathLength;
   private final PathMeasure mPathMeasure;
   private final PointF mPointF = new PointF();
   private final float[] mPosition = new float[2];
   private final Property mProperty;

   PathProperty(Property var1, Path var2) {
      super(Float.class, var1.getName());
      this.mProperty = var1;
      this.mPathMeasure = new PathMeasure(var2, false);
      this.mPathLength = this.mPathMeasure.getLength();
   }

   public Float get(Object var1) {
      return this.mCurrentFraction;
   }

   public void set(Object var1, Float var2) {
      this.mCurrentFraction = var2;
      this.mPathMeasure.getPosTan(this.mPathLength * var2, this.mPosition, (float[])null);
      PointF var4 = this.mPointF;
      float[] var3 = this.mPosition;
      var4.x = var3[0];
      var4.y = var3[1];
      this.mProperty.set(var1, var4);
   }
}
