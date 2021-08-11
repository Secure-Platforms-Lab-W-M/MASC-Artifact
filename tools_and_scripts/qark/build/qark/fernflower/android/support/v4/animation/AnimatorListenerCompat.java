package android.support.v4.animation;

import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public interface AnimatorListenerCompat {
   void onAnimationCancel(ValueAnimatorCompat var1);

   void onAnimationEnd(ValueAnimatorCompat var1);

   void onAnimationRepeat(ValueAnimatorCompat var1);

   void onAnimationStart(ValueAnimatorCompat var1);
}
