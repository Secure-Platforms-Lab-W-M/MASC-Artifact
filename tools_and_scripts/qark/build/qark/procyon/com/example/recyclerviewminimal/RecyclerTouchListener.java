// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.example.recyclerviewminimal;

import android.support.annotation.NonNull;
import android.view.GestureDetector$OnGestureListener;
import android.view.View;
import android.view.MotionEvent;
import android.view.GestureDetector$SimpleOnGestureListener;
import android.content.Context;
import android.view.GestureDetector;
import android.support.v7.widget.RecyclerView;

class RecyclerTouchListener implements OnItemTouchListener
{
    private ClickListener clickListener;
    private GestureDetector gestureDetector;
    
    public RecyclerTouchListener(final Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        this.gestureDetector = new GestureDetector(context, (GestureDetector$OnGestureListener)new GestureDetector$SimpleOnGestureListener() {
            public void onLongPress(final MotionEvent motionEvent) {
                final View childViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (childViewUnder != null) {
                    final ClickListener val$clickListener = clickListener;
                    if (val$clickListener != null) {
                        val$clickListener.onLongClick(childViewUnder, recyclerView.getChildPosition(childViewUnder));
                    }
                }
            }
            
            public boolean onSingleTapUp(final MotionEvent motionEvent) {
                return true;
            }
        });
    }
    
    @Override
    public boolean onInterceptTouchEvent(@NonNull final RecyclerView recyclerView, @NonNull final MotionEvent motionEvent) {
        final View childViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (childViewUnder != null && this.clickListener != null && this.gestureDetector.onTouchEvent(motionEvent)) {
            this.clickListener.onClick(childViewUnder, recyclerView.getChildPosition(childViewUnder));
        }
        return false;
    }
    
    @Override
    public void onRequestDisallowInterceptTouchEvent(final boolean b) {
    }
    
    @Override
    public void onTouchEvent(@NonNull final RecyclerView recyclerView, @NonNull final MotionEvent motionEvent) {
    }
    
    public interface ClickListener
    {
        void onClick(final View p0, final int p1);
        
        void onLongClick(final View p0, final int p1);
    }
}
