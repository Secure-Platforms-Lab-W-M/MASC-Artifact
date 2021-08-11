package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.Handler.Callback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class GifFrameLoader {
   private final BitmapPool bitmapPool;
   private final List callbacks;
   private GifFrameLoader.DelayTarget current;
   private Bitmap firstFrame;
   private int firstFrameSize;
   private final GifDecoder gifDecoder;
   private final Handler handler;
   private int height;
   private boolean isCleared;
   private boolean isLoadPending;
   private boolean isRunning;
   private GifFrameLoader.DelayTarget next;
   private GifFrameLoader.OnEveryFrameListener onEveryFrameListener;
   private GifFrameLoader.DelayTarget pendingTarget;
   private RequestBuilder requestBuilder;
   final RequestManager requestManager;
   private boolean startFromFirstFrame;
   private Transformation transformation;
   private int width;

   GifFrameLoader(Glide var1, GifDecoder var2, int var3, int var4, Transformation var5, Bitmap var6) {
      this(var1.getBitmapPool(), Glide.with(var1.getContext()), var2, (Handler)null, getRequestBuilder(Glide.with(var1.getContext()), var3, var4), var5, var6);
   }

   GifFrameLoader(BitmapPool var1, RequestManager var2, GifDecoder var3, Handler var4, RequestBuilder var5, Transformation var6, Bitmap var7) {
      this.callbacks = new ArrayList();
      this.requestManager = var2;
      Handler var8 = var4;
      if (var4 == null) {
         var8 = new Handler(Looper.getMainLooper(), new GifFrameLoader.FrameLoaderCallback());
      }

      this.bitmapPool = var1;
      this.handler = var8;
      this.requestBuilder = var5;
      this.gifDecoder = var3;
      this.setFrameTransformation(var6, var7);
   }

   private static Key getFrameSignature() {
      return new ObjectKey(Math.random());
   }

   private static RequestBuilder getRequestBuilder(RequestManager var0, int var1, int var2) {
      return var0.asBitmap().apply(((RequestOptions)((RequestOptions)RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).useAnimationPool(true)).skipMemoryCache(true)).override(var1, var2));
   }

   private void loadNextFrame() {
      if (this.isRunning) {
         if (!this.isLoadPending) {
            if (this.startFromFirstFrame) {
               boolean var2;
               if (this.pendingTarget == null) {
                  var2 = true;
               } else {
                  var2 = false;
               }

               Preconditions.checkArgument(var2, "Pending target must be null when starting from the first frame");
               this.gifDecoder.resetFrameIndex();
               this.startFromFirstFrame = false;
            }

            if (this.pendingTarget != null) {
               GifFrameLoader.DelayTarget var7 = this.pendingTarget;
               this.pendingTarget = null;
               this.onFrameReady(var7);
            } else {
               this.isLoadPending = true;
               int var1 = this.gifDecoder.getNextDelay();
               long var3 = SystemClock.uptimeMillis();
               long var5 = (long)var1;
               this.gifDecoder.advance();
               this.next = new GifFrameLoader.DelayTarget(this.handler, this.gifDecoder.getCurrentFrameIndex(), var3 + var5);
               this.requestBuilder.apply(RequestOptions.signatureOf(getFrameSignature())).load((Object)this.gifDecoder).into((Target)this.next);
            }
         }
      }
   }

   private void recycleFirstFrame() {
      Bitmap var1 = this.firstFrame;
      if (var1 != null) {
         this.bitmapPool.put(var1);
         this.firstFrame = null;
      }

   }

   private void start() {
      if (!this.isRunning) {
         this.isRunning = true;
         this.isCleared = false;
         this.loadNextFrame();
      }
   }

   private void stop() {
      this.isRunning = false;
   }

   void clear() {
      this.callbacks.clear();
      this.recycleFirstFrame();
      this.stop();
      GifFrameLoader.DelayTarget var1 = this.current;
      if (var1 != null) {
         this.requestManager.clear((Target)var1);
         this.current = null;
      }

      var1 = this.next;
      if (var1 != null) {
         this.requestManager.clear((Target)var1);
         this.next = null;
      }

      var1 = this.pendingTarget;
      if (var1 != null) {
         this.requestManager.clear((Target)var1);
         this.pendingTarget = null;
      }

      this.gifDecoder.clear();
      this.isCleared = true;
   }

   ByteBuffer getBuffer() {
      return this.gifDecoder.getData().asReadOnlyBuffer();
   }

   Bitmap getCurrentFrame() {
      GifFrameLoader.DelayTarget var1 = this.current;
      return var1 != null ? var1.getResource() : this.firstFrame;
   }

   int getCurrentIndex() {
      GifFrameLoader.DelayTarget var1 = this.current;
      return var1 != null ? var1.index : -1;
   }

   Bitmap getFirstFrame() {
      return this.firstFrame;
   }

   int getFrameCount() {
      return this.gifDecoder.getFrameCount();
   }

   Transformation getFrameTransformation() {
      return this.transformation;
   }

   int getHeight() {
      return this.height;
   }

   int getLoopCount() {
      return this.gifDecoder.getTotalIterationCount();
   }

   int getSize() {
      return this.gifDecoder.getByteSize() + this.firstFrameSize;
   }

   int getWidth() {
      return this.width;
   }

   void onFrameReady(GifFrameLoader.DelayTarget var1) {
      GifFrameLoader.OnEveryFrameListener var3 = this.onEveryFrameListener;
      if (var3 != null) {
         var3.onFrameReady();
      }

      this.isLoadPending = false;
      if (this.isCleared) {
         this.handler.obtainMessage(2, var1).sendToTarget();
      } else if (!this.isRunning) {
         this.pendingTarget = var1;
      } else {
         if (var1.getResource() != null) {
            this.recycleFirstFrame();
            GifFrameLoader.DelayTarget var4 = this.current;
            this.current = var1;

            for(int var2 = this.callbacks.size() - 1; var2 >= 0; --var2) {
               ((GifFrameLoader.FrameCallback)this.callbacks.get(var2)).onFrameReady();
            }

            if (var4 != null) {
               this.handler.obtainMessage(2, var4).sendToTarget();
            }
         }

         this.loadNextFrame();
      }
   }

   void setFrameTransformation(Transformation var1, Bitmap var2) {
      this.transformation = (Transformation)Preconditions.checkNotNull(var1);
      this.firstFrame = (Bitmap)Preconditions.checkNotNull(var2);
      this.requestBuilder = this.requestBuilder.apply((new RequestOptions()).transform(var1));
      this.firstFrameSize = Util.getBitmapByteSize(var2);
      this.width = var2.getWidth();
      this.height = var2.getHeight();
   }

   void setNextStartFromFirstFrame() {
      Preconditions.checkArgument(this.isRunning ^ true, "Can't restart a running animation");
      this.startFromFirstFrame = true;
      GifFrameLoader.DelayTarget var1 = this.pendingTarget;
      if (var1 != null) {
         this.requestManager.clear((Target)var1);
         this.pendingTarget = null;
      }

   }

   void setOnEveryFrameReadyListener(GifFrameLoader.OnEveryFrameListener var1) {
      this.onEveryFrameListener = var1;
   }

   void subscribe(GifFrameLoader.FrameCallback var1) {
      if (!this.isCleared) {
         if (!this.callbacks.contains(var1)) {
            boolean var2 = this.callbacks.isEmpty();
            this.callbacks.add(var1);
            if (var2) {
               this.start();
            }

         } else {
            throw new IllegalStateException("Cannot subscribe twice in a row");
         }
      } else {
         throw new IllegalStateException("Cannot subscribe to a cleared frame loader");
      }
   }

   void unsubscribe(GifFrameLoader.FrameCallback var1) {
      this.callbacks.remove(var1);
      if (this.callbacks.isEmpty()) {
         this.stop();
      }

   }

   static class DelayTarget extends CustomTarget {
      private final Handler handler;
      final int index;
      private Bitmap resource;
      private final long targetTime;

      DelayTarget(Handler var1, int var2, long var3) {
         this.handler = var1;
         this.index = var2;
         this.targetTime = var3;
      }

      Bitmap getResource() {
         return this.resource;
      }

      public void onLoadCleared(Drawable var1) {
         this.resource = null;
      }

      public void onResourceReady(Bitmap var1, Transition var2) {
         this.resource = var1;
         Message var3 = this.handler.obtainMessage(1, this);
         this.handler.sendMessageAtTime(var3, this.targetTime);
      }
   }

   public interface FrameCallback {
      void onFrameReady();
   }

   private class FrameLoaderCallback implements Callback {
      static final int MSG_CLEAR = 2;
      static final int MSG_DELAY = 1;

      FrameLoaderCallback() {
      }

      public boolean handleMessage(Message var1) {
         GifFrameLoader.DelayTarget var2;
         if (var1.what == 1) {
            var2 = (GifFrameLoader.DelayTarget)var1.obj;
            GifFrameLoader.this.onFrameReady(var2);
            return true;
         } else {
            if (var1.what == 2) {
               var2 = (GifFrameLoader.DelayTarget)var1.obj;
               GifFrameLoader.this.requestManager.clear((Target)var2);
            }

            return false;
         }
      }
   }

   interface OnEveryFrameListener {
      void onFrameReady();
   }
}
