package com.codetroopers.betterpickers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class AutoScrollHorizontalScrollView extends HorizontalScrollView {
   public AutoScrollHorizontalScrollView(Context var1) {
      super(var1);
   }

   public AutoScrollHorizontalScrollView(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public AutoScrollHorizontalScrollView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.fullScroll(66);
   }
}
