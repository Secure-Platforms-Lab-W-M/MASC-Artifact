package dnsfilter;

import java.io.IOException;
import java.io.InputStream;
import util.ExecutionEnvironment;
import util.Logger;

class DNSFilterProxy$1StandaloneEnvironment extends ExecutionEnvironment {
   boolean debug = false;
   boolean debugInit = false;

   public boolean debug() {
      if (!this.debugInit) {
         try {
            this.debug = Boolean.parseBoolean(DNSFilterManager.getInstance().getConfig().getProperty("debug", "false"));
         } catch (IOException var2) {
            Logger.getLogger().logException(var2);
         }

         this.debugInit = true;
      }

      return this.debug;
   }

   public InputStream getAsset(String var1) throws IOException {
      return Thread.currentThread().getContextClassLoader().getResourceAsStream(var1);
   }

   public void onReload() {
      DNSFilterProxy.access$000(DNSFilterManager.getInstance());
   }
}
