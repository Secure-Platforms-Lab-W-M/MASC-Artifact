package org.apache.http.impl.bootstrap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.config.SocketConfig;
import org.apache.http.protocol.HttpService;

public class HttpServer {
   private final HttpConnectionFactory connectionFactory;
   private final ExceptionLogger exceptionLogger;
   private final HttpService httpService;
   private final InetAddress ifAddress;
   private final ThreadPoolExecutor listenerExecutorService;
   private final int port;
   private volatile RequestListener requestListener;
   private volatile ServerSocket serverSocket;
   private final ServerSocketFactory serverSocketFactory;
   private final SocketConfig socketConfig;
   private final SSLServerSetupHandler sslSetupHandler;
   private final AtomicReference status;
   private final WorkerPoolExecutor workerExecutorService;
   private final ThreadGroup workerThreads;

   HttpServer(int var1, InetAddress var2, SocketConfig var3, ServerSocketFactory var4, HttpService var5, HttpConnectionFactory var6, SSLServerSetupHandler var7, ExceptionLogger var8) {
      this.port = var1;
      this.ifAddress = var2;
      this.socketConfig = var3;
      this.serverSocketFactory = var4;
      this.httpService = var5;
      this.connectionFactory = var6;
      this.sslSetupHandler = var7;
      this.exceptionLogger = var8;
      TimeUnit var9 = TimeUnit.MILLISECONDS;
      SynchronousQueue var10 = new SynchronousQueue();
      StringBuilder var11 = new StringBuilder();
      var11.append("HTTP-listener-");
      var11.append(this.port);
      this.listenerExecutorService = new ThreadPoolExecutor(1, 1, 0L, var9, var10, new ThreadFactoryImpl(var11.toString()));
      this.workerThreads = new ThreadGroup("HTTP-workers");
      this.workerExecutorService = new WorkerPoolExecutor(0, Integer.MAX_VALUE, 1L, TimeUnit.SECONDS, new SynchronousQueue(), new ThreadFactoryImpl("HTTP-worker", this.workerThreads));
      this.status = new AtomicReference(HttpServer.Status.READY);
   }

   public void awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      this.workerExecutorService.awaitTermination(var1, var3);
   }

   public InetAddress getInetAddress() {
      ServerSocket var1 = this.serverSocket;
      return var1 != null ? var1.getInetAddress() : null;
   }

   public int getLocalPort() {
      ServerSocket var1 = this.serverSocket;
      return var1 != null ? var1.getLocalPort() : -1;
   }

   public void shutdown(long var1, TimeUnit var3) {
      this.stop();
      if (var1 > 0L) {
         try {
            this.awaitTermination(var1, var3);
         } catch (InterruptedException var6) {
            Thread.currentThread().interrupt();
         }
      }

      Iterator var7 = this.workerExecutorService.getWorkers().iterator();

      while(var7.hasNext()) {
         HttpServerConnection var4 = ((Worker)var7.next()).getConnection();

         try {
            var4.shutdown();
         } catch (IOException var5) {
            this.exceptionLogger.log(var5);
         }
      }

   }

   public void start() throws IOException {
      if (this.status.compareAndSet(HttpServer.Status.READY, HttpServer.Status.ACTIVE)) {
         this.serverSocket = this.serverSocketFactory.createServerSocket(this.port, this.socketConfig.getBacklogSize(), this.ifAddress);
         this.serverSocket.setReuseAddress(this.socketConfig.isSoReuseAddress());
         if (this.socketConfig.getRcvBufSize() > 0) {
            this.serverSocket.setReceiveBufferSize(this.socketConfig.getRcvBufSize());
         }

         if (this.sslSetupHandler != null && this.serverSocket instanceof SSLServerSocket) {
            this.sslSetupHandler.initialize((SSLServerSocket)this.serverSocket);
         }

         this.requestListener = new RequestListener(this.socketConfig, this.serverSocket, this.httpService, this.connectionFactory, this.exceptionLogger, this.workerExecutorService);
         this.listenerExecutorService.execute(this.requestListener);
      }

   }

   public void stop() {
      if (this.status.compareAndSet(HttpServer.Status.ACTIVE, HttpServer.Status.STOPPING)) {
         this.listenerExecutorService.shutdown();
         this.workerExecutorService.shutdown();
         RequestListener var1 = this.requestListener;
         if (var1 != null) {
            try {
               var1.terminate();
            } catch (IOException var2) {
               this.exceptionLogger.log(var2);
            }
         }

         this.workerThreads.interrupt();
      }

   }

   static enum Status {
      ACTIVE,
      READY,
      STOPPING;

      static {
         HttpServer.Status var0 = new HttpServer.Status("STOPPING", 2);
         STOPPING = var0;
      }
   }
}
