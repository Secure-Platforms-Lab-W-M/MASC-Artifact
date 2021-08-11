package okhttp3.internal.http;

import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Sink;

public interface HttpCodec {
   int DISCARD_STREAM_TIMEOUT_MILLIS = 100;

   void cancel();

   Sink createRequestBody(Request var1, long var2);

   void finishRequest() throws IOException;

   void flushRequest() throws IOException;

   ResponseBody openResponseBody(Response var1) throws IOException;

   Response.Builder readResponseHeaders(boolean var1) throws IOException;

   void writeRequestHeaders(Request var1) throws IOException;
}
