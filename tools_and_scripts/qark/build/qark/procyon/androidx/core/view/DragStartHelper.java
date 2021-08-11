// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View$OnTouchListener;
import android.view.View$OnLongClickListener;

public class DragStartHelper
{
    private boolean mDragging;
    private int mLastTouchX;
    private int mLastTouchY;
    private final OnDragStartListener mListener;
    private final View$OnLongClickListener mLongClickListener;
    private final View$OnTouchListener mTouchListener;
    private final View mView;
    
    public DragStartHelper(final View mView, final OnDragStartListener mListener) {
        this.mLongClickListener = (View$OnLongClickListener)new View$OnLongClickListener() {
            public boolean onLongClick(final View view) {
                return DragStartHelper.this.onLongClick(view);
            }
        };
        this.mTouchListener = (View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return DragStartHelper.this.onTouch(view, motionEvent);
            }
        };
        this.mView = mView;
        this.mListener = mListener;
    }
    
    public void attach() {
        this.mView.setOnLongClickListener(this.mLongClickListener);
        this.mView.setOnTouchListener(this.mTouchListener);
    }
    
    public void detach() {
        this.mView.setOnLongClickListener((View$OnLongClickListener)null);
        this.mView.setOnTouchListener((View$OnTouchListener)null);
    }
    
    public void getTouchPosition(final Point point) {
        point.set(this.mLastTouchX, this.mLastTouchY);
    }
    
    public boolean onLongClick(final View view) {
        return this.mListener.onDragStart(view, this);
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int n = (int)motionEvent.getX();
        final int n2 = (int)motionEvent.getY();
        final int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                    if (action != 3) {
                        return false;
                    }
                }
                else {
                    if (!MotionEventCompat.isFromSource(motionEvent, 8194)) {
                        return false;
                    }
                    if ((motionEvent.getButtonState() & 0x1) == 0x0) {
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
                    return this.mDragging = this.mListener.onDragStart(view, this);
                }
            }
            return this.mDragging = false;
        }
        this.mLastTouchX = n;
        this.mLastTouchY = n2;
        return false;
    }
    
    public interface OnDragStartListener
    {
        boolean onDragStart(final View p0, final DragStartHelper p1);
    }
}
