package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.yalantis.ucrop.R.color;
import com.yalantis.ucrop.R.dimen;
import com.yalantis.ucrop.R.styleable;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;
import com.yalantis.ucrop.util.RectUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OverlayView extends View {
   public static final boolean DEFAULT_CIRCLE_DIMMED_LAYER = false;
   public static final int DEFAULT_CROP_GRID_COLUMN_COUNT = 2;
   public static final int DEFAULT_CROP_GRID_ROW_COUNT = 2;
   public static final int DEFAULT_FREESTYLE_CROP_MODE = 0;
   public static final boolean DEFAULT_SHOW_CROP_FRAME = true;
   public static final boolean DEFAULT_SHOW_CROP_GRID = true;
   public static final int FREESTYLE_CROP_MODE_DISABLE = 0;
   public static final int FREESTYLE_CROP_MODE_ENABLE = 1;
   public static final int FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH = 2;
   private OverlayViewChangeListener mCallback;
   private boolean mCircleDimmedLayer;
   private Path mCircularPath;
   private Paint mCropFrameCornersPaint;
   private Paint mCropFramePaint;
   protected float[] mCropGridCenter;
   private int mCropGridColumnCount;
   protected float[] mCropGridCorners;
   private Paint mCropGridPaint;
   private int mCropGridRowCount;
   private int mCropRectCornerTouchAreaLineLength;
   private int mCropRectMinSize;
   private final RectF mCropViewRect;
   private int mCurrentTouchCornerIndex;
   private int mDimmedColor;
   private Paint mDimmedStrokePaint;
   private int mFreestyleCropMode;
   private float[] mGridPoints;
   private float mPreviousTouchX;
   private float mPreviousTouchY;
   private boolean mShouldSetupCropBounds;
   private boolean mShowCropFrame;
   private boolean mShowCropGrid;
   private float mTargetAspectRatio;
   private final RectF mTempRect;
   protected int mThisHeight;
   protected int mThisWidth;
   private int mTouchPointThreshold;

   public OverlayView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public OverlayView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public OverlayView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mCropViewRect = new RectF();
      this.mTempRect = new RectF();
      this.mGridPoints = null;
      this.mCircularPath = new Path();
      this.mDimmedStrokePaint = new Paint(1);
      this.mCropGridPaint = new Paint(1);
      this.mCropFramePaint = new Paint(1);
      this.mCropFrameCornersPaint = new Paint(1);
      this.mFreestyleCropMode = 0;
      this.mPreviousTouchX = -1.0F;
      this.mPreviousTouchY = -1.0F;
      this.mCurrentTouchCornerIndex = -1;
      this.mTouchPointThreshold = this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_rect_corner_touch_threshold);
      this.mCropRectMinSize = this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_rect_min_size);
      this.mCropRectCornerTouchAreaLineLength = this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_rect_corner_touch_area_line_length);
      this.init();
   }

   private int getCurrentTouchIndex(float var1, float var2) {
      int var10 = -1;
      double var3 = (double)this.mTouchPointThreshold;

      double var5;
      for(int var9 = 0; var9 < 8; var3 = var5) {
         double var7 = Math.sqrt(Math.pow((double)(var1 - this.mCropGridCorners[var9]), 2.0D) + Math.pow((double)(var2 - this.mCropGridCorners[var9 + 1]), 2.0D));
         var5 = var3;
         if (var7 < var3) {
            var5 = var7;
            var10 = var9 / 2;
         }

         var9 += 2;
      }

      if (this.mFreestyleCropMode == 1 && var10 < 0 && this.mCropViewRect.contains(var1, var2)) {
         return 4;
      } else {
         return var10;
      }
   }

   private void initCropFrameStyle(TypedArray var1) {
      int var2 = var1.getDimensionPixelSize(styleable.ucrop_UCropView_ucrop_frame_stroke_size, this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_frame_stoke_width));
      int var3 = var1.getColor(styleable.ucrop_UCropView_ucrop_frame_color, this.getResources().getColor(color.ucrop_color_default_crop_frame));
      this.mCropFramePaint.setStrokeWidth((float)var2);
      this.mCropFramePaint.setColor(var3);
      this.mCropFramePaint.setStyle(Style.STROKE);
      this.mCropFrameCornersPaint.setStrokeWidth((float)(var2 * 3));
      this.mCropFrameCornersPaint.setColor(var3);
      this.mCropFrameCornersPaint.setStyle(Style.STROKE);
   }

   private void initCropGridStyle(TypedArray var1) {
      int var2 = var1.getDimensionPixelSize(styleable.ucrop_UCropView_ucrop_grid_stroke_size, this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_grid_stoke_width));
      int var3 = var1.getColor(styleable.ucrop_UCropView_ucrop_grid_color, this.getResources().getColor(color.ucrop_color_default_crop_grid));
      this.mCropGridPaint.setStrokeWidth((float)var2);
      this.mCropGridPaint.setColor(var3);
      this.mCropGridRowCount = var1.getInt(styleable.ucrop_UCropView_ucrop_grid_row_count, 2);
      this.mCropGridColumnCount = var1.getInt(styleable.ucrop_UCropView_ucrop_grid_column_count, 2);
   }

   private void updateCropViewRect(float var1, float var2) {
      this.mTempRect.set(this.mCropViewRect);
      int var4 = this.mCurrentTouchCornerIndex;
      boolean var5 = true;
      if (var4 != 0) {
         if (var4 != 1) {
            if (var4 != 2) {
               if (var4 != 3) {
                  if (var4 == 4) {
                     this.mTempRect.offset(var1 - this.mPreviousTouchX, var2 - this.mPreviousTouchY);
                     if (this.mTempRect.left > (float)this.getLeft() && this.mTempRect.top > (float)this.getTop() && this.mTempRect.right < (float)this.getRight() && this.mTempRect.bottom < (float)this.getBottom()) {
                        this.mCropViewRect.set(this.mTempRect);
                        this.updateGridPoints();
                        this.postInvalidate();
                     }

                     return;
                  }
               } else {
                  this.mTempRect.set(var1, this.mCropViewRect.top, this.mCropViewRect.right, var2);
               }
            } else {
               this.mTempRect.set(this.mCropViewRect.left, this.mCropViewRect.top, var1, var2);
            }
         } else {
            this.mTempRect.set(this.mCropViewRect.left, var2, var1, this.mCropViewRect.bottom);
         }
      } else {
         this.mTempRect.set(var1, var2, this.mCropViewRect.right, this.mCropViewRect.bottom);
      }

      boolean var8;
      if (this.mTempRect.height() >= (float)this.mCropRectMinSize) {
         var8 = true;
      } else {
         var8 = false;
      }

      if (this.mTempRect.width() < (float)this.mCropRectMinSize) {
         var5 = false;
      }

      RectF var7 = this.mCropViewRect;
      if (var5) {
         var1 = this.mTempRect.left;
      } else {
         var1 = var7.left;
      }

      RectF var6;
      if (var8) {
         var6 = this.mTempRect;
      } else {
         var6 = this.mCropViewRect;
      }

      var2 = var6.top;
      if (var5) {
         var6 = this.mTempRect;
      } else {
         var6 = this.mCropViewRect;
      }

      float var3 = var6.right;
      if (var8) {
         var6 = this.mTempRect;
      } else {
         var6 = this.mCropViewRect;
      }

      var7.set(var1, var2, var3, var6.bottom);
      if (var8 || var5) {
         this.updateGridPoints();
         this.postInvalidate();
      }

   }

   private void updateGridPoints() {
      this.mCropGridCorners = RectUtils.getCornersFromRect(this.mCropViewRect);
      this.mCropGridCenter = RectUtils.getCenterFromRect(this.mCropViewRect);
      this.mGridPoints = null;
      this.mCircularPath.reset();
      this.mCircularPath.addCircle(this.mCropViewRect.centerX(), this.mCropViewRect.centerY(), Math.min(this.mCropViewRect.width(), this.mCropViewRect.height()) / 2.0F, Direction.CW);
   }

   protected void drawCropGrid(Canvas var1) {
      int var2;
      if (this.mShowCropGrid) {
         float[] var5;
         if (this.mGridPoints == null && !this.mCropViewRect.isEmpty()) {
            this.mGridPoints = new float[this.mCropGridRowCount * 4 + this.mCropGridColumnCount * 4];
            var2 = 0;

            int var3;
            int var4;
            for(var3 = 0; var3 < this.mCropGridRowCount; var2 = var4 + 1) {
               var5 = this.mGridPoints;
               var4 = var2 + 1;
               var5[var2] = this.mCropViewRect.left;
               var5 = this.mGridPoints;
               var2 = var4 + 1;
               var5[var4] = this.mCropViewRect.height() * (((float)var3 + 1.0F) / (float)(this.mCropGridRowCount + 1)) + this.mCropViewRect.top;
               var5 = this.mGridPoints;
               var4 = var2 + 1;
               var5[var2] = this.mCropViewRect.right;
               this.mGridPoints[var4] = this.mCropViewRect.height() * (((float)var3 + 1.0F) / (float)(this.mCropGridRowCount + 1)) + this.mCropViewRect.top;
               ++var3;
            }

            for(var3 = 0; var3 < this.mCropGridColumnCount; var2 = var4 + 1) {
               var5 = this.mGridPoints;
               var4 = var2 + 1;
               var5[var2] = this.mCropViewRect.width() * (((float)var3 + 1.0F) / (float)(this.mCropGridColumnCount + 1)) + this.mCropViewRect.left;
               var5 = this.mGridPoints;
               var2 = var4 + 1;
               var5[var4] = this.mCropViewRect.top;
               var5 = this.mGridPoints;
               var4 = var2 + 1;
               var5[var2] = this.mCropViewRect.width() * (((float)var3 + 1.0F) / (float)(this.mCropGridColumnCount + 1)) + this.mCropViewRect.left;
               this.mGridPoints[var4] = this.mCropViewRect.bottom;
               ++var3;
            }
         }

         var5 = this.mGridPoints;
         if (var5 != null) {
            var1.drawLines(var5, this.mCropGridPaint);
         }
      }

      if (this.mShowCropFrame) {
         var1.drawRect(this.mCropViewRect, this.mCropFramePaint);
      }

      if (this.mFreestyleCropMode != 0) {
         var1.save();
         this.mTempRect.set(this.mCropViewRect);
         RectF var6 = this.mTempRect;
         var2 = this.mCropRectCornerTouchAreaLineLength;
         var6.inset((float)var2, (float)(-var2));
         var1.clipRect(this.mTempRect, Op.DIFFERENCE);
         this.mTempRect.set(this.mCropViewRect);
         var6 = this.mTempRect;
         var2 = this.mCropRectCornerTouchAreaLineLength;
         var6.inset((float)(-var2), (float)var2);
         var1.clipRect(this.mTempRect, Op.DIFFERENCE);
         var1.drawRect(this.mCropViewRect, this.mCropFrameCornersPaint);
         var1.restore();
      }

   }

   protected void drawDimmedLayer(Canvas var1) {
      var1.save();
      if (this.mCircleDimmedLayer) {
         var1.clipPath(this.mCircularPath, Op.DIFFERENCE);
      } else {
         var1.clipRect(this.mCropViewRect, Op.DIFFERENCE);
      }

      var1.drawColor(this.mDimmedColor);
      var1.restore();
      if (this.mCircleDimmedLayer) {
         var1.drawCircle(this.mCropViewRect.centerX(), this.mCropViewRect.centerY(), Math.min(this.mCropViewRect.width(), this.mCropViewRect.height()) / 2.0F, this.mDimmedStrokePaint);
      }

   }

   public RectF getCropViewRect() {
      return this.mCropViewRect;
   }

   public int getFreestyleCropMode() {
      return this.mFreestyleCropMode;
   }

   public OverlayViewChangeListener getOverlayViewChangeListener() {
      return this.mCallback;
   }

   protected void init() {
      if (VERSION.SDK_INT < 18) {
         this.setLayerType(1, (Paint)null);
      }

   }

   @Deprecated
   public boolean isFreestyleCropEnabled() {
      return this.mFreestyleCropMode == 1;
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      this.drawDimmedLayer(var1);
      this.drawCropGrid(var1);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (var1) {
         var2 = this.getPaddingLeft();
         var3 = this.getPaddingTop();
         var4 = this.getWidth();
         var5 = this.getPaddingRight();
         int var6 = this.getHeight();
         int var7 = this.getPaddingBottom();
         this.mThisWidth = var4 - var5 - var2;
         this.mThisHeight = var6 - var7 - var3;
         if (this.mShouldSetupCropBounds) {
            this.mShouldSetupCropBounds = false;
            this.setTargetAspectRatio(this.mTargetAspectRatio);
         }
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var6 = this.mCropViewRect.isEmpty();
      boolean var5 = false;
      if (!var6) {
         if (this.mFreestyleCropMode == 0) {
            return false;
         } else {
            float var3 = var1.getX();
            float var2 = var1.getY();
            if ((var1.getAction() & 255) == 0) {
               int var4 = this.getCurrentTouchIndex(var3, var2);
               this.mCurrentTouchCornerIndex = var4;
               if (var4 != -1) {
                  var5 = true;
               }

               if (!var5) {
                  this.mPreviousTouchX = -1.0F;
                  this.mPreviousTouchY = -1.0F;
                  return var5;
               } else {
                  if (this.mPreviousTouchX < 0.0F) {
                     this.mPreviousTouchX = var3;
                     this.mPreviousTouchY = var2;
                  }

                  return var5;
               }
            } else if ((var1.getAction() & 255) == 2 && var1.getPointerCount() == 1 && this.mCurrentTouchCornerIndex != -1) {
               var3 = Math.min(Math.max(var3, (float)this.getPaddingLeft()), (float)(this.getWidth() - this.getPaddingRight()));
               var2 = Math.min(Math.max(var2, (float)this.getPaddingTop()), (float)(this.getHeight() - this.getPaddingBottom()));
               this.updateCropViewRect(var3, var2);
               this.mPreviousTouchX = var3;
               this.mPreviousTouchY = var2;
               return true;
            } else {
               if ((var1.getAction() & 255) == 1) {
                  this.mPreviousTouchX = -1.0F;
                  this.mPreviousTouchY = -1.0F;
                  this.mCurrentTouchCornerIndex = -1;
                  OverlayViewChangeListener var7 = this.mCallback;
                  if (var7 != null) {
                     var7.onCropRectUpdated(this.mCropViewRect);
                  }
               }

               return false;
            }
         }
      } else {
         return false;
      }
   }

   protected void processStyledAttributes(TypedArray var1) {
      this.mCircleDimmedLayer = var1.getBoolean(styleable.ucrop_UCropView_ucrop_circle_dimmed_layer, false);
      int var2 = var1.getColor(styleable.ucrop_UCropView_ucrop_dimmed_color, this.getResources().getColor(color.ucrop_color_default_dimmed));
      this.mDimmedColor = var2;
      this.mDimmedStrokePaint.setColor(var2);
      this.mDimmedStrokePaint.setStyle(Style.STROKE);
      this.mDimmedStrokePaint.setStrokeWidth(1.0F);
      this.initCropFrameStyle(var1);
      this.mShowCropFrame = var1.getBoolean(styleable.ucrop_UCropView_ucrop_show_frame, true);
      this.initCropGridStyle(var1);
      this.mShowCropGrid = var1.getBoolean(styleable.ucrop_UCropView_ucrop_show_grid, true);
   }

   public void setCircleDimmedLayer(boolean var1) {
      this.mCircleDimmedLayer = var1;
   }

   public void setCropFrameColor(int var1) {
      this.mCropFramePaint.setColor(var1);
   }

   public void setCropFrameStrokeWidth(int var1) {
      this.mCropFramePaint.setStrokeWidth((float)var1);
   }

   public void setCropGridColor(int var1) {
      this.mCropGridPaint.setColor(var1);
   }

   public void setCropGridColumnCount(int var1) {
      this.mCropGridColumnCount = var1;
      this.mGridPoints = null;
   }

   public void setCropGridRowCount(int var1) {
      this.mCropGridRowCount = var1;
      this.mGridPoints = null;
   }

   public void setCropGridStrokeWidth(int var1) {
      this.mCropGridPaint.setStrokeWidth((float)var1);
   }

   public void setDimmedColor(int var1) {
      this.mDimmedColor = var1;
   }

   @Deprecated
   public void setFreestyleCropEnabled(boolean var1) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public void setFreestyleCropMode(int var1) {
      this.mFreestyleCropMode = var1;
      this.postInvalidate();
   }

   public void setOverlayViewChangeListener(OverlayViewChangeListener var1) {
      this.mCallback = var1;
   }

   public void setShowCropFrame(boolean var1) {
      this.mShowCropFrame = var1;
   }

   public void setShowCropGrid(boolean var1) {
      this.mShowCropGrid = var1;
   }

   public void setTargetAspectRatio(float var1) {
      this.mTargetAspectRatio = var1;
      if (this.mThisWidth > 0) {
         this.setupCropBounds();
         this.postInvalidate();
      } else {
         this.mShouldSetupCropBounds = true;
      }
   }

   public void setupCropBounds() {
      int var3 = this.mThisWidth;
      float var1 = (float)var3;
      float var2 = this.mTargetAspectRatio;
      int var4 = (int)(var1 / var2);
      int var5 = this.mThisHeight;
      if (var4 > var5) {
         var4 = (int)((float)var5 * var2);
         var3 = (var3 - var4) / 2;
         this.mCropViewRect.set((float)(this.getPaddingLeft() + var3), (float)this.getPaddingTop(), (float)(this.getPaddingLeft() + var4 + var3), (float)(this.getPaddingTop() + this.mThisHeight));
      } else {
         var3 = (var5 - var4) / 2;
         this.mCropViewRect.set((float)this.getPaddingLeft(), (float)(this.getPaddingTop() + var3), (float)(this.getPaddingLeft() + this.mThisWidth), (float)(this.getPaddingTop() + var4 + var3));
      }

      OverlayViewChangeListener var6 = this.mCallback;
      if (var6 != null) {
         var6.onCropRectUpdated(this.mCropViewRect);
      }

      this.updateGridPoints();
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface FreestyleMode {
   }
}
