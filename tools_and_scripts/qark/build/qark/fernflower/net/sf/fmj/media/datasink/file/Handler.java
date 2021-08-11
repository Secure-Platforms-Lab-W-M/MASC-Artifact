package net.sf.fmj.media.datasink.file;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import javax.media.Control;
import javax.media.IncompatibleSourceException;
import javax.media.MediaLocator;
import javax.media.protocol.DataSource;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PushDataSource;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.Seekable;
import javax.media.protocol.SourceStream;
import javax.media.protocol.SourceTransferHandler;
import net.sf.fmj.media.Syncable;
import net.sf.fmj.media.datasink.BasicDataSink;
import net.sf.fmj.media.datasink.RandomAccess;

public class Handler extends BasicDataSink implements SourceTransferHandler, Seekable, Runnable, RandomAccess, Syncable {
   protected static final int BUFFER_LEN = 131072;
   protected static final int CLOSED = 3;
   private static final boolean DEBUG = false;
   protected static final int NOT_INITIALIZED = 0;
   protected static final int OPENED = 1;
   protected static final int STARTED = 2;
   public int WRITE_CHUNK_SIZE = 16384;
   protected byte[] buffer1 = new byte[131072];
   protected int buffer1Length;
   protected boolean buffer1Pending = false;
   protected long buffer1PendingLocation = -1L;
   protected byte[] buffer2 = new byte[131072];
   protected int buffer2Length;
   protected boolean buffer2Pending = false;
   protected long buffer2PendingLocation = -1L;
   private Integer bufferLock = 0;
   protected int bytesWritten = 0;
   protected String contentType = null;
   protected Control[] controls;
   private boolean errorCreatingStreamingFile = false;
   protected boolean errorEncountered = false;
   protected String errorReason = null;
   protected File file;
   protected boolean fileClosed = false;
   protected FileDescriptor fileDescriptor = null;
   protected int filePointer = 0;
   protected int fileSize = 0;
   long lastSyncTime = -1L;
   protected MediaLocator locator = null;
   protected long nextLocation = 0L;
   protected boolean push;
   protected RandomAccessFile qtStrRaFile = null;
   protected RandomAccessFile raFile = null;
   private boolean receivedEOS = false;
   protected DataSource source;
   protected int state = 0;
   protected SourceStream stream;
   private boolean streamingEnabled = false;
   protected SourceStream[] streams;
   protected boolean syncEnabled = false;
   protected File tempFile = null;
   protected Thread writeThread = null;

   private boolean deleteFile(File var1) {
      try {
         boolean var2 = var1.delete();
         return var2;
      } finally {
         ;
      }
   }

   private void write(byte[] var1, long var2, int var4) {
      IOException var10000;
      label70: {
         boolean var10001;
         if (var2 != -1L) {
            try {
               this.doSeek(var2);
            } catch (IOException var13) {
               var10000 = var13;
               var10001 = false;
               break label70;
            }
         }

         int var5 = 0;

         while(true) {
            if (var4 <= 0) {
               return;
            }

            int var7;
            try {
               var7 = this.WRITE_CHUNK_SIZE;
            } catch (IOException var10) {
               var10000 = var10;
               var10001 = false;
               break;
            }

            int var6 = var7;
            if (var4 < var7) {
               var6 = var4;
            }

            try {
               this.raFile.write(var1, var5, var6);
               var7 = this.bytesWritten + var6;
               this.bytesWritten = var7;
               if (this.fileDescriptor != null && this.syncEnabled && var7 >= this.WRITE_CHUNK_SIZE) {
                  this.bytesWritten = var7 - this.WRITE_CHUNK_SIZE;
                  this.fileDescriptor.sync();
               }
            } catch (IOException var12) {
               var10000 = var12;
               var10001 = false;
               break;
            }

            try {
               var7 = this.filePointer + var6;
               this.filePointer = var7;
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
               break;
            }

            var4 -= var6;
            var5 += var6;

            try {
               if (var7 > this.fileSize) {
                  this.fileSize = var7;
               }
            } catch (IOException var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }

            try {
               Thread.yield();
            } catch (IOException var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }
         }
      }

      IOException var14 = var10000;
      this.errorEncountered = true;
      this.errorReason = var14.toString();
   }

   public void close() {
      this.close((String)null);
   }

   protected final void close(String param1) {
      // $FF: Couldn't be decompiled
   }

