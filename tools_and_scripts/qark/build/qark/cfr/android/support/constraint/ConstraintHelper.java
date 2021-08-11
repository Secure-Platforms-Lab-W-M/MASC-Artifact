/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 */
package android.support.constraint;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.R;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Helper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class ConstraintHelper
extends View {
    protected int mCount;
    protected Helper mHelperWidget;
    protected int[] mIds = new int[32];
    private String mReferenceIds;
    protected boolean mUseViewMeasure = false;
    protected Context myContext;

    public ConstraintHelper(Context context) {
        super(context);
        this.myContext = context;
        this.init(null);
    }

    public ConstraintHelper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.myContext = context;
        this.init(attributeSet);
    }

    public ConstraintHelper(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.myContext = context;
        this.init(attributeSet);
    }

    private void addID(String string2) {
        Object object;
        if (string2 == null) {
            return;
        }
        if (this.myContext == null) {
            return;
        }
        string2 = string2.trim();
        int n = 0;
        try {
            int n2;
            n = n2 = R.id.class.getField(string2).getInt(null);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (n == 0) {
            n = this.myContext.getResources().getIdentifier(string2, "id", this.myContext.getPackageName());
        }
        if (n == 0 && this.isInEditMode() && this.getParent() instanceof ConstraintLayout && (object = ((ConstraintLayout)this.getParent()).getDesignInformation(0, string2)) != null && object instanceof Integer) {
            n = (Integer)object;
        }
        if (n != 0) {
            this.setTag(n, null);
            return;
        }
        object = new StringBuilder();
        object.append("Could not find id of \"");
        object.append(string2);
        object.append("\"");
        Log.w((String)"ConstraintHelper", (String)object.toString());
    }

    private void setIds(String string2) {
        if (string2 == null) {
            return;
        }
        int n = 0;
        do {
            int n2;
            if ((n2 = string2.indexOf(44, n)) == -1) {
                this.addID(string2.substring(n));
                return;
            }
            this.addID(string2.substring(n, n2));
            n = n2 + 1;
        } while (true);
    }

    public int[] getReferencedIds() {
        return Arrays.copyOf(this.mIds, this.mCount);
    }

    protected void init(AttributeSet attributeSet) {
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.ConstraintLayout_Layout);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 != R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) continue;
                this.mReferenceIds = attributeSet.getString(n2);
                this.setIds(this.mReferenceIds);
            }
            return;
        }
    }

    public void onDraw(Canvas canvas) {
    }

    protected void onMeasure(int n, int n2) {
        if (this.mUseViewMeasure) {
            super.onMeasure(n, n2);
            return;
        }
        this.setMeasuredDimension(0, 0);
    }

    public void setReferencedIds(int[] arrn) {
        this.mCount = 0;
        for (int i = 0; i < arrn.length; ++i) {
            this.setTag(arrn[i], null);
        }
    }

    public void setTag(int n, Object arrn) {
        int n2 = this.mCount;
        arrn = this.mIds;
        if (n2 + 1 > arrn.length) {
            this.mIds = Arrays.copyOf(arrn, arrn.length * 2);
        }
        arrn = this.mIds;
        n2 = this.mCount;
        arrn[n2] = n;
        this.mCount = n2 + 1;
    }

    public void updatePostLayout(ConstraintLayout constraintLayout) {
    }

    public void updatePostMeasure(ConstraintLayout constraintLayout) {
    }

    public void updatePreLayout(ConstraintLayout constraintLayout) {
        Helper helper;
        if (this.isInEditMode()) {
            this.setIds(this.mReferenceIds);
        }
        if ((helper = this.mHelperWidget) == null) {
            return;
        }
        helper.removeAllIds();
        for (int i = 0; i < this.mCount; ++i) {
            helper = constraintLayout.getViewById(this.mIds[i]);
            if (helper == null) continue;
            this.mHelperWidget.add(constraintLayout.getViewWidget((View)helper));
        }
    }

    public void validateParams() {
        if (this.mHelperWidget == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ((ConstraintLayout.LayoutParams)layoutParams).widget = this.mHelperWidget;
            return;
        }
    }
}

