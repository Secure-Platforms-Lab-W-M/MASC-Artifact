package androidx.core.view;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public abstract class ActionProvider {
   private static final String TAG = "ActionProvider(support)";
   private final Context mContext;
   private ActionProvider.SubUiVisibilityListener mSubUiVisibilityListener;
   private ActionProvider.VisibilityListener mVisibilityListener;

   public ActionProvider(Context var1) {
      this.mContext = var1;
   }

   public Context getContext() {
      return this.mContext;
   }

   public boolean hasSubMenu() {
      return false;
   }

   public boolean isVisible() {
      return true;
   }

   public abstract View onCreateActionView();

   public View onCreateActionView(MenuItem var1) {
      return this.onCreateActionView();
   }

   public boolean onPerformDefaultAction() {
      return false;
   }

   public void onPrepareSubMenu(SubMenu var1) {
   }

   public boolean overridesItemVisibility() {
      return false;
   }

   public void refreshVisibility() {
      if (this.mVisibilityListener != null && this.overridesItemVisibility()) {
         this.mVisibilityListener.onActionProviderVisibilityChanged(this.isVisible());
      }

   }

   public void reset() {
      this.mVisibilityListener = null;
      this.mSubUiVisibilityListener = null;
   }

   public void setSubUiVisibilityListener(ActionProvider.SubUiVisibilityListener var1) {
      this.mSubUiVisibilityListener = var1;
   }

   public void setVisibilityListener(ActionProvider.VisibilityListener var1) {
      if (this.mVisibilityListener != null && var1 != null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this ");
         var2.append(this.getClass().getSimpleName());
         var2.append(" instance while it is still in use somewhere else?");
         Log.w("ActionProvider(support)", var2.toString());
      }

      this.mVisibilityListener = var1;
   }

   public void subUiVisibilityChanged(boolean var1) {
      ActionProvider.SubUiVisibilityListener var2 = this.mSubUiVisibilityListener;
      if (var2 != null) {
         var2.onSubUiVisibilityChanged(var1);
      }

   }

   public interface SubUiVisibilityListener {
      void onSubUiVisibilityChanged(boolean var1);
   }

   public interface VisibilityListener {
      void onActionProviderVisibilityChanged(boolean var1);
   }
}
