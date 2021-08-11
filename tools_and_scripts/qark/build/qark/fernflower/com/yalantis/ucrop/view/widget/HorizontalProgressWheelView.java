package com.yalantis.ucrop.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.yalantis.ucrop.R.color;
import com.yalantis.ucrop.R.dimen;

public class HorizontalProgressWheelView extends View {
   private final Rect mCanvasClipBounds;
   private float mLastTouchedPosition;
   private int mMiddleLineColor;
   private int mProgressLineHeight;
   private int mProgressLineMargin;
   private Paint mProgressLinePaint;
   private int mProgressLineWidth;
   private boolean mScrollStarted;
   private HorizontalProgressWheelView.ScrollingListener mScrollingListener;
   private float mTotalScrollDistance;

   public HorizontalProgressWheelView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public HorizontalProgressWheelView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public HorizontalProgressWheelView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mCanvasClipBounds = new Rect();
      this.init();
   }

   public HorizontalProgressWheelView(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.mCanvasClipBounds = new Rect();
   }

   private void init() {
      this.mMiddleLineColor = ContextCompat.getColor(this.getContext(), color.ucrop_color_progress_wheel_line);
      this.mProgressLineWidth = this.getContext().getResources().getDimensionPixelSize(dimen.ucrop_width_horizontal_wheel_progress_line);
      this.mProgressLineHeight = this.getContext().getResources().getDimensionPixelSize(dimen.ucrop_height_horizontal_wheel_progress_line);
      this.mProgressLineMargin = this.getContext().getResources().getDimensionPixelSize(dimen.ucrop_margin_horizontal_wheel_progress_line);
      Paint var1 = new Paint(1);
      this.mProgressLinePaint = var1;
      var1.setStyle(Style.STROKE);
      this.mProgressLinePaint.setStrokeWidth((float)this.mProgressLineWidth);
   }

   private void onScrollEvent(MotionEvent var1, float var2) {
      this.mTotalScrollDistance -= var2;
      this.postInvalidate();
      this.mLastTouchedPosition = var1.getX();
      HorizontalProgressWheelView.ScrollingListener var3 = this.mScrollingListener;
      if (var3 != null) {
         var3.onScroll(-var2, this.mTotalScrollDistance);
      }

   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      var1.getClipBounds(this.mCanvasClipBounds);
      int var4 = this.mCanvasClipBounds.width();
      int var3 = this.mProgressLineWidth;
      int var5 = this.mProgressLineMargin;
      var4 /= var3 + var5;
      float var2 = this.mTotalScrollDistance % (float)(var5 + var3);
      this.mProgressLinePaint.setColor(this.getResources().getColor(color.ucrop_color_progress_wheel_line));

      for(var3 = 0; var3 < var4; ++var3) {
         if (var3 < var4 / 4) {
            this.mProgressLinePaint.setAlpha((int)((float)var3 / (float)(var4 / 4) * 255.0F));
         } else if (var3 > var4 * 3 / 4) {
            this.mProgressLinePaint.setAlpha((int)((float)(var4 - var3) / (float)(var4 / 4) * 255.0F));
         } else {
            this.mProgressLinePaint.setAlpha(255);
         }

         var1.drawLine(-var2 + (float)this.mCanvasClipBounds.left + (float)((this.mProgressLineWidth + this.mProgressLineMargin) * var3), (float)this.mCanvasClipBounds.centerY() - (float)this.mProgressLineHeight / 4.0F, -var2 + (float)this.mCanvasClipBounds.left + (float)((this.mProgressLineWidth + this.mProgressLineMargin) * var3), (float)this.mCanvasClipBounds.centerY() + (float)this.mProgressLineHeight / 4.0F, this.mProgressLinePaint);
      }

      this.mProgressLinePaint.setColor(this.mMiddleLineColor);
      var1.drawLine((float)this.mCanvasClipBounds.centerX(), (float)this.mCanvasClipBounds.centerY() - (float)this.mProgressLineHeight / 2.0F, (float)this.mCanvasClipBounds.centerX(), (float)this.mCanvasClipBounds.centerY() + (float)this.mProgressLineHeight / 2.0F, this.mProgressLinePaint);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var3 = var1.getAction();
      if (var3 != 0) {
         if (var3 != 1) {
            if (var3 != 2) {
               return true;
            }

            float var2 = var1.getX() - this.mLastTouchedPosition;
            if (var2 != 0.0F) {
               if (!this.mScrollStarted) {
                  this.mScrollStarted = true;
                  HorizontalProgressWheelView.ScrollingListener var4 = this.mScrollingListener;
                  if (var4 != null) {
                     var4.onScrollStart();
                  }
               }

               this.onScrollEvent(var1, var2);
               return true;
            }
         } else {
            HorizontalProgressWheelView.ScrollingListener var5 = this.mScrollingListener;
            if (var5 != null) {
               this.mScrollStarted = false;
               var5.onScrollEnd();
               return true;
            }
         }
      } else {
         this.mLastTouchedPosition = var1.getX();
      }

      return true;
   }

   public void setMiddleLineColor(int var1) {
      this.mMiddleLineColor = var1;
      this.invalidate();
   }

   public void setScrollingListener(HorizontalProgressWheelView.ScrollingListener var1) {
      this.mScrollingListener = var1;
   }

   public interface ScrollingListener {
      void onScroll(float var1, float var2);

      void onScrollEnd();

      void onScrollStart();
   }
}
