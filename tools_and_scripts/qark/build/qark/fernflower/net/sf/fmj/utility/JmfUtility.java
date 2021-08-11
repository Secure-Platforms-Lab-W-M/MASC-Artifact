package net.sf.fmj.utility;

public class JmfUtility {
   public static boolean enableLogging() {
      try {
         Class.forName("com.sun.media.util.Registry").getMethod("set", String.class, Object.class).invoke((Object)null, "allowLogging", true);
         return true;
      } catch (Exception var1) {
         return false;
      }
   }
}
