package org.apache.http.client.entity;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

public class UrlEncodedFormEntity extends StringEntity {
   public UrlEncodedFormEntity(Iterable var1) {
      this((Iterable)var1, (Charset)null);
   }

   public UrlEncodedFormEntity(Iterable var1, Charset var2) {
      Charset var3;
      if (var2 != null) {
         var3 = var2;
      } else {
         var3 = HTTP.DEF_CONTENT_CHARSET;
      }

      super(URLEncodedUtils.format(var1, var3), ContentType.create("application/x-www-form-urlencoded", var2));
   }

   public UrlEncodedFormEntity(List var1) throws UnsupportedEncodingException {
      this((Iterable)var1, (Charset)((Charset)null));
   }

   public UrlEncodedFormEntity(List var1, String var2) throws UnsupportedEncodingException {
      String var3;
      if (var2 != null) {
         var3 = var2;
      } else {
         var3 = HTTP.DEF_CONTENT_CHARSET.name();
      }

      super(URLEncodedUtils.format(var1, var3), ContentType.create("application/x-www-form-urlencoded", var2));
   }
}
