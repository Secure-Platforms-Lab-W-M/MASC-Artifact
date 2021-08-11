package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;

abstract class BaseFragmentActivityDonut extends Activity {
   abstract View dispatchFragmentsOnCreateView(View var1, String var2, Context var3, AttributeSet var4);

   protected void onCreate(Bundle var1) {
      if (VERSION.SDK_INT < 11 && this.getLayoutInflater().getFactory() == null) {
         this.getLayoutInflater().setFactory(this);
      }

      super.onCreate(var1);
   }

   public View onCreateView(String var1, Context var2, AttributeSet var3) {
      View var4 = this.dispatchFragmentsOnCreateView((View)null, var1, var2, var3);
      return var4 == null ? super.onCreateView(var1, var2, var3) : var4;
   }
}
