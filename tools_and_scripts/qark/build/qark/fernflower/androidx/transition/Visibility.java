package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.R.id;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Visibility extends Transition {
   public static final int MODE_IN = 1;
   public static final int MODE_OUT = 2;
   private static final String PROPNAME_PARENT = "android:visibility:parent";
   private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
   static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
   private static final String[] sTransitionProperties = new String[]{"android:visibility:visibility", "android:visibility:parent"};
   private int mMode = 3;

   public Visibility() {
   }

   public Visibility(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var4 = var1.obtainStyledAttributes(var2, Styleable.VISIBILITY_TRANSITION);
      int var3 = TypedArrayUtils.getNamedInt(var4, (XmlResourceParser)var2, "transitionVisibilityMode", 0, 0);
      var4.recycle();
      if (var3 != 0) {
         this.setMode(var3);
      }

   }

   private void captureValues(TransitionValues var1) {
      int var2 = var1.view.getVisibility();
      var1.values.put("android:visibility:visibility", var2);
      var1.values.put("android:visibility:parent", var1.view.getParent());
      int[] var3 = new int[2];
      var1.view.getLocationOnScreen(var3);
      var1.values.put("android:visibility:screenLocation", var3);
   }

   private Visibility.VisibilityInfo getVisibilityChangeInfo(TransitionValues var1, TransitionValues var2) {
      Visibility.VisibilityInfo var3 = new Visibility.VisibilityInfo();
      var3.mVisibilityChange = false;
      var3.mFadeIn = false;
      if (var1 != null && var1.values.containsKey("android:visibility:visibility")) {
         var3.mStartVisibility = (Integer)var1.values.get("android:visibility:visibility");
         var3.mStartParent = (ViewGroup)var1.values.get("android:visibility:parent");
      } else {
         var3.mStartVisibility = -1;
         var3.mStartParent = null;
      }

      if (var2 != null && var2.values.containsKey("android:visibility:visibility")) {
         var3.mEndVisibility = (Integer)var2.values.get("android:visibility:visibility");
         var3.mEndParent = (ViewGroup)var2.values.get("android:visibility:parent");
      } else {
         var3.mEndVisibility = -1;
         var3.mEndParent = null;
      }

      if (var1 != null && var2 != null) {
         if (var3.mStartVisibility == var3.mEndVisibility && var3.mStartParent == var3.mEndParent) {
            return var3;
         }

         if (var3.mStartVisibility != var3.mEndVisibility) {
            if (var3.mStartVisibility == 0) {
               var3.mFadeIn = false;
               var3.mVisibilityChange = true;
               return var3;
            }

            if (var3.mEndVisibility == 0) {
               var3.mFadeIn = true;
               var3.mVisibilityChange = true;
               return var3;
            }
         } else {
            if (var3.mEndParent == null) {
               var3.mFadeIn = false;
               var3.mVisibilityChange = true;
               return var3;
            }

            if (var3.mStartParent == null) {
               var3.mFadeIn = true;
               var3.mVisibilityChange = true;
               return var3;
            }
         }
      } else {
         if (var1 == null && var3.mEndVisibility == 0) {
            var3.mFadeIn = true;
            var3.mVisibilityChange = true;
            return var3;
         }

         if (var2 == null && var3.mStartVisibility == 0) {
            var3.mFadeIn = false;
            var3.mVisibilityChange = true;
         }
      }

      return var3;
   }

   public void captureEndValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public void captureStartValues(TransitionValues var1) {
      this.captureValues(var1);
   }

   public Animator createAnimator(ViewGroup var1, TransitionValues var2, TransitionValues var3) {
      Visibility.VisibilityInfo var4 = this.getVisibilityChangeInfo(var2, var3);
      if (var4.mVisibilityChange && (var4.mStartParent != null || var4.mEndParent != null)) {
         return var4.mFadeIn ? this.onAppear(var1, var2, var4.mStartVisibility, var3, var4.mEndVisibility) : this.onDisappear(var1, var2, var4.mStartVisibility, var3, var4.mEndVisibility);
      } else {
         return null;
      }
   }

   public int getMode() {
      return this.mMode;
   }

   public String[] getTransitionProperties() {
      return sTransitionProperties;
   }

   public boolean isTransitionRequired(TransitionValues var1, TransitionValues var2) {
      boolean var4 = false;
      if (var1 == null && var2 == null) {
         return false;
      } else if (var1 != null && var2 != null && var2.values.containsKey("android:visibility:visibility") != var1.values.containsKey("android:visibility:visibility")) {
         return false;
      } else {
         Visibility.VisibilityInfo var5 = this.getVisibilityChangeInfo(var1, var2);
         boolean var3 = var4;
         if (var5.mVisibilityChange) {
            if (var5.mStartVisibility != 0) {
               var3 = var4;
               if (var5.mEndVisibility != 0) {
                  return var3;
               }
            }

            var3 = true;
         }

         return var3;
      }
   }

   public boolean isVisible(TransitionValues var1) {
      boolean var4 = false;
      if (var1 == null) {
         return false;
      } else {
         int var2 = (Integer)var1.values.get("android:visibility:visibility");
         View var5 = (View)var1.values.get("android:visibility:parent");
         boolean var3 = var4;
         if (var2 == 0) {
            var3 = var4;
            if (var5 != null) {
               var3 = true;
            }
         }

         return var3;
      }
   }

   public Animator onAppear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      return null;
   }

   public Animator onAppear(ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      if ((this.mMode & 1) == 1) {
         if (var4 == null) {
            return null;
         } else {
            if (var2 == null) {
               View var6 = (View)var4.view.getParent();
               if (this.getVisibilityChangeInfo(this.getMatchedTransitionValues(var6, false), this.getTransitionValues(var6, false)).mVisibilityChange) {
                  return null;
               }
            }

            return this.onAppear(var1, var4.view, var2, var4);
         }
      } else {
         return null;
      }
   }

   public Animator onDisappear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      return null;
   }

   public Animator onDisappear(final ViewGroup var1, TransitionValues var2, int var3, TransitionValues var4, int var5) {
      if ((this.mMode & 2) != 2) {
         return null;
      } else if (var2 == null) {
         return null;
      } else {
         final View var11 = var2.view;
         final View var8;
         if (var4 != null) {
            var8 = var4.view;
         } else {
            var8 = null;
         }

         View var10 = null;
         View var12 = null;
         View var9 = null;
         boolean var7 = false;
         View var13 = (View)var11.getTag(id.save_overlay_view);
         boolean var6;
         if (var13 != null) {
            var8 = var13;
            var6 = true;
         } else {
            boolean var17 = false;
            if (var8 != null && var8.getParent() != null) {
               if (var5 == 4) {
                  var9 = var8;
               } else if (var11 == var8) {
                  var9 = var8;
               } else {
                  var17 = true;
               }
            } else if (var8 != null) {
               var10 = var8;
            } else {
               var17 = true;
            }

            var8 = var10;
            var12 = var9;
            var6 = var7;
            if (var17) {
               if (var11.getParent() == null) {
                  var8 = var11;
                  var12 = var9;
                  var6 = var7;
               } else {
                  var8 = var10;
                  var12 = var9;
                  var6 = var7;
                  if (var11.getParent() instanceof View) {
                     var8 = (View)var11.getParent();
                     if (!this.getVisibilityChangeInfo(this.getTransitionValues(var8, true), this.getMatchedTransitionValues(var8, true)).mVisibilityChange) {
                        var8 = TransitionUtils.copyViewImage(var1, var11, var8);
                        var12 = var9;
                        var6 = var7;
                     } else {
                        var3 = var8.getId();
                        if (var8.getParent() == null) {
                           var8 = var10;
                           var12 = var9;
                           var6 = var7;
                           if (var3 != -1) {
                              var8 = var10;
                              var12 = var9;
                              var6 = var7;
                              if (var1.findViewById(var3) != null) {
                                 var8 = var10;
                                 var12 = var9;
                                 var6 = var7;
                                 if (this.mCanRemoveViews) {
                                    var8 = var11;
                                    var12 = var9;
                                    var6 = var7;
                                 }
                              }
                           }
                        } else {
                           var6 = var7;
                           var12 = var9;
                           var8 = var10;
                        }
                     }
                  }
               }
            }
         }

         if (var8 != null) {
            if (!var6) {
               int[] var18 = (int[])((int[])var2.values.get("android:visibility:screenLocation"));
               var3 = var18[0];
               var5 = var18[1];
               var18 = new int[2];
               var1.getLocationOnScreen(var18);
               var8.offsetLeftAndRight(var3 - var18[0] - var8.getLeft());
               var8.offsetTopAndBottom(var5 - var18[1] - var8.getTop());
               ViewGroupUtils.getOverlay(var1).add(var8);
            }

            Animator var16 = this.onDisappear(var1, var8, var2, var4);
            if (!var6) {
               if (var16 == null) {
                  ViewGroupUtils.getOverlay(var1).remove(var8);
                  return var16;
               }

               var11.setTag(id.save_overlay_view, var8);
               this.addListener(new TransitionListenerAdapter() {
                  public void onTransitionEnd(Transition var1x) {
                     var11.setTag(id.save_overlay_view, (Object)null);
                     ViewGroupUtils.getOverlay(var1).remove(var8);
                     var1x.removeListener(this);
                  }

                  public void onTransitionPause(Transition var1x) {
                     ViewGroupUtils.getOverlay(var1).remove(var8);
                  }

                  public void onTransitionResume(Transition var1x) {
                     if (var8.getParent() == null) {
                        ViewGroupUtils.getOverlay(var1).add(var8);
                     } else {
                        Visibility.this.cancel();
                     }
                  }
               });
            }

            return var16;
         } else if (var12 != null) {
            var3 = var12.getVisibility();
            ViewUtils.setTransitionVisibility(var12, 0);
            Animator var14 = this.onDisappear(var1, var12, var2, var4);
            if (var14 != null) {
               Visibility.DisappearListener var15 = new Visibility.DisappearListener(var12, var5, true);
               var14.addListener(var15);
               AnimatorUtils.addPauseListener(var14, var15);
               this.addListener(var15);
               return var14;
            } else {
               ViewUtils.setTransitionVisibility(var12, var3);
               return var14;
            }
         } else {
            return null;
         }
      }
   }

   public void setMode(int var1) {
      if ((var1 & -4) == 0) {
         this.mMode = var1;
      } else {
         throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
      }
   }

   private static class DisappearListener extends AnimatorListenerAdapter implements Transition.TransitionListener, AnimatorUtils.AnimatorPauseListenerCompat {
      boolean mCanceled = false;
      private final int mFinalVisibility;
      private boolean mLayoutSuppressed;
      private final ViewGroup mParent;
      private final boolean mSuppressLayout;
      private final View mView;

      DisappearListener(View var1, int var2, boolean var3) {
         this.mView = var1;
         this.mFinalVisibility = var2;
         this.mParent = (ViewGroup)var1.getParent();
         this.mSuppressLayout = var3;
         this.suppressLayout(true);
      }

      private void hideViewWhenNotCanceled() {
         if (!this.mCanceled) {
            ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
            ViewGroup var1 = this.mParent;
            if (var1 != null) {
               var1.invalidate();
            }
         }

         this.suppressLayout(false);
      }

      private void suppressLayout(boolean var1) {
         if (this.mSuppressLayout && this.mLayoutSuppressed != var1) {
            ViewGroup var2 = this.mParent;
            if (var2 != null) {
               this.mLayoutSuppressed = var1;
               ViewGroupUtils.suppressLayout(var2, var1);
            }
         }

      }

      public void onAnimationCancel(Animator var1) {
         this.mCanceled = true;
      }

      public void onAnimationEnd(Animator var1) {
         this.hideViewWhenNotCanceled();
      }

      public void onAnimationPause(Animator var1) {
         if (!this.mCanceled) {
            ViewUtils.setTransitionVisibility(this.mView, this.mFinalVisibility);
         }

      }

      public void onAnimationRepeat(Animator var1) {
      }

      public void onAnimationResume(Animator var1) {
         if (!this.mCanceled) {
            ViewUtils.setTransitionVisibility(this.mView, 0);
         }

      }

      public void onAnimationStart(Animator var1) {
      }

      public void onTransitionCancel(Transition var1) {
      }

      public void onTransitionEnd(Transition var1) {
         this.hideViewWhenNotCanceled();
         var1.removeListener(this);
      }

      public void onTransitionPause(Transition var1) {
         this.suppressLayout(false);
      }

      public void onTransitionResume(Transition var1) {
         this.suppressLayout(true);
      }

      public void onTransitionStart(Transition var1) {
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface Mode {
   }

   private static class VisibilityInfo {
      ViewGroup mEndParent;
      int mEndVisibility;
      boolean mFadeIn;
      ViewGroup mStartParent;
      int mStartVisibility;
      boolean mVisibilityChange;

      VisibilityInfo() {
      }
   }
}
