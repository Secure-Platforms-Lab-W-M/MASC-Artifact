/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.NumberPicker
 *  android.widget.NumberPicker$OnValueChangeListener
 */
package androidx.databinding.adapters;

import android.widget.NumberPicker;
import androidx.databinding.InverseBindingListener;

public class NumberPickerBindingAdapter {
    public static void setListeners(NumberPicker numberPicker, final NumberPicker.OnValueChangeListener onValueChangeListener, final InverseBindingListener inverseBindingListener) {
        if (inverseBindingListener == null) {
            numberPicker.setOnValueChangedListener(onValueChangeListener);
            return;
        }
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            public void onValueChange(NumberPicker numberPicker, int n, int n2) {
                NumberPicker.OnValueChangeListener onValueChangeListener2 = onValueChangeListener;
                if (onValueChangeListener2 != null) {
                    onValueChangeListener2.onValueChange(numberPicker, n, n2);
                }
                inverseBindingListener.onChange();
            }
        });
    }

    public static void setValue(NumberPicker numberPicker, int n) {
        if (numberPicker.getValue() != n) {
            numberPicker.setValue(n);
        }
    }

}

