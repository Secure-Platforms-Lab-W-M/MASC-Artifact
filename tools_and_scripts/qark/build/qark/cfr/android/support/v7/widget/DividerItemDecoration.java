/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.view.View
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class DividerItemDecoration
extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{16843284};
    public static final int HORIZONTAL = 0;
    private static final String TAG = "DividerItem";
    public static final int VERTICAL = 1;
    private final Rect mBounds = new Rect();
    private Drawable mDivider;
    private int mOrientation;

    public DividerItemDecoration(Context context, int n) {
        context = context.obtainStyledAttributes(ATTRS);
        this.mDivider = context.getDrawable(0);
        if (this.mDivider == null) {
            Log.w((String)"DividerItem", (String)"@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        context.recycle();
        this.setOrientation(n);
    }

    private void drawHorizontal(Canvas canvas, RecyclerView recyclerView) {
        int n;
        int n2;
        canvas.save();
        if (recyclerView.getClipToPadding()) {
            n = recyclerView.getPaddingTop();
            n2 = recyclerView.getHeight() - recyclerView.getPaddingBottom();
            canvas.clipRect(recyclerView.getPaddingLeft(), n, recyclerView.getWidth() - recyclerView.getPaddingRight(), n2);
        } else {
            n = 0;
            n2 = recyclerView.getHeight();
        }
        int n3 = recyclerView.getChildCount();
        for (int i = 0; i < n3; ++i) {
            View view = recyclerView.getChildAt(i);
            recyclerView.getLayoutManager().getDecoratedBoundsWithMargins(view, this.mBounds);
            int n4 = this.mBounds.right + Math.round(view.getTranslationX());
            int n5 = this.mDivider.getIntrinsicWidth();
            this.mDivider.setBounds(n4 - n5, n, n4, n2);
            this.mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawVertical(Canvas canvas, RecyclerView recyclerView) {
        int n;
        int n2;
        canvas.save();
        if (recyclerView.getClipToPadding()) {
            n = recyclerView.getPaddingLeft();
            n2 = recyclerView.getWidth() - recyclerView.getPaddingRight();
            canvas.clipRect(n, recyclerView.getPaddingTop(), n2, recyclerView.getHeight() - recyclerView.getPaddingBottom());
        } else {
            n = 0;
            n2 = recyclerView.getWidth();
        }
        int n3 = recyclerView.getChildCount();
        for (int i = 0; i < n3; ++i) {
            View view = recyclerView.getChildAt(i);
            recyclerView.getDecoratedBoundsWithMargins(view, this.mBounds);
            int n4 = this.mBounds.bottom + Math.round(view.getTranslationY());
            int n5 = this.mDivider.getIntrinsicHeight();
            this.mDivider.setBounds(n, n4 - n5, n2, n4);
            this.mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        view = this.mDivider;
        if (view == null) {
            rect.set(0, 0, 0, 0);
            return;
        }
        if (this.mOrientation == 1) {
            rect.set(0, 0, 0, view.getIntrinsicHeight());
            return;
        }
        rect.set(0, 0, view.getIntrinsicWidth(), 0);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        if (recyclerView.getLayoutManager() != null) {
            if (this.mDivider == null) {
                return;
            }
            if (this.mOrientation == 1) {
                this.drawVertical(canvas, recyclerView);
                return;
            }
            this.drawHorizontal(canvas, recyclerView);
            return;
        }
    }

    public void setDrawable(@NonNull Drawable drawable2) {
        if (drawable2 != null) {
            this.mDivider = drawable2;
            return;
        }
        throw new IllegalArgumentException("Drawable cannot be null.");
    }

    public void setOrientation(int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        this.mOrientation = n;
    }
}

