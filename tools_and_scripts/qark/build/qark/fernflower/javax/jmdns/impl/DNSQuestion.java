package javax.jmdns.impl;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Set;
import javax.jmdns.ServiceInfo;
import javax.jmdns.impl.constants.DNSConstants;
import javax.jmdns.impl.constants.DNSRecordClass;
import javax.jmdns.impl.constants.DNSRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DNSQuestion extends DNSEntry {
   private static Logger logger = LoggerFactory.getLogger(DNSQuestion.class.getName());

   DNSQuestion(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   public static DNSQuestion newQuestion(String var0, DNSRecordType var1, DNSRecordClass var2, boolean var3) {
      switch(null.$SwitchMap$javax$jmdns$impl$constants$DNSRecordType[var1.ordinal()]) {
      case 1:
         return new DNSQuestion.DNS4Address(var0, var1, var2, var3);
      case 2:
         return new DNSQuestion.DNS6Address(var0, var1, var2, var3);
      case 3:
         return new DNSQuestion.DNS6Address(var0, var1, var2, var3);
      case 4:
         return new DNSQuestion.AllRecords(var0, var1, var2, var3);
      case 5:
         return new DNSQuestion.HostInformation(var0, var1, var2, var3);
      case 6:
         return new DNSQuestion.Pointer(var0, var1, var2, var3);
      case 7:
         return new DNSQuestion.Service(var0, var1, var2, var3);
      case 8:
         return new DNSQuestion.Text(var0, var1, var2, var3);
      default:
         return new DNSQuestion(var0, var1, var2, var3);
      }
   }

   public void addAnswers(JmDNSImpl var1, Set var2) {
   }

   protected void addAnswersForServiceInfo(JmDNSImpl var1, Set var2, ServiceInfoImpl var3) {
      if (var3 != null && var3.isAnnounced()) {
         if (this.getName().equalsIgnoreCase(var3.getQualifiedName()) || this.getName().equalsIgnoreCase(var3.getType()) || this.getName().equalsIgnoreCase(var3.getTypeWithSubtype())) {
            var2.addAll(var1.getLocalHost().answers(this.getRecordClass(), true, DNSConstants.DNS_TTL));
            var2.addAll(var3.answers(this.getRecordClass(), true, DNSConstants.DNS_TTL, var1.getLocalHost()));
         }

         logger.debug("{} DNSQuestion({}).addAnswersForServiceInfo(): info: {}\n{}", new Object[]{var1.getName(), this.getName(), var3, var2});
      }

   }

   boolean answeredBy(DNSEntry var1) {
      return this.isSameRecordClass(var1) && this.isSameType(var1) && this.getName().equals(var1.getName());
   }

   public boolean iAmTheOnlyOne(JmDNSImpl var1) {
      return false;
   }

   public boolean isExpired(long var1) {
      return false;
   }

   public boolean isStale(long var1) {
      return false;
   }

   public void toString(StringBuilder var1) {
   }

   private static class AllRecords extends DNSQuestion {
      AllRecords(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
         super(var1, var2, var3, var4);
      }

      public void addAnswers(JmDNSImpl var1, Set var2) {
         String var3 = this.getName().toLowerCase();
         if (var1.getLocalHost().getName().equalsIgnoreCase(var3)) {
            var2.addAll(var1.getLocalHost().answers(this.getRecordClass(), this.isUnique(), DNSConstants.DNS_TTL));
         } else if (var1.getServiceTypes().containsKey(var3)) {
            (new DNSQuestion.Pointer(this.getName(), DNSRecordType.TYPE_PTR, this.getRecordClass(), this.isUnique())).addAnswers(var1, var2);
         } else {
            Iterator var4 = var1.getServices().values().iterator();

            while(var4.hasNext()) {
               this.addAnswersForServiceInfo(var1, var2, (ServiceInfoImpl)((ServiceInfo)var4.next()));
            }

         }
      }

      public boolean iAmTheOnlyOne(JmDNSImpl var1) {
         String var2 = this.getName().toLowerCase();
         return var1.getLocalHost().getName().equals(var2) || var1.getServices().containsKey(var2);
      }

      public boolean isSameType(DNSEntry var1) {
         return var1 != null;
      }
   }

   private static class DNS4Address extends DNSQuestion {
      DNS4Address(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
         super(var1, var2, var3, var4);
      }

      public void addAnswers(JmDNSImpl var1, Set var2) {
         DNSRecord.Address var3 = var1.getLocalHost().getDNSAddressRecord(this.getRecordType(), true, DNSConstants.DNS_TTL);
         if (var3 != null) {
            var2.add(var3);
         }

      }

      public boolean iAmTheOnlyOne(JmDNSImpl var1) {
         String var2 = this.getName().toLowerCase();
         return var1.getLocalHost().getName().equals(var2) || var1.getServices().containsKey(var2);
      }
   }

   private static class DNS6Address extends DNSQuestion {
      DNS6Address(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
         super(var1, var2, var3, var4);
      }

      public void addAnswers(JmDNSImpl var1, Set var2) {
         DNSRecord.Address var3 = var1.getLocalHost().getDNSAddressRecord(this.getRecordType(), true, DNSConstants.DNS_TTL);
         if (var3 != null) {
            var2.add(var3);
         }

      }

      public boolean iAmTheOnlyOne(JmDNSImpl var1) {
         String var2 = this.getName().toLowerCase();
         return var1.getLocalHost().getName().equals(var2) || var1.getServices().containsKey(var2);
      }
   }

   private static class HostInformation extends DNSQuestion {
      HostInformation(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
         super(var1, var2, var3, var4);
      }
   }

   private static class Pointer extends DNSQuestion {
      Pointer(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
         super(var1, var2, var3, var4);
      }

      public void addAnswers(JmDNSImpl var1, Set var2) {
         Iterator var3 = var1.getServices().values().iterator();

         while(var3.hasNext()) {
            this.addAnswersForServiceInfo(var1, var2, (ServiceInfoImpl)((ServiceInfo)var3.next()));
         }

         if (!this.isServicesDiscoveryMetaQuery()) {
            if (this.isReverseLookup()) {
               String var4 = (String)this.getQualifiedNameMap().get(ServiceInfo.Fields.Instance);
               if (var4 != null && var4.length() > 0) {
                  InetAddress var7 = var1.getLocalHost().getInetAddress();
                  String var8;
                  if (var7 != null) {
                     var8 = var7.getHostAddress();
                  } else {
                     var8 = "";
                  }

                  if (var4.equalsIgnoreCase(var8)) {
                     if (this.isV4ReverseLookup()) {
                        var2.add(var1.getLocalHost().getDNSReverseAddressRecord(DNSRecordType.TYPE_A, false, DNSConstants.DNS_TTL));
                     }

                     if (this.isV6ReverseLookup()) {
                        var2.add(var1.getLocalHost().getDNSReverseAddressRecord(DNSRecordType.TYPE_AAAA, false, DNSConstants.DNS_TTL));
                     }
                  }
               }

            } else {
               this.isDomainDiscoveryQuery();
            }
         } else {
            Iterator var5 = var1.getServiceTypes().values().iterator();

            while(var5.hasNext()) {
               JmDNSImpl.ServiceTypeEntry var6 = (JmDNSImpl.ServiceTypeEntry)var5.next();
               var2.add(new DNSRecord.Pointer("_services._dns-sd._udp.local.", DNSRecordClass.CLASS_IN, false, DNSConstants.DNS_TTL, var6.getType()));
            }

         }
      }
   }

   private static class Service extends DNSQuestion {
      Service(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
         super(var1, var2, var3, var4);
      }

      public void addAnswers(JmDNSImpl var1, Set var2) {
         String var3 = this.getName().toLowerCase();
         if (var1.getLocalHost().getName().equalsIgnoreCase(var3)) {
            var2.addAll(var1.getLocalHost().answers(this.getRecordClass(), this.isUnique(), DNSConstants.DNS_TTL));
         } else if (var1.getServiceTypes().containsKey(var3)) {
            (new DNSQuestion.Pointer(this.getName(), DNSRecordType.TYPE_PTR, this.getRecordClass(), this.isUnique())).addAnswers(var1, var2);
         } else {
            this.addAnswersForServiceInfo(var1, var2, (ServiceInfoImpl)var1.getServices().get(var3));
         }
      }

      public boolean iAmTheOnlyOne(JmDNSImpl var1) {
         String var2 = this.getName().toLowerCase();
         return var1.getLocalHost().getName().equals(var2) || var1.getServices().containsKey(var2);
      }
   }

   private static class Text extends DNSQuestion {
      Text(String var1, DNSRecordType var2, DNSRecordClass var3, boolean var4) {
         super(var1, var2, var3, var4);
      }

      public void addAnswers(JmDNSImpl var1, Set var2) {
         this.addAnswersForServiceInfo(var1, var2, (ServiceInfoImpl)var1.getServices().get(this.getName().toLowerCase()));
      }

      public boolean iAmTheOnlyOne(JmDNSImpl var1) {
         String var2 = this.getName().toLowerCase();
         return var1.getLocalHost().getName().equals(var2) || var1.getServices().containsKey(var2);
      }
   }
}
