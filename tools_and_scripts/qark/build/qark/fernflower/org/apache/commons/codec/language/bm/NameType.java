package org.apache.commons.codec.language.bm;

public enum NameType {
   ASHKENAZI("ash"),
   GENERIC("gen"),
   SEPHARDIC;

   private final String name;

   static {
      NameType var0 = new NameType("SEPHARDIC", 2, "sep");
      SEPHARDIC = var0;
   }

   private NameType(String var3) {
      this.name = var3;
   }

   public String getName() {
      return this.name;
   }
}
