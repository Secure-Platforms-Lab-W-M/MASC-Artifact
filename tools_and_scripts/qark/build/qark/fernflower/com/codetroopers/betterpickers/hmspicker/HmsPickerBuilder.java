package com.codetroopers.betterpickers.hmspicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import java.util.Vector;

public class HmsPickerBuilder {
   private Vector mHmsPickerDialogHandlerV2s = new Vector();
   private int mHours;
   private int mMinutes;
   private OnDialogDismissListener mOnDismissListener;
   private int mReference;
   private int mSeconds;
   private FragmentManager manager;
   private Integer plusMinusVisibility;
   private Integer styleResId;
   private Fragment targetFragment;

   private static int bounded(int var0, int var1, int var2) {
      return Math.min(Math.max(var0, var1), var2);
   }

   public HmsPickerBuilder addHmsPickerDialogHandler(HmsPickerDialogFragment.HmsPickerDialogHandlerV2 var1) {
      this.mHmsPickerDialogHandlerV2s.add(var1);
      return this;
   }

   public HmsPickerBuilder removeHmsPickerDialogHandler(HmsPickerDialogFragment.HmsPickerDialogHandlerV2 var1) {
      this.mHmsPickerDialogHandlerV2s.remove(var1);
      return this;
   }

   public HmsPickerBuilder setFragmentManager(FragmentManager var1) {
      this.manager = var1;
      return this;
   }

   public HmsPickerBuilder setOnDismissListener(OnDialogDismissListener var1) {
      this.mOnDismissListener = var1;
      return this;
   }

   public HmsPickerBuilder setPlusMinusVisibility(int var1) {
      this.plusMinusVisibility = var1;
      return this;
   }

   public HmsPickerBuilder setReference(int var1) {
      this.mReference = var1;
      return this;
   }

   public HmsPickerBuilder setStyleResId(int var1) {
      this.styleResId = var1;
      return this;
   }

   public HmsPickerBuilder setTargetFragment(Fragment var1) {
      this.targetFragment = var1;
      return this;
   }

   public HmsPickerBuilder setTime(int var1, int var2, int var3) {
      this.mHours = bounded(var1, 0, 99);
      this.mMinutes = bounded(var2, 0, 99);
      this.mSeconds = bounded(var3, 0, 99);
      return this;
   }

   public HmsPickerBuilder setTimeInMilliseconds(long var1) {
      return this.setTimeInSeconds((int)(var1 / 1000L));
   }

   public HmsPickerBuilder setTimeInSeconds(int var1) {
      int var2 = var1 / 3600;
      var1 %= 3600;
      return this.setTime(var2, var1 / 60, var1 % 60);
   }

   public void show() {
      FragmentManager var4 = this.manager;
      if (var4 != null && this.styleResId != null) {
         FragmentTransaction var5 = var4.beginTransaction();
         Fragment var6 = this.manager.findFragmentByTag("hms_dialog");
         FragmentTransaction var8 = var5;
         if (var6 != null) {
            var5.remove(var6).commit();
            var8 = this.manager.beginTransaction();
         }

         var8.addToBackStack((String)null);
         HmsPickerDialogFragment var7 = HmsPickerDialogFragment.newInstance(this.mReference, this.styleResId, this.plusMinusVisibility);
         var6 = this.targetFragment;
         if (var6 != null) {
            var7.setTargetFragment(var6, 0);
         }

         var7.setHmsPickerDialogHandlersV2(this.mHmsPickerDialogHandlerV2s);
         int var1 = this.mHours;
         int var2 = this.mMinutes;
         int var3 = this.mSeconds;
         if ((var1 | var2 | var3) != 0) {
            var7.setTime(var1, var2, var3);
         }

         var7.setOnDismissListener(this.mOnDismissListener);
         var7.show(var8, "hms_dialog");
      } else {
         Log.e("HmsPickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
      }
   }
}
