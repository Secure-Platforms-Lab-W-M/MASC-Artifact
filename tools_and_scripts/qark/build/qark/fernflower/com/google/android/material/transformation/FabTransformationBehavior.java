package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.id;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.google.android.material.animation.ChildrenAlphaProperty;
import com.google.android.material.animation.DrawableAlphaProperty;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.MotionTiming;
import com.google.android.material.animation.Positioning;
import com.google.android.material.circularreveal.CircularRevealCompat;
import com.google.android.material.circularreveal.CircularRevealHelper;
import com.google.android.material.circularreveal.CircularRevealWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.math.MathUtils;
import java.util.ArrayList;
import java.util.List;

public abstract class FabTransformationBehavior extends ExpandableTransformationBehavior {
   private float dependencyOriginalTranslationX;
   private float dependencyOriginalTranslationY;
   private final int[] tmpArray = new int[2];
   private final Rect tmpRect = new Rect();
   private final RectF tmpRectF1 = new RectF();
   private final RectF tmpRectF2 = new RectF();

   public FabTransformationBehavior() {
   }

   public FabTransformationBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private ViewGroup calculateChildContentContainer(View var1) {
      View var2 = var1.findViewById(id.mtrl_child_content_container);
      if (var2 != null) {
         return this.toViewGroupOrNull(var2);
      } else {
         return !(var1 instanceof TransformationChildLayout) && !(var1 instanceof TransformationChildCard) ? this.toViewGroupOrNull(var1) : this.toViewGroupOrNull(((ViewGroup)var1).getChildAt(0));
      }
   }

   private void calculateChildVisibleBoundsAtEndOfExpansion(View var1, FabTransformationBehavior.FabTransformationSpec var2, MotionTiming var3, MotionTiming var4, float var5, float var6, float var7, float var8, RectF var9) {
      var5 = this.calculateValueOfAnimationAtEndOfExpansion(var2, var3, var5, var7);
      var6 = this.calculateValueOfAnimationAtEndOfExpansion(var2, var4, var6, var8);
      Rect var11 = this.tmpRect;
      var1.getWindowVisibleDisplayFrame(var11);
      RectF var10 = this.tmpRectF1;
      var10.set(var11);
      RectF var12 = this.tmpRectF2;
      this.calculateWindowBounds(var1, var12);
      var12.offset(var5, var6);
      var12.intersect(var10);
      var9.set(var12);
   }

   private void calculateDependencyWindowBounds(View var1, RectF var2) {
      this.calculateWindowBounds(var1, var2);
      var2.offset(this.dependencyOriginalTranslationX, this.dependencyOriginalTranslationY);
   }

   private Pair calculateMotionTiming(float var1, float var2, boolean var3, FabTransformationBehavior.FabTransformationSpec var4) {
      MotionTiming var5;
      MotionTiming var6;
      MotionTiming var7;
      if (var1 != 0.0F && var2 != 0.0F) {
         if ((!var3 || var2 >= 0.0F) && (var3 || var2 <= 0.0F)) {
            var5 = var4.timings.getTiming("translationXCurveDownwards");
            var6 = var4.timings.getTiming("translationYCurveDownwards");
            var7 = var5;
            var5 = var6;
         } else {
            var5 = var4.timings.getTiming("translationXCurveUpwards");
            var6 = var4.timings.getTiming("translationYCurveUpwards");
            var7 = var5;
            var5 = var6;
         }
      } else {
         var6 = var4.timings.getTiming("translationXLinear");
         var5 = var4.timings.getTiming("translationYLinear");
         var7 = var6;
      }

      return new Pair(var7, var5);
   }

   private float calculateRevealCenterX(View var1, View var2, Positioning var3) {
      RectF var4 = this.tmpRectF1;
      RectF var5 = this.tmpRectF2;
      this.calculateDependencyWindowBounds(var1, var4);
      this.calculateWindowBounds(var2, var5);
      var5.offset(-this.calculateTranslationX(var1, var2, var3), 0.0F);
      return var4.centerX() - var5.left;
   }

