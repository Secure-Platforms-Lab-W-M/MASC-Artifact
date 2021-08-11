package androidx.databinding.adapters;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import androidx.databinding.InverseBindingListener;

public class RadioGroupBindingAdapter {
   public static void setCheckedButton(RadioGroup var0, int var1) {
      if (var1 != var0.getCheckedRadioButtonId()) {
         var0.check(var1);
      }

   }

   public static void setListeners(RadioGroup var0, final OnCheckedChangeListener var1, final InverseBindingListener var2) {
      if (var2 == null) {
         var0.setOnCheckedChangeListener(var1);
      } else {
         var0.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup var1x, int var2x) {
               OnCheckedChangeListener var3 = var1;
               if (var3 != null) {
                  var3.onCheckedChanged(var1x, var2x);
               }

               var2.onChange();
            }
         });
      }
   }
}
