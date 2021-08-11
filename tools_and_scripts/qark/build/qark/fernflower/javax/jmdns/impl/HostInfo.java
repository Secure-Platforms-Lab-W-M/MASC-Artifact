package javax.jmdns.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import javax.jmdns.NetworkTopologyDiscovery;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import javax.jmdns.impl.constants.DNSState;
import javax.jmdns.impl.tasks.DNSTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostInfo implements DNSStatefulObject {
   private static Logger logger = LoggerFactory.getLogger(HostInfo.class.getName());
   protected InetAddress _address;
   protected NetworkInterface _interfaze;
   protected String _name;
   private final HostInfo.HostInfoState _state;

   private HostInfo(InetAddress var1, String var2, JmDNSImpl var3) {
      this._state = new HostInfo.HostInfoState(var3);
      this._address = var1;
      this._name = var2;
      if (var1 != null) {
         try {
            this._interfaze = NetworkInterface.getByInetAddress(var1);
            return;
         } catch (Exception var4) {
            logger.warn("LocalHostInfo() exception ", var4);
         }
      }

   }

   private DNSRecord.Address getDNS4AddressRecord(boolean var1, int var2) {
      return this.getInetAddress() instanceof Inet4Address ? new DNSRecord.IPv4Address(this.getName(), DNSRecordClass.CLASS_IN, var1, var2, this.getInetAddress()) : null;
   }

   private DNSRecord.Pointer getDNS4ReverseAddressRecord(boolean var1, int var2) {
      if (this.getInetAddress() instanceof Inet4Address) {
         StringBuilder var3 = new StringBuilder();
         var3.append(this.getInetAddress().getHostAddress());
         var3.append(".in-addr.arpa.");
         return new DNSRecord.Pointer(var3.toString(), DNSRecordClass.CLASS_IN, var1, var2, this.getName());
      } else {
         return null;
      }
   }

   private DNSRecord.Address getDNS6AddressRecord(boolean var1, int var2) {
      return this.getInetAddress() instanceof Inet6Address ? new DNSRecord.IPv6Address(this.getName(), DNSRecordClass.CLASS_IN, var1, var2, this.getInetAddress()) : null;
   }

   private DNSRecord.Pointer getDNS6ReverseAddressRecord(boolean var1, int var2) {
      if (this.getInetAddress() instanceof Inet6Address) {
         StringBuilder var3 = new StringBuilder();
         var3.append(this.getInetAddress().getHostAddress());
         var3.append(".ip6.arpa.");
         return new DNSRecord.Pointer(var3.toString(), DNSRecordClass.CLASS_IN, var1, var2, this.getName());
      } else {
         return null;
      }
   }

   private static InetAddress loopbackAddress() {
      try {
         InetAddress var0 = InetAddress.getByName((String)null);
         return var0;
      } catch (UnknownHostException var1) {
         return null;
      }
   }

   public static HostInfo newHostInfo(InetAddress var0, JmDNSImpl var1, String var2) {
      String var6;
      if (var2 != null) {
         var6 = var2;
      } else {
         var6 = "";
      }

      InetAddress var5;
      String var19;
      label128: {
         label142: {
            IOException var10000;
            label140: {
               var5 = var0;
               String var4;
               boolean var10001;
               if (var0 == null) {
                  label141: {
                     try {
                        var4 = System.getProperty("net.mdns.interface");
                     } catch (IOException var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label140;
                     }

                     InetAddress var21;
                     if (var4 != null) {
                        try {
                           var21 = InetAddress.getByName(var4);
                        } catch (IOException var12) {
                           var10000 = var12;
                           var10001 = false;
                           break label140;
                        }
                     } else {
                        label138: {
                           try {
                              var5 = InetAddress.getLocalHost();
                           } catch (IOException var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label140;
                           }

                           var21 = var5;

                           InetAddress[] var7;
                           try {
                              if (!var5.isLoopbackAddress()) {
                                 break label138;
                              }

                              var7 = NetworkTopologyDiscovery.Factory.getInstance().getInetAddresses();
                           } catch (IOException var18) {
                              var10000 = var18;
                              var10001 = false;
                              break label140;
                           }

                           var21 = var5;

                           try {
                              if (var7.length <= 0) {
                                 break label138;
                              }
                           } catch (IOException var17) {
                              var10000 = var17;
                              var10001 = false;
                              break label140;
                           }

                           var21 = var7[0];
                        }
                     }

                     var5 = var21;

                     try {
                        if (!var21.isLoopbackAddress()) {
                           break label141;
                        }

                        logger.warn("Could not find any address beside the loopback.");
                     } catch (IOException var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label140;
                     }

                     var5 = var21;
                  }
               }

               var4 = var6;

               try {
                  if (var6.length() == 0) {
                     var4 = var5.getHostName();
                  }
               } catch (IOException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label140;
               }

               label136: {
                  try {
                     if (var4.contains("in-addr.arpa")) {
                        break label136;
                     }
                  } catch (IOException var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label140;
                  }

                  var6 = var4;

                  try {
                     if (!var4.equals(var5.getHostAddress())) {
                        break label128;
                     }
                  } catch (IOException var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label140;
                  }
               }

               if (var2 != null) {
                  label139: {
                     try {
                        if (var2.length() <= 0) {
                           break label139;
                        }
                     } catch (IOException var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label140;
                     }

                     var19 = var2;
                     break label142;
                  }
               }

               try {
                  var4 = var5.getHostAddress();
               } catch (IOException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label140;
               }

               var19 = var4;
               break label142;
            }

            IOException var22 = var10000;
            Logger var23 = logger;
            StringBuilder var24 = new StringBuilder();
            var24.append("Could not initialize the host network interface on ");
            var24.append(var0);
            var24.append("because of an error: ");
            var24.append(var22.getMessage());
            var23.warn(var24.toString(), var22);
            var5 = loopbackAddress();
            if (var2 != null && var2.length() > 0) {
               var19 = var2;
            } else {
               var19 = "computer";
            }

            var6 = var19;
            break label128;
         }

         var6 = var19;
      }

      int var3 = var6.indexOf(".local");
      var19 = var6;
      if (var3 > 0) {
         var19 = var6.substring(0, var3);
      }

      var19 = var19.replaceAll("[:%\\.]", "-");
      StringBuilder var20 = new StringBuilder();
      var20.append(var19);
      var20.append(".local.");
      return new HostInfo(var5, var20.toString(), var1);
   }

   public boolean advanceState(DNSTask var1) {
      return this._state.advanceState(var1);
   }

   public Collection answers(DNSRecordClass var1, boolean var2, int var3) {
      ArrayList var4 = new ArrayList();
      DNSRecord.Address var5 = this.getDNS4AddressRecord(var2, var3);
      if (var5 != null && var5.matchRecordClass(var1)) {
         var4.add(var5);
      }

      var5 = this.getDNS6AddressRecord(var2, var3);
      if (var5 != null && var5.matchRecordClass(var1)) {
         var4.add(var5);
      }

      return var4;
   }

   public void associateWithTask(DNSTask var1, DNSState var2) {
      this._state.associateWithTask(var1, var2);
   }

   public boolean cancelState() {
      return this._state.cancelState();
   }

   public boolean closeState() {
      return this._state.closeState();
   }

   public boolean conflictWithRecord(DNSRecord.Address var1) {
      DNSRecord.Address var4 = this.getDNSAddressRecord(var1.getRecordType(), var1.isUnique(), DNSConstants.DNS_TTL);
      boolean var3 = false;
      if (var4 != null) {
         boolean var2 = var3;
         if (var4.sameType(var1)) {
            var2 = var3;
            if (var4.sameName(var1)) {
               var2 = var3;
               if (!var4.sameValue(var1)) {
                  var2 = true;
               }
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   DNSRecord.Address getDNSAddressRecord(DNSRecordType var1, boolean var2, int var3) {
      int var4 = null.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType[var1.ordinal()];
      if (var4 != 1) {
         return var4 != 2 && var4 != 3 ? null : this.getDNS6AddressRecord(var2, var3);
      } else {
         return this.getDNS4AddressRecord(var2, var3);
      }
   }

   DNSRecord.Pointer getDNSReverseAddressRecord(DNSRecordType var1, boolean var2, int var3) {
      int var4 = null.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType[var1.ordinal()];
      if (var4 != 1) {
         return var4 != 2 && var4 != 3 ? null : this.getDNS6ReverseAddressRecord(var2, var3);
      } else {
         return this.getDNS4ReverseAddressRecord(var2, var3);
      }
   }

   public JmDNSImpl getDns() {
      return this._state.getDns();
   }

   Inet4Address getInet4Address() {
      return this.getInetAddress() instanceof Inet4Address ? (Inet4Address)this._address : null;
   }

   Inet6Address getInet6Address() {
      return this.getInetAddress() instanceof Inet6Address ? (Inet6Address)this._address : null;
   }

   public InetAddress getInetAddress() {
      return this._address;
   }

   public NetworkInterface getInterface() {
      return this._interfaze;
   }

   public String getName() {
      return this._name;
   }

   String incrementHostName() {
      synchronized(this){}

      String var1;
      try {
         var1 = NameRegister.Factory.getRegistry().incrementName(this.getInetAddress(), this._name, NameRegister.NameType.HOST);
         this._name = var1;
      } finally {
         ;
      }

      return var1;
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

   public boolean isProbing() {
      return this._state.isProbing();
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

   boolean shouldIgnorePacket(DatagramPacket var1) {
      boolean var3 = false;
      boolean var4 = false;
      boolean var2 = var3;
      if (this.getInetAddress() != null) {
         InetAddress var5 = var1.getAddress();
         var2 = var3;
         if (var5 != null) {
            label20: {
               if (!this.getInetAddress().isLinkLocalAddress()) {
                  var3 = var4;
                  if (!this.getInetAddress().isMCLinkLocal()) {
                     break label20;
                  }
               }

               var3 = var4;
               if (!var5.isLinkLocalAddress()) {
                  var3 = true;
               }
            }

            var2 = var3;
            if (var5.isLoopbackAddress()) {
               var2 = var3;
               if (!this.getInetAddress().isLoopbackAddress()) {
                  var2 = true;
               }
            }
         }
      }

      return var2;
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder(1024);
      var2.append("local host info[");
      String var1;
      if (this.getName() != null) {
         var1 = this.getName();
      } else {
         var1 = "no name";
      }

      var2.append(var1);
      var2.append(", ");
      if (this.getInterface() != null) {
         var1 = this.getInterface().getDisplayName();
      } else {
         var1 = "???";
      }

      var2.append(var1);
      var2.append(":");
      if (this.getInetAddress() != null) {
         var1 = this.getInetAddress().getHostAddress();
      } else {
         var1 = "no address";
      }

      var2.append(var1);
      var2.append(", ");
      var2.append(this._state);
      var2.append("]");
      return var2.toString();
   }

   public boolean waitForAnnounced(long var1) {
      return this._state.waitForAnnounced(var1);
   }

   public boolean waitForCanceled(long var1) {
      return this._address == null ? true : this._state.waitForCanceled(var1);
   }

   private static final class HostInfoState extends DNSStatefulObject.DefaultImplementation {
      private static final long serialVersionUID = -8191476803620402088L;

      public HostInfoState(JmDNSImpl var1) {
         this.setDns(var1);
      }
   }
}
