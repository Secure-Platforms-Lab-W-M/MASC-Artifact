package androidx.databinding.adapters;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import androidx.databinding.InverseBindingListener;

public class CompoundButtonBindingAdapter {
   public static void setChecked(CompoundButton var0, boolean var1) {
      if (var0.isChecked() != var1) {
         var0.setChecked(var1);
      }

   }

   public static void setListeners(CompoundButton var0, final OnCheckedChangeListener var1, final InverseBindingListener var2) {
      if (var2 == null) {
         var0.setOnCheckedChangeListener(var1);
      } else {
         var0.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton var1x, boolean var2x) {
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
