/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.GestureDetector$SimpleOnGestureListener
 *  android.view.MotionEvent
 *  android.view.View
 */
package com.example.recyclerviewminimal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

class RecyclerTouchListener
implements RecyclerView.OnItemTouchListener {
    private ClickListener clickListener;
    private GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        this.gestureDetector = new GestureDetector(context, (GestureDetector.OnGestureListener)new GestureDetector.SimpleOnGestureListener(){

            public void onLongPress(MotionEvent motionEvent) {
                ClickListener clickListener2;
                if ((motionEvent = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY())) != null && (clickListener2 = clickListener) != null) {
                    clickListener2.onLongClick((View)motionEvent, recyclerView.getChildPosition((View)motionEvent));
                    return;
                }
            }

            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (view != null && this.clickListener != null && this.gestureDetector.onTouchEvent(motionEvent)) {
            this.clickListener.onClick(view, recyclerView.getChildPosition(view));
        }
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean bl) {
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
    }

    public static interface ClickListener {
        public void onClick(View var1, int var2);

        public void onLongClick(View var1, int var2);
    }

}

