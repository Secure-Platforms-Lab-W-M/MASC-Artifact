package androidx.databinding.adapters;

import android.os.Build.VERSION;
import android.util.SparseArray;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class ListenerUtil {
   private static final SparseArray sListeners = new SparseArray();

   public static Object getListener(View var0, int var1) {
      if (VERSION.SDK_INT >= 14) {
         return var0.getTag(var1);
      } else {
         SparseArray var2 = sListeners;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label334: {
            WeakHashMap var3;
            try {
               var3 = (WeakHashMap)sListeners.get(var1);
            } catch (Throwable var45) {
               var10000 = var45;
               var10001 = false;
               break label334;
            }

            if (var3 == null) {
               label322:
               try {
                  return null;
               } catch (Throwable var41) {
                  var10000 = var41;
                  var10001 = false;
                  break label322;
               }
            } else {
               label330: {
                  WeakReference var46;
                  try {
                     var46 = (WeakReference)var3.get(var0);
                  } catch (Throwable var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label330;
                  }

                  if (var46 == null) {
                     label324:
                     try {
                        return null;
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label324;
                     }
                  } else {
                     label326:
                     try {
                        Object var48 = var46.get();
                        return var48;
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label326;
                     }
                  }
               }
            }
         }

         while(true) {
            Throwable var47 = var10000;

            try {
               throw var47;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public static Object trackListener(View var0, Object var1, int var2) {
      if (VERSION.SDK_INT >= 14) {
         Object var65 = var0.getTag(var2);
         var0.setTag(var2, var1);
         return var65;
      } else {
         SparseArray var5 = sListeners;
         synchronized(var5){}

         Throwable var10000;
         boolean var10001;
         label443: {
            WeakHashMap var4;
            try {
               var4 = (WeakHashMap)sListeners.get(var2);
            } catch (Throwable var61) {
               var10000 = var61;
               var10001 = false;
               break label443;
            }

            WeakHashMap var3 = var4;
            if (var4 == null) {
               try {
                  var3 = new WeakHashMap();
                  sListeners.put(var2, var3);
               } catch (Throwable var60) {
                  var10000 = var60;
                  var10001 = false;
                  break label443;
               }
            }

            WeakReference var62;
            if (var1 == null) {
               try {
                  var62 = (WeakReference)var3.remove(var0);
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break label443;
               }
            } else {
               try {
                  var62 = (WeakReference)var3.put(var0, new WeakReference(var1));
               } catch (Throwable var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label443;
               }
            }

            if (var62 == null) {
               label422:
               try {
                  return null;
               } catch (Throwable var56) {
                  var10000 = var56;
                  var10001 = false;
                  break label422;
               }
            } else {
               label424:
               try {
                  Object var64 = var62.get();
                  return var64;
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label424;
               }
            }
         }

         while(true) {
            Throwable var63 = var10000;

            try {
               throw var63;
            } catch (Throwable var55) {
               var10000 = var55;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
