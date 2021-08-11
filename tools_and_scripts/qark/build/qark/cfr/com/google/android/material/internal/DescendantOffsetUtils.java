/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 */
package com.google.android.material.internal;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class DescendantOffsetUtils {
    private static final ThreadLocal<Matrix> matrix = new ThreadLocal();
    private static final ThreadLocal<RectF> rectF = new ThreadLocal();

    public static void getDescendantRect(ViewGroup viewGroup, View view, Rect rect) {
        rect.set(0, 0, view.getWidth(), view.getHeight());
        DescendantOffsetUtils.offsetDescendantRect(viewGroup, view, rect);
    }

    private static void offsetDescendantMatrix(ViewParent viewParent, View view, Matrix matrix) {
        ViewParent viewParent2 = view.getParent();
        if (viewParent2 instanceof View && viewParent2 != viewParent) {
            viewParent2 = (View)viewParent2;
            DescendantOffsetUtils.offsetDescendantMatrix(viewParent, (View)viewParent2, matrix);
            matrix.preTranslate((float)(- viewParent2.getScrollX()), (float)(- viewParent2.getScrollY()));
        }
        matrix.preTranslate((float)view.getLeft(), (float)view.getTop());
        if (!view.getMatrix().isIdentity()) {
            matrix.preConcat(view.getMatrix());
        }
    }

    public static void offsetDescendantRect(ViewGroup viewGroup, View view, Rect rect) {
        Matrix matrix = DescendantOffsetUtils.matrix.get();
        if (matrix == null) {
            matrix = new Matrix();
            DescendantOffsetUtils.matrix.set(matrix);
        } else {
            matrix.reset();
        }
        DescendantOffsetUtils.offsetDescendantMatrix((ViewParent)viewGroup, view, matrix);
        view = rectF.get();
        viewGroup = view;
        if (view == null) {
            viewGroup = new RectF();
            rectF.set((RectF)viewGroup);
        }
        viewGroup.set(rect);
        matrix.mapRect((RectF)viewGroup);
        rect.set((int)(viewGroup.left + 0.5f), (int)(viewGroup.top + 0.5f), (int)(viewGroup.right + 0.5f), (int)(viewGroup.bottom + 0.5f));
    }
}

