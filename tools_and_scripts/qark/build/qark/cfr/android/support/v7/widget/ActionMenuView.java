/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.ContextThemeWrapper
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewDebug
 *  android.view.ViewDebug$ExportedProperty
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.accessibility.AccessibilityEvent
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class ActionMenuView
extends LinearLayoutCompat
implements MenuBuilder.ItemInvoker,
MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setBaselineAligned(false);
        float f = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * f);
        this.mGeneratedItemPadding = (int)(4.0f * f);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
    }

    static int measureChildForCells(View view, int n, int n2, int n3, int n4) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        int n5 = View.MeasureSpec.makeMeasureSpec((int)(View.MeasureSpec.getSize((int)n3) - n4), (int)View.MeasureSpec.getMode((int)n3));
        ActionMenuItemView actionMenuItemView = view instanceof ActionMenuItemView ? (ActionMenuItemView)view : null;
        boolean bl = false;
        n3 = actionMenuItemView != null && actionMenuItemView.hasText() ? 1 : 0;
        n4 = 0;
        if (n2 > 0 && (n3 == 0 || n2 >= 2)) {
            view.measure(View.MeasureSpec.makeMeasureSpec((int)(n * n2), (int)Integer.MIN_VALUE), n5);
            int n6 = view.getMeasuredWidth();
            n2 = n4 = n6 / n;
            if (n6 % n != 0) {
                n2 = n4 + 1;
            }
            if (n3 != 0 && n2 < 2) {
                n2 = 2;
            }
        } else {
            n2 = n4;
        }
        boolean bl2 = bl;
        if (!layoutParams.isOverflowButton) {
            bl2 = bl;
            if (n3 != 0) {
                bl2 = true;
            }
        }
        layoutParams.expandable = bl2;
        layoutParams.cellsUsed = n2;
        view.measure(View.MeasureSpec.makeMeasureSpec((int)(n2 * n), (int)1073741824), n5);
        return n2;
    }

    private void onMeasureExactFormat(int n, int n2) {
        Object object;
        int n3;
        LayoutParams layoutParams;
        int n4 = View.MeasureSpec.getMode((int)n2);
        n = View.MeasureSpec.getSize((int)n);
        int n5 = View.MeasureSpec.getSize((int)n2);
        int n6 = this.getPaddingLeft() + this.getPaddingRight();
        int n7 = this.getPaddingTop() + this.getPaddingBottom();
        int n8 = ActionMenuView.getChildMeasureSpec((int)n2, (int)n7, (int)-2);
        int n9 = n - n6;
        n = this.mMinCellSize;
        int n10 = n9 / n;
        int n11 = n9 % n;
        if (n10 == 0) {
            this.setMeasuredDimension(n9, 0);
            return;
        }
        int n12 = n + n11 / n10;
        int n13 = 0;
        int n14 = this.getChildCount();
        int n15 = 0;
        int n16 = 0;
        int n17 = 0;
        int n18 = 0;
        n = n10;
        long l = 0L;
        for (n3 = 0; n3 < n14; ++n3) {
            object = this.getChildAt(n3);
            if (object.getVisibility() == 8) {
                n2 = n17;
            } else {
                boolean bl = object instanceof ActionMenuItemView;
                ++n16;
                if (bl) {
                    n2 = this.mGeneratedItemPadding;
                    object.setPadding(n2, 0, n2, 0);
                }
                layoutParams = (LayoutParams)object.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                bl = bl && ((ActionMenuItemView)object).hasText();
                layoutParams.preventEdgeOffset = bl;
                n2 = layoutParams.isOverflowButton ? 1 : n;
                int n19 = ActionMenuView.measureChildForCells((View)object, n12, n2, n8, n7);
                n18 = Math.max(n18, n19);
                n2 = n17;
                if (layoutParams.expandable) {
                    n2 = n17 + 1;
                }
                if (layoutParams.isOverflowButton) {
                    n13 = 1;
                }
                n -= n19;
                n15 = Math.max(n15, object.getMeasuredHeight());
                if (n19 == 1) {
                    l |= (long)(1 << n3);
                }
            }
            n17 = n2;
        }
        n10 = n13 != 0 && n16 == 2 ? 1 : 0;
        n11 = 0;
        n6 = n;
        n2 = n14;
        n = n11;
        n7 = n9;
        while (n17 > 0 && n6 > 0) {
            long l2 = 0L;
            n3 = Integer.MAX_VALUE;
            n11 = 0;
            for (n9 = 0; n9 < n2; ++n9) {
                object = (LayoutParams)this.getChildAt(n9).getLayoutParams();
                if (!object.expandable) continue;
                if (object.cellsUsed < n3) {
                    n3 = object.cellsUsed;
                    l2 = 1 << n9;
                    n11 = 1;
                    continue;
                }
                if (object.cellsUsed != n3) continue;
                long l3 = 1 << n9;
                ++n11;
                l2 |= l3;
            }
            n9 = n;
            n = n2;
            l |= l2;
            if (n11 > n6) {
                n2 = n;
                n = n9;
                break;
            }
            for (n2 = 0; n2 < n; ++n2) {
                object = this.getChildAt(n2);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if ((l2 & (long)(1 << n2)) == 0L) {
                    if (layoutParams.cellsUsed != n3 + 1) continue;
                    l |= (long)(1 << n2);
                    continue;
                }
                if (n10 != 0 && layoutParams.preventEdgeOffset && n6 == 1) {
                    n9 = this.mGeneratedItemPadding;
                    object.setPadding(n9 + n12, 0, n9, 0);
                }
                ++layoutParams.cellsUsed;
                layoutParams.expanded = true;
                --n6;
            }
            n11 = 1;
            n2 = n;
            n = n11;
        }
        n13 = n13 == 0 && n16 == 1 ? 1 : 0;
        if (n6 > 0 && l != 0L && (n6 < n16 - 1 || n13 != 0 || n18 > 1)) {
            float f = Long.bitCount(l);
            if (n13 == 0) {
                if ((l & 1L) != 0L && !((LayoutParams)this.getChildAt((int)0).getLayoutParams()).preventEdgeOffset) {
                    f -= 0.5f;
                }
                if ((l & (long)(1 << n2 - 1)) != 0L && !((LayoutParams)this.getChildAt((int)(n2 - 1)).getLayoutParams()).preventEdgeOffset) {
                    f -= 0.5f;
                }
            }
            n13 = 0;
            if (f > 0.0f) {
                n13 = (int)((float)(n6 * n12) / f);
            }
            for (n17 = 0; n17 < n2; ++n17) {
                if ((l & (long)(1 << n17)) == 0L) continue;
                object = this.getChildAt(n17);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if (object instanceof ActionMenuItemView) {
                    layoutParams.extraPixels = n13;
                    layoutParams.expanded = true;
                    if (n17 == 0 && !layoutParams.preventEdgeOffset) {
                        layoutParams.leftMargin = (- n13) / 2;
                    }
                    n = 1;
                    continue;
                }
                if (layoutParams.isOverflowButton) {
                    layoutParams.extraPixels = n13;
                    layoutParams.expanded = true;
                    layoutParams.rightMargin = (- n13) / 2;
                    n = 1;
                    continue;
                }
                if (n17 != 0) {
                    layoutParams.leftMargin = n13 / 2;
                }
                if (n17 == n2 - 1) continue;
                layoutParams.rightMargin = n13 / 2;
            }
        }
        if (n != 0) {
            for (n = 0; n < n2; ++n) {
                object = this.getChildAt(n);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if (!layoutParams.expanded) continue;
                object.measure(View.MeasureSpec.makeMeasureSpec((int)(layoutParams.cellsUsed * n12 + layoutParams.extraPixels), (int)1073741824), n8);
            }
        }
        n = n4 != 1073741824 ? n15 : n5;
        this.setMeasuredDimension(n7, n);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams != null && layoutParams instanceof LayoutParams) {
            return true;
        }
        return false;
    }

    public void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus();
            return;
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams object) {
        if (object != null) {
            object = object instanceof LayoutParams ? new LayoutParams((LayoutParams)((Object)object)) : new LayoutParams((ViewGroup.LayoutParams)object);
            if (object.gravity <= 0) {
                object.gravity = 16;
                return object;
            }
            return object;
        }
        return this.generateDefaultLayoutParams();
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams layoutParams = this.generateDefaultLayoutParams();
        layoutParams.isOverflowButton = true;
        return layoutParams;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Object object = this.getContext();
            this.mMenu = new MenuBuilder((Context)object);
            this.mMenu.setCallback(new MenuBuilderCallback());
            this.mPresenter = new ActionMenuPresenter((Context)object);
            this.mPresenter.setReserveOverflow(true);
            ActionMenuPresenter actionMenuPresenter = this.mPresenter;
            object = this.mActionMenuPresenterCallback;
            if (object == null) {
                object = new ActionMenuPresenterCallback();
            }
            actionMenuPresenter.setCallback((MenuPresenter.Callback)object);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    @Nullable
    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public int getWindowAnimations() {
        return 0;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    protected boolean hasSupportDividerBeforeChildAt(int n) {
        if (n == 0) {
            return false;
        }
        View view = this.getChildAt(n - 1);
        View view2 = this.getChildAt(n);
        boolean bl = false;
        if (n < this.getChildCount() && view instanceof ActionMenuChildView) {
            bl = false | ((ActionMenuChildView)view).needsDividerAfter();
        }
        if (n > 0 && view2 instanceof ActionMenuChildView) {
            return bl | ((ActionMenuChildView)view2).needsDividerBefore();
        }
        return bl;
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu()) {
            return true;
        }
        return false;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    @Override
    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isOverflowMenuShowPending() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing()) {
            return true;
        }
        return false;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void onConfigurationChanged(Configuration object) {
        super.onConfigurationChanged((Configuration)object);
        object = this.mPresenter;
        if (object != null) {
            object.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
                return;
            }
            return;
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dismissPopupMenus();
    }

    @Override
    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        Object object;
        int n5;
        LayoutParams layoutParams;
        ActionMenuView actionMenuView = this;
        if (!actionMenuView.mFormatItems) {
            super.onLayout(bl, n, n2, n3, n4);
            return;
        }
        int n6 = this.getChildCount();
        int n7 = (n4 - n2) / 2;
        int n8 = this.getDividerWidth();
        n2 = 0;
        int n9 = 0;
        int n10 = 0;
        n4 = n3 - n - this.getPaddingRight() - this.getPaddingLeft();
        int n11 = 0;
        bl = ViewUtils.isLayoutRtl((View)this);
        for (n5 = 0; n5 < n6; ++n5) {
            int n12;
            object = actionMenuView.getChildAt(n5);
            if (object.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)object.getLayoutParams();
            if (layoutParams.isOverflowButton) {
                n2 = object.getMeasuredWidth();
                if (actionMenuView.hasSupportDividerBeforeChildAt(n5)) {
                    n2 += n8;
                }
                int n13 = object.getMeasuredHeight();
                if (bl) {
                    n11 = this.getPaddingLeft() + layoutParams.leftMargin;
                    n12 = n11 + n2;
                } else {
                    n12 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                    n11 = n12 - n2;
                }
                int n14 = n7 - n13 / 2;
                object.layout(n11, n14, n12, n14 + n13);
                n4 -= n2;
                n11 = 1;
                continue;
            }
            n12 = object.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            n9 += n12;
            n4 -= n12;
            if (actionMenuView.hasSupportDividerBeforeChildAt(n5)) {
                n9 += n8;
            }
            ++n10;
        }
        n9 = 1;
        if (n6 == 1 && n11 == 0) {
            actionMenuView = actionMenuView.getChildAt(0);
            n2 = actionMenuView.getMeasuredWidth();
            n4 = actionMenuView.getMeasuredHeight();
            n = (n3 - n) / 2 - n2 / 2;
            n3 = n7 - n4 / 2;
            actionMenuView.layout(n, n3, n + n2, n3 + n4);
            return;
        }
        n = n9;
        if (n11 != 0) {
            n = 0;
        }
        n = (n = n10 - n) > 0 ? n4 / n : 0;
        n9 = Math.max(0, n);
        if (bl) {
            n4 = this.getWidth() - this.getPaddingRight();
            n3 = n8;
            for (n = 0; n < n6; ++n) {
                object = actionMenuView.getChildAt(n);
                layoutParams = (LayoutParams)object.getLayoutParams();
                if (object.getVisibility() == 8 || layoutParams.isOverflowButton) continue;
                n5 = object.getMeasuredWidth();
                n10 = object.getMeasuredHeight();
                n11 = n7 - n10 / 2;
                object.layout(n4 - n5, n11, n4 -= layoutParams.rightMargin, n11 + n10);
                n4 -= layoutParams.leftMargin + n5 + n9;
            }
            return;
        }
        n2 = this.getPaddingLeft();
        for (n = 0; n < n6; ++n) {
            actionMenuView = this.getChildAt(n);
            object = (LayoutParams)actionMenuView.getLayoutParams();
            if (actionMenuView.getVisibility() == 8 || object.isOverflowButton) continue;
            n3 = actionMenuView.getMeasuredWidth();
            n4 = actionMenuView.getMeasuredHeight();
            n5 = n7 - n4 / 2;
            actionMenuView.layout(n2, n5, (n2 += object.leftMargin) + n3, n5 + n4);
            n2 += object.rightMargin + n3 + n9;
        }
    }

    @Override
    protected void onMeasure(int n, int n2) {
        Object object;
        boolean bl = this.mFormatItems;
        boolean bl2 = View.MeasureSpec.getMode((int)n) == 1073741824;
        this.mFormatItems = bl2;
        if (bl != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int n3 = View.MeasureSpec.getSize((int)n);
        if (this.mFormatItems && (object = this.mMenu) != null && n3 != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = n3;
            object.onItemsChanged(true);
        }
        int n4 = this.getChildCount();
        if (this.mFormatItems && n4 > 0) {
            this.onMeasureExactFormat(n, n2);
            return;
        }
        for (n3 = 0; n3 < n4; ++n3) {
            object = (LayoutParams)this.getChildAt(n3).getLayoutParams();
            object.rightMargin = 0;
            object.leftMargin = 0;
        }
        super.onMeasure(n, n2);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setExpandedActionViewsExclusive(boolean bl) {
        this.mPresenter.setExpandedActionViewsExclusive(bl);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setMenuCallbacks(MenuPresenter.Callback callback, MenuBuilder.Callback callback2) {
        this.mActionMenuPresenterCallback = callback;
        this.mMenuBuilderCallback = callback2;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOverflowIcon(@Nullable Drawable drawable2) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(drawable2);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setOverflowReserved(boolean bl) {
        this.mReserveOverflow = bl;
    }

    public void setPopupTheme(@StyleRes int n) {
        if (this.mPopupTheme != n) {
            this.mPopupTheme = n;
            if (n == 0) {
                this.mPopupContext = this.getContext();
                return;
            }
            this.mPopupContext = new ContextThemeWrapper(this.getContext(), n);
            return;
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
        this.mPresenter.setMenuView(this);
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu()) {
            return true;
        }
        return false;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static interface ActionMenuChildView {
        public boolean needsDividerAfter();

        public boolean needsDividerBefore();
    }

    private static class ActionMenuPresenterCallback
    implements MenuPresenter.Callback {
        ActionMenuPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            return false;
        }
    }

    public static class LayoutParams
    extends LinearLayoutCompat.LayoutParams {
        @ViewDebug.ExportedProperty
        public int cellsUsed;
        @ViewDebug.ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug.ExportedProperty
        public int extraPixels;
        @ViewDebug.ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug.ExportedProperty
        public boolean preventEdgeOffset;

        public LayoutParams(int n, int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }

        LayoutParams(int n, int n2, boolean bl) {
            super(n, n2);
            this.isOverflowButton = bl;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    private class MenuBuilderCallback
    implements MenuBuilder.Callback {
        MenuBuilderCallback() {
        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(menuItem)) {
                return true;
            }
            return false;
        }

        @Override
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
                return;
            }
        }
    }

    public static interface OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem var1);
    }

}

