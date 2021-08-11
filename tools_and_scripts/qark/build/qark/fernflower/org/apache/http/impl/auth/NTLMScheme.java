package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.NTCredentials;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class NTLMScheme extends AuthSchemeBase {
   private String challenge;
   private final NTLMEngine engine;
   private NTLMScheme.State state;

   public NTLMScheme() {
      this(new NTLMEngineImpl());
   }

   public NTLMScheme(NTLMEngine var1) {
      Args.notNull(var1, "NTLM engine");
      this.engine = var1;
      this.state = NTLMScheme.State.UNINITIATED;
      this.challenge = null;
   }

   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      NTCredentials var7;
      try {
         var7 = (NTCredentials)var1;
      } catch (ClassCastException var3) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Credentials cannot be used for NTLM authentication: ");
         var5.append(var1.getClass().getName());
         throw new InvalidCredentialsException(var5.toString());
      }

      if (this.state != NTLMScheme.State.FAILED) {
         String var4;
         if (this.state == NTLMScheme.State.CHALLENGE_RECEIVED) {
            var4 = this.engine.generateType1Msg(var7.getDomain(), var7.getWorkstation());
            this.state = NTLMScheme.State.MSG_TYPE1_GENERATED;
         } else {
            if (this.state != NTLMScheme.State.MSG_TYPE2_RECEVIED) {
               StringBuilder var6 = new StringBuilder();
               var6.append("Unexpected state: ");
               var6.append(this.state);
               throw new AuthenticationException(var6.toString());
            }

            var4 = this.engine.generateType3Msg(var7.getUserName(), var7.getPassword(), var7.getDomain(), var7.getWorkstation(), this.challenge);
            this.state = NTLMScheme.State.MSG_TYPE3_GENERATED;
         }

         CharArrayBuffer var8 = new CharArrayBuffer(32);
         if (this.isProxy()) {
            var8.append("Proxy-Authorization");
         } else {
            var8.append("Authorization");
         }

         var8.append(": NTLM ");
         var8.append(var4);
         return new BufferedHeader(var8);
      } else {
         throw new AuthenticationException("NTLM authentication failed");
      }
   }

   public String getParameter(String var1) {
      return null;
   }

   public String getRealm() {
      return null;
   }

   public String getSchemeName() {
      return "ntlm";
   }

   public boolean isComplete() {
      return this.state == NTLMScheme.State.MSG_TYPE3_GENERATED || this.state == NTLMScheme.State.FAILED;
   }

   public boolean isConnectionBased() {
      return true;
   }

   protected void parseChallenge(CharArrayBuffer var1, int var2, int var3) throws MalformedChallengeException {
      String var4 = var1.substringTrimmed(var2, var3);
      this.challenge = var4;
      if (var4.isEmpty()) {
         if (this.state == NTLMScheme.State.UNINITIATED) {
            this.state = NTLMScheme.State.CHALLENGE_RECEIVED;
         } else {
            this.state = NTLMScheme.State.FAILED;
         }
      } else if (this.state.compareTo(NTLMScheme.State.MSG_TYPE1_GENERATED) >= 0) {
         if (this.state == NTLMScheme.State.MSG_TYPE1_GENERATED) {
            this.state = NTLMScheme.State.MSG_TYPE2_RECEVIED;
         }

      } else {
         this.state = NTLMScheme.State.FAILED;
         throw new MalformedChallengeException("Out of sequence NTLM response message");
      }
   }

   static enum State {
      CHALLENGE_RECEIVED,
      FAILED,
      MSG_TYPE1_GENERATED,
      MSG_TYPE2_RECEVIED,
      MSG_TYPE3_GENERATED,
      UNINITIATED;

      static {
         NTLMScheme.State var0 = new NTLMScheme.State("FAILED", 5);
         FAILED = var0;
      }
   }
}
