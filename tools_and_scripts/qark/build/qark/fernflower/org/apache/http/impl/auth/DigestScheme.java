package org.apache.http.impl.auth;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;

public class DigestScheme extends RFC2617Scheme {
   private static final char[] HEXADECIMAL = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   private static final int QOP_AUTH = 2;
   private static final int QOP_AUTH_INT = 1;
   private static final int QOP_MISSING = 0;
   private static final int QOP_UNKNOWN = -1;
   private static final long serialVersionUID = 3883908186234566916L;
   // $FF: renamed from: a1 java.lang.String
   private String field_28;
   // $FF: renamed from: a2 java.lang.String
   private String field_29;
   private String cnonce;
   private boolean complete;
   private String lastNonce;
   private long nounceCount;

   public DigestScheme() {
      this(Consts.ASCII);
   }

   public DigestScheme(Charset var1) {
      super(var1);
      this.complete = false;
   }

   @Deprecated
   public DigestScheme(ChallengeState var1) {
      super(var1);
   }

   public static String createCnonce() {
      SecureRandom var0 = new SecureRandom();
      byte[] var1 = new byte[8];
      var0.nextBytes(var1);
      return encode(var1);
   }

   private Header createDigestHeader(Credentials var1, HttpRequest var2) throws AuthenticationException {
      String var10 = this.getParameter("uri");
      String var14 = this.getParameter("realm");
      String var15 = this.getParameter("nonce");
      String var13 = this.getParameter("opaque");
      String var11 = this.getParameter("methodname");
      String var7 = this.getParameter("algorithm");
      String var6 = var7;
      if (var7 == null) {
         var6 = "MD5";
      }

      HashSet var18 = new HashSet(8);
      String var8 = this.getParameter("qop");
      byte var3;
      if (var8 != null) {
         StringTokenizer var32 = new StringTokenizer(var8, ",");

         while(var32.hasMoreTokens()) {
            var18.add(var32.nextToken().trim().toLowerCase(Locale.ROOT));
         }

         if (var2 instanceof HttpEntityEnclosingRequest && var18.contains("auth-int")) {
            var3 = 1;
         } else if (var18.contains("auth")) {
            var3 = 2;
         } else {
            var3 = -1;
         }
      } else {
         var3 = 0;
      }

      StringBuilder var23;
      if (var3 != -1) {
         var8 = this.getParameter("charset");
         var7 = var8;
         if (var8 == null) {
            var7 = "ISO-8859-1";
         }

         var8 = var6;
         if (var6.equalsIgnoreCase("MD5-sess")) {
            var8 = "MD5";
         }

         MessageDigest var12;
         try {
            var12 = createMessageDigest(var8);
         } catch (UnsupportedDigestAlgorithmException var20) {
            var23 = new StringBuilder();
            var23.append("Unsuppported digest algorithm: ");
            var23.append(var8);
            throw new AuthenticationException(var23.toString());
         }

         String var16 = var1.getUserPrincipal().getName();
         String var24 = var1.getPassword();
         if (var15.equals(this.lastNonce)) {
            ++this.nounceCount;
         } else {
            this.nounceCount = 1L;
            this.cnonce = null;
            this.lastNonce = var15;
         }

         StringBuilder var19 = new StringBuilder(256);
         Formatter var34 = new Formatter(var19, Locale.US);
         var34.format("%08x", this.nounceCount);
         var34.close();
         String var17 = var19.toString();
         if (this.cnonce == null) {
            this.cnonce = createCnonce();
         }

         this.field_28 = null;
         this.field_29 = null;
         if (var6.equalsIgnoreCase("MD5-sess")) {
            var19.setLength(0);
            var19.append(var16);
            var19.append(':');
            var19.append(var14);
            var19.append(':');
            var19.append(var24);
            var24 = encode(var12.digest(EncodingUtils.getBytes(var19.toString(), var7)));
            var19.setLength(0);
            var19.append(var24);
            var19.append(':');
            var19.append(var15);
            var19.append(':');
            var19.append(this.cnonce);
            this.field_28 = var19.toString();
         } else {
            var19.setLength(0);
            var19.append(var16);
            var19.append(':');
            var19.append(var14);
            var19.append(':');
            var19.append(var24);
            this.field_28 = var19.toString();
         }

         String var37 = encode(var12.digest(EncodingUtils.getBytes(this.field_28, var7)));
         if (var3 == 2) {
            var23 = new StringBuilder();
            var23.append(var11);
            var23.append(':');
            var23.append(var10);
            this.field_29 = var23.toString();
         } else if (var3 == 1) {
            HttpEntity var27;
            if (var2 instanceof HttpEntityEnclosingRequest) {
               var27 = ((HttpEntityEnclosingRequest)var2).getEntity();
            } else {
               var27 = null;
            }

            if (var27 != null && !var27.isRepeatable()) {
               if (!var18.contains("auth")) {
                  throw new AuthenticationException("Qop auth-int cannot be used with a non-repeatable entity");
               }

               var23 = new StringBuilder();
               var23.append(var11);
               var23.append(':');
               var23.append(var10);
               this.field_29 = var23.toString();
               var3 = 2;
            } else {
               HttpEntityDigester var25 = new HttpEntityDigester(var12);
               IOException var28;
               if (var27 != null) {
                  try {
                     var27.writeTo(var25);
                  } catch (IOException var22) {
                     var28 = var22;
                     throw new AuthenticationException("I/O error reading entity content", var28);
                  }
               }

               try {
                  var25.close();
               } catch (IOException var21) {
                  var28 = var21;
                  throw new AuthenticationException("I/O error reading entity content", var28);
               }

               var23 = new StringBuilder();
               var23.append(var11);
               var23.append(':');
               var23.append(var10);
               var23.append(':');
               var23.append(encode(var25.getDigest()));
               this.field_29 = var23.toString();
            }
         } else {
            var23 = new StringBuilder();
            var23.append(var11);
            var23.append(':');
            var23.append(var10);
            this.field_29 = var23.toString();
         }

         var24 = "auth";
         var7 = encode(var12.digest(EncodingUtils.getBytes(this.field_29, var7)));
         String var26;
         if (var3 == 0) {
            var19.setLength(0);
            var19.append(var37);
            var19.append(':');
            var19.append(var15);
            var19.append(':');
            var19.append(var7);
            var26 = var19.toString();
         } else {
            var19.setLength(0);
            var19.append(var37);
            var19.append(':');
            var19.append(var15);
            var19.append(':');
            var19.append(var17);
            var19.append(':');
            var19.append(this.cnonce);
            var19.append(':');
            if (var3 == 1) {
               var26 = "auth-int";
            } else {
               var26 = var24;
            }

            var19.append(var26);
            var19.append(':');
            var19.append(var7);
            var26 = var19.toString();
         }

         var8 = encode(var12.digest(EncodingUtils.getAsciiBytes(var26)));
         CharArrayBuffer var33 = new CharArrayBuffer(128);
         if (this.isProxy()) {
            var33.append("Proxy-Authorization");
         } else {
            var33.append("Authorization");
         }

         var33.append(": Digest ");
         ArrayList var29 = new ArrayList(20);
         var29.add(new BasicNameValuePair("username", var16));
         var29.add(new BasicNameValuePair("realm", var14));
         var29.add(new BasicNameValuePair("nonce", var15));
         var29.add(new BasicNameValuePair("uri", var10));
         var29.add(new BasicNameValuePair("response", var8));
         if (var3 != 0) {
            if (var3 == 1) {
               var24 = "auth-int";
            }

            var29.add(new BasicNameValuePair("qop", var24));
            var29.add(new BasicNameValuePair("nc", var17));
            var29.add(new BasicNameValuePair("cnonce", this.cnonce));
         }

         var24 = "qop";
         var29.add(new BasicNameValuePair("algorithm", var6));
         if (var13 != null) {
            var29.add(new BasicNameValuePair("opaque", var13));
         }

         for(int var30 = 0; var30 < var29.size(); ++var30) {
            BasicNameValuePair var31 = (BasicNameValuePair)var29.get(var30);
            if (var30 > 0) {
               var33.append(", ");
            }

            var8 = var31.getName();
            boolean var4;
            if (!"nc".equals(var8) && !var24.equals(var8) && !"algorithm".equals(var8)) {
               var4 = false;
            } else {
               var4 = true;
            }

            BasicHeaderValueFormatter var36 = BasicHeaderValueFormatter.INSTANCE;
            boolean var5;
            if (!var4) {
               var5 = true;
            } else {
               var5 = false;
            }

            var36.formatNameValuePair(var33, var31, var5);
         }

         return new BufferedHeader(var33);
      } else {
         var23 = new StringBuilder();
         var23.append("None of the qop methods is supported: ");
         var23.append(var8);
         throw new AuthenticationException(var23.toString());
      }
   }

