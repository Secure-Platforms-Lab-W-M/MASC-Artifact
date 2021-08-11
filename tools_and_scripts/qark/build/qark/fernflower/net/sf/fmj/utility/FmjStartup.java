package net.sf.fmj.utility;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import net.sf.fmj.media.RegistryDefaults;
import org.atalk.android.util.javax.swing.UIManager;

public class FmjStartup {
   private static boolean initialized;
   public static boolean isApplet;
   private static final Logger logger;

   static {
      logger = LoggerSingleton.logger;
      initialized = false;
      isApplet = false;
   }

   public static final void init() {
      if (!initialized) {
         Logger var0 = logger;
         StringBuilder var1 = new StringBuilder();
         var1.append("OS: ");
         var1.append(System.getProperty("os.name"));
         var0.info(var1.toString());
         System.setProperty("java.util.logging.config.file", "logging.properties");

         Level var2;
         StringBuilder var3;
         Logger var6;
         try {
            LogManager.getLogManager().readConfiguration();
         } catch (Exception var5) {
            var6 = logger;
            var2 = Level.SEVERE;
            var3 = new StringBuilder();
            var3.append("Unable to read logging configuration: ");
            var3.append(var5);
            var6.log(var2, var3.toString(), var5);
            PrintStream var7 = System.err;
            StringBuilder var8 = new StringBuilder();
            var8.append("Unable to read logging configuration: ");
            var8.append(var5);
            var7.println(var8.toString());
            var5.printStackTrace();
         }

         if (!ClasspathChecker.checkAndWarn()) {
            logger.info("Enabling JMF logging");
            if (!JmfUtility.enableLogging()) {
               logger.warning("Failed to enable JMF logging");
            }

            logger.info("Registering FMJ prefixes and plugins with JMF");
            RegistryDefaults.registerAll(10);
         }

         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception var4) {
            var6 = logger;
            var2 = Level.WARNING;
            var3 = new StringBuilder();
            var3.append("");
            var3.append(var4);
            var6.log(var2, var3.toString(), var4);
         }

         initialized = true;
      }
   }

   public static final void initApplet() {
      if (!initialized) {
         if (!ClasspathChecker.checkAndWarn()) {
            logger.info("Enabling JMF logging");
            if (!JmfUtility.enableLogging()) {
               logger.warning("Failed to enable JMF logging");
            }

            logger.info("Registering FMJ prefixes and plugins with JMF");
            RegistryDefaults.registerAll(2);
         }

         initialized = true;
      }
   }
}
