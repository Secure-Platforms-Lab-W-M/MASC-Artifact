/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.codetroopers.betterpickers.hmspicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import java.util.Vector;

public class HmsPickerBuilder {
    private Vector<HmsPickerDialogFragment.HmsPickerDialogHandlerV2> mHmsPickerDialogHandlerV2s = new Vector();
    private int mHours;
    private int mMinutes;
    private OnDialogDismissListener mOnDismissListener;
    private int mReference;
    private int mSeconds;
    private FragmentManager manager;
    private Integer plusMinusVisibility;
    private Integer styleResId;
    private Fragment targetFragment;

    private static int bounded(int n, int n2, int n3) {
        return Math.min(Math.max(n, n2), n3);
    }

    public HmsPickerBuilder addHmsPickerDialogHandler(HmsPickerDialogFragment.HmsPickerDialogHandlerV2 hmsPickerDialogHandlerV2) {
        this.mHmsPickerDialogHandlerV2s.add(hmsPickerDialogHandlerV2);
        return this;
    }

    public HmsPickerBuilder removeHmsPickerDialogHandler(HmsPickerDialogFragment.HmsPickerDialogHandlerV2 hmsPickerDialogHandlerV2) {
        this.mHmsPickerDialogHandlerV2s.remove(hmsPickerDialogHandlerV2);
        return this;
    }

    public HmsPickerBuilder setFragmentManager(FragmentManager fragmentManager) {
        this.manager = fragmentManager;
        return this;
    }

    public HmsPickerBuilder setOnDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.mOnDismissListener = onDialogDismissListener;
        return this;
    }

    public HmsPickerBuilder setPlusMinusVisibility(int n) {
        this.plusMinusVisibility = n;
        return this;
    }

    public HmsPickerBuilder setReference(int n) {
        this.mReference = n;
        return this;
    }

    public HmsPickerBuilder setStyleResId(int n) {
        this.styleResId = n;
        return this;
    }

    public HmsPickerBuilder setTargetFragment(Fragment fragment) {
        this.targetFragment = fragment;
        return this;
    }

    public HmsPickerBuilder setTime(int n, int n2, int n3) {
        this.mHours = HmsPickerBuilder.bounded(n, 0, 99);
        this.mMinutes = HmsPickerBuilder.bounded(n2, 0, 99);
        this.mSeconds = HmsPickerBuilder.bounded(n3, 0, 99);
        return this;
    }

    public HmsPickerBuilder setTimeInMilliseconds(long l) {
        return this.setTimeInSeconds((int)(l / 1000L));
    }

    public HmsPickerBuilder setTimeInSeconds(int n) {
        int n2 = n / 3600;
        return this.setTime(n2, n / 60, (n %= 3600) % 60);
    }

    public void show() {
        Object object = this.manager;
        if (object != null && this.styleResId != null) {
            Object object2 = object.beginTransaction();
            Fragment fragment = this.manager.findFragmentByTag("hms_dialog");
            object = object2;
            if (fragment != null) {
                object2.remove(fragment).commit();
                object = this.manager.beginTransaction();
            }
            object.addToBackStack(null);
            object2 = HmsPickerDialogFragment.newInstance(this.mReference, this.styleResId, this.plusMinusVisibility);
            fragment = this.targetFragment;
            if (fragment != null) {
                object2.setTargetFragment(fragment, 0);
            }
            object2.setHmsPickerDialogHandlersV2(this.mHmsPickerDialogHandlerV2s);
            int n = this.mHours;
            int n2 = this.mMinutes;
            int n3 = this.mSeconds;
            if ((n | n2 | n3) != 0) {
                object2.setTime(n, n2, n3);
            }
            object2.setOnDismissListener(this.mOnDismissListener);
            object2.show((FragmentTransaction)object, "hms_dialog");
            return;
        }
        Log.e((String)"HmsPickerBuilder", (String)"setFragmentManager() and setStyleResId() must be called.");
    }
}

