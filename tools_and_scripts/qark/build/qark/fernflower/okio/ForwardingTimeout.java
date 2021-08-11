package okio;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ForwardingTimeout extends Timeout {
   private Timeout delegate;

   public ForwardingTimeout(Timeout var1) {
      if (var1 != null) {
         this.delegate = var1;
      } else {
         throw new IllegalArgumentException("delegate == null");
      }
   }

   public Timeout clearDeadline() {
      return this.delegate.clearDeadline();
   }

   public Timeout clearTimeout() {
      return this.delegate.clearTimeout();
   }

   public long deadlineNanoTime() {
      return this.delegate.deadlineNanoTime();
   }

   public Timeout deadlineNanoTime(long var1) {
      return this.delegate.deadlineNanoTime(var1);
   }

   public final Timeout delegate() {
      return this.delegate;
   }

   public boolean hasDeadline() {
      return this.delegate.hasDeadline();
   }

   public final ForwardingTimeout setDelegate(Timeout var1) {
      if (var1 != null) {
         this.delegate = var1;
         return this;
      } else {
         throw new IllegalArgumentException("delegate == null");
      }
   }

   public void throwIfReached() throws IOException {
      this.delegate.throwIfReached();
   }

   public Timeout timeout(long var1, TimeUnit var3) {
      return this.delegate.timeout(var1, var3);
   }

   public long timeoutNanos() {
      return this.delegate.timeoutNanos();
   }
}
