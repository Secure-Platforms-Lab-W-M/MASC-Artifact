package org.apache.http.impl.bootstrap;

import org.apache.http.ExceptionLogger;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.HttpService;

class Worker implements Runnable {
   private final HttpServerConnection conn;
   private final ExceptionLogger exceptionLogger;
   private final HttpService httpservice;

   Worker(HttpService var1, HttpServerConnection var2, ExceptionLogger var3) {
      this.httpservice = var1;
      this.conn = var2;
      this.exceptionLogger = var3;
   }

   public HttpServerConnection getConnection() {
      return this.conn;
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }
}
