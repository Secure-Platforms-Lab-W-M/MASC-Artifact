/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.widget.DatePicker
 *  android.widget.DatePicker$OnDateChangedListener
 *  androidx.databinding.library.baseAdapters.R
 *  androidx.databinding.library.baseAdapters.R$id
 */
package androidx.databinding.adapters;

import android.view.View;
import android.widget.DatePicker;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.adapters.ListenerUtil;
import androidx.databinding.library.baseAdapters.R;

public class DatePickerBindingAdapter {
    public static void setListeners(DatePicker datePicker, int n, int n2, int n3, DatePicker.OnDateChangedListener onDateChangedListener, InverseBindingListener inverseBindingListener, InverseBindingListener inverseBindingListener2, InverseBindingListener inverseBindingListener3) {
        DateChangedListener dateChangedListener;
        int n4 = n;
        if (n == 0) {
            n4 = datePicker.getYear();
        }
        n = n3;
        if (n3 == 0) {
            n = datePicker.getDayOfMonth();
        }
        if (inverseBindingListener == null && inverseBindingListener2 == null && inverseBindingListener3 == null) {
            datePicker.init(n4, n2, n, onDateChangedListener);
            return;
        }
        DateChangedListener dateChangedListener2 = dateChangedListener = (DateChangedListener)ListenerUtil.getListener((View)datePicker, R.id.onDateChanged);
        if (dateChangedListener == null) {
            dateChangedListener2 = new DateChangedListener();
            ListenerUtil.trackListener((View)datePicker, dateChangedListener2, R.id.onDateChanged);
        }
        dateChangedListener2.setListeners(onDateChangedListener, inverseBindingListener, inverseBindingListener2, inverseBindingListener3);
        datePicker.init(n4, n2, n, (DatePicker.OnDateChangedListener)dateChangedListener2);
    }

    private static class DateChangedListener
    implements DatePicker.OnDateChangedListener {
        InverseBindingListener mDayChanged;
        DatePicker.OnDateChangedListener mListener;
        InverseBindingListener mMonthChanged;
        InverseBindingListener mYearChanged;

        private DateChangedListener() {
        }

        public void onDateChanged(DatePicker object, int n, int n2, int n3) {
            DatePicker.OnDateChangedListener onDateChangedListener = this.mListener;
            if (onDateChangedListener != null) {
                onDateChangedListener.onDateChanged((DatePicker)object, n, n2, n3);
            }
            if ((object = this.mYearChanged) != null) {
                object.onChange();
            }
            if ((object = this.mMonthChanged) != null) {
                object.onChange();
            }
            if ((object = this.mDayChanged) != null) {
                object.onChange();
            }
        }

        public void setListeners(DatePicker.OnDateChangedListener onDateChangedListener, InverseBindingListener inverseBindingListener, InverseBindingListener inverseBindingListener2, InverseBindingListener inverseBindingListener3) {
            this.mListener = onDateChangedListener;
            this.mYearChanged = inverseBindingListener;
            this.mMonthChanged = inverseBindingListener2;
            this.mDayChanged = inverseBindingListener3;
        }
    }

}

