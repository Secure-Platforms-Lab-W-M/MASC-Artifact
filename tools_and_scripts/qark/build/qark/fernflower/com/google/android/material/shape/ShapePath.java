package com.google.android.material.shape;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.google.android.material.shadow.ShadowRenderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapePath {
   protected static final float ANGLE_LEFT = 180.0F;
   private static final float ANGLE_UP = 270.0F;
   @Deprecated
   public float currentShadowAngle;
   @Deprecated
   public float endShadowAngle;
   @Deprecated
   public float endX;
   @Deprecated
   public float endY;
   private final List operations = new ArrayList();
   private final List shadowCompatOperations = new ArrayList();
   @Deprecated
   public float startX;
   @Deprecated
   public float startY;

   public ShapePath() {
      this.reset(0.0F, 0.0F);
   }

   public ShapePath(float var1, float var2) {
      this.reset(var1, var2);
   }

   private void addConnectingShadowIfNecessary(float var1) {
      if (this.getCurrentShadowAngle() != var1) {
         float var2 = (var1 - this.getCurrentShadowAngle() + 360.0F) % 360.0F;
         if (var2 <= 180.0F) {
            ShapePath.PathArcOperation var3 = new ShapePath.PathArcOperation(this.getEndX(), this.getEndY(), this.getEndX(), this.getEndY());
            var3.setStartAngle(this.getCurrentShadowAngle());
            var3.setSweepAngle(var2);
            this.shadowCompatOperations.add(new ShapePath.ArcShadowOperation(var3));
            this.setCurrentShadowAngle(var1);
         }
      }
   }

   private void addShadowCompatOperation(ShapePath.ShadowCompatOperation var1, float var2, float var3) {
      this.addConnectingShadowIfNecessary(var2);
      this.shadowCompatOperations.add(var1);
      this.setCurrentShadowAngle(var3);
   }

   private float getCurrentShadowAngle() {
      return this.currentShadowAngle;
   }

   private float getEndShadowAngle() {
      return this.endShadowAngle;
   }

   private void setCurrentShadowAngle(float var1) {
      this.currentShadowAngle = var1;
   }

   private void setEndShadowAngle(float var1) {
      this.endShadowAngle = var1;
   }

   private void setEndX(float var1) {
      this.endX = var1;
   }

   private void setEndY(float var1) {
      this.endY = var1;
   }

   private void setStartX(float var1) {
      this.startX = var1;
   }

   private void setStartY(float var1) {
      this.startY = var1;
   }

   public void addArc(float var1, float var2, float var3, float var4, float var5, float var6) {
      ShapePath.PathArcOperation var10 = new ShapePath.PathArcOperation(var1, var2, var3, var4);
      var10.setStartAngle(var5);
      var10.setSweepAngle(var6);
      this.operations.add(var10);
      ShapePath.ArcShadowOperation var11 = new ShapePath.ArcShadowOperation(var10);
      float var8 = var5 + var6;
      boolean var9;
      if (var6 < 0.0F) {
         var9 = true;
      } else {
         var9 = false;
      }

      float var7;
      if (var9) {
         var7 = (var5 + 180.0F) % 360.0F;
      } else {
         var7 = var5;
      }

      if (var9) {
         var8 = (180.0F + var8) % 360.0F;
      }

      this.addShadowCompatOperation(var11, var7, var8);
      this.setEndX((var1 + var3) * 0.5F + (var3 - var1) / 2.0F * (float)Math.cos(Math.toRadians((double)(var5 + var6))));
      this.setEndY((var2 + var4) * 0.5F + (var4 - var2) / 2.0F * (float)Math.sin(Math.toRadians((double)(var5 + var6))));
   }

   public void applyToPath(Matrix var1, Path var2) {
      int var3 = 0;

      for(int var4 = this.operations.size(); var3 < var4; ++var3) {
         ((ShapePath.PathOperation)this.operations.get(var3)).applyToPath(var1, var2);
      }

   }

   ShapePath.ShadowCompatOperation createShadowCompatOperation(final Matrix var1) {
      this.addConnectingShadowIfNecessary(this.getEndShadowAngle());
      return new ShapePath.ShadowCompatOperation(new ArrayList(this.shadowCompatOperations)) {
         // $FF: synthetic field
         final List val$operations;

         {
            this.val$operations = var2;
         }

         public void draw(Matrix var1x, ShadowRenderer var2, int var3, Canvas var4) {
            Iterator var5 = this.val$operations.iterator();

            while(var5.hasNext()) {
               ((ShapePath.ShadowCompatOperation)var5.next()).draw(var1, var2, var3, var4);
            }

         }
      };
   }

   float getEndX() {
      return this.endX;
   }

   float getEndY() {
      return this.endY;
   }

   float getStartX() {
      return this.startX;
   }

   float getStartY() {
      return this.startY;
   }

   public void lineTo(float var1, float var2) {
      ShapePath.PathLineOperation var3 = new ShapePath.PathLineOperation();
      var3.field_58 = var1;
      var3.field_59 = var2;
      this.operations.add(var3);
      ShapePath.LineShadowOperation var4 = new ShapePath.LineShadowOperation(var3, this.getEndX(), this.getEndY());
      this.addShadowCompatOperation(var4, var4.getAngle() + 270.0F, var4.getAngle() + 270.0F);
      this.setEndX(var1);
      this.setEndY(var2);
   }

   public void quadToPoint(float var1, float var2, float var3, float var4) {
      ShapePath.PathQuadOperation var5 = new ShapePath.PathQuadOperation();
      var5.setControlX(var1);
      var5.setControlY(var2);
      var5.setEndX(var3);
      var5.setEndY(var4);
      this.operations.add(var5);
      this.setEndX(var3);
      this.setEndY(var4);
   }

   public void reset(float var1, float var2) {
      this.reset(var1, var2, 270.0F, 0.0F);
   }

   public void reset(float var1, float var2, float var3, float var4) {
      this.setStartX(var1);
      this.setStartY(var2);
      this.setEndX(var1);
      this.setEndY(var2);
      this.setCurrentShadowAngle(var3);
      this.setEndShadowAngle((var3 + var4) % 360.0F);
      this.operations.clear();
      this.shadowCompatOperations.clear();
   }

   static class ArcShadowOperation extends ShapePath.ShadowCompatOperation {
      private final ShapePath.PathArcOperation operation;

      public ArcShadowOperation(ShapePath.PathArcOperation var1) {
         this.operation = var1;
      }

      public void draw(Matrix var1, ShadowRenderer var2, int var3, Canvas var4) {
         float var5 = this.operation.getStartAngle();
         float var6 = this.operation.getSweepAngle();
         var2.drawCornerShadow(var4, var1, new RectF(this.operation.getLeft(), this.operation.getTop(), this.operation.getRight(), this.operation.getBottom()), var3, var5, var6);
      }
   }

   static class LineShadowOperation extends ShapePath.ShadowCompatOperation {
      private final ShapePath.PathLineOperation operation;
      private final float startX;
      private final float startY;

      public LineShadowOperation(ShapePath.PathLineOperation var1, float var2, float var3) {
         this.operation = var1;
         this.startX = var2;
         this.startY = var3;
      }

      public void draw(Matrix var1, ShadowRenderer var2, int var3, Canvas var4) {
         float var5 = this.operation.field_59;
         float var6 = this.startY;
         float var7 = this.operation.field_58;
         float var8 = this.startX;
         RectF var9 = new RectF(0.0F, 0.0F, (float)Math.hypot((double)(var5 - var6), (double)(var7 - var8)), 0.0F);
         var1 = new Matrix(var1);
         var1.preTranslate(this.startX, this.startY);
         var1.preRotate(this.getAngle());
         var2.drawEdgeShadow(var4, var1, var9, var3);
      }

      float getAngle() {
         return (float)Math.toDegrees(Math.atan((double)((this.operation.field_59 - this.startY) / (this.operation.field_58 - this.startX))));
      }
   }

   public static class PathArcOperation extends ShapePath.PathOperation {
      private static final RectF rectF = new RectF();
      @Deprecated
      public float bottom;
      @Deprecated
      public float left;
      @Deprecated
      public float right;
      @Deprecated
      public float startAngle;
      @Deprecated
      public float sweepAngle;
      @Deprecated
      public float top;

      public PathArcOperation(float var1, float var2, float var3, float var4) {
         this.setLeft(var1);
         this.setTop(var2);
         this.setRight(var3);
         this.setBottom(var4);
      }

      private float getBottom() {
         return this.bottom;
      }

      private float getLeft() {
         return this.left;
      }

      private float getRight() {
         return this.right;
      }

      private float getStartAngle() {
         return this.startAngle;
      }

      private float getSweepAngle() {
         return this.sweepAngle;
      }

      private float getTop() {
         return this.top;
      }

      private void setBottom(float var1) {
         this.bottom = var1;
      }

      private void setLeft(float var1) {
         this.left = var1;
      }

      private void setRight(float var1) {
         this.right = var1;
      }

      private void setStartAngle(float var1) {
         this.startAngle = var1;
      }

      private void setSweepAngle(float var1) {
         this.sweepAngle = var1;
      }

      private void setTop(float var1) {
         this.top = var1;
      }

      public void applyToPath(Matrix var1, Path var2) {
         Matrix var3 = this.matrix;
         var1.invert(var3);
         var2.transform(var3);
         rectF.set(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
         var2.arcTo(rectF, this.getStartAngle(), this.getSweepAngle(), false);
         var2.transform(var1);
      }
   }

   public static class PathLineOperation extends ShapePath.PathOperation {
      // $FF: renamed from: x float
      private float field_58;
      // $FF: renamed from: y float
      private float field_59;

      public void applyToPath(Matrix var1, Path var2) {
         Matrix var3 = this.matrix;
         var1.invert(var3);
         var2.transform(var3);
         var2.lineTo(this.field_58, this.field_59);
         var2.transform(var1);
      }
   }

   public abstract static class PathOperation {
      protected final Matrix matrix = new Matrix();

      public abstract void applyToPath(Matrix var1, Path var2);
   }

   public static class PathQuadOperation extends ShapePath.PathOperation {
      @Deprecated
      public float controlX;
      @Deprecated
      public float controlY;
      @Deprecated
      public float endX;
      @Deprecated
      public float endY;

      private float getControlX() {
         return this.controlX;
      }

      private float getControlY() {
         return this.controlY;
      }

      private float getEndX() {
         return this.endX;
      }

      private float getEndY() {
         return this.endY;
      }

      private void setControlX(float var1) {
         this.controlX = var1;
      }

      private void setControlY(float var1) {
         this.controlY = var1;
      }

      private void setEndX(float var1) {
         this.endX = var1;
      }

      private void setEndY(float var1) {
         this.endY = var1;
      }

      public void applyToPath(Matrix var1, Path var2) {
         Matrix var3 = this.matrix;
         var1.invert(var3);
         var2.transform(var3);
         var2.quadTo(this.getControlX(), this.getControlY(), this.getEndX(), this.getEndY());
         var2.transform(var1);
      }
   }

   abstract static class ShadowCompatOperation {
      static final Matrix IDENTITY_MATRIX = new Matrix();

      public abstract void draw(Matrix var1, ShadowRenderer var2, int var3, Canvas var4);

      public final void draw(ShadowRenderer var1, int var2, Canvas var3) {
         this.draw(IDENTITY_MATRIX, var1, var2, var3);
      }
   }
}
