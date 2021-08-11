package android.support.v7.app;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

@RequiresApi(14)
class AppCompatDelegateImplV11 extends AppCompatDelegateImplV9 {
   AppCompatDelegateImplV11(Context var1, Window var2, AppCompatCallback var3) {
      super(var1, var2, var3);
   }

   View callActivityOnCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return null;
   }

   public boolean hasWindowFeature(int var1) {
      return super.hasWindowFeature(var1) || this.mWindow.hasFeature(var1);
   }
}
