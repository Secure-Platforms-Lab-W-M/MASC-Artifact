package com.google.android.material.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class ShapeAppearancePathProvider {
   private final ShapePath[] cornerPaths = new ShapePath[4];
   private final Matrix[] cornerTransforms = new Matrix[4];
   private final Matrix[] edgeTransforms = new Matrix[4];
   private final PointF pointF = new PointF();
   private final float[] scratch = new float[2];
   private final float[] scratch2 = new float[2];
   private final ShapePath shapePath = new ShapePath();

   public ShapeAppearancePathProvider() {
      for(int var1 = 0; var1 < 4; ++var1) {
         this.cornerPaths[var1] = new ShapePath();
         this.cornerTransforms[var1] = new Matrix();
         this.edgeTransforms[var1] = new Matrix();
      }

   }

   private float angleOfEdge(int var1) {
      return (float)((var1 + 1) * 90);
   }

   private void appendCornerPath(ShapeAppearancePathProvider.ShapeAppearancePathSpec var1, int var2) {
      this.scratch[0] = this.cornerPaths[var2].getStartX();
      this.scratch[1] = this.cornerPaths[var2].getStartY();
      this.cornerTransforms[var2].mapPoints(this.scratch);
      Path var3;
      float[] var4;
      if (var2 == 0) {
         var3 = var1.path;
         var4 = this.scratch;
         var3.moveTo(var4[0], var4[1]);
      } else {
         var3 = var1.path;
         var4 = this.scratch;
         var3.lineTo(var4[0], var4[1]);
      }

      this.cornerPaths[var2].applyToPath(this.cornerTransforms[var2], var1.path);
      if (var1.pathListener != null) {
         var1.pathListener.onCornerPathCreated(this.cornerPaths[var2], this.cornerTransforms[var2], var2);
      }

   }

   private void appendEdgePath(ShapeAppearancePathProvider.ShapeAppearancePathSpec var1, int var2) {
      int var5 = (var2 + 1) % 4;
      this.scratch[0] = this.cornerPaths[var2].getEndX();
      this.scratch[1] = this.cornerPaths[var2].getEndY();
      this.cornerTransforms[var2].mapPoints(this.scratch);
      this.scratch2[0] = this.cornerPaths[var5].getStartX();
      this.scratch2[1] = this.cornerPaths[var5].getStartY();
      this.cornerTransforms[var5].mapPoints(this.scratch2);
      float[] var6 = this.scratch;
      float var3 = var6[0];
      float[] var7 = this.scratch2;
      var3 = Math.max((float)Math.hypot((double)(var3 - var7[0]), (double)(var6[1] - var7[1])) - 0.001F, 0.0F);
      float var4 = this.getEdgeCenterForIndex(var1.bounds, var2);
      this.shapePath.reset(0.0F, 0.0F);
      this.getEdgeTreatmentForIndex(var2, var1.shapeAppearanceModel).getEdgePath(var3, var4, var1.interpolation, this.shapePath);
      this.shapePath.applyToPath(this.edgeTransforms[var2], var1.path);
      if (var1.pathListener != null) {
         var1.pathListener.onEdgePathCreated(this.shapePath, this.edgeTransforms[var2], var2);
      }

   }

   private void getCoordinatesOfCorner(int var1, RectF var2, PointF var3) {
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 != 3) {
               var3.set(var2.right, var2.top);
            } else {
               var3.set(var2.left, var2.top);
            }
         } else {
            var3.set(var2.left, var2.bottom);
         }
      } else {
         var3.set(var2.right, var2.bottom);
      }
   }

   private CornerSize getCornerSizeForIndex(int var1, ShapeAppearanceModel var2) {
      if (var1 != 1) {
         if (var1 != 2) {
            return var1 != 3 ? var2.getTopRightCornerSize() : var2.getTopLeftCornerSize();
         } else {
            return var2.getBottomLeftCornerSize();
         }
      } else {
         return var2.getBottomRightCornerSize();
      }
   }

   private CornerTreatment getCornerTreatmentForIndex(int var1, ShapeAppearanceModel var2) {
      if (var1 != 1) {
         if (var1 != 2) {
            return var1 != 3 ? var2.getTopRightCorner() : var2.getTopLeftCorner();
         } else {
            return var2.getBottomLeftCorner();
         }
      } else {
         return var2.getBottomRightCorner();
      }
   }

   private float getEdgeCenterForIndex(RectF var1, int var2) {
      this.scratch[0] = this.cornerPaths[var2].endX;
      this.scratch[1] = this.cornerPaths[var2].endY;
      this.cornerTransforms[var2].mapPoints(this.scratch);
      return var2 != 1 && var2 != 3 ? Math.abs(var1.centerY() - this.scratch[1]) : Math.abs(var1.centerX() - this.scratch[0]);
   }

   private EdgeTreatment getEdgeTreatmentForIndex(int var1, ShapeAppearanceModel var2) {
      if (var1 != 1) {
         if (var1 != 2) {
            return var1 != 3 ? var2.getRightEdge() : var2.getTopEdge();
         } else {
            return var2.getLeftEdge();
         }
      } else {
         return var2.getBottomEdge();
      }
   }

   private void setCornerPathAndTransform(ShapeAppearancePathProvider.ShapeAppearancePathSpec var1, int var2) {
      CornerSize var4 = this.getCornerSizeForIndex(var2, var1.shapeAppearanceModel);
      this.getCornerTreatmentForIndex(var2, var1.shapeAppearanceModel).getCornerPath(this.cornerPaths[var2], 90.0F, var1.interpolation, var1.bounds, var4);
      float var3 = this.angleOfEdge(var2);
      this.cornerTransforms[var2].reset();
      this.getCoordinatesOfCorner(var2, var1.bounds, this.pointF);
      this.cornerTransforms[var2].setTranslate(this.pointF.x, this.pointF.y);
      this.cornerTransforms[var2].preRotate(var3);
   }

   private void setEdgePathAndTransform(int var1) {
      this.scratch[0] = this.cornerPaths[var1].getEndX();
      this.scratch[1] = this.cornerPaths[var1].getEndY();
      this.cornerTransforms[var1].mapPoints(this.scratch);
      float var2 = this.angleOfEdge(var1);
      this.edgeTransforms[var1].reset();
      Matrix var3 = this.edgeTransforms[var1];
      float[] var4 = this.scratch;
      var3.setTranslate(var4[0], var4[1]);
      this.edgeTransforms[var1].preRotate(var2);
   }

   public void calculatePath(ShapeAppearanceModel var1, float var2, RectF var3, Path var4) {
      this.calculatePath(var1, var2, var3, (ShapeAppearancePathProvider.PathListener)null, var4);
   }

   public void calculatePath(ShapeAppearanceModel var1, float var2, RectF var3, ShapeAppearancePathProvider.PathListener var4, Path var5) {
      var5.rewind();
      ShapeAppearancePathProvider.ShapeAppearancePathSpec var7 = new ShapeAppearancePathProvider.ShapeAppearancePathSpec(var1, var2, var3, var4, var5);

      int var6;
      for(var6 = 0; var6 < 4; ++var6) {
         this.setCornerPathAndTransform(var7, var6);
         this.setEdgePathAndTransform(var6);
      }

      for(var6 = 0; var6 < 4; ++var6) {
         this.appendCornerPath(var7, var6);
         this.appendEdgePath(var7, var6);
      }

      var5.close();
   }

   public interface PathListener {
      void onCornerPathCreated(ShapePath var1, Matrix var2, int var3);

      void onEdgePathCreated(ShapePath var1, Matrix var2, int var3);
   }

   static final class ShapeAppearancePathSpec {
      public final RectF bounds;
      public final float interpolation;
      public final Path path;
      public final ShapeAppearancePathProvider.PathListener pathListener;
      public final ShapeAppearanceModel shapeAppearanceModel;

      ShapeAppearancePathSpec(ShapeAppearanceModel var1, float var2, RectF var3, ShapeAppearancePathProvider.PathListener var4, Path var5) {
         this.pathListener = var4;
         this.shapeAppearanceModel = var1;
         this.interpolation = var2;
         this.bounds = var3;
         this.path = var5;
      }
   }
}
