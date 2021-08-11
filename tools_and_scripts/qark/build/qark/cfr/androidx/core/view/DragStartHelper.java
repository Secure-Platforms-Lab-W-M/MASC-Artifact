/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Point
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnLongClickListener
 *  android.view.View$OnTouchListener
 */
package androidx.core.view;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.MotionEventCompat;

public class DragStartHelper {
    private boolean mDragging;
    private int mLastTouchX;
    private int mLastTouchY;
    private final OnDragStartListener mListener;
    private final View.OnLongClickListener mLongClickListener;
    private final View.OnTouchListener mTouchListener;
    private final View mView;

    public DragStartHelper(View view, OnDragStartListener onDragStartListener) {
        this.mLongClickListener = new View.OnLongClickListener(){

            public boolean onLongClick(View view) {
                return DragStartHelper.this.onLongClick(view);
            }
        };
        this.mTouchListener = new View.OnTouchListener(){

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return DragStartHelper.this.onTouch(view, motionEvent);
            }
        };
        this.mView = view;
        this.mListener = onDragStartListener;
    }

    public void attach() {
        this.mView.setOnLongClickListener(this.mLongClickListener);
        this.mView.setOnTouchListener(this.mTouchListener);
    }

    public void detach() {
        this.mView.setOnLongClickListener(null);
        this.mView.setOnTouchListener(null);
    }

    public void getTouchPosition(Point point) {
        point.set(this.mLastTouchX, this.mLastTouchY);
    }

    public boolean onLongClick(View view) {
        return this.mListener.onDragStart(view, this);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int n = (int)motionEvent.getX();
        int n2 = (int)motionEvent.getY();
        int n3 = motionEvent.getAction();
        if (n3 != 0) {
            if (n3 != 1) {
                if (n3 != 2) {
                    if (n3 != 3) {
                        return false;
                    }
                } else {
                    boolean bl;
                    if (!MotionEventCompat.isFromSource(motionEvent, 8194)) return false;
                    if ((motionEvent.getButtonState() & 1) == 0) {
                        return false;
                    }
                    if (this.mDragging) {
                        return false;
                    }
                    if (this.mLastTouchX == n && this.mLastTouchY == n2) {
                        return false;
                    }
                    this.mLastTouchX = n;
                    this.mLastTouchY = n2;
                    this.mDragging = bl = this.mListener.onDragStart(view, this);
                    return bl;
                }
            }
            this.mDragging = false;
            return false;
        }
        this.mLastTouchX = n;
        this.mLastTouchY = n2;
        return false;
    }

    public static interface OnDragStartListener {
        public boolean onDragStart(View var1, DragStartHelper var2);
    }

}

