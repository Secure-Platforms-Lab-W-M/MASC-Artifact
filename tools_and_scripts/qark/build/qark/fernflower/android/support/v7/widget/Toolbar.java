package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
   private static final String TAG = "Toolbar";
   private MenuPresenter.Callback mActionMenuPresenterCallback;
   int mButtonGravity;
   ImageButton mCollapseButtonView;
   private CharSequence mCollapseDescription;
   private Drawable mCollapseIcon;
   private boolean mCollapsible;
   private int mContentInsetEndWithActions;
   private int mContentInsetStartWithNavigation;
   private RtlSpacingHelper mContentInsets;
   private boolean mEatingHover;
   private boolean mEatingTouch;
   View mExpandedActionView;
   private Toolbar.ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
   private int mGravity;
   private final ArrayList mHiddenViews;
   private ImageView mLogoView;
   private int mMaxButtonHeight;
   private MenuBuilder.Callback mMenuBuilderCallback;
   private ActionMenuView mMenuView;
   private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
   private ImageButton mNavButtonView;
   Toolbar.OnMenuItemClickListener mOnMenuItemClickListener;
   private ActionMenuPresenter mOuterActionMenuPresenter;
   private Context mPopupContext;
   private int mPopupTheme;
   private final Runnable mShowOverflowMenuRunnable;
   private CharSequence mSubtitleText;
   private int mSubtitleTextAppearance;
   private int mSubtitleTextColor;
   private TextView mSubtitleTextView;
   private final int[] mTempMargins;
   private final ArrayList mTempViews;
   private int mTitleMarginBottom;
   private int mTitleMarginEnd;
   private int mTitleMarginStart;
   private int mTitleMarginTop;
   private CharSequence mTitleText;
   private int mTitleTextAppearance;
   private int mTitleTextColor;
   private TextView mTitleTextView;
   private ToolbarWidgetWrapper mWrapper;

   public Toolbar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public Toolbar(Context var1, @Nullable AttributeSet var2) {
      this(var1, var2, R$attr.toolbarStyle);
   }

   public Toolbar(Context var1, @Nullable AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mGravity = 8388627;
      this.mTempViews = new ArrayList();
      this.mHiddenViews = new ArrayList();
      this.mTempMargins = new int[2];
      this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
         public boolean onMenuItemClick(MenuItem var1) {
            return Toolbar.this.mOnMenuItemClickListener != null ? Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(var1) : false;
         }
      };
      this.mShowOverflowMenuRunnable = new Runnable() {
         public void run() {
            Toolbar.this.showOverflowMenu();
         }
      };
      TintTypedArray var7 = TintTypedArray.obtainStyledAttributes(this.getContext(), var2, R$styleable.Toolbar, var3, 0);
      this.mTitleTextAppearance = var7.getResourceId(R$styleable.Toolbar_titleTextAppearance, 0);
      this.mSubtitleTextAppearance = var7.getResourceId(R$styleable.Toolbar_subtitleTextAppearance, 0);
      this.mGravity = var7.getInteger(R$styleable.Toolbar_android_gravity, this.mGravity);
      this.mButtonGravity = var7.getInteger(R$styleable.Toolbar_buttonGravity, 48);
      int var4 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMargin, 0);
      var3 = var4;
      if (var7.hasValue(R$styleable.Toolbar_titleMargins)) {
         var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMargins, var4);
      }

      this.mTitleMarginBottom = var3;
      this.mTitleMarginTop = var3;
      this.mTitleMarginEnd = var3;
      this.mTitleMarginStart = var3;
      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginStart, -1);
      if (var3 >= 0) {
         this.mTitleMarginStart = var3;
      }

      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginEnd, -1);
      if (var3 >= 0) {
         this.mTitleMarginEnd = var3;
      }

      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginTop, -1);
      if (var3 >= 0) {
         this.mTitleMarginTop = var3;
      }

      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_titleMarginBottom, -1);
      if (var3 >= 0) {
         this.mTitleMarginBottom = var3;
      }

      this.mMaxButtonHeight = var7.getDimensionPixelSize(R$styleable.Toolbar_maxButtonHeight, -1);
      var3 = var7.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
      var4 = var7.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
      int var5 = var7.getDimensionPixelSize(R$styleable.Toolbar_contentInsetLeft, 0);
      int var6 = var7.getDimensionPixelSize(R$styleable.Toolbar_contentInsetRight, 0);
      this.ensureContentInsets();
      this.mContentInsets.setAbsolute(var5, var6);
      if (var3 != Integer.MIN_VALUE || var4 != Integer.MIN_VALUE) {
         this.mContentInsets.setRelative(var3, var4);
      }

      this.mContentInsetStartWithNavigation = var7.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
      this.mContentInsetEndWithActions = var7.getDimensionPixelOffset(R$styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
      this.mCollapseIcon = var7.getDrawable(R$styleable.Toolbar_collapseIcon);
      this.mCollapseDescription = var7.getText(R$styleable.Toolbar_collapseContentDescription);
      CharSequence var8 = var7.getText(R$styleable.Toolbar_title);
      if (!TextUtils.isEmpty(var8)) {
         this.setTitle(var8);
      }

      var8 = var7.getText(R$styleable.Toolbar_subtitle);
      if (!TextUtils.isEmpty(var8)) {
         this.setSubtitle(var8);
      }

      this.mPopupContext = this.getContext();
      this.setPopupTheme(var7.getResourceId(R$styleable.Toolbar_popupTheme, 0));
      Drawable var9 = var7.getDrawable(R$styleable.Toolbar_navigationIcon);
      if (var9 != null) {
         this.setNavigationIcon(var9);
      }

      var8 = var7.getText(R$styleable.Toolbar_navigationContentDescription);
      if (!TextUtils.isEmpty(var8)) {
         this.setNavigationContentDescription(var8);
      }

      var9 = var7.getDrawable(R$styleable.Toolbar_logo);
      if (var9 != null) {
         this.setLogo(var9);
      }

      var8 = var7.getText(R$styleable.Toolbar_logoDescription);
      if (!TextUtils.isEmpty(var8)) {
         this.setLogoDescription(var8);
      }

      if (var7.hasValue(R$styleable.Toolbar_titleTextColor)) {
         this.setTitleTextColor(var7.getColor(R$styleable.Toolbar_titleTextColor, -1));
      }

      if (var7.hasValue(R$styleable.Toolbar_subtitleTextColor)) {
         this.setSubtitleTextColor(var7.getColor(R$styleable.Toolbar_subtitleTextColor, -1));
      }

      var7.recycle();
   }

   private void addCustomViewsWithGravity(List var1, int var2) {
      int var4 = ViewCompat.getLayoutDirection(this);
      boolean var3 = true;
      if (var4 != 1) {
         var3 = false;
      }

      int var5 = this.getChildCount();
      var4 = GravityCompat.getAbsoluteGravity(var2, ViewCompat.getLayoutDirection(this));
      var1.clear();
      View var6;
      Toolbar.LayoutParams var7;
      if (var3) {
         for(var2 = var5 - 1; var2 >= 0; --var2) {
            var6 = this.getChildAt(var2);
            var7 = (Toolbar.LayoutParams)var6.getLayoutParams();
            if (var7.mViewType == 0 && this.shouldLayout(var6) && this.getChildHorizontalGravity(var7.gravity) == var4) {
               var1.add(var6);
            }
         }

      } else {
         for(var2 = 0; var2 < var5; ++var2) {
            var6 = this.getChildAt(var2);
            var7 = (Toolbar.LayoutParams)var6.getLayoutParams();
            if (var7.mViewType == 0 && this.shouldLayout(var6) && this.getChildHorizontalGravity(var7.gravity) == var4) {
               var1.add(var6);
            }
         }

      }
   }

   private void addSystemView(View var1, boolean var2) {
      android.view.ViewGroup.LayoutParams var3 = var1.getLayoutParams();
      Toolbar.LayoutParams var4;
      if (var3 == null) {
         var4 = this.generateDefaultLayoutParams();
      } else if (!this.checkLayoutParams(var3)) {
         var4 = this.generateLayoutParams(var3);
      } else {
         var4 = (Toolbar.LayoutParams)var3;
      }

      var4.mViewType = 1;
      if (var2 && this.mExpandedActionView != null) {
         var1.setLayoutParams(var4);
         this.mHiddenViews.add(var1);
      } else {
         this.addView(var1, var4);
      }
   }

   private void ensureContentInsets() {
      if (this.mContentInsets == null) {
         this.mContentInsets = new RtlSpacingHelper();
      }

   }

   private void ensureLogoView() {
      if (this.mLogoView == null) {
         this.mLogoView = new AppCompatImageView(this.getContext());
      }

   }

   private void ensureMenu() {
      this.ensureMenuView();
      if (this.mMenuView.peekMenu() == null) {
         MenuBuilder var1 = (MenuBuilder)this.mMenuView.getMenu();
         if (this.mExpandedMenuPresenter == null) {
            this.mExpandedMenuPresenter = new Toolbar.ExpandedActionViewMenuPresenter();
         }

         this.mMenuView.setExpandedActionViewsExclusive(true);
         var1.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
      }

   }

   private void ensureMenuView() {
      if (this.mMenuView == null) {
         this.mMenuView = new ActionMenuView(this.getContext());
         this.mMenuView.setPopupTheme(this.mPopupTheme);
         this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
         this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388613 | this.mButtonGravity & 112;
         this.mMenuView.setLayoutParams(var1);
         this.addSystemView(this.mMenuView, false);
      }

   }

   private void ensureNavButtonView() {
      if (this.mNavButtonView == null) {
         this.mNavButtonView = new AppCompatImageButton(this.getContext(), (AttributeSet)null, R$attr.toolbarNavigationButtonStyle);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388611 | this.mButtonGravity & 112;
         this.mNavButtonView.setLayoutParams(var1);
      }

   }

   private int getChildHorizontalGravity(int var1) {
      int var2 = ViewCompat.getLayoutDirection(this);
      int var3 = GravityCompat.getAbsoluteGravity(var1, var2) & 7;
      if (var3 != 1) {
         byte var4 = 3;
         if (var3 != 3 && var3 != 5) {
            if (var2 == 1) {
               var4 = 5;
            }

            return var4;
         }
      }

      return var3;
   }

   private int getChildTop(View var1, int var2) {
      Toolbar.LayoutParams var7 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var4 = var1.getMeasuredHeight();
      if (var2 > 0) {
         var2 = (var4 - var2) / 2;
      } else {
         var2 = 0;
      }

      int var3 = this.getChildVerticalGravity(var7.gravity);
      if (var3 != 48) {
         if (var3 != 80) {
            int var5 = this.getPaddingTop();
            var2 = this.getPaddingBottom();
            int var6 = this.getHeight();
            var3 = (var6 - var5 - var2 - var4) / 2;
            if (var3 < var7.topMargin) {
               var2 = var7.topMargin;
            } else {
               var4 = var6 - var2 - var4 - var3 - var5;
               var2 = var3;
               if (var4 < var7.bottomMargin) {
                  var2 = Math.max(0, var3 - (var7.bottomMargin - var4));
               }
            }

            return var5 + var2;
         } else {
            return this.getHeight() - this.getPaddingBottom() - var4 - var7.bottomMargin - var2;
         }
      } else {
         return this.getPaddingTop() - var2;
      }
   }

   private int getChildVerticalGravity(int var1) {
      var1 &= 112;
      return var1 != 16 && var1 != 48 && var1 != 80 ? this.mGravity & 112 : var1;
   }

   private int getHorizontalMargins(View var1) {
      MarginLayoutParams var2 = (MarginLayoutParams)var1.getLayoutParams();
      return MarginLayoutParamsCompat.getMarginStart(var2) + MarginLayoutParamsCompat.getMarginEnd(var2);
   }

   private MenuInflater getMenuInflater() {
      return new SupportMenuInflater(this.getContext());
   }

   private int getVerticalMargins(View var1) {
      MarginLayoutParams var2 = (MarginLayoutParams)var1.getLayoutParams();
      return var2.topMargin + var2.bottomMargin;
   }

   private int getViewListMeasuredWidth(List var1, int[] var2) {
      int var6 = var2[0];
      int var5 = var2[1];
      int var4 = 0;
      int var7 = var1.size();

      for(int var3 = 0; var3 < var7; ++var3) {
         View var11 = (View)var1.get(var3);
         Toolbar.LayoutParams var10 = (Toolbar.LayoutParams)var11.getLayoutParams();
         var6 = var10.leftMargin - var6;
         var5 = var10.rightMargin - var5;
         int var8 = Math.max(0, var6);
         int var9 = Math.max(0, var5);
         var6 = Math.max(0, -var6);
         var5 = Math.max(0, -var5);
         var4 += var11.getMeasuredWidth() + var8 + var9;
      }

      return var4;
   }

   private boolean isChildOrHidden(View var1) {
      return var1.getParent() == this || this.mHiddenViews.contains(var1);
   }

   private static boolean isCustomView(View var0) {
      return ((Toolbar.LayoutParams)var0.getLayoutParams()).mViewType == 0;
   }

   private int layoutChildLeft(View var1, int var2, int[] var3, int var4) {
      Toolbar.LayoutParams var6 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var5 = var6.leftMargin - var3[0];
      var2 += Math.max(0, var5);
      var3[0] = Math.max(0, -var5);
      var4 = this.getChildTop(var1, var4);
      var5 = var1.getMeasuredWidth();
      var1.layout(var2, var4, var2 + var5, var1.getMeasuredHeight() + var4);
      return var2 + var6.rightMargin + var5;
   }

   private int layoutChildRight(View var1, int var2, int[] var3, int var4) {
      Toolbar.LayoutParams var6 = (Toolbar.LayoutParams)var1.getLayoutParams();
      int var5 = var6.rightMargin - var3[1];
      var2 -= Math.max(0, var5);
      var3[1] = Math.max(0, -var5);
      var4 = this.getChildTop(var1, var4);
      var5 = var1.getMeasuredWidth();
      var1.layout(var2 - var5, var4, var2, var1.getMeasuredHeight() + var4);
      return var2 - (var6.leftMargin + var5);
   }

   private int measureChildCollapseMargins(View var1, int var2, int var3, int var4, int var5, int[] var6) {
      MarginLayoutParams var10 = (MarginLayoutParams)var1.getLayoutParams();
      int var7 = var10.leftMargin - var6[0];
      int var8 = var10.rightMargin - var6[1];
      int var9 = Math.max(0, var7) + Math.max(0, var8);
      var6[0] = Math.max(0, -var7);
      var6[1] = Math.max(0, -var8);
      var1.measure(getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight() + var9 + var3, var10.width), getChildMeasureSpec(var4, this.getPaddingTop() + this.getPaddingBottom() + var10.topMargin + var10.bottomMargin + var5, var10.height));
      return var1.getMeasuredWidth() + var9;
   }

   private void measureChildConstrained(View var1, int var2, int var3, int var4, int var5, int var6) {
      MarginLayoutParams var8 = (MarginLayoutParams)var1.getLayoutParams();
      int var7 = getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight() + var8.leftMargin + var8.rightMargin + var3, var8.width);
      var3 = getChildMeasureSpec(var4, this.getPaddingTop() + this.getPaddingBottom() + var8.topMargin + var8.bottomMargin + var5, var8.height);
      var4 = MeasureSpec.getMode(var3);
      var2 = var3;
      if (var4 != 1073741824) {
         var2 = var3;
         if (var6 >= 0) {
            if (var4 != 0) {
               var6 = Math.min(MeasureSpec.getSize(var3), var6);
            }

            var2 = MeasureSpec.makeMeasureSpec(var6, 1073741824);
         }
      }

      var1.measure(var7, var2);
   }

   private void postShowOverflowMenu() {
      this.removeCallbacks(this.mShowOverflowMenuRunnable);
      this.post(this.mShowOverflowMenuRunnable);
   }

   private boolean shouldCollapse() {
      if (!this.mCollapsible) {
         return false;
      } else {
         int var2 = this.getChildCount();

         for(int var1 = 0; var1 < var2; ++var1) {
            View var3 = this.getChildAt(var1);
            if (this.shouldLayout(var3) && var3.getMeasuredWidth() > 0 && var3.getMeasuredHeight() > 0) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean shouldLayout(View var1) {
      return var1 != null && var1.getParent() == this && var1.getVisibility() != 8;
   }

   void addChildrenForExpandedActionView() {
      for(int var1 = this.mHiddenViews.size() - 1; var1 >= 0; --var1) {
         this.addView((View)this.mHiddenViews.get(var1));
      }

      this.mHiddenViews.clear();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean canShowOverflowMenu() {
      if (this.getVisibility() == 0) {
         ActionMenuView var1 = this.mMenuView;
         if (var1 != null && var1.isOverflowReserved()) {
            return true;
         }
      }

      return false;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return super.checkLayoutParams(var1) && var1 instanceof Toolbar.LayoutParams;
   }

   public void collapseActionView() {
      Toolbar.ExpandedActionViewMenuPresenter var1 = this.mExpandedMenuPresenter;
      MenuItemImpl var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = var1.mCurrentExpandedItem;
      }

      if (var2 != null) {
         var2.collapseActionView();
      }

   }

   public void dismissPopupMenus() {
      ActionMenuView var1 = this.mMenuView;
      if (var1 != null) {
         var1.dismissPopupMenus();
      }

   }

   void ensureCollapseButtonView() {
      if (this.mCollapseButtonView == null) {
         this.mCollapseButtonView = new AppCompatImageButton(this.getContext(), (AttributeSet)null, R$attr.toolbarNavigationButtonStyle);
         this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
         this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
         Toolbar.LayoutParams var1 = this.generateDefaultLayoutParams();
         var1.gravity = 8388611 | this.mButtonGravity & 112;
         var1.mViewType = 2;
         this.mCollapseButtonView.setLayoutParams(var1);
         this.mCollapseButtonView.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               Toolbar.this.collapseActionView();
            }
         });
      }

   }

   protected Toolbar.LayoutParams generateDefaultLayoutParams() {
      return new Toolbar.LayoutParams(-2, -2);
   }

   public Toolbar.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new Toolbar.LayoutParams(this.getContext(), var1);
   }

   protected Toolbar.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (var1 instanceof Toolbar.LayoutParams) {
         return new Toolbar.LayoutParams((Toolbar.LayoutParams)var1);
      } else if (var1 instanceof ActionBar.LayoutParams) {
         return new Toolbar.LayoutParams((ActionBar.LayoutParams)var1);
      } else {
         return var1 instanceof MarginLayoutParams ? new Toolbar.LayoutParams((MarginLayoutParams)var1) : new Toolbar.LayoutParams(var1);
      }
   }

   public int getContentInsetEnd() {
      RtlSpacingHelper var1 = this.mContentInsets;
      return var1 != null ? var1.getEnd() : 0;
   }

   public int getContentInsetEndWithActions() {
      int var1 = this.mContentInsetEndWithActions;
      return var1 != Integer.MIN_VALUE ? var1 : this.getContentInsetEnd();
   }

   public int getContentInsetLeft() {
      RtlSpacingHelper var1 = this.mContentInsets;
      return var1 != null ? var1.getLeft() : 0;
   }

   public int getContentInsetRight() {
      RtlSpacingHelper var1 = this.mContentInsets;
      return var1 != null ? var1.getRight() : 0;
   }

   public int getContentInsetStart() {
      RtlSpacingHelper var1 = this.mContentInsets;
      return var1 != null ? var1.getStart() : 0;
   }

   public int getContentInsetStartWithNavigation() {
      int var1 = this.mContentInsetStartWithNavigation;
      return var1 != Integer.MIN_VALUE ? var1 : this.getContentInsetStart();
   }

   public int getCurrentContentInsetEnd() {
      boolean var1 = false;
      ActionMenuView var2 = this.mMenuView;
      if (var2 != null) {
         MenuBuilder var3 = var2.peekMenu();
         if (var3 != null && var3.hasVisibleItems()) {
            var1 = true;
         } else {
            var1 = false;
         }
      }

      return var1 ? Math.max(this.getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0)) : this.getContentInsetEnd();
   }

   public int getCurrentContentInsetLeft() {
      return ViewCompat.getLayoutDirection(this) == 1 ? this.getCurrentContentInsetEnd() : this.getCurrentContentInsetStart();
   }

   public int getCurrentContentInsetRight() {
      return ViewCompat.getLayoutDirection(this) == 1 ? this.getCurrentContentInsetStart() : this.getCurrentContentInsetEnd();
   }

   public int getCurrentContentInsetStart() {
      return this.getNavigationIcon() != null ? Math.max(this.getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0)) : this.getContentInsetStart();
   }

   public Drawable getLogo() {
      ImageView var1 = this.mLogoView;
      return var1 != null ? var1.getDrawable() : null;
   }

   public CharSequence getLogoDescription() {
      ImageView var1 = this.mLogoView;
      return var1 != null ? var1.getContentDescription() : null;
   }

   public Menu getMenu() {
      this.ensureMenu();
      return this.mMenuView.getMenu();
   }

   @Nullable
   public CharSequence getNavigationContentDescription() {
      ImageButton var1 = this.mNavButtonView;
      return var1 != null ? var1.getContentDescription() : null;
   }

   @Nullable
   public Drawable getNavigationIcon() {
      ImageButton var1 = this.mNavButtonView;
      return var1 != null ? var1.getDrawable() : null;
   }

   ActionMenuPresenter getOuterActionMenuPresenter() {
      return this.mOuterActionMenuPresenter;
   }

   @Nullable
   public Drawable getOverflowIcon() {
      this.ensureMenu();
      return this.mMenuView.getOverflowIcon();
   }

   Context getPopupContext() {
      return this.mPopupContext;
   }

   public int getPopupTheme() {
      return this.mPopupTheme;
   }

   public CharSequence getSubtitle() {
      return this.mSubtitleText;
   }

   public CharSequence getTitle() {
      return this.mTitleText;
   }

   public int getTitleMarginBottom() {
      return this.mTitleMarginBottom;
   }

   public int getTitleMarginEnd() {
      return this.mTitleMarginEnd;
   }

   public int getTitleMarginStart() {
      return this.mTitleMarginStart;
   }

   public int getTitleMarginTop() {
      return this.mTitleMarginTop;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public DecorToolbar getWrapper() {
      if (this.mWrapper == null) {
         this.mWrapper = new ToolbarWidgetWrapper(this, true);
      }

      return this.mWrapper;
   }

   public boolean hasExpandedActionView() {
      Toolbar.ExpandedActionViewMenuPresenter var1 = this.mExpandedMenuPresenter;
      return var1 != null && var1.mCurrentExpandedItem != null;
   }

   public boolean hideOverflowMenu() {
      ActionMenuView var1 = this.mMenuView;
      return var1 != null && var1.hideOverflowMenu();
   }

   public void inflateMenu(@MenuRes int var1) {
      this.getMenuInflater().inflate(var1, this.getMenu());
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean isOverflowMenuShowPending() {
      ActionMenuView var1 = this.mMenuView;
      return var1 != null && var1.isOverflowMenuShowPending();
   }

   public boolean isOverflowMenuShowing() {
      ActionMenuView var1 = this.mMenuView;
      return var1 != null && var1.isOverflowMenuShowing();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean isTitleTruncated() {
      TextView var3 = this.mTitleTextView;
      if (var3 == null) {
         return false;
      } else {
         Layout var4 = var3.getLayout();
         if (var4 == null) {
            return false;
         } else {
            int var2 = var4.getLineCount();

            for(int var1 = 0; var1 < var2; ++var1) {
               if (var4.getEllipsisCount(var1) > 0) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.removeCallbacks(this.mShowOverflowMenuRunnable);
   }

   public boolean onHoverEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 9) {
         this.mEatingHover = false;
      }

      if (!this.mEatingHover) {
         boolean var3 = super.onHoverEvent(var1);
         if (var2 == 9 && !var3) {
            this.mEatingHover = true;
         }
      }

      if (var2 == 10 || var2 == 3) {
         this.mEatingHover = false;
      }

      return true;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      boolean var8;
      if (ViewCompat.getLayoutDirection(this) == 1) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var12 = this.getWidth();
      int var11 = this.getHeight();
      int var7 = this.getPaddingLeft();
      int var13 = this.getPaddingRight();
      int var10 = this.getPaddingTop();
      int var14 = this.getPaddingBottom();
      int var9 = var12 - var13;
      int[] var18 = this.mTempMargins;
      var18[1] = 0;
      var18[0] = 0;
      var2 = ViewCompat.getMinimumHeight(this);
      if (var2 >= 0) {
         var4 = Math.min(var2, var5 - var3);
      } else {
         var4 = 0;
      }

      var2 = var7;
      var3 = var9;
      if (this.shouldLayout(this.mNavButtonView)) {
         if (var8) {
            var3 = this.layoutChildRight(this.mNavButtonView, var9, var18, var4);
            var2 = var7;
         } else {
            var2 = this.layoutChildLeft(this.mNavButtonView, var7, var18, var4);
            var3 = var9;
         }
      }

      var5 = var2;
      int var6 = var3;
      if (this.shouldLayout(this.mCollapseButtonView)) {
         if (var8) {
            var6 = this.layoutChildRight(this.mCollapseButtonView, var3, var18, var4);
            var5 = var2;
         } else {
            var5 = this.layoutChildLeft(this.mCollapseButtonView, var2, var18, var4);
            var6 = var3;
         }
      }

      var3 = var5;
      var2 = var6;
      if (this.shouldLayout(this.mMenuView)) {
         if (var8) {
            var3 = this.layoutChildLeft(this.mMenuView, var5, var18, var4);
            var2 = var6;
         } else {
            var2 = this.layoutChildRight(this.mMenuView, var6, var18, var4);
            var3 = var5;
         }
      }

      var6 = this.getCurrentContentInsetLeft();
      var5 = this.getCurrentContentInsetRight();
      var18[0] = Math.max(0, var6 - var3);
      var18[1] = Math.max(0, var5 - (var12 - var13 - var2));
      var3 = Math.max(var3, var6);
      var5 = Math.min(var2, var12 - var13 - var5);
      var2 = var3;
      var6 = var5;
      if (this.shouldLayout(this.mExpandedActionView)) {
         if (var8) {
            var6 = this.layoutChildRight(this.mExpandedActionView, var5, var18, var4);
            var2 = var3;
         } else {
            var2 = this.layoutChildLeft(this.mExpandedActionView, var3, var18, var4);
            var6 = var5;
         }
      }

      var3 = var2;
      var5 = var6;
      if (this.shouldLayout(this.mLogoView)) {
         if (var8) {
            var5 = this.layoutChildRight(this.mLogoView, var6, var18, var4);
            var3 = var2;
         } else {
            var3 = this.layoutChildLeft(this.mLogoView, var2, var18, var4);
            var5 = var6;
         }
      }

      var1 = this.shouldLayout(this.mTitleTextView);
      boolean var15 = this.shouldLayout(this.mSubtitleTextView);
      var2 = 0;
      Toolbar.LayoutParams var16;
      if (var1) {
         var16 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
         var2 = 0 + var16.topMargin + this.mTitleTextView.getMeasuredHeight() + var16.bottomMargin;
      }

      var9 = var2;
      if (var15) {
         var16 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
         var9 = var2 + var16.topMargin + this.mSubtitleTextView.getMeasuredHeight() + var16.bottomMargin;
      }

      if (!var1 && !var15) {
         var2 = var3;
         var3 = var5;
      } else {
         TextView var21;
         if (var1) {
            var21 = this.mTitleTextView;
         } else {
            var21 = this.mSubtitleTextView;
         }

         TextView var17;
         if (var15) {
            var17 = this.mSubtitleTextView;
         } else {
            var17 = this.mTitleTextView;
         }

         var16 = (Toolbar.LayoutParams)var21.getLayoutParams();
         Toolbar.LayoutParams var22 = (Toolbar.LayoutParams)var17.getLayoutParams();
         boolean var19;
         if ((!var1 || this.mTitleTextView.getMeasuredWidth() <= 0) && (!var15 || this.mSubtitleTextView.getMeasuredWidth() <= 0)) {
            var19 = false;
         } else {
            var19 = true;
         }

         var2 = this.mGravity & 112;
         if (var2 != 48) {
            if (var2 != 80) {
               var2 = (var11 - var10 - var14 - var9) / 2;
               if (var2 < var16.topMargin + this.mTitleMarginTop) {
                  var2 = var16.topMargin + this.mTitleMarginTop;
               } else {
                  var9 = var11 - var14 - var9 - var2 - var10;
                  if (var9 < var16.bottomMargin + this.mTitleMarginBottom) {
                     var2 = Math.max(0, var2 - (var22.bottomMargin + this.mTitleMarginBottom - var9));
                  }
               }

               var2 += var10;
            } else {
               var2 = var11 - var14 - var22.bottomMargin - this.mTitleMarginBottom - var9;
            }
         } else {
            var2 = this.getPaddingTop() + var16.topMargin + this.mTitleMarginTop;
         }

         var9 = var3;
         int var20;
         if (var8) {
            if (var19) {
               var3 = this.mTitleMarginStart;
            } else {
               var3 = 0;
            }

            var20 = var3 - var18[1];
            var3 = var5 - Math.max(0, var20);
            var18[1] = Math.max(0, -var20);
            if (var1) {
               var16 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
               var11 = var3 - this.mTitleTextView.getMeasuredWidth();
               var10 = this.mTitleTextView.getMeasuredHeight() + var2;
               this.mTitleTextView.layout(var11, var2, var3, var10);
               var2 = var11 - this.mTitleMarginEnd;
               var10 += var16.bottomMargin;
            } else {
               var10 = var2;
               var2 = var3;
            }

            var20 = var3;
            if (var15) {
               var16 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
               var20 = var10 + var16.topMargin;
               var10 = this.mSubtitleTextView.getMeasuredWidth();
               var11 = this.mSubtitleTextView.getMeasuredHeight() + var20;
               this.mSubtitleTextView.layout(var3 - var10, var20, var3, var11);
               var20 = var3 - this.mTitleMarginEnd;
               var5 = var16.bottomMargin;
            }

            if (var19) {
               var3 = Math.min(var2, var20);
            }

            var2 = var9;
         } else {
            if (var19) {
               var3 = this.mTitleMarginStart;
            } else {
               var3 = 0;
            }

            var20 = var3 - var18[0];
            var3 = var9 + Math.max(0, var20);
            var18[0] = Math.max(0, -var20);
            var9 = var3;
            var20 = var3;
            if (var1) {
               var16 = (Toolbar.LayoutParams)this.mTitleTextView.getLayoutParams();
               var11 = this.mTitleTextView.getMeasuredWidth() + var3;
               var10 = this.mTitleTextView.getMeasuredHeight() + var2;
               this.mTitleTextView.layout(var3, var2, var11, var10);
               var9 = var11 + this.mTitleMarginEnd;
               var10 += var16.bottomMargin;
            } else {
               var10 = var2;
            }

            var2 = var3;
            var11 = var3;
            if (var15) {
               var16 = (Toolbar.LayoutParams)this.mSubtitleTextView.getLayoutParams();
               var3 = var10 + var16.topMargin;
               var10 = this.mSubtitleTextView.getMeasuredWidth() + var20;
               var11 = this.mSubtitleTextView.getMeasuredHeight() + var3;
               this.mSubtitleTextView.layout(var20, var3, var10, var11);
               var11 = var10 + this.mTitleMarginEnd;
               var3 = var16.bottomMargin;
            }

            var3 = var5;
            if (var19) {
               var2 = Math.max(var9, var11);
               var3 = var5;
            }
         }
      }

      var5 = var4;
      this.addCustomViewsWithGravity(this.mTempViews, 3);
      var6 = this.mTempViews.size();

      for(var4 = 0; var4 < var6; ++var4) {
         var2 = this.layoutChildLeft((View)this.mTempViews.get(var4), var2, var18, var5);
      }

      this.addCustomViewsWithGravity(this.mTempViews, 5);
      var6 = this.mTempViews.size();

      for(var4 = 0; var4 < var6; ++var4) {
         var3 = this.layoutChildRight((View)this.mTempViews.get(var4), var3, var18, var5);
      }

      this.addCustomViewsWithGravity(this.mTempViews, 1);
      var6 = this.getViewListMeasuredWidth(this.mTempViews, var18);
      var4 = var7 + (var12 - var7 - var13) / 2 - var6 / 2;
      var6 += var4;
      if (var4 >= var2) {
         var2 = var4;
         if (var6 > var3) {
            var2 = var4 - (var6 - var3);
         }
      }

      var3 = this.mTempViews.size();
      var4 = var2;

      for(var2 = 0; var2 < var3; ++var2) {
         var4 = this.layoutChildLeft((View)this.mTempViews.get(var2), var4, var18, var5);
      }

      this.mTempViews.clear();
   }

   protected void onMeasure(int var1, int var2) {
      int var6 = 0;
      int var5 = 0;
      int[] var11 = this.mTempMargins;
      byte var8;
      byte var9;
      if (ViewUtils.isLayoutRtl(this)) {
         var9 = 1;
         var8 = 0;
      } else {
         var9 = 0;
         var8 = 1;
      }

      int var7 = 0;
      if (this.shouldLayout(this.mNavButtonView)) {
         this.measureChildConstrained(this.mNavButtonView, var1, 0, var2, 0, this.mMaxButtonHeight);
         var7 = this.mNavButtonView.getMeasuredWidth() + this.getHorizontalMargins(this.mNavButtonView);
         var6 = Math.max(0, this.mNavButtonView.getMeasuredHeight() + this.getVerticalMargins(this.mNavButtonView));
         var5 = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
      }

      int var4 = var6;
      int var3 = var5;
      if (this.shouldLayout(this.mCollapseButtonView)) {
         this.measureChildConstrained(this.mCollapseButtonView, var1, 0, var2, 0, this.mMaxButtonHeight);
         var7 = this.mCollapseButtonView.getMeasuredWidth() + this.getHorizontalMargins(this.mCollapseButtonView);
         var4 = Math.max(var6, this.mCollapseButtonView.getMeasuredHeight() + this.getVerticalMargins(this.mCollapseButtonView));
         var3 = View.combineMeasuredStates(var5, this.mCollapseButtonView.getMeasuredState());
      }

      var5 = this.getCurrentContentInsetStart();
      var6 = 0 + Math.max(var5, var7);
      var11[var9] = Math.max(0, var5 - var7);
      int var14;
      if (this.shouldLayout(this.mMenuView)) {
         this.measureChildConstrained(this.mMenuView, var1, var6, var2, 0, this.mMaxButtonHeight);
         var7 = this.mMenuView.getMeasuredWidth();
         var14 = this.getHorizontalMargins(this.mMenuView);
         var4 = Math.max(var4, this.mMenuView.getMeasuredHeight() + this.getVerticalMargins(this.mMenuView));
         var5 = View.combineMeasuredStates(var3, this.mMenuView.getMeasuredState());
         var3 = var7 + var14;
      } else {
         var5 = var3;
         var3 = 0;
      }

      var14 = this.getCurrentContentInsetEnd();
      var7 = var6 + Math.max(var14, var3);
      var11[var8] = Math.max(0, var14 - var3);
      if (this.shouldLayout(this.mExpandedActionView)) {
         var7 += this.measureChildCollapseMargins(this.mExpandedActionView, var1, var7, var2, 0, var11);
         var6 = Math.max(var4, this.mExpandedActionView.getMeasuredHeight() + this.getVerticalMargins(this.mExpandedActionView));
         var5 = View.combineMeasuredStates(var5, this.mExpandedActionView.getMeasuredState());
      } else {
         var6 = var4;
      }

      int var13 = var7;
      var14 = var6;
      var4 = var5;
      if (this.shouldLayout(this.mLogoView)) {
         var13 = var7 + this.measureChildCollapseMargins(this.mLogoView, var1, var7, var2, 0, var11);
         var14 = Math.max(var6, this.mLogoView.getMeasuredHeight() + this.getVerticalMargins(this.mLogoView));
         var4 = View.combineMeasuredStates(var5, this.mLogoView.getMeasuredState());
      }

      var6 = this.getChildCount();
      var7 = 0;

      for(var3 = var14; var7 < var6; ++var7) {
         View var12 = this.getChildAt(var7);
         if (((Toolbar.LayoutParams)var12.getLayoutParams()).mViewType == 0 && this.shouldLayout(var12)) {
            var13 += this.measureChildCollapseMargins(var12, var1, var13, var2, 0, var11);
            var3 = Math.max(var3, var12.getMeasuredHeight() + this.getVerticalMargins(var12));
            var4 = View.combineMeasuredStates(var4, var12.getMeasuredState());
         }
      }

      var7 = 0;
      var6 = 0;
      var14 = this.mTitleMarginTop + this.mTitleMarginBottom;
      int var10 = this.mTitleMarginStart + this.mTitleMarginEnd;
      var5 = var4;
      if (this.shouldLayout(this.mTitleTextView)) {
         this.measureChildCollapseMargins(this.mTitleTextView, var1, var13 + var10, var2, var14, var11);
         var7 = this.mTitleTextView.getMeasuredWidth() + this.getHorizontalMargins(this.mTitleTextView);
         var6 = this.mTitleTextView.getMeasuredHeight() + this.getVerticalMargins(this.mTitleTextView);
         var5 = View.combineMeasuredStates(var4, this.mTitleTextView.getMeasuredState());
      }

      if (this.shouldLayout(this.mSubtitleTextView)) {
         var7 = Math.max(var7, this.measureChildCollapseMargins(this.mSubtitleTextView, var1, var13 + var10, var2, var6 + var14, var11));
         var4 = this.mSubtitleTextView.getMeasuredHeight();
         var14 = this.getVerticalMargins(this.mSubtitleTextView);
         var5 = View.combineMeasuredStates(var5, this.mSubtitleTextView.getMeasuredState());
         var6 += var4 + var14;
      }

      var3 = Math.max(var3, var6);
      var14 = this.getPaddingLeft();
      var10 = this.getPaddingRight();
      var4 = this.getPaddingTop();
      var6 = this.getPaddingBottom();
      var7 = View.resolveSizeAndState(Math.max(var13 + var7 + var14 + var10, this.getSuggestedMinimumWidth()), var1, -16777216 & var5);
      var1 = View.resolveSizeAndState(Math.max(var3 + var4 + var6, this.getSuggestedMinimumHeight()), var2, var5 << 16);
      if (this.shouldCollapse()) {
         var1 = 0;
      }

      this.setMeasuredDimension(var7, var1);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof Toolbar.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         Toolbar.SavedState var2 = (Toolbar.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         ActionMenuView var3 = this.mMenuView;
         MenuBuilder var4;
         if (var3 != null) {
            var4 = var3.peekMenu();
         } else {
            var4 = null;
         }

         if (var2.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && var4 != null) {
            MenuItem var5 = var4.findItem(var2.expandedMenuItemId);
            if (var5 != null) {
               var5.expandActionView();
            }
         }

         if (var2.isOverflowOpen) {
            this.postShowOverflowMenu();
         }

      }
   }

   public void onRtlPropertiesChanged(int var1) {
      if (VERSION.SDK_INT >= 17) {
         super.onRtlPropertiesChanged(var1);
      }

      this.ensureContentInsets();
      RtlSpacingHelper var3 = this.mContentInsets;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      var3.setDirection(var2);
   }

   protected Parcelable onSaveInstanceState() {
      Toolbar.SavedState var1 = new Toolbar.SavedState(super.onSaveInstanceState());
      Toolbar.ExpandedActionViewMenuPresenter var2 = this.mExpandedMenuPresenter;
      if (var2 != null && var2.mCurrentExpandedItem != null) {
         var1.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
      }

      var1.isOverflowOpen = this.isOverflowMenuShowing();
      return var1;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 == 0) {
         this.mEatingTouch = false;
      }

      if (!this.mEatingTouch) {
         boolean var3 = super.onTouchEvent(var1);
         if (var2 == 0 && !var3) {
            this.mEatingTouch = true;
         }
      }

      if (var2 == 1 || var2 == 3) {
         this.mEatingTouch = false;
      }

      return true;
   }

   void removeChildrenForExpandedActionView() {
      for(int var1 = this.getChildCount() - 1; var1 >= 0; --var1) {
         View var2 = this.getChildAt(var1);
         if (((Toolbar.LayoutParams)var2.getLayoutParams()).mViewType != 2 && var2 != this.mMenuView) {
            this.removeViewAt(var1);
            this.mHiddenViews.add(var2);
         }
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setCollapsible(boolean var1) {
      this.mCollapsible = var1;
      this.requestLayout();
   }

   public void setContentInsetEndWithActions(int var1) {
      int var2 = var1;
      if (var1 < 0) {
         var2 = Integer.MIN_VALUE;
      }

      if (var2 != this.mContentInsetEndWithActions) {
         this.mContentInsetEndWithActions = var2;
         if (this.getNavigationIcon() != null) {
            this.requestLayout();
         }
      }

   }

   public void setContentInsetStartWithNavigation(int var1) {
      int var2 = var1;
      if (var1 < 0) {
         var2 = Integer.MIN_VALUE;
      }

      if (var2 != this.mContentInsetStartWithNavigation) {
         this.mContentInsetStartWithNavigation = var2;
         if (this.getNavigationIcon() != null) {
            this.requestLayout();
         }
      }

   }

   public void setContentInsetsAbsolute(int var1, int var2) {
      this.ensureContentInsets();
      this.mContentInsets.setAbsolute(var1, var2);
   }

   public void setContentInsetsRelative(int var1, int var2) {
      this.ensureContentInsets();
      this.mContentInsets.setRelative(var1, var2);
   }

   public void setLogo(@DrawableRes int var1) {
      this.setLogo(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setLogo(Drawable var1) {
      ImageView var2;
      if (var1 != null) {
         this.ensureLogoView();
         if (!this.isChildOrHidden(this.mLogoView)) {
            this.addSystemView(this.mLogoView, true);
         }
      } else {
         var2 = this.mLogoView;
         if (var2 != null && this.isChildOrHidden(var2)) {
            this.removeView(this.mLogoView);
            this.mHiddenViews.remove(this.mLogoView);
         }
      }

      var2 = this.mLogoView;
      if (var2 != null) {
         var2.setImageDrawable(var1);
      }

   }

   public void setLogoDescription(@StringRes int var1) {
      this.setLogoDescription(this.getContext().getText(var1));
   }

   public void setLogoDescription(CharSequence var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.ensureLogoView();
      }

      ImageView var2 = this.mLogoView;
      if (var2 != null) {
         var2.setContentDescription(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setMenu(MenuBuilder var1, ActionMenuPresenter var2) {
      if (var1 != null || this.mMenuView != null) {
         this.ensureMenuView();
         MenuBuilder var3 = this.mMenuView.peekMenu();
         if (var3 != var1) {
            if (var3 != null) {
               var3.removeMenuPresenter(this.mOuterActionMenuPresenter);
               var3.removeMenuPresenter(this.mExpandedMenuPresenter);
            }

            if (this.mExpandedMenuPresenter == null) {
               this.mExpandedMenuPresenter = new Toolbar.ExpandedActionViewMenuPresenter();
            }

            var2.setExpandedActionViewsExclusive(true);
            if (var1 != null) {
               var1.addMenuPresenter(var2, this.mPopupContext);
               var1.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
            } else {
               var2.initForMenu(this.mPopupContext, (MenuBuilder)null);
               this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, (MenuBuilder)null);
               var2.updateMenuView(true);
               this.mExpandedMenuPresenter.updateMenuView(true);
            }

            this.mMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setPresenter(var2);
            this.mOuterActionMenuPresenter = var2;
         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setMenuCallbacks(MenuPresenter.Callback var1, MenuBuilder.Callback var2) {
      this.mActionMenuPresenterCallback = var1;
      this.mMenuBuilderCallback = var2;
      ActionMenuView var3 = this.mMenuView;
      if (var3 != null) {
         var3.setMenuCallbacks(var1, var2);
      }

   }

   public void setNavigationContentDescription(@StringRes int var1) {
      CharSequence var2;
      if (var1 != 0) {
         var2 = this.getContext().getText(var1);
      } else {
         var2 = null;
      }

      this.setNavigationContentDescription(var2);
   }

   public void setNavigationContentDescription(@Nullable CharSequence var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.ensureNavButtonView();
      }

      ImageButton var2 = this.mNavButtonView;
      if (var2 != null) {
         var2.setContentDescription(var1);
      }

   }

   public void setNavigationIcon(@DrawableRes int var1) {
      this.setNavigationIcon(AppCompatResources.getDrawable(this.getContext(), var1));
   }

   public void setNavigationIcon(@Nullable Drawable var1) {
      ImageButton var2;
      if (var1 != null) {
         this.ensureNavButtonView();
         if (!this.isChildOrHidden(this.mNavButtonView)) {
            this.addSystemView(this.mNavButtonView, true);
         }
      } else {
         var2 = this.mNavButtonView;
         if (var2 != null && this.isChildOrHidden(var2)) {
            this.removeView(this.mNavButtonView);
            this.mHiddenViews.remove(this.mNavButtonView);
         }
      }

      var2 = this.mNavButtonView;
      if (var2 != null) {
         var2.setImageDrawable(var1);
      }

   }

   public void setNavigationOnClickListener(OnClickListener var1) {
      this.ensureNavButtonView();
      this.mNavButtonView.setOnClickListener(var1);
   }

   public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener var1) {
      this.mOnMenuItemClickListener = var1;
   }

   public void setOverflowIcon(@Nullable Drawable var1) {
      this.ensureMenu();
      this.mMenuView.setOverflowIcon(var1);
   }

   public void setPopupTheme(@StyleRes int var1) {
      if (this.mPopupTheme != var1) {
         this.mPopupTheme = var1;
         if (var1 == 0) {
            this.mPopupContext = this.getContext();
            return;
         }

         this.mPopupContext = new ContextThemeWrapper(this.getContext(), var1);
      }

   }

   public void setSubtitle(@StringRes int var1) {
      this.setSubtitle(this.getContext().getText(var1));
   }

   public void setSubtitle(CharSequence var1) {
      TextView var4;
      if (!TextUtils.isEmpty(var1)) {
         if (this.mSubtitleTextView == null) {
            Context var3 = this.getContext();
            this.mSubtitleTextView = new AppCompatTextView(var3);
            this.mSubtitleTextView.setSingleLine();
            this.mSubtitleTextView.setEllipsize(TruncateAt.END);
            int var2 = this.mSubtitleTextAppearance;
            if (var2 != 0) {
               this.mSubtitleTextView.setTextAppearance(var3, var2);
            }

            var2 = this.mSubtitleTextColor;
            if (var2 != 0) {
               this.mSubtitleTextView.setTextColor(var2);
            }
         }

         if (!this.isChildOrHidden(this.mSubtitleTextView)) {
            this.addSystemView(this.mSubtitleTextView, true);
         }
      } else {
         var4 = this.mSubtitleTextView;
         if (var4 != null && this.isChildOrHidden(var4)) {
            this.removeView(this.mSubtitleTextView);
            this.mHiddenViews.remove(this.mSubtitleTextView);
         }
      }

      var4 = this.mSubtitleTextView;
      if (var4 != null) {
         var4.setText(var1);
      }

      this.mSubtitleText = var1;
   }

   public void setSubtitleTextAppearance(Context var1, @StyleRes int var2) {
      this.mSubtitleTextAppearance = var2;
      TextView var3 = this.mSubtitleTextView;
      if (var3 != null) {
         var3.setTextAppearance(var1, var2);
      }

   }

   public void setSubtitleTextColor(@ColorInt int var1) {
      this.mSubtitleTextColor = var1;
      TextView var2 = this.mSubtitleTextView;
      if (var2 != null) {
         var2.setTextColor(var1);
      }

   }

   public void setTitle(@StringRes int var1) {
      this.setTitle(this.getContext().getText(var1));
   }

   public void setTitle(CharSequence var1) {
      TextView var4;
      if (!TextUtils.isEmpty(var1)) {
         if (this.mTitleTextView == null) {
            Context var3 = this.getContext();
            this.mTitleTextView = new AppCompatTextView(var3);
            this.mTitleTextView.setSingleLine();
            this.mTitleTextView.setEllipsize(TruncateAt.END);
            int var2 = this.mTitleTextAppearance;
            if (var2 != 0) {
               this.mTitleTextView.setTextAppearance(var3, var2);
            }

            var2 = this.mTitleTextColor;
            if (var2 != 0) {
               this.mTitleTextView.setTextColor(var2);
            }
         }

         if (!this.isChildOrHidden(this.mTitleTextView)) {
            this.addSystemView(this.mTitleTextView, true);
         }
      } else {
         var4 = this.mTitleTextView;
         if (var4 != null && this.isChildOrHidden(var4)) {
            this.removeView(this.mTitleTextView);
            this.mHiddenViews.remove(this.mTitleTextView);
         }
      }

      var4 = this.mTitleTextView;
      if (var4 != null) {
         var4.setText(var1);
      }

      this.mTitleText = var1;
   }

   public void setTitleMargin(int var1, int var2, int var3, int var4) {
      this.mTitleMarginStart = var1;
      this.mTitleMarginTop = var2;
      this.mTitleMarginEnd = var3;
      this.mTitleMarginBottom = var4;
      this.requestLayout();
   }

   public void setTitleMarginBottom(int var1) {
      this.mTitleMarginBottom = var1;
      this.requestLayout();
   }

   public void setTitleMarginEnd(int var1) {
      this.mTitleMarginEnd = var1;
      this.requestLayout();
   }

   public void setTitleMarginStart(int var1) {
      this.mTitleMarginStart = var1;
      this.requestLayout();
   }

   public void setTitleMarginTop(int var1) {
      this.mTitleMarginTop = var1;
      this.requestLayout();
   }

   public void setTitleTextAppearance(Context var1, @StyleRes int var2) {
      this.mTitleTextAppearance = var2;
      TextView var3 = this.mTitleTextView;
      if (var3 != null) {
         var3.setTextAppearance(var1, var2);
      }

   }

   public void setTitleTextColor(@ColorInt int var1) {
      this.mTitleTextColor = var1;
      TextView var2 = this.mTitleTextView;
      if (var2 != null) {
         var2.setTextColor(var1);
      }

   }

   public boolean showOverflowMenu() {
      ActionMenuView var1 = this.mMenuView;
      return var1 != null && var1.showOverflowMenu();
   }

   private class ExpandedActionViewMenuPresenter implements MenuPresenter {
      MenuItemImpl mCurrentExpandedItem;
      MenuBuilder mMenu;

      ExpandedActionViewMenuPresenter() {
      }

      public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
         if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
            ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed();
         }

         Toolbar var3 = Toolbar.this;
         var3.removeView(var3.mExpandedActionView);
         var3 = Toolbar.this;
         var3.removeView(var3.mCollapseButtonView);
         var3 = Toolbar.this;
         var3.mExpandedActionView = null;
         var3.addChildrenForExpandedActionView();
         this.mCurrentExpandedItem = null;
         Toolbar.this.requestLayout();
         var2.setActionViewExpanded(false);
         return true;
      }

      public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
         Toolbar.this.ensureCollapseButtonView();
         ViewParent var4 = Toolbar.this.mCollapseButtonView.getParent();
         Toolbar var3 = Toolbar.this;
         if (var4 != var3) {
            var3.addView(var3.mCollapseButtonView);
         }

         Toolbar.this.mExpandedActionView = var2.getActionView();
         this.mCurrentExpandedItem = var2;
         var4 = Toolbar.this.mExpandedActionView.getParent();
         var3 = Toolbar.this;
         if (var4 != var3) {
            Toolbar.LayoutParams var5 = var3.generateDefaultLayoutParams();
            var5.gravity = 8388611 | Toolbar.this.mButtonGravity & 112;
            var5.mViewType = 2;
            Toolbar.this.mExpandedActionView.setLayoutParams(var5);
            Toolbar var6 = Toolbar.this;
            var6.addView(var6.mExpandedActionView);
         }

         Toolbar.this.removeChildrenForExpandedActionView();
         Toolbar.this.requestLayout();
         var2.setActionViewExpanded(true);
         if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
            ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded();
         }

         return true;
      }

      public boolean flagActionItems() {
         return false;
      }

      public int getId() {
         return 0;
      }

      public MenuView getMenuView(ViewGroup var1) {
         return null;
      }

      public void initForMenu(Context var1, MenuBuilder var2) {
         MenuBuilder var4 = this.mMenu;
         if (var4 != null) {
            MenuItemImpl var3 = this.mCurrentExpandedItem;
            if (var3 != null) {
               var4.collapseItemActionView(var3);
            }
         }

         this.mMenu = var2;
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public void onRestoreInstanceState(Parcelable var1) {
      }

      public Parcelable onSaveInstanceState() {
         return null;
      }

      public boolean onSubMenuSelected(SubMenuBuilder var1) {
         return false;
      }

      public void setCallback(MenuPresenter.Callback var1) {
      }

      public void updateMenuView(boolean var1) {
         if (this.mCurrentExpandedItem != null) {
            boolean var4 = false;
            MenuBuilder var6 = this.mMenu;
            boolean var3 = var4;
            if (var6 != null) {
               int var5 = var6.size();
               int var2 = 0;

               while(true) {
                  var3 = var4;
                  if (var2 >= var5) {
                     break;
                  }

                  if (this.mMenu.getItem(var2) == this.mCurrentExpandedItem) {
                     var3 = true;
                     break;
                  }

                  ++var2;
               }
            }

            if (!var3) {
               this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
            }
         }

      }
   }

   public static class LayoutParams extends ActionBar.LayoutParams {
      static final int CUSTOM = 0;
      static final int EXPANDED = 2;
      static final int SYSTEM = 1;
      int mViewType;

      public LayoutParams(int var1) {
         this(-2, -1, var1);
      }

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.mViewType = 0;
         this.gravity = 8388627;
      }

      public LayoutParams(int var1, int var2, int var3) {
         super(var1, var2);
         this.mViewType = 0;
         this.gravity = var3;
      }

      public LayoutParams(@NonNull Context var1, AttributeSet var2) {
         super(var1, var2);
         this.mViewType = 0;
      }

      public LayoutParams(ActionBar.LayoutParams var1) {
         super(var1);
         this.mViewType = 0;
      }

      public LayoutParams(Toolbar.LayoutParams var1) {
         super((ActionBar.LayoutParams)var1);
         this.mViewType = 0;
         this.mViewType = var1.mViewType;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
         this.mViewType = 0;
      }

      public LayoutParams(MarginLayoutParams var1) {
         super((android.view.ViewGroup.LayoutParams)var1);
         this.mViewType = 0;
         this.copyMarginsFromCompat(var1);
      }

      void copyMarginsFromCompat(MarginLayoutParams var1) {
         this.leftMargin = var1.leftMargin;
         this.topMargin = var1.topMargin;
         this.rightMargin = var1.rightMargin;
         this.bottomMargin = var1.bottomMargin;
      }
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public Toolbar.SavedState createFromParcel(Parcel var1) {
            return new Toolbar.SavedState(var1, (ClassLoader)null);
         }

         public Toolbar.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new Toolbar.SavedState(var1, var2);
         }

         public Toolbar.SavedState[] newArray(int var1) {
            return new Toolbar.SavedState[var1];
         }
      };
      int expandedMenuItemId;
      boolean isOverflowOpen;

      public SavedState(Parcel var1) {
         this(var1, (ClassLoader)null);
      }

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.expandedMenuItemId = var1.readInt();
         boolean var3;
         if (var1.readInt() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isOverflowOpen = var3;
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }
}
