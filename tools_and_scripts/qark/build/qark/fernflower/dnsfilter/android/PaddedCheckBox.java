package dnsfilter.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class PaddedCheckBox extends CheckBox {
   private static int dpAsPx_10 = 0;
   private static int dpAsPx_32 = 0;

   public PaddedCheckBox(Context var1) {
      super(var1);
      this.doPadding();
   }

   public PaddedCheckBox(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.doPadding();
   }

   public PaddedCheckBox(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.doPadding();
   }

   @SuppressLint({"NewApi"})
   public PaddedCheckBox(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.doPadding();
   }

   private int convertDpToPx(int var1) {
      float var2 = this.getResources().getDisplayMetrics().density;
      return (int)((float)var1 * var2 + 0.5F);
   }

   private void doPadding() {
      if (dpAsPx_32 == 0) {
         dpAsPx_32 = this.convertDpToPx(32);
         dpAsPx_10 = this.convertDpToPx(10);
      }

      if (VERSION.SDK_INT >= 17) {
         this.setPadding(dpAsPx_10, dpAsPx_10, dpAsPx_10, dpAsPx_10);
      } else {
         this.setPadding(dpAsPx_32, 0, 0, 0);
      }
   }
}
