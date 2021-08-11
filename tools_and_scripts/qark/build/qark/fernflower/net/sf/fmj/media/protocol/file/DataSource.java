package net.sf.fmj.media.protocol.file;

import com.lti.utils.PathUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Time;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;
import javax.media.protocol.Seekable;
import javax.media.protocol.SourceCloneable;
import net.sf.fmj.media.MimeManager;
import net.sf.fmj.utility.LoggerSingleton;
import net.sf.fmj.utility.URLUtils;

public class DataSource extends PullDataSource implements SourceCloneable {
   private static final Logger logger;
   protected boolean connected = false;
   private long contentLength = -1L;
   protected ContentDescriptor contentType;
   protected RandomAccessFile raf;
   protected DataSource.RAFPullSourceStream[] sources;

   static {
      logger = LoggerSingleton.logger;
   }

   private static String getContentTypeFor(String var0) {
      String var1 = MimeManager.getMimeType(PathUtils.extractExtension(var0));
      return var1 != null ? var1 : URLConnection.getFileNameMap().getContentTypeFor(var0);
   }

   public void connect() throws IOException {
      IOException var10000;
      label37: {
         String var1;
         boolean var10001;
         try {
            var1 = URLUtils.extractValidPathFromFileUrl(this.getLocator().toExternalForm());
         } catch (IOException var9) {
            var10000 = var9;
            var10001 = false;
            break label37;
         }

         if (var1 != null) {
            label31: {
               String var13;
               try {
                  RandomAccessFile var2 = new RandomAccessFile(var1, "r");
                  this.raf = var2;
                  this.contentLength = var2.length();
                  var13 = getContentTypeFor(var1);
               } catch (IOException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label31;
               }

               if (var13 != null) {
                  try {
                     this.contentType = new ContentDescriptor(ContentDescriptor.mimeTypeToPackageName(var13));
                     DataSource.RAFPullSourceStream[] var10 = new DataSource.RAFPullSourceStream[1];
                     this.sources = var10;
                     var10[0] = new DataSource.RAFPullSourceStream();
                     this.connected = true;
                     return;
                  } catch (IOException var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               } else {
                  try {
                     StringBuilder var14 = new StringBuilder();
                     var14.append("Unknown content type for path: ");
                     var14.append(var1);
                     throw new IOException(var14.toString());
                  } catch (IOException var6) {
                     var10000 = var6;
                     var10001 = false;
                  }
               }
            }
         } else {
            try {
               StringBuilder var12 = new StringBuilder();
               var12.append("Cannot determine valid file path from URL: ");
               var12.append(this.getLocator().toExternalForm());
               throw new IOException(var12.toString());
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
            }
         }
      }

      IOException var11 = var10000;
      Logger var15 = logger;
      Level var3 = Level.WARNING;
      StringBuilder var4 = new StringBuilder();
      var4.append("");
      var4.append(var11);
      var15.log(var3, var4.toString(), var11);
      throw var11;
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

   class RAFPullSourceStream implements PullSourceStream, Seekable {
      private boolean endOfStream = false;

      public boolean endOfStream() {
         return this.endOfStream;
      }

      public ContentDescriptor getContentDescriptor() {
         return DataSource.this.contentType;
      }

      public long getContentLength() {
         return DataSource.this.contentLength;
      }

      public Object getControl(String var1) {
         return null;
      }

      public Object[] getControls() {
         return new Object[0];
      }

      public boolean isRandomAccess() {
         return true;
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         var2 = DataSource.this.raf.read(var1, var2, var3);
         if (var2 == -1) {
            this.endOfStream = true;
         }

         return var2;
      }

      public long seek(long var1) {
         try {
            DataSource.this.raf.seek(var1);
            var1 = DataSource.this.raf.getFilePointer();
            return var1;
         } catch (IOException var7) {
            Logger var4 = DataSource.logger;
            Level var5 = Level.WARNING;
            StringBuilder var6 = new StringBuilder();
            var6.append("");
            var6.append(var7);
            var4.log(var5, var6.toString(), var7);
            throw new RuntimeException(var7);
         }
      }

      public long tell() {
         try {
            long var1 = DataSource.this.raf.getFilePointer();
            return var1;
         } catch (IOException var7) {
            Logger var4 = DataSource.logger;
            Level var5 = Level.WARNING;
            StringBuilder var6 = new StringBuilder();
            var6.append("");
            var6.append(var7);
            var4.log(var5, var6.toString(), var7);
            throw new RuntimeException(var7);
         }
      }

      public boolean willReadBlock() {
         return false;
      }
   }
}
