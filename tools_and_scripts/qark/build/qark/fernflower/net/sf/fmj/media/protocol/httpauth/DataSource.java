package net.sf.fmj.media.protocol.httpauth;

import com.lti.utils.StringUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.MediaLocator;
import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.SourceCloneable;
import net.sf.fmj.utility.LoggerSingleton;

public class DataSource extends PullDataSource implements SourceCloneable {
   private static final Logger logger;
   private URLConnection conn;
   private boolean connected = false;
   private ContentDescriptor contentType;
   private String contentTypeStr;
   protected DataSource.URLSourceStream[] sources;

   static {
      logger = LoggerSingleton.logger;
   }

   public DataSource() {
   }

   public DataSource(URL var1) {
      this.setLocator(new MediaLocator(var1));
   }

   private String stripTrailer(String var1) {
      int var2 = var1.indexOf(";");
      return var2 < 0 ? var1 : var1.substring(0, var2);
   }

   public void connect() throws IOException {
      String var4 = this.getLocator().getRemainder();
      int var1 = var4.indexOf(64);
      if (var1 < 0) {
         throw new IOException("Invalid httpauth url: expected: @");
      } else {
         int var2 = var4.indexOf(58);
         if (var2 >= 0 && var2 <= var1) {
            String var3 = var4.substring(0, var2);
            var4 = var4.substring(var2 + 1, var1);
            StringBuilder var5 = new StringBuilder();
            var5.append("http:");
            var5.append(this.getLocator().getRemainder().substring(var1 + 1));
            URLConnection var10 = (new URL(var5.toString())).openConnection();
            this.conn = var10;
            if (var10 instanceof HttpURLConnection) {
               HttpURLConnection var11 = (HttpURLConnection)var10;
               if (var3 != null && !var3.equals("")) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Basic ");
                  StringBuilder var7 = new StringBuilder();
                  var7.append(var3);
                  var7.append(":");
                  var7.append(var4);
                  var6.append(StringUtils.byteArrayToBase64String(var7.toString().getBytes()));
                  var11.setRequestProperty("Authorization", var6.toString());
               }

               var11.connect();
               var1 = var11.getResponseCode();
               if (var1 < 200 || var1 >= 300) {
                  var11.disconnect();
                  StringBuilder var8 = new StringBuilder();
                  var8.append("HTTP response code: ");
                  var8.append(var1);
                  throw new IOException(var8.toString());
               }

               this.contentTypeStr = ContentDescriptor.mimeTypeToPackageName(this.stripTrailer(this.conn.getContentType()));
            } else {
               var10.connect();
               this.contentTypeStr = ContentDescriptor.mimeTypeToPackageName(this.conn.getContentType());
            }

            this.contentType = new ContentDescriptor(this.contentTypeStr);
            DataSource.URLSourceStream[] var9 = new DataSource.URLSourceStream[1];
            this.sources = var9;
            var9[0] = new DataSource.URLSourceStream();
            this.connected = true;
         } else {
            throw new IOException("Invalid httpaut url: expected: :");
         }
      }
   }

   public javax.media.protocol.DataSource createClone() {
      DataSource var1;
      Logger var2;
      Level var3;
      StringBuilder var4;
      try {
         var1 = new DataSource(this.getLocator().getURL());
      } catch (MalformedURLException var6) {
         var2 = logger;
         var3 = Level.WARNING;
         var4 = new StringBuilder();
         var4.append("");
         var4.append(var6);
         var2.log(var3, var4.toString(), var6);
         return null;
      }

      if (this.connected) {
         try {
            var1.connect();
            return var1;
         } catch (IOException var5) {
            var2 = logger;
            var3 = Level.WARNING;
            var4 = new StringBuilder();
            var4.append("");
            var4.append(var5);
            var2.log(var3, var4.toString(), var5);
            return null;
         }
      } else {
         return var1;
      }
   }

   public void disconnect() {
      if (this.connected) {
         URLConnection var1 = this.conn;
         if (var1 != null && var1 instanceof HttpURLConnection) {
            ((HttpURLConnection)var1).disconnect();
         }

         this.connected = false;
      }
   }

   public String getContentType() {
      return this.contentTypeStr;
   }

   public Object getControl(String var1) {
      return null;
   }

   public Object[] getControls() {
      return new Object[0];
   }

   public Time getDuration() {
      return Time.TIME_UNKNOWN;
   }

   public PullSourceStream[] getStreams() {
      if (this.connected) {
         return this.sources;
      } else {
         throw new Error("Unconnected source.");
      }
   }

   public void start() throws IOException {
   }

   public void stop() throws IOException {
   }

   class URLSourceStream implements PullSourceStream {
      private boolean endOfStream = false;

      public boolean endOfStream() {
         return this.endOfStream;
      }

      public ContentDescriptor getContentDescriptor() {
         return DataSource.this.contentType;
      }

      public long getContentLength() {
         return (long)DataSource.this.conn.getContentLength();
      }

      public Object getControl(String var1) {
         return null;
      }

      public Object[] getControls() {
         return new Object[0];
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         var2 = DataSource.this.conn.getInputStream().read(var1, var2, var3);
         if (var2 == -1) {
            this.endOfStream = true;
         }

         return var2;
      }

      public boolean willReadBlock() {
         int var1;
         try {
            var1 = DataSource.this.conn.getInputStream().available();
         } catch (IOException var3) {
            return true;
         }

         return var1 <= 0;
      }
   }
}
