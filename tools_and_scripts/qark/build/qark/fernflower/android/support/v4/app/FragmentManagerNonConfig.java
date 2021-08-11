package android.support.v4.app;

import java.util.List;

public class FragmentManagerNonConfig {
   private final List mChildNonConfigs;
   private final List mFragments;

   FragmentManagerNonConfig(List var1, List var2) {
      this.mFragments = var1;
      this.mChildNonConfigs = var2;
   }

   List getChildNonConfigs() {
      return this.mChildNonConfigs;
   }

   List getFragments() {
      return this.mFragments;
   }
}
