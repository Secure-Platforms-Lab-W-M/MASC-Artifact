/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.widget.Adapter
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemSelectedListener
 */
package androidx.databinding.adapters;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import androidx.databinding.InverseBindingListener;

public class AdapterViewBindingAdapter {
    public static void setOnItemSelectedListener(AdapterView adapterView, OnItemSelected onItemSelected, OnNothingSelected onNothingSelected, InverseBindingListener inverseBindingListener) {
        if (onItemSelected == null && onNothingSelected == null && inverseBindingListener == null) {
            adapterView.setOnItemSelectedListener(null);
            return;
        }
        adapterView.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)new OnItemSelectedComponentListener(onItemSelected, onNothingSelected, inverseBindingListener));
    }

    public static void setSelectedItemPosition(AdapterView adapterView, int n) {
        if (adapterView.getSelectedItemPosition() != n) {
            adapterView.setSelection(n);
        }
    }

    public static void setSelectedItemPosition(AdapterView adapterView, int n, Adapter adapter) {
        if (adapter != adapterView.getAdapter()) {
            adapterView.setAdapter(adapter);
            adapterView.setSelection(n);
            return;
        }
        if (adapterView.getSelectedItemPosition() != n) {
            adapterView.setSelection(n);
        }
    }

    public static void setSelection(AdapterView adapterView, int n) {
        AdapterViewBindingAdapter.setSelectedItemPosition(adapterView, n);
    }

    public static void setSelection(AdapterView adapterView, int n, Adapter adapter) {
        AdapterViewBindingAdapter.setSelectedItemPosition(adapterView, n, adapter);
    }

    public static interface OnItemSelected {
        public void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4);
    }

    public static class OnItemSelectedComponentListener
    implements AdapterView.OnItemSelectedListener {
        private final InverseBindingListener mAttrChanged;
        private final OnNothingSelected mNothingSelected;
        private final OnItemSelected mSelected;

        public OnItemSelectedComponentListener(OnItemSelected onItemSelected, OnNothingSelected onNothingSelected, InverseBindingListener inverseBindingListener) {
            this.mSelected = onItemSelected;
            this.mNothingSelected = onNothingSelected;
            this.mAttrChanged = inverseBindingListener;
        }

        public void onItemSelected(AdapterView<?> object, View view, int n, long l) {
            OnItemSelected onItemSelected = this.mSelected;
            if (onItemSelected != null) {
                onItemSelected.onItemSelected(object, view, n, l);
            }
            if ((object = this.mAttrChanged) != null) {
                object.onChange();
            }
        }

        public void onNothingSelected(AdapterView<?> object) {
            OnNothingSelected onNothingSelected = this.mNothingSelected;
            if (onNothingSelected != null) {
                onNothingSelected.onNothingSelected(object);
            }
            if ((object = this.mAttrChanged) != null) {
                object.onChange();
            }
        }
    }

    public static interface OnNothingSelected {
        public void onNothingSelected(AdapterView<?> var1);
    }

}

