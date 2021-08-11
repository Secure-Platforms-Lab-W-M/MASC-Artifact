package androidx.transition;

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
      PathMeasure var3 = new PathMeasure(var2, false);
      this.mPathMeasure = var3;
      this.mPathLength = var3.getLength();
   }

   public Float get(Object var1) {
      return this.mCurrentFraction;
   }

   public void set(Object var1, Float var2) {
      this.mCurrentFraction = var2;
      this.mPathMeasure.getPosTan(this.mPathLength * var2, this.mPosition, (float[])null);
      this.mPointF.x = this.mPosition[0];
      this.mPointF.y = this.mPosition[1];
      this.mProperty.set(var1, this.mPointF);
   }
}
