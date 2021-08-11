/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.codetroopers.betterpickers.expirationpicker;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.expirationpicker.ExpirationPickerDialogFragment;
import java.util.Vector;

public class ExpirationPickerBuilder {
    private Vector<ExpirationPickerDialogFragment.ExpirationPickerDialogHandler> mExpirationPickerDialogHandlers = new Vector();
    private OnDialogDismissListener mOnDismissListener;
    private int mReference = -1;
    private FragmentManager manager;
    private Integer minimumYear;
    private Integer monthOfYear;
    private Integer styleResId;
    private Fragment targetFragment;
    private Integer year;

    public ExpirationPickerBuilder addExpirationPickerDialogHandler(ExpirationPickerDialogFragment.ExpirationPickerDialogHandler expirationPickerDialogHandler) {
        this.mExpirationPickerDialogHandlers.add(expirationPickerDialogHandler);
        return this;
    }

    public ExpirationPickerBuilder removeExpirationPickerDialogHandler(ExpirationPickerDialogFragment.ExpirationPickerDialogHandler expirationPickerDialogHandler) {
        this.mExpirationPickerDialogHandlers.remove(expirationPickerDialogHandler);
        return this;
    }

    public ExpirationPickerBuilder setFragmentManager(FragmentManager fragmentManager) {
        this.manager = fragmentManager;
        return this;
    }

    public ExpirationPickerBuilder setMinYear(int n) {
        this.minimumYear = n;
        return this;
    }

    public ExpirationPickerBuilder setMonthOfYear(int n) {
        this.monthOfYear = n;
        return this;
    }

    public ExpirationPickerBuilder setOnDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.mOnDismissListener = onDialogDismissListener;
        return this;
    }

    public ExpirationPickerBuilder setReference(int n) {
        this.mReference = n;
        return this;
    }

    public ExpirationPickerBuilder setStyleResId(int n) {
        this.styleResId = n;
        return this;
    }

    public ExpirationPickerBuilder setTargetFragment(Fragment fragment) {
        this.targetFragment = fragment;
        return this;
    }

    public ExpirationPickerBuilder setYear(int n) {
        this.year = n;
        return this;
    }

    public void show() {
        Object object = this.manager;
        if (object != null && this.styleResId != null) {
            Object object2 = object.beginTransaction();
            Fragment fragment = this.manager.findFragmentByTag("expiration_dialog");
            object = object2;
            if (fragment != null) {
                object2.remove(fragment).commit();
                object = this.manager.beginTransaction();
            }
            object.addToBackStack(null);
            object2 = ExpirationPickerDialogFragment.newInstance(this.mReference, this.styleResId, this.monthOfYear, this.year, this.minimumYear);
            fragment = this.targetFragment;
            if (fragment != null) {
                object2.setTargetFragment(fragment, 0);
            }
            object2.setExpirationPickerDialogHandlers(this.mExpirationPickerDialogHandlers);
            object2.setOnDismissListener(this.mOnDismissListener);
            object2.show((FragmentTransaction)object, "expiration_dialog");
            return;
        }
        Log.e((String)"ExpirationPickerBuilder", (String)"setFragmentManager() and setStyleResId() must be called.");
    }
}

