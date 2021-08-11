package javax.media.cdm;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import javax.media.CaptureDeviceInfo;
import javax.media.Format;
import net.sf.fmj.registry.Registry;

public class CaptureDeviceManager extends javax.media.CaptureDeviceManager {
   public static boolean addDevice(CaptureDeviceInfo var0) {
      synchronized(CaptureDeviceManager.class){}

      boolean var1;
      try {
         var1 = Registry.getInstance().addDevice(var0);
      } finally {
         ;
      }

      return var1;
   }

   public static void commit() throws IOException {
      synchronized(CaptureDeviceManager.class){}

      try {
         Registry.getInstance().commit();
      } finally {
         ;
      }

   }

   public static CaptureDeviceInfo getDevice(String var0) {
      synchronized(CaptureDeviceManager.class){}

      label85: {
         Throwable var10000;
         label84: {
            boolean var10001;
            Iterator var2;
            try {
               var2 = getDeviceList().iterator();
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label84;
            }

            while(true) {
               boolean var1;
               CaptureDeviceInfo var3;
               try {
                  if (!var2.hasNext()) {
                     break label85;
                  }

                  var3 = (CaptureDeviceInfo)var2.next();
                  var1 = var3.getName().equals(var0);
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }

               if (var1) {
                  return var3;
               }
            }
         }

         Throwable var10 = var10000;
         throw var10;
      }

      return null;
   }

   public static Vector getDeviceList() {
      synchronized(CaptureDeviceManager.class){}

      Vector var0;
      try {
         var0 = Registry.getInstance().getDeviceList();
      } finally {
         ;
      }

      return var0;
   }

   public static Vector getDeviceList(Format var0) {
      synchronized(CaptureDeviceManager.class){}

      Vector var3;
      label350: {
         Throwable var10000;
         label349: {
            Iterator var4;
            boolean var10001;
            try {
               var3 = new Vector();
               var4 = getDeviceList().iterator();
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label349;
            }

            label346:
            while(true) {
               CaptureDeviceInfo var5;
               try {
                  if (!var4.hasNext()) {
                     break label350;
                  }

                  var5 = (CaptureDeviceInfo)var4.next();
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break;
               }

               if (var0 == null) {
                  try {
                     var3.add(var5);
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break;
                  }
               } else {
                  int var2;
                  Format[] var6;
                  try {
                     var6 = var5.getFormats();
                     var2 = var6.length;
                  } catch (Throwable var33) {
                     var10000 = var33;
                     var10001 = false;
                     break;
                  }

                  for(int var1 = 0; var1 < var2; ++var1) {
                     try {
                        if (var0.matches(var6[var1])) {
                           var3.add(var5);
                           break;
                        }
                     } catch (Throwable var35) {
                        var10000 = var35;
                        var10001 = false;
                        break label346;
                     }
                  }
               }
            }
         }

         Throwable var37 = var10000;
         throw var37;
      }

      return var3;
   }

   public static boolean removeDevice(CaptureDeviceInfo var0) {
      synchronized(CaptureDeviceManager.class){}

      boolean var1;
      try {
         var1 = Registry.getInstance().removeDevice(var0);
      } finally {
         ;
      }

      return var1;
   }
}
