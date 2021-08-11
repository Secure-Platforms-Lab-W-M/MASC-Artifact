package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.R.attr;
import androidx.appcompat.R.id;
import androidx.appcompat.R.layout;
import androidx.appcompat.R.styleable;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.view.ViewCompat;

public class ActionBarContextView extends AbsActionBarView {
   private static final String TAG = "ActionBarContextView";
   private View mClose;
   private int mCloseItemLayout;
   private View mCustomView;
   private CharSequence mSubtitle;
   private int mSubtitleStyleRes;
   private TextView mSubtitleView;
   private CharSequence mTitle;
   private LinearLayout mTitleLayout;
   private boolean mTitleOptional;
   private int mTitleStyleRes;
   private TextView mTitleView;

   public ActionBarContextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ActionBarContextView(Context var1, AttributeSet var2) {
      this(var1, var2, attr.actionModeStyle);
   }

   public ActionBarContextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, var2, styleable.ActionMode, var3, 0);
      ViewCompat.setBackground(this, var4.getDrawable(styleable.ActionMode_background));
      this.mTitleStyleRes = var4.getResourceId(styleable.ActionMode_titleTextStyle, 0);
      this.mSubtitleStyleRes = var4.getResourceId(styleable.ActionMode_subtitleTextStyle, 0);
      this.mContentHeight = var4.getLayoutDimension(styleable.ActionMode_height, 0);
      this.mCloseItemLayout = var4.getResourceId(styleable.ActionMode_closeItemLayout, layout.abc_action_mode_close_item_material);
      var4.recycle();
   }

   private void initTitle() {
      LinearLayout var5;
      if (this.mTitleLayout == null) {
         LayoutInflater.from(this.getContext()).inflate(layout.abc_action_bar_title_item, this);
         var5 = (LinearLayout)this.getChildAt(this.getChildCount() - 1);
         this.mTitleLayout = var5;
         this.mTitleView = (TextView)var5.findViewById(id.action_bar_title);
         this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(id.action_bar_subtitle);
         if (this.mTitleStyleRes != 0) {
            this.mTitleView.setTextAppearance(this.getContext(), this.mTitleStyleRes);
         }

         if (this.mSubtitleStyleRes != 0) {
            this.mSubtitleView.setTextAppearance(this.getContext(), this.mSubtitleStyleRes);
         }
      }

      this.mTitleView.setText(this.mTitle);
      this.mSubtitleView.setText(this.mSubtitle);
      boolean var4 = TextUtils.isEmpty(this.mTitle);
      boolean var3 = TextUtils.isEmpty(this.mSubtitle) ^ true;
      TextView var6 = this.mSubtitleView;
      byte var2 = 0;
      byte var1;
      if (var3) {
         var1 = 0;
      } else {
         var1 = 8;
      }

      var6.setVisibility(var1);
      var5 = this.mTitleLayout;
      var1 = var2;
      if (!(var4 ^ true)) {
         if (var3) {
            var1 = var2;
         } else {
            var1 = 8;
         }
      }

      var5.setVisibility(var1);
      if (this.mTitleLayout.getParent() == null) {
         this.addView(this.mTitleLayout);
      }

   }

   public void closeMode() {
      if (this.mClose == null) {
         this.killMode();
      }
   }

   protected LayoutParams generateDefaultLayoutParams() {
      return new MarginLayoutParams(-1, -2);
   }

   public LayoutParams generateLayoutParams(AttributeSet var1) {
      return new MarginLayoutParams(this.getContext(), var1);
   }

   public CharSequence getSubtitle() {
      return this.mSubtitle;
   }

   public CharSequence getTitle() {
      return this.mTitle;
   }

   public boolean hideOverflowMenu() {
      return this.mActionMenuPresenter != null ? this.mActionMenuPresenter.hideOverflowMenu() : false;
   }

   public void initForMode(final ActionMode var1) {
      View var2 = this.mClose;
      if (var2 == null) {
         var2 = LayoutInflater.from(this.getContext()).inflate(this.mCloseItemLayout, this, false);
         this.mClose = var2;
         this.addView(var2);
      } else if (var2.getParent() == null) {
         this.addView(this.mClose);
      }

      this.mClose.findViewById(id.action_mode_close_button).setOnClickListener(new OnClickListener() {
         public void onClick(View var1x) {
            var1.finish();
         }
      });
      MenuBuilder var3 = (MenuBuilder)var1.getMenu();
      if (this.mActionMenuPresenter != null) {
         this.mActionMenuPresenter.dismissPopupMenus();
      }

      this.mActionMenuPresenter = new ActionMenuPresenter(this.getContext());
      this.mActionMenuPresenter.setReserveOverflow(true);
      LayoutParams var4 = new LayoutParams(-2, -1);
      var3.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
      this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
      ViewCompat.setBackground(this.mMenuView, (Drawable)null);
      this.addView(this.mMenuView, var4);
   }

   public boolean isOverflowMenuShowing() {
      return this.mActionMenuPresenter != null ? this.mActionMenuPresenter.isOverflowMenuShowing() : false;
   }

   public boolean isTitleOptional() {
      return this.mTitleOptional;
   }

   public void killMode() {
      this.removeAllViews();
      this.mCustomView = null;
      this.mMenuView = null;
   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      if (this.mActionMenuPresenter != null) {
         this.mActionMenuPresenter.hideOverflowMenu();
         this.mActionMenuPresenter.hideSubMenus();
      }

   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      if (var1.getEventType() == 32) {
         var1.setSource(this);
         var1.setClassName(this.getClass().getName());
         var1.setPackageName(this.getContext().getPackageName());
         var1.setContentDescription(this.mTitle);
      } else {
         super.onInitializeAccessibilityEvent(var1);
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var1 = ViewUtils.isLayoutRtl(this);
      int var6;
      if (var1) {
         var6 = var4 - var2 - this.getPaddingRight();
      } else {
         var6 = this.getPaddingLeft();
      }

      int var7 = this.getPaddingTop();
      int var8 = var5 - var3 - this.getPaddingTop() - this.getPaddingBottom();
      View var9 = this.mClose;
      if (var9 != null && var9.getVisibility() != 8) {
         MarginLayoutParams var10 = (MarginLayoutParams)this.mClose.getLayoutParams();
         if (var1) {
            var3 = var10.rightMargin;
         } else {
            var3 = var10.leftMargin;
         }

         if (var1) {
            var5 = var10.leftMargin;
         } else {
            var5 = var10.rightMargin;
         }

         var3 = next(var6, var3, var1);
         var3 = next(var3 + this.positionChild(this.mClose, var3, var7, var8, var1), var5, var1);
      } else {
         var3 = var6;
      }

      LinearLayout var11 = this.mTitleLayout;
      var5 = var3;
      if (var11 != null) {
         var5 = var3;
         if (this.mCustomView == null) {
            var5 = var3;
            if (var11.getVisibility() != 8) {
               var5 = var3 + this.positionChild(this.mTitleLayout, var3, var7, var8, var1);
            }
         }
      }

      var9 = this.mCustomView;
      if (var9 != null) {
         this.positionChild(var9, var5, var7, var8, var1);
      }

      if (var1) {
         var2 = this.getPaddingLeft();
      } else {
         var2 = var4 - var2 - this.getPaddingRight();
      }

      if (this.mMenuView != null) {
         this.positionChild(this.mMenuView, var2, var7, var8, var1 ^ true);
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      int var5 = 1073741824;
      StringBuilder var11;
      if (var3 == 1073741824) {
         if (MeasureSpec.getMode(var2) != 0) {
            int var8 = MeasureSpec.getSize(var1);
            if (this.mContentHeight > 0) {
               var3 = this.mContentHeight;
            } else {
               var3 = MeasureSpec.getSize(var2);
            }

            int var9 = this.getPaddingTop() + this.getPaddingBottom();
            var1 = var8 - this.getPaddingLeft() - this.getPaddingRight();
            int var6 = var3 - var9;
            int var4 = MeasureSpec.makeMeasureSpec(var6, Integer.MIN_VALUE);
            View var14 = this.mClose;
            byte var7 = 0;
            var2 = var1;
            if (var14 != null) {
               var1 = this.measureChildView(var14, var1, var4, 0);
               MarginLayoutParams var15 = (MarginLayoutParams)this.mClose.getLayoutParams();
               var2 = var1 - (var15.leftMargin + var15.rightMargin);
            }

            var1 = var2;
            if (this.mMenuView != null) {
               var1 = var2;
               if (this.mMenuView.getParent() == this) {
                  var1 = this.measureChildView(this.mMenuView, var2, var4, 0);
               }
            }

            LinearLayout var16 = this.mTitleLayout;
            var2 = var1;
            if (var16 != null) {
               var2 = var1;
               if (this.mCustomView == null) {
                  if (this.mTitleOptional) {
                     var2 = MeasureSpec.makeMeasureSpec(0, 0);
                     this.mTitleLayout.measure(var2, var4);
                     int var10 = this.mTitleLayout.getMeasuredWidth();
                     boolean var13;
                     if (var10 <= var1) {
                        var13 = true;
                     } else {
                        var13 = false;
                     }

                     var2 = var1;
                     if (var13) {
                        var2 = var1 - var10;
                     }

                     var16 = this.mTitleLayout;
                     byte var12;
                     if (var13) {
                        var12 = var7;
                     } else {
                        var12 = 8;
                     }

                     var16.setVisibility(var12);
                  } else {
                     var2 = this.measureChildView(var16, var1, var4, 0);
                  }
               }
            }

            var14 = this.mCustomView;
            if (var14 != null) {
               LayoutParams var17 = var14.getLayoutParams();
               if (var17.width != -2) {
                  var1 = 1073741824;
               } else {
                  var1 = Integer.MIN_VALUE;
               }

               if (var17.width >= 0) {
                  var2 = Math.min(var17.width, var2);
               }

               if (var17.height != -2) {
                  var4 = var5;
               } else {
                  var4 = Integer.MIN_VALUE;
               }

               if (var17.height >= 0) {
                  var5 = Math.min(var17.height, var6);
               } else {
                  var5 = var6;
               }

               this.mCustomView.measure(MeasureSpec.makeMeasureSpec(var2, var1), MeasureSpec.makeMeasureSpec(var5, var4));
            }

            if (this.mContentHeight <= 0) {
               var2 = 0;
               var5 = this.getChildCount();

               for(var1 = 0; var1 < var5; var2 = var3) {
                  var4 = this.getChildAt(var1).getMeasuredHeight() + var9;
                  var3 = var2;
                  if (var4 > var2) {
                     var3 = var4;
                  }

                  ++var1;
               }

               this.setMeasuredDimension(var8, var2);
            } else {
               this.setMeasuredDimension(var8, var3);
            }
         } else {
            var11 = new StringBuilder();
            var11.append(this.getClass().getSimpleName());
            var11.append(" can only be used with android:layout_height=\"wrap_content\"");
            throw new IllegalStateException(var11.toString());
         }
      } else {
         var11 = new StringBuilder();
         var11.append(this.getClass().getSimpleName());
         var11.append(" can only be used with android:layout_width=\"match_parent\" (or fill_parent)");
         throw new IllegalStateException(var11.toString());
      }
   }

   public void setContentHeight(int var1) {
      this.mContentHeight = var1;
   }

   public void setCustomView(View var1) {
      View var2 = this.mCustomView;
      if (var2 != null) {
         this.removeView(var2);
      }

      this.mCustomView = var1;
      if (var1 != null) {
         LinearLayout var3 = this.mTitleLayout;
         if (var3 != null) {
            this.removeView(var3);
            this.mTitleLayout = null;
         }
      }

      if (var1 != null) {
         this.addView(var1);
      }

      this.requestLayout();
   }

   public void setSubtitle(CharSequence var1) {
      this.mSubtitle = var1;
      this.initTitle();
   }

   public void setTitle(CharSequence var1) {
      this.mTitle = var1;
      this.initTitle();
   }

   public void setTitleOptional(boolean var1) {
      if (var1 != this.mTitleOptional) {
         this.requestLayout();
      }

      this.mTitleOptional = var1;
   }

   public boolean shouldDelayChildPressedState() {
      return false;
   }

   public boolean showOverflowMenu() {
      return this.mActionMenuPresenter != null ? this.mActionMenuPresenter.showOverflowMenu() : false;
   }
}
