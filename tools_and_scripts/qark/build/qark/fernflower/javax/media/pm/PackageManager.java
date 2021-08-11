package javax.media.pm;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.fmj.registry.Registry;
import net.sf.fmj.utility.LoggerSingleton;

public final class PackageManager extends javax.media.PackageManager {
   private static final Logger logger;
   private static Registry registry;

   static {
      logger = LoggerSingleton.logger;
      registry = Registry.getInstance();
   }

   public static void commitContentPrefixList() {
      synchronized(PackageManager.class){}

      try {
         registry.commit();
      } catch (Exception var6) {
         Logger var1 = logger;
         Level var2 = Level.WARNING;
         StringBuilder var3 = new StringBuilder();
         var3.append("");
         var3.append(var6);
         var1.log(var2, var3.toString(), var6);
      } finally {
         ;
      }

   }

   public static void commitProtocolPrefixList() {
      synchronized(PackageManager.class){}

      try {
         registry.commit();
      } catch (IOException var6) {
         Logger var1 = logger;
         Level var2 = Level.WARNING;
         StringBuilder var3 = new StringBuilder();
         var3.append("");
         var3.append(var6);
         var1.log(var2, var3.toString(), var6);
      } finally {
         ;
      }

   }

   public static Vector getContentPrefixList() {
      synchronized(PackageManager.class){}

      Vector var0;
      try {
         var0 = registry.getContentPrefixList();
      } finally {
         ;
      }

      return var0;
   }

   public static Vector getProtocolPrefixList() {
      synchronized(PackageManager.class){}

      Vector var0;
      try {
         var0 = registry.getProtocolPrefixList();
      } finally {
         ;
      }

      return var0;
   }

   public static void setContentPrefixList(Vector var0) {
      synchronized(PackageManager.class){}

      try {
         registry.setContentPrefixList(var0);
      } finally {
         ;
      }

   }

   public static void setProtocolPrefixList(Vector var0) {
      synchronized(PackageManager.class){}

      try {
         registry.setProtocolPrefixList(var0);
      } finally {
         ;
      }

   }
}
