package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;

abstract class RFC1522Codec {
   protected static final String POSTFIX = "?=";
   protected static final String PREFIX = "=?";
   protected static final char SEP = '?';

   protected String decodeText(String var1) throws DecoderException, UnsupportedEncodingException {
      if (var1 == null) {
         return null;
      } else if (var1.startsWith("=?") && var1.endsWith("?=")) {
         int var2 = var1.length() - 2;
         int var3 = var1.indexOf(63, 2);
         if (var3 != var2) {
            String var6 = var1.substring(2, var3);
            if (!var6.equals("")) {
               ++var3;
               int var4 = var1.indexOf(63, var3);
               if (var4 != var2) {
                  String var5 = var1.substring(var3, var4);
                  if (this.getEncoding().equalsIgnoreCase(var5)) {
                     var2 = var4 + 1;
                     return new String(this.doDecoding(StringUtils.getBytesUsAscii(var1.substring(var2, var1.indexOf(63, var2)))), var6);
                  } else {
                     StringBuilder var7 = new StringBuilder();
                     var7.append("This codec cannot decode ");
                     var7.append(var5);
                     var7.append(" encoded content");
                     throw new DecoderException(var7.toString());
                  }
               } else {
                  throw new DecoderException("RFC 1522 violation: encoding token not found");
               }
            } else {
               throw new DecoderException("RFC 1522 violation: charset not specified");
            }
         } else {
            throw new DecoderException("RFC 1522 violation: charset token not found");
         }
      } else {
         throw new DecoderException("RFC 1522 violation: malformed encoded content");
      }
   }

   protected abstract byte[] doDecoding(byte[] var1) throws DecoderException;

   protected abstract byte[] doEncoding(byte[] var1) throws EncoderException;

   protected String encodeText(String var1, String var2) throws EncoderException, UnsupportedEncodingException {
      return var1 == null ? null : this.encodeText(var1, Charset.forName(var2));
   }

   protected String encodeText(String var1, Charset var2) throws EncoderException {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("=?");
         var3.append(var2);
         var3.append('?');
         var3.append(this.getEncoding());
         var3.append('?');
         var3.append(StringUtils.newStringUsAscii(this.doEncoding(var1.getBytes(var2))));
         var3.append("?=");
         return var3.toString();
      }
   }

   protected abstract String getEncoding();
}
