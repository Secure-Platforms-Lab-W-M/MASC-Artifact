package org.apache.commons.text.lookup;

import java.nio.file.Files;
import java.nio.file.Paths;

final class FileStringLookup extends AbstractStringLookup {
   static final AbstractStringLookup INSTANCE = new FileStringLookup();

   private FileStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = var1.split(String.valueOf(':'));
         if (var2.length >= 2) {
            String var5 = var2[0];
            var1 = this.substringAfter(var1, ':');

            try {
               String var3 = new String(Files.readAllBytes(Paths.get(var1)), var5);
               return var3;
            } catch (Exception var4) {
               throw IllegalArgumentExceptions.format(var4, "Error looking up file [%s] with charset [%s].", var1, var5);
            }
         } else {
            throw IllegalArgumentExceptions.format("Bad file key format [%s], expected format is CharsetName:DocumentPath.", var1);
         }
      }
   }
}
