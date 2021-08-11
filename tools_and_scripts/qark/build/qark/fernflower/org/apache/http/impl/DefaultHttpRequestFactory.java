package org.apache.http.impl;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.util.Args;

public class DefaultHttpRequestFactory implements HttpRequestFactory {
   public static final DefaultHttpRequestFactory INSTANCE = new DefaultHttpRequestFactory();
   private static final String[] RFC2616_COMMON_METHODS = new String[]{"GET"};
   private static final String[] RFC2616_ENTITY_ENC_METHODS = new String[]{"POST", "PUT"};
   private static final String[] RFC2616_SPECIAL_METHODS = new String[]{"HEAD", "OPTIONS", "DELETE", "TRACE", "CONNECT"};
   private static final String[] RFC5789_ENTITY_ENC_METHODS = new String[]{"PATCH"};

   private static boolean isOneOf(String[] var0, String var1) {
      int var3 = var0.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var0[var2].equalsIgnoreCase(var1)) {
            return true;
         }
      }

      return false;
   }

   public HttpRequest newHttpRequest(String var1, String var2) throws MethodNotSupportedException {
      if (isOneOf(RFC2616_COMMON_METHODS, var1)) {
         return new BasicHttpRequest(var1, var2);
      } else if (isOneOf(RFC2616_ENTITY_ENC_METHODS, var1)) {
         return new BasicHttpEntityEnclosingRequest(var1, var2);
      } else if (isOneOf(RFC2616_SPECIAL_METHODS, var1)) {
         return new BasicHttpRequest(var1, var2);
      } else if (isOneOf(RFC5789_ENTITY_ENC_METHODS, var1)) {
         return new BasicHttpEntityEnclosingRequest(var1, var2);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(" method not supported");
         throw new MethodNotSupportedException(var3.toString());
      }
   }

   public HttpRequest newHttpRequest(RequestLine var1) throws MethodNotSupportedException {
      Args.notNull(var1, "Request line");
      String var2 = var1.getMethod();
      if (isOneOf(RFC2616_COMMON_METHODS, var2)) {
         return new BasicHttpRequest(var1);
      } else if (isOneOf(RFC2616_ENTITY_ENC_METHODS, var2)) {
         return new BasicHttpEntityEnclosingRequest(var1);
      } else if (isOneOf(RFC2616_SPECIAL_METHODS, var2)) {
         return new BasicHttpRequest(var1);
      } else if (isOneOf(RFC5789_ENTITY_ENC_METHODS, var2)) {
         return new BasicHttpEntityEnclosingRequest(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var2);
         var3.append(" method not supported");
         throw new MethodNotSupportedException(var3.toString());
      }
   }
}
