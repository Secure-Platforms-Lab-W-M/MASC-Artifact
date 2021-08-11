package org.apache.http.conn;

import java.io.IOException;

public class UnsupportedSchemeException extends IOException {
   private static final long serialVersionUID = 3597127619218687636L;

   public UnsupportedSchemeException(String var1) {
      super(var1);
   }
}
