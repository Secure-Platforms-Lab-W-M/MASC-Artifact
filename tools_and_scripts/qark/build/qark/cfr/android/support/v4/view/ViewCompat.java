/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.ValueAnimator
 *  android.content.ClipData
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.util.Log
 *  android.view.Display
 *  android.view.PointerIcon
 *  android.view.View
 *  android.view.View$AccessibilityDelegate
 *  android.view.View$DragShadowBuilder
 *  android.view.View$OnApplyWindowInsetsListener
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.WindowInsets
 *  android.view.WindowManager
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeProvider
 */
package android.support.v4.view;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.util.Log;
import android.view.Display;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.WeakHashMap;

public class ViewCompat {
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    static final ViewCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 26 ? new ViewCompatApi26Impl() : (Build.VERSION.SDK_INT >= 24 ? new ViewCompatApi24Impl() : (Build.VERSION.SDK_INT >= 23 ? new ViewCompatApi23Impl() : (Build.VERSION.SDK_INT >= 21 ? new ViewCompatApi21Impl() : (Build.VERSION.SDK_INT >= 19 ? new ViewCompatApi19Impl() : (Build.VERSION.SDK_INT >= 18 ? new ViewCompatApi18Impl() : (Build.VERSION.SDK_INT >= 17 ? new ViewCompatApi17Impl() : (Build.VERSION.SDK_INT >= 16 ? new ViewCompatApi16Impl() : (Build.VERSION.SDK_INT >= 15 ? new ViewCompatApi15Impl() : new ViewCompatBaseImpl()))))))));
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    @Deprecated
    public static final int LAYER_TYPE_HARDWARE = 2;
    @Deprecated
    public static final int LAYER_TYPE_NONE = 0;
    @Deprecated
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    @Deprecated
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    @Deprecated
    public static final int MEASURED_SIZE_MASK = 16777215;
    @Deprecated
    public static final int MEASURED_STATE_MASK = -16777216;
    @Deprecated
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    @Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;
    @Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    @Deprecated
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    private static final String TAG = "ViewCompat";
    public static final int TYPE_NON_TOUCH = 1;
    public static final int TYPE_TOUCH = 0;

    protected ViewCompat() {
    }

    public static void addKeyboardNavigationClusters(@NonNull View view, @NonNull Collection<View> collection, int n) {
        IMPL.addKeyboardNavigationClusters(view, collection, n);
    }

    public static ViewPropertyAnimatorCompat animate(View view) {
        return IMPL.animate(view);
    }

    @Deprecated
    public static boolean canScrollHorizontally(View view, int n) {
        return view.canScrollHorizontally(n);
    }

    @Deprecated
    public static boolean canScrollVertically(View view, int n) {
        return view.canScrollVertically(n);
    }

    public static void cancelDragAndDrop(View view) {
        IMPL.cancelDragAndDrop(view);
    }

    @Deprecated
    public static int combineMeasuredStates(int n, int n2) {
        return View.combineMeasuredStates((int)n, (int)n2);
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        return IMPL.dispatchApplyWindowInsets(view, windowInsetsCompat);
    }

    public static void dispatchFinishTemporaryDetach(View view) {
        IMPL.dispatchFinishTemporaryDetach(view);
    }

    public static boolean dispatchNestedFling(@NonNull View view, float f, float f2, boolean bl) {
        return IMPL.dispatchNestedFling(view, f, f2, bl);
    }

    public static boolean dispatchNestedPreFling(@NonNull View view, float f, float f2) {
        return IMPL.dispatchNestedPreFling(view, f, f2);
    }

    public static boolean dispatchNestedPreScroll(@NonNull View view, int n, int n2, @Nullable int[] arrn, @Nullable int[] arrn2) {
        return IMPL.dispatchNestedPreScroll(view, n, n2, arrn, arrn2);
    }

