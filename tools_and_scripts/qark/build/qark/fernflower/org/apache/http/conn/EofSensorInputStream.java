package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.util.Args;

public class EofSensorInputStream extends InputStream implements ConnectionReleaseTrigger {
   private final EofSensorWatcher eofWatcher;
   private boolean selfClosed;
   protected InputStream wrappedStream;

   public EofSensorInputStream(InputStream var1, EofSensorWatcher var2) {
      Args.notNull(var1, "Wrapped stream");
      this.wrappedStream = var1;
      this.selfClosed = false;
      this.eofWatcher = var2;
   }

   public void abortConnection() throws IOException {
      this.selfClosed = true;
      this.checkAbort();
   }

   public int available() throws IOException {
      if (this.isReadAllowed()) {
         try {
            int var1 = this.wrappedStream.available();
            return var1;
         } catch (IOException var3) {
            this.checkAbort();
            throw var3;
         }
      } else {
         return 0;
      }
   }

   protected void checkAbort() throws IOException {
      InputStream var2 = this.wrappedStream;
      if (var2 != null) {
         boolean var1 = true;

         label87: {
            Throwable var10000;
            label93: {
               boolean var10001;
               try {
                  if (this.eofWatcher != null) {
                     var1 = this.eofWatcher.streamAbort(var2);
                  }
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label93;
               }

               if (!var1) {
                  break label87;
               }

               label82:
               try {
                  var2.close();
                  break label87;
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label82;
               }
            }

            Throwable var9 = var10000;
            this.wrappedStream = null;
            throw var9;
         }

         this.wrappedStream = null;
      }
   }

   protected void checkClose() throws IOException {
      InputStream var2 = this.wrappedStream;
      if (var2 != null) {
         boolean var1 = true;

         label87: {
            Throwable var10000;
            label93: {
               boolean var10001;
               try {
                  if (this.eofWatcher != null) {
                     var1 = this.eofWatcher.streamClosed(var2);
                  }
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label93;
               }

               if (!var1) {
                  break label87;
               }

               label82:
               try {
                  var2.close();
                  break label87;
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label82;
               }
            }

            Throwable var9 = var10000;
            this.wrappedStream = null;
            throw var9;
         }

         this.wrappedStream = null;
      }
   }

   protected void checkEOF(int var1) throws IOException {
      InputStream var3 = this.wrappedStream;
      if (var3 != null && var1 < 0) {
         boolean var2 = true;

         label92: {
            Throwable var10000;
            label99: {
               boolean var10001;
               try {
                  if (this.eofWatcher != null) {
                     var2 = this.eofWatcher.eofDetected(var3);
                  }
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label99;
               }

               if (!var2) {
                  break label92;
               }

               label87:
               try {
                  var3.close();
                  break label92;
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label87;
               }
            }

            Throwable var10 = var10000;
            this.wrappedStream = null;
            throw var10;
         }

         this.wrappedStream = null;
      }
   }

   public void close() throws IOException {
      this.selfClosed = true;
      this.checkClose();
   }

   InputStream getWrappedStream() {
      return this.wrappedStream;
   }

   protected boolean isReadAllowed() throws IOException {
      if (!this.selfClosed) {
         return this.wrappedStream != null;
      } else {
         throw new IOException("Attempted read on closed stream.");
      }
   }

   boolean isSelfClosed() {
      return this.selfClosed;
   }

   public int read() throws IOException {
      if (this.isReadAllowed()) {
         try {
            int var1 = this.wrappedStream.read();
            this.checkEOF(var1);
            return var1;
         } catch (IOException var3) {
            this.checkAbort();
            throw var3;
         }
      } else {
         return -1;
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.isReadAllowed()) {
         try {
            var2 = this.wrappedStream.read(var1, var2, var3);
            this.checkEOF(var2);
            return var2;
         } catch (IOException var4) {
            this.checkAbort();
            throw var4;
         }
      } else {
         return -1;
      }
   }

   public void releaseConnection() throws IOException {
      this.close();
   }
}
