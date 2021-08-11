package javax.media.protocol;

import java.io.IOException;
import java.io.InputStream;

public class InputSourceStream implements PullSourceStream {
   private ContentDescriptor contentDescriptor;
   protected boolean eosReached;
   protected InputStream stream;

   public InputSourceStream(InputStream var1, ContentDescriptor var2) {
      this.stream = var1;
      this.contentDescriptor = var2;
   }

   public void close() throws IOException {
      this.stream.close();
   }

   public boolean endOfStream() {
      return this.eosReached;
   }

   public ContentDescriptor getContentDescriptor() {
      return this.contentDescriptor;
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
      var2 = this.stream.read(var1, var2, var3);
      if (var2 == -1) {
         this.eosReached = true;
      }

      return var2;
   }

   public boolean willReadBlock() {
      int var1;
      try {
         var1 = this.stream.available();
      } catch (IOException var3) {
         return true;
      }

      return var1 <= 0;
   }
}
