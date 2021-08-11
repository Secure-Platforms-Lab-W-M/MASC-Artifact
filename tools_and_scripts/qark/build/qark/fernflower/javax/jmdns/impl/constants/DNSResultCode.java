package javax.jmdns.impl.constants;

public enum DNSResultCode {
   static final int ExtendedRCode_MASK = 255;
   FormErr("Format Error", 1),
   NXDomain("Non-Existent Domain", 3),
   NXRRSet("RR Set that should exist does not", 8),
   NoError("No Error", 0),
   NotAuth("Server Not Authoritative for zone", 9),
   NotImp("Not Implemented", 4),
   NotZone;

   static final int RCode_MASK = 15;
   Refused("Query Refused", 5),
   ServFail("Server Failure", 2),
   Unknown("Unknown", 65535),
   YXDomain("Name Exists when it should not", 6),
   YXRRSet("RR Set Exists when it should not", 7);

   private final String _externalName;
   private final int _index;

   static {
      DNSResultCode var0 = new DNSResultCode("NotZone", 11, "NotZone Name not contained in zone", 10);
      NotZone = var0;
   }

   private DNSResultCode(String var3, int var4) {
      this._externalName = var3;
      this._index = var4;
   }

   public static DNSResultCode resultCodeForFlags(int var0) {
      DNSResultCode[] var3 = values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         DNSResultCode var4 = var3[var1];
         if (var4._index == (var0 & 15)) {
            return var4;
         }
      }

      return Unknown;
   }

   public static DNSResultCode resultCodeForFlags(int var0, int var1) {
      DNSResultCode[] var4 = values();
      int var3 = var4.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         DNSResultCode var5 = var4[var2];
         if (var5._index == (var1 >> 28 & 255 | var0 & 15)) {
            return var5;
         }
      }

      return Unknown;
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
