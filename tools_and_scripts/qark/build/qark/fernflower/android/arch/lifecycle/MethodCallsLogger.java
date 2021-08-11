package android.arch.lifecycle;

import android.support.annotation.RestrictTo;
import java.util.HashMap;
import java.util.Map;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MethodCallsLogger {
   private Map mCalledMethods = new HashMap();

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public boolean approveCall(String var1, int var2) {
      Integer var5 = (Integer)this.mCalledMethods.get(var1);
      int var3;
      if (var5 != null) {
         var3 = var5;
      } else {
         var3 = 0;
      }

      boolean var4;
      if ((var3 & var2) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.mCalledMethods.put(var1, var3 | var2);
      return !var4;
   }
}
