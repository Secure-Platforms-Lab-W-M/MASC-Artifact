/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnScrollChangedListener
 *  android.widget.ScrollView
 */
package com.google.android.material.shape;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import com.google.android.material.shape.MaterialShapeDrawable;

public class InterpolateOnScrollPositionChangeHelper {
    private final int[] containerLocation = new int[2];
    private ScrollView containingScrollView;
    private MaterialShapeDrawable materialShapeDrawable;
    private final ViewTreeObserver.OnScrollChangedListener scrollChangedListener;
    private final int[] scrollLocation = new int[2];
    private View shapedView;

    public InterpolateOnScrollPositionChangeHelper(View view, MaterialShapeDrawable materialShapeDrawable, ScrollView scrollView) {
        this.scrollChangedListener = new ViewTreeObserver.OnScrollChangedListener(){

            public void onScrollChanged() {
                InterpolateOnScrollPositionChangeHelper.this.updateInterpolationForScreenPosition();
            }
        };
        this.shapedView = view;
        this.materialShapeDrawable = materialShapeDrawable;
        this.containingScrollView = scrollView;
    }

    public void setContainingScrollView(ScrollView scrollView) {
        this.containingScrollView = scrollView;
    }

    public void setMaterialShapeDrawable(MaterialShapeDrawable materialShapeDrawable) {
        this.materialShapeDrawable = materialShapeDrawable;
    }

    public void startListeningForScrollChanges(ViewTreeObserver viewTreeObserver) {
        viewTreeObserver.addOnScrollChangedListener(this.scrollChangedListener);
    }

    public void stopListeningForScrollChanges(ViewTreeObserver viewTreeObserver) {
        viewTreeObserver.removeOnScrollChangedListener(this.scrollChangedListener);
    }

    public void updateInterpolationForScreenPosition() {
        ScrollView scrollView = this.containingScrollView;
        if (scrollView == null) {
            return;
        }
        if (scrollView.getChildCount() != 0) {
            this.containingScrollView.getLocationInWindow(this.scrollLocation);
            this.containingScrollView.getChildAt(0).getLocationInWindow(this.containerLocation);
            int n = this.shapedView.getTop() - this.scrollLocation[1] + this.containerLocation[1];
            int n2 = this.shapedView.getHeight();
            int n3 = this.containingScrollView.getHeight();
            if (n < 0) {
                this.materialShapeDrawable.setInterpolation(Math.max(0.0f, Math.min(1.0f, (float)n / (float)n2 + 1.0f)));
                this.shapedView.invalidate();
                return;
            }
            if (n + n2 > n3) {
                this.materialShapeDrawable.setInterpolation(Math.max(0.0f, Math.min(1.0f, 1.0f - (float)(n + n2 - n3) / (float)n2)));
                this.shapedView.invalidate();
            } else if (this.materialShapeDrawable.getInterpolation() != 1.0f) {
                this.materialShapeDrawable.setInterpolation(1.0f);
                this.shapedView.invalidate();
                return;
            }
            return;
        }
        throw new IllegalStateException("Scroll bar must contain a child to calculate interpolation.");
    }

}

