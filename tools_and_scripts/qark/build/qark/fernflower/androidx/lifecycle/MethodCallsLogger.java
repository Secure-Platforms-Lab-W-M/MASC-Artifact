package androidx.lifecycle;

import java.util.HashMap;
import java.util.Map;

public class MethodCallsLogger {
   private Map mCalledMethods = new HashMap();

   public boolean approveCall(String var1, int var2) {
      Integer var6 = (Integer)this.mCalledMethods.get(var1);
      boolean var5 = false;
      int var3;
      if (var6 != null) {
         var3 = var6;
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
      if (!var4) {
         var5 = true;
      }

      return var5;
   }
}