   public long doSeek(long var1) {
      RandomAccessFile var3 = this.raFile;
      if (var3 != null) {
         try {
            var3.seek(var1);
            this.filePointer = (int)var1;
            return var1;
         } catch (IOException var5) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Error in seek: ");
            var4.append(var5);
            this.close(var4.toString());
         }
      }

      return -1L;
   }

   public long doTell() {
      RandomAccessFile var3 = this.raFile;
      if (var3 != null) {
         try {
            long var1 = var3.getFilePointer();
            return var1;
         } catch (IOException var5) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Error in tell: ");
            var4.append(var5);
            this.close(var4.toString());
         }
      }

      return -1L;
   }

   public String getContentType() {
      return this.contentType;
   }

   public Object getControl(String var1) {
      return null;
   }

   public Object[] getControls() {
      if (this.controls == null) {
         this.controls = new Control[0];
      }

      return this.controls;
   }

   public MediaLocator getOutputLocator() {
      return this.locator;
   }

   public boolean isRandomAccess() {
      return true;
   }

   public void open() throws IOException, SecurityException {
      // $FF: Couldn't be decompiled
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public long seek(long var1) {
      synchronized(this){}

      try {
         this.nextLocation = var1;
      } finally {
         ;
      }

      return var1;
   }

   public void setEnabled(boolean var1) {
      this.streamingEnabled = var1;
   }

   public void setOutputLocator(MediaLocator var1) {
      this.locator = var1;
   }

   public void setSource(DataSource var1) throws IncompatibleSourceException {
      if (!(var1 instanceof PushDataSource) && !(var1 instanceof PullDataSource)) {
         throw new IncompatibleSourceException("Incompatible datasource");
      } else {
         this.source = var1;
         if (var1 instanceof PushDataSource) {
            this.push = true;

            try {
               ((PushDataSource)var1).connect();
            } catch (IOException var3) {
            }

            this.streams = ((PushDataSource)this.source).getStreams();
         } else {
            this.push = false;

            try {
               ((PullDataSource)var1).connect();
            } catch (IOException var2) {
            }

            this.streams = ((PullDataSource)this.source).getStreams();
         }

         SourceStream[] var4 = this.streams;
         if (var4 != null && var4.length == 1) {
            this.stream = var4[0];
            this.contentType = this.source.getContentType();
            if (this.push) {
               ((PushSourceStream)this.stream).setTransferHandler(this);
            }

         } else {
            throw new IncompatibleSourceException("DataSource should have 1 stream");
         }
      }
   }

   protected void setState(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void setSyncEnabled() {
      this.syncEnabled = true;
   }

   public void start() throws IOException {
      if (this.state == 1) {
         DataSource var1 = this.source;
         if (var1 != null) {
            var1.start();
         }

         if (this.writeThread == null) {
            Thread var2 = new Thread(this);
            this.writeThread = var2;
            var2.start();
         }

         this.setState(2);
      }

   }

   public void stop() throws IOException {
      if (this.state == 2) {
         DataSource var1 = this.source;
         if (var1 != null) {
            var1.stop();
         }

         this.setState(1);
      }

   }

   public long tell() {
      return this.nextLocation;
   }

   public void transferData(PushSourceStream param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean write(long var1, int var3) {
      Exception var10000;
      boolean var10001;
      if (var1 >= 0L && var3 > 0) {
         label43: {
            try {
               this.raFile.seek(var1);
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label43;
            }

            while(true) {
               if (var3 <= 0) {
                  return true;
               }

               int var4 = 131072;
               if (var3 <= 131072) {
                  var4 = var3;
               }

               try {
                  this.raFile.read(this.buffer1, 0, var4);
                  this.qtStrRaFile.write(this.buffer1, 0, var4);
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }

               var3 -= var4;
            }
         }
      } else if (var1 < 0L && var3 > 0) {
         try {
            this.qtStrRaFile.seek(0L);
            this.qtStrRaFile.seek((long)(var3 - 1));
            this.qtStrRaFile.writeByte(0);
            this.qtStrRaFile.seek(0L);
            return true;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
         }
      } else {
         try {
            this.sendEndofStreamEvent();
            return true;
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
         }
      }

      Exception var5 = var10000;
      this.errorCreatingStreamingFile = true;
      PrintStream var6 = System.err;
      StringBuilder var7 = new StringBuilder();
      var7.append("Exception when creating streamable version of media file: ");
      var7.append(var5.getMessage());
      var6.println(var7.toString());
      return false;
   }
}
