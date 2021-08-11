package com.bumptech.glide;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RequestManager implements ComponentCallbacks2, LifecycleListener, ModelTypes {
   private static final RequestOptions DECODE_TYPE_BITMAP = (RequestOptions)RequestOptions.decodeTypeOf(Bitmap.class).lock();
   private static final RequestOptions DECODE_TYPE_GIF = (RequestOptions)RequestOptions.decodeTypeOf(GifDrawable.class).lock();
   private static final RequestOptions DOWNLOAD_ONLY_OPTIONS;
   private final Runnable addSelfToLifecycle;
   private final ConnectivityMonitor connectivityMonitor;
   protected final Context context;
   private final CopyOnWriteArrayList defaultRequestListeners;
   protected final Glide glide;
   final Lifecycle lifecycle;
   private final Handler mainHandler;
   private boolean pauseAllRequestsOnTrimMemoryModerate;
   private RequestOptions requestOptions;
   private final RequestTracker requestTracker;
   private final TargetTracker targetTracker;
   private final RequestManagerTreeNode treeNode;

   static {
      DOWNLOAD_ONLY_OPTIONS = (RequestOptions)((RequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA).priority(Priority.LOW)).skipMemoryCache(true);
   }

   public RequestManager(Glide var1, Lifecycle var2, RequestManagerTreeNode var3, Context var4) {
      this(var1, var2, var3, new RequestTracker(), var1.getConnectivityMonitorFactory(), var4);
   }

   RequestManager(Glide var1, Lifecycle var2, RequestManagerTreeNode var3, RequestTracker var4, ConnectivityMonitorFactory var5, Context var6) {
      this.targetTracker = new TargetTracker();
      this.addSelfToLifecycle = new Runnable() {
         public void run() {
            RequestManager.this.lifecycle.addListener(RequestManager.this);
         }
      };
      this.mainHandler = new Handler(Looper.getMainLooper());
      this.glide = var1;
      this.lifecycle = var2;
      this.treeNode = var3;
      this.requestTracker = var4;
      this.context = var6;
      this.connectivityMonitor = var5.build(var6.getApplicationContext(), new RequestManager.RequestManagerConnectivityListener(var4));
      if (Util.isOnBackgroundThread()) {
         this.mainHandler.post(this.addSelfToLifecycle);
      } else {
         var2.addListener(this);
      }

      var2.addListener(this.connectivityMonitor);
      this.defaultRequestListeners = new CopyOnWriteArrayList(var1.getGlideContext().getDefaultRequestListeners());
      this.setRequestOptions(var1.getGlideContext().getDefaultRequestOptions());
      var1.registerRequestManager(this);
   }

   private void untrackOrDelegate(Target var1) {
      boolean var2 = this.untrack(var1);
      Request var3 = var1.getRequest();
      if (!var2 && !this.glide.removeFromManagers(var1) && var3 != null) {
         var1.setRequest((Request)null);
         var3.clear();
      }

   }

   private void updateRequestOptions(RequestOptions var1) {
      synchronized(this){}

      try {
         this.requestOptions = (RequestOptions)this.requestOptions.apply(var1);
      } finally {
         ;
      }

   }

   public RequestManager addDefaultRequestListener(RequestListener var1) {
      this.defaultRequestListeners.add(var1);
      return this;
   }

   public RequestManager applyDefaultRequestOptions(RequestOptions var1) {
      synchronized(this){}

      try {
         this.updateRequestOptions(var1);
      } finally {
         ;
      }

      return this;
   }

   // $FF: renamed from: as (java.lang.Class) com.bumptech.glide.RequestBuilder
   public RequestBuilder method_31(Class var1) {
      return new RequestBuilder(this.glide, this, var1, this.context);
   }

   public RequestBuilder asBitmap() {
      return this.method_31(Bitmap.class).apply(DECODE_TYPE_BITMAP);
   }

   public RequestBuilder asDrawable() {
      return this.method_31(Drawable.class);
   }

   public RequestBuilder asFile() {
      return this.method_31(File.class).apply(RequestOptions.skipMemoryCacheOf(true));
   }

   public RequestBuilder asGif() {
      return this.method_31(GifDrawable.class).apply(DECODE_TYPE_GIF);
   }

   public void clear(View var1) {
      this.clear((Target)(new RequestManager.ClearTarget(var1)));
   }

   public void clear(Target var1) {
      if (var1 != null) {
         this.untrackOrDelegate(var1);
      }
   }

   public RequestBuilder download(Object var1) {
      return this.downloadOnly().load(var1);
   }

   public RequestBuilder downloadOnly() {
      return this.method_31(File.class).apply(DOWNLOAD_ONLY_OPTIONS);
   }

   List getDefaultRequestListeners() {
      return this.defaultRequestListeners;
   }

   RequestOptions getDefaultRequestOptions() {
      synchronized(this){}

      RequestOptions var1;
      try {
         var1 = this.requestOptions;
      } finally {
         ;
      }

      return var1;
   }

   TransitionOptions getDefaultTransitionOptions(Class var1) {
      return this.glide.getGlideContext().getDefaultTransitionOptions(var1);
   }

   public boolean isPaused() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.requestTracker.isPaused();
      } finally {
         ;
      }

      return var1;
   }

   public RequestBuilder load(Bitmap var1) {
      return this.asDrawable().load(var1);
   }

   public RequestBuilder load(Drawable var1) {
      return this.asDrawable().load(var1);
   }

   public RequestBuilder load(Uri var1) {
      return this.asDrawable().load(var1);
   }

   public RequestBuilder load(File var1) {
      return this.asDrawable().load(var1);
   }

   public RequestBuilder load(Integer var1) {
      return this.asDrawable().load(var1);
   }

   public RequestBuilder load(Object var1) {
      return this.asDrawable().load(var1);
   }

   public RequestBuilder load(String var1) {
      return this.asDrawable().load(var1);
   }

   @Deprecated
   public RequestBuilder load(URL var1) {
      return this.asDrawable().load(var1);
   }

   public RequestBuilder load(byte[] var1) {
      return this.asDrawable().load(var1);
   }

   public void onConfigurationChanged(Configuration var1) {
   }

   public void onDestroy() {
      synchronized(this){}

      Throwable var10000;
      label132: {
         Iterator var1;
         boolean var10001;
         try {
            this.targetTracker.onDestroy();
            var1 = this.targetTracker.getAll().iterator();
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label132;
         }

         while(true) {
            try {
               if (var1.hasNext()) {
                  this.clear((Target)var1.next());
                  continue;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break;
            }

            try {
               this.targetTracker.clear();
               this.requestTracker.clearRequests();
               this.lifecycle.removeListener(this);
               this.lifecycle.removeListener(this.connectivityMonitor);
               this.mainHandler.removeCallbacks(this.addSelfToLifecycle);
               this.glide.unregisterRequestManager(this);
               return;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var14 = var10000;
      throw var14;
   }

   public void onLowMemory() {
   }

   public void onStart() {
      synchronized(this){}

      try {
         this.resumeRequests();
         this.targetTracker.onStart();
      } finally {
         ;
      }

   }

   public void onStop() {
      synchronized(this){}

      try {
         this.pauseRequests();
         this.targetTracker.onStop();
      } finally {
         ;
      }

   }

   public void onTrimMemory(int var1) {
      if (var1 == 60 && this.pauseAllRequestsOnTrimMemoryModerate) {
         this.pauseAllRequestsRecursive();
      }

   }

   public void pauseAllRequests() {
      synchronized(this){}

      try {
         this.requestTracker.pauseAllRequests();
      } finally {
         ;
      }

   }

   public void pauseAllRequestsRecursive() {
      synchronized(this){}

      Throwable var10000;
      label77: {
         Iterator var1;
         boolean var10001;
         try {
            this.pauseAllRequests();
            var1 = this.treeNode.getDescendants().iterator();
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label77;
         }

         while(true) {
            try {
               if (!var1.hasNext()) {
                  return;
               }

               ((RequestManager)var1.next()).pauseAllRequests();
            } catch (Throwable var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   public void pauseRequests() {
      synchronized(this){}

      try {
         this.requestTracker.pauseRequests();
      } finally {
         ;
      }

   }

   public void pauseRequestsRecursive() {
      synchronized(this){}

      Throwable var10000;
      label77: {
         Iterator var1;
         boolean var10001;
         try {
            this.pauseRequests();
            var1 = this.treeNode.getDescendants().iterator();
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label77;
         }

         while(true) {
            try {
               if (!var1.hasNext()) {
                  return;
               }

               ((RequestManager)var1.next()).pauseRequests();
            } catch (Throwable var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   public void resumeRequests() {
      synchronized(this){}

      try {
         this.requestTracker.resumeRequests();
      } finally {
         ;
      }

   }

   public void resumeRequestsRecursive() {
      synchronized(this){}

      Throwable var10000;
      label77: {
         Iterator var1;
         boolean var10001;
         try {
            Util.assertMainThread();
            this.resumeRequests();
            var1 = this.treeNode.getDescendants().iterator();
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label77;
         }

         while(true) {
            try {
               if (!var1.hasNext()) {
                  return;
               }

               ((RequestManager)var1.next()).resumeRequests();
            } catch (Throwable var6) {
               var10000 = var6;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   public RequestManager setDefaultRequestOptions(RequestOptions var1) {
      synchronized(this){}

      try {
         this.setRequestOptions(var1);
      } finally {
         ;
      }

      return this;
   }

   public void setPauseAllRequestsOnTrimMemoryModerate(boolean var1) {
      this.pauseAllRequestsOnTrimMemoryModerate = var1;
   }

   protected void setRequestOptions(RequestOptions var1) {
      synchronized(this){}

      try {
         this.requestOptions = (RequestOptions)((RequestOptions)var1.clone()).autoClone();
      } finally {
         ;
      }

   }

   public String toString() {
      synchronized(this){}

      String var4;
      try {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.toString());
         var1.append("{tracker=");
         var1.append(this.requestTracker);
         var1.append(", treeNode=");
         var1.append(this.treeNode);
         var1.append("}");
         var4 = var1.toString();
      } finally {
         ;
      }

      return var4;
   }

   void track(Target var1, Request var2) {
      synchronized(this){}

      try {
         this.targetTracker.track(var1);
         this.requestTracker.runRequest(var2);
      } finally {
         ;
      }

   }

   boolean untrack(Target var1) {
      synchronized(this){}

      Throwable var10000;
      label103: {
         boolean var10001;
         Request var2;
         try {
            var2 = var1.getRequest();
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label103;
         }

         if (var2 == null) {
            return true;
         }

         try {
            if (this.requestTracker.clearAndRemove(var2)) {
               this.targetTracker.untrack(var1);
               var1.setRequest((Request)null);
               return true;
            }
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label103;
         }

         return false;
      }

      Throwable var9 = var10000;
      throw var9;
   }

   private static class ClearTarget extends CustomViewTarget {
      ClearTarget(View var1) {
         super(var1);
      }

      public void onLoadFailed(Drawable var1) {
      }

      protected void onResourceCleared(Drawable var1) {
      }

      public void onResourceReady(Object var1, Transition var2) {
      }
   }

   private class RequestManagerConnectivityListener implements ConnectivityMonitor.ConnectivityListener {
      private final RequestTracker requestTracker;

      RequestManagerConnectivityListener(RequestTracker var2) {
         this.requestTracker = var2;
      }

      public void onConnectivityChanged(boolean param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
