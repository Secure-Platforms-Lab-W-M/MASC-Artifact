package com.codetroopers.betterpickers.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.styleable;
import com.codetroopers.betterpickers.widget.PickerLinearLayout;
import com.codetroopers.betterpickers.widget.UnderlinePageIndicatorPicker;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

public class DateView extends PickerLinearLayout {
   private final Typeface mAndroidClockMonoThin;
   private ZeroTopPaddingTextView mDate;
   private ZeroTopPaddingTextView mMonth;
   private Typeface mOriginalNumberTypeface;
   private ColorStateList mTitleColor;
   private UnderlinePageIndicatorPicker mUnderlinePageIndicatorPicker;
   private ZeroTopPaddingTextView mYearLabel;

   public DateView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public DateView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mAndroidClockMonoThin = Typeface.createFromAsset(var1.getAssets(), "fonts/AndroidClockMono-Thin.ttf");
      this.mOriginalNumberTypeface = Typeface.createFromAsset(var1.getAssets(), "fonts/Roboto-Bold.ttf");
      this.mTitleColor = this.getResources().getColorStateList(color.dialog_text_color_holo_dark);
      this.setWillNotDraw(false);
   }

   private void restyleViews() {
      ZeroTopPaddingTextView var1 = this.mMonth;
      if (var1 != null) {
         var1.setTextColor(this.mTitleColor);
      }

      var1 = this.mDate;
      if (var1 != null) {
         var1.setTextColor(this.mTitleColor);
      }

      var1 = this.mYearLabel;
      if (var1 != null) {
         var1.setTextColor(this.mTitleColor);
      }

   }

   public ZeroTopPaddingTextView getDate() {
      return this.mDate;
   }

   public ZeroTopPaddingTextView getMonth() {
      return this.mMonth;
   }

   public View getViewAt(int var1) {
      return this.getChildAt(var1);
   }

   public ZeroTopPaddingTextView getYear() {
      return this.mYearLabel;
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      this.mUnderlinePageIndicatorPicker.setTitleView(this);
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.mMonth = (ZeroTopPaddingTextView)this.findViewById(id.month);
      this.mDate = (ZeroTopPaddingTextView)this.findViewById(id.date);
      this.mYearLabel = (ZeroTopPaddingTextView)this.findViewById(id.year_label);
      char[] var3 = DateFormat.getDateFormatOrder(this.getContext());
      this.removeAllViews();

      for(int var1 = 0; var1 < var3.length; ++var1) {
         char var2 = var3[var1];
         if (var2 != 'M') {
            if (var2 != 'd') {
               if (var2 == 'y') {
                  this.addView(this.mYearLabel);
               }
            } else {
               this.addView(this.mDate);
            }
         } else {
            this.addView(this.mMonth);
         }
      }

      ZeroTopPaddingTextView var4 = this.mDate;
      if (var4 != null) {
         var4.setTypeface(this.mAndroidClockMonoThin);
         this.mDate.updatePadding();
      }

      var4 = this.mMonth;
      if (var4 != null) {
         var4.setTypeface(this.mAndroidClockMonoThin);
         this.mMonth.updatePadding();
      }

      this.restyleViews();
   }

   public void setDate(String var1, int var2, int var3) {
      if (this.mMonth != null) {
         if (var1.equals("")) {
            this.mMonth.setText("-");
            this.mMonth.setTypeface(this.mAndroidClockMonoThin);
            this.mMonth.setEnabled(false);
            this.mMonth.updatePadding();
         } else {
            this.mMonth.setText(var1);
            this.mMonth.setTypeface(this.mOriginalNumberTypeface);
            this.mMonth.setEnabled(true);
            this.mMonth.updatePaddingForBoldDate();
         }
      }

      ZeroTopPaddingTextView var5 = this.mDate;
      if (var5 != null) {
         if (var2 <= 0) {
            var5.setText("-");
            this.mDate.setEnabled(false);
            this.mDate.updatePadding();
         } else {
            var5.setText(Integer.toString(var2));
            this.mDate.setEnabled(true);
            this.mDate.updatePadding();
         }
      }

      var5 = this.mYearLabel;
      if (var5 != null) {
         if (var3 <= 0) {
            var5.setText("----");
            this.mYearLabel.setEnabled(false);
            this.mYearLabel.updatePadding();
            return;
         }

         StringBuilder var4;
         for(var1 = Integer.toString(var3); var1.length() < 4; var1 = var4.toString()) {
            var4 = new StringBuilder();
            var4.append("-");
            var4.append(var1);
         }

         this.mYearLabel.setText(var1);
         this.mYearLabel.setEnabled(true);
         this.mYearLabel.updatePadding();
      }

   }

   public void setOnClick(OnClickListener var1) {
      this.mDate.setOnClickListener(var1);
      this.mMonth.setOnClickListener(var1);
      this.mYearLabel.setOnClickListener(var1);
   }

   public void setTheme(int var1) {
      if (var1 != -1) {
         this.mTitleColor = this.getContext().obtainStyledAttributes(var1, styleable.BetterPickersDialogFragment).getColorStateList(styleable.BetterPickersDialogFragment_bpTitleColor);
      }

      this.restyleViews();
   }

   public void setUnderlinePage(UnderlinePageIndicatorPicker var1) {
      this.mUnderlinePageIndicatorPicker = var1;
   }
}
