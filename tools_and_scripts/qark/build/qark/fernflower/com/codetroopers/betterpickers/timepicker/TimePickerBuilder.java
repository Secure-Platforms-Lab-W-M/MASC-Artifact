package com.codetroopers.betterpickers.timepicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import java.util.Vector;

public class TimePickerBuilder {
   private OnDialogDismissListener mOnDismissListener;
   private int mReference = -1;
   private Vector mTimePickerDialogHandlers = new Vector();
   private FragmentManager manager;
   private Integer styleResId;
   private Fragment targetFragment;

   public TimePickerBuilder addTimePickerDialogHandler(TimePickerDialogFragment.TimePickerDialogHandler var1) {
      this.mTimePickerDialogHandlers.add(var1);
      return this;
   }

   public TimePickerBuilder removeTimePickerDialogHandler(TimePickerDialogFragment.TimePickerDialogHandler var1) {
      this.mTimePickerDialogHandlers.remove(var1);
      return this;
   }

   public TimePickerBuilder setFragmentManager(FragmentManager var1) {
      this.manager = var1;
      return this;
   }

   public TimePickerBuilder setOnDismissListener(OnDialogDismissListener var1) {
      this.mOnDismissListener = var1;
      return this;
   }

   public TimePickerBuilder setReference(int var1) {
      this.mReference = var1;
      return this;
   }

   public TimePickerBuilder setStyleResId(int var1) {
      this.styleResId = var1;
      return this;
   }

   public TimePickerBuilder setTargetFragment(Fragment var1) {
      this.targetFragment = var1;
      return this;
   }

   public void show() {
      FragmentManager var1 = this.manager;
      if (var1 != null && this.styleResId != null) {
         FragmentTransaction var2 = var1.beginTransaction();
         Fragment var3 = this.manager.findFragmentByTag("time_dialog");
         FragmentTransaction var4 = var2;
         if (var3 != null) {
            var2.remove(var3).commit();
            var4 = this.manager.beginTransaction();
         }

         var4.addToBackStack((String)null);
         TimePickerDialogFragment var5 = TimePickerDialogFragment.newInstance(this.mReference, this.styleResId);
         var3 = this.targetFragment;
         if (var3 != null) {
            var5.setTargetFragment(var3, 0);
         }

         var5.setTimePickerDialogHandlers(this.mTimePickerDialogHandlers);
         var5.setOnDismissListener(this.mOnDismissListener);
         var5.show(var4, "time_dialog");
      } else {
         Log.e("TimePickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
      }
   }
}
