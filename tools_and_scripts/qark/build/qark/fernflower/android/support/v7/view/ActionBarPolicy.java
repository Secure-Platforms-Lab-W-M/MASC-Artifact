package android.support.v7.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$bool;
import android.support.v7.appcompat.R$dimen;
import android.support.v7.appcompat.R$styleable;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class ActionBarPolicy {
   private Context mContext;

   private ActionBarPolicy(Context var1) {
      this.mContext = var1;
   }

   public static ActionBarPolicy get(Context var0) {
      return new ActionBarPolicy(var0);
   }

   public boolean enableHomeButtonByDefault() {
      return this.mContext.getApplicationInfo().targetSdkVersion < 14;
   }

   public int getEmbeddedMenuWidthLimit() {
      return this.mContext.getResources().getDisplayMetrics().widthPixels / 2;
   }

   public int getMaxActionButtons() {
      Configuration var3 = this.mContext.getResources().getConfiguration();
      int var1 = var3.screenWidthDp;
      int var2 = var3.screenHeightDp;
      if (var3.smallestScreenWidthDp <= 600 && var1 <= 600 && (var1 <= 960 || var2 <= 720) && (var1 <= 720 || var2 <= 960)) {
         if (var1 < 500 && (var1 <= 640 || var2 <= 480) && (var1 <= 480 || var2 <= 640)) {
            return var1 >= 360 ? 3 : 2;
         } else {
            return 4;
         }
      } else {
         return 5;
      }
   }

   public int getStackedTabMaxWidth() {
      return this.mContext.getResources().getDimensionPixelSize(R$dimen.abc_action_bar_stacked_tab_max_width);
   }

   public int getTabContainerHeight() {
      TypedArray var3 = this.mContext.obtainStyledAttributes((AttributeSet)null, R$styleable.ActionBar, R$attr.actionBarStyle, 0);
      int var2 = var3.getLayoutDimension(R$styleable.ActionBar_height, 0);
      Resources var4 = this.mContext.getResources();
      int var1 = var2;
      if (!this.hasEmbeddedTabs()) {
         var1 = Math.min(var2, var4.getDimensionPixelSize(R$dimen.abc_action_bar_stacked_max_height));
      }

      var3.recycle();
      return var1;
   }

   public boolean hasEmbeddedTabs() {
      return this.mContext.getResources().getBoolean(R$bool.abc_action_bar_embed_tabs);
   }

   public boolean showsOverflowMenuButton() {
      return VERSION.SDK_INT >= 19 ? true : ViewConfiguration.get(this.mContext).hasPermanentMenuKey() ^ true;
   }
}
