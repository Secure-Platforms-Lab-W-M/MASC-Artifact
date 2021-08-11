package javax.annotation.meta;

public enum When {
   ALWAYS,
   MAYBE,
   NEVER,
   UNKNOWN;

   static {
      When var0 = new When("NEVER", 3);
      NEVER = var0;
   }
}
