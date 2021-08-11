package android.support.v4.app;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;

abstract class BaseFragmentActivityHoneycomb extends BaseFragmentActivityGingerbread {
   public View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      View var6 = this.dispatchFragmentsOnCreateView(var1, var2, var3, var4);
      View var5 = var6;
      if (var6 == null) {
         var5 = var6;
         if (VERSION.SDK_INT >= 11) {
            var5 = super.onCreateView(var1, var2, var3, var4);
         }
      }

      return var5;
   }
}
