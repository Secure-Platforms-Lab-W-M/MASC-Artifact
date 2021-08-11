package androidx.databinding.adapters;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public class ImageViewBindingAdapter {
   public static void setImageDrawable(ImageView var0, Drawable var1) {
      var0.setImageDrawable(var1);
   }

   public static void setImageUri(ImageView var0, Uri var1) {
      var0.setImageURI(var1);
   }

   public static void setImageUri(ImageView var0, String var1) {
      if (var1 == null) {
         var0.setImageURI((Uri)null);
      } else {
         var0.setImageURI(Uri.parse(var1));
      }
   }
}
