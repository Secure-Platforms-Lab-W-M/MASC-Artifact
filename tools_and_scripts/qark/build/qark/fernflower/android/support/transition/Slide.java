package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;

public class Slide extends Visibility {
   private static final String PROPNAME_SCREEN_POSITION = "android:slide:screenPosition";
   private static final TimeInterpolator sAccelerate = new AccelerateInterpolator();
   private static final Slide.CalculateSlide sCalculateBottom = new Slide.CalculateSlideVertical() {
      public float getGoneY(ViewGroup var1, View var2) {
         return var2.getTranslationY() + (float)var1.getHeight();
      }
   };
   private static final Slide.CalculateSlide sCalculateEnd = new Slide.CalculateSlideHorizontal() {
      public float getGoneX(ViewGroup var1, View var2) {
         int var4 = ViewCompat.getLayoutDirection(var1);
         boolean var3 = true;
         if (var4 != 1) {
            var3 = false;
         }

         return var3 ? var2.getTranslationX() - (float)var1.getWidth() : var2.getTranslationX() + (float)var1.getWidth();
      }
   };
   private static final Slide.CalculateSlide sCalculateLeft = new Slide.CalculateSlideHorizontal() {
      public float getGoneX(ViewGroup var1, View var2) {
         return var2.getTranslationX() - (float)var1.getWidth();
      }
   };
   private static final Slide.CalculateSlide sCalculateRight = new Slide.CalculateSlideHorizontal() {
      public float getGoneX(ViewGroup var1, View var2) {
         return var2.getTranslationX() + (float)var1.getWidth();
      }
   };
   private static final Slide.CalculateSlide sCalculateStart = new Slide.CalculateSlideHorizontal() {
      public float getGoneX(ViewGroup var1, View var2) {
         int var4 = ViewCompat.getLayoutDirection(var1);
         boolean var3 = true;
         if (var4 != 1) {
            var3 = false;
         }

         return var3 ? var2.getTranslationX() + (float)var1.getWidth() : var2.getTranslationX() - (float)var1.getWidth();
      }
   };
   private static final Slide.CalculateSlide sCalculateTop = new Slide.CalculateSlideVertical() {
      public float getGoneY(ViewGroup var1, View var2) {
         return var2.getTranslationY() - (float)var1.getHeight();
      }
   };
   private static final TimeInterpolator sDecelerate = new DecelerateInterpolator();
   private Slide.CalculateSlide mSlideCalculator;
   private int mSlideEdge;

   public Slide() {
      this.mSlideCalculator = sCalculateBottom;
      this.mSlideEdge = 80;
      this.setSlideEdge(80);
   }

   public Slide(int var1) {
      this.mSlideCalculator = sCalculateBottom;
      this.mSlideEdge = 80;
      this.setSlideEdge(var1);
   }

   public Slide(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mSlideCalculator = sCalculateBottom;
      this.mSlideEdge = 80;
      TypedArray var4 = var1.obtainStyledAttributes(var2, Styleable.SLIDE);
      int var3 = TypedArrayUtils.getNamedInt(var4, (XmlPullParser)var2, "slideEdge", 0, 80);
      var4.recycle();
      this.setSlideEdge(var3);
   }

   private void captureValues(TransitionValues var1) {
      View var2 = var1.view;
      int[] var3 = new int[2];
      var2.getLocationOnScreen(var3);
      var1.values.put("android:slide:screenPosition", var3);
   }

   public void captureEndValues(@NonNull TransitionValues var1) {
      super.captureEndValues(var1);
      this.captureValues(var1);
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      super.captureStartValues(var1);
      this.captureValues(var1);
   }

   public int getSlideEdge() {
      return this.mSlideEdge;
   }

   public Animator onAppear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      if (var4 == null) {
         return null;
      } else {
         int[] var9 = (int[])var4.values.get("android:slide:screenPosition");
         float var5 = var2.getTranslationX();
         float var6 = var2.getTranslationY();
         float var7 = this.mSlideCalculator.getGoneX(var1, var2);
         float var8 = this.mSlideCalculator.getGoneY(var1, var2);
         return TranslationAnimationCreator.createAnimation(var2, var4, var9[0], var9[1], var7, var8, var5, var6, sDecelerate);
      }
   }

   public Animator onDisappear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      if (var3 == null) {
         return null;
      } else {
         int[] var9 = (int[])var3.values.get("android:slide:screenPosition");
         float var5 = var2.getTranslationX();
         float var6 = var2.getTranslationY();
         float var7 = this.mSlideCalculator.getGoneX(var1, var2);
         float var8 = this.mSlideCalculator.getGoneY(var1, var2);
         return TranslationAnimationCreator.createAnimation(var2, var3, var9[0], var9[1], var5, var6, var7, var8, sAccelerate);
      }
   }

   public void setSlideEdge(int var1) {
      if (var1 != 3) {
         if (var1 != 5) {
            if (var1 != 48) {
               if (var1 != 80) {
                  if (var1 != 8388611) {
                     if (var1 != 8388613) {
                        throw new IllegalArgumentException("Invalid slide direction");
                     }

                     this.mSlideCalculator = sCalculateEnd;
                  } else {
                     this.mSlideCalculator = sCalculateStart;
                  }
               } else {
                  this.mSlideCalculator = sCalculateBottom;
               }
            } else {
               this.mSlideCalculator = sCalculateTop;
            }
         } else {
            this.mSlideCalculator = sCalculateRight;
         }
      } else {
         this.mSlideCalculator = sCalculateLeft;
      }

      this.mSlideEdge = var1;
      SidePropagation var2 = new SidePropagation();
      var2.setSide(var1);
      this.setPropagation(var2);
   }

   private interface CalculateSlide {
      float getGoneX(ViewGroup var1, View var2);

      float getGoneY(ViewGroup var1, View var2);
   }

   private abstract static class CalculateSlideHorizontal implements Slide.CalculateSlide {
      private CalculateSlideHorizontal() {
      }

      // $FF: synthetic method
      CalculateSlideHorizontal(Object var1) {
         this();
      }

      public float getGoneY(ViewGroup var1, View var2) {
         return var2.getTranslationY();
      }
   }

   private abstract static class CalculateSlideVertical implements Slide.CalculateSlide {
      private CalculateSlideVertical() {
      }

      // $FF: synthetic method
      CalculateSlideVertical(Object var1) {
         this();
      }

      public float getGoneX(ViewGroup var1, View var2) {
         return var2.getTranslationX();
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface GravityFlag {
   }
}
