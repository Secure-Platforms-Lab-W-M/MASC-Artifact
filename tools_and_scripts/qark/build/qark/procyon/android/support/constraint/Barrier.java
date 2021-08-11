// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint;

import android.os.Build$VERSION;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;

public class Barrier extends ConstraintHelper
{
    public static final int BOTTOM = 3;
    public static final int END = 6;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int START = 5;
    public static final int TOP = 2;
    private android.support.constraint.solver.widgets.Barrier mBarrier;
    private int mIndicatedType;
    private int mResolvedType;
    
    public Barrier(final Context context) {
        super(context);
        super.setVisibility(8);
    }
    
    public Barrier(final Context context, final AttributeSet set) {
        super(context, set);
        super.setVisibility(8);
    }
    
    public Barrier(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        super.setVisibility(8);
    }
    
    public boolean allowsGoneWidget() {
        return this.mBarrier.allowsGoneWidget();
    }
    
    public int getType() {
        return this.mIndicatedType;
    }
    
    @Override
    protected void init(final AttributeSet set) {
        super.init(set);
        this.mBarrier = new android.support.constraint.solver.widgets.Barrier();
        if (set != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = obtainStyledAttributes.getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_barrierDirection) {
                    this.setType(obtainStyledAttributes.getInt(index, 0));
                }
                else if (index == R.styleable.ConstraintLayout_Layout_barrierAllowsGoneWidgets) {
                    this.mBarrier.setAllowsGoneWidget(obtainStyledAttributes.getBoolean(index, true));
                }
            }
        }
        this.mHelperWidget = this.mBarrier;
        this.validateParams();
    }
    
    public void setAllowsGoneWidget(final boolean allowsGoneWidget) {
        this.mBarrier.setAllowsGoneWidget(allowsGoneWidget);
    }
    
    public void setType(int n) {
        this.mIndicatedType = n;
        this.mResolvedType = n;
        if (Build$VERSION.SDK_INT < 17) {
            n = this.mIndicatedType;
            if (n == 5) {
                this.mResolvedType = 0;
            }
            else if (n == 6) {
                this.mResolvedType = 1;
            }
        }
        else {
            if (1 == this.getResources().getConfiguration().getLayoutDirection()) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (n != 0) {
                n = this.mIndicatedType;
                if (n == 5) {
                    this.mResolvedType = 1;
                }
                else if (n == 6) {
                    this.mResolvedType = 0;
                }
            }
            else {
                n = this.mIndicatedType;
                if (n == 5) {
                    this.mResolvedType = 0;
                }
                else if (n == 6) {
                    this.mResolvedType = 1;
                }
            }
        }
        this.mBarrier.setBarrierType(this.mResolvedType);
    }
}
