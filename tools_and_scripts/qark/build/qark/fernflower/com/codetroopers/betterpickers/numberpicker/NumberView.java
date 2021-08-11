package com.codetroopers.betterpickers.numberpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.styleable;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

public class NumberView extends LinearLayout {
   private final Typeface mAndroidClockMonoThin;
   private ZeroTopPaddingTextView mDecimal;
   private ZeroTopPaddingTextView mDecimalSeperator;
   private ZeroTopPaddingTextView mMinusLabel;
   private ZeroTopPaddingTextView mNumber;
   private Typeface mOriginalNumberTypeface;
   private ColorStateList mTextColor;

   public NumberView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NumberView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mAndroidClockMonoThin = Typeface.createFromAsset(var1.getAssets(), "fonts/AndroidClockMono-Thin.ttf");
      this.mTextColor = this.getResources().getColorStateList(color.dialog_text_color_holo_dark);
   }

   private void restyleViews() {
      ZeroTopPaddingTextView var1 = this.mNumber;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mDecimal;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mDecimalSeperator;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mMinusLabel;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.mNumber = (ZeroTopPaddingTextView)this.findViewById(id.number);
      this.mDecimal = (ZeroTopPaddingTextView)this.findViewById(id.decimal);
      this.mDecimalSeperator = (ZeroTopPaddingTextView)this.findViewById(id.decimal_separator);
      this.mMinusLabel = (ZeroTopPaddingTextView)this.findViewById(id.minus_label);
      ZeroTopPaddingTextView var1 = this.mNumber;
      if (var1 != null) {
         this.mOriginalNumberTypeface = var1.getTypeface();
      }

      var1 = this.mNumber;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
         this.mNumber.updatePadding();
      }

      var1 = this.mDecimal;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
         this.mDecimal.updatePadding();
      }

      this.restyleViews();
   }

   public void setNumber(String var1, String var2, boolean var3, boolean var4) {
      ZeroTopPaddingTextView var7 = this.mMinusLabel;
      byte var6 = 8;
      byte var5;
      if (var4) {
         var5 = 0;
      } else {
         var5 = 8;
      }

      var7.setVisibility(var5);
      if (this.mNumber != null) {
         if (var1.equals("")) {
            this.mNumber.setText("-");
            this.mNumber.setTypeface(this.mAndroidClockMonoThin);
            this.mNumber.setEnabled(false);
            this.mNumber.updatePadding();
            this.mNumber.setVisibility(0);
         } else if (var3) {
            this.mNumber.setText(var1);
            this.mNumber.setTypeface(this.mOriginalNumberTypeface);
            this.mNumber.setEnabled(true);
            this.mNumber.updatePaddingForBoldDate();
            this.mNumber.setVisibility(0);
         } else {
            this.mNumber.setText(var1);
            this.mNumber.setTypeface(this.mAndroidClockMonoThin);
            this.mNumber.setEnabled(true);
            this.mNumber.updatePadding();
            this.mNumber.setVisibility(0);
         }
      }

      if (this.mDecimal != null) {
         if (var2.equals("")) {
            this.mDecimal.setVisibility(8);
         } else {
            this.mDecimal.setText(var2);
            this.mDecimal.setTypeface(this.mAndroidClockMonoThin);
            this.mDecimal.setEnabled(true);
            this.mDecimal.updatePadding();
            this.mDecimal.setVisibility(0);
         }
      }

      ZeroTopPaddingTextView var8 = this.mDecimalSeperator;
      if (var8 != null) {
         var5 = var6;
         if (var3) {
            var5 = 0;
         }

         var8.setVisibility(var5);
      }

   }

   public void setTheme(int var1) {
      if (var1 != -1) {
         this.mTextColor = this.getContext().obtainStyledAttributes(var1, styleable.BetterPickersDialogFragment).getColorStateList(styleable.BetterPickersDialogFragment_bpTextColor);
      }

      this.restyleViews();
   }
}
