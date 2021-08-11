package com.codetroopers.betterpickers.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.TextView;
import com.codetroopers.betterpickers.R.string;

public class ZeroTopPaddingTextView extends TextView {
   private static final float BOLD_FONT_BOTTOM_PADDING_RATIO = 0.208F;
   private static final float BOLD_FONT_PADDING_RATIO = 0.208F;
   private static final float NORMAL_FONT_BOTTOM_PADDING_RATIO = 0.25F;
   private static final float NORMAL_FONT_PADDING_RATIO = 0.328F;
   private static final float PRE_ICS_BOTTOM_PADDING_RATIO = 0.233F;
   private static final Typeface SAN_SERIF_BOLD = Typeface.create("san-serif", 1);
   private static final Typeface SAN_SERIF_CONDENSED_BOLD = Typeface.create("sans-serif-condensed", 1);
   private String decimalSeperator;
   private int mPaddingRight;
   private String timeSeperator;

   public ZeroTopPaddingTextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ZeroTopPaddingTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ZeroTopPaddingTextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mPaddingRight = 0;
      this.decimalSeperator = "";
      this.timeSeperator = "";
      this.init();
      this.setIncludeFontPadding(false);
      this.updatePadding();
   }

   private void init() {
      this.decimalSeperator = this.getResources().getString(string.number_picker_seperator);
      this.timeSeperator = this.getResources().getString(string.time_picker_time_seperator);
   }

   public void setPaddingRight(int var1) {
      this.mPaddingRight = var1;
      this.updatePadding();
   }

   public void updatePadding() {
      float var2 = 0.328F;
      float var4 = 0.25F;
      float var3 = var2;
      float var1 = var4;
      if (this.getPaint().getTypeface() != null) {
         var3 = var2;
         var1 = var4;
         if (this.getPaint().getTypeface().equals(Typeface.DEFAULT_BOLD)) {
            var3 = 0.208F;
            var1 = 0.208F;
         }
      }

      var4 = var3;
      var2 = var1;
      if (this.getTypeface() != null) {
         var4 = var3;
         var2 = var1;
         if (this.getTypeface().equals(SAN_SERIF_BOLD)) {
            var4 = 0.208F;
            var2 = 0.208F;
         }
      }

      var3 = var4;
      var1 = var2;
      if (this.getTypeface() != null) {
         var3 = var4;
         var1 = var2;
         if (this.getTypeface().equals(SAN_SERIF_CONDENSED_BOLD)) {
            var3 = 0.208F;
            var1 = 0.208F;
         }
      }

      var2 = var1;
      if (VERSION.SDK_INT < 14) {
         var2 = var1;
         if (this.getText() != null) {
            label27: {
               if (!this.getText().toString().equals(this.decimalSeperator)) {
                  var2 = var1;
                  if (!this.getText().toString().equals(this.timeSeperator)) {
                     break label27;
                  }
               }

               var2 = 0.233F;
            }
         }
      }

      this.setPadding(0, (int)(-var3 * this.getTextSize()), this.mPaddingRight, (int)(-var2 * this.getTextSize()));
   }

   public void updatePaddingForBoldDate() {
      this.setPadding(0, (int)(-0.208F * this.getTextSize()), this.mPaddingRight, (int)(-0.208F * this.getTextSize()));
   }
}
