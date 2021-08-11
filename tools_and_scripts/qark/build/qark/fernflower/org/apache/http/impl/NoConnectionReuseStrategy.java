package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public class NoConnectionReuseStrategy implements ConnectionReuseStrategy {
   public static final NoConnectionReuseStrategy INSTANCE = new NoConnectionReuseStrategy();

   public boolean keepAlive(HttpResponse var1, HttpContext var2) {
      return false;
   }
}
