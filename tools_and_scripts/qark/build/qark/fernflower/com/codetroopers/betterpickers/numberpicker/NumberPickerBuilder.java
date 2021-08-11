package com.codetroopers.betterpickers.numberpicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import java.math.BigDecimal;
import java.util.Vector;

public class NumberPickerBuilder {
   private Double currentDecimalValue;
   private Integer currentNumberValue;
   private Integer currentSignValue;
   private Integer decimalVisibility;
   private String labelText;
   private Vector mNumberPickerDialogHandlersV2 = new Vector();
   private OnDialogDismissListener mOnDismissListener;
   private int mReference;
   private FragmentManager manager;
   private BigDecimal maxNumber;
   private BigDecimal minNumber;
   private Integer plusMinusVisibility;
   private Integer styleResId;
   private Fragment targetFragment;

   public NumberPickerBuilder addNumberPickerDialogHandler(NumberPickerDialogFragment.NumberPickerDialogHandlerV2 var1) {
      this.mNumberPickerDialogHandlersV2.add(var1);
      return this;
   }

   public NumberPickerBuilder removeNumberPickerDialogHandler(NumberPickerDialogFragment.NumberPickerDialogHandlerV2 var1) {
      this.mNumberPickerDialogHandlersV2.remove(var1);
      return this;
   }

   public NumberPickerBuilder setCurrentNumber(Integer var1) {
      if (var1 != null) {
         if (var1 >= 0) {
            this.currentSignValue = 0;
         } else {
            this.currentSignValue = 1;
            var1 = var1 * -1;
         }

         this.currentNumberValue = var1;
         this.currentDecimalValue = null;
      }

      return this;
   }

   public NumberPickerBuilder setCurrentNumber(BigDecimal var1) {
      if (var1 != null) {
         if (var1.signum() >= 0) {
            this.currentSignValue = 0;
         } else {
            this.currentSignValue = 1;
            var1 = var1.abs();
         }

         BigDecimal[] var2 = var1.divideAndRemainder(BigDecimal.ONE);
         this.currentNumberValue = var2[0].intValue();
         this.currentDecimalValue = var2[1].doubleValue();
      }

      return this;
   }

   public NumberPickerBuilder setDecimalVisibility(int var1) {
      this.decimalVisibility = var1;
      return this;
   }

   public NumberPickerBuilder setFragmentManager(FragmentManager var1) {
      this.manager = var1;
      return this;
   }

   public NumberPickerBuilder setLabelText(String var1) {
      this.labelText = var1;
      return this;
   }

   public NumberPickerBuilder setMaxNumber(BigDecimal var1) {
      this.maxNumber = var1;
      return this;
   }

   public NumberPickerBuilder setMinNumber(BigDecimal var1) {
      this.minNumber = var1;
      return this;
   }

   public NumberPickerBuilder setOnDismissListener(OnDialogDismissListener var1) {
      this.mOnDismissListener = var1;
      return this;
   }

   public NumberPickerBuilder setPlusMinusVisibility(int var1) {
      this.plusMinusVisibility = var1;
      return this;
   }

   public NumberPickerBuilder setReference(int var1) {
      this.mReference = var1;
      return this;
   }

   public NumberPickerBuilder setStyleResId(int var1) {
      this.styleResId = var1;
      return this;
   }

   public NumberPickerBuilder setTargetFragment(Fragment var1) {
      this.targetFragment = var1;
      return this;
   }

   public void show() {
      FragmentManager var1 = this.manager;
      if (var1 != null && this.styleResId != null) {
         FragmentTransaction var2 = var1.beginTransaction();
         Fragment var3 = this.manager.findFragmentByTag("number_dialog");
         FragmentTransaction var4 = var2;
         if (var3 != null) {
            var2.remove(var3).commit();
            var4 = this.manager.beginTransaction();
         }

         var4.addToBackStack((String)null);
         NumberPickerDialogFragment var5 = NumberPickerDialogFragment.newInstance(this.mReference, this.styleResId, this.minNumber, this.maxNumber, this.plusMinusVisibility, this.decimalVisibility, this.labelText, this.currentNumberValue, this.currentDecimalValue, this.currentSignValue);
         var3 = this.targetFragment;
         if (var3 != null) {
            var5.setTargetFragment(var3, 0);
         }

         var5.setNumberPickerDialogHandlersV2(this.mNumberPickerDialogHandlersV2);
         var5.setOnDismissListener(this.mOnDismissListener);
         var5.show(var4, "number_dialog");
      } else {
         Log.e("NumberPickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
      }
   }
}
