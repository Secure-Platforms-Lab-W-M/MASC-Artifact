package org.apache.http.impl.entity;

import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ProtocolException;
import org.apache.http.entity.ContentLengthStrategy;

public class DisallowIdentityContentLengthStrategy implements ContentLengthStrategy {
   public static final DisallowIdentityContentLengthStrategy INSTANCE = new DisallowIdentityContentLengthStrategy(new LaxContentLengthStrategy(0));
   private final ContentLengthStrategy contentLengthStrategy;

   public DisallowIdentityContentLengthStrategy(ContentLengthStrategy var1) {
      this.contentLengthStrategy = var1;
   }

   public long determineLength(HttpMessage var1) throws HttpException {
      long var2 = this.contentLengthStrategy.determineLength(var1);
      if (var2 != -1L) {
         return var2;
      } else {
         throw new ProtocolException("Identity transfer encoding cannot be used");
      }
   }
}
