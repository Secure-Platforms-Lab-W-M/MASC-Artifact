package org.apache.http;

public class TruncatedChunkException extends MalformedChunkCodingException {
   private static final long serialVersionUID = -23506263930279460L;

   public TruncatedChunkException(String var1) {
      super(var1);
   }

   public TruncatedChunkException(String var1, Object... var2) {
      super(HttpException.clean(String.format(var1, var2)));
   }
}
