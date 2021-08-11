package org.apache.commons.lang3;

import java.io.File;

public class SystemUtils {
   public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");
   public static final String FILE_ENCODING = getSystemProperty("file.encoding");
   @Deprecated
   public static final String FILE_SEPARATOR = getSystemProperty("file.separator");
   public static final boolean IS_JAVA_10;
   public static final boolean IS_JAVA_11;
   public static final boolean IS_JAVA_12;
   public static final boolean IS_JAVA_13;
   public static final boolean IS_JAVA_1_1;
   public static final boolean IS_JAVA_1_2;
   public static final boolean IS_JAVA_1_3;
   public static final boolean IS_JAVA_1_4;
   public static final boolean IS_JAVA_1_5;
   public static final boolean IS_JAVA_1_6;
   public static final boolean IS_JAVA_1_7;
   public static final boolean IS_JAVA_1_8;
   @Deprecated
   public static final boolean IS_JAVA_1_9;
   public static final boolean IS_JAVA_9;
   public static final boolean IS_OS_400;
   public static final boolean IS_OS_AIX;
   public static final boolean IS_OS_FREE_BSD;
   public static final boolean IS_OS_HP_UX;
   public static final boolean IS_OS_IRIX;
   public static final boolean IS_OS_LINUX;
   public static final boolean IS_OS_MAC;
   public static final boolean IS_OS_MAC_OSX;
   public static final boolean IS_OS_MAC_OSX_CHEETAH;
   public static final boolean IS_OS_MAC_OSX_EL_CAPITAN;
   public static final boolean IS_OS_MAC_OSX_JAGUAR;
   public static final boolean IS_OS_MAC_OSX_LEOPARD;
   public static final boolean IS_OS_MAC_OSX_LION;
   public static final boolean IS_OS_MAC_OSX_MAVERICKS;
   public static final boolean IS_OS_MAC_OSX_MOUNTAIN_LION;
   public static final boolean IS_OS_MAC_OSX_PANTHER;
   public static final boolean IS_OS_MAC_OSX_PUMA;
   public static final boolean IS_OS_MAC_OSX_SNOW_LEOPARD;
   public static final boolean IS_OS_MAC_OSX_TIGER;
   public static final boolean IS_OS_MAC_OSX_YOSEMITE;
   public static final boolean IS_OS_NET_BSD;
   public static final boolean IS_OS_OPEN_BSD;
   public static final boolean IS_OS_OS2;
   public static final boolean IS_OS_SOLARIS;
   public static final boolean IS_OS_SUN_OS;
   public static final boolean IS_OS_UNIX;
   public static final boolean IS_OS_WINDOWS;
   public static final boolean IS_OS_WINDOWS_10;
   public static final boolean IS_OS_WINDOWS_2000;
   public static final boolean IS_OS_WINDOWS_2003;
   public static final boolean IS_OS_WINDOWS_2008;
   public static final boolean IS_OS_WINDOWS_2012;
   public static final boolean IS_OS_WINDOWS_7;
   public static final boolean IS_OS_WINDOWS_8;
   public static final boolean IS_OS_WINDOWS_95;
   public static final boolean IS_OS_WINDOWS_98;
   public static final boolean IS_OS_WINDOWS_ME;
   public static final boolean IS_OS_WINDOWS_NT;
   public static final boolean IS_OS_WINDOWS_VISTA;
   public static final boolean IS_OS_WINDOWS_XP;
   public static final boolean IS_OS_ZOS;
   public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");
   public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");
   public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");
   public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");
   public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");
   public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");
   public static final String JAVA_COMPILER = getSystemProperty("java.compiler");
   public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");
   public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");
   public static final String JAVA_HOME = getSystemProperty("java.home");
   private static final String JAVA_HOME_KEY = "java.home";
   public static final String JAVA_IO_TMPDIR = getSystemProperty("java.io.tmpdir");
   private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
   public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");
   public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
   public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
   public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");
   public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");
   public static final String JAVA_SPECIFICATION_VERSION;
   private static final JavaVersion JAVA_SPECIFICATION_VERSION_AS_ENUM;
   public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY;
   public static final String JAVA_VENDOR;
   public static final String JAVA_VENDOR_URL;
   public static final String JAVA_VERSION;
   public static final String JAVA_VM_INFO;
   public static final String JAVA_VM_NAME;
   public static final String JAVA_VM_SPECIFICATION_NAME;
   public static final String JAVA_VM_SPECIFICATION_VENDOR;
   public static final String JAVA_VM_SPECIFICATION_VERSION;
   public static final String JAVA_VM_VENDOR;
   public static final String JAVA_VM_VERSION;
   @Deprecated
   public static final String LINE_SEPARATOR;
   public static final String OS_ARCH;
   public static final String OS_NAME;
   private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
   public static final String OS_VERSION;
   @Deprecated
   public static final String PATH_SEPARATOR;
   public static final String USER_COUNTRY;
   public static final String USER_DIR;
   private static final String USER_DIR_KEY = "user.dir";
   public static final String USER_HOME;
   private static final String USER_HOME_KEY = "user.home";
   public static final String USER_LANGUAGE;
   public static final String USER_NAME;
   public static final String USER_TIMEZONE;

