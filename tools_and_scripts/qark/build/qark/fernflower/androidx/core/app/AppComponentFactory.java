package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import java.lang.reflect.InvocationTargetException;

public class AppComponentFactory extends android.app.AppComponentFactory {
   public final Activity instantiateActivity(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (Activity)CoreComponentFactory.checkCompatWrapper(this.instantiateActivityCompat(var1, var2, var3));
   }

   public Activity instantiateActivityCompat(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      Object var6;
      try {
         Activity var7 = (Activity)Class.forName(var2, false, var1).getDeclaredConstructor().newInstance();
         return var7;
      } catch (InvocationTargetException var4) {
         var6 = var4;
      } catch (NoSuchMethodException var5) {
         var6 = var5;
      }

      throw new RuntimeException("Couldn't call constructor", (Throwable)var6);
   }

   public final Application instantiateApplication(ClassLoader var1, String var2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (Application)CoreComponentFactory.checkCompatWrapper(this.instantiateApplicationCompat(var1, var2));
   }

   public Application instantiateApplicationCompat(ClassLoader var1, String var2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      Object var5;
      try {
         Application var6 = (Application)Class.forName(var2, false, var1).getDeclaredConstructor().newInstance();
         return var6;
      } catch (InvocationTargetException var3) {
         var5 = var3;
      } catch (NoSuchMethodException var4) {
         var5 = var4;
      }

      throw new RuntimeException("Couldn't call constructor", (Throwable)var5);
   }

   public final ContentProvider instantiateProvider(ClassLoader var1, String var2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (ContentProvider)CoreComponentFactory.checkCompatWrapper(this.instantiateProviderCompat(var1, var2));
   }

   public ContentProvider instantiateProviderCompat(ClassLoader var1, String var2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      Object var5;
      try {
         ContentProvider var6 = (ContentProvider)Class.forName(var2, false, var1).getDeclaredConstructor().newInstance();
         return var6;
      } catch (InvocationTargetException var3) {
         var5 = var3;
      } catch (NoSuchMethodException var4) {
         var5 = var4;
      }

      throw new RuntimeException("Couldn't call constructor", (Throwable)var5);
   }

   public final BroadcastReceiver instantiateReceiver(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (BroadcastReceiver)CoreComponentFactory.checkCompatWrapper(this.instantiateReceiverCompat(var1, var2, var3));
   }

   public BroadcastReceiver instantiateReceiverCompat(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      Object var6;
      try {
         BroadcastReceiver var7 = (BroadcastReceiver)Class.forName(var2, false, var1).getDeclaredConstructor().newInstance();
         return var7;
      } catch (InvocationTargetException var4) {
         var6 = var4;
      } catch (NoSuchMethodException var5) {
         var6 = var5;
      }

      throw new RuntimeException("Couldn't call constructor", (Throwable)var6);
   }

   public final Service instantiateService(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (Service)CoreComponentFactory.checkCompatWrapper(this.instantiateServiceCompat(var1, var2, var3));
   }

   public Service instantiateServiceCompat(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      Object var6;
      try {
         Service var7 = (Service)Class.forName(var2, false, var1).getDeclaredConstructor().newInstance();
         return var7;
      } catch (InvocationTargetException var4) {
         var6 = var4;
      } catch (NoSuchMethodException var5) {
         var6 = var5;
      }

      throw new RuntimeException("Couldn't call constructor", (Throwable)var6);
   }
}
