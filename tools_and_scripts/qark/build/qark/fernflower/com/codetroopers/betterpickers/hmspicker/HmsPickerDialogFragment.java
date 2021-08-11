package com.codetroopers.betterpickers.hmspicker;

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

public class HmsPickerDialogFragment extends DialogFragment {
   private static final String PLUS_MINUS_VISIBILITY_KEY = "HmsPickerDialogFragment_PlusMinusVisibilityKey";
   private static final String REFERENCE_KEY = "HmsPickerDialogFragment_ReferenceKey";
   private static final String THEME_RES_ID_KEY = "HmsPickerDialogFragment_ThemeResIdKey";
   private int mDialogBackgroundResId;
   private OnDialogDismissListener mDismissCallback;
   private Vector mHmsPickerDialogHandlerV2s = new Vector();
   private int mHours;
   private int mMinutes;
   private HmsPicker mPicker;
   private int mPlusMinusVisibility = 4;
   private int mReference = -1;
   private int mSeconds;
   private ColorStateList mTextColor;
   private int mTheme = -1;

   public static HmsPickerDialogFragment newInstance(int var0, int var1, Integer var2) {
      HmsPickerDialogFragment var3 = new HmsPickerDialogFragment();
      Bundle var4 = new Bundle();
      var4.putInt("HmsPickerDialogFragment_ReferenceKey", var0);
      var4.putInt("HmsPickerDialogFragment_ThemeResIdKey", var1);
      if (var2 != null) {
         var4.putInt("HmsPickerDialogFragment_PlusMinusVisibilityKey", var2);
      }

      var3.setArguments(var4);
      return var3;
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      var1 = this.getArguments();
      if (var1 != null && var1.containsKey("HmsPickerDialogFragment_ReferenceKey")) {
         this.mReference = var1.getInt("HmsPickerDialogFragment_ReferenceKey");
      }

      if (var1 != null && var1.containsKey("HmsPickerDialogFragment_ThemeResIdKey")) {
         this.mTheme = var1.getInt("HmsPickerDialogFragment_ThemeResIdKey");
      }

      if (var1 != null && var1.containsKey("HmsPickerDialogFragment_PlusMinusVisibilityKey")) {
         this.mPlusMinusVisibility = var1.getInt("HmsPickerDialogFragment_PlusMinusVisibilityKey");
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
      View var4 = var1.inflate(layout.hms_picker_dialog, var2, false);
      Button var5 = (Button)var4.findViewById(id.done_button);
      Button var6 = (Button)var4.findViewById(id.cancel_button);
      var6.setTextColor(this.mTextColor);
      var6.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            HmsPickerDialogFragment.this.dismiss();
         }
      });
      var5.setTextColor(this.mTextColor);
      var5.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            Iterator var3 = HmsPickerDialogFragment.this.mHmsPickerDialogHandlerV2s.iterator();

            while(var3.hasNext()) {
               ((HmsPickerDialogFragment.HmsPickerDialogHandlerV2)var3.next()).onDialogHmsSet(HmsPickerDialogFragment.this.mReference, HmsPickerDialogFragment.this.mPicker.isNegative(), HmsPickerDialogFragment.this.mPicker.getHours(), HmsPickerDialogFragment.this.mPicker.getMinutes(), HmsPickerDialogFragment.this.mPicker.getSeconds());
            }

            FragmentActivity var4 = HmsPickerDialogFragment.this.getActivity();
            Fragment var2 = HmsPickerDialogFragment.this.getTargetFragment();
            if (var4 instanceof HmsPickerDialogFragment.HmsPickerDialogHandlerV2) {
               ((HmsPickerDialogFragment.HmsPickerDialogHandlerV2)var4).onDialogHmsSet(HmsPickerDialogFragment.this.mReference, HmsPickerDialogFragment.this.mPicker.isNegative(), HmsPickerDialogFragment.this.mPicker.getHours(), HmsPickerDialogFragment.this.mPicker.getMinutes(), HmsPickerDialogFragment.this.mPicker.getSeconds());
            } else if (var2 instanceof HmsPickerDialogFragment.HmsPickerDialogHandlerV2) {
               ((HmsPickerDialogFragment.HmsPickerDialogHandlerV2)var2).onDialogHmsSet(HmsPickerDialogFragment.this.mReference, HmsPickerDialogFragment.this.mPicker.isNegative(), HmsPickerDialogFragment.this.mPicker.getHours(), HmsPickerDialogFragment.this.mPicker.getMinutes(), HmsPickerDialogFragment.this.mPicker.getSeconds());
            }

            HmsPickerDialogFragment.this.dismiss();
         }
      });
      HmsPicker var7 = (HmsPicker)var4.findViewById(id.hms_picker);
      this.mPicker = var7;
      var7.setSetButton(var5);
      this.mPicker.setTime(this.mHours, this.mMinutes, this.mSeconds);
      this.mPicker.setTheme(this.mTheme);
      this.mPicker.setPlusMinusVisibility(this.mPlusMinusVisibility);
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

   public void setHmsPickerDialogHandlersV2(Vector var1) {
      this.mHmsPickerDialogHandlerV2s = var1;
   }

   public void setOnDismissListener(OnDialogDismissListener var1) {
      this.mDismissCallback = var1;
   }

   public void setTime(int var1, int var2, int var3) {
      this.mHours = var1;
      this.mMinutes = var2;
      this.mSeconds = var3;
      HmsPicker var4 = this.mPicker;
      if (var4 != null) {
         var4.setTime(var1, var2, var3);
      }

   }

   public interface HmsPickerDialogHandlerV2 {
      void onDialogHmsSet(int var1, boolean var2, int var3, int var4, int var5);
   }
}
