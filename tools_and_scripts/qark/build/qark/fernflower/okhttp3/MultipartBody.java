package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

public final class MultipartBody extends RequestBody {
   public static final MediaType ALTERNATIVE = MediaType.parse("multipart/alternative");
   private static final byte[] COLONSPACE = new byte[]{58, 32};
   private static final byte[] CRLF = new byte[]{13, 10};
   private static final byte[] DASHDASH = new byte[]{45, 45};
   public static final MediaType DIGEST = MediaType.parse("multipart/digest");
   public static final MediaType FORM = MediaType.parse("multipart/form-data");
   public static final MediaType MIXED = MediaType.parse("multipart/mixed");
   public static final MediaType PARALLEL = MediaType.parse("multipart/parallel");
   private final ByteString boundary;
   private long contentLength = -1L;
   private final MediaType contentType;
   private final MediaType originalType;
   private final List parts;

   MultipartBody(ByteString var1, MediaType var2, List var3) {
      this.boundary = var1;
      this.originalType = var2;
      StringBuilder var4 = new StringBuilder();
      var4.append(var2);
      var4.append("; boundary=");
      var4.append(var1.utf8());
      this.contentType = MediaType.parse(var4.toString());
      this.parts = Util.immutableList(var3);
   }

   static StringBuilder appendQuotedString(StringBuilder var0, String var1) {
      var0.append('"');
      int var3 = 0;

      for(int var4 = var1.length(); var3 < var4; ++var3) {
         char var2 = var1.charAt(var3);
         if (var2 != '\n') {
            if (var2 != '\r') {
               if (var2 != '"') {
                  var0.append(var2);
               } else {
                  var0.append("%22");
               }
            } else {
               var0.append("%0D");
            }
         } else {
            var0.append("%0A");
         }
      }

      var0.append('"');
      return var0;
   }

   private long writeOrCountBytes(@Nullable BufferedSink var1, boolean var2) throws IOException {
      long var7 = 0L;
      Headers var12 = null;
      Object var11;
      Object var14;
      if (var2) {
         var11 = new Buffer();
         var14 = var11;
      } else {
         var11 = var1;
         var14 = var12;
      }

      int var3 = 0;

      long var9;
      for(int var5 = this.parts.size(); var3 < var5; ++var3) {
         MultipartBody.Part var13 = (MultipartBody.Part)this.parts.get(var3);
         var12 = var13.headers;
         RequestBody var16 = var13.body;
         ((BufferedSink)var11).write(DASHDASH);
         ((BufferedSink)var11).write(this.boundary);
         ((BufferedSink)var11).write(CRLF);
         if (var12 != null) {
            int var4 = 0;

            for(int var6 = var12.size(); var4 < var6; ++var4) {
               ((BufferedSink)var11).writeUtf8(var12.name(var4)).write(COLONSPACE).writeUtf8(var12.value(var4)).write(CRLF);
            }
         }

         MediaType var15 = var16.contentType();
         if (var15 != null) {
            ((BufferedSink)var11).writeUtf8("Content-Type: ").writeUtf8(var15.toString()).write(CRLF);
         }

         var9 = var16.contentLength();
         if (var9 != -1L) {
            ((BufferedSink)var11).writeUtf8("Content-Length: ").writeDecimalLong(var9).write(CRLF);
         } else if (var2) {
            ((Buffer)var14).clear();
            return -1L;
         }

         ((BufferedSink)var11).write(CRLF);
         if (var2) {
            var7 += var9;
         } else {
            var16.writeTo((BufferedSink)var11);
         }

         ((BufferedSink)var11).write(CRLF);
      }

      ((BufferedSink)var11).write(DASHDASH);
      ((BufferedSink)var11).write(this.boundary);
      ((BufferedSink)var11).write(DASHDASH);
      ((BufferedSink)var11).write(CRLF);
      var9 = var7;
      if (var2) {
         var9 = var7 + ((Buffer)var14).size();
         ((Buffer)var14).clear();
      }

      return var9;
   }

   public String boundary() {
      return this.boundary.utf8();
   }

