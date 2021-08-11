package org.apache.http.impl;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import org.apache.http.config.ConnectionConfig;

public final class ConnSupport {
   public static CharsetDecoder createDecoder(ConnectionConfig var0) {
      if (var0 == null) {
         return null;
      } else {
         Charset var3 = var0.getCharset();
         CodingErrorAction var2 = var0.getMalformedInputAction();
         CodingErrorAction var1 = var0.getUnmappableInputAction();
         if (var3 != null) {
            CharsetDecoder var6 = var3.newDecoder();
            CodingErrorAction var4;
            if (var2 != null) {
               var4 = var2;
            } else {
               var4 = CodingErrorAction.REPORT;
            }

            CharsetDecoder var5 = var6.onMalformedInput(var4);
            if (var1 != null) {
               var4 = var1;
            } else {
               var4 = CodingErrorAction.REPORT;
            }

            return var5.onUnmappableCharacter(var4);
         } else {
            return null;
         }
      }
   }

   public static CharsetEncoder createEncoder(ConnectionConfig var0) {
      if (var0 == null) {
         return null;
      } else {
         Charset var3 = var0.getCharset();
         if (var3 != null) {
            CodingErrorAction var2 = var0.getMalformedInputAction();
            CodingErrorAction var1 = var0.getUnmappableInputAction();
            CharsetEncoder var6 = var3.newEncoder();
            CodingErrorAction var4;
            if (var2 != null) {
               var4 = var2;
            } else {
               var4 = CodingErrorAction.REPORT;
            }

            CharsetEncoder var5 = var6.onMalformedInput(var4);
            if (var1 != null) {
               var4 = var1;
            } else {
               var4 = CodingErrorAction.REPORT;
            }

            return var5.onUnmappableCharacter(var4);
         } else {
            return null;
         }
      }
   }
}
