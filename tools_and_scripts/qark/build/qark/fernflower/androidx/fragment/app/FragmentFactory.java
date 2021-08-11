package androidx.fragment.app;

import androidx.collection.SimpleArrayMap;
import java.lang.reflect.InvocationTargetException;

public class FragmentFactory {
   private static final SimpleArrayMap sClassMap = new SimpleArrayMap();

   static boolean isFragmentClass(ClassLoader var0, String var1) {
      try {
         boolean var2 = Fragment.class.isAssignableFrom(loadClass(var0, var1));
         return var2;
      } catch (ClassNotFoundException var3) {
         return false;
      }
   }

   private static Class loadClass(ClassLoader var0, String var1) throws ClassNotFoundException {
      Class var3 = (Class)sClassMap.get(var1);
      Class var2 = var3;
      if (var3 == null) {
         var2 = Class.forName(var1, false, var0);
         sClassMap.put(var1, var2);
      }

      return var2;
   }

   public static Class loadFragmentClass(ClassLoader var0, String var1) {
      StringBuilder var2;
      try {
         Class var5 = loadClass(var0, var1);
         return var5;
      } catch (ClassNotFoundException var3) {
         var2 = new StringBuilder();
         var2.append("Unable to instantiate fragment ");
         var2.append(var1);
         var2.append(": make sure class name exists");
         throw new Fragment.InstantiationException(var2.toString(), var3);
      } catch (ClassCastException var4) {
         var2 = new StringBuilder();
         var2.append("Unable to instantiate fragment ");
         var2.append(var1);
         var2.append(": make sure class is a valid subclass of Fragment");
         throw new Fragment.InstantiationException(var2.toString(), var4);
      }
   }

   public Fragment instantiate(ClassLoader var1, String var2) {
      StringBuilder var3;
      try {
         Fragment var8 = (Fragment)loadFragmentClass(var1, var2).getConstructor().newInstance();
         return var8;
      } catch (InstantiationException var4) {
         var3 = new StringBuilder();
         var3.append("Unable to instantiate fragment ");
         var3.append(var2);
         var3.append(": make sure class name exists, is public, and has an empty constructor that is public");
         throw new Fragment.InstantiationException(var3.toString(), var4);
      } catch (IllegalAccessException var5) {
         var3 = new StringBuilder();
         var3.append("Unable to instantiate fragment ");
         var3.append(var2);
         var3.append(": make sure class name exists, is public, and has an empty constructor that is public");
         throw new Fragment.InstantiationException(var3.toString(), var5);
      } catch (NoSuchMethodException var6) {
         var3 = new StringBuilder();
         var3.append("Unable to instantiate fragment ");
         var3.append(var2);
         var3.append(": could not find Fragment constructor");
         throw new Fragment.InstantiationException(var3.toString(), var6);
      } catch (InvocationTargetException var7) {
         var3 = new StringBuilder();
         var3.append("Unable to instantiate fragment ");
         var3.append(var2);
         var3.append(": calling Fragment constructor caused an exception");
         throw new Fragment.InstantiationException(var3.toString(), var7);
      }
   }
}
