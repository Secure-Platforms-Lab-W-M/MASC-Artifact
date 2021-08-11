package org.apache.http.entity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.TextUtils;

public final class ContentType implements Serializable {
   public static final ContentType APPLICATION_ATOM_XML;
   public static final ContentType APPLICATION_FORM_URLENCODED;
   public static final ContentType APPLICATION_JSON;
   public static final ContentType APPLICATION_OCTET_STREAM;
   public static final ContentType APPLICATION_SOAP_XML;
   public static final ContentType APPLICATION_SVG_XML;
   public static final ContentType APPLICATION_XHTML_XML;
   public static final ContentType APPLICATION_XML;
   private static final Map CONTENT_TYPE_MAP;
   public static final ContentType DEFAULT_BINARY;
   public static final ContentType DEFAULT_TEXT;
   public static final ContentType IMAGE_BMP;
   public static final ContentType IMAGE_GIF;
   public static final ContentType IMAGE_JPEG;
   public static final ContentType IMAGE_PNG;
   public static final ContentType IMAGE_SVG;
   public static final ContentType IMAGE_TIFF;
   public static final ContentType IMAGE_WEBP;
   public static final ContentType MULTIPART_FORM_DATA;
   public static final ContentType TEXT_HTML;
   public static final ContentType TEXT_PLAIN;
   public static final ContentType TEXT_XML;
   public static final ContentType WILDCARD;
   private static final long serialVersionUID = -7768694718232371896L;
   private final Charset charset;
   private final String mimeType;
   private final NameValuePair[] params;

   static {
      APPLICATION_ATOM_XML = create("application/atom+xml", Consts.ISO_8859_1);
      APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
      APPLICATION_JSON = create("application/json", Consts.UTF_8);
      Charset var2 = (Charset)null;
      APPLICATION_OCTET_STREAM = create("application/octet-stream", var2);
      APPLICATION_SOAP_XML = create("application/soap+xml", Consts.UTF_8);
      APPLICATION_SVG_XML = create("application/svg+xml", Consts.ISO_8859_1);
      APPLICATION_XHTML_XML = create("application/xhtml+xml", Consts.ISO_8859_1);
      APPLICATION_XML = create("application/xml", Consts.ISO_8859_1);
      IMAGE_BMP = create("image/bmp");
      IMAGE_GIF = create("image/gif");
      IMAGE_JPEG = create("image/jpeg");
      IMAGE_PNG = create("image/png");
      IMAGE_SVG = create("image/svg+xml");
      IMAGE_TIFF = create("image/tiff");
      IMAGE_WEBP = create("image/webp");
      MULTIPART_FORM_DATA = create("multipart/form-data", Consts.ISO_8859_1);
      TEXT_HTML = create("text/html", Consts.ISO_8859_1);
      TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
      TEXT_XML = create("text/xml", Consts.ISO_8859_1);
      WILDCARD = create("*/*", var2);
      ContentType[] var5 = new ContentType[]{APPLICATION_ATOM_XML, APPLICATION_FORM_URLENCODED, APPLICATION_JSON, APPLICATION_SVG_XML, APPLICATION_XHTML_XML, APPLICATION_XML, IMAGE_BMP, IMAGE_GIF, IMAGE_JPEG, IMAGE_PNG, IMAGE_SVG, IMAGE_TIFF, IMAGE_WEBP, MULTIPART_FORM_DATA, TEXT_HTML, TEXT_PLAIN, TEXT_XML};
      HashMap var3 = new HashMap();
      int var1 = var5.length;

      for(int var0 = 0; var0 < var1; ++var0) {
         ContentType var4 = var5[var0];
         var3.put(var4.getMimeType(), var4);
      }

      CONTENT_TYPE_MAP = Collections.unmodifiableMap(var3);
      DEFAULT_TEXT = TEXT_PLAIN;
      DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
   }

   ContentType(String var1, Charset var2) {
      this.mimeType = var1;
      this.charset = var2;
      this.params = null;
   }

   ContentType(String var1, Charset var2, NameValuePair[] var3) {
      this.mimeType = var1;
      this.charset = var2;
      this.params = var3;
   }

   public static ContentType create(String var0) {
      return create(var0, (Charset)null);
   }

   public static ContentType create(String var0, String var1) throws UnsupportedCharsetException {
      Charset var2;
      if (!TextUtils.isBlank(var1)) {
         var2 = Charset.forName(var1);
      } else {
         var2 = null;
      }

      return create(var0, var2);
   }

   public static ContentType create(String var0, Charset var1) {
      var0 = ((String)Args.notBlank(var0, "MIME type")).toLowerCase(Locale.ROOT);
      Args.check(valid(var0), "MIME type may not contain reserved characters");
      return new ContentType(var0, var1);
   }

   public static ContentType create(String var0, NameValuePair... var1) throws UnsupportedCharsetException {
      Args.check(valid(((String)Args.notBlank(var0, "MIME type")).toLowerCase(Locale.ROOT)), "MIME type may not contain reserved characters");
      return create(var0, var1, true);
   }

