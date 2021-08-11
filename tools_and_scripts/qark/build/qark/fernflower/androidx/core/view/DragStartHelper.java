package androidx.core.view;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public class DragStartHelper {
   private boolean mDragging;
   private int mLastTouchX;
   private int mLastTouchY;
   private final DragStartHelper.OnDragStartListener mListener;
   private final OnLongClickListener mLongClickListener = new OnLongClickListener() {
      public boolean onLongClick(View var1) {
         return DragStartHelper.this.onLongClick(var1);
      }
   };
   private final OnTouchListener mTouchListener = new OnTouchListener() {
      public boolean onTouch(View var1, MotionEvent var2) {
         return DragStartHelper.this.onTouch(var1, var2);
      }
   };
   private final View mView;

   public DragStartHelper(View var1, DragStartHelper.OnDragStartListener var2) {
      this.mView = var1;
      this.mListener = var2;
   }

   public void attach() {
      this.mView.setOnLongClickListener(this.mLongClickListener);
      this.mView.setOnTouchListener(this.mTouchListener);
   }

   public void detach() {
      this.mView.setOnLongClickListener((OnLongClickListener)null);
      this.mView.setOnTouchListener((OnTouchListener)null);
   }

   public void getTouchPosition(Point var1) {
      var1.set(this.mLastTouchX, this.mLastTouchY);
   }

   public boolean onLongClick(View var1) {
      return this.mListener.onDragStart(var1, this);
   }

   public boolean onTouch(View var1, MotionEvent var2) {
      int var3 = (int)var2.getX();
      int var4 = (int)var2.getY();
      int var5 = var2.getAction();
      if (var5 != 0) {
         if (var5 != 1) {
            if (var5 == 2) {
               if (MotionEventCompat.isFromSource(var2, 8194)) {
                  if ((var2.getButtonState() & 1) == 0) {
                     return false;
                  }

                  if (this.mDragging) {
                     return false;
                  }

                  if (this.mLastTouchX == var3 && this.mLastTouchY == var4) {
                     return false;
                  }

                  this.mLastTouchX = var3;
                  this.mLastTouchY = var4;
                  boolean var6 = this.mListener.onDragStart(var1, this);
                  this.mDragging = var6;
                  return var6;
               }

               return false;
            }

            if (var5 != 3) {
               return false;
            }
         }

         this.mDragging = false;
         return false;
      } else {
         this.mLastTouchX = var3;
         this.mLastTouchY = var4;
         return false;
      }
   }

   public interface OnDragStartListener {
      boolean onDragStart(View var1, DragStartHelper var2);
   }
}
