package android.support.v4.media;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class ParceledListSliceAdapterApi21 {
   private static Constructor sConstructor;

   static {
      Object var0;
      try {
         sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class);
         return;
      } catch (ClassNotFoundException var1) {
         var0 = var1;
      } catch (NoSuchMethodException var2) {
         var0 = var2;
      }

      ((ReflectiveOperationException)var0).printStackTrace();
   }

   private ParceledListSliceAdapterApi21() {
   }

   static Object newInstance(List var0) {
      Object var4;
      try {
         var4 = sConstructor.newInstance(var0);
         return var4;
      } catch (InstantiationException var1) {
         var4 = var1;
      } catch (IllegalAccessException var2) {
         var4 = var2;
      } catch (InvocationTargetException var3) {
         var4 = var3;
      }

      ((ReflectiveOperationException)var4).printStackTrace();
      return null;
   }
}
