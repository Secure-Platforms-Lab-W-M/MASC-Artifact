package okio;

import java.io.IOException;

public abstract class ForwardingSink implements Sink {
   private final Sink delegate;

   public ForwardingSink(Sink var1) {
      if (var1 != null) {
         this.delegate = var1;
      } else {
         throw new IllegalArgumentException("delegate == null");
      }
   }

   public void close() throws IOException {
      this.delegate.close();
   }

   public final Sink delegate() {
      return this.delegate;
   }

   public void flush() throws IOException {
      this.delegate.flush();
   }

   public Timeout timeout() {
      return this.delegate.timeout();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append("(");
      var1.append(this.delegate.toString());
      var1.append(")");
      return var1.toString();
   }

   public void write(Buffer var1, long var2) throws IOException {
      this.delegate.write(var1, var2);
   }
}
