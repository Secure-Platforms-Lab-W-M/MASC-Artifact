/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.codetroopers.betterpickers.timepicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import java.util.Vector;

public class TimePickerBuilder {
    private OnDialogDismissListener mOnDismissListener;
    private int mReference = -1;
    private Vector<TimePickerDialogFragment.TimePickerDialogHandler> mTimePickerDialogHandlers = new Vector();
    private FragmentManager manager;
    private Integer styleResId;
    private Fragment targetFragment;

    public TimePickerBuilder addTimePickerDialogHandler(TimePickerDialogFragment.TimePickerDialogHandler timePickerDialogHandler) {
        this.mTimePickerDialogHandlers.add(timePickerDialogHandler);
        return this;
    }

    public TimePickerBuilder removeTimePickerDialogHandler(TimePickerDialogFragment.TimePickerDialogHandler timePickerDialogHandler) {
        this.mTimePickerDialogHandlers.remove(timePickerDialogHandler);
        return this;
    }

    public TimePickerBuilder setFragmentManager(FragmentManager fragmentManager) {
        this.manager = fragmentManager;
        return this;
    }

    public TimePickerBuilder setOnDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.mOnDismissListener = onDialogDismissListener;
        return this;
    }

    public TimePickerBuilder setReference(int n) {
        this.mReference = n;
        return this;
    }

    public TimePickerBuilder setStyleResId(int n) {
        this.styleResId = n;
        return this;
    }

    public TimePickerBuilder setTargetFragment(Fragment fragment) {
        this.targetFragment = fragment;
        return this;
    }

    public void show() {
        Object object = this.manager;
        if (object != null && this.styleResId != null) {
            Object object2 = object.beginTransaction();
            Fragment fragment = this.manager.findFragmentByTag("time_dialog");
            object = object2;
            if (fragment != null) {
                object2.remove(fragment).commit();
                object = this.manager.beginTransaction();
            }
            object.addToBackStack(null);
            object2 = TimePickerDialogFragment.newInstance(this.mReference, this.styleResId);
            fragment = this.targetFragment;
            if (fragment != null) {
                object2.setTargetFragment(fragment, 0);
            }
            object2.setTimePickerDialogHandlers(this.mTimePickerDialogHandlers);
            object2.setOnDismissListener(this.mOnDismissListener);
            object2.show((FragmentTransaction)object, "time_dialog");
            return;
        }
        Log.e((String)"TimePickerBuilder", (String)"setFragmentManager() and setStyleResId() must be called.");
    }
}

