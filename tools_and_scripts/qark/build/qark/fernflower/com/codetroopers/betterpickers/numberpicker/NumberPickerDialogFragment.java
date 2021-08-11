package com.codetroopers.betterpickers.numberpicker;

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
import com.codetroopers.betterpickers.R.string;
import com.codetroopers.betterpickers.R.styleable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Vector;

public class NumberPickerDialogFragment extends DialogFragment {
   private static final String CURRENT_DECIMAL_KEY = "NumberPickerDialogFragment_CurrentDecimalKey";
   private static final String CURRENT_NUMBER_KEY = "NumberPickerDialogFragment_CurrentNumberKey";
   private static final String CURRENT_SIGN_KEY = "NumberPickerDialogFragment_CurrentSignKey";
   private static final String DECIMAL_VISIBILITY_KEY = "NumberPickerDialogFragment_DecimalVisibilityKey";
   private static final String LABEL_TEXT_KEY = "NumberPickerDialogFragment_LabelTextKey";
   private static final String MAX_NUMBER_KEY = "NumberPickerDialogFragment_MaxNumberKey";
   private static final String MIN_NUMBER_KEY = "NumberPickerDialogFragment_MinNumberKey";
   private static final String PLUS_MINUS_VISIBILITY_KEY = "NumberPickerDialogFragment_PlusMinusVisibilityKey";
   private static final String REFERENCE_KEY = "NumberPickerDialogFragment_ReferenceKey";
   private static final String THEME_RES_ID_KEY = "NumberPickerDialogFragment_ThemeResIdKey";
   private Double mCurrentDecimal = null;
   private Integer mCurrentNumber = null;
   private Integer mCurrentSign = null;
   private int mDecimalVisibility = 0;
   private int mDialogBackgroundResId;
   private OnDialogDismissListener mDismissCallback;
   private String mLabelText = "";
   private BigDecimal mMaxNumber = null;
   private BigDecimal mMinNumber = null;
   private Vector mNumberPickerDialogHandlersV2 = new Vector();
   private NumberPicker mPicker;
   private int mPlusMinusVisibility = 0;
   private int mReference = -1;
   private ColorStateList mTextColor;
   private int mTheme = -1;

   private boolean isBigger(BigDecimal var1) {
      return var1.compareTo(this.mMaxNumber) > 0;
   }

   private boolean isSmaller(BigDecimal var1) {
      return var1.compareTo(this.mMinNumber) < 0;
   }

   public static NumberPickerDialogFragment newInstance(int var0, int var1, BigDecimal var2, BigDecimal var3, Integer var4, Integer var5, String var6, Integer var7, Double var8, Integer var9) {
      NumberPickerDialogFragment var10 = new NumberPickerDialogFragment();
      Bundle var11 = new Bundle();
      var11.putInt("NumberPickerDialogFragment_ReferenceKey", var0);
      var11.putInt("NumberPickerDialogFragment_ThemeResIdKey", var1);
      if (var2 != null) {
         var11.putSerializable("NumberPickerDialogFragment_MinNumberKey", var2);
      }

      if (var3 != null) {
         var11.putSerializable("NumberPickerDialogFragment_MaxNumberKey", var3);
      }

      if (var4 != null) {
         var11.putInt("NumberPickerDialogFragment_PlusMinusVisibilityKey", var4);
      }

      if (var5 != null) {
         var11.putInt("NumberPickerDialogFragment_DecimalVisibilityKey", var5);
      }

      if (var6 != null) {
         var11.putString("NumberPickerDialogFragment_LabelTextKey", var6);
      }

      if (var7 != null) {
         var11.putInt("NumberPickerDialogFragment_CurrentNumberKey", var7);
      }

      if (var8 != null) {
         var11.putDouble("NumberPickerDialogFragment_CurrentDecimalKey", var8);
      }

      if (var9 != null) {
         var11.putInt("NumberPickerDialogFragment_CurrentSignKey", var9);
      }

      var10.setArguments(var11);
      return var10;
   }

   public void onCancel(DialogInterface var1) {
      super.onCancel(var1);
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      var1 = this.getArguments();
      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_ReferenceKey")) {
         this.mReference = var1.getInt("NumberPickerDialogFragment_ReferenceKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_ThemeResIdKey")) {
         this.mTheme = var1.getInt("NumberPickerDialogFragment_ThemeResIdKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_PlusMinusVisibilityKey")) {
         this.mPlusMinusVisibility = var1.getInt("NumberPickerDialogFragment_PlusMinusVisibilityKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_DecimalVisibilityKey")) {
         this.mDecimalVisibility = var1.getInt("NumberPickerDialogFragment_DecimalVisibilityKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_MinNumberKey")) {
         this.mMinNumber = (BigDecimal)var1.getSerializable("NumberPickerDialogFragment_MinNumberKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_MaxNumberKey")) {
         this.mMaxNumber = (BigDecimal)var1.getSerializable("NumberPickerDialogFragment_MaxNumberKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_LabelTextKey")) {
         this.mLabelText = var1.getString("NumberPickerDialogFragment_LabelTextKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_CurrentNumberKey")) {
         this.mCurrentNumber = var1.getInt("NumberPickerDialogFragment_CurrentNumberKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_CurrentDecimalKey")) {
         this.mCurrentDecimal = var1.getDouble("NumberPickerDialogFragment_CurrentDecimalKey");
      }

