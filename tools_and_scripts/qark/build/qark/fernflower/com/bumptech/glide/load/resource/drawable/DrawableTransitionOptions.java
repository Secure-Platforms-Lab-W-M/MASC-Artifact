package com.bumptech.glide.load.resource.drawable;

import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.TransitionFactory;

public final class DrawableTransitionOptions extends TransitionOptions {
   public static DrawableTransitionOptions with(TransitionFactory var0) {
      return (DrawableTransitionOptions)(new DrawableTransitionOptions()).transition(var0);
   }

   public static DrawableTransitionOptions withCrossFade() {
      return (new DrawableTransitionOptions()).crossFade();
   }

   public static DrawableTransitionOptions withCrossFade(int var0) {
      return (new DrawableTransitionOptions()).crossFade(var0);
   }

   public static DrawableTransitionOptions withCrossFade(DrawableCrossFadeFactory.Builder var0) {
      return (new DrawableTransitionOptions()).crossFade(var0);
   }

   public static DrawableTransitionOptions withCrossFade(DrawableCrossFadeFactory var0) {
      return (new DrawableTransitionOptions()).crossFade(var0);
   }

   public DrawableTransitionOptions crossFade() {
      return this.crossFade(new DrawableCrossFadeFactory.Builder());
   }

   public DrawableTransitionOptions crossFade(int var1) {
      return this.crossFade(new DrawableCrossFadeFactory.Builder(var1));
   }

   public DrawableTransitionOptions crossFade(DrawableCrossFadeFactory.Builder var1) {
      return this.crossFade(var1.build());
   }

   public DrawableTransitionOptions crossFade(DrawableCrossFadeFactory var1) {
      return (DrawableTransitionOptions)this.transition(var1);
   }
}