   private float calculateRevealCenterY(View var1, View var2, Positioning var3) {
      RectF var4 = this.tmpRectF1;
      RectF var5 = this.tmpRectF2;
      this.calculateDependencyWindowBounds(var1, var4);
      this.calculateWindowBounds(var2, var5);
      var5.offset(0.0F, -this.calculateTranslationY(var1, var2, var3));
      return var4.centerY() - var5.top;
   }

   private float calculateTranslationX(View var1, View var2, Positioning var3) {
      RectF var6 = this.tmpRectF1;
      RectF var7 = this.tmpRectF2;
      this.calculateDependencyWindowBounds(var1, var6);
      this.calculateWindowBounds(var2, var7);
      float var4 = 0.0F;
      int var5 = var3.gravity & 7;
      if (var5 != 1) {
         if (var5 != 3) {
            if (var5 == 5) {
               var4 = var7.right - var6.right;
            }
         } else {
            var4 = var7.left - var6.left;
         }
      } else {
         var4 = var7.centerX() - var6.centerX();
      }

      return var4 + var3.xAdjustment;
   }

   private float calculateTranslationY(View var1, View var2, Positioning var3) {
      RectF var6 = this.tmpRectF1;
      RectF var7 = this.tmpRectF2;
      this.calculateDependencyWindowBounds(var1, var6);
      this.calculateWindowBounds(var2, var7);
      float var4 = 0.0F;
      int var5 = var3.gravity & 112;
      if (var5 != 16) {
         if (var5 != 48) {
            if (var5 == 80) {
               var4 = var7.bottom - var6.bottom;
            }
         } else {
            var4 = var7.top - var6.top;
         }
      } else {
         var4 = var7.centerY() - var6.centerY();
      }

      return var4 + var3.yAdjustment;
   }

   private float calculateValueOfAnimationAtEndOfExpansion(FabTransformationBehavior.FabTransformationSpec var1, MotionTiming var2, float var3, float var4) {
      long var6 = var2.getDelay();
      long var8 = var2.getDuration();
      MotionTiming var10 = var1.timings.getTiming("expansion");
      float var5 = (float)(var10.getDelay() + var10.getDuration() + 17L - var6) / (float)var8;
      return AnimationUtils.lerp(var3, var4, var2.getInterpolator().getInterpolation(var5));
   }

