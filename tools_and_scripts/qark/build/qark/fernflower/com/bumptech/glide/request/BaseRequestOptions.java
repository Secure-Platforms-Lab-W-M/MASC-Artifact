package com.bumptech.glide.request;

import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.util.CachedHashCodeArrayMap;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.util.Map;

public abstract class BaseRequestOptions implements Cloneable {
   private static final int DISK_CACHE_STRATEGY = 4;
   private static final int ERROR_ID = 32;
   private static final int ERROR_PLACEHOLDER = 16;
   private static final int FALLBACK = 8192;
   private static final int FALLBACK_ID = 16384;
   private static final int IS_CACHEABLE = 256;
   private static final int ONLY_RETRIEVE_FROM_CACHE = 524288;
   private static final int OVERRIDE = 512;
   private static final int PLACEHOLDER = 64;
   private static final int PLACEHOLDER_ID = 128;
   private static final int PRIORITY = 8;
   private static final int RESOURCE_CLASS = 4096;
   private static final int SIGNATURE = 1024;
   private static final int SIZE_MULTIPLIER = 2;
   private static final int THEME = 32768;
   private static final int TRANSFORMATION = 2048;
   private static final int TRANSFORMATION_ALLOWED = 65536;
   private static final int TRANSFORMATION_REQUIRED = 131072;
   private static final int UNSET = -1;
   private static final int USE_ANIMATION_POOL = 1048576;
   private static final int USE_UNLIMITED_SOURCE_GENERATORS_POOL = 262144;
   private DiskCacheStrategy diskCacheStrategy;
   private int errorId;
   private Drawable errorPlaceholder;
   private Drawable fallbackDrawable;
   private int fallbackId;
   private int fields;
   private boolean isAutoCloneEnabled;
   private boolean isCacheable;
   private boolean isLocked;
   private boolean isScaleOnlyOrNoTransform;
   private boolean isTransformationAllowed;
   private boolean isTransformationRequired;
   private boolean onlyRetrieveFromCache;
   private Options options;
   private int overrideHeight;
   private int overrideWidth;
   private Drawable placeholderDrawable;
   private int placeholderId;
   private Priority priority;
   private Class resourceClass;
   private Key signature;
   private float sizeMultiplier = 1.0F;
   private Theme theme;
   private Map transformations;
   private boolean useAnimationPool;
   private boolean useUnlimitedSourceGeneratorsPool;

   public BaseRequestOptions() {
      this.diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
      this.priority = Priority.NORMAL;
      this.isCacheable = true;
      this.overrideHeight = -1;
      this.overrideWidth = -1;
      this.signature = EmptySignature.obtain();
      this.isTransformationAllowed = true;
      this.options = new Options();
      this.transformations = new CachedHashCodeArrayMap();
      this.resourceClass = Object.class;
      this.isScaleOnlyOrNoTransform = true;
   }

   private boolean isSet(int var1) {
      return isSet(this.fields, var1);
   }

   private static boolean isSet(int var0, int var1) {
      return (var0 & var1) != 0;
   }

   private BaseRequestOptions optionalScaleOnlyTransform(DownsampleStrategy var1, Transformation var2) {
      return this.scaleOnlyTransform(var1, var2, false);
   }

   private BaseRequestOptions scaleOnlyTransform(DownsampleStrategy var1, Transformation var2) {
      return this.scaleOnlyTransform(var1, var2, true);
   }

   private BaseRequestOptions scaleOnlyTransform(DownsampleStrategy var1, Transformation var2, boolean var3) {
      BaseRequestOptions var4;
      if (var3) {
         var4 = this.transform(var1, var2);
      } else {
         var4 = this.optionalTransform(var1, var2);
      }

      var4.isScaleOnlyOrNoTransform = true;
      return var4;
   }

   private BaseRequestOptions self() {
      return this;
   }

   private BaseRequestOptions selfOrThrowIfLocked() {
      if (!this.isLocked) {
         return this.self();
      } else {
         throw new IllegalStateException("You cannot modify locked T, consider clone()");
      }
   }

