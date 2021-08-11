package org.apache.http;

import java.io.IOException;

public class ContentTooLongException extends IOException {
   private static final long serialVersionUID = -924287689552495383L;

   public ContentTooLongException(String var1) {
      super(var1);
   }

   public ContentTooLongException(String var1, Object... var2) {
      super(HttpException.clean(String.format(var1, var2)));
   }
}
