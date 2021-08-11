package org.apache.http.impl.entity;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

@Deprecated
public class EntityDeserializer {
   private final ContentLengthStrategy lenStrategy;

   public EntityDeserializer(ContentLengthStrategy var1) {
      this.lenStrategy = (ContentLengthStrategy)Args.notNull(var1, "Content length strategy");
   }

   public HttpEntity deserialize(SessionInputBuffer var1, HttpMessage var2) throws HttpException, IOException {
      Args.notNull(var1, "Session input buffer");
      Args.notNull(var2, "HTTP message");
      return this.doDeserialize(var1, var2);
   }

   protected BasicHttpEntity doDeserialize(SessionInputBuffer var1, HttpMessage var2) throws HttpException, IOException {
      BasicHttpEntity var5 = new BasicHttpEntity();
      long var3 = this.lenStrategy.determineLength(var2);
      if (var3 == -2L) {
         var5.setChunked(true);
         var5.setContentLength(-1L);
         var5.setContent(new ChunkedInputStream(var1));
      } else if (var3 == -1L) {
         var5.setChunked(false);
         var5.setContentLength(-1L);
         var5.setContent(new IdentityInputStream(var1));
      } else {
         var5.setChunked(false);
         var5.setContentLength(var3);
         var5.setContent(new ContentLengthInputStream(var1, var3));
      }

      Header var6 = var2.getFirstHeader("Content-Type");
      if (var6 != null) {
         var5.setContentType(var6);
      }

      var6 = var2.getFirstHeader("Content-Encoding");
      if (var6 != null) {
         var5.setContentEncoding(var6);
      }

      return var5;
   }
}
