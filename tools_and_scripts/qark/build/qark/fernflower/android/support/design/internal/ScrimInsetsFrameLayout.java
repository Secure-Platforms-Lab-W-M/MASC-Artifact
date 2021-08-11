package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ScrimInsetsFrameLayout extends FrameLayout {
   Drawable mInsetForeground;
   Rect mInsets;
   private Rect mTempRect;

   public ScrimInsetsFrameLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ScrimInsetsFrameLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ScrimInsetsFrameLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mTempRect = new Rect();
      TypedArray var4 = var1.obtainStyledAttributes(var2, R$styleable.ScrimInsetsFrameLayout, var3, R$style.Widget_Design_ScrimInsetsFrameLayout);
      this.mInsetForeground = var4.getDrawable(R$styleable.ScrimInsetsFrameLayout_insetForeground);
      var4.recycle();
      this.setWillNotDraw(true);
      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            if (ScrimInsetsFrameLayout.this.mInsets == null) {
               ScrimInsetsFrameLayout.this.mInsets = new Rect();
            }

            ScrimInsetsFrameLayout.this.mInsets.set(var2.getSystemWindowInsetLeft(), var2.getSystemWindowInsetTop(), var2.getSystemWindowInsetRight(), var2.getSystemWindowInsetBottom());
            ScrimInsetsFrameLayout.this.onInsetsChanged(var2);
            ScrimInsetsFrameLayout var4 = ScrimInsetsFrameLayout.this;
            boolean var3;
            if (var2.hasSystemWindowInsets() && ScrimInsetsFrameLayout.this.mInsetForeground != null) {
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

   public void draw(@NonNull Canvas var1) {
      super.draw(var1);
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      if (this.mInsets != null && this.mInsetForeground != null) {
         int var4 = var1.save();
         var1.translate((float)this.getScrollX(), (float)this.getScrollY());
         this.mTempRect.set(0, 0, var2, this.mInsets.top);
         this.mInsetForeground.setBounds(this.mTempRect);
         this.mInsetForeground.draw(var1);
         this.mTempRect.set(0, var3 - this.mInsets.bottom, var2, var3);
         this.mInsetForeground.setBounds(this.mTempRect);
         this.mInsetForeground.draw(var1);
         this.mTempRect.set(0, this.mInsets.top, this.mInsets.left, var3 - this.mInsets.bottom);
         this.mInsetForeground.setBounds(this.mTempRect);
         this.mInsetForeground.draw(var1);
         this.mTempRect.set(var2 - this.mInsets.right, this.mInsets.top, var2, var3 - this.mInsets.bottom);
         this.mInsetForeground.setBounds(this.mTempRect);
         this.mInsetForeground.draw(var1);
         var1.restoreToCount(var4);
      }
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      Drawable var1 = this.mInsetForeground;
      if (var1 != null) {
         var1.setCallback(this);
      }
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      Drawable var1 = this.mInsetForeground;
      if (var1 != null) {
         var1.setCallback((Callback)null);
      }
   }

   protected void onInsetsChanged(WindowInsetsCompat var1) {
   }
}
