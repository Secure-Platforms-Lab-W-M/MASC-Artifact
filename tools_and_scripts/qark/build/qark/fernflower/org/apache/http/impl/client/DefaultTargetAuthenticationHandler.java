package org.apache.http.impl.client;

import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
public class DefaultTargetAuthenticationHandler extends AbstractAuthenticationHandler {
   protected List getAuthPreferences(HttpResponse var1, HttpContext var2) {
      List var3 = (List)var1.getParams().getParameter("http.auth.target-scheme-pref");
      return var3 != null ? var3 : super.getAuthPreferences(var1, var2);
   }

   public Map getChallenges(HttpResponse var1, HttpContext var2) throws MalformedChallengeException {
      Args.notNull(var1, "HTTP response");
      return this.parseChallenges(var1.getHeaders("WWW-Authenticate"));
   }

   public boolean isAuthenticationRequested(HttpResponse var1, HttpContext var2) {
      Args.notNull(var1, "HTTP response");
      return var1.getStatusLine().getStatusCode() == 401;
   }
}
