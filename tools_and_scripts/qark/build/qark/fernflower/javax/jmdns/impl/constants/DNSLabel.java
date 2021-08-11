package javax.jmdns.impl.constants;

public enum DNSLabel {
   Compressed("compressed label", 192),
   Extended;

   static final int LABEL_MASK = 192;
   static final int LABEL_NOT_MASK = 63;
   Standard("standard label", 0),
   Unknown("", 128);

   private final String _externalName;
   private final int _index;

   static {
      DNSLabel var0 = new DNSLabel("Extended", 3, "extended label", 64);
      Extended = var0;
   }

   private DNSLabel(String var3, int var4) {
      this._externalName = var3;
      this._index = var4;
   }

   public static DNSLabel labelForByte(int var0) {
      DNSLabel[] var3 = values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         DNSLabel var4 = var3[var1];
         if (var4._index == (var0 & 192)) {
            return var4;
         }
      }

      return Unknown;
   }

   public static int labelValue(int var0) {
      return var0 & 63;
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
