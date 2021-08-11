package android.support.v4.app;

import android.os.Bundle;
import java.util.Set;

@Deprecated
class RemoteInputCompatBase {
   public abstract static class RemoteInput {
      protected abstract boolean getAllowFreeFormInput();

      protected abstract Set getAllowedDataTypes();

      protected abstract CharSequence[] getChoices();

      protected abstract Bundle getExtras();

      protected abstract CharSequence getLabel();

      protected abstract String getResultKey();

      public interface Factory {
         RemoteInputCompatBase.RemoteInput build(String var1, CharSequence var2, CharSequence[] var3, boolean var4, Bundle var5, Set var6);

         RemoteInputCompatBase.RemoteInput[] newArray(int var1);
      }
   }
}
