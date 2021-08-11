// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.view.View;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.util.Log;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;

public class DividerItemDecoration extends ItemDecoration
{
    private static final int[] ATTRS;
    public static final int HORIZONTAL = 0;
    private static final String TAG = "DividerItem";
    public static final int VERTICAL = 1;
    private final Rect mBounds;
    private Drawable mDivider;
    private int mOrientation;
    
    static {
        ATTRS = new int[] { 16843284 };
    }
    
    public DividerItemDecoration(final Context context, final int orientation) {
        this.mBounds = new Rect();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(DividerItemDecoration.ATTRS);
        this.mDivider = obtainStyledAttributes.getDrawable(0);
        if (this.mDivider == null) {
            Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        obtainStyledAttributes.recycle();
        this.setOrientation(orientation);
    }
    
    private void drawHorizontal(final Canvas canvas, final RecyclerView recyclerView) {
        canvas.save();
        int paddingTop;
        int height;
        if (recyclerView.getClipToPadding()) {
            paddingTop = recyclerView.getPaddingTop();
            height = recyclerView.getHeight() - recyclerView.getPaddingBottom();
            canvas.clipRect(recyclerView.getPaddingLeft(), paddingTop, recyclerView.getWidth() - recyclerView.getPaddingRight(), height);
        }
        else {
            paddingTop = 0;
            height = recyclerView.getHeight();
        }
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = recyclerView.getChildAt(i);
            recyclerView.getLayoutManager().getDecoratedBoundsWithMargins(child, this.mBounds);
            final int n = this.mBounds.right + Math.round(child.getTranslationX());
            this.mDivider.setBounds(n - this.mDivider.getIntrinsicWidth(), paddingTop, n, height);
            this.mDivider.draw(canvas);
        }
        canvas.restore();
    }
    
    private void drawVertical(final Canvas canvas, final RecyclerView recyclerView) {
        canvas.save();
        int paddingLeft;
        int width;
        if (recyclerView.getClipToPadding()) {
            paddingLeft = recyclerView.getPaddingLeft();
            width = recyclerView.getWidth() - recyclerView.getPaddingRight();
            canvas.clipRect(paddingLeft, recyclerView.getPaddingTop(), width, recyclerView.getHeight() - recyclerView.getPaddingBottom());
        }
        else {
            paddingLeft = 0;
            width = recyclerView.getWidth();
        }
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = recyclerView.getChildAt(i);
            recyclerView.getDecoratedBoundsWithMargins(child, this.mBounds);
            final int n = this.mBounds.bottom + Math.round(child.getTranslationY());
            this.mDivider.setBounds(paddingLeft, n - this.mDivider.getIntrinsicHeight(), width, n);
            this.mDivider.draw(canvas);
        }
        canvas.restore();
    }
    
    @Override
    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
        final Drawable mDivider = this.mDivider;
        if (mDivider == null) {
            rect.set(0, 0, 0, 0);
            return;
        }
        if (this.mOrientation == 1) {
            rect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            return;
        }
        rect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    }
    
    @Override
    public void onDraw(final Canvas canvas, final RecyclerView recyclerView, final State state) {
        if (recyclerView.getLayoutManager() == null) {
            return;
        }
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            this.drawVertical(canvas, recyclerView);
            return;
        }
        this.drawHorizontal(canvas, recyclerView);
    }
    
    public void setDrawable(@NonNull final Drawable mDivider) {
        if (mDivider != null) {
            this.mDivider = mDivider;
            return;
        }
        throw new IllegalArgumentException("Drawable cannot be null.");
    }
    
    public void setOrientation(final int mOrientation) {
        if (mOrientation != 0 && mOrientation != 1) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        this.mOrientation = mOrientation;
    }
}
