package android.support.v7.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$string;
import android.support.v7.content.res.AppCompatResources;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;

public class ShareActionProvider extends ActionProvider {
   private static final int DEFAULT_INITIAL_ACTIVITY_COUNT = 4;
   public static final String DEFAULT_SHARE_HISTORY_FILE_NAME = "share_history.xml";
   final Context mContext;
   private int mMaxShownActivityCount = 4;
   private ActivityChooserModel.OnChooseActivityListener mOnChooseActivityListener;
   private final ShareActionProvider.ShareMenuItemOnMenuItemClickListener mOnMenuItemClickListener = new ShareActionProvider.ShareMenuItemOnMenuItemClickListener();
   ShareActionProvider.OnShareTargetSelectedListener mOnShareTargetSelectedListener;
   String mShareHistoryFileName = "share_history.xml";

   public ShareActionProvider(Context var1) {
      super(var1);
      this.mContext = var1;
   }

   private void setActivityChooserPolicyIfNeeded() {
      if (this.mOnShareTargetSelectedListener != null) {
         if (this.mOnChooseActivityListener == null) {
            this.mOnChooseActivityListener = new ShareActionProvider.ShareActivityChooserModelPolicy();
         }

         ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName).setOnChooseActivityListener(this.mOnChooseActivityListener);
      }
   }

   public boolean hasSubMenu() {
      return true;
   }

   public View onCreateActionView() {
      ActivityChooserView var1 = new ActivityChooserView(this.mContext);
      if (!var1.isInEditMode()) {
         var1.setActivityChooserModel(ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName));
      }

      TypedValue var2 = new TypedValue();
      this.mContext.getTheme().resolveAttribute(R$attr.actionModeShareDrawable, var2, true);
      var1.setExpandActivityOverflowButtonDrawable(AppCompatResources.getDrawable(this.mContext, var2.resourceId));
      var1.setProvider(this);
      var1.setDefaultActionButtonContentDescription(R$string.abc_shareactionprovider_share_with_application);
      var1.setExpandActivityOverflowButtonContentDescription(R$string.abc_shareactionprovider_share_with);
      return var1;
   }

   public void onPrepareSubMenu(SubMenu var1) {
      var1.clear();
      ActivityChooserModel var5 = ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName);
      PackageManager var6 = this.mContext.getPackageManager();
      int var3 = var5.getActivityCount();
      int var4 = Math.min(var3, this.mMaxShownActivityCount);

      int var2;
      ResolveInfo var7;
      for(var2 = 0; var2 < var4; ++var2) {
         var7 = var5.getActivity(var2);
         var1.add(0, var2, var2, var7.loadLabel(var6)).setIcon(var7.loadIcon(var6)).setOnMenuItemClickListener(this.mOnMenuItemClickListener);
      }

      if (var4 < var3) {
         var1 = var1.addSubMenu(0, var4, var4, this.mContext.getString(R$string.abc_activity_chooser_view_see_all));

         for(var2 = 0; var2 < var3; ++var2) {
            var7 = var5.getActivity(var2);
            var1.add(0, var2, var2, var7.loadLabel(var6)).setIcon(var7.loadIcon(var6)).setOnMenuItemClickListener(this.mOnMenuItemClickListener);
         }
      }

   }

   public void setOnShareTargetSelectedListener(ShareActionProvider.OnShareTargetSelectedListener var1) {
      this.mOnShareTargetSelectedListener = var1;
      this.setActivityChooserPolicyIfNeeded();
   }

   public void setShareHistoryFileName(String var1) {
      this.mShareHistoryFileName = var1;
      this.setActivityChooserPolicyIfNeeded();
   }

   public void setShareIntent(Intent var1) {
      if (var1 != null) {
         String var2 = var1.getAction();
         if ("android.intent.action.SEND".equals(var2) || "android.intent.action.SEND_MULTIPLE".equals(var2)) {
            this.updateIntent(var1);
         }
      }

      ActivityChooserModel.get(this.mContext, this.mShareHistoryFileName).setIntent(var1);
   }

   void updateIntent(Intent var1) {
      if (VERSION.SDK_INT >= 21) {
         var1.addFlags(134742016);
      } else {
         var1.addFlags(524288);
      }
   }

   public interface OnShareTargetSelectedListener {
      boolean onShareTargetSelected(ShareActionProvider var1, Intent var2);
   }

   private class ShareActivityChooserModelPolicy implements ActivityChooserModel.OnChooseActivityListener {
      ShareActivityChooserModelPolicy() {
      }

      public boolean onChooseActivity(ActivityChooserModel var1, Intent var2) {
         if (ShareActionProvider.this.mOnShareTargetSelectedListener != null) {
            ShareActionProvider.this.mOnShareTargetSelectedListener.onShareTargetSelected(ShareActionProvider.this, var2);
         }

         return false;
      }
   }

   private class ShareMenuItemOnMenuItemClickListener implements OnMenuItemClickListener {
      ShareMenuItemOnMenuItemClickListener() {
      }

      public boolean onMenuItemClick(MenuItem var1) {
         Intent var3 = ActivityChooserModel.get(ShareActionProvider.this.mContext, ShareActionProvider.this.mShareHistoryFileName).chooseActivity(var1.getItemId());
         if (var3 != null) {
            String var2 = var3.getAction();
            if ("android.intent.action.SEND".equals(var2) || "android.intent.action.SEND_MULTIPLE".equals(var2)) {
               ShareActionProvider.this.updateIntent(var3);
            }

            ShareActionProvider.this.mContext.startActivity(var3);
         }

         return true;
      }
   }
}
