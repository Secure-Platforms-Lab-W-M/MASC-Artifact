/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.View$OnKeyListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.FrameLayout
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.PopupWindow
 *  android.widget.PopupWindow$OnDismissListener
 *  android.widget.TextView
 */
package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Parcelable;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopup;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.MenuPopupWindow;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

final class StandardMenuPopup
extends MenuPopup
implements PopupWindow.OnDismissListener,
AdapterView.OnItemClickListener,
MenuPresenter,
View.OnKeyListener {
    private final MenuAdapter mAdapter;
    private View mAnchorView;
    private final View.OnAttachStateChangeListener mAttachStateChangeListener;
    private int mContentWidth;
    private final Context mContext;
    private int mDropDownGravity;
    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private boolean mHasContentWidth;
    private final MenuBuilder mMenu;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private final boolean mOverflowOnly;
    final MenuPopupWindow mPopup;
    private final int mPopupMaxWidth;
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    private MenuPresenter.Callback mPresenterCallback;
    private boolean mShowTitle;
    View mShownAnchorView;
    private ViewTreeObserver mTreeObserver;
    private boolean mWasDismissed;

    public StandardMenuPopup(Context context, MenuBuilder menuBuilder, View view, int n, int n2, boolean bl) {
        this.mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(){

            public void onGlobalLayout() {
                if (StandardMenuPopup.this.isShowing() && !StandardMenuPopup.this.mPopup.isModal()) {
                    View view = StandardMenuPopup.this.mShownAnchorView;
                    if (view != null && view.isShown()) {
                        StandardMenuPopup.this.mPopup.show();
                        return;
                    }
                    StandardMenuPopup.this.dismiss();
                    return;
                }
            }
        };
        this.mAttachStateChangeListener = new View.OnAttachStateChangeListener(){

            public void onViewAttachedToWindow(View view) {
            }

            public void onViewDetachedFromWindow(View view) {
                if (StandardMenuPopup.this.mTreeObserver != null) {
                    if (!StandardMenuPopup.this.mTreeObserver.isAlive()) {
                        StandardMenuPopup.this.mTreeObserver = view.getViewTreeObserver();
                    }
                    StandardMenuPopup.this.mTreeObserver.removeGlobalOnLayoutListener(StandardMenuPopup.this.mGlobalLayoutListener);
                }
                view.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
            }
        };
        this.mDropDownGravity = 0;
        this.mContext = context;
        this.mMenu = menuBuilder;
        this.mOverflowOnly = bl;
        this.mAdapter = new MenuAdapter(menuBuilder, LayoutInflater.from((Context)context), this.mOverflowOnly);
        this.mPopupStyleAttr = n;
        this.mPopupStyleRes = n2;
        Resources resources = context.getResources();
        this.mPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
        this.mAnchorView = view;
        this.mPopup = new MenuPopupWindow(this.mContext, null, this.mPopupStyleAttr, this.mPopupStyleRes);
        menuBuilder.addMenuPresenter(this, context);
    }

    private boolean tryShow() {
        if (this.isShowing()) {
            return true;
        }
        if (!this.mWasDismissed) {
            View view = this.mAnchorView;
            if (view == null) {
                return false;
            }
            this.mShownAnchorView = view;
            this.mPopup.setOnDismissListener(this);
            this.mPopup.setOnItemClickListener(this);
            this.mPopup.setModal(true);
            view = this.mShownAnchorView;
            boolean bl = this.mTreeObserver == null;
            this.mTreeObserver = view.getViewTreeObserver();
            if (bl) {
                this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }
            view.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
            this.mPopup.setAnchorView(view);
            this.mPopup.setDropDownGravity(this.mDropDownGravity);
            if (!this.mHasContentWidth) {
                this.mContentWidth = StandardMenuPopup.measureIndividualMenuWidth((ListAdapter)this.mAdapter, null, this.mContext, this.mPopupMaxWidth);
                this.mHasContentWidth = true;
            }
            this.mPopup.setContentWidth(this.mContentWidth);
            this.mPopup.setInputMethodMode(2);
            this.mPopup.setEpicenterBounds(this.getEpicenterBounds());
            this.mPopup.show();
            view = this.mPopup.getListView();
            view.setOnKeyListener((View.OnKeyListener)this);
            if (this.mShowTitle && this.mMenu.getHeaderTitle() != null) {
                FrameLayout frameLayout = (FrameLayout)LayoutInflater.from((Context)this.mContext).inflate(R.layout.abc_popup_menu_header_item_layout, (ViewGroup)view, false);
                TextView textView = (TextView)frameLayout.findViewById(16908310);
                if (textView != null) {
                    textView.setText(this.mMenu.getHeaderTitle());
                }
                frameLayout.setEnabled(false);
                view.addHeaderView((View)frameLayout, (Object)null, false);
            }
            this.mPopup.setAdapter((ListAdapter)this.mAdapter);
            this.mPopup.show();
            return true;
        }
        return false;
    }

    @Override
    public void addMenu(MenuBuilder menuBuilder) {
    }

    @Override
    public void dismiss() {
        if (this.isShowing()) {
            this.mPopup.dismiss();
            return;
        }
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    @Override
    public ListView getListView() {
        return this.mPopup.getListView();
    }

    @Override
    public boolean isShowing() {
        if (!this.mWasDismissed && this.mPopup.isShowing()) {
            return true;
        }
        return false;
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        if (menuBuilder != this.mMenu) {
            return;
        }
        this.dismiss();
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, bl);
            return;
        }
    }

    public void onDismiss() {
        this.mWasDismissed = true;
        this.mMenu.close();
        ViewTreeObserver viewTreeObserver = this.mTreeObserver;
        if (viewTreeObserver != null) {
            if (!viewTreeObserver.isAlive()) {
                this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
            }
            this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
            this.mTreeObserver = null;
        }
        this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
        viewTreeObserver = this.mOnDismissListener;
        if (viewTreeObserver != null) {
            viewTreeObserver.onDismiss();
            return;
        }
    }

    public boolean onKey(View view, int n, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1 && n == 82) {
            this.dismiss();
            return true;
        }
        return false;
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
        if (subMenuBuilder.hasVisibleItems()) {
            Object object = new MenuPopupHelper(this.mContext, subMenuBuilder, this.mShownAnchorView, this.mOverflowOnly, this.mPopupStyleAttr, this.mPopupStyleRes);
            object.setPresenterCallback(this.mPresenterCallback);
            object.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(subMenuBuilder));
            object.setGravity(this.mDropDownGravity);
            object.setOnDismissListener(this.mOnDismissListener);
            this.mOnDismissListener = null;
            this.mMenu.close(false);
            if (object.tryShow(this.mPopup.getHorizontalOffset(), this.mPopup.getVerticalOffset())) {
                object = this.mPresenterCallback;
                if (object != null) {
                    object.onOpenSubMenu(subMenuBuilder);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void setAnchorView(View view) {
        this.mAnchorView = view;
    }

    @Override
    public void setCallback(MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
    }

    @Override
    public void setForceShowIcon(boolean bl) {
        this.mAdapter.setForceShowIcon(bl);
    }

    @Override
    public void setGravity(int n) {
        this.mDropDownGravity = n;
    }

    @Override
    public void setHorizontalOffset(int n) {
        this.mPopup.setHorizontalOffset(n);
    }

    @Override
    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    @Override
    public void setShowTitle(boolean bl) {
        this.mShowTitle = bl;
    }

    @Override
    public void setVerticalOffset(int n) {
        this.mPopup.setVerticalOffset(n);
    }

    @Override
    public void show() {
        if (this.tryShow()) {
            return;
        }
        throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
    }

    @Override
    public void updateMenuView(boolean bl) {
        this.mHasContentWidth = false;
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
            return;
        }
    }

}

