package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import java.util.Arrays;

class BundleUtil {
   public static Bundle[] getBundleArrayFromBundle(Bundle var0, String var1) {
      Parcelable[] var2 = var0.getParcelableArray(var1);
      if (!(var2 instanceof Bundle[]) && var2 != null) {
         Bundle[] var3 = (Bundle[])Arrays.copyOf(var2, var2.length, Bundle[].class);
         var0.putParcelableArray(var1, var3);
         return var3;
      } else {
         return (Bundle[])((Bundle[])var2);
      }
   }
}
