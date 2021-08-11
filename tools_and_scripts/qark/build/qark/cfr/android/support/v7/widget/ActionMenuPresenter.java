/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.os.IBinder
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.SparseBooleanArray
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopup;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.TooltipCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;

class ActionMenuPresenter
extends BaseMenuPresenter
implements ActionProvider.SubUiVisibilityListener {
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
    ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    OverflowMenuButton mOverflowButton;
    OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback;
    OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;

    public ActionMenuPresenter(Context context) {
        super(context, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
        this.mPopupPresenterCallback = new PopupPresenterCallback();
    }

    private View findViewForItem(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        if (viewGroup == null) {
            return null;
        }
        int n = viewGroup.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = viewGroup.getChildAt(i);
            if (!(view instanceof MenuView.ItemView) || ((MenuView.ItemView)view).getItemData() != menuItem) continue;
            return view;
        }
        return null;
    }

    @Override
    public void bindItemView(MenuItemImpl object, MenuView.ItemView itemView) {
        itemView.initialize((MenuItemImpl)object, 0);
        object = (ActionMenuView)this.mMenuView;
        itemView = (ActionMenuItemView)itemView;
        itemView.setItemInvoker((MenuBuilder.ItemInvoker)object);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        itemView.setPopupCallback(this.mPopupCallback);
    }

    public boolean dismissPopupMenus() {
        return this.hideOverflowMenu() | this.hideSubMenus();
    }

    @Override
    public boolean filterLeftoverView(ViewGroup viewGroup, int n) {
        if (viewGroup.getChildAt(n) == this.mOverflowButton) {
            return false;
        }
        return super.filterLeftoverView(viewGroup, n);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean flagActionItems() {
        MenuItemImpl menuItemImpl;
        int n;
        int n2;
        ArrayList<MenuItemImpl> arrayList;
        ActionMenuPresenter actionMenuPresenter = this;
        if (actionMenuPresenter.mMenu != null) {
            arrayList = actionMenuPresenter.mMenu.getVisibleItems();
            n2 = arrayList.size();
        } else {
            arrayList = null;
            n2 = 0;
        }
        int n3 = actionMenuPresenter.mMaxItems;
        int n4 = actionMenuPresenter.mActionItemWidthLimit;
        int n5 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        Object object = (ViewGroup)actionMenuPresenter.mMenuView;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        for (n = 0; n < n2; ++n) {
            menuItemImpl = arrayList.get(n);
            if (menuItemImpl.requiresActionButton()) {
                ++n6;
            } else if (menuItemImpl.requestsActionButton()) {
                ++n7;
            } else {
                n9 = 1;
            }
            if (!actionMenuPresenter.mExpandedActionViewsExclusive || !menuItemImpl.isActionViewExpanded()) continue;
            n3 = 0;
        }
        if (actionMenuPresenter.mReserveOverflow && (n9 != 0 || n6 + n7 > n3)) {
            --n3;
        }
        n9 = n3 - n6;
        menuItemImpl = actionMenuPresenter.mActionButtonGroups;
        menuItemImpl.clear();
        n7 = 0;
        n = 0;
        if (actionMenuPresenter.mStrictWidthLimit) {
            n3 = actionMenuPresenter.mMinCellSize;
            n = n4 / n3;
            n7 = n3 + n4 % n3 / n;
        }
        int n10 = 0;
        n3 = n8;
        n8 = n6;
        actionMenuPresenter = object;
        n6 = n4;
        n4 = n2;
        do {
            View view;
            object = this;
            if (n10 >= n4) {
                return true;
            }
            MenuItemImpl menuItemImpl2 = arrayList.get(n10);
            if (menuItemImpl2.requiresActionButton()) {
                view = object.getItemView(menuItemImpl2, object.mScrapActionButtonView, (ViewGroup)actionMenuPresenter);
                if (object.mScrapActionButtonView == null) {
                    object.mScrapActionButtonView = view;
                }
                if (object.mStrictWidthLimit) {
                    n -= ActionMenuView.measureChildForCells(view, n7, n, n5, 0);
                } else {
                    view.measure(n5, n5);
                }
                n2 = view.getMeasuredWidth();
                n6 -= n2;
                if (n3 == 0) {
                    n3 = n2;
                }
                if ((n2 = menuItemImpl2.getGroupId()) != 0) {
                    menuItemImpl.put(n2, true);
                }
                menuItemImpl2.setIsActionButton(true);
                n2 = n9;
            } else if (menuItemImpl2.requestsActionButton()) {
                int n11;
                int n12 = menuItemImpl2.getGroupId();
                boolean bl = menuItemImpl.get(n12);
                int n13 = !(n9 <= 0 && !bl || n6 <= 0 || object.mStrictWidthLimit && n <= 0) ? 1 : 0;
                n2 = n9;
                if (n13 != 0) {
                    view = object.getItemView(menuItemImpl2, object.mScrapActionButtonView, (ViewGroup)actionMenuPresenter);
                    if (object.mScrapActionButtonView == null) {
                        object.mScrapActionButtonView = view;
                    }
                    if (object.mStrictWidthLimit) {
                        n9 = ActionMenuView.measureChildForCells(view, n7, n, n5, 0);
                        n -= n9;
                        if (n9 == 0) {
                            n13 = 0;
                        }
                    } else {
                        view.measure(n5, n5);
                    }
                    n11 = view.getMeasuredWidth();
                    n9 = n6 - n11;
                    if (n3 == 0) {
                        n3 = n11;
                    }
                    if (object.mStrictWidthLimit) {
                        n6 = n9 >= 0 ? 1 : 0;
                        n13 &= n6;
                        n6 = n9;
                        n9 = n3;
                    } else {
                        n6 = n9 + n3 > 0 ? 1 : 0;
                        n13 &= n6;
                        n6 = n9;
                        n9 = n3;
                    }
                } else {
                    n9 = n3;
                }
                if (n13 != 0 && n12 != 0) {
                    menuItemImpl.put(n12, true);
                    n3 = n2;
                } else if (bl) {
                    menuItemImpl.put(n12, false);
                    n3 = n2;
                    for (n11 = 0; n11 < n10; ++n11) {
                        object = arrayList.get(n11);
                        if (object.getGroupId() != n12) continue;
                        n2 = n3;
                        if (object.isActionButton()) {
                            n2 = n3 + 1;
                        }
                        object.setIsActionButton(false);
                        n3 = n2;
                    }
                } else {
                    n3 = n2;
                }
                n2 = n3;
                if (n13 != 0) {
                    n2 = n3 - 1;
                }
                menuItemImpl2.setIsActionButton((boolean)n13);
                n3 = n9;
            } else {
                menuItemImpl2.setIsActionButton(false);
                n2 = n9;
            }
            ++n10;
            n9 = n2;
        } while (true);
    }

    @Override
    public View getItemView(MenuItemImpl object, View view, ViewGroup viewGroup) {
        View view2 = object.getActionView();
        view = view2 != null && !object.hasCollapsibleActionView() ? view2 : super.getItemView((MenuItemImpl)object, view, viewGroup);
        int n = object.isActionViewExpanded() ? 8 : 0;
        view.setVisibility(n);
        object = (ActionMenuView)viewGroup;
        viewGroup = view.getLayoutParams();
        if (!object.checkLayoutParams((ViewGroup.LayoutParams)viewGroup)) {
            view.setLayoutParams((ViewGroup.LayoutParams)object.generateLayoutParams((ViewGroup.LayoutParams)viewGroup));
            return view;
        }
        return view;
    }

    @Override
    public MenuView getMenuView(ViewGroup object) {
        MenuView menuView = this.mMenuView;
        if (menuView != (object = super.getMenuView((ViewGroup)object))) {
            ((ActionMenuView)object).setPresenter(this);
            return object;
        }
        return object;
    }

    public Drawable getOverflowIcon() {
        OverflowMenuButton overflowMenuButton = this.mOverflowButton;
        if (overflowMenuButton != null) {
            return overflowMenuButton.getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }

    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
            ((View)this.mMenuView).removeCallbacks((Runnable)this.mPostedOpenRunnable);
            this.mPostedOpenRunnable = null;
            return true;
        }
        OverflowPopup overflowPopup = this.mOverflowPopup;
        if (overflowPopup != null) {
            overflowPopup.dismiss();
            return true;
        }
        return false;
    }

    public boolean hideSubMenus() {
        ActionButtonSubmenu actionButtonSubmenu = this.mActionButtonPopup;
        if (actionButtonSubmenu != null) {
            actionButtonSubmenu.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void initForMenu(@NonNull Context object, @Nullable MenuBuilder menuBuilder) {
        super.initForMenu((Context)object, menuBuilder);
        menuBuilder = object.getResources();
        object = ActionBarPolicy.get((Context)object);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = object.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = object.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = object.getMaxActionButtons();
        }
        int n = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                int n2 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                this.mOverflowButton.measure(n2, n2);
            }
            n -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = n;
        this.mMinCellSize = (int)(menuBuilder.getDisplayMetrics().density * 56.0f);
        this.mScrapActionButtonView = null;
    }

    public boolean isOverflowMenuShowPending() {
        if (this.mPostedOpenRunnable == null && !this.isOverflowMenuShowing()) {
            return false;
        }
        return true;
    }

    public boolean isOverflowMenuShowing() {
        OverflowPopup overflowPopup = this.mOverflowPopup;
        if (overflowPopup != null && overflowPopup.isShowing()) {
            return true;
        }
        return false;
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        this.dismissPopupMenus();
        super.onCloseMenu(menuBuilder, bl);
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons();
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
            return;
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            return;
        }
        parcelable = (SavedState)parcelable;
        if (parcelable.openSubMenuId > 0) {
            parcelable = this.mMenu.findItem(parcelable.openSubMenuId);
            if (parcelable != null) {
                this.onSubMenuSelected((SubMenuBuilder)parcelable.getSubMenu());
                return;
            }
            return;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.openSubMenuId = this.mOpenSubMenuId;
        return savedState;
    }

    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        boolean bl;
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        SubMenuBuilder subMenuBuilder2 = subMenuBuilder;
        while (subMenuBuilder2.getParentMenu() != this.mMenu) {
            subMenuBuilder2 = (SubMenuBuilder)subMenuBuilder2.getParentMenu();
        }
        if ((subMenuBuilder2 = this.findViewForItem(subMenuBuilder2.getItem())) == null) {
            return false;
        }
        this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
        boolean bl2 = false;
        int n = subMenuBuilder.size();
        int n2 = 0;
        do {
            bl = bl2;
            if (n2 >= n) break;
            MenuItem menuItem = subMenuBuilder.getItem(n2);
            if (menuItem.isVisible() && menuItem.getIcon() != null) {
                bl = true;
                break;
            }
            ++n2;
        } while (true);
        this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, subMenuBuilder, (View)subMenuBuilder2);
        this.mActionButtonPopup.setForceShowIcon(bl);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(subMenuBuilder);
        return true;
    }

    @Override
    public void onSubUiVisibilityChanged(boolean bl) {
        if (bl) {
            super.onSubMenuSelected(null);
            return;
        }
        if (this.mMenu != null) {
            this.mMenu.close(false);
            return;
        }
    }

    public void setExpandedActionViewsExclusive(boolean bl) {
        this.mExpandedActionViewsExclusive = bl;
    }

    public void setItemLimit(int n) {
        this.mMaxItems = n;
        this.mMaxItemsSet = true;
    }

    public void setMenuView(ActionMenuView actionMenuView) {
        this.mMenuView = actionMenuView;
        actionMenuView.initialize(this.mMenu);
    }

    public void setOverflowIcon(Drawable drawable2) {
        OverflowMenuButton overflowMenuButton = this.mOverflowButton;
        if (overflowMenuButton != null) {
            overflowMenuButton.setImageDrawable(drawable2);
            return;
        }
        this.mPendingOverflowIconSet = true;
        this.mPendingOverflowIcon = drawable2;
    }

    public void setReserveOverflow(boolean bl) {
        this.mReserveOverflow = bl;
        this.mReserveOverflowSet = true;
    }

    public void setWidthLimit(int n, boolean bl) {
        this.mWidthLimit = n;
        this.mStrictWidthLimit = bl;
        this.mWidthLimitSet = true;
    }

    @Override
    public boolean shouldIncludeItem(int n, MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }

    public boolean showOverflowMenu() {
        if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
            this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
            ((View)this.mMenuView).post((Runnable)this.mPostedOpenRunnable);
            super.onSubMenuSelected(null);
            return true;
        }
        return false;
    }

    @Override
    public void updateMenuView(boolean bl) {
        int n;
        int n2;
        Object object;
        super.updateMenuView(bl);
        ((View)this.mMenuView).requestLayout();
        if (this.mMenu != null) {
            object = this.mMenu.getActionItems();
            n = object.size();
            for (n2 = 0; n2 < n; ++n2) {
                ActionProvider actionProvider = object.get(n2).getSupportActionProvider();
                if (actionProvider == null) continue;
                actionProvider.setSubUiVisibilityListener(this);
            }
        }
        object = this.mMenu != null ? this.mMenu.getNonActionItems() : null;
        n2 = 0;
        if (this.mReserveOverflow && object != null) {
            n = object.size();
            n2 = 0;
            if (n == 1) {
                n2 = ((MenuItemImpl)object.get(0)).isActionViewExpanded() ^ true;
            } else if (n > 0) {
                n2 = 1;
            }
        }
        if (n2 != 0) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
            }
            if ((object = (ViewGroup)this.mOverflowButton.getParent()) != this.mMenuView) {
                if (object != null) {
                    object.removeView((View)this.mOverflowButton);
                }
                object = (ActionMenuView)this.mMenuView;
                object.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)object.generateOverflowButtonLayoutParams());
            }
        } else {
            object = this.mOverflowButton;
            if (object != null && object.getParent() == this.mMenuView) {
                ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton);
            }
        }
        ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }

    private class ActionButtonSubmenu
    extends MenuPopupHelper {
        public ActionButtonSubmenu(Context object, SubMenuBuilder subMenuBuilder, View view) {
            super((Context)object, subMenuBuilder, view, false, R.attr.actionOverflowMenuStyle);
            if (!((MenuItemImpl)subMenuBuilder.getItem()).isActionButton()) {
                object = ActionMenuPresenter.this.mOverflowButton == null ? (View)ActionMenuPresenter.this.mMenuView : ActionMenuPresenter.this.mOverflowButton;
                this.setAnchorView((View)object);
            }
            this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        @Override
        protected void onDismiss() {
            ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
            actionMenuPresenter.mActionButtonPopup = null;
            actionMenuPresenter.mOpenSubMenuId = 0;
            super.onDismiss();
        }
    }

    private class ActionMenuPopupCallback
    extends ActionMenuItemView.PopupCallback {
        ActionMenuPopupCallback() {
        }

        @Override
        public ShowableListMenu getPopup() {
            if (ActionMenuPresenter.this.mActionButtonPopup != null) {
                return ActionMenuPresenter.this.mActionButtonPopup.getPopup();
            }
            return null;
        }
    }

    private class OpenOverflowRunnable
    implements Runnable {
        private OverflowPopup mPopup;

        public OpenOverflowRunnable(OverflowPopup overflowPopup) {
            this.mPopup = overflowPopup;
        }

        @Override
        public void run() {
            View view;
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.changeMenuMode();
            }
            if ((view = (View)ActionMenuPresenter.this.mMenuView) != null && view.getWindowToken() != null && this.mPopup.tryShow()) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    private class OverflowMenuButton
    extends AppCompatImageView
    implements ActionMenuView.ActionMenuChildView {
        private final float[] mTempPts;

        public OverflowMenuButton(Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            this.mTempPts = new float[2];
            this.setClickable(true);
            this.setFocusable(true);
            this.setVisibility(0);
            this.setEnabled(true);
            TooltipCompat.setTooltipText((View)this, this.getContentDescription());
            this.setOnTouchListener((View.OnTouchListener)new ForwardingListener((View)this, ActionMenuPresenter.this){
                final /* synthetic */ ActionMenuPresenter val$this$0;
                {
                    this.val$this$0 = actionMenuPresenter;
                    super(view);
                }

                @Override
                public ShowableListMenu getPopup() {
                    if (ActionMenuPresenter.this.mOverflowPopup == null) {
                        return null;
                    }
                    return ActionMenuPresenter.this.mOverflowPopup.getPopup();
                }

                @Override
                public boolean onForwardingStarted() {
                    ActionMenuPresenter.this.showOverflowMenu();
                    return true;
                }

                @Override
                public boolean onForwardingStopped() {
                    if (ActionMenuPresenter.this.mPostedOpenRunnable != null) {
                        return false;
                    }
                    ActionMenuPresenter.this.hideOverflowMenu();
                    return true;
                }
            });
        }

        @Override
        public boolean needsDividerAfter() {
            return false;
        }

        @Override
        public boolean needsDividerBefore() {
            return false;
        }

        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            this.playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
        }

        protected boolean setFrame(int n, int n2, int n3, int n4) {
            boolean bl = super.setFrame(n, n2, n3, n4);
            Drawable drawable2 = this.getDrawable();
            Drawable drawable3 = this.getBackground();
            if (drawable2 != null && drawable3 != null) {
                int n5 = this.getWidth();
                n2 = this.getHeight();
                n = Math.max(n5, n2) / 2;
                int n6 = this.getPaddingLeft();
                int n7 = this.getPaddingRight();
                n3 = this.getPaddingTop();
                n4 = this.getPaddingBottom();
                n5 = (n5 + (n6 - n7)) / 2;
                n2 = (n2 + (n3 - n4)) / 2;
                DrawableCompat.setHotspotBounds(drawable3, n5 - n, n2 - n, n5 + n, n2 + n);
                return bl;
            }
            return bl;
        }

    }

    private class OverflowPopup
    extends MenuPopupHelper {
        public OverflowPopup(Context context, MenuBuilder menuBuilder, View view, boolean bl) {
            super(context, menuBuilder, view, bl, R.attr.actionOverflowMenuStyle);
            this.setGravity(8388613);
            this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        @Override
        protected void onDismiss() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.close();
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
            super.onDismiss();
        }
    }

    private class PopupPresenterCallback
    implements MenuPresenter.Callback {
        PopupPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
            MenuPresenter.Callback callback;
            if (menuBuilder instanceof SubMenuBuilder) {
                menuBuilder.getRootMenu().close(false);
            }
            if ((callback = ActionMenuPresenter.this.getCallback()) != null) {
                callback.onCloseMenu(menuBuilder, bl);
                return;
            }
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            boolean bl = false;
            if (menuBuilder == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)menuBuilder).getItem().getItemId();
            MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
            if (callback != null) {
                bl = callback.onOpenSubMenu(menuBuilder);
            }
            return bl;
        }
    }

    private static class SavedState
    implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(Parcel parcel) {
            this.openSubMenuId = parcel.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int n) {
            parcel.writeInt(this.openSubMenuId);
        }

    }

}

