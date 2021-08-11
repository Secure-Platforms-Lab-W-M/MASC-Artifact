package org.apache.http.auth;

public enum AuthProtocolState {
   CHALLENGED,
   FAILURE,
   HANDSHAKE,
   SUCCESS,
   UNCHALLENGED;

   static {
      AuthProtocolState var0 = new AuthProtocolState("SUCCESS", 4);
      SUCCESS = var0;
   }
}
