package okhttp3.internal.cache;

import java.io.IOException;
import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

class FaultHidingSink extends ForwardingSink {
   private boolean hasErrors;

   FaultHidingSink(Sink var1) {
      super(var1);
   }

   public void close() throws IOException {
      if (!this.hasErrors) {
         try {
            super.close();
         } catch (IOException var2) {
            this.hasErrors = true;
            this.onException(var2);
         }
      }
   }

   public void flush() throws IOException {
      if (!this.hasErrors) {
         try {
            super.flush();
         } catch (IOException var2) {
            this.hasErrors = true;
            this.onException(var2);
         }
      }
   }

   protected void onException(IOException var1) {
   }

   public void write(Buffer var1, long var2) throws IOException {
      if (this.hasErrors) {
         var1.skip(var2);
      } else {
         try {
            super.write(var1, var2);
         } catch (IOException var4) {
            this.hasErrors = true;
            this.onException(var4);
         }
      }
   }
}
