package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class Explode extends Visibility {
   private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
   private static final TimeInterpolator sAccelerate = new AccelerateInterpolator();
   private static final TimeInterpolator sDecelerate = new DecelerateInterpolator();
   private int[] mTempLoc = new int[2];

   public Explode() {
      this.setPropagation(new CircularPropagation());
   }

   public Explode(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setPropagation(new CircularPropagation());
   }

   private static float calculateDistance(float var0, float var1) {
      return (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
   }

   private static float calculateMaxDistance(View var0, int var1, int var2) {
      var1 = Math.max(var1, var0.getWidth() - var1);
      var2 = Math.max(var2, var0.getHeight() - var2);
      return calculateDistance((float)var1, (float)var2);
   }

   private void calculateOut(View var1, Rect var2, int[] var3) {
      var1.getLocationOnScreen(this.mTempLoc);
      int[] var13 = this.mTempLoc;
      int var9 = var13[0];
      int var10 = var13[1];
      Rect var14 = this.getEpicenter();
      int var7;
      int var8;
      if (var14 == null) {
         var7 = var1.getWidth() / 2 + var9 + Math.round(var1.getTranslationX());
         var8 = var1.getHeight() / 2 + var10 + Math.round(var1.getTranslationY());
      } else {
         var7 = var14.centerX();
         var8 = var14.centerY();
      }

      int var11 = var2.centerX();
      int var12 = var2.centerY();
      float var5 = (float)(var11 - var7);
      float var4 = (float)(var12 - var8);
      if (var5 == 0.0F && var4 == 0.0F) {
         var5 = (float)(Math.random() * 2.0D) - 1.0F;
         var4 = (float)(Math.random() * 2.0D) - 1.0F;
      }

      float var6 = calculateDistance(var5, var4);
      var5 /= var6;
      var4 /= var6;
      var6 = calculateMaxDistance(var1, var7 - var9, var8 - var10);
      var3[0] = Math.round(var6 * var5);
      var3[1] = Math.round(var6 * var4);
   }

   private void captureValues(TransitionValues var1) {
      View var6 = var1.view;
      var6.getLocationOnScreen(this.mTempLoc);
      int[] var7 = this.mTempLoc;
      int var2 = var7[0];
      int var3 = var7[1];
      int var4 = var6.getWidth();
      int var5 = var6.getHeight();
      var1.values.put("android:explode:screenBounds", new Rect(var2, var3, var4 + var2, var5 + var3));
   }

   public void captureEndValues(@NonNull TransitionValues var1) {
      super.captureEndValues(var1);
      this.captureValues(var1);
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      super.captureStartValues(var1);
      this.captureValues(var1);
   }

   public Animator onAppear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      if (var4 == null) {
         return null;
      } else {
         Rect var10 = (Rect)var4.values.get("android:explode:screenBounds");
         float var5 = var2.getTranslationX();
         float var6 = var2.getTranslationY();
         this.calculateOut(var1, var10, this.mTempLoc);
         int[] var9 = this.mTempLoc;
         float var7 = (float)var9[0];
         float var8 = (float)var9[1];
         return TranslationAnimationCreator.createAnimation(var2, var4, var10.left, var10.top, var5 + var7, var6 + var8, var5, var6, sDecelerate);
      }
   }

   public Animator onDisappear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      if (var3 == null) {
         return null;
      } else {
         Rect var13 = (Rect)var3.values.get("android:explode:screenBounds");
         int var9 = var13.left;
         int var10 = var13.top;
         float var7 = var2.getTranslationX();
         float var8 = var2.getTranslationY();
         float var5 = var7;
         float var6 = var8;
         int[] var11 = (int[])var3.view.getTag(R$id.transition_position);
         if (var11 != null) {
            var5 = var7 + (float)(var11[0] - var13.left);
            var6 = var8 + (float)(var11[1] - var13.top);
            var13.offsetTo(var11[0], var11[1]);
         }

         this.calculateOut(var1, var13, this.mTempLoc);
         int[] var12 = this.mTempLoc;
         return TranslationAnimationCreator.createAnimation(var2, var3, var9, var10, var7, var8, var5 + (float)var12[0], var6 + (float)var12[1], sAccelerate);
      }
   }
}
