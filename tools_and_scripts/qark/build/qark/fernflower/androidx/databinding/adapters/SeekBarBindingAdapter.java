package androidx.databinding.adapters;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import androidx.databinding.InverseBindingListener;

public class SeekBarBindingAdapter {
   public static void setOnSeekBarChangeListener(SeekBar var0, final SeekBarBindingAdapter.OnStartTrackingTouch var1, final SeekBarBindingAdapter.OnStopTrackingTouch var2, final SeekBarBindingAdapter.OnProgressChanged var3, final InverseBindingListener var4) {
      if (var1 == null && var2 == null && var3 == null && var4 == null) {
         var0.setOnSeekBarChangeListener((OnSeekBarChangeListener)null);
      } else {
         var0.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar var1x, int var2x, boolean var3x) {
               SeekBarBindingAdapter.OnProgressChanged var4x = var3;
               if (var4x != null) {
                  var4x.onProgressChanged(var1x, var2x, var3x);
               }

               InverseBindingListener var5 = var4;
               if (var5 != null) {
                  var5.onChange();
               }

            }

            public void onStartTrackingTouch(SeekBar var1x) {
               SeekBarBindingAdapter.OnStartTrackingTouch var2x = var1;
               if (var2x != null) {
                  var2x.onStartTrackingTouch(var1x);
               }

            }

            public void onStopTrackingTouch(SeekBar var1x) {
               SeekBarBindingAdapter.OnStopTrackingTouch var2x = var2;
               if (var2x != null) {
                  var2x.onStopTrackingTouch(var1x);
               }

            }
         });
      }
   }

   public static void setProgress(SeekBar var0, int var1) {
      if (var1 != var0.getProgress()) {
         var0.setProgress(var1);
      }

   }

   public interface OnProgressChanged {
      void onProgressChanged(SeekBar var1, int var2, boolean var3);
   }

   public interface OnStartTrackingTouch {
      void onStartTrackingTouch(SeekBar var1);
   }

   public interface OnStopTrackingTouch {
      void onStopTrackingTouch(SeekBar var1);
   }
}