   static {
      String var2 = getSystemProperty("java.specification.version");
      JAVA_SPECIFICATION_VERSION = var2;
      JAVA_SPECIFICATION_VERSION_AS_ENUM = JavaVersion.get(var2);
      JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");
      JAVA_VENDOR = getSystemProperty("java.vendor");
      JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");
      JAVA_VERSION = getSystemProperty("java.version");
      JAVA_VM_INFO = getSystemProperty("java.vm.info");
      JAVA_VM_NAME = getSystemProperty("java.vm.name");
      JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");
      JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");
      JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");
      JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");
      JAVA_VM_VERSION = getSystemProperty("java.vm.version");
      LINE_SEPARATOR = getSystemProperty("line.separator");
      OS_ARCH = getSystemProperty("os.arch");
      OS_NAME = getSystemProperty("os.name");
      OS_VERSION = getSystemProperty("os.version");
      PATH_SEPARATOR = getSystemProperty("path.separator");
      var2 = "user.country";
      if (getSystemProperty("user.country") == null) {
         var2 = "user.region";
      }

      USER_COUNTRY = getSystemProperty(var2);
      USER_DIR = getSystemProperty("user.dir");
      USER_HOME = getSystemProperty("user.home");
      USER_LANGUAGE = getSystemProperty("user.language");
      USER_NAME = getSystemProperty("user.name");
      USER_TIMEZONE = getSystemProperty("user.timezone");
      IS_JAVA_1_1 = getJavaVersionMatches("1.1");
      IS_JAVA_1_2 = getJavaVersionMatches("1.2");
      IS_JAVA_1_3 = getJavaVersionMatches("1.3");
      IS_JAVA_1_4 = getJavaVersionMatches("1.4");
      IS_JAVA_1_5 = getJavaVersionMatches("1.5");
      IS_JAVA_1_6 = getJavaVersionMatches("1.6");
      IS_JAVA_1_7 = getJavaVersionMatches("1.7");
      IS_JAVA_1_8 = getJavaVersionMatches("1.8");
      IS_JAVA_1_9 = getJavaVersionMatches("9");
      IS_JAVA_9 = getJavaVersionMatches("9");
      IS_JAVA_10 = getJavaVersionMatches("10");
      IS_JAVA_11 = getJavaVersionMatches("11");
      IS_JAVA_12 = getJavaVersionMatches("12");
      IS_JAVA_13 = getJavaVersionMatches("13");
      IS_OS_AIX = getOsMatchesName("AIX");
      IS_OS_HP_UX = getOsMatchesName("HP-UX");
      IS_OS_400 = getOsMatchesName("OS/400");
      IS_OS_IRIX = getOsMatchesName("Irix");
      boolean var0 = getOsMatchesName("Linux");
      boolean var1 = false;
      if (!var0 && !getOsMatchesName("LINUX")) {
         var0 = false;
      } else {
         var0 = true;
      }

      label35: {
         IS_OS_LINUX = var0;
         IS_OS_MAC = getOsMatchesName("Mac");
         IS_OS_MAC_OSX = getOsMatchesName("Mac OS X");
         IS_OS_MAC_OSX_CHEETAH = getOsMatches("Mac OS X", "10.0");
         IS_OS_MAC_OSX_PUMA = getOsMatches("Mac OS X", "10.1");
         IS_OS_MAC_OSX_JAGUAR = getOsMatches("Mac OS X", "10.2");
         IS_OS_MAC_OSX_PANTHER = getOsMatches("Mac OS X", "10.3");
         IS_OS_MAC_OSX_TIGER = getOsMatches("Mac OS X", "10.4");
         IS_OS_MAC_OSX_LEOPARD = getOsMatches("Mac OS X", "10.5");
         IS_OS_MAC_OSX_SNOW_LEOPARD = getOsMatches("Mac OS X", "10.6");
         IS_OS_MAC_OSX_LION = getOsMatches("Mac OS X", "10.7");
         IS_OS_MAC_OSX_MOUNTAIN_LION = getOsMatches("Mac OS X", "10.8");
         IS_OS_MAC_OSX_MAVERICKS = getOsMatches("Mac OS X", "10.9");
         IS_OS_MAC_OSX_YOSEMITE = getOsMatches("Mac OS X", "10.10");
         IS_OS_MAC_OSX_EL_CAPITAN = getOsMatches("Mac OS X", "10.11");
         IS_OS_FREE_BSD = getOsMatchesName("FreeBSD");
         IS_OS_OPEN_BSD = getOsMatchesName("OpenBSD");
         IS_OS_NET_BSD = getOsMatchesName("NetBSD");
         IS_OS_OS2 = getOsMatchesName("OS/2");
         IS_OS_SOLARIS = getOsMatchesName("Solaris");
         var0 = getOsMatchesName("SunOS");
         IS_OS_SUN_OS = var0;
         if (!IS_OS_AIX && !IS_OS_HP_UX && !IS_OS_IRIX && !IS_OS_LINUX && !IS_OS_MAC_OSX && !IS_OS_SOLARIS && !var0 && !IS_OS_FREE_BSD && !IS_OS_OPEN_BSD) {
            var0 = var1;
            if (!IS_OS_NET_BSD) {
               break label35;
            }
         }

         var0 = true;
      }

      IS_OS_UNIX = var0;
      IS_OS_WINDOWS = getOsMatchesName("Windows");
      IS_OS_WINDOWS_2000 = getOsMatchesName("Windows 2000");
      IS_OS_WINDOWS_2003 = getOsMatchesName("Windows 2003");
      IS_OS_WINDOWS_2008 = getOsMatchesName("Windows Server 2008");
      IS_OS_WINDOWS_2012 = getOsMatchesName("Windows Server 2012");
      IS_OS_WINDOWS_95 = getOsMatchesName("Windows 95");
      IS_OS_WINDOWS_98 = getOsMatchesName("Windows 98");
      IS_OS_WINDOWS_ME = getOsMatchesName("Windows Me");
      IS_OS_WINDOWS_NT = getOsMatchesName("Windows NT");
      IS_OS_WINDOWS_XP = getOsMatchesName("Windows XP");
      IS_OS_WINDOWS_VISTA = getOsMatchesName("Windows Vista");
      IS_OS_WINDOWS_7 = getOsMatchesName("Windows 7");
      IS_OS_WINDOWS_8 = getOsMatchesName("Windows 8");
      IS_OS_WINDOWS_10 = getOsMatchesName("Windows 10");
      IS_OS_ZOS = getOsMatchesName("z/OS");
   }

