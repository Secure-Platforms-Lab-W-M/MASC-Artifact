/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.View$OnClickListener
 *  android.view.View$OnLayoutChangeListener
 *  android.view.View$OnLongClickListener
 *  androidx.databinding.library.baseAdapters.R
 *  androidx.databinding.library.baseAdapters.R$id
 */
package androidx.databinding.adapters;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import androidx.databinding.adapters.ListenerUtil;
import androidx.databinding.library.baseAdapters.R;

public class ViewBindingAdapter {
    public static final int FADING_EDGE_HORIZONTAL = 1;
    public static final int FADING_EDGE_NONE = 0;
    public static final int FADING_EDGE_VERTICAL = 2;

    private static int pixelsToDimensionPixelSize(float f) {
        int n = (int)(0.5f + f);
        if (n != 0) {
            return n;
        }
        if (f == 0.0f) {
            return 0;
        }
        if (f > 0.0f) {
            return 1;
        }
        return -1;
    }

    public static void setBackground(View view, Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable2);
            return;
        }
        view.setBackgroundDrawable(drawable2);
    }

    public static void setClickListener(View view, View.OnClickListener onClickListener, boolean bl) {
        view.setOnClickListener(onClickListener);
        view.setClickable(bl);
    }

    public static void setOnAttachStateChangeListener(View view, OnViewDetachedFromWindow object, final OnViewAttachedToWindow onViewAttachedToWindow) {
        object = object == null && onViewAttachedToWindow == null ? null : new View.OnAttachStateChangeListener((OnViewDetachedFromWindow)object){
            final /* synthetic */ OnViewDetachedFromWindow val$detach;
            {
                this.val$detach = onViewDetachedFromWindow;
            }

            public void onViewAttachedToWindow(View view) {
                OnViewAttachedToWindow onViewAttachedToWindow2 = onViewAttachedToWindow;
                if (onViewAttachedToWindow2 != null) {
                    onViewAttachedToWindow2.onViewAttachedToWindow(view);
                }
            }

            public void onViewDetachedFromWindow(View view) {
                OnViewDetachedFromWindow onViewDetachedFromWindow = this.val$detach;
                if (onViewDetachedFromWindow != null) {
                    onViewDetachedFromWindow.onViewDetachedFromWindow(view);
                }
            }
        };
        onViewAttachedToWindow = (View.OnAttachStateChangeListener)ListenerUtil.trackListener(view, object, R.id.onAttachStateChangeListener);
        if (onViewAttachedToWindow != null) {
            view.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)onViewAttachedToWindow);
        }
        if (object != null) {
            view.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)object);
        }
    }

    public static void setOnClick(View view, View.OnClickListener onClickListener, boolean bl) {
        view.setOnClickListener(onClickListener);
        view.setClickable(bl);
    }

    public static void setOnLayoutChangeListener(View view, View.OnLayoutChangeListener onLayoutChangeListener, View.OnLayoutChangeListener onLayoutChangeListener2) {
        if (onLayoutChangeListener != null) {
            view.removeOnLayoutChangeListener(onLayoutChangeListener);
        }
        if (onLayoutChangeListener2 != null) {
            view.addOnLayoutChangeListener(onLayoutChangeListener2);
        }
    }

    public static void setOnLongClick(View view, View.OnLongClickListener onLongClickListener, boolean bl) {
        view.setOnLongClickListener(onLongClickListener);
        view.setLongClickable(bl);
    }

    public static void setOnLongClickListener(View view, View.OnLongClickListener onLongClickListener, boolean bl) {
        view.setOnLongClickListener(onLongClickListener);
        view.setLongClickable(bl);
    }

    public static void setPadding(View view, float f) {
        int n = ViewBindingAdapter.pixelsToDimensionPixelSize(f);
        view.setPadding(n, n, n, n);
    }

    public static void setPaddingBottom(View view, float f) {
        int n = ViewBindingAdapter.pixelsToDimensionPixelSize(f);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), n);
    }

    public static void setPaddingEnd(View view, float f) {
        int n = ViewBindingAdapter.pixelsToDimensionPixelSize(f);
        if (Build.VERSION.SDK_INT >= 17) {
            view.setPaddingRelative(view.getPaddingStart(), view.getPaddingTop(), n, view.getPaddingBottom());
            return;
        }
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), n, view.getPaddingBottom());
    }

    public static void setPaddingLeft(View view, float f) {
        view.setPadding(ViewBindingAdapter.pixelsToDimensionPixelSize(f), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingRight(View view, float f) {
        int n = ViewBindingAdapter.pixelsToDimensionPixelSize(f);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), n, view.getPaddingBottom());
    }

    public static void setPaddingStart(View view, float f) {
        int n = ViewBindingAdapter.pixelsToDimensionPixelSize(f);
        if (Build.VERSION.SDK_INT >= 17) {
            view.setPaddingRelative(n, view.getPaddingTop(), view.getPaddingEnd(), view.getPaddingBottom());
            return;
        }
        view.setPadding(n, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingTop(View view, float f) {
        int n = ViewBindingAdapter.pixelsToDimensionPixelSize(f);
        view.setPadding(view.getPaddingLeft(), n, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setRequiresFadingEdge(View view, int n) {
        boolean bl = false;
        boolean bl2 = (n & 2) != 0;
        if ((n & 1) != 0) {
            bl = true;
        }
        view.setVerticalFadingEdgeEnabled(bl2);
        view.setHorizontalFadingEdgeEnabled(bl);
    }

    public static interface OnViewAttachedToWindow {
        public void onViewAttachedToWindow(View var1);
    }

    public static interface OnViewDetachedFromWindow {
        public void onViewDetachedFromWindow(View var1);
    }

}

