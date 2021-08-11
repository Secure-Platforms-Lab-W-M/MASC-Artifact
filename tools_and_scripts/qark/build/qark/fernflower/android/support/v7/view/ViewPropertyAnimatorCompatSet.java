package android.support.v7.view;

import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Iterator;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ViewPropertyAnimatorCompatSet {
   final ArrayList mAnimators = new ArrayList();
   private long mDuration = -1L;
   private Interpolator mInterpolator;
   private boolean mIsStarted;
   ViewPropertyAnimatorListener mListener;
   private final ViewPropertyAnimatorListenerAdapter mProxyListener = new ViewPropertyAnimatorListenerAdapter() {
      private int mProxyEndCount = 0;
      private boolean mProxyStarted = false;

      public void onAnimationEnd(View var1) {
         int var2 = this.mProxyEndCount + 1;
         this.mProxyEndCount = var2;
         if (var2 == ViewPropertyAnimatorCompatSet.this.mAnimators.size()) {
            if (ViewPropertyAnimatorCompatSet.this.mListener != null) {
               ViewPropertyAnimatorCompatSet.this.mListener.onAnimationEnd((View)null);
            }

            this.onEnd();
         }

      }

      public void onAnimationStart(View var1) {
         if (!this.mProxyStarted) {
            this.mProxyStarted = true;
            if (ViewPropertyAnimatorCompatSet.this.mListener != null) {
               ViewPropertyAnimatorCompatSet.this.mListener.onAnimationStart((View)null);
            }

         }
      }

      void onEnd() {
         this.mProxyEndCount = 0;
         this.mProxyStarted = false;
         ViewPropertyAnimatorCompatSet.this.onAnimationsEnded();
      }
   };

   public void cancel() {
      if (this.mIsStarted) {
         Iterator var1 = this.mAnimators.iterator();

         while(var1.hasNext()) {
            ((ViewPropertyAnimatorCompat)var1.next()).cancel();
         }

         this.mIsStarted = false;
      }
   }

   void onAnimationsEnded() {
      this.mIsStarted = false;
   }

   public ViewPropertyAnimatorCompatSet play(ViewPropertyAnimatorCompat var1) {
      if (!this.mIsStarted) {
         this.mAnimators.add(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompatSet playSequentially(ViewPropertyAnimatorCompat var1, ViewPropertyAnimatorCompat var2) {
      this.mAnimators.add(var1);
      var2.setStartDelay(var1.getDuration());
      this.mAnimators.add(var2);
      return this;
   }

   public ViewPropertyAnimatorCompatSet setDuration(long var1) {
      if (!this.mIsStarted) {
         this.mDuration = var1;
      }

      return this;
   }

   public ViewPropertyAnimatorCompatSet setInterpolator(Interpolator var1) {
      if (!this.mIsStarted) {
         this.mInterpolator = var1;
      }

      return this;
   }

   public ViewPropertyAnimatorCompatSet setListener(ViewPropertyAnimatorListener var1) {
      if (!this.mIsStarted) {
         this.mListener = var1;
      }

      return this;
   }

   public void start() {
      if (!this.mIsStarted) {
         ViewPropertyAnimatorCompat var4;
         for(Iterator var3 = this.mAnimators.iterator(); var3.hasNext(); var4.start()) {
            var4 = (ViewPropertyAnimatorCompat)var3.next();
            long var1 = this.mDuration;
            if (var1 >= 0L) {
               var4.setDuration(var1);
            }

            Interpolator var5 = this.mInterpolator;
            if (var5 != null) {
               var4.setInterpolator(var5);
            }

            if (this.mListener != null) {
               var4.setListener(this.mProxyListener);
            }
         }

         this.mIsStarted = true;
      }
   }
}
