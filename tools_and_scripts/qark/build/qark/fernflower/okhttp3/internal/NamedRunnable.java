package okhttp3.internal;

public abstract class NamedRunnable implements Runnable {
   protected final String name;

   public NamedRunnable(String var1, Object... var2) {
      this.name = Util.format(var1, var2);
   }

   protected abstract void execute();

   public final void run() {
      String var1 = Thread.currentThread().getName();
      Thread.currentThread().setName(this.name);

      try {
         this.execute();
      } finally {
         Thread.currentThread().setName(var1);
      }

   }
}
