package com.google.android.material.datepicker;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.google.android.material.R.string;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

abstract class DateFormatTextWatcher implements TextWatcher {
   private final CalendarConstraints constraints;
   private final DateFormat dateFormat;
   private final String formatHint;
   private final String outOfRange;
   private final TextInputLayout textInputLayout;

   DateFormatTextWatcher(String var1, DateFormat var2, TextInputLayout var3, CalendarConstraints var4) {
      this.formatHint = var1;
      this.dateFormat = var2;
      this.textInputLayout = var3;
      this.constraints = var4;
      this.outOfRange = var3.getContext().getString(string.mtrl_picker_out_of_range);
   }

   public void afterTextChanged(Editable var1) {
   }

   public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
   }

   void onInvalidDate() {
   }

   public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      if (TextUtils.isEmpty(var1)) {
         this.textInputLayout.setError((CharSequence)null);
         this.onValidDate((Long)null);
      } else {
         try {
            Date var13 = this.dateFormat.parse(var1.toString());
            this.textInputLayout.setError((CharSequence)null);
            long var5 = var13.getTime();
            if (this.constraints.getDateValidator().isValid(var5) && this.constraints.isWithinBounds(var5)) {
               this.onValidDate(var13.getTime());
            } else {
               this.textInputLayout.setError(String.format(this.outOfRange, DateStrings.getDateString(var5)));
               this.onInvalidDate();
            }

         } catch (ParseException var11) {
            String var12 = this.textInputLayout.getContext().getString(string.mtrl_picker_invalid_format);
            String var7 = String.format(this.textInputLayout.getContext().getString(string.mtrl_picker_invalid_format_use), this.formatHint);
            String var8 = String.format(this.textInputLayout.getContext().getString(string.mtrl_picker_invalid_format_example), this.dateFormat.format(new Date(UtcDates.getTodayCalendar().getTimeInMillis())));
            TextInputLayout var9 = this.textInputLayout;
            StringBuilder var10 = new StringBuilder();
            var10.append(var12);
            var10.append("\n");
            var10.append(var7);
            var10.append("\n");
            var10.append(var8);
            var9.setError(var10.toString());
            this.onInvalidDate();
         }
      }
   }

   abstract void onValidDate(Long var1);
}
