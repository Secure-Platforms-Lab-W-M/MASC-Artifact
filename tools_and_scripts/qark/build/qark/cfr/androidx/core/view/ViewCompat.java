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
 *  android.text.TextUtils
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.Display
 *  android.view.KeyEvent
 *  android.view.PointerIcon
 *  android.view.View
 *  android.view.View$AccessibilityDelegate
 *  android.view.View$DragShadowBuilder
 *  android.view.View$OnApplyWindowInsetsListener
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.View$OnUnhandledKeyEventListener
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.view.WindowInsets
 *  android.view.WindowManager
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityManager
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeProvider
 *  androidx.core.R
 *  androidx.core.R$id
 */
package androidx.core.view;

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
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.collection.ArrayMap;
import androidx.core.R;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewCompat {
    private static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
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
    private static boolean sAccessibilityDelegateCheckFailed;
    private static Field sAccessibilityDelegateField;
    private static AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager;
    private static Method sChildrenDrawingOrderMethod;
    private static Method sDispatchFinishTemporaryDetach;
    private static Method sDispatchStartTemporaryDetach;
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    private static final AtomicInteger sNextGeneratedId;
    private static boolean sTempDetachBound;
    private static ThreadLocal<Rect> sThreadLocalRect;
    private static WeakHashMap<View, String> sTransitionNameMap;
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap;

    static {
        sNextGeneratedId = new AtomicInteger(1);
        sViewPropertyAnimatorMap = null;
        sAccessibilityDelegateCheckFailed = false;
        ACCESSIBILITY_ACTIONS_RESOURCE_IDS = new int[]{R.id.accessibility_custom_action_0, R.id.accessibility_custom_action_1, R.id.accessibility_custom_action_2, R.id.accessibility_custom_action_3, R.id.accessibility_custom_action_4, R.id.accessibility_custom_action_5, R.id.accessibility_custom_action_6, R.id.accessibility_custom_action_7, R.id.accessibility_custom_action_8, R.id.accessibility_custom_action_9, R.id.accessibility_custom_action_10, R.id.accessibility_custom_action_11, R.id.accessibility_custom_action_12, R.id.accessibility_custom_action_13, R.id.accessibility_custom_action_14, R.id.accessibility_custom_action_15, R.id.accessibility_custom_action_16, R.id.accessibility_custom_action_17, R.id.accessibility_custom_action_18, R.id.accessibility_custom_action_19, R.id.accessibility_custom_action_20, R.id.accessibility_custom_action_21, R.id.accessibility_custom_action_22, R.id.accessibility_custom_action_23, R.id.accessibility_custom_action_24, R.id.accessibility_custom_action_25, R.id.accessibility_custom_action_26, R.id.accessibility_custom_action_27, R.id.accessibility_custom_action_28, R.id.accessibility_custom_action_29, R.id.accessibility_custom_action_30, R.id.accessibility_custom_action_31};
        sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();
    }

    protected ViewCompat() {
    }

    private static AccessibilityViewProperty<Boolean> accessibilityHeadingProperty() {
        return new AccessibilityViewProperty<Boolean>(R.id.tag_accessibility_heading, Boolean.class, 28){

            @Override
            Boolean frameworkGet(View view) {
                return view.isAccessibilityHeading();
            }

            @Override
            void frameworkSet(View view, Boolean bl) {
                view.setAccessibilityHeading(bl.booleanValue());
            }

            @Override
            boolean shouldUpdate(Boolean bl, Boolean bl2) {
                return this.booleanNullToFalseEquals(bl, bl2) ^ true;
            }
        };
    }

    public static int addAccessibilityAction(View view, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
        int n = ViewCompat.getAvailableActionIdFromResources(view);
        if (n != -1) {
            ViewCompat.addAccessibilityAction(view, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(n, charSequence, accessibilityViewCommand));
        }
        return n;
    }

    private static void addAccessibilityAction(View view, AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat) {
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.getOrCreateAccessibilityDelegateCompat(view);
            ViewCompat.removeActionWithId(accessibilityActionCompat.getId(), view);
            ViewCompat.getActionList(view).add(accessibilityActionCompat);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }

    public static void addKeyboardNavigationClusters(View view, Collection<View> collection, int n) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.addKeyboardNavigationClusters(collection, n);
        }
    }

    public static void addOnUnhandledKeyEventListener(View view, final OnUnhandledKeyEventListenerCompat onUnhandledKeyEventListenerCompat) {
        ArrayList<OnUnhandledKeyEventListenerCompat> arrayList;
        if (Build.VERSION.SDK_INT >= 28) {
            Object object = (ArrayMap<OnUnhandledKeyEventListenerCompat, Object>)view.getTag(R.id.tag_unhandled_key_listeners);
            ArrayMap<OnUnhandledKeyEventListenerCompat, Object> arrayMap = object;
            if (object == null) {
                arrayMap = new ArrayMap<OnUnhandledKeyEventListenerCompat, Object>();
                view.setTag(R.id.tag_unhandled_key_listeners, arrayMap);
            }
            object = new View.OnUnhandledKeyEventListener(){

                public boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent) {
                    return onUnhandledKeyEventListenerCompat.onUnhandledKeyEvent(view, keyEvent);
                }
            };
            arrayMap.put(onUnhandledKeyEventListenerCompat, object);
            view.addOnUnhandledKeyEventListener((View.OnUnhandledKeyEventListener)object);
            return;
        }
        ArrayList<OnUnhandledKeyEventListenerCompat> arrayList2 = arrayList = (ArrayList<OnUnhandledKeyEventListenerCompat>)view.getTag(R.id.tag_unhandled_key_listeners);
        if (arrayList == null) {
            arrayList2 = new ArrayList<OnUnhandledKeyEventListenerCompat>();
            view.setTag(R.id.tag_unhandled_key_listeners, arrayList2);
        }
        arrayList2.add(onUnhandledKeyEventListenerCompat);
        if (arrayList2.size() == 1) {
            UnhandledKeyEventManager.registerListeningView(view);
        }
    }

    public static ViewPropertyAnimatorCompat animate(View view) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
        if (sViewPropertyAnimatorMap == null) {
            sViewPropertyAnimatorMap = new WeakHashMap();
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2 = viewPropertyAnimatorCompat = sViewPropertyAnimatorMap.get((Object)view);
        if (viewPropertyAnimatorCompat == null) {
            viewPropertyAnimatorCompat2 = new ViewPropertyAnimatorCompat(view);
            sViewPropertyAnimatorMap.put(view, viewPropertyAnimatorCompat2);
        }
        return viewPropertyAnimatorCompat2;
    }

    private static void bindTempDetach() {
        try {
            sDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);
            sDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.e((String)"ViewCompat", (String)"Couldn't find method", (Throwable)noSuchMethodException);
        }
        sTempDetachBound = true;
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
        if (Build.VERSION.SDK_INT >= 24) {
            view.cancelDragAndDrop();
        }
    }

    @Deprecated
    public static int combineMeasuredStates(int n, int n2) {
        return View.combineMeasuredStates((int)n, (int)n2);
    }

    private static void compatOffsetLeftAndRight(View view, int n) {
        view.offsetLeftAndRight(n);
        if (view.getVisibility() == 0) {
            ViewCompat.tickleInvalidationFlag(view);
            view = view.getParent();
            if (view instanceof View) {
                ViewCompat.tickleInvalidationFlag(view);
            }
        }
    }

    private static void compatOffsetTopAndBottom(View view, int n) {
        view.offsetTopAndBottom(n);
        if (view.getVisibility() == 0) {
            ViewCompat.tickleInvalidationFlag(view);
            view = view.getParent();
            if (view instanceof View) {
                ViewCompat.tickleInvalidationFlag(view);
            }
        }
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(View object, WindowInsetsCompat windowInsetsCompat) {
        if (Build.VERSION.SDK_INT >= 21) {
            windowInsetsCompat = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            WindowInsets windowInsets = object.dispatchApplyWindowInsets((WindowInsets)windowInsetsCompat);
            object = windowInsetsCompat;
            if (!windowInsets.equals((Object)windowInsetsCompat)) {
                object = new WindowInsets(windowInsets);
            }
            return WindowInsetsCompat.wrap(object);
        }
        return windowInsetsCompat;
    }

    public static void dispatchFinishTemporaryDetach(View view) {
        Method method;
        if (Build.VERSION.SDK_INT >= 24) {
            view.dispatchFinishTemporaryDetach();
            return;
        }
        if (!sTempDetachBound) {
            ViewCompat.bindTempDetach();
        }
        if ((method = sDispatchFinishTemporaryDetach) != null) {
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

    public static boolean dispatchNestedFling(View view, float f, float f2, boolean bl) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.dispatchNestedFling(f, f2, bl);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild)view).dispatchNestedFling(f, f2, bl);
        }
        return false;
    }

    public static boolean dispatchNestedPreFling(View view, float f, float f2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.dispatchNestedPreFling(f, f2);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild)view).dispatchNestedPreFling(f, f2);
        }
        return false;
    }

    public static boolean dispatchNestedPreScroll(View view, int n, int n2, int[] arrn, int[] arrn2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.dispatchNestedPreScroll(n, n2, arrn, arrn2);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild)view).dispatchNestedPreScroll(n, n2, arrn, arrn2);
        }
        return false;
    }

    public static boolean dispatchNestedPreScroll(View view, int n, int n2, int[] arrn, int[] arrn2, int n3) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).dispatchNestedPreScroll(n, n2, arrn, arrn2, n3);
        }
        if (n3 == 0) {
            return ViewCompat.dispatchNestedPreScroll(view, n, n2, arrn, arrn2);
        }
        return false;
    }

    public static void dispatchNestedScroll(View view, int n, int n2, int n3, int n4, int[] arrn, int n5, int[] arrn2) {
        if (view instanceof NestedScrollingChild3) {
            ((NestedScrollingChild3)view).dispatchNestedScroll(n, n2, n3, n4, arrn, n5, arrn2);
            return;
        }
        ViewCompat.dispatchNestedScroll(view, n, n2, n3, n4, arrn, n5);
    }

    public static boolean dispatchNestedScroll(View view, int n, int n2, int n3, int n4, int[] arrn) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.dispatchNestedScroll(n, n2, n3, n4, arrn);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild)view).dispatchNestedScroll(n, n2, n3, n4, arrn);
        }
        return false;
    }

    public static boolean dispatchNestedScroll(View view, int n, int n2, int n3, int n4, int[] arrn, int n5) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).dispatchNestedScroll(n, n2, n3, n4, arrn, n5);
        }
        if (n5 == 0) {
            return ViewCompat.dispatchNestedScroll(view, n, n2, n3, n4, arrn);
        }
        return false;
    }

    public static void dispatchStartTemporaryDetach(View view) {
        Method method;
        if (Build.VERSION.SDK_INT >= 24) {
            view.dispatchStartTemporaryDetach();
            return;
        }
        if (!sTempDetachBound) {
            ViewCompat.bindTempDetach();
        }
        if ((method = sDispatchStartTemporaryDetach) != null) {
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

    static boolean dispatchUnhandledKeyEventBeforeCallback(View view, KeyEvent keyEvent) {
        if (Build.VERSION.SDK_INT >= 28) {
            return false;
        }
        return UnhandledKeyEventManager.at(view).dispatch(view, keyEvent);
    }

    static boolean dispatchUnhandledKeyEventBeforeHierarchy(View view, KeyEvent keyEvent) {
        if (Build.VERSION.SDK_INT >= 28) {
            return false;
        }
        return UnhandledKeyEventManager.at(view).preDispatch(keyEvent);
    }

    public static void enableAccessibleClickableSpanSupport(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            ViewCompat.getOrCreateAccessibilityDelegateCompat(view);
        }
    }

    public static int generateViewId() {
        int n;
        int n2;
        if (Build.VERSION.SDK_INT >= 17) {
            return View.generateViewId();
        }
        do {
            int n3;
            n = sNextGeneratedId.get();
            n2 = n3 = n + 1;
            if (n3 <= 16777215) continue;
            n2 = 1;
        } while (!sNextGeneratedId.compareAndSet(n, n2));
        return n;
    }

    public static AccessibilityDelegateCompat getAccessibilityDelegate(View view) {
        if ((view = ViewCompat.getAccessibilityDelegateInternal(view)) == null) {
            return null;
        }
        if (view instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
            return ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter)view).mCompat;
        }
        return new AccessibilityDelegateCompat((View.AccessibilityDelegate)view);
    }

    private static View.AccessibilityDelegate getAccessibilityDelegateInternal(View object) {
        if (sAccessibilityDelegateCheckFailed) {
            return null;
        }
        if (sAccessibilityDelegateField == null) {
            try {
                Field field;
                sAccessibilityDelegateField = field = View.class.getDeclaredField("mAccessibilityDelegate");
                field.setAccessible(true);
            }
            catch (Throwable throwable) {
                sAccessibilityDelegateCheckFailed = true;
                return null;
            }
        }
        try {
            object = sAccessibilityDelegateField.get(object);
            if (object instanceof View.AccessibilityDelegate) {
                object = (View.AccessibilityDelegate)object;
                return object;
            }
            return null;
        }
        catch (Throwable throwable) {
            sAccessibilityDelegateCheckFailed = true;
            return null;
        }
    }

    public static int getAccessibilityLiveRegion(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return view.getAccessibilityLiveRegion();
        }
        return 0;
    }

    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        if (Build.VERSION.SDK_INT >= 16 && (view = view.getAccessibilityNodeProvider()) != null) {
            return new AccessibilityNodeProviderCompat((Object)view);
        }
        return null;
    }

    public static CharSequence getAccessibilityPaneTitle(View view) {
        return ViewCompat.paneTitleProperty().get(view);
    }

    private static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(View view) {
        ArrayList arrayList;
        ArrayList arrayList2 = arrayList = (ArrayList)view.getTag(R.id.tag_accessibility_actions);
        if (arrayList == null) {
            arrayList2 = new ArrayList();
            view.setTag(R.id.tag_accessibility_actions, arrayList2);
        }
        return arrayList2;
    }

    @Deprecated
    public static float getAlpha(View view) {
        return view.getAlpha();
    }

    private static int getAvailableActionIdFromResources(View object) {
        int[] arrn;
        int n = -1;
        object = ViewCompat.getActionList((View)object);
        for (int i = 0; i < (arrn = ACCESSIBILITY_ACTIONS_RESOURCE_IDS).length && n == -1; ++i) {
            int n2 = arrn[i];
            boolean bl = true;
            for (int j = 0; j < object.size(); ++j) {
                boolean bl2 = ((AccessibilityNodeInfoCompat.AccessibilityActionCompat)object.get(j)).getId() != n2;
                bl &= bl2;
            }
            if (!bl) continue;
            n = n2;
        }
        return n;
    }

    public static ColorStateList getBackgroundTintList(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintList();
        }
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView)view).getSupportBackgroundTintList();
        }
        return null;
    }

    public static PorterDuff.Mode getBackgroundTintMode(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.getBackgroundTintMode();
        }
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView)view).getSupportBackgroundTintMode();
        }
        return null;
    }

    public static Rect getClipBounds(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return view.getClipBounds();
        }
        return null;
    }

    public static Display getDisplay(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return view.getDisplay();
        }
        if (ViewCompat.isAttachedToWindow(view)) {
            return ((WindowManager)view.getContext().getSystemService("window")).getDefaultDisplay();
        }
        return null;
    }

    public static float getElevation(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.getElevation();
        }
        return 0.0f;
    }

    private static Rect getEmptyTempRect() {
        Rect rect;
        if (sThreadLocalRect == null) {
            sThreadLocalRect = new ThreadLocal();
        }
        Rect rect2 = rect = sThreadLocalRect.get();
        if (rect == null) {
            rect2 = new Rect();
            sThreadLocalRect.set(rect2);
        }
        rect2.setEmpty();
        return rect2;
    }

    public static boolean getFitsSystemWindows(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return view.getFitsSystemWindows();
        }
        return false;
    }

    public static int getImportantForAccessibility(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return view.getImportantForAccessibility();
        }
        return 0;
    }

    public static int getImportantForAutofill(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return view.getImportantForAutofill();
        }
        return 0;
    }

    public static int getLabelFor(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return view.getLabelFor();
        }
        return 0;
    }

    @Deprecated
    public static int getLayerType(View view) {
        return view.getLayerType();
    }

    public static int getLayoutDirection(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return view.getLayoutDirection();
        }
        return 0;
    }

    @Deprecated
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
        Field field;
        if (Build.VERSION.SDK_INT >= 16) {
            return view.getMinimumHeight();
        }
        if (!sMinHeightFieldFetched) {
            try {
                sMinHeightField = field = View.class.getDeclaredField("mMinHeight");
                field.setAccessible(true);
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

    public static int getMinimumWidth(View view) {
        Field field;
        if (Build.VERSION.SDK_INT >= 16) {
            return view.getMinimumWidth();
        }
        if (!sMinWidthFieldFetched) {
            try {
                sMinWidthField = field = View.class.getDeclaredField("mMinWidth");
                field.setAccessible(true);
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

    public static int getNextClusterForwardId(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return view.getNextClusterForwardId();
        }
        return -1;
    }

    static AccessibilityDelegateCompat getOrCreateAccessibilityDelegateCompat(View view) {
        AccessibilityDelegateCompat accessibilityDelegateCompat;
        AccessibilityDelegateCompat accessibilityDelegateCompat2 = accessibilityDelegateCompat = ViewCompat.getAccessibilityDelegate(view);
        if (accessibilityDelegateCompat == null) {
            accessibilityDelegateCompat2 = new AccessibilityDelegateCompat();
        }
        ViewCompat.setAccessibilityDelegate(view, accessibilityDelegateCompat2);
        return accessibilityDelegateCompat2;
    }

    @Deprecated
    public static int getOverScrollMode(View view) {
        return view.getOverScrollMode();
    }

    public static int getPaddingEnd(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return view.getPaddingEnd();
        }
        return view.getPaddingRight();
    }

    public static int getPaddingStart(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return view.getPaddingStart();
        }
        return view.getPaddingLeft();
    }

    public static ViewParent getParentForAccessibility(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return view.getParentForAccessibility();
        }
        return view.getParent();
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

    public static int getScrollIndicators(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            return view.getScrollIndicators();
        }
        return 0;
    }

    public static String getTransitionName(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.getTransitionName();
        }
        WeakHashMap<View, String> weakHashMap = sTransitionNameMap;
        if (weakHashMap == null) {
            return null;
        }
        return weakHashMap.get((Object)view);
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
        if (Build.VERSION.SDK_INT >= 21) {
            return view.getTranslationZ();
        }
        return 0.0f;
    }

    public static int getWindowSystemUiVisibility(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return view.getWindowSystemUiVisibility();
        }
        return 0;
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
        if (Build.VERSION.SDK_INT >= 21) {
            return view.getZ();
        }
        return 0.0f;
    }

    public static boolean hasAccessibilityDelegate(View view) {
        if (ViewCompat.getAccessibilityDelegateInternal(view) != null) {
            return true;
        }
        return false;
    }

    public static boolean hasExplicitFocusable(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return view.hasExplicitFocusable();
        }
        return view.hasFocusable();
    }

    public static boolean hasNestedScrollingParent(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.hasNestedScrollingParent();
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild)view).hasNestedScrollingParent();
        }
        return false;
    }

    public static boolean hasNestedScrollingParent(View view, int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).hasNestedScrollingParent(n);
        } else if (n == 0) {
            return ViewCompat.hasNestedScrollingParent(view);
        }
        return false;
    }

    public static boolean hasOnClickListeners(View view) {
        if (Build.VERSION.SDK_INT >= 15) {
            return view.hasOnClickListeners();
        }
        return false;
    }

    public static boolean hasOverlappingRendering(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return view.hasOverlappingRendering();
        }
        return true;
    }

    public static boolean hasTransientState(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return view.hasTransientState();
        }
        return false;
    }

    public static boolean isAccessibilityHeading(View object) {
        object = ViewCompat.accessibilityHeadingProperty().get((View)object);
        if (object == null) {
            return false;
        }
        return object.booleanValue();
    }

    public static boolean isAttachedToWindow(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return view.isAttachedToWindow();
        }
        if (view.getWindowToken() != null) {
            return true;
        }
        return false;
    }

    public static boolean isFocusedByDefault(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return view.isFocusedByDefault();
        }
        return false;
    }

    public static boolean isImportantForAccessibility(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.isImportantForAccessibility();
        }
        return true;
    }

    public static boolean isImportantForAutofill(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return view.isImportantForAutofill();
        }
        return true;
    }

    public static boolean isInLayout(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return view.isInLayout();
        }
        return false;
    }

    public static boolean isKeyboardNavigationCluster(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return view.isKeyboardNavigationCluster();
        }
        return false;
    }

    public static boolean isLaidOut(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return view.isLaidOut();
        }
        if (view.getWidth() > 0 && view.getHeight() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isLayoutDirectionResolved(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return view.isLayoutDirectionResolved();
        }
        return false;
    }

    public static boolean isNestedScrollingEnabled(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.isNestedScrollingEnabled();
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild)view).isNestedScrollingEnabled();
        }
        return false;
    }

    @Deprecated
    public static boolean isOpaque(View view) {
        return view.isOpaque();
    }

    public static boolean isPaddingRelative(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return view.isPaddingRelative();
        }
        return false;
    }

    public static boolean isScreenReaderFocusable(View object) {
        object = ViewCompat.screenReaderFocusableProperty().get((View)object);
        if (object == null) {
            return false;
        }
        return object.booleanValue();
    }

    @Deprecated
    public static void jumpDrawablesToCurrentState(View view) {
        view.jumpDrawablesToCurrentState();
    }

    public static View keyboardNavigationClusterSearch(View view, View view2, int n) {
        if (Build.VERSION.SDK_INT >= 26) {
            return view.keyboardNavigationClusterSearch(view2, n);
        }
        return null;
    }

    static void notifyViewAccessibilityStateChangedIfNeeded(View view, int n) {
        if (!((AccessibilityManager)view.getContext().getSystemService("accessibility")).isEnabled()) {
            return;
        }
        int n2 = ViewCompat.getAccessibilityPaneTitle(view) != null ? 1 : 0;
        if (ViewCompat.getAccessibilityLiveRegion(view) == 0 && (n2 == 0 || view.getVisibility() != 0)) {
            if (view.getParent() != null) {
                try {
                    view.getParent().notifySubtreeAccessibilityStateChanged(view, view, n);
                    return;
                }
                catch (AbstractMethodError abstractMethodError) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(view.getParent().getClass().getSimpleName());
                    stringBuilder.append(" does not fully implement ViewParent");
                    Log.e((String)"ViewCompat", (String)stringBuilder.toString(), (Throwable)abstractMethodError);
                    return;
                }
            }
            return;
        }
        AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain();
        n2 = n2 != 0 ? 32 : 2048;
        accessibilityEvent.setEventType(n2);
        accessibilityEvent.setContentChangeTypes(n);
        view.sendAccessibilityEventUnchecked(accessibilityEvent);
    }

    public static void offsetLeftAndRight(View view, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.offsetLeftAndRight(n);
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Rect rect = ViewCompat.getEmptyTempRect();
            boolean bl = false;
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof View) {
                View view2 = (View)viewParent;
                rect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                bl = rect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true;
            }
            ViewCompat.compatOffsetLeftAndRight(view, n);
            if (bl && rect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)viewParent).invalidate(rect);
            }
            return;
        }
        ViewCompat.compatOffsetLeftAndRight(view, n);
    }

    public static void offsetTopAndBottom(View view, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.offsetTopAndBottom(n);
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Rect rect = ViewCompat.getEmptyTempRect();
            boolean bl = false;
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof View) {
                View view2 = (View)viewParent;
                rect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                bl = rect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()) ^ true;
            }
            ViewCompat.compatOffsetTopAndBottom(view, n);
            if (bl && rect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)viewParent).invalidate(rect);
            }
            return;
        }
        ViewCompat.compatOffsetTopAndBottom(view, n);
    }

    public static WindowInsetsCompat onApplyWindowInsets(View object, WindowInsetsCompat windowInsetsCompat) {
        if (Build.VERSION.SDK_INT >= 21) {
            windowInsetsCompat = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            WindowInsets windowInsets = object.onApplyWindowInsets((WindowInsets)windowInsetsCompat);
            object = windowInsetsCompat;
            if (!windowInsets.equals((Object)windowInsetsCompat)) {
                object = new WindowInsets(windowInsets);
            }
            return WindowInsetsCompat.wrap(object);
        }
        return windowInsetsCompat;
    }

    @Deprecated
    public static void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        view.onInitializeAccessibilityEvent(accessibilityEvent);
    }

    public static void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        view.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat.unwrap());
    }

    @Deprecated
    public static void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        view.onPopulateAccessibilityEvent(accessibilityEvent);
    }

    private static AccessibilityViewProperty<CharSequence> paneTitleProperty() {
        return new AccessibilityViewProperty<CharSequence>(R.id.tag_accessibility_pane_title, CharSequence.class, 8, 28){

            @Override
            CharSequence frameworkGet(View view) {
                return view.getAccessibilityPaneTitle();
            }

            @Override
            void frameworkSet(View view, CharSequence charSequence) {
                view.setAccessibilityPaneTitle(charSequence);
            }

            @Override
            boolean shouldUpdate(CharSequence charSequence, CharSequence charSequence2) {
                return TextUtils.equals((CharSequence)charSequence, (CharSequence)charSequence2) ^ true;
            }
        };
    }

    public static boolean performAccessibilityAction(View view, int n, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            return view.performAccessibilityAction(n, bundle);
        }
        return false;
    }

    public static void postInvalidateOnAnimation(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation();
            return;
        }
        view.postInvalidate();
    }

    public static void postInvalidateOnAnimation(View view, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postInvalidateOnAnimation(n, n2, n3, n4);
            return;
        }
        view.postInvalidate(n, n2, n3, n4);
    }

    public static void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
            return;
        }
        view.postDelayed(runnable, ValueAnimator.getFrameDelay());
    }

    public static void postOnAnimationDelayed(View view, Runnable runnable, long l) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postOnAnimationDelayed(runnable, l);
            return;
        }
        view.postDelayed(runnable, ValueAnimator.getFrameDelay() + l);
    }

    public static void removeAccessibilityAction(View view, int n) {
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.removeActionWithId(n, view);
            ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }

    private static void removeActionWithId(int n, View object) {
        object = ViewCompat.getActionList((View)object);
        for (int i = 0; i < object.size(); ++i) {
            if (((AccessibilityNodeInfoCompat.AccessibilityActionCompat)object.get(i)).getId() != n) continue;
            object.remove(i);
            return;
        }
    }

    public static void removeOnUnhandledKeyEventListener(View view, OnUnhandledKeyEventListenerCompat onUnhandledKeyEventListenerCompat) {
        if (Build.VERSION.SDK_INT >= 28) {
            Map map = (Map)view.getTag(R.id.tag_unhandled_key_listeners);
            if (map == null) {
                return;
            }
            if ((onUnhandledKeyEventListenerCompat = (View.OnUnhandledKeyEventListener)map.get(onUnhandledKeyEventListenerCompat)) != null) {
                view.removeOnUnhandledKeyEventListener((View.OnUnhandledKeyEventListener)onUnhandledKeyEventListenerCompat);
            }
            return;
        }
        ArrayList arrayList = (ArrayList)view.getTag(R.id.tag_unhandled_key_listeners);
        if (arrayList != null) {
            arrayList.remove(onUnhandledKeyEventListenerCompat);
            if (arrayList.size() == 0) {
                UnhandledKeyEventManager.unregisterListeningView(view);
            }
        }
    }

    public static void replaceAccessibilityAction(View view, AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
        ViewCompat.addAccessibilityAction(view, accessibilityActionCompat.createReplacementAction(charSequence, accessibilityViewCommand));
    }

    public static void requestApplyInsets(View view) {
        if (Build.VERSION.SDK_INT >= 20) {
            view.requestApplyInsets();
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            view.requestFitSystemWindows();
        }
    }

    public static <T extends View> T requireViewById(View view, int n) {
        if (Build.VERSION.SDK_INT >= 28) {
            return (T)view.requireViewById(n);
        }
        if ((view = view.findViewById(n)) != null) {
            return (T)view;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this View");
    }

    @Deprecated
    public static int resolveSizeAndState(int n, int n2, int n3) {
        return View.resolveSizeAndState((int)n, (int)n2, (int)n3);
    }

    public static boolean restoreDefaultFocus(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return view.restoreDefaultFocus();
        }
        return view.requestFocus();
    }

    private static AccessibilityViewProperty<Boolean> screenReaderFocusableProperty() {
        return new AccessibilityViewProperty<Boolean>(R.id.tag_screen_reader_focusable, Boolean.class, 28){

            @Override
            Boolean frameworkGet(View view) {
                return view.isScreenReaderFocusable();
            }

            @Override
            void frameworkSet(View view, Boolean bl) {
                view.setScreenReaderFocusable(bl.booleanValue());
            }

            @Override
            boolean shouldUpdate(Boolean bl, Boolean bl2) {
                return this.booleanNullToFalseEquals(bl, bl2) ^ true;
            }
        };
    }

    public static void setAccessibilityDelegate(View view, AccessibilityDelegateCompat accessibilityDelegateCompat) {
        AccessibilityDelegateCompat accessibilityDelegateCompat2 = accessibilityDelegateCompat;
        if (accessibilityDelegateCompat == null) {
            accessibilityDelegateCompat2 = accessibilityDelegateCompat;
            if (ViewCompat.getAccessibilityDelegateInternal(view) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
                accessibilityDelegateCompat2 = new AccessibilityDelegateCompat();
            }
        }
        accessibilityDelegateCompat = accessibilityDelegateCompat2 == null ? null : accessibilityDelegateCompat2.getBridge();
        view.setAccessibilityDelegate((View.AccessibilityDelegate)accessibilityDelegateCompat);
    }

    public static void setAccessibilityHeading(View view, boolean bl) {
        ViewCompat.accessibilityHeadingProperty().set(view, bl);
    }

    public static void setAccessibilityLiveRegion(View view, int n) {
        if (Build.VERSION.SDK_INT >= 19) {
            view.setAccessibilityLiveRegion(n);
        }
    }

    public static void setAccessibilityPaneTitle(View view, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 19) {
            ViewCompat.paneTitleProperty().set(view, charSequence);
            if (charSequence != null) {
                sAccessibilityPaneVisibilityManager.addAccessibilityPane(view);
                return;
            }
            sAccessibilityPaneVisibilityManager.removeAccessibilityPane(view);
        }
    }

    @Deprecated
    public static void setActivated(View view, boolean bl) {
        view.setActivated(bl);
    }

    @Deprecated
    public static void setAlpha(View view, float f) {
        view.setAlpha(f);
    }

    public static /* varargs */ void setAutofillHints(View view, String ... arrstring) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setAutofillHints(arrstring);
        }
    }

    public static void setBackground(View view, Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable2);
            return;
        }
        view.setBackgroundDrawable(drawable2);
    }

    public static void setBackgroundTintList(View view, ColorStateList colorStateList) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setBackgroundTintList(colorStateList);
            if (Build.VERSION.SDK_INT == 21) {
                colorStateList = view.getBackground();
                boolean bl = view.getBackgroundTintList() != null || view.getBackgroundTintMode() != null;
                if (colorStateList != null && bl) {
                    if (colorStateList.isStateful()) {
                        colorStateList.setState(view.getDrawableState());
                    }
                    view.setBackground((Drawable)colorStateList);
                }
                return;
            }
        } else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintList(colorStateList);
        }
    }

    public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setBackgroundTintMode(mode);
            if (Build.VERSION.SDK_INT == 21) {
                mode = view.getBackground();
                boolean bl = view.getBackgroundTintList() != null || view.getBackgroundTintMode() != null;
                if (mode != null && bl) {
                    if (mode.isStateful()) {
                        mode.setState(view.getDrawableState());
                    }
                    view.setBackground((Drawable)mode);
                }
                return;
            }
        } else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintMode(mode);
        }
    }

    @Deprecated
    public static void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean bl) {
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

    public static void setClipBounds(View view, Rect rect) {
        if (Build.VERSION.SDK_INT >= 18) {
            view.setClipBounds(rect);
        }
    }

    public static void setElevation(View view, float f) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setElevation(f);
        }
    }

    @Deprecated
    public static void setFitsSystemWindows(View view, boolean bl) {
        view.setFitsSystemWindows(bl);
    }

    public static void setFocusedByDefault(View view, boolean bl) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setFocusedByDefault(bl);
        }
    }

    public static void setHasTransientState(View view, boolean bl) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setHasTransientState(bl);
        }
    }

    public static void setImportantForAccessibility(View view, int n) {
        if (Build.VERSION.SDK_INT >= 19) {
            view.setImportantForAccessibility(n);
            return;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            int n2 = n;
            if (n == 4) {
                n2 = 2;
            }
            view.setImportantForAccessibility(n2);
        }
    }

    public static void setImportantForAutofill(View view, int n) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setImportantForAutofill(n);
        }
    }

    public static void setKeyboardNavigationCluster(View view, boolean bl) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setKeyboardNavigationCluster(bl);
        }
    }

    public static void setLabelFor(View view, int n) {
        if (Build.VERSION.SDK_INT >= 17) {
            view.setLabelFor(n);
        }
    }

    public static void setLayerPaint(View view, Paint paint) {
        if (Build.VERSION.SDK_INT >= 17) {
            view.setLayerPaint(paint);
            return;
        }
        view.setLayerType(view.getLayerType(), paint);
        view.invalidate();
    }

    @Deprecated
    public static void setLayerType(View view, int n, Paint paint) {
        view.setLayerType(n, paint);
    }

    public static void setLayoutDirection(View view, int n) {
        if (Build.VERSION.SDK_INT >= 17) {
            view.setLayoutDirection(n);
        }
    }

    public static void setNestedScrollingEnabled(View view, boolean bl) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setNestedScrollingEnabled(bl);
            return;
        }
        if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild)view).setNestedScrollingEnabled(bl);
        }
    }

    public static void setNextClusterForwardId(View view, int n) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setNextClusterForwardId(n);
        }
    }

    public static void setOnApplyWindowInsetsListener(View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        if (Build.VERSION.SDK_INT >= 21) {
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
    }

    @Deprecated
    public static void setOverScrollMode(View view, int n) {
        view.setOverScrollMode(n);
    }

    public static void setPaddingRelative(View view, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 17) {
            view.setPaddingRelative(n, n2, n3, n4);
            return;
        }
        view.setPadding(n, n2, n3, n4);
    }

    @Deprecated
    public static void setPivotX(View view, float f) {
        view.setPivotX(f);
    }

    @Deprecated
    public static void setPivotY(View view, float f) {
        view.setPivotY(f);
    }

    public static void setPointerIcon(View view, PointerIconCompat object) {
        if (Build.VERSION.SDK_INT >= 24) {
            object = object != null ? object.getPointerIcon() : null;
            view.setPointerIcon((PointerIcon)object);
        }
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

    public static void setScreenReaderFocusable(View view, boolean bl) {
        ViewCompat.screenReaderFocusableProperty().set(view, bl);
    }

    public static void setScrollIndicators(View view, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(n);
        }
    }

    public static void setScrollIndicators(View view, int n, int n2) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.setScrollIndicators(n, n2);
        }
    }

    public static void setTooltipText(View view, CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 26) {
            view.setTooltipText(charSequence);
        }
    }

    public static void setTransitionName(View view, String string2) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setTransitionName(string2);
            return;
        }
        if (sTransitionNameMap == null) {
            sTransitionNameMap = new WeakHashMap();
        }
        sTransitionNameMap.put(view, string2);
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
        if (Build.VERSION.SDK_INT >= 21) {
            view.setTranslationZ(f);
        }
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
        if (Build.VERSION.SDK_INT >= 21) {
            view.setZ(f);
        }
    }

    public static boolean startDragAndDrop(View view, ClipData clipData, View.DragShadowBuilder dragShadowBuilder, Object object, int n) {
        if (Build.VERSION.SDK_INT >= 24) {
            return view.startDragAndDrop(clipData, dragShadowBuilder, object, n);
        }
        return view.startDrag(clipData, dragShadowBuilder, object, n);
    }

    public static boolean startNestedScroll(View view, int n) {
        if (Build.VERSION.SDK_INT >= 21) {
            return view.startNestedScroll(n);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild)view).startNestedScroll(n);
        }
        return false;
    }

    public static boolean startNestedScroll(View view, int n, int n2) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2)view).startNestedScroll(n, n2);
        }
        if (n2 == 0) {
            return ViewCompat.startNestedScroll(view, n);
        }
        return false;
    }

    public static void stopNestedScroll(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.stopNestedScroll();
            return;
        }
        if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild)view).stopNestedScroll();
        }
    }

    public static void stopNestedScroll(View view, int n) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2)view).stopNestedScroll(n);
            return;
        }
        if (n == 0) {
            ViewCompat.stopNestedScroll(view);
        }
    }

    private static void tickleInvalidationFlag(View view) {
        float f = view.getTranslationY();
        view.setTranslationY(1.0f + f);
        view.setTranslationY(f);
    }

    public static void updateDragShadow(View view, View.DragShadowBuilder dragShadowBuilder) {
        if (Build.VERSION.SDK_INT >= 24) {
            view.updateDragShadow(dragShadowBuilder);
        }
    }

    static class AccessibilityPaneVisibilityManager
    implements ViewTreeObserver.OnGlobalLayoutListener,
    View.OnAttachStateChangeListener {
        private WeakHashMap<View, Boolean> mPanesToVisible = new WeakHashMap();

        AccessibilityPaneVisibilityManager() {
        }

        private void checkPaneVisibility(View view, boolean bl) {
            boolean bl2 = view.getVisibility() == 0;
            if (bl != bl2) {
                if (bl2) {
                    ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, 16);
                }
                this.mPanesToVisible.put(view, bl2);
            }
        }

        private void registerForLayoutCallback(View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
        }

        private void unregisterForLayoutCallback(View view) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
        }

        void addAccessibilityPane(View view) {
            WeakHashMap<View, Boolean> weakHashMap = this.mPanesToVisible;
            boolean bl = view.getVisibility() == 0;
            weakHashMap.put(view, bl);
            view.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
            if (view.isAttachedToWindow()) {
                this.registerForLayoutCallback(view);
            }
        }

        public void onGlobalLayout() {
            for (Map.Entry<View, Boolean> entry : this.mPanesToVisible.entrySet()) {
                this.checkPaneVisibility(entry.getKey(), entry.getValue());
            }
        }

        public void onViewAttachedToWindow(View view) {
            this.registerForLayoutCallback(view);
        }

        public void onViewDetachedFromWindow(View view) {
        }

        void removeAccessibilityPane(View view) {
            this.mPanesToVisible.remove((Object)view);
            view.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
            this.unregisterForLayoutCallback(view);
        }
    }

    static abstract class AccessibilityViewProperty<T> {
        private final int mContentChangeType;
        private final int mFrameworkMinimumSdk;
        private final int mTagKey;
        private final Class<T> mType;

        AccessibilityViewProperty(int n, Class<T> class_, int n2) {
            this(n, class_, 0, n2);
        }

        AccessibilityViewProperty(int n, Class<T> class_, int n2, int n3) {
            this.mTagKey = n;
            this.mType = class_;
            this.mContentChangeType = n2;
            this.mFrameworkMinimumSdk = n3;
        }

        private boolean extrasAvailable() {
            if (Build.VERSION.SDK_INT >= 19) {
                return true;
            }
            return false;
        }

        private boolean frameworkAvailable() {
            if (Build.VERSION.SDK_INT >= this.mFrameworkMinimumSdk) {
                return true;
            }
            return false;
        }

        boolean booleanNullToFalseEquals(Boolean bl, Boolean bl2) {
            boolean bl3 = false;
            boolean bl4 = bl == null ? false : bl;
            boolean bl5 = bl2 == null ? false : bl2;
            if (bl4 == bl5) {
                bl3 = true;
            }
            return bl3;
        }

        abstract T frameworkGet(View var1);

        abstract void frameworkSet(View var1, T var2);

        T get(View object) {
            if (this.frameworkAvailable()) {
                return this.frameworkGet((View)object);
            }
            if (this.extrasAvailable() && this.mType.isInstance(object = object.getTag(this.mTagKey))) {
                return (T)object;
            }
            return null;
        }

        void set(View view, T t) {
            if (this.frameworkAvailable()) {
                this.frameworkSet(view, t);
                return;
            }
            if (this.extrasAvailable() && this.shouldUpdate(this.get(view), t)) {
                ViewCompat.getOrCreateAccessibilityDelegateCompat(view);
                view.setTag(this.mTagKey, t);
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, 0);
            }
        }

        boolean shouldUpdate(T t, T t2) {
            return t2.equals(t) ^ true;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface FocusDirection {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface FocusRealDirection {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface FocusRelativeDirection {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface NestedScrollType {
    }

    public static interface OnUnhandledKeyEventListenerCompat {
        public boolean onUnhandledKeyEvent(View var1, KeyEvent var2);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ScrollAxis {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ScrollIndicators {
    }

    static class UnhandledKeyEventManager {
        private static final ArrayList<WeakReference<View>> sViewsWithListeners = new ArrayList();
        private SparseArray<WeakReference<View>> mCapturedKeys = null;
        private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent = null;
        private WeakHashMap<View, Boolean> mViewsContainingListeners = null;

        UnhandledKeyEventManager() {
        }

        static UnhandledKeyEventManager at(View view) {
            UnhandledKeyEventManager unhandledKeyEventManager;
            UnhandledKeyEventManager unhandledKeyEventManager2 = unhandledKeyEventManager = (UnhandledKeyEventManager)view.getTag(R.id.tag_unhandled_key_event_manager);
            if (unhandledKeyEventManager == null) {
                unhandledKeyEventManager2 = new UnhandledKeyEventManager();
                view.setTag(R.id.tag_unhandled_key_event_manager, (Object)unhandledKeyEventManager2);
            }
            return unhandledKeyEventManager2;
        }

        private View dispatchInOrder(View view, KeyEvent keyEvent) {
            ViewGroup viewGroup = this.mViewsContainingListeners;
            if (viewGroup != null) {
                if (!viewGroup.containsKey((Object)view)) {
                    return null;
                }
                if (view instanceof ViewGroup) {
                    viewGroup = (ViewGroup)view;
                    for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                        View view2 = this.dispatchInOrder(viewGroup.getChildAt(i), keyEvent);
                        if (view2 == null) continue;
                        return view2;
                    }
                }
                if (this.onUnhandledKeyEvent(view, keyEvent)) {
                    return view;
                }
                return null;
            }
            return null;
        }

        private SparseArray<WeakReference<View>> getCapturedKeys() {
            if (this.mCapturedKeys == null) {
                this.mCapturedKeys = new SparseArray();
            }
            return this.mCapturedKeys;
        }

        private boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent) {
            ArrayList arrayList = (ArrayList)view.getTag(R.id.tag_unhandled_key_listeners);
            if (arrayList != null) {
                for (int i = arrayList.size() - 1; i >= 0; --i) {
                    if (!((OnUnhandledKeyEventListenerCompat)arrayList.get(i)).onUnhandledKeyEvent(view, keyEvent)) continue;
                    return true;
                }
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void recalcViewsWithUnhandled() {
            View view = this.mViewsContainingListeners;
            if (view != null) {
                view.clear();
            }
            if (sViewsWithListeners.isEmpty()) {
                return;
            }
            ArrayList<WeakReference<View>> arrayList = sViewsWithListeners;
            synchronized (arrayList) {
                if (this.mViewsContainingListeners == null) {
                    this.mViewsContainingListeners = new WeakHashMap();
                }
                int n = sViewsWithListeners.size() - 1;
                while (n >= 0) {
                    view = sViewsWithListeners.get(n).get();
                    if (view == null) {
                        sViewsWithListeners.remove(n);
                    } else {
                        this.mViewsContainingListeners.put(view, Boolean.TRUE);
                        view = view.getParent();
                        while (view instanceof View) {
                            this.mViewsContainingListeners.put(view, Boolean.TRUE);
                            view = view.getParent();
                        }
                    }
                    --n;
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        static void registerListeningView(View view) {
            ArrayList<WeakReference<View>> arrayList = sViewsWithListeners;
            synchronized (arrayList) {
                Iterator<WeakReference<View>> iterator = sViewsWithListeners.iterator();
                do {
                    if (iterator.hasNext()) continue;
                    sViewsWithListeners.add(new WeakReference<View>(view));
                    return;
                } while (iterator.next().get() != view);
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        static void unregisterListeningView(View view) {
            ArrayList<WeakReference<View>> arrayList = sViewsWithListeners;
            synchronized (arrayList) {
                int n = 0;
                while (n < sViewsWithListeners.size()) {
                    if (sViewsWithListeners.get(n).get() == view) {
                        sViewsWithListeners.remove(n);
                        return;
                    }
                    ++n;
                }
                return;
            }
        }

        boolean dispatch(View view, KeyEvent keyEvent) {
            if (keyEvent.getAction() == 0) {
                this.recalcViewsWithUnhandled();
            }
            view = this.dispatchInOrder(view, keyEvent);
            if (keyEvent.getAction() == 0) {
                int n = keyEvent.getKeyCode();
                if (view != null && !KeyEvent.isModifierKey((int)n)) {
                    this.getCapturedKeys().put(n, new WeakReference<View>(view));
                }
            }
            if (view != null) {
                return true;
            }
            return false;
        }

        boolean preDispatch(KeyEvent keyEvent) {
            WeakReference weakReference = this.mLastDispatchedPreViewKeyEvent;
            if (weakReference != null && weakReference.get() == keyEvent) {
                return false;
            }
            this.mLastDispatchedPreViewKeyEvent = new WeakReference<KeyEvent>(keyEvent);
            WeakReference weakReference2 = null;
            SparseArray<WeakReference<View>> sparseArray = this.getCapturedKeys();
            weakReference = weakReference2;
            if (keyEvent.getAction() == 1) {
                int n = sparseArray.indexOfKey(keyEvent.getKeyCode());
                weakReference = weakReference2;
                if (n >= 0) {
                    weakReference = (WeakReference)sparseArray.valueAt(n);
                    sparseArray.removeAt(n);
                }
            }
            weakReference2 = weakReference;
            if (weakReference == null) {
                weakReference2 = (WeakReference)sparseArray.get(keyEvent.getKeyCode());
            }
            if (weakReference2 != null) {
                weakReference = (View)weakReference2.get();
                if (weakReference != null && ViewCompat.isAttachedToWindow((View)weakReference)) {
                    this.onUnhandledKeyEvent((View)weakReference, keyEvent);
                }
                return true;
            }
            return false;
        }
    }

}

