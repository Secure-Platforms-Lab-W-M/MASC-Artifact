package okhttp3.internal;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.StreamAllocation;

public abstract class Internal {
   public static Internal instance;

   public static void initializeInstanceForTests() {
      new OkHttpClient();
   }

   public abstract void addLenient(Headers.Builder var1, String var2);

   public abstract void addLenient(Headers.Builder var1, String var2, String var3);

   public abstract void apply(ConnectionSpec var1, SSLSocket var2, boolean var3);

   public abstract int code(Response.Builder var1);

   public abstract boolean connectionBecameIdle(ConnectionPool var1, RealConnection var2);

   public abstract Socket deduplicate(ConnectionPool var1, Address var2, StreamAllocation var3);

   public abstract boolean equalsNonHost(Address var1, Address var2);

   public abstract RealConnection get(ConnectionPool var1, Address var2, StreamAllocation var3, Route var4);

   public abstract HttpUrl getHttpUrlChecked(String var1) throws MalformedURLException, UnknownHostException;

   public abstract Call newWebSocketCall(OkHttpClient var1, Request var2);

   public abstract void put(ConnectionPool var1, RealConnection var2);

   public abstract RouteDatabase routeDatabase(ConnectionPool var1);

   public abstract void setCache(OkHttpClient.Builder var1, InternalCache var2);

   public abstract StreamAllocation streamAllocation(Call var1);
}
