package com.bumptech.glide;

import android.content.Context;
import android.content.ContextWrapper;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GlideContext extends ContextWrapper {
   static final TransitionOptions DEFAULT_TRANSITION_OPTIONS = new GenericTransitionOptions();
   private final ArrayPool arrayPool;
   private final List defaultRequestListeners;
   private RequestOptions defaultRequestOptions;
   private final Glide.RequestOptionsFactory defaultRequestOptionsFactory;
   private final Map defaultTransitionOptions;
   private final Engine engine;
   private final ImageViewTargetFactory imageViewTargetFactory;
   private final boolean isLoggingRequestOriginsEnabled;
   private final int logLevel;
   private final Registry registry;

   public GlideContext(Context var1, ArrayPool var2, Registry var3, ImageViewTargetFactory var4, Glide.RequestOptionsFactory var5, Map var6, List var7, Engine var8, boolean var9, int var10) {
      super(var1.getApplicationContext());
      this.arrayPool = var2;
      this.registry = var3;
      this.imageViewTargetFactory = var4;
      this.defaultRequestOptionsFactory = var5;
      this.defaultRequestListeners = var7;
      this.defaultTransitionOptions = var6;
      this.engine = var8;
      this.isLoggingRequestOriginsEnabled = var9;
      this.logLevel = var10;
   }

   public ViewTarget buildImageViewTarget(ImageView var1, Class var2) {
      return this.imageViewTargetFactory.buildTarget(var1, var2);
   }

   public ArrayPool getArrayPool() {
      return this.arrayPool;
   }

   public List getDefaultRequestListeners() {
      return this.defaultRequestListeners;
   }

   public RequestOptions getDefaultRequestOptions() {
      synchronized(this){}

      RequestOptions var1;
      try {
         if (this.defaultRequestOptions == null) {
            this.defaultRequestOptions = (RequestOptions)this.defaultRequestOptionsFactory.build().lock();
         }

         var1 = this.defaultRequestOptions;
      } finally {
         ;
      }

      return var1;
   }

   public TransitionOptions getDefaultTransitionOptions(Class var1) {
      TransitionOptions var3 = (TransitionOptions)this.defaultTransitionOptions.get(var1);
      TransitionOptions var2 = var3;
      if (var3 == null) {
         Iterator var4 = this.defaultTransitionOptions.entrySet().iterator();

         while(true) {
            var2 = var3;
            if (!var4.hasNext()) {
               break;
            }

            Entry var6 = (Entry)var4.next();
            if (((Class)var6.getKey()).isAssignableFrom(var1)) {
               var3 = (TransitionOptions)var6.getValue();
            }
         }
      }

      TransitionOptions var5 = var2;
      if (var2 == null) {
         var5 = DEFAULT_TRANSITION_OPTIONS;
      }

      return var5;
   }

   public Engine getEngine() {
      return this.engine;
   }

   public int getLogLevel() {
      return this.logLevel;
   }

   public Registry getRegistry() {
      return this.registry;
   }

   public boolean isLoggingRequestOriginsEnabled() {
      return this.isLoggingRequestOriginsEnabled;
   }
}
