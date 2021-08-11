/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.BaseAdapter
 *  android.widget.TextView
 *  com.google.android.material.R
 *  com.google.android.material.R$layout
 *  com.google.android.material.R$string
 */
package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.R;
import com.google.android.material.datepicker.UtcDates;
import java.util.Calendar;
import java.util.Locale;

class DaysOfWeekAdapter
extends BaseAdapter {
    private static final int CALENDAR_DAY_STYLE;
    private static final int NARROW_FORMAT = 4;
    private final Calendar calendar;
    private final int daysInWeek;
    private final int firstDayOfWeek;

    static {
        int n = Build.VERSION.SDK_INT >= 26 ? 4 : 1;
        CALENDAR_DAY_STYLE = n;
    }

    public DaysOfWeekAdapter() {
        Calendar calendar;
        this.calendar = calendar = UtcDates.getUtcCalendar();
        this.daysInWeek = calendar.getMaximum(7);
        this.firstDayOfWeek = this.calendar.getFirstDayOfWeek();
    }

    private int positionToDayOfWeek(int n) {
        int n2 = this.firstDayOfWeek + n;
        int n3 = this.daysInWeek;
        n = n2;
        if (n2 > n3) {
            n = n2 - n3;
        }
        return n;
    }

    public int getCount() {
        return this.daysInWeek;
    }

    public Integer getItem(int n) {
        if (n >= this.daysInWeek) {
            return null;
        }
        return this.positionToDayOfWeek(n);
    }

    public long getItemId(int n) {
        return 0L;
    }

    public View getView(int n, View view, ViewGroup viewGroup) {
        TextView textView = (TextView)view;
        if (view == null) {
            textView = (TextView)LayoutInflater.from((Context)viewGroup.getContext()).inflate(R.layout.mtrl_calendar_day_of_week, viewGroup, false);
        }
        this.calendar.set(7, this.positionToDayOfWeek(n));
        textView.setText((CharSequence)this.calendar.getDisplayName(7, CALENDAR_DAY_STYLE, Locale.getDefault()));
        textView.setContentDescription((CharSequence)String.format(viewGroup.getContext().getString(R.string.mtrl_picker_day_of_week_column_header), this.calendar.getDisplayName(7, 2, Locale.getDefault())));
        return textView;
    }
}

