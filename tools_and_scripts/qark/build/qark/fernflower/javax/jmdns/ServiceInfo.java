package javax.jmdns;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Map;
import javax.jmdns.impl.ServiceInfoImpl;

public abstract class ServiceInfo implements Cloneable {
   public static ServiceInfo create(String var0, String var1, int var2, int var3, int var4, String var5) {
      return new ServiceInfoImpl(var0, var1, "", var2, var3, var4, false, var5);
   }

   public static ServiceInfo create(String var0, String var1, int var2, int var3, int var4, Map var5) {
      return new ServiceInfoImpl(var0, var1, "", var2, var3, var4, false, var5);
   }

   public static ServiceInfo create(String var0, String var1, int var2, int var3, int var4, boolean var5, String var6) {
      return new ServiceInfoImpl(var0, var1, "", var2, var3, var4, var5, var6);
   }

   public static ServiceInfo create(String var0, String var1, int var2, int var3, int var4, boolean var5, Map var6) {
      return new ServiceInfoImpl(var0, var1, "", var2, var3, var4, var5, var6);
   }

   public static ServiceInfo create(String var0, String var1, int var2, int var3, int var4, boolean var5, byte[] var6) {
      return new ServiceInfoImpl(var0, var1, "", var2, var3, var4, var5, var6);
   }

   public static ServiceInfo create(String var0, String var1, int var2, int var3, int var4, byte[] var5) {
      return new ServiceInfoImpl(var0, var1, "", var2, var3, var4, false, var5);
   }

   public static ServiceInfo create(String var0, String var1, int var2, String var3) {
      return new ServiceInfoImpl(var0, var1, "", var2, 0, 0, false, var3);
   }

   public static ServiceInfo create(String var0, String var1, String var2, int var3, int var4, int var5, String var6) {
      return new ServiceInfoImpl(var0, var1, var2, var3, var4, var5, false, var6);
   }

   public static ServiceInfo create(String var0, String var1, String var2, int var3, int var4, int var5, Map var6) {
      return new ServiceInfoImpl(var0, var1, var2, var3, var4, var5, false, var6);
   }

   public static ServiceInfo create(String var0, String var1, String var2, int var3, int var4, int var5, boolean var6, String var7) {
      return new ServiceInfoImpl(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static ServiceInfo create(String var0, String var1, String var2, int var3, int var4, int var5, boolean var6, Map var7) {
      return new ServiceInfoImpl(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static ServiceInfo create(String var0, String var1, String var2, int var3, int var4, int var5, boolean var6, byte[] var7) {
      return new ServiceInfoImpl(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static ServiceInfo create(String var0, String var1, String var2, int var3, int var4, int var5, byte[] var6) {
      return new ServiceInfoImpl(var0, var1, var2, var3, var4, var5, false, var6);
   }

   public static ServiceInfo create(String var0, String var1, String var2, int var3, String var4) {
      return new ServiceInfoImpl(var0, var1, var2, var3, 0, 0, false, var4);
   }

   public static ServiceInfo create(Map var0, int var1, int var2, int var3, boolean var4, Map var5) {
      return new ServiceInfoImpl(var0, var1, var2, var3, var4, var5);
   }

   public ServiceInfo clone() {
      try {
         ServiceInfo var1 = (ServiceInfo)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   @Deprecated
   public abstract InetAddress getAddress();

   public abstract String getApplication();

   public abstract String getDomain();

   @Deprecated
   public abstract String getHostAddress();

   public abstract String[] getHostAddresses();

   @Deprecated
   public abstract Inet4Address getInet4Address();

   public abstract Inet4Address[] getInet4Addresses();

   @Deprecated
   public abstract Inet6Address getInet6Address();

   public abstract Inet6Address[] getInet6Addresses();

   @Deprecated
   public abstract InetAddress getInetAddress();

   public abstract InetAddress[] getInetAddresses();

   public abstract String getKey();

   public abstract String getName();

   public abstract String getNiceTextString();

   public abstract int getPort();

   public abstract int getPriority();

   public abstract byte[] getPropertyBytes(String var1);

   public abstract Enumeration getPropertyNames();

   public abstract String getPropertyString(String var1);

   public abstract String getProtocol();

   public abstract String getQualifiedName();

   public abstract Map getQualifiedNameMap();

   public abstract String getServer();

   public abstract String getSubtype();

   public abstract byte[] getTextBytes();

   @Deprecated
   public abstract String getTextString();

   public abstract String getType();

   public abstract String getTypeWithSubtype();

   @Deprecated
   public abstract String getURL();

   @Deprecated
   public abstract String getURL(String var1);

   public abstract String[] getURLs();

   public abstract String[] getURLs(String var1);

   public abstract int getWeight();

   public abstract boolean hasData();

   public abstract boolean hasSameAddresses(ServiceInfo var1);

   public abstract boolean isPersistent();

   public abstract void setText(Map var1) throws IllegalStateException;

   public abstract void setText(byte[] var1) throws IllegalStateException;

   public static enum Fields {
      Application,
      Domain,
      Instance,
      Protocol,
      Subtype;

      static {
         ServiceInfo.Fields var0 = new ServiceInfo.Fields("Subtype", 4);
         Subtype = var0;
      }
   }
}
