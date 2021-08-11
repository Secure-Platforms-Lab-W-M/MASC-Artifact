package net.sf.fmj.media;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Track;
import net.sf.fmj.utility.LoggerSingleton;

public class TrackInputStream extends InputStream {
   private static final Logger logger;
   private Buffer buffer;
   private final Track track;

   static {
      logger = LoggerSingleton.logger;
   }

   public TrackInputStream(Track var1) {
      this.track = var1;
   }

   private void fillBuffer() {
      if (this.buffer == null) {
         Buffer var1 = new Buffer();
         this.buffer = var1;
         var1.setFormat(this.track.getFormat());
      }

      while(!this.buffer.isEOM()) {
         if (this.buffer.getLength() > 0) {
            return;
         }

         this.track.readFrame(this.buffer);
         Logger var3 = logger;
         StringBuilder var2 = new StringBuilder();
         var2.append("Read buffer from track: ");
         var2.append(this.buffer.getLength());
         var3.fine(var2.toString());
         if (!this.buffer.isDiscard()) {
            return;
         }
      }

   }

   public Buffer getBuffer() {
      return this.buffer;
   }

   public int read() throws IOException {
      this.fillBuffer();
      if (this.buffer.getLength() == 0 && this.buffer.isEOM()) {
         return -1;
      } else {
         byte var1 = ((byte[])((byte[])this.buffer.getData()))[this.buffer.getOffset()];
         Buffer var2 = this.buffer;
         var2.setOffset(var2.getOffset() + 1);
         var2 = this.buffer;
         var2.setLength(var2.getLength() - 1);
         return var1 & 255;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      this.fillBuffer();
      if (this.buffer.getLength() == 0 && this.buffer.isEOM()) {
         return -1;
      } else {
         byte[] var4 = (byte[])((byte[])this.buffer.getData());
         if (this.buffer.getLength() < var3) {
            var3 = this.buffer.getLength();
         }

         System.arraycopy(var4, this.buffer.getOffset(), var1, var2, var3);
         Buffer var5 = this.buffer;
         var5.setOffset(var5.getOffset() + var3);
         var5 = this.buffer;
         var5.setLength(var5.getLength() - var3);
         return var3;
      }
   }
}