    public static boolean dispatchNestedPreScroll(@NonNull View view, int n, int n2, @Nullable int[] arrn, @Nullable int[] arrn2, int n3) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).dispatchNestedPreScroll(n, n2, arrn, arrn2, n3);
        }
        if (n3 == 0) {
            return IMPL.dispatchNestedPreScroll(view, n, n2, arrn, arrn2);
        }
        return false;
    }

    public static boolean dispatchNestedScroll(@NonNull View view, int n, int n2, int n3, int n4, @Nullable int[] arrn) {
        return IMPL.dispatchNestedScroll(view, n, n2, n3, n4, arrn);
    }

    public static boolean dispatchNestedScroll(@NonNull View view, int n, int n2, int n3, int n4, @Nullable int[] arrn, int n5) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).dispatchNestedScroll(n, n2, n3, n4, arrn, n5);
        }
        if (n5 == 0) {
            return IMPL.dispatchNestedScroll(view, n, n2, n3, n4, arrn);
        }
        return false;
    }

    public static void dispatchStartTemporaryDetach(View view) {
        IMPL.dispatchStartTemporaryDetach(view);
    }

    public static int getAccessibilityLiveRegion(View view) {
        return IMPL.getAccessibilityLiveRegion(view);
    }

    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        return IMPL.getAccessibilityNodeProvider(view);
    }

    @Deprecated
    public static float getAlpha(View view) {
        return view.getAlpha();
    }

    public static ColorStateList getBackgroundTintList(View view) {
        return IMPL.getBackgroundTintList(view);
    }

    public static PorterDuff.Mode getBackgroundTintMode(View view) {
        return IMPL.getBackgroundTintMode(view);
    }

    public static Rect getClipBounds(View view) {
        return IMPL.getClipBounds(view);
    }

    public static Display getDisplay(@NonNull View view) {
        return IMPL.getDisplay(view);
    }

    public static float getElevation(View view) {
        return IMPL.getElevation(view);
    }

    public static boolean getFitsSystemWindows(View view) {
        return IMPL.getFitsSystemWindows(view);
    }

    public static int getImportantForAccessibility(View view) {
        return IMPL.getImportantForAccessibility(view);
    }

    public static int getLabelFor(View view) {
        return IMPL.getLabelFor(view);
    }

    @Deprecated
    public static int getLayerType(View view) {
        return view.getLayerType();
    }

    public static int getLayoutDirection(View view) {
        return IMPL.getLayoutDirection(view);
    }

    @Deprecated
    @Nullable
    public static Matrix getMatrix(View view) {
        return view.getMatrix();
    }

    @Deprecated
    public static int getMeasuredHeightAndState(View view) {
        return view.getMeasuredHeightAndState();
    }

    @Deprecated
    public static int getMeasuredState(View view) {
        return view.getMeasuredState();
    }

    @Deprecated
    public static int getMeasuredWidthAndState(View view) {
        return view.getMeasuredWidthAndState();
    }

    public static int getMinimumHeight(View view) {
        return IMPL.getMinimumHeight(view);
    }

    public static int getMinimumWidth(View view) {
        return IMPL.getMinimumWidth(view);
    }

    public static int getNextClusterForwardId(@NonNull View view) {
        return IMPL.getNextClusterForwardId(view);
    }

    @Deprecated
    public static int getOverScrollMode(View view) {
        return view.getOverScrollMode();
    }

    public static int getPaddingEnd(View view) {
        return IMPL.getPaddingEnd(view);
    }

    public static int getPaddingStart(View view) {
        return IMPL.getPaddingStart(view);
    }

    public static ViewParent getParentForAccessibility(View view) {
        return IMPL.getParentForAccessibility(view);
    }

    @Deprecated
    public static float getPivotX(View view) {
        return view.getPivotX();
    }

    @Deprecated
    public static float getPivotY(View view) {
        return view.getPivotY();
    }

    @Deprecated
    public static float getRotation(View view) {
        return view.getRotation();
    }

    @Deprecated
    public static float getRotationX(View view) {
        return view.getRotationX();
    }

    @Deprecated
    public static float getRotationY(View view) {
        return view.getRotationY();
    }

    @Deprecated
    public static float getScaleX(View view) {
        return view.getScaleX();
    }

    @Deprecated
    public static float getScaleY(View view) {
        return view.getScaleY();
    }

    public static int getScrollIndicators(@NonNull View view) {
        return IMPL.getScrollIndicators(view);
    }

    public static String getTransitionName(View view) {
        return IMPL.getTransitionName(view);
    }

    @Deprecated
    public static float getTranslationX(View view) {
        return view.getTranslationX();
    }

    @Deprecated
    public static float getTranslationY(View view) {
        return view.getTranslationY();
    }

    public static float getTranslationZ(View view) {
        return IMPL.getTranslationZ(view);
    }

    public static int getWindowSystemUiVisibility(View view) {
        return IMPL.getWindowSystemUiVisibility(view);
    }

    @Deprecated
    public static float getX(View view) {
        return view.getX();
    }

    @Deprecated
    public static float getY(View view) {
        return view.getY();
    }

    public static float getZ(View view) {
        return IMPL.getZ(view);
    }

    public static boolean hasAccessibilityDelegate(View view) {
        return IMPL.hasAccessibilityDelegate(view);
    }

    public static boolean hasExplicitFocusable(@NonNull View view) {
        return IMPL.hasExplicitFocusable(view);
    }

    public static boolean hasNestedScrollingParent(@NonNull View view) {
        return IMPL.hasNestedScrollingParent(view);
    }

    public static boolean hasNestedScrollingParent(@NonNull View view, int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).hasNestedScrollingParent(n);
        } else if (n == 0) {
            return IMPL.hasNestedScrollingParent(view);
        }
        return false;
    }

    public static boolean hasOnClickListeners(View view) {
        return IMPL.hasOnClickListeners(view);
    }

    public static boolean hasOverlappingRendering(View view) {
        return IMPL.hasOverlappingRendering(view);
    }

    public static boolean hasTransientState(View view) {
        return IMPL.hasTransientState(view);
    }

    public static boolean isAttachedToWindow(View view) {
        return IMPL.isAttachedToWindow(view);
    }

    public static boolean isFocusedByDefault(@NonNull View view) {
        return IMPL.isFocusedByDefault(view);
    }

    public static boolean isImportantForAccessibility(View view) {
        return IMPL.isImportantForAccessibility(view);
    }

    public static boolean isInLayout(View view) {
        return IMPL.isInLayout(view);
    }

    public static boolean isKeyboardNavigationCluster(@NonNull View view) {
        return IMPL.isKeyboardNavigationCluster(view);
    }

    public static boolean isLaidOut(View view) {
        return IMPL.isLaidOut(view);
    }

    public static boolean isLayoutDirectionResolved(View view) {
        return IMPL.isLayoutDirectionResolved(view);
    }

    public static boolean isNestedScrollingEnabled(@NonNull View view) {
        return IMPL.isNestedScrollingEnabled(view);
    }

    @Deprecated
    public static boolean isOpaque(View view) {
        return view.isOpaque();
    }

    public static boolean isPaddingRelative(View view) {
        return IMPL.isPaddingRelative(view);
    }

    @Deprecated
    public static void jumpDrawablesToCurrentState(View view) {
        view.jumpDrawablesToCurrentState();
    }

    public static View keyboardNavigationClusterSearch(@NonNull View view, View view2, int n) {
        return IMPL.keyboardNavigationClusterSearch(view, view2, n);
    }

    public static void offsetLeftAndRight(View view, int n) {
        IMPL.offsetLeftAndRight(view, n);
    }

    public static void offsetTopAndBottom(View view, int n) {
        IMPL.offsetTopAndBottom(view, n);
    }

    public static WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
        return IMPL.onApplyWindowInsets(view, windowInsetsCompat);
    }

    @Deprecated
    public static void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        view.onInitializeAccessibilityEvent(accessibilityEvent);
    }

    public static void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        IMPL.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
    }

    @Deprecated
    public static void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        view.onPopulateAccessibilityEvent(accessibilityEvent);
    }

    public static boolean performAccessibilityAction(View view, int n, Bundle bundle) {
        return IMPL.performAccessibilityAction(view, n, bundle);
    }

    public static void postInvalidateOnAnimation(View view) {
        IMPL.postInvalidateOnAnimation(view);
    }

    public static void postInvalidateOnAnimation(View view, int n, int n2, int n3, int n4) {
        IMPL.postInvalidateOnAnimation(view, n, n2, n3, n4);
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        IMPL.postOnAnimation(view, runnable);
    }

    public static void postOnAnimationDelayed(View view, Runnable runnable, long l) {
        IMPL.postOnAnimationDelayed(view, runnable, l);
    }

    public static void requestApplyInsets(View view) {
        IMPL.requestApplyInsets(view);
    }

    @Deprecated
    public static int resolveSizeAndState(int n, int n2, int n3) {
        return View.resolveSizeAndState((int)n, (int)n2, (int)n3);
    }

    public static boolean restoreDefaultFocus(@NonNull View view) {
        return IMPL.restoreDefaultFocus(view);
    }

    public static void setAccessibilityDelegate(View view, AccessibilityDelegateCompat accessibilityDelegateCompat) {
        IMPL.setAccessibilityDelegate(view, accessibilityDelegateCompat);
    }

    public static void setAccessibilityLiveRegion(View view, int n) {
        IMPL.setAccessibilityLiveRegion(view, n);
    }

    @Deprecated
    public static void setActivated(View view, boolean bl) {
        view.setActivated(bl);
    }

    @Deprecated
    public static void setAlpha(View view, @FloatRange(from=0.0, to=1.0) float f) {
        view.setAlpha(f);
    }

    public static void setBackground(View view, Drawable drawable2) {
        IMPL.setBackground(view, drawable2);
    }

    public static void setBackgroundTintList(View view, ColorStateList colorStateList) {
        IMPL.setBackgroundTintList(view, colorStateList);
    }

    public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        IMPL.setBackgroundTintMode(view, mode);
    }

    public static void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean bl) {
        IMPL.setChildrenDrawingOrderEnabled(viewGroup, bl);
    }

    public static void setClipBounds(View view, Rect rect) {
        IMPL.setClipBounds(view, rect);
    }

    public static void setElevation(View view, float f) {
        IMPL.setElevation(view, f);
    }

    @Deprecated
    public static void setFitsSystemWindows(View view, boolean bl) {
        view.setFitsSystemWindows(bl);
    }

    public static void setFocusedByDefault(@NonNull View view, boolean bl) {
        IMPL.setFocusedByDefault(view, bl);
    }

    public static void setHasTransientState(View view, boolean bl) {
        IMPL.setHasTransientState(view, bl);
    }

    public static void setImportantForAccessibility(View view, int n) {
        IMPL.setImportantForAccessibility(view, n);
    }

    public static void setKeyboardNavigationCluster(@NonNull View view, boolean bl) {
        IMPL.setKeyboardNavigationCluster(view, bl);
    }

    public static void setLabelFor(View view, @IdRes int n) {
        IMPL.setLabelFor(view, n);
    }

    public static void setLayerPaint(View view, Paint paint) {
        IMPL.setLayerPaint(view, paint);
    }

    @Deprecated
    public static void setLayerType(View view, int n, Paint paint) {
        view.setLayerType(n, paint);
    }

    public static void setLayoutDirection(View view, int n) {
        IMPL.setLayoutDirection(view, n);
    }

    public static void setNestedScrollingEnabled(@NonNull View view, boolean bl) {
        IMPL.setNestedScrollingEnabled(view, bl);
    }

    public static void setNextClusterForwardId(@NonNull View view, int n) {
        IMPL.setNextClusterForwardId(view, n);
    }

    public static void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        IMPL.setOnApplyWindowInsetsListener(view, onApplyWindowInsetsListener);
    }

    @Deprecated
    public static void setOverScrollMode(View view, int n) {
        view.setOverScrollMode(n);
    }

    public static void setPaddingRelative(View view, int n, int n2, int n3, int n4) {
        IMPL.setPaddingRelative(view, n, n2, n3, n4);
    }

    @Deprecated
    public static void setPivotX(View view, float f) {
        view.setPivotX(f);
    }

    @Deprecated
    public static void setPivotY(View view, float f) {
        view.setPivotY(f);
    }

    public static void setPointerIcon(@NonNull View view, PointerIconCompat pointerIconCompat) {
        IMPL.setPointerIcon(view, pointerIconCompat);
    }

    @Deprecated
    public static void setRotation(View view, float f) {
        view.setRotation(f);
    }

    @Deprecated
    public static void setRotationX(View view, float f) {
        view.setRotationX(f);
    }

    @Deprecated
    public static void setRotationY(View view, float f) {
        view.setRotationY(f);
    }

    @Deprecated
    public static void setSaveFromParentEnabled(View view, boolean bl) {
        view.setSaveFromParentEnabled(bl);
    }

    @Deprecated
    public static void setScaleX(View view, float f) {
        view.setScaleX(f);
    }

    @Deprecated
    public static void setScaleY(View view, float f) {
        view.setScaleY(f);
    }

    public static void setScrollIndicators(@NonNull View view, int n) {
        IMPL.setScrollIndicators(view, n);
    }

    public static void setScrollIndicators(@NonNull View view, int n, int n2) {
        IMPL.setScrollIndicators(view, n, n2);
    }

    public static void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence) {
        IMPL.setTooltipText(view, charSequence);
    }

    public static void setTransitionName(View view, String string2) {
        IMPL.setTransitionName(view, string2);
    }

    @Deprecated
    public static void setTranslationX(View view, float f) {
        view.setTranslationX(f);
    }

    @Deprecated
    public static void setTranslationY(View view, float f) {
        view.setTranslationY(f);
    }

    public static void setTranslationZ(View view, float f) {
        IMPL.setTranslationZ(view, f);
    }

    @Deprecated
    public static void setX(View view, float f) {
        view.setX(f);
    }

    @Deprecated
    public static void setY(View view, float f) {
        view.setY(f);
    }

    public static void setZ(View view, float f) {
        IMPL.setZ(view, f);
    }

    public static boolean startDragAndDrop(View view, ClipData clipData, View.DragShadowBuilder dragShadowBuilder, Object object, int n) {
        return IMPL.startDragAndDrop(view, clipData, dragShadowBuilder, object, n);
    }

    public static boolean startNestedScroll(@NonNull View view, int n) {
        return IMPL.startNestedScroll(view, n);
    }

    public static boolean startNestedScroll(@NonNull View view, int n, int n2) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).startNestedScroll(n, n2);
        }
        if (n2 == 0) {
            return IMPL.startNestedScroll(view, n);
        }
        return false;
    }

    public static void stopNestedScroll(@NonNull View view) {
        IMPL.stopNestedScroll(view);
    }

    public static void stopNestedScroll(@NonNull View view, int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).stopNestedScroll(n);
            return;
        }
        if (n == 0) {
            IMPL.stopNestedScroll(view);
            return;
        }
    }

    public static void updateDragShadow(View view, View.DragShadowBuilder dragShadowBuilder) {
        IMPL.updateDragShadow(view, dragShadowBuilder);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface AccessibilityLiveRegion {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface FocusDirection {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface FocusRealDirection {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface FocusRelativeDirection {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface ImportantForAccessibility {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface LayerType {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface LayoutDirectionMode {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface NestedScrollType {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface OverScroll {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface ResolvedLayoutDirectionMode {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface ScrollAxis {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface ScrollIndicators {
    }

    @RequiresApi(value=15)
    static class ViewCompatApi15Impl
    extends ViewCompatBaseImpl {
        ViewCompatApi15Impl() {
        }

        @Override
        public boolean hasOnClickListeners(View view) {
            return view.hasOnClickListeners();
        }
    }

    @RequiresApi(value=16)
    static class ViewCompatApi16Impl
    extends ViewCompatApi15Impl {
        ViewCompatApi16Impl() {
        }

        @Override
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
            if ((view = view.getAccessibilityNodeProvider()) != null) {
                return new AccessibilityNodeProviderCompat((Object)view);
            }
            return null;
        }

        @Override
        public boolean getFitsSystemWindows(View view) {
            return view.getFitsSystemWindows();
        }

        @Override
        public int getImportantForAccessibility(View view) {
            return view.getImportantForAccessibility();
        }

        @Override
        public int getMinimumHeight(View view) {
            return view.getMinimumHeight();
        }

        @Override
        public int getMinimumWidth(View view) {
            return view.getMinimumWidth();
        }

        @Override
        public ViewParent getParentForAccessibility(View view) {
            return view.getParentForAccessibility();
        }

        @Override
        public boolean hasOverlappingRendering(View view) {
            return view.hasOverlappingRendering();
        }

        @Override
        public boolean hasTransientState(View view) {
            return view.hasTransientState();
        }

        @Override
        public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
            return view.performAccessibilityAction(n, bundle);
        }

        @Override
        public void postInvalidateOnAnimation(View view) {
            view.postInvalidateOnAnimation();
        }

        @Override
        public void postInvalidateOnAnimation(View view, int n, int n2, int n3, int n4) {
            view.postInvalidateOnAnimation(n, n2, n3, n4);
        }

        @Override
        public void postOnAnimation(View view, Runnable runnable) {
            view.postOnAnimation(runnable);
        }

        @Override
        public void postOnAnimationDelayed(View view, Runnable runnable, long l) {
            view.postOnAnimationDelayed(runnable, l);
        }

        @Override
        public void requestApplyInsets(View view) {
            view.requestFitSystemWindows();
        }

        @Override
        public void setBackground(View view, Drawable drawable2) {
            view.setBackground(drawable2);
        }

        @Override
        public void setHasTransientState(View view, boolean bl) {
            view.setHasTransientState(bl);
        }

        @Override
        public void setImportantForAccessibility(View view, int n) {
            if (n == 4) {
                n = 2;
            }
            view.setImportantForAccessibility(n);
        }
    }

    @RequiresApi(value=17)
    static class ViewCompatApi17Impl
    extends ViewCompatApi16Impl {
        ViewCompatApi17Impl() {
        }

        @Override
        public Display getDisplay(View view) {
            return view.getDisplay();
        }

        @Override
        public int getLabelFor(View view) {
            return view.getLabelFor();
        }

        @Override
        public int getLayoutDirection(View view) {
            return view.getLayoutDirection();
        }

        @Override
        public int getPaddingEnd(View view) {
            return view.getPaddingEnd();
        }

        @Override
        public int getPaddingStart(View view) {
            return view.getPaddingStart();
        }

        @Override
        public int getWindowSystemUiVisibility(View view) {
            return view.getWindowSystemUiVisibility();
        }

        @Override
        public boolean isPaddingRelative(View view) {
            return view.isPaddingRelative();
        }

        @Override
        public void setLabelFor(View view, int n) {
            view.setLabelFor(n);
        }

        @Override
        public void setLayerPaint(View view, Paint paint) {
            view.setLayerPaint(paint);
        }

        @Override
        public void setLayoutDirection(View view, int n) {
            view.setLayoutDirection(n);
        }

        @Override
        public void setPaddingRelative(View view, int n, int n2, int n3, int n4) {
            view.setPaddingRelative(n, n2, n3, n4);
        }
    }

    @RequiresApi(value=18)
    static class ViewCompatApi18Impl
    extends ViewCompatApi17Impl {
        ViewCompatApi18Impl() {
        }

        @Override
        public Rect getClipBounds(View view) {
            return view.getClipBounds();
        }

        @Override
        public boolean isInLayout(View view) {
            return view.isInLayout();
        }

        @Override
        public void setClipBounds(View view, Rect rect) {
            view.setClipBounds(rect);
        }
    }

    @RequiresApi(value=19)
    static class ViewCompatApi19Impl
    extends ViewCompatApi18Impl {
        ViewCompatApi19Impl() {
        }

        @Override
        public int getAccessibilityLiveRegion(View view) {
            return view.getAccessibilityLiveRegion();
        }

        @Override
        public boolean isAttachedToWindow(View view) {
            return view.isAttachedToWindow();
        }

        @Override
        public boolean isLaidOut(View view) {
            return view.isLaidOut();
        }

        @Override
        public boolean isLayoutDirectionResolved(View view) {
            return view.isLayoutDirectionResolved();
        }

        @Override
        public void setAccessibilityLiveRegion(View view, int n) {
            view.setAccessibilityLiveRegion(n);
        }

        @Override
        public void setImportantForAccessibility(View view, int n) {
            view.setImportantForAccessibility(n);
        }
    }

    @RequiresApi(value=21)
    static class ViewCompatApi21Impl
    extends ViewCompatApi19Impl {
        private static ThreadLocal<Rect> sThreadLocalRect;

        ViewCompatApi21Impl() {
        }

        private static Rect getEmptyTempRect() {
            Rect rect;
            if (sThreadLocalRect == null) {
                sThreadLocalRect = new ThreadLocal();
            }
            if ((rect = sThreadLocalRect.get()) == null) {
                rect = new Rect();
                sThreadLocalRect.set(rect);
            }
            rect.setEmpty();
            return rect;
        }

        @Override
        public WindowInsetsCompat dispatchApplyWindowInsets(View object, WindowInsetsCompat windowInsetsCompat) {
            object = (object = object.dispatchApplyWindowInsets((WindowInsets)(windowInsetsCompat = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat)))) != windowInsetsCompat ? new WindowInsets((WindowInsets)object) : windowInsetsCompat;
            return WindowInsetsCompat.wrap(object);
        }

        @Override
        public boolean dispatchNestedFling(View view, float f, float f2, boolean bl) {
            return view.dispatchNestedFling(f, f2, bl);
        }

        @Override
        public boolean dispatchNestedPreFling(View view, float f, float f2) {
            return view.dispatchNestedPreFling(f, f2);
        }

        @Override
        public boolean dispatchNestedPreScroll(View view, int n, int n2, int[] arrn, int[] arrn2) {
            return view.dispatchNestedPreScroll(n, n2, arrn, arrn2);
        }

        @Override
        public boolean dispatchNestedScroll(View view, int n, int n2, int n3, int n4, int[] arrn) {
            return view.dispatchNestedScroll(n, n2, n3, n4, arrn);
        }

        @Override
        public ColorStateList getBackgroundTintList(View view) {
            return view.getBackgroundTintList();
        }

        @Override
        public PorterDuff.Mode getBackgroundTintMode(View view) {
            return view.getBackgroundTintMode();
        }

        @Override
        public float getElevation(View view) {
            return view.getElevation();
        }

        @Override
        public String getTransitionName(View view) {
            return view.getTransitionName();
        }

        @Override
        public float getTranslationZ(View view) {
            return view.getTranslationZ();
        }

        @Override
        public float getZ(View view) {
            return view.getZ();
        }

        @Override
        public boolean hasNestedScrollingParent(View view) {
            return view.hasNestedScrollingParent();
        }

        @Override
        public boolean isImportantForAccessibility(View view) {
            return view.isImportantForAccessibility();
        }

        @Override
        public boolean isNestedScrollingEnabled(View view) {
            return view.isNestedScrollingEnabled();
        }

        @Override
        public void offsetLeftAndRight(View view, int n) {
            Rect rect = ViewCompatApi21Impl.getEmptyTempRect();
            boolean bl = false;
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof View) {
                View view2 = (View)viewParent;
                rect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                bl = rect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true;
            }
            super.offsetLeftAndRight(view, n);
            if (bl && rect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)viewParent).invalidate(rect);
                return;
            }
        }

        @Override
        public void offsetTopAndBottom(View view, int n) {
            Rect rect = ViewCompatApi21Impl.getEmptyTempRect();
            boolean bl = false;
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof View) {
                View view2 = (View)viewParent;
                rect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                bl = rect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true;
            }
            super.offsetTopAndBottom(view, n);
            if (bl && rect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)viewParent).invalidate(rect);
                return;
            }
        }

        @Override
        public WindowInsetsCompat onApplyWindowInsets(View object, WindowInsetsCompat windowInsetsCompat) {
            object = (object = object.onApplyWindowInsets((WindowInsets)(windowInsetsCompat = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat)))) != windowInsetsCompat ? new WindowInsets((WindowInsets)object) : windowInsetsCompat;
            return WindowInsetsCompat.wrap(object);
        }

        @Override
        public void requestApplyInsets(View view) {
            view.requestApplyInsets();
        }

        @Override
        public void setBackgroundTintList(View view, ColorStateList colorStateList) {
            view.setBackgroundTintList(colorStateList);
            if (Build.VERSION.SDK_INT == 21) {
                colorStateList = view.getBackground();
                boolean bl = view.getBackgroundTintList() != null && view.getBackgroundTintMode() != null;
                if (colorStateList != null && bl) {
                    if (colorStateList.isStateful()) {
                        colorStateList.setState(view.getDrawableState());
                    }
                    view.setBackground((Drawable)colorStateList);
                    return;
                }
                return;
            }
        }

        @Override
        public void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
            view.setBackgroundTintMode(mode);
            if (Build.VERSION.SDK_INT == 21) {
                mode = view.getBackground();
                boolean bl = view.getBackgroundTintList() != null && view.getBackgroundTintMode() != null;
                if (mode != null && bl) {
                    if (mode.isStateful()) {
                        mode.setState(view.getDrawableState());
                    }
                    view.setBackground((Drawable)mode);
                    return;
                }
                return;
            }
        }

        @Override
        public void setElevation(View view, float f) {
            view.setElevation(f);
        }

        @Override
        public void setNestedScrollingEnabled(View view, boolean bl) {
            view.setNestedScrollingEnabled(bl);
        }

        @Override
        public void setOnApplyWindowInsetsListener(View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
            if (onApplyWindowInsetsListener == null) {
                view.setOnApplyWindowInsetsListener(null);
                return;
            }
            view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener(){

                public WindowInsets onApplyWindowInsets(View view, WindowInsets object) {
                    object = WindowInsetsCompat.wrap(object);
                    return (WindowInsets)WindowInsetsCompat.unwrap(onApplyWindowInsetsListener.onApplyWindowInsets(view, (WindowInsetsCompat)object));
                }
            });
        }

        @Override
        public void setTransitionName(View view, String string2) {
            view.setTransitionName(string2);
        }

        @Override
        public void setTranslationZ(View view, float f) {
            view.setTranslationZ(f);
        }

        @Override
        public void setZ(View view, float f) {
            view.setZ(f);
        }

        @Override
        public boolean startNestedScroll(View view, int n) {
            return view.startNestedScroll(n);
        }

        @Override
        public void stopNestedScroll(View view) {
            view.stopNestedScroll();
        }

    }

    @RequiresApi(value=23)
    static class ViewCompatApi23Impl
    extends ViewCompatApi21Impl {
        ViewCompatApi23Impl() {
        }

        @Override
        public int getScrollIndicators(View view) {
            return view.getScrollIndicators();
        }

        @Override
        public void offsetLeftAndRight(View view, int n) {
            view.offsetLeftAndRight(n);
        }

        @Override
        public void offsetTopAndBottom(View view, int n) {
            view.offsetTopAndBottom(n);
        }

        @Override
        public void setScrollIndicators(View view, int n) {
            view.setScrollIndicators(n);
        }

        @Override
        public void setScrollIndicators(View view, int n, int n2) {
            view.setScrollIndicators(n, n2);
        }
    }

    @RequiresApi(value=24)
    static class ViewCompatApi24Impl
    extends ViewCompatApi23Impl {
        ViewCompatApi24Impl() {
        }

        @Override
        public void cancelDragAndDrop(View view) {
            view.cancelDragAndDrop();
        }

        @Override
        public void dispatchFinishTemporaryDetach(View view) {
            view.dispatchFinishTemporaryDetach();
        }

        @Override
        public void dispatchStartTemporaryDetach(View view) {
            view.dispatchStartTemporaryDetach();
        }

        @Override
        public void setPointerIcon(View view, PointerIconCompat object) {
            object = object != null ? object.getPointerIcon() : null;
            view.setPointerIcon((PointerIcon)object);
        }

        @Override
        public boolean startDragAndDrop(View view, ClipData clipData, View.DragShadowBuilder dragShadowBuilder, Object object, int n) {
            return view.startDragAndDrop(clipData, dragShadowBuilder, object, n);
        }

        @Override
        public void updateDragShadow(View view, View.DragShadowBuilder dragShadowBuilder) {
            view.updateDragShadow(dragShadowBuilder);
        }
    }

    @RequiresApi(value=26)
    static class ViewCompatApi26Impl
    extends ViewCompatApi24Impl {
        ViewCompatApi26Impl() {
        }

        @Override
        public void addKeyboardNavigationClusters(@NonNull View view, @NonNull Collection<View> collection, int n) {
            view.addKeyboardNavigationClusters(collection, n);
        }

        @Override
        public int getNextClusterForwardId(@NonNull View view) {
            return view.getNextClusterForwardId();
        }

        @Override
        public boolean hasExplicitFocusable(@NonNull View view) {
            return view.hasExplicitFocusable();
        }

        @Override
        public boolean isFocusedByDefault(@NonNull View view) {
            return view.isFocusedByDefault();
        }

        @Override
        public boolean isKeyboardNavigationCluster(@NonNull View view) {
            return view.isKeyboardNavigationCluster();
        }

        @Override
        public View keyboardNavigationClusterSearch(@NonNull View view, View view2, int n) {
            return view.keyboardNavigationClusterSearch(view2, n);
        }

        @Override
        public boolean restoreDefaultFocus(@NonNull View view) {
            return view.restoreDefaultFocus();
        }

        @Override
        public void setFocusedByDefault(@NonNull View view, boolean bl) {
            view.setFocusedByDefault(bl);
        }

        @Override
        public void setKeyboardNavigationCluster(@NonNull View view, boolean bl) {
            view.setKeyboardNavigationCluster(bl);
        }

        @Override
        public void setNextClusterForwardId(@NonNull View view, int n) {
            view.setNextClusterForwardId(n);
        }

        @Override
        public void setTooltipText(View view, CharSequence charSequence) {
            view.setTooltipText(charSequence);
        }
    }

    static class ViewCompatBaseImpl {
        static boolean sAccessibilityDelegateCheckFailed = false;
        static Field sAccessibilityDelegateField;
        private static Method sChildrenDrawingOrderMethod;
        private static Field sMinHeightField;
        private static boolean sMinHeightFieldFetched;
        private static Field sMinWidthField;
        private static boolean sMinWidthFieldFetched;
        private static WeakHashMap<View, String> sTransitionNameMap;
        private Method mDispatchFinishTemporaryDetach;
        private Method mDispatchStartTemporaryDetach;
        private boolean mTempDetachBound;
        WeakHashMap<View, ViewPropertyAnimatorCompat> mViewPropertyAnimatorCompatMap = null;

        ViewCompatBaseImpl() {
        }

        private void bindTempDetach() {
            try {
                this.mDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);
                this.mDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.e((String)"ViewCompat", (String)"Couldn't find method", (Throwable)noSuchMethodException);
            }
            this.mTempDetachBound = true;
        }

        private static void tickleInvalidationFlag(View view) {
            float f = view.getTranslationY();
            view.setTranslationY(1.0f + f);
            view.setTranslationY(f);
        }

        public void addKeyboardNavigationClusters(@NonNull View view, @NonNull Collection<View> collection, int n) {
        }

        public ViewPropertyAnimatorCompat animate(View view) {
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
            if (this.mViewPropertyAnimatorCompatMap == null) {
                this.mViewPropertyAnimatorCompatMap = new WeakHashMap();
            }
            if ((viewPropertyAnimatorCompat = this.mViewPropertyAnimatorCompatMap.get((Object)view)) == null) {
                viewPropertyAnimatorCompat = new ViewPropertyAnimatorCompat(view);
                this.mViewPropertyAnimatorCompatMap.put(view, viewPropertyAnimatorCompat);
                return viewPropertyAnimatorCompat;
            }
            return viewPropertyAnimatorCompat;
        }

        public void cancelDragAndDrop(View view) {
        }

        public WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }

        public void dispatchFinishTemporaryDetach(View view) {
            Method method;
            if (!this.mTempDetachBound) {
                this.bindTempDetach();
            }
            if ((method = this.mDispatchFinishTemporaryDetach) != null) {
                try {
                    method.invoke((Object)view, new Object[0]);
                }
                catch (Exception exception) {
                    Log.d((String)"ViewCompat", (String)"Error calling dispatchFinishTemporaryDetach", (Throwable)exception);
                }
                return;
            }
            view.onFinishTemporaryDetach();
        }

        public boolean dispatchNestedFling(View view, float f, float f2, boolean bl) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild)view).dispatchNestedFling(f, f2, bl);
            }
            return false;
        }

        public boolean dispatchNestedPreFling(View view, float f, float f2) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild)view).dispatchNestedPreFling(f, f2);
            }
            return false;
        }

        public boolean dispatchNestedPreScroll(View view, int n, int n2, int[] arrn, int[] arrn2) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild)view).dispatchNestedPreScroll(n, n2, arrn, arrn2);
            }
            return false;
        }

        public boolean dispatchNestedScroll(View view, int n, int n2, int n3, int n4, int[] arrn) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild)view).dispatchNestedScroll(n, n2, n3, n4, arrn);
            }
            return false;
        }

        public void dispatchStartTemporaryDetach(View view) {
            Method method;
            if (!this.mTempDetachBound) {
                this.bindTempDetach();
            }
            if ((method = this.mDispatchStartTemporaryDetach) != null) {
                try {
                    method.invoke((Object)view, new Object[0]);
                }
                catch (Exception exception) {
                    Log.d((String)"ViewCompat", (String)"Error calling dispatchStartTemporaryDetach", (Throwable)exception);
                }
                return;
            }
            view.onStartTemporaryDetach();
        }

        public int getAccessibilityLiveRegion(View view) {
            return 0;
        }

        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
            return null;
        }

        public ColorStateList getBackgroundTintList(View view) {
            if (view instanceof TintableBackgroundView) {
                return ((TintableBackgroundView)view).getSupportBackgroundTintList();
            }
            return null;
        }

        public PorterDuff.Mode getBackgroundTintMode(View view) {
            if (view instanceof TintableBackgroundView) {
                return ((TintableBackgroundView)view).getSupportBackgroundTintMode();
            }
            return null;
        }

        public Rect getClipBounds(View view) {
            return null;
        }

        public Display getDisplay(View view) {
            if (this.isAttachedToWindow(view)) {
                return ((WindowManager)view.getContext().getSystemService("window")).getDefaultDisplay();
            }
            return null;
        }

        public float getElevation(View view) {
            return 0.0f;
        }

        public boolean getFitsSystemWindows(View view) {
            return false;
        }

        long getFrameTime() {
            return ValueAnimator.getFrameDelay();
        }

        public int getImportantForAccessibility(View view) {
            return 0;
        }

        public int getLabelFor(View view) {
            return 0;
        }

        public int getLayoutDirection(View view) {
            return 0;
        }

        public int getMinimumHeight(View view) {
            Field field;
            if (!sMinHeightFieldFetched) {
                try {
                    sMinHeightField = View.class.getDeclaredField("mMinHeight");
                    sMinHeightField.setAccessible(true);
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    // empty catch block
                }
                sMinHeightFieldFetched = true;
            }
            if ((field = sMinHeightField) != null) {
                try {
                    int n = (Integer)field.get((Object)view);
                    return n;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            return 0;
        }

        public int getMinimumWidth(View view) {
            Field field;
            if (!sMinWidthFieldFetched) {
                try {
                    sMinWidthField = View.class.getDeclaredField("mMinWidth");
                    sMinWidthField.setAccessible(true);
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    // empty catch block
                }
                sMinWidthFieldFetched = true;
            }
            if ((field = sMinWidthField) != null) {
                try {
                    int n = (Integer)field.get((Object)view);
                    return n;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            return 0;
        }

        public int getNextClusterForwardId(@NonNull View view) {
            return -1;
        }

        public int getPaddingEnd(View view) {
            return view.getPaddingRight();
        }

        public int getPaddingStart(View view) {
            return view.getPaddingLeft();
        }

        public ViewParent getParentForAccessibility(View view) {
            return view.getParent();
        }

        public int getScrollIndicators(View view) {
            return 0;
        }

        public String getTransitionName(View view) {
            WeakHashMap<View, String> weakHashMap = sTransitionNameMap;
            if (weakHashMap == null) {
                return null;
            }
            return weakHashMap.get((Object)view);
        }

        public float getTranslationZ(View view) {
            return 0.0f;
        }

        public int getWindowSystemUiVisibility(View view) {
            return 0;
        }

        public float getZ(View view) {
            return this.getTranslationZ(view) + this.getElevation(view);
        }

        public boolean hasAccessibilityDelegate(View object) {
            boolean bl = sAccessibilityDelegateCheckFailed;
            boolean bl2 = false;
            if (bl) {
                return false;
            }
            if (sAccessibilityDelegateField == null) {
                try {
                    sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");
                    sAccessibilityDelegateField.setAccessible(true);
                }
                catch (Throwable throwable) {
                    sAccessibilityDelegateCheckFailed = true;
                    return false;
                }
            }
            try {
                object = sAccessibilityDelegateField.get(object);
                if (object != null) {
                    bl2 = true;
                }
                return bl2;
            }
            catch (Throwable throwable) {
                sAccessibilityDelegateCheckFailed = true;
                return false;
            }
        }

        public boolean hasExplicitFocusable(@NonNull View view) {
            return view.hasFocusable();
        }

        public boolean hasNestedScrollingParent(View view) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild)view).hasNestedScrollingParent();
            }
            return false;
        }

        public boolean hasOnClickListeners(View view) {
            return false;
        }

        public boolean hasOverlappingRendering(View view) {
            return true;
        }

        public boolean hasTransientState(View view) {
            return false;
        }

        public boolean isAttachedToWindow(View view) {
            if (view.getWindowToken() != null) {
                return true;
            }
            return false;
        }

        public boolean isFocusedByDefault(@NonNull View view) {
            return false;
        }

        public boolean isImportantForAccessibility(View view) {
            return true;
        }

        public boolean isInLayout(View view) {
            return false;
        }

        public boolean isKeyboardNavigationCluster(@NonNull View view) {
            return false;
        }

        public boolean isLaidOut(View view) {
            if (view.getWidth() > 0 && view.getHeight() > 0) {
                return true;
            }
            return false;
        }

        public boolean isLayoutDirectionResolved(View view) {
            return false;
        }

        public boolean isNestedScrollingEnabled(View view) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild)view).isNestedScrollingEnabled();
            }
            return false;
        }

        public boolean isPaddingRelative(View view) {
            return false;
        }

        public View keyboardNavigationClusterSearch(@NonNull View view, View view2, int n) {
            return null;
        }

        public void offsetLeftAndRight(View view, int n) {
            view.offsetLeftAndRight(n);
            if (view.getVisibility() == 0) {
                ViewCompatBaseImpl.tickleInvalidationFlag(view);
                view = view.getParent();
                if (view instanceof View) {
                    ViewCompatBaseImpl.tickleInvalidationFlag(view);
                    return;
                }
                return;
            }
        }

        public void offsetTopAndBottom(View view, int n) {
            view.offsetTopAndBottom(n);
            if (view.getVisibility() == 0) {
                ViewCompatBaseImpl.tickleInvalidationFlag(view);
                view = view.getParent();
                if (view instanceof View) {
                    ViewCompatBaseImpl.tickleInvalidationFlag(view);
                    return;
                }
                return;
            }
        }

        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            view.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat.unwrap());
        }

        public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
            return false;
        }

        public void postInvalidateOnAnimation(View view) {
            view.postInvalidate();
        }

        public void postInvalidateOnAnimation(View view, int n, int n2, int n3, int n4) {
            view.postInvalidate(n, n2, n3, n4);
        }

        public void postOnAnimation(View view, Runnable runnable) {
            view.postDelayed(runnable, this.getFrameTime());
        }

        public void postOnAnimationDelayed(View view, Runnable runnable, long l) {
            view.postDelayed(runnable, this.getFrameTime() + l);
        }

        public void requestApplyInsets(View view) {
        }

        public boolean restoreDefaultFocus(@NonNull View view) {
            return view.requestFocus();
        }

        public void setAccessibilityDelegate(View view, @Nullable AccessibilityDelegateCompat accessibilityDelegateCompat) {
            accessibilityDelegateCompat = accessibilityDelegateCompat == null ? null : accessibilityDelegateCompat.getBridge();
            view.setAccessibilityDelegate((View.AccessibilityDelegate)accessibilityDelegateCompat);
        }

        public void setAccessibilityLiveRegion(View view, int n) {
        }

        public void setBackground(View view, Drawable drawable2) {
            view.setBackgroundDrawable(drawable2);
        }

        public void setBackgroundTintList(View view, ColorStateList colorStateList) {
            if (view instanceof TintableBackgroundView) {
                ((TintableBackgroundView)view).setSupportBackgroundTintList(colorStateList);
                return;
            }
        }

        public void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
            if (view instanceof TintableBackgroundView) {
                ((TintableBackgroundView)view).setSupportBackgroundTintMode(mode);
                return;
            }
        }

        public void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean bl) {
            if (sChildrenDrawingOrderMethod == null) {
                try {
                    sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    Log.e((String)"ViewCompat", (String)"Unable to find childrenDrawingOrderEnabled", (Throwable)noSuchMethodException);
                }
                sChildrenDrawingOrderMethod.setAccessible(true);
            }
            try {
                sChildrenDrawingOrderMethod.invoke((Object)viewGroup, bl);
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.e((String)"ViewCompat", (String)"Unable to invoke childrenDrawingOrderEnabled", (Throwable)invocationTargetException);
                return;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e((String)"ViewCompat", (String)"Unable to invoke childrenDrawingOrderEnabled", (Throwable)illegalArgumentException);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)"ViewCompat", (String)"Unable to invoke childrenDrawingOrderEnabled", (Throwable)illegalAccessException);
            }
        }

        public void setClipBounds(View view, Rect rect) {
        }

        public void setElevation(View view, float f) {
        }

        public void setFocusedByDefault(@NonNull View view, boolean bl) {
        }

        public void setHasTransientState(View view, boolean bl) {
        }

        public void setImportantForAccessibility(View view, int n) {
        }

        public void setKeyboardNavigationCluster(@NonNull View view, boolean bl) {
        }

        public void setLabelFor(View view, int n) {
        }

        public void setLayerPaint(View view, Paint paint) {
            view.setLayerType(view.getLayerType(), paint);
            view.invalidate();
        }

        public void setLayoutDirection(View view, int n) {
        }

        public void setNestedScrollingEnabled(View view, boolean bl) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild)view).setNestedScrollingEnabled(bl);
                return;
            }
        }

        public void setNextClusterForwardId(@NonNull View view, int n) {
        }

        public void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        }

        public void setPaddingRelative(View view, int n, int n2, int n3, int n4) {
            view.setPadding(n, n2, n3, n4);
        }

        public void setPointerIcon(View view, PointerIconCompat pointerIconCompat) {
        }

        public void setScrollIndicators(View view, int n) {
        }

        public void setScrollIndicators(View view, int n, int n2) {
        }

        public void setTooltipText(View view, CharSequence charSequence) {
        }

        public void setTransitionName(View view, String string2) {
            if (sTransitionNameMap == null) {
                sTransitionNameMap = new WeakHashMap();
            }
            sTransitionNameMap.put(view, string2);
        }

        public void setTranslationZ(View view, float f) {
        }

        public void setZ(View view, float f) {
        }

        public boolean startDragAndDrop(View view, ClipData clipData, View.DragShadowBuilder dragShadowBuilder, Object object, int n) {
            return view.startDrag(clipData, dragShadowBuilder, object, n);
        }

        public boolean startNestedScroll(View view, int n) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild)view).startNestedScroll(n);
            }
            return false;
        }

        public void stopNestedScroll(View view) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild)view).stopNestedScroll();
                return;
            }
        }

        public void updateDragShadow(View view, View.DragShadowBuilder dragShadowBuilder) {
        }
    }

}

