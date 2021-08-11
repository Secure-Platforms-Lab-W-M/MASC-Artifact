package javax.jmdns.impl.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DNSRecordType {
   TYPE_A("a", 1),
   TYPE_A6("a6", 38),
   TYPE_AAAA("aaaa", 28),
   TYPE_AFSDB("afsdb", 18),
   TYPE_ANY,
   TYPE_APL("apl", 42),
   TYPE_ATMA("atma", 34),
   TYPE_AXFR("axfr", 252),
   TYPE_CERT("cert", 37),
   TYPE_CNAME("cname", 5),
   TYPE_DNAME("dname", 39),
   TYPE_DNSKEY("dnskey", 48),
   TYPE_DS("ds", 43),
   TYPE_EID("eid", 31),
   TYPE_GID("gid", 102),
   TYPE_GPOS("gpos", 27),
   TYPE_HINFO("hinfo", 13),
   TYPE_IGNORE("ignore", 0),
   TYPE_ISDN("isdn", 20),
   TYPE_IXFR("ixfr", 251),
   TYPE_KEY("key", 25),
   TYPE_KX("kx", 36),
   TYPE_LOC("loc", 29),
   TYPE_MAILA("mails", 253),
   TYPE_MAILB("mailb", 254),
   TYPE_MB("mb", 7),
   TYPE_MD("md", 3),
   TYPE_MF("mf", 4),
   TYPE_MG("mg", 8),
   TYPE_MINFO("minfo", 14),
   TYPE_MR("mr", 9),
   TYPE_MX("mx", 15),
   TYPE_NAPTR("naptr", 35),
   TYPE_NIMLOC("nimloc", 32),
   TYPE_NS("ns", 2),
   TYPE_NSAP("nsap", 22),
   TYPE_NSAP_PTR("nsap-otr", 23),
   TYPE_NSEC("nsec", 47),
   TYPE_NULL("null", 10),
   TYPE_NXT("nxt", 30),
   TYPE_OPT("opt", 41),
   TYPE_PTR("ptr", 12),
   TYPE_PX("px", 26),
   TYPE_RP("rp", 17),
   TYPE_RRSIG("rrsig", 46),
   TYPE_RT("rt", 21),
   TYPE_SIG("sig", 24),
   TYPE_SINK("sink", 40),
   TYPE_SOA("soa", 6),
   TYPE_SRV("srv", 33),
   TYPE_SSHFP("sshfp", 44),
   TYPE_TKEY("tkey", 249),
   TYPE_TSIG("tsig", 250),
   TYPE_TXT("txt", 16),
   TYPE_UID("uid", 101),
   TYPE_UINFO("uinfo", 100),
   TYPE_UNSPEC("unspec", 103),
   TYPE_WKS("wks", 11),
   TYPE_X25("x25", 19);

   private static Logger logger;
   private final String _externalName;
   private final int _index;

   static {
      DNSRecordType var0 = new DNSRecordType("TYPE_ANY", 58, "any", 255);
      TYPE_ANY = var0;
      logger = LoggerFactory.getLogger(DNSRecordType.class.getName());
   }

   private DNSRecordType(String var3, int var4) {
      this._externalName = var3;
      this._index = var4;
   }

   public static DNSRecordType typeForIndex(int var0) {
      DNSRecordType[] var3 = values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         DNSRecordType var4 = var3[var1];
         if (var4._index == var0) {
            return var4;
         }
      }

      logger.warn("Could not find record type for index: {}", var0);
      return TYPE_IGNORE;
   }

   public static DNSRecordType typeForName(String var0) {
      if (var0 != null) {
         String var3 = var0.toLowerCase();
         DNSRecordType[] var4 = values();
         int var2 = var4.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            DNSRecordType var5 = var4[var1];
            if (var5._externalName.equals(var3)) {
               return var5;
            }
         }
      }

      logger.warn("Could not find record type for name: {}", var0);
      return TYPE_IGNORE;
   }

   public String externalName() {
      return this._externalName;
   }

   public int indexValue() {
      return this._index;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.name());
      var1.append(" index ");
      var1.append(this.indexValue());
      return var1.toString();
   }
}
