package net.sf.fmj.media.multiplexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.media.Buffer;
import net.sf.fmj.media.BufferQueueInputStream;
import net.sf.fmj.utility.LoggerSingleton;

public class StreamPipe {
   private static final Logger logger;
   // $FF: renamed from: is net.sf.fmj.media.BufferQueueInputStream
   private final BufferQueueInputStream field_56 = new BufferQueueInputStream();
   // $FF: renamed from: os net.sf.fmj.media.multiplexer.StreamPipe$MyOutputStream
   private final StreamPipe.MyOutputStream field_57 = new StreamPipe.MyOutputStream();

   static {
      logger = LoggerSingleton.logger;
   }

   private Buffer createBuffer(byte[] var1, int var2, int var3) {
      Buffer var4 = new Buffer();
      var4.setData(var1);
      var4.setOffset(var2);
      var4.setLength(var3);
      return var4;
   }

   private Buffer createEOMBuffer() {
      Buffer var1 = new Buffer();
      var1.setData(new byte[0]);
      var1.setOffset(0);
      var1.setLength(0);
      var1.setEOM(true);
      return var1;
   }

   public InputStream getInputStream() {
      return this.field_56;
   }

   public OutputStream getOutputStream() {
      return this.field_57;
   }

   private class MyOutputStream extends OutputStream {
      private MyOutputStream() {
      }

      // $FF: synthetic method
      MyOutputStream(Object var2) {
         this();
      }

      public void close() throws IOException {
         StreamPipe.logger.finer("MyOutputStream Closing, putting EOM buffer");
         StreamPipe.this.field_56.blockingPut(StreamPipe.this.createEOMBuffer());
         super.close();
      }

      public void write(int var1) throws IOException {
         this.write(new byte[]{(byte)var1});
      }

      public void write(byte[] var1) throws IOException {
         this.write(var1, 0, var1.length);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         StreamPipe.this.field_56.blockingPut(StreamPipe.this.createBuffer(var1, var2, var3));
      }
   }
}
