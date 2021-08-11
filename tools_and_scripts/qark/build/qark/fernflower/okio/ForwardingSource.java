package okio;

import java.io.IOException;

public abstract class ForwardingSource implements Source {
   private final Source delegate;

   public ForwardingSource(Source var1) {
      if (var1 != null) {
         this.delegate = var1;
      } else {
         throw new IllegalArgumentException("delegate == null");
      }
   }

   public void close() throws IOException {
      this.delegate.close();
   }

   public final Source delegate() {
      return this.delegate;
   }

   public long read(Buffer var1, long var2) throws IOException {
      return this.delegate.read(var1, var2);
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
}
