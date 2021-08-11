package android.support.v4.view;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.AccessibilityDelegate;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

public class AccessibilityDelegateCompat {
   private static final AccessibilityDelegate DEFAULT_DELEGATE;
   private static final AccessibilityDelegateCompat.AccessibilityDelegateBaseImpl IMPL;
   final AccessibilityDelegate mBridge;

   static {
      if (VERSION.SDK_INT >= 16) {
         IMPL = new AccessibilityDelegateCompat.AccessibilityDelegateApi16Impl();
      } else {
         IMPL = new AccessibilityDelegateCompat.AccessibilityDelegateBaseImpl();
      }

      DEFAULT_DELEGATE = new AccessibilityDelegate();
   }

   public AccessibilityDelegateCompat() {
      this.mBridge = IMPL.newAccessibilityDelegateBridge(this);
   }

   public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
      return DEFAULT_DELEGATE.dispatchPopulateAccessibilityEvent(var1, var2);
   }

   public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
      return IMPL.getAccessibilityNodeProvider(DEFAULT_DELEGATE, var1);
   }

   AccessibilityDelegate getBridge() {
      return this.mBridge;
   }

   public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
      DEFAULT_DELEGATE.onInitializeAccessibilityEvent(var1, var2);
   }

   public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
      DEFAULT_DELEGATE.onInitializeAccessibilityNodeInfo(var1, var2.unwrap());
   }

   public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
      DEFAULT_DELEGATE.onPopulateAccessibilityEvent(var1, var2);
   }

   public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
      return DEFAULT_DELEGATE.onRequestSendAccessibilityEvent(var1, var2, var3);
   }

   public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
      return IMPL.performAccessibilityAction(DEFAULT_DELEGATE, var1, var2, var3);
   }

   public void sendAccessibilityEvent(View var1, int var2) {
      DEFAULT_DELEGATE.sendAccessibilityEvent(var1, var2);
   }

   public void sendAccessibilityEventUnchecked(View var1, AccessibilityEvent var2) {
      DEFAULT_DELEGATE.sendAccessibilityEventUnchecked(var1, var2);
   }

   @RequiresApi(16)
   static class AccessibilityDelegateApi16Impl extends AccessibilityDelegateCompat.AccessibilityDelegateBaseImpl {
      public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(AccessibilityDelegate var1, View var2) {
         AccessibilityNodeProvider var3 = var1.getAccessibilityNodeProvider(var2);
         return var3 != null ? new AccessibilityNodeProviderCompat(var3) : null;
      }

      public AccessibilityDelegate newAccessibilityDelegateBridge(final AccessibilityDelegateCompat var1) {
         return new AccessibilityDelegate() {
            public boolean dispatchPopulateAccessibilityEvent(View var1x, AccessibilityEvent var2) {
               return var1.dispatchPopulateAccessibilityEvent(var1x, var2);
            }

            public AccessibilityNodeProvider getAccessibilityNodeProvider(View var1x) {
               AccessibilityNodeProviderCompat var2 = var1.getAccessibilityNodeProvider(var1x);
               return var2 != null ? (AccessibilityNodeProvider)var2.getProvider() : null;
            }

            public void onInitializeAccessibilityEvent(View var1x, AccessibilityEvent var2) {
               var1.onInitializeAccessibilityEvent(var1x, var2);
            }

            public void onInitializeAccessibilityNodeInfo(View var1x, AccessibilityNodeInfo var2) {
               var1.onInitializeAccessibilityNodeInfo(var1x, AccessibilityNodeInfoCompat.wrap(var2));
            }

            public void onPopulateAccessibilityEvent(View var1x, AccessibilityEvent var2) {
               var1.onPopulateAccessibilityEvent(var1x, var2);
            }

            public boolean onRequestSendAccessibilityEvent(ViewGroup var1x, View var2, AccessibilityEvent var3) {
               return var1.onRequestSendAccessibilityEvent(var1x, var2, var3);
            }

            public boolean performAccessibilityAction(View var1x, int var2, Bundle var3) {
               return var1.performAccessibilityAction(var1x, var2, var3);
            }

            public void sendAccessibilityEvent(View var1x, int var2) {
               var1.sendAccessibilityEvent(var1x, var2);
            }

            public void sendAccessibilityEventUnchecked(View var1x, AccessibilityEvent var2) {
               var1.sendAccessibilityEventUnchecked(var1x, var2);
            }
         };
      }

      public boolean performAccessibilityAction(AccessibilityDelegate var1, View var2, int var3, Bundle var4) {
         return var1.performAccessibilityAction(var2, var3, var4);
      }
   }

   static class AccessibilityDelegateBaseImpl {
      public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(AccessibilityDelegate var1, View var2) {
         return null;
      }

      public AccessibilityDelegate newAccessibilityDelegateBridge(final AccessibilityDelegateCompat var1) {
         return new AccessibilityDelegate() {
            public boolean dispatchPopulateAccessibilityEvent(View var1x, AccessibilityEvent var2) {
               return var1.dispatchPopulateAccessibilityEvent(var1x, var2);
            }

            public void onInitializeAccessibilityEvent(View var1x, AccessibilityEvent var2) {
               var1.onInitializeAccessibilityEvent(var1x, var2);
            }

            public void onInitializeAccessibilityNodeInfo(View var1x, AccessibilityNodeInfo var2) {
               var1.onInitializeAccessibilityNodeInfo(var1x, AccessibilityNodeInfoCompat.wrap(var2));
            }

            public void onPopulateAccessibilityEvent(View var1x, AccessibilityEvent var2) {
               var1.onPopulateAccessibilityEvent(var1x, var2);
            }

            public boolean onRequestSendAccessibilityEvent(ViewGroup var1x, View var2, AccessibilityEvent var3) {
               return var1.onRequestSendAccessibilityEvent(var1x, var2, var3);
            }

            public void sendAccessibilityEvent(View var1x, int var2) {
               var1.sendAccessibilityEvent(var1x, var2);
            }

            public void sendAccessibilityEventUnchecked(View var1x, AccessibilityEvent var2) {
               var1.sendAccessibilityEventUnchecked(var1x, var2);
            }
         };
      }

      public boolean performAccessibilityAction(AccessibilityDelegate var1, View var2, int var3, Bundle var4) {
         return false;
      }
   }
}
