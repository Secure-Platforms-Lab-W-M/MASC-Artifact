package com.google.android.material.datepicker;

import androidx.fragment.app.Fragment;
import java.util.LinkedHashSet;

abstract class PickerFragment extends Fragment {
   protected final LinkedHashSet onSelectionChangedListeners = new LinkedHashSet();

   boolean addOnSelectionChangedListener(OnSelectionChangedListener var1) {
      return this.onSelectionChangedListeners.add(var1);
   }

   void clearOnSelectionChangedListeners() {
      this.onSelectionChangedListeners.clear();
   }

   abstract DateSelector getDateSelector();

   boolean removeOnSelectionChangedListener(OnSelectionChangedListener var1) {
      return this.onSelectionChangedListeners.remove(var1);
   }
}
