package org.apache.http.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpMessage;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ChunkedOutputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.ContentLengthOutputStream;
import org.apache.http.impl.io.EmptyInputStream;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.impl.io.IdentityOutputStream;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.impl.io.SessionOutputBufferImpl;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.NetUtils;

public class BHttpConnectionBase implements HttpInetConnection {
   private final HttpConnectionMetricsImpl connMetrics;
   private final SessionInputBufferImpl inBuffer;
   private final ContentLengthStrategy incomingContentStrategy;
   private final MessageConstraints messageConstraints;
   private final SessionOutputBufferImpl outbuffer;
   private final ContentLengthStrategy outgoingContentStrategy;
   private final AtomicReference socketHolder;

   protected BHttpConnectionBase(int var1, int var2, CharsetDecoder var3, CharsetEncoder var4, MessageConstraints var5, ContentLengthStrategy var6, ContentLengthStrategy var7) {
      Args.positive(var1, "Buffer size");
      HttpTransportMetricsImpl var9 = new HttpTransportMetricsImpl();
      HttpTransportMetricsImpl var10 = new HttpTransportMetricsImpl();
      MessageConstraints var8;
      if (var5 != null) {
         var8 = var5;
      } else {
         var8 = MessageConstraints.DEFAULT;
      }

      this.inBuffer = new SessionInputBufferImpl(var9, var1, -1, var8, var3);
      this.outbuffer = new SessionOutputBufferImpl(var10, var1, var2, var4);
      this.messageConstraints = var5;
      this.connMetrics = new HttpConnectionMetricsImpl(var9, var10);
      if (var6 == null) {
         var6 = LaxContentLengthStrategy.INSTANCE;
      }

      this.incomingContentStrategy = (ContentLengthStrategy)var6;
      if (var7 == null) {
         var7 = StrictContentLengthStrategy.INSTANCE;
      }

      this.outgoingContentStrategy = (ContentLengthStrategy)var7;
      this.socketHolder = new AtomicReference();
   }

   private int fillInputBuffer(int var1) throws IOException {
      Socket var3 = (Socket)this.socketHolder.get();
      int var2 = var3.getSoTimeout();

      try {
         var3.setSoTimeout(var1);
         var1 = this.inBuffer.fillBuffer();
      } finally {
         var3.setSoTimeout(var2);
      }

      return var1;
   }

   protected boolean awaitInput(int var1) throws IOException {
      if (this.inBuffer.hasBufferedData()) {
         return true;
      } else {
         this.fillInputBuffer(var1);
         return this.inBuffer.hasBufferedData();
      }
   }

   protected void bind(Socket var1) throws IOException {
      Args.notNull(var1, "Socket");
      this.socketHolder.set(var1);
      this.inBuffer.bind((InputStream)null);
      this.outbuffer.bind((OutputStream)null);
   }

   public void close() throws IOException {
      // $FF: Couldn't be decompiled
   }

   protected InputStream createInputStream(long var1, SessionInputBuffer var3) {
      if (var1 == -2L) {
         return new ChunkedInputStream(var3, this.messageConstraints);
      } else if (var1 == -1L) {
         return new IdentityInputStream(var3);
      } else {
         return (InputStream)(var1 == 0L ? EmptyInputStream.INSTANCE : new ContentLengthInputStream(var3, var1));
      }
   }

   protected OutputStream createOutputStream(long var1, SessionOutputBuffer var3) {
      if (var1 == -2L) {
         return new ChunkedOutputStream(2048, var3);
      } else {
         return (OutputStream)(var1 == -1L ? new IdentityOutputStream(var3) : new ContentLengthOutputStream(var3, var1));
      }
   }

   protected void doFlush() throws IOException {
      this.outbuffer.flush();
   }

   protected void ensureOpen() throws IOException {
      Socket var1 = (Socket)this.socketHolder.get();
      if (var1 != null) {
         if (!this.inBuffer.isBound()) {
            this.inBuffer.bind(this.getSocketInputStream(var1));
         }

         if (!this.outbuffer.isBound()) {
            this.outbuffer.bind(this.getSocketOutputStream(var1));
         }

      } else {
         throw new ConnectionClosedException();
      }
   }

