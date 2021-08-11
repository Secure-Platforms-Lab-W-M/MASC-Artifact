package okhttp3.internal.http2;

import okhttp3.internal.Util;
import okio.ByteString;

public final class Header {
   public static final ByteString PSEUDO_PREFIX = ByteString.encodeUtf8(":");
   public static final ByteString RESPONSE_STATUS = ByteString.encodeUtf8(":status");
   public static final ByteString TARGET_AUTHORITY = ByteString.encodeUtf8(":authority");
   public static final ByteString TARGET_METHOD = ByteString.encodeUtf8(":method");
   public static final ByteString TARGET_PATH = ByteString.encodeUtf8(":path");
   public static final ByteString TARGET_SCHEME = ByteString.encodeUtf8(":scheme");
   final int hpackSize;
   public final ByteString name;
   public final ByteString value;

   public Header(String var1, String var2) {
      this(ByteString.encodeUtf8(var1), ByteString.encodeUtf8(var2));
   }

   public Header(ByteString var1, String var2) {
      this(var1, ByteString.encodeUtf8(var2));
   }

   public Header(ByteString var1, ByteString var2) {
      this.name = var1;
      this.value = var2;
      this.hpackSize = var1.size() + 32 + var2.size();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Header) {
         Header var2 = (Header)var1;
         return this.name.equals(var2.name) && this.value.equals(var2.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (17 * 31 + this.name.hashCode()) * 31 + this.value.hashCode();
   }

   public String toString() {
      return Util.format("%s: %s", this.name.utf8(), this.value.utf8());
   }
}
