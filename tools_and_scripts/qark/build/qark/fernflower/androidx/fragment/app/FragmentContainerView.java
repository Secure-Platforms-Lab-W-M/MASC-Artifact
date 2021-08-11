package androidx.fragment.app;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import androidx.fragment.R.styleable;
import java.util.ArrayList;

public final class FragmentContainerView extends FrameLayout {
   private ArrayList mDisappearingFragmentChildren;
   private boolean mDrawDisappearingViewsFirst = true;
   private ArrayList mTransitioningFragmentViews;

   public FragmentContainerView(Context var1) {
      super(var1);
   }

   public FragmentContainerView(Context var1, AttributeSet var2) {
      super(var1, var2);
      throw new UnsupportedOperationException("FragmentContainerView must be within a FragmentActivity to be instantiated from XML.");
   }

   public FragmentContainerView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      throw new UnsupportedOperationException("FragmentContainerView must be within a FragmentActivity to be instantiated from XML.");
   }

   FragmentContainerView(Context var1, AttributeSet var2, FragmentManager var3) {
      super(var1, var2);
      String var6 = var2.getClassAttribute();
      TypedArray var7 = var1.obtainStyledAttributes(var2, styleable.FragmentContainerView);
      String var5 = var6;
      if (var6 == null) {
         var5 = var7.getString(styleable.FragmentContainerView_android_name);
      }

      var6 = var7.getString(styleable.FragmentContainerView_android_tag);
      var7.recycle();
      int var4 = this.getId();
      Fragment var12 = var3.findFragmentById(var4);
      if (var5 != null && var12 == null) {
         if (var4 <= 0) {
            String var9;
            if (var6 != null) {
               StringBuilder var8 = new StringBuilder();
               var8.append(" with tag ");
               var8.append(var6);
               var9 = var8.toString();
            } else {
               var9 = "";
            }

            StringBuilder var10 = new StringBuilder();
            var10.append("FragmentContainerView must have an android:id to add Fragment ");
            var10.append(var5);
            var10.append(var9);
            throw new IllegalStateException(var10.toString());
         }

         Fragment var11 = var3.getFragmentFactory().instantiate(var1.getClassLoader(), var5);
         var11.onInflate((Context)var1, var2, (Bundle)null);
         var3.beginTransaction().setReorderingAllowed(true).add((ViewGroup)this, (Fragment)var11, var6).commitNowAllowingStateLoss();
      }

   }

   private void addDisappearingFragmentView(View var1) {
      if (var1.getAnimation() == null) {
         ArrayList var2 = this.mTransitioningFragmentViews;
         if (var2 == null || !var2.contains(var1)) {
            return;
         }
      }

      if (this.mDisappearingFragmentChildren == null) {
         this.mDisappearingFragmentChildren = new ArrayList();
      }

      this.mDisappearingFragmentChildren.add(var1);
   }

   public void addView(View var1, int var2, LayoutParams var3) {
      if (FragmentManager.getViewFragment(var1) != null) {
         super.addView(var1, var2, var3);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Views added to a FragmentContainerView must be associated with a Fragment. View ");
         var4.append(var1);
         var4.append(" is not associated with a Fragment.");
         throw new IllegalStateException(var4.toString());
      }
   }

   protected boolean addViewInLayout(View var1, int var2, LayoutParams var3, boolean var4) {
      if (FragmentManager.getViewFragment(var1) != null) {
         return super.addViewInLayout(var1, var2, var3, var4);
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("Views added to a FragmentContainerView must be associated with a Fragment. View ");
         var5.append(var1);
         var5.append(" is not associated with a Fragment.");
         throw new IllegalStateException(var5.toString());
      }
   }

   protected void dispatchDraw(Canvas var1) {
      if (this.mDrawDisappearingViewsFirst && this.mDisappearingFragmentChildren != null) {
         for(int var2 = 0; var2 < this.mDisappearingFragmentChildren.size(); ++var2) {
            super.drawChild(var1, (View)this.mDisappearingFragmentChildren.get(var2), this.getDrawingTime());
         }
      }

      super.dispatchDraw(var1);
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      if (this.mDrawDisappearingViewsFirst) {
         ArrayList var5 = this.mDisappearingFragmentChildren;
         if (var5 != null && var5.size() > 0 && this.mDisappearingFragmentChildren.contains(var2)) {
            return false;
         }
      }

      return super.drawChild(var1, var2, var3);
   }

   public void endViewTransition(View var1) {
      ArrayList var2 = this.mTransitioningFragmentViews;
      if (var2 != null) {
         var2.remove(var1);
         var2 = this.mDisappearingFragmentChildren;
         if (var2 != null && var2.remove(var1)) {
            this.mDrawDisappearingViewsFirst = true;
         }
      }

      super.endViewTransition(var1);
   }

   public WindowInsets onApplyWindowInsets(WindowInsets var1) {
      for(int var2 = 0; var2 < this.getChildCount(); ++var2) {
         this.getChildAt(var2).dispatchApplyWindowInsets(new WindowInsets(var1));
      }

      return var1;
   }

   public void removeAllViewsInLayout() {
      for(int var1 = this.getChildCount() - 1; var1 >= 0; --var1) {
         this.addDisappearingFragmentView(this.getChildAt(var1));
      }

      super.removeAllViewsInLayout();
   }

   protected void removeDetachedView(View var1, boolean var2) {
      if (var2) {
         this.addDisappearingFragmentView(var1);
      }

      super.removeDetachedView(var1, var2);
   }

   public void removeView(View var1) {
      this.addDisappearingFragmentView(var1);
      super.removeView(var1);
   }

   public void removeViewAt(int var1) {
      this.addDisappearingFragmentView(this.getChildAt(var1));
      super.removeViewAt(var1);
   }

   public void removeViewInLayout(View var1) {
      this.addDisappearingFragmentView(var1);
      super.removeViewInLayout(var1);
   }

   public void removeViews(int var1, int var2) {
      for(int var3 = var1; var3 < var1 + var2; ++var3) {
         this.addDisappearingFragmentView(this.getChildAt(var3));
      }

      super.removeViews(var1, var2);
   }

   public void removeViewsInLayout(int var1, int var2) {
      for(int var3 = var1; var3 < var1 + var2; ++var3) {
         this.addDisappearingFragmentView(this.getChildAt(var3));
      }

      super.removeViewsInLayout(var1, var2);
   }

   void setDrawDisappearingViewsLast(boolean var1) {
      this.mDrawDisappearingViewsFirst = var1;
   }

   public void setLayoutTransition(LayoutTransition var1) {
      if (VERSION.SDK_INT < 18) {
         super.setLayoutTransition(var1);
      } else {
         throw new UnsupportedOperationException("FragmentContainerView does not support Layout Transitions or animateLayoutChanges=\"true\".");
      }
   }

   public void startViewTransition(View var1) {
      if (var1.getParent() == this) {
         if (this.mTransitioningFragmentViews == null) {
            this.mTransitioningFragmentViews = new ArrayList();
         }

         this.mTransitioningFragmentViews.add(var1);
      }

      super.startViewTransition(var1);
   }
}
