package com.codetroopers.betterpickers.expirationpicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import java.util.Vector;

public class ExpirationPickerBuilder {
   private Vector mExpirationPickerDialogHandlers = new Vector();
   private OnDialogDismissListener mOnDismissListener;
   private int mReference = -1;
   private FragmentManager manager;
   private Integer minimumYear;
   private Integer monthOfYear;
   private Integer styleResId;
   private Fragment targetFragment;
   private Integer year;

   public ExpirationPickerBuilder addExpirationPickerDialogHandler(ExpirationPickerDialogFragment.ExpirationPickerDialogHandler var1) {
      this.mExpirationPickerDialogHandlers.add(var1);
      return this;
   }

   public ExpirationPickerBuilder removeExpirationPickerDialogHandler(ExpirationPickerDialogFragment.ExpirationPickerDialogHandler var1) {
      this.mExpirationPickerDialogHandlers.remove(var1);
      return this;
   }

   public ExpirationPickerBuilder setFragmentManager(FragmentManager var1) {
      this.manager = var1;
      return this;
   }

   public ExpirationPickerBuilder setMinYear(int var1) {
      this.minimumYear = var1;
      return this;
   }

   public ExpirationPickerBuilder setMonthOfYear(int var1) {
      this.monthOfYear = var1;
      return this;
   }

   public ExpirationPickerBuilder setOnDismissListener(OnDialogDismissListener var1) {
      this.mOnDismissListener = var1;
      return this;
   }

   public ExpirationPickerBuilder setReference(int var1) {
      this.mReference = var1;
      return this;
   }

   public ExpirationPickerBuilder setStyleResId(int var1) {
      this.styleResId = var1;
      return this;
   }

   public ExpirationPickerBuilder setTargetFragment(Fragment var1) {
      this.targetFragment = var1;
      return this;
   }

   public ExpirationPickerBuilder setYear(int var1) {
      this.year = var1;
      return this;
   }

   public void show() {
      FragmentManager var1 = this.manager;
      if (var1 != null && this.styleResId != null) {
         FragmentTransaction var2 = var1.beginTransaction();
         Fragment var3 = this.manager.findFragmentByTag("expiration_dialog");
         FragmentTransaction var4 = var2;
         if (var3 != null) {
            var2.remove(var3).commit();
            var4 = this.manager.beginTransaction();
         }

         var4.addToBackStack((String)null);
         ExpirationPickerDialogFragment var5 = ExpirationPickerDialogFragment.newInstance(this.mReference, this.styleResId, this.monthOfYear, this.year, this.minimumYear);
         var3 = this.targetFragment;
         if (var3 != null) {
            var5.setTargetFragment(var3, 0);
         }

         var5.setExpirationPickerDialogHandlers(this.mExpirationPickerDialogHandlers);
         var5.setOnDismissListener(this.mOnDismissListener);
         var5.show(var4, "expiration_dialog");
      } else {
         Log.e("ExpirationPickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
      }
   }
}
