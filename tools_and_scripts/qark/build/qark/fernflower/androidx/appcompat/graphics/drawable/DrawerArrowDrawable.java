package androidx.appcompat.graphics.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.R.attr;
import androidx.appcompat.R.style;
import androidx.appcompat.R.styleable;
import androidx.core.graphics.drawable.DrawableCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable extends Drawable {
   public static final int ARROW_DIRECTION_END = 3;
   public static final int ARROW_DIRECTION_LEFT = 0;
   public static final int ARROW_DIRECTION_RIGHT = 1;
   public static final int ARROW_DIRECTION_START = 2;
   private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0D);
   private float mArrowHeadLength;
   private float mArrowShaftLength;
   private float mBarGap;
   private float mBarLength;
   private int mDirection = 2;
   private float mMaxCutForBarSize;
   private final Paint mPaint = new Paint();
   private final Path mPath = new Path();
   private float mProgress;
   private final int mSize;
   private boolean mSpin;
   private boolean mVerticalMirror = false;

   public DrawerArrowDrawable(Context var1) {
      this.mPaint.setStyle(Style.STROKE);
      this.mPaint.setStrokeJoin(Join.MITER);
      this.mPaint.setStrokeCap(Cap.BUTT);
      this.mPaint.setAntiAlias(true);
      TypedArray var2 = var1.getTheme().obtainStyledAttributes((AttributeSet)null, styleable.DrawerArrowToggle, attr.drawerArrowStyle, style.Base_Widget_AppCompat_DrawerArrowToggle);
      this.setColor(var2.getColor(styleable.DrawerArrowToggle_color, 0));
      this.setBarThickness(var2.getDimension(styleable.DrawerArrowToggle_thickness, 0.0F));
      this.setSpinEnabled(var2.getBoolean(styleable.DrawerArrowToggle_spinBars, true));
      this.setGapSize((float)Math.round(var2.getDimension(styleable.DrawerArrowToggle_gapBetweenBars, 0.0F)));
      this.mSize = var2.getDimensionPixelSize(styleable.DrawerArrowToggle_drawableSize, 0);
      this.mBarLength = (float)Math.round(var2.getDimension(styleable.DrawerArrowToggle_barLength, 0.0F));
      this.mArrowHeadLength = (float)Math.round(var2.getDimension(styleable.DrawerArrowToggle_arrowHeadLength, 0.0F));
      this.mArrowShaftLength = var2.getDimension(styleable.DrawerArrowToggle_arrowShaftLength, 0.0F);
      var2.recycle();
   }

   private static float lerp(float var0, float var1, float var2) {
      return (var1 - var0) * var2 + var0;
   }

   public void draw(Canvas var1) {
      Rect var12 = this.getBounds();
      int var11 = this.mDirection;
      boolean var9;
      if (var11 != 0) {
         if (var11 != 1) {
            boolean var10 = false;
            var9 = false;
            if (var11 != 3) {
               if (DrawableCompat.getLayoutDirection(this) == 1) {
                  var9 = true;
               }
            } else {
               var9 = var10;
               if (DrawableCompat.getLayoutDirection(this) == 0) {
                  var9 = true;
               }
            }
         } else {
            var9 = true;
         }
      } else {
         var9 = false;
      }

      float var2 = this.mArrowHeadLength;
      var2 = (float)Math.sqrt((double)(var2 * var2 * 2.0F));
      float var6 = lerp(this.mBarLength, var2, this.mProgress);
      float var4 = lerp(this.mBarLength, this.mArrowShaftLength, this.mProgress);
      float var5 = (float)Math.round(lerp(0.0F, this.mMaxCutForBarSize, this.mProgress));
      float var7 = lerp(0.0F, ARROW_HEAD_ANGLE, this.mProgress);
      if (var9) {
         var2 = 0.0F;
      } else {
         var2 = -180.0F;
      }

      float var3;
      if (var9) {
         var3 = 180.0F;
      } else {
         var3 = 0.0F;
      }

      var2 = lerp(var2, var3, this.mProgress);
      var3 = (float)Math.round((double)var6 * Math.cos((double)var7));
      var6 = (float)Math.round((double)var6 * Math.sin((double)var7));
      this.mPath.rewind();
      var7 = lerp(this.mBarGap + this.mPaint.getStrokeWidth(), -this.mMaxCutForBarSize, this.mProgress);
      float var8 = -var4 / 2.0F;
      this.mPath.moveTo(var8 + var5, 0.0F);
      this.mPath.rLineTo(var4 - var5 * 2.0F, 0.0F);
      this.mPath.moveTo(var8, var7);
      this.mPath.rLineTo(var3, var6);
      this.mPath.moveTo(var8, -var7);
      this.mPath.rLineTo(var3, -var6);
      this.mPath.close();
      var1.save();
      var3 = this.mPaint.getStrokeWidth();
      var5 = (float)var12.height();
      var4 = this.mBarGap;
      var5 = (float)((int)(var5 - 3.0F * var3 - 2.0F * var4) / 4 * 2);
      var1.translate((float)var12.centerX(), var5 + 1.5F * var3 + var4);
      if (this.mSpin) {
         byte var13;
         if (this.mVerticalMirror ^ var9) {
            var13 = -1;
         } else {
            var13 = 1;
         }

         var1.rotate((float)var13 * var2);
      } else if (var9) {
         var1.rotate(180.0F);
      }

      var1.drawPath(this.mPath, this.mPaint);
      var1.restore();
   }

   public float getArrowHeadLength() {
      return this.mArrowHeadLength;
   }

   public float getArrowShaftLength() {
      return this.mArrowShaftLength;
   }

   public float getBarLength() {
      return this.mBarLength;
   }

   public float getBarThickness() {
      return this.mPaint.getStrokeWidth();
   }

   public int getColor() {
      return this.mPaint.getColor();
   }

   public int getDirection() {
      return this.mDirection;
   }

   public float getGapSize() {
      return this.mBarGap;
   }

   public int getIntrinsicHeight() {
      return this.mSize;
   }

   public int getIntrinsicWidth() {
      return this.mSize;
   }

   public int getOpacity() {
      return -3;
   }

   public final Paint getPaint() {
      return this.mPaint;
   }

   public float getProgress() {
      return this.mProgress;
   }

   public boolean isSpinEnabled() {
      return this.mSpin;
   }

   public void setAlpha(int var1) {
      if (var1 != this.mPaint.getAlpha()) {
         this.mPaint.setAlpha(var1);
         this.invalidateSelf();
      }

   }

   public void setArrowHeadLength(float var1) {
      if (this.mArrowHeadLength != var1) {
         this.mArrowHeadLength = var1;
         this.invalidateSelf();
      }

   }

   public void setArrowShaftLength(float var1) {
      if (this.mArrowShaftLength != var1) {
         this.mArrowShaftLength = var1;
         this.invalidateSelf();
      }

   }

   public void setBarLength(float var1) {
      if (this.mBarLength != var1) {
         this.mBarLength = var1;
         this.invalidateSelf();
      }

   }

   public void setBarThickness(float var1) {
      if (this.mPaint.getStrokeWidth() != var1) {
         this.mPaint.setStrokeWidth(var1);
         this.mMaxCutForBarSize = (float)((double)(var1 / 2.0F) * Math.cos((double)ARROW_HEAD_ANGLE));
         this.invalidateSelf();
      }

   }

   public void setColor(int var1) {
      if (var1 != this.mPaint.getColor()) {
         this.mPaint.setColor(var1);
         this.invalidateSelf();
      }

   }

   public void setColorFilter(ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
      this.invalidateSelf();
   }

   public void setDirection(int var1) {
      if (var1 != this.mDirection) {
         this.mDirection = var1;
         this.invalidateSelf();
      }

   }

   public void setGapSize(float var1) {
      if (var1 != this.mBarGap) {
         this.mBarGap = var1;
         this.invalidateSelf();
      }

   }

   public void setProgress(float var1) {
      if (this.mProgress != var1) {
         this.mProgress = var1;
         this.invalidateSelf();
      }

   }

   public void setSpinEnabled(boolean var1) {
      if (this.mSpin != var1) {
         this.mSpin = var1;
         this.invalidateSelf();
      }

   }

   public void setVerticalMirror(boolean var1) {
      if (this.mVerticalMirror != var1) {
         this.mVerticalMirror = var1;
         this.invalidateSelf();
      }

   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface ArrowDirection {
   }
}
