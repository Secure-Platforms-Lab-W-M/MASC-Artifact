package com.codetroopers.betterpickers.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.styleable;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

public class TimerView extends LinearLayout {
   private final Typeface mAndroidClockMonoThin;
   private ZeroTopPaddingTextView mHoursOnes;
   private ZeroTopPaddingTextView mHoursSeperator;
   private ZeroTopPaddingTextView mHoursTens;
   private ZeroTopPaddingTextView mMinutesOnes;
   private ZeroTopPaddingTextView mMinutesTens;
   private Typeface mOriginalHoursTypeface;
   private ColorStateList mTextColor;

   public TimerView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TimerView(Context var1, AttributeSet var2) {
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

      var1 = this.mHoursTens;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mMinutesTens;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

      var1 = this.mHoursSeperator;
      if (var1 != null) {
         var1.setTextColor(this.mTextColor);
      }

   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.mHoursTens = (ZeroTopPaddingTextView)this.findViewById(id.hours_tens);
      this.mMinutesTens = (ZeroTopPaddingTextView)this.findViewById(id.minutes_tens);
      this.mHoursOnes = (ZeroTopPaddingTextView)this.findViewById(id.hours_ones);
      this.mMinutesOnes = (ZeroTopPaddingTextView)this.findViewById(id.minutes_ones);
      this.mHoursSeperator = (ZeroTopPaddingTextView)this.findViewById(id.hours_seperator);
      ZeroTopPaddingTextView var1 = this.mHoursOnes;
      if (var1 != null) {
         this.mOriginalHoursTypeface = var1.getTypeface();
      }

      var1 = this.mMinutesTens;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
         this.mMinutesTens.updatePadding();
      }

      var1 = this.mMinutesOnes;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
         this.mMinutesOnes.updatePadding();
      }

   }

   public void setTheme(int var1) {
      if (var1 != -1) {
         this.mTextColor = this.getContext().obtainStyledAttributes(var1, styleable.BetterPickersDialogFragment).getColorStateList(styleable.BetterPickersDialogFragment_bpTextColor);
      }

      this.restyleViews();
   }

   public void setTime(int var1, int var2, int var3, int var4) {
      ZeroTopPaddingTextView var5 = this.mHoursTens;
      if (var5 != null) {
         if (var1 == -2) {
            var5.setVisibility(4);
         } else if (var1 == -1) {
            var5.setText("-");
            this.mHoursTens.setTypeface(this.mAndroidClockMonoThin);
            this.mHoursTens.setEnabled(false);
            this.mHoursTens.updatePadding();
            this.mHoursTens.setVisibility(0);
         } else {
            var5.setText(String.format("%d", var1));
            this.mHoursTens.setTypeface(this.mOriginalHoursTypeface);
            this.mHoursTens.setEnabled(true);
            this.mHoursTens.updatePaddingForBoldDate();
            this.mHoursTens.setVisibility(0);
         }
      }

      var5 = this.mHoursOnes;
      if (var5 != null) {
         if (var2 == -1) {
            var5.setText("-");
            this.mHoursOnes.setTypeface(this.mAndroidClockMonoThin);
            this.mHoursOnes.setEnabled(false);
            this.mHoursOnes.updatePadding();
         } else {
            var5.setText(String.format("%d", var2));
            this.mHoursOnes.setTypeface(this.mOriginalHoursTypeface);
            this.mHoursOnes.setEnabled(true);
            this.mHoursOnes.updatePaddingForBoldDate();
         }
      }

      var5 = this.mMinutesTens;
      if (var5 != null) {
         if (var3 == -1) {
            var5.setText("-");
            this.mMinutesTens.setEnabled(false);
         } else {
            var5.setEnabled(true);
            this.mMinutesTens.setText(String.format("%d", var3));
         }
      }

      var5 = this.mMinutesOnes;
      if (var5 != null) {
         if (var4 == -1) {
            var5.setText("-");
            this.mMinutesOnes.setEnabled(false);
            return;
         }

         var5.setText(String.format("%d", var4));
         this.mMinutesOnes.setEnabled(true);
      }

   }
}
