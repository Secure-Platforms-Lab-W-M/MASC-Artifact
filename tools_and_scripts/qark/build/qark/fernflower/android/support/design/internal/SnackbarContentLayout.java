package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.design.R$dimen;
import android.support.design.R$id;
import android.support.design.R$styleable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SnackbarContentLayout extends LinearLayout implements BaseTransientBottomBar.ContentViewCallback {
   private Button mActionView;
   private int mMaxInlineActionWidth;
   private int mMaxWidth;
   private TextView mMessageView;

   public SnackbarContentLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SnackbarContentLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.SnackbarLayout);
      this.mMaxWidth = var3.getDimensionPixelSize(R$styleable.SnackbarLayout_android_maxWidth, -1);
      this.mMaxInlineActionWidth = var3.getDimensionPixelSize(R$styleable.SnackbarLayout_maxActionInlineWidth, -1);
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

      if (this.mMessageView.getPaddingTop() == var2 && this.mMessageView.getPaddingBottom() == var3) {
         return var4;
      } else {
         updateTopBottomPadding(this.mMessageView, var2, var3);
         return true;
      }
   }

   public void animateContentIn(int var1, int var2) {
      this.mMessageView.setAlpha(0.0F);
      this.mMessageView.animate().alpha(1.0F).setDuration((long)var2).setStartDelay((long)var1).start();
      if (this.mActionView.getVisibility() == 0) {
         this.mActionView.setAlpha(0.0F);
         this.mActionView.animate().alpha(1.0F).setDuration((long)var2).setStartDelay((long)var1).start();
      }
   }

   public void animateContentOut(int var1, int var2) {
      this.mMessageView.setAlpha(1.0F);
      this.mMessageView.animate().alpha(0.0F).setDuration((long)var2).setStartDelay((long)var1).start();
      if (this.mActionView.getVisibility() == 0) {
         this.mActionView.setAlpha(1.0F);
         this.mActionView.animate().alpha(0.0F).setDuration((long)var2).setStartDelay((long)var1).start();
      }
   }

   public Button getActionView() {
      return this.mActionView;
   }

   public TextView getMessageView() {
      return this.mMessageView;
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.mMessageView = (TextView)this.findViewById(R$id.snackbar_text);
      this.mActionView = (Button)this.findViewById(R$id.snackbar_action);
   }

   protected void onMeasure(int var1, int var2) {
      int var3;
      label44: {
         super.onMeasure(var1, var2);
         if (this.mMaxWidth > 0) {
            var3 = this.getMeasuredWidth();
            int var4 = this.mMaxWidth;
            if (var3 > var4) {
               var3 = MeasureSpec.makeMeasureSpec(var4, 1073741824);
               super.onMeasure(var3, var2);
               break label44;
            }
         }

         var3 = var1;
      }

      int var5 = this.getResources().getDimensionPixelSize(R$dimen.design_snackbar_padding_vertical_2lines);
      int var6 = this.getResources().getDimensionPixelSize(R$dimen.design_snackbar_padding_vertical);
      boolean var7;
      if (this.mMessageView.getLayout().getLineCount() > 1) {
         var7 = true;
      } else {
         var7 = false;
      }

      boolean var8 = false;
      if (var7 && this.mMaxInlineActionWidth > 0 && this.mActionView.getMeasuredWidth() > this.mMaxInlineActionWidth) {
         if (this.updateViewsWithinLayout(1, var5, var5 - var6)) {
            var7 = true;
         } else {
            var7 = var8;
         }
      } else {
         if (var7) {
            var1 = var5;
         } else {
            var1 = var6;
         }

         if (this.updateViewsWithinLayout(0, var1, var1)) {
            var7 = true;
         } else {
            var7 = var8;
         }
      }

      if (var7) {
         super.onMeasure(var3, var2);
      }
   }
}
