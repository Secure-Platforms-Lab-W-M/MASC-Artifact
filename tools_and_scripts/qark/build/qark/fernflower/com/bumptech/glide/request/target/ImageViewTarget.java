package com.bumptech.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.transition.Transition;

public abstract class ImageViewTarget extends ViewTarget implements Transition.ViewAdapter {
   private Animatable animatable;

   public ImageViewTarget(ImageView var1) {
      super(var1);
   }

   @Deprecated
   public ImageViewTarget(ImageView var1, boolean var2) {
      super(var1, var2);
   }

   private void maybeUpdateAnimatable(Object var1) {
      if (var1 instanceof Animatable) {
         Animatable var2 = (Animatable)var1;
         this.animatable = var2;
         var2.start();
      } else {
         this.animatable = null;
      }
   }

   private void setResourceInternal(Object var1) {
      this.setResource(var1);
      this.maybeUpdateAnimatable(var1);
   }

   public Drawable getCurrentDrawable() {
      return ((ImageView)this.view).getDrawable();
   }

   public void onLoadCleared(Drawable var1) {
      super.onLoadCleared(var1);
      Animatable var2 = this.animatable;
      if (var2 != null) {
         var2.stop();
      }

      this.setResourceInternal((Object)null);
      this.setDrawable(var1);
   }

   public void onLoadFailed(Drawable var1) {
      super.onLoadFailed(var1);
      this.setResourceInternal((Object)null);
      this.setDrawable(var1);
   }

   public void onLoadStarted(Drawable var1) {
      super.onLoadStarted(var1);
      this.setResourceInternal((Object)null);
      this.setDrawable(var1);
   }

   public void onResourceReady(Object var1, Transition var2) {
      if (var2 != null && var2.transition(var1, this)) {
         this.maybeUpdateAnimatable(var1);
      } else {
         this.setResourceInternal(var1);
      }
   }

   public void onStart() {
      Animatable var1 = this.animatable;
      if (var1 != null) {
         var1.start();
      }

   }

   public void onStop() {
      Animatable var1 = this.animatable;
      if (var1 != null) {
         var1.stop();
      }

   }

   public void setDrawable(Drawable var1) {
      ((ImageView)this.view).setImageDrawable(var1);
   }

   protected abstract void setResource(Object var1);
}
