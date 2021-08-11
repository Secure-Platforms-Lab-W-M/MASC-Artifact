package javax.jmdns.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.util.ByteWrangler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DNSRecord extends DNSEntry {
   private static Logger logger = LoggerFactory.getLogger(DNSRecord.class.getName());
   private long _created;
   private int _isStaleAndShouldBeRefreshedPercentage;
   private final int _randomStaleRefreshOffset;
   private InetAddress _source;
   private int _ttl;

   DNSRecord(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4, int var5) {
      super(var1, var2, var3, var4);
      this._ttl = var5;
      this._created = System.currentTimeMillis();
      var5 = (new Random()).nextInt(3);
      this._randomStaleRefreshOffset = var5;
      this._isStaleAndShouldBeRefreshedPercentage = var5 + 80;
   }

   abstract DNSOutgoing addAnswer(JmDNSImpl var1, DNSIncoming var2, InetAddress var3, int var4, DNSOutgoing var5) throws IOException;

   public boolean equals(Object var1) {
      return var1 instanceof DNSRecord && super.equals(var1) && this.sameValue((DNSRecord)var1);
   }

   long getExpirationTime(int var1) {
      return this._created + (long)var1 * (long)this._ttl * 10L;
   }

   public InetAddress getRecordSource() {
      return this._source;
   }

   int getRemainingTTL(long var1) {
      return (int)Math.max(0L, (this.getExpirationTime(100) - var1) / 1000L);
   }

   public abstract ServiceEvent getServiceEvent(JmDNSImpl var1);

   public ServiceInfo getServiceInfo() {
      return this.getServiceInfo(false);
   }

   public abstract ServiceInfo getServiceInfo(boolean var1);

   public int getTTL() {
      return this._ttl;
   }

   abstract boolean handleQuery(JmDNSImpl var1, long var2);

   abstract boolean handleResponse(JmDNSImpl var1);

   public void incrementRefreshPercentage() {
      int var1 = this._isStaleAndShouldBeRefreshedPercentage + 5;
      this._isStaleAndShouldBeRefreshedPercentage = var1;
      if (var1 > 100) {
         this._isStaleAndShouldBeRefreshedPercentage = 100;
      }

   }

   public boolean isExpired(long var1) {
      return this.getExpirationTime(100) <= var1;
   }

   public abstract boolean isSingleValued();

   public boolean isStale(long var1) {
      return this.getExpirationTime(50) <= var1;
   }

   public boolean isStaleAndShouldBeRefreshed(long var1) {
      return this.getExpirationTime(this._isStaleAndShouldBeRefreshedPercentage) <= var1;
   }

   void resetTTL(DNSRecord var1) {
      this._created = var1._created;
      this._ttl = var1._ttl;
      this._isStaleAndShouldBeRefreshedPercentage = this._randomStaleRefreshOffset + 80;
   }

   boolean sameType(DNSRecord var1) {
      return this.getRecordType() == var1.getRecordType();
   }

   abstract boolean sameValue(DNSRecord var1);

   public void setRecordSource(InetAddress var1) {
      this._source = var1;
   }

   public void setTTL(int var1) {
      this._ttl = var1;
   }

   void setWillExpireSoon(long var1) {
      this._created = var1;
      this._ttl = 1;
   }

   boolean suppressedBy(DNSIncoming param1) {
      // $FF: Couldn't be decompiled
   }

   boolean suppressedBy(DNSRecord var1) {
      return this.equals(var1) && var1._ttl > this._ttl / 2;
   }

   protected void toString(StringBuilder var1) {
      super.toString(var1);
      int var2 = this.getRemainingTTL(System.currentTimeMillis());
      var1.append(" ttl: '");
      var1.append(var2);
      var1.append('/');
      var1.append(this._ttl);
      var1.append('\'');
   }

   abstract void write(DNSOutgoing.MessageOutputStream var1);

   public abstract static class Address extends DNSRecord {
      private static Logger logger1 = LoggerFactory.getLogger(DNSRecord.Address.class.getName());
      InetAddress _addr;

      protected Address(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4, int var5, InetAddress var6) {
         super(var1, var2, var3, var4, var5);
         this._addr = var6;
      }

      protected Address(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4, int var5, byte[] var6) {
         super(var1, var2, var3, var4, var5);

         try {
            this._addr = InetAddress.getByAddress(var6);
         } catch (UnknownHostException var7) {
            logger1.warn("Address() exception ", var7);
         }
      }

      DNSOutgoing addAnswer(JmDNSImpl var1, DNSIncoming var2, InetAddress var3, int var4, DNSOutgoing var5) throws IOException {
         return var5;
      }

      InetAddress getAddress() {
         return this._addr;
      }

      public ServiceEvent getServiceEvent(JmDNSImpl var1) {
         ServiceInfo var2 = this.getServiceInfo(false);
         ((ServiceInfoImpl)var2).setDns(var1);
         return new ServiceEventImpl(var1, var2.getType(), var2.getName(), var2);
      }

      public ServiceInfo getServiceInfo(boolean var1) {
         return new ServiceInfoImpl(this.getQualifiedNameMap(), 0, 0, 0, var1, (byte[])null);
      }

      boolean handleQuery(JmDNSImpl var1, long var2) {
         if (var1.getLocalHost().conflictWithRecord(this)) {
            DNSRecord.Address var5 = var1.getLocalHost().getDNSAddressRecord(this.getRecordType(), this.isUnique(), DNSConstants.DNS_TTL);
            if (var5 != null) {
               int var4 = this.compareTo(var5);
               if (var4 == 0) {
                  logger1.debug("handleQuery() Ignoring an identical address query");
                  return false;
               }

               logger1.debug("handleQuery() Conflicting query detected.");
               if (var1.isProbing() && var4 > 0) {
                  var1.getLocalHost().incrementHostName();
                  var1.getCache().clear();
                  Iterator var6 = var1.getServices().values().iterator();

                  while(var6.hasNext()) {
                     ((ServiceInfoImpl)((ServiceInfo)var6.next())).revertState();
                  }
               }

               var1.revertState();
               return true;
            }
         }

         return false;
      }

      boolean handleResponse(JmDNSImpl var1) {
         if (!var1.getLocalHost().conflictWithRecord(this)) {
            return false;
         } else {
            logger1.debug("handleResponse() Denial detected");
            if (var1.isProbing()) {
               var1.getLocalHost().incrementHostName();
               var1.getCache().clear();
               Iterator var2 = var1.getServices().values().iterator();

               while(var2.hasNext()) {
                  ((ServiceInfoImpl)((ServiceInfo)var2.next())).revertState();
               }
            }

            var1.revertState();
            return true;
         }
      }

      public boolean isSingleValued() {
         return false;
      }

      boolean same(DNSRecord var1) {
         boolean var2 = var1 instanceof DNSRecord.Address;
         boolean var3 = false;
         if (!var2) {
            return false;
         } else {
            var2 = var3;
            if (this.sameName(var1)) {
               var2 = var3;
               if (this.sameValue(var1)) {
                  var2 = true;
               }
            }

            return var2;
         }
      }

      boolean sameName(DNSRecord var1) {
         return this.getName().equalsIgnoreCase(var1.getName());
      }

      boolean sameValue(DNSRecord var1) {
         try {
            if (!(var1 instanceof DNSRecord.Address)) {
               return false;
            } else {
               DNSRecord.Address var4 = (DNSRecord.Address)var1;
               if (this.getAddress() == null && var4.getAddress() != null) {
                  return false;
               } else {
                  boolean var2 = this.getAddress().equals(var4.getAddress());
                  return var2;
               }
            }
         } catch (Exception var3) {
            logger1.info("Failed to compare addresses of DNSRecords", var3);
            return false;
         }
      }

      protected void toByteArray(DataOutputStream var1) throws IOException {
         super.toByteArray(var1);
         byte[] var3 = this.getAddress().getAddress();

         for(int var2 = 0; var2 < var3.length; ++var2) {
            var1.writeByte(var3[var2]);
         }

      }

      protected void toString(StringBuilder var1) {
         super.toString(var1);
         var1.append(" address: '");
         String var2;
         if (this.getAddress() != null) {
            var2 = this.getAddress().getHostAddress();
         } else {
            var2 = "null";
         }

         var1.append(var2);
         var1.append('\'');
      }
   }

   public static class HostInformation extends DNSRecord {
      String _cpu;
      String _os;

      public HostInformation(String var1, DNSRecordClass var2, boolean var3, int var4, String var5, String var6) {
         super(var1, DNSRecordType.TYPE_HINFO, var2, var3, var4);
         this._cpu = var5;
         this._os = var6;
      }

      DNSOutgoing addAnswer(JmDNSImpl var1, DNSIncoming var2, InetAddress var3, int var4, DNSOutgoing var5) throws IOException {
         return var5;
      }

      public ServiceEvent getServiceEvent(JmDNSImpl var1) {
         ServiceInfo var2 = this.getServiceInfo(false);
         ((ServiceInfoImpl)var2).setDns(var1);
         return new ServiceEventImpl(var1, var2.getType(), var2.getName(), var2);
      }

      public ServiceInfo getServiceInfo(boolean var1) {
         HashMap var2 = new HashMap(2);
         var2.put("cpu", this._cpu);
         var2.put("os", this._os);
         return new ServiceInfoImpl(this.getQualifiedNameMap(), 0, 0, 0, var1, var2);
      }

      boolean handleQuery(JmDNSImpl var1, long var2) {
         return false;
      }

      boolean handleResponse(JmDNSImpl var1) {
         return false;
      }

      public boolean isSingleValued() {
         return true;
      }

      boolean sameValue(DNSRecord var1) {
         boolean var2 = var1 instanceof DNSRecord.HostInformation;
         boolean var3 = false;
         if (!var2) {
            return false;
         } else {
            DNSRecord.HostInformation var4 = (DNSRecord.HostInformation)var1;
            if (this._cpu == null && var4._cpu != null) {
               return false;
            } else if (this._os == null && var4._os != null) {
               return false;
            } else {
               var2 = var3;
               if (this._cpu.equals(var4._cpu)) {
                  var2 = var3;
                  if (this._os.equals(var4._os)) {
                     var2 = true;
                  }
               }

               return var2;
            }
         }
      }

      protected void toString(StringBuilder var1) {
         super.toString(var1);
         var1.append(" cpu: '");
         var1.append(this._cpu);
         var1.append("' os: '");
         var1.append(this._os);
         var1.append('\'');
      }

      void write(DNSOutgoing.MessageOutputStream var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append(this._cpu);
         var2.append(" ");
         var2.append(this._os);
         String var3 = var2.toString();
         var1.writeUTF(var3, 0, var3.length());
      }
   }

   public static class IPv4Address extends DNSRecord.Address {
      IPv4Address(String var1, DNSRecordClass var2, boolean var3, int var4, InetAddress var5) {
         super(var1, DNSRecordType.TYPE_A, var2, var3, var4, var5);
      }

      IPv4Address(String var1, DNSRecordClass var2, boolean var3, int var4, byte[] var5) {
         super(var1, DNSRecordType.TYPE_A, var2, var3, var4, var5);
      }

      public ServiceInfo getServiceInfo(boolean var1) {
         ServiceInfoImpl var2 = (ServiceInfoImpl)super.getServiceInfo(var1);
         var2.addAddress((Inet4Address)this._addr);
         return var2;
      }

      void write(DNSOutgoing.MessageOutputStream var1) {
         if (this._addr != null) {
            byte[] var2 = this._addr.getAddress();
            if (!(this._addr instanceof Inet4Address)) {
               byte[] var3 = new byte[4];
               System.arraycopy(var2, 12, var3, 0, 4);
               var2 = var3;
            }

            var1.writeBytes((byte[])var2, 0, var2.length);
         }

      }
   }

   public static class IPv6Address extends DNSRecord.Address {
      IPv6Address(String var1, DNSRecordClass var2, boolean var3, int var4, InetAddress var5) {
         super(var1, DNSRecordType.TYPE_AAAA, var2, var3, var4, var5);
      }

      IPv6Address(String var1, DNSRecordClass var2, boolean var3, int var4, byte[] var5) {
         super(var1, DNSRecordType.TYPE_AAAA, var2, var3, var4, var5);
      }

      public ServiceInfo getServiceInfo(boolean var1) {
         ServiceInfoImpl var2 = (ServiceInfoImpl)super.getServiceInfo(var1);
         var2.addAddress((Inet6Address)this._addr);
         return var2;
      }

      void write(DNSOutgoing.MessageOutputStream var1) {
         if (this._addr != null) {
            byte[] var4 = this._addr.getAddress();
            byte[] var3 = var4;
            if (this._addr instanceof Inet4Address) {
               byte[] var5 = new byte[16];
               int var2 = 0;

               while(true) {
                  var3 = var5;
                  if (var2 >= 16) {
                     break;
                  }

                  if (var2 < 11) {
                     var5[var2] = var4[var2 - 12];
                  } else {
                     var5[var2] = 0;
                  }

                  ++var2;
               }
            }

            var1.writeBytes((byte[])var3, 0, var3.length);
         }

      }
   }

   public static class Pointer extends DNSRecord {
      private final String _alias;

      public Pointer(String var1, DNSRecordClass var2, boolean var3, int var4, String var5) {
         super(var1, DNSRecordType.TYPE_PTR, var2, var3, var4);
         this._alias = var5;
      }

      DNSOutgoing addAnswer(JmDNSImpl var1, DNSIncoming var2, InetAddress var3, int var4, DNSOutgoing var5) throws IOException {
         return var5;
      }

      String getAlias() {
         return this._alias;
      }

      public ServiceEvent getServiceEvent(JmDNSImpl var1) {
         ServiceInfo var2 = this.getServiceInfo(false);
         ((ServiceInfoImpl)var2).setDns(var1);
         String var3 = var2.getType();
         return new ServiceEventImpl(var1, var3, JmDNSImpl.toUnqualifiedName(var3, this.getAlias()), var2);
      }

      public ServiceInfo getServiceInfo(boolean var1) {
         if (this.isServicesDiscoveryMetaQuery()) {
            return new ServiceInfoImpl(ServiceInfoImpl.decodeQualifiedNameMapForType(this.getAlias()), 0, 0, 0, var1, (byte[])null);
         } else if (this.isReverseLookup()) {
            return new ServiceInfoImpl(this.getQualifiedNameMap(), 0, 0, 0, var1, (byte[])null);
         } else if (this.isDomainDiscoveryQuery()) {
            return new ServiceInfoImpl(this.getQualifiedNameMap(), 0, 0, 0, var1, (byte[])null);
         } else {
            Map var2 = ServiceInfoImpl.decodeQualifiedNameMapForType(this.getAlias());
            var2.put(ServiceInfo.Fields.Subtype, this.getQualifiedNameMap().get(ServiceInfo.Fields.Subtype));
            return new ServiceInfoImpl(var2, 0, 0, 0, var1, this.getAlias());
         }
      }

      boolean handleQuery(JmDNSImpl var1, long var2) {
         return false;
      }

      boolean handleResponse(JmDNSImpl var1) {
         return false;
      }

      public boolean isSameEntry(DNSEntry var1) {
         return super.isSameEntry(var1) && var1 instanceof DNSRecord.Pointer && this.sameValue((DNSRecord.Pointer)var1);
      }

      public boolean isSingleValued() {
         return false;
      }

      boolean sameValue(DNSRecord var1) {
         if (!(var1 instanceof DNSRecord.Pointer)) {
            return false;
         } else {
            DNSRecord.Pointer var2 = (DNSRecord.Pointer)var1;
            return this._alias == null && var2._alias != null ? false : this._alias.equals(var2._alias);
         }
      }

      protected void toString(StringBuilder var1) {
         super.toString(var1);
         var1.append(" alias: '");
         String var2 = this._alias;
         if (var2 != null) {
            var2 = var2.toString();
         } else {
            var2 = "null";
         }

         var1.append(var2);
         var1.append('\'');
      }

      void write(DNSOutgoing.MessageOutputStream var1) {
         var1.writeName(this._alias);
      }
   }

   public static class Service extends DNSRecord {
      private static Logger logger1 = LoggerFactory.getLogger(DNSRecord.Service.class.getName());
      private final int _port;
      private final int _priority;
      private final String _server;
      private final int _weight;

      public Service(String var1, DNSRecordClass var2, boolean var3, int var4, int var5, int var6, int var7, String var8) {
         super(var1, DNSRecordType.TYPE_SRV, var2, var3, var4);
         this._priority = var5;
         this._weight = var6;
         this._port = var7;
         this._server = var8;
      }

      DNSOutgoing addAnswer(JmDNSImpl var1, DNSIncoming var2, InetAddress var3, int var4, DNSOutgoing var5) throws IOException {
         ServiceInfoImpl var7 = (ServiceInfoImpl)var1.getServices().get(this.getKey());
         if (var7 != null) {
            boolean var6;
            if (this._port == var7.getPort()) {
               var6 = true;
            } else {
               var6 = false;
            }

            if (var6 != this._server.equals(var1.getLocalHost().getName())) {
               return var1.addAnswer(var2, var3, var4, var5, new DNSRecord.Service(var7.getQualifiedName(), DNSRecordClass.CLASS_IN, true, DNSConstants.DNS_TTL, var7.getPriority(), var7.getWeight(), var7.getPort(), var1.getLocalHost().getName()));
            }
         }

         return var5;
      }

      public int getPort() {
         return this._port;
      }

      public int getPriority() {
         return this._priority;
      }

      String getServer() {
         return this._server;
      }

      public ServiceEvent getServiceEvent(JmDNSImpl var1) {
         ServiceInfo var2 = this.getServiceInfo(false);
         ((ServiceInfoImpl)var2).setDns(var1);
         return new ServiceEventImpl(var1, var2.getType(), var2.getName(), var2);
      }

      public ServiceInfo getServiceInfo(boolean var1) {
         return new ServiceInfoImpl(this.getQualifiedNameMap(), this._port, this._weight, this._priority, var1, (byte[])null);
      }

      public int getWeight() {
         return this._weight;
      }

      boolean handleQuery(JmDNSImpl var1, long var2) {
         ServiceInfoImpl var5 = (ServiceInfoImpl)var1.getServices().get(this.getKey());
         if (var5 != null && (var5.isAnnouncing() || var5.isAnnounced()) && (this._port != var5.getPort() || !this._server.equalsIgnoreCase(var1.getLocalHost().getName()))) {
            logger1.debug("handleQuery() Conflicting probe detected from: {}", this.getRecordSource());
            DNSRecord.Service var6 = new DNSRecord.Service(var5.getQualifiedName(), DNSRecordClass.CLASS_IN, true, DNSConstants.DNS_TTL, var5.getPriority(), var5.getWeight(), var5.getPort(), var1.getLocalHost().getName());

            try {
               if (var1.getInetAddress().equals(this.getRecordSource())) {
                  logger1.warn("Got conflicting probe from ourselves\nincoming: {}\nlocal   : {}", this.toString(), var6.toString());
               }
            } catch (IOException var8) {
               logger1.warn("IOException", var8);
            }

            int var4 = this.compareTo(var6);
            if (var4 == 0) {
               logger1.debug("handleQuery() Ignoring a identical service query");
               return false;
            } else if (var5.isProbing() && var4 > 0) {
               String var9 = var5.getQualifiedName().toLowerCase();
               var5.setName(NameRegister.Factory.getRegistry().incrementName(var1.getLocalHost().getInetAddress(), var5.getName(), NameRegister.NameType.SERVICE));
               var1.getServices().remove(var9);
               var1.getServices().put(var5.getQualifiedName().toLowerCase(), var5);
               logger1.debug("handleQuery() Lost tie break: new unique name chosen:{}", var5.getName());
               var5.revertState();
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      boolean handleResponse(JmDNSImpl var1) {
         ServiceInfoImpl var2 = (ServiceInfoImpl)var1.getServices().get(this.getKey());
         if (var2 != null && (this._port != var2.getPort() || !this._server.equalsIgnoreCase(var1.getLocalHost().getName()))) {
            logger1.debug("handleResponse() Denial detected");
            if (var2.isProbing()) {
               String var3 = var2.getQualifiedName().toLowerCase();
               var2.setName(NameRegister.Factory.getRegistry().incrementName(var1.getLocalHost().getInetAddress(), var2.getName(), NameRegister.NameType.SERVICE));
               var1.getServices().remove(var3);
               var1.getServices().put(var2.getQualifiedName().toLowerCase(), var2);
               logger1.debug("handleResponse() New unique name chose:{}", var2.getName());
            }

            var2.revertState();
            return true;
         } else {
            return false;
         }
      }

      public boolean isSingleValued() {
         return true;
      }

      boolean sameValue(DNSRecord var1) {
         boolean var2 = var1 instanceof DNSRecord.Service;
         boolean var3 = false;
         if (!var2) {
            return false;
         } else {
            DNSRecord.Service var4 = (DNSRecord.Service)var1;
            var2 = var3;
            if (this._priority == var4._priority) {
               var2 = var3;
               if (this._weight == var4._weight) {
                  var2 = var3;
                  if (this._port == var4._port) {
                     var2 = var3;
                     if (this._server.equals(var4._server)) {
                        var2 = true;
                     }
                  }
               }
            }

            return var2;
         }
      }

      protected void toByteArray(DataOutputStream var1) throws IOException {
         super.toByteArray(var1);
         var1.writeShort(this._priority);
         var1.writeShort(this._weight);
         var1.writeShort(this._port);

         try {
            var1.write(this._server.getBytes("UTF-8"));
         } catch (UnsupportedEncodingException var2) {
         }
      }

      protected void toString(StringBuilder var1) {
         super.toString(var1);
         var1.append(" server: '");
         var1.append(this._server);
         var1.append(':');
         var1.append(this._port);
         var1.append('\'');
      }

      void write(DNSOutgoing.MessageOutputStream var1) {
         var1.writeShort(this._priority);
         var1.writeShort(this._weight);
         var1.writeShort(this._port);
         if (DNSIncoming.USE_DOMAIN_NAME_FORMAT_FOR_SRV_TARGET) {
            var1.writeName(this._server);
         } else {
            String var2 = this._server;
            var1.writeUTF(var2, 0, var2.length());
            var1.writeByte(0);
         }
      }
   }

   public static class Text extends DNSRecord {
      private final byte[] _text;

      public Text(String var1, DNSRecordClass var2, boolean var3, int var4, byte[] var5) {
         super(var1, DNSRecordType.TYPE_TXT, var2, var3, var4);
         if (var5 == null || var5.length <= 0) {
            var5 = ByteWrangler.EMPTY_TXT;
         }

         this._text = var5;
      }

      DNSOutgoing addAnswer(JmDNSImpl var1, DNSIncoming var2, InetAddress var3, int var4, DNSOutgoing var5) throws IOException {
         return var5;
      }

      public ServiceEvent getServiceEvent(JmDNSImpl var1) {
         ServiceInfo var2 = this.getServiceInfo(false);
         ((ServiceInfoImpl)var2).setDns(var1);
         return new ServiceEventImpl(var1, var2.getType(), var2.getName(), var2);
      }

      public ServiceInfo getServiceInfo(boolean var1) {
         return new ServiceInfoImpl(this.getQualifiedNameMap(), 0, 0, 0, var1, this._text);
      }

      byte[] getText() {
         return this._text;
      }

      boolean handleQuery(JmDNSImpl var1, long var2) {
         return false;
      }

      boolean handleResponse(JmDNSImpl var1) {
         return false;
      }

      public boolean isSingleValued() {
         return true;
      }

      boolean sameValue(DNSRecord var1) {
         if (!(var1 instanceof DNSRecord.Text)) {
            return false;
         } else {
            DNSRecord.Text var5 = (DNSRecord.Text)var1;
            if (this._text == null && var5._text != null) {
               return false;
            } else {
               int var2 = var5._text.length;
               byte[] var4 = this._text;
               if (var2 != var4.length) {
                  return false;
               } else {
                  var2 = var4.length;

                  while(true) {
                     int var3 = var2 - 1;
                     if (var2 <= 0) {
                        return true;
                     }

                     if (var5._text[var3] != this._text[var3]) {
                        return false;
                     }

                     var2 = var3;
                  }
               }
            }
         }
      }

      protected void toString(StringBuilder var1) {
         super.toString(var1);
         var1.append(" text: '");
         String var2 = ByteWrangler.readUTF(this._text);
         if (20 < var2.length()) {
            var1.append(var2, 0, 17);
            var1.append("...");
         } else {
            var1.append(var2);
         }

         var1.append('\'');
      }

      void write(DNSOutgoing.MessageOutputStream var1) {
         byte[] var2 = this._text;
         var1.writeBytes((byte[])var2, 0, var2.length);
      }
   }
}
