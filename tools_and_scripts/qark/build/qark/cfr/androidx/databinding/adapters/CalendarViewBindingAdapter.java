/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.CalendarView
 *  android.widget.CalendarView$OnDateChangeListener
 */
package androidx.databinding.adapters;

import android.widget.CalendarView;
import androidx.databinding.InverseBindingListener;

public class CalendarViewBindingAdapter {
    public static void setDate(CalendarView calendarView, long l) {
        if (calendarView.getDate() != l) {
            calendarView.setDate(l);
        }
    }

    public static void setListeners(CalendarView calendarView, final CalendarView.OnDateChangeListener onDateChangeListener, final InverseBindingListener inverseBindingListener) {
        if (inverseBindingListener == null) {
            calendarView.setOnDateChangeListener(onDateChangeListener);
            return;
        }
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            public void onSelectedDayChange(CalendarView calendarView, int n, int n2, int n3) {
                CalendarView.OnDateChangeListener onDateChangeListener2 = onDateChangeListener;
                if (onDateChangeListener2 != null) {
                    onDateChangeListener2.onSelectedDayChange(calendarView, n, n2, n3);
                }
                inverseBindingListener.onChange();
            }
        });
    }

}

