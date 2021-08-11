package android.arch.lifecycle;

import android.support.annotation.Nullable;
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

   private ClassesInfoCache.CallbackInfo createInfo(Class var1, @Nullable Method[] var2) {
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
         throw new IllegalArgumentException("Method " + var6.getName() + " in " + var4.getName() + " already declared with different @OnLifecycleEvent value: previous" + " value " + var5 + ", new value " + var3);
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
      if (this.mHasLifecycleMethods.containsKey(var1)) {
         return (Boolean)this.mHasLifecycleMethods.get(var1);
      } else {
         Method[] var4 = this.getDeclaredMethods(var1);
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if ((OnLifecycleEvent)var4[var2].getAnnotation(OnLifecycleEvent.class) != null) {
               this.createInfo(var1, var4);
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
         this.mMethod.setAccessible(true);
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (var1 != null && this.getClass() == var1.getClass()) {
               ClassesInfoCache.MethodReference var2 = (ClassesInfoCache.MethodReference)var1;
               if (this.mCallType == var2.mCallType && this.mMethod.getName().equals(var2.mMethod.getName())) {
                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return true;
         }
      }

      public int hashCode() {
         return this.mCallType * 31 + this.mMethod.getName().hashCode();
      }

      void invokeCallback(LifecycleOwner var1, Lifecycle.Event var2, Object var3) {
         try {
            switch(this.mCallType) {
            case 0:
               this.mMethod.invoke(var3);
               return;
            case 1:
               this.mMethod.invoke(var3, var1);
               return;
            case 2:
               this.mMethod.invoke(var3, var1, var2);
               return;
            default:
            }
         } catch (InvocationTargetException var4) {
            throw new RuntimeException("Failed to call observer method", var4.getCause());
         } catch (IllegalAccessException var5) {
            throw new RuntimeException(var5);
         }
      }
   }
}
