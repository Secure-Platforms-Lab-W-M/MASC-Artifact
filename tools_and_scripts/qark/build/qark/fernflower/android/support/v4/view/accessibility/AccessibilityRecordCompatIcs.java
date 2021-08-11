package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityRecord;
import java.util.List;

@TargetApi(14)
@RequiresApi(14)
class AccessibilityRecordCompatIcs {
   public static int getAddedCount(Object var0) {
      return ((AccessibilityRecord)var0).getAddedCount();
   }

   public static CharSequence getBeforeText(Object var0) {
      return ((AccessibilityRecord)var0).getBeforeText();
   }

   public static CharSequence getClassName(Object var0) {
      return ((AccessibilityRecord)var0).getClassName();
   }

   public static CharSequence getContentDescription(Object var0) {
      return ((AccessibilityRecord)var0).getContentDescription();
   }

   public static int getCurrentItemIndex(Object var0) {
      return ((AccessibilityRecord)var0).getCurrentItemIndex();
   }

   public static int getFromIndex(Object var0) {
      return ((AccessibilityRecord)var0).getFromIndex();
   }

   public static int getItemCount(Object var0) {
      return ((AccessibilityRecord)var0).getItemCount();
   }

   public static Parcelable getParcelableData(Object var0) {
      return ((AccessibilityRecord)var0).getParcelableData();
   }

   public static int getRemovedCount(Object var0) {
      return ((AccessibilityRecord)var0).getRemovedCount();
   }

   public static int getScrollX(Object var0) {
      return ((AccessibilityRecord)var0).getScrollX();
   }

   public static int getScrollY(Object var0) {
      return ((AccessibilityRecord)var0).getScrollY();
   }

   public static Object getSource(Object var0) {
      return ((AccessibilityRecord)var0).getSource();
   }

   public static List getText(Object var0) {
      return ((AccessibilityRecord)var0).getText();
   }

   public static int getToIndex(Object var0) {
      return ((AccessibilityRecord)var0).getToIndex();
   }

   public static int getWindowId(Object var0) {
      return ((AccessibilityRecord)var0).getWindowId();
   }

   public static boolean isChecked(Object var0) {
      return ((AccessibilityRecord)var0).isChecked();
   }

   public static boolean isEnabled(Object var0) {
      return ((AccessibilityRecord)var0).isEnabled();
   }

   public static boolean isFullScreen(Object var0) {
      return ((AccessibilityRecord)var0).isFullScreen();
   }

   public static boolean isPassword(Object var0) {
      return ((AccessibilityRecord)var0).isPassword();
   }

   public static boolean isScrollable(Object var0) {
      return ((AccessibilityRecord)var0).isScrollable();
   }

   public static Object obtain() {
      return AccessibilityRecord.obtain();
   }

   public static Object obtain(Object var0) {
      return AccessibilityRecord.obtain((AccessibilityRecord)var0);
   }

   public static void recycle(Object var0) {
      ((AccessibilityRecord)var0).recycle();
   }

   public static void setAddedCount(Object var0, int var1) {
      ((AccessibilityRecord)var0).setAddedCount(var1);
   }

   public static void setBeforeText(Object var0, CharSequence var1) {
      ((AccessibilityRecord)var0).setBeforeText(var1);
   }

   public static void setChecked(Object var0, boolean var1) {
      ((AccessibilityRecord)var0).setChecked(var1);
   }

   public static void setClassName(Object var0, CharSequence var1) {
      ((AccessibilityRecord)var0).setClassName(var1);
   }

   public static void setContentDescription(Object var0, CharSequence var1) {
      ((AccessibilityRecord)var0).setContentDescription(var1);
   }

   public static void setCurrentItemIndex(Object var0, int var1) {
      ((AccessibilityRecord)var0).setCurrentItemIndex(var1);
   }

   public static void setEnabled(Object var0, boolean var1) {
      ((AccessibilityRecord)var0).setEnabled(var1);
   }

   public static void setFromIndex(Object var0, int var1) {
      ((AccessibilityRecord)var0).setFromIndex(var1);
   }

   public static void setFullScreen(Object var0, boolean var1) {
      ((AccessibilityRecord)var0).setFullScreen(var1);
   }

   public static void setItemCount(Object var0, int var1) {
      ((AccessibilityRecord)var0).setItemCount(var1);
   }

   public static void setParcelableData(Object var0, Parcelable var1) {
      ((AccessibilityRecord)var0).setParcelableData(var1);
   }

   public static void setPassword(Object var0, boolean var1) {
      ((AccessibilityRecord)var0).setPassword(var1);
   }

   public static void setRemovedCount(Object var0, int var1) {
      ((AccessibilityRecord)var0).setRemovedCount(var1);
   }

   public static void setScrollX(Object var0, int var1) {
      ((AccessibilityRecord)var0).setScrollX(var1);
   }

   public static void setScrollY(Object var0, int var1) {
      ((AccessibilityRecord)var0).setScrollY(var1);
   }

   public static void setScrollable(Object var0, boolean var1) {
      ((AccessibilityRecord)var0).setScrollable(var1);
   }

   public static void setSource(Object var0, View var1) {
      ((AccessibilityRecord)var0).setSource(var1);
   }

   public static void setToIndex(Object var0, int var1) {
      ((AccessibilityRecord)var0).setToIndex(var1);
   }
}
