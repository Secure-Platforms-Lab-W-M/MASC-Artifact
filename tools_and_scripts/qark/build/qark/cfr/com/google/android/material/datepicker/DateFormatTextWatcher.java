/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.Editable
 *  android.text.TextUtils
 *  android.text.TextWatcher
 *  com.google.android.material.R
 *  com.google.android.material.R$string
 */
package com.google.android.material.datepicker;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.google.android.material.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateStrings;
import com.google.android.material.datepicker.UtcDates;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

abstract class DateFormatTextWatcher
implements TextWatcher {
    private final CalendarConstraints constraints;
    private final DateFormat dateFormat;
    private final String formatHint;
    private final String outOfRange;
    private final TextInputLayout textInputLayout;

    DateFormatTextWatcher(String string2, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints) {
        this.formatHint = string2;
        this.dateFormat = dateFormat;
        this.textInputLayout = textInputLayout;
        this.constraints = calendarConstraints;
        this.outOfRange = textInputLayout.getContext().getString(R.string.mtrl_picker_out_of_range);
    }

    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
    }

    void onInvalidDate() {
    }

    public void onTextChanged(CharSequence object, int n, int n2, int n3) {
        if (TextUtils.isEmpty((CharSequence)object)) {
            this.textInputLayout.setError(null);
            this.onValidDate(null);
            return;
        }
        try {
            object = this.dateFormat.parse(object.toString());
            this.textInputLayout.setError(null);
            long l = object.getTime();
            if (this.constraints.getDateValidator().isValid(l) && this.constraints.isWithinBounds(l)) {
                this.onValidDate(object.getTime());
            } else {
                this.textInputLayout.setError(String.format(this.outOfRange, DateStrings.getDateString(l)));
                this.onInvalidDate();
            }
            return;
        }
        catch (ParseException parseException) {
            String string2 = this.textInputLayout.getContext().getString(R.string.mtrl_picker_invalid_format);
            String string3 = String.format(this.textInputLayout.getContext().getString(R.string.mtrl_picker_invalid_format_use), this.formatHint);
            String string4 = String.format(this.textInputLayout.getContext().getString(R.string.mtrl_picker_invalid_format_example), this.dateFormat.format(new Date(UtcDates.getTodayCalendar().getTimeInMillis())));
            TextInputLayout textInputLayout = this.textInputLayout;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("\n");
            stringBuilder.append(string3);
            stringBuilder.append("\n");
            stringBuilder.append(string4);
            textInputLayout.setError(stringBuilder.toString());
            this.onInvalidDate();
            return;
        }
    }

    abstract void onValidDate(Long var1);
}

