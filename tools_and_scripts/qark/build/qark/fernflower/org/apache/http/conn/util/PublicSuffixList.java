package org.apache.http.conn.util;

import java.util.Collections;
import java.util.List;
import org.apache.http.util.Args;

public final class PublicSuffixList {
   private final List exceptions;
   private final List rules;
   private final DomainType type;

   public PublicSuffixList(List var1, List var2) {
      this(DomainType.UNKNOWN, var1, var2);
   }

   public PublicSuffixList(DomainType var1, List var2, List var3) {
      this.type = (DomainType)Args.notNull(var1, "Domain type");
      this.rules = Collections.unmodifiableList((List)Args.notNull(var2, "Domain suffix rules"));
      if (var3 == null) {
         var3 = Collections.emptyList();
      }

      this.exceptions = Collections.unmodifiableList(var3);
   }

   public List getExceptions() {
      return this.exceptions;
   }

   public List getRules() {
      return this.rules;
   }

   public DomainType getType() {
      return this.type;
   }
}
