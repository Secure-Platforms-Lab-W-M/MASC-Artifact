package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.transition.BitmapTransitionFactory;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.TransitionFactory;

public final class BitmapTransitionOptions extends TransitionOptions {
   public static BitmapTransitionOptions with(TransitionFactory var0) {
      return (BitmapTransitionOptions)(new BitmapTransitionOptions()).transition(var0);
   }

   public static BitmapTransitionOptions withCrossFade() {
      return (new BitmapTransitionOptions()).crossFade();
   }

   public static BitmapTransitionOptions withCrossFade(int var0) {
      return (new BitmapTransitionOptions()).crossFade(var0);
   }

   public static BitmapTransitionOptions withCrossFade(DrawableCrossFadeFactory.Builder var0) {
      return (new BitmapTransitionOptions()).crossFade(var0);
   }

   public static BitmapTransitionOptions withCrossFade(DrawableCrossFadeFactory var0) {
      return (new BitmapTransitionOptions()).crossFade(var0);
   }

   public static BitmapTransitionOptions withWrapped(TransitionFactory var0) {
      return (new BitmapTransitionOptions()).transitionUsing(var0);
   }

   public BitmapTransitionOptions crossFade() {
      return this.crossFade(new DrawableCrossFadeFactory.Builder());
   }

   public BitmapTransitionOptions crossFade(int var1) {
      return this.crossFade(new DrawableCrossFadeFactory.Builder(var1));
   }

   public BitmapTransitionOptions crossFade(DrawableCrossFadeFactory.Builder var1) {
      return this.transitionUsing(var1.build());
   }

   public BitmapTransitionOptions crossFade(DrawableCrossFadeFactory var1) {
      return this.transitionUsing(var1);
   }

   public BitmapTransitionOptions transitionUsing(TransitionFactory var1) {
      return (BitmapTransitionOptions)this.transition(new BitmapTransitionFactory(var1));
   }
}
