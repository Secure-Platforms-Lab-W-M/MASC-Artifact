package com.bumptech.glide.request;

import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;

public class RequestOptions extends BaseRequestOptions {
   private static RequestOptions centerCropOptions;
   private static RequestOptions centerInsideOptions;
   private static RequestOptions circleCropOptions;
   private static RequestOptions fitCenterOptions;
   private static RequestOptions noAnimationOptions;
   private static RequestOptions noTransformOptions;
   private static RequestOptions skipMemoryCacheFalseOptions;
   private static RequestOptions skipMemoryCacheTrueOptions;

   public static RequestOptions bitmapTransform(Transformation var0) {
      return (RequestOptions)(new RequestOptions()).transform(var0);
   }

   public static RequestOptions centerCropTransform() {
      if (centerCropOptions == null) {
         centerCropOptions = (RequestOptions)((RequestOptions)(new RequestOptions()).centerCrop()).autoClone();
      }

      return centerCropOptions;
   }

   public static RequestOptions centerInsideTransform() {
      if (centerInsideOptions == null) {
         centerInsideOptions = (RequestOptions)((RequestOptions)(new RequestOptions()).centerInside()).autoClone();
      }

      return centerInsideOptions;
   }

   public static RequestOptions circleCropTransform() {
      if (circleCropOptions == null) {
         circleCropOptions = (RequestOptions)((RequestOptions)(new RequestOptions()).circleCrop()).autoClone();
      }

      return circleCropOptions;
   }

   public static RequestOptions decodeTypeOf(Class var0) {
      return (RequestOptions)(new RequestOptions()).decode(var0);
   }

   public static RequestOptions diskCacheStrategyOf(DiskCacheStrategy var0) {
      return (RequestOptions)(new RequestOptions()).diskCacheStrategy(var0);
   }

   public static RequestOptions downsampleOf(DownsampleStrategy var0) {
      return (RequestOptions)(new RequestOptions()).downsample(var0);
   }

   public static RequestOptions encodeFormatOf(CompressFormat var0) {
      return (RequestOptions)(new RequestOptions()).encodeFormat(var0);
   }

   public static RequestOptions encodeQualityOf(int var0) {
      return (RequestOptions)(new RequestOptions()).encodeQuality(var0);
   }

   public static RequestOptions errorOf(int var0) {
      return (RequestOptions)(new RequestOptions()).error(var0);
   }

   public static RequestOptions errorOf(Drawable var0) {
      return (RequestOptions)(new RequestOptions()).error(var0);
   }

   public static RequestOptions fitCenterTransform() {
      if (fitCenterOptions == null) {
         fitCenterOptions = (RequestOptions)((RequestOptions)(new RequestOptions()).fitCenter()).autoClone();
      }

      return fitCenterOptions;
   }

   public static RequestOptions formatOf(DecodeFormat var0) {
      return (RequestOptions)(new RequestOptions()).format(var0);
   }

   public static RequestOptions frameOf(long var0) {
      return (RequestOptions)(new RequestOptions()).frame(var0);
   }

   public static RequestOptions noAnimation() {
      if (noAnimationOptions == null) {
         noAnimationOptions = (RequestOptions)((RequestOptions)(new RequestOptions()).dontAnimate()).autoClone();
      }

      return noAnimationOptions;
   }

   public static RequestOptions noTransformation() {
      if (noTransformOptions == null) {
         noTransformOptions = (RequestOptions)((RequestOptions)(new RequestOptions()).dontTransform()).autoClone();
      }

      return noTransformOptions;
   }

   public static RequestOptions option(Option var0, Object var1) {
      return (RequestOptions)(new RequestOptions()).set(var0, var1);
   }

   public static RequestOptions overrideOf(int var0) {
      return overrideOf(var0, var0);
   }

   public static RequestOptions overrideOf(int var0, int var1) {
      return (RequestOptions)(new RequestOptions()).override(var0, var1);
   }

   public static RequestOptions placeholderOf(int var0) {
      return (RequestOptions)(new RequestOptions()).placeholder(var0);
   }

   public static RequestOptions placeholderOf(Drawable var0) {
      return (RequestOptions)(new RequestOptions()).placeholder(var0);
   }

   public static RequestOptions priorityOf(Priority var0) {
      return (RequestOptions)(new RequestOptions()).priority(var0);
   }

   public static RequestOptions signatureOf(Key var0) {
      return (RequestOptions)(new RequestOptions()).signature(var0);
   }

   public static RequestOptions sizeMultiplierOf(float var0) {
      return (RequestOptions)(new RequestOptions()).sizeMultiplier(var0);
   }

   public static RequestOptions skipMemoryCacheOf(boolean var0) {
      if (var0) {
         if (skipMemoryCacheTrueOptions == null) {
            skipMemoryCacheTrueOptions = (RequestOptions)((RequestOptions)(new RequestOptions()).skipMemoryCache(true)).autoClone();
         }

         return skipMemoryCacheTrueOptions;
      } else {
         if (skipMemoryCacheFalseOptions == null) {
            skipMemoryCacheFalseOptions = (RequestOptions)((RequestOptions)(new RequestOptions()).skipMemoryCache(false)).autoClone();
         }

         return skipMemoryCacheFalseOptions;
      }
   }

   public static RequestOptions timeoutOf(int var0) {
      return (RequestOptions)(new RequestOptions()).timeout(var0);
   }
}
