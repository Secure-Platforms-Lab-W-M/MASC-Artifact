/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.ViewParent
 *  android.view.inputmethod.InputMethodManager
 */
package com.google.android.material.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewUtils {
    private ViewUtils() {
    }

    public static void doOnApplyWindowInsets(View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        ViewCompat.setOnApplyWindowInsetsListener(view, new androidx.core.view.OnApplyWindowInsetsListener(new RelativePadding(ViewCompat.getPaddingStart(view), view.getPaddingTop(), ViewCompat.getPaddingEnd(view), view.getPaddingBottom())){
            final /* synthetic */ RelativePadding val$initialPadding;
            {
                this.val$initialPadding = relativePadding;
            }

            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return onApplyWindowInsetsListener.onApplyWindowInsets(view, windowInsetsCompat, new RelativePadding(this.val$initialPadding));
            }
        });
        ViewUtils.requestApplyInsetsWhenAttached(view);
    }

    public static float dpToPx(Context context, int n) {
        context = context.getResources();
        return TypedValue.applyDimension((int)1, (float)n, (DisplayMetrics)context.getDisplayMetrics());
    }

    public static float getParentAbsoluteElevation(View view) {
        float f = 0.0f;
        view = view.getParent();
        while (view instanceof View) {
            f += ViewCompat.getElevation(view);
            view = view.getParent();
        }
        return f;
    }

    public static boolean isLayoutRtl(View view) {
        if (ViewCompat.getLayoutDirection(view) == 1) {
            return true;
        }
        return false;
    }

    public static PorterDuff.Mode parseTintMode(int n, PorterDuff.Mode mode) {
        if (n != 3) {
            if (n != 5) {
                if (n != 9) {
                    switch (n) {
                        default: {
                            return mode;
                        }
                        case 16: {
                            return PorterDuff.Mode.ADD;
                        }
                        case 15: {
                            return PorterDuff.Mode.SCREEN;
                        }
                        case 14: 
                    }
                    return PorterDuff.Mode.MULTIPLY;
                }
                return PorterDuff.Mode.SRC_ATOP;
            }
            return PorterDuff.Mode.SRC_IN;
        }
        return PorterDuff.Mode.SRC_OVER;
    }

    public static void requestApplyInsetsWhenAttached(View view) {
        if (ViewCompat.isAttachedToWindow(view)) {
            ViewCompat.requestApplyInsets(view);
            return;
        }
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(){

            public void onViewAttachedToWindow(View view) {
                view.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
                ViewCompat.requestApplyInsets(view);
            }

            public void onViewDetachedFromWindow(View view) {
            }
        });
    }

    public static void requestFocusAndShowKeyboard(final View view) {
        view.requestFocus();
        view.post(new Runnable(){

            @Override
            public void run() {
                ((InputMethodManager)view.getContext().getSystemService("input_method")).showSoftInput(view, 1);
            }
        });
    }

    public static interface OnApplyWindowInsetsListener {
        public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2, RelativePadding var3);
    }

    public static class RelativePadding {
        public int bottom;
        public int end;
        public int start;
        public int top;

        public RelativePadding(int n, int n2, int n3, int n4) {
            this.start = n;
            this.top = n2;
            this.end = n3;
            this.bottom = n4;
        }

        public RelativePadding(RelativePadding relativePadding) {
            this.start = relativePadding.start;
            this.top = relativePadding.top;
            this.end = relativePadding.end;
            this.bottom = relativePadding.bottom;
        }

        public void applyToView(View view) {
            ViewCompat.setPaddingRelative(view, this.start, this.top, this.end, this.bottom);
        }
    }

}

