package org.apache.http.cookie;

public class CookieRestrictionViolationException extends MalformedCookieException {
   private static final long serialVersionUID = 7371235577078589013L;

   public CookieRestrictionViolationException() {
   }

   public CookieRestrictionViolationException(String var1) {
      super(var1);
   }
}
