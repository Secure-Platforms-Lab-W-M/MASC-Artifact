package org.apache.commons.text.lookup;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

final class JavaPlatformStringLookup extends AbstractStringLookup {
   static final JavaPlatformStringLookup INSTANCE = new JavaPlatformStringLookup();
   private static final String KEY_HARDWARE = "hardware";
   private static final String KEY_LOCALE = "locale";
   private static final String KEY_OS = "os";
   private static final String KEY_RUNTIME = "runtime";
   private static final String KEY_VERSION = "version";
   private static final String KEY_VM = "vm";

   private JavaPlatformStringLookup() {
   }

   private String getSystemProperty(String var1) {
      return SystemPropertyStringLookup.INSTANCE.lookup(var1);
   }

   private String getSystemProperty(String var1, String var2) {
      var2 = this.getSystemProperty(var2);
      if (StringUtils.isEmpty(var2)) {
         return "";
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(var2);
         return var3.toString();
      }
   }

   public static void main(String[] var0) {
      System.out.println(JavaPlatformStringLookup.class);
      System.out.printf("%s = %s%n", "version", INSTANCE.lookup("version"));
      System.out.printf("%s = %s%n", "runtime", INSTANCE.lookup("runtime"));
      System.out.printf("%s = %s%n", "vm", INSTANCE.lookup("vm"));
      System.out.printf("%s = %s%n", "os", INSTANCE.lookup("os"));
      System.out.printf("%s = %s%n", "hardware", INSTANCE.lookup("hardware"));
      System.out.printf("%s = %s%n", "locale", INSTANCE.lookup("locale"));
   }

   String getHardware() {
      StringBuilder var1 = new StringBuilder();
      var1.append("processors: ");
      var1.append(Runtime.getRuntime().availableProcessors());
      var1.append(", architecture: ");
      var1.append(this.getSystemProperty("os.arch"));
      var1.append(this.getSystemProperty("-", "sun.arch.data.model"));
      var1.append(this.getSystemProperty(", instruction sets: ", "sun.cpu.isalist"));
      return var1.toString();
   }

   String getLocale() {
      StringBuilder var1 = new StringBuilder();
      var1.append("default locale: ");
      var1.append(Locale.getDefault());
      var1.append(", platform encoding: ");
      var1.append(this.getSystemProperty("file.encoding"));
      return var1.toString();
   }

   String getOperatingSystem() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getSystemProperty("os.name"));
      var1.append(" ");
      var1.append(this.getSystemProperty("os.version"));
      var1.append(this.getSystemProperty(" ", "sun.os.patch.level"));
      var1.append(", architecture: ");
      var1.append(this.getSystemProperty("os.arch"));
      var1.append(this.getSystemProperty("-", "sun.arch.data.model"));
      return var1.toString();
   }

   String getRuntime() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getSystemProperty("java.runtime.name"));
      var1.append(" (build ");
      var1.append(this.getSystemProperty("java.runtime.version"));
      var1.append(") from ");
      var1.append(this.getSystemProperty("java.vendor"));
      return var1.toString();
   }

   String getVirtualMachine() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getSystemProperty("java.vm.name"));
      var1.append(" (build ");
      var1.append(this.getSystemProperty("java.vm.version"));
      var1.append(", ");
      var1.append(this.getSystemProperty("java.vm.info"));
      var1.append(")");
      return var1.toString();
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         byte var2 = -1;
         switch(var1.hashCode()) {
         case -1097462182:
            if (var1.equals("locale")) {
               var2 = 5;
            }
            break;
         case 3556:
            if (var1.equals("os")) {
               var2 = 3;
            }
            break;
         case 3767:
            if (var1.equals("vm")) {
               var2 = 2;
            }
            break;
         case 116909544:
            if (var1.equals("hardware")) {
               var2 = 4;
            }
            break;
         case 351608024:
            if (var1.equals("version")) {
               var2 = 0;
            }
            break;
         case 1550962648:
            if (var1.equals("runtime")) {
               var2 = 1;
            }
         }

         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        if (var2 == 5) {
                           return this.getLocale();
                        } else {
                           throw new IllegalArgumentException(var1);
                        }
                     } else {
                        return this.getHardware();
                     }
                  } else {
                     return this.getOperatingSystem();
                  }
               } else {
                  return this.getVirtualMachine();
               }
            } else {
               return this.getRuntime();
            }
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Java version ");
            var3.append(this.getSystemProperty("java.version"));
            return var3.toString();
         }
      }
   }
}
