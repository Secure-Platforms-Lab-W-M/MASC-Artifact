package org.apache.http.auth;

import java.util.Queue;
import org.apache.http.util.Args;

public class AuthState {
   private Queue authOptions;
   private AuthScheme authScheme;
   private AuthScope authScope;
   private Credentials credentials;
   private AuthProtocolState state;

   public AuthState() {
      this.state = AuthProtocolState.UNCHALLENGED;
   }

   public Queue getAuthOptions() {
      return this.authOptions;
   }

   public AuthScheme getAuthScheme() {
      return this.authScheme;
   }

   @Deprecated
   public AuthScope getAuthScope() {
      return this.authScope;
   }

   public Credentials getCredentials() {
      return this.credentials;
   }

   public AuthProtocolState getState() {
      return this.state;
   }

   public boolean hasAuthOptions() {
      Queue var1 = this.authOptions;
      return var1 != null && !var1.isEmpty();
   }

   @Deprecated
   public void invalidate() {
      this.reset();
   }

   public boolean isConnectionBased() {
      AuthScheme var1 = this.authScheme;
      return var1 != null && var1.isConnectionBased();
   }

   @Deprecated
   public boolean isValid() {
      return this.authScheme != null;
   }

   public void reset() {
      this.state = AuthProtocolState.UNCHALLENGED;
      this.authOptions = null;
      this.authScheme = null;
      this.authScope = null;
      this.credentials = null;
   }

   @Deprecated
   public void setAuthScheme(AuthScheme var1) {
      if (var1 == null) {
         this.reset();
      } else {
         this.authScheme = var1;
      }
   }

   @Deprecated
   public void setAuthScope(AuthScope var1) {
      this.authScope = var1;
   }

   @Deprecated
   public void setCredentials(Credentials var1) {
      this.credentials = var1;
   }

   public void setState(AuthProtocolState var1) {
      if (var1 == null) {
         var1 = AuthProtocolState.UNCHALLENGED;
      }

      this.state = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("state:");
      var1.append(this.state);
      var1.append(";");
      if (this.authScheme != null) {
         var1.append("auth scheme:");
         var1.append(this.authScheme.getSchemeName());
         var1.append(";");
      }

      if (this.credentials != null) {
         var1.append("credentials present");
      }

      return var1.toString();
   }

   public void update(Queue var1) {
      Args.notEmpty(var1, "Queue of auth options");
      this.authOptions = var1;
      this.authScheme = null;
      this.credentials = null;
   }

   public void update(AuthScheme var1, Credentials var2) {
      Args.notNull(var1, "Auth scheme");
      Args.notNull(var2, "Credentials");
      this.authScheme = var1;
      this.credentials = var2;
      this.authOptions = null;
   }
}
