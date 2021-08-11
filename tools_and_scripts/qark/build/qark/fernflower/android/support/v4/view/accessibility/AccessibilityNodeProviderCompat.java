package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
   public static final int HOST_VIEW_ID = -1;
   private static final AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl IMPL;
   private final Object mProvider;

   static {
      if (VERSION.SDK_INT >= 19) {
         IMPL = new AccessibilityNodeProviderCompat.AccessibilityNodeProviderKitKatImpl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new AccessibilityNodeProviderCompat.AccessibilityNodeProviderJellyBeanImpl();
      } else {
         IMPL = new AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl();
      }
   }

   public AccessibilityNodeProviderCompat() {
      this.mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
   }

   public AccessibilityNodeProviderCompat(Object var1) {
      this.mProvider = var1;
   }

   @Nullable
   public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int var1) {
      return null;
   }

   @Nullable
   public List findAccessibilityNodeInfosByText(String var1, int var2) {
      return null;
   }

   @Nullable
   public AccessibilityNodeInfoCompat findFocus(int var1) {
      return null;
   }

   public Object getProvider() {
      return this.mProvider;
   }

   public boolean performAction(int var1, int var2, Bundle var3) {
      return false;
   }

   interface AccessibilityNodeProviderImpl {
      Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat var1);
   }

   @RequiresApi(16)
   private static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl {
      AccessibilityNodeProviderJellyBeanImpl() {
      }

      public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat var1) {
         return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge() {
            public Object createAccessibilityNodeInfo(int var1x) {
               AccessibilityNodeInfoCompat var2 = var1.createAccessibilityNodeInfo(var1x);
               return var2 == null ? null : var2.unwrap();
            }

            public List findAccessibilityNodeInfosByText(String var1x, int var2) {
               List var5 = var1.findAccessibilityNodeInfosByText(var1x, var2);
               if (var5 == null) {
                  return null;
               } else {
                  ArrayList var4 = new ArrayList();
                  int var3 = var5.size();

                  for(var2 = 0; var2 < var3; ++var2) {
                     var4.add(((AccessibilityNodeInfoCompat)var5.get(var2)).unwrap());
                  }

                  return var4;
               }
            }

            public boolean performAction(int var1x, int var2, Bundle var3) {
               return var1.performAction(var1x, var2, var3);
            }
         });
      }
   }

   @RequiresApi(19)
   private static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl {
      AccessibilityNodeProviderKitKatImpl() {
      }

      public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat var1) {
         return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge() {
            public Object createAccessibilityNodeInfo(int var1x) {
               AccessibilityNodeInfoCompat var2 = var1.createAccessibilityNodeInfo(var1x);
               return var2 == null ? null : var2.unwrap();
            }

            public List findAccessibilityNodeInfosByText(String var1x, int var2) {
               List var5 = var1.findAccessibilityNodeInfosByText(var1x, var2);
               if (var5 == null) {
                  return null;
               } else {
                  ArrayList var4 = new ArrayList();
                  int var3 = var5.size();

                  for(var2 = 0; var2 < var3; ++var2) {
                     var4.add(((AccessibilityNodeInfoCompat)var5.get(var2)).unwrap());
                  }

                  return var4;
               }
            }

            public Object findFocus(int var1x) {
               AccessibilityNodeInfoCompat var2 = var1.findFocus(var1x);
               return var2 == null ? null : var2.unwrap();
            }

            public boolean performAction(int var1x, int var2, Bundle var3) {
               return var1.performAction(var1x, var2, var3);
            }
         });
      }
   }

   static class AccessibilityNodeProviderStubImpl implements AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl {
      public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat var1) {
         return null;
      }
   }
}
