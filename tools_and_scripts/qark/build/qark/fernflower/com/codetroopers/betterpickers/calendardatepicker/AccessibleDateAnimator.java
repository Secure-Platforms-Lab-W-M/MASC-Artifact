package com.codetroopers.betterpickers.calendardatepicker;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ViewAnimator;

public class AccessibleDateAnimator extends ViewAnimator {
   private long mDateMillis;

   public AccessibleDateAnimator(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      if (var1.getEventType() == 32) {
         var1.getText().clear();
         String var2 = DateUtils.formatDateTime(this.getContext(), this.mDateMillis, 22);
         var1.getText().add(var2);
         return true;
      } else {
         return super.dispatchPopulateAccessibilityEvent(var1);
      }
   }

   public void setDateMillis(long var1) {
      this.mDateMillis = var1;
   }
}
