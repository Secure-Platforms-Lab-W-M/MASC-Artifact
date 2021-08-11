package androidx.lifecycle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class ClassesInfoCache {
   private static final int CALL_TYPE_NO_ARG = 0;
   private static final int CALL_TYPE_PROVIDER = 1;
   private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
   static ClassesInfoCache sInstance = new ClassesInfoCache();
   private final Map mCallbackMap = new HashMap();
   private final Map mHasLifecycleMethods = new HashMap();

   private ClassesInfoCache.CallbackInfo createInfo(Class var1, Method[] var2) {
      Class var8 = var1.getSuperclass();
      HashMap var7 = new HashMap();
      if (var8 != null) {
         ClassesInfoCache.CallbackInfo var13 = this.getInfo(var8);
         if (var13 != null) {
            var7.putAll(var13.mHandlerToEvent);
         }
      }

      Class[] var14 = var1.getInterfaces();
      int var4 = var14.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Iterator var9 = this.getInfo(var14[var3]).mHandlerToEvent.entrySet().iterator();

         while(var9.hasNext()) {
            Entry var10 = (Entry)var9.next();
            this.verifyAndPutHandler(var7, (ClassesInfoCache.MethodReference)var10.getKey(), (Lifecycle.Event)var10.getValue(), var1);
         }
      }

      if (var2 == null) {
         var2 = this.getDeclaredMethods(var1);
      }

      boolean var6 = false;
      int var5 = var2.length;

      for(var4 = 0; var4 < var5; ++var4) {
         Method var15 = var2[var4];
         OnLifecycleEvent var17 = (OnLifecycleEvent)var15.getAnnotation(OnLifecycleEvent.class);
         if (var17 != null) {
            var6 = true;
            Class[] var16 = var15.getParameterTypes();
            byte var12 = 0;
            if (var16.length > 0) {
               var12 = 1;
               if (!var16[0].isAssignableFrom(LifecycleOwner.class)) {
                  throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
               }
            }

            Lifecycle.Event var18 = var17.value();
            if (var16.length > 1) {
               var12 = 2;
               if (!var16[1].isAssignableFrom(Lifecycle.Event.class)) {
                  throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
               }

               if (var18 != Lifecycle.Event.ON_ANY) {
                  throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
               }
            }

            if (var16.length > 2) {
               throw new IllegalArgumentException("cannot have more than 2 params");
            }

            this.verifyAndPutHandler(var7, new ClassesInfoCache.MethodReference(var12, var15), var18, var1);
         }
      }

      ClassesInfoCache.CallbackInfo var11 = new ClassesInfoCache.CallbackInfo(var7);
      this.mCallbackMap.put(var1, var11);
      this.mHasLifecycleMethods.put(var1, var6);
      return var11;
   }

   private Method[] getDeclaredMethods(Class var1) {
      try {
         Method[] var3 = var1.getDeclaredMethods();
         return var3;
      } catch (NoClassDefFoundError var2) {
         throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", var2);
      }
   }

   private void verifyAndPutHandler(Map var1, ClassesInfoCache.MethodReference var2, Lifecycle.Event var3, Class var4) {
      Lifecycle.Event var5 = (Lifecycle.Event)var1.get(var2);
      if (var5 != null && var3 != var5) {
         Method var6 = var2.mMethod;
         StringBuilder var7 = new StringBuilder();
         var7.append("Method ");
         var7.append(var6.getName());
         var7.append(" in ");
         var7.append(var4.getName());
         var7.append(" already declared with different @OnLifecycleEvent value: previous value ");
         var7.append(var5);
         var7.append(", new value ");
         var7.append(var3);
         throw new IllegalArgumentException(var7.toString());
      } else {
         if (var5 == null) {
            var1.put(var2, var3);
         }

      }
   }

   ClassesInfoCache.CallbackInfo getInfo(Class var1) {
      ClassesInfoCache.CallbackInfo var2 = (ClassesInfoCache.CallbackInfo)this.mCallbackMap.get(var1);
      return var2 != null ? var2 : this.createInfo(var1, (Method[])null);
   }

   boolean hasLifecycleMethods(Class var1) {
      Boolean var4 = (Boolean)this.mHasLifecycleMethods.get(var1);
      if (var4 != null) {
         return var4;
      } else {
         Method[] var5 = this.getDeclaredMethods(var1);
         int var3 = var5.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if ((OnLifecycleEvent)var5[var2].getAnnotation(OnLifecycleEvent.class) != null) {
               this.createInfo(var1, var5);
               return true;
            }
         }

         this.mHasLifecycleMethods.put(var1, false);
         return false;
      }
   }

   static class CallbackInfo {
      final Map mEventToHandlers;
      final Map mHandlerToEvent;

      CallbackInfo(Map var1) {
         this.mHandlerToEvent = var1;
         this.mEventToHandlers = new HashMap();

         Entry var4;
         Object var6;
         for(Iterator var3 = var1.entrySet().iterator(); var3.hasNext(); ((List)var6).add(var4.getKey())) {
            var4 = (Entry)var3.next();
            Lifecycle.Event var5 = (Lifecycle.Event)var4.getValue();
            List var2 = (List)this.mEventToHandlers.get(var5);
            var6 = var2;
            if (var2 == null) {
               var6 = new ArrayList();
               this.mEventToHandlers.put(var5, var6);
            }
         }

      }

      private static void invokeMethodsForEvent(List var0, LifecycleOwner var1, Lifecycle.Event var2, Object var3) {
         if (var0 != null) {
            for(int var4 = var0.size() - 1; var4 >= 0; --var4) {
               ((ClassesInfoCache.MethodReference)var0.get(var4)).invokeCallback(var1, var2, var3);
            }
         }

      }

      void invokeCallbacks(LifecycleOwner var1, Lifecycle.Event var2, Object var3) {
         invokeMethodsForEvent((List)this.mEventToHandlers.get(var2), var1, var2, var3);
         invokeMethodsForEvent((List)this.mEventToHandlers.get(Lifecycle.Event.ON_ANY), var1, var2, var3);
      }
   }

   static class MethodReference {
      final int mCallType;
      final Method mMethod;

      MethodReference(int var1, Method var2) {
         this.mCallType = var1;
         this.mMethod = var2;
         var2.setAccessible(true);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null) {
            if (this.getClass() != var1.getClass()) {
               return false;
            } else {
               ClassesInfoCache.MethodReference var2 = (ClassesInfoCache.MethodReference)var1;
               return this.mCallType == var2.mCallType && this.mMethod.getName().equals(var2.mMethod.getName());
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.mCallType * 31 + this.mMethod.getName().hashCode();
      }

      void invokeCallback(LifecycleOwner var1, Lifecycle.Event var2, Object var3) {
         InvocationTargetException var15;
         label50: {
            IllegalAccessException var10000;
            label49: {
               int var4;
               boolean var10001;
               try {
                  var4 = this.mCallType;
               } catch (InvocationTargetException var11) {
                  var15 = var11;
                  var10001 = false;
                  break label50;
               } catch (IllegalAccessException var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label49;
               }

               if (var4 != 0) {
                  if (var4 != 1) {
                     if (var4 != 2) {
                        return;
                     }

                     try {
                        this.mMethod.invoke(var3, var1, var2);
                        return;
                     } catch (InvocationTargetException var5) {
                        var15 = var5;
                        var10001 = false;
                        break label50;
                     } catch (IllegalAccessException var6) {
                        var10000 = var6;
                        var10001 = false;
                     }
                  } else {
                     try {
                        this.mMethod.invoke(var3, var1);
                        return;
                     } catch (InvocationTargetException var7) {
                        var15 = var7;
                        var10001 = false;
                        break label50;
                     } catch (IllegalAccessException var8) {
                        var10000 = var8;
                        var10001 = false;
                     }
                  }
               } else {
                  try {
                     this.mMethod.invoke(var3);
                     return;
                  } catch (InvocationTargetException var9) {
                     var15 = var9;
                     var10001 = false;
                     break label50;
                  } catch (IllegalAccessException var10) {
                     var10000 = var10;
                     var10001 = false;
                  }
               }
            }

            IllegalAccessException var13 = var10000;
            throw new RuntimeException(var13);
         }

         InvocationTargetException var14 = var15;
         throw new RuntimeException("Failed to call observer method", var14.getCause());
      }
   }
}
