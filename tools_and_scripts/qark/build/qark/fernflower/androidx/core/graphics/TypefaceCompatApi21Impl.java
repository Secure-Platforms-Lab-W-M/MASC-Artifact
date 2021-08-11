package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
   private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
   private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
   private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
   private static final String TAG = "TypefaceCompatApi21Impl";
   private static Method sAddFontWeightStyle;
   private static Method sCreateFromFamiliesWithDefault;
   private static Class sFontFamily;
   private static Constructor sFontFamilyCtor;
   private static boolean sHasInitBeenCalled = false;

   private static boolean addFontWeightStyle(Object var0, String var1, int var2, boolean var3) {
      init();

      try {
         var3 = (Boolean)sAddFontWeightStyle.invoke(var0, var1, var2, var3);
         return var3;
      } catch (IllegalAccessException var4) {
         var0 = var4;
      } catch (InvocationTargetException var5) {
         var0 = var5;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private static Typeface createFromFamiliesWithDefault(Object var0) {
      init();

      try {
         Object var1 = Array.newInstance(sFontFamily, 1);
         Array.set(var1, 0, var0);
         Typeface var4 = (Typeface)sCreateFromFamiliesWithDefault.invoke((Object)null, var1);
         return var4;
      } catch (IllegalAccessException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private File getFile(ParcelFileDescriptor var1) {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append("/proc/self/fd/");
         var2.append(var1.getFd());
         String var4 = Os.readlink(var2.toString());
         if (OsConstants.S_ISREG(Os.stat(var4).st_mode)) {
            File var5 = new File(var4);
            return var5;
         } else {
            return null;
         }
      } catch (ErrnoException var3) {
         return null;
      }
   }

   private static void init() {
      if (!sHasInitBeenCalled) {
         sHasInitBeenCalled = true;

         Class var1;
         Constructor var2;
         Method var3;
         Method var6;
         label18: {
            Object var0;
            try {
               var1 = Class.forName("android.graphics.FontFamily");
               var2 = var1.getConstructor();
               var3 = var1.getMethod("addFontWeightStyle", String.class, Integer.TYPE, Boolean.TYPE);
               var6 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(var1, 1).getClass());
               break label18;
            } catch (ClassNotFoundException var4) {
               var0 = var4;
            } catch (NoSuchMethodException var5) {
               var0 = var5;
            }

            Log.e("TypefaceCompatApi21Impl", var0.getClass().getName(), (Throwable)var0);
            var1 = null;
            var2 = null;
            var3 = null;
            var6 = null;
         }

         sFontFamilyCtor = var2;
         sFontFamily = var1;
         sAddFontWeightStyle = var3;
         sCreateFromFamiliesWithDefault = var6;
      }
   }

   private static Object newFamily() {
      init();

      Object var0;
      try {
         var0 = sFontFamilyCtor.newInstance();
         return var0;
      } catch (IllegalAccessException var1) {
         var0 = var1;
      } catch (InstantiationException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context param1, FontResourcesParserCompat.FontFamilyFilesResourceEntry param2, Resources param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   public Typeface createFromFontInfo(Context var1, CancellationSignal var2, FontsContractCompat.FontInfo[] var3, int var4) {
      if (var3.length < 1) {
         return null;
      } else {
         FontsContractCompat.FontInfo var244 = this.findBestInfo(var3, var4);
         ContentResolver var5 = var1.getContentResolver();

         boolean var10001;
         ParcelFileDescriptor var242;
         try {
            var242 = var5.openFileDescriptor(var244.getUri(), "r", var2);
         } catch (IOException var239) {
            var10001 = false;
            return null;
         }

         if (var242 == null) {
            if (var242 != null) {
               try {
                  var242.close();
               } catch (IOException var230) {
                  var10001 = false;
                  return null;
               }
            }

            return null;
         } else {
            Throwable var10000;
            Throwable var241;
            label1666: {
               File var245;
               try {
                  var245 = this.getFile(var242);
               } catch (Throwable var238) {
                  var10000 = var238;
                  var10001 = false;
                  break label1666;
               }

               Typeface var240;
               if (var245 != null) {
                  label1665: {
                     try {
                        if (!var245.canRead()) {
                           break label1665;
                        }
                     } catch (Throwable var237) {
                        var10000 = var237;
                        var10001 = false;
                        break label1666;
                     }

                     try {
                        var240 = Typeface.createFromFile(var245);
                     } catch (Throwable var233) {
                        var10000 = var233;
                        var10001 = false;
                        break label1666;
                     }

                     if (var242 != null) {
                        try {
                           var242.close();
                        } catch (IOException var231) {
                           var10001 = false;
                           return null;
                        }
                     }

                     return var240;
                  }
               }

               FileInputStream var246;
               try {
                  var246 = new FileInputStream(var242.getFileDescriptor());
               } catch (Throwable var236) {
                  var10000 = var236;
                  var10001 = false;
                  break label1666;
               }

               try {
                  var240 = super.createFromInputStream(var1, var246);
               } catch (Throwable var235) {
                  var241 = var235;

                  try {
                     throw var241;
                  } finally {
                     try {
                        var246.close();
                     } catch (Throwable var228) {
                        Throwable var247 = var228;

                        label1606:
                        try {
                           var241.addSuppressed(var247);
                           break label1606;
                        } catch (Throwable var227) {
                           var10000 = var227;
                           var10001 = false;
                           break label1666;
                        }
                     }

                     try {
                        throw var5;
                     } catch (Throwable var226) {
                        var10000 = var226;
                        var10001 = false;
                        break label1666;
                     }
                  }
               }

               try {
                  var246.close();
               } catch (Throwable var234) {
                  var10000 = var234;
                  var10001 = false;
                  break label1666;
               }

               if (var242 != null) {
                  try {
                     var242.close();
                  } catch (IOException var232) {
                     var10001 = false;
                     return null;
                  }
               }

               return var240;
            }

            var241 = var10000;

            try {
               throw var241;
            } finally {
               if (var242 != null) {
                  try {
                     var242.close();
                  } catch (Throwable var224) {
                     Throwable var243 = var224;

                     label1594:
                     try {
                        var241.addSuppressed(var243);
                        break label1594;
                     } catch (IOException var223) {
                        var10001 = false;
                        return null;
                     }
                  }
               }

               try {
                  ;
               } catch (IOException var222) {
                  var10001 = false;
                  return null;
               }
            }
         }
      }
   }
}
