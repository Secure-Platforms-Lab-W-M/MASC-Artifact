package androidx.core.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;

public final class AccessibilityServiceInfoCompat {
   public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
   public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
   public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
   public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
   public static final int FEEDBACK_ALL_MASK = -1;
   public static final int FEEDBACK_BRAILLE = 32;
   public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
   public static final int FLAG_REPORT_VIEW_IDS = 16;
   public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
   public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
   public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;

   private AccessibilityServiceInfoCompat() {
   }

   public static String capabilityToString(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            if (var0 != 4) {
               return var0 != 8 ? "UNKNOWN" : "CAPABILITY_CAN_FILTER_KEY_EVENTS";
            } else {
               return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
            }
         } else {
            return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
         }
      } else {
         return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
      }
   }

   public static String feedbackTypeToString(int var0) {
      StringBuilder var2 = new StringBuilder();
      var2.append("[");

      while(var0 > 0) {
         int var1 = 1 << Integer.numberOfTrailingZeros(var0);
         var0 &= var1;
         if (var2.length() > 1) {
            var2.append(", ");
         }

         if (var1 != 1) {
            if (var1 != 2) {
               if (var1 != 4) {
                  if (var1 != 8) {
                     if (var1 == 16) {
                        var2.append("FEEDBACK_GENERIC");
                     }
                  } else {
                     var2.append("FEEDBACK_VISUAL");
                  }
               } else {
                  var2.append("FEEDBACK_AUDIBLE");
               }
            } else {
               var2.append("FEEDBACK_HAPTIC");
            }
         } else {
            var2.append("FEEDBACK_SPOKEN");
         }
      }

      var2.append("]");
      return var2.toString();
   }

   public static String flagToString(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            if (var0 != 4) {
               if (var0 != 8) {
                  if (var0 != 16) {
                     return var0 != 32 ? null : "FLAG_REQUEST_FILTER_KEY_EVENTS";
                  } else {
                     return "FLAG_REPORT_VIEW_IDS";
                  }
               } else {
                  return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
               }
            } else {
               return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
            }
         } else {
            return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
         }
      } else {
         return "DEFAULT";
      }
   }

   public static int getCapabilities(AccessibilityServiceInfo var0) {
      if (VERSION.SDK_INT >= 18) {
         return var0.getCapabilities();
      } else {
         return var0.getCanRetrieveWindowContent() ? 1 : 0;
      }
   }

   public static String loadDescription(AccessibilityServiceInfo var0, PackageManager var1) {
      return VERSION.SDK_INT >= 16 ? var0.loadDescription(var1) : var0.getDescription();
   }
}
