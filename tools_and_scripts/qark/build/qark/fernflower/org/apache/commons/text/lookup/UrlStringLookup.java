package org.apache.commons.text.lookup;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

final class UrlStringLookup extends AbstractStringLookup {
   static final UrlStringLookup INSTANCE = new UrlStringLookup();

   private UrlStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var3 = var1.split(SPLIT_STR);
         if (var3.length >= 2) {
            String var47 = var3[0];
            var1 = this.substringAfter(var1, ':');

            Exception var10000;
            label343: {
               StringWriter var5;
               char[] var6;
               InputStreamReader var48;
               boolean var10001;
               try {
                  URL var4 = new URL(var1);
                  var5 = new StringWriter(8192);
                  var6 = new char[8192];
                  var48 = new InputStreamReader(new BufferedInputStream(var4.openStream()), var47);
               } catch (Exception var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label343;
               }

               Throwable var53;
               while(true) {
                  int var2;
                  try {
                     var2 = var48.read(var6);
                  } catch (Throwable var46) {
                     var53 = var46;
                     var10001 = false;
                     break;
                  }

                  if (-1 == var2) {
                     try {
                        var48.close();
                        String var52 = var5.toString();
                        return var52;
                     } catch (Exception var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label343;
                     }
                  }

                  try {
                     var5.write(var6, 0, var2);
                  } catch (Throwable var45) {
                     var53 = var45;
                     var10001 = false;
                     break;
                  }
               }

               Throwable var51 = var53;

               try {
                  throw var51;
               } finally {
                  label345: {
                     try {
                        var48.close();
                     } catch (Throwable var41) {
                        Throwable var49 = var41;

                        label312:
                        try {
                           var51.addSuppressed(var49);
                           break label312;
                        } catch (Exception var40) {
                           var10000 = var40;
                           var10001 = false;
                           break label345;
                        }
                     }

                     label310:
                     try {
                        ;
                     } catch (Exception var39) {
                        var10000 = var39;
                        var10001 = false;
                        break label310;
                     }
                  }
               }
            }

            Exception var50 = var10000;
            throw IllegalArgumentExceptions.format(var50, "Error looking up URL [%s] with Charset [%s].", var1, var47);
         } else {
            throw IllegalArgumentExceptions.format("Bad URL key format [%s]; expected format is DocumentPath:Key.", var1);
         }
      }
   }
}
