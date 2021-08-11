// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.view.menu;

import android.os.Parcelable;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.support.v7.widget.MenuPopupWindow;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.content.Context;
import android.view.View$OnAttachStateChangeListener;
import android.view.View;
import android.view.View$OnKeyListener;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.PopupWindow$OnDismissListener;

final class StandardMenuPopup extends MenuPopup implements PopupWindow$OnDismissListener, AdapterView$OnItemClickListener, MenuPresenter, View$OnKeyListener
{
    private final MenuAdapter mAdapter;
    private View mAnchorView;
    private final View$OnAttachStateChangeListener mAttachStateChangeListener;
    private int mContentWidth;
    private final Context mContext;
    private int mDropDownGravity;
    private final ViewTreeObserver$OnGlobalLayoutListener mGlobalLayoutListener;
    private boolean mHasContentWidth;
    private final MenuBuilder mMenu;
    private PopupWindow$OnDismissListener mOnDismissListener;
    private final boolean mOverflowOnly;
    final MenuPopupWindow mPopup;
    private final int mPopupMaxWidth;
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    private Callback mPresenterCallback;
    private boolean mShowTitle;
    View mShownAnchorView;
    private ViewTreeObserver mTreeObserver;
    private boolean mWasDismissed;
    
    public StandardMenuPopup(final Context mContext, final MenuBuilder mMenu, final View mAnchorView, final int mPopupStyleAttr, final int mPopupStyleRes, final boolean mOverflowOnly) {
        this.mGlobalLayoutListener = (ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (!StandardMenuPopup.this.isShowing() || StandardMenuPopup.this.mPopup.isModal()) {
                    return;
                }
                final View mShownAnchorView = StandardMenuPopup.this.mShownAnchorView;
                if (mShownAnchorView != null && mShownAnchorView.isShown()) {
                    StandardMenuPopup.this.mPopup.show();
                    return;
                }
                StandardMenuPopup.this.dismiss();
            }
        };
        this.mAttachStateChangeListener = (View$OnAttachStateChangeListener)new View$OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(final View view) {
            }
            