      if (var1 != null && var1.containsKey("NumberPickerDialogFragment_CurrentSignKey")) {
         this.mCurrentSign = var1.getInt("NumberPickerDialogFragment_CurrentSignKey");
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
      View var4 = var1.inflate(layout.number_picker_dialog, var2, false);
      Button var5 = (Button)var4.findViewById(id.done_button);
      Button var7 = (Button)var4.findViewById(id.cancel_button);
      var7.setTextColor(this.mTextColor);
      var7.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            NumberPickerDialogFragment.this.dismiss();
         }
      });
      var5.setTextColor(this.mTextColor);
      var5.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            BigDecimal var4 = NumberPickerDialogFragment.this.mPicker.getEnteredNumber();
            String var5;
            if (NumberPickerDialogFragment.this.mMinNumber == null || NumberPickerDialogFragment.this.mMaxNumber == null || !NumberPickerDialogFragment.this.isSmaller(var4) && !NumberPickerDialogFragment.this.isBigger(var4)) {
               if (NumberPickerDialogFragment.this.mMinNumber != null && NumberPickerDialogFragment.this.isSmaller(var4)) {
                  var5 = NumberPickerDialogFragment.this.getString(string.min_error, new Object[]{NumberPickerDialogFragment.this.mMinNumber});
                  NumberPickerDialogFragment.this.mPicker.getErrorView().setText(var5);
                  NumberPickerDialogFragment.this.mPicker.getErrorView().show();
               } else if (NumberPickerDialogFragment.this.mMaxNumber != null && NumberPickerDialogFragment.this.isBigger(var4)) {
                  var5 = NumberPickerDialogFragment.this.getString(string.max_error, new Object[]{NumberPickerDialogFragment.this.mMaxNumber});
                  NumberPickerDialogFragment.this.mPicker.getErrorView().setText(var5);
                  NumberPickerDialogFragment.this.mPicker.getErrorView().show();
               } else {
                  Iterator var2 = NumberPickerDialogFragment.this.mNumberPickerDialogHandlersV2.iterator();

                  while(var2.hasNext()) {
                     ((NumberPickerDialogFragment.NumberPickerDialogHandlerV2)var2.next()).onDialogNumberSet(NumberPickerDialogFragment.this.mReference, NumberPickerDialogFragment.this.mPicker.getNumber(), NumberPickerDialogFragment.this.mPicker.getDecimal(), NumberPickerDialogFragment.this.mPicker.getIsNegative(), var4);
                  }

                  FragmentActivity var6 = NumberPickerDialogFragment.this.getActivity();
                  Fragment var3 = NumberPickerDialogFragment.this.getTargetFragment();
                  if (var6 instanceof NumberPickerDialogFragment.NumberPickerDialogHandlerV2) {
                     ((NumberPickerDialogFragment.NumberPickerDialogHandlerV2)var6).onDialogNumberSet(NumberPickerDialogFragment.this.mReference, NumberPickerDialogFragment.this.mPicker.getNumber(), NumberPickerDialogFragment.this.mPicker.getDecimal(), NumberPickerDialogFragment.this.mPicker.getIsNegative(), var4);
                  } else if (var3 instanceof NumberPickerDialogFragment.NumberPickerDialogHandlerV2) {
                     ((NumberPickerDialogFragment.NumberPickerDialogHandlerV2)var3).onDialogNumberSet(NumberPickerDialogFragment.this.mReference, NumberPickerDialogFragment.this.mPicker.getNumber(), NumberPickerDialogFragment.this.mPicker.getDecimal(), NumberPickerDialogFragment.this.mPicker.getIsNegative(), var4);
                  }

                  NumberPickerDialogFragment.this.dismiss();
               }
            } else {
               var5 = NumberPickerDialogFragment.this.getString(string.min_max_error, new Object[]{NumberPickerDialogFragment.this.mMinNumber, NumberPickerDialogFragment.this.mMaxNumber});
               NumberPickerDialogFragment.this.mPicker.getErrorView().setText(var5);
               NumberPickerDialogFragment.this.mPicker.getErrorView().show();
            }
         }
      });
      NumberPicker var8 = (NumberPicker)var4.findViewById(id.number_picker);
      this.mPicker = var8;
      var8.setSetButton(var5);
      this.mPicker.setTheme(this.mTheme);
      this.mPicker.setDecimalVisibility(this.mDecimalVisibility);
      this.mPicker.setPlusMinusVisibility(this.mPlusMinusVisibility);
      this.mPicker.setLabelText(this.mLabelText);
      BigDecimal var6 = this.mMinNumber;
      if (var6 != null) {
         this.mPicker.setMin(var6);
      }

      var6 = this.mMaxNumber;
      if (var6 != null) {
         this.mPicker.setMax(var6);
      }

      this.mPicker.setNumber(this.mCurrentNumber, this.mCurrentDecimal, this.mCurrentSign);
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

   public void setNumberPickerDialogHandlersV2(Vector var1) {
      this.mNumberPickerDialogHandlersV2 = var1;
   }

   public void setOnDismissListener(OnDialogDismissListener var1) {
      this.mDismissCallback = var1;
   }

   public interface NumberPickerDialogHandlerV2 {
      void onDialogNumberSet(int var1, BigInteger var2, double var3, boolean var5, BigDecimal var6);
   }
}
