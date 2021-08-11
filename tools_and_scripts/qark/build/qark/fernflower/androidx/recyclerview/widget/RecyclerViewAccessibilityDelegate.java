package androidx.recyclerview.widget;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import java.util.Map;
import java.util.WeakHashMap;

public class RecyclerViewAccessibilityDelegate extends AccessibilityDelegateCompat {
   private final RecyclerViewAccessibilityDelegate.ItemDelegate mItemDelegate;
   final RecyclerView mRecyclerView;

   public RecyclerViewAccessibilityDelegate(RecyclerView var1) {
      this.mRecyclerView = var1;
      AccessibilityDelegateCompat var2 = this.getItemDelegate();
      if (var2 != null && var2 instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
         this.mItemDelegate = (RecyclerViewAccessibilityDelegate.ItemDelegate)var2;
      } else {
         this.mItemDelegate = new RecyclerViewAccessibilityDelegate.ItemDelegate(this);
      }
   }

   public AccessibilityDelegateCompat getItemDelegate() {
      return this.mItemDelegate;
   }

   public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
      super.onInitializeAccessibilityEvent(var1, var2);
      if (var1 instanceof RecyclerView && !this.shouldIgnore()) {
         RecyclerView var3 = (RecyclerView)var1;
         if (var3.getLayoutManager() != null) {
            var3.getLayoutManager().onInitializeAccessibilityEvent(var2);
         }
      }

   }

   public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
      super.onInitializeAccessibilityNodeInfo(var1, var2);
      if (!this.shouldIgnore() && this.mRecyclerView.getLayoutManager() != null) {
         this.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfo(var2);
      }

   }

   public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
      if (super.performAccessibilityAction(var1, var2, var3)) {
         return true;
      } else {
         return !this.shouldIgnore() && this.mRecyclerView.getLayoutManager() != null ? this.mRecyclerView.getLayoutManager().performAccessibilityAction(var2, var3) : false;
      }
   }

   boolean shouldIgnore() {
      return this.mRecyclerView.hasPendingAdapterUpdates();
   }

   public static class ItemDelegate extends AccessibilityDelegateCompat {
      private Map mOriginalItemDelegates = new WeakHashMap();
      final RecyclerViewAccessibilityDelegate mRecyclerViewDelegate;

      public ItemDelegate(RecyclerViewAccessibilityDelegate var1) {
         this.mRecyclerViewDelegate = var1;
      }

      public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         AccessibilityDelegateCompat var3 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
         return var3 != null ? var3.dispatchPopulateAccessibilityEvent(var1, var2) : super.dispatchPopulateAccessibilityEvent(var1, var2);
      }

      public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
         AccessibilityDelegateCompat var2 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
         return var2 != null ? var2.getAccessibilityNodeProvider(var1) : super.getAccessibilityNodeProvider(var1);
      }

      AccessibilityDelegateCompat getAndRemoveOriginalDelegateForItem(View var1) {
         return (AccessibilityDelegateCompat)this.mOriginalItemDelegates.remove(var1);
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         AccessibilityDelegateCompat var3 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
         if (var3 != null) {
            var3.onInitializeAccessibilityEvent(var1, var2);
         } else {
            super.onInitializeAccessibilityEvent(var1, var2);
         }
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         if (!this.mRecyclerViewDelegate.shouldIgnore() && this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() != null) {
            this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfoForItem(var1, var2);
            AccessibilityDelegateCompat var3 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
            if (var3 != null) {
               var3.onInitializeAccessibilityNodeInfo(var1, var2);
            } else {
               super.onInitializeAccessibilityNodeInfo(var1, var2);
            }

         } else {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
         }
      }

      public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         AccessibilityDelegateCompat var3 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
         if (var3 != null) {
            var3.onPopulateAccessibilityEvent(var1, var2);
         } else {
            super.onPopulateAccessibilityEvent(var1, var2);
         }
      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         AccessibilityDelegateCompat var4 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
         return var4 != null ? var4.onRequestSendAccessibilityEvent(var1, var2, var3) : super.onRequestSendAccessibilityEvent(var1, var2, var3);
      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         if (!this.mRecyclerViewDelegate.shouldIgnore() && this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() != null) {
            AccessibilityDelegateCompat var4 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
            if (var4 != null) {
               if (var4.performAccessibilityAction(var1, var2, var3)) {
                  return true;
               }
            } else if (super.performAccessibilityAction(var1, var2, var3)) {
               return true;
            }

            return this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().performAccessibilityActionForItem(var1, var2, var3);
         } else {
            return super.performAccessibilityAction(var1, var2, var3);
         }
      }

      void saveOriginalDelegate(View var1) {
         AccessibilityDelegateCompat var2 = ViewCompat.getAccessibilityDelegate(var1);
         if (var2 != null && var2 != this) {
            this.mOriginalItemDelegates.put(var1, var2);
         }

      }

      public void sendAccessibilityEvent(View var1, int var2) {
         AccessibilityDelegateCompat var3 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
         if (var3 != null) {
            var3.sendAccessibilityEvent(var1, var2);
         } else {
            super.sendAccessibilityEvent(var1, var2);
         }
      }

      public void sendAccessibilityEventUnchecked(View var1, AccessibilityEvent var2) {
         AccessibilityDelegateCompat var3 = (AccessibilityDelegateCompat)this.mOriginalItemDelegates.get(var1);
         if (var3 != null) {
            var3.sendAccessibilityEventUnchecked(var1, var2);
         } else {
            super.sendAccessibilityEventUnchecked(var1, var2);
         }
      }
   }
}
