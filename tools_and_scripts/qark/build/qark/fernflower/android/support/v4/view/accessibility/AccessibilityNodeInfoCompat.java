package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import android.view.accessibility.AccessibilityNodeInfo.RangeInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AccessibilityNodeInfoCompat {
   public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
   public static final String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";
   public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
   public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
   public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
   public static final String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";
   public static final String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";
   public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
   public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
   public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
   public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
   public static final int ACTION_CLEAR_FOCUS = 2;
   public static final int ACTION_CLEAR_SELECTION = 8;
   public static final int ACTION_CLICK = 16;
   public static final int ACTION_COLLAPSE = 524288;
   public static final int ACTION_COPY = 16384;
   public static final int ACTION_CUT = 65536;
   public static final int ACTION_DISMISS = 1048576;
   public static final int ACTION_EXPAND = 262144;
   public static final int ACTION_FOCUS = 1;
   public static final int ACTION_LONG_CLICK = 32;
   public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
   public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
   public static final int ACTION_PASTE = 32768;
   public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
   public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
   public static final int ACTION_SCROLL_BACKWARD = 8192;
   public static final int ACTION_SCROLL_FORWARD = 4096;
   public static final int ACTION_SELECT = 4;
   public static final int ACTION_SET_SELECTION = 131072;
   public static final int ACTION_SET_TEXT = 2097152;
   public static final int FOCUS_ACCESSIBILITY = 2;
   public static final int FOCUS_INPUT = 1;
   static final AccessibilityNodeInfoCompat.AccessibilityNodeInfoBaseImpl IMPL;
   public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
   public static final int MOVEMENT_GRANULARITY_LINE = 4;
   public static final int MOVEMENT_GRANULARITY_PAGE = 16;
   public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
   public static final int MOVEMENT_GRANULARITY_WORD = 2;
   private final AccessibilityNodeInfo mInfo;
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int mParentVirtualDescendantId = -1;

   static {
      if (VERSION.SDK_INT >= 24) {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi24Impl();
      } else if (VERSION.SDK_INT >= 23) {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi23Impl();
      } else if (VERSION.SDK_INT >= 22) {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi22Impl();
      } else if (VERSION.SDK_INT >= 21) {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi21Impl();
      } else if (VERSION.SDK_INT >= 19) {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi19Impl();
      } else if (VERSION.SDK_INT >= 18) {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi18Impl();
      } else if (VERSION.SDK_INT >= 17) {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi17Impl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi16Impl();
      } else {
         IMPL = new AccessibilityNodeInfoCompat.AccessibilityNodeInfoBaseImpl();
      }
   }

   private AccessibilityNodeInfoCompat(AccessibilityNodeInfo var1) {
      this.mInfo = var1;
   }

   @Deprecated
   public AccessibilityNodeInfoCompat(Object var1) {
      this.mInfo = (AccessibilityNodeInfo)var1;
   }

   private static String getActionSymbolicName(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            switch(var0) {
            case 4:
               return "ACTION_SELECT";
            case 8:
               return "ACTION_CLEAR_SELECTION";
            case 16:
               return "ACTION_CLICK";
            case 32:
               return "ACTION_LONG_CLICK";
            case 64:
               return "ACTION_ACCESSIBILITY_FOCUS";
            case 128:
               return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            case 256:
               return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            case 512:
               return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            case 1024:
               return "ACTION_NEXT_HTML_ELEMENT";
            case 2048:
               return "ACTION_PREVIOUS_HTML_ELEMENT";
            case 4096:
               return "ACTION_SCROLL_FORWARD";
            case 8192:
               return "ACTION_SCROLL_BACKWARD";
            case 16384:
               return "ACTION_COPY";
            case 32768:
               return "ACTION_PASTE";
            case 65536:
               return "ACTION_CUT";
            case 131072:
               return "ACTION_SET_SELECTION";
            default:
               return "ACTION_UNKNOWN";
            }
         } else {
            return "ACTION_CLEAR_FOCUS";
         }
      } else {
         return "ACTION_FOCUS";
      }
   }

   public static AccessibilityNodeInfoCompat obtain() {
      return wrap(AccessibilityNodeInfo.obtain());
   }

   public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat var0) {
      return wrap(AccessibilityNodeInfo.obtain(var0.mInfo));
   }

   public static AccessibilityNodeInfoCompat obtain(View var0) {
      return wrap(AccessibilityNodeInfo.obtain(var0));
   }

   public static AccessibilityNodeInfoCompat obtain(View var0, int var1) {
      return wrapNonNullInstance(IMPL.obtain(var0, var1));
   }

   public static AccessibilityNodeInfoCompat wrap(@NonNull AccessibilityNodeInfo var0) {
      return new AccessibilityNodeInfoCompat(var0);
   }

   static AccessibilityNodeInfoCompat wrapNonNullInstance(Object var0) {
      return var0 != null ? new AccessibilityNodeInfoCompat(var0) : null;
   }

   public void addAction(int var1) {
      this.mInfo.addAction(var1);
   }

   public void addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat var1) {
      IMPL.addAction(this.mInfo, var1.mAction);
   }

   public void addChild(View var1) {
      this.mInfo.addChild(var1);
   }

   public void addChild(View var1, int var2) {
      IMPL.addChild(this.mInfo, var1, var2);
   }

   public boolean canOpenPopup() {
      return IMPL.canOpenPopup(this.mInfo);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         AccessibilityNodeInfoCompat var3 = (AccessibilityNodeInfoCompat)var1;
         AccessibilityNodeInfo var2 = this.mInfo;
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

   public List findAccessibilityNodeInfosByText(String var1) {
      ArrayList var4 = new ArrayList();
      List var5 = this.mInfo.findAccessibilityNodeInfosByText(var1);
      int var3 = var5.size();

      for(int var2 = 0; var2 < var3; ++var2) {
         var4.add(wrap((AccessibilityNodeInfo)var5.get(var2)));
      }

      return var4;
   }

   public List findAccessibilityNodeInfosByViewId(String var1) {
      List var2 = IMPL.findAccessibilityNodeInfosByViewId(this.mInfo, var1);
      if (var2 == null) {
         return Collections.emptyList();
      } else {
         ArrayList var3 = new ArrayList();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.add(wrap((AccessibilityNodeInfo)var4.next()));
         }

         return var3;
      }
   }

   public AccessibilityNodeInfoCompat findFocus(int var1) {
      return wrapNonNullInstance(IMPL.findFocus(this.mInfo, var1));
   }

   public AccessibilityNodeInfoCompat focusSearch(int var1) {
      return wrapNonNullInstance(IMPL.focusSearch(this.mInfo, var1));
   }

   public List getActionList() {
      List var3 = IMPL.getActionList(this.mInfo);
      if (var3 == null) {
         return Collections.emptyList();
      } else {
         ArrayList var4 = new ArrayList();
         int var2 = var3.size();

         for(int var1 = 0; var1 < var2; ++var1) {
            var4.add(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(var3.get(var1)));
         }

         return var4;
      }
   }

   public int getActions() {
      return this.mInfo.getActions();
   }

   public void getBoundsInParent(Rect var1) {
      this.mInfo.getBoundsInParent(var1);
   }

   public void getBoundsInScreen(Rect var1) {
      this.mInfo.getBoundsInScreen(var1);
   }

   public AccessibilityNodeInfoCompat getChild(int var1) {
      return wrapNonNullInstance(this.mInfo.getChild(var1));
   }

   public int getChildCount() {
      return this.mInfo.getChildCount();
   }

   public CharSequence getClassName() {
      return this.mInfo.getClassName();
   }

   public AccessibilityNodeInfoCompat.CollectionInfoCompat getCollectionInfo() {
      Object var1 = IMPL.getCollectionInfo(this.mInfo);
      return var1 == null ? null : new AccessibilityNodeInfoCompat.CollectionInfoCompat(var1);
   }

   public AccessibilityNodeInfoCompat.CollectionItemInfoCompat getCollectionItemInfo() {
      Object var1 = IMPL.getCollectionItemInfo(this.mInfo);
      return var1 == null ? null : new AccessibilityNodeInfoCompat.CollectionItemInfoCompat(var1);
   }

   public CharSequence getContentDescription() {
      return this.mInfo.getContentDescription();
   }

   public int getDrawingOrder() {
      return IMPL.getDrawingOrder(this.mInfo);
   }

   public CharSequence getError() {
      return IMPL.getError(this.mInfo);
   }

   public Bundle getExtras() {
      return IMPL.getExtras(this.mInfo);
   }

   @Deprecated
   public Object getInfo() {
      return this.mInfo;
   }

   public int getInputType() {
      return IMPL.getInputType(this.mInfo);
   }

   public AccessibilityNodeInfoCompat getLabelFor() {
      return wrapNonNullInstance(IMPL.getLabelFor(this.mInfo));
   }

   public AccessibilityNodeInfoCompat getLabeledBy() {
      return wrapNonNullInstance(IMPL.getLabeledBy(this.mInfo));
   }

   public int getLiveRegion() {
      return IMPL.getLiveRegion(this.mInfo);
   }

   public int getMaxTextLength() {
      return IMPL.getMaxTextLength(this.mInfo);
   }

   public int getMovementGranularities() {
      return IMPL.getMovementGranularities(this.mInfo);
   }

   public CharSequence getPackageName() {
      return this.mInfo.getPackageName();
   }

   public AccessibilityNodeInfoCompat getParent() {
      return wrapNonNullInstance(this.mInfo.getParent());
   }

   public AccessibilityNodeInfoCompat.RangeInfoCompat getRangeInfo() {
      Object var1 = IMPL.getRangeInfo(this.mInfo);
      return var1 == null ? null : new AccessibilityNodeInfoCompat.RangeInfoCompat(var1);
   }

   @Nullable
   public CharSequence getRoleDescription() {
      return IMPL.getRoleDescription(this.mInfo);
   }

   public CharSequence getText() {
      return this.mInfo.getText();
   }

   public int getTextSelectionEnd() {
      return IMPL.getTextSelectionEnd(this.mInfo);
   }

   public int getTextSelectionStart() {
      return IMPL.getTextSelectionStart(this.mInfo);
   }

   public AccessibilityNodeInfoCompat getTraversalAfter() {
      return wrapNonNullInstance(IMPL.getTraversalAfter(this.mInfo));
   }

   public AccessibilityNodeInfoCompat getTraversalBefore() {
      return wrapNonNullInstance(IMPL.getTraversalBefore(this.mInfo));
   }

   public String getViewIdResourceName() {
      return IMPL.getViewIdResourceName(this.mInfo);
   }

   public AccessibilityWindowInfoCompat getWindow() {
      return AccessibilityWindowInfoCompat.wrapNonNullInstance(IMPL.getWindow(this.mInfo));
   }

   public int getWindowId() {
      return this.mInfo.getWindowId();
   }

   public int hashCode() {
      AccessibilityNodeInfo var1 = this.mInfo;
      return var1 == null ? 0 : var1.hashCode();
   }

   public boolean isAccessibilityFocused() {
      return IMPL.isAccessibilityFocused(this.mInfo);
   }

   public boolean isCheckable() {
      return this.mInfo.isCheckable();
   }

   public boolean isChecked() {
      return this.mInfo.isChecked();
   }

   public boolean isClickable() {
      return this.mInfo.isClickable();
   }

   public boolean isContentInvalid() {
      return IMPL.isContentInvalid(this.mInfo);
   }

   public boolean isContextClickable() {
      return IMPL.isContextClickable(this.mInfo);
   }

   public boolean isDismissable() {
      return IMPL.isDismissable(this.mInfo);
   }

   public boolean isEditable() {
      return IMPL.isEditable(this.mInfo);
   }

   public boolean isEnabled() {
      return this.mInfo.isEnabled();
   }

   public boolean isFocusable() {
      return this.mInfo.isFocusable();
   }

   public boolean isFocused() {
      return this.mInfo.isFocused();
   }

   public boolean isImportantForAccessibility() {
      return IMPL.isImportantForAccessibility(this.mInfo);
   }

   public boolean isLongClickable() {
      return this.mInfo.isLongClickable();
   }

   public boolean isMultiLine() {
      return IMPL.isMultiLine(this.mInfo);
   }

   public boolean isPassword() {
      return this.mInfo.isPassword();
   }

   public boolean isScrollable() {
      return this.mInfo.isScrollable();
   }

   public boolean isSelected() {
      return this.mInfo.isSelected();
   }

   public boolean isVisibleToUser() {
      return IMPL.isVisibleToUser(this.mInfo);
   }

   public boolean performAction(int var1) {
      return this.mInfo.performAction(var1);
   }

   public boolean performAction(int var1, Bundle var2) {
      return IMPL.performAction(this.mInfo, var1, var2);
   }

   public void recycle() {
      this.mInfo.recycle();
   }

   public boolean refresh() {
      return IMPL.refresh(this.mInfo);
   }

   public boolean removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat var1) {
      return IMPL.removeAction(this.mInfo, var1.mAction);
   }

   public boolean removeChild(View var1) {
      return IMPL.removeChild(this.mInfo, var1);
   }

   public boolean removeChild(View var1, int var2) {
      return IMPL.removeChild(this.mInfo, var1, var2);
   }

   public void setAccessibilityFocused(boolean var1) {
      IMPL.setAccessibilityFocused(this.mInfo, var1);
   }

   public void setBoundsInParent(Rect var1) {
      this.mInfo.setBoundsInParent(var1);
   }

   public void setBoundsInScreen(Rect var1) {
      this.mInfo.setBoundsInScreen(var1);
   }

   public void setCanOpenPopup(boolean var1) {
      IMPL.setCanOpenPopup(this.mInfo, var1);
   }

   public void setCheckable(boolean var1) {
      this.mInfo.setCheckable(var1);
   }

   public void setChecked(boolean var1) {
      this.mInfo.setChecked(var1);
   }

   public void setClassName(CharSequence var1) {
      this.mInfo.setClassName(var1);
   }

   public void setClickable(boolean var1) {
      this.mInfo.setClickable(var1);
   }

   public void setCollectionInfo(Object var1) {
      IMPL.setCollectionInfo(this.mInfo, ((AccessibilityNodeInfoCompat.CollectionInfoCompat)var1).mInfo);
   }

   public void setCollectionItemInfo(Object var1) {
      IMPL.setCollectionItemInfo(this.mInfo, ((AccessibilityNodeInfoCompat.CollectionItemInfoCompat)var1).mInfo);
   }

   public void setContentDescription(CharSequence var1) {
      this.mInfo.setContentDescription(var1);
   }

   public void setContentInvalid(boolean var1) {
      IMPL.setContentInvalid(this.mInfo, var1);
   }

   public void setContextClickable(boolean var1) {
      IMPL.setContextClickable(this.mInfo, var1);
   }

   public void setDismissable(boolean var1) {
      IMPL.setDismissable(this.mInfo, var1);
   }

   public void setDrawingOrder(int var1) {
      IMPL.setDrawingOrder(this.mInfo, var1);
   }

   public void setEditable(boolean var1) {
      IMPL.setEditable(this.mInfo, var1);
   }

   public void setEnabled(boolean var1) {
      this.mInfo.setEnabled(var1);
   }

   public void setError(CharSequence var1) {
      IMPL.setError(this.mInfo, var1);
   }

   public void setFocusable(boolean var1) {
      this.mInfo.setFocusable(var1);
   }

   public void setFocused(boolean var1) {
      this.mInfo.setFocused(var1);
   }

   public void setImportantForAccessibility(boolean var1) {
      IMPL.setImportantForAccessibility(this.mInfo, var1);
   }

   public void setInputType(int var1) {
      IMPL.setInputType(this.mInfo, var1);
   }

   public void setLabelFor(View var1) {
      IMPL.setLabelFor(this.mInfo, var1);
   }

   public void setLabelFor(View var1, int var2) {
      IMPL.setLabelFor(this.mInfo, var1, var2);
   }

   public void setLabeledBy(View var1) {
      IMPL.setLabeledBy(this.mInfo, var1);
   }

   public void setLabeledBy(View var1, int var2) {
      IMPL.setLabeledBy(this.mInfo, var1, var2);
   }

   public void setLiveRegion(int var1) {
      IMPL.setLiveRegion(this.mInfo, var1);
   }

   public void setLongClickable(boolean var1) {
      this.mInfo.setLongClickable(var1);
   }

   public void setMaxTextLength(int var1) {
      IMPL.setMaxTextLength(this.mInfo, var1);
   }

   public void setMovementGranularities(int var1) {
      IMPL.setMovementGranularities(this.mInfo, var1);
   }

   public void setMultiLine(boolean var1) {
      IMPL.setMultiLine(this.mInfo, var1);
   }

   public void setPackageName(CharSequence var1) {
      this.mInfo.setPackageName(var1);
   }

   public void setParent(View var1) {
      this.mInfo.setParent(var1);
   }

   public void setParent(View var1, int var2) {
      this.mParentVirtualDescendantId = var2;
      IMPL.setParent(this.mInfo, var1, var2);
   }

   public void setPassword(boolean var1) {
      this.mInfo.setPassword(var1);
   }

   public void setRangeInfo(AccessibilityNodeInfoCompat.RangeInfoCompat var1) {
      IMPL.setRangeInfo(this.mInfo, var1.mInfo);
   }

   public void setRoleDescription(@Nullable CharSequence var1) {
      IMPL.setRoleDescription(this.mInfo, var1);
   }

   public void setScrollable(boolean var1) {
      this.mInfo.setScrollable(var1);
   }

   public void setSelected(boolean var1) {
      this.mInfo.setSelected(var1);
   }

   public void setSource(View var1) {
      this.mInfo.setSource(var1);
   }

   public void setSource(View var1, int var2) {
      IMPL.setSource(this.mInfo, var1, var2);
   }

   public void setText(CharSequence var1) {
      this.mInfo.setText(var1);
   }

   public void setTextSelection(int var1, int var2) {
      IMPL.setTextSelection(this.mInfo, var1, var2);
   }

   public void setTraversalAfter(View var1) {
      IMPL.setTraversalAfter(this.mInfo, var1);
   }

   public void setTraversalAfter(View var1, int var2) {
      IMPL.setTraversalAfter(this.mInfo, var1, var2);
   }

   public void setTraversalBefore(View var1) {
      IMPL.setTraversalBefore(this.mInfo, var1);
   }

   public void setTraversalBefore(View var1, int var2) {
      IMPL.setTraversalBefore(this.mInfo, var1, var2);
   }

   public void setViewIdResourceName(String var1) {
      IMPL.setViewIdResourceName(this.mInfo, var1);
   }

   public void setVisibleToUser(boolean var1) {
      IMPL.setVisibleToUser(this.mInfo, var1);
   }

   public String toString() {
      StringBuilder var3 = new StringBuilder();
      var3.append(super.toString());
      Rect var4 = new Rect();
      this.getBoundsInParent(var4);
      StringBuilder var5 = new StringBuilder();
      var5.append("; boundsInParent: ");
      var5.append(var4);
      var3.append(var5.toString());
      this.getBoundsInScreen(var4);
      var5 = new StringBuilder();
      var5.append("; boundsInScreen: ");
      var5.append(var4);
      var3.append(var5.toString());
      var3.append("; packageName: ");
      var3.append(this.getPackageName());
      var3.append("; className: ");
      var3.append(this.getClassName());
      var3.append("; text: ");
      var3.append(this.getText());
      var3.append("; contentDescription: ");
      var3.append(this.getContentDescription());
      var3.append("; viewId: ");
      var3.append(this.getViewIdResourceName());
      var3.append("; checkable: ");
      var3.append(this.isCheckable());
      var3.append("; checked: ");
      var3.append(this.isChecked());
      var3.append("; focusable: ");
      var3.append(this.isFocusable());
      var3.append("; focused: ");
      var3.append(this.isFocused());
      var3.append("; selected: ");
      var3.append(this.isSelected());
      var3.append("; clickable: ");
      var3.append(this.isClickable());
      var3.append("; longClickable: ");
      var3.append(this.isLongClickable());
      var3.append("; enabled: ");
      var3.append(this.isEnabled());
      var3.append("; password: ");
      var3.append(this.isPassword());
      StringBuilder var6 = new StringBuilder();
      var6.append("; scrollable: ");
      var6.append(this.isScrollable());
      var3.append(var6.toString());
      var3.append("; [");
      int var1 = this.getActions();

      while(var1 != 0) {
         int var2 = 1 << Integer.numberOfTrailingZeros(var1);
         var1 &= ~var2;
         var3.append(getActionSymbolicName(var2));
         if (var1 != 0) {
            var3.append(", ");
         }
      }

      var3.append("]");
      return var3.toString();
   }

   public AccessibilityNodeInfo unwrap() {
      return this.mInfo;
   }

   public static class AccessibilityActionCompat {
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(64, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(128, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_FOCUS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLEAR_SELECTION = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(8, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CLICK = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_COLLAPSE = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(524288, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CONTEXT_CLICK;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_COPY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16384, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_CUT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(65536, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_DISMISS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(1048576, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_EXPAND = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(262144, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_FOCUS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(1, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_LONG_CLICK = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(32, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(256, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(1024, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PASTE = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(32768, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(512, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2048, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_BACKWARD = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(8192, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_DOWN;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_FORWARD = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(4096, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_LEFT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_RIGHT;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_TO_POSITION;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SCROLL_UP;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SELECT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(4, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_PROGRESS;
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_SELECTION = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(131072, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SET_TEXT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(2097152, (CharSequence)null);
      public static final AccessibilityNodeInfoCompat.AccessibilityActionCompat ACTION_SHOW_ON_SCREEN;
      final Object mAction;

      static {
         ACTION_SHOW_ON_SCREEN = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionShowOnScreen());
         ACTION_SCROLL_TO_POSITION = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollToPosition());
         ACTION_SCROLL_UP = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollUp());
         ACTION_SCROLL_LEFT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollLeft());
         ACTION_SCROLL_DOWN = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollDown());
         ACTION_SCROLL_RIGHT = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollRight());
         ACTION_CONTEXT_CLICK = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionContextClick());
         ACTION_SET_PROGRESS = new AccessibilityNodeInfoCompat.AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionSetProgress());
      }

      public AccessibilityActionCompat(int var1, CharSequence var2) {
         this(AccessibilityNodeInfoCompat.IMPL.newAccessibilityAction(var1, var2));
      }

      AccessibilityActionCompat(Object var1) {
         this.mAction = var1;
      }

      public int getId() {
         return AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionId(this.mAction);
      }

      public CharSequence getLabel() {
         return AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionLabel(this.mAction);
      }
   }

   @RequiresApi(16)
   static class AccessibilityNodeInfoApi16Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoBaseImpl {
      public void addChild(AccessibilityNodeInfo var1, View var2, int var3) {
         var1.addChild(var2, var3);
      }

      public Object findFocus(AccessibilityNodeInfo var1, int var2) {
         return var1.findFocus(var2);
      }

      public Object focusSearch(AccessibilityNodeInfo var1, int var2) {
         return var1.focusSearch(var2);
      }

      public int getMovementGranularities(AccessibilityNodeInfo var1) {
         return var1.getMovementGranularities();
      }

      public boolean isAccessibilityFocused(AccessibilityNodeInfo var1) {
         return var1.isAccessibilityFocused();
      }

      public boolean isVisibleToUser(AccessibilityNodeInfo var1) {
         return var1.isVisibleToUser();
      }

      public AccessibilityNodeInfo obtain(View var1, int var2) {
         return AccessibilityNodeInfo.obtain(var1, var2);
      }

      public boolean performAction(AccessibilityNodeInfo var1, int var2, Bundle var3) {
         return var1.performAction(var2, var3);
      }

      public void setAccessibilityFocused(AccessibilityNodeInfo var1, boolean var2) {
         var1.setAccessibilityFocused(var2);
      }

      public void setMovementGranularities(AccessibilityNodeInfo var1, int var2) {
         var1.setMovementGranularities(var2);
      }

      public void setParent(AccessibilityNodeInfo var1, View var2, int var3) {
         var1.setParent(var2, var3);
      }

      public void setSource(AccessibilityNodeInfo var1, View var2, int var3) {
         var1.setSource(var2, var3);
      }

      public void setVisibleToUser(AccessibilityNodeInfo var1, boolean var2) {
         var1.setVisibleToUser(var2);
      }
   }

   @RequiresApi(17)
   static class AccessibilityNodeInfoApi17Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi16Impl {
      public Object getLabelFor(AccessibilityNodeInfo var1) {
         return var1.getLabelFor();
      }

      public Object getLabeledBy(AccessibilityNodeInfo var1) {
         return var1.getLabeledBy();
      }

      public void setLabelFor(AccessibilityNodeInfo var1, View var2) {
         var1.setLabelFor(var2);
      }

      public void setLabelFor(AccessibilityNodeInfo var1, View var2, int var3) {
         var1.setLabelFor(var2, var3);
      }

      public void setLabeledBy(AccessibilityNodeInfo var1, View var2) {
         var1.setLabeledBy(var2);
      }

      public void setLabeledBy(AccessibilityNodeInfo var1, View var2, int var3) {
         var1.setLabeledBy(var2, var3);
      }
   }

   @RequiresApi(18)
   static class AccessibilityNodeInfoApi18Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi17Impl {
      public List findAccessibilityNodeInfosByViewId(AccessibilityNodeInfo var1, String var2) {
         return var1.findAccessibilityNodeInfosByViewId(var2);
      }

      public int getTextSelectionEnd(AccessibilityNodeInfo var1) {
         return var1.getTextSelectionEnd();
      }

      public int getTextSelectionStart(AccessibilityNodeInfo var1) {
         return var1.getTextSelectionStart();
      }

      public String getViewIdResourceName(AccessibilityNodeInfo var1) {
         return var1.getViewIdResourceName();
      }

      public boolean isEditable(AccessibilityNodeInfo var1) {
         return var1.isEditable();
      }

      public boolean refresh(AccessibilityNodeInfo var1) {
         return var1.refresh();
      }

      public void setEditable(AccessibilityNodeInfo var1, boolean var2) {
         var1.setEditable(var2);
      }

      public void setTextSelection(AccessibilityNodeInfo var1, int var2, int var3) {
         var1.setTextSelection(var2, var3);
      }

      public void setViewIdResourceName(AccessibilityNodeInfo var1, String var2) {
         var1.setViewIdResourceName(var2);
      }
   }

   @RequiresApi(19)
   static class AccessibilityNodeInfoApi19Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi18Impl {
      private static final String ROLE_DESCRIPTION_KEY = "AccessibilityNodeInfo.roleDescription";

      public boolean canOpenPopup(AccessibilityNodeInfo var1) {
         return var1.canOpenPopup();
      }

      public Object getCollectionInfo(AccessibilityNodeInfo var1) {
         return var1.getCollectionInfo();
      }

      public int getCollectionInfoColumnCount(Object var1) {
         return ((CollectionInfo)var1).getColumnCount();
      }

      public int getCollectionInfoRowCount(Object var1) {
         return ((CollectionInfo)var1).getRowCount();
      }

      public int getCollectionItemColumnIndex(Object var1) {
         return ((CollectionItemInfo)var1).getColumnIndex();
      }

      public int getCollectionItemColumnSpan(Object var1) {
         return ((CollectionItemInfo)var1).getColumnSpan();
      }

      public Object getCollectionItemInfo(AccessibilityNodeInfo var1) {
         return var1.getCollectionItemInfo();
      }

      public int getCollectionItemRowIndex(Object var1) {
         return ((CollectionItemInfo)var1).getRowIndex();
      }

      public int getCollectionItemRowSpan(Object var1) {
         return ((CollectionItemInfo)var1).getRowSpan();
      }

      public Bundle getExtras(AccessibilityNodeInfo var1) {
         return var1.getExtras();
      }

      public int getInputType(AccessibilityNodeInfo var1) {
         return var1.getInputType();
      }

      public int getLiveRegion(AccessibilityNodeInfo var1) {
         return var1.getLiveRegion();
      }

      public Object getRangeInfo(AccessibilityNodeInfo var1) {
         return var1.getRangeInfo();
      }

      public CharSequence getRoleDescription(AccessibilityNodeInfo var1) {
         return this.getExtras(var1).getCharSequence("AccessibilityNodeInfo.roleDescription");
      }

      public boolean isCollectionInfoHierarchical(Object var1) {
         return ((CollectionInfo)var1).isHierarchical();
      }

      public boolean isCollectionItemHeading(Object var1) {
         return ((CollectionItemInfo)var1).isHeading();
      }

      public boolean isContentInvalid(AccessibilityNodeInfo var1) {
         return var1.isContentInvalid();
      }

      public boolean isDismissable(AccessibilityNodeInfo var1) {
         return var1.isDismissable();
      }

      public boolean isMultiLine(AccessibilityNodeInfo var1) {
         return var1.isMultiLine();
      }

      public Object obtainCollectionInfo(int var1, int var2, boolean var3) {
         return CollectionInfo.obtain(var1, var2, var3);
      }

      public Object obtainCollectionInfo(int var1, int var2, boolean var3, int var4) {
         return CollectionInfo.obtain(var1, var2, var3);
      }

      public Object obtainCollectionItemInfo(int var1, int var2, int var3, int var4, boolean var5) {
         return CollectionItemInfo.obtain(var1, var2, var3, var4, var5);
      }

      public Object obtainCollectionItemInfo(int var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         return CollectionItemInfo.obtain(var1, var2, var3, var4, var5);
      }

      public Object obtainRangeInfo(int var1, float var2, float var3, float var4) {
         return RangeInfo.obtain(var1, var2, var3, var4);
      }

      public void setCanOpenPopup(AccessibilityNodeInfo var1, boolean var2) {
         var1.setCanOpenPopup(var2);
      }

      public void setCollectionInfo(AccessibilityNodeInfo var1, Object var2) {
         var1.setCollectionInfo((CollectionInfo)var2);
      }

      public void setCollectionItemInfo(AccessibilityNodeInfo var1, Object var2) {
         var1.setCollectionItemInfo((CollectionItemInfo)var2);
      }

      public void setContentInvalid(AccessibilityNodeInfo var1, boolean var2) {
         var1.setContentInvalid(var2);
      }

      public void setDismissable(AccessibilityNodeInfo var1, boolean var2) {
         var1.setDismissable(var2);
      }

      public void setInputType(AccessibilityNodeInfo var1, int var2) {
         var1.setInputType(var2);
      }

      public void setLiveRegion(AccessibilityNodeInfo var1, int var2) {
         var1.setLiveRegion(var2);
      }

      public void setMultiLine(AccessibilityNodeInfo var1, boolean var2) {
         var1.setMultiLine(var2);
      }

      public void setRangeInfo(AccessibilityNodeInfo var1, Object var2) {
         var1.setRangeInfo((RangeInfo)var2);
      }

      public void setRoleDescription(AccessibilityNodeInfo var1, CharSequence var2) {
         this.getExtras(var1).putCharSequence("AccessibilityNodeInfo.roleDescription", var2);
      }
   }

   @RequiresApi(21)
   static class AccessibilityNodeInfoApi21Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi19Impl {
      public void addAction(AccessibilityNodeInfo var1, Object var2) {
         var1.addAction((AccessibilityAction)var2);
      }

      public int getAccessibilityActionId(Object var1) {
         return ((AccessibilityAction)var1).getId();
      }

      public CharSequence getAccessibilityActionLabel(Object var1) {
         return ((AccessibilityAction)var1).getLabel();
      }

      public List getActionList(AccessibilityNodeInfo var1) {
         return (List)var1.getActionList();
      }

      public int getCollectionInfoSelectionMode(Object var1) {
         return ((CollectionInfo)var1).getSelectionMode();
      }

      public CharSequence getError(AccessibilityNodeInfo var1) {
         return var1.getError();
      }

      public int getMaxTextLength(AccessibilityNodeInfo var1) {
         return var1.getMaxTextLength();
      }

      public Object getWindow(AccessibilityNodeInfo var1) {
         return var1.getWindow();
      }

      public boolean isCollectionItemSelected(Object var1) {
         return ((CollectionItemInfo)var1).isSelected();
      }

      public Object newAccessibilityAction(int var1, CharSequence var2) {
         return new AccessibilityAction(var1, var2);
      }

      public Object obtainCollectionInfo(int var1, int var2, boolean var3, int var4) {
         return CollectionInfo.obtain(var1, var2, var3, var4);
      }

      public Object obtainCollectionItemInfo(int var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         return CollectionItemInfo.obtain(var1, var2, var3, var4, var5, var6);
      }

      public boolean removeAction(AccessibilityNodeInfo var1, Object var2) {
         return var1.removeAction((AccessibilityAction)var2);
      }

      public boolean removeChild(AccessibilityNodeInfo var1, View var2) {
         return var1.removeChild(var2);
      }

      public boolean removeChild(AccessibilityNodeInfo var1, View var2, int var3) {
         return var1.removeChild(var2, var3);
      }

      public void setError(AccessibilityNodeInfo var1, CharSequence var2) {
         var1.setError(var2);
      }

      public void setMaxTextLength(AccessibilityNodeInfo var1, int var2) {
         var1.setMaxTextLength(var2);
      }
   }

   @RequiresApi(22)
   static class AccessibilityNodeInfoApi22Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi21Impl {
      public Object getTraversalAfter(AccessibilityNodeInfo var1) {
         return var1.getTraversalAfter();
      }

      public Object getTraversalBefore(AccessibilityNodeInfo var1) {
         return var1.getTraversalBefore();
      }

      public void setTraversalAfter(AccessibilityNodeInfo var1, View var2) {
         var1.setTraversalAfter(var2);
      }

      public void setTraversalAfter(AccessibilityNodeInfo var1, View var2, int var3) {
         var1.setTraversalAfter(var2, var3);
      }

      public void setTraversalBefore(AccessibilityNodeInfo var1, View var2) {
         var1.setTraversalBefore(var2);
      }

      public void setTraversalBefore(AccessibilityNodeInfo var1, View var2, int var3) {
         var1.setTraversalBefore(var2, var3);
      }
   }

   @RequiresApi(23)
   static class AccessibilityNodeInfoApi23Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi22Impl {
      public Object getActionContextClick() {
         return AccessibilityAction.ACTION_CONTEXT_CLICK;
      }

      public Object getActionScrollDown() {
         return AccessibilityAction.ACTION_SCROLL_DOWN;
      }

      public Object getActionScrollLeft() {
         return AccessibilityAction.ACTION_SCROLL_LEFT;
      }

      public Object getActionScrollRight() {
         return AccessibilityAction.ACTION_SCROLL_RIGHT;
      }

      public Object getActionScrollToPosition() {
         return AccessibilityAction.ACTION_SCROLL_TO_POSITION;
      }

      public Object getActionScrollUp() {
         return AccessibilityAction.ACTION_SCROLL_UP;
      }

      public Object getActionShowOnScreen() {
         return AccessibilityAction.ACTION_SHOW_ON_SCREEN;
      }

      public boolean isContextClickable(AccessibilityNodeInfo var1) {
         return var1.isContextClickable();
      }

      public void setContextClickable(AccessibilityNodeInfo var1, boolean var2) {
         var1.setContextClickable(var2);
      }
   }

   @RequiresApi(24)
   static class AccessibilityNodeInfoApi24Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoApi23Impl {
      public Object getActionSetProgress() {
         return AccessibilityAction.ACTION_SET_PROGRESS;
      }

      public int getDrawingOrder(AccessibilityNodeInfo var1) {
         return var1.getDrawingOrder();
      }

      public boolean isImportantForAccessibility(AccessibilityNodeInfo var1) {
         return var1.isImportantForAccessibility();
      }

      public void setDrawingOrder(AccessibilityNodeInfo var1, int var2) {
         var1.setDrawingOrder(var2);
      }

      public void setImportantForAccessibility(AccessibilityNodeInfo var1, boolean var2) {
         var1.setImportantForAccessibility(var2);
      }
   }

   static class AccessibilityNodeInfoBaseImpl {
      public void addAction(AccessibilityNodeInfo var1, Object var2) {
      }

      public void addChild(AccessibilityNodeInfo var1, View var2, int var3) {
      }

      public boolean canOpenPopup(AccessibilityNodeInfo var1) {
         return false;
      }

      public List findAccessibilityNodeInfosByViewId(AccessibilityNodeInfo var1, String var2) {
         return Collections.emptyList();
      }

      public Object findFocus(AccessibilityNodeInfo var1, int var2) {
         return null;
      }

      public Object focusSearch(AccessibilityNodeInfo var1, int var2) {
         return null;
      }

      public int getAccessibilityActionId(Object var1) {
         return 0;
      }

      public CharSequence getAccessibilityActionLabel(Object var1) {
         return null;
      }

      public Object getActionContextClick() {
         return null;
      }

      public List getActionList(AccessibilityNodeInfo var1) {
         return null;
      }

      public Object getActionScrollDown() {
         return null;
      }

      public Object getActionScrollLeft() {
         return null;
      }

      public Object getActionScrollRight() {
         return null;
      }

      public Object getActionScrollToPosition() {
         return null;
      }

      public Object getActionScrollUp() {
         return null;
      }

      public Object getActionSetProgress() {
         return null;
      }

      public Object getActionShowOnScreen() {
         return null;
      }

      public Object getCollectionInfo(AccessibilityNodeInfo var1) {
         return null;
      }

      public int getCollectionInfoColumnCount(Object var1) {
         return 0;
      }

      public int getCollectionInfoRowCount(Object var1) {
         return 0;
      }

      public int getCollectionInfoSelectionMode(Object var1) {
         return 0;
      }

      public int getCollectionItemColumnIndex(Object var1) {
         return 0;
      }

      public int getCollectionItemColumnSpan(Object var1) {
         return 0;
      }

      public Object getCollectionItemInfo(AccessibilityNodeInfo var1) {
         return null;
      }

      public int getCollectionItemRowIndex(Object var1) {
         return 0;
      }

      public int getCollectionItemRowSpan(Object var1) {
         return 0;
      }

      public int getDrawingOrder(AccessibilityNodeInfo var1) {
         return 0;
      }

      public CharSequence getError(AccessibilityNodeInfo var1) {
         return null;
      }

      public Bundle getExtras(AccessibilityNodeInfo var1) {
         return new Bundle();
      }

      public int getInputType(AccessibilityNodeInfo var1) {
         return 0;
      }

      public Object getLabelFor(AccessibilityNodeInfo var1) {
         return null;
      }

      public Object getLabeledBy(AccessibilityNodeInfo var1) {
         return null;
      }

      public int getLiveRegion(AccessibilityNodeInfo var1) {
         return 0;
      }

      public int getMaxTextLength(AccessibilityNodeInfo var1) {
         return -1;
      }

      public int getMovementGranularities(AccessibilityNodeInfo var1) {
         return 0;
      }

      public Object getRangeInfo(AccessibilityNodeInfo var1) {
         return null;
      }

      public CharSequence getRoleDescription(AccessibilityNodeInfo var1) {
         return null;
      }

      public int getTextSelectionEnd(AccessibilityNodeInfo var1) {
         return -1;
      }

      public int getTextSelectionStart(AccessibilityNodeInfo var1) {
         return -1;
      }

      public Object getTraversalAfter(AccessibilityNodeInfo var1) {
         return null;
      }

      public Object getTraversalBefore(AccessibilityNodeInfo var1) {
         return null;
      }

      public String getViewIdResourceName(AccessibilityNodeInfo var1) {
         return null;
      }

      public Object getWindow(AccessibilityNodeInfo var1) {
         return null;
      }

      public boolean isAccessibilityFocused(AccessibilityNodeInfo var1) {
         return false;
      }

      public boolean isCollectionInfoHierarchical(Object var1) {
         return false;
      }

      public boolean isCollectionItemHeading(Object var1) {
         return false;
      }

      public boolean isCollectionItemSelected(Object var1) {
         return false;
      }

      public boolean isContentInvalid(AccessibilityNodeInfo var1) {
         return false;
      }

      public boolean isContextClickable(AccessibilityNodeInfo var1) {
         return false;
      }

      public boolean isDismissable(AccessibilityNodeInfo var1) {
         return false;
      }

      public boolean isEditable(AccessibilityNodeInfo var1) {
         return false;
      }

      public boolean isImportantForAccessibility(AccessibilityNodeInfo var1) {
         return true;
      }

      public boolean isMultiLine(AccessibilityNodeInfo var1) {
         return false;
      }

      public boolean isVisibleToUser(AccessibilityNodeInfo var1) {
         return false;
      }

      public Object newAccessibilityAction(int var1, CharSequence var2) {
         return null;
      }

      public AccessibilityNodeInfo obtain(View var1, int var2) {
         return null;
      }

      public Object obtainCollectionInfo(int var1, int var2, boolean var3) {
         return null;
      }

      public Object obtainCollectionInfo(int var1, int var2, boolean var3, int var4) {
         return null;
      }

      public Object obtainCollectionItemInfo(int var1, int var2, int var3, int var4, boolean var5) {
         return null;
      }

      public Object obtainCollectionItemInfo(int var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         return null;
      }

      public Object obtainRangeInfo(int var1, float var2, float var3, float var4) {
         return null;
      }

      public boolean performAction(AccessibilityNodeInfo var1, int var2, Bundle var3) {
         return false;
      }

      public boolean refresh(AccessibilityNodeInfo var1) {
         return false;
      }

      public boolean removeAction(AccessibilityNodeInfo var1, Object var2) {
         return false;
      }

      public boolean removeChild(AccessibilityNodeInfo var1, View var2) {
         return false;
      }

      public boolean removeChild(AccessibilityNodeInfo var1, View var2, int var3) {
         return false;
      }

      public void setAccessibilityFocused(AccessibilityNodeInfo var1, boolean var2) {
      }

      public void setCanOpenPopup(AccessibilityNodeInfo var1, boolean var2) {
      }

      public void setCollectionInfo(AccessibilityNodeInfo var1, Object var2) {
      }

      public void setCollectionItemInfo(AccessibilityNodeInfo var1, Object var2) {
      }

      public void setContentInvalid(AccessibilityNodeInfo var1, boolean var2) {
      }

      public void setContextClickable(AccessibilityNodeInfo var1, boolean var2) {
      }

      public void setDismissable(AccessibilityNodeInfo var1, boolean var2) {
      }

      public void setDrawingOrder(AccessibilityNodeInfo var1, int var2) {
      }

      public void setEditable(AccessibilityNodeInfo var1, boolean var2) {
      }

      public void setError(AccessibilityNodeInfo var1, CharSequence var2) {
      }

      public void setImportantForAccessibility(AccessibilityNodeInfo var1, boolean var2) {
      }

      public void setInputType(AccessibilityNodeInfo var1, int var2) {
      }

      public void setLabelFor(AccessibilityNodeInfo var1, View var2) {
      }

      public void setLabelFor(AccessibilityNodeInfo var1, View var2, int var3) {
      }

      public void setLabeledBy(AccessibilityNodeInfo var1, View var2) {
      }

      public void setLabeledBy(AccessibilityNodeInfo var1, View var2, int var3) {
      }

      public void setLiveRegion(AccessibilityNodeInfo var1, int var2) {
      }

      public void setMaxTextLength(AccessibilityNodeInfo var1, int var2) {
      }

      public void setMovementGranularities(AccessibilityNodeInfo var1, int var2) {
      }

      public void setMultiLine(AccessibilityNodeInfo var1, boolean var2) {
      }

      public void setParent(AccessibilityNodeInfo var1, View var2, int var3) {
      }

      public void setRangeInfo(AccessibilityNodeInfo var1, Object var2) {
      }

      public void setRoleDescription(AccessibilityNodeInfo var1, CharSequence var2) {
      }

      public void setSource(AccessibilityNodeInfo var1, View var2, int var3) {
      }

      public void setTextSelection(AccessibilityNodeInfo var1, int var2, int var3) {
      }

      public void setTraversalAfter(AccessibilityNodeInfo var1, View var2) {
      }

      public void setTraversalAfter(AccessibilityNodeInfo var1, View var2, int var3) {
      }

      public void setTraversalBefore(AccessibilityNodeInfo var1, View var2) {
      }

      public void setTraversalBefore(AccessibilityNodeInfo var1, View var2, int var3) {
      }

      public void setViewIdResourceName(AccessibilityNodeInfo var1, String var2) {
      }

      public void setVisibleToUser(AccessibilityNodeInfo var1, boolean var2) {
      }
   }

   public static class CollectionInfoCompat {
      public static final int SELECTION_MODE_MULTIPLE = 2;
      public static final int SELECTION_MODE_NONE = 0;
      public static final int SELECTION_MODE_SINGLE = 1;
      final Object mInfo;

      CollectionInfoCompat(Object var1) {
         this.mInfo = var1;
      }

      public static AccessibilityNodeInfoCompat.CollectionInfoCompat obtain(int var0, int var1, boolean var2) {
         return new AccessibilityNodeInfoCompat.CollectionInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(var0, var1, var2));
      }

      public static AccessibilityNodeInfoCompat.CollectionInfoCompat obtain(int var0, int var1, boolean var2, int var3) {
         return new AccessibilityNodeInfoCompat.CollectionInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(var0, var1, var2, var3));
      }

      public int getColumnCount() {
         return AccessibilityNodeInfoCompat.IMPL.getCollectionInfoColumnCount(this.mInfo);
      }

      public int getRowCount() {
         return AccessibilityNodeInfoCompat.IMPL.getCollectionInfoRowCount(this.mInfo);
      }

      public int getSelectionMode() {
         return AccessibilityNodeInfoCompat.IMPL.getCollectionInfoSelectionMode(this.mInfo);
      }

      public boolean isHierarchical() {
         return AccessibilityNodeInfoCompat.IMPL.isCollectionInfoHierarchical(this.mInfo);
      }
   }

   public static class CollectionItemInfoCompat {
      final Object mInfo;

      CollectionItemInfoCompat(Object var1) {
         this.mInfo = var1;
      }

      public static AccessibilityNodeInfoCompat.CollectionItemInfoCompat obtain(int var0, int var1, int var2, int var3, boolean var4) {
         return new AccessibilityNodeInfoCompat.CollectionItemInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(var0, var1, var2, var3, var4));
      }

      public static AccessibilityNodeInfoCompat.CollectionItemInfoCompat obtain(int var0, int var1, int var2, int var3, boolean var4, boolean var5) {
         return new AccessibilityNodeInfoCompat.CollectionItemInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(var0, var1, var2, var3, var4, var5));
      }

      public int getColumnIndex() {
         return AccessibilityNodeInfoCompat.IMPL.getCollectionItemColumnIndex(this.mInfo);
      }

      public int getColumnSpan() {
         return AccessibilityNodeInfoCompat.IMPL.getCollectionItemColumnSpan(this.mInfo);
      }

      public int getRowIndex() {
         return AccessibilityNodeInfoCompat.IMPL.getCollectionItemRowIndex(this.mInfo);
      }

      public int getRowSpan() {
         return AccessibilityNodeInfoCompat.IMPL.getCollectionItemRowSpan(this.mInfo);
      }

      public boolean isHeading() {
         return AccessibilityNodeInfoCompat.IMPL.isCollectionItemHeading(this.mInfo);
      }

      public boolean isSelected() {
         return AccessibilityNodeInfoCompat.IMPL.isCollectionItemSelected(this.mInfo);
      }
   }

   public static class RangeInfoCompat {
      public static final int RANGE_TYPE_FLOAT = 1;
      public static final int RANGE_TYPE_INT = 0;
      public static final int RANGE_TYPE_PERCENT = 2;
      final Object mInfo;

      RangeInfoCompat(Object var1) {
         this.mInfo = var1;
      }

      public static AccessibilityNodeInfoCompat.RangeInfoCompat obtain(int var0, float var1, float var2, float var3) {
         return new AccessibilityNodeInfoCompat.RangeInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainRangeInfo(var0, var1, var2, var3));
      }

      public float getCurrent() {
         return AccessibilityNodeInfoCompatKitKat.RangeInfo.getCurrent(this.mInfo);
      }

      public float getMax() {
         return AccessibilityNodeInfoCompatKitKat.RangeInfo.getMax(this.mInfo);
      }

      public float getMin() {
         return AccessibilityNodeInfoCompatKitKat.RangeInfo.getMin(this.mInfo);
      }

      public int getType() {
         return AccessibilityNodeInfoCompatKitKat.RangeInfo.getType(this.mInfo);
      }
   }
}
