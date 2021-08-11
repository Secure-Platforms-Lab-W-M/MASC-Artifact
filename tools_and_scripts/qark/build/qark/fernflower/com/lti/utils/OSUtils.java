package com.lti.utils;

import java.util.logging.Logger;

public final class OSUtils {
   private static final Logger logger;

   static {
      logger = Logger.global;
   }

   private OSUtils() {
      Logger var1 = logger;
      StringBuilder var2 = new StringBuilder();
      var2.append("OS: ");
      var2.append(System.getProperty("os.name"));
      var1.fine(var2.toString());
   }

   public static final boolean isLinux() {
      return System.getProperty("os.name").equals("Linux");
   }

   public static final boolean isMacOSX() {
      return System.getProperty("os.name").equals("Mac OS X");
   }

   public static final boolean isSolaris() {
      return System.getProperty("os.name").equals("SunOS");
   }

   public static final boolean isWindows() {
      return System.getProperty("os.name").startsWith("Windows");
   }
}
