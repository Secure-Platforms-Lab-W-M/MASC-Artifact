package javax.jmdns.impl.constants;

public enum DNSOptionCode {
   LLQ("LLQ", 1),
   NSID("NSID", 3),
   Owner,
   // $FF: renamed from: UL javax.jmdns.impl.constants.DNSOptionCode
   field_169("UL", 2),
   Unknown("Unknown", 65535);

   private final String _externalName;
   private final int _index;

   static {
      DNSOptionCode var0 = new DNSOptionCode("Owner", 4, "Owner", 4);
      Owner = var0;
   }

   private DNSOptionCode(String var3, int var4) {
      this._externalName = var3;
      this._index = var4;
   }

   public static DNSOptionCode resultCodeForFlags(int var0) {
      DNSOptionCode[] var3 = values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         DNSOptionCode var4 = var3[var1];
         if (var4._index == var0) {
            return var4;
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
