package org.apache.http.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;

public interface AuthScheme {
   @Deprecated
   Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException;

   String getParameter(String var1);

   String getRealm();

   String getSchemeName();

   boolean isComplete();

   boolean isConnectionBased();

   void processChallenge(Header var1) throws MalformedChallengeException;
}
