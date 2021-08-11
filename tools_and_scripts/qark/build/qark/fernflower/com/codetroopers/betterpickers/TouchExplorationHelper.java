package com.codetroopers.betterpickers;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class TouchExplorationHelper extends AccessibilityNodeProviderCompat {
   public static final int INVALID_ID = Integer.MIN_VALUE;
   private Object mCurrentItem = null;
   private final AccessibilityDelegateCompat mDelegate = new AccessibilityDelegateCompat() {
      public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
         return TouchExplorationHelper.this;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(var1.getClass().getName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         var2.setClassName(var1.getClass().getName());
      }
   };
   private int mFocusedItemId = Integer.MIN_VALUE;
   private final AccessibilityManager mManager;
   private View mParentView;
   private final int[] mTempGlobalRect = new int[2];
   private final Rect mTempParentRect = new Rect();
   private final Rect mTempScreenRect = new Rect();
   private final Rect mTempVisibleRect = new Rect();

   public TouchExplorationHelper(Context var1, View var2) {
      this.mManager = (AccessibilityManager)var1.getSystemService("accessibility");
      this.mParentView = var2;
   }

   private AccessibilityEvent getEventForItem(Object var1, int var2) {
      AccessibilityEvent var3 = AccessibilityEvent.obtain(var2);
      AccessibilityRecordCompat var4 = new AccessibilityRecordCompat(var3);
      var2 = this.getIdForItem(var1);
      var3.setEnabled(true);
      this.populateEventForItem(var1, var3);
      if (var3.getText().isEmpty() && TextUtils.isEmpty(var3.getContentDescription())) {
         throw new RuntimeException("You must add text or a content description in populateEventForItem()");
      } else {
         var3.setClassName(var1.getClass().getName());
         var3.setPackageName(this.mParentView.getContext().getPackageName());
         var4.setSource(this.mParentView, var2);
         return var3;
      }
   }

   private AccessibilityNodeInfoCompat getNodeForParent() {
      AccessibilityNodeInfoCompat var2 = AccessibilityNodeInfoCompat.obtain(this.mParentView);
      ViewCompat.onInitializeAccessibilityNodeInfo(this.mParentView, var2);
      LinkedList var3 = new LinkedList();
      this.getVisibleItems(var3);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         int var1 = this.getIdForItem(var4.next());
         var2.addChild(this.mParentView, var1);
      }

      return var2;
   }

   private boolean intersectVisibleToUser(Rect var1) {
      if (var1 != null) {
         if (var1.isEmpty()) {
            return false;
         } else if (this.mParentView.getWindowVisibility() != 0) {
            return false;
         } else {
            View var3;
            for(Object var2 = this; var2 instanceof View; var2 = var3.getParent()) {
               var3 = (View)var2;
               if (var3.getAlpha() <= 0.0F) {
                  return false;
               }

               if (var3.getVisibility() != 0) {
                  return false;
               }
            }

            if (!this.mParentView.getLocalVisibleRect(this.mTempVisibleRect)) {
               return false;
            } else {
               return var1.intersect(this.mTempVisibleRect);
            }
         }
      } else {
         return false;
      }
   }

   private AccessibilityNodeInfoCompat populateNodeForItemInternal(Object var1, AccessibilityNodeInfoCompat var2) {
      int var3 = this.getIdForItem(var1);
      var2.setEnabled(true);
      this.populateNodeForItem(var1, var2);
      if (TextUtils.isEmpty(var2.getText()) && TextUtils.isEmpty(var2.getContentDescription())) {
         throw new RuntimeException("You must add text or a content description in populateNodeForItem()");
      } else {
         var2.setPackageName(this.mParentView.getContext().getPackageName());
         var2.setClassName(var1.getClass().getName());
         var2.setParent(this.mParentView);
         var2.setSource(this.mParentView, var3);
         if (this.mFocusedItemId == var3) {
            var2.addAction(128);
         } else {
            var2.addAction(64);
         }

         var2.getBoundsInParent(this.mTempParentRect);
         if (!this.mTempParentRect.isEmpty()) {
            if (this.intersectVisibleToUser(this.mTempParentRect)) {
               var2.setVisibleToUser(true);
               var2.setBoundsInParent(this.mTempParentRect);
            }

            this.mParentView.getLocationOnScreen(this.mTempGlobalRect);
            int[] var5 = this.mTempGlobalRect;
            var3 = var5[0];
            int var4 = var5[1];
            this.mTempScreenRect.set(this.mTempParentRect);
            this.mTempScreenRect.offset(var3, var4);
            var2.setBoundsInScreen(this.mTempScreenRect);
            return var2;
         } else {
            throw new RuntimeException("You must set parent bounds in populateNodeForItem()");
         }
      }
   }

   private void setCurrentItem(Object var1) {
      Object var2 = this.mCurrentItem;
      if (var2 != var1) {
         if (var2 != null) {
            this.sendEventForItem(var2, 256);
         }

         this.mCurrentItem = var1;
         if (var1 != null) {
            this.sendEventForItem(var1, 128);
         }

      }
   }

   public void clearFocusedItem() {
      int var1 = this.mFocusedItemId;
      if (var1 != Integer.MIN_VALUE) {
         this.performAction(var1, 128, (Bundle)null);
      }
   }

   public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int var1) {
      if (var1 == -1) {
         return this.getNodeForParent();
      } else {
         Object var2 = this.getItemForId(var1);
         if (var2 == null) {
            return null;
         } else {
            AccessibilityNodeInfoCompat var3 = AccessibilityNodeInfoCompat.obtain();
            this.populateNodeForItemInternal(var2, var3);
            return var3;
         }
      }
   }

   public AccessibilityDelegateCompat getAccessibilityDelegate() {
      return this.mDelegate;
   }

   public Object getFocusedItem() {
      return this.getItemForId(this.mFocusedItemId);
   }

   protected abstract int getIdForItem(Object var1);

   protected abstract Object getItemAt(float var1, float var2);

   protected abstract Object getItemForId(int var1);

   protected abstract void getVisibleItems(List var1);

   public void invalidateItem(Object var1) {
      this.sendEventForItem(var1, 2048);
   }

   public void invalidateParent() {
      this.mParentView.sendAccessibilityEvent(2048);
   }

   public boolean performAction(int var1, int var2, Bundle var3) {
      if (var1 == -1) {
         return ViewCompat.performAccessibilityAction(this.mParentView, var2, var3);
      } else {
         Object var5 = this.getItemForId(var1);
         if (var5 == null) {
            return false;
         } else {
            boolean var4 = false;
            if (var2 != 64) {
               if (var2 == 128 && this.mFocusedItemId == var1) {
                  this.mFocusedItemId = Integer.MIN_VALUE;
                  this.sendEventForItem(var5, 65536);
                  var4 = true;
               }
            } else if (this.mFocusedItemId != var1) {
               this.mFocusedItemId = var1;
               this.sendEventForItem(var5, 32768);
               var4 = true;
            }

            return var4 | this.performActionForItem(var5, var2, var3);
         }
      }
   }

   protected abstract boolean performActionForItem(Object var1, int var2, Bundle var3);

   protected abstract void populateEventForItem(Object var1, AccessibilityEvent var2);

   protected abstract void populateNodeForItem(Object var1, AccessibilityNodeInfoCompat var2);

   public boolean sendEventForItem(Object var1, int var2) {
      if (!this.mManager.isEnabled()) {
         return false;
      } else {
         AccessibilityEvent var3 = this.getEventForItem(var1, var2);
         return ((ViewGroup)this.mParentView.getParent()).requestSendAccessibilityEvent(this.mParentView, var3);
      }
   }

   public void setFocusedItem(Object var1) {
      int var2 = this.getIdForItem(var1);
      if (var2 != Integer.MIN_VALUE) {
         this.performAction(var2, 64, (Bundle)null);
      }
   }
}
