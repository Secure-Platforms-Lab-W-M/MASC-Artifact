package com.google.android.material.circularreveal;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.Path.Direction;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import com.google.android.material.math.MathUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CircularRevealHelper {
   public static final int BITMAP_SHADER = 0;
   public static final int CLIP_PATH = 1;
   private static final boolean DEBUG = false;
   public static final int REVEAL_ANIMATOR = 2;
   public static final int STRATEGY;
   private boolean buildingCircularRevealCache;
   private Paint debugPaint;
   private final CircularRevealHelper.Delegate delegate;
   private boolean hasCircularRevealCache;
   private Drawable overlayDrawable;
   private CircularRevealWidget.RevealInfo revealInfo;
   private final Paint revealPaint;
   private final Path revealPath;
   private final Paint scrimPaint;
   private final View view;

   static {
      if (VERSION.SDK_INT >= 21) {
         STRATEGY = 2;
      } else if (VERSION.SDK_INT >= 18) {
         STRATEGY = 1;
      } else {
         STRATEGY = 0;
      }
   }

   public CircularRevealHelper(CircularRevealHelper.Delegate var1) {
      this.delegate = var1;
      View var2 = (View)var1;
      this.view = var2;
      var2.setWillNotDraw(false);
      this.revealPath = new Path();
      this.revealPaint = new Paint(7);
      Paint var3 = new Paint(1);
      this.scrimPaint = var3;
      var3.setColor(0);
   }

   private void drawDebugCircle(Canvas var1, int var2, float var3) {
      this.debugPaint.setColor(var2);
      this.debugPaint.setStrokeWidth(var3);
      var1.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius - var3 / 2.0F, this.debugPaint);
   }

   private void drawDebugMode(Canvas var1) {
      this.delegate.actualDraw(var1);
      if (this.shouldDrawScrim()) {
         var1.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.scrimPaint);
      }

      if (this.shouldDrawCircularReveal()) {
         this.drawDebugCircle(var1, -16777216, 10.0F);
         this.drawDebugCircle(var1, -65536, 5.0F);
      }

      this.drawOverlayDrawable(var1);
   }

   private void drawOverlayDrawable(Canvas var1) {
      if (this.shouldDrawOverlayDrawable()) {
         Rect var4 = this.overlayDrawable.getBounds();
         float var2 = this.revealInfo.centerX - (float)var4.width() / 2.0F;
         float var3 = this.revealInfo.centerY - (float)var4.height() / 2.0F;
         var1.translate(var2, var3);
         this.overlayDrawable.draw(var1);
         var1.translate(-var2, -var3);
      }

   }

   private float getDistanceToFurthestCorner(CircularRevealWidget.RevealInfo var1) {
      return MathUtils.distanceToFurthestCorner(var1.centerX, var1.centerY, 0.0F, 0.0F, (float)this.view.getWidth(), (float)this.view.getHeight());
   }

   private void invalidateRevealInfo() {
      if (STRATEGY == 1) {
         this.revealPath.rewind();
         CircularRevealWidget.RevealInfo var1 = this.revealInfo;
         if (var1 != null) {
            this.revealPath.addCircle(var1.centerX, this.revealInfo.centerY, this.revealInfo.radius, Direction.CW);
         }
      }

      this.view.invalidate();
   }

   private boolean shouldDrawCircularReveal() {
      CircularRevealWidget.RevealInfo var4 = this.revealInfo;
      boolean var2 = false;
      boolean var3 = false;
      boolean var1;
      if (var4 != null && !var4.isInvalid()) {
         var1 = false;
      } else {
         var1 = true;
      }

      if (STRATEGY == 0) {
         var2 = var3;
         if (!var1) {
            var2 = var3;
            if (this.hasCircularRevealCache) {
               var2 = true;
            }
         }

         return var2;
      } else {
         if (!var1) {
            var2 = true;
         }

         return var2;
      }
   }

   private boolean shouldDrawOverlayDrawable() {
      return !this.buildingCircularRevealCache && this.overlayDrawable != null && this.revealInfo != null;
   }

   private boolean shouldDrawScrim() {
      return !this.buildingCircularRevealCache && Color.alpha(this.scrimPaint.getColor()) != 0;
   }

   public void buildCircularRevealCache() {
      if (STRATEGY == 0) {
         this.buildingCircularRevealCache = true;
         this.hasCircularRevealCache = false;
         this.view.buildDrawingCache();
         Bitmap var2 = this.view.getDrawingCache();
         Bitmap var1 = var2;
         if (var2 == null) {
            var1 = var2;
            if (this.view.getWidth() != 0) {
               var1 = var2;
               if (this.view.getHeight() != 0) {
                  var1 = Bitmap.createBitmap(this.view.getWidth(), this.view.getHeight(), Config.ARGB_8888);
                  Canvas var3 = new Canvas(var1);
                  this.view.draw(var3);
               }
            }
         }

         if (var1 != null) {
            this.revealPaint.setShader(new BitmapShader(var1, TileMode.CLAMP, TileMode.CLAMP));
         }

         this.buildingCircularRevealCache = false;
         this.hasCircularRevealCache = true;
      }

   }

   public void destroyCircularRevealCache() {
      if (STRATEGY == 0) {
         this.hasCircularRevealCache = false;
         this.view.destroyDrawingCache();
         this.revealPaint.setShader((Shader)null);
         this.view.invalidate();
      }

   }

   public void draw(Canvas var1) {
      if (this.shouldDrawCircularReveal()) {
         int var2 = STRATEGY;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Unsupported strategy ");
                  var3.append(STRATEGY);
                  throw new IllegalStateException(var3.toString());
               }

               this.delegate.actualDraw(var1);
               if (this.shouldDrawScrim()) {
                  var1.drawRect(0.0F, 0.0F, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
               }
            } else {
               var2 = var1.save();
               var1.clipPath(this.revealPath);
               this.delegate.actualDraw(var1);
               if (this.shouldDrawScrim()) {
                  var1.drawRect(0.0F, 0.0F, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
               }

               var1.restoreToCount(var2);
            }
         } else {
            var1.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.revealPaint);
            if (this.shouldDrawScrim()) {
               var1.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.scrimPaint);
            }
         }
      } else {
         this.delegate.actualDraw(var1);
         if (this.shouldDrawScrim()) {
            var1.drawRect(0.0F, 0.0F, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
         }
      }

      this.drawOverlayDrawable(var1);
   }

   public Drawable getCircularRevealOverlayDrawable() {
      return this.overlayDrawable;
   }

   public int getCircularRevealScrimColor() {
      return this.scrimPaint.getColor();
   }

   public CircularRevealWidget.RevealInfo getRevealInfo() {
      CircularRevealWidget.RevealInfo var1 = this.revealInfo;
      if (var1 == null) {
         return null;
      } else {
         var1 = new CircularRevealWidget.RevealInfo(var1);
         if (var1.isInvalid()) {
            var1.radius = this.getDistanceToFurthestCorner(var1);
         }

         return var1;
      }
   }

   public boolean isOpaque() {
      return this.delegate.actualIsOpaque() && !this.shouldDrawCircularReveal();
   }

   public void setCircularRevealOverlayDrawable(Drawable var1) {
      this.overlayDrawable = var1;
      this.view.invalidate();
   }

   public void setCircularRevealScrimColor(int var1) {
      this.scrimPaint.setColor(var1);
      this.view.invalidate();
   }

   public void setRevealInfo(CircularRevealWidget.RevealInfo var1) {
      if (var1 == null) {
         this.revealInfo = null;
      } else {
         CircularRevealWidget.RevealInfo var2 = this.revealInfo;
         if (var2 == null) {
            this.revealInfo = new CircularRevealWidget.RevealInfo(var1);
         } else {
            var2.set(var1);
         }

         if (MathUtils.geq(var1.radius, this.getDistanceToFurthestCorner(var1), 1.0E-4F)) {
            this.revealInfo.radius = Float.MAX_VALUE;
         }
      }

      this.invalidateRevealInfo();
   }

   public interface Delegate {
      void actualDraw(Canvas var1);

      boolean actualIsOpaque();
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface Strategy {
   }
}
