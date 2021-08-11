/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.widget.FrameLayout
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

public class ContentFrameLayout
extends FrameLayout {
    private OnAttachListener mAttachListener;
    private final Rect mDecorPadding = new Rect();
    private TypedValue mFixedHeightMajor;
    private TypedValue mFixedHeightMinor;
    private TypedValue mFixedWidthMajor;
    private TypedValue mFixedWidthMinor;
    private TypedValue mMinWidthMajor;
    private TypedValue mMinWidthMinor;

    public ContentFrameLayout(Context context) {
        this(context, null);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void dispatchFitSystemWindows(Rect rect) {
        this.fitSystemWindows(rect);
    }

    public TypedValue getFixedHeightMajor() {
        if (this.mFixedHeightMajor == null) {
            this.mFixedHeightMajor = new TypedValue();
        }
        return this.mFixedHeightMajor;
    }

    public TypedValue getFixedHeightMinor() {
        if (this.mFixedHeightMinor == null) {
            this.mFixedHeightMinor = new TypedValue();
        }
        return this.mFixedHeightMinor;
    }

    public TypedValue getFixedWidthMajor() {
        if (this.mFixedWidthMajor == null) {
            this.mFixedWidthMajor = new TypedValue();
        }
        return this.mFixedWidthMajor;
    }

    public TypedValue getFixedWidthMinor() {
        if (this.mFixedWidthMinor == null) {
            this.mFixedWidthMinor = new TypedValue();
        }
        return this.mFixedWidthMinor;
    }

    public TypedValue getMinWidthMajor() {
        if (this.mMinWidthMajor == null) {
            this.mMinWidthMajor = new TypedValue();
        }
        return this.mMinWidthMajor;
    }

    public TypedValue getMinWidthMinor() {
        if (this.mMinWidthMinor == null) {
            this.mMinWidthMinor = new TypedValue();
        }
        return this.mMinWidthMinor;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onAttachedFromWindow();
            return;
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onDetachedFromWindow();
            return;
        }
    }

    protected void onMeasure(int n, int n2) {
        TypedValue typedValue;
        int n3;
        int n4;
        DisplayMetrics displayMetrics = this.getContext().getResources().getDisplayMetrics();
        boolean bl = displayMetrics.widthPixels < displayMetrics.heightPixels;
        int n5 = View.MeasureSpec.getMode((int)n);
        int n6 = View.MeasureSpec.getMode((int)n2);
        int n7 = 0;
        if (n5 == Integer.MIN_VALUE) {
            typedValue = bl ? this.mFixedWidthMinor : this.mFixedWidthMajor;
            if (typedValue != null && typedValue.type != 0) {
                n3 = 0;
                if (typedValue.type == 5) {
                    n3 = (int)typedValue.getDimension(displayMetrics);
                } else if (typedValue.type == 6) {
                    n3 = (int)typedValue.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                }
                if (n3 > 0) {
                    n4 = View.MeasureSpec.makeMeasureSpec((int)Math.min(n3 - (this.mDecorPadding.left + this.mDecorPadding.right), View.MeasureSpec.getSize((int)n)), (int)1073741824);
                    n3 = 1;
                } else {
                    n3 = n7;
                    n4 = n;
                }
            } else {
                n3 = n7;
                n4 = n;
            }
        } else {
            n4 = n;
            n3 = n7;
        }
        if (n6 == Integer.MIN_VALUE) {
            typedValue = bl ? this.mFixedHeightMajor : this.mFixedHeightMinor;
            if (typedValue != null && typedValue.type != 0) {
                n = 0;
                if (typedValue.type == 5) {
                    n = (int)typedValue.getDimension(displayMetrics);
                } else if (typedValue.type == 6) {
                    n = (int)typedValue.getFraction((float)displayMetrics.heightPixels, (float)displayMetrics.heightPixels);
                }
                if (n > 0) {
                    n2 = View.MeasureSpec.makeMeasureSpec((int)Math.min(n - (this.mDecorPadding.top + this.mDecorPadding.bottom), View.MeasureSpec.getSize((int)n2)), (int)1073741824);
                }
            }
        }
        super.onMeasure(n4, n2);
        n6 = this.getMeasuredWidth();
        n4 = 0;
        n7 = View.MeasureSpec.makeMeasureSpec((int)n6, (int)1073741824);
        if (n3 == 0 && n5 == Integer.MIN_VALUE) {
            typedValue = bl ? this.mMinWidthMinor : this.mMinWidthMajor;
            if (typedValue != null && typedValue.type != 0) {
                n = 0;
                if (typedValue.type == 5) {
                    n = (int)typedValue.getDimension(displayMetrics);
                } else if (typedValue.type == 6) {
                    n = (int)typedValue.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                }
                if (n > 0) {
                    n -= this.mDecorPadding.left + this.mDecorPadding.right;
                }
                if (n6 < n) {
                    n3 = View.MeasureSpec.makeMeasureSpec((int)n, (int)1073741824);
                    n = 1;
                } else {
                    n = n4;
                    n3 = n7;
                }
            } else {
                n = n4;
                n3 = n7;
            }
        } else {
            n3 = n7;
            n = n4;
        }
        if (n != 0) {
            super.onMeasure(n3, n2);
            return;
        }
    }

    public void setAttachListener(OnAttachListener onAttachListener) {
        this.mAttachListener = onAttachListener;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setDecorPadding(int n, int n2, int n3, int n4) {
        this.mDecorPadding.set(n, n2, n3, n4);
        if (ViewCompat.isLaidOut((View)this)) {
            this.requestLayout();
            return;
        }
    }

    public static interface OnAttachListener {
        public void onAttachedFromWindow();

        public void onDetachedFromWindow();
    }

}

