/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.codetroopers.betterpickers.numberpicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import java.math.BigDecimal;
import java.util.Vector;

public class NumberPickerBuilder {
    private Double currentDecimalValue;
    private Integer currentNumberValue;
    private Integer currentSignValue;
    private Integer decimalVisibility;
    private String labelText;
    private Vector<NumberPickerDialogFragment.NumberPickerDialogHandlerV2> mNumberPickerDialogHandlersV2 = new Vector();
    private OnDialogDismissListener mOnDismissListener;
    private int mReference;
    private FragmentManager manager;
    private BigDecimal maxNumber;
    private BigDecimal minNumber;
    private Integer plusMinusVisibility;
    private Integer styleResId;
    private Fragment targetFragment;

    public NumberPickerBuilder addNumberPickerDialogHandler(NumberPickerDialogFragment.NumberPickerDialogHandlerV2 numberPickerDialogHandlerV2) {
        this.mNumberPickerDialogHandlersV2.add(numberPickerDialogHandlerV2);
        return this;
    }

    public NumberPickerBuilder removeNumberPickerDialogHandler(NumberPickerDialogFragment.NumberPickerDialogHandlerV2 numberPickerDialogHandlerV2) {
        this.mNumberPickerDialogHandlersV2.remove(numberPickerDialogHandlerV2);
        return this;
    }

    public NumberPickerBuilder setCurrentNumber(Integer n) {
        if (n != null) {
            if (n >= 0) {
                this.currentSignValue = 0;
            } else {
                this.currentSignValue = 1;
                n = n * -1;
            }
            this.currentNumberValue = n;
            this.currentDecimalValue = null;
        }
        return this;
    }

    public NumberPickerBuilder setCurrentNumber(BigDecimal arrbigDecimal) {
        if (arrbigDecimal != null) {
            if (arrbigDecimal.signum() >= 0) {
                this.currentSignValue = 0;
            } else {
                this.currentSignValue = 1;
                arrbigDecimal = arrbigDecimal.abs();
            }
            arrbigDecimal = arrbigDecimal.divideAndRemainder(BigDecimal.ONE);
            this.currentNumberValue = arrbigDecimal[0].intValue();
            this.currentDecimalValue = arrbigDecimal[1].doubleValue();
        }
        return this;
    }

    public NumberPickerBuilder setDecimalVisibility(int n) {
        this.decimalVisibility = n;
        return this;
    }

    public NumberPickerBuilder setFragmentManager(FragmentManager fragmentManager) {
        this.manager = fragmentManager;
        return this;
    }

    public NumberPickerBuilder setLabelText(String string2) {
        this.labelText = string2;
        return this;
    }

    public NumberPickerBuilder setMaxNumber(BigDecimal bigDecimal) {
        this.maxNumber = bigDecimal;
        return this;
    }

    public NumberPickerBuilder setMinNumber(BigDecimal bigDecimal) {
        this.minNumber = bigDecimal;
        return this;
    }

    public NumberPickerBuilder setOnDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.mOnDismissListener = onDialogDismissListener;
        return this;
    }

    public NumberPickerBuilder setPlusMinusVisibility(int n) {
        this.plusMinusVisibility = n;
        return this;
    }

    public NumberPickerBuilder setReference(int n) {
        this.mReference = n;
        return this;
    }

    public NumberPickerBuilder setStyleResId(int n) {
        this.styleResId = n;
        return this;
    }

    public NumberPickerBuilder setTargetFragment(Fragment fragment) {
        this.targetFragment = fragment;
        return this;
    }

    public void show() {
        Object object = this.manager;
        if (object != null && this.styleResId != null) {
            Object object2 = object.beginTransaction();
            Fragment fragment = this.manager.findFragmentByTag("number_dialog");
            object = object2;
            if (fragment != null) {
                object2.remove(fragment).commit();
                object = this.manager.beginTransaction();
            }
            object.addToBackStack(null);
            object2 = NumberPickerDialogFragment.newInstance(this.mReference, this.styleResId, this.minNumber, this.maxNumber, this.plusMinusVisibility, this.decimalVisibility, this.labelText, this.currentNumberValue, this.currentDecimalValue, this.currentSignValue);
            fragment = this.targetFragment;
            if (fragment != null) {
                object2.setTargetFragment(fragment, 0);
            }
            object2.setNumberPickerDialogHandlersV2(this.mNumberPickerDialogHandlersV2);
            object2.setOnDismissListener(this.mOnDismissListener);
            object2.show((FragmentTransaction)object, "number_dialog");
            return;
        }
        Log.e((String)"NumberPickerBuilder", (String)"setFragmentManager() and setStyleResId() must be called.");
    }
}

