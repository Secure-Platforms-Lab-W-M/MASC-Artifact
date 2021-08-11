package com.codetroopers.betterpickers.hmspicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.styleable;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

public class HmsView extends LinearLayout {
   private final Typeface mAndroidClockMonoThin;
   private ZeroTopPaddingTextView mHoursOnes;
   private ZeroTopPaddingTextView mMinusLabel;
   private ZeroTopPaddingTextView mMinutesOnes;
   private ZeroTopPaddingTextView mMinutesTens;
   private Typeface mOriginalHoursTypeface;
   private ZeroTopPaddingTextView mSecondsOnes;
   private ZeroTopPaddingTextView mSecondsTens;
   private ColorStateList mTextColor;

   public HmsView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public HmsView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mAndroidClockMonoThin = Typeface.createFromAsset(var1.getAssets(), "fonts/AndroidClockMono-Thin.ttf");
      this.mTextColor = this.getResources().getColorStateList(color.dialog_text_color_holo_dark);
   }

   private void restyleViews() {
      ZeroTopPaddingTextView var1 = this.mHoursOnes;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mMinutesOnes;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mMinutesTens;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mSecondsOnes;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mSecondsTens;
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
      this.mHoursOnes = (ZeroTopPaddingTextView)this.findViewById(id.hours_ones);
      this.mMinutesTens = (ZeroTopPaddingTextView)this.findViewById(id.minutes_tens);
      this.mMinutesOnes = (ZeroTopPaddingTextView)this.findViewById(id.minutes_ones);
      this.mSecondsTens = (ZeroTopPaddingTextView)this.findViewById(id.seconds_tens);
      this.mSecondsOnes = (ZeroTopPaddingTextView)this.findViewById(id.seconds_ones);
      this.mMinusLabel = (ZeroTopPaddingTextView)this.findViewById(id.minus_label);
      ZeroTopPaddingTextView var1 = this.mHoursOnes;
      if (var1 != null) {
         this.mOriginalHoursTypeface = var1.getTypeface();
         this.mHoursOnes.updatePaddingForBoldDate();
      }

      var1 = this.mMinutesTens;
      if (var1 != null) {
         var1.updatePaddingForBoldDate();
      }

      var1 = this.mMinutesOnes;
      if (var1 != null) {
         var1.updatePaddingForBoldDate();
      }

      var1 = this.mSecondsTens;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
         this.mSecondsTens.updatePadding();
      }

      var1 = this.mSecondsOnes;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
         this.mSecondsOnes.updatePadding();
      }

   }

   public void setTheme(int var1) {
      if (var1 != -1) {
         this.mTextColor = this.getContext().obtainStyledAttributes(var1, styleable.BetterPickersDialogFragment).getColorStateList(styleable.BetterPickersDialogFragment_bpTextColor);
      }

      this.restyleViews();
   }

   public void setTime(int var1, int var2, int var3, int var4, int var5) {
      this.setTime(false, var1, var2, var3, var4, var5);
   }

   public void setTime(boolean var1, int var2, int var3, int var4, int var5, int var6) {
      ZeroTopPaddingTextView var8 = this.mMinusLabel;
      byte var7;
      if (var1) {
         var7 = 0;
      } else {
         var7 = 8;
      }

      var8.setVisibility(var7);
      var8 = this.mHoursOnes;
      if (var8 != null) {
         var8.setText(String.format("%d", var2));
      }

      var8 = this.mMinutesTens;
      if (var8 != null) {
         var8.setText(String.format("%d", var3));
      }

      var8 = this.mMinutesOnes;
      if (var8 != null) {
         var8.setText(String.format("%d", var4));
      }

      var8 = this.mSecondsTens;
      if (var8 != null) {
         var8.setText(String.format("%d", var5));
      }

      var8 = this.mSecondsOnes;
      if (var8 != null) {
         var8.setText(String.format("%d", var6));
      }

   }
}
