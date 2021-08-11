/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.widget.AbsSpinner
 *  android.widget.ArrayAdapter
 *  android.widget.SpinnerAdapter
 */
package androidx.databinding.adapters;

import android.content.Context;
import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import androidx.databinding.adapters.ObservableListAdapter;
import java.util.List;

public class AbsSpinnerBindingAdapter {
    public static <T> void setEntries(AbsSpinner absSpinner, List<T> list) {
        if (list != null) {
            SpinnerAdapter spinnerAdapter = absSpinner.getAdapter();
            if (spinnerAdapter instanceof ObservableListAdapter) {
                ((ObservableListAdapter)spinnerAdapter).setList(list);
            } else {
                absSpinner.setAdapter(new ObservableListAdapter<T>(absSpinner.getContext(), list, 17367048, 17367049, 0));
            }
            return;
        }
        absSpinner.setAdapter(null);
    }

    public static <T extends CharSequence> void setEntries(AbsSpinner absSpinner, T[] arrayAdapter) {
        if (arrayAdapter != null) {
            int n;
            SpinnerAdapter spinnerAdapter = absSpinner.getAdapter();
            int n2 = n = 1;
            if (spinnerAdapter != null) {
                n2 = n;
                if (spinnerAdapter.getCount() == arrayAdapter.length) {
                    int n3 = 0;
                    n = 0;
                    do {
                        n2 = n3;
                        if (n >= arrayAdapter.length) break;
                        if (!arrayAdapter[n].equals(spinnerAdapter.getItem(n))) {
                            n2 = 1;
                            break;
                        }
                        ++n;
                    } while (true);
                }
            }
            if (n2 != 0) {
                arrayAdapter = new ArrayAdapter(absSpinner.getContext(), 17367048, (Object[])arrayAdapter);
                arrayAdapter.setDropDownViewResource(17367049);
                absSpinner.setAdapter((SpinnerAdapter)arrayAdapter);
            }
            return;
        }
        absSpinner.setAdapter(null);
    }
}

