package javax.jmdns.impl;

import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface NameRegister {
   boolean checkName(InetAddress var1, String var2, NameRegister.NameType var3);

   String incrementName(InetAddress var1, String var2, NameRegister.NameType var3);

   void register(InetAddress var1, String var2, NameRegister.NameType var3);

   public abstract static class BaseRegister implements NameRegister {
      protected String incrementNameWithDash(String var1) {
         StringBuilder var5 = new StringBuilder(var1.length() + 5);
         int var3 = var1.indexOf(".local.");
         int var4 = var1.lastIndexOf(45);
         int var2;
         if (var4 < 0) {
            var2 = 1;
            var5.append(var1.substring(0, var3));
         } else {
            try {
               var2 = Integer.parseInt(var1.substring(var4 + 1, var3)) + 1;
               var5.append(var1.substring(0, var4));
            } catch (Exception var7) {
               var2 = 1;
               var5.append(var1.substring(0, var3));
            }
         }

         var5.append('-');
         var5.append(var2);
         var5.append(".local.");
         return var5.toString();
      }

      protected String incrementNameWithParentesis(String var1) {
         StringBuilder var4 = new StringBuilder(var1.length() + 5);
         int var2 = var1.lastIndexOf(40);
         int var3 = var1.lastIndexOf(41);
         if (var2 >= 0 && var2 < var3) {
            try {
               var4.append(var1.substring(0, var2));
               var4.append('(');
               var4.append(Integer.parseInt(var1.substring(var2 + 1, var3)) + 1);
               var4.append(')');
            } catch (NumberFormatException var6) {
               var4.setLength(0);
               var4.append(var1);
               var4.append(" (2)");
            }
         } else {
            var4.append(var1);
            var4.append(" (2)");
         }

         return var4.toString();
      }
   }

   public static class Factory {
      private static volatile NameRegister _register;

      public static NameRegister getRegistry() {
         if (_register == null) {
            _register = new NameRegister.UniqueNamePerInterface();
         }

         return _register;
      }

      public static void setRegistry(NameRegister var0) throws IllegalStateException {
         if (_register == null) {
            if (var0 != null) {
               _register = var0;
            }

         } else {
            throw new IllegalStateException("The register can only be set once.");
         }
      }
   }

   public static enum NameType {
      HOST,
      SERVICE;

      static {
         NameRegister.NameType var0 = new NameRegister.NameType("SERVICE", 1);
         SERVICE = var0;
      }
   }

   public static class UniqueNameAcrossInterface extends NameRegister.BaseRegister {
      public boolean checkName(InetAddress var1, String var2, NameRegister.NameType var3) {
         int var4 = null.$SwitchMap$javax$jmdns$impl$NameRegister$NameType[var3.ordinal()];
         return false;
      }

      public String incrementName(InetAddress var1, String var2, NameRegister.NameType var3) {
         int var4 = null.$SwitchMap$javax$jmdns$impl$NameRegister$NameType[var3.ordinal()];
         if (var4 != 1) {
            return var4 != 2 ? var2 : this.incrementNameWithParentesis(var2);
         } else {
            return this.incrementNameWithDash(var2);
         }
      }

      public void register(InetAddress var1, String var2, NameRegister.NameType var3) {
         int var4 = null.$SwitchMap$javax$jmdns$impl$NameRegister$NameType[var3.ordinal()];
      }
   }

   public static class UniqueNamePerInterface extends NameRegister.BaseRegister {
      private final ConcurrentMap _hostNames = new ConcurrentHashMap();
      private final ConcurrentMap _serviceNames = new ConcurrentHashMap();

      public boolean checkName(InetAddress var1, String var2, NameRegister.NameType var3) {
         int var4 = null.$SwitchMap$javax$jmdns$impl$NameRegister$NameType[var3.ordinal()];
         boolean var7 = false;
         boolean var6 = false;
         boolean var5;
         if (var4 != 1) {
            if (var4 != 2) {
               return false;
            } else {
               Set var9 = (Set)this._serviceNames.get(var1);
               var5 = var6;
               if (var9 != null) {
                  var5 = var6;
                  if (var9.contains(var2)) {
                     var5 = true;
                  }
               }

               return var5;
            }
         } else {
            String var8 = (String)this._hostNames.get(var1);
            var5 = var7;
            if (var8 != null) {
               var5 = var7;
               if (var8.equals(var2)) {
                  var5 = true;
               }
            }

            return var5;
         }
      }

      public String incrementName(InetAddress var1, String var2, NameRegister.NameType var3) {
         int var4 = null.$SwitchMap$javax$jmdns$impl$NameRegister$NameType[var3.ordinal()];
         if (var4 != 1) {
            return var4 != 2 ? var2 : this.incrementNameWithParentesis(var2);
         } else {
            return this.incrementNameWithDash(var2);
         }
      }

      public void register(InetAddress var1, String var2, NameRegister.NameType var3) {
         int var4 = null.$SwitchMap$javax$jmdns$impl$NameRegister$NameType[var3.ordinal()];
      }
   }
}
