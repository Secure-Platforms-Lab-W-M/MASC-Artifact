package okhttp3;

import javax.annotation.Nullable;

public final class Challenge {
   private final String realm;
   private final String scheme;

   public Challenge(String var1, String var2) {
      if (var1 != null) {
         if (var2 != null) {
            this.scheme = var1;
            this.realm = var2;
         } else {
            throw new NullPointerException("realm == null");
         }
      } else {
         throw new NullPointerException("scheme == null");
      }
   }

   public boolean equals(@Nullable Object var1) {
      return var1 instanceof Challenge && ((Challenge)var1).scheme.equals(this.scheme) && ((Challenge)var1).realm.equals(this.realm);
   }

   public int hashCode() {
      return (29 * 31 + this.realm.hashCode()) * 31 + this.scheme.hashCode();
   }

   public String realm() {
      return this.realm;
   }

   public String scheme() {
      return this.scheme;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.scheme);
      var1.append(" realm=\"");
      var1.append(this.realm);
      var1.append("\"");
      return var1.toString();
   }
}
