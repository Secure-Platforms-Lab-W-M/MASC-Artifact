/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 */
package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

public class Constraints
extends ViewGroup {
    public static final String TAG = "Constraints";
    ConstraintSet myConstraintSet;

    public Constraints(Context context) {
        super(context);
        super.setVisibility(8);
    }

    public Constraints(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(attributeSet);
        super.setVisibility(8);
    }

    public Constraints(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init(attributeSet);
        super.setVisibility(8);
    }

    private void init(AttributeSet attributeSet) {
        Log.v((String)"Constraints", (String)" ################# init");
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new ConstraintLayout.LayoutParams(layoutParams);
    }

    public ConstraintSet getConstraintSet() {
        if (this.myConstraintSet == null) {
            this.myConstraintSet = new ConstraintSet();
        }
        this.myConstraintSet.clone(this);
        return this.myConstraintSet;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
    }

    public static class LayoutParams
    extends ConstraintLayout.LayoutParams {
        public float alpha = 1.0f;
        public boolean applyElevation = false;
        public float elevation = 0.0f;
        public float rotation = 0.0f;
        public float rotationX = 0.0f;
        public float rotationY = 0.0f;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public float transformPivotX = 0.0f;
        public float transformPivotY = 0.0f;
        public float translationX = 0.0f;
        public float translationY = 0.0f;
        public float translationZ = 0.0f;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, R.styleable.ConstraintSet);
            int n = context.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = context.getIndex(i);
                if (n2 == R.styleable.ConstraintSet_android_alpha) {
                    this.alpha = context.getFloat(n2, this.alpha);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_elevation) {
                    this.elevation = context.getFloat(n2, this.elevation);
                    this.applyElevation = true;
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_rotationX) {
                    this.rotationX = context.getFloat(n2, this.rotationX);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_rotationY) {
                    this.rotationY = context.getFloat(n2, this.rotationY);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_rotation) {
                    this.rotation = context.getFloat(n2, this.rotation);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_scaleX) {
                    this.scaleX = context.getFloat(n2, this.scaleX);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_scaleY) {
                    this.scaleY = context.getFloat(n2, this.scaleY);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_transformPivotX) {
                    this.transformPivotX = context.getFloat(n2, this.transformPivotX);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_transformPivotY) {
                    this.transformPivotY = context.getFloat(n2, this.transformPivotY);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_translationX) {
                    this.translationX = context.getFloat(n2, this.translationX);
                    continue;
                }
                if (n2 == R.styleable.ConstraintSet_android_translationY) {
                    this.translationY = context.getFloat(n2, this.translationY);
                    continue;
                }
                if (n2 != R.styleable.ConstraintSet_android_translationZ) continue;
                this.translationX = context.getFloat(n2, this.translationZ);
            }
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

}

