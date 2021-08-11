package androidx.databinding.adapters;

import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.library.baseAdapters.R.id;

public class DatePickerBindingAdapter {
   public static void setListeners(DatePicker var0, int var1, int var2, int var3, OnDateChangedListener var4, InverseBindingListener var5, InverseBindingListener var6, InverseBindingListener var7) {
      int var8 = var1;
      if (var1 == 0) {
         var8 = var0.getYear();
      }

      var1 = var3;
      if (var3 == 0) {
         var1 = var0.getDayOfMonth();
      }

      if (var5 == null && var6 == null && var7 == null) {
         var0.init(var8, var2, var1, var4);
      } else {
         DatePickerBindingAdapter.DateChangedListener var10 = (DatePickerBindingAdapter.DateChangedListener)ListenerUtil.getListener(var0, id.onDateChanged);
         DatePickerBindingAdapter.DateChangedListener var9 = var10;
         if (var10 == null) {
            var9 = new DatePickerBindingAdapter.DateChangedListener();
            ListenerUtil.trackListener(var0, var9, id.onDateChanged);
         }

         var9.setListeners(var4, var5, var6, var7);
         var0.init(var8, var2, var1, var9);
      }
   }

   private static class DateChangedListener implements OnDateChangedListener {
      InverseBindingListener mDayChanged;
      OnDateChangedListener mListener;
      InverseBindingListener mMonthChanged;
      InverseBindingListener mYearChanged;

      private DateChangedListener() {
      }

      // $FF: synthetic method
      DateChangedListener(Object var1) {
         this();
      }

      public void onDateChanged(DatePicker var1, int var2, int var3, int var4) {
         OnDateChangedListener var5 = this.mListener;
         if (var5 != null) {
            var5.onDateChanged(var1, var2, var3, var4);
         }

         InverseBindingListener var6 = this.mYearChanged;
         if (var6 != null) {
            var6.onChange();
         }

         var6 = this.mMonthChanged;
         if (var6 != null) {
            var6.onChange();
         }

         var6 = this.mDayChanged;
         if (var6 != null) {
            var6.onChange();
         }

      }

      public void setListeners(OnDateChangedListener var1, InverseBindingListener var2, InverseBindingListener var3, InverseBindingListener var4) {
         this.mListener = var1;
         this.mYearChanged = var2;
         this.mMonthChanged = var3;
         this.mDayChanged = var4;
      }
   }
}
