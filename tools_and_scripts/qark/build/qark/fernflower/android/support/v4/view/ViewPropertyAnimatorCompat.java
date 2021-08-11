package android.support.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;

public final class ViewPropertyAnimatorCompat {
   static final int LISTENER_TAG_ID = 2113929216;
   private static final String TAG = "ViewAnimatorCompat";
   Runnable mEndAction = null;
   int mOldLayerType = -1;
   Runnable mStartAction = null;
   private WeakReference mView;

   ViewPropertyAnimatorCompat(View var1) {
      this.mView = new WeakReference(var1);
   }

   private void setListenerInternal(final View var1, final ViewPropertyAnimatorListener var2) {
      if (var2 != null) {
         var1.animate().setListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               var2.onAnimationCancel(var1);
            }

            public void onAnimationEnd(Animator var1x) {
               var2.onAnimationEnd(var1);
            }

            public void onAnimationStart(Animator var1x) {
               var2.onAnimationStart(var1);
            }
         });
      } else {
         var1.animate().setListener((AnimatorListener)null);
      }
   }

   public ViewPropertyAnimatorCompat alpha(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().alpha(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat alphaBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().alphaBy(var1);
      }

      return this;
   }

   public void cancel() {
      View var1 = (View)this.mView.get();
      if (var1 != null) {
         var1.animate().cancel();
      }

   }

   public long getDuration() {
      View var1 = (View)this.mView.get();
      return var1 != null ? var1.animate().getDuration() : 0L;
   }

   public Interpolator getInterpolator() {
      View var1 = (View)this.mView.get();
      return var1 != null && VERSION.SDK_INT >= 18 ? (Interpolator)var1.animate().getInterpolator() : null;
   }

   public long getStartDelay() {
      View var1 = (View)this.mView.get();
      return var1 != null ? var1.animate().getStartDelay() : 0L;
   }

   public ViewPropertyAnimatorCompat rotation(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().rotation(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat rotationBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().rotationBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat rotationX(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().rotationX(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat rotationXBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().rotationXBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat rotationY(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().rotationY(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat rotationYBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().rotationYBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat scaleX(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().scaleX(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat scaleXBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().scaleXBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat scaleY(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().scaleY(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat scaleYBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().scaleYBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setDuration(long var1) {
      View var3 = (View)this.mView.get();
      if (var3 != null) {
         var3.animate().setDuration(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setInterpolator(Interpolator var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().setInterpolator(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setListener(ViewPropertyAnimatorListener var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         if (VERSION.SDK_INT >= 16) {
            this.setListenerInternal(var2, var1);
            return this;
         }

         var2.setTag(2113929216, var1);
         this.setListenerInternal(var2, new ViewPropertyAnimatorCompat.ViewPropertyAnimatorListenerApi14(this));
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setStartDelay(long var1) {
      View var3 = (View)this.mView.get();
      if (var3 != null) {
         var3.animate().setStartDelay(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat setUpdateListener(final ViewPropertyAnimatorUpdateListener var1) {
      final View var3 = (View)this.mView.get();
      if (var3 != null && VERSION.SDK_INT >= 19) {
         AnimatorUpdateListener var2 = null;
         if (var1 != null) {
            var2 = new AnimatorUpdateListener() {
               public void onAnimationUpdate(ValueAnimator var1x) {
                  var1.onAnimationUpdate(var3);
               }
            };
         }

         var3.animate().setUpdateListener(var2);
      }

      return this;
   }

   public void start() {
      View var1 = (View)this.mView.get();
      if (var1 != null) {
         var1.animate().start();
      }

   }

   public ViewPropertyAnimatorCompat translationX(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().translationX(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat translationXBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().translationXBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat translationY(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().translationY(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat translationYBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().translationYBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat translationZ(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null && VERSION.SDK_INT >= 21) {
         var2.animate().translationZ(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat translationZBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null && VERSION.SDK_INT >= 21) {
         var2.animate().translationZBy(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat withEndAction(Runnable var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         if (VERSION.SDK_INT >= 16) {
            var2.animate().withEndAction(var1);
            return this;
         }

         this.setListenerInternal(var2, new ViewPropertyAnimatorCompat.ViewPropertyAnimatorListenerApi14(this));
         this.mEndAction = var1;
      }

      return this;
   }

   public ViewPropertyAnimatorCompat withLayer() {
      View var1 = (View)this.mView.get();
      if (var1 != null) {
         if (VERSION.SDK_INT >= 16) {
            var1.animate().withLayer();
            return this;
         }

         this.mOldLayerType = var1.getLayerType();
         this.setListenerInternal(var1, new ViewPropertyAnimatorCompat.ViewPropertyAnimatorListenerApi14(this));
      }

      return this;
   }

   public ViewPropertyAnimatorCompat withStartAction(Runnable var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         if (VERSION.SDK_INT >= 16) {
            var2.animate().withStartAction(var1);
            return this;
         }

         this.setListenerInternal(var2, new ViewPropertyAnimatorCompat.ViewPropertyAnimatorListenerApi14(this));
         this.mStartAction = var1;
      }

      return this;
   }

   // $FF: renamed from: x (float) android.support.v4.view.ViewPropertyAnimatorCompat
   public ViewPropertyAnimatorCompat method_4(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().x(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat xBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().xBy(var1);
      }

      return this;
   }

   // $FF: renamed from: y (float) android.support.v4.view.ViewPropertyAnimatorCompat
   public ViewPropertyAnimatorCompat method_5(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().y(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat yBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null) {
         var2.animate().yBy(var1);
      }

      return this;
   }

   // $FF: renamed from: z (float) android.support.v4.view.ViewPropertyAnimatorCompat
   public ViewPropertyAnimatorCompat method_6(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null && VERSION.SDK_INT >= 21) {
         var2.animate().z(var1);
      }

      return this;
   }

   public ViewPropertyAnimatorCompat zBy(float var1) {
      View var2 = (View)this.mView.get();
      if (var2 != null && VERSION.SDK_INT >= 21) {
         var2.animate().zBy(var1);
      }

      return this;
   }

   static class ViewPropertyAnimatorListenerApi14 implements ViewPropertyAnimatorListener {
      boolean mAnimEndCalled;
      ViewPropertyAnimatorCompat mVpa;

      ViewPropertyAnimatorListenerApi14(ViewPropertyAnimatorCompat var1) {
         this.mVpa = var1;
      }

      public void onAnimationCancel(View var1) {
         Object var3 = var1.getTag(2113929216);
         ViewPropertyAnimatorListener var2 = null;
         if (var3 instanceof ViewPropertyAnimatorListener) {
            var2 = (ViewPropertyAnimatorListener)var3;
         }

         if (var2 != null) {
            var2.onAnimationCancel(var1);
         }

      }

      public void onAnimationEnd(View var1) {
         if (this.mVpa.mOldLayerType > -1) {
            var1.setLayerType(this.mVpa.mOldLayerType, (Paint)null);
            this.mVpa.mOldLayerType = -1;
         }

         if (VERSION.SDK_INT >= 16 || !this.mAnimEndCalled) {
            if (this.mVpa.mEndAction != null) {
               Runnable var2 = this.mVpa.mEndAction;
               this.mVpa.mEndAction = null;
               var2.run();
            }

            Object var3 = var1.getTag(2113929216);
            ViewPropertyAnimatorListener var4 = null;
            if (var3 instanceof ViewPropertyAnimatorListener) {
               var4 = (ViewPropertyAnimatorListener)var3;
            }

            if (var4 != null) {
               var4.onAnimationEnd(var1);
            }

            this.mAnimEndCalled = true;
         }

      }

      public void onAnimationStart(View var1) {
         this.mAnimEndCalled = false;
         if (this.mVpa.mOldLayerType > -1) {
            var1.setLayerType(2, (Paint)null);
         }

         if (this.mVpa.mStartAction != null) {
            Runnable var2 = this.mVpa.mStartAction;
            this.mVpa.mStartAction = null;
            var2.run();
         }

         Object var3 = var1.getTag(2113929216);
         ViewPropertyAnimatorListener var4 = null;
         if (var3 instanceof ViewPropertyAnimatorListener) {
            var4 = (ViewPropertyAnimatorListener)var3;
         }

         if (var4 != null) {
            var4.onAnimationStart(var1);
         }

      }
   }
}
