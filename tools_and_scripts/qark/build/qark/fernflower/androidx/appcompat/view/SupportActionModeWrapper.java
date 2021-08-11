package androidx.appcompat.view;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ActionMode.Callback;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.view.menu.MenuWrapperICS;
import androidx.collection.SimpleArrayMap;
import androidx.core.internal.view.SupportMenu;
import androidx.core.internal.view.SupportMenuItem;
import java.util.ArrayList;

public class SupportActionModeWrapper extends android.view.ActionMode {
   final Context mContext;
   final ActionMode mWrappedObject;

   public SupportActionModeWrapper(Context var1, ActionMode var2) {
      this.mContext = var1;
      this.mWrappedObject = var2;
   }

   public void finish() {
      this.mWrappedObject.finish();
   }

   public View getCustomView() {
      return this.mWrappedObject.getCustomView();
   }

   public Menu getMenu() {
      return new MenuWrapperICS(this.mContext, (SupportMenu)this.mWrappedObject.getMenu());
   }

   public MenuInflater getMenuInflater() {
      return this.mWrappedObject.getMenuInflater();
   }

   public CharSequence getSubtitle() {
      return this.mWrappedObject.getSubtitle();
   }

   public Object getTag() {
      return this.mWrappedObject.getTag();
   }

   public CharSequence getTitle() {
      return this.mWrappedObject.getTitle();
   }

   public boolean getTitleOptionalHint() {
      return this.mWrappedObject.getTitleOptionalHint();
   }

   public void invalidate() {
      this.mWrappedObject.invalidate();
   }

   public boolean isTitleOptional() {
      return this.mWrappedObject.isTitleOptional();
   }

   public void setCustomView(View var1) {
      this.mWrappedObject.setCustomView(var1);
   }

   public void setSubtitle(int var1) {
      this.mWrappedObject.setSubtitle(var1);
   }

   public void setSubtitle(CharSequence var1) {
      this.mWrappedObject.setSubtitle(var1);
   }

   public void setTag(Object var1) {
      this.mWrappedObject.setTag(var1);
   }

   public void setTitle(int var1) {
      this.mWrappedObject.setTitle(var1);
   }

   public void setTitle(CharSequence var1) {
      this.mWrappedObject.setTitle(var1);
   }

   public void setTitleOptionalHint(boolean var1) {
      this.mWrappedObject.setTitleOptionalHint(var1);
   }

   public static class CallbackWrapper implements ActionMode.Callback {
      final ArrayList mActionModes;
      final Context mContext;
      final SimpleArrayMap mMenus;
      final Callback mWrappedCallback;

      public CallbackWrapper(Context var1, Callback var2) {
         this.mContext = var1;
         this.mWrappedCallback = var2;
         this.mActionModes = new ArrayList();
         this.mMenus = new SimpleArrayMap();
      }

      private Menu getMenuWrapper(Menu var1) {
         Menu var3 = (Menu)this.mMenus.get(var1);
         Object var2 = var3;
         if (var3 == null) {
            var2 = new MenuWrapperICS(this.mContext, (SupportMenu)var1);
            this.mMenus.put(var1, var2);
         }

         return (Menu)var2;
      }

      public android.view.ActionMode getActionModeWrapper(ActionMode var1) {
         int var2 = 0;

         for(int var3 = this.mActionModes.size(); var2 < var3; ++var2) {
            SupportActionModeWrapper var4 = (SupportActionModeWrapper)this.mActionModes.get(var2);
            if (var4 != null && var4.mWrappedObject == var1) {
               return var4;
            }
         }

         SupportActionModeWrapper var5 = new SupportActionModeWrapper(this.mContext, var1);
         this.mActionModes.add(var5);
         return var5;
      }

      public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
         return this.mWrappedCallback.onActionItemClicked(this.getActionModeWrapper(var1), new MenuItemWrapperICS(this.mContext, (SupportMenuItem)var2));
      }

      public boolean onCreateActionMode(ActionMode var1, Menu var2) {
         return this.mWrappedCallback.onCreateActionMode(this.getActionModeWrapper(var1), this.getMenuWrapper(var2));
      }

      public void onDestroyActionMode(ActionMode var1) {
         this.mWrappedCallback.onDestroyActionMode(this.getActionModeWrapper(var1));
      }

      public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
         return this.mWrappedCallback.onPrepareActionMode(this.getActionModeWrapper(var1), this.getMenuWrapper(var2));
      }
   }
}
