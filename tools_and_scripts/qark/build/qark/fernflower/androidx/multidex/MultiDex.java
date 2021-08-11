package androidx.multidex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import android.util.Log;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipFile;

public final class MultiDex {
   private static final String CODE_CACHE_NAME = "code_cache";
   private static final String CODE_CACHE_SECONDARY_FOLDER_NAME = "secondary-dexes";
   private static final boolean IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));
   private static final int MAX_SUPPORTED_SDK_VERSION = 20;
   private static final int MIN_SDK_VERSION = 4;
   private static final String NO_KEY_PREFIX = "";
   private static final String OLD_SECONDARY_FOLDER_NAME = "secondary-dexes";
   static final String TAG = "MultiDex";
   private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
   private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
   private static final Set installedApk = new HashSet();

   private MultiDex() {
   }

   private static void clearOldDexDir(Context var0) throws Exception {
      File var6 = new File(var0.getFilesDir(), "secondary-dexes");
      if (var6.isDirectory()) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Clearing old secondary dex dir (");
         var3.append(var6.getPath());
         var3.append(").");
         Log.i("MultiDex", var3.toString());
         File[] var7 = var6.listFiles();
         if (var7 == null) {
            var3 = new StringBuilder();
            var3.append("Failed to list secondary dex dir content (");
            var3.append(var6.getPath());
            var3.append(").");
            Log.w("MultiDex", var3.toString());
            return;
         }

         int var2 = var7.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            File var4 = var7[var1];
            StringBuilder var5 = new StringBuilder();
            var5.append("Trying to delete old file ");
            var5.append(var4.getPath());
            var5.append(" of size ");
            var5.append(var4.length());
            Log.i("MultiDex", var5.toString());
            if (!var4.delete()) {
               var5 = new StringBuilder();
               var5.append("Failed to delete old file ");
               var5.append(var4.getPath());
               Log.w("MultiDex", var5.toString());
            } else {
               var5 = new StringBuilder();
               var5.append("Deleted old file ");
               var5.append(var4.getPath());
               Log.i("MultiDex", var5.toString());
            }
         }

         if (!var6.delete()) {
            var3 = new StringBuilder();
            var3.append("Failed to delete secondary dex dir ");
            var3.append(var6.getPath());
            Log.w("MultiDex", var3.toString());
            return;
         }

         var3 = new StringBuilder();
         var3.append("Deleted old secondary dex dir ");
         var3.append(var6.getPath());
         Log.i("MultiDex", var3.toString());
      }

   }

   private static void doInstallation(Context param0, File param1, File param2, String param3, String param4, boolean param5) throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException {
      // $FF: Couldn't be decompiled
   }

   private static void expandFieldArray(Object var0, String var1, Object[] var2) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
      Field var5 = findField(var0, var1);
      Object[] var3 = (Object[])((Object[])var5.get(var0));
      Object[] var4 = (Object[])((Object[])Array.newInstance(var3.getClass().getComponentType(), var3.length + var2.length));
      System.arraycopy(var3, 0, var4, 0, var3.length);
      System.arraycopy(var2, 0, var4, var3.length, var2.length);
      var5.set(var0, var4);
   }

   private static Field findField(Object var0, String var1) throws NoSuchFieldException {
      Class var2 = var0.getClass();

      while(var2 != null) {
         try {
            Field var3 = var2.getDeclaredField(var1);
            if (!var3.isAccessible()) {
               var3.setAccessible(true);
            }

            return var3;
         } catch (NoSuchFieldException var4) {
            var2 = var2.getSuperclass();
         }
      }

      StringBuilder var5 = new StringBuilder();
      var5.append("Field ");
      var5.append(var1);
      var5.append(" not found in ");
      var5.append(var0.getClass());
      throw new NoSuchFieldException(var5.toString());
   }

   private static Method findMethod(Object var0, String var1, Class... var2) throws NoSuchMethodException {
      Class var3 = var0.getClass();

      while(var3 != null) {
         try {
            Method var4 = var3.getDeclaredMethod(var1, var2);
            if (!var4.isAccessible()) {
               var4.setAccessible(true);
            }

            return var4;
         } catch (NoSuchMethodException var5) {
            var3 = var3.getSuperclass();
         }
      }

      StringBuilder var6 = new StringBuilder();
      var6.append("Method ");
      var6.append(var1);
      var6.append(" with parameters ");
      var6.append(Arrays.asList(var2));
      var6.append(" not found in ");
      var6.append(var0.getClass());
      throw new NoSuchMethodException(var6.toString());
   }

   private static ApplicationInfo getApplicationInfo(Context var0) {
      try {
         ApplicationInfo var2 = var0.getApplicationInfo();
         return var2;
      } catch (RuntimeException var1) {
         Log.w("MultiDex", "Failure while trying to obtain ApplicationInfo from Context. Must be running in test mode. Skip patching.", var1);
         return null;
      }
   }

   private static ClassLoader getDexClassloader(Context var0) {
      ClassLoader var2;
      try {
         var2 = var0.getClassLoader();
      } catch (RuntimeException var1) {
         Log.w("MultiDex", "Failure while trying to obtain Context class loader. Must be running in test mode. Skip patching.", var1);
         return null;
      }

      if (VERSION.SDK_INT >= 14) {
         if (var2 instanceof BaseDexClassLoader) {
            return var2;
         }
      } else {
         if (var2 instanceof DexClassLoader) {
            return var2;
         }

         if (var2 instanceof PathClassLoader) {
            return var2;
         }
      }

      Log.e("MultiDex", "Context class loader is null or not dex-capable. Must be running in test mode. Skip patching.");
      return null;
   }

   private static File getDexDir(Context var0, File var1, String var2) throws IOException {
      var1 = new File(var1, "code_cache");

      File var4;
      label13: {
         try {
            mkdirChecked(var1);
         } catch (IOException var3) {
            var4 = new File(var0.getFilesDir(), "code_cache");
            mkdirChecked(var4);
            break label13;
         }

         var4 = var1;
      }

      var4 = new File(var4, var2);
      mkdirChecked(var4);
      return var4;
   }

   public static void install(Context var0) {
      Log.i("MultiDex", "Installing application");
      if (IS_VM_MULTIDEX_CAPABLE) {
         Log.i("MultiDex", "VM has multidex support, MultiDex support library is disabled.");
      } else if (VERSION.SDK_INT >= 4) {
         Exception var10000;
         label32: {
            ApplicationInfo var1;
            boolean var10001;
            try {
               var1 = getApplicationInfo(var0);
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label32;
            }

            if (var1 == null) {
               try {
                  Log.i("MultiDex", "No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                  return;
               } catch (Exception var2) {
                  var10000 = var2;
                  var10001 = false;
               }
            } else {
               label28: {
                  try {
                     doInstallation(var0, new File(var1.sourceDir), new File(var1.dataDir), "secondary-dexes", "", true);
                  } catch (Exception var3) {
                     var10000 = var3;
                     var10001 = false;
                     break label28;
                  }

                  Log.i("MultiDex", "install done");
                  return;
               }
            }
         }

         Exception var6 = var10000;
         Log.e("MultiDex", "MultiDex installation failure", var6);
         StringBuilder var7 = new StringBuilder();
         var7.append("MultiDex installation failed (");
         var7.append(var6.getMessage());
         var7.append(").");
         throw new RuntimeException(var7.toString());
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("MultiDex installation failed. SDK ");
         var5.append(VERSION.SDK_INT);
         var5.append(" is unsupported. Min SDK version is ");
         var5.append(4);
         var5.append(".");
         throw new RuntimeException(var5.toString());
      }
   }

   public static void installInstrumentation(Context var0, Context var1) {
      Log.i("MultiDex", "Installing instrumentation");
      if (IS_VM_MULTIDEX_CAPABLE) {
         Log.i("MultiDex", "VM has multidex support, MultiDex support library is disabled.");
      } else if (VERSION.SDK_INT >= 4) {
         Exception var10000;
         label46: {
            ApplicationInfo var3;
            boolean var10001;
            try {
               var3 = getApplicationInfo(var0);
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label46;
            }

            if (var3 == null) {
               try {
                  Log.i("MultiDex", "No ApplicationInfo available for instrumentation, i.e. running on a test Context: MultiDex support library is disabled.");
                  return;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            } else {
               label42: {
                  ApplicationInfo var2;
                  try {
                     var2 = getApplicationInfo(var1);
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label42;
                  }

                  if (var2 == null) {
                     try {
                        Log.i("MultiDex", "No ApplicationInfo available, i.e. running on a test Context: MultiDex support library is disabled.");
                        return;
                     } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                     }
                  } else {
                     label38: {
                        try {
                           StringBuilder var4 = new StringBuilder();
                           var4.append(var0.getPackageName());
                           var4.append(".");
                           String var13 = var4.toString();
                           File var16 = new File(var2.dataDir);
                           File var15 = new File(var3.sourceDir);
                           StringBuilder var5 = new StringBuilder();
                           var5.append(var13);
                           var5.append("secondary-dexes");
                           doInstallation(var1, var15, var16, var5.toString(), var13, false);
                           doInstallation(var1, new File(var2.sourceDir), var16, "secondary-dexes", "", false);
                        } catch (Exception var8) {
                           var10000 = var8;
                           var10001 = false;
                           break label38;
                        }

                        Log.i("MultiDex", "Installation done");
                        return;
                     }
                  }
               }
            }
         }

         Exception var12 = var10000;
         Log.e("MultiDex", "MultiDex installation failure", var12);
         StringBuilder var14 = new StringBuilder();
         var14.append("MultiDex installation failed (");
         var14.append(var12.getMessage());
         var14.append(").");
         throw new RuntimeException(var14.toString());
      } else {
         StringBuilder var11 = new StringBuilder();
         var11.append("MultiDex installation failed. SDK ");
         var11.append(VERSION.SDK_INT);
         var11.append(" is unsupported. Min SDK version is ");
         var11.append(4);
         var11.append(".");
         throw new RuntimeException(var11.toString());
      }
   }

   private static void installSecondaryDexes(ClassLoader var0, File var1, List var2) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, SecurityException, ClassNotFoundException, InstantiationException {
      if (!var2.isEmpty()) {
         if (VERSION.SDK_INT >= 19) {
            MultiDex.V19.install(var0, var2, var1);
            return;
         }

         if (VERSION.SDK_INT >= 14) {
            MultiDex.V14.install(var0, var2);
            return;
         }

         MultiDex.class_13.install(var0, var2);
      }

   }

   static boolean isVMMultidexCapable(String var0) {
      boolean var4 = false;
      boolean var3 = var4;
      if (var0 != null) {
         StringTokenizer var7 = new StringTokenizer(var0, ".");
         var3 = var7.hasMoreTokens();
         String var6 = null;
         String var5;
         if (var3) {
            var5 = var7.nextToken();
         } else {
            var5 = null;
         }

         if (var7.hasMoreTokens()) {
            var6 = var7.nextToken();
         }

         var3 = var4;
         if (var5 != null) {
            var3 = var4;
            if (var6 != null) {
               label38: {
                  int var1;
                  int var2;
                  try {
                     var1 = Integer.parseInt(var5);
                     var2 = Integer.parseInt(var6);
                  } catch (NumberFormatException var8) {
                     var3 = var4;
                     break label38;
                  }

                  var4 = true;
                  var3 = var4;
                  if (var1 <= 2) {
                     if (var1 == 2 && var2 >= 1) {
                        var3 = var4;
                     } else {
                        var3 = false;
                     }
                  }
               }
            }
         }
      }

      StringBuilder var9 = new StringBuilder();
      var9.append("VM with version ");
      var9.append(var0);
      if (var3) {
         var0 = " has multidex support";
      } else {
         var0 = " does not have multidex support";
      }

      var9.append(var0);
      Log.i("MultiDex", var9.toString());
      return var3;
   }

   private static void mkdirChecked(File var0) throws IOException {
      var0.mkdir();
      if (!var0.isDirectory()) {
         File var1 = var0.getParentFile();
         StringBuilder var3;
         if (var1 == null) {
            var3 = new StringBuilder();
            var3.append("Failed to create dir ");
            var3.append(var0.getPath());
            var3.append(". Parent file is null.");
            Log.e("MultiDex", var3.toString());
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Failed to create dir ");
            var2.append(var0.getPath());
            var2.append(". parent file is a dir ");
            var2.append(var1.isDirectory());
            var2.append(", a file ");
            var2.append(var1.isFile());
            var2.append(", exists ");
            var2.append(var1.exists());
            var2.append(", readable ");
            var2.append(var1.canRead());
            var2.append(", writable ");
            var2.append(var1.canWrite());
            Log.e("MultiDex", var2.toString());
         }

         var3 = new StringBuilder();
         var3.append("Failed to create directory ");
         var3.append(var0.getPath());
         throw new IOException(var3.toString());
      }
   }

   private static final class V14 {
      private static final int EXTRACTED_SUFFIX_LENGTH = ".zip".length();
      private final MultiDex.V14.ElementConstructor elementConstructor;

      private V14() throws ClassNotFoundException, SecurityException, NoSuchMethodException {
         Class var2 = Class.forName("dalvik.system.DexPathList$Element");

         Object var1;
         try {
            var1 = new MultiDex.V14.ICSElementConstructor(var2);
         } catch (NoSuchMethodException var4) {
            try {
               var1 = new MultiDex.V14.JBMR11ElementConstructor(var2);
            } catch (NoSuchMethodException var3) {
               var1 = new MultiDex.V14.JBMR2ElementConstructor(var2);
            }
         }

         this.elementConstructor = (MultiDex.V14.ElementConstructor)var1;
      }

      static void install(ClassLoader var0, List var1) throws IOException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
         Object var4 = MultiDex.findField(var0, "pathList").get(var0);
         Object[] var5 = (new MultiDex.V14()).makeDexElements(var1);

         try {
            MultiDex.expandFieldArray(var4, "dexElements", var5);
         } catch (NoSuchFieldException var3) {
            Log.w("MultiDex", "Failed find field 'dexElements' attempting 'pathElements'", var3);
            MultiDex.expandFieldArray(var4, "pathElements", var5);
         }
      }

      private Object[] makeDexElements(List var1) throws IOException, SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
         Object[] var3 = new Object[var1.size()];

         for(int var2 = 0; var2 < var3.length; ++var2) {
            File var4 = (File)var1.get(var2);
            var3[var2] = this.elementConstructor.newInstance(var4, DexFile.loadDex(var4.getPath(), optimizedPathFor(var4), 0));
         }

         return var3;
      }

      private static String optimizedPathFor(File var0) {
         File var1 = var0.getParentFile();
         String var3 = var0.getName();
         StringBuilder var2 = new StringBuilder();
         var2.append(var3.substring(0, var3.length() - EXTRACTED_SUFFIX_LENGTH));
         var2.append(".dex");
         return (new File(var1, var2.toString())).getPath();
      }

      private interface ElementConstructor {
         Object newInstance(File var1, DexFile var2) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException;
      }

      private static class ICSElementConstructor implements MultiDex.V14.ElementConstructor {
         private final Constructor elementConstructor;

         ICSElementConstructor(Class var1) throws SecurityException, NoSuchMethodException {
            Constructor var2 = var1.getConstructor(File.class, ZipFile.class, DexFile.class);
            this.elementConstructor = var2;
            var2.setAccessible(true);
         }

         public Object newInstance(File var1, DexFile var2) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
            return this.elementConstructor.newInstance(var1, new ZipFile(var1), var2);
         }
      }

      private static class JBMR11ElementConstructor implements MultiDex.V14.ElementConstructor {
         private final Constructor elementConstructor;

         JBMR11ElementConstructor(Class var1) throws SecurityException, NoSuchMethodException {
            Constructor var2 = var1.getConstructor(File.class, File.class, DexFile.class);
            this.elementConstructor = var2;
            var2.setAccessible(true);
         }

         public Object newInstance(File var1, DexFile var2) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
            return this.elementConstructor.newInstance(var1, var1, var2);
         }
      }

      private static class JBMR2ElementConstructor implements MultiDex.V14.ElementConstructor {
         private final Constructor elementConstructor;

         JBMR2ElementConstructor(Class var1) throws SecurityException, NoSuchMethodException {
            Constructor var2 = var1.getConstructor(File.class, Boolean.TYPE, File.class, DexFile.class);
            this.elementConstructor = var2;
            var2.setAccessible(true);
         }

         public Object newInstance(File var1, DexFile var2) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
            return this.elementConstructor.newInstance(var1, Boolean.FALSE, var1, var2);
         }
      }
   }

   private static final class V19 {
      static void install(ClassLoader var0, List var1, File var2) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException {
         Object var4 = MultiDex.findField(var0, "pathList").get(var0);
         ArrayList var3 = new ArrayList();
         MultiDex.expandFieldArray(var4, "dexElements", makeDexElements(var4, new ArrayList(var1), var2, var3));
         if (var3.size() > 0) {
            Iterator var5 = var3.iterator();

            while(var5.hasNext()) {
               Log.w("MultiDex", "Exception in makeDexElement", (IOException)var5.next());
            }

            Field var7 = MultiDex.findField(var4, "dexElementsSuppressedExceptions");
            IOException[] var9 = (IOException[])((IOException[])var7.get(var4));
            IOException[] var6;
            if (var9 == null) {
               var6 = (IOException[])var3.toArray(new IOException[var3.size()]);
            } else {
               var6 = new IOException[var3.size() + var9.length];
               var3.toArray(var6);
               System.arraycopy(var9, 0, var6, var3.size(), var9.length);
            }

            var7.set(var4, var6);
            IOException var8 = new IOException("I/O exception during makeDexElement");
            var8.initCause((Throwable)var3.get(0));
            throw var8;
         }
      }

      private static Object[] makeDexElements(Object var0, ArrayList var1, File var2, ArrayList var3) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
         return (Object[])((Object[])MultiDex.findMethod(var0, "makeDexElements", ArrayList.class, File.class, ArrayList.class).invoke(var0, var1, var2, var3));
      }
   }

   private static final class class_13 {
      static void install(ClassLoader var0, List var1) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, IOException {
         int var2 = var1.size();
         Field var3 = MultiDex.findField(var0, "path");
         StringBuilder var4 = new StringBuilder((String)var3.get(var0));
         String[] var5 = new String[var2];
         File[] var6 = new File[var2];
         ZipFile[] var7 = new ZipFile[var2];
         DexFile[] var8 = new DexFile[var2];

         String var9;
         StringBuilder var11;
         for(ListIterator var12 = var1.listIterator(); var12.hasNext(); var8[var2] = DexFile.loadDex(var9, var11.toString(), 0)) {
            File var10 = (File)var12.next();
            var9 = var10.getAbsolutePath();
            var4.append(':');
            var4.append(var9);
            var2 = var12.previousIndex();
            var5[var2] = var9;
            var6[var2] = var10;
            var7[var2] = new ZipFile(var10);
            var11 = new StringBuilder();
            var11.append(var9);
            var11.append(".dex");
         }

         var3.set(var0, var4.toString());
         MultiDex.expandFieldArray(var0, "mPaths", var5);
         MultiDex.expandFieldArray(var0, "mFiles", var6);
         MultiDex.expandFieldArray(var0, "mZips", var7);
         MultiDex.expandFieldArray(var0, "mDexs", var8);
      }
   }
}
