package net.sf.fmj.utility;

import java.util.logging.Logger;
import javax.media.Manager;
import javax.media.PackageManager;

public final class ClasspathChecker {
   private static final Logger logger;

   static {
      logger = LoggerSingleton.logger;
   }

   public static boolean check() {
      boolean var0 = true;
      if (!checkFMJPrefixInPackageManager()) {
         var0 = false;
      }

      if (!checkManagerImplementation()) {
         var0 = false;
      }

      return var0;
   }

   public static boolean checkAndWarn() {
      boolean var0 = true;
      if (!checkFMJPrefixInPackageManager()) {
         logger.warning("net.sf.fmj not found in PackageManager.getContentPrefixList() and PackageManager.getProtocolPrefixList(); is JMF ahead of FMJ in the classpath?");
         var0 = false;
      }

      if (checkJMFInClassPath()) {
         logger.info("JMF detected in classpath");
      }

      if (!checkManagerImplementation()) {
         logger.warning("javax.media.Manager is JMF's implementation, not FMJ's; is JMF ahead of FMJ in the classpath?");
         var0 = false;
      }

      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append("javax.media.Manager version: ");
      var2.append(Manager.getVersion());
      var1.info(var2.toString());
      return var0;
   }

   public static boolean checkFMJPrefixInPackageManager() {
      if (!PackageManager.getContentPrefixList().contains("net.sf.fmj")) {
         return false;
      } else {
         return PackageManager.getProtocolPrefixList().contains("net.sf.fmj");
      }
   }

   public static boolean checkJMFInClassPath() {
      try {
         Class.forName("com.sun.media.BasicClock");
         Class.forName("com.sun.media.BasicCodec");
         Class.forName("com.sun.media.BasicConnector");
         return true;
      } catch (Exception var1) {
         return false;
      }
   }

   public static boolean checkManagerImplementation() {
      try {
         Manager.class.getField("FMJ_TAG");
         return true;
      } catch (Exception var1) {
         return false;
      }
   }
}