            public void onViewDetachedFromWindow(final View view) {
                if (StandardMenuPopup.this.mTreeObserver != null) {
                    if (!StandardMenuPopup.this.mTreeObserver.isAlive()) {
                        StandardMenuPopup.this.mTreeObserver = view.getViewTreeObserver();
                    }
                    StandardMenuPopup.this.mTreeObserver.removeGlobalOnLayoutListener(StandardMenuPopup.this.mGlobalLayoutListener);
                }
                view.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
            }
        };
        this.mDropDownGravity = 0;
        this.mContext = mContext;
        this.mMenu = mMenu;
        this.mOverflowOnly = mOverflowOnly;
        this.mAdapter = new MenuAdapter(mMenu, LayoutInflater.from(mContext), this.mOverflowOnly);
        this.mPopupStyleAttr = mPopupStyleAttr;
        this.mPopupStyleRes = mPopupStyleRes;
        final Resources resources = mContext.getResources();
        this.mPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
        this.mAnchorView = mAnchorView;
        this.mPopup = new MenuPopupWindow(this.mContext, null, this.mPopupStyleAttr, this.mPopupStyleRes);
        mMenu.addMenuPresenter(this, mContext);
    }
    
    private boolean tryShow() {
        if (this.isShowing()) {
            return true;
        }
        if (this.mWasDismissed) {
            return false;
        }
        final View mAnchorView = this.mAnchorView;
        if (mAnchorView == null) {
            return false;
        }
        this.mShownAnchorView = mAnchorView;
        this.mPopup.setOnDismissListener((PopupWindow$OnDismissListener)this);
        this.mPopup.setOnItemClickListener((AdapterView$OnItemClickListener)this);
        this.mPopup.setModal(true);
        final View mShownAnchorView = this.mShownAnchorView;
        final boolean b = this.mTreeObserver == null;
        this.mTreeObserver = mShownAnchorView.getViewTreeObserver();
        if (b) {
            this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
        }
        mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
        this.mPopup.setAnchorView(mShownAnchorView);
        this.mPopup.setDropDownGravity(this.mDropDownGravity);
        if (!this.mHasContentWidth) {
            this.mContentWidth = MenuPopup.measureIndividualMenuWidth((ListAdapter)this.mAdapter, null, this.mContext, this.mPopupMaxWidth);
            this.mHasContentWidth = true;
        }
        this.mPopup.setContentWidth(this.mContentWidth);
        this.mPopup.setInputMethodMode(2);
        this.mPopup.setEpicenterBounds(this.getEpicenterBounds());
        this.mPopup.show();
        final ListView listView = this.mPopup.getListView();
        listView.setOnKeyListener((View$OnKeyListener)this);
        if (this.mShowTitle && this.mMenu.getHeaderTitle() != null) {
            final FrameLayout frameLayout = (FrameLayout)LayoutInflater.from(this.mContext).inflate(R.layout.abc_popup_menu_header_item_layout, (ViewGroup)listView, false);
            final TextView textView = (TextView)frameLayout.findViewById(16908310);
            if (textView != null) {
                textView.setText(this.mMenu.getHeaderTitle());
            }
            frameLayout.setEnabled(false);
            listView.addHeaderView((View)frameLayout, (Object)null, false);
        }
        this.mPopup.setAdapter((ListAdapter)this.mAdapter);
        this.mPopup.show();
        return true;
    }
    
    @Override
    public void addMenu(final MenuBuilder menuBuilder) {
    }
    
    public void dismiss() {
        if (this.isShowing()) {
            this.mPopup.dismiss();
        }
    }
    
    public boolean flagActionItems() {
        return false;
    }
    
    public ListView getListView() {
        return this.mPopup.getListView();
    }
    
    public boolean isShowing() {
        return !this.mWasDismissed && this.mPopup.isShowing();
    }
    
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        if (menuBuilder != this.mMenu) {
            return;
        }
        this.dismiss();
        final Callback mPresenterCallback = this.mPresenterCallback;
        if (mPresenterCallback != null) {
            mPresenterCallback.onCloseMenu(menuBuilder, b);
        }
    }
    
    public void onDismiss() {
        this.mWasDismissed = true;
        this.mMenu.close();
        final ViewTreeObserver mTreeObserver = this.mTreeObserver;
        if (mTreeObserver != null) {
            if (!mTreeObserver.isAlive()) {
                this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
            }
            this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
            this.mTreeObserver = null;
        }
        this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
        final PopupWindow$OnDismissListener mOnDismissListener = this.mOnDismissListener;
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }
    
    public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1 && n == 82) {
            this.dismiss();
            return true;
        }
        return false;
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
    }
    
    public Parcelable onSaveInstanceState() {
        return null;
    }
    
    public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        final MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this.mContext, subMenuBuilder, this.mShownAnchorView, this.mOverflowOnly, this.mPopupStyleAttr, this.mPopupStyleRes);
        menuPopupHelper.setPresenterCallback(this.mPresenterCallback);
        menuPopupHelper.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(subMenuBuilder));
        menuPopupHelper.setGravity(this.mDropDownGravity);
        menuPopupHelper.setOnDismissListener(this.mOnDismissListener);
        this.mOnDismissListener = null;
        this.mMenu.close(false);
        if (menuPopupHelper.tryShow(this.mPopup.getHorizontalOffset(), this.mPopup.getVerticalOffset())) {
            final Callback mPresenterCallback = this.mPresenterCallback;
            if (mPresenterCallback != null) {
                mPresenterCallback.onOpenSubMenu(subMenuBuilder);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void setAnchorView(final View mAnchorView) {
        this.mAnchorView = mAnchorView;
    }
    
    public void setCallback(final Callback mPresenterCallback) {
        this.mPresenterCallback = mPresenterCallback;
    }
    
    @Override
    public void setForceShowIcon(final boolean forceShowIcon) {
        this.mAdapter.setForceShowIcon(forceShowIcon);
    }
    
    @Override
    public void setGravity(final int mDropDownGravity) {
        this.mDropDownGravity = mDropDownGravity;
    }
    
    @Override
    public void setHorizontalOffset(final int horizontalOffset) {
        this.mPopup.setHorizontalOffset(horizontalOffset);
    }
    
    @Override
    public void setOnDismissListener(final PopupWindow$OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    @Override
    public void setShowTitle(final boolean mShowTitle) {
        this.mShowTitle = mShowTitle;
    }
    
    @Override
    public void setVerticalOffset(final int verticalOffset) {
        this.mPopup.setVerticalOffset(verticalOffset);
    }
    
    public void show() {
        if (this.tryShow()) {
            return;
        }
        throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
    }
    
    public void updateMenuView(final boolean b) {
        this.mHasContentWidth = false;
        final MenuAdapter mAdapter = this.mAdapter;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
