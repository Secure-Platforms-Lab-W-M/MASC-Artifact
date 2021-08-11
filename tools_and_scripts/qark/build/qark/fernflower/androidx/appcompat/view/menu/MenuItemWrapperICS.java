package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.ActionProvider;
import android.view.CollapsibleActionView;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ActionProvider.VisibilityListener;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.FrameLayout;
import androidx.core.internal.view.SupportMenuItem;
import java.lang.reflect.Method;

public class MenuItemWrapperICS extends BaseMenuWrapper implements MenuItem {
   static final String LOG_TAG = "MenuItemWrapper";
   private Method mSetExclusiveCheckableMethod;
   private final SupportMenuItem mWrappedObject;

   public MenuItemWrapperICS(Context var1, SupportMenuItem var2) {
      super(var1);
      if (var2 != null) {
         this.mWrappedObject = var2;
      } else {
         throw new IllegalArgumentException("Wrapped Object can not be null.");
      }
   }

   public boolean collapseActionView() {
      return this.mWrappedObject.collapseActionView();
   }

   public boolean expandActionView() {
      return this.mWrappedObject.expandActionView();
   }

   public ActionProvider getActionProvider() {
      androidx.core.view.ActionProvider var1 = this.mWrappedObject.getSupportActionProvider();
      return var1 instanceof MenuItemWrapperICS.ActionProviderWrapper ? ((MenuItemWrapperICS.ActionProviderWrapper)var1).mInner : null;
   }

   public View getActionView() {
      View var1 = this.mWrappedObject.getActionView();
      return var1 instanceof MenuItemWrapperICS.CollapsibleActionViewWrapper ? ((MenuItemWrapperICS.CollapsibleActionViewWrapper)var1).getWrappedView() : var1;
   }

   public int getAlphabeticModifiers() {
      return this.mWrappedObject.getAlphabeticModifiers();
   }

   public char getAlphabeticShortcut() {
      return this.mWrappedObject.getAlphabeticShortcut();
   }

   public CharSequence getContentDescription() {
      return this.mWrappedObject.getContentDescription();
   }

   public int getGroupId() {
      return this.mWrappedObject.getGroupId();
   }

   public Drawable getIcon() {
      return this.mWrappedObject.getIcon();
   }

   public ColorStateList getIconTintList() {
      return this.mWrappedObject.getIconTintList();
   }

   public Mode getIconTintMode() {
      return this.mWrappedObject.getIconTintMode();
   }

   public Intent getIntent() {
      return this.mWrappedObject.getIntent();
   }

   public int getItemId() {
      return this.mWrappedObject.getItemId();
   }

   public ContextMenuInfo getMenuInfo() {
      return this.mWrappedObject.getMenuInfo();
   }

   public int getNumericModifiers() {
      return this.mWrappedObject.getNumericModifiers();
   }

   public char getNumericShortcut() {
      return this.mWrappedObject.getNumericShortcut();
   }

   public int getOrder() {
      return this.mWrappedObject.getOrder();
   }

   public SubMenu getSubMenu() {
      return this.getSubMenuWrapper(this.mWrappedObject.getSubMenu());
   }

   public CharSequence getTitle() {
      return this.mWrappedObject.getTitle();
   }

   public CharSequence getTitleCondensed() {
      return this.mWrappedObject.getTitleCondensed();
   }

   public CharSequence getTooltipText() {
      return this.mWrappedObject.getTooltipText();
   }

   public boolean hasSubMenu() {
      return this.mWrappedObject.hasSubMenu();
   }

   public boolean isActionViewExpanded() {
      return this.mWrappedObject.isActionViewExpanded();
   }

   public boolean isCheckable() {
      return this.mWrappedObject.isCheckable();
   }

   public boolean isChecked() {
      return this.mWrappedObject.isChecked();
   }

   public boolean isEnabled() {
      return this.mWrappedObject.isEnabled();
   }

   public boolean isVisible() {
      return this.mWrappedObject.isVisible();
   }

   public MenuItem setActionProvider(ActionProvider var1) {
      Object var2;
      if (VERSION.SDK_INT >= 16) {
         var2 = new MenuItemWrapperICS.ActionProviderWrapperJB(this.mContext, var1);
      } else {
         var2 = new MenuItemWrapperICS.ActionProviderWrapper(this.mContext, var1);
      }

      SupportMenuItem var3 = this.mWrappedObject;
      if (var1 == null) {
         var2 = null;
      }

      var3.setSupportActionProvider((androidx.core.view.ActionProvider)var2);
      return this;
   }

   public MenuItem setActionView(int var1) {
      this.mWrappedObject.setActionView(var1);
      View var2 = this.mWrappedObject.getActionView();
      if (var2 instanceof CollapsibleActionView) {
         this.mWrappedObject.setActionView(new MenuItemWrapperICS.CollapsibleActionViewWrapper(var2));
      }

      return this;
   }

   public MenuItem setActionView(View var1) {
      Object var2 = var1;
      if (var1 instanceof CollapsibleActionView) {
         var2 = new MenuItemWrapperICS.CollapsibleActionViewWrapper(var1);
      }

      this.mWrappedObject.setActionView((View)var2);
      return this;
   }

   public MenuItem setAlphabeticShortcut(char var1) {
      this.mWrappedObject.setAlphabeticShortcut(var1);
      return this;
   }

   public MenuItem setAlphabeticShortcut(char var1, int var2) {
      this.mWrappedObject.setAlphabeticShortcut(var1, var2);
      return this;
   }

   public MenuItem setCheckable(boolean var1) {
      this.mWrappedObject.setCheckable(var1);
      return this;
   }

   public MenuItem setChecked(boolean var1) {
      this.mWrappedObject.setChecked(var1);
      return this;
   }

