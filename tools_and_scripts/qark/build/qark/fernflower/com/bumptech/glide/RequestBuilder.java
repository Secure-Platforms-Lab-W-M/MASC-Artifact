package com.bumptech.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.ErrorRequestCoordinator;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.AndroidResourceSignature;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class RequestBuilder extends BaseRequestOptions implements Cloneable, ModelTypes {
   protected static final RequestOptions DOWNLOAD_ONLY_OPTIONS;
   private final Context context;
   private RequestBuilder errorBuilder;
   private final Glide glide;
   private final GlideContext glideContext;
   private boolean isDefaultTransitionOptionsSet;
   private boolean isModelSet;
   private boolean isThumbnailBuilt;
   private Object model;
   private List requestListeners;
   private final RequestManager requestManager;
   private Float thumbSizeMultiplier;
   private RequestBuilder thumbnailBuilder;
   private final Class transcodeClass;
   private TransitionOptions transitionOptions;

   static {
      DOWNLOAD_ONLY_OPTIONS = (RequestOptions)((RequestOptions)((RequestOptions)(new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.DATA)).priority(Priority.LOW)).skipMemoryCache(true);
   }

   protected RequestBuilder(Glide var1, RequestManager var2, Class var3, Context var4) {
      this.isDefaultTransitionOptionsSet = true;
      this.glide = var1;
      this.requestManager = var2;
      this.transcodeClass = var3;
      this.context = var4;
      this.transitionOptions = var2.getDefaultTransitionOptions(var3);
      this.glideContext = var1.getGlideContext();
      this.initRequestListeners(var2.getDefaultRequestListeners());
      this.apply(var2.getDefaultRequestOptions());
   }

   protected RequestBuilder(Class var1, RequestBuilder var2) {
      this(var2.glide, var2.requestManager, var1, var2.context);
      this.model = var2.model;
      this.isModelSet = var2.isModelSet;
      this.apply(var2);
   }

   private Request buildRequest(Target var1, RequestListener var2, BaseRequestOptions var3, Executor var4) {
      return this.buildRequestRecursive(new Object(), var1, var2, (RequestCoordinator)null, this.transitionOptions, var3.getPriority(), var3.getOverrideWidth(), var3.getOverrideHeight(), var3, var4);
   }

   private Request buildRequestRecursive(Object var1, Target var2, RequestListener var3, RequestCoordinator var4, TransitionOptions var5, Priority var6, int var7, int var8, BaseRequestOptions var9, Executor var10) {
      Object var15;
      Object var17;
      if (this.errorBuilder != null) {
         var15 = new ErrorRequestCoordinator(var1, var4);
         var17 = var15;
      } else {
         Object var16 = null;
         var15 = var4;
         var17 = var16;
      }

      Request var18 = this.buildThumbnailRequestRecursive(var1, var2, var3, (RequestCoordinator)var15, var5, var6, var7, var8, var9, var10);
      if (var17 == null) {
         return var18;
      } else {
         int var13 = this.errorBuilder.getOverrideWidth();
         int var14 = this.errorBuilder.getOverrideHeight();
         int var12 = var13;
         int var11 = var14;
         if (Util.isValidDimensions(var7, var8)) {
            var12 = var13;
            var11 = var14;
            if (!this.errorBuilder.isValidOverride()) {
               var12 = var9.getOverrideWidth();
               var11 = var9.getOverrideHeight();
            }
         }

         RequestBuilder var19 = this.errorBuilder;
         ((ErrorRequestCoordinator)var17).setRequests(var18, var19.buildRequestRecursive(var1, var2, var3, (RequestCoordinator)var17, var19.transitionOptions, var19.getPriority(), var12, var11, this.errorBuilder, var10));
         return (Request)var17;
      }
   }

   private Request buildThumbnailRequestRecursive(Object var1, Target var2, RequestListener var3, RequestCoordinator var4, TransitionOptions var5, Priority var6, int var7, int var8, BaseRequestOptions var9, Executor var10) {
      RequestBuilder var14 = this.thumbnailBuilder;
      ThumbnailRequestCoordinator var16;
      if (var14 != null) {
         if (!this.isThumbnailBuilt) {
            TransitionOptions var13 = var14.transitionOptions;
            if (var14.isDefaultTransitionOptionsSet) {
               var13 = var5;
            }

            Priority var19;
            if (this.thumbnailBuilder.isPrioritySet()) {
               var19 = this.thumbnailBuilder.getPriority();
            } else {
               var19 = this.getThumbnailPriority(var6);
            }

            int var11 = this.thumbnailBuilder.getOverrideWidth();
            int var12 = this.thumbnailBuilder.getOverrideHeight();
            if (Util.isValidDimensions(var7, var8) && !this.thumbnailBuilder.isValidOverride()) {
               var11 = var9.getOverrideWidth();
               var12 = var9.getOverrideHeight();
            }

            var16 = new ThumbnailRequestCoordinator(var1, var4);
            Request var17 = this.obtainRequest(var1, var2, var3, var9, var16, var5, var6, var7, var8, var10);
            this.isThumbnailBuilt = true;
            RequestBuilder var18 = this.thumbnailBuilder;
            Request var15 = var18.buildRequestRecursive(var1, var2, var3, var16, var13, var19, var11, var12, var18, var10);
            this.isThumbnailBuilt = false;
            var16.setRequests(var17, var15);
            return var16;
         } else {
            throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
         }
      } else if (this.thumbSizeMultiplier != null) {
         var16 = new ThumbnailRequestCoordinator(var1, var4);
         var16.setRequests(this.obtainRequest(var1, var2, var3, var9, var16, var5, var6, var7, var8, var10), this.obtainRequest(var1, var2, var3, var9.clone().sizeMultiplier(this.thumbSizeMultiplier), var16, var5, this.getThumbnailPriority(var6), var7, var8, var10));
         return var16;
      } else {
         return this.obtainRequest(var1, var2, var3, var9, var4, var5, var6, var7, var8, var10);
      }
   }

   private Priority getThumbnailPriority(Priority var1) {
      int var2 = null.$SwitchMap$com$bumptech$glide$Priority[var1.ordinal()];
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 3 && var2 != 4) {
               StringBuilder var3 = new StringBuilder();
               var3.append("unknown priority: ");
               var3.append(this.getPriority());
               throw new IllegalArgumentException(var3.toString());
            } else {
               return Priority.IMMEDIATE;
            }
         } else {
            return Priority.HIGH;
         }
      } else {
         return Priority.NORMAL;
      }
   }

   private void initRequestListeners(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.addListener((RequestListener)var2.next());
      }

   }

   private Target into(Target var1, RequestListener var2, BaseRequestOptions var3, Executor var4) {
      Preconditions.checkNotNull(var1);
      if (this.isModelSet) {
         Request var5 = this.buildRequest(var1, var2, var3, var4);
         Request var6 = var1.getRequest();
         if (var5.isEquivalentTo(var6) && !this.isSkipMemoryCacheWithCompletePreviousRequest(var3, var6)) {
            if (!((Request)Preconditions.checkNotNull(var6)).isRunning()) {
               var6.begin();
            }

            return var1;
         } else {
            this.requestManager.clear(var1);
            var1.setRequest(var5);
            this.requestManager.track(var1, var5);
            return var1;
         }
      } else {
         throw new IllegalArgumentException("You must call #load() before calling #into()");
      }
   }

   private boolean isSkipMemoryCacheWithCompletePreviousRequest(BaseRequestOptions var1, Request var2) {
      return !var1.isMemoryCacheable() && var2.isComplete();
   }

   private RequestBuilder loadGeneric(Object var1) {
      this.model = var1;
      this.isModelSet = true;
      return this;
   }

   private Request obtainRequest(Object var1, Target var2, RequestListener var3, BaseRequestOptions var4, RequestCoordinator var5, TransitionOptions var6, Priority var7, int var8, int var9, Executor var10) {
      Context var11 = this.context;
      GlideContext var12 = this.glideContext;
      return SingleRequest.obtain(var11, var12, var1, this.model, this.transcodeClass, var4, var8, var9, var7, var2, var3, this.requestListeners, var5, var12.getEngine(), var6.getTransitionFactory(), var10);
   }

   public RequestBuilder addListener(RequestListener var1) {
      if (var1 != null) {
         if (this.requestListeners == null) {
            this.requestListeners = new ArrayList();
         }

         this.requestListeners.add(var1);
      }

      return this;
   }

   public RequestBuilder apply(BaseRequestOptions var1) {
      Preconditions.checkNotNull(var1);
      return (RequestBuilder)super.apply(var1);
   }

   public RequestBuilder clone() {
      RequestBuilder var1 = (RequestBuilder)super.clone();
      var1.transitionOptions = var1.transitionOptions.clone();
      return var1;
   }

   @Deprecated
   public FutureTarget downloadOnly(int var1, int var2) {
      return this.getDownloadOnlyRequest().submit(var1, var2);
   }

   @Deprecated
   public Target downloadOnly(Target var1) {
      return this.getDownloadOnlyRequest().into(var1);
   }

   public RequestBuilder error(RequestBuilder var1) {
      this.errorBuilder = var1;
      return this;
   }

   protected RequestBuilder getDownloadOnlyRequest() {
      return (new RequestBuilder(File.class, this)).apply(DOWNLOAD_ONLY_OPTIONS);
   }

   @Deprecated
   public FutureTarget into(int var1, int var2) {
      return this.submit(var1, var2);
   }

   public Target into(Target var1) {
      return this.into(var1, (RequestListener)null, Executors.mainThreadExecutor());
   }

   Target into(Target var1, RequestListener var2, Executor var3) {
      return this.into(var1, var2, this, var3);
   }

   public ViewTarget into(ImageView var1) {
      Util.assertMainThread();
      Preconditions.checkNotNull(var1);
      Object var2 = this;
      if (!this.isTransformationSet()) {
         var2 = this;
         if (this.isTransformationAllowed()) {
            var2 = this;
            if (var1.getScaleType() != null) {
               switch(null.$SwitchMap$android$widget$ImageView$ScaleType[var1.getScaleType().ordinal()]) {
               case 1:
                  var2 = this.clone().optionalCenterCrop();
                  break;
               case 2:
                  var2 = this.clone().optionalCenterInside();
                  break;
               case 3:
               case 4:
               case 5:
                  var2 = this.clone().optionalFitCenter();
                  break;
               case 6:
                  var2 = this.clone().optionalCenterInside();
                  break;
               default:
                  var2 = this;
               }
            }
         }
      }

      return (ViewTarget)this.into(this.glideContext.buildImageViewTarget(var1, this.transcodeClass), (RequestListener)null, (BaseRequestOptions)var2, Executors.mainThreadExecutor());
   }

   public RequestBuilder listener(RequestListener var1) {
      this.requestListeners = null;
      return this.addListener(var1);
   }

   public RequestBuilder load(Bitmap var1) {
      return this.loadGeneric(var1).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
   }

   public RequestBuilder load(Drawable var1) {
      return this.loadGeneric(var1).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
   }

   public RequestBuilder load(Uri var1) {
      return this.loadGeneric(var1);
   }

   public RequestBuilder load(File var1) {
      return this.loadGeneric(var1);
   }

   public RequestBuilder load(Integer var1) {
      return this.loadGeneric(var1).apply(RequestOptions.signatureOf(AndroidResourceSignature.obtain(this.context)));
   }

   public RequestBuilder load(Object var1) {
      return this.loadGeneric(var1);
   }

   public RequestBuilder load(String var1) {
      return this.loadGeneric(var1);
   }

   @Deprecated
   public RequestBuilder load(URL var1) {
      return this.loadGeneric(var1);
   }

   public RequestBuilder load(byte[] var1) {
      RequestBuilder var2 = this.loadGeneric(var1);
      RequestBuilder var3 = var2;
      if (!var2.isDiskCacheStrategySet()) {
         var3 = var2.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE));
      }

      var2 = var3;
      if (!var3.isSkipMemoryCacheSet()) {
         var2 = var3.apply(RequestOptions.skipMemoryCacheOf(true));
      }

      return var2;
   }

   public Target preload() {
      return this.preload(Integer.MIN_VALUE, Integer.MIN_VALUE);
   }

   public Target preload(int var1, int var2) {
      return this.into((Target)PreloadTarget.obtain(this.requestManager, var1, var2));
   }

   public FutureTarget submit() {
      return this.submit(Integer.MIN_VALUE, Integer.MIN_VALUE);
   }

   public FutureTarget submit(int var1, int var2) {
      RequestFutureTarget var3 = new RequestFutureTarget(var1, var2);
      return (FutureTarget)this.into(var3, var3, Executors.directExecutor());
   }

   public RequestBuilder thumbnail(float var1) {
      if (var1 >= 0.0F && var1 <= 1.0F) {
         this.thumbSizeMultiplier = var1;
         return this;
      } else {
         throw new IllegalArgumentException("sizeMultiplier must be between 0 and 1");
      }
   }

   public RequestBuilder thumbnail(RequestBuilder var1) {
      this.thumbnailBuilder = var1;
      return this;
   }

   public RequestBuilder thumbnail(RequestBuilder... var1) {
      if (var1 != null && var1.length != 0) {
         RequestBuilder var3 = null;

         for(int var2 = var1.length - 1; var2 >= 0; --var2) {
            RequestBuilder var4 = var1[var2];
            if (var4 != null) {
               if (var3 == null) {
                  var3 = var4;
               } else {
                  var3 = var4.thumbnail(var3);
               }
            }
         }

         return this.thumbnail(var3);
      } else {
         return this.thumbnail((RequestBuilder)null);
      }
   }

   public RequestBuilder transition(TransitionOptions var1) {
      this.transitionOptions = (TransitionOptions)Preconditions.checkNotNull(var1);
      this.isDefaultTransitionOptionsSet = false;
      return this;
   }
}
