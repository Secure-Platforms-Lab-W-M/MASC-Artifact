package android.support.v4.view.accessibility;

import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityRecord;
import java.util.List;

public class AccessibilityRecordCompat {
   private static final AccessibilityRecordCompat.AccessibilityRecordCompatBaseImpl IMPL;
   private final AccessibilityRecord mRecord;

   static {
      if (VERSION.SDK_INT >= 16) {
         IMPL = new AccessibilityRecordCompat.AccessibilityRecordCompatApi16Impl();
      } else if (VERSION.SDK_INT >= 15) {
         IMPL = new AccessibilityRecordCompat.AccessibilityRecordCompatApi15Impl();
      } else {
         IMPL = new AccessibilityRecordCompat.AccessibilityRecordCompatBaseImpl();
      }
   }

   @Deprecated
   public AccessibilityRecordCompat(Object var1) {
      this.mRecord = (AccessibilityRecord)var1;
   }

   public static int getMaxScrollX(AccessibilityRecord var0) {
      return IMPL.getMaxScrollX(var0);
   }

   public static int getMaxScrollY(AccessibilityRecord var0) {
      return IMPL.getMaxScrollY(var0);
   }

   @Deprecated
   public static AccessibilityRecordCompat obtain() {
      return new AccessibilityRecordCompat(AccessibilityRecord.obtain());
   }

   @Deprecated
   public static AccessibilityRecordCompat obtain(AccessibilityRecordCompat var0) {
      return new AccessibilityRecordCompat(AccessibilityRecord.obtain(var0.mRecord));
   }

   public static void setMaxScrollX(AccessibilityRecord var0, int var1) {
      IMPL.setMaxScrollX(var0, var1);
   }

   public static void setMaxScrollY(AccessibilityRecord var0, int var1) {
      IMPL.setMaxScrollY(var0, var1);
   }

   public static void setSource(@NonNull AccessibilityRecord var0, View var1, int var2) {
      IMPL.setSource(var0, var1, var2);
   }

   @Deprecated
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AccessibilityRecordCompat var3 = (AccessibilityRecordCompat)var1;
         AccessibilityRecord var2 = this.mRecord;
         if (var2 == null) {
            if (var3.mRecord != null) {
               return false;
            }
         } else if (!var2.equals(var3.mRecord)) {
            return false;
         }

