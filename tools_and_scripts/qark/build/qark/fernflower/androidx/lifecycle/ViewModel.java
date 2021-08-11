package androidx.lifecycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class ViewModel {
   private final Map mBagOfTags = new HashMap();
   private volatile boolean mCleared = false;

   private static void closeWithRuntimeException(Object var0) {
      if (var0 instanceof Closeable) {
         try {
            ((Closeable)var0).close();
         } catch (IOException var1) {
            throw new RuntimeException(var1);
         }
      }
   }

   final void clear() {
      this.mCleared = true;
      Map var1 = this.mBagOfTags;
      if (var1 != null) {
         label232: {
            synchronized(var1){}

            Throwable var10000;
            boolean var10001;
            label231: {
               Iterator var2;
               try {
                  var2 = this.mBagOfTags.values().iterator();
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label231;
               }

               while(true) {
                  try {
                     if (!var2.hasNext()) {
                        break;
                     }

                     closeWithRuntimeException(var2.next());
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label231;
                  }
               }

               label214:
               try {
                  break label232;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label214;
               }
            }

            while(true) {
               Throwable var23 = var10000;

               try {
                  throw var23;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      this.onCleared();
   }

   Object getTag(String param1) {
      // $FF: Couldn't be decompiled
   }

   protected void onCleared() {
   }

   Object setTagIfAbsent(String var1, Object var2) {
      Map var4 = this.mBagOfTags;
      synchronized(var4){}

      Object var3;
      label234: {
         Throwable var10000;
         boolean var10001;
         label235: {
            try {
               var3 = this.mBagOfTags.get(var1);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label235;
            }

            if (var3 == null) {
               try {
                  this.mBagOfTags.put(var1, var2);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label235;
               }
            }

            label222:
            try {
               break label234;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label222;
            }
         }

         while(true) {
            Throwable var25 = var10000;

            try {
               throw var25;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               continue;
            }
         }
      }

      Object var26;
      if (var3 == null) {
         var26 = var2;
      } else {
         var26 = var3;
      }

      if (this.mCleared) {
         closeWithRuntimeException(var26);
      }

      return var26;
   }
}
