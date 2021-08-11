package javax.jmdns;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import javax.jmdns.impl.JmDNSImpl;

public abstract class JmDNS implements Closeable {
   public static String VERSION;

   static {
      label52: {
         label51: {
            InputStream var0;
            boolean var10001;
            try {
               var0 = JmDNS.class.getClassLoader().getResourceAsStream("version.properties");
            } catch (Exception var8) {
               var10001 = false;
               break label51;
            }

            try {
               Properties var1 = new Properties();
               var1.load(var0);
               VERSION = var1.getProperty("jmdns.version");
               break label52;
            } finally {
               label45:
               try {
                  var0.close();
               } catch (Exception var6) {
                  var10001 = false;
                  break label45;
               }
            }
         }

         VERSION = "VERSION MISSING";
         return;
      }

   }

   public static JmDNS create() throws IOException {
      return new JmDNSImpl((InetAddress)null, (String)null);
   }

   public static JmDNS create(String var0) throws IOException {
      return new JmDNSImpl((InetAddress)null, var0);
   }

   public static JmDNS create(InetAddress var0) throws IOException {
      return new JmDNSImpl(var0, (String)null);
   }

   public static JmDNS create(InetAddress var0, String var1) throws IOException {
      return new JmDNSImpl(var0, var1);
   }

   public abstract void addServiceListener(String var1, ServiceListener var2);

   public abstract void addServiceTypeListener(ServiceTypeListener var1) throws IOException;

   public abstract JmDNS.Delegate getDelegate();

   public abstract String getHostName();

   public abstract InetAddress getInetAddress() throws IOException;

   @Deprecated
   public abstract InetAddress getInterface() throws IOException;

   public abstract String getName();

   public abstract ServiceInfo getServiceInfo(String var1, String var2);

   public abstract ServiceInfo getServiceInfo(String var1, String var2, long var3);

   public abstract ServiceInfo getServiceInfo(String var1, String var2, boolean var3);

   public abstract ServiceInfo getServiceInfo(String var1, String var2, boolean var3, long var4);

   public abstract ServiceInfo[] list(String var1);

   public abstract ServiceInfo[] list(String var1, long var2);

   public abstract Map listBySubtype(String var1);

   public abstract Map listBySubtype(String var1, long var2);

   @Deprecated
   public abstract void printServices();

   public abstract void registerService(ServiceInfo var1) throws IOException;

   public abstract boolean registerServiceType(String var1);

   public abstract void removeServiceListener(String var1, ServiceListener var2);

   public abstract void removeServiceTypeListener(ServiceTypeListener var1);

   public abstract void requestServiceInfo(String var1, String var2);

   public abstract void requestServiceInfo(String var1, String var2, long var3);

   public abstract void requestServiceInfo(String var1, String var2, boolean var3);

   public abstract void requestServiceInfo(String var1, String var2, boolean var3, long var4);

   public abstract JmDNS.Delegate setDelegate(JmDNS.Delegate var1);

   public abstract void unregisterAllServices();

   public abstract void unregisterService(ServiceInfo var1);

   public interface Delegate {
      void cannotRecoverFromIOError(JmDNS var1, Collection var2);
   }
}
