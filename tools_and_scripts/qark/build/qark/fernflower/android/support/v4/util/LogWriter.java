package android.support.v4.util;

import android.support.annotation.RestrictTo;
import android.util.Log;
import java.io.Writer;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class LogWriter extends Writer {
   private StringBuilder mBuilder = new StringBuilder(128);
   private final String mTag;

   public LogWriter(String var1) {
      this.mTag = var1;
   }

   private void flushBuilder() {
      if (this.mBuilder.length() > 0) {
         Log.d(this.mTag, this.mBuilder.toString());
         StringBuilder var1 = this.mBuilder;
         var1.delete(0, var1.length());
      }

   }

   public void close() {
      this.flushBuilder();
   }

   public void flush() {
      this.flushBuilder();
   }

   public void write(char[] var1, int var2, int var3) {
      for(int var5 = 0; var5 < var3; ++var5) {
         char var4 = var1[var2 + var5];
         if (var4 == '\n') {
            this.flushBuilder();
         } else {
            this.mBuilder.append(var4);
         }
      }

   }
}
