package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

public class UsernamePasswordCredentials implements Credentials, Serializable {
   private static final long serialVersionUID = 243343858802739403L;
   private final String password;
   private final BasicUserPrincipal principal;

   @Deprecated
   public UsernamePasswordCredentials(String var1) {
      Args.notNull(var1, "Username:password string");
      int var2 = var1.indexOf(58);
      if (var2 >= 0) {
         this.principal = new BasicUserPrincipal(var1.substring(0, var2));
         this.password = var1.substring(var2 + 1);
      } else {
         this.principal = new BasicUserPrincipal(var1);
         this.password = null;
      }
   }

   public UsernamePasswordCredentials(String var1, String var2) {
      Args.notNull(var1, "Username");
      this.principal = new BasicUserPrincipal(var1);
      this.password = var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         if (var1 instanceof UsernamePasswordCredentials) {
            UsernamePasswordCredentials var2 = (UsernamePasswordCredentials)var1;
            if (LangUtils.equals(this.principal, var2.principal)) {
               return true;
            }
         }

         return false;
      }
   }

   public String getPassword() {
      return this.password;
   }

   public String getUserName() {
      return this.principal.getName();
   }

   public Principal getUserPrincipal() {
      return this.principal;
   }

   public int hashCode() {
      return this.principal.hashCode();
   }

   public String toString() {
      return this.principal.toString();
   }
}
