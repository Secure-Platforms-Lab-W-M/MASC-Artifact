package android.support.transition;

import android.animation.Animator;
import android.graphics.Matrix;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

@RequiresApi(14)
interface ImageViewUtilsImpl {
   void animateTransform(ImageView var1, Matrix var2);

   void reserveEndAnimateTransform(ImageView var1, Animator var2);

   void startAnimateTransform(ImageView var1);
}
