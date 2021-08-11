package org.apache.commons.text.lookup;

import java.util.Objects;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

final class ScriptStringLookup extends AbstractStringLookup {
   static final ScriptStringLookup INSTANCE = new ScriptStringLookup();

   private ScriptStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = var1.split(SPLIT_STR);
         if (var2.length == 2) {
            var1 = var2[0];
            String var7 = var2[1];

            Exception var10000;
            label30: {
               boolean var10001;
               ScriptEngine var3;
               try {
                  var3 = (new ScriptEngineManager()).getEngineByName(var1);
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label30;
               }

               if (var3 != null) {
                  try {
                     return Objects.toString(var3.eval(var7), (String)null);
                  } catch (Exception var4) {
                     var10000 = var4;
                     var10001 = false;
                  }
               } else {
                  try {
                     StringBuilder var9 = new StringBuilder();
                     var9.append("No script engine named ");
                     var9.append(var1);
                     throw new IllegalArgumentException(var9.toString());
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               }
            }

            Exception var8 = var10000;
            throw IllegalArgumentExceptions.format(var8, "Error in script engine [%s] evaluating script [%s].", var1, var7);
         } else {
            throw IllegalArgumentExceptions.format("Bad script key format [%s]; expected format is DocumentPath:Key.", var1);
         }
      }
   }
}
