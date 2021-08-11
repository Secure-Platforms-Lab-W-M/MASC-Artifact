package com.codetroopers.betterpickers.datepicker;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.R.color;
import com.codetroopers.betterpickers.R.drawable;
import com.codetroopers.betterpickers.R.id;
import com.codetroopers.betterpickers.R.layout;
import com.codetroopers.betterpickers.R.styleable;
import java.util.Iterator;
import java.util.Vector;

public class DatePickerDialogFragment extends DialogFragment {
   private static final String DAY_KEY = "DatePickerDialogFragment_DayKey";
   private static final String MONTH_KEY = "DatePickerDialogFragment_MonthKey";
   private static final String REFERENCE_KEY = "DatePickerDialogFragment_ReferenceKey";
   private static final String THEME_RES_ID_KEY = "DatePickerDialogFragment_ThemeResIdKey";
   private static final String YEAR_KEY = "DatePickerDialogFragment_YearKey";
   private static final String YEAR_OPTIONAL_KEY = "DatePickerDialogFragment_YearOptionalKey";
   private Vector mDatePickerDialogHandlers = new Vector();
   private int mDayOfMonth = 0;
   private int mDialogBackgroundResId;
   private OnDialogDismissListener mDismissCallback;
   private int mMonthOfYear = -1;
   private DatePicker mPicker;
   private int mReference = -1;
   private ColorStateList mTextColor;
   private int mTheme = -1;
   private int mYear = 0;
   private boolean mYearOptional = false;

   public static DatePickerDialogFragment newInstance(int var0, int var1, Integer var2, Integer var3, Integer var4, Boolean var5) {
      DatePickerDialogFragment var6 = new DatePickerDialogFragment();
      Bundle var7 = new Bundle();
      var7.putInt("DatePickerDialogFragment_ReferenceKey", var0);
      var7.putInt("DatePickerDialogFragment_ThemeResIdKey", var1);
      if (var2 != null) {
         var7.putInt("DatePickerDialogFragment_MonthKey", var2);
      }

      if (var3 != null) {
         var7.putInt("DatePickerDialogFragment_DayKey", var3);
      }

      if (var4 != null) {
         var7.putInt("DatePickerDialogFragment_YearKey", var4);
      }

      var7.putBoolean("DatePickerDialogFragment_YearOptionalKey", var5);
      var6.setArguments(var7);
      return var6;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      var1 = this.getArguments();
      if (var1 != null && var1.containsKey("DatePickerDialogFragment_ReferenceKey")) {
         this.mReference = var1.getInt("DatePickerDialogFragment_ReferenceKey");
      }

      if (var1 != null && var1.containsKey("DatePickerDialogFragment_ThemeResIdKey")) {
         this.mTheme = var1.getInt("DatePickerDialogFragment_ThemeResIdKey");
      }

      if (var1 != null && var1.containsKey("DatePickerDialogFragment_MonthKey")) {
         this.mMonthOfYear = var1.getInt("DatePickerDialogFragment_MonthKey");
      }

      if (var1 != null && var1.containsKey("DatePickerDialogFragment_DayKey")) {
         this.mDayOfMonth = var1.getInt("DatePickerDialogFragment_DayKey");
      }

      if (var1 != null && var1.containsKey("DatePickerDialogFragment_YearKey")) {
         this.mYear = var1.getInt("DatePickerDialogFragment_YearKey");
      }

      if (var1 != null && var1.containsKey("DatePickerDialogFragment_YearOptionalKey")) {
         this.mYearOptional = var1.getBoolean("DatePickerDialogFragment_YearOptionalKey");
      }

      this.setStyle(1, 0);
      this.mTextColor = this.getResources().getColorStateList(color.dialog_text_color_holo_dark);
      this.mDialogBackgroundResId = drawable.dialog_full_holo_dark;
      if (this.mTheme != -1) {
         TypedArray var2 = this.getActivity().getApplicationContext().obtainStyledAttributes(this.mTheme, styleable.BetterPickersDialogFragment);
         this.mTextColor = var2.getColorStateList(styleable.BetterPickersDialogFragment_bpTextColor);
         this.mDialogBackgroundResId = var2.getResourceId(styleable.BetterPickersDialogFragment_bpDialogBackground, this.mDialogBackgroundResId);
      }

   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = var1.inflate(layout.date_picker_dialog, var2, false);
      Button var5 = (Button)var4.findViewById(id.done_button);
      Button var6 = (Button)var4.findViewById(id.cancel_button);
      var6.setTextColor(this.mTextColor);
      var6.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            DatePickerDialogFragment.this.dismiss();
         }
      });
      var5.setTextColor(this.mTextColor);
      var5.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Iterator var3 = DatePickerDialogFragment.this.mDatePickerDialogHandlers.iterator();

            while(var3.hasNext()) {
               ((DatePickerDialogFragment.DatePickerDialogHandler)var3.next()).onDialogDateSet(DatePickerDialogFragment.this.mReference, DatePickerDialogFragment.this.mPicker.getYear(), DatePickerDialogFragment.this.mPicker.getMonthOfYear(), DatePickerDialogFragment.this.mPicker.getDayOfMonth());
            }

            FragmentActivity var4 = DatePickerDialogFragment.this.getActivity();
            Fragment var2 = DatePickerDialogFragment.this.getTargetFragment();
            if (var4 instanceof DatePickerDialogFragment.DatePickerDialogHandler) {
               ((DatePickerDialogFragment.DatePickerDialogHandler)var4).onDialogDateSet(DatePickerDialogFragment.this.mReference, DatePickerDialogFragment.this.mPicker.getYear(), DatePickerDialogFragment.this.mPicker.getMonthOfYear(), DatePickerDialogFragment.this.mPicker.getDayOfMonth());
            } else if (var2 instanceof DatePickerDialogFragment.DatePickerDialogHandler) {
               ((DatePickerDialogFragment.DatePickerDialogHandler)var2).onDialogDateSet(DatePickerDialogFragment.this.mReference, DatePickerDialogFragment.this.mPicker.getYear(), DatePickerDialogFragment.this.mPicker.getMonthOfYear(), DatePickerDialogFragment.this.mPicker.getDayOfMonth());
            }

            DatePickerDialogFragment.this.dismiss();
         }
      });
      DatePicker var7 = (DatePicker)var4.findViewById(id.date_picker);
      this.mPicker = var7;
      var7.setSetButton(var5);
      this.mPicker.setDate(this.mYear, this.mMonthOfYear, this.mDayOfMonth);
      this.mPicker.setYearOptional(this.mYearOptional);
      this.mPicker.setTheme(this.mTheme);
      this.getDialog().getWindow().setBackgroundDrawableResource(this.mDialogBackgroundResId);
      return var4;
   }

   public void onDismiss(DialogInterface var1) {
      super.onDismiss(var1);
      OnDialogDismissListener var2 = this.mDismissCallback;
      if (var2 != null) {
         var2.onDialogDismiss(var1);
      }

   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
   }

   public void setDatePickerDialogHandlers(Vector var1) {
      this.mDatePickerDialogHandlers = var1;
   }

   public void setOnDismissListener(OnDialogDismissListener var1) {
      this.mDismissCallback = var1;
   }

   public interface DatePickerDialogHandler {
      void onDialogDateSet(int var1, int var2, int var3, int var4);
   }
}
