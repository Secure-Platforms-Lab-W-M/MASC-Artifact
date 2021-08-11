package androidx.databinding.adapters;

import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import java.util.List;

public class AbsSpinnerBindingAdapter {
   public static void setEntries(AbsSpinner var0, List var1) {
      if (var1 != null) {
         SpinnerAdapter var2 = var0.getAdapter();
         if (var2 instanceof ObservableListAdapter) {
            ((ObservableListAdapter)var2).setList(var1);
         } else {
            var0.setAdapter(new ObservableListAdapter(var0.getContext(), var1, 17367048, 17367049, 0));
         }

      } else {
         var0.setAdapter((SpinnerAdapter)null);
      }
   }

   public static void setEntries(AbsSpinner var0, CharSequence[] var1) {
      if (var1 == null) {
         var0.setAdapter((SpinnerAdapter)null);
      } else {
         SpinnerAdapter var5 = var0.getAdapter();
         boolean var3 = true;
         boolean var2 = var3;
         if (var5 != null) {
            var2 = var3;
            if (var5.getCount() == var1.length) {
               boolean var4 = false;
               int var7 = 0;

               while(true) {
                  var2 = var4;
                  if (var7 >= var1.length) {
                     break;
                  }

                  if (!var1[var7].equals(var5.getItem(var7))) {
                     var2 = true;
                     break;
                  }

                  ++var7;
               }
            }
         }

         if (var2) {
            ArrayAdapter var6 = new ArrayAdapter(var0.getContext(), 17367048, var1);
            var6.setDropDownViewResource(17367049);
            var0.setAdapter(var6);
         }

      }
   }
}
