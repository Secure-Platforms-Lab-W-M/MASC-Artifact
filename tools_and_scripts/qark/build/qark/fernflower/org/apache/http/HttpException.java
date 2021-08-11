package org.apache.http;

public class HttpException extends Exception {
   private static final int FIRST_VALID_CHAR = 32;
   private static final long serialVersionUID = -5437299376222011036L;

   public HttpException() {
   }

   public HttpException(String var1) {
      super(clean(var1));
   }

   public HttpException(String var1, Throwable var2) {
      super(clean(var1));
      this.initCause(var2);
   }

   static String clean(String var0) {
      char[] var3 = var0.toCharArray();

      int var2;
      for(var2 = 0; var2 < var3.length && var3[var2] >= ' '; ++var2) {
      }

      if (var2 == var3.length) {
         return var0;
      } else {
         StringBuilder var5 = new StringBuilder(var3.length * 2);

         for(var2 = 0; var2 < var3.length; ++var2) {
            char var1 = var3[var2];
            if (var1 < ' ') {
               var5.append("[0x");
               String var4 = Integer.toHexString(var2);
               if (var4.length() == 1) {
                  var5.append("0");
               }

               var5.append(var4);
               var5.append("]");
            } else {
               var5.append(var1);
            }
         }

         return var5.toString();
      }
   }
}
