package android.support.v4.view;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
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
import android.view.View.AccessibilityDelegate;
import android.view.View.DragShadowBuilder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeProvider;
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
   static final ViewCompat.ViewCompatBaseImpl IMPL;
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

   static {
      if (VERSION.SDK_INT >= 26) {
         IMPL = new ViewCompat.ViewCompatApi26Impl();
      } else if (VERSION.SDK_INT >= 24) {
         IMPL = new ViewCompat.ViewCompatApi24Impl();
      } else if (VERSION.SDK_INT >= 23) {
         IMPL = new ViewCompat.ViewCompatApi23Impl();
      } else if (VERSION.SDK_INT >= 21) {
         IMPL = new ViewCompat.ViewCompatApi21Impl();
      } else if (VERSION.SDK_INT >= 19) {
         IMPL = new ViewCompat.ViewCompatApi19Impl();
      } else if (VERSION.SDK_INT >= 18) {
         IMPL = new ViewCompat.ViewCompatApi18Impl();
      } else if (VERSION.SDK_INT >= 17) {
         IMPL = new ViewCompat.ViewCompatApi17Impl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new ViewCompat.ViewCompatApi16Impl();
      } else if (VERSION.SDK_INT >= 15) {
         IMPL = new ViewCompat.ViewCompatApi15Impl();
      } else {
         IMPL = new ViewCompat.ViewCompatBaseImpl();
      }
   }

   protected ViewCompat() {
   }

   public static void addKeyboardNavigationClusters(@NonNull View var0, @NonNull Collection var1, int var2) {
      IMPL.addKeyboardNavigationClusters(var0, var1, var2);
   }

   public static ViewPropertyAnimatorCompat animate(View var0) {
      return IMPL.animate(var0);
   }

   @Deprecated
   public static boolean canScrollHorizontally(View var0, int var1) {
      return var0.canScrollHorizontally(var1);
   }

   @Deprecated
   public static boolean canScrollVertically(View var0, int var1) {
      return var0.canScrollVertically(var1);
   }

   public static void cancelDragAndDrop(View var0) {
      IMPL.cancelDragAndDrop(var0);
   }

   @Deprecated
   public static int combineMeasuredStates(int var0, int var1) {
      return View.combineMeasuredStates(var0, var1);
   }

   public static WindowInsetsCompat dispatchApplyWindowInsets(View var0, WindowInsetsCompat var1) {
      return IMPL.dispatchApplyWindowInsets(var0, var1);
   }

   public static void dispatchFinishTemporaryDetach(View var0) {
      IMPL.dispatchFinishTemporaryDetach(var0);
   }

   public static boolean dispatchNestedFling(@NonNull View var0, float var1, float var2, boolean var3) {
      return IMPL.dispatchNestedFling(var0, var1, var2, var3);
   }

   public static boolean dispatchNestedPreFling(@NonNull View var0, float var1, float var2) {
      return IMPL.dispatchNestedPreFling(var0, var1, var2);
   }

   public static boolean dispatchNestedPreScroll(@NonNull View var0, int var1, int var2, @Nullable int[] var3, @Nullable int[] var4) {
      return IMPL.dispatchNestedPreScroll(var0, var1, var2, var3, var4);
   }

   public static boolean dispatchNestedPreScroll(@NonNull View var0, int var1, int var2, @Nullable int[] var3, @Nullable int[] var4, int var5) {
      if (var0 instanceof NestedScrollingChild2) {
         return ((NestedScrollingChild2)var0).dispatchNestedPreScroll(var1, var2, var3, var4, var5);
      } else {
         return var5 == 0 ? IMPL.dispatchNestedPreScroll(var0, var1, var2, var3, var4) : false;
      }
   }

   public static boolean dispatchNestedScroll(@NonNull View var0, int var1, int var2, int var3, int var4, @Nullable int[] var5) {
      return IMPL.dispatchNestedScroll(var0, var1, var2, var3, var4, var5);
   }

   public static boolean dispatchNestedScroll(@NonNull View var0, int var1, int var2, int var3, int var4, @Nullable int[] var5, int var6) {
      if (var0 instanceof NestedScrollingChild2) {
         return ((NestedScrollingChild2)var0).dispatchNestedScroll(var1, var2, var3, var4, var5, var6);
      } else {
         return var6 == 0 ? IMPL.dispatchNestedScroll(var0, var1, var2, var3, var4, var5) : false;
      }
   }

   public static void dispatchStartTemporaryDetach(View var0) {
      IMPL.dispatchStartTemporaryDetach(var0);
   }

   public static int getAccessibilityLiveRegion(View var0) {
      return IMPL.getAccessibilityLiveRegion(var0);
   }

   public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var0) {
      return IMPL.getAccessibilityNodeProvider(var0);
   }

   @Deprecated
   public static float getAlpha(View var0) {
      return var0.getAlpha();
   }

   public static ColorStateList getBackgroundTintList(View var0) {
      return IMPL.getBackgroundTintList(var0);
   }

   public static Mode getBackgroundTintMode(View var0) {
      return IMPL.getBackgroundTintMode(var0);
   }

   public static Rect getClipBounds(View var0) {
      return IMPL.getClipBounds(var0);
   }

   public static Display getDisplay(@NonNull View var0) {
      return IMPL.getDisplay(var0);
   }

   public static float getElevation(View var0) {
      return IMPL.getElevation(var0);
   }

   public static boolean getFitsSystemWindows(View var0) {
      return IMPL.getFitsSystemWindows(var0);
   }

   public static int getImportantForAccessibility(View var0) {
      return IMPL.getImportantForAccessibility(var0);
   }

   public static int getLabelFor(View var0) {
      return IMPL.getLabelFor(var0);
   }

   @Deprecated
   public static int getLayerType(View var0) {
      return var0.getLayerType();
   }

   public static int getLayoutDirection(View var0) {
      return IMPL.getLayoutDirection(var0);
   }

   @Deprecated
   @Nullable
   public static Matrix getMatrix(View var0) {
      return var0.getMatrix();
   }

   @Deprecated
   public static int getMeasuredHeightAndState(View var0) {
      return var0.getMeasuredHeightAndState();
   }

   @Deprecated
   public static int getMeasuredState(View var0) {
      return var0.getMeasuredState();
   }

   @Deprecated
   public static int getMeasuredWidthAndState(View var0) {
      return var0.getMeasuredWidthAndState();
   }

   public static int getMinimumHeight(View var0) {
      return IMPL.getMinimumHeight(var0);
   }

   public static int getMinimumWidth(View var0) {
      return IMPL.getMinimumWidth(var0);
   }

   public static int getNextClusterForwardId(@NonNull View var0) {
      return IMPL.getNextClusterForwardId(var0);
   }

   @Deprecated
   public static int getOverScrollMode(View var0) {
      return var0.getOverScrollMode();
   }

   public static int getPaddingEnd(View var0) {
      return IMPL.getPaddingEnd(var0);
   }

   public static int getPaddingStart(View var0) {
      return IMPL.getPaddingStart(var0);
   }

   public static ViewParent getParentForAccessibility(View var0) {
      return IMPL.getParentForAccessibility(var0);
   }

   @Deprecated
   public static float getPivotX(View var0) {
      return var0.getPivotX();
   }

   @Deprecated
   public static float getPivotY(View var0) {
      return var0.getPivotY();
   }

   @Deprecated
   public static float getRotation(View var0) {
      return var0.getRotation();
   }

   @Deprecated
   public static float getRotationX(View var0) {
      return var0.getRotationX();
   }

   @Deprecated
   public static float getRotationY(View var0) {
      return var0.getRotationY();
   }

   @Deprecated
   public static float getScaleX(View var0) {
      return var0.getScaleX();
   }

   @Deprecated
   public static float getScaleY(View var0) {
      return var0.getScaleY();
   }

   public static int getScrollIndicators(@NonNull View var0) {
      return IMPL.getScrollIndicators(var0);
   }

   public static String getTransitionName(View var0) {
      return IMPL.getTransitionName(var0);
   }

   @Deprecated
   public static float getTranslationX(View var0) {
      return var0.getTranslationX();
   }

   @Deprecated
   public static float getTranslationY(View var0) {
      return var0.getTranslationY();
   }

   public static float getTranslationZ(View var0) {
      return IMPL.getTranslationZ(var0);
   }

   public static int getWindowSystemUiVisibility(View var0) {
      return IMPL.getWindowSystemUiVisibility(var0);
   }

   @Deprecated
   public static float getX(View var0) {
      return var0.getX();
   }

   @Deprecated
   public static float getY(View var0) {
      return var0.getY();
   }

   public static float getZ(View var0) {
      return IMPL.getZ(var0);
   }

   public static boolean hasAccessibilityDelegate(View var0) {
      return IMPL.hasAccessibilityDelegate(var0);
   }

   public static boolean hasExplicitFocusable(@NonNull View var0) {
      return IMPL.hasExplicitFocusable(var0);
   }

   public static boolean hasNestedScrollingParent(@NonNull View var0) {
      return IMPL.hasNestedScrollingParent(var0);
   }

   public static boolean hasNestedScrollingParent(@NonNull View var0, int var1) {
      if (var0 instanceof NestedScrollingChild2) {
         ((NestedScrollingChild2)var0).hasNestedScrollingParent(var1);
      } else if (var1 == 0) {
         return IMPL.hasNestedScrollingParent(var0);
      }

      return false;
   }

   public static boolean hasOnClickListeners(View var0) {
      return IMPL.hasOnClickListeners(var0);
   }

   public static boolean hasOverlappingRendering(View var0) {
      return IMPL.hasOverlappingRendering(var0);
   }

   public static boolean hasTransientState(View var0) {
      return IMPL.hasTransientState(var0);
   }

   public static boolean isAttachedToWindow(View var0) {
      return IMPL.isAttachedToWindow(var0);
   }

   public static boolean isFocusedByDefault(@NonNull View var0) {
      return IMPL.isFocusedByDefault(var0);
   }

   public static boolean isImportantForAccessibility(View var0) {
      return IMPL.isImportantForAccessibility(var0);
   }

   public static boolean isInLayout(View var0) {
      return IMPL.isInLayout(var0);
   }

   public static boolean isKeyboardNavigationCluster(@NonNull View var0) {
      return IMPL.isKeyboardNavigationCluster(var0);
   }

   public static boolean isLaidOut(View var0) {
      return IMPL.isLaidOut(var0);
   }

   public static boolean isLayoutDirectionResolved(View var0) {
      return IMPL.isLayoutDirectionResolved(var0);
   }

   public static boolean isNestedScrollingEnabled(@NonNull View var0) {
      return IMPL.isNestedScrollingEnabled(var0);
   }

   @Deprecated
   public static boolean isOpaque(View var0) {
      return var0.isOpaque();
   }

   public static boolean isPaddingRelative(View var0) {
      return IMPL.isPaddingRelative(var0);
   }

   @Deprecated
   public static void jumpDrawablesToCurrentState(View var0) {
      var0.jumpDrawablesToCurrentState();
   }

   public static View keyboardNavigationClusterSearch(@NonNull View var0, View var1, int var2) {
      return IMPL.keyboardNavigationClusterSearch(var0, var1, var2);
   }

   public static void offsetLeftAndRight(View var0, int var1) {
      IMPL.offsetLeftAndRight(var0, var1);
   }

   public static void offsetTopAndBottom(View var0, int var1) {
      IMPL.offsetTopAndBottom(var0, var1);
   }

   public static WindowInsetsCompat onApplyWindowInsets(View var0, WindowInsetsCompat var1) {
      return IMPL.onApplyWindowInsets(var0, var1);
   }

   @Deprecated
   public static void onInitializeAccessibilityEvent(View var0, AccessibilityEvent var1) {
      var0.onInitializeAccessibilityEvent(var1);
   }

   public static void onInitializeAccessibilityNodeInfo(View var0, AccessibilityNodeInfoCompat var1) {
      IMPL.onInitializeAccessibilityNodeInfo(var0, var1);
   }

   @Deprecated
   public static void onPopulateAccessibilityEvent(View var0, AccessibilityEvent var1) {
      var0.onPopulateAccessibilityEvent(var1);
   }

   public static boolean performAccessibilityAction(View var0, int var1, Bundle var2) {
      return IMPL.performAccessibilityAction(var0, var1, var2);
   }

   public static void postInvalidateOnAnimation(View var0) {
      IMPL.postInvalidateOnAnimation(var0);
   }

   public static void postInvalidateOnAnimation(View var0, int var1, int var2, int var3, int var4) {
      IMPL.postInvalidateOnAnimation(var0, var1, var2, var3, var4);
   }

   public static void postOnAnimation(View var0, Runnable var1) {
      IMPL.postOnAnimation(var0, var1);
   }

   public static void postOnAnimationDelayed(View var0, Runnable var1, long var2) {
      IMPL.postOnAnimationDelayed(var0, var1, var2);
   }

   public static void requestApplyInsets(View var0) {
      IMPL.requestApplyInsets(var0);
   }

   @Deprecated
   public static int resolveSizeAndState(int var0, int var1, int var2) {
      return View.resolveSizeAndState(var0, var1, var2);
   }

   public static boolean restoreDefaultFocus(@NonNull View var0) {
      return IMPL.restoreDefaultFocus(var0);
   }

   public static void setAccessibilityDelegate(View var0, AccessibilityDelegateCompat var1) {
      IMPL.setAccessibilityDelegate(var0, var1);
   }

   public static void setAccessibilityLiveRegion(View var0, int var1) {
      IMPL.setAccessibilityLiveRegion(var0, var1);
   }

   @Deprecated
   public static void setActivated(View var0, boolean var1) {
      var0.setActivated(var1);
   }

   @Deprecated
   public static void setAlpha(View var0, @FloatRange(from = 0.0D,to = 1.0D) float var1) {
      var0.setAlpha(var1);
   }

   public static void setBackground(View var0, Drawable var1) {
      IMPL.setBackground(var0, var1);
   }

   public static void setBackgroundTintList(View var0, ColorStateList var1) {
      IMPL.setBackgroundTintList(var0, var1);
   }

   public static void setBackgroundTintMode(View var0, Mode var1) {
      IMPL.setBackgroundTintMode(var0, var1);
   }

   public static void setChildrenDrawingOrderEnabled(ViewGroup var0, boolean var1) {
      IMPL.setChildrenDrawingOrderEnabled(var0, var1);
   }

   public static void setClipBounds(View var0, Rect var1) {
      IMPL.setClipBounds(var0, var1);
   }

   public static void setElevation(View var0, float var1) {
      IMPL.setElevation(var0, var1);
   }

   @Deprecated
   public static void setFitsSystemWindows(View var0, boolean var1) {
      var0.setFitsSystemWindows(var1);
   }

   public static void setFocusedByDefault(@NonNull View var0, boolean var1) {
      IMPL.setFocusedByDefault(var0, var1);
   }

   public static void setHasTransientState(View var0, boolean var1) {
      IMPL.setHasTransientState(var0, var1);
   }

   public static void setImportantForAccessibility(View var0, int var1) {
      IMPL.setImportantForAccessibility(var0, var1);
   }

   public static void setKeyboardNavigationCluster(@NonNull View var0, boolean var1) {
      IMPL.setKeyboardNavigationCluster(var0, var1);
   }

   public static void setLabelFor(View var0, @IdRes int var1) {
      IMPL.setLabelFor(var0, var1);
   }

   public static void setLayerPaint(View var0, Paint var1) {
      IMPL.setLayerPaint(var0, var1);
   }

   @Deprecated
   public static void setLayerType(View var0, int var1, Paint var2) {
      var0.setLayerType(var1, var2);
   }

   public static void setLayoutDirection(View var0, int var1) {
      IMPL.setLayoutDirection(var0, var1);
   }

   public static void setNestedScrollingEnabled(@NonNull View var0, boolean var1) {
      IMPL.setNestedScrollingEnabled(var0, var1);
   }

   public static void setNextClusterForwardId(@NonNull View var0, int var1) {
      IMPL.setNextClusterForwardId(var0, var1);
   }

   public static void setOnApplyWindowInsetsListener(View var0, OnApplyWindowInsetsListener var1) {
      IMPL.setOnApplyWindowInsetsListener(var0, var1);
   }

   @Deprecated
   public static void setOverScrollMode(View var0, int var1) {
      var0.setOverScrollMode(var1);
   }

   public static void setPaddingRelative(View var0, int var1, int var2, int var3, int var4) {
      IMPL.setPaddingRelative(var0, var1, var2, var3, var4);
   }

   @Deprecated
   public static void setPivotX(View var0, float var1) {
      var0.setPivotX(var1);
   }

   @Deprecated
   public static void setPivotY(View var0, float var1) {
      var0.setPivotY(var1);
   }

   public static void setPointerIcon(@NonNull View var0, PointerIconCompat var1) {
      IMPL.setPointerIcon(var0, var1);
   }

   @Deprecated
   public static void setRotation(View var0, float var1) {
      var0.setRotation(var1);
   }

   @Deprecated
   public static void setRotationX(View var0, float var1) {
      var0.setRotationX(var1);
   }

   @Deprecated
   public static void setRotationY(View var0, float var1) {
      var0.setRotationY(var1);
   }

   @Deprecated
   public static void setSaveFromParentEnabled(View var0, boolean var1) {
      var0.setSaveFromParentEnabled(var1);
   }

   @Deprecated
   public static void setScaleX(View var0, float var1) {
      var0.setScaleX(var1);
   }

   @Deprecated
   public static void setScaleY(View var0, float var1) {
      var0.setScaleY(var1);
   }

   public static void setScrollIndicators(@NonNull View var0, int var1) {
      IMPL.setScrollIndicators(var0, var1);
   }

   public static void setScrollIndicators(@NonNull View var0, int var1, int var2) {
      IMPL.setScrollIndicators(var0, var1, var2);
   }

   public static void setTooltipText(@NonNull View var0, @Nullable CharSequence var1) {
      IMPL.setTooltipText(var0, var1);
   }

   public static void setTransitionName(View var0, String var1) {
      IMPL.setTransitionName(var0, var1);
   }

   @Deprecated
   public static void setTranslationX(View var0, float var1) {
      var0.setTranslationX(var1);
   }

   @Deprecated
   public static void setTranslationY(View var0, float var1) {
      var0.setTranslationY(var1);
   }

   public static void setTranslationZ(View var0, float var1) {
      IMPL.setTranslationZ(var0, var1);
   }

   @Deprecated
   public static void setX(View var0, float var1) {
      var0.setX(var1);
   }

   @Deprecated
   public static void setY(View var0, float var1) {
      var0.setY(var1);
   }

   public static void setZ(View var0, float var1) {
      IMPL.setZ(var0, var1);
   }

   public static boolean startDragAndDrop(View var0, ClipData var1, DragShadowBuilder var2, Object var3, int var4) {
      return IMPL.startDragAndDrop(var0, var1, var2, var3, var4);
   }

   public static boolean startNestedScroll(@NonNull View var0, int var1) {
      return IMPL.startNestedScroll(var0, var1);
   }

   public static boolean startNestedScroll(@NonNull View var0, int var1, int var2) {
      if (var0 instanceof NestedScrollingChild2) {
         return ((NestedScrollingChild2)var0).startNestedScroll(var1, var2);
      } else {
         return var2 == 0 ? IMPL.startNestedScroll(var0, var1) : false;
      }
   }

   public static void stopNestedScroll(@NonNull View var0) {
      IMPL.stopNestedScroll(var0);
   }

   public static void stopNestedScroll(@NonNull View var0, int var1) {
      if (var0 instanceof NestedScrollingChild2) {
         ((NestedScrollingChild2)var0).stopNestedScroll(var1);
      } else {
         if (var1 == 0) {
            IMPL.stopNestedScroll(var0);
         }

      }
   }

   public static void updateDragShadow(View var0, DragShadowBuilder var1) {
      IMPL.updateDragShadow(var0, var1);
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface AccessibilityLiveRegion {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface FocusDirection {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface FocusRealDirection {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface FocusRelativeDirection {
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface ImportantForAccessibility {
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface LayerType {
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface LayoutDirectionMode {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface NestedScrollType {
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface OverScroll {
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface ResolvedLayoutDirectionMode {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface ScrollAxis {
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface ScrollIndicators {
   }

   @RequiresApi(15)
   static class ViewCompatApi15Impl extends ViewCompat.ViewCompatBaseImpl {
      public boolean hasOnClickListeners(View var1) {
         return var1.hasOnClickListeners();
      }
   }

   @RequiresApi(16)
   static class ViewCompatApi16Impl extends ViewCompat.ViewCompatApi15Impl {
      public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
         AccessibilityNodeProvider var2 = var1.getAccessibilityNodeProvider();
         return var2 != null ? new AccessibilityNodeProviderCompat(var2) : null;
      }

      public boolean getFitsSystemWindows(View var1) {
         return var1.getFitsSystemWindows();
      }

      public int getImportantForAccessibility(View var1) {
         return var1.getImportantForAccessibility();
      }

      public int getMinimumHeight(View var1) {
         return var1.getMinimumHeight();
      }

      public int getMinimumWidth(View var1) {
         return var1.getMinimumWidth();
      }

      public ViewParent getParentForAccessibility(View var1) {
         return var1.getParentForAccessibility();
      }

      public boolean hasOverlappingRendering(View var1) {
         return var1.hasOverlappingRendering();
      }

      public boolean hasTransientState(View var1) {
         return var1.hasTransientState();
      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         return var1.performAccessibilityAction(var2, var3);
      }

      public void postInvalidateOnAnimation(View var1) {
         var1.postInvalidateOnAnimation();
      }

      public void postInvalidateOnAnimation(View var1, int var2, int var3, int var4, int var5) {
         var1.postInvalidateOnAnimation(var2, var3, var4, var5);
      }

      public void postOnAnimation(View var1, Runnable var2) {
         var1.postOnAnimation(var2);
      }

      public void postOnAnimationDelayed(View var1, Runnable var2, long var3) {
         var1.postOnAnimationDelayed(var2, var3);
      }

      public void requestApplyInsets(View var1) {
         var1.requestFitSystemWindows();
      }

      public void setBackground(View var1, Drawable var2) {
         var1.setBackground(var2);
      }

      public void setHasTransientState(View var1, boolean var2) {
         var1.setHasTransientState(var2);
      }

      public void setImportantForAccessibility(View var1, int var2) {
         int var3 = var2;
         if (var2 == 4) {
            var3 = 2;
         }

         var1.setImportantForAccessibility(var3);
      }
   }

   @RequiresApi(17)
   static class ViewCompatApi17Impl extends ViewCompat.ViewCompatApi16Impl {
      public Display getDisplay(View var1) {
         return var1.getDisplay();
      }

      public int getLabelFor(View var1) {
         return var1.getLabelFor();
      }

      public int getLayoutDirection(View var1) {
         return var1.getLayoutDirection();
      }

      public int getPaddingEnd(View var1) {
         return var1.getPaddingEnd();
      }

      public int getPaddingStart(View var1) {
         return var1.getPaddingStart();
      }

      public int getWindowSystemUiVisibility(View var1) {
         return var1.getWindowSystemUiVisibility();
      }

      public boolean isPaddingRelative(View var1) {
         return var1.isPaddingRelative();
      }

      public void setLabelFor(View var1, int var2) {
         var1.setLabelFor(var2);
      }

      public void setLayerPaint(View var1, Paint var2) {
         var1.setLayerPaint(var2);
      }

      public void setLayoutDirection(View var1, int var2) {
         var1.setLayoutDirection(var2);
      }

      public void setPaddingRelative(View var1, int var2, int var3, int var4, int var5) {
         var1.setPaddingRelative(var2, var3, var4, var5);
      }
   }

   @RequiresApi(18)
   static class ViewCompatApi18Impl extends ViewCompat.ViewCompatApi17Impl {
      public Rect getClipBounds(View var1) {
         return var1.getClipBounds();
      }

      public boolean isInLayout(View var1) {
         return var1.isInLayout();
      }

      public void setClipBounds(View var1, Rect var2) {
         var1.setClipBounds(var2);
      }
   }

   @RequiresApi(19)
   static class ViewCompatApi19Impl extends ViewCompat.ViewCompatApi18Impl {
      public int getAccessibilityLiveRegion(View var1) {
         return var1.getAccessibilityLiveRegion();
      }

      public boolean isAttachedToWindow(View var1) {
         return var1.isAttachedToWindow();
      }

      public boolean isLaidOut(View var1) {
         return var1.isLaidOut();
      }

      public boolean isLayoutDirectionResolved(View var1) {
         return var1.isLayoutDirectionResolved();
      }

      public void setAccessibilityLiveRegion(View var1, int var2) {
         var1.setAccessibilityLiveRegion(var2);
      }

      public void setImportantForAccessibility(View var1, int var2) {
         var1.setImportantForAccessibility(var2);
      }
   }

   @RequiresApi(21)
   static class ViewCompatApi21Impl extends ViewCompat.ViewCompatApi19Impl {
      private static ThreadLocal sThreadLocalRect;

      private static Rect getEmptyTempRect() {
         if (sThreadLocalRect == null) {
            sThreadLocalRect = new ThreadLocal();
         }

         Rect var1 = (Rect)sThreadLocalRect.get();
         Rect var0 = var1;
         if (var1 == null) {
            var0 = new Rect();
            sThreadLocalRect.set(var0);
         }

         var0.setEmpty();
         return var0;
      }

      public WindowInsetsCompat dispatchApplyWindowInsets(View var1, WindowInsetsCompat var2) {
         WindowInsets var5 = (WindowInsets)WindowInsetsCompat.unwrap(var2);
         WindowInsets var3 = var1.dispatchApplyWindowInsets(var5);
         WindowInsets var4 = var5;
         if (var3 != var5) {
            var4 = new WindowInsets(var3);
         }

         return WindowInsetsCompat.wrap(var4);
      }

      public boolean dispatchNestedFling(View var1, float var2, float var3, boolean var4) {
         return var1.dispatchNestedFling(var2, var3, var4);
      }

      public boolean dispatchNestedPreFling(View var1, float var2, float var3) {
         return var1.dispatchNestedPreFling(var2, var3);
      }

      public boolean dispatchNestedPreScroll(View var1, int var2, int var3, int[] var4, int[] var5) {
         return var1.dispatchNestedPreScroll(var2, var3, var4, var5);
      }

      public boolean dispatchNestedScroll(View var1, int var2, int var3, int var4, int var5, int[] var6) {
         return var1.dispatchNestedScroll(var2, var3, var4, var5, var6);
      }

      public ColorStateList getBackgroundTintList(View var1) {
         return var1.getBackgroundTintList();
      }

      public Mode getBackgroundTintMode(View var1) {
         return var1.getBackgroundTintMode();
      }

      public float getElevation(View var1) {
         return var1.getElevation();
      }

      public String getTransitionName(View var1) {
         return var1.getTransitionName();
      }

      public float getTranslationZ(View var1) {
         return var1.getTranslationZ();
      }

      public float getZ(View var1) {
         return var1.getZ();
      }

      public boolean hasNestedScrollingParent(View var1) {
         return var1.hasNestedScrollingParent();
      }

      public boolean isImportantForAccessibility(View var1) {
         return var1.isImportantForAccessibility();
      }

      public boolean isNestedScrollingEnabled(View var1) {
         return var1.isNestedScrollingEnabled();
      }

      public void offsetLeftAndRight(View var1, int var2) {
         Rect var4 = getEmptyTempRect();
         boolean var3 = false;
         ViewParent var5 = var1.getParent();
         if (var5 instanceof View) {
            View var6 = (View)var5;
            var4.set(var6.getLeft(), var6.getTop(), var6.getRight(), var6.getBottom());
            var3 = var4.intersects(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom()) ^ true;
         }

         super.offsetLeftAndRight(var1, var2);
         if (var3 && var4.intersect(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom())) {
            ((View)var5).invalidate(var4);
         }

      }

      public void offsetTopAndBottom(View var1, int var2) {
         Rect var4 = getEmptyTempRect();
         boolean var3 = false;
         ViewParent var5 = var1.getParent();
         if (var5 instanceof View) {
            View var6 = (View)var5;
            var4.set(var6.getLeft(), var6.getTop(), var6.getRight(), var6.getBottom());
            var3 = var4.intersects(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom()) ^ true;
         }

         super.offsetTopAndBottom(var1, var2);
         if (var3 && var4.intersect(var1.getLeft(), var1.getTop(), var1.getRight(), var1.getBottom())) {
            ((View)var5).invalidate(var4);
         }

      }

      public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
         WindowInsets var5 = (WindowInsets)WindowInsetsCompat.unwrap(var2);
         WindowInsets var3 = var1.onApplyWindowInsets(var5);
         WindowInsets var4 = var5;
         if (var3 != var5) {
            var4 = new WindowInsets(var3);
         }

         return WindowInsetsCompat.wrap(var4);
      }

      public void requestApplyInsets(View var1) {
         var1.requestApplyInsets();
      }

      public void setBackgroundTintList(View var1, ColorStateList var2) {
         var1.setBackgroundTintList(var2);
         if (VERSION.SDK_INT == 21) {
            Drawable var4 = var1.getBackground();
            boolean var3;
            if (var1.getBackgroundTintList() != null && var1.getBackgroundTintMode() != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            if (var4 != null && var3) {
               if (var4.isStateful()) {
                  var4.setState(var1.getDrawableState());
               }

               var1.setBackground(var4);
            }
         }

      }

      public void setBackgroundTintMode(View var1, Mode var2) {
         var1.setBackgroundTintMode(var2);
         if (VERSION.SDK_INT == 21) {
            Drawable var4 = var1.getBackground();
            boolean var3;
            if (var1.getBackgroundTintList() != null && var1.getBackgroundTintMode() != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            if (var4 != null && var3) {
               if (var4.isStateful()) {
                  var4.setState(var1.getDrawableState());
               }

               var1.setBackground(var4);
            }
         }

      }

      public void setElevation(View var1, float var2) {
         var1.setElevation(var2);
      }

      public void setNestedScrollingEnabled(View var1, boolean var2) {
         var1.setNestedScrollingEnabled(var2);
      }

      public void setOnApplyWindowInsetsListener(View var1, final OnApplyWindowInsetsListener var2) {
         if (var2 == null) {
            var1.setOnApplyWindowInsetsListener((android.view.View.OnApplyWindowInsetsListener)null);
         } else {
            var1.setOnApplyWindowInsetsListener(new android.view.View.OnApplyWindowInsetsListener() {
               public WindowInsets onApplyWindowInsets(View var1, WindowInsets var2x) {
                  WindowInsetsCompat var3 = WindowInsetsCompat.wrap(var2x);
                  return (WindowInsets)WindowInsetsCompat.unwrap(var2.onApplyWindowInsets(var1, var3));
               }
            });
         }
      }

      public void setTransitionName(View var1, String var2) {
         var1.setTransitionName(var2);
      }

      public void setTranslationZ(View var1, float var2) {
         var1.setTranslationZ(var2);
      }

      public void setZ(View var1, float var2) {
         var1.setZ(var2);
      }

      public boolean startNestedScroll(View var1, int var2) {
         return var1.startNestedScroll(var2);
      }

      public void stopNestedScroll(View var1) {
         var1.stopNestedScroll();
      }
   }

   @RequiresApi(23)
   static class ViewCompatApi23Impl extends ViewCompat.ViewCompatApi21Impl {
      public int getScrollIndicators(View var1) {
         return var1.getScrollIndicators();
      }

      public void offsetLeftAndRight(View var1, int var2) {
         var1.offsetLeftAndRight(var2);
      }

      public void offsetTopAndBottom(View var1, int var2) {
         var1.offsetTopAndBottom(var2);
      }

      public void setScrollIndicators(View var1, int var2) {
         var1.setScrollIndicators(var2);
      }

      public void setScrollIndicators(View var1, int var2, int var3) {
         var1.setScrollIndicators(var2, var3);
      }
   }

   @RequiresApi(24)
   static class ViewCompatApi24Impl extends ViewCompat.ViewCompatApi23Impl {
      public void cancelDragAndDrop(View var1) {
         var1.cancelDragAndDrop();
      }

      public void dispatchFinishTemporaryDetach(View var1) {
         var1.dispatchFinishTemporaryDetach();
      }

      public void dispatchStartTemporaryDetach(View var1) {
         var1.dispatchStartTemporaryDetach();
      }

      public void setPointerIcon(View var1, PointerIconCompat var2) {
         Object var3;
         if (var2 != null) {
            var3 = var2.getPointerIcon();
         } else {
            var3 = null;
         }

         var1.setPointerIcon((PointerIcon)((PointerIcon)var3));
      }

      public boolean startDragAndDrop(View var1, ClipData var2, DragShadowBuilder var3, Object var4, int var5) {
         return var1.startDragAndDrop(var2, var3, var4, var5);
      }

      public void updateDragShadow(View var1, DragShadowBuilder var2) {
         var1.updateDragShadow(var2);
      }
   }

   @RequiresApi(26)
   static class ViewCompatApi26Impl extends ViewCompat.ViewCompatApi24Impl {
      public void addKeyboardNavigationClusters(@NonNull View var1, @NonNull Collection var2, int var3) {
         var1.addKeyboardNavigationClusters(var2, var3);
      }

      public int getNextClusterForwardId(@NonNull View var1) {
         return var1.getNextClusterForwardId();
      }

      public boolean hasExplicitFocusable(@NonNull View var1) {
         return var1.hasExplicitFocusable();
      }

      public boolean isFocusedByDefault(@NonNull View var1) {
         return var1.isFocusedByDefault();
      }

      public boolean isKeyboardNavigationCluster(@NonNull View var1) {
         return var1.isKeyboardNavigationCluster();
      }

      public View keyboardNavigationClusterSearch(@NonNull View var1, View var2, int var3) {
         return var1.keyboardNavigationClusterSearch(var2, var3);
      }

      public boolean restoreDefaultFocus(@NonNull View var1) {
         return var1.restoreDefaultFocus();
      }

      public void setFocusedByDefault(@NonNull View var1, boolean var2) {
         var1.setFocusedByDefault(var2);
      }

      public void setKeyboardNavigationCluster(@NonNull View var1, boolean var2) {
         var1.setKeyboardNavigationCluster(var2);
      }

      public void setNextClusterForwardId(@NonNull View var1, int var2) {
         var1.setNextClusterForwardId(var2);
      }

      public void setTooltipText(View var1, CharSequence var2) {
         var1.setTooltipText(var2);
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
      private static WeakHashMap sTransitionNameMap;
      private Method mDispatchFinishTemporaryDetach;
      private Method mDispatchStartTemporaryDetach;
      private boolean mTempDetachBound;
      WeakHashMap mViewPropertyAnimatorCompatMap = null;

      private void bindTempDetach() {
         try {
            this.mDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach");
            this.mDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach");
         } catch (NoSuchMethodException var2) {
            Log.e("ViewCompat", "Couldn't find method", var2);
         }

         this.mTempDetachBound = true;
      }

      private static void tickleInvalidationFlag(View var0) {
         float var1 = var0.getTranslationY();
         var0.setTranslationY(1.0F + var1);
         var0.setTranslationY(var1);
      }

      public void addKeyboardNavigationClusters(@NonNull View var1, @NonNull Collection var2, int var3) {
      }

      public ViewPropertyAnimatorCompat animate(View var1) {
         if (this.mViewPropertyAnimatorCompatMap == null) {
            this.mViewPropertyAnimatorCompatMap = new WeakHashMap();
         }

         ViewPropertyAnimatorCompat var3 = (ViewPropertyAnimatorCompat)this.mViewPropertyAnimatorCompatMap.get(var1);
         ViewPropertyAnimatorCompat var2 = var3;
         if (var3 == null) {
            var2 = new ViewPropertyAnimatorCompat(var1);
            this.mViewPropertyAnimatorCompatMap.put(var1, var2);
         }

         return var2;
      }

      public void cancelDragAndDrop(View var1) {
      }

      public WindowInsetsCompat dispatchApplyWindowInsets(View var1, WindowInsetsCompat var2) {
         return var2;
      }

      public void dispatchFinishTemporaryDetach(View var1) {
         if (!this.mTempDetachBound) {
            this.bindTempDetach();
         }

         Method var2 = this.mDispatchFinishTemporaryDetach;
         if (var2 != null) {
            try {
               var2.invoke(var1);
            } catch (Exception var3) {
               Log.d("ViewCompat", "Error calling dispatchFinishTemporaryDetach", var3);
            }

         } else {
            var1.onFinishTemporaryDetach();
         }
      }

      public boolean dispatchNestedFling(View var1, float var2, float var3, boolean var4) {
         return var1 instanceof NestedScrollingChild ? ((NestedScrollingChild)var1).dispatchNestedFling(var2, var3, var4) : false;
      }

      public boolean dispatchNestedPreFling(View var1, float var2, float var3) {
         return var1 instanceof NestedScrollingChild ? ((NestedScrollingChild)var1).dispatchNestedPreFling(var2, var3) : false;
      }

      public boolean dispatchNestedPreScroll(View var1, int var2, int var3, int[] var4, int[] var5) {
         return var1 instanceof NestedScrollingChild ? ((NestedScrollingChild)var1).dispatchNestedPreScroll(var2, var3, var4, var5) : false;
      }

      public boolean dispatchNestedScroll(View var1, int var2, int var3, int var4, int var5, int[] var6) {
         return var1 instanceof NestedScrollingChild ? ((NestedScrollingChild)var1).dispatchNestedScroll(var2, var3, var4, var5, var6) : false;
      }

      public void dispatchStartTemporaryDetach(View var1) {
         if (!this.mTempDetachBound) {
            this.bindTempDetach();
         }

         Method var2 = this.mDispatchStartTemporaryDetach;
         if (var2 != null) {
            try {
               var2.invoke(var1);
            } catch (Exception var3) {
               Log.d("ViewCompat", "Error calling dispatchStartTemporaryDetach", var3);
            }

         } else {
            var1.onStartTemporaryDetach();
         }
      }

      public int getAccessibilityLiveRegion(View var1) {
         return 0;
      }

      public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View var1) {
         return null;
      }

      public ColorStateList getBackgroundTintList(View var1) {
         return var1 instanceof TintableBackgroundView ? ((TintableBackgroundView)var1).getSupportBackgroundTintList() : null;
      }

      public Mode getBackgroundTintMode(View var1) {
         return var1 instanceof TintableBackgroundView ? ((TintableBackgroundView)var1).getSupportBackgroundTintMode() : null;
      }

      public Rect getClipBounds(View var1) {
         return null;
      }

      public Display getDisplay(View var1) {
         return this.isAttachedToWindow(var1) ? ((WindowManager)var1.getContext().getSystemService("window")).getDefaultDisplay() : null;
      }

      public float getElevation(View var1) {
         return 0.0F;
      }

      public boolean getFitsSystemWindows(View var1) {
         return false;
      }

      long getFrameTime() {
         return ValueAnimator.getFrameDelay();
      }

      public int getImportantForAccessibility(View var1) {
         return 0;
      }

      public int getLabelFor(View var1) {
         return 0;
      }

      public int getLayoutDirection(View var1) {
         return 0;
      }

      public int getMinimumHeight(View var1) {
         if (!sMinHeightFieldFetched) {
            try {
               sMinHeightField = View.class.getDeclaredField("mMinHeight");
               sMinHeightField.setAccessible(true);
            } catch (NoSuchFieldException var4) {
            }

            sMinHeightFieldFetched = true;
         }

         Field var3 = sMinHeightField;
         if (var3 != null) {
            try {
               int var2 = (Integer)var3.get(var1);
               return var2;
            } catch (Exception var5) {
            }
         }

         return 0;
      }

      public int getMinimumWidth(View var1) {
         if (!sMinWidthFieldFetched) {
            try {
               sMinWidthField = View.class.getDeclaredField("mMinWidth");
               sMinWidthField.setAccessible(true);
            } catch (NoSuchFieldException var4) {
            }

            sMinWidthFieldFetched = true;
         }

         Field var3 = sMinWidthField;
         if (var3 != null) {
            try {
               int var2 = (Integer)var3.get(var1);
               return var2;
            } catch (Exception var5) {
            }
         }

         return 0;
      }

      public int getNextClusterForwardId(@NonNull View var1) {
         return -1;
      }

      public int getPaddingEnd(View var1) {
         return var1.getPaddingRight();
      }

      public int getPaddingStart(View var1) {
         return var1.getPaddingLeft();
      }

      public ViewParent getParentForAccessibility(View var1) {
         return var1.getParent();
      }

      public int getScrollIndicators(View var1) {
         return 0;
      }

      public String getTransitionName(View var1) {
         WeakHashMap var2 = sTransitionNameMap;
         return var2 == null ? null : (String)var2.get(var1);
      }

      public float getTranslationZ(View var1) {
         return 0.0F;
      }

      public int getWindowSystemUiVisibility(View var1) {
         return 0;
      }

      public float getZ(View var1) {
         return this.getTranslationZ(var1) + this.getElevation(var1);
      }

      public boolean hasAccessibilityDelegate(View var1) {
         boolean var3 = sAccessibilityDelegateCheckFailed;
         boolean var2 = false;
         if (var3) {
            return false;
         } else {
            if (sAccessibilityDelegateField == null) {
               try {
                  sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");
                  sAccessibilityDelegateField.setAccessible(true);
               } catch (Throwable var5) {
                  sAccessibilityDelegateCheckFailed = true;
                  return false;
               }
            }

            Object var6;
            try {
               var6 = sAccessibilityDelegateField.get(var1);
            } catch (Throwable var4) {
               sAccessibilityDelegateCheckFailed = true;
               return false;
            }

            if (var6 != null) {
               var2 = true;
            }

            return var2;
         }
      }

      public boolean hasExplicitFocusable(@NonNull View var1) {
         return var1.hasFocusable();
      }

      public boolean hasNestedScrollingParent(View var1) {
         return var1 instanceof NestedScrollingChild ? ((NestedScrollingChild)var1).hasNestedScrollingParent() : false;
      }

      public boolean hasOnClickListeners(View var1) {
         return false;
      }

      public boolean hasOverlappingRendering(View var1) {
         return true;
      }

      public boolean hasTransientState(View var1) {
         return false;
      }

      public boolean isAttachedToWindow(View var1) {
         return var1.getWindowToken() != null;
      }

      public boolean isFocusedByDefault(@NonNull View var1) {
         return false;
      }

      public boolean isImportantForAccessibility(View var1) {
         return true;
      }

      public boolean isInLayout(View var1) {
         return false;
      }

      public boolean isKeyboardNavigationCluster(@NonNull View var1) {
         return false;
      }

      public boolean isLaidOut(View var1) {
         return var1.getWidth() > 0 && var1.getHeight() > 0;
      }

      public boolean isLayoutDirectionResolved(View var1) {
         return false;
      }

      public boolean isNestedScrollingEnabled(View var1) {
         return var1 instanceof NestedScrollingChild ? ((NestedScrollingChild)var1).isNestedScrollingEnabled() : false;
      }

      public boolean isPaddingRelative(View var1) {
         return false;
      }

      public View keyboardNavigationClusterSearch(@NonNull View var1, View var2, int var3) {
         return null;
      }

      public void offsetLeftAndRight(View var1, int var2) {
         var1.offsetLeftAndRight(var2);
         if (var1.getVisibility() == 0) {
            tickleInvalidationFlag(var1);
            ViewParent var3 = var1.getParent();
            if (var3 instanceof View) {
               tickleInvalidationFlag((View)var3);
            }
         }

      }

      public void offsetTopAndBottom(View var1, int var2) {
         var1.offsetTopAndBottom(var2);
         if (var1.getVisibility() == 0) {
            tickleInvalidationFlag(var1);
            ViewParent var3 = var1.getParent();
            if (var3 instanceof View) {
               tickleInvalidationFlag((View)var3);
            }
         }

      }

      public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
         return var2;
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         var1.onInitializeAccessibilityNodeInfo(var2.unwrap());
      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         return false;
      }

      public void postInvalidateOnAnimation(View var1) {
         var1.postInvalidate();
      }

      public void postInvalidateOnAnimation(View var1, int var2, int var3, int var4, int var5) {
         var1.postInvalidate(var2, var3, var4, var5);
      }

      public void postOnAnimation(View var1, Runnable var2) {
         var1.postDelayed(var2, this.getFrameTime());
      }

      public void postOnAnimationDelayed(View var1, Runnable var2, long var3) {
         var1.postDelayed(var2, this.getFrameTime() + var3);
      }

      public void requestApplyInsets(View var1) {
      }

      public boolean restoreDefaultFocus(@NonNull View var1) {
         return var1.requestFocus();
      }

      public void setAccessibilityDelegate(View var1, @Nullable AccessibilityDelegateCompat var2) {
         AccessibilityDelegate var3;
         if (var2 == null) {
            var3 = null;
         } else {
            var3 = var2.getBridge();
         }

         var1.setAccessibilityDelegate(var3);
      }

      public void setAccessibilityLiveRegion(View var1, int var2) {
      }

      public void setBackground(View var1, Drawable var2) {
         var1.setBackgroundDrawable(var2);
      }

      public void setBackgroundTintList(View var1, ColorStateList var2) {
         if (var1 instanceof TintableBackgroundView) {
            ((TintableBackgroundView)var1).setSupportBackgroundTintList(var2);
         }

      }

      public void setBackgroundTintMode(View var1, Mode var2) {
         if (var1 instanceof TintableBackgroundView) {
            ((TintableBackgroundView)var1).setSupportBackgroundTintMode(var2);
         }

      }

      public void setChildrenDrawingOrderEnabled(ViewGroup var1, boolean var2) {
         if (sChildrenDrawingOrderMethod == null) {
            try {
               sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
            } catch (NoSuchMethodException var7) {
               Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", var7);
            }

            sChildrenDrawingOrderMethod.setAccessible(true);
         }

         try {
            sChildrenDrawingOrderMethod.invoke(var1, var2);
         } catch (IllegalAccessException var4) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", var4);
         } catch (IllegalArgumentException var5) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", var5);
         } catch (InvocationTargetException var6) {
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", var6);
            return;
         }

      }

      public void setClipBounds(View var1, Rect var2) {
      }

      public void setElevation(View var1, float var2) {
      }

      public void setFocusedByDefault(@NonNull View var1, boolean var2) {
      }

      public void setHasTransientState(View var1, boolean var2) {
      }

      public void setImportantForAccessibility(View var1, int var2) {
      }

      public void setKeyboardNavigationCluster(@NonNull View var1, boolean var2) {
      }

      public void setLabelFor(View var1, int var2) {
      }

      public void setLayerPaint(View var1, Paint var2) {
         var1.setLayerType(var1.getLayerType(), var2);
         var1.invalidate();
      }

      public void setLayoutDirection(View var1, int var2) {
      }

      public void setNestedScrollingEnabled(View var1, boolean var2) {
         if (var1 instanceof NestedScrollingChild) {
            ((NestedScrollingChild)var1).setNestedScrollingEnabled(var2);
         }

      }

      public void setNextClusterForwardId(@NonNull View var1, int var2) {
      }

      public void setOnApplyWindowInsetsListener(View var1, OnApplyWindowInsetsListener var2) {
      }

      public void setPaddingRelative(View var1, int var2, int var3, int var4, int var5) {
         var1.setPadding(var2, var3, var4, var5);
      }

      public void setPointerIcon(View var1, PointerIconCompat var2) {
      }

      public void setScrollIndicators(View var1, int var2) {
      }

      public void setScrollIndicators(View var1, int var2, int var3) {
      }

      public void setTooltipText(View var1, CharSequence var2) {
      }

      public void setTransitionName(View var1, String var2) {
         if (sTransitionNameMap == null) {
            sTransitionNameMap = new WeakHashMap();
         }

         sTransitionNameMap.put(var1, var2);
      }

      public void setTranslationZ(View var1, float var2) {
      }

      public void setZ(View var1, float var2) {
      }

      public boolean startDragAndDrop(View var1, ClipData var2, DragShadowBuilder var3, Object var4, int var5) {
         return var1.startDrag(var2, var3, var4, var5);
      }

      public boolean startNestedScroll(View var1, int var2) {
         return var1 instanceof NestedScrollingChild ? ((NestedScrollingChild)var1).startNestedScroll(var2) : false;
      }

      public void stopNestedScroll(View var1) {
         if (var1 instanceof NestedScrollingChild) {
            ((NestedScrollingChild)var1).stopNestedScroll();
         }

      }

      public void updateDragShadow(View var1, DragShadowBuilder var2) {
      }
   }
}
