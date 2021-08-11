package androidx.databinding.adapters;

import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import androidx.databinding.InverseBindingListener;

public class RatingBarBindingAdapter {
   public static void setListeners(RatingBar var0, final OnRatingBarChangeListener var1, final InverseBindingListener var2) {
      if (var2 == null) {
         var0.setOnRatingBarChangeListener(var1);
      } else {
         var0.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar var1x, float var2x, boolean var3) {
               OnRatingBarChangeListener var4 = var1;
               if (var4 != null) {
                  var4.onRatingChanged(var1x, var2x, var3);
               }

               var2.onChange();
            }
         });
      }
   }

   public static void setRating(RatingBar var0, float var1) {
      if (var0.getRating() != var1) {
         var0.setRating(var1);
      }

   }
}