   public long contentLength() throws IOException {
      long var1 = this.contentLength;
      if (var1 != -1L) {
         return var1;
      } else {
         var1 = this.writeOrCountBytes((BufferedSink)null, true);
         this.contentLength = var1;
         return var1;
      }
   }

   public MediaType contentType() {
      return this.contentType;
   }

   public MultipartBody.Part part(int var1) {
      return (MultipartBody.Part)this.parts.get(var1);
   }

   public List parts() {
      return this.parts;
   }

   public int size() {
      return this.parts.size();
   }

   public MediaType type() {
      return this.originalType;
   }

   public void writeTo(BufferedSink var1) throws IOException {
      this.writeOrCountBytes(var1, false);
   }

   public static final class Builder {
      private final ByteString boundary;
      private final List parts;
      private MediaType type;

      public Builder() {
         this(UUID.randomUUID().toString());
      }

      public Builder(String var1) {
         this.type = MultipartBody.MIXED;
         this.parts = new ArrayList();
         this.boundary = ByteString.encodeUtf8(var1);
      }

      public MultipartBody.Builder addFormDataPart(String var1, String var2) {
         return this.addPart(MultipartBody.Part.createFormData(var1, var2));
      }

      public MultipartBody.Builder addFormDataPart(String var1, @Nullable String var2, RequestBody var3) {
         return this.addPart(MultipartBody.Part.createFormData(var1, var2, var3));
      }

      public MultipartBody.Builder addPart(@Nullable Headers var1, RequestBody var2) {
         return this.addPart(MultipartBody.Part.create(var1, var2));
      }

      public MultipartBody.Builder addPart(MultipartBody.Part var1) {
         if (var1 != null) {
            this.parts.add(var1);
            return this;
         } else {
            throw new NullPointerException("part == null");
         }
      }

      public MultipartBody.Builder addPart(RequestBody var1) {
         return this.addPart(MultipartBody.Part.create(var1));
      }

      public MultipartBody build() {
         if (!this.parts.isEmpty()) {
            return new MultipartBody(this.boundary, this.type, this.parts);
         } else {
            throw new IllegalStateException("Multipart body must have at least one part.");
         }
      }

      public MultipartBody.Builder setType(MediaType var1) {
         if (var1 != null) {
            if (var1.type().equals("multipart")) {
               this.type = var1;
               return this;
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("multipart != ");
               var2.append(var1);
               throw new IllegalArgumentException(var2.toString());
            }
         } else {
            throw new NullPointerException("type == null");
         }
      }
   }

   public static final class Part {
      final RequestBody body;
      @Nullable
      final Headers headers;

      private Part(@Nullable Headers var1, RequestBody var2) {
         this.headers = var1;
         this.body = var2;
      }

      public static MultipartBody.Part create(@Nullable Headers var0, RequestBody var1) {
         if (var1 != null) {
            if (var0 != null && var0.get("Content-Type") != null) {
               throw new IllegalArgumentException("Unexpected header: Content-Type");
            } else if (var0 != null && var0.get("Content-Length") != null) {
               throw new IllegalArgumentException("Unexpected header: Content-Length");
            } else {
               return new MultipartBody.Part(var0, var1);
            }
         } else {
            throw new NullPointerException("body == null");
         }
      }

      public static MultipartBody.Part create(RequestBody var0) {
         return create((Headers)null, var0);
      }

      public static MultipartBody.Part createFormData(String var0, String var1) {
         return createFormData(var0, (String)null, RequestBody.create((MediaType)null, (String)var1));
      }

      public static MultipartBody.Part createFormData(String var0, @Nullable String var1, RequestBody var2) {
         if (var0 != null) {
            StringBuilder var3 = new StringBuilder("form-data; name=");
            MultipartBody.appendQuotedString(var3, var0);
            if (var1 != null) {
               var3.append("; filename=");
               MultipartBody.appendQuotedString(var3, var1);
            }

            return create(Headers.method_24("Content-Disposition", var3.toString()), var2);
         } else {
            throw new NullPointerException("name == null");
         }
      }

      public RequestBody body() {
         return this.body;
      }

      @Nullable
      public Headers headers() {
         return this.headers;
      }
   }
}
