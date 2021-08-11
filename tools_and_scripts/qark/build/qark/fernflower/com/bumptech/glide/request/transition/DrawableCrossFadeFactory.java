package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public class DrawableCrossFadeFactory implements TransitionFactory {
   private final int duration;
   private final boolean isCrossFadeEnabled;
   private DrawableCrossFadeTransition resourceTransition;

   protected DrawableCrossFadeFactory(int var1, boolean var2) {
      this.duration = var1;
      this.isCrossFadeEnabled = var2;
   }

   private Transition getResourceTransition() {
      if (this.resourceTransition == null) {
         this.resourceTransition = new DrawableCrossFadeTransition(this.duration, this.isCrossFadeEnabled);
      }

      return this.resourceTransition;
   }

   public Transition build(DataSource var1, boolean var2) {
      return var1 == DataSource.MEMORY_CACHE ? NoTransition.get() : this.getResourceTransition();
   }

   public static class Builder {
      private static final int DEFAULT_DURATION_MS = 300;
      private final int durationMillis;
      private boolean isCrossFadeEnabled;

      public Builder() {
         this(300);
      }

      public Builder(int var1) {
         this.durationMillis = var1;
      }

      public DrawableCrossFadeFactory build() {
         return new DrawableCrossFadeFactory(this.durationMillis, this.isCrossFadeEnabled);
      }

      public DrawableCrossFadeFactory.Builder setCrossFadeEnabled(boolean var1) {
         this.isCrossFadeEnabled = var1;
         return this;
      }
   }
}
