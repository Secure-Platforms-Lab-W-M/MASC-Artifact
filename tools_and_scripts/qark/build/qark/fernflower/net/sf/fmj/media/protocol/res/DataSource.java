package net.sf.fmj.media.protocol.res;

import com.lti.utils.PathUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.SourceCloneable;
import net.sf.fmj.media.MimeManager;
import net.sf.fmj.utility.LoggerSingleton;

public class DataSource extends PullDataSource implements SourceCloneable {
   private static final Logger logger;
   private boolean connected = false;
   private ContentDescriptor contentType;
   private InputStream inputStream;
   private DataSource.ResSourceStream[] sources;

   static {
      logger = LoggerSingleton.logger;
   }

   private static String getContentTypeFor(String var0) {
      String var1 = MimeManager.getMimeType(PathUtils.extractExtension(var0));
      return var1 != null ? var1 : URLConnection.getFileNameMap().getContentTypeFor(var0);
   }

   private String stripTrailer(String var1) {
      int var2 = var1.indexOf(";");
      return var2 < 0 ? var1 : var1.substring(0, var2);
   }

   public void connect() throws IOException {
      String var1 = this.getLocator().getRemainder();
      this.inputStream = DataSource.class.getResourceAsStream(var1);
      String var2 = getContentTypeFor(var1);
      if (var2 != null) {
         this.contentType = new ContentDescriptor(ContentDescriptor.mimeTypeToPackageName(var2));
         DataSource.ResSourceStream[] var3 = new DataSource.ResSourceStream[1];
         this.sources = var3;
         var3[0] = new DataSource.ResSourceStream();
         this.connected = true;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unknown content type for path: ");
         var4.append(var1);
         throw new IOException(var4.toString());
      }
   }

   public javax.media.protocol.DataSource createClone() {
      DataSource var1 = new DataSource();
      var1.setLocator(this.getLocator());
      if (this.connected) {
         try {
            var1.connect();
            return var1;
         } catch (IOException var5) {
            Logger var2 = logger;
            Level var3 = Level.WARNING;
            StringBuilder var4 = new StringBuilder();
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
         InputStream var1 = this.inputStream;
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var5) {
               Logger var2 = logger;
               Level var3 = Level.WARNING;
               StringBuilder var4 = new StringBuilder();
               var4.append("");
               var4.append(var5);
               var2.log(var3, var4.toString(), var5);
            }
         }

         this.connected = false;
      }
   }

   public String getContentType() {
      if (this.connected) {
         return ContentDescriptor.mimeTypeToPackageName(getContentTypeFor(this.getLocator().getRemainder()));
      } else {
         throw new Error("Source is unconnected.");
      }
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

   class ResSourceStream implements PullSourceStream {
      private boolean endOfStream = false;

      public boolean endOfStream() {
         return this.endOfStream;
      }

      public ContentDescriptor getContentDescriptor() {
         return DataSource.this.contentType;
      }

      public long getContentLength() {
         return -1L;
      }

      public Object getControl(String var1) {
         return null;
      }

      public Object[] getControls() {
         return new Object[0];
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         var2 = DataSource.this.inputStream.read(var1, var2, var3);
         if (var2 == -1) {
            this.endOfStream = true;
         }

         return var2;
      }

      public boolean willReadBlock() {
         int var1;
         try {
            var1 = DataSource.this.inputStream.available();
         } catch (IOException var3) {
            return true;
         }

         return var1 <= 0;
      }
   }
}
