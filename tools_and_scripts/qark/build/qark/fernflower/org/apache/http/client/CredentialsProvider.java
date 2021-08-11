package org.apache.http.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;

public interface CredentialsProvider {
   void clear();

   Credentials getCredentials(AuthScope var1);

   void setCredentials(AuthScope var1, Credentials var2);
}
