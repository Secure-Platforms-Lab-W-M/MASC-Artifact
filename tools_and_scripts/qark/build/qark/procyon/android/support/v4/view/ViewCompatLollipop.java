// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.view;

import android.view.View$OnApplyWindowInsetsListener;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.view.ViewParent;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.view.WindowInsets;
import android.view.View;
import android.graphics.Rect;

class ViewCompatLollipop
{
    private static ThreadLocal<Rect> sThreadLocalRect;
    
    public static WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        WindowInsetsCompat windowInsetsCompat2 = windowInsetsCompat;
        if (windowInsetsCompat instanceof WindowInsetsCompatApi21) {
            final WindowInsets unwrap = ((WindowInsetsCompatApi21)windowInsetsCompat).unwrap();
            final WindowInsets dispatchApplyWindowInsets = view.dispatchApplyWindowInsets(unwrap);
            windowInsetsCompat2 = windowInsetsCompat;
            if (dispatchApplyWindowInsets != unwrap) {
                windowInsetsCompat2 = new WindowInsetsCompatApi21(dispatchApplyWindowInsets);
            }
        }
        return windowInsetsCompat2;
    }
    
    public static boolean dispatchNestedFling(final View view, final float n, final float n2, final boolean b) {
        return view.dispatchNestedFling(n, n2, b);
    }
    
    public static boolean dispatchNestedPreFling(final View view, final float n, final float n2) {
        return view.dispatchNestedPreFling(n, n2);
    }
    
    public static boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2) {
        return view.dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public static boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
        return view.dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    static ColorStateList getBackgroundTintList(final View view) {
        return view.getBackgroundTintList();
    }
    
    static PorterDuff$Mode getBackgroundTintMode(final View view) {
        return view.getBackgroundTintMode();
    }
    
    public static float getElevation(final View view) {
        return view.getElevation();
    }
    
    private static Rect getEmptyTempRect() {
        if (ViewCompatLollipop.sThreadLocalRect == null) {
            ViewCompatLollipop.sThreadLocalRect = new ThreadLocal<Rect>();
        }
        Rect rect;
        if ((rect = ViewCompatLollipop.sThreadLocalRect.get()) == null) {
            rect = new Rect();
            ViewCompatLollipop.sThreadLocalRect.set(rect);
        }
        rect.setEmpty();
        return rect;
    }
    
    public static String getTransitionName(final View view) {
        return view.getTransitionName();
    }
    
    public static float getTranslationZ(final View view) {
        return view.getTranslationZ();
    }
    
    public static float getZ(final View view) {
        return view.getZ();
    }
    
    public static boolean hasNestedScrollingParent(final View view) {
        return view.hasNestedScrollingParent();
    }
    
    public static boolean isImportantForAccessibility(final View view) {
        return view.isImportantForAccessibility();
    }
    
    public static boolean isNestedScrollingEnabled(final View view) {
        return view.isNestedScrollingEnabled();
    }
    
    static void offsetLeftAndRight(final View view, final int n) {
        final Rect emptyTempRect = getEmptyTempRect();
        boolean b = false;
        final ViewParent parent = view.getParent();
        if (parent instanceof View) {
            final View view2 = (View)parent;
            emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
            if (!emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                b = true;
            }
            else {
                b = false;
            }
        }
        ViewCompatHC.offsetLeftAndRight(view, n);
        if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
            ((View)parent).invalidate(emptyTempRect);
        }
    }
    
    static void offsetTopAndBottom(final View view, final int n) {
        final Rect emptyTempRect = getEmptyTempRect();
        boolean b = false;
        final ViewParent parent = view.getParent();
        if (parent instanceof View) {
            final View view2 = (View)parent;
            emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
            if (!emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                b = true;
            }
            else {
                b = false;
            }
        }
        ViewCompatHC.offsetTopAndBottom(view, n);
        if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
            ((View)parent).invalidate(emptyTempRect);
        }
    }
    
    public static WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        WindowInsetsCompat windowInsetsCompat2 = windowInsetsCompat;
        if (windowInsetsCompat instanceof WindowInsetsCompatApi21) {
            final WindowInsets unwrap = ((WindowInsetsCompatApi21)windowInsetsCompat).unwrap();
            final WindowInsets onApplyWindowInsets = view.onApplyWindowInsets(unwrap);
            windowInsetsCompat2 = windowInsetsCompat;
            if (onApplyWindowInsets != unwrap) {
                windowInsetsCompat2 = new WindowInsetsCompatApi21(onApplyWindowInsets);
            }
        }
        return windowInsetsCompat2;
    }
    
    public static void requestApplyInsets(final View view) {
        view.requestApplyInsets();
    }
    
    static void setBackgroundTintList(final View view, final ColorStateList backgroundTintList) {
        view.setBackgroundTintList(backgroundTintList);
        if (Build$VERSION.SDK_INT == 21) {
            final Drawable background = view.getBackground();
            boolean b;
            if (view.getBackgroundTintList() != null && view.getBackgroundTintMode() != null) {
                b = true;
            }
            else {
                b = false;
            }
            if (background != null && b) {
                if (background.isStateful()) {
                    background.setState(view.getDrawableState());
                }
                view.setBackground(background);
            }
        }
    }
    
    static void setBackgroundTintMode(final View view, final PorterDuff$Mode backgroundTintMode) {
        view.setBackgroundTintMode(backgroundTintMode);
        if (Build$VERSION.SDK_INT == 21) {
            final Drawable background = view.getBackground();
            boolean b;
            if (view.getBackgroundTintList() != null && view.getBackgroundTintMode() != null) {
                b = true;
            }
            else {
                b = false;
            }
            if (background != null && b) {
                if (background.isStateful()) {
                    background.setState(view.getDrawableState());
                }
                view.setBackground(background);
            }
        }
    }
    
    public static void setElevation(final View view, final float elevation) {
        view.setElevation(elevation);
    }
    
    public static void setNestedScrollingEnabled(final View view, final boolean nestedScrollingEnabled) {
        view.setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    public static void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        if (onApplyWindowInsetsListener == null) {
            view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)null);
            return;
        }
        view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new View$OnApplyWindowInsetsListener() {
            public WindowInsets onApplyWindowInsets(final View view, final WindowInsets windowInsets) {
                return ((WindowInsetsCompatApi21)onApplyWindowInsetsListener.onApplyWindowInsets(view, new WindowInsetsCompatApi21(windowInsets))).unwrap();
            }
        });
    }
    
    public static void setTransitionName(final View view, final String transitionName) {
        view.setTransitionName(transitionName);
    }
    
    public static void setTranslationZ(final View view, final float translationZ) {
        view.setTranslationZ(translationZ);
    }
    
    public static boolean startNestedScroll(final View view, final int n) {
        return view.startNestedScroll(n);
    }
    
    public static void stopNestedScroll(final View view) {
        view.stopNestedScroll();
    }
}
