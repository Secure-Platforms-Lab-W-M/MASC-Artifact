/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.constraint;

import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintHelper;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class Group
extends ConstraintHelper {
    public Group(Context context) {
        super(context);
    }

    public Group(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Group(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        this.mUseViewMeasure = false;
    }

    @Override
    public void updatePostLayout(ConstraintLayout object) {
        object = (ConstraintLayout.LayoutParams)this.getLayoutParams();
        object.widget.setWidth(0);
        object.widget.setHeight(0);
    }

    @Override
    public void updatePreLayout(ConstraintLayout constraintLayout) {
        int n = this.getVisibility();
        float f = 0.0f;
        if (Build.VERSION.SDK_INT >= 21) {
            f = this.getElevation();
        }
        for (int i = 0; i < this.mCount; ++i) {
            View view = constraintLayout.getViewById(this.mIds[i]);
            if (view == null) continue;
            view.setVisibility(n);
            if (f <= 0.0f || Build.VERSION.SDK_INT < 21) continue;
            view.setElevation(f);
        }
    }
}

