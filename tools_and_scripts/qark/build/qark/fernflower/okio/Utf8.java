package okio;

public final class Utf8 {
   private Utf8() {
   }

   public static long size(String var0) {
      return size(var0, 0, var0.length());
   }

   public static long size(String var0, int var1, int var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("string == null");
      } else {
         StringBuilder var8;
         if (var1 < 0) {
            var8 = new StringBuilder();
            var8.append("beginIndex < 0: ");
            var8.append(var1);
            throw new IllegalArgumentException(var8.toString());
         } else if (var2 < var1) {
            var8 = new StringBuilder();
            var8.append("endIndex < beginIndex: ");
            var8.append(var2);
            var8.append(" < ");
            var8.append(var1);
            throw new IllegalArgumentException(var8.toString());
         } else if (var2 > var0.length()) {
            StringBuilder var7 = new StringBuilder();
            var7.append("endIndex > string.length: ");
            var7.append(var2);
            var7.append(" > ");
            var7.append(var0.length());
            throw new IllegalArgumentException(var7.toString());
         } else {
            long var5 = 0L;

            while(true) {
               while(var1 < var2) {
                  char var4 = var0.charAt(var1);
                  if (var4 < 128) {
                     ++var5;
                     ++var1;
                  } else if (var4 < 2048) {
                     var5 += 2L;
                     ++var1;
                  } else if (var4 >= '\ud800' && var4 <= '\udfff') {
                     char var3;
                     if (var1 + 1 < var2) {
                        var3 = var0.charAt(var1 + 1);
                     } else {
                        var3 = 0;
                     }

                     if (var4 <= '\udbff' && var3 >= '\udc00' && var3 <= '\udfff') {
                        var5 += 4L;
                        var1 += 2;
                     } else {
                        ++var5;
                        ++var1;
                     }
                  } else {
                     var5 += 3L;
                     ++var1;
                  }
               }

               return var5;
            }
         }
      }
   }
}
