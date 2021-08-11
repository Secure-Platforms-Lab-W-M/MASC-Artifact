package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

public final class FormBody extends RequestBody {
   private static final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
   private final List encodedNames;
   private final List encodedValues;

   FormBody(List var1, List var2) {
      this.encodedNames = Util.immutableList(var1);
      this.encodedValues = Util.immutableList(var2);
   }

   private long writeOrCountBytes(@Nullable BufferedSink var1, boolean var2) {
      long var5 = 0L;
      Buffer var7;
      if (var2) {
         var7 = new Buffer();
      } else {
         var7 = var1.buffer();
      }

      int var3 = 0;

      for(int var4 = this.encodedNames.size(); var3 < var4; ++var3) {
         if (var3 > 0) {
            var7.writeByte(38);
         }

         var7.writeUtf8((String)this.encodedNames.get(var3));
         var7.writeByte(61);
         var7.writeUtf8((String)this.encodedValues.get(var3));
      }

      if (var2) {
         var5 = var7.size();
         var7.clear();
      }

      return var5;
   }

   public long contentLength() {
      return this.writeOrCountBytes((BufferedSink)null, true);
   }

   public MediaType contentType() {
      return CONTENT_TYPE;
   }

   public String encodedName(int var1) {
      return (String)this.encodedNames.get(var1);
   }

   public String encodedValue(int var1) {
      return (String)this.encodedValues.get(var1);
   }

   public String name(int var1) {
      return HttpUrl.percentDecode(this.encodedName(var1), true);
   }

   public int size() {
      return this.encodedNames.size();
   }

   public String value(int var1) {
      return HttpUrl.percentDecode(this.encodedValue(var1), true);
   }

   public void writeTo(BufferedSink var1) throws IOException {
      this.writeOrCountBytes(var1, false);
   }

   public static final class Builder {
      private final List names = new ArrayList();
      private final List values = new ArrayList();

      public FormBody.Builder add(String var1, String var2) {
         this.names.add(HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true));
         this.values.add(HttpUrl.canonicalize(var2, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true));
         return this;
      }

      public FormBody.Builder addEncoded(String var1, String var2) {
         this.names.add(HttpUrl.canonicalize(var1, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true));
         this.values.add(HttpUrl.canonicalize(var2, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true));
         return this;
      }

      public FormBody build() {
         return new FormBody(this.names, this.values);
      }
   }
}
