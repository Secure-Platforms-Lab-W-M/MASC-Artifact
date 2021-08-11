package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Matrix;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@RequiresApi(14)
class ImageViewUtilsApi14 implements ImageViewUtilsImpl {
   public void animateTransform(ImageView var1, Matrix var2) {
      var1.setImageMatrix(var2);
   }

   public void reserveEndAnimateTransform(final ImageView var1, Animator var2) {
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            ScaleType var2 = (ScaleType)var1.getTag(R$id.save_scale_type);
            var1.setScaleType(var2);
            var1.setTag(R$id.save_scale_type, (Object)null);
            if (var2 == ScaleType.MATRIX) {
               ImageView var3 = var1;
               var3.setImageMatrix((Matrix)var3.getTag(R$id.save_image_matrix));
               var1.setTag(R$id.save_image_matrix, (Object)null);
            }

            var1x.removeListener(this);
         }
      });
   }

   public void startAnimateTransform(ImageView var1) {
      ScaleType var2 = var1.getScaleType();
      var1.setTag(R$id.save_scale_type, var2);
      if (var2 == ScaleType.MATRIX) {
         var1.setTag(R$id.save_image_matrix, var1.getImageMatrix());
      } else {
         var1.setScaleType(ScaleType.MATRIX);
      }

      var1.setImageMatrix(MatrixUtils.IDENTITY_MATRIX);
   }
}
