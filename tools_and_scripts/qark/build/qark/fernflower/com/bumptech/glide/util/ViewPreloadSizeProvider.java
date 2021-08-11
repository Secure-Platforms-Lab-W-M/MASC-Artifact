package com.bumptech.glide.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import java.util.Arrays;

public class ViewPreloadSizeProvider implements ListPreloader.PreloadSizeProvider, SizeReadyCallback {
   private int[] size;
   private ViewPreloadSizeProvider.SizeViewTarget viewTarget;

   public ViewPreloadSizeProvider() {
   }

   public ViewPreloadSizeProvider(View var1) {
      ViewPreloadSizeProvider.SizeViewTarget var2 = new ViewPreloadSizeProvider.SizeViewTarget(var1);
      this.viewTarget = var2;
      var2.getSize(this);
   }

   public int[] getPreloadSize(Object var1, int var2, int var3) {
      int[] var4 = this.size;
      return var4 == null ? null : Arrays.copyOf(var4, var4.length);
   }

   public void onSizeReady(int var1, int var2) {
      this.size = new int[]{var1, var2};
      this.viewTarget = null;
   }

   public void setView(View var1) {
      if (this.size == null) {
         if (this.viewTarget == null) {
            ViewPreloadSizeProvider.SizeViewTarget var2 = new ViewPreloadSizeProvider.SizeViewTarget(var1);
            this.viewTarget = var2;
            var2.getSize(this);
         }
      }
   }

   static final class SizeViewTarget extends CustomViewTarget {
      SizeViewTarget(View var1) {
         super(var1);
      }

      public void onLoadFailed(Drawable var1) {
      }

      protected void onResourceCleared(Drawable var1) {
      }

      public void onResourceReady(Object var1, Transition var2) {
      }
   }
}
