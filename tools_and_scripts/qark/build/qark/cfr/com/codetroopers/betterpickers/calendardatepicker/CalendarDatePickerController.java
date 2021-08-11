/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.codetroopers.betterpickers.calendardatepicker;

import android.util.SparseArray;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

interface CalendarDatePickerController {
    public SparseArray<MonthAdapter.CalendarDay> getDisabledDays();

    public int getFirstDayOfWeek();

    public MonthAdapter.CalendarDay getMaxDate();

    public MonthAdapter.CalendarDay getMinDate();

    public MonthAdapter.CalendarDay getSelectedDay();

    public void onDayOfMonthSelected(int var1, int var2, int var3);

    public void onYearSelected(int var1);

    public void registerOnDateChangedListener(CalendarDatePickerDialogFragment.OnDateChangedListener var1);

    public void tryVibrate();

    public void unregisterOnDateChangedListener(CalendarDatePickerDialogFragment.OnDateChangedListener var1);
}

