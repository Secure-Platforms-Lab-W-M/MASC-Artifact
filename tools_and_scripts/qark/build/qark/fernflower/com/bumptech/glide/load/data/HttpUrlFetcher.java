package com.bumptech.glide.load.data;

import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.LogTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUrlFetcher implements DataFetcher {
   static final HttpUrlFetcher.HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new HttpUrlFetcher.DefaultHttpUrlConnectionFactory();
   private static final int INVALID_STATUS_CODE = -1;
   private static final int MAXIMUM_REDIRECTS = 5;
   private static final String TAG = "HttpUrlFetcher";
   private final HttpUrlFetcher.HttpUrlConnectionFactory connectionFactory;
   private final GlideUrl glideUrl;
   private volatile boolean isCancelled;
   private InputStream stream;
   private final int timeout;
   private HttpURLConnection urlConnection;

   public HttpUrlFetcher(GlideUrl var1, int var2) {
      this(var1, var2, DEFAULT_CONNECTION_FACTORY);
   }

   HttpUrlFetcher(GlideUrl var1, int var2, HttpUrlFetcher.HttpUrlConnectionFactory var3) {
      this.glideUrl = var1;
      this.timeout = var2;
      this.connectionFactory = var3;
   }

   private InputStream getStreamForSuccessfulRequest(HttpURLConnection var1) throws IOException {
      if (TextUtils.isEmpty(var1.getContentEncoding())) {
         int var2 = var1.getContentLength();
         this.stream = ContentLengthInputStream.obtain(var1.getInputStream(), (long)var2);
      } else {
         if (Log.isLoggable("HttpUrlFetcher", 3)) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Got non empty content encoding: ");
            var3.append(var1.getContentEncoding());
            Log.d("HttpUrlFetcher", var3.toString());
         }

         this.stream = var1.getInputStream();
      }

      return this.stream;
   }

   private static boolean isHttpOk(int var0) {
      return var0 / 100 == 2;
   }

   private static boolean isHttpRedirect(int var0) {
      return var0 / 100 == 3;
   }

   private InputStream loadDataWithRedirects(URL var1, int var2, URL var3, Map var4) throws IOException {
      if (var2 >= 5) {
         throw new HttpException("Too many (> 5) redirects!");
      } else {
         if (var3 != null) {
            try {
               if (var1.toURI().equals(var3.toURI())) {
                  throw new HttpException("In re-direct loop");
               }
            } catch (URISyntaxException var7) {
            }
         }

         this.urlConnection = this.connectionFactory.build(var1);
         Iterator var8 = var4.entrySet().iterator();

         while(var8.hasNext()) {
            Entry var6 = (Entry)var8.next();
            this.urlConnection.addRequestProperty((String)var6.getKey(), (String)var6.getValue());
         }

         this.urlConnection.setConnectTimeout(this.timeout);
         this.urlConnection.setReadTimeout(this.timeout);
         this.urlConnection.setUseCaches(false);
         this.urlConnection.setDoInput(true);
         this.urlConnection.setInstanceFollowRedirects(false);
         this.urlConnection.connect();
         this.stream = this.urlConnection.getInputStream();
         if (this.isCancelled) {
            return null;
         } else {
            int var5 = this.urlConnection.getResponseCode();
            if (isHttpOk(var5)) {
               return this.getStreamForSuccessfulRequest(this.urlConnection);
            } else if (isHttpRedirect(var5)) {
               String var9 = this.urlConnection.getHeaderField("Location");
               if (!TextUtils.isEmpty(var9)) {
                  var3 = new URL(var1, var9);
                  this.cleanup();
                  return this.loadDataWithRedirects(var3, var2 + 1, var1, var4);
               } else {
                  throw new HttpException("Received empty or null redirect url");
               }
            } else if (var5 == -1) {
               throw new HttpException(var5);
            } else {
               throw new HttpException(this.urlConnection.getResponseMessage(), var5);
            }
         }
      }
   }

   public void cancel() {
      this.isCancelled = true;
   }

   public void cleanup() {
      InputStream var1 = this.stream;
      if (var1 != null) {
         try {
            var1.close();
         } catch (IOException var2) {
         }
      }

      HttpURLConnection var3 = this.urlConnection;
      if (var3 != null) {
         var3.disconnect();
      }

      this.urlConnection = null;
   }

   public Class getDataClass() {
      return InputStream.class;
   }

   public DataSource getDataSource() {
      return DataSource.REMOTE;
   }

   public void loadData(Priority var1, DataFetcher.DataCallback var2) {
      long var3 = LogTime.getLogTime();
      boolean var7 = false;

      StringBuilder var10;
      label94: {
         label95: {
            try {
               var7 = true;
               var2.onDataReady(this.loadDataWithRedirects(this.glideUrl.toURL(), 0, (URL)null, this.glideUrl.getHeaders()));
               var7 = false;
               break label95;
            } catch (IOException var8) {
               if (Log.isLoggable("HttpUrlFetcher", 3)) {
                  Log.d("HttpUrlFetcher", "Failed to load data for url", var8);
               }

               var2.onLoadFailed(var8);
               var7 = false;
            } finally {
               if (var7) {
                  if (Log.isLoggable("HttpUrlFetcher", 2)) {
                     StringBuilder var11 = new StringBuilder();
                     var11.append("Finished http url fetcher fetch in ");
                     var11.append(LogTime.getElapsedMillis(var3));
                     Log.v("HttpUrlFetcher", var11.toString());
                  }

               }
            }

            if (!Log.isLoggable("HttpUrlFetcher", 2)) {
               return;
            }

            var10 = new StringBuilder();
            break label94;
         }

         if (!Log.isLoggable("HttpUrlFetcher", 2)) {
            return;
         }

         var10 = new StringBuilder();
      }

      var10.append("Finished http url fetcher fetch in ");
      var10.append(LogTime.getElapsedMillis(var3));
      Log.v("HttpUrlFetcher", var10.toString());
   }

   private static class DefaultHttpUrlConnectionFactory implements HttpUrlFetcher.HttpUrlConnectionFactory {
      DefaultHttpUrlConnectionFactory() {
      }

      public HttpURLConnection build(URL var1) throws IOException {
         return (HttpURLConnection)var1.openConnection();
      }
   }

   interface HttpUrlConnectionFactory {
      HttpURLConnection build(URL var1) throws IOException;
   }
}
