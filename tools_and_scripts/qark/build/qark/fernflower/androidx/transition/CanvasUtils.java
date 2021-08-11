package androidx.transition;

import android.graphics.Canvas;
import android.os.Build.VERSION;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class CanvasUtils {
   private static Method sInorderBarrierMethod;
   private static boolean sOrderMethodsFetched;
   private static Method sReorderBarrierMethod;

   private CanvasUtils() {
   }

   static void enableZ(Canvas var0, boolean var1) {
      if (VERSION.SDK_INT >= 21) {
         if (VERSION.SDK_INT >= 29) {
            if (var1) {
               var0.enableZ();
            } else {
               var0.disableZ();
            }
         } else if (VERSION.SDK_INT != 28) {
            if (!sOrderMethodsFetched) {
               try {
                  Method var2 = Canvas.class.getDeclaredMethod("insertReorderBarrier");
                  sReorderBarrierMethod = var2;
                  var2.setAccessible(true);
                  var2 = Canvas.class.getDeclaredMethod("insertInorderBarrier");
                  sInorderBarrierMethod = var2;
                  var2.setAccessible(true);
               } catch (NoSuchMethodException var3) {
               }

               sOrderMethodsFetched = true;
            }

            InvocationTargetException var10000;
            label68: {
               boolean var10001;
               if (var1) {
                  try {
                     if (sReorderBarrierMethod != null) {
                        sReorderBarrierMethod.invoke(var0);
                     }
                  } catch (IllegalAccessException var6) {
                     var10001 = false;
                     return;
                  } catch (InvocationTargetException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label68;
                  }
               }

               if (var1) {
                  return;
               }

               try {
                  if (sInorderBarrierMethod != null) {
                     sInorderBarrierMethod.invoke(var0);
                  }

                  return;
               } catch (IllegalAccessException var4) {
                  var10001 = false;
                  return;
               } catch (InvocationTargetException var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            InvocationTargetException var8 = var10000;
            throw new RuntimeException(var8.getCause());
         } else {
            throw new IllegalStateException("This method doesn't work on Pie!");
         }
      }
   }
}
