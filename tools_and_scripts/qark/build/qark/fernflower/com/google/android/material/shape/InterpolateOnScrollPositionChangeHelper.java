package com.google.android.material.shape;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ScrollView;

public class InterpolateOnScrollPositionChangeHelper {
   private final int[] containerLocation = new int[2];
   private ScrollView containingScrollView;
   private MaterialShapeDrawable materialShapeDrawable;
   private final OnScrollChangedListener scrollChangedListener = new OnScrollChangedListener() {
      public void onScrollChanged() {
         InterpolateOnScrollPositionChangeHelper.this.updateInterpolationForScreenPosition();
      }
   };
   private final int[] scrollLocation = new int[2];
   private View shapedView;

   public InterpolateOnScrollPositionChangeHelper(View var1, MaterialShapeDrawable var2, ScrollView var3) {
      this.shapedView = var1;
      this.materialShapeDrawable = var2;
      this.containingScrollView = var3;
   }

   public void setContainingScrollView(ScrollView var1) {
      this.containingScrollView = var1;
   }

   public void setMaterialShapeDrawable(MaterialShapeDrawable var1) {
      this.materialShapeDrawable = var1;
   }

   public void startListeningForScrollChanges(ViewTreeObserver var1) {
      var1.addOnScrollChangedListener(this.scrollChangedListener);
   }

   public void stopListeningForScrollChanges(ViewTreeObserver var1) {
      var1.removeOnScrollChangedListener(this.scrollChangedListener);
   }

   public void updateInterpolationForScreenPosition() {
      ScrollView var4 = this.containingScrollView;
      if (var4 != null) {
         if (var4.getChildCount() != 0) {
            this.containingScrollView.getLocationInWindow(this.scrollLocation);
            this.containingScrollView.getChildAt(0).getLocationInWindow(this.containerLocation);
            int var1 = this.shapedView.getTop() - this.scrollLocation[1] + this.containerLocation[1];
            int var2 = this.shapedView.getHeight();
            int var3 = this.containingScrollView.getHeight();
            if (var1 < 0) {
               this.materialShapeDrawable.setInterpolation(Math.max(0.0F, Math.min(1.0F, (float)var1 / (float)var2 + 1.0F)));
               this.shapedView.invalidate();
            } else {
               if (var1 + var2 > var3) {
                  this.materialShapeDrawable.setInterpolation(Math.max(0.0F, Math.min(1.0F, 1.0F - (float)(var1 + var2 - var3) / (float)var2)));
                  this.shapedView.invalidate();
               } else if (this.materialShapeDrawable.getInterpolation() != 1.0F) {
                  this.materialShapeDrawable.setInterpolation(1.0F);
                  this.shapedView.invalidate();
                  return;
               }

            }
         } else {
            throw new IllegalStateException("Scroll bar must contain a child to calculate interpolation.");
         }
      }
   }
}
