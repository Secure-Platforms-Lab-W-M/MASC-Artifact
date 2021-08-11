package org.apache.commons.text.lookup;

import java.net.InetAddress;
import java.net.UnknownHostException;

final class LocalHostStringLookup extends AbstractStringLookup {
   static final LocalHostStringLookup INSTANCE = new LocalHostStringLookup();

   private LocalHostStringLookup() {
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         byte var2 = -1;

         boolean var10001;
         int var3;
         try {
            var3 = var1.hashCode();
         } catch (UnknownHostException var11) {
            var10001 = false;
            return null;
         }

         if (var3 != -1147692044) {
            if (var3 != 3373707) {
               if (var3 == 1339224004) {
                  label69: {
                     try {
                        if (!var1.equals("canonical-name")) {
                           break label69;
                        }
                     } catch (UnknownHostException var10) {
                        var10001 = false;
                        return null;
                     }

                     var2 = 1;
                  }
               }
            } else {
               label65: {
                  try {
                     if (!var1.equals("name")) {
                        break label65;
                     }
                  } catch (UnknownHostException var9) {
                     var10001 = false;
                     return null;
                  }

                  var2 = 0;
               }
            }
         } else {
            label61: {
               try {
                  if (!var1.equals("address")) {
                     break label61;
                  }
               } catch (UnknownHostException var8) {
                  var10001 = false;
                  return null;
               }

               var2 = 2;
            }
         }

         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 == 2) {
                  try {
                     return InetAddress.getLocalHost().getHostAddress();
                  } catch (UnknownHostException var4) {
                     var10001 = false;
                  }
               } else {
                  try {
                     throw new IllegalArgumentException(var1);
                  } catch (UnknownHostException var5) {
                     var10001 = false;
                  }
               }
            } else {
               try {
                  return InetAddress.getLocalHost().getCanonicalHostName();
               } catch (UnknownHostException var6) {
                  var10001 = false;
               }
            }
         } else {
            try {
               var1 = InetAddress.getLocalHost().getHostName();
               return var1;
            } catch (UnknownHostException var7) {
               var10001 = false;
            }
         }

         return null;
      }
   }
}
