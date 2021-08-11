package okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;

public interface Interceptor {
   Response intercept(Interceptor.Chain var1) throws IOException;

   public interface Chain {
      @Nullable
      Connection connection();

      Response proceed(Request var1) throws IOException;

      Request request();
   }
}
