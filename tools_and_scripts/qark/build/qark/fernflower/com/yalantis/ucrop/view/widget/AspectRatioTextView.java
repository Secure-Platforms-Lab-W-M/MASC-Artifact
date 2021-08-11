package com.yalantis.ucrop.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.yalantis.ucrop.R.color;
import com.yalantis.ucrop.R.dimen;
import com.yalantis.ucrop.R.styleable;
import com.yalantis.ucrop.model.AspectRatio;
import java.util.Locale;

public class AspectRatioTextView extends TextView {
   private float mAspectRatio;
   private String mAspectRatioTitle;
   private float mAspectRatioX;
   private float mAspectRatioY;
   private final Rect mCanvasClipBounds;
   private Paint mDotPaint;
   private int mDotSize;

   public AspectRatioTextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AspectRatioTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public AspectRatioTextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mCanvasClipBounds = new Rect();
      this.init(var1.obtainStyledAttributes(var2, styleable.ucrop_AspectRatioTextView));
   }

   public AspectRatioTextView(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.mCanvasClipBounds = new Rect();
      this.init(var1.obtainStyledAttributes(var2, styleable.ucrop_AspectRatioTextView));
   }

   private void applyActiveColor(int var1) {
      Paint var3 = this.mDotPaint;
      if (var3 != null) {
         var3.setColor(var1);
      }

      int[] var4 = new int[]{16842913};
      int var2 = ContextCompat.getColor(this.getContext(), color.ucrop_color_widget);
      this.setTextColor(new ColorStateList(new int[][]{var4, {0}}, new int[]{var1, var2}));
   }

   private void init(TypedArray var1) {
      this.setGravity(1);
      this.mAspectRatioTitle = var1.getString(styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_title);
      this.mAspectRatioX = var1.getFloat(styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_x, 0.0F);
      float var2 = var1.getFloat(styleable.ucrop_AspectRatioTextView_ucrop_artv_ratio_y, 0.0F);
      this.mAspectRatioY = var2;
      float var3 = this.mAspectRatioX;
      if (var3 != 0.0F && var2 != 0.0F) {
         this.mAspectRatio = var3 / var2;
      } else {
         this.mAspectRatio = 0.0F;
      }

      this.mDotSize = this.getContext().getResources().getDimensionPixelSize(dimen.ucrop_size_dot_scale_text_view);
      Paint var4 = new Paint(1);
      this.mDotPaint = var4;
      var4.setStyle(Style.FILL);
      this.setTitle();
      this.applyActiveColor(this.getResources().getColor(color.ucrop_color_widget_active));
      var1.recycle();
   }

   private void setTitle() {
      if (!TextUtils.isEmpty(this.mAspectRatioTitle)) {
         this.setText(this.mAspectRatioTitle);
      } else {
         this.setText(String.format(Locale.US, "%d:%d", (int)this.mAspectRatioX, (int)this.mAspectRatioY));
      }
   }

   private void toggleAspectRatio() {
      if (this.mAspectRatio != 0.0F) {
         float var1 = this.mAspectRatioX;
         float var2 = this.mAspectRatioY;
         this.mAspectRatioX = var2;
         this.mAspectRatioY = var1;
         this.mAspectRatio = var2 / var1;
      }

   }

   public float getAspectRatio(boolean var1) {
      if (var1) {
         this.toggleAspectRatio();
         this.setTitle();
      }

      return this.mAspectRatio;
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.isSelected()) {
         var1.getClipBounds(this.mCanvasClipBounds);
         float var2 = (float)(this.mCanvasClipBounds.right - this.mCanvasClipBounds.left) / 2.0F;
         int var3 = this.mCanvasClipBounds.bottom;
         int var4 = this.mDotSize;
         var1.drawCircle(var2, (float)(var3 - var4), (float)(var4 / 2), this.mDotPaint);
      }

   }

   public void setActiveColor(int var1) {
      this.applyActiveColor(var1);
      this.invalidate();
   }

   public void setAspectRatio(AspectRatio var1) {
      this.mAspectRatioTitle = var1.getAspectRatioTitle();
      this.mAspectRatioX = var1.getAspectRatioX();
      float var2 = var1.getAspectRatioY();
      this.mAspectRatioY = var2;
      float var3 = this.mAspectRatioX;
      if (var3 != 0.0F && var2 != 0.0F) {
         this.mAspectRatio = var3 / var2;
      } else {
         this.mAspectRatio = 0.0F;
      }

      this.setTitle();
   }
}
