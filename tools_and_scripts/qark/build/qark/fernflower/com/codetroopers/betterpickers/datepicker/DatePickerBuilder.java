package com.codetroopers.betterpickers.datepicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import java.util.Vector;

public class DatePickerBuilder {
   private Integer dayOfMonth;
   private Vector mDatePickerDialogHandlers = new Vector();
   private OnDialogDismissListener mOnDismissListener;
   private int mReference = -1;
   private FragmentManager manager;
   private Integer monthOfYear;
   private Integer styleResId;
   private Fragment targetFragment;
   private Integer year;
   private Boolean yearOptional = false;

   public DatePickerBuilder addDatePickerDialogHandler(DatePickerDialogFragment.DatePickerDialogHandler var1) {
      this.mDatePickerDialogHandlers.add(var1);
      return this;
   }

   public DatePickerBuilder removeDatePickerDialogHandler(DatePickerDialogFragment.DatePickerDialogHandler var1) {
      this.mDatePickerDialogHandlers.remove(var1);
      return this;
   }

   public DatePickerBuilder setDayOfMonth(int var1) {
      this.dayOfMonth = var1;
      return this;
   }

   public DatePickerBuilder setFragmentManager(FragmentManager var1) {
      this.manager = var1;
      return this;
   }

   public DatePickerBuilder setMonthOfYear(int var1) {
      this.monthOfYear = var1;
      return this;
   }

   public DatePickerBuilder setOnDismissListener(OnDialogDismissListener var1) {
      this.mOnDismissListener = var1;
      return this;
   }

   public DatePickerBuilder setReference(int var1) {
      this.mReference = var1;
      return this;
   }

   public DatePickerBuilder setStyleResId(int var1) {
      this.styleResId = var1;
      return this;
   }

   public DatePickerBuilder setTargetFragment(Fragment var1) {
      this.targetFragment = var1;
      return this;
   }

   public DatePickerBuilder setYear(int var1) {
      this.year = var1;
      return this;
   }

   public DatePickerBuilder setYearOptional(boolean var1) {
      this.yearOptional = var1;
      return this;
   }

   public void show() {
      FragmentManager var1 = this.manager;
      if (var1 != null && this.styleResId != null) {
         FragmentTransaction var2 = var1.beginTransaction();
         Fragment var3 = this.manager.findFragmentByTag("date_dialog");
         FragmentTransaction var4 = var2;
         if (var3 != null) {
            var2.remove(var3).commit();
            var4 = this.manager.beginTransaction();
         }

         var4.addToBackStack((String)null);
         DatePickerDialogFragment var5 = DatePickerDialogFragment.newInstance(this.mReference, this.styleResId, this.monthOfYear, this.dayOfMonth, this.year, this.yearOptional);
         var3 = this.targetFragment;
         if (var3 != null) {
            var5.setTargetFragment(var3, 0);
         }

         var5.setDatePickerDialogHandlers(this.mDatePickerDialogHandlers);
         var5.setOnDismissListener(this.mOnDismissListener);
         var5.show(var4, "date_dialog");
      } else {
         Log.e("DatePickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
      }
   }
}
