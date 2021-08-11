// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.constraint;

import android.view.ViewGroup$LayoutParams;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import java.util.Arrays;
import android.util.Log;
import android.util.AttributeSet;
import android.content.Context;
import android.support.constraint.solver.widgets.Helper;
import android.view.View;

public abstract class ConstraintHelper extends View
{
    protected int mCount;
    protected Helper mHelperWidget;
    protected int[] mIds;
    private String mReferenceIds;
    protected boolean mUseViewMeasure;
    protected Context myContext;
    
    public ConstraintHelper(final Context myContext) {
        super(myContext);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.myContext = myContext;
        this.init(null);
    }
    
    public ConstraintHelper(final Context myContext, final AttributeSet set) {
        super(myContext, set);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.myContext = myContext;
        this.init(set);
    }
    
    public ConstraintHelper(final Context myContext, final AttributeSet set, final int n) {
        super(myContext, set, n);
        this.mIds = new int[32];
        this.mUseViewMeasure = false;
        this.myContext = myContext;
        this.init(set);
    }
    
    private void addID(String trim) {
        if (trim == null) {
            return;
        }
        if (this.myContext == null) {
            return;
        }
        trim = trim.trim();
        int n = 0;
        try {
            n = R.id.class.getField(trim).getInt(null);
        }
        catch (Exception ex) {}
        if (n == 0) {
            n = this.myContext.getResources().getIdentifier(trim, "id", this.myContext.getPackageName());
        }
        if (n == 0 && this.isInEditMode() && this.getParent() instanceof ConstraintLayout) {
            final Object designInformation = ((ConstraintLayout)this.getParent()).getDesignInformation(0, trim);
            if (designInformation != null && designInformation instanceof Integer) {
                n = (int)designInformation;
            }
        }
        if (n != 0) {
            this.setTag(n, null);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not find id of \"");
        sb.append(trim);
        sb.append("\"");
        Log.w("ConstraintHelper", sb.toString());
    }
    
    private void setIds(final String s) {
        if (s == null) {
            return;
        }
        int n = 0;
        while (true) {
            final int index = s.indexOf(44, n);
            if (index == -1) {
                break;
            }
            this.addID(s.substring(n, index));
            n = index + 1;
        }
        this.addID(s.substring(n));
    }
    
    public int[] getReferencedIds() {
        return Arrays.copyOf(this.mIds, this.mCount);
    }
    
    protected void init(final AttributeSet set) {
        if (set != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.ConstraintLayout_Layout);
            for (int indexCount = obtainStyledAttributes.getIndexCount(), i = 0; i < indexCount; ++i) {
                final int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
                    this.setIds(this.mReferenceIds = obtainStyledAttributes.getString(index));
                }
            }
        }
    }
    
    public void onDraw(final Canvas canvas) {
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mUseViewMeasure) {
            super.onMeasure(n, n2);
            return;
        }
        this.setMeasuredDimension(0, 0);
    }
    
    public void setReferencedIds(final int[] array) {
        this.mCount = 0;
        for (int i = 0; i < array.length; ++i) {
            this.setTag(array[i], null);
        }
    }
    
    public void setTag(final int n, final Object o) {
        final int mCount = this.mCount;
        final int[] mIds = this.mIds;
        if (mCount + 1 > mIds.length) {
            this.mIds = Arrays.copyOf(mIds, mIds.length * 2);
        }
        final int[] mIds2 = this.mIds;
        final int mCount2 = this.mCount;
        mIds2[mCount2] = n;
        this.mCount = mCount2 + 1;
    }
    
    public void updatePostLayout(final ConstraintLayout constraintLayout) {
    }
    
    public void updatePostMeasure(final ConstraintLayout constraintLayout) {
    }
    
    public void updatePreLayout(final ConstraintLayout constraintLayout) {
        if (this.isInEditMode()) {
            this.setIds(this.mReferenceIds);
        }
        final Helper mHelperWidget = this.mHelperWidget;
        if (mHelperWidget == null) {
            return;
        }
        mHelperWidget.removeAllIds();
        for (int i = 0; i < this.mCount; ++i) {
            final View viewById = constraintLayout.getViewById(this.mIds[i]);
            if (viewById != null) {
                this.mHelperWidget.add(constraintLayout.getViewWidget(viewById));
            }
        }
    }
    
    public void validateParams() {
        if (this.mHelperWidget == null) {
            return;
        }
        final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ((ConstraintLayout.LayoutParams)layoutParams).widget = this.mHelperWidget;
        }
    }
}
