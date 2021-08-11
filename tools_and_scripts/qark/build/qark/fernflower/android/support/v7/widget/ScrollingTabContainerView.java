package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R$attr;
import android.support.v7.view.ActionBarPolicy;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ScrollingTabContainerView extends HorizontalScrollView implements OnItemSelectedListener {
   private static final int FADE_DURATION = 200;
   private static final String TAG = "ScrollingTabContainerView";
   private static final Interpolator sAlphaInterpolator = new DecelerateInterpolator();
   private boolean mAllowCollapse;
   private int mContentHeight;
   int mMaxTabWidth;
   private int mSelectedTabIndex;
   int mStackedTabMaxWidth;
   private ScrollingTabContainerView.TabClickListener mTabClickListener;
   LinearLayoutCompat mTabLayout;
   Runnable mTabSelector;
   private Spinner mTabSpinner;
   protected final ScrollingTabContainerView.VisibilityAnimListener mVisAnimListener = new ScrollingTabContainerView.VisibilityAnimListener();
   protected ViewPropertyAnimator mVisibilityAnim;

   public ScrollingTabContainerView(Context var1) {
      super(var1);
      this.setHorizontalScrollBarEnabled(false);
      ActionBarPolicy var2 = ActionBarPolicy.get(var1);
      this.setContentHeight(var2.getTabContainerHeight());
      this.mStackedTabMaxWidth = var2.getStackedTabMaxWidth();
      this.mTabLayout = this.createTabLayout();
      this.addView(this.mTabLayout, new LayoutParams(-2, -1));
   }

   private Spinner createSpinner() {
      AppCompatSpinner var1 = new AppCompatSpinner(this.getContext(), (AttributeSet)null, R$attr.actionDropDownStyle);
      var1.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
      var1.setOnItemSelectedListener(this);
      return var1;
   }

   private LinearLayoutCompat createTabLayout() {
      LinearLayoutCompat var1 = new LinearLayoutCompat(this.getContext(), (AttributeSet)null, R$attr.actionBarTabBarStyle);
      var1.setMeasureWithLargestChildEnabled(true);
      var1.setGravity(17);
      var1.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
      return var1;
   }

   private boolean isCollapsed() {
      Spinner var1 = this.mTabSpinner;
      return var1 != null && var1.getParent() == this;
   }

   private void performCollapse() {
      if (!this.isCollapsed()) {
         if (this.mTabSpinner == null) {
            this.mTabSpinner = this.createSpinner();
         }

         this.removeView(this.mTabLayout);
         this.addView(this.mTabSpinner, new LayoutParams(-2, -1));
         if (this.mTabSpinner.getAdapter() == null) {
            this.mTabSpinner.setAdapter(new ScrollingTabContainerView.TabAdapter());
         }

         Runnable var1 = this.mTabSelector;
         if (var1 != null) {
            this.removeCallbacks(var1);
            this.mTabSelector = null;
         }

         this.mTabSpinner.setSelection(this.mSelectedTabIndex);
      }
   }

   private boolean performExpand() {
      if (!this.isCollapsed()) {
         return false;
      } else {
         this.removeView(this.mTabSpinner);
         this.addView(this.mTabLayout, new LayoutParams(-2, -1));
         this.setTabSelected(this.mTabSpinner.getSelectedItemPosition());
         return false;
      }
   }

   public void addTab(ActionBar.Tab var1, int var2, boolean var3) {
      ScrollingTabContainerView.TabView var5 = this.createTabView(var1, false);
      this.mTabLayout.addView(var5, var2, new LinearLayoutCompat.LayoutParams(0, -1, 1.0F));
      Spinner var4 = this.mTabSpinner;
      if (var4 != null) {
         ((ScrollingTabContainerView.TabAdapter)var4.getAdapter()).notifyDataSetChanged();
      }

      if (var3) {
         var5.setSelected(true);
      }

      if (this.mAllowCollapse) {
         this.requestLayout();
      }

   }

   public void addTab(ActionBar.Tab var1, boolean var2) {
      ScrollingTabContainerView.TabView var4 = this.createTabView(var1, false);
      this.mTabLayout.addView(var4, new LinearLayoutCompat.LayoutParams(0, -1, 1.0F));
      Spinner var3 = this.mTabSpinner;
      if (var3 != null) {
         ((ScrollingTabContainerView.TabAdapter)var3.getAdapter()).notifyDataSetChanged();
      }

      if (var2) {
         var4.setSelected(true);
      }

      if (this.mAllowCollapse) {
         this.requestLayout();
      }

   }

   public void animateToTab(int var1) {
      final View var2 = this.mTabLayout.getChildAt(var1);
      Runnable var3 = this.mTabSelector;
      if (var3 != null) {
         this.removeCallbacks(var3);
      }

      this.mTabSelector = new Runnable() {
         public void run() {
            int var1 = var2.getLeft();
            int var2x = (ScrollingTabContainerView.this.getWidth() - var2.getWidth()) / 2;
            ScrollingTabContainerView.this.smoothScrollTo(var1 - var2x, 0);
            ScrollingTabContainerView.this.mTabSelector = null;
         }
      };
      this.post(this.mTabSelector);
   }

   public void animateToVisibility(int var1) {
      ViewPropertyAnimator var2 = this.mVisibilityAnim;
      if (var2 != null) {
         var2.cancel();
      }

      if (var1 == 0) {
         if (this.getVisibility() != 0) {
            this.setAlpha(0.0F);
         }

         var2 = this.animate().alpha(1.0F);
         var2.setDuration(200L);
         var2.setInterpolator(sAlphaInterpolator);
         var2.setListener(this.mVisAnimListener.withFinalVisibility(var2, var1));
         var2.start();
      } else {
         var2 = this.animate().alpha(0.0F);
         var2.setDuration(200L);
         var2.setInterpolator(sAlphaInterpolator);
         var2.setListener(this.mVisAnimListener.withFinalVisibility(var2, var1));
         var2.start();
      }
   }

   ScrollingTabContainerView.TabView createTabView(ActionBar.Tab var1, boolean var2) {
      ScrollingTabContainerView.TabView var3 = new ScrollingTabContainerView.TabView(this.getContext(), var1, var2);
      if (var2) {
         var3.setBackgroundDrawable((Drawable)null);
         var3.setLayoutParams(new android.widget.AbsListView.LayoutParams(-1, this.mContentHeight));
         return var3;
      } else {
         var3.setFocusable(true);
         if (this.mTabClickListener == null) {
            this.mTabClickListener = new ScrollingTabContainerView.TabClickListener();
         }

         var3.setOnClickListener(this.mTabClickListener);
         return var3;
      }
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      Runnable var1 = this.mTabSelector;
      if (var1 != null) {
         this.post(var1);
      }

   }

   protected void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      ActionBarPolicy var2 = ActionBarPolicy.get(this.getContext());
      this.setContentHeight(var2.getTabContainerHeight());
      this.mStackedTabMaxWidth = var2.getStackedTabMaxWidth();
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      Runnable var1 = this.mTabSelector;
      if (var1 != null) {
         this.removeCallbacks(var1);
      }

   }

   public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
      ((ScrollingTabContainerView.TabView)var2).getTab().select();
   }

   public void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      boolean var6 = true;
      boolean var5;
      if (var3 == 1073741824) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.setFillViewport(var5);
      int var4 = this.mTabLayout.getChildCount();
      if (var4 <= 1 || var3 != 1073741824 && var3 != Integer.MIN_VALUE) {
         this.mMaxTabWidth = -1;
      } else {
         if (var4 > 2) {
            this.mMaxTabWidth = (int)((float)MeasureSpec.getSize(var1) * 0.4F);
         } else {
            this.mMaxTabWidth = MeasureSpec.getSize(var1) / 2;
         }

         this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
      }

      var3 = MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
      if (var5 || !this.mAllowCollapse) {
         var6 = false;
      }

      if (var6) {
         this.mTabLayout.measure(0, var3);
         if (this.mTabLayout.getMeasuredWidth() > MeasureSpec.getSize(var1)) {
            this.performCollapse();
         } else {
            this.performExpand();
         }
      } else {
         this.performExpand();
      }

      var2 = this.getMeasuredWidth();
      super.onMeasure(var1, var3);
      var1 = this.getMeasuredWidth();
      if (var5 && var2 != var1) {
         this.setTabSelected(this.mSelectedTabIndex);
      }

   }

   public void onNothingSelected(AdapterView var1) {
   }

   public void removeAllTabs() {
      this.mTabLayout.removeAllViews();
      Spinner var1 = this.mTabSpinner;
      if (var1 != null) {
         ((ScrollingTabContainerView.TabAdapter)var1.getAdapter()).notifyDataSetChanged();
      }

      if (this.mAllowCollapse) {
         this.requestLayout();
      }

   }

   public void removeTabAt(int var1) {
      this.mTabLayout.removeViewAt(var1);
      Spinner var2 = this.mTabSpinner;
      if (var2 != null) {
         ((ScrollingTabContainerView.TabAdapter)var2.getAdapter()).notifyDataSetChanged();
      }

      if (this.mAllowCollapse) {
         this.requestLayout();
      }

   }

   public void setAllowCollapse(boolean var1) {
      this.mAllowCollapse = var1;
   }

   public void setContentHeight(int var1) {
      this.mContentHeight = var1;
      this.requestLayout();
   }

   public void setTabSelected(int var1) {
      this.mSelectedTabIndex = var1;
      int var3 = this.mTabLayout.getChildCount();

      for(int var2 = 0; var2 < var3; ++var2) {
         View var5 = this.mTabLayout.getChildAt(var2);
         boolean var4;
         if (var2 == var1) {
            var4 = true;
         } else {
            var4 = false;
         }

         var5.setSelected(var4);
         if (var4) {
            this.animateToTab(var1);
         }
      }

      Spinner var6 = this.mTabSpinner;
      if (var6 != null && var1 >= 0) {
         var6.setSelection(var1);
      }

   }

   public void updateTab(int var1) {
      ((ScrollingTabContainerView.TabView)this.mTabLayout.getChildAt(var1)).update();
      Spinner var2 = this.mTabSpinner;
      if (var2 != null) {
         ((ScrollingTabContainerView.TabAdapter)var2.getAdapter()).notifyDataSetChanged();
      }

      if (this.mAllowCollapse) {
         this.requestLayout();
      }

   }

   private class TabAdapter extends BaseAdapter {
      TabAdapter() {
      }

      public int getCount() {
         return ScrollingTabContainerView.this.mTabLayout.getChildCount();
      }

      public Object getItem(int var1) {
         return ((ScrollingTabContainerView.TabView)ScrollingTabContainerView.this.mTabLayout.getChildAt(var1)).getTab();
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public View getView(int var1, View var2, ViewGroup var3) {
         if (var2 == null) {
            return ScrollingTabContainerView.this.createTabView((ActionBar.Tab)this.getItem(var1), true);
         } else {
            ((ScrollingTabContainerView.TabView)var2).bindTab((ActionBar.Tab)this.getItem(var1));
            return var2;
         }
      }
   }

   private class TabClickListener implements OnClickListener {
      TabClickListener() {
      }

      public void onClick(View var1) {
         ((ScrollingTabContainerView.TabView)var1).getTab().select();
         int var3 = ScrollingTabContainerView.this.mTabLayout.getChildCount();

         for(int var2 = 0; var2 < var3; ++var2) {
            View var5 = ScrollingTabContainerView.this.mTabLayout.getChildAt(var2);
            boolean var4;
            if (var5 == var1) {
               var4 = true;
            } else {
               var4 = false;
            }

            var5.setSelected(var4);
         }

      }
   }

   private class TabView extends LinearLayoutCompat {
      private final int[] BG_ATTRS = new int[]{16842964};
      private View mCustomView;
      private ImageView mIconView;
      private ActionBar.Tab mTab;
      private TextView mTextView;

      public TabView(Context var2, ActionBar.Tab var3, boolean var4) {
         super(var2, (AttributeSet)null, R$attr.actionBarTabStyle);
         this.mTab = var3;
         TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var2, (AttributeSet)null, this.BG_ATTRS, R$attr.actionBarTabStyle, 0);
         if (var5.hasValue(0)) {
            this.setBackgroundDrawable(var5.getDrawable(0));
         }

         var5.recycle();
         if (var4) {
            this.setGravity(8388627);
         }

         this.update();
      }

      public void bindTab(ActionBar.Tab var1) {
         this.mTab = var1;
         this.update();
      }

      public ActionBar.Tab getTab() {
         return this.mTab;
      }

      public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
         super.onInitializeAccessibilityEvent(var1);
         var1.setClassName(ActionBar.Tab.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
         super.onInitializeAccessibilityNodeInfo(var1);
         var1.setClassName(ActionBar.Tab.class.getName());
      }

      public void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         if (ScrollingTabContainerView.this.mMaxTabWidth > 0 && this.getMeasuredWidth() > ScrollingTabContainerView.this.mMaxTabWidth) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(ScrollingTabContainerView.this.mMaxTabWidth, 1073741824), var2);
         }

      }

      public void setSelected(boolean var1) {
         boolean var2;
         if (this.isSelected() != var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         super.setSelected(var1);
         if (var2 && var1) {
            this.sendAccessibilityEvent(4);
         }

      }

      public void update() {
         ActionBar.Tab var3 = this.mTab;
         View var4 = var3.getCustomView();
         CharSequence var2 = null;
         if (var4 != null) {
            ViewParent var8 = var4.getParent();
            if (var8 != this) {
               if (var8 != null) {
                  ((ViewGroup)var8).removeView(var4);
               }

               this.addView(var4);
            }

            this.mCustomView = var4;
            TextView var9 = this.mTextView;
            if (var9 != null) {
               var9.setVisibility(8);
            }

            ImageView var10 = this.mIconView;
            if (var10 != null) {
               var10.setVisibility(8);
               this.mIconView.setImageDrawable((Drawable)null);
            }

         } else {
            var4 = this.mCustomView;
            if (var4 != null) {
               this.removeView(var4);
               this.mCustomView = null;
            }

            Drawable var5 = var3.getIcon();
            CharSequence var11 = var3.getText();
            if (var5 != null) {
               if (this.mIconView == null) {
                  AppCompatImageView var6 = new AppCompatImageView(this.getContext());
                  LinearLayoutCompat.LayoutParams var7 = new LinearLayoutCompat.LayoutParams(-2, -2);
                  var7.gravity = 16;
                  var6.setLayoutParams(var7);
                  this.addView(var6, 0);
                  this.mIconView = var6;
               }

               this.mIconView.setImageDrawable(var5);
               this.mIconView.setVisibility(0);
            } else {
               ImageView var13 = this.mIconView;
               if (var13 != null) {
                  var13.setVisibility(8);
                  this.mIconView.setImageDrawable((Drawable)null);
               }
            }

            boolean var1 = TextUtils.isEmpty(var11) ^ true;
            if (var1) {
               if (this.mTextView == null) {
                  AppCompatTextView var15 = new AppCompatTextView(this.getContext(), (AttributeSet)null, R$attr.actionBarTabTextStyle);
                  var15.setEllipsize(TruncateAt.END);
                  LinearLayoutCompat.LayoutParams var16 = new LinearLayoutCompat.LayoutParams(-2, -2);
                  var16.gravity = 16;
                  var15.setLayoutParams(var16);
                  this.addView(var15);
                  this.mTextView = var15;
               }

               this.mTextView.setText(var11);
               this.mTextView.setVisibility(0);
            } else {
               TextView var12 = this.mTextView;
               if (var12 != null) {
                  var12.setVisibility(8);
                  this.mTextView.setText((CharSequence)null);
               }
            }

            ImageView var14 = this.mIconView;
            if (var14 != null) {
               var14.setContentDescription(var3.getContentDescription());
            }

            if (!var1) {
               var2 = var3.getContentDescription();
            }

            TooltipCompat.setTooltipText(this, var2);
         }
      }
   }

   protected class VisibilityAnimListener extends AnimatorListenerAdapter {
      private boolean mCanceled = false;
      private int mFinalVisibility;

      public void onAnimationCancel(Animator var1) {
         this.mCanceled = true;
      }

      public void onAnimationEnd(Animator var1) {
         if (!this.mCanceled) {
            ScrollingTabContainerView var2 = ScrollingTabContainerView.this;
            var2.mVisibilityAnim = null;
            var2.setVisibility(this.mFinalVisibility);
         }
      }

      public void onAnimationStart(Animator var1) {
         ScrollingTabContainerView.this.setVisibility(0);
         this.mCanceled = false;
      }

      public ScrollingTabContainerView.VisibilityAnimListener withFinalVisibility(ViewPropertyAnimator var1, int var2) {
         this.mFinalVisibility = var2;
         ScrollingTabContainerView.this.mVisibilityAnim = var1;
         return this;
      }
   }
}
