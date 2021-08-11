package org.apache.http.entity.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;

abstract class AbstractMultipartForm {
   private static final ByteArrayBuffer CR_LF;
   private static final ByteArrayBuffer FIELD_SEP;
   private static final ByteArrayBuffer TWO_DASHES;
   final String boundary;
   final Charset charset;

   static {
      FIELD_SEP = encode(MIME.DEFAULT_CHARSET, ": ");
      CR_LF = encode(MIME.DEFAULT_CHARSET, "\r\n");
      TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
   }

   public AbstractMultipartForm(String var1) {
      this((Charset)null, var1);
   }

   public AbstractMultipartForm(Charset var1, String var2) {
      Args.notNull(var2, "Multipart boundary");
      if (var1 == null) {
         var1 = MIME.DEFAULT_CHARSET;
      }

      this.charset = var1;
      this.boundary = var2;
   }

   private static ByteArrayBuffer encode(Charset var0, String var1) {
      ByteBuffer var2 = var0.encode(CharBuffer.wrap(var1));
      ByteArrayBuffer var3 = new ByteArrayBuffer(var2.remaining());
      var3.append(var2.array(), var2.position(), var2.remaining());
      return var3;
   }

   private static void writeBytes(String var0, OutputStream var1) throws IOException {
      writeBytes(encode(MIME.DEFAULT_CHARSET, var0), var1);
   }

   private static void writeBytes(String var0, Charset var1, OutputStream var2) throws IOException {
      writeBytes(encode(var1, var0), var2);
   }

   private static void writeBytes(ByteArrayBuffer var0, OutputStream var1) throws IOException {
      var1.write(var0.buffer(), 0, var0.length());
   }

   protected static void writeField(MinimalField var0, OutputStream var1) throws IOException {
      writeBytes(var0.getName(), var1);
      writeBytes(FIELD_SEP, var1);
      writeBytes(var0.getBody(), var1);
      writeBytes(CR_LF, var1);
   }

   protected static void writeField(MinimalField var0, Charset var1, OutputStream var2) throws IOException {
      writeBytes(var0.getName(), var1, var2);
      writeBytes(FIELD_SEP, var2);
      writeBytes(var0.getBody(), var1, var2);
      writeBytes(CR_LF, var2);
   }

   void doWriteTo(OutputStream var1, boolean var2) throws IOException {
      ByteArrayBuffer var3 = encode(this.charset, this.boundary);

      for(Iterator var4 = this.getBodyParts().iterator(); var4.hasNext(); writeBytes(CR_LF, var1)) {
         FormBodyPart var5 = (FormBodyPart)var4.next();
         writeBytes(TWO_DASHES, var1);
         writeBytes(var3, var1);
         writeBytes(CR_LF, var1);
         this.formatMultipartHeader(var5, var1);
         writeBytes(CR_LF, var1);
         if (var2) {
            var5.getBody().writeTo(var1);
         }
      }

      writeBytes(TWO_DASHES, var1);
      writeBytes(var3, var1);
      writeBytes(TWO_DASHES, var1);
      writeBytes(CR_LF, var1);
   }

   protected abstract void formatMultipartHeader(FormBodyPart var1, OutputStream var2) throws IOException;

   public abstract List getBodyParts();

   public long getTotalLength() {
      long var2 = 0L;

      long var4;
      for(Iterator var6 = this.getBodyParts().iterator(); var6.hasNext(); var2 += var4) {
         var4 = ((FormBodyPart)var6.next()).getBody().getContentLength();
         if (var4 < 0L) {
            return -1L;
         }
      }

      ByteArrayOutputStream var8 = new ByteArrayOutputStream();

      int var1;
      try {
         this.doWriteTo(var8, false);
         var1 = var8.toByteArray().length;
      } catch (IOException var7) {
         return -1L;
      }

      return (long)var1 + var2;
   }

   public void writeTo(OutputStream var1) throws IOException {
      this.doWriteTo(var1, true);
   }
}
