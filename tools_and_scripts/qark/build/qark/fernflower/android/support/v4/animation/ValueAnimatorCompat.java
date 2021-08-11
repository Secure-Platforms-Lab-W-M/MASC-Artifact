package android.support.v4.animation;

import android.support.annotation.RestrictTo;
import android.view.View;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public interface ValueAnimatorCompat {
   void addListener(AnimatorListenerCompat var1);

   void addUpdateListener(AnimatorUpdateListenerCompat var1);

   void cancel();

   float getAnimatedFraction();

   void setDuration(long var1);

   void setTarget(View var1);

   void start();
}
