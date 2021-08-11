package com.bumptech.glide.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
public final class ManifestParser {
   private static final String GLIDE_MODULE_VALUE = "GlideModule";
   private static final String TAG = "ManifestParser";
   private final Context context;

   public ManifestParser(Context var1) {
      this.context = var1;
   }

   private static GlideModule parseModule(String var0) {
      Class var3;
      try {
         var3 = Class.forName(var0);
      } catch (ClassNotFoundException var4) {
         throw new IllegalArgumentException("Unable to find GlideModule implementation", var4);
      }

      StringBuilder var1 = null;
      Object var9 = null;

      label29: {
         Object var2;
         try {
            var2 = var3.getDeclaredConstructor().newInstance();
         } catch (InstantiationException var5) {
            throwInstantiateGlideModuleException(var3, var5);
            break label29;
         } catch (IllegalAccessException var6) {
            throwInstantiateGlideModuleException(var3, var6);
            break label29;
         } catch (NoSuchMethodException var7) {
            throwInstantiateGlideModuleException(var3, var7);
            break label29;
         } catch (InvocationTargetException var8) {
            throwInstantiateGlideModuleException(var3, var8);
            var9 = var1;
            break label29;
         }

         var9 = var2;
      }

      if (var9 instanceof GlideModule) {
         return (GlideModule)var9;
      } else {
         var1 = new StringBuilder();
         var1.append("Expected instanceof GlideModule, but found: ");
         var1.append(var9);
         throw new RuntimeException(var1.toString());
      }
   }

   private static void throwInstantiateGlideModuleException(Class var0, Exception var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Unable to instantiate GlideModule implementation for ");
      var2.append(var0);
      throw new RuntimeException(var2.toString(), var1);
   }

   public List parse() {
      if (Log.isLoggable("ManifestParser", 3)) {
         Log.d("ManifestParser", "Loading Glide modules");
      }

      ArrayList var1 = new ArrayList();

      label61: {
         NameNotFoundException var10000;
         label67: {
            ApplicationInfo var2;
            boolean var10001;
            try {
               var2 = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
               if (var2.metaData == null) {
                  if (Log.isLoggable("ManifestParser", 3)) {
                     Log.d("ManifestParser", "Got null app info metadata");
                     return var1;
                  }

                  return var1;
               }
            } catch (NameNotFoundException var9) {
               var10000 = var9;
               var10001 = false;
               break label67;
            }

            try {
               if (Log.isLoggable("ManifestParser", 2)) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Got app info metadata: ");
                  var3.append(var2.metaData);
                  Log.v("ManifestParser", var3.toString());
               }
            } catch (NameNotFoundException var8) {
               var10000 = var8;
               var10001 = false;
               break label67;
            }

            Iterator var11;
            try {
               var11 = var2.metaData.keySet().iterator();
            } catch (NameNotFoundException var7) {
               var10000 = var7;
               var10001 = false;
               break label67;
            }

            while(true) {
               try {
                  if (!var11.hasNext()) {
                     break label61;
                  }

                  String var4 = (String)var11.next();
                  if ("GlideModule".equals(var2.metaData.get(var4))) {
                     var1.add(parseModule(var4));
                     if (Log.isLoggable("ManifestParser", 3)) {
                        StringBuilder var5 = new StringBuilder();
                        var5.append("Loaded Glide module: ");
                        var5.append(var4);
                        Log.d("ManifestParser", var5.toString());
                     }
                  }
               } catch (NameNotFoundException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break;
               }
            }
         }

         NameNotFoundException var10 = var10000;
         throw new RuntimeException("Unable to find metadata to parse GlideModules", var10);
      }

      if (Log.isLoggable("ManifestParser", 3)) {
         Log.d("ManifestParser", "Finished loading Glide modules");
      }

      return var1;
   }
}
