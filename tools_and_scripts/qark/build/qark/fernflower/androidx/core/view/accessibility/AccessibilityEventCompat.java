package androidx.core.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityRecord;

public final class AccessibilityEventCompat {
   public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 4;
   public static final int CONTENT_CHANGE_TYPE_PANE_APPEARED = 16;
   public static final int CONTENT_CHANGE_TYPE_PANE_DISAPPEARED = 32;
   public static final int CONTENT_CHANGE_TYPE_PANE_TITLE = 8;
   public static final int CONTENT_CHANGE_TYPE_SUBTREE = 1;
   public static final int CONTENT_CHANGE_TYPE_TEXT = 2;
   public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0;
   public static final int TYPES_ALL_MASK = -1;
   public static final int TYPE_ANNOUNCEMENT = 16384;
   public static final int TYPE_ASSIST_READING_CONTEXT = 16777216;
   public static final int TYPE_GESTURE_DETECTION_END = 524288;
   public static final int TYPE_GESTURE_DETECTION_START = 262144;
   @Deprecated
   public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 1024;
   @Deprecated
   public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 512;
   public static final int TYPE_TOUCH_INTERACTION_END = 2097152;
   public static final int TYPE_TOUCH_INTERACTION_START = 1048576;
   public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 32768;
   public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 65536;
   public static final int TYPE_VIEW_CONTEXT_CLICKED = 8388608;
   @Deprecated
   public static final int TYPE_VIEW_HOVER_ENTER = 128;
   @Deprecated
   public static final int TYPE_VIEW_HOVER_EXIT = 256;
   @Deprecated
   public static final int TYPE_VIEW_SCROLLED = 4096;
   @Deprecated
   public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 8192;
   public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 131072;
   public static final int TYPE_WINDOWS_CHANGED = 4194304;
   @Deprecated
   public static final int TYPE_WINDOW_CONTENT_CHANGED = 2048;

   private AccessibilityEventCompat() {
   }

   @Deprecated
   public static void appendRecord(AccessibilityEvent var0, AccessibilityRecordCompat var1) {
      var0.appendRecord((AccessibilityRecord)var1.getImpl());
   }

   @Deprecated
   public static AccessibilityRecordCompat asRecord(AccessibilityEvent var0) {
      return new AccessibilityRecordCompat(var0);
   }

   public static int getAction(AccessibilityEvent var0) {
      return VERSION.SDK_INT >= 16 ? var0.getAction() : 0;
   }

   public static int getContentChangeTypes(AccessibilityEvent var0) {
      return VERSION.SDK_INT >= 19 ? var0.getContentChangeTypes() : 0;
   }

   public static int getMovementGranularity(AccessibilityEvent var0) {
      return VERSION.SDK_INT >= 16 ? var0.getMovementGranularity() : 0;
   }

   @Deprecated
   public static AccessibilityRecordCompat getRecord(AccessibilityEvent var0, int var1) {
      return new AccessibilityRecordCompat(var0.getRecord(var1));
   }

   @Deprecated
   public static int getRecordCount(AccessibilityEvent var0) {
      return var0.getRecordCount();
   }

   public static void setAction(AccessibilityEvent var0, int var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.setAction(var1);
      }

   }

   public static void setContentChangeTypes(AccessibilityEvent var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.setContentChangeTypes(var1);
      }

   }

   public static void setMovementGranularity(AccessibilityEvent var0, int var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.setMovementGranularity(var1);
      }

   }
}
