package net.sf.fmj.utility;

import com.lti.utils.OSUtils;
import com.lti.utils.StringUtils;
import java.io.File;

public final class URLUtils {
   private URLUtils() {
   }

   public static String createAbsoluteFileUrl(String var0) {
      var0 = extractValidPathFromFileUrl(var0);
      return var0 == null ? null : createUrlStr(new File(var0));
   }

   public static String createUrlStr(File var0) {
      String var2 = var0.getAbsolutePath();
      String var3;
      if (var2.startsWith("/")) {
         var3 = "file://";
      } else {
         var3 = "file:///";
      }

      String var1 = var2;
      if (OSUtils.isWindows()) {
         var1 = var2.replaceAll("\\\\", "/");
      }

      StringBuilder var4 = new StringBuilder();
      var4.append(var3);
      var4.append(StringUtils.replaceSpecialUrlChars(var1, true));
      return var4.toString();
   }

   private static boolean exists(String var0, boolean var1) {
      File var2 = new File(var0);
      if (var1) {
         return var2.getParentFile() == null || var2.getParentFile().exists();
      } else {
         return var2.exists();
      }
   }

   public static String extractValidNewFilePathFromFileUrl(String var0) {
      return extractValidPathFromFileUrl(var0, true);
   }

   public static String extractValidPathFromFileUrl(String var0) {
      return extractValidPathFromFileUrl(var0, false);
   }

   private static String extractValidPathFromFileUrl(String var0, boolean var1) {
      if (!var0.startsWith("file:")) {
         return null;
      } else {
         var0 = StringUtils.restoreSpecialURLChars(var0.substring("file:".length()));
         if (!var0.startsWith("/")) {
            return var0;
         } else {
            String var2;
            if (var0.startsWith("//")) {
               var2 = var0.substring(2);
               if (exists(var2, var1)) {
                  return windowsSafe(var2);
               }
            }

            while(var0.startsWith("//")) {
               var0 = var0.substring(1);
            }

            var2 = var0;
            if (exists(var0, var1)) {
               return windowsSafe(var0);
            } else {
               do {
                  if (!var2.startsWith("/")) {
                     return null;
                  }

                  var0 = var2.substring(1);
                  var2 = var0;
               } while(!exists(var0, var1));

               return windowsSafe(var0);
            }
         }
      }
   }

   private static String windowsSafe(String var0) {
      return OSUtils.isWindows() && var0.startsWith("/") ? var0.substring(1) : var0;
   }
}
