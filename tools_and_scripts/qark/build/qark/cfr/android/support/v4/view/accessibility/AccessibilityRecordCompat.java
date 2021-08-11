/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 *  android.view.View
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityRecord
 */
package android.support.v4.view.accessibility;

import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityRecord;
import java.util.List;

public class AccessibilityRecordCompat {
    private static final AccessibilityRecordCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 16 ? new AccessibilityRecordCompatApi16Impl() : (Build.VERSION.SDK_INT >= 15 ? new AccessibilityRecordCompatApi15Impl() : new AccessibilityRecordCompatBaseImpl());
    private final AccessibilityRecord mRecord;

    @Deprecated
    public AccessibilityRecordCompat(Object object) {
        this.mRecord = (AccessibilityRecord)object;
    }

    public static int getMaxScrollX(AccessibilityRecord accessibilityRecord) {
        return IMPL.getMaxScrollX(accessibilityRecord);
    }

    public static int getMaxScrollY(AccessibilityRecord accessibilityRecord) {
        return IMPL.getMaxScrollY(accessibilityRecord);
    }

    @Deprecated
    public static AccessibilityRecordCompat obtain() {
        return new AccessibilityRecordCompat((Object)AccessibilityRecord.obtain());
    }

    @Deprecated
    public static AccessibilityRecordCompat obtain(AccessibilityRecordCompat accessibilityRecordCompat) {
        return new AccessibilityRecordCompat((Object)AccessibilityRecord.obtain((AccessibilityRecord)accessibilityRecordCompat.mRecord));
    }

    public static void setMaxScrollX(AccessibilityRecord accessibilityRecord, int n) {
        IMPL.setMaxScrollX(accessibilityRecord, n);
    }

    public static void setMaxScrollY(AccessibilityRecord accessibilityRecord, int n) {
        IMPL.setMaxScrollY(accessibilityRecord, n);
    }

    public static void setSource(@NonNull AccessibilityRecord accessibilityRecord, View view, int n) {
        IMPL.setSource(accessibilityRecord, view, n);
    }

    @Deprecated
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
        object = (AccessibilityRecordCompat)object;
        AccessibilityRecord accessibilityRecord = this.mRecord;
        if (accessibilityRecord == null) {
            if (object.mRecord != null) {
                return false;
            }
            return true;
        }
        if (!accessibilityRecord.equals((Object)object.mRecord)) {
            return false;
        }
        return true;
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
        return AccessibilityRecordCompat.getMaxScrollX(this.mRecord);
    }

    @Deprecated
    public int getMaxScrollY() {
        return AccessibilityRecordCompat.getMaxScrollY(this.mRecord);
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
        return AccessibilityNodeInfoCompat.wrapNonNullInstance((Object)this.mRecord.getSource());
    }

    @Deprecated
    public List<CharSequence> getText() {
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
        AccessibilityRecord accessibilityRecord = this.mRecord;
        if (accessibilityRecord == null) {
            return 0;
        }
        return accessibilityRecord.hashCode();
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
    public void setAddedCount(int n) {
        this.mRecord.setAddedCount(n);
    }

    @Deprecated
    public void setBeforeText(CharSequence charSequence) {
        this.mRecord.setBeforeText(charSequence);
    }

    @Deprecated
    public void setChecked(boolean bl) {
        this.mRecord.setChecked(bl);
    }

    @Deprecated
    public void setClassName(CharSequence charSequence) {
        this.mRecord.setClassName(charSequence);
    }

    @Deprecated
    public void setContentDescription(CharSequence charSequence) {
        this.mRecord.setContentDescription(charSequence);
    }

    @Deprecated
    public void setCurrentItemIndex(int n) {
        this.mRecord.setCurrentItemIndex(n);
    }

    @Deprecated
    public void setEnabled(boolean bl) {
        this.mRecord.setEnabled(bl);
    }

    @Deprecated
    public void setFromIndex(int n) {
        this.mRecord.setFromIndex(n);
    }

    @Deprecated
    public void setFullScreen(boolean bl) {
        this.mRecord.setFullScreen(bl);
    }

    @Deprecated
    public void setItemCount(int n) {
        this.mRecord.setItemCount(n);
    }

    @Deprecated
    public void setMaxScrollX(int n) {
        AccessibilityRecordCompat.setMaxScrollX(this.mRecord, n);
    }

    @Deprecated
    public void setMaxScrollY(int n) {
        AccessibilityRecordCompat.setMaxScrollY(this.mRecord, n);
    }

    @Deprecated
    public void setParcelableData(Parcelable parcelable) {
        this.mRecord.setParcelableData(parcelable);
    }

    @Deprecated
    public void setPassword(boolean bl) {
        this.mRecord.setPassword(bl);
    }

    @Deprecated
    public void setRemovedCount(int n) {
        this.mRecord.setRemovedCount(n);
    }

    @Deprecated
    public void setScrollX(int n) {
        this.mRecord.setScrollX(n);
    }

    @Deprecated
    public void setScrollY(int n) {
        this.mRecord.setScrollY(n);
    }

    @Deprecated
    public void setScrollable(boolean bl) {
        this.mRecord.setScrollable(bl);
    }

    @Deprecated
    public void setSource(View view) {
        this.mRecord.setSource(view);
    }

    @Deprecated
    public void setSource(View view, int n) {
        AccessibilityRecordCompat.setSource(this.mRecord, view, n);
    }

    @Deprecated
    public void setToIndex(int n) {
        this.mRecord.setToIndex(n);
    }

    @RequiresApi(value=15)
    static class AccessibilityRecordCompatApi15Impl
    extends AccessibilityRecordCompatBaseImpl {
        AccessibilityRecordCompatApi15Impl() {
        }

        @Override
        public int getMaxScrollX(AccessibilityRecord accessibilityRecord) {
            return accessibilityRecord.getMaxScrollX();
        }

        @Override
        public int getMaxScrollY(AccessibilityRecord accessibilityRecord) {
            return accessibilityRecord.getMaxScrollY();
        }

        @Override
        public void setMaxScrollX(AccessibilityRecord accessibilityRecord, int n) {
            accessibilityRecord.setMaxScrollX(n);
        }

        @Override
        public void setMaxScrollY(AccessibilityRecord accessibilityRecord, int n) {
            accessibilityRecord.setMaxScrollY(n);
        }
    }

    @RequiresApi(value=16)
    static class AccessibilityRecordCompatApi16Impl
    extends AccessibilityRecordCompatApi15Impl {
        AccessibilityRecordCompatApi16Impl() {
        }

        @Override
        public void setSource(AccessibilityRecord accessibilityRecord, View view, int n) {
            accessibilityRecord.setSource(view, n);
        }
    }

    static class AccessibilityRecordCompatBaseImpl {
        AccessibilityRecordCompatBaseImpl() {
        }

        public int getMaxScrollX(AccessibilityRecord accessibilityRecord) {
            return 0;
        }

        public int getMaxScrollY(AccessibilityRecord accessibilityRecord) {
            return 0;
        }

        public void setMaxScrollX(AccessibilityRecord accessibilityRecord, int n) {
        }

        public void setMaxScrollY(AccessibilityRecord accessibilityRecord, int n) {
        }

        public void setSource(AccessibilityRecord accessibilityRecord, View view, int n) {
        }
    }

}

