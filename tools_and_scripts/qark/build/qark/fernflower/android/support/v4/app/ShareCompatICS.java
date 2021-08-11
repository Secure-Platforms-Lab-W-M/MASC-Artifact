package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.RequiresApi;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

@TargetApi(14)
@RequiresApi(14)
class ShareCompatICS {
   private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";

   public static void configureMenuItem(MenuItem var0, Activity var1, Intent var2) {
      ActionProvider var3 = var0.getActionProvider();
      ShareActionProvider var4;
      if (!(var3 instanceof ShareActionProvider)) {
         var4 = new ShareActionProvider(var1);
      } else {
         var4 = (ShareActionProvider)var3;
      }

      var4.setShareHistoryFileName(".sharecompat_" + var1.getClass().getName());
      var4.setShareIntent(var2);
      var0.setActionProvider(var4);
   }
}
