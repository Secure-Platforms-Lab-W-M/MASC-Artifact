package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class RouteException extends RuntimeException {
   private static final Method addSuppressedExceptionMethod;
   private IOException lastException;

   static {
      Method var0;
      try {
         var0 = Throwable.class.getDeclaredMethod("addSuppressed", Throwable.class);
      } catch (Exception var1) {
         var0 = null;
      }

      addSuppressedExceptionMethod = var0;
   }

   public RouteException(IOException var1) {
      super(var1);
      this.lastException = var1;
   }

   private void addSuppressedIfPossible(IOException var1, IOException var2) {
      Method var3 = addSuppressedExceptionMethod;
      if (var3 != null) {
         try {
            var3.invoke(var1, var2);
            return;
         } catch (InvocationTargetException var4) {
         } catch (IllegalAccessException var5) {
            return;
         }
      }

   }

   public void addConnectException(IOException var1) {
      this.addSuppressedIfPossible(var1, this.lastException);
      this.lastException = var1;
   }

   public IOException getLastConnectException() {
      return this.lastException;
   }
}
