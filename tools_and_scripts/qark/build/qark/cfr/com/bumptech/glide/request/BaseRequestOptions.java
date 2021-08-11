/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.request;

import android.content.res.Resources;
import android.graphics.Bitmap;
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

public abstract class BaseRequestOptions<T extends BaseRequestOptions<T>>
implements Cloneable {
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
    private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.AUTOMATIC;
    private int errorId;
    private Drawable errorPlaceholder;
    private Drawable fallbackDrawable;
    private int fallbackId;
    private int fields;
    private boolean isAutoCloneEnabled;
    private boolean isCacheable = true;
    private boolean isLocked;
    private boolean isScaleOnlyOrNoTransform = true;
    private boolean isTransformationAllowed = true;
    private boolean isTransformationRequired;
    private boolean onlyRetrieveFromCache;
    private Options options = new Options();
    private int overrideHeight = -1;
    private int overrideWidth = -1;
    private Drawable placeholderDrawable;
    private int placeholderId;
    private Priority priority = Priority.NORMAL;
    private Class<?> resourceClass = Object.class;
    private Key signature = EmptySignature.obtain();
    private float sizeMultiplier = 1.0f;
    private Resources.Theme theme;
    private Map<Class<?>, Transformation<?>> transformations = new CachedHashCodeArrayMap();
    private boolean useAnimationPool;
    private boolean useUnlimitedSourceGeneratorsPool;

    private boolean isSet(int n) {
        return BaseRequestOptions.isSet(this.fields, n);
    }

    private static boolean isSet(int n, int n2) {
        if ((n & n2) != 0) {
            return true;
        }
        return false;
    }

    private T optionalScaleOnlyTransform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation) {
        return this.scaleOnlyTransform(downsampleStrategy, transformation, false);
    }

    private T scaleOnlyTransform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation) {
        return this.scaleOnlyTransform(downsampleStrategy, transformation, true);
    }

    private T scaleOnlyTransform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation, boolean bl) {
        downsampleStrategy = bl ? this.transform(downsampleStrategy, transformation) : this.optionalTransform(downsampleStrategy, transformation);
        downsampleStrategy.isScaleOnlyOrNoTransform = true;
        return (T)downsampleStrategy;
    }

    private T self() {
        return (T)this;
    }

    private T selfOrThrowIfLocked() {
        if (!this.isLocked) {
            return this.self();
        }
        throw new IllegalStateException("You cannot modify locked T, consider clone()");
    }

    public T apply(BaseRequestOptions<?> baseRequestOptions) {
        if (this.isAutoCloneEnabled) {
            return this.clone().apply(baseRequestOptions);
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 2)) {
            this.sizeMultiplier = baseRequestOptions.sizeMultiplier;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 262144)) {
            this.useUnlimitedSourceGeneratorsPool = baseRequestOptions.useUnlimitedSourceGeneratorsPool;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 1048576)) {
            this.useAnimationPool = baseRequestOptions.useAnimationPool;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 4)) {
            this.diskCacheStrategy = baseRequestOptions.diskCacheStrategy;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 8)) {
            this.priority = baseRequestOptions.priority;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 16)) {
            this.errorPlaceholder = baseRequestOptions.errorPlaceholder;
            this.errorId = 0;
            this.fields &= -33;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 32)) {
            this.errorId = baseRequestOptions.errorId;
            this.errorPlaceholder = null;
            this.fields &= -17;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 64)) {
            this.placeholderDrawable = baseRequestOptions.placeholderDrawable;
            this.placeholderId = 0;
            this.fields &= -129;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 128)) {
            this.placeholderId = baseRequestOptions.placeholderId;
            this.placeholderDrawable = null;
            this.fields &= -65;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 256)) {
            this.isCacheable = baseRequestOptions.isCacheable;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 512)) {
            this.overrideWidth = baseRequestOptions.overrideWidth;
            this.overrideHeight = baseRequestOptions.overrideHeight;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 1024)) {
            this.signature = baseRequestOptions.signature;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 4096)) {
            this.resourceClass = baseRequestOptions.resourceClass;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 8192)) {
            this.fallbackDrawable = baseRequestOptions.fallbackDrawable;
            this.fallbackId = 0;
            this.fields &= -16385;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 16384)) {
            this.fallbackId = baseRequestOptions.fallbackId;
            this.fallbackDrawable = null;
            this.fields &= -8193;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 32768)) {
            this.theme = baseRequestOptions.theme;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 65536)) {
            this.isTransformationAllowed = baseRequestOptions.isTransformationAllowed;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 131072)) {
            this.isTransformationRequired = baseRequestOptions.isTransformationRequired;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 2048)) {
            this.transformations.putAll(baseRequestOptions.transformations);
            this.isScaleOnlyOrNoTransform = baseRequestOptions.isScaleOnlyOrNoTransform;
        }
        if (BaseRequestOptions.isSet(baseRequestOptions.fields, 524288)) {
            this.onlyRetrieveFromCache = baseRequestOptions.onlyRetrieveFromCache;
        }
        if (!this.isTransformationAllowed) {
            int n;
            this.transformations.clear();
            this.fields = n = this.fields & -2049;
            this.isTransformationRequired = false;
            this.fields = n & -131073;
            this.isScaleOnlyOrNoTransform = true;
        }
        this.fields |= baseRequestOptions.fields;
        this.options.putAll(baseRequestOptions.options);
        return this.selfOrThrowIfLocked();
    }

    public T autoClone() {
        if (this.isLocked && !this.isAutoCloneEnabled) {
            throw new IllegalStateException("You cannot auto lock an already locked options object, try clone() first");
        }
        this.isAutoCloneEnabled = true;
        return this.lock();
    }

    public T centerCrop() {
        return this.transform(DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
    }

    public T centerInside() {
        return this.scaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }

    public T circleCrop() {
        return this.transform(DownsampleStrategy.CENTER_INSIDE, new CircleCrop());
    }

    public T clone() {
        BaseRequestOptions baseRequestOptions;
        try {
            baseRequestOptions = (BaseRequestOptions)super.clone();
            Object object = new Options();
            baseRequestOptions.options = object;
            object.putAll(this.options);
            baseRequestOptions.transformations = object = new CachedHashCodeArrayMap();
            object.putAll(this.transformations);
            baseRequestOptions.isLocked = false;
            baseRequestOptions.isAutoCloneEnabled = false;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new RuntimeException(cloneNotSupportedException);
        }
        return (T)baseRequestOptions;
    }

    public T decode(Class<?> class_) {
        if (this.isAutoCloneEnabled) {
            return this.clone().decode(class_);
        }
        this.resourceClass = Preconditions.checkNotNull(class_);
        this.fields |= 4096;
        return this.selfOrThrowIfLocked();
    }

    public T disallowHardwareConfig() {
        return this.set(Downsampler.ALLOW_HARDWARE_CONFIG, false);
    }

    public T diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        if (this.isAutoCloneEnabled) {
            return this.clone().diskCacheStrategy(diskCacheStrategy);
        }
        this.diskCacheStrategy = Preconditions.checkNotNull(diskCacheStrategy);
        this.fields |= 4;
        return this.selfOrThrowIfLocked();
    }

    public T dontAnimate() {
        return this.set(GifOptions.DISABLE_ANIMATION, true);
    }

    public T dontTransform() {
        int n;
        if (this.isAutoCloneEnabled) {
            return this.clone().dontTransform();
        }
        this.transformations.clear();
        this.fields = n = this.fields & -2049;
        this.isTransformationRequired = false;
        this.fields = n &= -131073;
        this.isTransformationAllowed = false;
        this.fields = n | 65536;
        this.isScaleOnlyOrNoTransform = true;
        return this.selfOrThrowIfLocked();
    }

    public T downsample(DownsampleStrategy downsampleStrategy) {
        return this.set(DownsampleStrategy.OPTION, Preconditions.checkNotNull(downsampleStrategy));
    }

    public T encodeFormat(Bitmap.CompressFormat compressFormat) {
        return this.set((Option<Y>)BitmapEncoder.COMPRESSION_FORMAT, (Y)Preconditions.checkNotNull(compressFormat));
    }

    public T encodeQuality(int n) {
        return this.set(BitmapEncoder.COMPRESSION_QUALITY, n);
    }

    public boolean equals(Object object) {
        if (object instanceof BaseRequestOptions) {
            object = (BaseRequestOptions)object;
            if (Float.compare(object.sizeMultiplier, this.sizeMultiplier) == 0 && this.errorId == object.errorId && Util.bothNullOrEqual((Object)this.errorPlaceholder, (Object)object.errorPlaceholder) && this.placeholderId == object.placeholderId && Util.bothNullOrEqual((Object)this.placeholderDrawable, (Object)object.placeholderDrawable) && this.fallbackId == object.fallbackId && Util.bothNullOrEqual((Object)this.fallbackDrawable, (Object)object.fallbackDrawable) && this.isCacheable == object.isCacheable && this.overrideHeight == object.overrideHeight && this.overrideWidth == object.overrideWidth && this.isTransformationRequired == object.isTransformationRequired && this.isTransformationAllowed == object.isTransformationAllowed && this.useUnlimitedSourceGeneratorsPool == object.useUnlimitedSourceGeneratorsPool && this.onlyRetrieveFromCache == object.onlyRetrieveFromCache && this.diskCacheStrategy.equals(object.diskCacheStrategy) && this.priority == object.priority && this.options.equals(object.options) && this.transformations.equals(object.transformations) && this.resourceClass.equals(object.resourceClass) && Util.bothNullOrEqual(this.signature, object.signature) && Util.bothNullOrEqual((Object)this.theme, (Object)object.theme)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public T error(int n) {
        if (this.isAutoCloneEnabled) {
            return this.clone().error(n);
        }
        this.errorId = n;
        this.fields = n = this.fields | 32;
        this.errorPlaceholder = null;
        this.fields = n & -17;
        return this.selfOrThrowIfLocked();
    }

    public T error(Drawable drawable2) {
        int n;
        if (this.isAutoCloneEnabled) {
            return this.clone().error(drawable2);
        }
        this.errorPlaceholder = drawable2;
        this.fields = n = this.fields | 16;
        this.errorId = 0;
        this.fields = n & -33;
        return this.selfOrThrowIfLocked();
    }

    public T fallback(int n) {
        if (this.isAutoCloneEnabled) {
            return this.clone().fallback(n);
        }
        this.fallbackId = n;
        this.fields = n = this.fields | 16384;
        this.fallbackDrawable = null;
        this.fields = n & -8193;
        return this.selfOrThrowIfLocked();
    }

    public T fallback(Drawable drawable2) {
        int n;
        if (this.isAutoCloneEnabled) {
            return this.clone().fallback(drawable2);
        }
        this.fallbackDrawable = drawable2;
        this.fields = n = this.fields | 8192;
        this.fallbackId = 0;
        this.fields = n & -16385;
        return this.selfOrThrowIfLocked();
    }

    public T fitCenter() {
        return this.scaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }

    public T format(DecodeFormat decodeFormat) {
        Preconditions.checkNotNull(decodeFormat);
        return this.set((Option<Y>)Downsampler.DECODE_FORMAT, (Y)((Object)decodeFormat)).set((Option<Y>)GifOptions.DECODE_FORMAT, (Y)((Object)decodeFormat));
    }

    public T frame(long l) {
        return this.set(VideoDecoder.TARGET_FRAME, l);
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

    public final Class<?> getResourceClass() {
        return this.resourceClass;
    }

    public final Key getSignature() {
        return this.signature;
    }

    public final float getSizeMultiplier() {
        return this.sizeMultiplier;
    }

    public final Resources.Theme getTheme() {
        return this.theme;
    }

    public final Map<Class<?>, Transformation<?>> getTransformations() {
        return this.transformations;
    }

    public final boolean getUseAnimationPool() {
        return this.useAnimationPool;
    }

    public final boolean getUseUnlimitedSourceGeneratorsPool() {
        return this.useUnlimitedSourceGeneratorsPool;
    }

    public int hashCode() {
        int n = Util.hashCode(this.sizeMultiplier);
        n = Util.hashCode(this.errorId, n);
        n = Util.hashCode((Object)this.errorPlaceholder, n);
        n = Util.hashCode(this.placeholderId, n);
        n = Util.hashCode((Object)this.placeholderDrawable, n);
        n = Util.hashCode(this.fallbackId, n);
        n = Util.hashCode((Object)this.fallbackDrawable, n);
        n = Util.hashCode(this.isCacheable, n);
        n = Util.hashCode(this.overrideHeight, n);
        n = Util.hashCode(this.overrideWidth, n);
        n = Util.hashCode(this.isTransformationRequired, n);
        n = Util.hashCode(this.isTransformationAllowed, n);
        n = Util.hashCode(this.useUnlimitedSourceGeneratorsPool, n);
        n = Util.hashCode(this.onlyRetrieveFromCache, n);
        n = Util.hashCode(this.diskCacheStrategy, n);
        n = Util.hashCode((Object)this.priority, n);
        n = Util.hashCode(this.options, n);
        n = Util.hashCode(this.transformations, n);
        n = Util.hashCode(this.resourceClass, n);
        n = Util.hashCode(this.signature, n);
        return Util.hashCode((Object)this.theme, n);
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

    public T lock() {
        this.isLocked = true;
        return this.self();
    }

    public T onlyRetrieveFromCache(boolean bl) {
        if (this.isAutoCloneEnabled) {
            return this.clone().onlyRetrieveFromCache(bl);
        }
        this.onlyRetrieveFromCache = bl;
        this.fields |= 524288;
        return this.selfOrThrowIfLocked();
    }

    public T optionalCenterCrop() {
        return this.optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
    }

    public T optionalCenterInside() {
        return this.optionalScaleOnlyTransform(DownsampleStrategy.CENTER_INSIDE, new CenterInside());
    }

    public T optionalCircleCrop() {
        return this.optionalTransform(DownsampleStrategy.CENTER_OUTSIDE, new CircleCrop());
    }

    public T optionalFitCenter() {
        return this.optionalScaleOnlyTransform(DownsampleStrategy.FIT_CENTER, new FitCenter());
    }

    public T optionalTransform(Transformation<Bitmap> transformation) {
        return this.transform(transformation, false);
    }

    final T optionalTransform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return this.clone().optionalTransform(downsampleStrategy, transformation);
        }
        this.downsample(downsampleStrategy);
        return this.transform(transformation, false);
    }

    public <Y> T optionalTransform(Class<Y> class_, Transformation<Y> transformation) {
        return this.transform(class_, transformation, false);
    }

    public T override(int n) {
        return this.override(n, n);
    }

    public T override(int n, int n2) {
        if (this.isAutoCloneEnabled) {
            return this.clone().override(n, n2);
        }
        this.overrideWidth = n;
        this.overrideHeight = n2;
        this.fields |= 512;
        return this.selfOrThrowIfLocked();
    }

    public T placeholder(int n) {
        if (this.isAutoCloneEnabled) {
            return this.clone().placeholder(n);
        }
        this.placeholderId = n;
        this.fields = n = this.fields | 128;
        this.placeholderDrawable = null;
        this.fields = n & -65;
        return this.selfOrThrowIfLocked();
    }

    public T placeholder(Drawable drawable2) {
        int n;
        if (this.isAutoCloneEnabled) {
            return this.clone().placeholder(drawable2);
        }
        this.placeholderDrawable = drawable2;
        this.fields = n = this.fields | 64;
        this.placeholderId = 0;
        this.fields = n & -129;
        return this.selfOrThrowIfLocked();
    }

    public T priority(Priority priority) {
        if (this.isAutoCloneEnabled) {
            return this.clone().priority(priority);
        }
        this.priority = Preconditions.checkNotNull(priority);
        this.fields |= 8;
        return this.selfOrThrowIfLocked();
    }

    public <Y> T set(Option<Y> option, Y y) {
        if (this.isAutoCloneEnabled) {
            return this.clone().set(option, y);
        }
        Preconditions.checkNotNull(option);
        Preconditions.checkNotNull(y);
        this.options.set(option, y);
        return this.selfOrThrowIfLocked();
    }

    public T signature(Key key) {
        if (this.isAutoCloneEnabled) {
            return this.clone().signature(key);
        }
        this.signature = Preconditions.checkNotNull(key);
        this.fields |= 1024;
        return this.selfOrThrowIfLocked();
    }

    public T sizeMultiplier(float f) {
        if (this.isAutoCloneEnabled) {
            return this.clone().sizeMultiplier(f);
        }
        if (f >= 0.0f && f <= 1.0f) {
            this.sizeMultiplier = f;
            this.fields |= 2;
            return this.selfOrThrowIfLocked();
        }
        throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
    }

    public T skipMemoryCache(boolean bl) {
        if (this.isAutoCloneEnabled) {
            return this.clone().skipMemoryCache(true);
        }
        this.isCacheable = bl ^ true;
        this.fields |= 256;
        return this.selfOrThrowIfLocked();
    }

    public T theme(Resources.Theme theme) {
        if (this.isAutoCloneEnabled) {
            return this.clone().theme(theme);
        }
        this.theme = theme;
        this.fields |= 32768;
        return this.selfOrThrowIfLocked();
    }

    public T timeout(int n) {
        return this.set(HttpGlideUrlLoader.TIMEOUT, n);
    }

    public T transform(Transformation<Bitmap> transformation) {
        return this.transform(transformation, true);
    }

    T transform(Transformation<Bitmap> transformation, boolean bl) {
        if (this.isAutoCloneEnabled) {
            return this.clone().transform(transformation, bl);
        }
        DrawableTransformation drawableTransformation = new DrawableTransformation(transformation, bl);
        this.transform(Bitmap.class, transformation, bl);
        this.transform(Drawable.class, drawableTransformation, bl);
        this.transform(BitmapDrawable.class, drawableTransformation.asBitmapDrawable(), bl);
        this.transform(GifDrawable.class, new GifDrawableTransformation(transformation), bl);
        return this.selfOrThrowIfLocked();
    }

    final T transform(DownsampleStrategy downsampleStrategy, Transformation<Bitmap> transformation) {
        if (this.isAutoCloneEnabled) {
            return this.clone().transform(downsampleStrategy, transformation);
        }
        this.downsample(downsampleStrategy);
        return this.transform(transformation);
    }

    public <Y> T transform(Class<Y> class_, Transformation<Y> transformation) {
        return this.transform(class_, transformation, true);
    }

    <Y> T transform(Class<Y> class_, Transformation<Y> transformation, boolean bl) {
        int n;
        if (this.isAutoCloneEnabled) {
            return this.clone().transform(class_, transformation, bl);
        }
        Preconditions.checkNotNull(class_);
        Preconditions.checkNotNull(transformation);
        this.transformations.put(class_, transformation);
        this.fields = n = this.fields | 2048;
        this.isTransformationAllowed = true;
        this.fields = n |= 65536;
        this.isScaleOnlyOrNoTransform = false;
        if (bl) {
            this.fields = n | 131072;
            this.isTransformationRequired = true;
        }
        return this.selfOrThrowIfLocked();
    }

    public /* varargs */ T transform(Transformation<Bitmap> ... arrtransformation) {
        if (arrtransformation.length > 1) {
            return this.transform(new MultiTransformation<Bitmap>(arrtransformation), true);
        }
        if (arrtransformation.length == 1) {
            return this.transform(arrtransformation[0]);
        }
        return this.selfOrThrowIfLocked();
    }

    @Deprecated
    public /* varargs */ T transforms(Transformation<Bitmap> ... arrtransformation) {
        return this.transform(new MultiTransformation<Bitmap>(arrtransformation), true);
    }

    public T useAnimationPool(boolean bl) {
        if (this.isAutoCloneEnabled) {
            return this.clone().useAnimationPool(bl);
        }
        this.useAnimationPool = bl;
        this.fields |= 1048576;
        return this.selfOrThrowIfLocked();
    }

    public T useUnlimitedSourceGeneratorsPool(boolean bl) {
        if (this.isAutoCloneEnabled) {
            return this.clone().useUnlimitedSourceGeneratorsPool(bl);
        }
        this.useUnlimitedSourceGeneratorsPool = bl;
        this.fields |= 262144;
        return this.selfOrThrowIfLocked();
    }
}

