package com.bumptech.glide.load.engine.prefill;

import android.graphics.Bitmap.Config;
import com.bumptech.glide.util.Preconditions;

public final class PreFillType {
   static final Config DEFAULT_CONFIG;
   private final Config config;
   private final int height;
   private final int weight;
   private final int width;

   static {
      DEFAULT_CONFIG = Config.RGB_565;
   }

   PreFillType(int var1, int var2, Config var3, int var4) {
      this.config = (Config)Preconditions.checkNotNull(var3, "Config must not be null");
      this.width = var1;
      this.height = var2;
      this.weight = var4;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof PreFillType;
      boolean var3 = false;
      if (var2) {
         PreFillType var4 = (PreFillType)var1;
         var2 = var3;
         if (this.height == var4.height) {
            var2 = var3;
            if (this.width == var4.width) {
               var2 = var3;
               if (this.weight == var4.weight) {
                  var2 = var3;
                  if (this.config == var4.config) {
                     var2 = true;
                  }
               }
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   Config getConfig() {
      return this.config;
   }

   int getHeight() {
      return this.height;
   }

   int getWeight() {
      return this.weight;
   }

   int getWidth() {
      return this.width;
   }

   public int hashCode() {
      return ((this.width * 31 + this.height) * 31 + this.config.hashCode()) * 31 + this.weight;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("PreFillSize{width=");
      var1.append(this.width);
      var1.append(", height=");
      var1.append(this.height);
      var1.append(", config=");
      var1.append(this.config);
      var1.append(", weight=");
      var1.append(this.weight);
      var1.append('}');
      return var1.toString();
   }

   public static class Builder {
      private Config config;
      private final int height;
      private int weight;
      private final int width;

      public Builder(int var1) {
         this(var1, var1);
      }

      public Builder(int var1, int var2) {
         this.weight = 1;
         if (var1 > 0) {
            if (var2 > 0) {
               this.width = var1;
               this.height = var2;
            } else {
               throw new IllegalArgumentException("Height must be > 0");
            }
         } else {
            throw new IllegalArgumentException("Width must be > 0");
         }
      }

      PreFillType build() {
         return new PreFillType(this.width, this.height, this.config, this.weight);
      }

      Config getConfig() {
         return this.config;
      }

      public PreFillType.Builder setConfig(Config var1) {
         this.config = var1;
         return this;
      }

      public PreFillType.Builder setWeight(int var1) {
         if (var1 > 0) {
            this.weight = var1;
            return this;
         } else {
            throw new IllegalArgumentException("Weight must be > 0");
         }
      }
   }
}
