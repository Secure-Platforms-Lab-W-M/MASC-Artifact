package androidx.fragment.app;

import java.util.Collection;
import java.util.Map;

@Deprecated
public class FragmentManagerNonConfig {
   private final Map mChildNonConfigs;
   private final Collection mFragments;
   private final Map mViewModelStores;

   FragmentManagerNonConfig(Collection var1, Map var2, Map var3) {
      this.mFragments = var1;
      this.mChildNonConfigs = var2;
      this.mViewModelStores = var3;
   }

   Map getChildNonConfigs() {
      return this.mChildNonConfigs;
   }

   Collection getFragments() {
      return this.mFragments;
   }

   Map getViewModelStores() {
      return this.mViewModelStores;
   }

   boolean isRetaining(Fragment var1) {
      Collection var2 = this.mFragments;
      return var2 == null ? false : var2.contains(var1);
   }
}
