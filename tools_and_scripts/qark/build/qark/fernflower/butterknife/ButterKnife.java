package butterknife;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ButterKnife {
   static final Map BINDINGS = new LinkedHashMap();
   private static final String TAG = "ButterKnife";
   private static boolean debug = false;

   private ButterKnife() {
      throw new AssertionError("No instances.");
   }

   public static Unbinder bind(Activity var0) {
      return bind(var0, (View)var0.getWindow().getDecorView());
   }

   public static Unbinder bind(Dialog var0) {
      return bind(var0, (View)var0.getWindow().getDecorView());
   }

   public static Unbinder bind(View var0) {
      return bind(var0, (View)var0);
   }

   public static Unbinder bind(Object var0, Activity var1) {
      return bind(var0, var1.getWindow().getDecorView());
   }

   public static Unbinder bind(Object var0, Dialog var1) {
      return bind(var0, var1.getWindow().getDecorView());
   }

   public static Unbinder bind(Object var0, View var1) {
      Class var2 = var0.getClass();
      if (debug) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Looking up binding for ");
         var3.append(var2.getName());
         Log.d("ButterKnife", var3.toString());
      }

      Constructor var10 = findBindingConstructorForClass(var2);
      if (var10 == null) {
         return Unbinder.EMPTY;
      } else {
         StringBuilder var9;
         try {
            Unbinder var8 = (Unbinder)var10.newInstance(var0, var1);
            return var8;
         } catch (IllegalAccessException var4) {
            var9 = new StringBuilder();
            var9.append("Unable to invoke ");
            var9.append(var10);
            throw new RuntimeException(var9.toString(), var4);
         } catch (InstantiationException var5) {
            var9 = new StringBuilder();
            var9.append("Unable to invoke ");
            var9.append(var10);
            throw new RuntimeException(var9.toString(), var5);
         } catch (InvocationTargetException var6) {
            Throwable var7 = var6.getCause();
            if (!(var7 instanceof RuntimeException)) {
               if (var7 instanceof Error) {
                  throw (Error)var7;
               } else {
                  throw new RuntimeException("Unable to create binding instance.", var7);
               }
            } else {
               throw (RuntimeException)var7;
            }
         }
      }
   }

   private static Constructor findBindingConstructorForClass(Class var0) {
      Constructor var1 = (Constructor)BINDINGS.get(var0);
      if (var1 == null && !BINDINGS.containsKey(var0)) {
         String var3 = var0.getName();
         if (!var3.startsWith("android.") && !var3.startsWith("java.") && !var3.startsWith("androidx.")) {
            label55: {
               Constructor var11;
               label66: {
                  StringBuilder var10;
                  label53: {
                     NoSuchMethodException var10000;
                     label67: {
                        boolean var10001;
                        try {
                           ClassLoader var9 = var0.getClassLoader();
                           StringBuilder var2 = new StringBuilder();
                           var2.append(var3);
                           var2.append("_ViewBinding");
                           var11 = var9.loadClass(var2.toString()).getConstructor(var0, View.class);
                        } catch (ClassNotFoundException var6) {
                           var10001 = false;
                           break label53;
                        } catch (NoSuchMethodException var7) {
                           var10000 = var7;
                           var10001 = false;
                           break label67;
                        }

                        var1 = var11;

                        try {
                           if (!debug) {
                              break label55;
                           }

                           Log.d("ButterKnife", "HIT: Loaded binding class and constructor.");
                           break label66;
                        } catch (ClassNotFoundException var4) {
                           var10001 = false;
                           break label53;
                        } catch (NoSuchMethodException var5) {
                           var10000 = var5;
                           var10001 = false;
                        }
                     }

                     NoSuchMethodException var8 = var10000;
                     var10 = new StringBuilder();
                     var10.append("Unable to find binding constructor for ");
                     var10.append(var3);
                     throw new RuntimeException(var10.toString(), var8);
                  }

                  if (debug) {
                     var10 = new StringBuilder();
                     var10.append("Not found. Trying superclass ");
                     var10.append(var0.getSuperclass().getName());
                     Log.d("ButterKnife", var10.toString());
                  }

                  var1 = findBindingConstructorForClass(var0.getSuperclass());
                  break label55;
               }

               var1 = var11;
            }

            BINDINGS.put(var0, var1);
            return var1;
         } else {
            if (debug) {
               Log.d("ButterKnife", "MISS: Reached framework class. Abandoning search.");
            }

            return null;
         }
      } else {
         if (debug) {
            Log.d("ButterKnife", "HIT: Cached in binding map.");
         }

         return var1;
      }
   }

   public static void setDebug(boolean var0) {
      debug = var0;
   }
}
