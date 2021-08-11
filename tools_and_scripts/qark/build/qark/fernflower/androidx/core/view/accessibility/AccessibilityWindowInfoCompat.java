package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityWindowInfo;

public class AccessibilityWindowInfoCompat {
   public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
   public static final int TYPE_APPLICATION = 1;
   public static final int TYPE_INPUT_METHOD = 2;
   public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
   public static final int TYPE_SYSTEM = 3;
   private static final int UNDEFINED = -1;
   private Object mInfo;

   private AccessibilityWindowInfoCompat(Object var1) {
      this.mInfo = var1;
   }

   public static AccessibilityWindowInfoCompat obtain() {
      return VERSION.SDK_INT >= 21 ? wrapNonNullInstance(AccessibilityWindowInfo.obtain()) : null;
   }

   public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0 == null ? null : wrapNonNullInstance(AccessibilityWindowInfo.obtain((AccessibilityWindowInfo)var0.mInfo));
      } else {
         return null;
      }
   }

   private static String typeToString(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            if (var0 != 3) {
               return var0 != 4 ? "<UNKNOWN>" : "TYPE_ACCESSIBILITY_OVERLAY";
            } else {
               return "TYPE_SYSTEM";
            }
         } else {
            return "TYPE_INPUT_METHOD";
         }
      } else {
         return "TYPE_APPLICATION";
      }
   }

   static AccessibilityWindowInfoCompat wrapNonNullInstance(Object var0) {
      return var0 != null ? new AccessibilityWindowInfoCompat(var0) : null;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AccessibilityWindowInfoCompat var3 = (AccessibilityWindowInfoCompat)var1;
         Object var2 = this.mInfo;
         if (var2 == null) {
            if (var3.mInfo != null) {
               return false;
            }
         } else if (!var2.equals(var3.mInfo)) {
            return false;
         }

         return true;
      }
   }

   public AccessibilityNodeInfoCompat getAnchor() {
      return VERSION.SDK_INT >= 24 ? AccessibilityNodeInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getAnchor()) : null;
   }

   public void getBoundsInScreen(Rect var1) {
      if (VERSION.SDK_INT >= 21) {
         ((AccessibilityWindowInfo)this.mInfo).getBoundsInScreen(var1);
      }

   }

   public AccessibilityWindowInfoCompat getChild(int var1) {
      return VERSION.SDK_INT >= 21 ? wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getChild(var1)) : null;
   }

   public int getChildCount() {
      return VERSION.SDK_INT >= 21 ? ((AccessibilityWindowInfo)this.mInfo).getChildCount() : 0;
   }

   public int getId() {
      return VERSION.SDK_INT >= 21 ? ((AccessibilityWindowInfo)this.mInfo).getId() : -1;
   }

   public int getLayer() {
      return VERSION.SDK_INT >= 21 ? ((AccessibilityWindowInfo)this.mInfo).getLayer() : -1;
   }

   public AccessibilityWindowInfoCompat getParent() {
      return VERSION.SDK_INT >= 21 ? wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getParent()) : null;
   }

   public AccessibilityNodeInfoCompat getRoot() {
      return VERSION.SDK_INT >= 21 ? AccessibilityNodeInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getRoot()) : null;
   }

   public CharSequence getTitle() {
      return VERSION.SDK_INT >= 24 ? ((AccessibilityWindowInfo)this.mInfo).getTitle() : null;
   }

   public int getType() {
      return VERSION.SDK_INT >= 21 ? ((AccessibilityWindowInfo)this.mInfo).getType() : -1;
   }

   public int hashCode() {
      Object var1 = this.mInfo;
      return var1 == null ? 0 : var1.hashCode();
   }

   public boolean isAccessibilityFocused() {
      return VERSION.SDK_INT >= 21 ? ((AccessibilityWindowInfo)this.mInfo).isAccessibilityFocused() : true;
   }

   public boolean isActive() {
      return VERSION.SDK_INT >= 21 ? ((AccessibilityWindowInfo)this.mInfo).isActive() : true;
   }

   public boolean isFocused() {
      return VERSION.SDK_INT >= 21 ? ((AccessibilityWindowInfo)this.mInfo).isFocused() : true;
   }

   public void recycle() {
      if (VERSION.SDK_INT >= 21) {
         ((AccessibilityWindowInfo)this.mInfo).recycle();
      }

   }

   public String toString() {
      StringBuilder var3 = new StringBuilder();
      Rect var4 = new Rect();
      this.getBoundsInScreen(var4);
      var3.append("AccessibilityWindowInfo[");
      var3.append("id=");
      var3.append(this.getId());
      var3.append(", type=");
      var3.append(typeToString(this.getType()));
      var3.append(", layer=");
      var3.append(this.getLayer());
      var3.append(", bounds=");
      var3.append(var4);
      var3.append(", focused=");
      var3.append(this.isFocused());
      var3.append(", active=");
      var3.append(this.isActive());
      var3.append(", hasParent=");
      AccessibilityWindowInfoCompat var5 = this.getParent();
      boolean var2 = true;
      boolean var1;
      if (var5 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      var3.append(var1);
      var3.append(", hasChildren=");
      if (this.getChildCount() > 0) {
         var1 = var2;
      } else {
         var1 = false;
      }

      var3.append(var1);
      var3.append(']');
      return var3.toString();
   }
}