         return true;
      }
   }

   @Deprecated
   public int getAddedCount() {
      return this.mRecord.getAddedCount();
   }

   @Deprecated
   public CharSequence getBeforeText() {
      return this.mRecord.getBeforeText();
   }

   @Deprecated
   public CharSequence getClassName() {
      return this.mRecord.getClassName();
   }

   @Deprecated
   public CharSequence getContentDescription() {
      return this.mRecord.getContentDescription();
   }

   @Deprecated
   public int getCurrentItemIndex() {
      return this.mRecord.getCurrentItemIndex();
   }

   @Deprecated
   public int getFromIndex() {
      return this.mRecord.getFromIndex();
   }

   @Deprecated
   public Object getImpl() {
      return this.mRecord;
   }

   @Deprecated
   public int getItemCount() {
      return this.mRecord.getItemCount();
   }

   @Deprecated
   public int getMaxScrollX() {
      return getMaxScrollX(this.mRecord);
   }

   @Deprecated
   public int getMaxScrollY() {
      return getMaxScrollY(this.mRecord);
   }

   @Deprecated
   public Parcelable getParcelableData() {
      return this.mRecord.getParcelableData();
   }

   @Deprecated
   public int getRemovedCount() {
      return this.mRecord.getRemovedCount();
   }

   @Deprecated
   public int getScrollX() {
      return this.mRecord.getScrollX();
   }

   @Deprecated
   public int getScrollY() {
      return this.mRecord.getScrollY();
   }

   @Deprecated
   public AccessibilityNodeInfoCompat getSource() {
      return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mRecord.getSource());
   }

   @Deprecated
   public List getText() {
      return this.mRecord.getText();
   }

   @Deprecated
   public int getToIndex() {
      return this.mRecord.getToIndex();
   }

   @Deprecated
   public int getWindowId() {
      return this.mRecord.getWindowId();
   }

   @Deprecated
   public int hashCode() {
      AccessibilityRecord var1 = this.mRecord;
      return var1 == null ? 0 : var1.hashCode();
   }

   @Deprecated
   public boolean isChecked() {
      return this.mRecord.isChecked();
   }

   @Deprecated
   public boolean isEnabled() {
      return this.mRecord.isEnabled();
   }

   @Deprecated
   public boolean isFullScreen() {
      return this.mRecord.isFullScreen();
   }

   @Deprecated
   public boolean isPassword() {
      return this.mRecord.isPassword();
   }

   @Deprecated
   public boolean isScrollable() {
      return this.mRecord.isScrollable();
   }

   @Deprecated
   public void recycle() {
      this.mRecord.recycle();
   }

   @Deprecated
   public void setAddedCount(int var1) {
      this.mRecord.setAddedCount(var1);
   }

   @Deprecated
   public void setBeforeText(CharSequence var1) {
      this.mRecord.setBeforeText(var1);
   }

   @Deprecated
   public void setChecked(boolean var1) {
      this.mRecord.setChecked(var1);
   }

   @Deprecated
   public void setClassName(CharSequence var1) {
      this.mRecord.setClassName(var1);
   }

   @Deprecated
   public void setContentDescription(CharSequence var1) {
      this.mRecord.setContentDescription(var1);
   }

   @Deprecated
   public void setCurrentItemIndex(int var1) {
      this.mRecord.setCurrentItemIndex(var1);
   }

   @Deprecated
   public void setEnabled(boolean var1) {
      this.mRecord.setEnabled(var1);
   }

   @Deprecated
   public void setFromIndex(int var1) {
      this.mRecord.setFromIndex(var1);
   }

   @Deprecated
   public void setFullScreen(boolean var1) {
      this.mRecord.setFullScreen(var1);
   }

   @Deprecated
   public void setItemCount(int var1) {
      this.mRecord.setItemCount(var1);
   }

   @Deprecated
   public void setMaxScrollX(int var1) {
      setMaxScrollX(this.mRecord, var1);
   }

   @Deprecated
   public void setMaxScrollY(int var1) {
      setMaxScrollY(this.mRecord, var1);
   }

   @Deprecated
   public void setParcelableData(Parcelable var1) {
      this.mRecord.setParcelableData(var1);
   }

   @Deprecated
   public void setPassword(boolean var1) {
      this.mRecord.setPassword(var1);
   }

   @Deprecated
   public void setRemovedCount(int var1) {
      this.mRecord.setRemovedCount(var1);
   }

   @Deprecated
   public void setScrollX(int var1) {
      this.mRecord.setScrollX(var1);
   }

   @Deprecated
   public void setScrollY(int var1) {
      this.mRecord.setScrollY(var1);
   }

   @Deprecated
   public void setScrollable(boolean var1) {
      this.mRecord.setScrollable(var1);
   }

   @Deprecated
   public void setSource(View var1) {
      this.mRecord.setSource(var1);
   }

   @Deprecated
   public void setSource(View var1, int var2) {
      setSource(this.mRecord, var1, var2);
   }

   @Deprecated
   public void setToIndex(int var1) {
      this.mRecord.setToIndex(var1);
   }

   @RequiresApi(15)
   static class AccessibilityRecordCompatApi15Impl extends AccessibilityRecordCompat.AccessibilityRecordCompatBaseImpl {
      public int getMaxScrollX(AccessibilityRecord var1) {
         return var1.getMaxScrollX();
      }

      public int getMaxScrollY(AccessibilityRecord var1) {
         return var1.getMaxScrollY();
      }

      public void setMaxScrollX(AccessibilityRecord var1, int var2) {
         var1.setMaxScrollX(var2);
      }

      public void setMaxScrollY(AccessibilityRecord var1, int var2) {
         var1.setMaxScrollY(var2);
      }
   }

   @RequiresApi(16)
   static class AccessibilityRecordCompatApi16Impl extends AccessibilityRecordCompat.AccessibilityRecordCompatApi15Impl {
      public void setSource(AccessibilityRecord var1, View var2, int var3) {
         var1.setSource(var2, var3);
      }
   }

   static class AccessibilityRecordCompatBaseImpl {
      public int getMaxScrollX(AccessibilityRecord var1) {
         return 0;
      }

      public int getMaxScrollY(AccessibilityRecord var1) {
         return 0;
      }

      public void setMaxScrollX(AccessibilityRecord var1, int var2) {
      }

      public void setMaxScrollY(AccessibilityRecord var1, int var2) {
      }

      public void setSource(AccessibilityRecord var1, View var2, int var3) {
      }
   }
}
