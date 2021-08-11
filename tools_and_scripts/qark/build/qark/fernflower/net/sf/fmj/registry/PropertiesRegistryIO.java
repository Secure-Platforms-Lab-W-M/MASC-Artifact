package net.sf.fmj.registry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

class PropertiesRegistryIO implements RegistryIO {
   private static final int MAX = 100;
   final String CONTENT_PREFIX_STRING = "content-prefix";
   final String[] PLUGIN_TYPE_STRINGS = new String[]{"demux", "codec", "effect", "renderer", "mux"};
   final String PROTOCOL_PREFIX_STRING = "protocol-prefix";
   private final RegistryContents contents;

   public PropertiesRegistryIO(RegistryContents var1) {
      this.contents = var1;
   }

   private void fromProperties(Properties var1) {
      int var2;
      for(var2 = 0; var2 < this.contents.plugins.length; ++var2) {
         String var4 = this.PLUGIN_TYPE_STRINGS[var2];
         Vector var5 = this.contents.plugins[var2];

         for(int var3 = 0; var3 < 100; ++var3) {
            StringBuilder var6 = new StringBuilder();
            var6.append(var4);
            var6.append(var3);
            String var10 = var1.getProperty(var6.toString());
            if (var10 != null && !var10.equals("")) {
               var5.add(var10);
            }
         }
      }

      Vector var7 = this.contents.contentPrefixList;

      StringBuilder var8;
      String var9;
      for(var2 = 0; var2 < 100; ++var2) {
         var8 = new StringBuilder();
         var8.append("content-prefix");
         var8.append(var2);
         var9 = var1.getProperty(var8.toString());
         if (var9 != null && !var9.equals("")) {
            var7.add(var9);
         }
      }

      var7 = this.contents.protocolPrefixList;

      for(var2 = 0; var2 < 100; ++var2) {
         var8 = new StringBuilder();
         var8.append("protocol-prefix");
         var8.append(var2);
         var9 = var1.getProperty(var8.toString());
         if (var9 != null && !var9.equals("")) {
            var7.add(var9);
         }
      }

   }

   private Properties toProperties() {
      Properties var3 = new Properties();

      int var1;
      for(var1 = 0; var1 < this.contents.plugins.length; ++var1) {
         String var4 = this.PLUGIN_TYPE_STRINGS[var1];
         Vector var5 = this.contents.plugins[var1];

         for(int var2 = 0; var2 < var5.size(); ++var2) {
            StringBuilder var6 = new StringBuilder();
            var6.append(var4);
            var6.append(var2);
            var3.setProperty(var6.toString(), (String)var5.get(var2));
         }
      }

      Vector var7 = this.contents.contentPrefixList;

      StringBuilder var8;
      for(var1 = 0; var1 < var7.size(); ++var1) {
         var8 = new StringBuilder();
         var8.append("content-prefix");
         var8.append(var1);
         var3.setProperty(var8.toString(), (String)var7.get(var1));
      }

      var7 = this.contents.protocolPrefixList;

      for(var1 = 0; var1 < var7.size(); ++var1) {
         var8 = new StringBuilder();
         var8.append("protocol-prefix");
         var8.append(var1);
         var3.setProperty(var8.toString(), (String)var7.get(var1));
      }

      return var3;
   }

   public void load(InputStream var1) throws IOException {
      Properties var2 = new Properties();
      var2.load(var1);
      this.fromProperties(var2);
   }

   public void write(OutputStream var1) throws IOException {
      this.toProperties().store(var1, "FMJ registry");
   }
}
