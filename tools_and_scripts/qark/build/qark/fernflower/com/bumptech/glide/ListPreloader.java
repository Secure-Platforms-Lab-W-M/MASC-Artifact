package com.bumptech.glide;

import android.graphics.drawable.Drawable;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.util.List;
import java.util.Queue;

public class ListPreloader implements OnScrollListener {
   private boolean isIncreasing = true;
   private int lastEnd;
   private int lastFirstVisible = -1;
   private int lastStart;
   private final int maxPreload;
   private final ListPreloader.PreloadSizeProvider preloadDimensionProvider;
   private final ListPreloader.PreloadModelProvider preloadModelProvider;
   private final ListPreloader.PreloadTargetQueue preloadTargetQueue;
   private final RequestManager requestManager;
   private int totalItemCount;

   public ListPreloader(RequestManager var1, ListPreloader.PreloadModelProvider var2, ListPreloader.PreloadSizeProvider var3, int var4) {
      this.requestManager = var1;
      this.preloadModelProvider = var2;
      this.preloadDimensionProvider = var3;
      this.maxPreload = var4;
      this.preloadTargetQueue = new ListPreloader.PreloadTargetQueue(var4 + 1);
   }

   private void cancelAll() {
      for(int var1 = 0; var1 < this.preloadTargetQueue.queue.size(); ++var1) {
         this.requestManager.clear((Target)this.preloadTargetQueue.next(0, 0));
      }

   }

   private void preload(int var1, int var2) {
      int var3;
      int var4;
      if (var1 < var2) {
         var3 = Math.max(this.lastEnd, var1);
         var4 = var2;
      } else {
         var3 = var2;
         var4 = Math.min(this.lastStart, var1);
      }

      var4 = Math.min(this.totalItemCount, var4);
      var3 = Math.min(this.totalItemCount, Math.max(0, var3));
      if (var1 < var2) {
         for(var1 = var3; var1 < var4; ++var1) {
            this.preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(var1), var1, true);
         }
      } else {
         for(var1 = var4 - 1; var1 >= var3; --var1) {
            this.preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(var1), var1, false);
         }
      }

      this.lastStart = var3;
      this.lastEnd = var4;
   }

   private void preload(int var1, boolean var2) {
      if (this.isIncreasing != var2) {
         this.isIncreasing = var2;
         this.cancelAll();
      }

      int var3 = this.maxPreload;
      if (!var2) {
         var3 = -var3;
      }

      this.preload(var1, var3 + var1);
   }

   private void preloadAdapterPosition(List var1, int var2, boolean var3) {
      int var5 = var1.size();
      int var4;
      if (var3) {
         for(var4 = 0; var4 < var5; ++var4) {
            this.preloadItem(var1.get(var4), var2, var4);
         }

      } else {
         for(var4 = var5 - 1; var4 >= 0; --var4) {
            this.preloadItem(var1.get(var4), var2, var4);
         }

      }
   }

   private void preloadItem(Object var1, int var2, int var3) {
      if (var1 != null) {
         int[] var4 = this.preloadDimensionProvider.getPreloadSize(var1, var2, var3);
         if (var4 != null) {
            RequestBuilder var5 = this.preloadModelProvider.getPreloadRequestBuilder(var1);
            if (var5 != null) {
               var5.into((Target)this.preloadTargetQueue.next(var4[0], var4[1]));
            }
         }
      }
   }

   public void onScroll(AbsListView var1, int var2, int var3, int var4) {
      this.totalItemCount = var4;
      var4 = this.lastFirstVisible;
      if (var2 > var4) {
         this.preload(var2 + var3, true);
      } else if (var2 < var4) {
         this.preload(var2, false);
      }

      this.lastFirstVisible = var2;
   }

   public void onScrollStateChanged(AbsListView var1, int var2) {
   }

   public interface PreloadModelProvider {
      List getPreloadItems(int var1);

      RequestBuilder getPreloadRequestBuilder(Object var1);
   }

   public interface PreloadSizeProvider {
      int[] getPreloadSize(Object var1, int var2, int var3);
   }

   private static final class PreloadTarget implements Target {
      int photoHeight;
      int photoWidth;
      private Request request;

      PreloadTarget() {
      }

      public Request getRequest() {
         return this.request;
      }

      public void getSize(SizeReadyCallback var1) {
         var1.onSizeReady(this.photoWidth, this.photoHeight);
      }

      public void onDestroy() {
      }

      public void onLoadCleared(Drawable var1) {
      }

      public void onLoadFailed(Drawable var1) {
      }

      public void onLoadStarted(Drawable var1) {
      }

      public void onResourceReady(Object var1, Transition var2) {
      }

      public void onStart() {
      }

      public void onStop() {
      }

      public void removeCallback(SizeReadyCallback var1) {
      }

      public void setRequest(Request var1) {
         this.request = var1;
      }
   }

   private static final class PreloadTargetQueue {
      final Queue queue;

      PreloadTargetQueue(int var1) {
         this.queue = Util.createQueue(var1);

         for(int var2 = 0; var2 < var1; ++var2) {
            this.queue.offer(new ListPreloader.PreloadTarget());
         }

      }

      public ListPreloader.PreloadTarget next(int var1, int var2) {
         ListPreloader.PreloadTarget var3 = (ListPreloader.PreloadTarget)this.queue.poll();
         this.queue.offer(var3);
         var3.photoWidth = var1;
         var3.photoHeight = var2;
         return var3;
      }
   }
}
