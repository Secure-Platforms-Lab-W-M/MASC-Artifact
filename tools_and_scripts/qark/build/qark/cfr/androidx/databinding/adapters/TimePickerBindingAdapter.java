/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.widget.TimePicker
 *  android.widget.TimePicker$OnTimeChangedListener
 */
package androidx.databinding.adapters;

import android.os.Build;
import android.widget.TimePicker;
import androidx.databinding.InverseBindingListener;

public class TimePickerBindingAdapter {
    public static int getHour(TimePicker object) {
        if (Build.VERSION.SDK_INT >= 23) {
            return object.getHour();
        }
        if ((object = object.getCurrentHour()) == null) {
            return 0;
        }
        return object.intValue();
    }

    public static int getMinute(TimePicker object) {
        if (Build.VERSION.SDK_INT >= 23) {
            return object.getMinute();
        }
        if ((object = object.getCurrentMinute()) == null) {
            return 0;
        }
        return object.intValue();
    }

    public static void setHour(TimePicker timePicker, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (timePicker.getHour() != n) {
                timePicker.setHour(n);
                return;
            }
        } else if (timePicker.getCurrentHour() != n) {
            timePicker.setCurrentHour(Integer.valueOf(n));
        }
    }

    public static void setListeners(TimePicker timePicker, final TimePicker.OnTimeChangedListener onTimeChangedListener, final InverseBindingListener inverseBindingListener, final InverseBindingListener inverseBindingListener2) {
        if (inverseBindingListener == null && inverseBindingListener2 == null) {
            timePicker.setOnTimeChangedListener(onTimeChangedListener);
            return;
        }
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

            public void onTimeChanged(TimePicker object, int n, int n2) {
                TimePicker.OnTimeChangedListener onTimeChangedListener2 = onTimeChangedListener;
                if (onTimeChangedListener2 != null) {
                    onTimeChangedListener2.onTimeChanged((TimePicker)object, n, n2);
                }
                if ((object = inverseBindingListener) != null) {
                    object.onChange();
                }
                if ((object = inverseBindingListener2) != null) {
                    object.onChange();
                }
            }
        });
    }

    public static void setMinute(TimePicker timePicker, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (timePicker.getMinute() != n) {
                timePicker.setMinute(n);
                return;
            }
        } else if (timePicker.getCurrentMinute() != n) {
            timePicker.setCurrentHour(Integer.valueOf(n));
        }
    }

}

