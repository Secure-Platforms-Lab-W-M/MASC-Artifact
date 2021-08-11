/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.BaseAdapter
 *  android.widget.TextView
 *  com.google.android.material.R
 *  com.google.android.material.R$layout
 */
package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CalendarItemStyle;
import com.google.android.material.datepicker.CalendarStyle;
import com.google.android.material.datepicker.DateSelector;
import com.google.android.material.datepicker.DateStrings;
import com.google.android.material.datepicker.Month;
import com.google.android.material.datepicker.UtcDates;
import java.util.Collection;
import java.util.Iterator;

class MonthAdapter
extends BaseAdapter {
    static final int MAXIMUM_WEEKS = UtcDates.getUtcCalendar().getMaximum(4);
    final CalendarConstraints calendarConstraints;
    CalendarStyle calendarStyle;
    final DateSelector<?> dateSelector;
    final Month month;

    MonthAdapter(Month month, DateSelector<?> dateSelector, CalendarConstraints calendarConstraints) {
        this.month = month;
        this.dateSelector = dateSelector;
        this.calendarConstraints = calendarConstraints;
    }

    private void initializeStyles(Context context) {
        if (this.calendarStyle == null) {
            this.calendarStyle = new CalendarStyle(context);
        }
    }

    int dayToPosition(int n) {
        return this.firstPositionInMonth() + (n - 1);
    }

    int firstPositionInMonth() {
        return this.month.daysFromStartOfWeekToFirstOfMonth();
    }

    public int getCount() {
        return this.month.daysInMonth + this.firstPositionInMonth();
    }

    public Long getItem(int n) {
        if (n >= this.month.daysFromStartOfWeekToFirstOfMonth() && n <= this.lastPositionInMonth()) {
            return this.month.getDay(this.positionToDay(n));
        }
        return null;
    }

    public long getItemId(int n) {
        return n / this.month.daysInWeek;
    }

    public TextView getView(int n, View object, ViewGroup object2) {
        int n2;
        long l;
        this.initializeStyles(object2.getContext());
        TextView textView = (TextView)object;
        if (object == null) {
            textView = (TextView)LayoutInflater.from((Context)object2.getContext()).inflate(R.layout.mtrl_calendar_day, (ViewGroup)object2, false);
        }
        if ((n2 = n - this.firstPositionInMonth()) >= 0 && n2 < this.month.daysInMonth) {
            textView.setTag((Object)this.month);
            textView.setText((CharSequence)String.valueOf(++n2));
            l = this.month.getDay(n2);
            if (this.month.year == Month.today().year) {
                textView.setContentDescription((CharSequence)DateStrings.getMonthDayOfWeekDay(l));
            } else {
                textView.setContentDescription((CharSequence)DateStrings.getYearMonthDayOfWeekDay(l));
            }
            textView.setVisibility(0);
            textView.setEnabled(true);
        } else {
            textView.setVisibility(8);
            textView.setEnabled(false);
        }
        object = this.getItem(n);
        if (object == null) {
            return textView;
        }
        if (this.calendarConstraints.getDateValidator().isValid(object.longValue())) {
            textView.setEnabled(true);
            object2 = this.dateSelector.getSelectedDays().iterator();
            while (object2.hasNext()) {
                l = (Long)object2.next();
                if (UtcDates.canonicalYearMonthDay(object.longValue()) != UtcDates.canonicalYearMonthDay(l)) continue;
                this.calendarStyle.selectedDay.styleItem(textView);
                return textView;
            }
            if (UtcDates.getTodayCalendar().getTimeInMillis() == object.longValue()) {
                this.calendarStyle.todayDay.styleItem(textView);
                return textView;
            }
            this.calendarStyle.day.styleItem(textView);
            return textView;
        }
        textView.setEnabled(false);
        this.calendarStyle.invalidDay.styleItem(textView);
        return textView;
    }

    public boolean hasStableIds() {
        return true;
    }

    boolean isFirstInRow(int n) {
        if (n % this.month.daysInWeek == 0) {
            return true;
        }
        return false;
    }

    boolean isLastInRow(int n) {
        if ((n + 1) % this.month.daysInWeek == 0) {
            return true;
        }
        return false;
    }

    int lastPositionInMonth() {
        return this.month.daysFromStartOfWeekToFirstOfMonth() + this.month.daysInMonth - 1;
    }

    int positionToDay(int n) {
        return n - this.month.daysFromStartOfWeekToFirstOfMonth() + 1;
    }

    boolean withinMonth(int n) {
        if (n >= this.firstPositionInMonth() && n <= this.lastPositionInMonth()) {
            return true;
        }
        return false;
    }
}