   public BaseRequestOptions apply(BaseRequestOptions var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().apply(var1);
      } else {
         if (isSet(var1.fields, 2)) {
            this.sizeMultiplier = var1.sizeMultiplier;
         }

         if (isSet(var1.fields, 262144)) {
            this.useUnlimitedSourceGeneratorsPool = var1.useUnlimitedSourceGeneratorsPool;
         }

         if (isSet(var1.fields, 1048576)) {
            this.useAnimationPool = var1.useAnimationPool;
         }

         if (isSet(var1.fields, 4)) {
            this.diskCacheStrategy = var1.diskCacheStrategy;
         }

         if (isSet(var1.fields, 8)) {
            this.priority = var1.priority;
         }

         if (isSet(var1.fields, 16)) {
            this.errorPlaceholder = var1.errorPlaceholder;
            this.errorId = 0;
            this.fields &= -33;
         }

         if (isSet(var1.fields, 32)) {
            this.errorId = var1.errorId;
            this.errorPlaceholder = null;
            this.fields &= -17;
         }

         if (isSet(var1.fields, 64)) {
            this.placeholderDrawable = var1.placeholderDrawable;
            this.placeholderId = 0;
            this.fields &= -129;
         }

         if (isSet(var1.fields, 128)) {
            this.placeholderId = var1.placeholderId;
            this.placeholderDrawable = null;
            this.fields &= -65;
         }

         if (isSet(var1.fields, 256)) {
            this.isCacheable = var1.isCacheable;
         }

         if (isSet(var1.fields, 512)) {
            this.overrideWidth = var1.overrideWidth;
            this.overrideHeight = var1.overrideHeight;
         }

         if (isSet(var1.fields, 1024)) {
            this.signature = var1.signature;
         }

         if (isSet(var1.fields, 4096)) {
            this.resourceClass = var1.resourceClass;
         }

         if (isSet(var1.fields, 8192)) {
            this.fallbackDrawable = var1.fallbackDrawable;
            this.fallbackId = 0;
            this.fields &= -16385;
         }

         if (isSet(var1.fields, 16384)) {
            this.fallbackId = var1.fallbackId;
            this.fallbackDrawable = null;
            this.fields &= -8193;
         }

         if (isSet(var1.fields, 32768)) {
            this.theme = var1.theme;
         }

         if (isSet(var1.fields, 65536)) {
            this.isTransformationAllowed = var1.isTransformationAllowed;
         }

         if (isSet(var1.fields, 131072)) {
            this.isTransformationRequired = var1.isTransformationRequired;
         }

         if (isSet(var1.fields, 2048)) {
            this.transformations.putAll(var1.transformations);
            this.isScaleOnlyOrNoTransform = var1.isScaleOnlyOrNoTransform;
         }

         if (isSet(var1.fields, 524288)) {
            this.onlyRetrieveFromCache = var1.onlyRetrieveFromCache;
         }

         if (!this.isTransformationAllowed) {
            this.transformations.clear();
            int var2 = this.fields & -2049;
            this.fields = var2;
            this.isTransformationRequired = false;
            this.fields = var2 & -131073;
            this.isScaleOnlyOrNoTransform = true;
         }

         this.fields |= var1.fields;
         this.options.putAll(var1.options);
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions autoClone() {
      if (this.isLocked && !this.isAutoCloneEnabled) {
         throw new IllegalStateException("You cannot auto lock an already locked options object, try clone() first");
      } else {
         this.isAutoCloneEnabled = true;
         return this.lock();
      }
   }

   public BaseRequestOptions centerCrop() {
      return this.transform((DownsampleStrategy)DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
   }

   public BaseRequestOptions centerInside() {
      return this.scaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
   }

   public BaseRequestOptions circleCrop() {
      return this.transform((DownsampleStrategy)DownsampleStrategy.CENTER_INSIDE, new CircleCrop());
   }

   public BaseRequestOptions clone() {
      try {
         BaseRequestOptions var1 = (BaseRequestOptions)super.clone();
         Options var2 = new Options();
         var1.options = var2;
         var2.putAll(this.options);
         CachedHashCodeArrayMap var4 = new CachedHashCodeArrayMap();
         var1.transformations = var4;
         var4.putAll(this.transformations);
         var1.isLocked = false;
         var1.isAutoCloneEnabled = false;
         return var1;
      } catch (CloneNotSupportedException var3) {
         throw new RuntimeException(var3);
      }
   }

   public BaseRequestOptions decode(Class var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().decode(var1);
      } else {
         this.resourceClass = (Class)Preconditions.checkNotNull(var1);
         this.fields |= 4096;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions disallowHardwareConfig() {
      return this.set(Downsampler.ALLOW_HARDWARE_CONFIG, false);
   }

   public BaseRequestOptions diskCacheStrategy(DiskCacheStrategy var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().diskCacheStrategy(var1);
      } else {
         this.diskCacheStrategy = (DiskCacheStrategy)Preconditions.checkNotNull(var1);
         this.fields |= 4;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions dontAnimate() {
      return this.set(GifOptions.DISABLE_ANIMATION, true);
   }

   public BaseRequestOptions dontTransform() {
      if (this.isAutoCloneEnabled) {
         return this.clone().dontTransform();
      } else {
         this.transformations.clear();
         int var1 = this.fields & -2049;
         this.fields = var1;
         this.isTransformationRequired = false;
         var1 &= -131073;
         this.fields = var1;
         this.isTransformationAllowed = false;
         this.fields = var1 | 65536;
         this.isScaleOnlyOrNoTransform = true;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions downsample(DownsampleStrategy var1) {
      return this.set(DownsampleStrategy.OPTION, Preconditions.checkNotNull(var1));
   }

   public BaseRequestOptions encodeFormat(CompressFormat var1) {
      return this.set(BitmapEncoder.COMPRESSION_FORMAT, Preconditions.checkNotNull(var1));
   }

   public BaseRequestOptions encodeQuality(int var1) {
      return this.set(BitmapEncoder.COMPRESSION_QUALITY, var1);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof BaseRequestOptions) {
         BaseRequestOptions var2 = (BaseRequestOptions)var1;
         return Float.compare(var2.sizeMultiplier, this.sizeMultiplier) == 0 && this.errorId == var2.errorId && Util.bothNullOrEqual(this.errorPlaceholder, var2.errorPlaceholder) && this.placeholderId == var2.placeholderId && Util.bothNullOrEqual(this.placeholderDrawable, var2.placeholderDrawable) && this.fallbackId == var2.fallbackId && Util.bothNullOrEqual(this.fallbackDrawable, var2.fallbackDrawable) && this.isCacheable == var2.isCacheable && this.overrideHeight == var2.overrideHeight && this.overrideWidth == var2.overrideWidth && this.isTransformationRequired == var2.isTransformationRequired && this.isTransformationAllowed == var2.isTransformationAllowed && this.useUnlimitedSourceGeneratorsPool == var2.useUnlimitedSourceGeneratorsPool && this.onlyRetrieveFromCache == var2.onlyRetrieveFromCache && this.diskCacheStrategy.equals(var2.diskCacheStrategy) && this.priority == var2.priority && this.options.equals(var2.options) && this.transformations.equals(var2.transformations) && this.resourceClass.equals(var2.resourceClass) && Util.bothNullOrEqual(this.signature, var2.signature) && Util.bothNullOrEqual(this.theme, var2.theme);
      } else {
         return false;
      }
   }

   public BaseRequestOptions error(int var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().error(var1);
      } else {
         this.errorId = var1;
         var1 = this.fields | 32;
         this.fields = var1;
         this.errorPlaceholder = null;
         this.fields = var1 & -17;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions error(Drawable var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().error(var1);
      } else {
         this.errorPlaceholder = var1;
         int var2 = this.fields | 16;
         this.fields = var2;
         this.errorId = 0;
         this.fields = var2 & -33;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions fallback(int var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().fallback(var1);
      } else {
         this.fallbackId = var1;
         var1 = this.fields | 16384;
         this.fields = var1;
         this.fallbackDrawable = null;
         this.fields = var1 & -8193;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions fallback(Drawable var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().fallback(var1);
      } else {
         this.fallbackDrawable = var1;
         int var2 = this.fields | 8192;
         this.fields = var2;
         this.fallbackId = 0;
         this.fields = var2 & -16385;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions fitCenter() {
      return this.scaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
   }

   public BaseRequestOptions format(DecodeFormat var1) {
      Preconditions.checkNotNull(var1);
      return this.set(Downsampler.DECODE_FORMAT, var1).set(GifOptions.DECODE_FORMAT, var1);
   }

   public BaseRequestOptions frame(long var1) {
      return this.set(VideoDecoder.TARGET_FRAME, var1);
   }

   public final DiskCacheStrategy getDiskCacheStrategy() {
      return this.diskCacheStrategy;
   }

   public final int getErrorId() {
      return this.errorId;
   }

   public final Drawable getErrorPlaceholder() {
      return this.errorPlaceholder;
   }

   public final Drawable getFallbackDrawable() {
      return this.fallbackDrawable;
   }

   public final int getFallbackId() {
      return this.fallbackId;
   }

   public final boolean getOnlyRetrieveFromCache() {
      return this.onlyRetrieveFromCache;
   }

   public final Options getOptions() {
      return this.options;
   }

   public final int getOverrideHeight() {
      return this.overrideHeight;
   }

   public final int getOverrideWidth() {
      return this.overrideWidth;
   }

   public final Drawable getPlaceholderDrawable() {
      return this.placeholderDrawable;
   }

   public final int getPlaceholderId() {
      return this.placeholderId;
   }

   public final Priority getPriority() {
      return this.priority;
   }

   public final Class getResourceClass() {
      return this.resourceClass;
   }

   public final Key getSignature() {
      return this.signature;
   }

   public final float getSizeMultiplier() {
      return this.sizeMultiplier;
   }

   public final Theme getTheme() {
      return this.theme;
   }

   public final Map getTransformations() {
      return this.transformations;
   }

   public final boolean getUseAnimationPool() {
      return this.useAnimationPool;
   }

   public final boolean getUseUnlimitedSourceGeneratorsPool() {
      return this.useUnlimitedSourceGeneratorsPool;
   }

   public int hashCode() {
      int var1 = Util.hashCode(this.sizeMultiplier);
      var1 = Util.hashCode(this.errorId, var1);
      var1 = Util.hashCode(this.errorPlaceholder, var1);
      var1 = Util.hashCode(this.placeholderId, var1);
      var1 = Util.hashCode(this.placeholderDrawable, var1);
      var1 = Util.hashCode(this.fallbackId, var1);
      var1 = Util.hashCode(this.fallbackDrawable, var1);
      var1 = Util.hashCode(this.isCacheable, var1);
      var1 = Util.hashCode(this.overrideHeight, var1);
      var1 = Util.hashCode(this.overrideWidth, var1);
      var1 = Util.hashCode(this.isTransformationRequired, var1);
      var1 = Util.hashCode(this.isTransformationAllowed, var1);
      var1 = Util.hashCode(this.useUnlimitedSourceGeneratorsPool, var1);
      var1 = Util.hashCode(this.onlyRetrieveFromCache, var1);
      var1 = Util.hashCode(this.diskCacheStrategy, var1);
      var1 = Util.hashCode(this.priority, var1);
      var1 = Util.hashCode(this.options, var1);
      var1 = Util.hashCode(this.transformations, var1);
      var1 = Util.hashCode(this.resourceClass, var1);
      var1 = Util.hashCode(this.signature, var1);
      return Util.hashCode(this.theme, var1);
   }

   protected boolean isAutoCloneEnabled() {
      return this.isAutoCloneEnabled;
   }

   public final boolean isDiskCacheStrategySet() {
      return this.isSet(4);
   }

   public final boolean isLocked() {
      return this.isLocked;
   }

   public final boolean isMemoryCacheable() {
      return this.isCacheable;
   }

   public final boolean isPrioritySet() {
      return this.isSet(8);
   }

   boolean isScaleOnlyOrNoTransform() {
      return this.isScaleOnlyOrNoTransform;
   }

   public final boolean isSkipMemoryCacheSet() {
      return this.isSet(256);
   }

   public final boolean isTransformationAllowed() {
      return this.isTransformationAllowed;
   }

   public final boolean isTransformationRequired() {
      return this.isTransformationRequired;
   }

   public final boolean isTransformationSet() {
      return this.isSet(2048);
   }

   public final boolean isValidOverride() {
      return Util.isValidDimensions(this.overrideWidth, this.overrideHeight);
   }

   public BaseRequestOptions lock() {
      this.isLocked = true;
      return this.self();
   }

   public BaseRequestOptions onlyRetrieveFromCache(boolean var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().onlyRetrieveFromCache(var1);
      } else {
         this.onlyRetrieveFromCache = var1;
         this.fields |= 524288;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions optionalCenterCrop() {
      return this.optionalTransform((DownsampleStrategy)DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
   }

   public BaseRequestOptions optionalCenterInside() {
      return this.optionalScaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
   }

   public BaseRequestOptions optionalCircleCrop() {
      return this.optionalTransform((DownsampleStrategy)DownsampleStrategy.CENTER_OUTSIDE, new CircleCrop());
   }

   public BaseRequestOptions optionalFitCenter() {
      return this.optionalScaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
   }

   public BaseRequestOptions optionalTransform(Transformation var1) {
      return this.transform(var1, false);
   }

   final BaseRequestOptions optionalTransform(DownsampleStrategy var1, Transformation var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().optionalTransform(var1, var2);
      } else {
         this.downsample(var1);
         return this.transform(var2, false);
      }
   }

   public BaseRequestOptions optionalTransform(Class var1, Transformation var2) {
      return this.transform(var1, var2, false);
   }

   public BaseRequestOptions override(int var1) {
      return this.override(var1, var1);
   }

   public BaseRequestOptions override(int var1, int var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().override(var1, var2);
      } else {
         this.overrideWidth = var1;
         this.overrideHeight = var2;
         this.fields |= 512;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions placeholder(int var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().placeholder(var1);
      } else {
         this.placeholderId = var1;
         var1 = this.fields | 128;
         this.fields = var1;
         this.placeholderDrawable = null;
         this.fields = var1 & -65;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions placeholder(Drawable var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().placeholder(var1);
      } else {
         this.placeholderDrawable = var1;
         int var2 = this.fields | 64;
         this.fields = var2;
         this.placeholderId = 0;
         this.fields = var2 & -129;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions priority(Priority var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().priority(var1);
      } else {
         this.priority = (Priority)Preconditions.checkNotNull(var1);
         this.fields |= 8;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions set(Option var1, Object var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().set(var1, var2);
      } else {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         this.options.set(var1, var2);
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions signature(Key var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().signature(var1);
      } else {
         this.signature = (Key)Preconditions.checkNotNull(var1);
         this.fields |= 1024;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions sizeMultiplier(float var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().sizeMultiplier(var1);
      } else if (var1 >= 0.0F && var1 <= 1.0F) {
         this.sizeMultiplier = var1;
         this.fields |= 2;
         return this.selfOrThrowIfLocked();
      } else {
         throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
      }
   }

   public BaseRequestOptions skipMemoryCache(boolean var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().skipMemoryCache(true);
      } else {
         this.isCacheable = var1 ^ true;
         this.fields |= 256;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions theme(Theme var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().theme(var1);
      } else {
         this.theme = var1;
         this.fields |= 32768;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions timeout(int var1) {
      return this.set(HttpGlideUrlLoader.TIMEOUT, var1);
   }

   public BaseRequestOptions transform(Transformation var1) {
      return this.transform(var1, true);
   }

   BaseRequestOptions transform(Transformation var1, boolean var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().transform(var1, var2);
      } else {
         DrawableTransformation var3 = new DrawableTransformation(var1, var2);
         this.transform(Bitmap.class, var1, var2);
         this.transform(Drawable.class, var3, var2);
         this.transform(BitmapDrawable.class, var3.asBitmapDrawable(), var2);
         this.transform(GifDrawable.class, new GifDrawableTransformation(var1), var2);
         return this.selfOrThrowIfLocked();
      }
   }

   final BaseRequestOptions transform(DownsampleStrategy var1, Transformation var2) {
      if (this.isAutoCloneEnabled) {
         return this.clone().transform(var1, var2);
      } else {
         this.downsample(var1);
         return this.transform(var2);
      }
   }

   public BaseRequestOptions transform(Class var1, Transformation var2) {
      return this.transform(var1, var2, true);
   }

   BaseRequestOptions transform(Class var1, Transformation var2, boolean var3) {
      if (this.isAutoCloneEnabled) {
         return this.clone().transform(var1, var2, var3);
      } else {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         this.transformations.put(var1, var2);
         int var4 = this.fields | 2048;
         this.fields = var4;
         this.isTransformationAllowed = true;
         var4 |= 65536;
         this.fields = var4;
         this.isScaleOnlyOrNoTransform = false;
         if (var3) {
            this.fields = var4 | 131072;
            this.isTransformationRequired = true;
         }

         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions transform(Transformation... var1) {
      if (var1.length > 1) {
         return this.transform(new MultiTransformation(var1), true);
      } else {
         return var1.length == 1 ? this.transform(var1[0]) : this.selfOrThrowIfLocked();
      }
   }

   @Deprecated
   public BaseRequestOptions transforms(Transformation... var1) {
      return this.transform(new MultiTransformation(var1), true);
   }

   public BaseRequestOptions useAnimationPool(boolean var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().useAnimationPool(var1);
      } else {
         this.useAnimationPool = var1;
         this.fields |= 1048576;
         return this.selfOrThrowIfLocked();
      }
   }

   public BaseRequestOptions useUnlimitedSourceGeneratorsPool(boolean var1) {
      if (this.isAutoCloneEnabled) {
         return this.clone().useUnlimitedSourceGeneratorsPool(var1);
      } else {
         this.useUnlimitedSourceGeneratorsPool = var1;
         this.fields |= 262144;
         return this.selfOrThrowIfLocked();
      }
   }
}
