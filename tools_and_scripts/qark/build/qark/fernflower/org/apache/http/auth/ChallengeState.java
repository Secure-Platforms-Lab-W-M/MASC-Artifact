package org.apache.http.auth;

public enum ChallengeState {
   PROXY,
   TARGET;

   static {
      ChallengeState var0 = new ChallengeState("PROXY", 1);
      PROXY = var0;
   }
}
