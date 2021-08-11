package com.codetroopers.betterpickers.expirationpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.styleable;
import com.codetroopers.betterpickers.widget.PickerLinearLayout;
import com.codetroopers.betterpickers.widget.UnderlinePageIndicatorPicker;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

public class ExpirationView extends PickerLinearLayout {
   private final Typeface mAndroidClockMonoThin;
   private ZeroTopPaddingTextView mMonth;
   private Typeface mOriginalNumberTypeface;
   private ZeroTopPaddingTextView mSeperator;
   private ColorStateList mTitleColor;
   private UnderlinePageIndicatorPicker mUnderlinePageIndicatorPicker;
   private ZeroTopPaddingTextView mYearLabel;

   public ExpirationView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ExpirationView(Context var1, AttributeSet var2) {
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

      var1 = this.mYearLabel;
      if (var1 != null) {
         var1.setTextColor(this.mTitleColor);
      }

      var1 = this.mSeperator;
      if (var1 != null) {
         var1.setTextColor(this.mTitleColor);
      }

   }

   public ZeroTopPaddingTextView getMonth() {
      return this.mMonth;
   }

   public View getViewAt(int var1) {
      int[] var2 = new int[]{0, 2};
      return var1 > var2.length ? null : this.getChildAt(var2[var1]);
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
      this.mYearLabel = (ZeroTopPaddingTextView)this.findViewById(id.year_label);
      this.mSeperator = (ZeroTopPaddingTextView)this.findViewById(id.expiration_seperator);
      ZeroTopPaddingTextView var1 = this.mMonth;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
         this.mMonth.updatePadding();
      }

      var1 = this.mYearLabel;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
      }

      var1 = this.mSeperator;
      if (var1 != null) {
         var1.setTypeface(this.mAndroidClockMonoThin);
      }

      this.restyleViews();
   }

   public void setExpiration(String var1, int var2) {
      if (this.mMonth != null) {
         if (var1.equals("")) {
            this.mMonth.setText("--");
            this.mMonth.setEnabled(false);
            this.mMonth.updatePadding();
         } else {
            this.mMonth.setText(var1);
            this.mMonth.setEnabled(true);
            this.mMonth.updatePadding();
         }
      }

      ZeroTopPaddingTextView var4 = this.mYearLabel;
      if (var4 != null) {
         if (var2 <= 0) {
            var4.setText("----");
            this.mYearLabel.setEnabled(false);
            this.mYearLabel.updatePadding();
            return;
         }

         StringBuilder var3;
         for(var1 = Integer.toString(var2); var1.length() < 4; var1 = var3.toString()) {
            var3 = new StringBuilder();
            var3.append(var1);
            var3.append("-");
         }

         this.mYearLabel.setText(var1);
         this.mYearLabel.setEnabled(true);
         this.mYearLabel.updatePadding();
      }

   }

   public void setOnClick(OnClickListener var1) {
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
