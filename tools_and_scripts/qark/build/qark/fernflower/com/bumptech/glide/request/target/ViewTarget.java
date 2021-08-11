package com.bumptech.glide.request.target;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import com.bumptech.glide.R.id;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public abstract class ViewTarget extends BaseTarget {
   private static final String TAG = "ViewTarget";
   private static boolean isTagUsedAtLeastOnce;
   private static int tagId;
   private OnAttachStateChangeListener attachStateListener;
   private boolean isAttachStateListenerAdded;
   private boolean isClearedByUs;
   private final ViewTarget.SizeDeterminer sizeDeterminer;
   protected final View view;

   static {
      tagId = id.glide_custom_view_target_tag;
   }

   public ViewTarget(View var1) {
      this.view = (View)Preconditions.checkNotNull(var1);
      this.sizeDeterminer = new ViewTarget.SizeDeterminer(var1);
   }

   @Deprecated
   public ViewTarget(View var1, boolean var2) {
      this(var1);
      if (var2) {
         this.waitForLayout();
      }

   }

   private Object getTag() {
      return this.view.getTag(tagId);
   }

   private void maybeAddAttachStateListener() {
      OnAttachStateChangeListener var1 = this.attachStateListener;
      if (var1 != null) {
         if (!this.isAttachStateListenerAdded) {
            this.view.addOnAttachStateChangeListener(var1);
            this.isAttachStateListenerAdded = true;
         }
      }
   }

   private void maybeRemoveAttachStateListener() {
      OnAttachStateChangeListener var1 = this.attachStateListener;
      if (var1 != null) {
         if (this.isAttachStateListenerAdded) {
            this.view.removeOnAttachStateChangeListener(var1);
            this.isAttachStateListenerAdded = false;
         }
      }
   }

   private void setTag(Object var1) {
      isTagUsedAtLeastOnce = true;
      this.view.setTag(tagId, var1);
   }

   @Deprecated
   public static void setTagId(int var0) {
      if (!isTagUsedAtLeastOnce) {
         tagId = var0;
      } else {
         throw new IllegalArgumentException("You cannot set the tag id more than once or change the tag id after the first request has been made");
      }
   }

   public final ViewTarget clearOnDetach() {
      if (this.attachStateListener != null) {
         return this;
      } else {
         this.attachStateListener = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View var1) {
               ViewTarget.this.resumeMyRequest();
            }

            public void onViewDetachedFromWindow(View var1) {
               ViewTarget.this.pauseMyRequest();
            }
         };
         this.maybeAddAttachStateListener();
         return this;
      }
   }

   public Request getRequest() {
      Object var1 = this.getTag();
      if (var1 != null) {
         if (var1 instanceof Request) {
            return (Request)var1;
         } else {
            throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
         }
      } else {
         return null;
      }
   }

   public void getSize(SizeReadyCallback var1) {
      this.sizeDeterminer.getSize(var1);
   }

   public View getView() {
      return this.view;
   }

   public void onLoadCleared(Drawable var1) {
      super.onLoadCleared(var1);
      this.sizeDeterminer.clearCallbacksAndListener();
      if (!this.isClearedByUs) {
         this.maybeRemoveAttachStateListener();
      }

   }

   public void onLoadStarted(Drawable var1) {
      super.onLoadStarted(var1);
      this.maybeAddAttachStateListener();
   }

   void pauseMyRequest() {
      Request var1 = this.getRequest();
      if (var1 != null) {
         this.isClearedByUs = true;
         var1.clear();
         this.isClearedByUs = false;
      }

   }

   public void removeCallback(SizeReadyCallback var1) {
      this.sizeDeterminer.removeCallback(var1);
   }

   void resumeMyRequest() {
      Request var1 = this.getRequest();
      if (var1 != null && var1.isCleared()) {
         var1.begin();
      }

   }

   public void setRequest(Request var1) {
      this.setTag(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Target for: ");
      var1.append(this.view);
      return var1.toString();
   }

   public final ViewTarget waitForLayout() {
      this.sizeDeterminer.waitForLayout = true;
      return this;
   }

   static final class SizeDeterminer {
      private static final int PENDING_SIZE = 0;
      static Integer maxDisplayLength;
      private final List cbs = new ArrayList();
      private ViewTarget.SizeDeterminer.SizeDeterminerLayoutListener layoutListener;
      private final View view;
      boolean waitForLayout;

      SizeDeterminer(View var1) {
         this.view = var1;
      }

      private static int getMaxDisplayLength(Context var0) {
         if (maxDisplayLength == null) {
            Display var2 = ((WindowManager)Preconditions.checkNotNull((WindowManager)var0.getSystemService("window"))).getDefaultDisplay();
            Point var1 = new Point();
            var2.getSize(var1);
            maxDisplayLength = Math.max(var1.x, var1.y);
         }

         return maxDisplayLength;
      }

      private int getTargetDimen(int var1, int var2, int var3) {
         int var4 = var2 - var3;
         if (var4 > 0) {
            return var4;
         } else if (this.waitForLayout && this.view.isLayoutRequested()) {
            return 0;
         } else {
            var1 -= var3;
            if (var1 > 0) {
               return var1;
            } else if (!this.view.isLayoutRequested() && var2 == -2) {
               if (Log.isLoggable("ViewTarget", 4)) {
                  Log.i("ViewTarget", "Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this device's screen dimensions. If you want to load the original image and are ok with the corresponding memory cost and OOMs (depending on the input size), use override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT, set layout_width and layout_height to fixed dimension, or use .override() with fixed dimensions.");
               }

               return getMaxDisplayLength(this.view.getContext());
            } else {
               return 0;
            }
         }
      }

      private int getTargetHeight() {
         int var2 = this.view.getPaddingTop();
         int var3 = this.view.getPaddingBottom();
         LayoutParams var4 = this.view.getLayoutParams();
         int var1;
         if (var4 != null) {
            var1 = var4.height;
         } else {
            var1 = 0;
         }

         return this.getTargetDimen(this.view.getHeight(), var1, var2 + var3);
      }

      private int getTargetWidth() {
         int var2 = this.view.getPaddingLeft();
         int var3 = this.view.getPaddingRight();
         LayoutParams var4 = this.view.getLayoutParams();
         int var1;
         if (var4 != null) {
            var1 = var4.width;
         } else {
            var1 = 0;
         }

         return this.getTargetDimen(this.view.getWidth(), var1, var2 + var3);
      }

      private boolean isDimensionValid(int var1) {
         return var1 > 0 || var1 == Integer.MIN_VALUE;
      }

      private boolean isViewStateAndSizeValid(int var1, int var2) {
         return this.isDimensionValid(var1) && this.isDimensionValid(var2);
      }

      private void notifyCbs(int var1, int var2) {
         Iterator var3 = (new ArrayList(this.cbs)).iterator();

         while(var3.hasNext()) {
            ((SizeReadyCallback)var3.next()).onSizeReady(var1, var2);
         }

      }

      void checkCurrentDimens() {
         if (!this.cbs.isEmpty()) {
            int var1 = this.getTargetWidth();
            int var2 = this.getTargetHeight();
            if (this.isViewStateAndSizeValid(var1, var2)) {
               this.notifyCbs(var1, var2);
               this.clearCallbacksAndListener();
            }
         }
      }

      void clearCallbacksAndListener() {
         ViewTreeObserver var1 = this.view.getViewTreeObserver();
         if (var1.isAlive()) {
            var1.removeOnPreDrawListener(this.layoutListener);
         }

         this.layoutListener = null;
         this.cbs.clear();
      }

      void getSize(SizeReadyCallback var1) {
         int var2 = this.getTargetWidth();
         int var3 = this.getTargetHeight();
         if (this.isViewStateAndSizeValid(var2, var3)) {
            var1.onSizeReady(var2, var3);
         } else {
            if (!this.cbs.contains(var1)) {
               this.cbs.add(var1);
            }

            if (this.layoutListener == null) {
               ViewTreeObserver var5 = this.view.getViewTreeObserver();
               ViewTarget.SizeDeterminer.SizeDeterminerLayoutListener var4 = new ViewTarget.SizeDeterminer.SizeDeterminerLayoutListener(this);
               this.layoutListener = var4;
               var5.addOnPreDrawListener(var4);
            }

         }
      }

      void removeCallback(SizeReadyCallback var1) {
         this.cbs.remove(var1);
      }

      private static final class SizeDeterminerLayoutListener implements OnPreDrawListener {
         private final WeakReference sizeDeterminerRef;

         SizeDeterminerLayoutListener(ViewTarget.SizeDeterminer var1) {
            this.sizeDeterminerRef = new WeakReference(var1);
         }

         public boolean onPreDraw() {
            if (Log.isLoggable("ViewTarget", 2)) {
               StringBuilder var1 = new StringBuilder();
               var1.append("OnGlobalLayoutListener called attachStateListener=");
               var1.append(this);
               Log.v("ViewTarget", var1.toString());
            }

            ViewTarget.SizeDeterminer var2 = (ViewTarget.SizeDeterminer)this.sizeDeterminerRef.get();
            if (var2 != null) {
               var2.checkCurrentDimens();
            }

            return true;
         }
      }
   }
}
