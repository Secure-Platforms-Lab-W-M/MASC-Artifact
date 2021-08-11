/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.RadioGroup
 *  android.widget.RadioGroup$OnCheckedChangeListener
 */
package androidx.databinding.adapters;

import android.widget.RadioGroup;
import androidx.databinding.InverseBindingListener;

public class RadioGroupBindingAdapter {
    public static void setCheckedButton(RadioGroup radioGroup, int n) {
        if (n != radioGroup.getCheckedRadioButtonId()) {
            radioGroup.check(n);
        }
    }

    public static void setListeners(RadioGroup radioGroup, final RadioGroup.OnCheckedChangeListener onCheckedChangeListener, final InverseBindingListener inverseBindingListener) {
        if (inverseBindingListener == null) {
            radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
            return;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            public void onCheckedChanged(RadioGroup radioGroup, int n) {
                RadioGroup.OnCheckedChangeListener onCheckedChangeListener2 = onCheckedChangeListener;
                if (onCheckedChangeListener2 != null) {
                    onCheckedChangeListener2.onCheckedChanged(radioGroup, n);
                }
                inverseBindingListener.onChange();
            }
        });
    }

}

