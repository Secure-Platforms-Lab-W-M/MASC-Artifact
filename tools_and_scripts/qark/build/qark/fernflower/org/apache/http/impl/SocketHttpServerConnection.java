package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.apache.http.HttpInetConnection;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class SocketHttpServerConnection extends AbstractHttpServerConnection implements HttpInetConnection {
   private volatile boolean open;
   private volatile Socket socket = null;

   private static void formatAddress(StringBuilder var0, SocketAddress var1) {
      if (var1 instanceof InetSocketAddress) {
         InetSocketAddress var2 = (InetSocketAddress)var1;
         Object var3;
         if (var2.getAddress() != null) {
            var3 = var2.getAddress().getHostAddress();
         } else {
            var3 = var2.getAddress();
         }

         var0.append(var3);
         var0.append(':');
         var0.append(var2.getPort());
      } else {
         var0.append(var1);
      }
   }

   protected void assertNotOpen() {
      Asserts.check(this.open ^ true, "Connection is already open");
   }

   protected void assertOpen() {
      Asserts.check(this.open, "Connection is not open");
   }

   protected void bind(Socket var1, HttpParams var2) throws IOException {
      Args.notNull(var1, "Socket");
      Args.notNull(var2, "HTTP parameters");
      this.socket = var1;
      int var3 = var2.getIntParameter("http.socket.buffer-size", -1);
      this.init(this.createSessionInputBuffer(var1, var3, var2), this.createSessionOutputBuffer(var1, var3, var2), var2);
      this.open = true;
   }

   public void close() throws IOException {
      // $FF: Couldn't be decompiled
   }

   protected SessionInputBuffer createSessionInputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      return new SocketInputBuffer(var1, var2, var3);
   }

   protected SessionOutputBuffer createSessionOutputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      return new SocketOutputBuffer(var1, var2, var3);
   }

   public InetAddress getLocalAddress() {
      return this.socket != null ? this.socket.getLocalAddress() : null;
   }

   public int getLocalPort() {
      return this.socket != null ? this.socket.getLocalPort() : -1;
   }

   public InetAddress getRemoteAddress() {
      return this.socket != null ? this.socket.getInetAddress() : null;
   }

   public int getRemotePort() {
      return this.socket != null ? this.socket.getPort() : -1;
   }

   protected Socket getSocket() {
      return this.socket;
   }

   public int getSocketTimeout() {
      if (this.socket != null) {
         try {
            int var1 = this.socket.getSoTimeout();
            return var1;
         } catch (SocketException var3) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setSocketTimeout(int var1) {
      this.assertOpen();
      if (this.socket != null) {
         try {
            this.socket.setSoTimeout(var1);
            return;
         } catch (SocketException var3) {
         }
      }

   }

   public void shutdown() throws IOException {
      this.open = false;
      Socket var1 = this.socket;
      if (var1 != null) {
         var1.close();
      }

   }

   public String toString() {
      if (this.socket != null) {
         StringBuilder var1 = new StringBuilder();
         SocketAddress var2 = this.socket.getRemoteSocketAddress();
         SocketAddress var3 = this.socket.getLocalSocketAddress();
         if (var2 != null && var3 != null) {
            formatAddress(var1, var3);
            var1.append("<->");
            formatAddress(var1, var2);
         }

         return var1.toString();
      } else {
         return super.toString();
      }
   }
}
