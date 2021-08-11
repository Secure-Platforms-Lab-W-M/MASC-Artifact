package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class FragmentContainer {
   public Fragment instantiate(Context var1, String var2, Bundle var3) {
      return Fragment.instantiate(var1, var2, var3);
   }

   @Nullable
   public abstract View onFindViewById(@IdRes int var1);

   public abstract boolean onHasView();
}
