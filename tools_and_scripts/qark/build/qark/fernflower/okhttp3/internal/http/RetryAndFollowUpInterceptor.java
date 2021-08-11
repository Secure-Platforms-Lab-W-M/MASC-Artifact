package okhttp3.internal.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.Proxy.Type;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http2.ConnectionShutdownException;

public final class RetryAndFollowUpInterceptor implements Interceptor {
   private static final int MAX_FOLLOW_UPS = 20;
   private Object callStackTrace;
   private volatile boolean canceled;
   private final OkHttpClient client;
   private final boolean forWebSocket;
   private StreamAllocation streamAllocation;

   public RetryAndFollowUpInterceptor(OkHttpClient var1, boolean var2) {
      this.client = var1;
      this.forWebSocket = var2;
   }

   private Address createAddress(HttpUrl var1) {
      SSLSocketFactory var2 = null;
      HostnameVerifier var3 = null;
      CertificatePinner var4 = null;
      if (var1.isHttps()) {
         var2 = this.client.sslSocketFactory();
         var3 = this.client.hostnameVerifier();
         var4 = this.client.certificatePinner();
      }

      return new Address(var1.host(), var1.port(), this.client.dns(), this.client.socketFactory(), var2, var3, var4, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
   }

   private Request followUpRequest(Response var1) throws IOException {
      if (var1 != null) {
         RealConnection var4 = this.streamAllocation.connection();
         Proxy var5 = null;
         Route var9;
         if (var4 != null) {
            var9 = var4.route();
         } else {
            var9 = null;
         }

         int var2 = var1.code();
         String var6 = var1.request().method();
         if (var2 != 307 && var2 != 308) {
            if (var2 == 401) {
               return this.client.authenticator().authenticate(var9, var1);
            }

            if (var2 == 407) {
               if (var9 != null) {
                  var5 = var9.proxy();
               } else {
                  var5 = this.client.proxy();
               }

               if (var5.type() == Type.HTTP) {
                  return this.client.proxyAuthenticator().authenticate(var9, var1);
               }

               throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            }

            if (var2 == 408) {
               if (var1.request().body() instanceof UnrepeatableRequestBody) {
                  return null;
               }

               return var1.request();
            }

            switch(var2) {
            case 300:
            case 301:
            case 302:
            case 303:
               break;
            default:
               return null;
            }
         } else if (!var6.equals("GET") && !var6.equals("HEAD")) {
            return null;
         }

         if (!this.client.followRedirects()) {
            return null;
         } else {
            String var10 = var1.header("Location");
            if (var10 == null) {
               return null;
            } else {
               HttpUrl var7 = var1.request().url().resolve(var10);
               if (var7 == null) {
                  return null;
               } else if (!var7.scheme().equals(var1.request().url().scheme()) && !this.client.followSslRedirects()) {
                  return null;
               } else {
                  Request.Builder var8 = var1.request().newBuilder();
                  if (HttpMethod.permitsRequestBody(var6)) {
                     boolean var3 = HttpMethod.redirectsWithBody(var6);
                     if (HttpMethod.redirectsToGet(var6)) {
                        var8.method("GET", (RequestBody)null);
                     } else {
                        RequestBody var11 = var5;
                        if (var3) {
                           var11 = var1.request().body();
                        }

                        var8.method(var6, var11);
                     }

                     if (!var3) {
                        var8.removeHeader("Transfer-Encoding");
                        var8.removeHeader("Content-Length");
                        var8.removeHeader("Content-Type");
                     }
                  }

                  if (!this.sameConnection(var1, var7)) {
                     var8.removeHeader("Authorization");
                  }

                  return var8.url(var7).build();
               }
            }
         }
      } else {
         throw new IllegalStateException();
      }
   }

   private boolean isRecoverable(IOException var1, boolean var2) {
      boolean var3 = var1 instanceof ProtocolException;
      boolean var4 = false;
      if (var3) {
         return false;
      } else if (var1 instanceof InterruptedIOException) {
         var3 = var4;
         if (var1 instanceof SocketTimeoutException) {
            var3 = var4;
            if (!var2) {
               var3 = true;
            }
         }

         return var3;
      } else if (var1 instanceof SSLHandshakeException && var1.getCause() instanceof CertificateException) {
         return false;
      } else {
         return !(var1 instanceof SSLPeerUnverifiedException);
      }
   }

   private boolean recover(IOException var1, boolean var2, Request var3) {
      this.streamAllocation.streamFailed(var1);
      if (!this.client.retryOnConnectionFailure()) {
         return false;
      } else if (var2 && var3.body() instanceof UnrepeatableRequestBody) {
         return false;
      } else if (!this.isRecoverable(var1, var2)) {
         return false;
      } else {
         return this.streamAllocation.hasMoreRoutes();
      }
   }

   private boolean sameConnection(Response var1, HttpUrl var2) {
      HttpUrl var3 = var1.request().url();
      return var3.host().equals(var2.host()) && var3.port() == var2.port() && var3.scheme().equals(var2.scheme());
   }

   public void cancel() {
      this.canceled = true;
      StreamAllocation var1 = this.streamAllocation;
      if (var1 != null) {
         var1.cancel();
      }

   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      Request var4 = var1.request();
      this.streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(var4.url()), this.callStackTrace);
      int var2 = 0;
      Response var5 = null;

      while(!this.canceled) {
         boolean var3 = false;

         Response var68;
         label771: {
            Throwable var10000;
            label772: {
               IOException var6;
               boolean var10001;
               label773: {
                  RouteException var67;
                  try {
                     try {
                        var68 = ((RealInterceptorChain)var1).proceed(var4, this.streamAllocation, (HttpCodec)null, (RealConnection)null);
                        break label771;
                     } catch (RouteException var60) {
                        var67 = var60;
                     } catch (IOException var61) {
                        var6 = var61;
                        break label773;
                     }
                  } catch (Throwable var62) {
                     var10000 = var62;
                     var10001 = false;
                     break label772;
                  }

                  try {
                     var3 = this.recover(var67.getLastConnectException(), false, var4);
                  } catch (Throwable var59) {
                     var10000 = var59;
                     var10001 = false;
                     break label772;
                  }

                  if (var3) {
                     if (false) {
                        this.streamAllocation.streamFailed((IOException)null);
                        this.streamAllocation.release();
                     }
                     continue;
                  }

                  try {
                     throw var67.getLastConnectException();
                  } catch (Throwable var55) {
                     var10000 = var55;
                     var10001 = false;
                     break label772;
                  }
               }

               label751: {
                  try {
                     if (var6 instanceof ConnectionShutdownException) {
                        break label751;
                     }
                  } catch (Throwable var58) {
                     var10000 = var58;
                     var10001 = false;
                     break label772;
                  }

                  var3 = true;
               }

               try {
                  var3 = this.recover(var6, var3, var4);
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label772;
               }

               if (var3) {
                  if (false) {
                     this.streamAllocation.streamFailed((IOException)null);
                     this.streamAllocation.release();
                  }
                  continue;
               }

               label741:
               try {
                  throw var6;
               } catch (Throwable var56) {
                  var10000 = var56;
                  var10001 = false;
                  break label741;
               }
            }

            Throwable var63 = var10000;
            if (true) {
               this.streamAllocation.streamFailed((IOException)null);
               this.streamAllocation.release();
            }

            throw var63;
         }

         if (false) {
            this.streamAllocation.streamFailed((IOException)null);
            this.streamAllocation.release();
         }

         Response var65 = var68;
         if (var5 != null) {
            var65 = var68.newBuilder().priorResponse(var5.newBuilder().body((ResponseBody)null).build()).build();
         }

         Request var66 = this.followUpRequest(var65);
         if (var66 == null) {
            if (!this.forWebSocket) {
               this.streamAllocation.release();
            }

            return var65;
         }

         Util.closeQuietly((Closeable)var65.body());
         ++var2;
         StringBuilder var64;
         if (var2 > 20) {
            this.streamAllocation.release();
            var64 = new StringBuilder();
            var64.append("Too many follow-up requests: ");
            var64.append(var2);
            throw new ProtocolException(var64.toString());
         }

         if (var66.body() instanceof UnrepeatableRequestBody) {
            this.streamAllocation.release();
            throw new HttpRetryException("Cannot retry streamed HTTP body", var65.code());
         }

         if (!this.sameConnection(var65, var66.url())) {
            this.streamAllocation.release();
            this.streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(var66.url()), this.callStackTrace);
         } else if (this.streamAllocation.codec() != null) {
            var64 = new StringBuilder();
            var64.append("Closing the body of ");
            var64.append(var65);
            var64.append(" didn't close its backing stream. Bad interceptor?");
            throw new IllegalStateException(var64.toString());
         }

         var4 = var66;
         var5 = var65;
      }

      this.streamAllocation.release();
      throw new IOException("Canceled");
   }

   public boolean isCanceled() {
      return this.canceled;
   }

   public void setCallStackTrace(Object var1) {
      this.callStackTrace = var1;
   }

   public StreamAllocation streamAllocation() {
      return this.streamAllocation;
   }
}
