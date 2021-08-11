package com.google.android.material.snackbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.id;
import com.google.android.material.R.styleable;
import com.google.android.material.color.MaterialColors;

public class SnackbarContentLayout extends LinearLayout implements ContentViewCallback {
   private Button actionView;
   private int maxInlineActionWidth;
   private int maxWidth;
   private TextView messageView;

   public SnackbarContentLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SnackbarContentLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, styleable.SnackbarLayout);
      this.maxWidth = var3.getDimensionPixelSize(styleable.SnackbarLayout_android_maxWidth, -1);
      this.maxInlineActionWidth = var3.getDimensionPixelSize(styleable.SnackbarLayout_maxActionInlineWidth, -1);
      var3.recycle();
   }

   private static void updateTopBottomPadding(View var0, int var1, int var2) {
      if (ViewCompat.isPaddingRelative(var0)) {
         ViewCompat.setPaddingRelative(var0, ViewCompat.getPaddingStart(var0), var1, ViewCompat.getPaddingEnd(var0), var2);
      } else {
         var0.setPadding(var0.getPaddingLeft(), var1, var0.getPaddingRight(), var2);
      }
   }

   private boolean updateViewsWithinLayout(int var1, int var2, int var3) {
      boolean var4 = false;
      if (var1 != this.getOrientation()) {
         this.setOrientation(var1);
         var4 = true;
      }

      if (this.messageView.getPaddingTop() != var2 || this.messageView.getPaddingBottom() != var3) {
         updateTopBottomPadding(this.messageView, var2, var3);
         var4 = true;
      }

      return var4;
   }

   public void animateContentIn(int var1, int var2) {
      this.messageView.setAlpha(0.0F);
      this.messageView.animate().alpha(1.0F).setDuration((long)var2).setStartDelay((long)var1).start();
      if (this.actionView.getVisibility() == 0) {
         this.actionView.setAlpha(0.0F);
         this.actionView.animate().alpha(1.0F).setDuration((long)var2).setStartDelay((long)var1).start();
      }

   }

   public void animateContentOut(int var1, int var2) {
      this.messageView.setAlpha(1.0F);
      this.messageView.animate().alpha(0.0F).setDuration((long)var2).setStartDelay((long)var1).start();
      if (this.actionView.getVisibility() == 0) {
         this.actionView.setAlpha(1.0F);
         this.actionView.animate().alpha(0.0F).setDuration((long)var2).setStartDelay((long)var1).start();
      }

   }

   public Button getActionView() {
      return this.actionView;
   }

   public TextView getMessageView() {
      return this.messageView;
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.messageView = (TextView)this.findViewById(id.snackbar_text);
      this.actionView = (Button)this.findViewById(id.snackbar_action);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      int var3 = var1;
      int var4;
      int var5;
      if (this.maxWidth > 0) {
         var4 = this.getMeasuredWidth();
         var5 = this.maxWidth;
         var3 = var1;
         if (var4 > var5) {
            var3 = MeasureSpec.makeMeasureSpec(var5, 1073741824);
            super.onMeasure(var3, var2);
         }
      }

      var4 = this.getResources().getDimensionPixelSize(dimen.design_snackbar_padding_vertical_2lines);
      var5 = this.getResources().getDimensionPixelSize(dimen.design_snackbar_padding_vertical);
      boolean var7;
      if (this.messageView.getLayout().getLineCount() > 1) {
         var7 = true;
      } else {
         var7 = false;
      }

      boolean var6 = false;
      if (var7 && this.maxInlineActionWidth > 0 && this.actionView.getMeasuredWidth() > this.maxInlineActionWidth) {
         var7 = var6;
         if (this.updateViewsWithinLayout(1, var4, var4 - var5)) {
            var7 = true;
         }
      } else {
         if (!var7) {
            var4 = var5;
         }

         var7 = var6;
         if (this.updateViewsWithinLayout(0, var4, var4)) {
            var7 = true;
         }
      }

      if (var7) {
         super.onMeasure(var3, var2);
      }

   }

   void updateActionTextColorAlphaIfNeeded(float var1) {
      if (var1 != 1.0F) {
         int var2 = this.actionView.getCurrentTextColor();
         var2 = MaterialColors.layer(MaterialColors.getColor(this, attr.colorSurface), var2, var1);
         this.actionView.setTextColor(var2);
      }

   }
}
