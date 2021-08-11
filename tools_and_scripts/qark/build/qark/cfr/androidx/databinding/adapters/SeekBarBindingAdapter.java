/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.widget.SeekBar
 *  android.widget.SeekBar$OnSeekBarChangeListener
 */
package androidx.databinding.adapters;

import android.widget.SeekBar;
import androidx.databinding.InverseBindingListener;

public class SeekBarBindingAdapter {
    public static void setOnSeekBarChangeListener(SeekBar seekBar, final OnStartTrackingTouch onStartTrackingTouch, final OnStopTrackingTouch onStopTrackingTouch, final OnProgressChanged onProgressChanged, final InverseBindingListener inverseBindingListener) {
        if (onStartTrackingTouch == null && onStopTrackingTouch == null && onProgressChanged == null && inverseBindingListener == null) {
            seekBar.setOnSeekBarChangeListener(null);
            return;
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            public void onProgressChanged(SeekBar object, int n, boolean bl) {
                OnProgressChanged onProgressChanged2 = onProgressChanged;
                if (onProgressChanged2 != null) {
                    onProgressChanged2.onProgressChanged((SeekBar)object, n, bl);
                }
                if ((object = inverseBindingListener) != null) {
                    object.onChange();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                OnStartTrackingTouch onStartTrackingTouch2 = onStartTrackingTouch;
                if (onStartTrackingTouch2 != null) {
                    onStartTrackingTouch2.onStartTrackingTouch(seekBar);
                }
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                OnStopTrackingTouch onStopTrackingTouch2 = onStopTrackingTouch;
                if (onStopTrackingTouch2 != null) {
                    onStopTrackingTouch2.onStopTrackingTouch(seekBar);
                }
            }
        });
    }

    public static void setProgress(SeekBar seekBar, int n) {
        if (n != seekBar.getProgress()) {
            seekBar.setProgress(n);
        }
    }

    public static interface OnProgressChanged {
        public void onProgressChanged(SeekBar var1, int var2, boolean var3);
    }

    public static interface OnStartTrackingTouch {
        public void onStartTrackingTouch(SeekBar var1);
    }

    public static interface OnStopTrackingTouch {
        public void onStopTrackingTouch(SeekBar var1);
    }

}

