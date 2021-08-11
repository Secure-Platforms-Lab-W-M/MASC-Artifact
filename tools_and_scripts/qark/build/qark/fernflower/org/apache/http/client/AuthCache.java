package org.apache.http.client;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;

public interface AuthCache {
   void clear();

   AuthScheme get(HttpHost var1);

   void put(HttpHost var1, AuthScheme var2);

   void remove(HttpHost var1);
}
