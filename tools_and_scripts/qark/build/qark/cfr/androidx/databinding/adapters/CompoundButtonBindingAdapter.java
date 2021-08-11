/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 */
package androidx.databinding.adapters;

import android.widget.CompoundButton;
import androidx.databinding.InverseBindingListener;

public class CompoundButtonBindingAdapter {
    public static void setChecked(CompoundButton compoundButton, boolean bl) {
        if (compoundButton.isChecked() != bl) {
            compoundButton.setChecked(bl);
        }
    }

    public static void setListeners(CompoundButton compoundButton, final CompoundButton.OnCheckedChangeListener onCheckedChangeListener, final InverseBindingListener inverseBindingListener) {
        if (inverseBindingListener == null) {
            compoundButton.setOnCheckedChangeListener(onCheckedChangeListener);
            return;
        }
        compoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                CompoundButton.OnCheckedChangeListener onCheckedChangeListener2 = onCheckedChangeListener;
                if (onCheckedChangeListener2 != null) {
                    onCheckedChangeListener2.onCheckedChanged(compoundButton, bl);
                }
                inverseBindingListener.onChange();
            }
        });
    }

}

