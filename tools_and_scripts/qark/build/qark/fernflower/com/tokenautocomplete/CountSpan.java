package com.tokenautocomplete;

import android.content.Context;
import android.widget.TextView;

public class CountSpan extends ViewSpan {
   public String text = "";

   public CountSpan(int var1, Context var2, int var3, int var4, int var5) {
      super(new TextView(var2), var5);
      TextView var6 = (TextView)this.view;
      var6.setTextColor(var3);
      var6.setTextSize(0, (float)var4);
      this.setCount(var1);
   }

   public void setCount(int var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("+");
      var2.append(var1);
      this.text = var2.toString();
      ((TextView)this.view).setText(this.text);
   }
}
