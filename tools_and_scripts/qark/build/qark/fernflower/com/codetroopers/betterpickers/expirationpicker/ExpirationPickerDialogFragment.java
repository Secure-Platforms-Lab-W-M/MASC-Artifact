package com.codetroopers.betterpickers.expirationpicker;

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

public class ExpirationPickerDialogFragment extends DialogFragment {
   private static final String MINIMUM_YEAR_KEY = "ExpirationPickerDialogFragment_MinimumYearKey";
   private static final String MONTH_KEY = "ExpirationPickerDialogFragment_MonthKey";
   private static final String REFERENCE_KEY = "ExpirationPickerDialogFragment_ReferenceKey";
   private static final String THEME_RES_ID_KEY = "ExpirationPickerDialogFragment_ThemeResIdKey";
   private static final String YEAR_KEY = "ExpirationPickerDialogFragment_YearKey";
   private int mDialogBackgroundResId;
   private OnDialogDismissListener mDismissCallback;
   private Vector mExpirationPickerDialogHandlers = new Vector();
   private int mMinimumYear = 0;
   private int mMonthOfYear = -1;
   private ExpirationPicker mPicker;
   private int mReference = -1;
   private ColorStateList mTextColor;
   private int mTheme = -1;
   private int mYear = 0;

   public static ExpirationPickerDialogFragment newInstance(int var0, int var1, Integer var2, Integer var3, Integer var4) {
      ExpirationPickerDialogFragment var5 = new ExpirationPickerDialogFragment();
      Bundle var6 = new Bundle();
      var6.putInt("ExpirationPickerDialogFragment_ReferenceKey", var0);
      var6.putInt("ExpirationPickerDialogFragment_ThemeResIdKey", var1);
      if (var2 != null) {
         var6.putInt("ExpirationPickerDialogFragment_MonthKey", var2);
      }

      if (var3 != null) {
         var6.putInt("ExpirationPickerDialogFragment_YearKey", var3);
      }

      if (var4 != null) {
         var6.putInt("ExpirationPickerDialogFragment_MinimumYearKey", var4);
      }

      var5.setArguments(var6);
      return var5;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      var1 = this.getArguments();
      if (var1 != null && var1.containsKey("ExpirationPickerDialogFragment_ReferenceKey")) {
         this.mReference = var1.getInt("ExpirationPickerDialogFragment_ReferenceKey");
      }

      if (var1 != null && var1.containsKey("ExpirationPickerDialogFragment_ThemeResIdKey")) {
         this.mTheme = var1.getInt("ExpirationPickerDialogFragment_ThemeResIdKey");
      }

      if (var1 != null && var1.containsKey("ExpirationPickerDialogFragment_MonthKey")) {
         this.mMonthOfYear = var1.getInt("ExpirationPickerDialogFragment_MonthKey");
      }

      if (var1 != null && var1.containsKey("ExpirationPickerDialogFragment_YearKey")) {
         this.mYear = var1.getInt("ExpirationPickerDialogFragment_YearKey");
      }

      if (var1 != null && var1.containsKey("ExpirationPickerDialogFragment_MinimumYearKey")) {
         this.mMinimumYear = var1.getInt("ExpirationPickerDialogFragment_MinimumYearKey");
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
      View var5 = var1.inflate(layout.expiration_picker_dialog, var2, false);
      Button var6 = (Button)var5.findViewById(id.done_button);
      Button var7 = (Button)var5.findViewById(id.cancel_button);
      var7.setTextColor(this.mTextColor);
      var7.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            ExpirationPickerDialogFragment.this.dismiss();
         }
      });
      var6.setTextColor(this.mTextColor);
      var6.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Iterator var3 = ExpirationPickerDialogFragment.this.mExpirationPickerDialogHandlers.iterator();

            while(var3.hasNext()) {
               ((ExpirationPickerDialogFragment.ExpirationPickerDialogHandler)var3.next()).onDialogExpirationSet(ExpirationPickerDialogFragment.this.mReference, ExpirationPickerDialogFragment.this.mPicker.getYear(), ExpirationPickerDialogFragment.this.mPicker.getMonthOfYear());
            }

            FragmentActivity var4 = ExpirationPickerDialogFragment.this.getActivity();
            Fragment var2 = ExpirationPickerDialogFragment.this.getTargetFragment();
            if (var4 instanceof ExpirationPickerDialogFragment.ExpirationPickerDialogHandler) {
               ((ExpirationPickerDialogFragment.ExpirationPickerDialogHandler)var4).onDialogExpirationSet(ExpirationPickerDialogFragment.this.mReference, ExpirationPickerDialogFragment.this.mPicker.getYear(), ExpirationPickerDialogFragment.this.mPicker.getMonthOfYear());
            } else if (var2 instanceof ExpirationPickerDialogFragment.ExpirationPickerDialogHandler) {
               ((ExpirationPickerDialogFragment.ExpirationPickerDialogHandler)var2).onDialogExpirationSet(ExpirationPickerDialogFragment.this.mReference, ExpirationPickerDialogFragment.this.mPicker.getYear(), ExpirationPickerDialogFragment.this.mPicker.getMonthOfYear());
            }

            ExpirationPickerDialogFragment.this.dismiss();
         }
      });
      ExpirationPicker var8 = (ExpirationPicker)var5.findViewById(id.expiration_picker);
      this.mPicker = var8;
      var8.setSetButton(var6);
      this.mPicker.setTheme(this.mTheme);
      int var4 = this.mMinimumYear;
      if (var4 != 0) {
         this.mPicker.setMinYear(var4);
      }

      if (this.mMonthOfYear != -1 || this.mYear != 0) {
         this.mPicker.setExpiration(this.mYear, this.mMonthOfYear);
      }

      this.getDialog().getWindow().setBackgroundDrawableResource(this.mDialogBackgroundResId);
      return var5;
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

   public void setExpirationPickerDialogHandlers(Vector var1) {
      this.mExpirationPickerDialogHandlers = var1;
   }

   public void setOnDismissListener(OnDialogDismissListener var1) {
      this.mDismissCallback = var1;
   }

   public interface ExpirationPickerDialogHandler {
      void onDialogExpirationSet(int var1, int var2, int var3);
   }
}
