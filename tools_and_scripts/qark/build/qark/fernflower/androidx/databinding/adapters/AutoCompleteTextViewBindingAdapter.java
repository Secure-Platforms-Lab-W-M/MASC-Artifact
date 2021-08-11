package androidx.databinding.adapters;

import android.widget.AutoCompleteTextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView.Validator;
import androidx.databinding.InverseBindingListener;

public class AutoCompleteTextViewBindingAdapter {
   public static void setOnItemSelectedListener(AutoCompleteTextView var0, AdapterViewBindingAdapter.OnItemSelected var1, AdapterViewBindingAdapter.OnNothingSelected var2) {
      if (var1 == null && var2 == null) {
         var0.setOnItemSelectedListener((OnItemSelectedListener)null);
      } else {
         var0.setOnItemSelectedListener(new AdapterViewBindingAdapter.OnItemSelectedComponentListener(var1, var2, (InverseBindingListener)null));
      }
   }

   public static void setValidator(AutoCompleteTextView var0, final AutoCompleteTextViewBindingAdapter.FixText var1, final AutoCompleteTextViewBindingAdapter.IsValid var2) {
      if (var1 == null && var2 == null) {
         var0.setValidator((Validator)null);
      } else {
         var0.setValidator(new Validator() {
            public CharSequence fixText(CharSequence var1x) {
               AutoCompleteTextViewBindingAdapter.FixText var2x = var1;
               return var2x != null ? var2x.fixText(var1x) : var1x;
            }

            public boolean isValid(CharSequence var1x) {
               AutoCompleteTextViewBindingAdapter.IsValid var2x = var2;
               return var2x != null ? var2x.isValid(var1x) : true;
            }
         });
      }
   }

   public interface FixText {
      CharSequence fixText(CharSequence var1);
   }

   public interface IsValid {
      boolean isValid(CharSequence var1);
   }
}
