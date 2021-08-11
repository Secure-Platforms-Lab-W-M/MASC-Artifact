package org.apache.http.entity.mime;

public class MinimalField {
   private final String name;
   private final String value;

   public MinimalField(String var1, String var2) {
      this.name = var1;
      this.value = var2;
   }

   public String getBody() {
      return this.value;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.name);
      var1.append(": ");
      var1.append(this.value);
      return var1.toString();
   }
}
