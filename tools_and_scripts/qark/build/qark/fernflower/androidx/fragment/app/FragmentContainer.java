package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class FragmentContainer {
   @Deprecated
   public Fragment instantiate(Context var1, String var2, Bundle var3) {
      return Fragment.instantiate(var1, var2, var3);
   }

   public abstract View onFindViewById(int var1);

   public abstract boolean onHasView();
}
