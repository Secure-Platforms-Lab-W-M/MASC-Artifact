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
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;

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
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onDetachedFromWindow();
        }
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        TypedValue typedValue;
        int n4;
        DisplayMetrics displayMetrics = this.getContext().getResources().getDisplayMetrics();
        int n5 = displayMetrics.widthPixels < displayMetrics.heightPixels ? 1 : 0;
        int n6 = View.MeasureSpec.getMode((int)n);
        int n7 = View.MeasureSpec.getMode((int)n2);
        int n8 = n3 = 0;
        int n9 = n;
        if (n6 == Integer.MIN_VALUE) {
            typedValue = n5 != 0 ? this.mFixedWidthMinor : this.mFixedWidthMajor;
            n8 = n3;
            n9 = n;
            if (typedValue != null) {
                n8 = n3;
                n9 = n;
                if (typedValue.type != 0) {
                    n4 = 0;
                    if (typedValue.type == 5) {
                        n4 = (int)typedValue.getDimension(displayMetrics);
                    } else if (typedValue.type == 6) {
                        n4 = (int)typedValue.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                    }
                    n8 = n3;
                    n9 = n;
                    if (n4 > 0) {
                        n9 = View.MeasureSpec.makeMeasureSpec((int)Math.min(n4 - (this.mDecorPadding.left + this.mDecorPadding.right), View.MeasureSpec.getSize((int)n)), (int)1073741824);
                        n8 = 1;
                    }
                }
            }
        }
        n4 = n2;
        if (n7 == Integer.MIN_VALUE) {
            typedValue = n5 != 0 ? this.mFixedHeightMajor : this.mFixedHeightMinor;
            n4 = n2;
            if (typedValue != null) {
                n4 = n2;
                if (typedValue.type != 0) {
                    n = 0;
                    if (typedValue.type == 5) {
                        n = (int)typedValue.getDimension(displayMetrics);
                    } else if (typedValue.type == 6) {
                        n = (int)typedValue.getFraction((float)displayMetrics.heightPixels, (float)displayMetrics.heightPixels);
                    }
                    n4 = n2;
                    if (n > 0) {
                        n4 = View.MeasureSpec.makeMeasureSpec((int)Math.min(n - (this.mDecorPadding.top + this.mDecorPadding.bottom), View.MeasureSpec.getSize((int)n2)), (int)1073741824);
                    }
                }
            }
        }
        super.onMeasure(n9, n4);
        n7 = this.getMeasuredWidth();
        n9 = 0;
        n3 = View.MeasureSpec.makeMeasureSpec((int)n7, (int)1073741824);
        n2 = n9;
        n = n3;
        if (n8 == 0) {
            n2 = n9;
            n = n3;
            if (n6 == Integer.MIN_VALUE) {
                typedValue = n5 != 0 ? this.mMinWidthMinor : this.mMinWidthMajor;
                n2 = n9;
                n = n3;
                if (typedValue != null) {
                    n2 = n9;
                    n = n3;
                    if (typedValue.type != 0) {
                        n = 0;
                        if (typedValue.type == 5) {
                            n = (int)typedValue.getDimension(displayMetrics);
                        } else if (typedValue.type == 6) {
                            n = (int)typedValue.getFraction((float)displayMetrics.widthPixels, (float)displayMetrics.widthPixels);
                        }
                        n5 = n;
                        if (n > 0) {
                            n5 = n - (this.mDecorPadding.left + this.mDecorPadding.right);
                        }
                        n2 = n9;
                        n = n3;
                        if (n7 < n5) {
                            n = View.MeasureSpec.makeMeasureSpec((int)n5, (int)1073741824);
                            n2 = 1;
                        }
                    }
                }
            }
        }
        if (n2 != 0) {
            super.onMeasure(n, n4);
        }
    }

    public void setAttachListener(OnAttachListener onAttachListener) {
        this.mAttachListener = onAttachListener;
    }

    public void setDecorPadding(int n, int n2, int n3, int n4) {
        this.mDecorPadding.set(n, n2, n3, n4);
        if (ViewCompat.isLaidOut((View)this)) {
            this.requestLayout();
        }
    }

    public static interface OnAttachListener {
        public void onAttachedFromWindow();

        public void onDetachedFromWindow();
    }

}

