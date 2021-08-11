package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(14)
@RequiresApi(14)
class FadePort extends VisibilityPort {
   private static boolean DBG = false;
   // $FF: renamed from: IN int
   public static final int field_11 = 1;
   private static final String LOG_TAG = "Fade";
   public static final int OUT = 2;
   private static final String PROPNAME_SCREEN_X = "android:fade:screenX";
   private static final String PROPNAME_SCREEN_Y = "android:fade:screenY";
   private int mFadingMode;

   public FadePort() {
      this(3);
   }

   public FadePort(int var1) {
      this.mFadingMode = var1;
   }

   private void captureValues(TransitionValues var1) {
      int[] var2 = new int[2];
      var1.view.getLocationOnScreen(var2);
      var1.values.put("android:fade:screenX", var2[0]);
      var1.values.put("android:fade:screenY", var2[1]);
   }

   private Animator createAnimation(View var1, float var2, float var3, AnimatorListenerAdapter var4) {
      ObjectAnimator var5 = null;
      ObjectAnimator var6;
      if (var2 == var3) {
         var6 = var5;
         if (var4 != null) {
            var4.onAnimationEnd((Animator)null);
            var6 = var5;
         }
      } else {
         var5 = ObjectAnimator.ofFloat(var1, "alpha", new float[]{var2, var3});
         if (DBG) {
            Log.d("Fade", "Created animator " + var5);
         }

         var6 = var5;
         if (var4 != null) {
            var5.addListener(var4);
            return var5;
         }
      }

      return var6;
   }

   public void captureStartValues(TransitionValues var1) {
      super.captureStartValues(var1);
      this.captureValues(var1);
   }

   public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      if ((this.mFadingMode & 1) == 1 && var4 != null) {
         final View var7 = var4.view;
         if (DBG) {
            View var6;
            if (var2 != null) {
               var6 = var2.view;
            } else {
               var6 = null;
            }

            Log.d("Fade", "Fade.onAppear: startView, startVis, endView, endVis = " + var6 + ", " + var3 + ", " + var7 + ", " + var5);
         }

         var7.setAlpha(0.0F);
         this.addListener(new TransitionPort.TransitionListenerAdapter() {
            boolean mCanceled = false;
            float mPausedAlpha;

            public void onTransitionCancel(TransitionPort var1) {
               var7.setAlpha(1.0F);
               this.mCanceled = true;
            }

            public void onTransitionEnd(TransitionPort var1) {
               if (!this.mCanceled) {
                  var7.setAlpha(1.0F);
               }

            }

            public void onTransitionPause(TransitionPort var1) {
               this.mPausedAlpha = var7.getAlpha();
               var7.setAlpha(1.0F);
            }

            public void onTransitionResume(TransitionPort var1) {
               var7.setAlpha(this.mPausedAlpha);
            }
         });
         return this.createAnimation(var7, 0.0F, 1.0F, (AnimatorListenerAdapter)null);
      } else {
         return null;
      }
   }

   public Animator onDisappear(final ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, final int var5) {
      if ((this.mFadingMode & 2) != 2) {
         return null;
      } else {
         Object var11 = null;
         View var7;
         if (var2 != null) {
            var7 = var2.view;
         } else {
            var7 = null;
         }

         final View var14;
         if (var4 != null) {
            var14 = var4.view;
         } else {
            var14 = null;
         }

         if (DBG) {
            Log.d("Fade", "Fade.onDisappear: startView, startVis, endView, endVis = " + var7 + ", " + var3 + ", " + var14 + ", " + var5);
         }

         Object var12 = null;
         Object var10 = null;
         final View var8;
         final View var9;
         if (var14 != null && var14.getParent() != null) {
            if (var5 == 4) {
               var9 = var14;
               var8 = (View)var12;
            } else if (var7 == var14) {
               var9 = var14;
               var8 = (View)var12;
            } else {
               var14 = var7;
               var8 = var7;
               var9 = (View)var10;
            }
         } else if (var14 != null) {
            var8 = var14;
            var9 = (View)var10;
         } else {
            var8 = (View)var12;
            var14 = (View)var11;
            var9 = (View)var10;
            if (var7 != null) {
               if (var7.getParent() == null) {
                  var8 = var7;
                  var14 = var7;
                  var9 = (View)var10;
               } else {
                  var8 = (View)var12;
                  var14 = (View)var11;
                  var9 = (View)var10;
                  if (var7.getParent() instanceof View) {
                     var8 = (View)var12;
                     var14 = (View)var11;
                     var9 = (View)var10;
                     if (var7.getParent().getParent() == null) {
                        var3 = ((View)var7.getParent()).getId();
                        var8 = (View)var12;
                        var14 = (View)var11;
                        var9 = (View)var10;
                        if (var3 != -1) {
                           var8 = (View)var12;
                           var14 = (View)var11;
                           var9 = (View)var10;
                           if (var1.findViewById(var3) != null) {
                              var8 = (View)var12;
                              var14 = (View)var11;
                              var9 = (View)var10;
                              if (this.mCanRemoveViews) {
                                 var8 = var7;
                                 var14 = var7;
                                 var9 = (View)var10;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         if (var8 != null) {
            var3 = (Integer)var2.values.get("android:fade:screenX");
            int var6 = (Integer)var2.values.get("android:fade:screenY");
            int[] var13 = new int[2];
            var1.getLocationOnScreen(var13);
            ViewCompat.offsetLeftAndRight(var8, var3 - var13[0] - var8.getLeft());
            ViewCompat.offsetTopAndBottom(var8, var6 - var13[1] - var8.getTop());
            ViewGroupOverlay.createFrom(var1).add(var8);
            return this.createAnimation(var14, 1.0F, 0.0F, new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  var14.setAlpha(1.0F);
                  if (var9 != null) {
                     var9.setVisibility(var5);
                  }

                  if (var8 != null) {
                     ViewGroupOverlay.createFrom(var1).remove(var8);
                  }

               }
            });
         } else if (var9 != null) {
            var9.setVisibility(0);
            return this.createAnimation(var14, 1.0F, 0.0F, new AnimatorListenerAdapter() {
               boolean mCanceled = false;
               float mPausedAlpha = -1.0F;

               public void onAnimationCancel(Animator var1x) {
                  this.mCanceled = true;
                  if (this.mPausedAlpha >= 0.0F) {
                     var14.setAlpha(this.mPausedAlpha);
                  }

               }

               public void onAnimationEnd(Animator var1x) {
                  if (!this.mCanceled) {
                     var14.setAlpha(1.0F);
                  }

                  if (var9 != null && !this.mCanceled) {
                     var9.setVisibility(var5);
                  }

                  if (var8 != null) {
                     ViewGroupOverlay.createFrom(var1).add(var8);
                  }

               }
            });
         } else {
            return null;
         }
      }
   }
}
