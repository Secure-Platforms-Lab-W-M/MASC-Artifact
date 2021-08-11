package androidx.databinding.adapters;

import android.os.Build.VERSION;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import androidx.databinding.InverseBindingListener;

public class TimePickerBindingAdapter {
   public static int getHour(TimePicker var0) {
      if (VERSION.SDK_INT >= 23) {
         return var0.getHour();
      } else {
         Integer var1 = var0.getCurrentHour();
         return var1 == null ? 0 : var1;
      }
   }

   public static int getMinute(TimePicker var0) {
      if (VERSION.SDK_INT >= 23) {
         return var0.getMinute();
      } else {
         Integer var1 = var0.getCurrentMinute();
         return var1 == null ? 0 : var1;
      }
   }

   public static void setHour(TimePicker var0, int var1) {
      if (VERSION.SDK_INT >= 23) {
         if (var0.getHour() != var1) {
            var0.setHour(var1);
            return;
         }
      } else if (var0.getCurrentHour() != var1) {
         var0.setCurrentHour(var1);
      }

   }

   public static void setListeners(TimePicker var0, final OnTimeChangedListener var1, final InverseBindingListener var2, final InverseBindingListener var3) {
      if (var2 == null && var3 == null) {
         var0.setOnTimeChangedListener(var1);
      } else {
         var0.setOnTimeChangedListener(new OnTimeChangedListener() {
            public void onTimeChanged(TimePicker var1x, int var2x, int var3x) {
               OnTimeChangedListener var4 = var1;
               if (var4 != null) {
                  var4.onTimeChanged(var1x, var2x, var3x);
               }

               InverseBindingListener var5 = var2;
               if (var5 != null) {
                  var5.onChange();
               }

               var5 = var3;
               if (var5 != null) {
                  var5.onChange();
               }

            }
         });
      }
   }

   public static void setMinute(TimePicker var0, int var1) {
      if (VERSION.SDK_INT >= 23) {
         if (var0.getMinute() != var1) {
            var0.setMinute(var1);
            return;
         }
      } else if (var0.getCurrentMinute() != var1) {
         var0.setCurrentHour(var1);
      }

   }
}
