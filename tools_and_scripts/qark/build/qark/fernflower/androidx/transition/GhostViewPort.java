package androidx.transition;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.transition.R.id;

class GhostViewPort extends ViewGroup implements GhostView {
   private Matrix mMatrix;
   private final OnPreDrawListener mOnPreDrawListener = new OnPreDrawListener() {
      public boolean onPreDraw() {
         ViewCompat.postInvalidateOnAnimation(GhostViewPort.this);
         if (GhostViewPort.this.mStartParent != null && GhostViewPort.this.mStartView != null) {
            GhostViewPort.this.mStartParent.endViewTransition(GhostViewPort.this.mStartView);
            ViewCompat.postInvalidateOnAnimation(GhostViewPort.this.mStartParent);
            GhostViewPort.this.mStartParent = null;
            GhostViewPort.this.mStartView = null;
         }

         return true;
      }
   };
   int mReferences;
   ViewGroup mStartParent;
   View mStartView;
   final View mView;

   GhostViewPort(View var1) {
      super(var1.getContext());
      this.mView = var1;
      this.setWillNotDraw(false);
      this.setLayerType(2, (Paint)null);
   }

   static GhostViewPort addGhost(View var0, ViewGroup var1, Matrix var2) {
      if (var0.getParent() instanceof ViewGroup) {
         GhostViewHolder var6 = GhostViewHolder.getHolder(var1);
         GhostViewPort var7 = getGhostView(var0);
         byte var4 = 0;
         GhostViewPort var5 = var7;
         int var3 = var4;
         if (var7 != null) {
            GhostViewHolder var8 = (GhostViewHolder)var7.getParent();
            var5 = var7;
            var3 = var4;
            if (var8 != var6) {
               var3 = var7.mReferences;
               var8.removeView(var7);
               var5 = null;
            }
         }

         GhostViewPort var10;
         if (var5 == null) {
            Matrix var12 = var2;
            if (var2 == null) {
               var12 = new Matrix();
               calculateMatrix(var0, var1, var12);
            }

            GhostViewPort var11 = new GhostViewPort(var0);
            var11.setMatrix(var12);
            GhostViewHolder var9;
            if (var6 == null) {
               var9 = new GhostViewHolder(var1);
            } else {
               var6.popToOverlayTop();
               var9 = var6;
            }

            copySize(var1, var9);
            copySize(var1, var11);
            var9.addGhostView(var11);
            var11.mReferences = var3;
            var10 = var11;
         } else {
            var10 = var5;
            if (var2 != null) {
               var5.setMatrix(var2);
               var10 = var5;
            }
         }

         ++var10.mReferences;
         return var10;
      } else {
         throw new IllegalArgumentException("Ghosted views must be parented by a ViewGroup");
      }
   }

   static void calculateMatrix(View var0, ViewGroup var1, Matrix var2) {
      ViewGroup var3 = (ViewGroup)var0.getParent();
      var2.reset();
      ViewUtils.transformMatrixToGlobal(var3, var2);
      var2.preTranslate((float)(-var3.getScrollX()), (float)(-var3.getScrollY()));
      ViewUtils.transformMatrixToLocal(var1, var2);
   }

   static void copySize(View var0, View var1) {
      ViewUtils.setLeftTopRightBottom(var1, var1.getLeft(), var1.getTop(), var1.getLeft() + var0.getWidth(), var1.getTop() + var0.getHeight());
   }

   static GhostViewPort getGhostView(View var0) {
      return (GhostViewPort)var0.getTag(id.ghost_view);
   }

   static void removeGhost(View var0) {
      GhostViewPort var2 = getGhostView(var0);
      if (var2 != null) {
         int var1 = var2.mReferences - 1;
         var2.mReferences = var1;
         if (var1 <= 0) {
            ((GhostViewHolder)var2.getParent()).removeView(var2);
         }
      }

   }

   static void setGhostView(View var0, GhostViewPort var1) {
      var0.setTag(id.ghost_view, var1);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      setGhostView(this.mView, this);
      this.mView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
      ViewUtils.setTransitionVisibility(this.mView, 4);
      if (this.mView.getParent() != null) {
         ((View)this.mView.getParent()).invalidate();
      }

   }

   protected void onDetachedFromWindow() {
      this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
      ViewUtils.setTransitionVisibility(this.mView, 0);
      setGhostView(this.mView, (GhostViewPort)null);
      if (this.mView.getParent() != null) {
         ((View)this.mView.getParent()).invalidate();
      }

      super.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      CanvasUtils.enableZ(var1, true);
      var1.setMatrix(this.mMatrix);
      ViewUtils.setTransitionVisibility(this.mView, 0);
      this.mView.invalidate();
      ViewUtils.setTransitionVisibility(this.mView, 4);
      this.drawChild(var1, this.mView, this.getDrawingTime());
      CanvasUtils.enableZ(var1, false);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
   }

   public void reserveEndViewTransition(ViewGroup var1, View var2) {
      this.mStartParent = var1;
      this.mStartView = var2;
   }

   void setMatrix(Matrix var1) {
      this.mMatrix = var1;
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      if (getGhostView(this.mView) == this) {
         byte var2;
         if (var1 == 0) {
            var2 = 4;
         } else {
            var2 = 0;
         }

         ViewUtils.setTransitionVisibility(this.mView, var2);
      }

   }
}
