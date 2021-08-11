package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Cache implements Closeable, Flushable {
   private static final int ENTRY_BODY = 1;
   private static final int ENTRY_COUNT = 2;
   private static final int ENTRY_METADATA = 0;
   private static final int VERSION = 201105;
   final DiskLruCache cache;
   private int hitCount;
   final InternalCache internalCache;
   private int networkCount;
   private int requestCount;
   int writeAbortCount;
   int writeSuccessCount;

   public Cache(File var1, long var2) {
      this(var1, var2, FileSystem.SYSTEM);
   }

   Cache(File var1, long var2, FileSystem var4) {
      this.internalCache = new InternalCache() {
         public Response get(Request var1) throws IOException {
            return Cache.this.get(var1);
         }

         public CacheRequest put(Response var1) throws IOException {
            return Cache.this.put(var1);
         }

         public void remove(Request var1) throws IOException {
            Cache.this.remove(var1);
         }

         public void trackConditionalCacheHit() {
            Cache.this.trackConditionalCacheHit();
         }

         public void trackResponse(CacheStrategy var1) {
            Cache.this.trackResponse(var1);
         }

         public void update(Response var1, Response var2) {
            Cache.this.update(var1, var2);
         }
      };
      this.cache = DiskLruCache.create(var4, var1, 201105, 2, var2);
   }

   private void abortQuietly(@Nullable DiskLruCache.Editor var1) {
      if (var1 != null) {
         try {
            var1.abort();
         } catch (IOException var2) {
            return;
         }
      }

   }

   public static String key(HttpUrl var0) {
      return ByteString.encodeUtf8(var0.toString()).md5().hex();
   }

   static int readInt(BufferedSource var0) throws IOException {
      NumberFormatException var10000;
      label31: {
         long var1;
         boolean var10001;
         String var7;
         try {
            var1 = var0.readDecimalLong();
            var7 = var0.readUtf8LineStrict();
         } catch (NumberFormatException var6) {
            var10000 = var6;
            var10001 = false;
            break label31;
         }

         if (var1 >= 0L && var1 <= 2147483647L) {
            try {
               if (var7.isEmpty()) {
                  return (int)var1;
               }
            } catch (NumberFormatException var5) {
               var10000 = var5;
               var10001 = false;
               break label31;
            }
         }

         try {
            StringBuilder var3 = new StringBuilder();
            var3.append("expected an int but was \"");
            var3.append(var1);
            var3.append(var7);
            var3.append("\"");
            throw new IOException(var3.toString());
         } catch (NumberFormatException var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      NumberFormatException var8 = var10000;
      throw new IOException(var8.getMessage());
   }

   public void close() throws IOException {
      this.cache.close();
   }

   public void delete() throws IOException {
      this.cache.delete();
   }

   public File directory() {
      return this.cache.getDirectory();
   }

   public void evictAll() throws IOException {
      this.cache.evictAll();
   }

   public void flush() throws IOException {
      this.cache.flush();
   }

   @Nullable
   Response get(Request var1) {
      String var2 = key(var1.url());

      DiskLruCache.Snapshot var6;
      try {
         var6 = this.cache.get(var2);
      } catch (IOException var5) {
         return null;
      }

      if (var6 == null) {
         return null;
      } else {
         Cache.Entry var3;
         try {
            var3 = new Cache.Entry(var6.getSource(0));
         } catch (IOException var4) {
            Util.closeQuietly((Closeable)var6);
            return null;
         }

         Response var7 = var3.response(var6);
         if (!var3.matches(var1, var7)) {
            Util.closeQuietly((Closeable)var7.body());
            return null;
         } else {
            return var7;
         }
      }
   }

   public int hitCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.hitCount;
      } finally {
         ;
      }

      return var1;
   }

   public void initialize() throws IOException {
      this.cache.initialize();
   }

   public boolean isClosed() {
      return this.cache.isClosed();
   }

   public long maxSize() {
      return this.cache.getMaxSize();
   }

   public int networkCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.networkCount;
      } finally {
         ;
      }

      return var1;
   }

   @Nullable
   CacheRequest put(Response var1) {
      String var2 = var1.request().method();
      if (HttpMethod.invalidatesCache(var1.request().method())) {
         try {
            this.remove(var1.request());
            return null;
         } catch (IOException var4) {
            return null;
         }
      } else if (!var2.equals("GET")) {
         return null;
      } else if (HttpHeaders.hasVaryAll(var1)) {
         return null;
      } else {
         Cache.Entry var3 = new Cache.Entry(var1);
         DiskLruCache.Editor var10 = null;

         label53: {
            boolean var10001;
            DiskLruCache.Editor var8;
            try {
               var8 = this.cache.edit(key(var1.request().url()));
            } catch (IOException var7) {
               var10001 = false;
               break label53;
            }

            if (var8 == null) {
               return null;
            }

            var10 = var8;

            try {
               var3.writeTo(var8);
            } catch (IOException var6) {
               var10001 = false;
               break label53;
            }

            var10 = var8;

            try {
               Cache.CacheRequestImpl var9 = new Cache.CacheRequestImpl(var8);
               return var9;
            } catch (IOException var5) {
               var10001 = false;
            }
         }

         this.abortQuietly(var10);
         return null;
      }
   }

   void remove(Request var1) throws IOException {
      this.cache.remove(key(var1.url()));
   }

   public int requestCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.requestCount;
      } finally {
         ;
      }

      return var1;
   }

   public long size() throws IOException {
      return this.cache.size();
   }

   void trackConditionalCacheHit() {
      synchronized(this){}

      try {
         ++this.hitCount;
      } finally {
         ;
      }

   }

   void trackResponse(CacheStrategy var1) {
      synchronized(this){}

      try {
         ++this.requestCount;
         if (var1.networkRequest != null) {
            ++this.networkCount;
         } else if (var1.cacheResponse != null) {
            ++this.hitCount;
         }
      } finally {
         ;
      }

   }

   void update(Response var1, Response var2) {
      Cache.Entry var3 = new Cache.Entry(var2);
      DiskLruCache.Snapshot var8 = ((Cache.CacheResponseBody)var1.body()).snapshot;
      DiskLruCache.Editor var7 = null;

      label37: {
         boolean var10001;
         DiskLruCache.Editor var9;
         try {
            var9 = var8.edit();
         } catch (IOException var6) {
            var10001 = false;
            break label37;
         }

         if (var9 == null) {
            return;
         }

         var7 = var9;

         try {
            var3.writeTo(var9);
         } catch (IOException var5) {
            var10001 = false;
            break label37;
         }

         var7 = var9;

         try {
            var9.commit();
            return;
         } catch (IOException var4) {
            var10001 = false;
         }
      }

      this.abortQuietly(var7);
   }

   public Iterator urls() throws IOException {
      return new Iterator() {
         boolean canRemove;
         final Iterator delegate;
         @Nullable
         String nextUrl;

         {
            this.delegate = Cache.this.cache.snapshots();
         }

         public boolean hasNext() {
            if (this.nextUrl != null) {
               return true;
            } else {
               this.canRemove = false;

               while(true) {
                  if (this.delegate.hasNext()) {
                     DiskLruCache.Snapshot var1 = (DiskLruCache.Snapshot)this.delegate.next();

                     try {
                        this.nextUrl = Okio.buffer(var1.getSource(0)).readUtf8LineStrict();
                     } catch (IOException var5) {
                        continue;
                     } finally {
                        var1.close();
                     }

                     return true;
                  }

                  return false;
               }
            }
         }

         public String next() {
            if (this.hasNext()) {
               String var1 = this.nextUrl;
               this.nextUrl = null;
               this.canRemove = true;
               return var1;
            } else {
               throw new NoSuchElementException();
            }
         }

         public void remove() {
            if (this.canRemove) {
               this.delegate.remove();
            } else {
               throw new IllegalStateException("remove() before next()");
            }
         }
      };
   }

   public int writeAbortCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.writeAbortCount;
      } finally {
         ;
      }

      return var1;
   }

   public int writeSuccessCount() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.writeSuccessCount;
      } finally {
         ;
      }

      return var1;
   }

   private final class CacheRequestImpl implements CacheRequest {
      private Sink body;
      private Sink cacheOut;
      boolean done;
      private final DiskLruCache.Editor editor;

      CacheRequestImpl(final DiskLruCache.Editor var2) {
         this.editor = var2;
         Sink var3 = var2.newSink(1);
         this.cacheOut = var3;
         this.body = new ForwardingSink(var3) {
            public void close() throws IOException {
               Cache var1 = Cache.this;
               synchronized(var1){}

               Throwable var10000;
               boolean var10001;
               label137: {
                  try {
                     if (CacheRequestImpl.this.done) {
                        return;
                     }
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label137;
                  }

                  try {
                     CacheRequestImpl.this.done = true;
                     Cache var15 = Cache.this;
                     ++var15.writeSuccessCount;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label137;
                  }

                  super.close();
                  var2.commit();
                  return;
               }

               while(true) {
                  Throwable var2x = var10000;

                  try {
                     throw var2x;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     continue;
                  }
               }
            }
         };
      }

      public void abort() {
         Cache var1 = Cache.this;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label155: {
            try {
               if (this.done) {
                  return;
               }
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label155;
            }

            try {
               this.done = true;
               Cache var19 = Cache.this;
               ++var19.writeAbortCount;
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               break label155;
            }

            Util.closeQuietly((Closeable)this.cacheOut);

            try {
               this.editor.abort();
               return;
            } catch (IOException var15) {
               return;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               continue;
            }
         }
      }

      public Sink body() {
         return this.body;
      }
   }

   private static class CacheResponseBody extends ResponseBody {
      private final BufferedSource bodySource;
      @Nullable
      private final String contentLength;
      @Nullable
      private final String contentType;
      final DiskLruCache.Snapshot snapshot;

      CacheResponseBody(final DiskLruCache.Snapshot var1, String var2, String var3) {
         this.snapshot = var1;
         this.contentType = var2;
         this.contentLength = var3;
         this.bodySource = Okio.buffer((Source)(new ForwardingSource(var1.getSource(1)) {
            public void close() throws IOException {
               var1.close();
               super.close();
            }
         }));
      }

      public long contentLength() {
         long var1 = -1L;

         try {
            if (this.contentLength != null) {
               var1 = Long.parseLong(this.contentLength);
            }

            return var1;
         } catch (NumberFormatException var4) {
            return -1L;
         }
      }

      public MediaType contentType() {
         String var1 = this.contentType;
         return var1 != null ? MediaType.parse(var1) : null;
      }

      public BufferedSource source() {
         return this.bodySource;
      }
   }

   private static final class Entry {
      private static final String RECEIVED_MILLIS;
      private static final String SENT_MILLIS;
      private final int code;
      @Nullable
      private final Handshake handshake;
      private final String message;
      private final Protocol protocol;
      private final long receivedResponseMillis;
      private final String requestMethod;
      private final Headers responseHeaders;
      private final long sentRequestMillis;
      private final String url;
      private final Headers varyHeaders;

      static {
         StringBuilder var0 = new StringBuilder();
         var0.append(Platform.get().getPrefix());
         var0.append("-Sent-Millis");
         SENT_MILLIS = var0.toString();
         var0 = new StringBuilder();
         var0.append(Platform.get().getPrefix());
         var0.append("-Received-Millis");
         RECEIVED_MILLIS = var0.toString();
      }

      Entry(Response var1) {
         this.url = var1.request().url().toString();
         this.varyHeaders = HttpHeaders.varyHeaders(var1);
         this.requestMethod = var1.request().method();
         this.protocol = var1.protocol();
         this.code = var1.code();
         this.message = var1.message();
         this.responseHeaders = var1.headers();
         this.handshake = var1.handshake();
         this.sentRequestMillis = var1.sentRequestAtMillis();
         this.receivedResponseMillis = var1.receivedResponseAtMillis();
      }

      Entry(Source var1) throws IOException {
         Throwable var10000;
         label1598: {
            int var3;
            BufferedSource var8;
            Headers.Builder var9;
            boolean var10001;
            try {
               var8 = Okio.buffer(var1);
               this.url = var8.readUtf8LineStrict();
               this.requestMethod = var8.readUtf8LineStrict();
               var9 = new Headers.Builder();
               var3 = Cache.readInt(var8);
            } catch (Throwable var192) {
               var10000 = var192;
               var10001 = false;
               break label1598;
            }

            int var2;
            for(var2 = 0; var2 < var3; ++var2) {
               try {
                  var9.addLenient(var8.readUtf8LineStrict());
               } catch (Throwable var191) {
                  var10000 = var191;
                  var10001 = false;
                  break label1598;
               }
            }

            try {
               this.varyHeaders = var9.build();
               StatusLine var195 = StatusLine.parse(var8.readUtf8LineStrict());
               this.protocol = var195.protocol;
               this.code = var195.code;
               this.message = var195.message;
               var9 = new Headers.Builder();
               var3 = Cache.readInt(var8);
            } catch (Throwable var190) {
               var10000 = var190;
               var10001 = false;
               break label1598;
            }

            for(var2 = 0; var2 < var3; ++var2) {
               try {
                  var9.addLenient(var8.readUtf8LineStrict());
               } catch (Throwable var189) {
                  var10000 = var189;
                  var10001 = false;
                  break label1598;
               }
            }

            String var10;
            String var11;
            try {
               var10 = var9.get(SENT_MILLIS);
               var11 = var9.get(RECEIVED_MILLIS);
               var9.removeAll(SENT_MILLIS);
               var9.removeAll(RECEIVED_MILLIS);
            } catch (Throwable var188) {
               var10000 = var188;
               var10001 = false;
               break label1598;
            }

            long var6 = 0L;
            long var4;
            if (var10 != null) {
               try {
                  var4 = Long.parseLong(var10);
               } catch (Throwable var187) {
                  var10000 = var187;
                  var10001 = false;
                  break label1598;
               }
            } else {
               var4 = 0L;
            }

            try {
               this.sentRequestMillis = var4;
            } catch (Throwable var186) {
               var10000 = var186;
               var10001 = false;
               break label1598;
            }

            if (var11 != null) {
               try {
                  var4 = Long.parseLong(var11);
               } catch (Throwable var185) {
                  var10000 = var185;
                  var10001 = false;
                  break label1598;
               }
            } else {
               var4 = var6;
            }

            String var198;
            label1580: {
               label1579: {
                  label1578: {
                     TlsVersion var194;
                     CipherSuite var199;
                     List var200;
                     List var201;
                     label1602: {
                        try {
                           this.receivedResponseMillis = var4;
                           this.responseHeaders = var9.build();
                           if (!this.isHttps()) {
                              break label1578;
                           }

                           var198 = var8.readUtf8LineStrict();
                           if (var198.length() > 0) {
                              break label1580;
                           }

                           var199 = CipherSuite.forJavaName(var8.readUtf8LineStrict());
                           var200 = this.readCertificateList(var8);
                           var201 = this.readCertificateList(var8);
                           if (!var8.exhausted()) {
                              var194 = TlsVersion.forJavaName(var8.readUtf8LineStrict());
                              break label1602;
                           }
                        } catch (Throwable var193) {
                           var10000 = var193;
                           var10001 = false;
                           break label1598;
                        }

                        try {
                           var194 = TlsVersion.SSL_3_0;
                        } catch (Throwable var184) {
                           var10000 = var184;
                           var10001 = false;
                           break label1598;
                        }
                     }

                     try {
                        this.handshake = Handshake.get(var194, var199, var200, var201);
                        break label1579;
                     } catch (Throwable var183) {
                        var10000 = var183;
                        var10001 = false;
                        break label1598;
                     }
                  }

                  try {
                     this.handshake = null;
                  } catch (Throwable var182) {
                     var10000 = var182;
                     var10001 = false;
                     break label1598;
                  }
               }

               var1.close();
               return;
            }

            label1539:
            try {
               StringBuilder var196 = new StringBuilder();
               var196.append("expected \"\" but was \"");
               var196.append(var198);
               var196.append("\"");
               throw new IOException(var196.toString());
            } catch (Throwable var181) {
               var10000 = var181;
               var10001 = false;
               break label1539;
            }
         }

         Throwable var197 = var10000;
         var1.close();
         throw var197;
      }

      private boolean isHttps() {
         return this.url.startsWith("https://");
      }

      private List readCertificateList(BufferedSource var1) throws IOException {
         int var3 = Cache.readInt(var1);
         if (var3 == -1) {
            return Collections.emptyList();
         } else {
            CertificateException var10000;
            label34: {
               boolean var10001;
               CertificateFactory var4;
               ArrayList var5;
               try {
                  var4 = CertificateFactory.getInstance("X.509");
                  var5 = new ArrayList(var3);
               } catch (CertificateException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label34;
               }

               int var2 = 0;

               while(true) {
                  if (var2 >= var3) {
                     return var5;
                  }

                  try {
                     String var6 = var1.readUtf8LineStrict();
                     Buffer var7 = new Buffer();
                     var7.write(ByteString.decodeBase64(var6));
                     var5.add(var4.generateCertificate(var7.inputStream()));
                  } catch (CertificateException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }

                  ++var2;
               }
            }

            CertificateException var10 = var10000;
            throw new IOException(var10.getMessage());
         }
      }

      private void writeCertList(BufferedSink var1, List var2) throws IOException {
         CertificateEncodingException var10000;
         label35: {
            boolean var10001;
            try {
               var1.writeDecimalLong((long)var2.size()).writeByte(10);
            } catch (CertificateEncodingException var7) {
               var10000 = var7;
               var10001 = false;
               break label35;
            }

            int var3 = 0;

            int var4;
            try {
               var4 = var2.size();
            } catch (CertificateEncodingException var6) {
               var10000 = var6;
               var10001 = false;
               break label35;
            }

            while(true) {
               if (var3 >= var4) {
                  return;
               }

               try {
                  var1.writeUtf8(ByteString.method_6(((Certificate)var2.get(var3)).getEncoded()).base64()).writeByte(10);
               } catch (CertificateEncodingException var5) {
                  var10000 = var5;
                  var10001 = false;
                  break;
               }

               ++var3;
            }
         }

         CertificateEncodingException var8 = var10000;
         throw new IOException(var8.getMessage());
      }

      public boolean matches(Request var1, Response var2) {
         return this.url.equals(var1.url().toString()) && this.requestMethod.equals(var1.method()) && HttpHeaders.varyMatches(var2, this.varyHeaders, var1);
      }

      public Response response(DiskLruCache.Snapshot var1) {
         String var2 = this.responseHeaders.get("Content-Type");
         String var3 = this.responseHeaders.get("Content-Length");
         Request var4 = (new Request.Builder()).url(this.url).method(this.requestMethod, (RequestBody)null).headers(this.varyHeaders).build();
         return (new Response.Builder()).request(var4).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new Cache.CacheResponseBody(var1, var2, var3)).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
      }

      public void writeTo(DiskLruCache.Editor var1) throws IOException {
         BufferedSink var4 = Okio.buffer(var1.newSink(0));
         var4.writeUtf8(this.url).writeByte(10);
         var4.writeUtf8(this.requestMethod).writeByte(10);
         var4.writeDecimalLong((long)this.varyHeaders.size()).writeByte(10);
         int var2 = 0;

         int var3;
         for(var3 = this.varyHeaders.size(); var2 < var3; ++var2) {
            var4.writeUtf8(this.varyHeaders.name(var2)).writeUtf8(": ").writeUtf8(this.varyHeaders.value(var2)).writeByte(10);
         }

         var4.writeUtf8((new StatusLine(this.protocol, this.code, this.message)).toString()).writeByte(10);
         var4.writeDecimalLong((long)(this.responseHeaders.size() + 2)).writeByte(10);
         var2 = 0;

         for(var3 = this.responseHeaders.size(); var2 < var3; ++var2) {
            var4.writeUtf8(this.responseHeaders.name(var2)).writeUtf8(": ").writeUtf8(this.responseHeaders.value(var2)).writeByte(10);
         }

         var4.writeUtf8(SENT_MILLIS).writeUtf8(": ").writeDecimalLong(this.sentRequestMillis).writeByte(10);
         var4.writeUtf8(RECEIVED_MILLIS).writeUtf8(": ").writeDecimalLong(this.receivedResponseMillis).writeByte(10);
         if (this.isHttps()) {
            var4.writeByte(10);
            var4.writeUtf8(this.handshake.cipherSuite().javaName()).writeByte(10);
            this.writeCertList(var4, this.handshake.peerCertificates());
            this.writeCertList(var4, this.handshake.localCertificates());
            var4.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
         }

         var4.close();
      }
   }
}
