package org.apache.commons.text.lookup;

import java.net.InetAddress;
import java.net.UnknownHostException;

final class DnsStringLookup extends AbstractStringLookup {
   static final DnsStringLookup INSTANCE = new DnsStringLookup();

   private DnsStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var5 = var1.trim().split("\\|");
         int var3 = var5.length;
         byte var2 = 0;
         String var4 = var5[0].trim();
         if (var3 >= 2) {
            var1 = var5[1].trim();
         }

         boolean var10001;
         InetAddress var14;
         try {
            var14 = InetAddress.getByName(var1);
            var3 = var4.hashCode();
         } catch (UnknownHostException var13) {
            var10001 = false;
            return null;
         }

         label78: {
            label92: {
               if (var3 != -1147692044) {
                  if (var3 != 3373707) {
                     if (var3 == 1339224004) {
                        label91: {
                           try {
                              if (!var4.equals("canonical-name")) {
                                 break label91;
                              }
                           } catch (UnknownHostException var12) {
                              var10001 = false;
                              return null;
                           }

                           var2 = 1;
                           break label78;
                        }
                     }
                  } else {
                     try {
                        if (var4.equals("name")) {
                           break label78;
                        }
                     } catch (UnknownHostException var10) {
                        var10001 = false;
                        return null;
                     }
                  }
               } else {
                  try {
                     if (var4.equals("address")) {
                        break label92;
                     }
                  } catch (UnknownHostException var11) {
                     var10001 = false;
                     return null;
                  }
               }

               var2 = -1;
               break label78;
            }

            var2 = 2;
         }

         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  try {
                     return var14.getHostAddress();
                  } catch (UnknownHostException var6) {
                     var10001 = false;
                  }
               } else {
                  try {
                     return var14.getHostAddress();
                  } catch (UnknownHostException var7) {
                     var10001 = false;
                  }
               }
            } else {
               try {
                  return var14.getCanonicalHostName();
               } catch (UnknownHostException var8) {
                  var10001 = false;
               }
            }
         } else {
            try {
               var1 = var14.getHostName();
               return var1;
            } catch (UnknownHostException var9) {
               var10001 = false;
            }
         }

         return null;
      }
   }
}
