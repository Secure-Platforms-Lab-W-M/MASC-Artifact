package org.apache.http.conn.ssl;

import org.apache.http.util.Args;

final class SubjectName {
   static final int DNS = 2;
   // $FF: renamed from: IP int
   static final int field_61 = 7;
   private final int type;
   private final String value;

   SubjectName(String var1, int var2) {
      this.value = (String)Args.notNull(var1, "Value");
      this.type = Args.positive(var2, "Type");
   }

   static SubjectName DNS(String var0) {
      return new SubjectName(var0, 2);
   }

   // $FF: renamed from: IP (java.lang.String) org.apache.http.conn.ssl.SubjectName
   static SubjectName method_17(String var0) {
      return new SubjectName(var0, 7);
   }

   public int getType() {
      return this.type;
   }

   public String getValue() {
      return this.value;
   }

   public String toString() {
      return this.value;
   }
}
