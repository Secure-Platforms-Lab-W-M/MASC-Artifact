package org.apache.http;

import java.io.IOException;

public class ConnectionClosedException extends IOException {
   private static final long serialVersionUID = 617550366255636674L;

   public ConnectionClosedException() {
      super("Connection is closed");
   }

   public ConnectionClosedException(String var1) {
      super(HttpException.clean(var1));
   }

   public ConnectionClosedException(String var1, Object... var2) {
      super(HttpException.clean(String.format(var1, var2)));
   }
}
