package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.R.anim;
import androidx.fragment.R.id;

class FragmentAnim {
   private FragmentAnim() {
   }

   static void animateRemoveFragment(final Fragment var0, FragmentAnim.AnimationOrAnimator var1, final FragmentTransition.Callback var2) {
      final View var5 = var0.mView;
      final ViewGroup var3 = var0.mContainer;
      var3.startViewTransition(var5);
      final CancellationSignal var4 = new CancellationSignal();
      var4.setOnCancelListener(new CancellationSignal.OnCancelListener() {
         public void onCancel() {
            if (var0.getAnimatingAway() != null) {
               View var1 = var0.getAnimatingAway();
               var0.setAnimatingAway((View)null);
               var1.clearAnimation();
            }

            var0.setAnimator((Animator)null);
         }
      });
      var2.onStart(var0, var4);
      if (var1.animation != null) {
         FragmentAnim.EndViewTransitionAnimation var7 = new FragmentAnim.EndViewTransitionAnimation(var1.animation, var3, var5);
         var0.setAnimatingAway(var0.mView);
         var7.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation var1) {
               var3.post(new Runnable() {
                  public void run() {
                     if (var0.getAnimatingAway() != null) {
                        var0.setAnimatingAway((View)null);
                        var2.onComplete(var0, var4);
                     }

                  }
               });
            }

            public void onAnimationRepeat(Animation var1) {
            }

            public void onAnimationStart(Animation var1) {
            }
         });
         var0.mView.startAnimation(var7);
      } else {
         Animator var6 = var1.animator;
         var0.setAnimator(var1.animator);
         var6.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               var3.endViewTransition(var5);
               var1 = var0.getAnimator();
               var0.setAnimator((Animator)null);
               if (var1 != null && var3.indexOfChild(var5) < 0) {
                  var2.onComplete(var0, var4);
               }

            }
         });
         var6.setTarget(var0.mView);
         var6.start();
      }
   }

   static FragmentAnim.AnimationOrAnimator loadAnimation(Context var0, FragmentContainer var1, Fragment var2, boolean var3) {
      int var6 = var2.getNextTransition();
      int var7 = var2.getNextAnim();
      var2.setNextAnim(0);
      View var16 = var1.onFindViewById(var2.mContainerId);
      if (var16 != null && var16.getTag(id.visible_removing_fragment_view_tag) != null) {
         var16.setTag(id.visible_removing_fragment_view_tag, (Object)null);
      }

      if (var2.mContainer != null && var2.mContainer.getLayoutTransition() != null) {
         return null;
      } else {
         Animation var17 = var2.onCreateAnimation(var6, var3, var7);
         if (var17 != null) {
            return new FragmentAnim.AnimationOrAnimator(var17);
         } else {
            Animator var18 = var2.onCreateAnimator(var6, var3, var7);
            if (var18 != null) {
               return new FragmentAnim.AnimationOrAnimator(var18);
            } else {
               NotFoundException var10000;
               label96: {
                  if (var7 != 0) {
                     boolean var8 = "anim".equals(var0.getResources().getResourceTypeName(var7));
                     boolean var5 = false;
                     boolean var4 = var5;
                     boolean var10001;
                     FragmentAnim.AnimationOrAnimator var19;
                     if (var8) {
                        label83: {
                           label97: {
                              try {
                                 var17 = AnimationUtils.loadAnimation(var0, var7);
                              } catch (NotFoundException var13) {
                                 var10000 = var13;
                                 var10001 = false;
                                 break label96;
                              } catch (RuntimeException var14) {
                                 var10001 = false;
                                 break label97;
                              }

                              if (var17 == null) {
                                 var4 = true;
                                 break label83;
                              }

                              try {
                                 var19 = new FragmentAnim.AnimationOrAnimator(var17);
                                 return var19;
                              } catch (NotFoundException var11) {
                                 var10000 = var11;
                                 var10001 = false;
                                 break label96;
                              } catch (RuntimeException var12) {
                                 var10001 = false;
                              }
                           }

                           var4 = var5;
                        }
                     }

                     if (!var4) {
                        label98: {
                           RuntimeException var22;
                           label99: {
                              try {
                                 var18 = AnimatorInflater.loadAnimator(var0, var7);
                              } catch (RuntimeException var10) {
                                 var22 = var10;
                                 var10001 = false;
                                 break label99;
                              }

                              if (var18 == null) {
                                 break label98;
                              }

                              try {
                                 var19 = new FragmentAnim.AnimationOrAnimator(var18);
                                 return var19;
                              } catch (RuntimeException var9) {
                                 var22 = var9;
                                 var10001 = false;
                              }
                           }

                           RuntimeException var20 = var22;
                           if (var8) {
                              throw var20;
                           }

                           var17 = AnimationUtils.loadAnimation(var0, var7);
                           if (var17 != null) {
                              return new FragmentAnim.AnimationOrAnimator(var17);
                           }
                        }
                     }
                  }

                  if (var6 == 0) {
                     return null;
                  }

                  int var21 = transitToAnimResourceId(var6, var3);
                  if (var21 < 0) {
                     return null;
                  }

                  return new FragmentAnim.AnimationOrAnimator(AnimationUtils.loadAnimation(var0, var21));
               }

               NotFoundException var15 = var10000;
               throw var15;
            }
         }
      }
   }

   private static int transitToAnimResourceId(int var0, boolean var1) {
      if (var0 != 4097) {
         if (var0 != 4099) {
            if (var0 != 8194) {
               return -1;
            } else {
               if (var1) {
                  var0 = anim.fragment_close_enter;
               } else {
                  var0 = anim.fragment_close_exit;
               }

               return var0;
            }
         } else {
            if (var1) {
               var0 = anim.fragment_fade_enter;
            } else {
               var0 = anim.fragment_fade_exit;
            }

            return var0;
         }
      } else {
         if (var1) {
            var0 = anim.fragment_open_enter;
         } else {
            var0 = anim.fragment_open_exit;
         }

         return var0;
      }
   }

   static class AnimationOrAnimator {
      public final Animation animation;
      public final Animator animator;

      AnimationOrAnimator(Animator var1) {
         this.animation = null;
         this.animator = var1;
         if (var1 == null) {
            throw new IllegalStateException("Animator cannot be null");
         }
      }

      AnimationOrAnimator(Animation var1) {
         this.animation = var1;
         this.animator = null;
         if (var1 == null) {
            throw new IllegalStateException("Animation cannot be null");
         }
      }
   }

   private static class EndViewTransitionAnimation extends AnimationSet implements Runnable {
      private boolean mAnimating = true;
      private final View mChild;
      private boolean mEnded;
      private final ViewGroup mParent;
      private boolean mTransitionEnded;

      EndViewTransitionAnimation(Animation var1, ViewGroup var2, View var3) {
         super(false);
         this.mParent = var2;
         this.mChild = var3;
         this.addAnimation(var1);
         this.mParent.post(this);
      }

      public boolean getTransformation(long var1, Transformation var3) {
         this.mAnimating = true;
         if (this.mEnded) {
            return true ^ this.mTransitionEnded;
         } else {
            if (!super.getTransformation(var1, var3)) {
               this.mEnded = true;
               OneShotPreDrawListener.add(this.mParent, this);
            }

            return true;
         }
      }

      public boolean getTransformation(long var1, Transformation var3, float var4) {
         this.mAnimating = true;
         if (this.mEnded) {
            return true ^ this.mTransitionEnded;
         } else {
            if (!super.getTransformation(var1, var3, var4)) {
               this.mEnded = true;
               OneShotPreDrawListener.add(this.mParent, this);
            }

            return true;
         }
      }

      public void run() {
         if (!this.mEnded && this.mAnimating) {
            this.mAnimating = false;
            this.mParent.post(this);
         } else {
            this.mParent.endViewTransition(this.mChild);
            this.mTransitionEnded = true;
         }
      }
   }
}
