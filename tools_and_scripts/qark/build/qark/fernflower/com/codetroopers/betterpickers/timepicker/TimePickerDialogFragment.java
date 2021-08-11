package com.codetroopers.betterpickers.timepicker;

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

public class TimePickerDialogFragment extends DialogFragment {
   private static final String REFERENCE_KEY = "TimePickerDialogFragment_ReferenceKey";
   private static final String THEME_RES_ID_KEY = "TimePickerDialogFragment_ThemeResIdKey";
   private int mDialogBackgroundResId;
   private OnDialogDismissListener mDismissCallback;
   private TimePicker mPicker;
   private int mReference = -1;
   private ColorStateList mTextColor;
   private int mTheme = -1;
   private Vector mTimePickerDialogHandlers = new Vector();

   public static TimePickerDialogFragment newInstance(int var0, int var1) {
      TimePickerDialogFragment var2 = new TimePickerDialogFragment();
      Bundle var3 = new Bundle();
      var3.putInt("TimePickerDialogFragment_ReferenceKey", var0);
      var3.putInt("TimePickerDialogFragment_ThemeResIdKey", var1);
      var2.setArguments(var3);
      return var2;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      var1 = this.getArguments();
      if (var1 != null && var1.containsKey("TimePickerDialogFragment_ReferenceKey")) {
         this.mReference = var1.getInt("TimePickerDialogFragment_ReferenceKey");
      }

      if (var1 != null && var1.containsKey("TimePickerDialogFragment_ThemeResIdKey")) {
         this.mTheme = var1.getInt("TimePickerDialogFragment_ThemeResIdKey");
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
      View var4 = var1.inflate(layout.time_picker_dialog, var2, false);
      Button var5 = (Button)var4.findViewById(id.done_button);
      Button var6 = (Button)var4.findViewById(id.cancel_button);
      var6.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            TimePickerDialogFragment.this.dismiss();
         }
      });
      var6.setTextColor(this.mTextColor);
      var5.setTextColor(this.mTextColor);
      var5.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Iterator var3 = TimePickerDialogFragment.this.mTimePickerDialogHandlers.iterator();

            while(var3.hasNext()) {
               ((TimePickerDialogFragment.TimePickerDialogHandler)var3.next()).onDialogTimeSet(TimePickerDialogFragment.this.mReference, TimePickerDialogFragment.this.mPicker.getHours(), TimePickerDialogFragment.this.mPicker.getMinutes());
            }

            FragmentActivity var4 = TimePickerDialogFragment.this.getActivity();
            Fragment var2 = TimePickerDialogFragment.this.getTargetFragment();
            if (var4 instanceof TimePickerDialogFragment.TimePickerDialogHandler) {
               ((TimePickerDialogFragment.TimePickerDialogHandler)var4).onDialogTimeSet(TimePickerDialogFragment.this.mReference, TimePickerDialogFragment.this.mPicker.getHours(), TimePickerDialogFragment.this.mPicker.getMinutes());
            } else if (var2 instanceof TimePickerDialogFragment.TimePickerDialogHandler) {
               ((TimePickerDialogFragment.TimePickerDialogHandler)var2).onDialogTimeSet(TimePickerDialogFragment.this.mReference, TimePickerDialogFragment.this.mPicker.getHours(), TimePickerDialogFragment.this.mPicker.getMinutes());
            }

            TimePickerDialogFragment.this.dismiss();
         }
      });
      TimePicker var7 = (TimePicker)var4.findViewById(id.time_picker);
      this.mPicker = var7;
      var7.setSetButton(var5);
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

   public void setOnDismissListener(OnDialogDismissListener var1) {
      this.mDismissCallback = var1;
   }

   public void setTimePickerDialogHandlers(Vector var1) {
      this.mTimePickerDialogHandlers = var1;
   }

   public interface TimePickerDialogHandler {
      void onDialogTimeSet(int var1, int var2, int var3);
   }
}
