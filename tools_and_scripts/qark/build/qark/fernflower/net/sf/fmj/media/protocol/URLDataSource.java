package net.sf.fmj.media.protocol;

import com.lti.utils.PathUtils;
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
import javax.media.protocol.DataSource;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.SourceCloneable;
import net.sf.fmj.media.MimeManager;
import net.sf.fmj.utility.LoggerSingleton;
import net.sf.fmj.utility.URLUtils;

public class URLDataSource extends PullDataSource implements SourceCloneable {
   private static final Logger logger;
   protected URLConnection conn;
   protected boolean connected = false;
   private ContentDescriptor contentType;
   private String contentTypeStr;
   protected URLDataSource.URLSourceStream[] sources;

   static {
      logger = LoggerSingleton.logger;
   }

   protected URLDataSource() {
   }

   public URLDataSource(URL var1) {
      this.setLocator(new MediaLocator(var1));
   }

   private String stripTrailer(String var1) {
      int var2 = var1.indexOf(";");
      return var2 < 0 ? var1 : var1.substring(0, var2);
   }

   public void connect() throws IOException {
      URL var4 = this.getLocator().getURL();
      URL var3 = var4;
      if (var4.getProtocol().equals("file")) {
         String var5 = URLUtils.createAbsoluteFileUrl(var4.toExternalForm());
         var3 = var4;
         if (var5 != null) {
            var3 = var4;
            if (!var5.toString().equals(var4.toExternalForm())) {
               Logger var8 = logger;
               StringBuilder var6 = new StringBuilder();
               var6.append("Changing file URL to absolute for URL.openConnection, from ");
               var6.append(var4.toExternalForm());
               var6.append(" to ");
               var6.append(var5);
               var8.warning(var6.toString());
               var3 = new URL(var5);
            }
         }
      }

      this.conn = var3.openConnection();
      Logger var11;
      StringBuilder var13;
      if (!var3.getProtocol().equals("ftp") && this.conn.getURL().getProtocol().equals("ftp")) {
         var11 = logger;
         var13 = new StringBuilder();
         var13.append("URL.openConnection() morphed ");
         var13.append(var3);
         var13.append(" to ");
         var13.append(this.conn.getURL());
         var11.warning(var13.toString());
         StringBuilder var16 = new StringBuilder();
         var16.append("URL.openConnection() returned an FTP connection for a non-ftp url: ");
         var16.append(var3);
         throw new IOException(var16.toString());
      } else {
         URLConnection var9 = this.conn;
         if (var9 instanceof HttpURLConnection) {
            HttpURLConnection var10 = (HttpURLConnection)var9;
            var10.connect();
            int var1 = var10.getResponseCode();
            if (var1 < 200 || var1 >= 300) {
               var10.disconnect();
               StringBuilder var14 = new StringBuilder();
               var14.append("HTTP response code: ");
               var14.append(var1);
               throw new IOException(var14.toString());
            }

            var11 = logger;
            var13 = new StringBuilder();
            var13.append("URL: ");
            var13.append(var3);
            var11.finer(var13.toString());
            var11 = logger;
            var13 = new StringBuilder();
            var13.append("Response code: ");
            var13.append(var1);
            var11.finer(var13.toString());
            var11 = logger;
            var13 = new StringBuilder();
            var13.append("Full content type: ");
            var13.append(this.conn.getContentType());
            var11.finer(var13.toString());
            boolean var2 = false;
            boolean var7 = var2;
            if (this.stripTrailer(this.conn.getContentType()).equals("text/plain")) {
               String var12 = PathUtils.extractExtension(var3.getPath());
               var7 = var2;
               if (var12 != null) {
                  var12 = MimeManager.getMimeType(var12);
                  var7 = var2;
                  if (var12 != null) {
                     this.contentTypeStr = ContentDescriptor.mimeTypeToPackageName(var12);
                     var7 = true;
                     var11 = logger;
                     var13 = new StringBuilder();
                     var13.append("Received content type ");
                     var13.append(this.conn.getContentType());
                     var13.append("; overriding based on extension, to: ");
                     var13.append(var12);
                     var11.fine(var13.toString());
                  }
               }
            }

            if (!var7) {
               this.contentTypeStr = ContentDescriptor.mimeTypeToPackageName(this.stripTrailer(this.conn.getContentType()));
            }
         } else {
            var9.connect();
            this.contentTypeStr = ContentDescriptor.mimeTypeToPackageName(this.conn.getContentType());
         }

         this.contentType = new ContentDescriptor(this.contentTypeStr);
         URLDataSource.URLSourceStream[] var15 = new URLDataSource.URLSourceStream[1];
         this.sources = var15;
         var15[0] = new URLDataSource.URLSourceStream();
         this.connected = true;
      }
   }

   public DataSource createClone() {
      URLDataSource var1;
      Logger var2;
      Level var3;
      StringBuilder var4;
      try {
         var1 = new URLDataSource(this.getLocator().getURL());
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
         return URLDataSource.this.contentType;
      }

      public long getContentLength() {
         return (long)URLDataSource.this.conn.getContentLength();
      }

      public Object getControl(String var1) {
         return null;
      }

      public Object[] getControls() {
         return new Object[0];
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         var2 = URLDataSource.this.conn.getInputStream().read(var1, var2, var3);
         if (var2 == -1) {
            this.endOfStream = true;
         }

         return var2;
      }

      public boolean willReadBlock() {
         int var1;
         try {
            var1 = URLDataSource.this.conn.getInputStream().available();
         } catch (IOException var3) {
            return true;
         }

         return var1 <= 0;
      }
   }
}
