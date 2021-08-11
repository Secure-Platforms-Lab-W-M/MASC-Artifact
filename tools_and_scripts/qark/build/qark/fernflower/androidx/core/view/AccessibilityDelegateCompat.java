package androidx.core.view;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.style.ClickableSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.AccessibilityDelegate;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.R.id;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class AccessibilityDelegateCompat {
   private static final AccessibilityDelegate DEFAULT_DELEGATE = new AccessibilityDelegate();
   private final AccessibilityDelegate mBridge;
   private final AccessibilityDelegate mOriginalDelegate;

   public AccessibilityDelegateCompat() {
      this(DEFAULT_DELEGATE);
   }

   public AccessibilityDelegateCompat(AccessibilityDelegate var1) {
      this.mOriginalDelegate = var1;
      this.mBridge = new AccessibilityDelegateCompat.AccessibilityDelegateAdapter(this);
   }

   static List getActionList(View var0) {
      List var1 = (List)var0.getTag(id.tag_accessibility_actions);
      return var1 == null ? Collections.emptyList() : var1;
   }

   private boolean isSpanStillValid(ClickableSpan var1, View var2) {
      if (var1 != null) {
         ClickableSpan[] var4 = AccessibilityNodeInfoCompat.getClickableSpans(var2.createAccessibilityNodeInfo().getText());

         for(int var3 = 0; var4 != null && var3 < var4.length; ++var3) {
            if (var1.equals(var4[var3])) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean performClickableSpanAction(int var1, View var2) {
      SparseArray var3 = (SparseArray)var2.getTag(id.tag_accessibility_clickable_spans);
      if (var3 != null) {
         WeakReference var4 = (WeakReference)var3.get(var1);
         if (var4 != null) {
            ClickableSpan var5 = (ClickableSpan)var4.get();
            if (this.isSpanStillValid(var5, var2)) {
               var5.onClick(var2);
               return true;
            }
         }
      }

      return false;
   }

   public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
      return this.mOriginalDelegate.dispatchPopulateAccessibilityEvent(var1, var2);
   }

   public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
      if (VERSION.SDK_INT >= 16) {
         AccessibilityNodeProvider var2 = this.mOriginalDelegate.getAccessibilityNodeProvider(var1);
         if (var2 != null) {
            return new AccessibilityNodeProviderCompat(var2);
         }
      }

      return null;
   }

   AccessibilityDelegate getBridge() {
      return this.mBridge;
   }

   public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
      this.mOriginalDelegate.onInitializeAccessibilityEvent(var1, var2);
   }

   public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
      this.mOriginalDelegate.onInitializeAccessibilityNodeInfo(var1, var2.unwrap());
   }

   public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
      this.mOriginalDelegate.onPopulateAccessibilityEvent(var1, var2);
   }

   public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
      return this.mOriginalDelegate.onRequestSendAccessibilityEvent(var1, var2, var3);
   }

   public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
      boolean var6 = false;
      List var7 = getActionList(var1);
      int var4 = 0;

      boolean var5;
      while(true) {
         var5 = var6;
         if (var4 >= var7.size()) {
            break;
         }

         AccessibilityNodeInfoCompat.AccessibilityActionCompat var8 = (AccessibilityNodeInfoCompat.AccessibilityActionCompat)var7.get(var4);
         if (var8.getId() == var2) {
            var5 = var8.perform(var1, var3);
            break;
         }

         ++var4;
      }

      var6 = var5;
      if (!var5) {
         var6 = var5;
         if (VERSION.SDK_INT >= 16) {
            var6 = this.mOriginalDelegate.performAccessibilityAction(var1, var2, var3);
         }
      }

      var5 = var6;
      if (!var6) {
         var5 = var6;
         if (var2 == id.accessibility_action_clickable_span) {
            var5 = this.performClickableSpanAction(var3.getInt("ACCESSIBILITY_CLICKABLE_SPAN_ID", -1), var1);
         }
      }

      return var5;
   }

   public void sendAccessibilityEvent(View var1, int var2) {
      this.mOriginalDelegate.sendAccessibilityEvent(var1, var2);
   }

   public void sendAccessibilityEventUnchecked(View var1, AccessibilityEvent var2) {
      this.mOriginalDelegate.sendAccessibilityEventUnchecked(var1, var2);
   }

   static final class AccessibilityDelegateAdapter extends AccessibilityDelegate {
      final AccessibilityDelegateCompat mCompat;

      AccessibilityDelegateAdapter(AccessibilityDelegateCompat var1) {
         this.mCompat = var1;
      }

      public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         return this.mCompat.dispatchPopulateAccessibilityEvent(var1, var2);
      }

      public AccessibilityNodeProvider getAccessibilityNodeProvider(View var1) {
         AccessibilityNodeProviderCompat var2 = this.mCompat.getAccessibilityNodeProvider(var1);
         return var2 != null ? (AccessibilityNodeProvider)var2.getProvider() : null;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         this.mCompat.onInitializeAccessibilityEvent(var1, var2);
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfo var2) {
         AccessibilityNodeInfoCompat var4 = AccessibilityNodeInfoCompat.wrap(var2);
         var4.setScreenReaderFocusable(ViewCompat.isScreenReaderFocusable(var1));
         var4.setHeading(ViewCompat.isAccessibilityHeading(var1));
         var4.setPaneTitle(ViewCompat.getAccessibilityPaneTitle(var1));
         this.mCompat.onInitializeAccessibilityNodeInfo(var1, var4);
         var4.addSpansToExtras(var2.getText(), var1);
         List var5 = AccessibilityDelegateCompat.getActionList(var1);

         for(int var3 = 0; var3 < var5.size(); ++var3) {
            var4.addAction((AccessibilityNodeInfoCompat.AccessibilityActionCompat)var5.get(var3));
         }

      }

      public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         this.mCompat.onPopulateAccessibilityEvent(var1, var2);
      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         return this.mCompat.onRequestSendAccessibilityEvent(var1, var2, var3);
      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         return this.mCompat.performAccessibilityAction(var1, var2, var3);
      }

      public void sendAccessibilityEvent(View var1, int var2) {
         this.mCompat.sendAccessibilityEvent(var1, var2);
      }

      public void sendAccessibilityEventUnchecked(View var1, AccessibilityEvent var2) {
         this.mCompat.sendAccessibilityEventUnchecked(var1, var2);
      }
   }
}
