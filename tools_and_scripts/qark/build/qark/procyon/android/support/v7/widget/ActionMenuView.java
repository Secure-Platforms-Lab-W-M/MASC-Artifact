// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.view.ViewDebug$ExportedProperty;
import android.view.ContextThemeWrapper;
import android.support.annotation.StyleRes;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.annotation.Nullable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.support.annotation.RestrictTo;
import android.view.accessibility.AccessibilityEvent;
import android.view.ViewGroup$LayoutParams;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View$MeasureSpec;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.MenuBuilder;

public class ActionMenuView extends LinearLayoutCompat implements ItemInvoker, MenuView
{
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;
    
    public ActionMenuView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuView(final Context mPopupContext, final AttributeSet set) {
        super(mPopupContext, set);
        this.setBaselineAligned(false);
        final float density = mPopupContext.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * density);
        this.mGeneratedItemPadding = (int)(4.0f * density);
        this.mPopupContext = mPopupContext;
        this.mPopupTheme = 0;
    }
    
    static int measureChildForCells(final View view, final int n, int cellsUsed, int n2, int n3) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2) - n3, View$MeasureSpec.getMode(n2));
        ActionMenuItemView actionMenuItemView;
        if (view instanceof ActionMenuItemView) {
            actionMenuItemView = (ActionMenuItemView)view;
        }
        else {
            actionMenuItemView = null;
        }
        final boolean b = false;
        if (actionMenuItemView != null && actionMenuItemView.hasText()) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        n3 = 0;
        if (cellsUsed > 0 && (n2 == 0 || cellsUsed >= 2)) {
            view.measure(View$MeasureSpec.makeMeasureSpec(n * cellsUsed, Integer.MIN_VALUE), measureSpec);
            final int measuredWidth = view.getMeasuredWidth();
            n3 = (cellsUsed = measuredWidth / n);
            if (measuredWidth % n != 0) {
                cellsUsed = n3 + 1;
            }
            if (n2 != 0 && cellsUsed < 2) {
                cellsUsed = 2;
            }
        }
        else {
            cellsUsed = n3;
        }
        boolean expandable = b;
        if (!layoutParams.isOverflowButton) {
            expandable = b;
            if (n2 != 0) {
                expandable = true;
            }
        }
        layoutParams.expandable = expandable;
        layoutParams.cellsUsed = cellsUsed;
        view.measure(View$MeasureSpec.makeMeasureSpec(cellsUsed * n, 1073741824), measureSpec);
        return cellsUsed;
    }
    
    private void onMeasureExactFormat(int i, int j) {
        final int mode = View$MeasureSpec.getMode(j);
        i = View$MeasureSpec.getSize(i);
        final int size = View$MeasureSpec.getSize(j);
        final int n = this.getPaddingLeft() + this.getPaddingRight();
        final int n2 = this.getPaddingTop() + this.getPaddingBottom();
        final int childMeasureSpec = getChildMeasureSpec(j, n2, -2);
        final int n3 = i - n;
        i = this.mMinCellSize;
        final int n4 = n3 / i;
        final int n5 = n3 % i;
        if (n4 == 0) {
            this.setMeasuredDimension(n3, 0);
            return;
        }
        final int n6 = i + n5 / n4;
        boolean b = false;
        final int childCount = this.getChildCount();
        int max = 0;
        int n7 = 0;
        int n8 = 0;
        int max2 = 0;
        i = n4;
        int k = 0;
        long n9 = 0L;
        while (k < childCount) {
            final View child = this.getChildAt(k);
            if (child.getVisibility() == 8) {
                j = n8;
            }
            else {
                final boolean b2 = child instanceof ActionMenuItemView;
                ++n7;
                if (b2) {
                    j = this.mGeneratedItemPadding;
                    child.setPadding(j, 0, j, 0);
                }
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.preventEdgeOffset = (b2 && ((ActionMenuItemView)child).hasText());
                if (layoutParams.isOverflowButton) {
                    j = 1;
                }
                else {
                    j = i;
                }
                final int measureChildForCells = measureChildForCells(child, n6, j, childMeasureSpec, n2);
                max2 = Math.max(max2, measureChildForCells);
                j = n8;
                if (layoutParams.expandable) {
                    j = n8 + 1;
                }
                if (layoutParams.isOverflowButton) {
                    b = true;
                }
                i -= measureChildForCells;
                max = Math.max(max, child.getMeasuredHeight());
                if (measureChildForCells == 1) {
                    n9 |= 1 << k;
                }
            }
            ++k;
            n8 = j;
        }
        final boolean b3 = b && n7 == 2;
        final int n10 = 0;
        int n11 = i;
        j = childCount;
        i = n10;
        final int n12 = n3;
        while (n8 > 0 && n11 > 0) {
            long n13 = 0L;
            int cellsUsed = Integer.MAX_VALUE;
            int n14 = 0;
            for (int l = 0; l < j; ++l) {
                final LayoutParams layoutParams2 = (LayoutParams)this.getChildAt(l).getLayoutParams();
                if (layoutParams2.expandable) {
                    if (layoutParams2.cellsUsed < cellsUsed) {
                        cellsUsed = layoutParams2.cellsUsed;
                        n13 = 1 << l;
                        n14 = 1;
                    }
                    else if (layoutParams2.cellsUsed == cellsUsed) {
                        final long n15 = 1 << l;
                        ++n14;
                        n13 |= n15;
                    }
                }
            }
            final int n16 = i;
            i = j;
            n9 |= n13;
            if (n14 > n11) {
                j = i;
                i = n16;
                break;
            }
            View child2;
            LayoutParams layoutParams3;
            int mGeneratedItemPadding;
            for (j = 0; j < i; ++j) {
                child2 = this.getChildAt(j);
                layoutParams3 = (LayoutParams)child2.getLayoutParams();
                if ((n13 & (long)(1 << j)) == 0x0L) {
                    if (layoutParams3.cellsUsed == cellsUsed + 1) {
                        n9 |= 1 << j;
                    }
                }
                else {
                    if (b3 && layoutParams3.preventEdgeOffset && n11 == 1) {
                        mGeneratedItemPadding = this.mGeneratedItemPadding;
                        child2.setPadding(mGeneratedItemPadding + n6, 0, mGeneratedItemPadding, 0);
                    }
                    ++layoutParams3.cellsUsed;
                    layoutParams3.expanded = true;
                    --n11;
                }
            }
            final int n17 = 1;
            j = i;
            i = n17;
        }
        final boolean b4 = !b && n7 == 1;
        if (n11 > 0 && n9 != 0L && (n11 < n7 - 1 || b4 || max2 > 1)) {
            float n18 = (float)Long.bitCount(n9);
            if (!b4) {
                if ((n9 & 0x1L) != 0x0L) {
                    if (!((LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                        n18 -= 0.5f;
                    }
                }
                if ((n9 & (long)(1 << j - 1)) != 0x0L) {
                    if (!((LayoutParams)this.getChildAt(j - 1).getLayoutParams()).preventEdgeOffset) {
                        n18 -= 0.5f;
                    }
                }
            }
            int n19 = 0;
            if (n18 > 0.0f) {
                n19 = (int)(n11 * n6 / n18);
            }
            for (int n20 = 0; n20 < j; ++n20) {
                if ((n9 & (long)(1 << n20)) != 0x0L) {
                    final View child3 = this.getChildAt(n20);
                    final LayoutParams layoutParams4 = (LayoutParams)child3.getLayoutParams();
                    if (child3 instanceof ActionMenuItemView) {
                        layoutParams4.extraPixels = n19;
                        layoutParams4.expanded = true;
                        if (n20 == 0 && !layoutParams4.preventEdgeOffset) {
                            layoutParams4.leftMargin = -n19 / 2;
                        }
                        i = 1;
                    }
                    else if (layoutParams4.isOverflowButton) {
                        layoutParams4.extraPixels = n19;
                        layoutParams4.expanded = true;
                        layoutParams4.rightMargin = -n19 / 2;
                        i = 1;
                    }
                    else {
                        if (n20 != 0) {
                            layoutParams4.leftMargin = n19 / 2;
                        }
                        if (n20 != j - 1) {
                            layoutParams4.rightMargin = n19 / 2;
                        }
                    }
                }
            }
        }
        if (i != 0) {
            View child4;
            LayoutParams layoutParams5;
            for (i = 0; i < j; ++i) {
                child4 = this.getChildAt(i);
                layoutParams5 = (LayoutParams)child4.getLayoutParams();
                if (layoutParams5.expanded) {
                    child4.measure(View$MeasureSpec.makeMeasureSpec(layoutParams5.cellsUsed * n6 + layoutParams5.extraPixels, 1073741824), childMeasureSpec);
                }
            }
        }
        if (mode != 1073741824) {
            i = max;
        }
        else {
            i = size;
        }
        this.setMeasuredDimension(n12, i);
    }
    
    @Override
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams != null && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void dismissPopupMenus() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        if (mPresenter != null) {
            mPresenter.dismissPopupMenus();
        }
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        return false;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        final LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams == null) {
            return this.generateDefaultLayoutParams();
        }
        LayoutParams layoutParams;
        if (viewGroup$LayoutParams instanceof LayoutParams) {
            layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
        }
        else {
            layoutParams = new LayoutParams(viewGroup$LayoutParams);
        }
        if (layoutParams.gravity <= 0) {
            layoutParams.gravity = 16;
            return layoutParams;
        }
        return layoutParams;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public LayoutParams generateOverflowButtonLayoutParams() {
        final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
        generateDefaultLayoutParams.isOverflowButton = true;
        return generateDefaultLayoutParams;
    }
    
    public Menu getMenu() {
        if (this.mMenu == null) {
            final Context context = this.getContext();
            (this.mMenu = new MenuBuilder(context)).setCallback((MenuBuilder.Callback)new MenuBuilderCallback());
            (this.mPresenter = new ActionMenuPresenter(context)).setReserveOverflow(true);
            final ActionMenuPresenter mPresenter = this.mPresenter;
            MenuPresenter.Callback mActionMenuPresenterCallback = this.mActionMenuPresenterCallback;
            if (mActionMenuPresenterCallback == null) {
                mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
            }
            mPresenter.setCallback(mActionMenuPresenterCallback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return (Menu)this.mMenu;
    }
    
    @Nullable
    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public int getWindowAnimations() {
        return 0;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected boolean hasSupportDividerBeforeChildAt(final int n) {
        if (n == 0) {
            return false;
        }
        final View child = this.getChildAt(n - 1);
        final View child2 = this.getChildAt(n);
        boolean b = false;
        if (n < this.getChildCount() && child instanceof ActionMenuChildView) {
            b = (false | ((ActionMenuChildView)child).needsDividerAfter());
        }
        if (n > 0 && child2 instanceof ActionMenuChildView) {
            return b | ((ActionMenuChildView)child2).needsDividerBefore();
        }
        return b;
    }
    
    public boolean hideOverflowMenu() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        return mPresenter != null && mPresenter.hideOverflowMenu();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public void initialize(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public boolean invokeItem(final MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction((MenuItem)menuItemImpl, 0);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isOverflowMenuShowPending() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        return mPresenter != null && mPresenter.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        return mPresenter != null && mPresenter.isOverflowMenuShowing();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final ActionMenuPresenter mPresenter = this.mPresenter;
        if (mPresenter == null) {
            return;
        }
        mPresenter.updateMenuView(false);
        if (this.mPresenter.isOverflowMenuShowing()) {
            this.mPresenter.hideOverflowMenu();
            this.mPresenter.showOverflowMenu();
        }
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dismissPopupMenus();
    }
    
    @Override
    protected void onLayout(final boolean b, int i, int n, int measuredWidth, int n2) {
        if (!this.mFormatItems) {
            super.onLayout(b, i, n, measuredWidth, n2);
            return;
        }
        final int childCount = this.getChildCount();
        final int n3 = (n2 - n) / 2;
        final int dividerWidth = this.getDividerWidth();
        n = 0;
        int n4 = 0;
        int n5 = 0;
        n2 = measuredWidth - i - this.getPaddingRight() - this.getPaddingLeft();
        boolean b2 = false;
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        for (int j = 0; j < childCount; ++j) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.isOverflowButton) {
                    n = child.getMeasuredWidth();
                    if (this.hasSupportDividerBeforeChildAt(j)) {
                        n += dividerWidth;
                    }
                    final int measuredHeight = child.getMeasuredHeight();
                    int n6;
                    int n7;
                    if (layoutRtl) {
                        n6 = this.getPaddingLeft() + layoutParams.leftMargin;
                        n7 = n6 + n;
                    }
                    else {
                        n7 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                        n6 = n7 - n;
                    }
                    final int n8 = n3 - measuredHeight / 2;
                    child.layout(n6, n8, n7, n8 + measuredHeight);
                    n2 -= n;
                    b2 = true;
                }
                else {
                    final int n9 = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                    n4 += n9;
                    n2 -= n9;
                    if (this.hasSupportDividerBeforeChildAt(j)) {
                        n4 += dividerWidth;
                    }
                    ++n5;
                }
            }
        }
        final int n10 = 1;
        if (childCount == 1 && !b2) {
            final View child2 = this.getChildAt(0);
            n = child2.getMeasuredWidth();
            n2 = child2.getMeasuredHeight();
            i = (measuredWidth - i) / 2 - n / 2;
            measuredWidth = n3 - n2 / 2;
            child2.layout(i, measuredWidth, i + n, measuredWidth + n2);
            return;
        }
        i = n10;
        if (b2) {
            i = 0;
        }
        i = n5 - i;
        if (i > 0) {
            i = n2 / i;
        }
        else {
            i = 0;
        }
        final int max = Math.max(0, i);
        if (layoutRtl) {
            n2 = this.getWidth() - this.getPaddingRight();
            i = 0;
            measuredWidth = dividerWidth;
            while (i < childCount) {
                final View child3 = this.getChildAt(i);
                final LayoutParams layoutParams2 = (LayoutParams)child3.getLayoutParams();
                if (child3.getVisibility() != 8) {
                    if (!layoutParams2.isOverflowButton) {
                        n2 -= layoutParams2.rightMargin;
                        final int measuredWidth2 = child3.getMeasuredWidth();
                        final int measuredHeight2 = child3.getMeasuredHeight();
                        final int n11 = n3 - measuredHeight2 / 2;
                        child3.layout(n2 - measuredWidth2, n11, n2, n11 + measuredHeight2);
                        n2 -= layoutParams2.leftMargin + measuredWidth2 + max;
                    }
                }
                ++i;
            }
            return;
        }
        n = this.getPaddingLeft();
        View child4;
        LayoutParams layoutParams3;
        int n12;
        for (i = 0; i < childCount; ++i) {
            child4 = this.getChildAt(i);
            layoutParams3 = (LayoutParams)child4.getLayoutParams();
            if (child4.getVisibility() != 8) {
                if (!layoutParams3.isOverflowButton) {
                    n += layoutParams3.leftMargin;
                    measuredWidth = child4.getMeasuredWidth();
                    n2 = child4.getMeasuredHeight();
                    n12 = n3 - n2 / 2;
                    child4.layout(n, n12, n + measuredWidth, n12 + n2);
                    n += layoutParams3.rightMargin + measuredWidth + max;
                }
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        final boolean mFormatItems = this.mFormatItems;
        this.mFormatItems = (View$MeasureSpec.getMode(n) == 1073741824);
        if (mFormatItems != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        final int size = View$MeasureSpec.getSize(n);
        if (this.mFormatItems) {
            final MenuBuilder mMenu = this.mMenu;
            if (mMenu != null && size != this.mFormatItemsWidth) {
                this.mFormatItemsWidth = size;
                mMenu.onItemsChanged(true);
            }
        }
        final int childCount = this.getChildCount();
        if (this.mFormatItems && childCount > 0) {
            this.onMeasureExactFormat(n, n2);
            return;
        }
        for (int i = 0; i < childCount; ++i) {
            final LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            layoutParams.rightMargin = 0;
            layoutParams.leftMargin = 0;
        }
        super.onMeasure(n, n2);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setExpandedActionViewsExclusive(final boolean expandedActionViewsExclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(expandedActionViewsExclusive);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(@Nullable final Drawable overflowIcon) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(overflowIcon);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setOverflowReserved(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
    }
    
    public void setPopupTheme(@StyleRes final int mPopupTheme) {
        if (this.mPopupTheme == mPopupTheme) {
            return;
        }
        if ((this.mPopupTheme = mPopupTheme) == 0) {
            this.mPopupContext = this.getContext();
            return;
        }
        this.mPopupContext = (Context)new ContextThemeWrapper(this.getContext(), mPopupTheme);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setPresenter(final ActionMenuPresenter mPresenter) {
        (this.mPresenter = mPresenter).setMenuView(this);
    }
    
    public boolean showOverflowMenu() {
        final ActionMenuPresenter mPresenter = this.mPresenter;
        return mPresenter != null && mPresenter.showOverflowMenu();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public interface ActionMenuChildView
    {
        boolean needsDividerAfter();
        
        boolean needsDividerBefore();
    }
    
    private static class ActionMenuPresenterCallback implements MenuPresenter.Callback
    {
        ActionMenuPresenterCallback() {
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            return false;
        }
    }
    
    public static class LayoutParams extends LinearLayoutCompat.LayoutParams
    {
        @ViewDebug$ExportedProperty
        public int cellsUsed;
        @ViewDebug$ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug$ExportedProperty
        public int extraPixels;
        @ViewDebug$ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug$ExportedProperty
        public boolean preventEdgeOffset;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }
        
        LayoutParams(final int n, final int n2, final boolean isOverflowButton) {
            super(n, n2);
            this.isOverflowButton = isOverflowButton;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
    }
    
    private class MenuBuilderCallback implements Callback
    {
        MenuBuilderCallback() {
        }
        
        @Override
        public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
        }
        
        @Override
        public void onMenuModeChange(final MenuBuilder menuBuilder) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
}
