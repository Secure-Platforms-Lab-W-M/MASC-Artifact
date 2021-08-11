/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.AutoCompleteTextView
 *  android.widget.AutoCompleteTextView$Validator
 */
package androidx.databinding.adapters;

import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.adapters.AdapterViewBindingAdapter;

public class AutoCompleteTextViewBindingAdapter {
    public static void setOnItemSelectedListener(AutoCompleteTextView autoCompleteTextView, AdapterViewBindingAdapter.OnItemSelected onItemSelected, AdapterViewBindingAdapter.OnNothingSelected onNothingSelected) {
        if (onItemSelected == null && onNothingSelected == null) {
            autoCompleteTextView.setOnItemSelectedListener(null);
            return;
        }
        autoCompleteTextView.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)new AdapterViewBindingAdapter.OnItemSelectedComponentListener(onItemSelected, onNothingSelected, null));
    }

    public static void setValidator(AutoCompleteTextView autoCompleteTextView, final FixText fixText, final IsValid isValid) {
        if (fixText == null && isValid == null) {
            autoCompleteTextView.setValidator(null);
            return;
        }
        autoCompleteTextView.setValidator(new AutoCompleteTextView.Validator(){

            public CharSequence fixText(CharSequence charSequence) {
                FixText fixText2 = fixText;
                if (fixText2 != null) {
                    return fixText2.fixText(charSequence);
                }
                return charSequence;
            }

            public boolean isValid(CharSequence charSequence) {
                IsValid isValid2 = isValid;
                if (isValid2 != null) {
                    return isValid2.isValid(charSequence);
                }
                return true;
            }
        });
    }

    public static interface FixText {
        public CharSequence fixText(CharSequence var1);
    }

    public static interface IsValid {
        public boolean isValid(CharSequence var1);
    }

}

