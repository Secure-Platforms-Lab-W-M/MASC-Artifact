package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;

public class ScrimInsetsFrameLayout extends FrameLayout {
   private boolean drawBottomInsetForeground;
   private boolean drawTopInsetForeground;
   Drawable insetForeground;
   Rect insets;
   private Rect tempRect;

   public ScrimInsetsFrameLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ScrimInsetsFrameLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ScrimInsetsFrameLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.tempRect = new Rect();
      this.drawTopInsetForeground = true;
      this.drawBottomInsetForeground = true;
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.ScrimInsetsFrameLayout, var3, style.Widget_Design_ScrimInsetsFrameLayout);
      this.insetForeground = var4.getDrawable(styleable.ScrimInsetsFrameLayout_insetForeground);
      var4.recycle();
      this.setWillNotDraw(true);
      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            if (ScrimInsetsFrameLayout.this.insets == null) {
               ScrimInsetsFrameLayout.this.insets = new Rect();
            }

            ScrimInsetsFrameLayout.this.insets.set(var2.getSystemWindowInsetLeft(), var2.getSystemWindowInsetTop(), var2.getSystemWindowInsetRight(), var2.getSystemWindowInsetBottom());
            ScrimInsetsFrameLayout.this.onInsetsChanged(var2);
            ScrimInsetsFrameLayout var4 = ScrimInsetsFrameLayout.this;
            boolean var3;
            if (var2.hasSystemWindowInsets() && ScrimInsetsFrameLayout.this.insetForeground != null) {
               var3 = false;
            } else {
               var3 = true;
            }

            var4.setWillNotDraw(var3);
            ViewCompat.postInvalidateOnAnimation(ScrimInsetsFrameLayout.this);
            return var2.consumeSystemWindowInsets();
         }
      });
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      if (this.insets != null && this.insetForeground != null) {
         int var4 = var1.save();
         var1.translate((float)this.getScrollX(), (float)this.getScrollY());
         if (this.drawTopInsetForeground) {
            this.tempRect.set(0, 0, var2, this.insets.top);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(var1);
         }

         if (this.drawBottomInsetForeground) {
            this.tempRect.set(0, var3 - this.insets.bottom, var2, var3);
            this.insetForeground.setBounds(this.tempRect);
            this.insetForeground.draw(var1);
         }

         this.tempRect.set(0, this.insets.top, this.insets.left, var3 - this.insets.bottom);
         this.insetForeground.setBounds(this.tempRect);
         this.insetForeground.draw(var1);
         this.tempRect.set(var2 - this.insets.right, this.insets.top, var2, var3 - this.insets.bottom);
         this.insetForeground.setBounds(this.tempRect);
         this.insetForeground.draw(var1);
         var1.restoreToCount(var4);
      }

   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      Drawable var1 = this.insetForeground;
      if (var1 != null) {
         var1.setCallback(this);
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      Drawable var1 = this.insetForeground;
      if (var1 != null) {
         var1.setCallback((Callback)null);
      }

   }

   protected void onInsetsChanged(WindowInsetsCompat var1) {
   }

   public void setDrawBottomInsetForeground(boolean var1) {
      this.drawBottomInsetForeground = var1;
   }

   public void setDrawTopInsetForeground(boolean var1) {
      this.drawTopInsetForeground = var1;
   }

   public void setScrimInsetForeground(Drawable var1) {
      this.insetForeground = var1;
   }
}
