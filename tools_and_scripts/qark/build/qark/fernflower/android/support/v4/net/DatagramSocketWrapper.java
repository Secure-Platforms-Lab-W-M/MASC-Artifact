package android.support.v4.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

class DatagramSocketWrapper extends Socket {
   public DatagramSocketWrapper(DatagramSocket var1, FileDescriptor var2) throws SocketException {
      super(new DatagramSocketWrapper.DatagramSocketImplWrapper(var1, var2));
   }

   private static class DatagramSocketImplWrapper extends SocketImpl {
      public DatagramSocketImplWrapper(DatagramSocket var1, FileDescriptor var2) {
         this.localport = var1.getLocalPort();
         this.fd = var2;
      }

      protected void accept(SocketImpl var1) throws IOException {
         throw new UnsupportedOperationException();
      }

      protected int available() throws IOException {
         throw new UnsupportedOperationException();
      }

      protected void bind(InetAddress var1, int var2) throws IOException {
         throw new UnsupportedOperationException();
      }

      protected void close() throws IOException {
         throw new UnsupportedOperationException();
      }

      protected void connect(String var1, int var2) throws IOException {
         throw new UnsupportedOperationException();
      }

      protected void connect(InetAddress var1, int var2) throws IOException {
         throw new UnsupportedOperationException();
      }

      protected void connect(SocketAddress var1, int var2) throws IOException {
         throw new UnsupportedOperationException();
      }

      protected void create(boolean var1) throws IOException {
         throw new UnsupportedOperationException();
      }

      protected InputStream getInputStream() throws IOException {
         throw new UnsupportedOperationException();
      }

      public Object getOption(int var1) throws SocketException {
         throw new UnsupportedOperationException();
      }

      protected OutputStream getOutputStream() throws IOException {
         throw new UnsupportedOperationException();
      }

      protected void listen(int var1) throws IOException {
         throw new UnsupportedOperationException();
      }

      protected void sendUrgentData(int var1) throws IOException {
         throw new UnsupportedOperationException();
      }

      public void setOption(int var1, Object var2) throws SocketException {
         throw new UnsupportedOperationException();
      }
   }
}