   public MenuItem setContentDescription(CharSequence var1) {
      this.mWrappedObject.setContentDescription(var1);
      return this;
   }

   public MenuItem setEnabled(boolean var1) {
      this.mWrappedObject.setEnabled(var1);
      return this;
   }

   public void setExclusiveCheckable(boolean var1) {
      try {
         if (this.mSetExclusiveCheckableMethod == null) {
            this.mSetExclusiveCheckableMethod = this.mWrappedObject.getClass().getDeclaredMethod("setExclusiveCheckable", Boolean.TYPE);
         }

         this.mSetExclusiveCheckableMethod.invoke(this.mWrappedObject, var1);
      } catch (Exception var3) {
         Log.w("MenuItemWrapper", "Error while calling setExclusiveCheckable", var3);
      }
   }

   public MenuItem setIcon(int var1) {
      this.mWrappedObject.setIcon(var1);
      return this;
   }

   public MenuItem setIcon(Drawable var1) {
      this.mWrappedObject.setIcon(var1);
      return this;
   }

   public MenuItem setIconTintList(ColorStateList var1) {
      this.mWrappedObject.setIconTintList(var1);
      return this;
   }

   public MenuItem setIconTintMode(Mode var1) {
      this.mWrappedObject.setIconTintMode(var1);
      return this;
   }

   public MenuItem setIntent(Intent var1) {
      this.mWrappedObject.setIntent(var1);
      return this;
   }

   public MenuItem setNumericShortcut(char var1) {
      this.mWrappedObject.setNumericShortcut(var1);
      return this;
   }

   public MenuItem setNumericShortcut(char var1, int var2) {
      this.mWrappedObject.setNumericShortcut(var1, var2);
      return this;
   }

   public MenuItem setOnActionExpandListener(OnActionExpandListener var1) {
      SupportMenuItem var2 = this.mWrappedObject;
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
      SupportMenuItem var2 = this.mWrappedObject;
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
      this.mWrappedObject.setShortcut(var1, var2);
      return this;
   }

   public MenuItem setShortcut(char var1, char var2, int var3, int var4) {
      this.mWrappedObject.setShortcut(var1, var2, var3, var4);
      return this;
   }

   public void setShowAsAction(int var1) {
      this.mWrappedObject.setShowAsAction(var1);
   }

   public MenuItem setShowAsActionFlags(int var1) {
      this.mWrappedObject.setShowAsActionFlags(var1);
      return this;
   }

   public MenuItem setTitle(int var1) {
      this.mWrappedObject.setTitle(var1);
      return this;
   }

   public MenuItem setTitle(CharSequence var1) {
      this.mWrappedObject.setTitle(var1);
      return this;
   }

   public MenuItem setTitleCondensed(CharSequence var1) {
      this.mWrappedObject.setTitleCondensed(var1);
      return this;
   }

   public MenuItem setTooltipText(CharSequence var1) {
      this.mWrappedObject.setTooltipText(var1);
      return this;
   }

   public MenuItem setVisible(boolean var1) {
      return this.mWrappedObject.setVisible(var1);
   }

   private class ActionProviderWrapper extends androidx.core.view.ActionProvider {
      final ActionProvider mInner;

      ActionProviderWrapper(Context var2, ActionProvider var3) {
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

   private class ActionProviderWrapperJB extends MenuItemWrapperICS.ActionProviderWrapper implements VisibilityListener {
      private androidx.core.view.ActionProvider.VisibilityListener mListener;

      ActionProviderWrapperJB(Context var2, ActionProvider var3) {
         super(var2, var3);
      }

      public boolean isVisible() {
         return this.mInner.isVisible();
      }

      public void onActionProviderVisibilityChanged(boolean var1) {
         androidx.core.view.ActionProvider.VisibilityListener var2 = this.mListener;
         if (var2 != null) {
            var2.onActionProviderVisibilityChanged(var1);
         }

      }

      public View onCreateActionView(MenuItem var1) {
         return this.mInner.onCreateActionView(var1);
      }

      public boolean overridesItemVisibility() {
         return this.mInner.overridesItemVisibility();
      }

      public void refreshVisibility() {
         this.mInner.refreshVisibility();
      }

      public void setVisibilityListener(androidx.core.view.ActionProvider.VisibilityListener var1) {
         this.mListener = var1;
         ActionProvider var2 = this.mInner;
         MenuItemWrapperICS.ActionProviderWrapperJB var3;
         if (var1 != null) {
            var3 = this;
         } else {
            var3 = null;
         }

         var2.setVisibilityListener(var3);
      }
   }

   static class CollapsibleActionViewWrapper extends FrameLayout implements androidx.appcompat.view.CollapsibleActionView {
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

   private class OnActionExpandListenerWrapper implements OnActionExpandListener {
      private final OnActionExpandListener mObject;

      OnActionExpandListenerWrapper(OnActionExpandListener var2) {
         this.mObject = var2;
      }

      public boolean onMenuItemActionCollapse(MenuItem var1) {
         return this.mObject.onMenuItemActionCollapse(MenuItemWrapperICS.this.getMenuItemWrapper(var1));
      }

      public boolean onMenuItemActionExpand(MenuItem var1) {
         return this.mObject.onMenuItemActionExpand(MenuItemWrapperICS.this.getMenuItemWrapper(var1));
      }
   }

   private class OnMenuItemClickListenerWrapper implements OnMenuItemClickListener {
      private final OnMenuItemClickListener mObject;

      OnMenuItemClickListenerWrapper(OnMenuItemClickListener var2) {
         this.mObject = var2;
      }

      public boolean onMenuItemClick(MenuItem var1) {
         return this.mObject.onMenuItemClick(MenuItemWrapperICS.this.getMenuItemWrapper(var1));
      }
   }
}
