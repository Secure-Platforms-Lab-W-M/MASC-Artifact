package android.support.transition;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;

@SuppressLint({"ViewConstructor"})
@RequiresApi(14)
class GhostViewApi14 extends View implements GhostViewImpl {
   Matrix mCurrentMatrix;
   private int mDeltaX;
   private int mDeltaY;
   private final Matrix mMatrix = new Matrix();
   private final OnPreDrawListener mOnPreDrawListener = new OnPreDrawListener() {
      public boolean onPreDraw() {
         GhostViewApi14 var1 = GhostViewApi14.this;
         var1.mCurrentMatrix = var1.mView.getMatrix();
         ViewCompat.postInvalidateOnAnimation(GhostViewApi14.this);
         if (GhostViewApi14.this.mStartParent != null && GhostViewApi14.this.mStartView != null) {
            GhostViewApi14.this.mStartParent.endViewTransition(GhostViewApi14.this.mStartView);
            ViewCompat.postInvalidateOnAnimation(GhostViewApi14.this.mStartParent);
            var1 = GhostViewApi14.this;
            var1.mStartParent = null;
            var1.mStartView = null;
         }

         return true;
      }
   };
   int mReferences;
   ViewGroup mStartParent;
   View mStartView;
   final View mView;

   GhostViewApi14(View var1) {
      super(var1.getContext());
      this.mView = var1;
      this.setLayerType(2, (Paint)null);
   }

   static GhostViewApi14 getGhostView(@NonNull View var0) {
      return (GhostViewApi14)var0.getTag(R$id.ghost_view);
   }

   private static void setGhostView(@NonNull View var0, GhostViewApi14 var1) {
      var0.setTag(R$id.ghost_view, var1);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      setGhostView(this.mView, this);
      int[] var1 = new int[2];
      int[] var2 = new int[2];
      this.getLocationOnScreen(var1);
      this.mView.getLocationOnScreen(var2);
      var2[0] = (int)((float)var2[0] - this.mView.getTranslationX());
      var2[1] = (int)((float)var2[1] - this.mView.getTranslationY());
      this.mDeltaX = var2[0] - var1[0];
      this.mDeltaY = var2[1] - var1[1];
      this.mView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
      this.mView.setVisibility(4);
   }

   protected void onDetachedFromWindow() {
      this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
      this.mView.setVisibility(0);
      setGhostView(this.mView, (GhostViewApi14)null);
      super.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      this.mMatrix.set(this.mCurrentMatrix);
      this.mMatrix.postTranslate((float)this.mDeltaX, (float)this.mDeltaY);
      var1.setMatrix(this.mMatrix);
      this.mView.draw(var1);
   }

   public void reserveEndViewTransition(ViewGroup var1, View var2) {
      this.mStartParent = var1;
      this.mStartView = var2;
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      View var2 = this.mView;
      byte var3;
      if (var1 == 0) {
         var3 = 4;
      } else {
         var3 = 0;
      }

      var2.setVisibility(var3);
   }

   static class Creator implements GhostViewImpl.Creator {
      private static FrameLayout findFrameLayout(ViewGroup var0) {
         while(!(var0 instanceof FrameLayout)) {
            ViewParent var1 = var0.getParent();
            if (!(var1 instanceof ViewGroup)) {
               return null;
            }

            var0 = (ViewGroup)var1;
         }

         return (FrameLayout)var0;
      }

      public GhostViewImpl addGhost(View var1, ViewGroup var2, Matrix var3) {
         GhostViewApi14 var6 = GhostViewApi14.getGhostView(var1);
         GhostViewApi14 var4;
         if (var6 == null) {
            FrameLayout var5 = findFrameLayout(var2);
            if (var5 == null) {
               return null;
            }

            var4 = new GhostViewApi14(var1);
            var5.addView(var4);
         } else {
            var4 = var6;
         }

         ++var4.mReferences;
         return var4;
      }

      public void removeGhost(View var1) {
         GhostViewApi14 var3 = GhostViewApi14.getGhostView(var1);
         if (var3 != null) {
            --var3.mReferences;
            if (var3.mReferences <= 0) {
               ViewParent var2 = var3.getParent();
               if (var2 instanceof ViewGroup) {
                  ViewGroup var4 = (ViewGroup)var2;
                  var4.endViewTransition(var3);
                  var4.removeView(var3);
               }
            }
         }
      }
   }
}
