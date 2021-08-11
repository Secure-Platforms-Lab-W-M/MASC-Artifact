package javax.jmdns.impl.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {
   private final ThreadFactory _delegate;
   private final String _namePrefix;

   public NamedThreadFactory(String var1) {
      this._namePrefix = var1;
      this._delegate = Executors.defaultThreadFactory();
   }

   public Thread newThread(Runnable var1) {
      Thread var3 = this._delegate.newThread(var1);
      StringBuilder var2 = new StringBuilder();
      var2.append(this._namePrefix);
      var2.append(' ');
      var2.append(var3.getName());
      var3.setName(var2.toString());
      return var3;
   }
}
