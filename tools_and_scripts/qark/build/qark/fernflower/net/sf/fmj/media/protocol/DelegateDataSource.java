package net.sf.fmj.media.protocol;

import java.io.IOException;
import javax.media.Buffer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.MediaLocator;
import javax.media.Time;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import net.sf.fmj.media.Log;

public class DelegateDataSource extends PushBufferDataSource implements Streamable {
   protected boolean connected = false;
   protected String contentType = "raw";
   protected PushBufferDataSource master;
   protected boolean started = false;
   protected DelegateDataSource.DelegateStream[] streams;

   public DelegateDataSource(Format[] var1) {
      this.streams = new DelegateDataSource.DelegateStream[var1.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.streams[var2] = new DelegateDataSource.DelegateStream(var1[var2]);
      }

      try {
         this.connect();
      } catch (IOException var3) {
      }
   }

   public void connect() throws IOException {
      if (!this.connected) {
         PushBufferDataSource var1 = this.master;
         if (var1 != null) {
            var1.connect();
         }

         this.connected = true;
      }
   }

   public void disconnect() {
      try {
         if (this.started) {
            this.stop();
         }
      } catch (IOException var2) {
      }

      PushBufferDataSource var1 = this.master;
      if (var1 != null) {
         var1.disconnect();
      }

      this.connected = false;
   }

   public String getContentType() {
      if (!this.connected) {
         System.err.println("Error: DataSource not connected");
         return null;
      } else {
         return this.contentType;
      }
   }

   public Object getControl(String var1) {
      PushBufferDataSource var2 = this.master;
      return var2 != null ? var2.getControl(var1) : null;
   }

   public Object[] getControls() {
      PushBufferDataSource var1 = this.master;
      return var1 != null ? var1.getControls() : new Object[0];
   }

   public Time getDuration() {
      PushBufferDataSource var1 = this.master;
      return var1 != null ? var1.getDuration() : Duration.DURATION_UNKNOWN;
   }

   public MediaLocator getLocator() {
      PushBufferDataSource var1 = this.master;
      return var1 != null ? var1.getLocator() : null;
   }

   public DataSource getMaster() {
      return this.master;
   }

   public PushBufferStream[] getStreams() {
      return this.streams;
   }

   public boolean isPrefetchable() {
      return false;
   }

   public void setMaster(PushBufferDataSource var1) throws IOException {
      this.master = var1;
      PushBufferStream[] var5 = var1.getStreams();

      int var2;
      for(var2 = 0; var2 < var5.length; ++var2) {
         int var3 = 0;

         while(true) {
            DelegateDataSource.DelegateStream[] var4 = this.streams;
            if (var3 >= var4.length) {
               break;
            }

            if (var4[var3].getFormat().matches(var5[var2].getFormat())) {
               this.streams[var3].setMaster(var5[var2]);
            }

            ++var3;
         }
      }

      for(var2 = 0; var2 < var5.length; ++var2) {
         if (this.streams[var2].getMaster() == null) {
            StringBuilder var6 = new StringBuilder();
            var6.append("DelegateDataSource: cannot not find a matching track from the master with this format: ");
            var6.append(this.streams[var2].getFormat());
            Log.error(var6.toString());
         }
      }

      if (this.connected) {
         this.master.connect();
      }

      if (this.started) {
         this.master.start();
      }

   }

   public void start() throws IOException {
      if (this.connected) {
         if (!this.started) {
            PushBufferDataSource var1 = this.master;
            if (var1 != null) {
               var1.start();
            }

            this.started = true;
         }
      } else {
         throw new Error("DataSource must be connected before it can be started");
      }
   }

   public void stop() throws IOException {
      if (this.connected) {
         if (this.started) {
            PushBufferDataSource var1 = this.master;
            if (var1 != null) {
               var1.stop();
            }

            this.started = false;
         }
      }
   }

   class DelegateStream implements PushBufferStream, BufferTransferHandler {
      Format format;
      PushBufferStream master;
      // $FF: renamed from: th javax.media.protocol.BufferTransferHandler
      BufferTransferHandler field_203;

      public DelegateStream(Format var2) {
         this.format = var2;
      }

      public boolean endOfStream() {
         PushBufferStream var1 = this.master;
         return var1 != null ? var1.endOfStream() : false;
      }

      public ContentDescriptor getContentDescriptor() {
         PushBufferStream var1 = this.master;
         return var1 != null ? var1.getContentDescriptor() : new ContentDescriptor("raw");
      }

      public long getContentLength() {
         PushBufferStream var1 = this.master;
         return var1 != null ? var1.getContentLength() : -1L;
      }

      public Object getControl(String var1) {
         PushBufferStream var2 = this.master;
         return var2 != null ? var2.getControl(var1) : null;
      }

      public Object[] getControls() {
         PushBufferStream var1 = this.master;
         return var1 != null ? var1.getControls() : new Object[0];
      }

      public Format getFormat() {
         PushBufferStream var1 = this.master;
         return var1 != null ? var1.getFormat() : this.format;
      }

      public PushBufferStream getMaster() {
         return this.master;
      }

      public void read(Buffer var1) throws IOException {
         PushBufferStream var2 = this.master;
         if (var2 != null) {
            var2.read(var1);
         }

         throw new IOException("No data available");
      }

      public void setMaster(PushBufferStream var1) {
         this.master = var1;
         var1.setTransferHandler(this);
      }

      public void setTransferHandler(BufferTransferHandler var1) {
         this.field_203 = var1;
      }

      public void transferData(PushBufferStream var1) {
         BufferTransferHandler var2 = this.field_203;
         if (var2 != null) {
            var2.transferData(var1);
         }

      }
   }
}
