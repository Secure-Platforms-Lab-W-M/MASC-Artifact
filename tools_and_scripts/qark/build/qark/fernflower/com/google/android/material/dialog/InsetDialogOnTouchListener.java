package com.google.android.material.dialog;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnTouchListener;

public class InsetDialogOnTouchListener implements OnTouchListener {
   private final Dialog dialog;
   private final int leftInset;
   private final int prePieSlop;
   private final int topInset;

   public InsetDialogOnTouchListener(Dialog var1, Rect var2) {
      this.dialog = var1;
      this.leftInset = var2.left;
      this.topInset = var2.top;
      this.prePieSlop = ViewConfiguration.get(var1.getContext()).getScaledWindowTouchSlop();
   }

   public boolean onTouch(View var1, MotionEvent var2) {
      View var7 = var1.findViewById(16908290);
      int var3 = this.leftInset + var7.getLeft();
      int var4 = var7.getWidth();
      int var5 = this.topInset + var7.getTop();
      int var6 = var7.getHeight();
      if ((new RectF((float)var3, (float)var5, (float)(var4 + var3), (float)(var6 + var5))).contains(var2.getX(), var2.getY())) {
         return false;
      } else {
         var2 = MotionEvent.obtain(var2);
         var2.setAction(4);
         if (VERSION.SDK_INT < 28) {
            var2.setAction(0);
            var3 = this.prePieSlop;
            var2.setLocation((float)(-var3 - 1), (float)(-var3 - 1));
         }

         var1.performClick();
         return this.dialog.onTouchEvent(var2);
      }
   }
}
