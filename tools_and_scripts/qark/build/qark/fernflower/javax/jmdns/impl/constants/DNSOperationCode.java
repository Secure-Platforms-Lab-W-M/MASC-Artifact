package javax.jmdns.impl.constants;

public enum DNSOperationCode {
   IQuery("Inverse Query", 1),
   Notify("Notify", 4);

   static final int OpCode_MASK = 30720;
   Query("Query", 0),
   Status("Status", 2),
   Unassigned("Unassigned", 3),
   Update;

   private final String _externalName;
   private final int _index;

   static {
      DNSOperationCode var0 = new DNSOperationCode("Update", 5, "Update", 5);
      Update = var0;
   }

   private DNSOperationCode(String var3, int var4) {
      this._externalName = var3;
      this._index = var4;
   }

   public static DNSOperationCode operationCodeForFlags(int var0) {
      DNSOperationCode[] var3 = values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         DNSOperationCode var4 = var3[var1];
         if (var4._index == (var0 & 30720) >> 11) {
            return var4;
         }
      }

      return Unassigned;
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
