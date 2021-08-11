package androidx.databinding.adapters;

import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import androidx.databinding.InverseBindingListener;

public class CalendarViewBindingAdapter {
   public static void setDate(CalendarView var0, long var1) {
      if (var0.getDate() != var1) {
         var0.setDate(var1);
      }

   }

   public static void setListeners(CalendarView var0, final OnDateChangeListener var1, final InverseBindingListener var2) {
      if (var2 == null) {
         var0.setOnDateChangeListener(var1);
      } else {
         var0.setOnDateChangeListener(new OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView var1x, int var2x, int var3, int var4) {
               OnDateChangeListener var5 = var1;
               if (var5 != null) {
                  var5.onSelectedDayChange(var1x, var2x, var3, var4);
               }

               var2.onChange();
            }
         });
      }
   }
}