   private void calculateWindowBounds(View var1, RectF var2) {
      var2.set(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
      int[] var3 = this.tmpArray;
      var1.getLocationInWindow(var3);
      var2.offsetTo((float)var3[0], (float)var3[1]);
      var2.offset((float)((int)(-var1.getTranslationX())), (float)((int)(-var1.getTranslationY())));
   }

   private void createChildrenFadeAnimation(View var1, View var2, boolean var3, boolean var4, FabTransformationBehavior.FabTransformationSpec var5, List var6, List var7) {
      if (var2 instanceof ViewGroup) {
         if (!(var2 instanceof CircularRevealWidget) || CircularRevealHelper.STRATEGY != 0) {
            ViewGroup var8 = this.calculateChildContentContainer(var2);
            if (var8 != null) {
               ObjectAnimator var9;
               if (var3) {
                  if (!var4) {
                     ChildrenAlphaProperty.CHILDREN_ALPHA.set(var8, 0.0F);
                  }

                  var9 = ObjectAnimator.ofFloat(var8, ChildrenAlphaProperty.CHILDREN_ALPHA, new float[]{1.0F});
               } else {
                  var9 = ObjectAnimator.ofFloat(var8, ChildrenAlphaProperty.CHILDREN_ALPHA, new float[]{0.0F});
               }

               var5.timings.getTiming("contentFade").apply(var9);
               var6.add(var9);
            }
         }
      }
   }

   private void createColorAnimation(View var1, View var2, boolean var3, boolean var4, FabTransformationBehavior.FabTransformationSpec var5, List var6, List var7) {
      if (var2 instanceof CircularRevealWidget) {
         CircularRevealWidget var10 = (CircularRevealWidget)var2;
         int var8 = this.getBackgroundTint(var1);
         ObjectAnimator var9;
         if (var3) {
            if (!var4) {
               var10.setCircularRevealScrimColor(var8);
            }

            var9 = ObjectAnimator.ofInt(var10, CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, new int[]{16777215 & var8});
         } else {
            var9 = ObjectAnimator.ofInt(var10, CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, new int[]{var8});
         }

         var9.setEvaluator(ArgbEvaluatorCompat.getInstance());
         var5.timings.getTiming("color").apply(var9);
         var6.add(var9);
      }
   }

   private void createDependencyTranslationAnimation(View var1, View var2, boolean var3, FabTransformationBehavior.FabTransformationSpec var4, List var5) {
      float var6 = this.calculateTranslationX(var1, var2, var4.positioning);
      float var7 = this.calculateTranslationY(var1, var2, var4.positioning);
      Pair var12 = this.calculateMotionTiming(var6, var7, var3, var4);
      MotionTiming var11 = (MotionTiming)var12.first;
      MotionTiming var13 = (MotionTiming)var12.second;
      Property var8 = View.TRANSLATION_X;
      if (!var3) {
         var6 = this.dependencyOriginalTranslationX;
      }

      ObjectAnimator var14 = ObjectAnimator.ofFloat(var1, var8, new float[]{var6});
      Property var9 = View.TRANSLATION_Y;
      if (var3) {
         var6 = var7;
      } else {
         var6 = this.dependencyOriginalTranslationY;
      }

      ObjectAnimator var10 = ObjectAnimator.ofFloat(var1, var9, new float[]{var6});
      var11.apply(var14);
      var13.apply(var10);
      var5.add(var14);
      var5.add(var10);
   }

   private void createElevationAnimation(View var1, View var2, boolean var3, boolean var4, FabTransformationBehavior.FabTransformationSpec var5, List var6, List var7) {
      float var8 = ViewCompat.getElevation(var2) - ViewCompat.getElevation(var1);
      ObjectAnimator var9;
      if (var3) {
         if (!var4) {
            var2.setTranslationZ(-var8);
         }

         var9 = ObjectAnimator.ofFloat(var2, View.TRANSLATION_Z, new float[]{0.0F});
      } else {
         var9 = ObjectAnimator.ofFloat(var2, View.TRANSLATION_Z, new float[]{-var8});
      }

      var5.timings.getTiming("elevation").apply(var9);
      var6.add(var9);
   }

   private void createExpansionAnimation(View var1, View var2, boolean var3, boolean var4, FabTransformationBehavior.FabTransformationSpec var5, float var6, float var7, List var8, List var9) {
      if (var2 instanceof CircularRevealWidget) {
         final CircularRevealWidget var15 = (CircularRevealWidget)var2;
         float var11 = this.calculateRevealCenterX(var1, var2, var5.positioning);
         float var12 = this.calculateRevealCenterY(var1, var2, var5.positioning);
         ((FloatingActionButton)var1).getContentRect(this.tmpRect);
         float var10 = (float)this.tmpRect.width() / 2.0F;
         MotionTiming var13 = var5.timings.getTiming("expansion");
         Animator var16;
         if (var3) {
            if (!var4) {
               var15.setRevealInfo(new CircularRevealWidget.RevealInfo(var11, var12, var10));
            }

            if (var4) {
               var10 = var15.getRevealInfo().radius;
            }

            var16 = CircularRevealCompat.createCircularReveal(var15, var11, var12, MathUtils.distanceToFurthestCorner(var11, var12, 0.0F, 0.0F, var6, var7));
            var16.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  CircularRevealWidget.RevealInfo var2 = var15.getRevealInfo();
                  var2.radius = Float.MAX_VALUE;
                  var15.setRevealInfo(var2);
               }
            });
            this.createPreFillRadialExpansion(var2, var13.getDelay(), (int)var11, (int)var12, var10, var8);
         } else {
            var6 = var15.getRevealInfo().radius;
            Animator var14 = CircularRevealCompat.createCircularReveal(var15, var11, var12, var10);
            this.createPreFillRadialExpansion(var2, var13.getDelay(), (int)var11, (int)var12, var6, var8);
            this.createPostFillRadialExpansion(var2, var13.getDelay(), var13.getDuration(), var5.timings.getTotalDuration(), (int)var11, (int)var12, var10, var8);
            var16 = var14;
         }

