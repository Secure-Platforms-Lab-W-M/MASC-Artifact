package android.arch.lifecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class Lifecycling {
   private static Map sCallbackCache;
   private static Constructor sREFLECTIVE;

   static {
      try {
         sREFLECTIVE = ReflectiveGenericLifecycleObserver.class.getDeclaredConstructor(Object.class);
      } catch (NoSuchMethodException var1) {
      }

      sCallbackCache = new HashMap();
   }

   static String getAdapterName(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0.replace(".", "_"));
      var1.append("_LifecycleAdapter");
      return var1.toString();
   }

   @NonNull
   static GenericLifecycleObserver getCallback(Object var0) {
      if (var0 instanceof GenericLifecycleObserver) {
         return (GenericLifecycleObserver)var0;
      } else {
         IllegalAccessException var26;
         label84: {
            InstantiationException var25;
            label83: {
               InvocationTargetException var10000;
               label82: {
                  Constructor var1;
                  Class var2;
                  boolean var10001;
                  try {
                     var2 = var0.getClass();
                     var1 = (Constructor)sCallbackCache.get(var2);
                  } catch (IllegalAccessException var18) {
                     var26 = var18;
                     var10001 = false;
                     break label84;
                  } catch (InstantiationException var19) {
                     var25 = var19;
                     var10001 = false;
                     break label83;
                  } catch (InvocationTargetException var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label82;
                  }

                  if (var1 != null) {
                     try {
                        return (GenericLifecycleObserver)var1.newInstance(var0);
                     } catch (IllegalAccessException var3) {
                        var26 = var3;
                        var10001 = false;
                        break label84;
                     } catch (InstantiationException var4) {
                        var25 = var4;
                        var10001 = false;
                        break label83;
                     } catch (InvocationTargetException var5) {
                        var10000 = var5;
                        var10001 = false;
                     }
                  } else {
                     label89: {
                        try {
                           var1 = getGeneratedAdapterConstructor(var2);
                        } catch (IllegalAccessException var15) {
                           var26 = var15;
                           var10001 = false;
                           break label84;
                        } catch (InstantiationException var16) {
                           var25 = var16;
                           var10001 = false;
                           break label83;
                        } catch (InvocationTargetException var17) {
                           var10000 = var17;
                           var10001 = false;
                           break label89;
                        }

                        if (var1 != null) {
                           try {
                              if (!var1.isAccessible()) {
                                 var1.setAccessible(true);
                              }
                           } catch (IllegalAccessException var12) {
                              var26 = var12;
                              var10001 = false;
                              break label84;
                           } catch (InstantiationException var13) {
                              var25 = var13;
                              var10001 = false;
                              break label83;
                           } catch (InvocationTargetException var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label89;
                           }
                        } else {
                           try {
                              var1 = sREFLECTIVE;
                           } catch (IllegalAccessException var9) {
                              var26 = var9;
                              var10001 = false;
                              break label84;
                           } catch (InstantiationException var10) {
                              var25 = var10;
                              var10001 = false;
                              break label83;
                           } catch (InvocationTargetException var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label89;
                           }
                        }

                        try {
                           sCallbackCache.put(var2, var1);
                           GenericLifecycleObserver var24 = (GenericLifecycleObserver)var1.newInstance(var0);
                           return var24;
                        } catch (IllegalAccessException var6) {
                           var26 = var6;
                           var10001 = false;
                           break label84;
                        } catch (InstantiationException var7) {
                           var25 = var7;
                           var10001 = false;
                           break label83;
                        } catch (InvocationTargetException var8) {
                           var10000 = var8;
                           var10001 = false;
                        }
                     }
                  }
               }

               InvocationTargetException var21 = var10000;
               throw new RuntimeException(var21);
            }

            InstantiationException var22 = var25;
            throw new RuntimeException(var22);
         }

         IllegalAccessException var23 = var26;
         throw new RuntimeException(var23);
      }
   }

   @Nullable
   private static Constructor getGeneratedAdapterConstructor(Class var0) {
      Package var1 = var0.getPackage();
      String var11;
      if (var1 != null) {
         var11 = var1.getName();
      } else {
         var11 = "";
      }

      String var2 = var0.getCanonicalName();
      if (var2 == null) {
         return null;
      } else {
         if (!var11.isEmpty()) {
            var2 = var2.substring(var11.length() + 1);
         }

         var2 = getAdapterName(var2);

         label53: {
            NoSuchMethodException var10000;
            label52: {
               boolean var10001;
               label51: {
                  label50: {
                     try {
                        if (var11.isEmpty()) {
                           break label50;
                        }
                     } catch (ClassNotFoundException var8) {
                        var10001 = false;
                        break label53;
                     } catch (NoSuchMethodException var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label52;
                     }

                     try {
                        StringBuilder var3 = new StringBuilder();
                        var3.append(var11);
                        var3.append(".");
                        var3.append(var2);
                        var11 = var3.toString();
                        break label51;
                     } catch (ClassNotFoundException var6) {
                        var10001 = false;
                        break label53;
                     } catch (NoSuchMethodException var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label52;
                     }
                  }

                  var11 = var2;
               }

               try {
                  Constructor var12 = Class.forName(var11).getDeclaredConstructor(var0);
                  return var12;
               } catch (ClassNotFoundException var4) {
                  var10001 = false;
                  break label53;
               } catch (NoSuchMethodException var5) {
                  var10000 = var5;
                  var10001 = false;
               }
            }

            NoSuchMethodException var10 = var10000;
            throw new RuntimeException(var10);
         }

         var0 = var0.getSuperclass();
         return var0 != null ? getGeneratedAdapterConstructor(var0) : null;
      }
   }
}
