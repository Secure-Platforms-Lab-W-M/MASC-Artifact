package com.google.android.material.datepicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;

public final class MaterialTextInputPicker extends PickerFragment {
   private static final String CALENDAR_CONSTRAINTS_KEY = "CALENDAR_CONSTRAINTS_KEY";
   private static final String DATE_SELECTOR_KEY = "DATE_SELECTOR_KEY";
   private CalendarConstraints calendarConstraints;
   private DateSelector dateSelector;

   static MaterialTextInputPicker newInstance(DateSelector var0, CalendarConstraints var1) {
      MaterialTextInputPicker var2 = new MaterialTextInputPicker();
      Bundle var3 = new Bundle();
      var3.putParcelable("DATE_SELECTOR_KEY", var0);
      var3.putParcelable("CALENDAR_CONSTRAINTS_KEY", var1);
      var2.setArguments(var3);
      return var2;
   }

   public DateSelector getDateSelector() {
      DateSelector var1 = this.dateSelector;
      if (var1 != null) {
         return var1;
      } else {
         throw new IllegalStateException("dateSelector should not be null. Use MaterialTextInputPicker#newInstance() to create this fragment with a DateSelector, and call this method after the fragment has been created.");
      }
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (var1 == null) {
         var1 = this.getArguments();
      }

      this.dateSelector = (DateSelector)var1.getParcelable("DATE_SELECTOR_KEY");
      this.calendarConstraints = (CalendarConstraints)var1.getParcelable("CALENDAR_CONSTRAINTS_KEY");
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      return this.dateSelector.onCreateTextInputView(var1, var2, var3, this.calendarConstraints, new OnSelectionChangedListener() {
         public void onSelectionChanged(Object var1) {
            Iterator var2 = MaterialTextInputPicker.this.onSelectionChangedListeners.iterator();

            while(var2.hasNext()) {
               ((OnSelectionChangedListener)var2.next()).onSelectionChanged(var1);
            }

         }
      });
   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      var1.putParcelable("DATE_SELECTOR_KEY", this.dateSelector);
      var1.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.calendarConstraints);
   }
}
