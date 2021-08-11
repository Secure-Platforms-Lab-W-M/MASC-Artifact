package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;

public class CoreComponentFactory extends android.app.AppComponentFactory {
   static Object checkCompatWrapper(Object var0) {
      if (var0 instanceof CoreComponentFactory.CompatWrapped) {
         Object var1 = ((CoreComponentFactory.CompatWrapped)var0).getWrapper();
         if (var1 != null) {
            return var1;
         }
      }

      return var0;
   }

   public Activity instantiateActivity(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (Activity)checkCompatWrapper(super.instantiateActivity(var1, var2, var3));
   }

   public Application instantiateApplication(ClassLoader var1, String var2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (Application)checkCompatWrapper(super.instantiateApplication(var1, var2));
   }

   public ContentProvider instantiateProvider(ClassLoader var1, String var2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (ContentProvider)checkCompatWrapper(super.instantiateProvider(var1, var2));
   }

   public BroadcastReceiver instantiateReceiver(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (BroadcastReceiver)checkCompatWrapper(super.instantiateReceiver(var1, var2, var3));
   }

   public Service instantiateService(ClassLoader var1, String var2, Intent var3) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      return (Service)checkCompatWrapper(super.instantiateService(var1, var2, var3));
   }

   public interface CompatWrapped {
      Object getWrapper();
   }
}
