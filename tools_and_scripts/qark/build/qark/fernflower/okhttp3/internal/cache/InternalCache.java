package okhttp3.internal.cache;

import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;

public interface InternalCache {
   Response get(Request var1) throws IOException;

   CacheRequest put(Response var1) throws IOException;

   void remove(Request var1) throws IOException;

   void trackConditionalCacheHit();

   void trackResponse(CacheStrategy var1);

   void update(Response var1, Response var2);
}
