/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewOverlay
 *  android.widget.FrameLayout
 */
package com.google.android.material.badge;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.ParcelableSparseArray;

public class BadgeUtils {
    public static final boolean USE_COMPAT_PARENT;

    static {
        boolean bl = Build.VERSION.SDK_INT < 18;
        USE_COMPAT_PARENT = bl;
    }

    private BadgeUtils() {
    }

    public static void attachBadgeDrawable(BadgeDrawable badgeDrawable, View view, FrameLayout frameLayout) {
        BadgeUtils.setBadgeDrawableBounds(badgeDrawable, view, frameLayout);
        if (USE_COMPAT_PARENT) {
            frameLayout.setForeground((Drawable)badgeDrawable);
            return;
        }
        view.getOverlay().add((Drawable)badgeDrawable);
    }

    public static SparseArray<BadgeDrawable> createBadgeDrawablesFromSavedStates(Context context, ParcelableSparseArray parcelableSparseArray) {
        SparseArray sparseArray = new SparseArray(parcelableSparseArray.size());
        for (int i = 0; i < parcelableSparseArray.size(); ++i) {
            int n = parcelableSparseArray.keyAt(i);
            BadgeDrawable.SavedState savedState = (BadgeDrawable.SavedState)parcelableSparseArray.valueAt(i);
            if (savedState != null) {
                sparseArray.put(n, (Object)BadgeDrawable.createFromSavedState(context, savedState));
                continue;
            }
            throw new IllegalArgumentException("BadgeDrawable's savedState cannot be null");
        }
        return sparseArray;
    }

    public static ParcelableSparseArray createParcelableBadgeStates(SparseArray<BadgeDrawable> sparseArray) {
        ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
        for (int i = 0; i < sparseArray.size(); ++i) {
            int n = sparseArray.keyAt(i);
            BadgeDrawable badgeDrawable = (BadgeDrawable)sparseArray.valueAt(i);
            if (badgeDrawable != null) {
                parcelableSparseArray.put(n, (Object)badgeDrawable.getSavedState());
                continue;
            }
            throw new IllegalArgumentException("badgeDrawable cannot be null");
        }
        return parcelableSparseArray;
    }

    public static void detachBadgeDrawable(BadgeDrawable badgeDrawable, View view, FrameLayout frameLayout) {
        if (badgeDrawable == null) {
            return;
        }
        if (USE_COMPAT_PARENT) {
            frameLayout.setForeground(null);
            return;
        }
        view.getOverlay().remove((Drawable)badgeDrawable);
    }

    public static void setBadgeDrawableBounds(BadgeDrawable badgeDrawable, View view, FrameLayout frameLayout) {
        Rect rect = new Rect();
        FrameLayout frameLayout2 = USE_COMPAT_PARENT ? frameLayout : view;
        frameLayout2.getDrawingRect(rect);
        badgeDrawable.setBounds(rect);
        badgeDrawable.updateBadgeCoordinates(view, (ViewGroup)frameLayout);
    }

    public static void updateBadgeBounds(Rect rect, float f, float f2, float f3, float f4) {
        rect.set((int)(f - f3), (int)(f2 - f4), (int)(f + f3), (int)(f2 + f4));
    }
}

