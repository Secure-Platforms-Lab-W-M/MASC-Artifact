package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.util.Args;

public class StrictContentLengthStrategy implements ContentLengthStrategy {
   public static final StrictContentLengthStrategy INSTANCE = new StrictContentLengthStrategy();
   private final int implicitLen;

   public StrictContentLengthStrategy() {
      this(-1);
   }

   public StrictContentLengthStrategy(int var1) {
      this.implicitLen = var1;
   }

   public long determineLength(HttpMessage var1) throws HttpException {
      Args.notNull(var1, "HTTP message");
      Header var4 = var1.getFirstHeader("Transfer-Encoding");
      StringBuilder var10;
      if (var4 != null) {
         String var11 = var4.getValue();
         if ("chunked".equalsIgnoreCase(var11)) {
            if (!var1.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
               return -2L;
            } else {
               var10 = new StringBuilder();
               var10.append("Chunked transfer encoding not allowed for ");
               var10.append(var1.getProtocolVersion());
               throw new ProtocolException(var10.toString());
            }
         } else if ("identity".equalsIgnoreCase(var11)) {
            return -1L;
         } else {
            StringBuilder var9 = new StringBuilder();
            var9.append("Unsupported transfer encoding: ");
            var9.append(var11);
            throw new ProtocolException(var9.toString());
         }
      } else {
         Header var7 = var1.getFirstHeader("Content-Length");
         if (var7 != null) {
            String var8 = var7.getValue();

            label48: {
               boolean var10001;
               long var2;
               try {
                  var2 = Long.parseLong(var8);
               } catch (NumberFormatException var6) {
                  var10001 = false;
                  break label48;
               }

               if (var2 >= 0L) {
                  return var2;
               }

               try {
                  var10 = new StringBuilder();
                  var10.append("Negative content length: ");
                  var10.append(var8);
                  throw new ProtocolException(var10.toString());
               } catch (NumberFormatException var5) {
                  var10001 = false;
               }
            }

            var10 = new StringBuilder();
            var10.append("Invalid content length: ");
            var10.append(var8);
            throw new ProtocolException(var10.toString());
         } else {
            return (long)this.implicitLen;
         }
      }
   }
}
