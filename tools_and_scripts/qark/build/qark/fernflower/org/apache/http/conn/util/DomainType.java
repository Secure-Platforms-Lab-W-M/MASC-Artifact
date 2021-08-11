package org.apache.http.conn.util;

public enum DomainType {
   ICANN,
   PRIVATE,
   UNKNOWN;

   static {
      DomainType var0 = new DomainType("PRIVATE", 2);
      PRIVATE = var0;
   }
}
