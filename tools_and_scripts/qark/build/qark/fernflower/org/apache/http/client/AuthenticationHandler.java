package org.apache.http.client;

import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface AuthenticationHandler {
   Map getChallenges(HttpResponse var1, HttpContext var2) throws MalformedChallengeException;

   boolean isAuthenticationRequested(HttpResponse var1, HttpContext var2);

   AuthScheme selectScheme(Map var1, HttpResponse var2, HttpContext var3) throws AuthenticationException;
}