   private static MessageDigest createMessageDigest(String var0) throws UnsupportedDigestAlgorithmException {
      try {
         MessageDigest var3 = MessageDigest.getInstance(var0);
         return var3;
      } catch (Exception var2) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Unsupported algorithm in HTTP Digest authentication: ");
         var1.append(var0);
         throw new UnsupportedDigestAlgorithmException(var1.toString());
      }
   }

   static String encode(byte[] var0) {
      int var2 = var0.length;
      char[] var5 = new char[var2 * 2];

      for(int var1 = 0; var1 < var2; ++var1) {
         byte var3 = var0[var1];
         byte var4 = var0[var1];
         char[] var6 = HEXADECIMAL;
         var5[var1 * 2] = var6[(var4 & 240) >> 4];
         var5[var1 * 2 + 1] = var6[var3 & 15];
      }

      return new String(var5);
   }

   @Deprecated
   public Header authenticate(Credentials var1, HttpRequest var2) throws AuthenticationException {
      return this.authenticate(var1, var2, new BasicHttpContext());
   }

   public Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException {
      Args.notNull(var1, "Credentials");
      Args.notNull(var2, "HTTP request");
      if (this.getParameter("realm") != null) {
         if (this.getParameter("nonce") != null) {
            this.getParameters().put("methodname", var2.getRequestLine().getMethod());
            this.getParameters().put("uri", var2.getRequestLine().getUri());
            if (this.getParameter("charset") == null) {
               this.getParameters().put("charset", this.getCredentialsCharset(var2));
            }

            return this.createDigestHeader(var1, var2);
         } else {
            throw new AuthenticationException("missing nonce in challenge");
         }
      } else {
         throw new AuthenticationException("missing realm in challenge");
      }
   }

   String getA1() {
      return this.field_28;
   }

   String getA2() {
      return this.field_29;
   }

   String getCnonce() {
      return this.cnonce;
   }

   public String getSchemeName() {
      return "digest";
   }

   public boolean isComplete() {
      return "true".equalsIgnoreCase(this.getParameter("stale")) ? false : this.complete;
   }

   public boolean isConnectionBased() {
      return false;
   }

   public void overrideParamter(String var1, String var2) {
      this.getParameters().put(var1, var2);
   }

   public void processChallenge(Header var1) throws MalformedChallengeException {
      super.processChallenge(var1);
      this.complete = true;
      if (this.getParameters().isEmpty()) {
         throw new MalformedChallengeException("Authentication challenge is empty");
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DIGEST [complete=");
      var1.append(this.complete);
      var1.append(", nonce=");
      var1.append(this.lastNonce);
      var1.append(", nc=");
      var1.append(this.nounceCount);
      var1.append("]");
      return var1.toString();
   }
}
