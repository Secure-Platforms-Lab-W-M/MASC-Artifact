package android.support.v7.view.menu;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenuItem;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;
import android.view.ActionProvider.VisibilityListener;

@RequiresApi(16)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class MenuItemWrapperJB extends MenuItemWrapperICS {
   MenuItemWrapperJB(Context var1, SupportMenuItem var2) {
      super(var1, var2);
   }

   MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(ActionProvider var1) {
      return new MenuItemWrapperJB.ActionProviderWrapperJB(this.mContext, var1);
   }

   class ActionProviderWrapperJB extends MenuItemWrapperICS.ActionProviderWrapper implements VisibilityListener {
      android.support.v4.view.ActionProvider.VisibilityListener mListener;

      public ActionProviderWrapperJB(Context var2, ActionProvider var3) {
         super(var2, var3);
      }

      public boolean isVisible() {
         return this.mInner.isVisible();
      }

      public void onActionProviderVisibilityChanged(boolean var1) {
         android.support.v4.view.ActionProvider.VisibilityListener var2 = this.mListener;
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

      public void setVisibilityListener(android.support.v4.view.ActionProvider.VisibilityListener var1) {
         this.mListener = var1;
         ActionProvider var2 = this.mInner;
         MenuItemWrapperJB.ActionProviderWrapperJB var3;
         if (var1 != null) {
            var3 = this;
         } else {
            var3 = null;
         }

         var2.setVisibilityListener(var3);
      }
   }
}
