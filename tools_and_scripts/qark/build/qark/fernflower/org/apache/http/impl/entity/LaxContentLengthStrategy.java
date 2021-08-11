package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.util.Args;

public class LaxContentLengthStrategy implements ContentLengthStrategy {
   public static final LaxContentLengthStrategy INSTANCE = new LaxContentLengthStrategy();
   private final int implicitLen;

   public LaxContentLengthStrategy() {
      this(-1);
   }

   public LaxContentLengthStrategy(int var1) {
      this.implicitLen = var1;
   }

   public long determineLength(HttpMessage var1) throws HttpException {
      Args.notNull(var1, "HTTP message");
      Header var9 = var1.getFirstHeader("Transfer-Encoding");
      long var5 = -1L;
      int var2;
      if (var9 != null) {
         HeaderElement[] var14;
         try {
            var14 = var9.getElements();
         } catch (ParseException var11) {
            StringBuilder var10 = new StringBuilder();
            var10.append("Invalid Transfer-Encoding header value: ");
            var10.append(var9);
            throw new ProtocolException(var10.toString(), var11);
         }

         var2 = var14.length;
         if ("identity".equalsIgnoreCase(var9.getValue())) {
            return -1L;
         } else {
            return var2 > 0 && "chunked".equalsIgnoreCase(var14[var2 - 1].getName()) ? -2L : -1L;
         }
      } else if (var1.getFirstHeader("Content-Length") == null) {
         return (long)this.implicitLen;
      } else {
         long var7 = -1L;
         Header[] var13 = var1.getHeaders("Content-Length");
         var2 = var13.length - 1;

         long var3;
         while(true) {
            var3 = var7;
            if (var2 < 0) {
               break;
            }

            var9 = var13[var2];

            try {
               var3 = Long.parseLong(var9.getValue());
               break;
            } catch (NumberFormatException var12) {
               --var2;
            }
         }

         if (var3 >= 0L) {
            var5 = var3;
         }

         return var5;
      }
   }
}
