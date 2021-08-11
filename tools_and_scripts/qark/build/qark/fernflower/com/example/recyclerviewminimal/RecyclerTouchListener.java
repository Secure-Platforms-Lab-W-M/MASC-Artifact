package com.example.recyclerviewminimal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;

class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
   private RecyclerTouchListener.ClickListener clickListener;
   private GestureDetector gestureDetector;

   public RecyclerTouchListener(Context var1, final RecyclerView var2, final RecyclerTouchListener.ClickListener var3) {
      this.clickListener = var3;
      this.gestureDetector = new GestureDetector(var1, new SimpleOnGestureListener() {
         public void onLongPress(MotionEvent var1) {
            View var3x = var2.findChildViewUnder(var1.getX(), var1.getY());
            if (var3x != null) {
               RecyclerTouchListener.ClickListener var2x = var3;
               if (var2x != null) {
                  var2x.onLongClick(var3x, var2.getChildPosition(var3x));
                  return;
               }
            }

         }

         public boolean onSingleTapUp(MotionEvent var1) {
            return true;
         }
      });
   }

   public boolean onInterceptTouchEvent(@NonNull RecyclerView var1, @NonNull MotionEvent var2) {
      View var3 = var1.findChildViewUnder(var2.getX(), var2.getY());
      if (var3 != null && this.clickListener != null && this.gestureDetector.onTouchEvent(var2)) {
         this.clickListener.onClick(var3, var1.getChildPosition(var3));
      }

      return false;
   }

   public void onRequestDisallowInterceptTouchEvent(boolean var1) {
   }

   public void onTouchEvent(@NonNull RecyclerView var1, @NonNull MotionEvent var2) {
   }

   public interface ClickListener {
      void onClick(View var1, int var2);

      void onLongClick(View var1, int var2);
   }
}