         var13.apply(var16);
         var8.add(var16);
         var9.add(CircularRevealCompat.createCircularRevealListener(var15));
      }
   }

   private void createIconFadeAnimation(View var1, final View var2, boolean var3, boolean var4, FabTransformationBehavior.FabTransformationSpec var5, List var6, List var7) {
      if (var2 instanceof CircularRevealWidget) {
         if (var1 instanceof ImageView) {
            final CircularRevealWidget var8 = (CircularRevealWidget)var2;
            final Drawable var9 = ((ImageView)var1).getDrawable();
            if (var9 != null) {
               var9.mutate();
               ObjectAnimator var10;
               if (var3) {
                  if (!var4) {
                     var9.setAlpha(255);
                  }

                  var10 = ObjectAnimator.ofInt(var9, DrawableAlphaProperty.DRAWABLE_ALPHA_COMPAT, new int[]{0});
               } else {
                  var10 = ObjectAnimator.ofInt(var9, DrawableAlphaProperty.DRAWABLE_ALPHA_COMPAT, new int[]{255});
               }

               var10.addUpdateListener(new AnimatorUpdateListener() {
                  public void onAnimationUpdate(ValueAnimator var1) {
                     var2.invalidate();
                  }
               });
               var5.timings.getTiming("iconFade").apply(var10);
               var6.add(var10);
               var7.add(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     var8.setCircularRevealOverlayDrawable((Drawable)null);
                  }

                  public void onAnimationStart(Animator var1) {
                     var8.setCircularRevealOverlayDrawable(var9);
                  }
               });
            }
         }
      }
   }

   private void createPostFillRadialExpansion(View var1, long var2, long var4, long var6, int var8, int var9, float var10, List var11) {
      if (VERSION.SDK_INT >= 21 && var2 + var4 < var6) {
         Animator var12 = ViewAnimationUtils.createCircularReveal(var1, var8, var9, var10, var10);
         var12.setStartDelay(var2 + var4);
         var12.setDuration(var6 - (var2 + var4));
         var11.add(var12);
      }

   }

   private void createPreFillRadialExpansion(View var1, long var2, int var4, int var5, float var6, List var7) {
      if (VERSION.SDK_INT >= 21 && var2 > 0L) {
         Animator var8 = ViewAnimationUtils.createCircularReveal(var1, var4, var5, var6, var6);
         var8.setStartDelay(0L);
         var8.setDuration(var2);
         var7.add(var8);
      }

   }

   private void createTranslationAnimation(View var1, View var2, boolean var3, boolean var4, FabTransformationBehavior.FabTransformationSpec var5, List var6, List var7, RectF var8) {
      float var9 = this.calculateTranslationX(var1, var2, var5.positioning);
      float var10 = this.calculateTranslationY(var1, var2, var5.positioning);
      Pair var13 = this.calculateMotionTiming(var9, var10, var3, var5);
      MotionTiming var11 = (MotionTiming)var13.first;
      MotionTiming var12 = (MotionTiming)var13.second;
      ObjectAnimator var14;
      ObjectAnimator var15;
      if (var3) {
         if (!var4) {
            var2.setTranslationX(-var9);
            var2.setTranslationY(-var10);
         }

         var14 = ObjectAnimator.ofFloat(var2, View.TRANSLATION_X, new float[]{0.0F});
         ObjectAnimator var16 = ObjectAnimator.ofFloat(var2, View.TRANSLATION_Y, new float[]{0.0F});
         this.calculateChildVisibleBoundsAtEndOfExpansion(var2, var5, var11, var12, -var9, -var10, 0.0F, 0.0F, var8);
         var15 = var16;
      } else {
         var14 = ObjectAnimator.ofFloat(var2, View.TRANSLATION_X, new float[]{-var9});
         var15 = ObjectAnimator.ofFloat(var2, View.TRANSLATION_Y, new float[]{-var10});
      }

      var11.apply(var14);
      var12.apply(var15);
      var6.add(var14);
      var6.add(var15);
   }

   private int getBackgroundTint(View var1) {
      ColorStateList var2 = ViewCompat.getBackgroundTintList(var1);
      return var2 != null ? var2.getColorForState(var1.getDrawableState(), var2.getDefaultColor()) : 0;
   }

   private ViewGroup toViewGroupOrNull(View var1) {
      return var1 instanceof ViewGroup ? (ViewGroup)var1 : null;
   }

   public boolean layoutDependsOn(CoordinatorLayout var1, View var2, View var3) {
      if (var2.getVisibility() != 8) {
         boolean var6 = var3 instanceof FloatingActionButton;
         boolean var5 = false;
         if (!var6) {
            return false;
         } else {
            int var4 = ((FloatingActionButton)var3).getExpandedComponentIdHint();
            if (var4 == 0 || var4 == var2.getId()) {
               var5 = true;
            }

            return var5;
         }
      } else {
         throw new IllegalStateException("This behavior cannot be attached to a GONE view. Set the view to INVISIBLE instead.");
      }
   }

   public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams var1) {
      if (var1.dodgeInsetEdges == 0) {
         var1.dodgeInsetEdges = 80;
      }

   }

   protected AnimatorSet onCreateExpandedStateChangeAnimation(final View var1, final View var2, final boolean var3, boolean var4) {
      FabTransformationBehavior.FabTransformationSpec var11 = this.onCreateMotionSpec(var2.getContext(), var3);
      if (var3) {
         this.dependencyOriginalTranslationX = var1.getTranslationX();
         this.dependencyOriginalTranslationY = var1.getTranslationY();
      }

      ArrayList var9 = new ArrayList();
      ArrayList var10 = new ArrayList();
      if (VERSION.SDK_INT >= 21) {
         this.createElevationAnimation(var1, var2, var3, var4, var11, var9, var10);
      }

      RectF var12 = this.tmpRectF1;
      this.createTranslationAnimation(var1, var2, var3, var4, var11, var9, var10, var12);
      float var5 = var12.width();
      float var6 = var12.height();
      this.createDependencyTranslationAnimation(var1, var2, var3, var11, var9);
      this.createIconFadeAnimation(var1, var2, var3, var4, var11, var9, var10);
      this.createExpansionAnimation(var1, var2, var3, var4, var11, var5, var6, var9, var10);
      this.createColorAnimation(var1, var2, var3, var4, var11, var9, var10);
      this.createChildrenFadeAnimation(var1, var2, var3, var4, var11, var9, var10);
      AnimatorSet var13 = new AnimatorSet();
      AnimatorSetCompat.playTogether(var13, var9);
      var13.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            if (!var3) {
               var2.setVisibility(4);
               var1.setAlpha(1.0F);
               var1.setVisibility(0);
            }

         }

         public void onAnimationStart(Animator var1x) {
            if (var3) {
               var2.setVisibility(0);
               var1.setAlpha(0.0F);
               var1.setVisibility(4);
            }

         }
      });
      int var7 = 0;

      for(int var8 = var10.size(); var7 < var8; ++var7) {
         var13.addListener((AnimatorListener)var10.get(var7));
      }

      return var13;
   }

   protected abstract FabTransformationBehavior.FabTransformationSpec onCreateMotionSpec(Context var1, boolean var2);

   protected static class FabTransformationSpec {
      public Positioning positioning;
      public MotionSpec timings;
   }
}
