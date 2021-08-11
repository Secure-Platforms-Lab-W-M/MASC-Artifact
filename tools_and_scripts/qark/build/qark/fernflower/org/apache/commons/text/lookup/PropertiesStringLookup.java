package org.apache.commons.text.lookup;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

final class PropertiesStringLookup extends AbstractStringLookup {
   static final PropertiesStringLookup INSTANCE = new PropertiesStringLookup();

   private PropertiesStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var2 = var1.split("::");
         if (var2.length >= 2) {
            String var38 = var2[0];
            var1 = this.substringAfter(var1, "::");

            Exception var10000;
            label251: {
               InputStream var3;
               Properties var4;
               boolean var10001;
               try {
                  var4 = new Properties();
                  var3 = Files.newInputStream(Paths.get(var38));
               } catch (Exception var37) {
                  var10000 = var37;
                  var10001 = false;
                  break label251;
               }

               try {
                  var4.load(var3);
               } catch (Throwable var36) {
                  Throwable var42 = var36;

                  try {
                     throw var42;
                  } finally {
                     if (var3 != null) {
                        try {
                           var3.close();
                        } catch (Throwable var32) {
                           Throwable var39 = var32;

                           label223:
                           try {
                              var42.addSuppressed(var39);
                              break label223;
                           } catch (Exception var31) {
                              var10000 = var31;
                              var10001 = false;
                              break label251;
                           }
                        }
                     }

                     try {
                        ;
                     } catch (Exception var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label251;
                     }
                  }
               }

               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Exception var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label251;
                  }
               }

               try {
                  String var41 = var4.getProperty(var1);
                  return var41;
               } catch (Exception var34) {
                  var10000 = var34;
                  var10001 = false;
               }
            }

            Exception var40 = var10000;
            throw IllegalArgumentExceptions.format(var40, "Error looking up properties [%s] and key [%s].", var38, var1);
         } else {
            throw IllegalArgumentExceptions.format("Bad properties key format [%s]; expected format is DocumentPath::Key.", var1);
         }
      }
   }
}
