package org.apache.commons.codec.language.bm;

public enum RuleType {
   APPROX("approx"),
   EXACT("exact"),
   RULES;

   private final String name;

   static {
      RuleType var0 = new RuleType("RULES", 2, "rules");
      RULES = var0;
   }

   private RuleType(String var3) {
      this.name = var3;
   }

   public String getName() {
      return this.name;
   }
}
