package com.codetroopers.betterpickers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public abstract class PickerLinearLayout extends LinearLayout {
   public PickerLinearLayout(Context var1) {
      super(var1);
   }

   public PickerLinearLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public abstract View getViewAt(int var1);
}
