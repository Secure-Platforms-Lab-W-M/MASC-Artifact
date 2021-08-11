package net.sf.fmj.ejmf.toolkit.install;

import java.util.Vector;
import javax.media.PackageManager;

public class PackageUtility {
   public static void addContentPrefix(String var0) {
      addContentPrefix(var0, false);
   }

   public static void addContentPrefix(String var0, boolean var1) {
      Vector var2 = PackageManager.getContentPrefixList();
      if (!var2.contains(var0)) {
         var2.addElement(var0);
         PackageManager.setContentPrefixList(var2);
         if (var1) {
            PackageManager.commitContentPrefixList();
         }
      }

   }

   public static void addProtocolPrefix(String var0) {
      addProtocolPrefix(var0, false);
   }

   public static void addProtocolPrefix(String var0, boolean var1) {
      Vector var2 = PackageManager.getProtocolPrefixList();
      if (!var2.contains(var0)) {
         var2.addElement(var0);
         PackageManager.setProtocolPrefixList(var2);
         if (var1) {
            PackageManager.commitProtocolPrefixList();
         }
      }

   }

   public static void removeContentPrefix(String var0) {
      removeContentPrefix(var0, false);
   }

   public static void removeContentPrefix(String var0, boolean var1) {
      Vector var2 = PackageManager.getContentPrefixList();
      if (var2.contains(var0)) {
         var2.removeElement(var0);
         PackageManager.setContentPrefixList(var2);
         if (var1) {
            PackageManager.commitContentPrefixList();
         }
      }

   }

   public static void removeProtocolPrefix(String var0) {
      removeProtocolPrefix(var0, false);
   }

   public static void removeProtocolPrefix(String var0, boolean var1) {
      Vector var2 = PackageManager.getProtocolPrefixList();
      if (var2.contains(var0)) {
         var2.removeElement(var0);
         PackageManager.setProtocolPrefixList(var2);
         if (var1) {
            PackageManager.commitProtocolPrefixList();
         }
      }

   }
}
