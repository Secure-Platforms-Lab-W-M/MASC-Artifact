/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.text.Layout
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.util.AttributeSet
 *  android.view.ContextThemeWrapper
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.widget.ImageButton
 *  android.widget.ImageView
 *  android.widget.TextView
 *  androidx.appcompat.R
 *  androidx.appcompat.R$attr
 *  androidx.appcompat.R$styleable
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionMenuPresenter;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.DecorToolbar;
import androidx.appcompat.widget.RtlSpacingHelper;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import java.util.ArrayList;
import java.util.List;

public class Toolbar
extends ViewGroup {
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
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity = 8388627;
    private final ArrayList<View> mHiddenViews = new ArrayList();
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private ColorStateList mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins = new int[2];
    private final ArrayList<View> mTempViews = new ArrayList();
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private ColorStateList mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.toolbarStyle);
    }

    public Toolbar(Context object, AttributeSet object2, int n) {
        int n2;
        super((Context)object, (AttributeSet)object2, n);
        this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (Toolbar.this.mOnMenuItemClickListener != null) {
                    return Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
                }
                return false;
            }
        };
        this.mShowOverflowMenuRunnable = new Runnable(){

            @Override
            public void run() {
                Toolbar.this.showOverflowMenu();
            }
        };
        object = TintTypedArray.obtainStyledAttributes(this.getContext(), (AttributeSet)object2, R.styleable.Toolbar, n, 0);
        this.mTitleTextAppearance = object.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = object.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = object.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = object.getInteger(R.styleable.Toolbar_buttonGravity, 48);
        n = n2 = object.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
        if (object.hasValue(R.styleable.Toolbar_titleMargins)) {
            n = object.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, n2);
        }
        this.mTitleMarginBottom = n;
        this.mTitleMarginTop = n;
        this.mTitleMarginEnd = n;
        this.mTitleMarginStart = n;
        n = object.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
        if (n >= 0) {
            this.mTitleMarginStart = n;
        }
        if ((n = object.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1)) >= 0) {
            this.mTitleMarginEnd = n;
        }
        if ((n = object.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1)) >= 0) {
            this.mTitleMarginTop = n;
        }
        if ((n = object.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1)) >= 0) {
            this.mTitleMarginBottom = n;
        }
        this.mMaxButtonHeight = object.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
        n = object.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        n2 = object.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        int n3 = object.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
        int n4 = object.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
        this.ensureContentInsets();
        this.mContentInsets.setAbsolute(n3, n4);
        if (n != Integer.MIN_VALUE || n2 != Integer.MIN_VALUE) {
            this.mContentInsets.setRelative(n, n2);
        }
        this.mContentInsetStartWithNavigation = object.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = object.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        this.mCollapseIcon = object.getDrawable(R.styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = object.getText(R.styleable.Toolbar_collapseContentDescription);
        object2 = object.getText(R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty((CharSequence)object2)) {
            this.setTitle((CharSequence)object2);
        }
        if (!TextUtils.isEmpty((CharSequence)(object2 = object.getText(R.styleable.Toolbar_subtitle)))) {
            this.setSubtitle((CharSequence)object2);
        }
        this.mPopupContext = this.getContext();
        this.setPopupTheme(object.getResourceId(R.styleable.Toolbar_popupTheme, 0));
        object2 = object.getDrawable(R.styleable.Toolbar_navigationIcon);
        if (object2 != null) {
            this.setNavigationIcon((Drawable)object2);
        }
        if (!TextUtils.isEmpty((CharSequence)(object2 = object.getText(R.styleable.Toolbar_navigationContentDescription)))) {
            this.setNavigationContentDescription((CharSequence)object2);
        }
        if ((object2 = object.getDrawable(R.styleable.Toolbar_logo)) != null) {
            this.setLogo((Drawable)object2);
        }
        if (!TextUtils.isEmpty((CharSequence)(object2 = object.getText(R.styleable.Toolbar_logoDescription)))) {
            this.setLogoDescription((CharSequence)object2);
        }
        if (object.hasValue(R.styleable.Toolbar_titleTextColor)) {
            this.setTitleTextColor(object.getColorStateList(R.styleable.Toolbar_titleTextColor));
        }
        if (object.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
            this.setSubtitleTextColor(object.getColorStateList(R.styleable.Toolbar_subtitleTextColor));
        }
        if (object.hasValue(R.styleable.Toolbar_menu)) {
            this.inflateMenu(object.getResourceId(R.styleable.Toolbar_menu, 0));
        }
        object.recycle();
    }

    private void addCustomViewsWithGravity(List<View> list, int n) {
        int n2 = ViewCompat.getLayoutDirection((View)this);
        boolean bl = true;
        if (n2 != 1) {
            bl = false;
        }
        int n3 = this.getChildCount();
        n2 = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this));
        list.clear();
        if (bl) {
            for (n = n3 - 1; n >= 0; --n) {
                View view = this.getChildAt(n);
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (layoutParams.mViewType != 0 || !this.shouldLayout(view) || this.getChildHorizontalGravity(layoutParams.gravity) != n2) continue;
                list.add(view);
            }
            return;
        }
        for (n = 0; n < n3; ++n) {
            View view = this.getChildAt(n);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.mViewType != 0 || !this.shouldLayout(view) || this.getChildHorizontalGravity(layoutParams.gravity) != n2) continue;
            list.add(view);
        }
    }

    private void addSystemView(View view, boolean bl) {
        Object object = view.getLayoutParams();
        object = object == null ? this.generateDefaultLayoutParams() : (!this.checkLayoutParams((ViewGroup.LayoutParams)object) ? this.generateLayoutParams((ViewGroup.LayoutParams)object) : (LayoutParams)((Object)object));
        object.mViewType = 1;
        if (bl && this.mExpandedActionView != null) {
            view.setLayoutParams((ViewGroup.LayoutParams)object);
            this.mHiddenViews.add(view);
            return;
        }
        this.addView(view, (ViewGroup.LayoutParams)object);
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
            MenuBuilder menuBuilder = (MenuBuilder)this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            Object object = new ActionMenuView(this.getContext());
            this.mMenuView = object;
            object.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            object = this.generateDefaultLayoutParams();
            object.gravity = 8388613 | this.mButtonGravity & 112;
            this.mMenuView.setLayoutParams((ViewGroup.LayoutParams)object);
            this.addSystemView((View)this.mMenuView, false);
        }
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(this.getContext(), null, R.attr.toolbarNavigationButtonStyle);
            LayoutParams layoutParams = this.generateDefaultLayoutParams();
            layoutParams.gravity = 8388611 | this.mButtonGravity & 112;
            this.mNavButtonView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        }
    }

    private int getChildHorizontalGravity(int n) {
        int n2 = ViewCompat.getLayoutDirection((View)this);
        int n3 = GravityCompat.getAbsoluteGravity(n, n2) & 7;
        if (n3 != 1) {
            n = 3;
            if (n3 != 3 && n3 != 5) {
                if (n2 == 1) {
                    n = 5;
                }
                return n;
            }
        }
        return n3;
    }

    private int getChildTop(View view, int n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n2 = view.getMeasuredHeight();
        n = n > 0 ? (n2 - n) / 2 : 0;
        int n3 = this.getChildVerticalGravity(layoutParams.gravity);
        if (n3 != 48) {
            if (n3 != 80) {
                int n4 = this.getPaddingTop();
                n = this.getPaddingBottom();
                int n5 = this.getHeight();
                n3 = (n5 - n4 - n - n2) / 2;
                if (n3 < layoutParams.topMargin) {
                    n = layoutParams.topMargin;
                } else {
                    n2 = n5 - n - n2 - n3 - n4;
                    n = n3;
                    if (n2 < layoutParams.bottomMargin) {
                        n = Math.max(0, n3 - (layoutParams.bottomMargin - n2));
                    }
                }
                return n4 + n;
            }
            return this.getHeight() - this.getPaddingBottom() - n2 - layoutParams.bottomMargin - n;
        }
        return this.getPaddingTop() - n;
    }

    private int getChildVerticalGravity(int n) {
        if ((n &= 112) != 16 && n != 48 && n != 80) {
            return this.mGravity & 112;
        }
        return n;
    }

    private int getHorizontalMargins(View view) {
        view = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart((ViewGroup.MarginLayoutParams)view) + MarginLayoutParamsCompat.getMarginEnd((ViewGroup.MarginLayoutParams)view);
    }

    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.getContext());
    }

    private int getVerticalMargins(View view) {
        view = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        return view.topMargin + view.bottomMargin;
    }

    private int getViewListMeasuredWidth(List<View> list, int[] view) {
        int n = view[0];
        int n2 = view[1];
        int n3 = 0;
        int n4 = list.size();
        for (int i = 0; i < n4; ++i) {
            view = list.get(i);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            n = layoutParams.leftMargin - n;
            n2 = layoutParams.rightMargin - n2;
            int n5 = Math.max(0, n);
            int n6 = Math.max(0, n2);
            n = Math.max(0, - n);
            n2 = Math.max(0, - n2);
            n3 += view.getMeasuredWidth() + n5 + n6;
        }
        return n3;
    }

    private boolean isChildOrHidden(View view) {
        if (view.getParent() != this && !this.mHiddenViews.contains((Object)view)) {
            return false;
        }
        return true;
    }

    private static boolean isCustomView(View view) {
        if (((LayoutParams)view.getLayoutParams()).mViewType == 0) {
            return true;
        }
        return false;
    }

    private int layoutChildLeft(View view, int n, int[] arrn, int n2) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n3 = layoutParams.leftMargin - arrn[0];
        arrn[0] = Math.max(0, - n3);
        n2 = this.getChildTop(view, n2);
        n3 = view.getMeasuredWidth();
        view.layout(n, n2, (n += Math.max(0, n3)) + n3, view.getMeasuredHeight() + n2);
        return n + (layoutParams.rightMargin + n3);
    }

    private int layoutChildRight(View view, int n, int[] arrn, int n2) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n3 = layoutParams.rightMargin - arrn[1];
        arrn[1] = Math.max(0, - n3);
        n2 = this.getChildTop(view, n2);
        n3 = view.getMeasuredWidth();
        view.layout(n - n3, n2, n -= Math.max(0, n3), view.getMeasuredHeight() + n2);
        return n - (layoutParams.leftMargin + n3);
    }

    private int measureChildCollapseMargins(View view, int n, int n2, int n3, int n4, int[] arrn) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        int n5 = marginLayoutParams.leftMargin - arrn[0];
        int n6 = marginLayoutParams.rightMargin - arrn[1];
        int n7 = Math.max(0, n5) + Math.max(0, n6);
        arrn[0] = Math.max(0, - n5);
        arrn[1] = Math.max(0, - n6);
        view.measure(Toolbar.getChildMeasureSpec((int)n, (int)(this.getPaddingLeft() + this.getPaddingRight() + n7 + n2), (int)marginLayoutParams.width), Toolbar.getChildMeasureSpec((int)n3, (int)(this.getPaddingTop() + this.getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + n4), (int)marginLayoutParams.height));
        return view.getMeasuredWidth() + n7;
    }

    private void measureChildConstrained(View view, int n, int n2, int n3, int n4, int n5) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        int n6 = Toolbar.getChildMeasureSpec((int)n, (int)(this.getPaddingLeft() + this.getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + n2), (int)marginLayoutParams.width);
        n2 = Toolbar.getChildMeasureSpec((int)n3, (int)(this.getPaddingTop() + this.getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + n4), (int)marginLayoutParams.height);
        n3 = View.MeasureSpec.getMode((int)n2);
        n = n2;
        if (n3 != 1073741824) {
            n = n2;
            if (n5 >= 0) {
                if (n3 != 0) {
                    n5 = Math.min(View.MeasureSpec.getSize((int)n2), n5);
                }
                n = View.MeasureSpec.makeMeasureSpec((int)n5, (int)1073741824);
            }
        }
        view.measure(n6, n);
    }

    private void postShowOverflowMenu() {
        this.removeCallbacks(this.mShowOverflowMenuRunnable);
        this.post(this.mShowOverflowMenuRunnable);
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if (!this.shouldLayout(view) || view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) continue;
            return false;
        }
        return true;
    }

    private boolean shouldLayout(View view) {
        if (view != null && view.getParent() == this && view.getVisibility() != 8) {
            return true;
        }
        return false;
    }

    void addChildrenForExpandedActionView() {
        for (int i = this.mHiddenViews.size() - 1; i >= 0; --i) {
            this.addView(this.mHiddenViews.get(i));
        }
        this.mHiddenViews.clear();
    }

    public boolean canShowOverflowMenu() {
        ActionMenuView actionMenuView;
        if (this.getVisibility() == 0 && (actionMenuView = this.mMenuView) != null && actionMenuView.isOverflowReserved()) {
            return true;
        }
        return false;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (super.checkLayoutParams(layoutParams) && layoutParams instanceof LayoutParams) {
            return true;
        }
        return false;
    }

    public void collapseActionView() {
        Object object = this.mExpandedMenuPresenter;
        object = object == null ? null : object.mCurrentExpandedItem;
        if (object != null) {
            object.collapseActionView();
        }
    }

    public void dismissPopupMenus() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.dismissPopupMenus();
        }
    }

    void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            Object object = new AppCompatImageButton(this.getContext(), null, R.attr.toolbarNavigationButtonStyle);
            this.mCollapseButtonView = object;
            object.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            object = this.generateDefaultLayoutParams();
            object.gravity = 8388611 | this.mButtonGravity & 112;
            object.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams((ViewGroup.LayoutParams)object);
            this.mCollapseButtonView.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view) {
                    Toolbar.this.collapseActionView();
                }
            });
        }
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)layoutParams);
        }
        if (layoutParams instanceof ActionBar.LayoutParams) {
            return new LayoutParams((ActionBar.LayoutParams)layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public CharSequence getCollapseContentDescription() {
        ImageButton imageButton = this.mCollapseButtonView;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public Drawable getCollapseIcon() {
        ImageButton imageButton = this.mCollapseButtonView;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    public int getContentInsetEnd() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getEnd();
        }
        return 0;
    }

    public int getContentInsetEndWithActions() {
        int n = this.mContentInsetEndWithActions;
        if (n != Integer.MIN_VALUE) {
            return n;
        }
        return this.getContentInsetEnd();
    }

    public int getContentInsetLeft() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getLeft();
        }
        return 0;
    }

    public int getContentInsetRight() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getRight();
        }
        return 0;
    }

    public int getContentInsetStart() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getStart();
        }
        return 0;
    }

    public int getContentInsetStartWithNavigation() {
        int n = this.mContentInsetStartWithNavigation;
        if (n != Integer.MIN_VALUE) {
            return n;
        }
        return this.getContentInsetStart();
    }

    public int getCurrentContentInsetEnd() {
        boolean bl = false;
        Object object = this.mMenuView;
        if (object != null) {
            bl = (object = object.peekMenu()) != null && object.hasVisibleItems();
        }
        if (bl) {
            return Math.max(this.getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
        }
        return this.getContentInsetEnd();
    }

    public int getCurrentContentInsetLeft() {
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            return this.getCurrentContentInsetEnd();
        }
        return this.getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            return this.getCurrentContentInsetStart();
        }
        return this.getCurrentContentInsetEnd();
    }

    public int getCurrentContentInsetStart() {
        if (this.getNavigationIcon() != null) {
            return Math.max(this.getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        return this.getContentInsetStart();
    }

    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            return imageView.getDrawable();
        }
        return null;
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            return imageView.getContentDescription();
        }
        return null;
    }

    public Menu getMenu() {
        this.ensureMenu();
        return this.mMenuView.getMenu();
    }

    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }

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

    final TextView getSubtitleTextView() {
        return this.mSubtitleTextView;
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

    final TextView getTitleTextView() {
        return this.mTitleTextView;
    }

    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter != null && expandedActionViewMenuPresenter.mCurrentExpandedItem != null) {
            return true;
        }
        return false;
    }

    public boolean hideOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null && actionMenuView.hideOverflowMenu()) {
            return true;
        }
        return false;
    }

    public void inflateMenu(int n) {
        this.getMenuInflater().inflate(n, this.getMenu());
    }

    public boolean isOverflowMenuShowPending() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null && actionMenuView.isOverflowMenuShowPending()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null && actionMenuView.isOverflowMenuShowing()) {
            return true;
        }
        return false;
    }

    public boolean isTitleTruncated() {
        TextView textView = this.mTitleTextView;
        if (textView == null) {
            return false;
        }
        if ((textView = textView.getLayout()) == null) {
            return false;
        }
        int n = textView.getLineCount();
        for (int i = 0; i < n; ++i) {
            if (textView.getEllipsisCount(i) <= 0) continue;
            return true;
        }
        return false;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    public boolean onHoverEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        if (n == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            boolean bl = super.onHoverEvent(motionEvent);
            if (n == 9 && !bl) {
                this.mEatingHover = true;
            }
        }
        if (n == 10 || n == 3) {
            this.mEatingHover = false;
        }
        return true;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        LayoutParams layoutParams;
        int n5 = ViewCompat.getLayoutDirection((View)this) == 1 ? 1 : 0;
        int n6 = this.getWidth();
        int n7 = this.getHeight();
        int n8 = this.getPaddingLeft();
        int n9 = this.getPaddingRight();
        int n10 = this.getPaddingTop();
        int n11 = this.getPaddingBottom();
        int n12 = n8;
        int n13 = n6 - n9;
        int[] arrn = this.mTempMargins;
        arrn[1] = 0;
        arrn[0] = 0;
        n = ViewCompat.getMinimumHeight((View)this);
        n3 = n >= 0 ? Math.min(n, n4 - n2) : 0;
        n = n12;
        n2 = n13;
        if (this.shouldLayout((View)this.mNavButtonView)) {
            if (n5 != 0) {
                n2 = this.layoutChildRight((View)this.mNavButtonView, n13, arrn, n3);
                n = n12;
            } else {
                n = this.layoutChildLeft((View)this.mNavButtonView, n12, arrn, n3);
                n2 = n13;
            }
        }
        n4 = n;
        n12 = n2;
        if (this.shouldLayout((View)this.mCollapseButtonView)) {
            if (n5 != 0) {
                n12 = this.layoutChildRight((View)this.mCollapseButtonView, n2, arrn, n3);
                n4 = n;
            } else {
                n4 = this.layoutChildLeft((View)this.mCollapseButtonView, n, arrn, n3);
                n12 = n2;
            }
        }
        n2 = n4;
        n = n12;
        if (this.shouldLayout((View)this.mMenuView)) {
            if (n5 != 0) {
                n2 = this.layoutChildLeft((View)this.mMenuView, n4, arrn, n3);
                n = n12;
            } else {
                n = this.layoutChildRight((View)this.mMenuView, n12, arrn, n3);
                n2 = n4;
            }
        }
        n12 = this.getCurrentContentInsetLeft();
        n4 = this.getCurrentContentInsetRight();
        arrn[0] = Math.max(0, n12 - n2);
        arrn[1] = Math.max(0, n4 - (n6 - n9 - n));
        n2 = Math.max(n2, n12);
        n4 = Math.min(n, n6 - n9 - n4);
        n = n2;
        n12 = n4;
        if (this.shouldLayout(this.mExpandedActionView)) {
            if (n5 != 0) {
                n12 = this.layoutChildRight(this.mExpandedActionView, n4, arrn, n3);
                n = n2;
            } else {
                n = this.layoutChildLeft(this.mExpandedActionView, n2, arrn, n3);
                n12 = n4;
            }
        }
        n2 = n;
        n4 = n12;
        if (this.shouldLayout((View)this.mLogoView)) {
            if (n5 != 0) {
                n4 = this.layoutChildRight((View)this.mLogoView, n12, arrn, n3);
                n2 = n;
            } else {
                n2 = this.layoutChildLeft((View)this.mLogoView, n, arrn, n3);
                n4 = n12;
            }
        }
        bl = this.shouldLayout((View)this.mTitleTextView);
        boolean bl2 = this.shouldLayout((View)this.mSubtitleTextView);
        n = 0;
        if (bl) {
            layoutParams = (LayoutParams)this.mTitleTextView.getLayoutParams();
            n = 0 + (layoutParams.topMargin + this.mTitleTextView.getMeasuredHeight() + layoutParams.bottomMargin);
        }
        n13 = n;
        if (bl2) {
            layoutParams = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
            n13 = n + (layoutParams.topMargin + this.mSubtitleTextView.getMeasuredHeight() + layoutParams.bottomMargin);
        }
        if (!bl && !bl2) {
            n = n2;
        } else {
            int n14;
            layoutParams = bl ? this.mTitleTextView : this.mSubtitleTextView;
            Object object = bl2 ? this.mSubtitleTextView : this.mTitleTextView;
            layoutParams = (LayoutParams)layoutParams.getLayoutParams();
            object = (LayoutParams)object.getLayoutParams();
            n12 = bl && this.mTitleTextView.getMeasuredWidth() > 0 || bl2 && this.mSubtitleTextView.getMeasuredWidth() > 0 ? 1 : 0;
            n = this.mGravity & 112;
            if (n != 48) {
                if (n != 80) {
                    n14 = (n7 - n10 - n11 - n13) / 2;
                    if (n14 < layoutParams.topMargin + this.mTitleMarginTop) {
                        n = layoutParams.topMargin + this.mTitleMarginTop;
                    } else {
                        n13 = n7 - n11 - n13 - n14 - n10;
                        n = n14;
                        if (n13 < layoutParams.bottomMargin + this.mTitleMarginBottom) {
                            n = Math.max(0, n14 - (object.bottomMargin + this.mTitleMarginBottom - n13));
                        }
                    }
                    n = n10 + n;
                } else {
                    n = n7 - n11 - object.bottomMargin - this.mTitleMarginBottom - n13;
                }
            } else {
                n = this.getPaddingTop() + layoutParams.topMargin + this.mTitleMarginTop;
            }
            n13 = n2;
            if (n5 != 0) {
                n2 = n12 != 0 ? this.mTitleMarginStart : 0;
                n5 = n2 - arrn[1];
                n2 = n4 - Math.max(0, n5);
                arrn[1] = Math.max(0, - n5);
                n5 = n2;
                n4 = n2;
                if (bl) {
                    layoutParams = (LayoutParams)this.mTitleTextView.getLayoutParams();
                    n10 = n5 - this.mTitleTextView.getMeasuredWidth();
                    n14 = this.mTitleTextView.getMeasuredHeight() + n;
                    this.mTitleTextView.layout(n10, n, n5, n14);
                    n = n10 - this.mTitleMarginEnd;
                    n14 += layoutParams.bottomMargin;
                } else {
                    n14 = n;
                    n = n5;
                }
                n5 = n4;
                if (bl2) {
                    layoutParams = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
                    n5 = n14 + layoutParams.topMargin;
                    n14 = this.mSubtitleTextView.getMeasuredWidth();
                    n10 = this.mSubtitleTextView.getMeasuredHeight() + n5;
                    this.mSubtitleTextView.layout(n4 - n14, n5, n4, n10);
                    n5 = n4 - this.mTitleMarginEnd;
                    n4 = layoutParams.bottomMargin;
                }
                if (n12 != 0) {
                    n2 = Math.min(n, n5);
                }
                n = n13;
                n4 = n2;
            } else {
                n2 = n12 != 0 ? this.mTitleMarginStart : 0;
                n5 = n2 - arrn[0];
                n2 = n13 + Math.max(0, n5);
                arrn[0] = Math.max(0, - n5);
                n13 = n2;
                n5 = n2;
                if (bl) {
                    layoutParams = (LayoutParams)this.mTitleTextView.getLayoutParams();
                    n10 = this.mTitleTextView.getMeasuredWidth() + n13;
                    n14 = this.mTitleTextView.getMeasuredHeight() + n;
                    this.mTitleTextView.layout(n13, n, n10, n14);
                    n = n10 + this.mTitleMarginEnd;
                    n14 += layoutParams.bottomMargin;
                } else {
                    n14 = n;
                    n = n13;
                }
                n13 = n5;
                if (bl2) {
                    layoutParams = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
                    n13 = n14 + layoutParams.topMargin;
                    n14 = this.mSubtitleTextView.getMeasuredWidth() + n5;
                    n10 = this.mSubtitleTextView.getMeasuredHeight() + n13;
                    this.mSubtitleTextView.layout(n5, n13, n14, n10);
                    n13 = n14 + this.mTitleMarginEnd;
                    n5 = layoutParams.bottomMargin;
                }
                n = n12 != 0 ? Math.max(n, n13) : n2;
            }
        }
        n12 = n3;
        this.addCustomViewsWithGravity(this.mTempViews, 3);
        n3 = this.mTempViews.size();
        for (n2 = 0; n2 < n3; ++n2) {
            n = this.layoutChildLeft(this.mTempViews.get(n2), n, arrn, n12);
        }
        this.addCustomViewsWithGravity(this.mTempViews, 5);
        n3 = this.mTempViews.size();
        for (n2 = 0; n2 < n3; ++n2) {
            n4 = this.layoutChildRight(this.mTempViews.get(n2), n4, arrn, n12);
        }
        this.addCustomViewsWithGravity(this.mTempViews, 1);
        n2 = this.getViewListMeasuredWidth(this.mTempViews, arrn);
        n3 = n8 + (n6 - n8 - n9) / 2 - n2 / 2;
        n8 = n3 + n2;
        if (n3 < n) {
            n2 = n;
        } else {
            n2 = n3;
            if (n8 > n4) {
                n2 = n3 - (n8 - n4);
            }
        }
        n8 = this.mTempViews.size();
        n4 = 0;
        n3 = n2;
        for (n2 = n4; n2 < n8; ++n2) {
            n3 = this.layoutChildLeft(this.mTempViews.get(n2), n3, arrn, n12);
        }
        this.mTempViews.clear();
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        int n4;
        int n5 = 0;
        int n6 = 0;
        int[] arrn = this.mTempMargins;
        if (ViewUtils.isLayoutRtl((View)this)) {
            n3 = 1;
            n4 = 0;
        } else {
            n3 = 0;
            n4 = 1;
        }
        int n7 = 0;
        if (this.shouldLayout((View)this.mNavButtonView)) {
            this.measureChildConstrained((View)this.mNavButtonView, n, 0, n2, 0, this.mMaxButtonHeight);
            n7 = this.mNavButtonView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mNavButtonView);
            n5 = Math.max(0, this.mNavButtonView.getMeasuredHeight() + this.getVerticalMargins((View)this.mNavButtonView));
            n6 = View.combineMeasuredStates((int)0, (int)this.mNavButtonView.getMeasuredState());
        }
        int n8 = n5;
        int n9 = n6;
        if (this.shouldLayout((View)this.mCollapseButtonView)) {
            this.measureChildConstrained((View)this.mCollapseButtonView, n, 0, n2, 0, this.mMaxButtonHeight);
            n7 = this.mCollapseButtonView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mCollapseButtonView);
            n8 = Math.max(n5, this.mCollapseButtonView.getMeasuredHeight() + this.getVerticalMargins((View)this.mCollapseButtonView));
            n9 = View.combineMeasuredStates((int)n6, (int)this.mCollapseButtonView.getMeasuredState());
        }
        n6 = this.getCurrentContentInsetStart();
        n5 = 0 + Math.max(n6, n7);
        arrn[n3] = Math.max(0, n6 - n7);
        if (this.shouldLayout((View)this.mMenuView)) {
            this.measureChildConstrained((View)this.mMenuView, n, n5, n2, 0, this.mMaxButtonHeight);
            n6 = this.mMenuView.getMeasuredWidth();
            n7 = this.getHorizontalMargins((View)this.mMenuView);
            n8 = Math.max(n8, this.mMenuView.getMeasuredHeight() + this.getVerticalMargins((View)this.mMenuView));
            n9 = View.combineMeasuredStates((int)n9, (int)this.mMenuView.getMeasuredState());
            n6 += n7;
        } else {
            n6 = 0;
        }
        n7 = this.getCurrentContentInsetEnd();
        n5 += Math.max(n7, n6);
        arrn[n4] = Math.max(0, n7 - n6);
        if (this.shouldLayout(this.mExpandedActionView)) {
            n4 = n5 + this.measureChildCollapseMargins(this.mExpandedActionView, n, n5, n2, 0, arrn);
            n8 = Math.max(n8, this.mExpandedActionView.getMeasuredHeight() + this.getVerticalMargins(this.mExpandedActionView));
            n9 = View.combineMeasuredStates((int)n9, (int)this.mExpandedActionView.getMeasuredState());
        } else {
            n4 = n5;
        }
        n6 = n4;
        n5 = n8;
        n7 = n9;
        if (this.shouldLayout((View)this.mLogoView)) {
            n6 = n4 + this.measureChildCollapseMargins((View)this.mLogoView, n, n4, n2, 0, arrn);
            n5 = Math.max(n8, this.mLogoView.getMeasuredHeight() + this.getVerticalMargins((View)this.mLogoView));
            n7 = View.combineMeasuredStates((int)n9, (int)this.mLogoView.getMeasuredState());
        }
        n8 = this.getChildCount();
        n9 = n7;
        n4 = 0;
        n7 = n5;
        n5 = n6;
        for (n6 = n4; n6 < n8; ++n6) {
            View view = this.getChildAt(n6);
            if (((LayoutParams)view.getLayoutParams()).mViewType != 0 || !this.shouldLayout(view)) continue;
            n5 += this.measureChildCollapseMargins(view, n, n5, n2, 0, arrn);
            n7 = Math.max(n7, view.getMeasuredHeight() + this.getVerticalMargins(view));
            n9 = View.combineMeasuredStates((int)n9, (int)view.getMeasuredState());
        }
        n4 = n9;
        n6 = 0;
        n8 = 0;
        n3 = this.mTitleMarginTop + this.mTitleMarginBottom;
        int n10 = this.mTitleMarginStart + this.mTitleMarginEnd;
        n9 = n4;
        if (this.shouldLayout((View)this.mTitleTextView)) {
            this.measureChildCollapseMargins((View)this.mTitleTextView, n, n5 + n10, n2, n3, arrn);
            n6 = this.mTitleTextView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mTitleTextView);
            n8 = this.mTitleTextView.getMeasuredHeight() + this.getVerticalMargins((View)this.mTitleTextView);
            n9 = View.combineMeasuredStates((int)n4, (int)this.mTitleTextView.getMeasuredState());
        }
        if (this.shouldLayout((View)this.mSubtitleTextView)) {
            n6 = Math.max(n6, this.measureChildCollapseMargins((View)this.mSubtitleTextView, n, n5 + n10, n2, n8 + n3, arrn));
            n4 = this.mSubtitleTextView.getMeasuredHeight();
            n3 = this.getVerticalMargins((View)this.mSubtitleTextView);
            n9 = View.combineMeasuredStates((int)n9, (int)this.mSubtitleTextView.getMeasuredState());
            n8 += n4 + n3;
        }
        n8 = Math.max(n7, n8);
        n3 = this.getPaddingLeft();
        n10 = this.getPaddingRight();
        n7 = this.getPaddingTop();
        n4 = this.getPaddingBottom();
        n6 = View.resolveSizeAndState((int)Math.max(n5 + n6 + (n3 + n10), this.getSuggestedMinimumWidth()), (int)n, (int)(-16777216 & n9));
        n = View.resolveSizeAndState((int)Math.max(n8 + (n7 + n4), this.getSuggestedMinimumHeight()), (int)n2, (int)(n9 << 16));
        if (this.shouldCollapse()) {
            n = 0;
        }
        this.setMeasuredDimension(n6, n);
    }

    protected void onRestoreInstanceState(Parcelable object) {
        if (!(object instanceof SavedState)) {
            super.onRestoreInstanceState((Parcelable)object);
            return;
        }
        SavedState savedState = (SavedState)object;
        super.onRestoreInstanceState(savedState.getSuperState());
        object = this.mMenuView;
        object = object != null ? object.peekMenu() : null;
        if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && object != null && (object = object.findItem(savedState.expandedMenuItemId)) != null) {
            object.expandActionView();
        }
        if (savedState.isOverflowOpen) {
            this.postShowOverflowMenu();
        }
    }

    public void onRtlPropertiesChanged(int n) {
        if (Build.VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(n);
        }
        this.ensureContentInsets();
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        rtlSpacingHelper.setDirection(bl);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter != null && expandedActionViewMenuPresenter.mCurrentExpandedItem != null) {
            savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        savedState.isOverflowOpen = this.isOverflowMenuShowing();
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        if (n == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean bl = super.onTouchEvent(motionEvent);
            if (n == 0 && !bl) {
                this.mEatingTouch = true;
            }
        }
        if (n == 1 || n == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    void removeChildrenForExpandedActionView() {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            View view = this.getChildAt(i);
            if (((LayoutParams)view.getLayoutParams()).mViewType == 2 || view == this.mMenuView) continue;
            this.removeViewAt(i);
            this.mHiddenViews.add(view);
        }
    }

    public void setCollapseContentDescription(int n) {
        CharSequence charSequence = n != 0 ? this.getContext().getText(n) : null;
        this.setCollapseContentDescription(charSequence);
    }

    public void setCollapseContentDescription(CharSequence charSequence) {
        ImageButton imageButton;
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            this.ensureCollapseButtonView();
        }
        if ((imageButton = this.mCollapseButtonView) != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    public void setCollapseIcon(int n) {
        this.setCollapseIcon(AppCompatResources.getDrawable(this.getContext(), n));
    }

    public void setCollapseIcon(Drawable drawable2) {
        if (drawable2 != null) {
            this.ensureCollapseButtonView();
            this.mCollapseButtonView.setImageDrawable(drawable2);
            return;
        }
        drawable2 = this.mCollapseButtonView;
        if (drawable2 != null) {
            drawable2.setImageDrawable(this.mCollapseIcon);
        }
    }

    public void setCollapsible(boolean bl) {
        this.mCollapsible = bl;
        this.requestLayout();
    }

    public void setContentInsetEndWithActions(int n) {
        int n2 = n;
        if (n < 0) {
            n2 = Integer.MIN_VALUE;
        }
        if (n2 != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = n2;
            if (this.getNavigationIcon() != null) {
                this.requestLayout();
            }
        }
    }

    public void setContentInsetStartWithNavigation(int n) {
        int n2 = n;
        if (n < 0) {
            n2 = Integer.MIN_VALUE;
        }
        if (n2 != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = n2;
            if (this.getNavigationIcon() != null) {
                this.requestLayout();
            }
        }
    }

    public void setContentInsetsAbsolute(int n, int n2) {
        this.ensureContentInsets();
        this.mContentInsets.setAbsolute(n, n2);
    }

    public void setContentInsetsRelative(int n, int n2) {
        this.ensureContentInsets();
        this.mContentInsets.setRelative(n, n2);
    }

    public void setLogo(int n) {
        this.setLogo(AppCompatResources.getDrawable(this.getContext(), n));
    }

    public void setLogo(Drawable drawable2) {
        ImageView imageView;
        if (drawable2 != null) {
            this.ensureLogoView();
            if (!this.isChildOrHidden((View)this.mLogoView)) {
                this.addSystemView((View)this.mLogoView, true);
            }
        } else {
            imageView = this.mLogoView;
            if (imageView != null && this.isChildOrHidden((View)imageView)) {
                this.removeView((View)this.mLogoView);
                this.mHiddenViews.remove((Object)this.mLogoView);
            }
        }
        if ((imageView = this.mLogoView) != null) {
            imageView.setImageDrawable(drawable2);
        }
    }

    public void setLogoDescription(int n) {
        this.setLogoDescription(this.getContext().getText(n));
    }

    public void setLogoDescription(CharSequence charSequence) {
        ImageView imageView;
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            this.ensureLogoView();
        }
        if ((imageView = this.mLogoView) != null) {
            imageView.setContentDescription(charSequence);
        }
    }

    public void setMenu(MenuBuilder menuBuilder, ActionMenuPresenter actionMenuPresenter) {
        if (menuBuilder == null && this.mMenuView == null) {
            return;
        }
        this.ensureMenuView();
        MenuBuilder menuBuilder2 = this.mMenuView.peekMenu();
        if (menuBuilder2 == menuBuilder) {
            return;
        }
        if (menuBuilder2 != null) {
            menuBuilder2.removeMenuPresenter(this.mOuterActionMenuPresenter);
            menuBuilder2.removeMenuPresenter(this.mExpandedMenuPresenter);
        }
        if (this.mExpandedMenuPresenter == null) {
            this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
        }
        actionMenuPresenter.setExpandedActionViewsExclusive(true);
        if (menuBuilder != null) {
            menuBuilder.addMenuPresenter(actionMenuPresenter, this.mPopupContext);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        } else {
            actionMenuPresenter.initForMenu(this.mPopupContext, null);
            this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
            actionMenuPresenter.updateMenuView(true);
            this.mExpandedMenuPresenter.updateMenuView(true);
        }
        this.mMenuView.setPopupTheme(this.mPopupTheme);
        this.mMenuView.setPresenter(actionMenuPresenter);
        this.mOuterActionMenuPresenter = actionMenuPresenter;
    }

    public void setMenuCallbacks(MenuPresenter.Callback callback, MenuBuilder.Callback callback2) {
        this.mActionMenuPresenterCallback = callback;
        this.mMenuBuilderCallback = callback2;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.setMenuCallbacks(callback, callback2);
        }
    }

    public void setNavigationContentDescription(int n) {
        CharSequence charSequence = n != 0 ? this.getContext().getText(n) : null;
        this.setNavigationContentDescription(charSequence);
    }

    public void setNavigationContentDescription(CharSequence charSequence) {
        ImageButton imageButton;
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            this.ensureNavButtonView();
        }
        if ((imageButton = this.mNavButtonView) != null) {
            imageButton.setContentDescription(charSequence);
        }
    }

    public void setNavigationIcon(int n) {
        this.setNavigationIcon(AppCompatResources.getDrawable(this.getContext(), n));
    }

    public void setNavigationIcon(Drawable drawable2) {
        ImageButton imageButton;
        if (drawable2 != null) {
            this.ensureNavButtonView();
            if (!this.isChildOrHidden((View)this.mNavButtonView)) {
                this.addSystemView((View)this.mNavButtonView, true);
            }
        } else {
            imageButton = this.mNavButtonView;
            if (imageButton != null && this.isChildOrHidden((View)imageButton)) {
                this.removeView((View)this.mNavButtonView);
                this.mHiddenViews.remove((Object)this.mNavButtonView);
            }
        }
        if ((imageButton = this.mNavButtonView) != null) {
            imageButton.setImageDrawable(drawable2);
        }
    }

    public void setNavigationOnClickListener(View.OnClickListener onClickListener) {
        this.ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(onClickListener);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOverflowIcon(Drawable drawable2) {
        this.ensureMenu();
        this.mMenuView.setOverflowIcon(drawable2);
    }

    public void setPopupTheme(int n) {
        if (this.mPopupTheme != n) {
            this.mPopupTheme = n;
            if (n == 0) {
                this.mPopupContext = this.getContext();
                return;
            }
            this.mPopupContext = new ContextThemeWrapper(this.getContext(), n);
        }
    }

    public void setSubtitle(int n) {
        this.setSubtitle(this.getContext().getText(n));
    }

    public void setSubtitle(CharSequence charSequence) {
        TextView textView;
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            if (this.mSubtitleTextView == null) {
                textView = this.getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView((Context)textView);
                this.mSubtitleTextView = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int n = this.mSubtitleTextAppearance;
                if (n != 0) {
                    this.mSubtitleTextView.setTextAppearance((Context)textView, n);
                }
                if ((textView = this.mSubtitleTextColor) != null) {
                    this.mSubtitleTextView.setTextColor((ColorStateList)textView);
                }
            }
            if (!this.isChildOrHidden((View)this.mSubtitleTextView)) {
                this.addSystemView((View)this.mSubtitleTextView, true);
            }
        } else {
            textView = this.mSubtitleTextView;
            if (textView != null && this.isChildOrHidden((View)textView)) {
                this.removeView((View)this.mSubtitleTextView);
                this.mHiddenViews.remove((Object)this.mSubtitleTextView);
            }
        }
        if ((textView = this.mSubtitleTextView) != null) {
            textView.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }

    public void setSubtitleTextAppearance(Context context, int n) {
        this.mSubtitleTextAppearance = n;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, n);
        }
    }

    public void setSubtitleTextColor(int n) {
        this.setSubtitleTextColor(ColorStateList.valueOf((int)n));
    }

    public void setSubtitleTextColor(ColorStateList colorStateList) {
        this.mSubtitleTextColor = colorStateList;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public void setTitle(int n) {
        this.setTitle(this.getContext().getText(n));
    }

    public void setTitle(CharSequence charSequence) {
        TextView textView;
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            if (this.mTitleTextView == null) {
                textView = this.getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView((Context)textView);
                this.mTitleTextView = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int n = this.mTitleTextAppearance;
                if (n != 0) {
                    this.mTitleTextView.setTextAppearance((Context)textView, n);
                }
                if ((textView = this.mTitleTextColor) != null) {
                    this.mTitleTextView.setTextColor((ColorStateList)textView);
                }
            }
            if (!this.isChildOrHidden((View)this.mTitleTextView)) {
                this.addSystemView((View)this.mTitleTextView, true);
            }
        } else {
            textView = this.mTitleTextView;
            if (textView != null && this.isChildOrHidden((View)textView)) {
                this.removeView((View)this.mTitleTextView);
                this.mHiddenViews.remove((Object)this.mTitleTextView);
            }
        }
        if ((textView = this.mTitleTextView) != null) {
            textView.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }

    public void setTitleMargin(int n, int n2, int n3, int n4) {
        this.mTitleMarginStart = n;
        this.mTitleMarginTop = n2;
        this.mTitleMarginEnd = n3;
        this.mTitleMarginBottom = n4;
        this.requestLayout();
    }

    public void setTitleMarginBottom(int n) {
        this.mTitleMarginBottom = n;
        this.requestLayout();
    }

    public void setTitleMarginEnd(int n) {
        this.mTitleMarginEnd = n;
        this.requestLayout();
    }

    public void setTitleMarginStart(int n) {
        this.mTitleMarginStart = n;
        this.requestLayout();
    }

    public void setTitleMarginTop(int n) {
        this.mTitleMarginTop = n;
        this.requestLayout();
    }

    public void setTitleTextAppearance(Context context, int n) {
        this.mTitleTextAppearance = n;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, n);
        }
    }

    public void setTitleTextColor(int n) {
        this.setTitleTextColor(ColorStateList.valueOf((int)n));
    }

    public void setTitleTextColor(ColorStateList colorStateList) {
        this.mTitleTextColor = colorStateList;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(colorStateList);
        }
    }

    public boolean showOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null && actionMenuView.showOverflowMenu()) {
            return true;
        }
        return false;
    }

    private class ExpandedActionViewMenuPresenter
    implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        ExpandedActionViewMenuPresenter() {
        }

        @Override
        public boolean collapseItemActionView(MenuBuilder object, MenuItemImpl menuItemImpl) {
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed();
            }
            object = Toolbar.this;
            object.removeView(object.mExpandedActionView);
            object = Toolbar.this;
            object.removeView((View)object.mCollapseButtonView);
            Toolbar.this.mExpandedActionView = null;
            Toolbar.this.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }

        @Override
        public boolean expandItemActionView(MenuBuilder object, MenuItemImpl menuItemImpl) {
            Toolbar.this.ensureCollapseButtonView();
            object = Toolbar.this.mCollapseButtonView.getParent();
            Toolbar toolbar = Toolbar.this;
            if (object != toolbar) {
                if (object instanceof ViewGroup) {
                    ((ViewGroup)object).removeView((View)toolbar.mCollapseButtonView);
                }
                object = Toolbar.this;
                object.addView((View)object.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = menuItemImpl.getActionView();
            this.mCurrentExpandedItem = menuItemImpl;
            object = Toolbar.this.mExpandedActionView.getParent();
            if (object != (toolbar = Toolbar.this)) {
                if (object instanceof ViewGroup) {
                    ((ViewGroup)object).removeView(toolbar.mExpandedActionView);
                }
                object = Toolbar.this.generateDefaultLayoutParams();
                object.gravity = 8388611 | Toolbar.this.mButtonGravity & 112;
                object.mViewType = 2;
                Toolbar.this.mExpandedActionView.setLayoutParams((ViewGroup.LayoutParams)object);
                object = Toolbar.this;
                object.addView(object.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(true);
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        @Override
        public boolean flagActionItems() {
            return false;
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public MenuView getMenuView(ViewGroup viewGroup) {
            return null;
        }

        @Override
        public void initForMenu(Context object, MenuBuilder menuBuilder) {
            MenuItemImpl menuItemImpl;
            object = this.mMenu;
            if (object != null && (menuItemImpl = this.mCurrentExpandedItem) != null) {
                object.collapseItemActionView(menuItemImpl);
            }
            this.mMenu = menuBuilder;
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        }

        @Override
        public void onRestoreInstanceState(Parcelable parcelable) {
        }

        @Override
        public Parcelable onSaveInstanceState() {
            return null;
        }

        @Override
        public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
            return false;
        }

        @Override
        public void setCallback(MenuPresenter.Callback callback) {
        }

        @Override
        public void updateMenuView(boolean bl) {
            if (this.mCurrentExpandedItem != null) {
                boolean bl2 = false;
                MenuBuilder menuBuilder = this.mMenu;
                boolean bl3 = bl2;
                if (menuBuilder != null) {
                    int n = menuBuilder.size();
                    int n2 = 0;
                    do {
                        bl3 = bl2;
                        if (n2 >= n) break;
                        if (this.mMenu.getItem(n2) == this.mCurrentExpandedItem) {
                            bl3 = true;
                            break;
                        }
                        ++n2;
                    } while (true);
                }
                if (!bl3) {
                    this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }
    }

    public static class LayoutParams
    extends ActionBar.LayoutParams {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType = 0;

        public LayoutParams(int n) {
            this(-2, -1, n);
        }

        public LayoutParams(int n, int n2) {
            super(n, n2);
            this.gravity = 8388627;
        }

        public LayoutParams(int n, int n2, int n3) {
            super(n, n2);
            this.gravity = n3;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super((ViewGroup.LayoutParams)marginLayoutParams);
            this.copyMarginsFromCompat(marginLayoutParams);
        }

        public LayoutParams(ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = layoutParams.mViewType;
        }

        void copyMarginsFromCompat(ViewGroup.MarginLayoutParams marginLayoutParams) {
            this.leftMargin = marginLayoutParams.leftMargin;
            this.topMargin = marginLayoutParams.topMargin;
            this.rightMargin = marginLayoutParams.rightMargin;
            this.bottomMargin = marginLayoutParams.bottomMargin;
        }
    }

    public static interface OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem var1);
    }

    public static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int expandedMenuItemId;
        boolean isOverflowOpen;

        public SavedState(Parcel parcel) {
            this(parcel, null);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.expandedMenuItemId = parcel.readInt();
            boolean bl = parcel.readInt() != 0;
            this.isOverflowOpen = bl;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            RuntimeException runtimeException;
            super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
            throw runtimeException;
        }

    }

}

