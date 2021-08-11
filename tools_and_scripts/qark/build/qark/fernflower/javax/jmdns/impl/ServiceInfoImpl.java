package javax.jmdns.impl;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.constants.DNSState;
import javax.jmdns.impl.tasks.DNSTask;
import javax.jmdns.impl.util.ByteWrangler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceInfoImpl extends ServiceInfo implements DNSListener, DNSStatefulObject {
   private static Logger logger = LoggerFactory.getLogger(ServiceInfoImpl.class.getName());
   private String _application;
   private ServiceInfoImpl.Delegate _delegate;
   private String _domain;
   private final Set _ipv4Addresses;
   private final Set _ipv6Addresses;
   private transient String _key;
   private String _name;
   private boolean _needTextAnnouncing;
   private boolean _persistent;
   private int _port;
   private int _priority;
   private Map _props;
   private String _protocol;
   private String _server;
   private final ServiceInfoImpl.ServiceInfoState _state;
   private String _subtype;
   private byte[] _text;
   private int _weight;

   public ServiceInfoImpl(String var1, String var2, String var3, int var4, int var5, int var6, boolean var7, String var8) {
      this(decodeQualifiedNameMap(var1, var2, var3), var4, var5, var6, var7, (byte[])null);

      try {
         this._text = ByteWrangler.encodeText(var8);
      } catch (IOException var9) {
         StringBuilder var10 = new StringBuilder();
         var10.append("Unexpected exception: ");
         var10.append(var9);
         throw new RuntimeException(var10.toString());
      }

      this._server = var8;
   }

   public ServiceInfoImpl(String var1, String var2, String var3, int var4, int var5, int var6, boolean var7, Map var8) {
      this(decodeQualifiedNameMap(var1, var2, var3), var4, var5, var6, var7, ByteWrangler.textFromProperties(var8));
   }

   public ServiceInfoImpl(String var1, String var2, String var3, int var4, int var5, int var6, boolean var7, byte[] var8) {
      this(decodeQualifiedNameMap(var1, var2, var3), var4, var5, var6, var7, var8);
   }

   ServiceInfoImpl(Map var1, int var2, int var3, int var4, boolean var5, String var6) {
      this(var1, var2, var3, var4, var5, (byte[])null);

      try {
         this._text = ByteWrangler.encodeText(var6);
      } catch (IOException var7) {
         StringBuilder var8 = new StringBuilder();
         var8.append("Unexpected exception: ");
         var8.append(var7);
         throw new RuntimeException(var8.toString());
      }

      this._server = var6;
   }

   public ServiceInfoImpl(Map var1, int var2, int var3, int var4, boolean var5, Map var6) {
      this(var1, var2, var3, var4, var5, ByteWrangler.textFromProperties(var6));
   }

   ServiceInfoImpl(Map var1, int var2, int var3, int var4, boolean var5, byte[] var6) {
      var1 = checkQualifiedNameMap(var1);
      this._domain = (String)var1.get(ServiceInfo.Fields.Domain);
      this._protocol = (String)var1.get(ServiceInfo.Fields.Protocol);
      this._application = (String)var1.get(ServiceInfo.Fields.Application);
      this._name = (String)var1.get(ServiceInfo.Fields.Instance);
      this._subtype = (String)var1.get(ServiceInfo.Fields.Subtype);
      this._port = var2;
      this._weight = var3;
      this._priority = var4;
      this._text = var6;
      this.setNeedTextAnnouncing(false);
      this._state = new ServiceInfoImpl.ServiceInfoState(this);
      this._persistent = var5;
      this._ipv4Addresses = Collections.synchronizedSet(new LinkedHashSet());
      this._ipv6Addresses = Collections.synchronizedSet(new LinkedHashSet());
   }

   ServiceInfoImpl(ServiceInfo var1) {
      this._ipv4Addresses = Collections.synchronizedSet(new LinkedHashSet());
      this._ipv6Addresses = Collections.synchronizedSet(new LinkedHashSet());
      if (var1 != null) {
         this._domain = var1.getDomain();
         this._protocol = var1.getProtocol();
         this._application = var1.getApplication();
         this._name = var1.getName();
         this._subtype = var1.getSubtype();
         this._port = var1.getPort();
         this._weight = var1.getWeight();
         this._priority = var1.getPriority();
         this._text = var1.getTextBytes();
         this._persistent = var1.isPersistent();
         Inet6Address[] var5 = var1.getInet6Addresses();
         int var4 = var5.length;
         byte var3 = 0;

         int var2;
         for(var2 = 0; var2 < var4; ++var2) {
            Inet6Address var6 = var5[var2];
            this._ipv6Addresses.add(var6);
         }

         Inet4Address[] var7 = var1.getInet4Addresses();
         var4 = var7.length;

         for(var2 = var3; var2 < var4; ++var2) {
            Inet4Address var8 = var7[var2];
            this._ipv4Addresses.add(var8);
         }
      }

      this._state = new ServiceInfoImpl.ServiceInfoState(this);
   }

   protected static Map checkQualifiedNameMap(Map var0) {
      HashMap var5 = new HashMap(5);
      String var2;
      if (var0.containsKey(ServiceInfo.Fields.Domain)) {
         var2 = (String)var0.get(ServiceInfo.Fields.Domain);
      } else {
         var2 = "local";
      }

      String var3;
      label66: {
         if (var2 != null) {
            var3 = var2;
            if (var2.length() != 0) {
               break label66;
            }
         }

         var3 = "local";
      }

      var2 = removeSeparators(var3);
      var5.put(ServiceInfo.Fields.Domain, var2);
      if (var0.containsKey(ServiceInfo.Fields.Protocol)) {
         var2 = (String)var0.get(ServiceInfo.Fields.Protocol);
      } else {
         var2 = "tcp";
      }

      label60: {
         if (var2 != null) {
            var3 = var2;
            if (var2.length() != 0) {
               break label60;
            }
         }

         var3 = "tcp";
      }

      var2 = removeSeparators(var3);
      var5.put(ServiceInfo.Fields.Protocol, var2);
      boolean var1 = var0.containsKey(ServiceInfo.Fields.Application);
      String var4 = "";
      if (var1) {
         var2 = (String)var0.get(ServiceInfo.Fields.Application);
      } else {
         var2 = "";
      }

      label54: {
         if (var2 != null) {
            var3 = var2;
            if (var2.length() != 0) {
               break label54;
            }
         }

         var3 = "";
      }

      var2 = removeSeparators(var3);
      var5.put(ServiceInfo.Fields.Application, var2);
      if (var0.containsKey(ServiceInfo.Fields.Instance)) {
         var2 = (String)var0.get(ServiceInfo.Fields.Instance);
      } else {
         var2 = "";
      }

      label48: {
         if (var2 != null) {
            var3 = var2;
            if (var2.length() != 0) {
               break label48;
            }
         }

         var3 = "";
      }

      var2 = removeSeparators(var3);
      var5.put(ServiceInfo.Fields.Instance, var2);
      var2 = var4;
      if (var0.containsKey(ServiceInfo.Fields.Subtype)) {
         var2 = (String)var0.get(ServiceInfo.Fields.Subtype);
      }

      String var6;
      label42: {
         if (var2 != null) {
            var6 = var2;
            if (var2.length() != 0) {
               break label42;
            }
         }

         var6 = "";
      }

      var6 = removeSeparators(var6);
      var5.put(ServiceInfo.Fields.Subtype, var6);
      return var5;
   }

   public static Map decodeQualifiedNameMap(String var0, String var1, String var2) {
      Map var3 = decodeQualifiedNameMapForType(var0);
      var3.put(ServiceInfo.Fields.Instance, var1);
      var3.put(ServiceInfo.Fields.Subtype, var2);
      return checkQualifiedNameMap(var3);
   }

   public static Map decodeQualifiedNameMapForType(String var0) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:632)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s2stmt(TypeTransformer.java:820)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:843)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   private boolean handleExpiredRecord(DNSRecord var1) {
      int var2 = null.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType[var1.getRecordType().ordinal()];
      if (var2 != 1 && var2 != 2) {
         logger.trace("Unhandled expired record: {}", var1);
      } else if (var1.getName().equalsIgnoreCase(this.getServer())) {
         DNSRecord.Address var3 = (DNSRecord.Address)var1;
         if (DNSRecordType.TYPE_A.equals(var1.getRecordType())) {
            Inet4Address var4 = (Inet4Address)var3.getAddress();
            if (this._ipv4Addresses.remove(var4)) {
               logger.debug("Removed expired IPv4: {}", var4);
               return true;
            }

            logger.debug("Expired IPv4 not in this service: {}", var4);
         } else {
            Inet6Address var5 = (Inet6Address)var3.getAddress();
            if (this._ipv6Addresses.remove(var5)) {
               logger.debug("Removed expired IPv6: {}", var5);
               return true;
            }

            logger.debug("Expired IPv6 not in this service: {}", var5);
         }
      }

      return false;
   }

   private boolean handleUpdateRecord(DNSCache var1, long var2, DNSRecord var4) {
      boolean var8 = false;
      boolean var10 = false;
      boolean var9 = false;
      int var5 = null.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType[var4.getRecordType().ordinal()];
      boolean var6 = true;
      boolean var7;
      DNSRecord.Address var12;
      if (var5 != 1) {
         if (var5 != 2) {
            if (var5 != 3) {
               if (var5 != 4) {
                  if (var5 != 5) {
                     return false;
                  }

                  var7 = var10;
                  if (this.getSubtype().length() == 0) {
                     var7 = var10;
                     if (var4.getSubtype().length() != 0) {
                        this._subtype = var4.getSubtype();
                        return true;
                     }
                  }
               } else {
                  var7 = var10;
                  if (var4.getName().equalsIgnoreCase(this.getQualifiedName())) {
                     this._text = ((DNSRecord.Text)var4).getText();
                     this._props = null;
                     return true;
                  }
               }
            } else {
               var7 = var10;
               if (var4.getName().equalsIgnoreCase(this.getQualifiedName())) {
                  DNSRecord.Service var15 = (DNSRecord.Service)var4;
                  String var11 = this._server;
                  boolean var17 = var6;
                  if (var11 != null) {
                     if (!var11.equalsIgnoreCase(var15.getServer())) {
                        var17 = var6;
                     } else {
                        var17 = false;
                     }
                  }

                  this._server = var15.getServer();
                  this._port = var15.getPort();
                  this._weight = var15.getWeight();
                  this._priority = var15.getPriority();
                  if (var17) {
                     this._ipv4Addresses.clear();
                     this._ipv6Addresses.clear();
                     Iterator var16 = var1.getDNSEntryList(this._server, DNSRecordType.TYPE_A, DNSRecordClass.CLASS_IN).iterator();

                     while(var16.hasNext()) {
                        this.updateRecord(var1, var2, (DNSEntry)var16.next());
                     }

                     var16 = var1.getDNSEntryList(this._server, DNSRecordType.TYPE_AAAA, DNSRecordClass.CLASS_IN).iterator();

                     while(var16.hasNext()) {
                        this.updateRecord(var1, var2, (DNSEntry)var16.next());
                     }

                     var7 = var9;
                  } else {
                     var7 = true;
                  }

                  return var7;
               }
            }
         } else {
            var7 = var10;
            if (var4.getName().equalsIgnoreCase(this.getServer())) {
               var12 = (DNSRecord.Address)var4;
               var7 = var8;
               if (var12.getAddress() instanceof Inet6Address) {
                  Inet6Address var13 = (Inet6Address)var12.getAddress();
                  var7 = var8;
                  if (this._ipv6Addresses.add(var13)) {
                     var7 = true;
                  }
               }

               return var7;
            }
         }
      } else {
         var7 = var10;
         if (var4.getName().equalsIgnoreCase(this.getServer())) {
            var12 = (DNSRecord.Address)var4;
            var7 = var10;
            if (var12.getAddress() instanceof Inet4Address) {
               Inet4Address var14 = (Inet4Address)var12.getAddress();
               var7 = var10;
               if (this._ipv4Addresses.add(var14)) {
                  var7 = true;
               }
            }
         }
      }

      return var7;
   }

   private final boolean hasInetAddress() {
      return this._ipv4Addresses.size() > 0 || this._ipv6Addresses.size() > 0;
   }

   private static String removeSeparators(String var0) {
      if (var0 == null) {
         return "";
      } else {
         String var1 = var0.trim();
         var0 = var1;
         if (var1.startsWith(".")) {
            var0 = var1.substring(1);
         }

         var1 = var0;
         if (var0.startsWith("_")) {
            var1 = var0.substring(1);
         }

         var0 = var1;
         if (var1.endsWith(".")) {
            var0 = var1.substring(0, var1.length() - 1);
         }

         return var0;
      }
   }

   void _setText(byte[] var1) {
      this._text = var1;
      this._props = null;
   }

   void addAddress(Inet4Address var1) {
      this._ipv4Addresses.add(var1);
   }

   void addAddress(Inet6Address var1) {
      this._ipv6Addresses.add(var1);
   }

   public boolean advanceState(DNSTask var1) {
      return this._state.advanceState(var1);
   }

   public Collection answers(DNSRecordClass var1, boolean var2, int var3, HostInfo var4) {
      ArrayList var5 = new ArrayList();
      if (var1 == DNSRecordClass.CLASS_ANY || var1 == DNSRecordClass.CLASS_IN) {
         if (this.getSubtype().length() > 0) {
            var5.add(new DNSRecord.Pointer(this.getTypeWithSubtype(), DNSRecordClass.CLASS_IN, false, var3, this.getQualifiedName()));
         }

         var5.add(new DNSRecord.Pointer(this.getType(), DNSRecordClass.CLASS_IN, false, var3, this.getQualifiedName()));
         var5.add(new DNSRecord.Service(this.getQualifiedName(), DNSRecordClass.CLASS_IN, var2, var3, this._priority, this._weight, this._port, var4.getName()));
         var5.add(new DNSRecord.Text(this.getQualifiedName(), DNSRecordClass.CLASS_IN, var2, var3, this.getTextBytes()));
      }

      return var5;
   }

   public void associateWithTask(DNSTask var1, DNSState var2) {
      this._state.associateWithTask(var1, var2);
   }

   public boolean cancelState() {
      return this._state.cancelState();
   }

   public ServiceInfoImpl clone() {
      ServiceInfoImpl var4 = new ServiceInfoImpl(this.getQualifiedNameMap(), this._port, this._weight, this._priority, this._persistent, this._text);
      Inet6Address[] var5 = this.getInet6Addresses();
      int var3 = var5.length;
      byte var2 = 0;

      int var1;
      for(var1 = 0; var1 < var3; ++var1) {
         Inet6Address var6 = var5[var1];
         var4._ipv6Addresses.add(var6);
      }

      Inet4Address[] var7 = this.getInet4Addresses();
      var3 = var7.length;

      for(var1 = var2; var1 < var3; ++var1) {
         Inet4Address var8 = var7[var1];
         var4._ipv4Addresses.add(var8);
      }

      return var4;
   }

   public boolean closeState() {
      return this._state.closeState();
   }

   public boolean equals(Object var1) {
      return var1 instanceof ServiceInfoImpl && this.getQualifiedName().equals(((ServiceInfoImpl)var1).getQualifiedName());
   }

   @Deprecated
   public InetAddress getAddress() {
      return this.getInetAddress();
   }

   public String getApplication() {
      String var1 = this._application;
      return var1 != null ? var1 : "";
   }

   ServiceInfoImpl.Delegate getDelegate() {
      return this._delegate;
   }

   public JmDNSImpl getDns() {
      return this._state.getDns();
   }

   public String getDomain() {
      String var1 = this._domain;
      return var1 != null ? var1 : "local";
   }

   @Deprecated
   public String getHostAddress() {
      String[] var1 = this.getHostAddresses();
      return var1.length > 0 ? var1[0] : "";
   }

   public String[] getHostAddresses() {
      Inet4Address[] var3 = this.getInet4Addresses();
      Inet6Address[] var4 = this.getInet6Addresses();
      String[] var5 = new String[var3.length + var4.length];

      int var1;
      for(var1 = 0; var1 < var3.length; ++var1) {
         var5[var1] = var3[var1].getHostAddress();
      }

      for(var1 = 0; var1 < var4.length; ++var1) {
         int var2 = var3.length;
         StringBuilder var6 = new StringBuilder();
         var6.append("[");
         var6.append(var4[var1].getHostAddress());
         var6.append("]");
         var5[var2 + var1] = var6.toString();
      }

      return var5;
   }

   @Deprecated
   public Inet4Address getInet4Address() {
      Inet4Address[] var1 = this.getInet4Addresses();
      return var1.length > 0 ? var1[0] : null;
   }

   public Inet4Address[] getInet4Addresses() {
      Set var1 = this._ipv4Addresses;
      return (Inet4Address[])var1.toArray(new Inet4Address[var1.size()]);
   }

   @Deprecated
   public Inet6Address getInet6Address() {
      Inet6Address[] var1 = this.getInet6Addresses();
      return var1.length > 0 ? var1[0] : null;
   }

   public Inet6Address[] getInet6Addresses() {
      Set var1 = this._ipv6Addresses;
      return (Inet6Address[])var1.toArray(new Inet6Address[var1.size()]);
   }

   @Deprecated
   public InetAddress getInetAddress() {
      InetAddress[] var1 = this.getInetAddresses();
      return var1.length > 0 ? var1[0] : null;
   }

   public InetAddress[] getInetAddresses() {
      ArrayList var1 = new ArrayList(this._ipv4Addresses.size() + this._ipv6Addresses.size());
      var1.addAll(this._ipv4Addresses);
      var1.addAll(this._ipv6Addresses);
      return (InetAddress[])var1.toArray(new InetAddress[var1.size()]);
   }

   public String getKey() {
      if (this._key == null) {
         this._key = this.getQualifiedName().toLowerCase();
      }

      return this._key;
   }

   public String getName() {
      String var1 = this._name;
      return var1 != null ? var1 : "";
   }

   public String getNiceTextString() {
      StringBuilder var4 = new StringBuilder();
      int var1 = 0;

      for(int var2 = this.getTextBytes().length; var1 < var2; ++var1) {
         if (var1 >= 200) {
            var4.append("...");
            break;
         }

         int var3 = this.getTextBytes()[var1] & 255;
         if (var3 >= 32 && var3 <= 127) {
            var4.append((char)var3);
         } else {
            var4.append("\\0");
            var4.append(Integer.toString(var3, 8));
         }
      }

      return var4.toString();
   }

   public int getPort() {
      return this._port;
   }

   public int getPriority() {
      return this._priority;
   }

   Map getProperties() {
      // $FF: Couldn't be decompiled
   }

   public byte[] getPropertyBytes(String var1) {
      synchronized(this){}

      byte[] var4;
      try {
         var4 = (byte[])this.getProperties().get(var1);
      } finally {
         ;
      }

      return var4;
   }

   public Enumeration getPropertyNames() {
      Map var1 = this.getProperties();
      Set var2;
      if (var1 != null) {
         var2 = var1.keySet();
      } else {
         var2 = Collections.emptySet();
      }

      return (new Vector(var2)).elements();
   }

   public String getPropertyString(String var1) {
      synchronized(this){}

      Throwable var10000;
      label136: {
         boolean var10001;
         byte[] var14;
         try {
            var14 = (byte[])this.getProperties().get(var1);
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label136;
         }

         if (var14 == null) {
            return null;
         }

         try {
            if (var14 == ByteWrangler.NO_VALUE) {
               return "true";
            }
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label136;
         }

         try {
            var1 = ByteWrangler.readUTF(var14, 0, var14.length);
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label136;
         }

         return var1;
      }

      Throwable var15 = var10000;
      throw var15;
   }

   public String getProtocol() {
      String var1 = this._protocol;
      return var1 != null ? var1 : "tcp";
   }

   public String getQualifiedName() {
      String var4 = this.getDomain();
      String var6 = this.getProtocol();
      String var7 = this.getApplication();
      String var2 = this.getName();
      StringBuilder var5 = new StringBuilder();
      int var1 = var2.length();
      String var3 = "";
      if (var1 > 0) {
         StringBuilder var8 = new StringBuilder();
         var8.append(var2);
         var8.append(".");
         var2 = var8.toString();
      } else {
         var2 = "";
      }

      var5.append(var2);
      StringBuilder var9;
      if (var7.length() > 0) {
         var9 = new StringBuilder();
         var9.append("_");
         var9.append(var7);
         var9.append(".");
         var2 = var9.toString();
      } else {
         var2 = "";
      }

      var5.append(var2);
      var2 = var3;
      if (var6.length() > 0) {
         var9 = new StringBuilder();
         var9.append("_");
         var9.append(var6);
         var9.append(".");
         var2 = var9.toString();
      }

      var5.append(var2);
      var5.append(var4);
      var5.append(".");
      return var5.toString();
   }

   public Map getQualifiedNameMap() {
      HashMap var1 = new HashMap(5);
      var1.put(ServiceInfo.Fields.Domain, this.getDomain());
      var1.put(ServiceInfo.Fields.Protocol, this.getProtocol());
      var1.put(ServiceInfo.Fields.Application, this.getApplication());
      var1.put(ServiceInfo.Fields.Instance, this.getName());
      var1.put(ServiceInfo.Fields.Subtype, this.getSubtype());
      return var1;
   }

   public String getServer() {
      String var1 = this._server;
      return var1 != null ? var1 : "";
   }

   public String getSubtype() {
      String var1 = this._subtype;
      return var1 != null ? var1 : "";
   }

   public byte[] getTextBytes() {
      byte[] var1 = this._text;
      return var1 != null && var1.length > 0 ? var1 : ByteWrangler.EMPTY_TXT;
   }

   @Deprecated
   public String getTextString() {
      Iterator var1 = this.getProperties().entrySet().iterator();
      if (var1.hasNext()) {
         Entry var4 = (Entry)var1.next();
         byte[] var2 = (byte[])var4.getValue();
         if (var2 != null && var2.length > 0) {
            String var5 = ByteWrangler.readUTF(var2);
            StringBuilder var3 = new StringBuilder();
            var3.append((String)var4.getKey());
            var3.append("=");
            var3.append(var5);
            return var3.toString();
         } else {
            return (String)var4.getKey();
         }
      } else {
         return "";
      }
   }

   public String getType() {
      String var4 = this.getDomain();
      String var6 = this.getProtocol();
      String var2 = this.getApplication();
      StringBuilder var5 = new StringBuilder();
      int var1 = var2.length();
      String var3 = "";
      if (var1 > 0) {
         StringBuilder var7 = new StringBuilder();
         var7.append("_");
         var7.append(var2);
         var7.append(".");
         var2 = var7.toString();
      } else {
         var2 = "";
      }

      var5.append(var2);
      var2 = var3;
      if (var6.length() > 0) {
         StringBuilder var8 = new StringBuilder();
         var8.append("_");
         var8.append(var6);
         var8.append(".");
         var2 = var8.toString();
      }

      var5.append(var2);
      var5.append(var4);
      var5.append(".");
      return var5.toString();
   }

   public String getTypeWithSubtype() {
      String var1 = this.getSubtype();
      StringBuilder var2 = new StringBuilder();
      if (var1.length() > 0) {
         StringBuilder var3 = new StringBuilder();
         var3.append("_");
         var3.append(var1);
         var3.append("._sub.");
         var1 = var3.toString();
      } else {
         var1 = "";
      }

      var2.append(var1);
      var2.append(this.getType());
      return var2.toString();
   }

   @Deprecated
   public String getURL() {
      return this.getURL("http");
   }

   @Deprecated
   public String getURL(String var1) {
      String[] var2 = this.getURLs(var1);
      if (var2.length > 0) {
         return var2[0];
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append("://null:");
         var3.append(this.getPort());
         return var3.toString();
      }
   }

   public String[] getURLs() {
      return this.getURLs("http");
   }

   public String[] getURLs(String var1) {
      InetAddress[] var7 = this.getInetAddresses();
      ArrayList var8 = new ArrayList(var7.length);
      int var3 = var7.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         InetAddress var6 = var7[var2];
         String var5 = var6.getHostAddress();
         String var4 = var5;
         StringBuilder var9;
         if (var6 instanceof Inet6Address) {
            var9 = new StringBuilder();
            var9.append("[");
            var9.append(var5);
            var9.append("]");
            var4 = var9.toString();
         }

         StringBuilder var10 = new StringBuilder();
         var10.append(var1);
         var10.append("://");
         var10.append(var4);
         var10.append(":");
         var10.append(this.getPort());
         String var11 = var10.toString();
         var5 = this.getPropertyString("path");
         var4 = var11;
         if (var5 != null) {
            if (var5.indexOf("://") >= 0) {
               var4 = var5;
            } else {
               var9 = new StringBuilder();
               var9.append(var11);
               if (!var5.startsWith("/")) {
                  StringBuilder var12 = new StringBuilder();
                  var12.append("/");
                  var12.append(var5);
                  var5 = var12.toString();
               }

               var9.append(var5);
               var4 = var9.toString();
            }
         }

         var8.add(var4);
      }

      return (String[])var8.toArray(new String[var8.size()]);
   }

   public int getWeight() {
      return this._weight;
   }

   public boolean hasData() {
      synchronized(this){}
      boolean var5 = false;

      boolean var2;
      label59: {
         int var1;
         try {
            label57: {
               var5 = true;
               if (this.getServer() != null) {
                  if (!this.hasInetAddress()) {
                     var5 = false;
                     break label59;
                  }

                  if (this.getTextBytes() != null) {
                     var1 = this.getTextBytes().length;
                     var5 = false;
                     break label57;
                  }

                  var5 = false;
                  break label59;
               }

               var5 = false;
               break label59;
            }
         } finally {
            if (var5) {
               ;
            }
         }

         if (var1 > 0) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public boolean hasSameAddresses(ServiceInfo var1) {
      if (var1 == null) {
         return false;
      } else if (var1 instanceof ServiceInfoImpl) {
         ServiceInfoImpl var4 = (ServiceInfoImpl)var1;
         return this._ipv4Addresses.size() == var4._ipv4Addresses.size() && this._ipv6Addresses.size() == var4._ipv6Addresses.size() && this._ipv4Addresses.equals(var4._ipv4Addresses) && this._ipv6Addresses.equals(var4._ipv6Addresses);
      } else {
         InetAddress[] var2 = this.getInetAddresses();
         InetAddress[] var3 = var1.getInetAddresses();
         return var2.length == var3.length && (new HashSet(Arrays.asList(var2))).equals(new HashSet(Arrays.asList(var3)));
      }
   }

   public int hashCode() {
      return this.getQualifiedName().hashCode();
   }

   public boolean isAnnounced() {
      return this._state.isAnnounced();
   }

   public boolean isAnnouncing() {
      return this._state.isAnnouncing();
   }

   public boolean isAssociatedWithTask(DNSTask var1, DNSState var2) {
      return this._state.isAssociatedWithTask(var1, var2);
   }

   public boolean isCanceled() {
      return this._state.isCanceled();
   }

   public boolean isCanceling() {
      return this._state.isCanceling();
   }

   public boolean isClosed() {
      return this._state.isClosed();
   }

   public boolean isClosing() {
      return this._state.isClosing();
   }

   public boolean isPersistent() {
      return this._persistent;
   }

   public boolean isProbing() {
      return this._state.isProbing();
   }

   public boolean needTextAnnouncing() {
      return this._needTextAnnouncing;
   }

   public boolean recoverState() {
      return this._state.recoverState();
   }

   public void removeAssociationWithTask(DNSTask var1) {
      this._state.removeAssociationWithTask(var1);
   }

   public boolean revertState() {
      return this._state.revertState();
   }

   void setDelegate(ServiceInfoImpl.Delegate var1) {
      this._delegate = var1;
   }

   public void setDns(JmDNSImpl var1) {
      this._state.setDns(var1);
   }

   void setName(String var1) {
      this._name = var1;
      this._key = null;
   }

   public void setNeedTextAnnouncing(boolean var1) {
      this._needTextAnnouncing = var1;
      if (var1) {
         this._state.setTask((DNSTask)null);
      }

   }

   void setServer(String var1) {
      this._server = var1;
   }

   public void setText(Map var1) throws IllegalStateException {
      this.setText(ByteWrangler.textFromProperties(var1));
   }

   public void setText(byte[] param1) throws IllegalStateException {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      StringBuilder var4 = new StringBuilder();
      var4.append('[');
      var4.append(this.getClass().getSimpleName());
      var4.append('@');
      var4.append(System.identityHashCode(this));
      var4.append(" name: '");
      if (this.getName().length() > 0) {
         var4.append(this.getName());
         var4.append('.');
      }

      var4.append(this.getTypeWithSubtype());
      var4.append("' address: '");
      InetAddress[] var3 = this.getInetAddresses();
      if (var3.length > 0) {
         int var2 = var3.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var4.append(var3[var1]);
            var4.append(':');
            var4.append(this.getPort());
            var4.append(' ');
         }
      } else {
         var4.append("(null):");
         var4.append(this.getPort());
      }

      var4.append("' status: '");
      var4.append(this._state.toString());
      String var7;
      if (this.isPersistent()) {
         var7 = "' is persistent,";
      } else {
         var7 = "',";
      }

      var4.append(var7);
      if (this.hasData()) {
         var4.append(" has data");
      } else {
         var4.append(" has NO data");
      }

      if (this.getTextBytes().length > 0) {
         Map var8 = this.getProperties();
         if (!var8.isEmpty()) {
            Iterator var9 = var8.entrySet().iterator();

            while(var9.hasNext()) {
               Entry var5 = (Entry)var9.next();
               String var6 = ByteWrangler.readUTF((byte[])var5.getValue());
               var4.append("\n\t");
               var4.append((String)var5.getKey());
               var4.append(": ");
               var4.append(var6);
            }
         } else {
            var4.append(", empty");
         }
      }

      var4.append(']');
      return var4.toString();
   }

   public void updateRecord(DNSCache param1, long param2, DNSEntry param4) {
      // $FF: Couldn't be decompiled
   }

   public boolean waitForAnnounced(long var1) {
      return this._state.waitForAnnounced(var1);
   }

   public boolean waitForCanceled(long var1) {
      return this._state.waitForCanceled(var1);
   }

   public interface Delegate {
      void textValueUpdated(ServiceInfo var1, byte[] var2);
   }

   private static final class ServiceInfoState extends DNSStatefulObject.DefaultImplementation {
      private static final long serialVersionUID = 1104131034952196820L;
      private final ServiceInfoImpl _info;

      public ServiceInfoState(ServiceInfoImpl var1) {
         this._info = var1;
      }

      public void setDns(JmDNSImpl var1) {
         super.setDns(var1);
      }

      protected void setTask(DNSTask var1) {
         super.setTask(var1);
         if (this._task == null && this._info.needTextAnnouncing()) {
            this.lock();

            try {
               if (this._task == null && this._info.needTextAnnouncing()) {
                  if (this._state.isAnnounced()) {
                     this.setState(DNSState.ANNOUNCING_1);
                     if (this.getDns() != null) {
                        this.getDns().startAnnouncer();
                     }
                  }

                  this._info.setNeedTextAnnouncing(false);
               }
            } finally {
               this.unlock();
            }

         }
      }
   }
}
