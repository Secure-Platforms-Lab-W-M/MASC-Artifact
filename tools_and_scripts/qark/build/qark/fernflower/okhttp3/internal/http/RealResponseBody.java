package okhttp3.internal.http;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public final class RealResponseBody extends ResponseBody {
   private final Headers headers;
   private final BufferedSource source;

   public RealResponseBody(Headers var1, BufferedSource var2) {
      this.headers = var1;
      this.source = var2;
   }

   public long contentLength() {
      return HttpHeaders.contentLength(this.headers);
   }

   public MediaType contentType() {
      String var1 = this.headers.get("Content-Type");
      return var1 != null ? MediaType.parse(var1) : null;
   }

   public BufferedSource source() {
      return this.source;
   }
}
