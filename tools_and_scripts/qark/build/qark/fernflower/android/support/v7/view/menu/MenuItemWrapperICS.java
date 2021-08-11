package android.support.v7.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenuItem;
import android.util.Log;
import android.view.ActionProvider;
import android.view.CollapsibleActionView;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.FrameLayout;
import java.lang.reflect.Method;

@RequiresApi(14)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MenuItemWrapperICS extends BaseMenuWrapper implements MenuItem {
   static final String LOG_TAG = "MenuItemWrapper";
   private Method mSetExclusiveCheckableMethod;

   MenuItemWrapperICS(Context var1, SupportMenuItem var2) {
      super(var1, var2);
   }

   public boolean collapseActionView() {
      return ((SupportMenuItem)this.mWrappedObject).collapseActionView();
   }

   MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(ActionProvider var1) {
      return new MenuItemWrapperICS.ActionProviderWrapper(this.mContext, var1);
   }

   public boolean expandActionView() {
      return ((SupportMenuItem)this.mWrappedObject).expandActionView();
   }

   public ActionProvider getActionProvider() {
      android.support.v4.view.ActionProvider var1 = ((SupportMenuItem)this.mWrappedObject).getSupportActionProvider();
      return var1 instanceof MenuItemWrapperICS.ActionProviderWrapper ? ((MenuItemWrapperICS.ActionProviderWrapper)var1).mInner : null;
   }

   public View getActionView() {
      View var1 = ((SupportMenuItem)this.mWrappedObject).getActionView();
      return var1 instanceof MenuItemWrapperICS.CollapsibleActionViewWrapper ? ((MenuItemWrapperICS.CollapsibleActionViewWrapper)var1).getWrappedView() : var1;
   }

   public int getAlphabeticModifiers() {
      return ((SupportMenuItem)this.mWrappedObject).getAlphabeticModifiers();
   }

   public char getAlphabeticShortcut() {
      return ((SupportMenuItem)this.mWrappedObject).getAlphabeticShortcut();
   }

   public CharSequence getContentDescription() {
      return ((SupportMenuItem)this.mWrappedObject).getContentDescription();
   }

   public int getGroupId() {
      return ((SupportMenuItem)this.mWrappedObject).getGroupId();
   }

   public Drawable getIcon() {
      return ((SupportMenuItem)this.mWrappedObject).getIcon();
   }

   public ColorStateList getIconTintList() {
      return ((SupportMenuItem)this.mWrappedObject).getIconTintList();
   }

   public Mode getIconTintMode() {
      return ((SupportMenuItem)this.mWrappedObject).getIconTintMode();
   }

   public Intent getIntent() {
      return ((SupportMenuItem)this.mWrappedObject).getIntent();
   }

   public int getItemId() {
      return ((SupportMenuItem)this.mWrappedObject).getItemId();
   }

   public ContextMenuInfo getMenuInfo() {
      return ((SupportMenuItem)this.mWrappedObject).getMenuInfo();
   }

   public int getNumericModifiers() {
      return ((SupportMenuItem)this.mWrappedObject).getNumericModifiers();
   }

   public char getNumericShortcut() {
      return ((SupportMenuItem)this.mWrappedObject).getNumericShortcut();
   }

   public int getOrder() {
      return ((SupportMenuItem)this.mWrappedObject).getOrder();
   }

   public SubMenu getSubMenu() {
      return this.getSubMenuWrapper(((SupportMenuItem)this.mWrappedObject).getSubMenu());
   }

   public CharSequence getTitle() {
      return ((SupportMenuItem)this.mWrappedObject).getTitle();
   }

   public CharSequence getTitleCondensed() {
      return ((SupportMenuItem)this.mWrappedObject).getTitleCondensed();
   }

   public CharSequence getTooltipText() {
      return ((SupportMenuItem)this.mWrappedObject).getTooltipText();
   }

   public boolean hasSubMenu() {
      return ((SupportMenuItem)this.mWrappedObject).hasSubMenu();
   }

   public boolean isActionViewExpanded() {
      return ((SupportMenuItem)this.mWrappedObject).isActionViewExpanded();
   }

   public boolean isCheckable() {
      return ((SupportMenuItem)this.mWrappedObject).isCheckable();
   }

   public boolean isChecked() {
      return ((SupportMenuItem)this.mWrappedObject).isChecked();
   }

   public boolean isEnabled() {
      return ((SupportMenuItem)this.mWrappedObject).isEnabled();
   }

   public boolean isVisible() {
      return ((SupportMenuItem)this.mWrappedObject).isVisible();
   }

   public MenuItem setActionProvider(ActionProvider var1) {
      SupportMenuItem var2 = (SupportMenuItem)this.mWrappedObject;
      MenuItemWrapperICS.ActionProviderWrapper var3;
      if (var1 != null) {
         var3 = this.createActionProviderWrapper(var1);
      } else {
         var3 = null;
      }

      var2.setSupportActionProvider(var3);
      return this;
   }

   public MenuItem setActionView(int var1) {
      ((SupportMenuItem)this.mWrappedObject).setActionView(var1);
      View var2 = ((SupportMenuItem)this.mWrappedObject).getActionView();
      if (var2 instanceof CollapsibleActionView) {
         ((SupportMenuItem)this.mWrappedObject).setActionView(new MenuItemWrapperICS.CollapsibleActionViewWrapper(var2));
      }

      return this;
   }

   public MenuItem setActionView(View var1) {
      Object var2 = var1;
      if (var1 instanceof CollapsibleActionView) {
         var2 = new MenuItemWrapperICS.CollapsibleActionViewWrapper(var1);
      }

      ((SupportMenuItem)this.mWrappedObject).setActionView((View)var2);
      return this;
   }

   public MenuItem setAlphabeticShortcut(char var1) {
      ((SupportMenuItem)this.mWrappedObject).setAlphabeticShortcut(var1);
      return this;
   }

   public MenuItem setAlphabeticShortcut(char var1, int var2) {
      ((SupportMenuItem)this.mWrappedObject).setAlphabeticShortcut(var1, var2);
      return this;
   }

   public MenuItem setCheckable(boolean var1) {
      ((SupportMenuItem)this.mWrappedObject).setCheckable(var1);
      return this;
   }

   public MenuItem setChecked(boolean var1) {
      ((SupportMenuItem)this.mWrappedObject).setChecked(var1);
      return this;
   }

   public MenuItem setContentDescription(CharSequence var1) {
      ((SupportMenuItem)this.mWrappedObject).setContentDescription(var1);
      return this;
   }

   public MenuItem setEnabled(boolean var1) {
      ((SupportMenuItem)this.mWrappedObject).setEnabled(var1);
      return this;
   }

   public void setExclusiveCheckable(boolean var1) {
      try {
         if (this.mSetExclusiveCheckableMethod == null) {
            this.mSetExclusiveCheckableMethod = ((SupportMenuItem)this.mWrappedObject).getClass().getDeclaredMethod("setExclusiveCheckable", Boolean.TYPE);
         }

         this.mSetExclusiveCheckableMethod.invoke(this.mWrappedObject, var1);
      } catch (Exception var3) {
         Log.w("MenuItemWrapper", "Error while calling setExclusiveCheckable", var3);
      }
   }

   public MenuItem setIcon(int var1) {
      ((SupportMenuItem)this.mWrappedObject).setIcon(var1);
      return this;
   }

   public MenuItem setIcon(Drawable var1) {
      ((SupportMenuItem)this.mWrappedObject).setIcon(var1);
      return this;
   }

   public MenuItem setIconTintList(ColorStateList var1) {
      ((SupportMenuItem)this.mWrappedObject).setIconTintList(var1);
      return this;
   }

   public MenuItem setIconTintMode(Mode var1) {
      ((SupportMenuItem)this.mWrappedObject).setIconTintMode(var1);
      return this;
   }

   public MenuItem setIntent(Intent var1) {
      ((SupportMenuItem)this.mWrappedObject).setIntent(var1);
      return this;
   }

   public MenuItem setNumericShortcut(char var1) {
      ((SupportMenuItem)this.mWrappedObject).setNumericShortcut(var1);
      return this;
   }

   public MenuItem setNumericShortcut(char var1, int var2) {
      ((SupportMenuItem)this.mWrappedObject).setNumericShortcut(var1, var2);
      return this;
   }

   public MenuItem setOnActionExpandListener(OnActionExpandListener var1) {
      SupportMenuItem var2 = (SupportMenuItem)this.mWrappedObject;
      MenuItemWrapperICS.OnActionExpandListenerWrapper var3;
      if (var1 != null) {
         var3 = new MenuItemWrapperICS.OnActionExpandListenerWrapper(var1);
      } else {
         var3 = null;
      }

      var2.setOnActionExpandListener(var3);
      return this;
   }

   public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener var1) {
      SupportMenuItem var2 = (SupportMenuItem)this.mWrappedObject;
      MenuItemWrapperICS.OnMenuItemClickListenerWrapper var3;
      if (var1 != null) {
         var3 = new MenuItemWrapperICS.OnMenuItemClickListenerWrapper(var1);
      } else {
         var3 = null;
      }

      var2.setOnMenuItemClickListener(var3);
      return this;
   }

   public MenuItem setShortcut(char var1, char var2) {
      ((SupportMenuItem)this.mWrappedObject).setShortcut(var1, var2);
      return this;
   }

   public MenuItem setShortcut(char var1, char var2, int var3, int var4) {
      ((SupportMenuItem)this.mWrappedObject).setShortcut(var1, var2, var3, var4);
      return this;
   }

   public void setShowAsAction(int var1) {
      ((SupportMenuItem)this.mWrappedObject).setShowAsAction(var1);
   }

   public MenuItem setShowAsActionFlags(int var1) {
      ((SupportMenuItem)this.mWrappedObject).setShowAsActionFlags(var1);
      return this;
   }

   public MenuItem setTitle(int var1) {
      ((SupportMenuItem)this.mWrappedObject).setTitle(var1);
      return this;
   }

   public MenuItem setTitle(CharSequence var1) {
      ((SupportMenuItem)this.mWrappedObject).setTitle(var1);
      return this;
   }

   public MenuItem setTitleCondensed(CharSequence var1) {
      ((SupportMenuItem)this.mWrappedObject).setTitleCondensed(var1);
      return this;
   }

   public MenuItem setTooltipText(CharSequence var1) {
      ((SupportMenuItem)this.mWrappedObject).setTooltipText(var1);
      return this;
   }

   public MenuItem setVisible(boolean var1) {
      return ((SupportMenuItem)this.mWrappedObject).setVisible(var1);
   }

   class ActionProviderWrapper extends android.support.v4.view.ActionProvider {
      final ActionProvider mInner;

      public ActionProviderWrapper(Context var2, ActionProvider var3) {
         super(var2);
         this.mInner = var3;
      }

      public boolean hasSubMenu() {
         return this.mInner.hasSubMenu();
      }

      public View onCreateActionView() {
         return this.mInner.onCreateActionView();
      }

      public boolean onPerformDefaultAction() {
         return this.mInner.onPerformDefaultAction();
      }

      public void onPrepareSubMenu(SubMenu var1) {
         this.mInner.onPrepareSubMenu(MenuItemWrapperICS.this.getSubMenuWrapper(var1));
      }
   }

   static class CollapsibleActionViewWrapper extends FrameLayout implements android.support.v7.view.CollapsibleActionView {
      final CollapsibleActionView mWrappedView;

      CollapsibleActionViewWrapper(View var1) {
         super(var1.getContext());
         this.mWrappedView = (CollapsibleActionView)var1;
         this.addView(var1);
      }

      View getWrappedView() {
         return (View)this.mWrappedView;
      }

      public void onActionViewCollapsed() {
         this.mWrappedView.onActionViewCollapsed();
      }

      public void onActionViewExpanded() {
         this.mWrappedView.onActionViewExpanded();
      }
   }

   private class OnActionExpandListenerWrapper extends BaseWrapper implements OnActionExpandListener {
      OnActionExpandListenerWrapper(OnActionExpandListener var2) {
         super(var2);
      }

      public boolean onMenuItemActionCollapse(MenuItem var1) {
         return ((OnActionExpandListener)this.mWrappedObject).onMenuItemActionCollapse(MenuItemWrapperICS.this.getMenuItemWrapper(var1));
      }

      public boolean onMenuItemActionExpand(MenuItem var1) {
         return ((OnActionExpandListener)this.mWrappedObject).onMenuItemActionExpand(MenuItemWrapperICS.this.getMenuItemWrapper(var1));
      }
   }

   private class OnMenuItemClickListenerWrapper extends BaseWrapper implements OnMenuItemClickListener {
      OnMenuItemClickListenerWrapper(OnMenuItemClickListener var2) {
         super(var2);
      }

      public boolean onMenuItemClick(MenuItem var1) {
         return ((OnMenuItemClickListener)this.mWrappedObject).onMenuItemClick(MenuItemWrapperICS.this.getMenuItemWrapper(var1));
      }
   }
}
