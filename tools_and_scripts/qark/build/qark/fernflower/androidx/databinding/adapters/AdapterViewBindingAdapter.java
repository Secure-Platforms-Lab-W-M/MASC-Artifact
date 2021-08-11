package androidx.databinding.adapters;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import androidx.databinding.InverseBindingListener;

public class AdapterViewBindingAdapter {
   public static void setOnItemSelectedListener(AdapterView var0, AdapterViewBindingAdapter.OnItemSelected var1, AdapterViewBindingAdapter.OnNothingSelected var2, InverseBindingListener var3) {
      if (var1 == null && var2 == null && var3 == null) {
         var0.setOnItemSelectedListener((OnItemSelectedListener)null);
      } else {
         var0.setOnItemSelectedListener(new AdapterViewBindingAdapter.OnItemSelectedComponentListener(var1, var2, var3));
      }
   }

   public static void setSelectedItemPosition(AdapterView var0, int var1) {
      if (var0.getSelectedItemPosition() != var1) {
         var0.setSelection(var1);
      }

   }

   public static void setSelectedItemPosition(AdapterView var0, int var1, Adapter var2) {
      if (var2 != var0.getAdapter()) {
         var0.setAdapter(var2);
         var0.setSelection(var1);
      } else {
         if (var0.getSelectedItemPosition() != var1) {
            var0.setSelection(var1);
         }

      }
   }

   public static void setSelection(AdapterView var0, int var1) {
      setSelectedItemPosition(var0, var1);
   }

   public static void setSelection(AdapterView var0, int var1, Adapter var2) {
      setSelectedItemPosition(var0, var1, var2);
   }

   public interface OnItemSelected {
      void onItemSelected(AdapterView var1, View var2, int var3, long var4);
   }

   public static class OnItemSelectedComponentListener implements OnItemSelectedListener {
      private final InverseBindingListener mAttrChanged;
      private final AdapterViewBindingAdapter.OnNothingSelected mNothingSelected;
      private final AdapterViewBindingAdapter.OnItemSelected mSelected;

      public OnItemSelectedComponentListener(AdapterViewBindingAdapter.OnItemSelected var1, AdapterViewBindingAdapter.OnNothingSelected var2, InverseBindingListener var3) {
         this.mSelected = var1;
         this.mNothingSelected = var2;
         this.mAttrChanged = var3;
      }

      public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
         AdapterViewBindingAdapter.OnItemSelected var6 = this.mSelected;
         if (var6 != null) {
            var6.onItemSelected(var1, var2, var3, var4);
         }

         InverseBindingListener var7 = this.mAttrChanged;
         if (var7 != null) {
            var7.onChange();
         }

      }

      public void onNothingSelected(AdapterView var1) {
         AdapterViewBindingAdapter.OnNothingSelected var2 = this.mNothingSelected;
         if (var2 != null) {
            var2.onNothingSelected(var1);
         }

         InverseBindingListener var3 = this.mAttrChanged;
         if (var3 != null) {
            var3.onChange();
         }

      }
   }

   public interface OnNothingSelected {
      void onNothingSelected(AdapterView var1);
   }
}