   private static ContentType create(String var0, NameValuePair[] var1, boolean var2) {
      Object var7 = null;
      Object var6 = null;
      int var4 = var1.length;
      int var3 = 0;

      Charset var5;
      while(true) {
         var5 = (Charset)var7;
         if (var3 >= var4) {
            break;
         }

         NameValuePair var10 = var1[var3];
         if (var10.getName().equalsIgnoreCase("charset")) {
            String var8 = var10.getValue();
            var5 = (Charset)var7;
            if (TextUtils.isBlank(var8)) {
               break;
            }

            try {
               var5 = Charset.forName(var8);
               break;
            } catch (UnsupportedCharsetException var9) {
               if (!var2) {
                  var5 = (Charset)var6;
                  break;
               }

               throw var9;
            }
         }

         ++var3;
      }

      if (var1 == null || var1.length <= 0) {
         var1 = null;
      }

      return new ContentType(var0, var5, var1);
   }

   private static ContentType create(HeaderElement var0, boolean var1) {
      return create(var0.getName(), var0.getParameters(), var1);
   }

   public static ContentType get(HttpEntity var0) throws ParseException, UnsupportedCharsetException {
      if (var0 == null) {
         return null;
      } else {
         Header var1 = var0.getContentType();
         if (var1 != null) {
            HeaderElement[] var2 = var1.getElements();
            if (var2.length > 0) {
               return create(var2[0], true);
            }
         }

         return null;
      }
   }

   public static ContentType getByMimeType(String var0) {
      return var0 == null ? null : (ContentType)CONTENT_TYPE_MAP.get(var0);
   }

   public static ContentType getLenient(HttpEntity var0) {
      if (var0 == null) {
         return null;
      } else {
         Header var2 = var0.getContentType();
         if (var2 != null) {
            try {
               HeaderElement[] var3 = var2.getElements();
               if (var3.length > 0) {
                  ContentType var4 = create(var3[0], false);
                  return var4;
               } else {
                  return null;
               }
            } catch (ParseException var1) {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public static ContentType getLenientOrDefault(HttpEntity var0) throws ParseException, UnsupportedCharsetException {
      ContentType var1 = get(var0);
      return var1 != null ? var1 : DEFAULT_TEXT;
   }

   public static ContentType getOrDefault(HttpEntity var0) throws ParseException, UnsupportedCharsetException {
      ContentType var1 = get(var0);
      return var1 != null ? var1 : DEFAULT_TEXT;
   }

   public static ContentType parse(String var0) throws ParseException, UnsupportedCharsetException {
      Args.notNull(var0, "Content type");
      CharArrayBuffer var1 = new CharArrayBuffer(var0.length());
      var1.append(var0);
      ParserCursor var2 = new ParserCursor(0, var0.length());
      HeaderElement[] var3 = BasicHeaderValueParser.INSTANCE.parseElements(var1, var2);
      if (var3.length > 0) {
         return create(var3[0], true);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid content type: ");
         var4.append(var0);
         throw new ParseException(var4.toString());
      }
   }

   private static boolean valid(String var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         char var2 = var0.charAt(var1);
         if (var2 == '"' || var2 == ',' || var2 == ';') {
            return false;
         }
      }

      return true;
   }

   public Charset getCharset() {
      return this.charset;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public String getParameter(String var1) {
      Args.notEmpty(var1, "Parameter name");
      if (this.params == null) {
         return null;
      } else {
         NameValuePair[] var4 = this.params;
         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            NameValuePair var5 = var4[var2];
            if (var5.getName().equalsIgnoreCase(var1)) {
               return var5.getValue();
            }
         }

         return null;
      }
   }

   public String toString() {
      CharArrayBuffer var1 = new CharArrayBuffer(64);
      var1.append(this.mimeType);
      if (this.params != null) {
         var1.append("; ");
         BasicHeaderValueFormatter.INSTANCE.formatParameters(var1, this.params, false);
      } else if (this.charset != null) {
         var1.append("; charset=");
         var1.append(this.charset.name());
      }

      return var1.toString();
   }

   public ContentType withCharset(String var1) {
      return create(this.getMimeType(), var1);
   }

   public ContentType withCharset(Charset var1) {
      return create(this.getMimeType(), var1);
   }

   public ContentType withParameters(NameValuePair... var1) throws UnsupportedCharsetException {
      if (var1.length == 0) {
         return this;
      } else {
         LinkedHashMap var4 = new LinkedHashMap();
         int var2;
         int var3;
         if (this.params != null) {
            NameValuePair[] var5 = this.params;
            var3 = var5.length;

            for(var2 = 0; var2 < var3; ++var2) {
               NameValuePair var6 = var5[var2];
               var4.put(var6.getName(), var6.getValue());
            }
         }

         var3 = var1.length;

         for(var2 = 0; var2 < var3; ++var2) {
            NameValuePair var9 = var1[var2];
            var4.put(var9.getName(), var9.getValue());
         }

         ArrayList var7 = new ArrayList(var4.size() + 1);
         if (this.charset != null && !var4.containsKey("charset")) {
            var7.add(new BasicNameValuePair("charset", this.charset.name()));
         }

         Iterator var8 = var4.entrySet().iterator();

         while(var8.hasNext()) {
            Entry var10 = (Entry)var8.next();
            var7.add(new BasicNameValuePair((String)var10.getKey(), (String)var10.getValue()));
         }

         return create(this.getMimeType(), (NameValuePair[])var7.toArray(new NameValuePair[var7.size()]), true);
      }
   }
}
