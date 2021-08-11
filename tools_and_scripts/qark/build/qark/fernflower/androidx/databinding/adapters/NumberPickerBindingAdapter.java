package androidx.databinding.adapters;

import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import androidx.databinding.InverseBindingListener;

public class NumberPickerBindingAdapter {
   public static void setListeners(NumberPicker var0, final OnValueChangeListener var1, final InverseBindingListener var2) {
      if (var2 == null) {
         var0.setOnValueChangedListener(var1);
      } else {
         var0.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker var1x, int var2x, int var3) {
               OnValueChangeListener var4 = var1;
               if (var4 != null) {
                  var4.onValueChange(var1x, var2x, var3);
               }

               var2.onChange();
            }
         });
      }
   }

   public static void setValue(NumberPicker var0, int var1) {
      if (var0.getValue() != var1) {
         var0.setValue(var1);
      }

   }
}
