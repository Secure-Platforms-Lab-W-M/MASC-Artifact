package org.apache.commons.text.lookup;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class ResourceBundleStringLookup extends AbstractStringLookup {
   static final ResourceBundleStringLookup INSTANCE = new ResourceBundleStringLookup();
   private final String bundleName;

   private ResourceBundleStringLookup() {
      this((String)null);
   }

   ResourceBundleStringLookup(String var1) {
      this.bundleName = var1;
   }

   public String lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         String[] var4 = var1.split(SPLIT_STR);
         int var3 = var4.length;
         boolean var2;
         if (this.bundleName == null) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2 && var3 != 2) {
            throw IllegalArgumentExceptions.format("Bad resource bundle key format [%s]; expected format is BundleName:KeyName.", var1);
         } else if (this.bundleName != null && var3 != 1) {
            throw IllegalArgumentExceptions.format("Bad resource bundle key format [%s]; expected format is KeyName.", var1);
         } else {
            if (var2) {
               var1 = var4[0];
            } else {
               var1 = this.bundleName;
            }

            String var8;
            if (var2) {
               var8 = var4[1];
            } else {
               var8 = var4[0];
            }

            try {
               String var5 = ResourceBundle.getBundle(var1).getString(var8);
               return var5;
            } catch (MissingResourceException var6) {
               return null;
            } catch (Exception var7) {
               throw IllegalArgumentExceptions.format(var7, "Error looking up resource bundle [%s] and key [%s].", var1, var8);
            }
         }
      }
   }
}