   public InetAddress getLocalAddress() {
      Socket var1 = (Socket)this.socketHolder.get();
      return var1 != null ? var1.getLocalAddress() : null;
   }

   public int getLocalPort() {
      Socket var1 = (Socket)this.socketHolder.get();
      return var1 != null ? var1.getLocalPort() : -1;
   }

   public HttpConnectionMetrics getMetrics() {
      return this.connMetrics;
   }

   public InetAddress getRemoteAddress() {
      Socket var1 = (Socket)this.socketHolder.get();
      return var1 != null ? var1.getInetAddress() : null;
   }

   public int getRemotePort() {
      Socket var1 = (Socket)this.socketHolder.get();
      return var1 != null ? var1.getPort() : -1;
   }

   protected SessionInputBuffer getSessionInputBuffer() {
      return this.inBuffer;
   }

   protected SessionOutputBuffer getSessionOutputBuffer() {
      return this.outbuffer;
   }

   protected Socket getSocket() {
      return (Socket)this.socketHolder.get();
   }

   protected InputStream getSocketInputStream(Socket var1) throws IOException {
      return var1.getInputStream();
   }

   protected OutputStream getSocketOutputStream(Socket var1) throws IOException {
      return var1.getOutputStream();
   }

   public int getSocketTimeout() {
      Socket var2 = (Socket)this.socketHolder.get();
      if (var2 != null) {
         try {
            int var1 = var2.getSoTimeout();
            return var1;
         } catch (SocketException var3) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   protected void incrementRequestCount() {
      this.connMetrics.incrementRequestCount();
   }

   protected void incrementResponseCount() {
      this.connMetrics.incrementResponseCount();
   }

   public boolean isOpen() {
      return this.socketHolder.get() != null;
   }

   public boolean isStale() {
      if (!this.isOpen()) {
         return true;
      } else {
         int var1;
         try {
            var1 = this.fillInputBuffer(1);
         } catch (SocketTimeoutException var3) {
            return false;
         } catch (IOException var4) {
            return true;
         }

         return var1 < 0;
      }
   }

   protected HttpEntity prepareInput(HttpMessage var1) throws HttpException {
      BasicHttpEntity var4 = new BasicHttpEntity();
      long var2 = this.incomingContentStrategy.determineLength(var1);
      InputStream var5 = this.createInputStream(var2, this.inBuffer);
      if (var2 == -2L) {
         var4.setChunked(true);
         var4.setContentLength(-1L);
         var4.setContent(var5);
      } else if (var2 == -1L) {
         var4.setChunked(false);
         var4.setContentLength(-1L);
         var4.setContent(var5);
      } else {
         var4.setChunked(false);
         var4.setContentLength(var2);
         var4.setContent(var5);
      }

      Header var6 = var1.getFirstHeader("Content-Type");
      if (var6 != null) {
         var4.setContentType(var6);
      }

      Header var7 = var1.getFirstHeader("Content-Encoding");
      if (var7 != null) {
         var4.setContentEncoding(var7);
      }

      return var4;
   }

   protected OutputStream prepareOutput(HttpMessage var1) throws HttpException {
      return this.createOutputStream(this.outgoingContentStrategy.determineLength(var1), this.outbuffer);
   }

   public void setSocketTimeout(int var1) {
      Socket var2 = (Socket)this.socketHolder.get();
      if (var2 != null) {
         try {
            var2.setSoTimeout(var1);
            return;
         } catch (SocketException var3) {
         }
      }

   }

   public void shutdown() throws IOException {
      Socket var1 = (Socket)this.socketHolder.getAndSet((Object)null);
      if (var1 != null) {
         try {
            var1.setSoLinger(true, 0);
         } catch (IOException var5) {
         } finally {
            var1.close();
         }

      }
   }

   public String toString() {
      Socket var3 = (Socket)this.socketHolder.get();
      if (var3 != null) {
         StringBuilder var1 = new StringBuilder();
         SocketAddress var2 = var3.getRemoteSocketAddress();
         SocketAddress var4 = var3.getLocalSocketAddress();
         if (var2 != null && var4 != null) {
            NetUtils.formatAddress(var1, var4);
            var1.append("<->");
            NetUtils.formatAddress(var1, var2);
         }

         return var1.toString();
      } else {
         return "[Not bound]";
      }
   }
}