   public static String getEnvironmentVariable(String var0, String var1) {
      try {
         var0 = System.getenv(var0);
      } catch (SecurityException var2) {
         return var1;
      }

      return var0 == null ? var1 : var0;
   }

   public static String getHostName() {
      String var0;
      if (IS_OS_WINDOWS) {
         var0 = "COMPUTERNAME";
      } else {
         var0 = "HOSTNAME";
      }

      return System.getenv(var0);
   }

   public static File getJavaHome() {
      return new File(System.getProperty("java.home"));
   }

   public static File getJavaIoTmpDir() {
      return new File(System.getProperty("java.io.tmpdir"));
   }

   private static boolean getJavaVersionMatches(String var0) {
      return isJavaVersionMatch(JAVA_SPECIFICATION_VERSION, var0);
   }

   private static boolean getOsMatches(String var0, String var1) {
      return isOSMatch(OS_NAME, OS_VERSION, var0, var1);
   }

   private static boolean getOsMatchesName(String var0) {
      return isOSNameMatch(OS_NAME, var0);
   }

   private static String getSystemProperty(String var0) {
      try {
         var0 = System.getProperty(var0);
         return var0;
      } catch (SecurityException var1) {
         return null;
      }
   }

   public static File getUserDir() {
      return new File(System.getProperty("user.dir"));
   }

   public static File getUserHome() {
      return new File(System.getProperty("user.home"));
   }

   public static boolean isJavaAwtHeadless() {
      return Boolean.TRUE.toString().equals(JAVA_AWT_HEADLESS);
   }

   public static boolean isJavaVersionAtLeast(JavaVersion var0) {
      return JAVA_SPECIFICATION_VERSION_AS_ENUM.atLeast(var0);
   }

   public static boolean isJavaVersionAtMost(JavaVersion var0) {
      return JAVA_SPECIFICATION_VERSION_AS_ENUM.atMost(var0);
   }

   static boolean isJavaVersionMatch(String var0, String var1) {
      return var0 == null ? false : var0.startsWith(var1);
   }

   static boolean isOSMatch(String var0, String var1, String var2, String var3) {
      boolean var5 = false;
      if (var0 != null) {
         if (var1 == null) {
            return false;
         } else {
            boolean var4 = var5;
            if (isOSNameMatch(var0, var2)) {
               var4 = var5;
               if (isOSVersionMatch(var1, var3)) {
                  var4 = true;
               }
            }

            return var4;
         }
      } else {
         return false;
      }
   }

   static boolean isOSNameMatch(String var0, String var1) {
      return var0 == null ? false : var0.startsWith(var1);
   }

   static boolean isOSVersionMatch(String var0, String var1) {
      if (StringUtils.isEmpty(var0)) {
         return false;
      } else {
         String[] var4 = var1.split("\\.");
         String[] var3 = var0.split("\\.");

         for(int var2 = 0; var2 < Math.min(var4.length, var3.length); ++var2) {
            if (!var4[var2].equals(var3[var2])) {
               return false;
            }
         }

         return true;
      }
   }
}
