package android.support.v7.view;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.ActionBarContextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.lang.ref.WeakReference;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class StandaloneActionMode extends ActionMode implements MenuBuilder.Callback {
   private ActionMode.Callback mCallback;
   private Context mContext;
   private ActionBarContextView mContextView;
   private WeakReference mCustomView;
   private boolean mFinished;
   private boolean mFocusable;
   private MenuBuilder mMenu;

   public StandaloneActionMode(Context var1, ActionBarContextView var2, ActionMode.Callback var3, boolean var4) {
      this.mContext = var1;
      this.mContextView = var2;
      this.mCallback = var3;
      this.mMenu = (new MenuBuilder(var2.getContext())).setDefaultShowAsAction(1);
      this.mMenu.setCallback(this);
      this.mFocusable = var4;
   }

   public void finish() {
      if (!this.mFinished) {
         this.mFinished = true;
         this.mContextView.sendAccessibilityEvent(32);
         this.mCallback.onDestroyActionMode(this);
      }
   }

   public View getCustomView() {
      WeakReference var1 = this.mCustomView;
      return var1 != null ? (View)var1.get() : null;
   }

   public Menu getMenu() {
      return this.mMenu;
   }

   public MenuInflater getMenuInflater() {
      return new SupportMenuInflater(this.mContextView.getContext());
   }

   public CharSequence getSubtitle() {
      return this.mContextView.getSubtitle();
   }

   public CharSequence getTitle() {
      return this.mContextView.getTitle();
   }

   public void invalidate() {
      this.mCallback.onPrepareActionMode(this, this.mMenu);
   }

   public boolean isTitleOptional() {
      return this.mContextView.isTitleOptional();
   }

   public boolean isUiFocusable() {
      return this.mFocusable;
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
   }

   public void onCloseSubMenu(SubMenuBuilder var1) {
   }

   public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      return this.mCallback.onActionItemClicked(this, var2);
   }

   public void onMenuModeChange(MenuBuilder var1) {
      this.invalidate();
      this.mContextView.showOverflowMenu();
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      if (!var1.hasVisibleItems()) {
         return true;
      } else {
         (new MenuPopupHelper(this.mContextView.getContext(), var1)).show();
         return true;
      }
   }

   public void setCustomView(View var1) {
      this.mContextView.setCustomView(var1);
      WeakReference var2;
      if (var1 != null) {
         var2 = new WeakReference(var1);
      } else {
         var2 = null;
      }

      this.mCustomView = var2;
   }

   public void setSubtitle(int var1) {
      this.setSubtitle(this.mContext.getString(var1));
   }

   public void setSubtitle(CharSequence var1) {
      this.mContextView.setSubtitle(var1);
   }

   public void setTitle(int var1) {
      this.setTitle(this.mContext.getString(var1));
   }

   public void setTitle(CharSequence var1) {
      this.mContextView.setTitle(var1);
   }

   public void setTitleOptionalHint(boolean var1) {
      super.setTitleOptionalHint(var1);
      this.mContextView.setTitleOptional(var1);
   }
}
