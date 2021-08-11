/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.View
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionInfo
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo
 *  android.view.accessibility.AccessibilityNodeInfo$RangeInfo
 *  android.view.accessibility.AccessibilityWindowInfo
 */
package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompatKitKat;
import android.support.v4.view.accessibility.AccessibilityWindowInfoCompat;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
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
    static final AccessibilityNodeInfoBaseImpl IMPL = Build.VERSION.SDK_INT >= 24 ? new AccessibilityNodeInfoApi24Impl() : (Build.VERSION.SDK_INT >= 23 ? new AccessibilityNodeInfoApi23Impl() : (Build.VERSION.SDK_INT >= 22 ? new AccessibilityNodeInfoApi22Impl() : (Build.VERSION.SDK_INT >= 21 ? new AccessibilityNodeInfoApi21Impl() : (Build.VERSION.SDK_INT >= 19 ? new AccessibilityNodeInfoApi19Impl() : (Build.VERSION.SDK_INT >= 18 ? new AccessibilityNodeInfoApi18Impl() : (Build.VERSION.SDK_INT >= 17 ? new AccessibilityNodeInfoApi17Impl() : (Build.VERSION.SDK_INT >= 16 ? new AccessibilityNodeInfoApi16Impl() : new AccessibilityNodeInfoBaseImpl())))))));
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    private final AccessibilityNodeInfo mInfo;
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public int mParentVirtualDescendantId = -1;

    private AccessibilityNodeInfoCompat(AccessibilityNodeInfo accessibilityNodeInfo) {
        this.mInfo = accessibilityNodeInfo;
    }

    @Deprecated
    public AccessibilityNodeInfoCompat(Object object) {
        this.mInfo = (AccessibilityNodeInfo)object;
    }

    private static String getActionSymbolicName(int n) {
        switch (n) {
            default: {
                return "ACTION_UNKNOWN";
            }
            case 131072: {
                return "ACTION_SET_SELECTION";
            }
            case 65536: {
                return "ACTION_CUT";
            }
            case 32768: {
                return "ACTION_PASTE";
            }
            case 16384: {
                return "ACTION_COPY";
            }
            case 8192: {
                return "ACTION_SCROLL_BACKWARD";
            }
            case 4096: {
                return "ACTION_SCROLL_FORWARD";
            }
            case 2048: {
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            }
            case 1024: {
                return "ACTION_NEXT_HTML_ELEMENT";
            }
            case 512: {
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            }
            case 256: {
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            }
            case 128: {
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            }
            case 64: {
                return "ACTION_ACCESSIBILITY_FOCUS";
            }
            case 32: {
                return "ACTION_LONG_CLICK";
            }
            case 16: {
                return "ACTION_CLICK";
            }
            case 8: {
                return "ACTION_CLEAR_SELECTION";
            }
            case 4: {
                return "ACTION_SELECT";
            }
            case 2: {
                return "ACTION_CLEAR_FOCUS";
            }
            case 1: 
        }
        return "ACTION_FOCUS";
    }

    public static AccessibilityNodeInfoCompat obtain() {
        return AccessibilityNodeInfoCompat.wrap(AccessibilityNodeInfo.obtain());
    }

    public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return AccessibilityNodeInfoCompat.wrap(AccessibilityNodeInfo.obtain((AccessibilityNodeInfo)accessibilityNodeInfoCompat.mInfo));
    }

    public static AccessibilityNodeInfoCompat obtain(View view) {
        return AccessibilityNodeInfoCompat.wrap(AccessibilityNodeInfo.obtain((View)view));
    }

    public static AccessibilityNodeInfoCompat obtain(View view, int n) {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance((Object)IMPL.obtain(view, n));
    }

    public static AccessibilityNodeInfoCompat wrap(@NonNull AccessibilityNodeInfo accessibilityNodeInfo) {
        return new AccessibilityNodeInfoCompat(accessibilityNodeInfo);
    }

    static AccessibilityNodeInfoCompat wrapNonNullInstance(Object object) {
        if (object != null) {
            return new AccessibilityNodeInfoCompat(object);
        }
        return null;
    }

    public void addAction(int n) {
        this.mInfo.addAction(n);
    }

    public void addAction(AccessibilityActionCompat accessibilityActionCompat) {
        IMPL.addAction(this.mInfo, accessibilityActionCompat.mAction);
    }

    public void addChild(View view) {
        this.mInfo.addChild(view);
    }

    public void addChild(View view, int n) {
        IMPL.addChild(this.mInfo, view, n);
    }

    public boolean canOpenPopup() {
        return IMPL.canOpenPopup(this.mInfo);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        object = (AccessibilityNodeInfoCompat)object;
        AccessibilityNodeInfo accessibilityNodeInfo = this.mInfo;
        if (accessibilityNodeInfo == null) {
            if (object.mInfo != null) {
                return false;
            }
            return true;
        }
        if (!accessibilityNodeInfo.equals((Object)object.mInfo)) {
            return false;
        }
        return true;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String object) {
        ArrayList<AccessibilityNodeInfoCompat> arrayList = new ArrayList<AccessibilityNodeInfoCompat>();
        object = this.mInfo.findAccessibilityNodeInfosByText((String)object);
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            arrayList.add(AccessibilityNodeInfoCompat.wrap((AccessibilityNodeInfo)object.get(i)));
        }
        return arrayList;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId(String object) {
        List<AccessibilityNodeInfo> list = IMPL.findAccessibilityNodeInfosByViewId(this.mInfo, (String)object);
        if (list != null) {
            object = new ArrayList();
            list = list.iterator();
            while (list.hasNext()) {
                object.add(AccessibilityNodeInfoCompat.wrap((AccessibilityNodeInfo)list.next()));
            }
            return object;
        }
        return Collections.emptyList();
    }

    public AccessibilityNodeInfoCompat findFocus(int n) {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.findFocus(this.mInfo, n));
    }

    public AccessibilityNodeInfoCompat focusSearch(int n) {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.focusSearch(this.mInfo, n));
    }

    public List<AccessibilityActionCompat> getActionList() {
        List<Object> list = IMPL.getActionList(this.mInfo);
        if (list != null) {
            ArrayList<AccessibilityActionCompat> arrayList = new ArrayList<AccessibilityActionCompat>();
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                arrayList.add(new AccessibilityActionCompat(list.get(i)));
            }
            return arrayList;
        }
        return Collections.emptyList();
    }

    public int getActions() {
        return this.mInfo.getActions();
    }

    public void getBoundsInParent(Rect rect) {
        this.mInfo.getBoundsInParent(rect);
    }

    public void getBoundsInScreen(Rect rect) {
        this.mInfo.getBoundsInScreen(rect);
    }

    public AccessibilityNodeInfoCompat getChild(int n) {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance((Object)this.mInfo.getChild(n));
    }

    public int getChildCount() {
        return this.mInfo.getChildCount();
    }

    public CharSequence getClassName() {
        return this.mInfo.getClassName();
    }

    public CollectionInfoCompat getCollectionInfo() {
        Object object = IMPL.getCollectionInfo(this.mInfo);
        if (object == null) {
            return null;
        }
        return new CollectionInfoCompat(object);
    }

    public CollectionItemInfoCompat getCollectionItemInfo() {
        Object object = IMPL.getCollectionItemInfo(this.mInfo);
        if (object == null) {
            return null;
        }
        return new CollectionItemInfoCompat(object);
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
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getLabelFor(this.mInfo));
    }

    public AccessibilityNodeInfoCompat getLabeledBy() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getLabeledBy(this.mInfo));
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
        return AccessibilityNodeInfoCompat.wrapNonNullInstance((Object)this.mInfo.getParent());
    }

    public RangeInfoCompat getRangeInfo() {
        Object object = IMPL.getRangeInfo(this.mInfo);
        if (object == null) {
            return null;
        }
        return new RangeInfoCompat(object);
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
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getTraversalAfter(this.mInfo));
    }

    public AccessibilityNodeInfoCompat getTraversalBefore() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getTraversalBefore(this.mInfo));
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
        AccessibilityNodeInfo accessibilityNodeInfo = this.mInfo;
        if (accessibilityNodeInfo == null) {
            return 0;
        }
        return accessibilityNodeInfo.hashCode();
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

    public boolean performAction(int n) {
        return this.mInfo.performAction(n);
    }

    public boolean performAction(int n, Bundle bundle) {
        return IMPL.performAction(this.mInfo, n, bundle);
    }

    public void recycle() {
        this.mInfo.recycle();
    }

    public boolean refresh() {
        return IMPL.refresh(this.mInfo);
    }

    public boolean removeAction(AccessibilityActionCompat accessibilityActionCompat) {
        return IMPL.removeAction(this.mInfo, accessibilityActionCompat.mAction);
    }

    public boolean removeChild(View view) {
        return IMPL.removeChild(this.mInfo, view);
    }

    public boolean removeChild(View view, int n) {
        return IMPL.removeChild(this.mInfo, view, n);
    }

    public void setAccessibilityFocused(boolean bl) {
        IMPL.setAccessibilityFocused(this.mInfo, bl);
    }

    public void setBoundsInParent(Rect rect) {
        this.mInfo.setBoundsInParent(rect);
    }

    public void setBoundsInScreen(Rect rect) {
        this.mInfo.setBoundsInScreen(rect);
    }

    public void setCanOpenPopup(boolean bl) {
        IMPL.setCanOpenPopup(this.mInfo, bl);
    }

    public void setCheckable(boolean bl) {
        this.mInfo.setCheckable(bl);
    }

    public void setChecked(boolean bl) {
        this.mInfo.setChecked(bl);
    }

    public void setClassName(CharSequence charSequence) {
        this.mInfo.setClassName(charSequence);
    }

    public void setClickable(boolean bl) {
        this.mInfo.setClickable(bl);
    }

    public void setCollectionInfo(Object object) {
        IMPL.setCollectionInfo(this.mInfo, ((CollectionInfoCompat)object).mInfo);
    }

    public void setCollectionItemInfo(Object object) {
        IMPL.setCollectionItemInfo(this.mInfo, ((CollectionItemInfoCompat)object).mInfo);
    }

    public void setContentDescription(CharSequence charSequence) {
        this.mInfo.setContentDescription(charSequence);
    }

    public void setContentInvalid(boolean bl) {
        IMPL.setContentInvalid(this.mInfo, bl);
    }

    public void setContextClickable(boolean bl) {
        IMPL.setContextClickable(this.mInfo, bl);
    }

    public void setDismissable(boolean bl) {
        IMPL.setDismissable(this.mInfo, bl);
    }

    public void setDrawingOrder(int n) {
        IMPL.setDrawingOrder(this.mInfo, n);
    }

    public void setEditable(boolean bl) {
        IMPL.setEditable(this.mInfo, bl);
    }

    public void setEnabled(boolean bl) {
        this.mInfo.setEnabled(bl);
    }

    public void setError(CharSequence charSequence) {
        IMPL.setError(this.mInfo, charSequence);
    }

    public void setFocusable(boolean bl) {
        this.mInfo.setFocusable(bl);
    }

    public void setFocused(boolean bl) {
        this.mInfo.setFocused(bl);
    }

    public void setImportantForAccessibility(boolean bl) {
        IMPL.setImportantForAccessibility(this.mInfo, bl);
    }

    public void setInputType(int n) {
        IMPL.setInputType(this.mInfo, n);
    }

    public void setLabelFor(View view) {
        IMPL.setLabelFor(this.mInfo, view);
    }

    public void setLabelFor(View view, int n) {
        IMPL.setLabelFor(this.mInfo, view, n);
    }

    public void setLabeledBy(View view) {
        IMPL.setLabeledBy(this.mInfo, view);
    }

    public void setLabeledBy(View view, int n) {
        IMPL.setLabeledBy(this.mInfo, view, n);
    }

    public void setLiveRegion(int n) {
        IMPL.setLiveRegion(this.mInfo, n);
    }

    public void setLongClickable(boolean bl) {
        this.mInfo.setLongClickable(bl);
    }

    public void setMaxTextLength(int n) {
        IMPL.setMaxTextLength(this.mInfo, n);
    }

    public void setMovementGranularities(int n) {
        IMPL.setMovementGranularities(this.mInfo, n);
    }

    public void setMultiLine(boolean bl) {
        IMPL.setMultiLine(this.mInfo, bl);
    }

    public void setPackageName(CharSequence charSequence) {
        this.mInfo.setPackageName(charSequence);
    }

    public void setParent(View view) {
        this.mInfo.setParent(view);
    }

    public void setParent(View view, int n) {
        this.mParentVirtualDescendantId = n;
        IMPL.setParent(this.mInfo, view, n);
    }

    public void setPassword(boolean bl) {
        this.mInfo.setPassword(bl);
    }

    public void setRangeInfo(RangeInfoCompat rangeInfoCompat) {
        IMPL.setRangeInfo(this.mInfo, rangeInfoCompat.mInfo);
    }

    public void setRoleDescription(@Nullable CharSequence charSequence) {
        IMPL.setRoleDescription(this.mInfo, charSequence);
    }

    public void setScrollable(boolean bl) {
        this.mInfo.setScrollable(bl);
    }

    public void setSelected(boolean bl) {
        this.mInfo.setSelected(bl);
    }

    public void setSource(View view) {
        this.mInfo.setSource(view);
    }

    public void setSource(View view, int n) {
        IMPL.setSource(this.mInfo, view, n);
    }

    public void setText(CharSequence charSequence) {
        this.mInfo.setText(charSequence);
    }

    public void setTextSelection(int n, int n2) {
        IMPL.setTextSelection(this.mInfo, n, n2);
    }

    public void setTraversalAfter(View view) {
        IMPL.setTraversalAfter(this.mInfo, view);
    }

    public void setTraversalAfter(View view, int n) {
        IMPL.setTraversalAfter(this.mInfo, view, n);
    }

    public void setTraversalBefore(View view) {
        IMPL.setTraversalBefore(this.mInfo, view);
    }

    public void setTraversalBefore(View view, int n) {
        IMPL.setTraversalBefore(this.mInfo, view, n);
    }

    public void setViewIdResourceName(String string2) {
        IMPL.setViewIdResourceName(this.mInfo, string2);
    }

    public void setVisibleToUser(boolean bl) {
        IMPL.setVisibleToUser(this.mInfo, bl);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        Object object = new Rect();
        this.getBoundsInParent((Rect)object);
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("; boundsInParent: ");
        stringBuilder2.append(object);
        stringBuilder.append(stringBuilder2.toString());
        this.getBoundsInScreen((Rect)object);
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("; boundsInScreen: ");
        stringBuilder2.append(object);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder.append("; packageName: ");
        stringBuilder.append(this.getPackageName());
        stringBuilder.append("; className: ");
        stringBuilder.append(this.getClassName());
        stringBuilder.append("; text: ");
        stringBuilder.append(this.getText());
        stringBuilder.append("; contentDescription: ");
        stringBuilder.append(this.getContentDescription());
        stringBuilder.append("; viewId: ");
        stringBuilder.append(this.getViewIdResourceName());
        stringBuilder.append("; checkable: ");
        stringBuilder.append(this.isCheckable());
        stringBuilder.append("; checked: ");
        stringBuilder.append(this.isChecked());
        stringBuilder.append("; focusable: ");
        stringBuilder.append(this.isFocusable());
        stringBuilder.append("; focused: ");
        stringBuilder.append(this.isFocused());
        stringBuilder.append("; selected: ");
        stringBuilder.append(this.isSelected());
        stringBuilder.append("; clickable: ");
        stringBuilder.append(this.isClickable());
        stringBuilder.append("; longClickable: ");
        stringBuilder.append(this.isLongClickable());
        stringBuilder.append("; enabled: ");
        stringBuilder.append(this.isEnabled());
        stringBuilder.append("; password: ");
        stringBuilder.append(this.isPassword());
        object = new StringBuilder();
        object.append("; scrollable: ");
        object.append(this.isScrollable());
        stringBuilder.append(object.toString());
        stringBuilder.append("; [");
        int n = this.getActions();
        while (n != 0) {
            int n2 = 1 << Integer.numberOfTrailingZeros(n);
            stringBuilder.append(AccessibilityNodeInfoCompat.getActionSymbolicName(n2));
            if ((n &= ~ n2) == 0) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public AccessibilityNodeInfo unwrap() {
        return this.mInfo;
    }

    public static class AccessibilityActionCompat {
        public static final AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_SELECTION;
        public static final AccessibilityActionCompat ACTION_CLICK;
        public static final AccessibilityActionCompat ACTION_COLLAPSE;
        public static final AccessibilityActionCompat ACTION_CONTEXT_CLICK;
        public static final AccessibilityActionCompat ACTION_COPY;
        public static final AccessibilityActionCompat ACTION_CUT;
        public static final AccessibilityActionCompat ACTION_DISMISS;
        public static final AccessibilityActionCompat ACTION_EXPAND;
        public static final AccessibilityActionCompat ACTION_FOCUS;
        public static final AccessibilityActionCompat ACTION_LONG_CLICK;
        public static final AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_PASTE;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_SCROLL_BACKWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_DOWN;
        public static final AccessibilityActionCompat ACTION_SCROLL_FORWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_LEFT;
        public static final AccessibilityActionCompat ACTION_SCROLL_RIGHT;
        public static final AccessibilityActionCompat ACTION_SCROLL_TO_POSITION;
        public static final AccessibilityActionCompat ACTION_SCROLL_UP;
        public static final AccessibilityActionCompat ACTION_SELECT;
        public static final AccessibilityActionCompat ACTION_SET_PROGRESS;
        public static final AccessibilityActionCompat ACTION_SET_SELECTION;
        public static final AccessibilityActionCompat ACTION_SET_TEXT;
        public static final AccessibilityActionCompat ACTION_SHOW_ON_SCREEN;
        final Object mAction;

        static {
            ACTION_FOCUS = new AccessibilityActionCompat(1, null);
            ACTION_CLEAR_FOCUS = new AccessibilityActionCompat(2, null);
            ACTION_SELECT = new AccessibilityActionCompat(4, null);
            ACTION_CLEAR_SELECTION = new AccessibilityActionCompat(8, null);
            ACTION_CLICK = new AccessibilityActionCompat(16, null);
            ACTION_LONG_CLICK = new AccessibilityActionCompat(32, null);
            ACTION_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(64, null);
            ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(128, null);
            ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(256, null);
            ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(512, null);
            ACTION_NEXT_HTML_ELEMENT = new AccessibilityActionCompat(1024, null);
            ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityActionCompat(2048, null);
            ACTION_SCROLL_FORWARD = new AccessibilityActionCompat(4096, null);
            ACTION_SCROLL_BACKWARD = new AccessibilityActionCompat(8192, null);
            ACTION_COPY = new AccessibilityActionCompat(16384, null);
            ACTION_PASTE = new AccessibilityActionCompat(32768, null);
            ACTION_CUT = new AccessibilityActionCompat(65536, null);
            ACTION_SET_SELECTION = new AccessibilityActionCompat(131072, null);
            ACTION_EXPAND = new AccessibilityActionCompat(262144, null);
            ACTION_COLLAPSE = new AccessibilityActionCompat(524288, null);
            ACTION_DISMISS = new AccessibilityActionCompat(1048576, null);
            ACTION_SET_TEXT = new AccessibilityActionCompat(2097152, null);
            ACTION_SHOW_ON_SCREEN = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionShowOnScreen());
            ACTION_SCROLL_TO_POSITION = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollToPosition());
            ACTION_SCROLL_UP = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollUp());
            ACTION_SCROLL_LEFT = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollLeft());
            ACTION_SCROLL_DOWN = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollDown());
            ACTION_SCROLL_RIGHT = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollRight());
            ACTION_CONTEXT_CLICK = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionContextClick());
            ACTION_SET_PROGRESS = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionSetProgress());
        }

        public AccessibilityActionCompat(int n, CharSequence charSequence) {
            this(AccessibilityNodeInfoCompat.IMPL.newAccessibilityAction(n, charSequence));
        }

        AccessibilityActionCompat(Object object) {
            this.mAction = object;
        }

        public int getId() {
            return AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionId(this.mAction);
        }

        public CharSequence getLabel() {
            return AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionLabel(this.mAction);
        }
    }

    @RequiresApi(value=16)
    static class AccessibilityNodeInfoApi16Impl
    extends AccessibilityNodeInfoBaseImpl {
        AccessibilityNodeInfoApi16Impl() {
        }

        @Override
        public void addChild(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            accessibilityNodeInfo.addChild(view, n);
        }

        @Override
        public Object findFocus(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            return accessibilityNodeInfo.findFocus(n);
        }

        @Override
        public Object focusSearch(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            return accessibilityNodeInfo.focusSearch(n);
        }

        @Override
        public int getMovementGranularities(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getMovementGranularities();
        }

        @Override
        public boolean isAccessibilityFocused(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isAccessibilityFocused();
        }

        @Override
        public boolean isVisibleToUser(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isVisibleToUser();
        }

        @Override
        public AccessibilityNodeInfo obtain(View view, int n) {
            return AccessibilityNodeInfo.obtain((View)view, (int)n);
        }

        @Override
        public boolean performAction(AccessibilityNodeInfo accessibilityNodeInfo, int n, Bundle bundle) {
            return accessibilityNodeInfo.performAction(n, bundle);
        }

        @Override
        public void setAccessibilityFocused(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setAccessibilityFocused(bl);
        }

        @Override
        public void setMovementGranularities(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            accessibilityNodeInfo.setMovementGranularities(n);
        }

        @Override
        public void setParent(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            accessibilityNodeInfo.setParent(view, n);
        }

        @Override
        public void setSource(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            accessibilityNodeInfo.setSource(view, n);
        }

        @Override
        public void setVisibleToUser(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setVisibleToUser(bl);
        }
    }

    @RequiresApi(value=17)
    static class AccessibilityNodeInfoApi17Impl
    extends AccessibilityNodeInfoApi16Impl {
        AccessibilityNodeInfoApi17Impl() {
        }

        @Override
        public Object getLabelFor(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getLabelFor();
        }

        @Override
        public Object getLabeledBy(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getLabeledBy();
        }

        @Override
        public void setLabelFor(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
            accessibilityNodeInfo.setLabelFor(view);
        }

        @Override
        public void setLabelFor(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            accessibilityNodeInfo.setLabelFor(view, n);
        }

        @Override
        public void setLabeledBy(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
            accessibilityNodeInfo.setLabeledBy(view);
        }

        @Override
        public void setLabeledBy(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            accessibilityNodeInfo.setLabeledBy(view, n);
        }
    }

    @RequiresApi(value=18)
    static class AccessibilityNodeInfoApi18Impl
    extends AccessibilityNodeInfoApi17Impl {
        AccessibilityNodeInfoApi18Impl() {
        }

        @Override
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(AccessibilityNodeInfo accessibilityNodeInfo, String string2) {
            return accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(string2);
        }

        @Override
        public int getTextSelectionEnd(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getTextSelectionEnd();
        }

        @Override
        public int getTextSelectionStart(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getTextSelectionStart();
        }

        @Override
        public String getViewIdResourceName(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getViewIdResourceName();
        }

        @Override
        public boolean isEditable(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isEditable();
        }

        @Override
        public boolean refresh(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.refresh();
        }

        @Override
        public void setEditable(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setEditable(bl);
        }

        @Override
        public void setTextSelection(AccessibilityNodeInfo accessibilityNodeInfo, int n, int n2) {
            accessibilityNodeInfo.setTextSelection(n, n2);
        }

        @Override
        public void setViewIdResourceName(AccessibilityNodeInfo accessibilityNodeInfo, String string2) {
            accessibilityNodeInfo.setViewIdResourceName(string2);
        }
    }

    @RequiresApi(value=19)
    static class AccessibilityNodeInfoApi19Impl
    extends AccessibilityNodeInfoApi18Impl {
        private static final String ROLE_DESCRIPTION_KEY = "AccessibilityNodeInfo.roleDescription";

        AccessibilityNodeInfoApi19Impl() {
        }

        @Override
        public boolean canOpenPopup(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.canOpenPopup();
        }

        @Override
        public Object getCollectionInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getCollectionInfo();
        }

        @Override
        public int getCollectionInfoColumnCount(Object object) {
            return ((AccessibilityNodeInfo.CollectionInfo)object).getColumnCount();
        }

        @Override
        public int getCollectionInfoRowCount(Object object) {
            return ((AccessibilityNodeInfo.CollectionInfo)object).getRowCount();
        }

        @Override
        public int getCollectionItemColumnIndex(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).getColumnIndex();
        }

        @Override
        public int getCollectionItemColumnSpan(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).getColumnSpan();
        }

        @Override
        public Object getCollectionItemInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getCollectionItemInfo();
        }

        @Override
        public int getCollectionItemRowIndex(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).getRowIndex();
        }

        @Override
        public int getCollectionItemRowSpan(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).getRowSpan();
        }

        @Override
        public Bundle getExtras(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getExtras();
        }

        @Override
        public int getInputType(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getInputType();
        }

        @Override
        public int getLiveRegion(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getLiveRegion();
        }

        @Override
        public Object getRangeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getRangeInfo();
        }

        @Override
        public CharSequence getRoleDescription(AccessibilityNodeInfo accessibilityNodeInfo) {
            return this.getExtras(accessibilityNodeInfo).getCharSequence("AccessibilityNodeInfo.roleDescription");
        }

        @Override
        public boolean isCollectionInfoHierarchical(Object object) {
            return ((AccessibilityNodeInfo.CollectionInfo)object).isHierarchical();
        }

        @Override
        public boolean isCollectionItemHeading(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).isHeading();
        }

        @Override
        public boolean isContentInvalid(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isContentInvalid();
        }

        @Override
        public boolean isDismissable(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isDismissable();
        }

        @Override
        public boolean isMultiLine(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isMultiLine();
        }

        @Override
        public Object obtainCollectionInfo(int n, int n2, boolean bl) {
            return AccessibilityNodeInfo.CollectionInfo.obtain((int)n, (int)n2, (boolean)bl);
        }

        @Override
        public Object obtainCollectionInfo(int n, int n2, boolean bl, int n3) {
            return AccessibilityNodeInfo.CollectionInfo.obtain((int)n, (int)n2, (boolean)bl);
        }

        @Override
        public Object obtainCollectionItemInfo(int n, int n2, int n3, int n4, boolean bl) {
            return AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n, (int)n2, (int)n3, (int)n4, (boolean)bl);
        }

        @Override
        public Object obtainCollectionItemInfo(int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
            return AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n, (int)n2, (int)n3, (int)n4, (boolean)bl);
        }

        @Override
        public Object obtainRangeInfo(int n, float f, float f2, float f3) {
            return AccessibilityNodeInfo.RangeInfo.obtain((int)n, (float)f, (float)f2, (float)f3);
        }

        @Override
        public void setCanOpenPopup(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setCanOpenPopup(bl);
        }

        @Override
        public void setCollectionInfo(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
            accessibilityNodeInfo.setCollectionInfo((AccessibilityNodeInfo.CollectionInfo)object);
        }

        @Override
        public void setCollectionItemInfo(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
            accessibilityNodeInfo.setCollectionItemInfo((AccessibilityNodeInfo.CollectionItemInfo)object);
        }

        @Override
        public void setContentInvalid(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setContentInvalid(bl);
        }

        @Override
        public void setDismissable(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setDismissable(bl);
        }

        @Override
        public void setInputType(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            accessibilityNodeInfo.setInputType(n);
        }

        @Override
        public void setLiveRegion(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            accessibilityNodeInfo.setLiveRegion(n);
        }

        @Override
        public void setMultiLine(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setMultiLine(bl);
        }

        @Override
        public void setRangeInfo(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
            accessibilityNodeInfo.setRangeInfo((AccessibilityNodeInfo.RangeInfo)object);
        }

        @Override
        public void setRoleDescription(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence charSequence) {
            this.getExtras(accessibilityNodeInfo).putCharSequence("AccessibilityNodeInfo.roleDescription", charSequence);
        }
    }

    @RequiresApi(value=21)
    static class AccessibilityNodeInfoApi21Impl
    extends AccessibilityNodeInfoApi19Impl {
        AccessibilityNodeInfoApi21Impl() {
        }

        @Override
        public void addAction(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
            accessibilityNodeInfo.addAction((AccessibilityNodeInfo.AccessibilityAction)object);
        }

        @Override
        public int getAccessibilityActionId(Object object) {
            return ((AccessibilityNodeInfo.AccessibilityAction)object).getId();
        }

        @Override
        public CharSequence getAccessibilityActionLabel(Object object) {
            return ((AccessibilityNodeInfo.AccessibilityAction)object).getLabel();
        }

        @Override
        public List<Object> getActionList(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getActionList();
        }

        @Override
        public int getCollectionInfoSelectionMode(Object object) {
            return ((AccessibilityNodeInfo.CollectionInfo)object).getSelectionMode();
        }

        @Override
        public CharSequence getError(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getError();
        }

        @Override
        public int getMaxTextLength(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getMaxTextLength();
        }

        @Override
        public Object getWindow(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getWindow();
        }

        @Override
        public boolean isCollectionItemSelected(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).isSelected();
        }

        @Override
        public Object newAccessibilityAction(int n, CharSequence charSequence) {
            return new AccessibilityNodeInfo.AccessibilityAction(n, charSequence);
        }

        @Override
        public Object obtainCollectionInfo(int n, int n2, boolean bl, int n3) {
            return AccessibilityNodeInfo.CollectionInfo.obtain((int)n, (int)n2, (boolean)bl, (int)n3);
        }

        @Override
        public Object obtainCollectionItemInfo(int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
            return AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n, (int)n2, (int)n3, (int)n4, (boolean)bl, (boolean)bl2);
        }

        @Override
        public boolean removeAction(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
            return accessibilityNodeInfo.removeAction((AccessibilityNodeInfo.AccessibilityAction)object);
        }

        @Override
        public boolean removeChild(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
            return accessibilityNodeInfo.removeChild(view);
        }

        @Override
        public boolean removeChild(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            return accessibilityNodeInfo.removeChild(view, n);
        }

        @Override
        public void setError(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence charSequence) {
            accessibilityNodeInfo.setError(charSequence);
        }

        @Override
        public void setMaxTextLength(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            accessibilityNodeInfo.setMaxTextLength(n);
        }
    }

    @RequiresApi(value=22)
    static class AccessibilityNodeInfoApi22Impl
    extends AccessibilityNodeInfoApi21Impl {
        AccessibilityNodeInfoApi22Impl() {
        }

        @Override
        public Object getTraversalAfter(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getTraversalAfter();
        }

        @Override
        public Object getTraversalBefore(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getTraversalBefore();
        }

        @Override
        public void setTraversalAfter(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
            accessibilityNodeInfo.setTraversalAfter(view);
        }

        @Override
        public void setTraversalAfter(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            accessibilityNodeInfo.setTraversalAfter(view, n);
        }

        @Override
        public void setTraversalBefore(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
            accessibilityNodeInfo.setTraversalBefore(view);
        }

        @Override
        public void setTraversalBefore(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            accessibilityNodeInfo.setTraversalBefore(view, n);
        }
    }

    @RequiresApi(value=23)
    static class AccessibilityNodeInfoApi23Impl
    extends AccessibilityNodeInfoApi22Impl {
        AccessibilityNodeInfoApi23Impl() {
        }

        @Override
        public Object getActionContextClick() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK;
        }

        @Override
        public Object getActionScrollDown() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN;
        }

        @Override
        public Object getActionScrollLeft() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT;
        }

        @Override
        public Object getActionScrollRight() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT;
        }

        @Override
        public Object getActionScrollToPosition() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION;
        }

        @Override
        public Object getActionScrollUp() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP;
        }

        @Override
        public Object getActionShowOnScreen() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN;
        }

        @Override
        public boolean isContextClickable(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isContextClickable();
        }

        @Override
        public void setContextClickable(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setContextClickable(bl);
        }
    }

    @RequiresApi(value=24)
    static class AccessibilityNodeInfoApi24Impl
    extends AccessibilityNodeInfoApi23Impl {
        AccessibilityNodeInfoApi24Impl() {
        }

        @Override
        public Object getActionSetProgress() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS;
        }

        @Override
        public int getDrawingOrder(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getDrawingOrder();
        }

        @Override
        public boolean isImportantForAccessibility(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isImportantForAccessibility();
        }

        @Override
        public void setDrawingOrder(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            accessibilityNodeInfo.setDrawingOrder(n);
        }

        @Override
        public void setImportantForAccessibility(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
            accessibilityNodeInfo.setImportantForAccessibility(bl);
        }
    }

    static class AccessibilityNodeInfoBaseImpl {
        AccessibilityNodeInfoBaseImpl() {
        }

        public void addAction(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
        }

        public void addChild(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
        }

        public boolean canOpenPopup(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(AccessibilityNodeInfo accessibilityNodeInfo, String string2) {
            return Collections.emptyList();
        }

        public Object findFocus(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            return null;
        }

        public Object focusSearch(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
            return null;
        }

        public int getAccessibilityActionId(Object object) {
            return 0;
        }

        public CharSequence getAccessibilityActionLabel(Object object) {
            return null;
        }

        public Object getActionContextClick() {
            return null;
        }

        public List<Object> getActionList(AccessibilityNodeInfo accessibilityNodeInfo) {
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

        public Object getCollectionInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public int getCollectionInfoColumnCount(Object object) {
            return 0;
        }

        public int getCollectionInfoRowCount(Object object) {
            return 0;
        }

        public int getCollectionInfoSelectionMode(Object object) {
            return 0;
        }

        public int getCollectionItemColumnIndex(Object object) {
            return 0;
        }

        public int getCollectionItemColumnSpan(Object object) {
            return 0;
        }

        public Object getCollectionItemInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public int getCollectionItemRowIndex(Object object) {
            return 0;
        }

        public int getCollectionItemRowSpan(Object object) {
            return 0;
        }

        public int getDrawingOrder(AccessibilityNodeInfo accessibilityNodeInfo) {
            return 0;
        }

        public CharSequence getError(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public Bundle getExtras(AccessibilityNodeInfo accessibilityNodeInfo) {
            return new Bundle();
        }

        public int getInputType(AccessibilityNodeInfo accessibilityNodeInfo) {
            return 0;
        }

        public Object getLabelFor(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public Object getLabeledBy(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public int getLiveRegion(AccessibilityNodeInfo accessibilityNodeInfo) {
            return 0;
        }

        public int getMaxTextLength(AccessibilityNodeInfo accessibilityNodeInfo) {
            return -1;
        }

        public int getMovementGranularities(AccessibilityNodeInfo accessibilityNodeInfo) {
            return 0;
        }

        public Object getRangeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public CharSequence getRoleDescription(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public int getTextSelectionEnd(AccessibilityNodeInfo accessibilityNodeInfo) {
            return -1;
        }

        public int getTextSelectionStart(AccessibilityNodeInfo accessibilityNodeInfo) {
            return -1;
        }

        public Object getTraversalAfter(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public Object getTraversalBefore(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public String getViewIdResourceName(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public Object getWindow(AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }

        public boolean isAccessibilityFocused(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public boolean isCollectionInfoHierarchical(Object object) {
            return false;
        }

        public boolean isCollectionItemHeading(Object object) {
            return false;
        }

        public boolean isCollectionItemSelected(Object object) {
            return false;
        }

        public boolean isContentInvalid(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public boolean isContextClickable(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public boolean isDismissable(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public boolean isEditable(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public boolean isImportantForAccessibility(AccessibilityNodeInfo accessibilityNodeInfo) {
            return true;
        }

        public boolean isMultiLine(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public boolean isVisibleToUser(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public Object newAccessibilityAction(int n, CharSequence charSequence) {
            return null;
        }

        public AccessibilityNodeInfo obtain(View view, int n) {
            return null;
        }

        public Object obtainCollectionInfo(int n, int n2, boolean bl) {
            return null;
        }

        public Object obtainCollectionInfo(int n, int n2, boolean bl, int n3) {
            return null;
        }

        public Object obtainCollectionItemInfo(int n, int n2, int n3, int n4, boolean bl) {
            return null;
        }

        public Object obtainCollectionItemInfo(int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
            return null;
        }

        public Object obtainRangeInfo(int n, float f, float f2, float f3) {
            return null;
        }

        public boolean performAction(AccessibilityNodeInfo accessibilityNodeInfo, int n, Bundle bundle) {
            return false;
        }

        public boolean refresh(AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }

        public boolean removeAction(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
            return false;
        }

        public boolean removeChild(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
            return false;
        }

        public boolean removeChild(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
            return false;
        }

        public void setAccessibilityFocused(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }

        public void setCanOpenPopup(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }

        public void setCollectionInfo(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
        }

        public void setCollectionItemInfo(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
        }

        public void setContentInvalid(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }

        public void setContextClickable(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }

        public void setDismissable(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }

        public void setDrawingOrder(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
        }

        public void setEditable(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }

        public void setError(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence charSequence) {
        }

        public void setImportantForAccessibility(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }

        public void setInputType(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
        }

        public void setLabelFor(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
        }

        public void setLabelFor(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
        }

        public void setLabeledBy(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
        }

        public void setLabeledBy(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
        }

        public void setLiveRegion(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
        }

        public void setMaxTextLength(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
        }

        public void setMovementGranularities(AccessibilityNodeInfo accessibilityNodeInfo, int n) {
        }

        public void setMultiLine(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }

        public void setParent(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
        }

        public void setRangeInfo(AccessibilityNodeInfo accessibilityNodeInfo, Object object) {
        }

        public void setRoleDescription(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence charSequence) {
        }

        public void setSource(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
        }

        public void setTextSelection(AccessibilityNodeInfo accessibilityNodeInfo, int n, int n2) {
        }

        public void setTraversalAfter(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
        }

        public void setTraversalAfter(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
        }

        public void setTraversalBefore(AccessibilityNodeInfo accessibilityNodeInfo, View view) {
        }

        public void setTraversalBefore(AccessibilityNodeInfo accessibilityNodeInfo, View view, int n) {
        }

        public void setViewIdResourceName(AccessibilityNodeInfo accessibilityNodeInfo, String string2) {
        }

        public void setVisibleToUser(AccessibilityNodeInfo accessibilityNodeInfo, boolean bl) {
        }
    }

    public static class CollectionInfoCompat {
        public static final int SELECTION_MODE_MULTIPLE = 2;
        public static final int SELECTION_MODE_NONE = 0;
        public static final int SELECTION_MODE_SINGLE = 1;
        final Object mInfo;

        CollectionInfoCompat(Object object) {
            this.mInfo = object;
        }

        public static CollectionInfoCompat obtain(int n, int n2, boolean bl) {
            return new CollectionInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(n, n2, bl));
        }

        public static CollectionInfoCompat obtain(int n, int n2, boolean bl, int n3) {
            return new CollectionInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(n, n2, bl, n3));
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

        CollectionItemInfoCompat(Object object) {
            this.mInfo = object;
        }

        public static CollectionItemInfoCompat obtain(int n, int n2, int n3, int n4, boolean bl) {
            return new CollectionItemInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(n, n2, n3, n4, bl));
        }

        public static CollectionItemInfoCompat obtain(int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
            return new CollectionItemInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(n, n2, n3, n4, bl, bl2));
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

        RangeInfoCompat(Object object) {
            this.mInfo = object;
        }

        public static RangeInfoCompat obtain(int n, float f, float f2, float f3) {
            return new RangeInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainRangeInfo(n, f, f2, f3));
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

