/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.codetroopers.betterpickers.datepicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import java.util.Vector;

public class DatePickerBuilder {
    private Integer dayOfMonth;
    private Vector<DatePickerDialogFragment.DatePickerDialogHandler> mDatePickerDialogHandlers = new Vector();
    private OnDialogDismissListener mOnDismissListener;
    private int mReference = -1;
    private FragmentManager manager;
    private Integer monthOfYear;
    private Integer styleResId;
    private Fragment targetFragment;
    private Integer year;
    private Boolean yearOptional = false;

    public DatePickerBuilder addDatePickerDialogHandler(DatePickerDialogFragment.DatePickerDialogHandler datePickerDialogHandler) {
        this.mDatePickerDialogHandlers.add(datePickerDialogHandler);
        return this;
    }

    public DatePickerBuilder removeDatePickerDialogHandler(DatePickerDialogFragment.DatePickerDialogHandler datePickerDialogHandler) {
        this.mDatePickerDialogHandlers.remove(datePickerDialogHandler);
        return this;
    }

    public DatePickerBuilder setDayOfMonth(int n) {
        this.dayOfMonth = n;
        return this;
    }

    public DatePickerBuilder setFragmentManager(FragmentManager fragmentManager) {
        this.manager = fragmentManager;
        return this;
    }

    public DatePickerBuilder setMonthOfYear(int n) {
        this.monthOfYear = n;
        return this;
    }

    public DatePickerBuilder setOnDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.mOnDismissListener = onDialogDismissListener;
        return this;
    }

    public DatePickerBuilder setReference(int n) {
        this.mReference = n;
        return this;
    }

    public DatePickerBuilder setStyleResId(int n) {
        this.styleResId = n;
        return this;
    }

    public DatePickerBuilder setTargetFragment(Fragment fragment) {
        this.targetFragment = fragment;
        return this;
    }

    public DatePickerBuilder setYear(int n) {
        this.year = n;
        return this;
    }

    public DatePickerBuilder setYearOptional(boolean bl) {
        this.yearOptional = bl;
        return this;
    }

    public void show() {
        Object object = this.manager;
        if (object != null && this.styleResId != null) {
            Object object2 = object.beginTransaction();
            Fragment fragment = this.manager.findFragmentByTag("date_dialog");
            object = object2;
            if (fragment != null) {
                object2.remove(fragment).commit();
                object = this.manager.beginTransaction();
            }
            object.addToBackStack(null);
            object2 = DatePickerDialogFragment.newInstance(this.mReference, this.styleResId, this.monthOfYear, this.dayOfMonth, this.year, this.yearOptional);
            fragment = this.targetFragment;
            if (fragment != null) {
                object2.setTargetFragment(fragment, 0);
            }
            object2.setDatePickerDialogHandlers(this.mDatePickerDialogHandlers);
            object2.setOnDismissListener(this.mOnDismissListener);
            object2.show((FragmentTransaction)object, "date_dialog");
            return;
        }
        Log.e((String)"DatePickerBuilder", (String)"setFragmentManager() and setStyleResId() must be called.");
    }
}

