/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 */
package android.support.constraint;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.constraint.ConstraintHelper;
import android.support.constraint.R;
import android.support.constraint.solver.widgets.Helper;
import android.util.AttributeSet;

public class Barrier
extends ConstraintHelper {
    public static final int BOTTOM = 3;
    public static final int END = 6;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int START = 5;
    public static final int TOP = 2;
    private android.support.constraint.solver.widgets.Barrier mBarrier;
    private int mIndicatedType;
    private int mResolvedType;

    public Barrier(Context context) {
        super(context);
        super.setVisibility(8);
    }

    public Barrier(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setVisibility(8);
    }

    public Barrier(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        super.setVisibility(8);
    }

    public boolean allowsGoneWidget() {
        return this.mBarrier.allowsGoneWidget();
    }

    public int getType() {
        return this.mIndicatedType;
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        this.mBarrier = new android.support.constraint.solver.widgets.Barrier();
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_Layout);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 == R.styleable.ConstraintLayout_Layout_barrierDirection) {
                    this.setType(attributeSet.getInt(n2, 0));
                    continue;
                }
                if (n2 != R.styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) continue;
                this.mBarrier.setAllowsGoneWidget(attributeSet.getBoolean(n2, true));
            }
        }
        this.mHelperWidget = this.mBarrier;
        this.validateParams();
    }

    public void setAllowsGoneWidget(boolean bl) {
        this.mBarrier.setAllowsGoneWidget(bl);
    }

    public void setType(int n) {
        this.mIndicatedType = n;
        this.mResolvedType = n;
        if (Build.VERSION.SDK_INT < 17) {
            n = this.mIndicatedType;
            if (n == 5) {
                this.mResolvedType = 0;
            } else if (n == 6) {
                this.mResolvedType = 1;
            }
        } else {
            n = 1 == this.getResources().getConfiguration().getLayoutDirection() ? 1 : 0;
            if (n != 0) {
                n = this.mIndicatedType;
                if (n == 5) {
                    this.mResolvedType = 1;
                } else if (n == 6) {
                    this.mResolvedType = 0;
                }
            } else {
                n = this.mIndicatedType;
                if (n == 5) {
                    this.mResolvedType = 0;
                } else if (n == 6) {
                    this.mResolvedType = 1;
                }
            }
        }
        this.mBarrier.setBarrierType(this.mResolvedType);
    }
}

